package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.QueryListener;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.NodeUsageCollector;
import com.avaje.ebean.bean.NodeUsageListener;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.api.LoadContext;
import com.avaje.ebeaninternal.api.SpiExpressionList;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Mode;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import com.avaje.ebeaninternal.server.core.Message;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.core.SpiOrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanCollectionHelp;
import com.avaje.ebeaninternal.server.deploy.BeanCollectionHelpFactory;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DbReadContext;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryDetail;
import com.avaje.ebeaninternal.server.querydefn.OrmQueryProperties;
import com.avaje.ebeaninternal.server.transaction.DefaultPersistenceContext;
import com.avaje.ebeaninternal.server.type.DataBind;
import com.avaje.ebeaninternal.server.type.DataReader;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class CQuery<T>
  implements DbReadContext, CancelableQuery
{
  private static final Logger logger = Logger.getLogger(CQuery.class.getName());
  private static final int GLOBAL_ROW_LIMIT = 1000000;
  private int rowCount;
  private int loadedBeanCount;
  private boolean noMoreRows;
  private Object loadedBeanId;
  boolean loadedBeanChanged;
  private Object loadedBean;
  private Object prevLoadedBean;
  private Object loadedManyBean;
  private Object prevDetailCollection;
  private Object currentDetailCollection;
  private final BeanCollection<T> collection;
  private final BeanCollectionHelp<T> help;
  private final OrmQueryRequest<T> request;
  private final BeanDescriptor<T> desc;
  private final SpiQuery<T> query;
  private final QueryListener<T> queryListener;
  private Map<String, String> currentPathMap;
  private String currentPrefix;
  private final boolean manyIncluded;
  private final CQueryPredicates predicates;
  private final SqlTree sqlTree;
  private final boolean rawSql;
  private final String sql;
  private final String logWhereSql;
  private final boolean rowNumberIncluded;
  private final SqlTreeNode rootNode;
  private final BeanPropertyAssocMany<?> manyProperty;
  private final ElPropertyValue manyPropertyEl;
  private final int backgroundFetchAfter;
  private final int maxRowsLimit;
  private boolean hasHitBackgroundFetchAfter;
  private final PersistenceContext persistenceContext;
  private DataReader dataReader;
  private PreparedStatement pstmt;
  private boolean cancelled;
  private String bindLog;
  private final CQueryPlan queryPlan;
  private long startNano;
  private final SpiQuery.Mode queryMode;
  private final boolean autoFetchProfiling;
  private final ObjectGraphNode autoFetchParentNode;
  private final AutoFetchManager autoFetchManager;
  private final WeakReference<NodeUsageListener> autoFetchManagerRef;
  private int executionTimeMicros;
  private final Boolean readOnly;
  private final SpiExpressionList<?> filterMany;
  private BeanCollectionAdd currentDetailAdd;
  
  public CQuery(OrmQueryRequest<T> request, CQueryPredicates predicates, CQueryPlan queryPlan)
  {
    this.request = request;
    this.queryPlan = queryPlan;
    this.query = request.getQuery();
    this.queryMode = this.query.getMode();
    
    this.readOnly = request.isReadOnly();
    
    this.autoFetchManager = this.query.getAutoFetchManager();
    this.autoFetchProfiling = (this.autoFetchManager != null);
    this.autoFetchParentNode = (this.autoFetchProfiling ? this.query.getParentNode() : null);
    this.autoFetchManagerRef = (this.autoFetchProfiling ? new WeakReference(this.autoFetchManager) : null);
    
    this.query.setGeneratedSql(queryPlan.getSql());
    
    this.sqlTree = queryPlan.getSqlTree();
    this.rootNode = this.sqlTree.getRootNode();
    
    this.manyProperty = this.sqlTree.getManyProperty();
    this.manyPropertyEl = this.sqlTree.getManyPropertyEl();
    this.manyIncluded = this.sqlTree.isManyIncluded();
    if (this.manyIncluded)
    {
      String manyPropertyName = this.sqlTree.getManyPropertyName();
      OrmQueryProperties chunk = this.query.getDetail().getChunk(manyPropertyName, false);
      this.filterMany = chunk.getFilterMany();
    }
    else
    {
      this.filterMany = null;
    }
    this.sql = queryPlan.getSql();
    this.rawSql = queryPlan.isRawSql();
    this.rowNumberIncluded = queryPlan.isRowNumberIncluded();
    this.logWhereSql = queryPlan.getLogWhereSql();
    this.desc = request.getBeanDescriptor();
    this.predicates = predicates;
    
    this.queryListener = this.query.getListener();
    if (this.queryListener == null) {
      this.persistenceContext = request.getPersistenceContext();
    } else {
      this.persistenceContext = new DefaultPersistenceContext();
    }
    this.maxRowsLimit = (this.query.getMaxRows() > 0 ? this.query.getMaxRows() : 1000000);
    this.backgroundFetchAfter = (this.query.getBackgroundFetchAfter() > 0 ? this.query.getBackgroundFetchAfter() : Integer.MAX_VALUE);
    
    this.help = createHelp(request);
    this.collection = ((BeanCollection)(this.help != null ? this.help.createEmpty(false) : null));
  }
  
  private BeanCollectionHelp<T> createHelp(OrmQueryRequest<T> request)
  {
    if (request.isFindById()) {
      return null;
    }
    SpiQuery.Type manyType = request.getQuery().getType();
    if (manyType == null) {
      return null;
    }
    return BeanCollectionHelpFactory.create(request);
  }
  
  public Boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public void propagateState(Object e)
  {
    if ((Boolean.TRUE.equals(this.readOnly)) && 
      ((e instanceof EntityBean))) {
      ((EntityBean)e)._ebean_getIntercept().setReadOnly(true);
    }
  }
  
  public DataReader getDataReader()
  {
    return this.dataReader;
  }
  
  public SpiQuery.Mode getQueryMode()
  {
    return this.queryMode;
  }
  
  public boolean isVanillaMode()
  {
    return this.request.isVanillaMode();
  }
  
  public CQueryPredicates getPredicates()
  {
    return this.predicates;
  }
  
  public LoadContext getGraphContext()
  {
    return this.request.getGraphContext();
  }
  
  public SpiOrmQueryRequest<?> getQueryRequest()
  {
    return this.request;
  }
  
  public void cancel()
  {
    synchronized (this)
    {
      this.cancelled = true;
      if (this.pstmt != null) {
        try
        {
          this.pstmt.cancel();
        }
        catch (SQLException e)
        {
          String msg = "Error cancelling query";
          throw new PersistenceException(msg, e);
        }
      }
    }
  }
  
  public boolean prepareBindExecuteQuery()
    throws SQLException
  {
    synchronized (this)
    {
      if ((this.cancelled) || (this.query.isCancelled()))
      {
        this.cancelled = true;
        return false;
      }
      this.startNano = System.nanoTime();
      
      SpiTransaction t = this.request.getTransaction();
      Connection conn = t.getInternalConnection();
      this.pstmt = conn.prepareStatement(this.sql);
      if (this.query.getTimeout() > 0) {
        this.pstmt.setQueryTimeout(this.query.getTimeout());
      }
      if (this.query.getBufferFetchSizeHint() > 0) {
        this.pstmt.setFetchSize(this.query.getBufferFetchSizeHint());
      }
      DataBind dataBind = new DataBind(this.pstmt);
      
      this.queryPlan.bindEncryptedProperties(dataBind);
      
      this.bindLog = this.predicates.bind(dataBind);
      
      ResultSet rset = this.pstmt.executeQuery();
      this.dataReader = this.queryPlan.createDataReader(rset);
      
      return true;
    }
  }
  
  public void close()
  {
    try
    {
      if (this.dataReader != null)
      {
        this.dataReader.close();
        this.dataReader = null;
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, null, e);
    }
    try
    {
      if (this.pstmt != null)
      {
        this.pstmt.close();
        this.pstmt = null;
      }
    }
    catch (SQLException e)
    {
      logger.log(Level.SEVERE, null, e);
    }
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.persistenceContext;
  }
  
  public void setLoadedBean(Object bean, Object id)
  {
    if ((id == null) || (!id.equals(this.loadedBeanId)))
    {
      if (this.manyIncluded)
      {
        if (this.rowCount > 1) {
          this.loadedBeanChanged = true;
        }
        this.prevLoadedBean = this.loadedBean;
        this.loadedBeanId = id;
      }
      this.loadedBean = bean;
    }
  }
  
  public void setLoadedManyBean(Object manyValue)
  {
    this.loadedManyBean = manyValue;
  }
  
  public T getLoadedBean()
  {
    if (this.manyIncluded) {
      if ((this.prevDetailCollection instanceof BeanCollection)) {
        ((BeanCollection)this.prevDetailCollection).setModifyListening(this.manyProperty.getModifyListenMode());
      } else if ((this.currentDetailCollection instanceof BeanCollection)) {
        ((BeanCollection)this.currentDetailCollection).setModifyListening(this.manyProperty.getModifyListenMode());
      }
    }
    if (this.prevLoadedBean != null) {
      return (T)this.prevLoadedBean;
    }
    return (T)this.loadedBean;
  }
  
  private boolean hasMoreRows()
    throws SQLException
  {
    synchronized (this)
    {
      if (this.cancelled) {
        return false;
      }
      return this.dataReader.next();
    }
  }
  
  private boolean readRow()
    throws SQLException
  {
    synchronized (this)
    {
      if (this.cancelled) {
        return false;
      }
      if (!this.dataReader.next()) {
        return false;
      }
      this.rowCount += 1;
      this.dataReader.resetColumnPosition();
      if (this.rowNumberIncluded) {
        this.dataReader.incrementPos(1);
      }
      this.rootNode.load(this, null);
      
      return true;
    }
  }
  
  public int getQueryExecutionTimeMicros()
  {
    return this.executionTimeMicros;
  }
  
  public boolean readBean()
    throws SQLException
  {
    boolean result = readBeanInternal(true);
    
    updateExecutionStatistics();
    
    return result;
  }
  
  private boolean readBeanInternal(boolean inForeground)
    throws SQLException
  {
    if (this.loadedBeanCount >= this.maxRowsLimit)
    {
      this.collection.setHasMoreRows(hasMoreRows());
      return false;
    }
    if ((inForeground) && (this.loadedBeanCount >= this.backgroundFetchAfter))
    {
      this.hasHitBackgroundFetchAfter = true;
      this.collection.setFinishedFetch(false);
      return false;
    }
    if (!this.manyIncluded) {
      return readRow();
    }
    if (this.noMoreRows) {
      return false;
    }
    if (this.rowCount == 0)
    {
      if (!readRow()) {
        return false;
      }
      createNewDetailCollection();
    }
    if (readIntoCurrentDetailCollection())
    {
      createNewDetailCollection();
      
      return true;
    }
    this.prevDetailCollection = null;
    this.prevLoadedBean = null;
    this.noMoreRows = true;
    return true;
  }
  
  private boolean readIntoCurrentDetailCollection()
    throws SQLException
  {
    while (readRow())
    {
      if (this.loadedBeanChanged)
      {
        this.loadedBeanChanged = false;
        return true;
      }
      addToCurrentDetailCollection();
    }
    return false;
  }
  
  private void createNewDetailCollection()
  {
    this.prevDetailCollection = this.currentDetailCollection;
    if (this.queryMode.equals(SpiQuery.Mode.LAZYLOAD_MANY))
    {
      this.currentDetailCollection = this.manyPropertyEl.elGetValue(this.loadedBean);
    }
    else
    {
      this.currentDetailCollection = this.manyProperty.createEmpty(this.request.isVanillaMode());
      this.manyPropertyEl.elSetValue(this.loadedBean, this.currentDetailCollection, false, false);
    }
    if ((this.filterMany != null) && (!this.request.isVanillaMode())) {
      ((BeanCollection)this.currentDetailCollection).setFilterMany(this.filterMany);
    }
    this.currentDetailAdd = this.manyProperty.getBeanCollectionAdd(this.currentDetailCollection, null);
    addToCurrentDetailCollection();
  }
  
  private void addToCurrentDetailCollection()
  {
    if (this.loadedManyBean != null) {
      this.currentDetailAdd.addBean(this.loadedManyBean);
    }
  }
  
  public BeanCollection<T> continueFetchingInBackground()
    throws SQLException
  {
    readTheRows(false);
    this.collection.setFinishedFetch(true);
    return this.collection;
  }
  
  public BeanCollection<T> readCollection()
    throws SQLException
  {
    readTheRows(true);
    
    updateExecutionStatistics();
    
    return this.collection;
  }
  
  protected void updateExecutionStatistics()
  {
    try
    {
      long exeNano = System.nanoTime() - this.startNano;
      this.executionTimeMicros = ((int)exeNano / 1000);
      if (this.autoFetchProfiling) {
        this.autoFetchManager.collectQueryInfo(this.autoFetchParentNode, this.loadedBeanCount, this.executionTimeMicros);
      }
      this.queryPlan.executionTime(this.loadedBeanCount, this.executionTimeMicros);
    }
    catch (Exception e)
    {
      logger.log(Level.SEVERE, null, e);
    }
  }
  
  public QueryIterator<T> readIterate(int bufferSize, OrmQueryRequest<T> request)
  {
    if (bufferSize > 0) {
      return new CQueryIteratorWithBuffer(this, request, bufferSize);
    }
    return new CQueryIteratorSimple(this, request);
  }
  
  private void readTheRows(boolean inForeground)
    throws SQLException
  {
    while (hasNextBean(inForeground)) {
      if (this.queryListener != null) {
        this.queryListener.process(getLoadedBean());
      } else {
        this.help.add(this.collection, getLoadedBean());
      }
    }
  }
  
  protected boolean hasNextBean(boolean inForeground)
    throws SQLException
  {
    if (!readBeanInternal(inForeground)) {
      return false;
    }
    this.loadedBeanCount += 1;
    return true;
  }
  
  public String getLoadedRowDetail()
  {
    if (!this.manyIncluded) {
      return String.valueOf(this.rowCount);
    }
    return this.loadedBeanCount + ":" + this.rowCount;
  }
  
  public void register(String path, EntityBeanIntercept ebi)
  {
    path = getPath(path);
    this.request.getGraphContext().register(path, ebi);
  }
  
  public void register(String path, BeanCollection<?> bc)
  {
    path = getPath(path);
    this.request.getGraphContext().register(path, bc);
  }
  
  public boolean useBackgroundToContinueFetch()
  {
    return this.hasHitBackgroundFetchAfter;
  }
  
  public String getName()
  {
    return this.query.getName();
  }
  
  public boolean isRawSql()
  {
    return this.rawSql;
  }
  
  public String getLogWhereSql()
  {
    return this.logWhereSql;
  }
  
  public BeanPropertyAssocMany<?> getManyProperty()
  {
    return this.manyProperty;
  }
  
  public String getSummary()
  {
    return this.sqlTree.getSummary();
  }
  
  public SqlTree getSqlTree()
  {
    return this.sqlTree;
  }
  
  public String getBindLog()
  {
    return this.bindLog;
  }
  
  public SpiTransaction getTransaction()
  {
    return this.request.getTransaction();
  }
  
  public String getBeanType()
  {
    return this.desc.getFullName();
  }
  
  public String getBeanName()
  {
    return this.desc.getName();
  }
  
  public String getGeneratedSql()
  {
    return this.sql;
  }
  
  public PersistenceException createPersistenceException(SQLException e)
  {
    return createPersistenceException(e, getTransaction(), this.bindLog, this.sql);
  }
  
  public static PersistenceException createPersistenceException(SQLException e, SpiTransaction t, String bindLog, String sql)
  {
    if (t.isLogSummary())
    {
      String errMsg = StringHelper.replaceStringMulti(e.getMessage(), new String[] { "\r", "\n" }, "\\n ");
      String msg = "ERROR executing query:   bindLog[" + bindLog + "] error[" + errMsg + "]";
      t.logInternal(msg);
    }
    t.getConnection();
    
    String m = Message.msg("fetch.sqlerror", e.getMessage(), bindLog, sql);
    return new PersistenceException(m, e);
  }
  
  public boolean isAutoFetchProfiling()
  {
    return (this.autoFetchProfiling) && (this.query.isUsageProfiling());
  }
  
  private String getPath(String propertyName)
  {
    if (this.currentPrefix == null) {
      return propertyName;
    }
    if (propertyName == null) {
      return this.currentPrefix;
    }
    String path = (String)this.currentPathMap.get(propertyName);
    if (path != null) {
      return path;
    }
    return this.currentPrefix + "." + propertyName;
  }
  
  public void profileBean(EntityBeanIntercept ebi, String prefix)
  {
    ObjectGraphNode node = this.request.getGraphContext().getObjectGraphNode(prefix);
    
    ebi.setNodeUsageCollector(new NodeUsageCollector(node, this.autoFetchManagerRef));
  }
  
  public void setCurrentPrefix(String currentPrefix, Map<String, String> currentPathMap)
  {
    this.currentPrefix = currentPrefix;
    this.currentPathMap = currentPathMap;
  }
}
