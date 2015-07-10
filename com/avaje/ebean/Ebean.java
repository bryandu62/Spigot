package com.avaje.ebean;

import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.text.csv.CsvReader;
import com.avaje.ebean.text.json.JsonContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

public final class Ebean
{
  private static final Logger logger = Logger.getLogger(Ebean.class.getName());
  private static final ServerManager serverMgr = new ServerManager(null);
  
  private static final class ServerManager
  {
    private final ConcurrentHashMap<String, EbeanServer> concMap = new ConcurrentHashMap();
    private final HashMap<String, EbeanServer> syncMap = new HashMap();
    private final Object monitor = new Object();
    private EbeanServer primaryServer;
    
    private ServerManager()
    {
      if (GlobalProperties.isSkipPrimaryServer())
      {
        Ebean.logger.fine("GlobalProperties.isSkipPrimaryServer()");
      }
      else
      {
        String primaryName = getPrimaryServerName();
        Ebean.logger.fine("primaryName:" + primaryName);
        if ((primaryName != null) && (primaryName.trim().length() > 0)) {
          this.primaryServer = getWithCreate(primaryName.trim());
        }
      }
    }
    
    private String getPrimaryServerName()
    {
      String serverName = GlobalProperties.get("ebean.default.datasource", null);
      return GlobalProperties.get("datasource.default", serverName);
    }
    
    private EbeanServer getPrimaryServer()
    {
      if (this.primaryServer == null)
      {
        String msg = "The default EbeanServer has not been defined?";
        msg = msg + " This is normally set via the ebean.datasource.default property.";
        msg = msg + " Otherwise it should be registered programatically via registerServer()";
        throw new PersistenceException(msg);
      }
      return this.primaryServer;
    }
    
    private EbeanServer get(String name)
    {
      if ((name == null) || (name.length() == 0)) {
        return this.primaryServer;
      }
      EbeanServer server = (EbeanServer)this.concMap.get(name);
      if (server != null) {
        return server;
      }
      return getWithCreate(name);
    }
    
    private EbeanServer getWithCreate(String name)
    {
      synchronized (this.monitor)
      {
        EbeanServer server = (EbeanServer)this.syncMap.get(name);
        if (server == null)
        {
          server = EbeanServerFactory.create(name);
          register(server, false);
        }
        return server;
      }
    }
    
    private void register(EbeanServer server, boolean isPrimaryServer)
    {
      synchronized (this.monitor)
      {
        this.concMap.put(server.getName(), server);
        EbeanServer existingServer = (EbeanServer)this.syncMap.put(server.getName(), server);
        if (existingServer != null)
        {
          String msg = "Existing EbeanServer [" + server.getName() + "] is being replaced?";
          Ebean.logger.warning(msg);
        }
        if (isPrimaryServer) {
          this.primaryServer = server;
        }
      }
    }
  }
  
  public static EbeanServer getServer(String name)
  {
    return serverMgr.get(name);
  }
  
  public static ExpressionFactory getExpressionFactory()
  {
    return serverMgr.getPrimaryServer().getExpressionFactory();
  }
  
  protected static void register(EbeanServer server, boolean isPrimaryServer)
  {
    serverMgr.register(server, isPrimaryServer);
  }
  
  public static Object nextId(Class<?> beanType)
  {
    return serverMgr.getPrimaryServer().nextId(beanType);
  }
  
  public static void logComment(String msg)
  {
    serverMgr.getPrimaryServer().logComment(msg);
  }
  
  public static Transaction beginTransaction()
  {
    return serverMgr.getPrimaryServer().beginTransaction();
  }
  
  public static Transaction beginTransaction(TxIsolation isolation)
  {
    return serverMgr.getPrimaryServer().beginTransaction(isolation);
  }
  
  public static Transaction currentTransaction()
  {
    return serverMgr.getPrimaryServer().currentTransaction();
  }
  
  public static void commitTransaction()
  {
    serverMgr.getPrimaryServer().commitTransaction();
  }
  
  public static void rollbackTransaction()
  {
    serverMgr.getPrimaryServer().rollbackTransaction();
  }
  
  public static void endTransaction()
  {
    serverMgr.getPrimaryServer().endTransaction();
  }
  
  public static InvalidValue validate(Object bean)
  {
    return serverMgr.getPrimaryServer().validate(bean);
  }
  
  public static InvalidValue[] validate(Object bean, String propertyName, Object value)
  {
    return serverMgr.getPrimaryServer().validate(bean, propertyName, value);
  }
  
  public static Map<String, ValuePair> diff(Object a, Object b)
  {
    return serverMgr.getPrimaryServer().diff(a, b);
  }
  
  public static void save(Object bean)
    throws OptimisticLockException
  {
    serverMgr.getPrimaryServer().save(bean);
  }
  
  public static void update(Object bean)
  {
    serverMgr.getPrimaryServer().update(bean);
  }
  
  public static void update(Object bean, Set<String> updateProps)
  {
    serverMgr.getPrimaryServer().update(bean, updateProps);
  }
  
  public static int save(Iterator<?> iterator)
    throws OptimisticLockException
  {
    return serverMgr.getPrimaryServer().save(iterator);
  }
  
  public static int save(Collection<?> c)
    throws OptimisticLockException
  {
    return save(c.iterator());
  }
  
  public static int deleteManyToManyAssociations(Object ownerBean, String propertyName)
  {
    return serverMgr.getPrimaryServer().deleteManyToManyAssociations(ownerBean, propertyName);
  }
  
  public static void saveManyToManyAssociations(Object ownerBean, String propertyName)
  {
    serverMgr.getPrimaryServer().saveManyToManyAssociations(ownerBean, propertyName);
  }
  
  public static void saveAssociation(Object ownerBean, String propertyName)
  {
    serverMgr.getPrimaryServer().saveAssociation(ownerBean, propertyName);
  }
  
  public static void delete(Object bean)
    throws OptimisticLockException
  {
    serverMgr.getPrimaryServer().delete(bean);
  }
  
  public static int delete(Class<?> beanType, Object id)
  {
    return serverMgr.getPrimaryServer().delete(beanType, id);
  }
  
  public static void delete(Class<?> beanType, Collection<?> ids)
  {
    serverMgr.getPrimaryServer().delete(beanType, ids);
  }
  
  public static int delete(Iterator<?> it)
    throws OptimisticLockException
  {
    return serverMgr.getPrimaryServer().delete(it);
  }
  
  public static int delete(Collection<?> c)
    throws OptimisticLockException
  {
    return delete(c.iterator());
  }
  
  public static void refresh(Object bean)
  {
    serverMgr.getPrimaryServer().refresh(bean);
  }
  
  public static void refreshMany(Object bean, String manyPropertyName)
  {
    serverMgr.getPrimaryServer().refreshMany(bean, manyPropertyName);
  }
  
  public static <T> T getReference(Class<T> beanType, Object id)
  {
    return (T)serverMgr.getPrimaryServer().getReference(beanType, id);
  }
  
  public static <T> void sort(List<T> list, String sortByClause)
  {
    serverMgr.getPrimaryServer().sort(list, sortByClause);
  }
  
  public static <T> T find(Class<T> beanType, Object id)
  {
    return (T)serverMgr.getPrimaryServer().find(beanType, id);
  }
  
  public static SqlQuery createSqlQuery(String sql)
  {
    return serverMgr.getPrimaryServer().createSqlQuery(sql);
  }
  
  public static SqlQuery createNamedSqlQuery(String namedQuery)
  {
    return serverMgr.getPrimaryServer().createNamedSqlQuery(namedQuery);
  }
  
  public static SqlUpdate createSqlUpdate(String sql)
  {
    return serverMgr.getPrimaryServer().createSqlUpdate(sql);
  }
  
  public static CallableSql createCallableSql(String sql)
  {
    return serverMgr.getPrimaryServer().createCallableSql(sql);
  }
  
  public static SqlUpdate createNamedSqlUpdate(String namedQuery)
  {
    return serverMgr.getPrimaryServer().createNamedSqlUpdate(namedQuery);
  }
  
  public static <T> Query<T> createNamedQuery(Class<T> beanType, String namedQuery)
  {
    return serverMgr.getPrimaryServer().createNamedQuery(beanType, namedQuery);
  }
  
  public static <T> Query<T> createQuery(Class<T> beanType, String query)
  {
    return serverMgr.getPrimaryServer().createQuery(beanType, query);
  }
  
  public static <T> Update<T> createNamedUpdate(Class<T> beanType, String namedUpdate)
  {
    return serverMgr.getPrimaryServer().createNamedUpdate(beanType, namedUpdate);
  }
  
  public static <T> Update<T> createUpdate(Class<T> beanType, String ormUpdate)
  {
    return serverMgr.getPrimaryServer().createUpdate(beanType, ormUpdate);
  }
  
  public static <T> CsvReader<T> createCsvReader(Class<T> beanType)
  {
    return serverMgr.getPrimaryServer().createCsvReader(beanType);
  }
  
  public static <T> Query<T> createQuery(Class<T> beanType)
  {
    return serverMgr.getPrimaryServer().createQuery(beanType);
  }
  
  public static <T> Query<T> find(Class<T> beanType)
  {
    return serverMgr.getPrimaryServer().find(beanType);
  }
  
  public static <T> Filter<T> filter(Class<T> beanType)
  {
    return serverMgr.getPrimaryServer().filter(beanType);
  }
  
  public static int execute(SqlUpdate sqlUpdate)
  {
    return serverMgr.getPrimaryServer().execute(sqlUpdate);
  }
  
  public static int execute(CallableSql callableSql)
  {
    return serverMgr.getPrimaryServer().execute(callableSql);
  }
  
  public static void execute(TxScope scope, TxRunnable r)
  {
    serverMgr.getPrimaryServer().execute(scope, r);
  }
  
  public static void execute(TxRunnable r)
  {
    serverMgr.getPrimaryServer().execute(r);
  }
  
  public static <T> T execute(TxScope scope, TxCallable<T> c)
  {
    return (T)serverMgr.getPrimaryServer().execute(scope, c);
  }
  
  public static <T> T execute(TxCallable<T> c)
  {
    return (T)serverMgr.getPrimaryServer().execute(c);
  }
  
  public static void externalModification(String tableName, boolean inserts, boolean updates, boolean deletes)
  {
    serverMgr.getPrimaryServer().externalModification(tableName, inserts, updates, deletes);
  }
  
  public static BeanState getBeanState(Object bean)
  {
    return serverMgr.getPrimaryServer().getBeanState(bean);
  }
  
  public static ServerCacheManager getServerCacheManager()
  {
    return serverMgr.getPrimaryServer().getServerCacheManager();
  }
  
  public static BackgroundExecutor getBackgroundExecutor()
  {
    return serverMgr.getPrimaryServer().getBackgroundExecutor();
  }
  
  public static void runCacheWarming()
  {
    serverMgr.getPrimaryServer().runCacheWarming();
  }
  
  public static void runCacheWarming(Class<?> beanType)
  {
    serverMgr.getPrimaryServer().runCacheWarming(beanType);
  }
  
  public static JsonContext createJsonContext()
  {
    return serverMgr.getPrimaryServer().createJsonContext();
  }
}
