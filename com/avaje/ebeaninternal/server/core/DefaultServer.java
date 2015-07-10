package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.AdminAutofetch;
import com.avaje.ebean.AdminLogging;
import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebean.BeanState;
import com.avaje.ebean.CallableSql;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.Filter;
import com.avaje.ebean.FutureIds;
import com.avaje.ebean.FutureList;
import com.avaje.ebean.FutureRowCount;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;
import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.QueryResultVisitor;
import com.avaje.ebean.SqlFutureList;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.TxCallable;
import com.avaje.ebean.TxIsolation;
import com.avaje.ebean.TxRunnable;
import com.avaje.ebean.TxScope;
import com.avaje.ebean.TxType;
import com.avaje.ebean.Update;
import com.avaje.ebean.ValuePair;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.CallStack;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.config.EncryptKeyManager;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.ldap.LdapConfig;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebean.event.BeanQueryAdapter;
import com.avaje.ebean.text.csv.CsvReader;
import com.avaje.ebean.text.json.JsonContext;
import com.avaje.ebean.text.json.JsonElement;
import com.avaje.ebeaninternal.api.LoadBeanRequest;
import com.avaje.ebeaninternal.api.LoadManyRequest;
import com.avaje.ebeaninternal.api.ScopeTrans;
import com.avaje.ebeaninternal.api.SpiBackgroundExecutor;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Mode;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.api.SpiSqlQuery;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.api.TransactionEventTable;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import com.avaje.ebeaninternal.server.deploy.BeanManager;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.DNativeQuery;
import com.avaje.ebeaninternal.server.deploy.DeployNamedQuery;
import com.avaje.ebeaninternal.server.deploy.DeployNamedUpdate;
import com.avaje.ebeaninternal.server.deploy.InheritInfo;
import com.avaje.ebeaninternal.server.el.ElFilter;
import com.avaje.ebeaninternal.server.jmx.MAdminAutofetch;
import com.avaje.ebeaninternal.server.ldap.DefaultLdapOrmQuery;
import com.avaje.ebeaninternal.server.ldap.LdapOrmQueryEngine;
import com.avaje.ebeaninternal.server.ldap.LdapOrmQueryRequest;
import com.avaje.ebeaninternal.server.ldap.expression.LdapExpressionFactory;
import com.avaje.ebeaninternal.server.lib.ShutdownManager;
import com.avaje.ebeaninternal.server.loadcontext.DLoadContext;
import com.avaje.ebeaninternal.server.query.CQuery;
import com.avaje.ebeaninternal.server.query.CQueryEngine;
import com.avaje.ebeaninternal.server.query.CallableQueryIds;
import com.avaje.ebeaninternal.server.query.CallableQueryList;
import com.avaje.ebeaninternal.server.query.CallableQueryRowCount;
import com.avaje.ebeaninternal.server.query.CallableSqlQueryList;
import com.avaje.ebeaninternal.server.query.LimitOffsetPagingQuery;
import com.avaje.ebeaninternal.server.query.QueryFutureIds;
import com.avaje.ebeaninternal.server.query.QueryFutureList;
import com.avaje.ebeaninternal.server.query.QueryFutureRowCount;
import com.avaje.ebeaninternal.server.query.SqlQueryFutureList;
import com.avaje.ebeaninternal.server.querydefn.DefaultOrmQuery;
import com.avaje.ebeaninternal.server.querydefn.DefaultOrmUpdate;
import com.avaje.ebeaninternal.server.querydefn.DefaultRelationalQuery;
import com.avaje.ebeaninternal.server.querydefn.NaturalKeyBindParam;
import com.avaje.ebeaninternal.server.text.csv.TCsvReader;
import com.avaje.ebeaninternal.server.transaction.DefaultPersistenceContext;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import com.avaje.ebeaninternal.server.transaction.TransactionManager;
import com.avaje.ebeaninternal.server.transaction.TransactionScopeManager;
import com.avaje.ebeaninternal.util.ParamTypeHelper;
import com.avaje.ebeaninternal.util.ParamTypeHelper.TypeInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.persistence.PersistenceException;

public final class DefaultServer
  implements SpiEbeanServer
{
  private static final Logger logger = Logger.getLogger(DefaultServer.class.getName());
  private static final InvalidValue[] EMPTY_INVALID_VALUES = new InvalidValue[0];
  private final String serverName;
  private final DatabasePlatform databasePlatform;
  private final AdminLogging adminLogging;
  private final AdminAutofetch adminAutofetch;
  private final TransactionManager transactionManager;
  private final TransactionScopeManager transactionScopeManager;
  private final int maxCallStack;
  private final boolean rollbackOnChecked;
  private final boolean defaultDeleteMissingChildren;
  private final boolean defaultUpdateNullProperties;
  private final boolean vanillaMode;
  private final boolean vanillaRefMode;
  private final LdapOrmQueryEngine ldapQueryEngine;
  private final Persister persister;
  private final OrmQueryEngine queryEngine;
  private final RelationalQueryEngine relationalQueryEngine;
  private final ServerCacheManager serverCacheManager;
  private final BeanDescriptorManager beanDescriptorManager;
  private final DiffHelp diffHelp = new DiffHelp();
  private final AutoFetchManager autoFetchManager;
  private final CQueryEngine cqueryEngine;
  private final DdlGenerator ddlGenerator;
  private final ExpressionFactory ldapExpressionFactory = new LdapExpressionFactory();
  private final ExpressionFactory expressionFactory;
  private final SpiBackgroundExecutor backgroundExecutor;
  private final DefaultBeanLoader beanLoader;
  private final EncryptKeyManager encryptKeyManager;
  private final JsonContext jsonContext;
  private String mbeanName;
  private MBeanServer mbeanServer;
  private int lazyLoadBatchSize;
  private int queryBatchSize;
  private PstmtBatch pstmtBatch;
  private static final int IGNORE_LEADING_ELEMENTS = 5;
  
  public DefaultServer(InternalConfiguration config, ServerCacheManager cache)
  {
    this.vanillaMode = config.getServerConfig().isVanillaMode();
    this.vanillaRefMode = config.getServerConfig().isVanillaRefMode();
    
    this.serverCacheManager = cache;
    this.pstmtBatch = config.getPstmtBatch();
    this.databasePlatform = config.getDatabasePlatform();
    this.backgroundExecutor = config.getBackgroundExecutor();
    this.serverName = config.getServerConfig().getName();
    this.lazyLoadBatchSize = config.getServerConfig().getLazyLoadBatchSize();
    this.queryBatchSize = config.getServerConfig().getQueryBatchSize();
    this.cqueryEngine = config.getCQueryEngine();
    this.expressionFactory = config.getExpressionFactory();
    this.adminLogging = config.getLogControl();
    this.encryptKeyManager = config.getServerConfig().getEncryptKeyManager();
    
    this.beanDescriptorManager = config.getBeanDescriptorManager();
    this.beanDescriptorManager.setEbeanServer(this);
    
    this.maxCallStack = GlobalProperties.getInt("ebean.maxCallStack", 5);
    
    this.defaultUpdateNullProperties = "true".equalsIgnoreCase(config.getServerConfig().getProperty("defaultUpdateNullProperties", "false"));
    
    this.defaultDeleteMissingChildren = "true".equalsIgnoreCase(config.getServerConfig().getProperty("defaultDeleteMissingChildren", "true"));
    
    this.rollbackOnChecked = GlobalProperties.getBoolean("ebean.transaction.rollbackOnChecked", true);
    this.transactionManager = config.getTransactionManager();
    this.transactionScopeManager = config.getTransactionScopeManager();
    
    this.persister = config.createPersister(this);
    this.queryEngine = config.createOrmQueryEngine();
    this.relationalQueryEngine = config.createRelationalQueryEngine();
    
    this.autoFetchManager = config.createAutoFetchManager(this);
    this.adminAutofetch = new MAdminAutofetch(this.autoFetchManager);
    
    this.ddlGenerator = new DdlGenerator(this, config.getDatabasePlatform(), config.getServerConfig());
    this.beanLoader = new DefaultBeanLoader(this, config.getDebugLazyLoad());
    this.jsonContext = config.createJsonContext(this);
    
    LdapConfig ldapConfig = config.getServerConfig().getLdapConfig();
    if (ldapConfig == null) {
      this.ldapQueryEngine = null;
    } else {
      this.ldapQueryEngine = new LdapOrmQueryEngine(ldapConfig.isVanillaMode(), ldapConfig.getContextFactory());
    }
    ShutdownManager.register(new Shutdown(null));
  }
  
  public boolean isDefaultDeleteMissingChildren()
  {
    return this.defaultDeleteMissingChildren;
  }
  
  public boolean isDefaultUpdateNullProperties()
  {
    return this.defaultUpdateNullProperties;
  }
  
  public boolean isVanillaMode()
  {
    return this.vanillaMode;
  }
  
  public int getLazyLoadBatchSize()
  {
    return this.lazyLoadBatchSize;
  }
  
  public PstmtBatch getPstmtBatch()
  {
    return this.pstmtBatch;
  }
  
  public DatabasePlatform getDatabasePlatform()
  {
    return this.databasePlatform;
  }
  
  public BackgroundExecutor getBackgroundExecutor()
  {
    return this.backgroundExecutor;
  }
  
  public ExpressionFactory getExpressionFactory()
  {
    return this.expressionFactory;
  }
  
  public DdlGenerator getDdlGenerator()
  {
    return this.ddlGenerator;
  }
  
  public AdminLogging getAdminLogging()
  {
    return this.adminLogging;
  }
  
  public AdminAutofetch getAdminAutofetch()
  {
    return this.adminAutofetch;
  }
  
  public AutoFetchManager getAutoFetchManager()
  {
    return this.autoFetchManager;
  }
  
  public void initialise()
  {
    if (this.encryptKeyManager != null) {
      this.encryptKeyManager.initialise();
    }
    List<BeanDescriptor<?>> list = this.beanDescriptorManager.getBeanDescriptorList();
    for (int i = 0; i < list.size(); i++) {
      ((BeanDescriptor)list.get(i)).cacheInitialise();
    }
  }
  
  public void start() {}
  
  public void registerMBeans(MBeanServer mbeanServer, int uniqueServerId)
  {
    this.mbeanServer = mbeanServer;
    this.mbeanName = ("Ebean:server=" + this.serverName + uniqueServerId);
    ObjectName adminName;
    ObjectName autofethcName;
    try
    {
      adminName = new ObjectName(this.mbeanName + ",function=Logging");
      autofethcName = new ObjectName(this.mbeanName + ",key=AutoFetch");
    }
    catch (Exception e)
    {
      String msg = "Failed to register the JMX beans for Ebean server [" + this.serverName + "].";
      logger.log(Level.SEVERE, msg, e);
      return;
    }
    try
    {
      mbeanServer.registerMBean(this.adminLogging, adminName);
      mbeanServer.registerMBean(this.adminAutofetch, autofethcName);
    }
    catch (InstanceAlreadyExistsException e)
    {
      String msg = "JMX beans for Ebean server [" + this.serverName + "] already registered. Will try unregister/register" + e.getMessage();
      logger.log(Level.WARNING, msg);
      try
      {
        mbeanServer.unregisterMBean(adminName);
        mbeanServer.unregisterMBean(autofethcName);
        
        mbeanServer.registerMBean(this.adminLogging, adminName);
        mbeanServer.registerMBean(this.adminAutofetch, autofethcName);
      }
      catch (Exception ae)
      {
        String amsg = "Unable to unregister/register the JMX beans for Ebean server [" + this.serverName + "].";
        logger.log(Level.SEVERE, amsg, ae);
      }
    }
    catch (Exception e)
    {
      String msg = "Error registering MBean[" + this.mbeanName + "]";
      logger.log(Level.SEVERE, msg, e);
    }
  }
  
  private final class Shutdown
    implements Runnable
  {
    private Shutdown() {}
    
    public void run()
    {
      try
      {
        if (DefaultServer.this.mbeanServer != null)
        {
          DefaultServer.this.mbeanServer.unregisterMBean(new ObjectName(DefaultServer.this.mbeanName + ",function=Logging"));
          DefaultServer.this.mbeanServer.unregisterMBean(new ObjectName(DefaultServer.this.mbeanName + ",key=AutoFetch"));
        }
      }
      catch (Exception e)
      {
        String msg = "Error unregistering Ebean " + DefaultServer.this.mbeanName;
        DefaultServer.logger.log(Level.SEVERE, msg, e);
      }
      DefaultServer.this.transactionManager.shutdown();
      DefaultServer.this.autoFetchManager.shutdown();
      DefaultServer.this.backgroundExecutor.shutdown();
    }
  }
  
  public String getName()
  {
    return this.serverName;
  }
  
  public BeanState getBeanState(Object bean)
  {
    if ((bean instanceof EntityBean)) {
      return new DefaultBeanState((EntityBean)bean);
    }
    return null;
  }
  
  public void runCacheWarming()
  {
    List<BeanDescriptor<?>> descList = this.beanDescriptorManager.getBeanDescriptorList();
    for (int i = 0; i < descList.size(); i++) {
      ((BeanDescriptor)descList.get(i)).runCacheWarming();
    }
  }
  
  public void runCacheWarming(Class<?> beanType)
  {
    BeanDescriptor<?> desc = this.beanDescriptorManager.getBeanDescriptor(beanType);
    if (desc == null)
    {
      String msg = "Is " + beanType + " an entity? Could not find a BeanDescriptor";
      throw new PersistenceException(msg);
    }
    desc.runCacheWarming();
  }
  
  public <T> CQuery<T> compileQuery(Query<T> query, Transaction t)
  {
    SpiOrmQueryRequest<T> qr = createQueryRequest(SpiQuery.Type.SUBQUERY, query, t);
    OrmQueryRequest<T> orm = (OrmQueryRequest)qr;
    return this.cqueryEngine.buildQuery(orm);
  }
  
  public CQueryEngine getQueryEngine()
  {
    return this.cqueryEngine;
  }
  
  public ServerCacheManager getServerCacheManager()
  {
    return this.serverCacheManager;
  }
  
  public AutoFetchManager getProfileListener()
  {
    return this.autoFetchManager;
  }
  
  public RelationalQueryEngine getRelationalQueryEngine()
  {
    return this.relationalQueryEngine;
  }
  
  public void refreshMany(Object parentBean, String propertyName, Transaction t)
  {
    this.beanLoader.refreshMany(parentBean, propertyName, t);
  }
  
  public void refreshMany(Object parentBean, String propertyName)
  {
    this.beanLoader.refreshMany(parentBean, propertyName);
  }
  
  public void loadMany(LoadManyRequest loadRequest)
  {
    this.beanLoader.loadMany(loadRequest);
  }
  
  public void loadMany(BeanCollection<?> bc, boolean onlyIds)
  {
    this.beanLoader.loadMany(bc, null, onlyIds);
  }
  
  public void refresh(Object bean)
  {
    this.beanLoader.refresh(bean);
  }
  
  public void loadBean(LoadBeanRequest loadRequest)
  {
    this.beanLoader.loadBean(loadRequest);
  }
  
  public void loadBean(EntityBeanIntercept ebi)
  {
    this.beanLoader.loadBean(ebi);
  }
  
  public InvalidValue validate(Object bean)
  {
    if (bean == null) {
      return null;
    }
    BeanDescriptor<?> beanDescriptor = getBeanDescriptor(bean.getClass());
    return beanDescriptor.validate(true, bean);
  }
  
  public InvalidValue[] validate(Object bean, String propertyName, Object value)
  {
    if (bean == null) {
      return null;
    }
    BeanDescriptor<?> beanDescriptor = getBeanDescriptor(bean.getClass());
    BeanProperty prop = beanDescriptor.getBeanProperty(propertyName);
    if (prop == null)
    {
      String msg = "property " + propertyName + " was not found?";
      throw new PersistenceException(msg);
    }
    if (value == null) {
      value = prop.getValue(bean);
    }
    List<InvalidValue> errors = prop.validate(true, value);
    if (errors == null) {
      return EMPTY_INVALID_VALUES;
    }
    return InvalidValue.toArray(errors);
  }
  
  public Map<String, ValuePair> diff(Object a, Object b)
  {
    if (a == null) {
      return null;
    }
    BeanDescriptor<?> desc = getBeanDescriptor(a.getClass());
    return this.diffHelp.diff(a, b, desc);
  }
  
  public void externalModification(TransactionEventTable tableEvent)
  {
    SpiTransaction t = this.transactionScopeManager.get();
    if (t != null) {
      t.getEvent().add(tableEvent);
    } else {
      this.transactionManager.externalModification(tableEvent);
    }
  }
  
  public void externalModification(String tableName, boolean inserts, boolean updates, boolean deletes)
  {
    TransactionEventTable evt = new TransactionEventTable();
    evt.add(tableName, inserts, updates, deletes);
    
    externalModification(evt);
  }
  
  public void clearQueryStatistics()
  {
    for (BeanDescriptor<?> desc : getBeanDescriptors()) {
      desc.clearQueryStatistics();
    }
  }
  
  public <T> T createEntityBean(Class<T> type)
  {
    BeanDescriptor<T> desc = getBeanDescriptor(type);
    return desc.createEntityBean();
  }
  
  public ObjectInputStream createProxyObjectInputStream(InputStream is)
  {
    try
    {
      return new ProxyBeanObjectInputStream(is, this);
    }
    catch (IOException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public <T> T getReference(Class<T> type, Object id)
  {
    if (id == null) {
      throw new NullPointerException("The id is null");
    }
    BeanDescriptor desc = getBeanDescriptor(type);
    
    id = desc.convertId(id);
    
    Object ref = null;
    PersistenceContext ctx = null;
    
    SpiTransaction t = this.transactionScopeManager.get();
    if (t != null)
    {
      ctx = t.getPersistenceContext();
      ref = ctx.get(type, id);
    }
    if (ref == null)
    {
      InheritInfo inheritInfo = desc.getInheritInfo();
      if (inheritInfo != null)
      {
        BeanProperty[] idProps = desc.propertiesId();
        String idNames;
        switch (idProps.length)
        {
        case 0: 
          throw new PersistenceException("No ID properties for this type? " + desc);
        case 1: 
          idNames = idProps[0].getName();
          break;
        default: 
          idNames = Arrays.toString(idProps);
          idNames = idNames.substring(1, idNames.length() - 1);
        }
        Query<T> query = createQuery(type);
        query.select(idNames).setId(id);
        
        ref = query.findUnique();
      }
      else
      {
        ref = desc.createReference(this.vanillaRefMode, null, id, null);
      }
      if ((ctx != null) && ((ref instanceof EntityBean))) {
        ctx.put(id, ref);
      }
    }
    return (T)ref;
  }
  
  public Transaction createTransaction()
  {
    return this.transactionManager.createTransaction(true, -1);
  }
  
  public Transaction createTransaction(TxIsolation isolation)
  {
    return this.transactionManager.createTransaction(true, isolation.getLevel());
  }
  
  public void logComment(String msg)
  {
    Transaction t = this.transactionScopeManager.get();
    if (t != null) {
      t.log(msg);
    }
  }
  
  public <T> T execute(TxCallable<T> c)
  {
    return (T)execute(null, c);
  }
  
  public <T> T execute(TxScope scope, TxCallable<T> c)
  {
    ScopeTrans scopeTrans = createScopeTrans(scope);
    try
    {
      return (T)c.call();
    }
    catch (Error e)
    {
      throw scopeTrans.caughtError(e);
    }
    catch (RuntimeException e)
    {
      throw ((RuntimeException)scopeTrans.caughtThrowable(e));
    }
    finally
    {
      scopeTrans.onFinally();
    }
  }
  
  public void execute(TxRunnable r)
  {
    execute(null, r);
  }
  
  public void execute(TxScope scope, TxRunnable r)
  {
    ScopeTrans scopeTrans = createScopeTrans(scope);
    try
    {
      r.run();
    }
    catch (Error e)
    {
      throw scopeTrans.caughtError(e);
    }
    catch (RuntimeException e)
    {
      throw ((RuntimeException)scopeTrans.caughtThrowable(e));
    }
    finally
    {
      scopeTrans.onFinally();
    }
  }
  
  private boolean createNewTransaction(SpiTransaction t, TxScope scope)
  {
    TxType type = scope.getType();
    switch (type)
    {
    case REQUIRED: 
      return t == null;
    case REQUIRES_NEW: 
      return true;
    case MANDATORY: 
      if (t == null) {
        throw new PersistenceException("Transaction missing when MANDATORY");
      }
      return true;
    case NEVER: 
      if (t != null) {
        throw new PersistenceException("Transaction exists for Transactional NEVER");
      }
      return false;
    case SUPPORTS: 
      return false;
    case NOT_SUPPORTED: 
      throw new RuntimeException("NOT_SUPPORTED should already be handled?");
    }
    throw new RuntimeException("Should never get here?");
  }
  
  public ScopeTrans createScopeTrans(TxScope txScope)
  {
    if (txScope == null) {
      txScope = new TxScope();
    }
    SpiTransaction suspended = null;
    
    SpiTransaction t = this.transactionScopeManager.get();
    boolean newTransaction;
    if (txScope.getType().equals(TxType.NOT_SUPPORTED))
    {
      boolean newTransaction = false;
      suspended = t;
      t = null;
    }
    else
    {
      newTransaction = createNewTransaction(t, txScope);
      if (newTransaction)
      {
        suspended = t;
        
        int isoLevel = -1;
        TxIsolation isolation = txScope.getIsolation();
        if (isolation != null) {
          isoLevel = isolation.getLevel();
        }
        t = this.transactionManager.createTransaction(true, isoLevel);
      }
    }
    this.transactionScopeManager.replace(t);
    
    return new ScopeTrans(this.rollbackOnChecked, newTransaction, t, txScope, suspended, this.transactionScopeManager);
  }
  
  public SpiTransaction getCurrentServerTransaction()
  {
    return this.transactionScopeManager.get();
  }
  
  public Transaction beginTransaction()
  {
    SpiTransaction t = this.transactionManager.createTransaction(true, -1);
    this.transactionScopeManager.set(t);
    return t;
  }
  
  public Transaction beginTransaction(TxIsolation isolation)
  {
    SpiTransaction t = this.transactionManager.createTransaction(true, isolation.getLevel());
    this.transactionScopeManager.set(t);
    return t;
  }
  
  public Transaction currentTransaction()
  {
    return this.transactionScopeManager.get();
  }
  
  public void commitTransaction()
  {
    this.transactionScopeManager.commit();
  }
  
  public void rollbackTransaction()
  {
    this.transactionScopeManager.rollback();
  }
  
  public void endTransaction()
  {
    this.transactionScopeManager.end();
  }
  
  public Object nextId(Class<?> beanType)
  {
    BeanDescriptor<?> desc = getBeanDescriptor(beanType);
    return desc.nextId(null);
  }
  
  public <T> void sort(List<T> list, String sortByClause)
  {
    if (list == null) {
      throw new NullPointerException("list is null");
    }
    if (sortByClause == null) {
      throw new NullPointerException("sortByClause is null");
    }
    if (list.size() == 0) {
      return;
    }
    Class<T> beanType = list.get(0).getClass();
    BeanDescriptor<T> beanDescriptor = getBeanDescriptor(beanType);
    if (beanDescriptor == null)
    {
      String m = "BeanDescriptor not found, is [" + beanType + "] an entity bean?";
      throw new PersistenceException(m);
    }
    beanDescriptor.sort(list, sortByClause);
  }
  
  public <T> Query<T> createQuery(Class<T> beanType)
    throws PersistenceException
  {
    return createQuery(beanType, null);
  }
  
  public <T> Query<T> createNamedQuery(Class<T> beanType, String namedQuery)
    throws PersistenceException
  {
    BeanDescriptor<?> desc = getBeanDescriptor(beanType);
    if (desc == null) {
      throw new PersistenceException("Is " + beanType.getName() + " an Entity Bean? BeanDescriptor not found?");
    }
    DeployNamedQuery deployQuery = desc.getNamedQuery(namedQuery);
    if (deployQuery == null) {
      throw new PersistenceException("named query " + namedQuery + " was not found for " + desc.getFullName());
    }
    return new DefaultOrmQuery(beanType, this, this.expressionFactory, deployQuery);
  }
  
  public <T> Filter<T> filter(Class<T> beanType)
  {
    BeanDescriptor<T> desc = getBeanDescriptor(beanType);
    if (desc == null)
    {
      String m = beanType.getName() + " is NOT an Entity Bean registered with this server?";
      throw new PersistenceException(m);
    }
    return new ElFilter(desc);
  }
  
  public <T> CsvReader<T> createCsvReader(Class<T> beanType)
  {
    BeanDescriptor<T> descriptor = getBeanDescriptor(beanType);
    if (descriptor == null) {
      throw new NullPointerException("BeanDescriptor for " + beanType.getName() + " not found");
    }
    return new TCsvReader(this, descriptor);
  }
  
  public <T> Query<T> find(Class<T> beanType)
  {
    return createQuery(beanType);
  }
  
  public <T> Query<T> createQuery(Class<T> beanType, String query)
  {
    BeanDescriptor<?> desc = getBeanDescriptor(beanType);
    if (desc == null)
    {
      String m = beanType.getName() + " is NOT an Entity Bean registered with this server?";
      throw new PersistenceException(m);
    }
    switch (desc.getEntityType())
    {
    case SQL: 
      if (query != null) {
        throw new PersistenceException("You must used Named queries for this Entity " + desc.getFullName());
      }
      DeployNamedQuery defaultSqlSelect = desc.getNamedQuery("default");
      return new DefaultOrmQuery(beanType, this, this.expressionFactory, defaultSqlSelect);
    case LDAP: 
      return new DefaultLdapOrmQuery(beanType, this, this.ldapExpressionFactory, query);
    }
    return new DefaultOrmQuery(beanType, this, this.expressionFactory, query);
  }
  
  public <T> Update<T> createNamedUpdate(Class<T> beanType, String namedUpdate)
  {
    BeanDescriptor<?> desc = getBeanDescriptor(beanType);
    if (desc == null)
    {
      String m = beanType.getName() + " is NOT an Entity Bean registered with this server?";
      throw new PersistenceException(m);
    }
    DeployNamedUpdate deployUpdate = desc.getNamedUpdate(namedUpdate);
    if (deployUpdate == null) {
      throw new PersistenceException("named update " + namedUpdate + " was not found for " + desc.getFullName());
    }
    return new DefaultOrmUpdate(beanType, this, desc.getBaseTable(), deployUpdate);
  }
  
  public <T> Update<T> createUpdate(Class<T> beanType, String ormUpdate)
  {
    BeanDescriptor<?> desc = getBeanDescriptor(beanType);
    if (desc == null)
    {
      String m = beanType.getName() + " is NOT an Entity Bean registered with this server?";
      throw new PersistenceException(m);
    }
    return new DefaultOrmUpdate(beanType, this, desc.getBaseTable(), ormUpdate);
  }
  
  public SqlQuery createSqlQuery(String sql)
  {
    return new DefaultRelationalQuery(this, sql);
  }
  
  public SqlQuery createNamedSqlQuery(String namedQuery)
  {
    DNativeQuery nq = this.beanDescriptorManager.getNativeQuery(namedQuery);
    if (nq == null) {
      throw new PersistenceException("SqlQuery " + namedQuery + " not found.");
    }
    return new DefaultRelationalQuery(this, nq.getQuery());
  }
  
  public SqlUpdate createSqlUpdate(String sql)
  {
    return new DefaultSqlUpdate(this, sql);
  }
  
  public CallableSql createCallableSql(String sql)
  {
    return new DefaultCallableSql(this, sql);
  }
  
  public SqlUpdate createNamedSqlUpdate(String namedQuery)
  {
    DNativeQuery nq = this.beanDescriptorManager.getNativeQuery(namedQuery);
    if (nq == null) {
      throw new PersistenceException("SqlUpdate " + namedQuery + " not found.");
    }
    return new DefaultSqlUpdate(this, nq.getQuery());
  }
  
  public <T> T find(Class<T> beanType, Object uid)
  {
    return (T)find(beanType, uid, null);
  }
  
  public <T> T find(Class<T> beanType, Object id, Transaction t)
  {
    if (id == null) {
      throw new NullPointerException("The id is null");
    }
    Query<T> query = createQuery(beanType).setId(id);
    return (T)findId(query, t);
  }
  
  private <T> SpiOrmQueryRequest<T> createQueryRequest(SpiQuery.Type type, Query<T> query, Transaction t)
  {
    SpiQuery<T> spiQuery = (SpiQuery)query;
    spiQuery.setType(type);
    
    BeanDescriptor<T> desc = this.beanDescriptorManager.getBeanDescriptor(spiQuery.getBeanType());
    spiQuery.setBeanDescriptor(desc);
    
    return createQueryRequest(desc, spiQuery, t);
  }
  
  public <T> SpiOrmQueryRequest<T> createQueryRequest(BeanDescriptor<T> desc, SpiQuery<T> query, Transaction t)
  {
    if (desc.isLdapEntityType()) {
      return new LdapOrmQueryRequest(query, desc, this.ldapQueryEngine);
    }
    if ((desc.isAutoFetchTunable()) && (!query.isSqlSelect())) {
      if (!this.autoFetchManager.tuneQuery(query)) {
        query.setDefaultSelectClause();
      }
    }
    if (query.selectAllForLazyLoadProperty()) {
      if (logger.isLoggable(Level.FINE)) {
        logger.log(Level.FINE, "Using selectAllForLazyLoadProperty");
      }
    }
    if (query.getParentNode() == null)
    {
      CallStack callStack = createCallStack();
      query.setOrigin(callStack);
    }
    if (query.initManyWhereJoins()) {
      query.setDistinct(true);
    }
    boolean allowOneManyFetch = true;
    if (SpiQuery.Mode.LAZYLOAD_MANY.equals(query.getMode())) {
      allowOneManyFetch = false;
    } else if ((query.hasMaxRowsOrFirstRow()) && (!query.isRawSql()) && (!query.isSqlSelect()) && (query.getBackgroundFetchAfter() == 0)) {
      allowOneManyFetch = false;
    }
    query.convertManyFetchJoinsToQueryJoins(allowOneManyFetch, this.queryBatchSize);
    
    SpiTransaction serverTrans = (SpiTransaction)t;
    OrmQueryRequest<T> request = new OrmQueryRequest(this, this.queryEngine, query, desc, serverTrans);
    
    BeanQueryAdapter queryAdapter = desc.getQueryAdapter();
    if (queryAdapter != null) {
      queryAdapter.preQuery(request);
    }
    request.calculateQueryPlanHash();
    
    return request;
  }
  
  private <T> T findIdCheckPersistenceContextAndCache(Transaction transaction, BeanDescriptor<T> beanDescriptor, SpiQuery<T> query)
  {
    SpiTransaction t = (SpiTransaction)transaction;
    if (t == null) {
      t = getCurrentServerTransaction();
    }
    PersistenceContext context = null;
    if (t != null)
    {
      context = t.getPersistenceContext();
      if (context != null)
      {
        Object o = context.get(beanDescriptor.getBeanType(), query.getId());
        if (o != null) {
          return (T)o;
        }
      }
    }
    if (!beanDescriptor.calculateUseCache(query.isUseBeanCache())) {
      return null;
    }
    boolean vanilla = query.isVanillaMode(this.vanillaMode);
    Object cachedBean = beanDescriptor.cacheGetBean(query.getId(), vanilla, query.isReadOnly());
    if (cachedBean != null)
    {
      if (context == null) {
        context = new DefaultPersistenceContext();
      }
      context.put(query.getId(), cachedBean);
      if (!vanilla)
      {
        DLoadContext loadContext = new DLoadContext(this, beanDescriptor, query.isReadOnly(), false, null, false);
        loadContext.setPersistenceContext(context);
        
        EntityBeanIntercept ebi = ((EntityBean)cachedBean)._ebean_getIntercept();
        ebi.setPersistenceContext(context);
        loadContext.register(null, ebi);
      }
    }
    return (T)cachedBean;
  }
  
  private <T> T findId(Query<T> query, Transaction t)
  {
    SpiQuery<T> spiQuery = (SpiQuery)query;
    spiQuery.setType(SpiQuery.Type.BEAN);
    
    BeanDescriptor<T> desc = this.beanDescriptorManager.getBeanDescriptor(spiQuery.getBeanType());
    spiQuery.setBeanDescriptor(desc);
    if ((SpiQuery.Mode.NORMAL.equals(spiQuery.getMode())) && (!spiQuery.isLoadBeanCache()))
    {
      T bean = findIdCheckPersistenceContextAndCache(t, desc, spiQuery);
      if (bean != null) {
        return bean;
      }
    }
    SpiOrmQueryRequest<T> request = createQueryRequest(desc, spiQuery, t);
    try
    {
      request.initTransIfRequired();
      
      T bean = request.findId();
      request.endTransIfRequired();
      
      return bean;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public <T> T findUnique(Query<T> query, Transaction t)
  {
    SpiQuery<T> q = (SpiQuery)query;
    Object id = q.getId();
    if (id != null) {
      return (T)findId(query, t);
    }
    BeanDescriptor<T> desc = this.beanDescriptorManager.getBeanDescriptor(q.getBeanType());
    if (desc.calculateUseNaturalKeyCache(q.isUseBeanCache()))
    {
      NaturalKeyBindParam keyBindParam = q.getNaturalKeyBindParam();
      if ((keyBindParam != null) && (desc.cacheIsNaturalKey(keyBindParam.getName())))
      {
        Object id2 = desc.cacheGetNaturalKeyId(keyBindParam.getValue());
        if (id2 != null)
        {
          SpiQuery<T> copy = q.copy();
          copy.convertWhereNaturalKeyToId(id2);
          return (T)findId(copy, t);
        }
      }
    }
    List<T> list = findList(query, t);
    if (list.size() == 0) {
      return null;
    }
    if (list.size() > 1)
    {
      String m = "Unique expecting 0 or 1 rows but got [" + list.size() + "]";
      throw new PersistenceException(m);
    }
    return (T)list.get(0);
  }
  
  public <T> Set<T> findSet(Query<T> query, Transaction t)
  {
    SpiOrmQueryRequest request = createQueryRequest(SpiQuery.Type.SET, query, t);
    
    Object result = request.getFromQueryCache();
    if (result != null) {
      return (Set)result;
    }
    try
    {
      request.initTransIfRequired();
      Set<T> set = request.findSet();
      request.endTransIfRequired();
      
      return set;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public <T> Map<?, T> findMap(Query<T> query, Transaction t)
  {
    SpiOrmQueryRequest request = createQueryRequest(SpiQuery.Type.MAP, query, t);
    
    Object result = request.getFromQueryCache();
    if (result != null) {
      return (Map)result;
    }
    try
    {
      request.initTransIfRequired();
      Map<?, T> map = request.findMap();
      request.endTransIfRequired();
      
      return map;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public <T> int findRowCount(Query<T> query, Transaction t)
  {
    SpiQuery<T> copy = ((SpiQuery)query).copy();
    return findRowCountWithCopy(copy, t);
  }
  
  public <T> int findRowCountWithCopy(Query<T> query, Transaction t)
  {
    SpiOrmQueryRequest<T> request = createQueryRequest(SpiQuery.Type.ROWCOUNT, query, t);
    try
    {
      request.initTransIfRequired();
      int rowCount = request.findRowCount();
      request.endTransIfRequired();
      
      return rowCount;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public <T> List<Object> findIds(Query<T> query, Transaction t)
  {
    SpiQuery<T> copy = ((SpiQuery)query).copy();
    
    return findIdsWithCopy(copy, t);
  }
  
  public <T> List<Object> findIdsWithCopy(Query<T> query, Transaction t)
  {
    SpiOrmQueryRequest<T> request = createQueryRequest(SpiQuery.Type.ID_LIST, query, t);
    try
    {
      request.initTransIfRequired();
      List<Object> list = request.findIds();
      request.endTransIfRequired();
      
      return list;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public <T> FutureRowCount<T> findFutureRowCount(Query<T> q, Transaction t)
  {
    SpiQuery<T> copy = ((SpiQuery)q).copy();
    copy.setFutureFetch(true);
    
    Transaction newTxn = createTransaction();
    
    CallableQueryRowCount<T> call = new CallableQueryRowCount(this, copy, newTxn);
    FutureTask<Integer> futureTask = new FutureTask(call);
    
    QueryFutureRowCount<T> queryFuture = new QueryFutureRowCount(copy, futureTask);
    this.backgroundExecutor.execute(futureTask);
    
    return queryFuture;
  }
  
  public <T> FutureIds<T> findFutureIds(Query<T> query, Transaction t)
  {
    SpiQuery<T> copy = ((SpiQuery)query).copy();
    copy.setFutureFetch(true);
    
    List<Object> idList = Collections.synchronizedList(new ArrayList());
    copy.setIdList(idList);
    
    Transaction newTxn = createTransaction();
    
    CallableQueryIds<T> call = new CallableQueryIds(this, copy, newTxn);
    FutureTask<List<Object>> futureTask = new FutureTask(call);
    
    QueryFutureIds<T> queryFuture = new QueryFutureIds(copy, futureTask);
    
    this.backgroundExecutor.execute(futureTask);
    
    return queryFuture;
  }
  
  public <T> FutureList<T> findFutureList(Query<T> query, Transaction t)
  {
    SpiQuery<T> spiQuery = (SpiQuery)query;
    spiQuery.setFutureFetch(true);
    if (spiQuery.getPersistenceContext() == null) {
      if (t != null)
      {
        spiQuery.setPersistenceContext(((SpiTransaction)t).getPersistenceContext());
      }
      else
      {
        SpiTransaction st = getCurrentServerTransaction();
        if (st != null) {
          spiQuery.setPersistenceContext(st.getPersistenceContext());
        }
      }
    }
    Transaction newTxn = createTransaction();
    CallableQueryList<T> call = new CallableQueryList(this, query, newTxn);
    
    FutureTask<List<T>> futureTask = new FutureTask(call);
    
    this.backgroundExecutor.execute(futureTask);
    
    return new QueryFutureList(query, futureTask);
  }
  
  public <T> PagingList<T> findPagingList(Query<T> query, Transaction t, int pageSize)
  {
    SpiQuery<T> spiQuery = (SpiQuery)query;
    
    PersistenceContext pc = spiQuery.getPersistenceContext();
    if (pc == null)
    {
      SpiTransaction currentTransaction = getCurrentServerTransaction();
      if (currentTransaction != null) {
        pc = currentTransaction.getPersistenceContext();
      }
      if (pc == null) {
        pc = new DefaultPersistenceContext();
      }
      spiQuery.setPersistenceContext(pc);
    }
    return new LimitOffsetPagingQuery(this, spiQuery, pageSize);
  }
  
  public <T> void findVisit(Query<T> query, QueryResultVisitor<T> visitor, Transaction t)
  {
    SpiOrmQueryRequest<T> request = createQueryRequest(SpiQuery.Type.LIST, query, t);
    try
    {
      request.initTransIfRequired();
      request.findVisit(visitor);
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public <T> QueryIterator<T> findIterate(Query<T> query, Transaction t)
  {
    SpiOrmQueryRequest<T> request = createQueryRequest(SpiQuery.Type.LIST, query, t);
    try
    {
      request.initTransIfRequired();
      return request.findIterate();
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public <T> List<T> findList(Query<T> query, Transaction t)
  {
    SpiOrmQueryRequest<T> request = createQueryRequest(SpiQuery.Type.LIST, query, t);
    
    Object result = request.getFromQueryCache();
    if (result != null) {
      return (List)result;
    }
    try
    {
      request.initTransIfRequired();
      List<T> list = request.findList();
      request.endTransIfRequired();
      
      return list;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public SqlRow findUnique(SqlQuery query, Transaction t)
  {
    List<SqlRow> list = findList(query, t);
    if (list.size() == 0) {
      return null;
    }
    if (list.size() > 1)
    {
      String m = "Unique expecting 0 or 1 rows but got [" + list.size() + "]";
      throw new PersistenceException(m);
    }
    return (SqlRow)list.get(0);
  }
  
  public SqlFutureList findFutureList(SqlQuery query, Transaction t)
  {
    SpiSqlQuery spiQuery = (SpiSqlQuery)query;
    spiQuery.setFutureFetch(true);
    
    Transaction newTxn = createTransaction();
    CallableSqlQueryList call = new CallableSqlQueryList(this, query, newTxn);
    
    FutureTask<List<SqlRow>> futureTask = new FutureTask(call);
    
    this.backgroundExecutor.execute(futureTask);
    
    return new SqlQueryFutureList(query, futureTask);
  }
  
  public List<SqlRow> findList(SqlQuery query, Transaction t)
  {
    RelationalQueryRequest request = new RelationalQueryRequest(this, this.relationalQueryEngine, query, t);
    try
    {
      request.initTransIfRequired();
      List<SqlRow> list = request.findList();
      request.endTransIfRequired();
      
      return list;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public Set<SqlRow> findSet(SqlQuery query, Transaction t)
  {
    RelationalQueryRequest request = new RelationalQueryRequest(this, this.relationalQueryEngine, query, t);
    try
    {
      request.initTransIfRequired();
      Set<SqlRow> set = request.findSet();
      request.endTransIfRequired();
      
      return set;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public Map<?, SqlRow> findMap(SqlQuery query, Transaction t)
  {
    RelationalQueryRequest request = new RelationalQueryRequest(this, this.relationalQueryEngine, query, t);
    try
    {
      request.initTransIfRequired();
      Map<?, SqlRow> map = request.findMap();
      request.endTransIfRequired();
      
      return map;
    }
    catch (RuntimeException ex)
    {
      request.rollbackTransIfRequired();
      throw ex;
    }
  }
  
  public void save(Object bean)
  {
    save(bean, null);
  }
  
  public void save(Object bean, Transaction t)
  {
    if (bean == null) {
      throw new NullPointerException(Message.msg("bean.isnull"));
    }
    this.persister.save(bean, t);
  }
  
  public void update(Object bean)
  {
    update(bean, null, null);
  }
  
  public void update(Object bean, Set<String> updateProps)
  {
    update(bean, updateProps, null);
  }
  
  public void update(Object bean, Transaction t)
  {
    update(bean, null, t);
  }
  
  public void update(Object bean, Set<String> updateProps, Transaction t)
  {
    update(bean, updateProps, t, this.defaultDeleteMissingChildren, this.defaultUpdateNullProperties);
  }
  
  public void update(Object bean, Set<String> updateProps, Transaction t, boolean deleteMissingChildren, boolean updateNullProperties)
  {
    if (bean == null) {
      throw new NullPointerException(Message.msg("bean.isnull"));
    }
    this.persister.forceUpdate(bean, updateProps, t, deleteMissingChildren, updateNullProperties);
  }
  
  public void insert(Object bean)
  {
    insert(bean, null);
  }
  
  public void insert(Object bean, Transaction t)
  {
    if (bean == null) {
      throw new NullPointerException(Message.msg("bean.isnull"));
    }
    this.persister.forceInsert(bean, t);
  }
  
  public int deleteManyToManyAssociations(Object ownerBean, String propertyName)
  {
    return deleteManyToManyAssociations(ownerBean, propertyName, null);
  }
  
  public int deleteManyToManyAssociations(Object ownerBean, String propertyName, Transaction t)
  {
    TransWrapper wrap = initTransIfRequired(t);
    try
    {
      SpiTransaction trans = wrap.transaction;
      int rc = this.persister.deleteManyToManyAssociations(ownerBean, propertyName, trans);
      wrap.commitIfCreated();
      return rc;
    }
    catch (RuntimeException e)
    {
      wrap.rollbackIfCreated();
      throw e;
    }
  }
  
  public void saveManyToManyAssociations(Object ownerBean, String propertyName)
  {
    saveManyToManyAssociations(ownerBean, propertyName, null);
  }
  
  public void saveManyToManyAssociations(Object ownerBean, String propertyName, Transaction t)
  {
    TransWrapper wrap = initTransIfRequired(t);
    try
    {
      SpiTransaction trans = wrap.transaction;
      
      this.persister.saveManyToManyAssociations(ownerBean, propertyName, trans);
      
      wrap.commitIfCreated();
    }
    catch (RuntimeException e)
    {
      wrap.rollbackIfCreated();
      throw e;
    }
  }
  
  public void saveAssociation(Object ownerBean, String propertyName)
  {
    saveAssociation(ownerBean, propertyName, null);
  }
  
  public void saveAssociation(Object ownerBean, String propertyName, Transaction t)
  {
    if ((ownerBean instanceof EntityBean))
    {
      Set<String> loadedProps = ((EntityBean)ownerBean)._ebean_getIntercept().getLoadedProps();
      if ((loadedProps != null) && (!loadedProps.contains(propertyName)))
      {
        logger.fine("Skip saveAssociation as property " + propertyName + " is not loaded");
        return;
      }
    }
    TransWrapper wrap = initTransIfRequired(t);
    try
    {
      SpiTransaction trans = wrap.transaction;
      
      this.persister.saveAssociation(ownerBean, propertyName, trans);
      
      wrap.commitIfCreated();
    }
    catch (RuntimeException e)
    {
      wrap.rollbackIfCreated();
      throw e;
    }
  }
  
  public int save(Iterator<?> it)
  {
    return save(it, null);
  }
  
  public int save(Collection<?> c)
  {
    return save(c.iterator(), null);
  }
  
  public int save(Iterator<?> it, Transaction t)
  {
    TransWrapper wrap = initTransIfRequired(t);
    try
    {
      SpiTransaction trans = wrap.transaction;
      int saveCount = 0;
      while (it.hasNext())
      {
        Object bean = it.next();
        this.persister.save(bean, trans);
        saveCount++;
      }
      wrap.commitIfCreated();
      
      return saveCount;
    }
    catch (RuntimeException e)
    {
      wrap.rollbackIfCreated();
      throw e;
    }
  }
  
  public int delete(Class<?> beanType, Object id)
  {
    return delete(beanType, id, null);
  }
  
  public int delete(Class<?> beanType, Object id, Transaction t)
  {
    TransWrapper wrap = initTransIfRequired(t);
    try
    {
      SpiTransaction trans = wrap.transaction;
      int rowCount = this.persister.delete(beanType, id, trans);
      wrap.commitIfCreated();
      
      return rowCount;
    }
    catch (RuntimeException e)
    {
      wrap.rollbackIfCreated();
      throw e;
    }
  }
  
  public void delete(Class<?> beanType, Collection<?> ids)
  {
    delete(beanType, ids, null);
  }
  
  public void delete(Class<?> beanType, Collection<?> ids, Transaction t)
  {
    TransWrapper wrap = initTransIfRequired(t);
    try
    {
      SpiTransaction trans = wrap.transaction;
      this.persister.deleteMany(beanType, ids, trans);
      wrap.commitIfCreated();
    }
    catch (RuntimeException e)
    {
      wrap.rollbackIfCreated();
      throw e;
    }
  }
  
  public void delete(Object bean)
  {
    delete(bean, null);
  }
  
  public void delete(Object bean, Transaction t)
  {
    if (bean == null) {
      throw new NullPointerException(Message.msg("bean.isnull"));
    }
    this.persister.delete(bean, t);
  }
  
  public int delete(Iterator<?> it)
  {
    return delete(it, null);
  }
  
  public int delete(Collection<?> c)
  {
    return delete(c.iterator(), null);
  }
  
  public int delete(Iterator<?> it, Transaction t)
  {
    TransWrapper wrap = initTransIfRequired(t);
    try
    {
      SpiTransaction trans = wrap.transaction;
      int deleteCount = 0;
      while (it.hasNext())
      {
        Object bean = it.next();
        this.persister.delete(bean, trans);
        deleteCount++;
      }
      wrap.commitIfCreated();
      
      return deleteCount;
    }
    catch (RuntimeException e)
    {
      wrap.rollbackIfCreated();
      throw e;
    }
  }
  
  public int execute(CallableSql callSql, Transaction t)
  {
    return this.persister.executeCallable(callSql, t);
  }
  
  public int execute(CallableSql callSql)
  {
    return execute(callSql, null);
  }
  
  public int execute(SqlUpdate updSql, Transaction t)
  {
    return this.persister.executeSqlUpdate(updSql, t);
  }
  
  public int execute(SqlUpdate updSql)
  {
    return execute(updSql, null);
  }
  
  public int execute(Update<?> update, Transaction t)
  {
    return this.persister.executeOrmUpdate(update, t);
  }
  
  public int execute(Update<?> update)
  {
    return execute(update, null);
  }
  
  public <T> BeanManager<T> getBeanManager(Class<T> beanClass)
  {
    return this.beanDescriptorManager.getBeanManager(beanClass);
  }
  
  public List<BeanDescriptor<?>> getBeanDescriptors()
  {
    return this.beanDescriptorManager.getBeanDescriptorList();
  }
  
  public void register(BeanPersistController c)
  {
    List<BeanDescriptor<?>> list = this.beanDescriptorManager.getBeanDescriptorList();
    for (int i = 0; i < list.size(); i++) {
      ((BeanDescriptor)list.get(i)).register(c);
    }
  }
  
  public void deregister(BeanPersistController c)
  {
    List<BeanDescriptor<?>> list = this.beanDescriptorManager.getBeanDescriptorList();
    for (int i = 0; i < list.size(); i++) {
      ((BeanDescriptor)list.get(i)).deregister(c);
    }
  }
  
  public boolean isSupportedType(Type genericType)
  {
    ParamTypeHelper.TypeInfo typeInfo = ParamTypeHelper.getTypeInfo(genericType);
    if (typeInfo == null) {
      return false;
    }
    Class<?> beanType = typeInfo.getBeanType();
    if (JsonElement.class.isAssignableFrom(beanType)) {
      return true;
    }
    return getBeanDescriptor(typeInfo.getBeanType()) != null;
  }
  
  public Object getBeanId(Object bean)
  {
    BeanDescriptor<?> desc = getBeanDescriptor(bean.getClass());
    if (desc == null)
    {
      String m = bean.getClass().getName() + " is NOT an Entity Bean registered with this server?";
      throw new PersistenceException(m);
    }
    return desc.getId(bean);
  }
  
  public <T> BeanDescriptor<T> getBeanDescriptor(Class<T> beanClass)
  {
    return this.beanDescriptorManager.getBeanDescriptor(beanClass);
  }
  
  public List<BeanDescriptor<?>> getBeanDescriptors(String tableName)
  {
    return this.beanDescriptorManager.getBeanDescriptors(tableName);
  }
  
  public BeanDescriptor<?> getBeanDescriptorById(String descriptorId)
  {
    return this.beanDescriptorManager.getBeanDescriptorById(descriptorId);
  }
  
  public void remoteTransactionEvent(RemoteTransactionEvent event)
  {
    this.transactionManager.remoteTransactionEvent(event);
  }
  
  TransWrapper initTransIfRequired(Transaction t)
  {
    if (t != null) {
      return new TransWrapper((SpiTransaction)t, false);
    }
    boolean wasCreated = false;
    SpiTransaction trans = this.transactionScopeManager.get();
    if (trans == null)
    {
      trans = this.transactionManager.createTransaction(false, -1);
      wasCreated = true;
    }
    return new TransWrapper(trans, wasCreated);
  }
  
  public SpiTransaction createServerTransaction(boolean isExplicit, int isolationLevel)
  {
    return this.transactionManager.createTransaction(isExplicit, isolationLevel);
  }
  
  public SpiTransaction createQueryTransaction()
  {
    return this.transactionManager.createQueryTransaction();
  }
  
  private static final String AVAJE_EBEAN = Ebean.class.getName().substring(0, 15);
  
  public CallStack createCallStack()
  {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    for (int startIndex = 5; startIndex < stackTrace.length; startIndex++) {
      if (!stackTrace[startIndex].getClassName().startsWith(AVAJE_EBEAN)) {
        break;
      }
    }
    int stackLength = stackTrace.length - startIndex;
    if (stackLength > this.maxCallStack) {
      stackLength = this.maxCallStack;
    }
    StackTraceElement[] finalTrace = new StackTraceElement[stackLength];
    for (int i = 0; i < stackLength; i++) {
      finalTrace[i] = stackTrace[(i + startIndex)];
    }
    if (stackLength < 1) {
      throw new RuntimeException("StackTraceElement size 0?  stack: " + Arrays.toString(stackTrace));
    }
    return new CallStack(finalTrace);
  }
  
  public JsonContext createJsonContext()
  {
    return this.jsonContext;
  }
}
