package com.avaje.ebeaninternal.server.querydefn;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.FutureIds;
import com.avaje.ebean.FutureList;
import com.avaje.ebean.FutureRowCount;
import com.avaje.ebean.OrderBy;
import com.avaje.ebean.OrderBy.Property;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;
import com.avaje.ebean.Query.UseIndex;
import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.QueryListener;
import com.avaje.ebean.QueryResultVisitor;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.bean.BeanCollectionTouched;
import com.avaje.ebean.bean.CallStack;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.bean.ObjectGraphOrigin;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebean.meta.MetaAutoFetchStatistic;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionList;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Mode;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.DRawSqlSelect;
import com.avaje.ebeaninternal.server.deploy.DeployNamedQuery;
import com.avaje.ebeaninternal.server.deploy.TableJoin;
import com.avaje.ebeaninternal.server.deploy.id.IdBinder;
import com.avaje.ebeaninternal.server.expression.SimpleExpression;
import com.avaje.ebeaninternal.server.query.CancelableQuery;
import com.avaje.ebeaninternal.util.DefaultExpressionList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.PersistenceException;

public class DefaultOrmQuery<T>
  implements SpiQuery<T>
{
  private static final long serialVersionUID = 6838006264714672460L;
  private final Class<T> beanType;
  private final transient EbeanServer server;
  private transient BeanCollectionTouched beanCollectionTouched;
  private final transient ExpressionFactory expressionFactory;
  private transient ArrayList<EntityBean> contextAdditions;
  private transient QueryListener<T> queryListener;
  private transient TableJoin includeTableJoin;
  private transient AutoFetchManager autoFetchManager;
  private transient BeanDescriptor<?> beanDescriptor;
  private boolean cancelled;
  private transient CancelableQuery cancelableQuery;
  private String name;
  private Query.UseIndex useIndex;
  private SpiQuery.Type type;
  private SpiQuery.Mode mode = SpiQuery.Mode.NORMAL;
  private OrmQueryDetail detail;
  private int maxRows;
  private int firstRow;
  private int totalHits;
  private String rawWhereClause;
  private OrderBy<T> orderBy;
  private String loadMode;
  private String loadDescription;
  private String generatedSql;
  private String query;
  private String additionalWhere;
  private String additionalHaving;
  private String lazyLoadProperty;
  private String lazyLoadManyPath;
  private Boolean vanillaMode;
  private boolean distinct;
  private boolean futureFetch;
  private List<Object> partialIds;
  private int backgroundFetchAfter;
  private int timeout = -1;
  private String mapKey;
  private Object id;
  private BindParams bindParams;
  private DefaultExpressionList<T> whereExpressions;
  private DefaultExpressionList<T> havingExpressions;
  private int bufferFetchSizeHint;
  private boolean usageProfiling = true;
  private boolean loadBeanCache;
  private Boolean useBeanCache;
  private Boolean useQueryCache;
  private Boolean readOnly;
  private boolean sqlSelect;
  private Boolean autoFetch;
  private Boolean forUpdate;
  private boolean autoFetchTuned;
  private ObjectGraphNode parentNode;
  private int queryPlanHash;
  private transient PersistenceContext persistenceContext;
  private ManyWhereJoins manyWhereJoins;
  private RawSql rawSql;
  
  public DefaultOrmQuery(Class<T> beanType, EbeanServer server, ExpressionFactory expressionFactory, String query)
  {
    this.beanType = beanType;
    this.server = server;
    this.expressionFactory = expressionFactory;
    this.detail = new OrmQueryDetail();
    this.name = "";
    if (query != null) {
      setQuery(query);
    }
  }
  
  public DefaultOrmQuery(Class<T> beanType, EbeanServer server, ExpressionFactory expressionFactory, DeployNamedQuery namedQuery)
    throws PersistenceException
  {
    this.beanType = beanType;
    this.server = server;
    this.expressionFactory = expressionFactory;
    this.detail = new OrmQueryDetail();
    if (namedQuery == null)
    {
      this.name = "";
    }
    else
    {
      this.name = namedQuery.getName();
      this.sqlSelect = namedQuery.isSqlSelect();
      if (this.sqlSelect)
      {
        DRawSqlSelect sqlSelect = namedQuery.getSqlSelect();
        this.additionalWhere = sqlSelect.getWhereClause();
        this.additionalHaving = sqlSelect.getHavingClause();
      }
      else if (namedQuery.isRawSql())
      {
        this.rawSql = namedQuery.getRawSql();
      }
      else
      {
        setQuery(namedQuery.getQuery());
      }
    }
  }
  
  public int getTotalHits()
  {
    return this.totalHits;
  }
  
  public void setTotalHits(int totalHits)
  {
    this.totalHits = totalHits;
  }
  
  public void setBeanDescriptor(BeanDescriptor<?> beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
  }
  
  public boolean selectAllForLazyLoadProperty()
  {
    if ((this.lazyLoadProperty != null) && 
      (!this.detail.containsProperty(this.lazyLoadProperty)))
    {
      this.detail.select("*");
      return true;
    }
    return false;
  }
  
  public RawSql getRawSql()
  {
    return this.rawSql;
  }
  
  public DefaultOrmQuery<T> setRawSql(RawSql rawSql)
  {
    this.rawSql = rawSql;
    return this;
  }
  
  public String getLazyLoadProperty()
  {
    return this.lazyLoadProperty;
  }
  
  public void setLazyLoadProperty(String lazyLoadProperty)
  {
    this.lazyLoadProperty = lazyLoadProperty;
  }
  
  public String getLazyLoadManyPath()
  {
    return this.lazyLoadManyPath;
  }
  
  public ExpressionFactory getExpressionFactory()
  {
    return this.expressionFactory;
  }
  
  public MetaAutoFetchStatistic getMetaAutoFetchStatistic()
  {
    if ((this.parentNode != null) && (this.server != null))
    {
      ObjectGraphOrigin origin = this.parentNode.getOriginQueryPoint();
      return (MetaAutoFetchStatistic)this.server.find(MetaAutoFetchStatistic.class, origin.getKey());
    }
    return null;
  }
  
  public boolean initManyWhereJoins()
  {
    this.manyWhereJoins = new ManyWhereJoins();
    if (this.whereExpressions != null) {
      this.whereExpressions.containsMany(this.beanDescriptor, this.manyWhereJoins);
    }
    return !this.manyWhereJoins.isEmpty();
  }
  
  public ManyWhereJoins getManyWhereJoins()
  {
    return this.manyWhereJoins;
  }
  
  public List<OrmQueryProperties> removeQueryJoins()
  {
    List<OrmQueryProperties> queryJoins = this.detail.removeSecondaryQueries();
    if ((queryJoins != null) && 
      (this.orderBy != null)) {
      for (int i = 0; i < queryJoins.size(); i++)
      {
        OrmQueryProperties joinPath = (OrmQueryProperties)queryJoins.get(i);
        
        List<OrderBy.Property> properties = this.orderBy.getProperties();
        Iterator<OrderBy.Property> it = properties.iterator();
        while (it.hasNext())
        {
          OrderBy.Property property = (OrderBy.Property)it.next();
          if (property.getProperty().startsWith(joinPath.getPath()))
          {
            it.remove();
            joinPath.addSecJoinOrderProperty(property);
          }
        }
      }
    }
    return queryJoins;
  }
  
  public List<OrmQueryProperties> removeLazyJoins()
  {
    return this.detail.removeSecondaryLazyQueries();
  }
  
  public void setLazyLoadManyPath(String lazyLoadManyPath)
  {
    this.lazyLoadManyPath = lazyLoadManyPath;
  }
  
  public void convertManyFetchJoinsToQueryJoins(boolean allowOne, int queryBatch)
  {
    this.detail.convertManyFetchJoinsToQueryJoins(this.beanDescriptor, this.lazyLoadManyPath, allowOne, queryBatch);
  }
  
  public void setSelectId()
  {
    this.detail.clear();
    
    select(this.beanDescriptor.getIdBinder().getIdProperty());
  }
  
  public void convertWhereNaturalKeyToId(Object idValue)
  {
    this.whereExpressions = new DefaultExpressionList(this, null);
    setId(idValue);
  }
  
  public NaturalKeyBindParam getNaturalKeyBindParam()
  {
    NaturalKeyBindParam namedBind = null;
    if (this.bindParams != null)
    {
      namedBind = this.bindParams.getNaturalKeyBindParam();
      if (namedBind == null) {
        return null;
      }
    }
    if (this.whereExpressions != null)
    {
      List<SpiExpression> exprList = this.whereExpressions.internalList();
      if (exprList.size() > 1) {
        return null;
      }
      if (exprList.size() == 0) {
        return namedBind;
      }
      if (namedBind != null) {
        return null;
      }
      SpiExpression se = (SpiExpression)exprList.get(0);
      if ((se instanceof SimpleExpression))
      {
        SimpleExpression e = (SimpleExpression)se;
        if (e.isOpEquals()) {
          return new NaturalKeyBindParam(e.getPropertyName(), e.getValue());
        }
      }
    }
    return null;
  }
  
  public DefaultOrmQuery<T> copy()
  {
    DefaultOrmQuery<T> copy = new DefaultOrmQuery(this.beanType, this.server, this.expressionFactory, (String)null);
    copy.name = this.name;
    copy.includeTableJoin = this.includeTableJoin;
    copy.autoFetchManager = this.autoFetchManager;
    
    copy.query = this.query;
    copy.additionalWhere = this.additionalWhere;
    copy.additionalHaving = this.additionalHaving;
    copy.distinct = this.distinct;
    copy.backgroundFetchAfter = this.backgroundFetchAfter;
    copy.timeout = this.timeout;
    copy.mapKey = this.mapKey;
    copy.id = this.id;
    copy.vanillaMode = this.vanillaMode;
    copy.loadBeanCache = this.loadBeanCache;
    copy.useBeanCache = this.useBeanCache;
    copy.useQueryCache = this.useQueryCache;
    copy.readOnly = this.readOnly;
    copy.sqlSelect = this.sqlSelect;
    if (this.detail != null) {
      copy.detail = this.detail.copy();
    }
    copy.firstRow = this.firstRow;
    copy.maxRows = this.maxRows;
    copy.rawWhereClause = this.rawWhereClause;
    if (this.orderBy != null) {
      copy.orderBy = this.orderBy.copy();
    }
    if (this.bindParams != null) {
      copy.bindParams = this.bindParams.copy();
    }
    if (this.whereExpressions != null) {
      copy.whereExpressions = this.whereExpressions.copy(copy);
    }
    if (this.havingExpressions != null) {
      copy.havingExpressions = this.havingExpressions.copy(copy);
    }
    copy.usageProfiling = this.usageProfiling;
    copy.autoFetch = this.autoFetch;
    copy.parentNode = this.parentNode;
    copy.forUpdate = this.forUpdate;
    copy.rawSql = this.rawSql;
    copy.rawWhereClause = this.rawWhereClause;
    return copy;
  }
  
  public SpiQuery.Type getType()
  {
    return this.type;
  }
  
  public void setType(SpiQuery.Type type)
  {
    this.type = type;
  }
  
  public Query.UseIndex getUseIndex()
  {
    return this.useIndex;
  }
  
  public DefaultOrmQuery<T> setUseIndex(Query.UseIndex useIndex)
  {
    this.useIndex = useIndex;
    return this;
  }
  
  public String getLoadDescription()
  {
    return this.loadDescription;
  }
  
  public String getLoadMode()
  {
    return this.loadMode;
  }
  
  public void setLoadDescription(String loadMode, String loadDescription)
  {
    this.loadMode = loadMode;
    this.loadDescription = loadDescription;
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.persistenceContext;
  }
  
  public void setPersistenceContext(PersistenceContext persistenceContext)
  {
    this.persistenceContext = persistenceContext;
  }
  
  public boolean isDetailEmpty()
  {
    return this.detail.isEmpty();
  }
  
  public boolean isAutofetchTuned()
  {
    return this.autoFetchTuned;
  }
  
  public void setAutoFetchTuned(boolean autoFetchTuned)
  {
    this.autoFetchTuned = autoFetchTuned;
  }
  
  public Boolean isAutofetch()
  {
    return this.sqlSelect ? Boolean.FALSE : this.autoFetch;
  }
  
  public Boolean isForUpdate()
  {
    return this.forUpdate;
  }
  
  public DefaultOrmQuery<T> setAutoFetch(boolean autoFetch)
  {
    return setAutofetch(autoFetch);
  }
  
  public DefaultOrmQuery<T> setAutofetch(boolean autoFetch)
  {
    this.autoFetch = Boolean.valueOf(autoFetch);
    return this;
  }
  
  public DefaultOrmQuery<T> setForUpdate(boolean forUpdate)
  {
    this.forUpdate = Boolean.valueOf(forUpdate);
    return this;
  }
  
  public AutoFetchManager getAutoFetchManager()
  {
    return this.autoFetchManager;
  }
  
  public void setAutoFetchManager(AutoFetchManager autoFetchManager)
  {
    this.autoFetchManager = autoFetchManager;
  }
  
  public SpiQuery.Mode getMode()
  {
    return this.mode;
  }
  
  public void setMode(SpiQuery.Mode mode)
  {
    this.mode = mode;
  }
  
  public boolean isUsageProfiling()
  {
    return this.usageProfiling;
  }
  
  public void setUsageProfiling(boolean usageProfiling)
  {
    this.usageProfiling = usageProfiling;
  }
  
  public void setParentNode(ObjectGraphNode parentNode)
  {
    this.parentNode = parentNode;
  }
  
  public ObjectGraphNode getParentNode()
  {
    return this.parentNode;
  }
  
  public ObjectGraphNode setOrigin(CallStack callStack)
  {
    ObjectGraphOrigin o = new ObjectGraphOrigin(calculateOriginQueryHash(), callStack, this.beanType.getName());
    this.parentNode = new ObjectGraphNode(o, null);
    return this.parentNode;
  }
  
  private int calculateOriginQueryHash()
  {
    int hc = this.beanType.getName().hashCode();
    hc = hc * 31 + (this.type == null ? 0 : this.type.ordinal());
    return hc;
  }
  
  private int calculateHash(BeanQueryRequest<?> request)
  {
    int hc = this.beanType.getName().hashCode();
    
    hc = hc * 31 + (this.type == null ? 0 : this.type.ordinal());
    hc = hc * 31 + (this.useIndex == null ? 0 : this.useIndex.hashCode());
    
    hc = hc * 31 + (this.rawSql == null ? 0 : this.rawSql.queryHash());
    
    hc = hc * 31 + (this.autoFetchTuned ? 31 : 0);
    hc = hc * 31 + (this.distinct ? 31 : 0);
    hc = hc * 31 + (this.query == null ? 0 : this.query.hashCode());
    hc = hc * 31 + this.detail.queryPlanHash(request);
    
    hc = hc * 31 + (this.firstRow == 0 ? 0 : this.firstRow);
    hc = hc * 31 + (this.maxRows == 0 ? 0 : this.maxRows);
    hc = hc * 31 + (this.orderBy == null ? 0 : this.orderBy.hash());
    hc = hc * 31 + (this.rawWhereClause == null ? 0 : this.rawWhereClause.hashCode());
    
    hc = hc * 31 + (this.additionalWhere == null ? 0 : this.additionalWhere.hashCode());
    hc = hc * 31 + (this.additionalHaving == null ? 0 : this.additionalHaving.hashCode());
    hc = hc * 31 + (this.mapKey == null ? 0 : this.mapKey.hashCode());
    hc = hc * 31 + (this.id == null ? 0 : 1);
    if (this.bindParams != null) {
      hc = hc * 31 + this.bindParams.getQueryPlanHash();
    }
    if (request == null)
    {
      hc = hc * 31 + (this.whereExpressions == null ? 0 : this.whereExpressions.queryAutoFetchHash());
      hc = hc * 31 + (this.havingExpressions == null ? 0 : this.havingExpressions.queryAutoFetchHash());
    }
    else
    {
      hc = hc * 31 + (this.whereExpressions == null ? 0 : this.whereExpressions.queryPlanHash(request));
      hc = hc * 31 + (this.havingExpressions == null ? 0 : this.havingExpressions.queryPlanHash(request));
    }
    hc = hc * 31 + (this.forUpdate == null ? 0 : this.forUpdate.hashCode());
    
    return hc;
  }
  
  public int queryAutofetchHash()
  {
    return calculateHash(null);
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    this.queryPlanHash = calculateHash(request);
    return this.queryPlanHash;
  }
  
  public int queryBindHash()
  {
    int hc = this.id == null ? 0 : this.id.hashCode();
    hc = hc * 31 + (this.whereExpressions == null ? 0 : this.whereExpressions.queryBindHash());
    hc = hc * 31 + (this.havingExpressions == null ? 0 : this.havingExpressions.queryBindHash());
    hc = hc * 31 + (this.bindParams == null ? 0 : this.bindParams.queryBindHash());
    hc = hc * 31 + (this.contextAdditions == null ? 0 : this.contextAdditions.hashCode());
    
    return hc;
  }
  
  public int queryHash()
  {
    int hc = this.queryPlanHash;
    hc = hc * 31 + queryBindHash();
    return hc;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public boolean isSqlSelect()
  {
    return this.sqlSelect;
  }
  
  public boolean isRawSql()
  {
    return this.rawSql != null;
  }
  
  public String getAdditionalWhere()
  {
    return this.additionalWhere;
  }
  
  public int getTimeout()
  {
    return this.timeout;
  }
  
  public String getAdditionalHaving()
  {
    return this.additionalHaving;
  }
  
  public boolean hasMaxRowsOrFirstRow()
  {
    return (this.maxRows > 0) || (this.firstRow > 0);
  }
  
  public boolean isVanillaMode(boolean defaultVanillaMode)
  {
    if (this.vanillaMode != null) {
      return this.vanillaMode.booleanValue();
    }
    return defaultVanillaMode;
  }
  
  public DefaultOrmQuery<T> setVanillaMode(boolean vanillaMode)
  {
    this.vanillaMode = Boolean.valueOf(vanillaMode);
    return this;
  }
  
  public Boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public DefaultOrmQuery<T> setReadOnly(boolean readOnly)
  {
    this.readOnly = Boolean.valueOf(readOnly);
    return this;
  }
  
  public Boolean isUseBeanCache()
  {
    return this.useBeanCache;
  }
  
  public boolean isUseQueryCache()
  {
    return Boolean.TRUE.equals(this.useQueryCache);
  }
  
  public DefaultOrmQuery<T> setUseCache(boolean useBeanCache)
  {
    this.useBeanCache = Boolean.valueOf(useBeanCache);
    return this;
  }
  
  public DefaultOrmQuery<T> setUseQueryCache(boolean useQueryCache)
  {
    this.useQueryCache = Boolean.valueOf(useQueryCache);
    return this;
  }
  
  public boolean isLoadBeanCache()
  {
    return this.loadBeanCache;
  }
  
  public DefaultOrmQuery<T> setLoadBeanCache(boolean loadBeanCache)
  {
    this.loadBeanCache = loadBeanCache;
    return this;
  }
  
  public DefaultOrmQuery<T> setTimeout(int secs)
  {
    this.timeout = secs;
    return this;
  }
  
  public DefaultOrmQuery<T> setQuery(String queryString)
    throws PersistenceException
  {
    this.query = queryString;
    
    OrmQueryDetailParser parser = new OrmQueryDetailParser(queryString);
    parser.parse();
    parser.assign(this);
    
    return this;
  }
  
  protected void setOrmQueryDetail(OrmQueryDetail detail)
  {
    this.detail = detail;
  }
  
  protected void setRawWhereClause(String rawWhereClause)
  {
    this.rawWhereClause = rawWhereClause;
  }
  
  public DefaultOrmQuery<T> setProperties(String columns)
  {
    return select(columns);
  }
  
  public void setDefaultSelectClause()
  {
    this.detail.setDefaultSelectClause(this.beanDescriptor);
  }
  
  public DefaultOrmQuery<T> select(String columns)
  {
    this.detail.select(columns);
    return this;
  }
  
  public DefaultOrmQuery<T> fetch(String property)
  {
    return fetch(property, null, null);
  }
  
  public DefaultOrmQuery<T> fetch(String property, FetchConfig joinConfig)
  {
    return fetch(property, null, joinConfig);
  }
  
  public DefaultOrmQuery<T> fetch(String property, String columns)
  {
    return fetch(property, columns, null);
  }
  
  public DefaultOrmQuery<T> fetch(String property, String columns, FetchConfig config)
  {
    this.detail.addFetch(property, columns, config);
    return this;
  }
  
  public List<Object> findIds()
  {
    return this.server.findIds(this, null);
  }
  
  public int findRowCount()
  {
    return this.server.findRowCount(this, null);
  }
  
  public void findVisit(QueryResultVisitor<T> visitor)
  {
    this.server.findVisit(this, visitor, null);
  }
  
  public QueryIterator<T> findIterate()
  {
    return this.server.findIterate(this, null);
  }
  
  public List<T> findList()
  {
    return this.server.findList(this, null);
  }
  
  public Set<T> findSet()
  {
    return this.server.findSet(this, null);
  }
  
  public Map<?, T> findMap()
  {
    return this.server.findMap(this, null);
  }
  
  public <K> Map<K, T> findMap(String keyProperty, Class<K> keyType)
  {
    setMapKey(keyProperty);
    return findMap();
  }
  
  public T findUnique()
  {
    return (T)this.server.findUnique(this, null);
  }
  
  public FutureIds<T> findFutureIds()
  {
    return this.server.findFutureIds(this, null);
  }
  
  public FutureList<T> findFutureList()
  {
    return this.server.findFutureList(this, null);
  }
  
  public FutureRowCount<T> findFutureRowCount()
  {
    return this.server.findFutureRowCount(this, null);
  }
  
  public PagingList<T> findPagingList(int pageSize)
  {
    return this.server.findPagingList(this, null, pageSize);
  }
  
  public DefaultOrmQuery<T> setParameter(int position, Object value)
  {
    if (this.bindParams == null) {
      this.bindParams = new BindParams();
    }
    this.bindParams.setParameter(position, value);
    return this;
  }
  
  public DefaultOrmQuery<T> setParameter(String name, Object value)
  {
    if (this.bindParams == null) {
      this.bindParams = new BindParams();
    }
    this.bindParams.setParameter(name, value);
    return this;
  }
  
  public OrderBy<T> getOrderBy()
  {
    return this.orderBy;
  }
  
  public String getRawWhereClause()
  {
    return this.rawWhereClause;
  }
  
  public OrderBy<T> orderBy()
  {
    return order();
  }
  
  public OrderBy<T> order()
  {
    if (this.orderBy == null) {
      this.orderBy = new OrderBy(this, null);
    }
    return this.orderBy;
  }
  
  public DefaultOrmQuery<T> setOrderBy(String orderByClause)
  {
    return order(orderByClause);
  }
  
  public DefaultOrmQuery<T> orderBy(String orderByClause)
  {
    return order(orderByClause);
  }
  
  public DefaultOrmQuery<T> order(String orderByClause)
  {
    if ((orderByClause == null) || (orderByClause.trim().length() == 0)) {
      this.orderBy = null;
    } else {
      this.orderBy = new OrderBy(this, orderByClause);
    }
    return this;
  }
  
  public DefaultOrmQuery<T> setOrderBy(OrderBy<T> orderBy)
  {
    return setOrder(orderBy);
  }
  
  public DefaultOrmQuery<T> setOrder(OrderBy<T> orderBy)
  {
    this.orderBy = orderBy;
    if (orderBy != null) {
      orderBy.setQuery(this);
    }
    return this;
  }
  
  public boolean isDistinct()
  {
    return this.distinct;
  }
  
  public DefaultOrmQuery<T> setDistinct(boolean isDistinct)
  {
    this.distinct = isDistinct;
    return this;
  }
  
  public QueryListener<T> getListener()
  {
    return this.queryListener;
  }
  
  public DefaultOrmQuery<T> setListener(QueryListener<T> queryListener)
  {
    this.queryListener = queryListener;
    return this;
  }
  
  public Class<T> getBeanType()
  {
    return this.beanType;
  }
  
  public void setDetail(OrmQueryDetail detail)
  {
    this.detail = detail;
  }
  
  public boolean tuneFetchProperties(OrmQueryDetail tunedDetail)
  {
    return this.detail.tuneFetchProperties(tunedDetail);
  }
  
  public OrmQueryDetail getDetail()
  {
    return this.detail;
  }
  
  public final ArrayList<EntityBean> getContextAdditions()
  {
    return this.contextAdditions;
  }
  
  public void contextAdd(EntityBean bean)
  {
    if (this.contextAdditions == null) {
      this.contextAdditions = new ArrayList();
    }
    this.contextAdditions.add(bean);
  }
  
  public String toString()
  {
    return "Query [" + this.whereExpressions + "]";
  }
  
  public TableJoin getIncludeTableJoin()
  {
    return this.includeTableJoin;
  }
  
  public void setIncludeTableJoin(TableJoin includeTableJoin)
  {
    this.includeTableJoin = includeTableJoin;
  }
  
  public int getFirstRow()
  {
    return this.firstRow;
  }
  
  public DefaultOrmQuery<T> setFirstRow(int firstRow)
  {
    this.firstRow = firstRow;
    return this;
  }
  
  public int getMaxRows()
  {
    return this.maxRows;
  }
  
  public DefaultOrmQuery<T> setMaxRows(int maxRows)
  {
    this.maxRows = maxRows;
    return this;
  }
  
  public String getMapKey()
  {
    return this.mapKey;
  }
  
  public DefaultOrmQuery<T> setMapKey(String mapKey)
  {
    this.mapKey = mapKey;
    return this;
  }
  
  public int getBackgroundFetchAfter()
  {
    return this.backgroundFetchAfter;
  }
  
  public DefaultOrmQuery<T> setBackgroundFetchAfter(int backgroundFetchAfter)
  {
    this.backgroundFetchAfter = backgroundFetchAfter;
    return this;
  }
  
  public Object getId()
  {
    return this.id;
  }
  
  public DefaultOrmQuery<T> setId(Object id)
  {
    this.id = id;
    return this;
  }
  
  public BindParams getBindParams()
  {
    return this.bindParams;
  }
  
  public String getQuery()
  {
    return this.query;
  }
  
  public DefaultOrmQuery<T> addWhere(String addToWhereClause)
  {
    return where(addToWhereClause);
  }
  
  public DefaultOrmQuery<T> addWhere(Expression expression)
  {
    return where(expression);
  }
  
  public ExpressionList<T> addWhere()
  {
    return where();
  }
  
  public DefaultOrmQuery<T> where(String addToWhereClause)
  {
    if (this.additionalWhere == null) {
      this.additionalWhere = addToWhereClause;
    } else {
      this.additionalWhere = (this.additionalWhere + " " + addToWhereClause);
    }
    return this;
  }
  
  public DefaultOrmQuery<T> where(Expression expression)
  {
    if (this.whereExpressions == null) {
      this.whereExpressions = new DefaultExpressionList(this, null);
    }
    this.whereExpressions.add(expression);
    return this;
  }
  
  public ExpressionList<T> where()
  {
    if (this.whereExpressions == null) {
      this.whereExpressions = new DefaultExpressionList(this, null);
    }
    return this.whereExpressions;
  }
  
  public ExpressionList<T> filterMany(String prop)
  {
    OrmQueryProperties chunk = this.detail.getChunk(prop, true);
    return chunk.filterMany(this);
  }
  
  public void setFilterMany(String prop, ExpressionList<?> filterMany)
  {
    if (filterMany != null)
    {
      OrmQueryProperties chunk = this.detail.getChunk(prop, true);
      chunk.setFilterMany((SpiExpressionList)filterMany);
    }
  }
  
  public DefaultOrmQuery<T> addHaving(String addToHavingClause)
  {
    return having(addToHavingClause);
  }
  
  public DefaultOrmQuery<T> addHaving(Expression expression)
  {
    return having(expression);
  }
  
  public ExpressionList<T> addHaving()
  {
    return having();
  }
  
  public DefaultOrmQuery<T> having(String addToHavingClause)
  {
    if (this.additionalHaving == null) {
      this.additionalHaving = addToHavingClause;
    } else {
      this.additionalHaving = (this.additionalHaving + " " + addToHavingClause);
    }
    return this;
  }
  
  public DefaultOrmQuery<T> having(Expression expression)
  {
    if (this.havingExpressions == null) {
      this.havingExpressions = new DefaultExpressionList(this, null);
    }
    this.havingExpressions.add(expression);
    return this;
  }
  
  public ExpressionList<T> having()
  {
    if (this.havingExpressions == null) {
      this.havingExpressions = new DefaultExpressionList(this, null);
    }
    return this.havingExpressions;
  }
  
  public SpiExpressionList<T> getHavingExpressions()
  {
    return this.havingExpressions;
  }
  
  public SpiExpressionList<T> getWhereExpressions()
  {
    return this.whereExpressions;
  }
  
  public boolean createOwnTransaction()
  {
    if (this.futureFetch) {
      return false;
    }
    if ((this.backgroundFetchAfter > 0) || (this.queryListener != null)) {
      return true;
    }
    return false;
  }
  
  public String getGeneratedSql()
  {
    return this.generatedSql;
  }
  
  public void setGeneratedSql(String generatedSql)
  {
    this.generatedSql = generatedSql;
  }
  
  public Query<T> setBufferFetchSizeHint(int bufferFetchSizeHint)
  {
    this.bufferFetchSizeHint = bufferFetchSizeHint;
    return this;
  }
  
  public int getBufferFetchSizeHint()
  {
    return this.bufferFetchSizeHint;
  }
  
  public void setBeanCollectionTouched(BeanCollectionTouched notify)
  {
    this.beanCollectionTouched = notify;
  }
  
  public BeanCollectionTouched getBeanCollectionTouched()
  {
    return this.beanCollectionTouched;
  }
  
  public List<Object> getIdList()
  {
    return this.partialIds;
  }
  
  public void setIdList(List<Object> partialIds)
  {
    this.partialIds = partialIds;
  }
  
  public boolean isFutureFetch()
  {
    return this.futureFetch;
  }
  
  public void setFutureFetch(boolean backgroundFetch)
  {
    this.futureFetch = backgroundFetch;
  }
  
  public void setCancelableQuery(CancelableQuery cancelableQuery)
  {
    synchronized (this)
    {
      this.cancelableQuery = cancelableQuery;
    }
  }
  
  public void cancel()
  {
    synchronized (this)
    {
      this.cancelled = true;
      if (this.cancelableQuery != null) {
        this.cancelableQuery.cancel();
      }
    }
  }
  
  /* Error */
  public boolean isCancelled()
  {
    // Byte code:
    //   0: aload_0
    //   1: dup
    //   2: astore_1
    //   3: monitorenter
    //   4: aload_0
    //   5: getfield 909	com/avaje/ebeaninternal/server/querydefn/DefaultOrmQuery:cancelled	Z
    //   8: aload_1
    //   9: monitorexit
    //   10: ireturn
    //   11: astore_2
    //   12: aload_1
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Line number table:
    //   Java source line #1323	-> byte code offset #0
    //   Java source line #1324	-> byte code offset #4
    //   Java source line #1325	-> byte code offset #11
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	16	0	this	DefaultOrmQuery<T>
    //   2	11	1	Ljava/lang/Object;	Object
    //   11	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   4	10	11	finally
    //   11	14	11	finally
  }
}
