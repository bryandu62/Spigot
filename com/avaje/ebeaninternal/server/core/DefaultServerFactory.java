package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.cache.ServerCacheFactory;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.cache.ServerCacheOptions;
import com.avaje.ebean.common.BootupEbeanManager;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.NamingConvention;
import com.avaje.ebean.config.PstmtDelegate;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.UnderscoreNamingConvention;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebeaninternal.api.SpiBackgroundExecutor;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.cache.DefaultServerCacheFactory;
import com.avaje.ebeaninternal.server.cache.DefaultServerCacheManager;
import com.avaje.ebeaninternal.server.cluster.ClusterManager;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.avaje.ebeaninternal.server.jdbc.OraclePstmtBatch;
import com.avaje.ebeaninternal.server.jdbc.StandardPstmtDelegate;
import com.avaje.ebeaninternal.server.lib.ShutdownManager;
import com.avaje.ebeaninternal.server.lib.sql.DataSourceGlobalManager;
import com.avaje.ebeaninternal.server.lib.sql.DataSourcePool;
import com.avaje.ebeaninternal.server.lib.thread.ThreadPool;
import com.avaje.ebeaninternal.server.lib.thread.ThreadPoolManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

public class DefaultServerFactory
  implements BootupEbeanManager
{
  private static final Logger logger = Logger.getLogger(DefaultServerFactory.class.getName());
  private final ClusterManager clusterManager;
  private final JndiDataSourceLookup jndiDataSourceFactory;
  private final BootupClassPathSearch bootupClassSearch;
  private final AtomicInteger serverId = new AtomicInteger(1);
  private final XmlConfigLoader xmlConfigLoader;
  private final XmlConfig xmlConfig;
  
  public DefaultServerFactory()
  {
    this.clusterManager = new ClusterManager();
    this.jndiDataSourceFactory = new JndiDataSourceLookup();
    
    List<String> packages = getSearchJarsPackages(GlobalProperties.get("ebean.search.packages", null));
    List<String> jars = getSearchJarsPackages(GlobalProperties.get("ebean.search.jars", null));
    
    this.bootupClassSearch = new BootupClassPathSearch(null, packages, jars);
    this.xmlConfigLoader = new XmlConfigLoader(null);
    
    this.xmlConfig = this.xmlConfigLoader.load();
    
    ShutdownManager.registerServerFactory(this);
  }
  
  private List<String> getSearchJarsPackages(String searchPackages)
  {
    List<String> hitList = new ArrayList();
    if (searchPackages != null)
    {
      String[] entries = searchPackages.split("[ ,;]");
      for (int i = 0; i < entries.length; i++) {
        hitList.add(entries[i].trim());
      }
    }
    return hitList;
  }
  
  public void shutdown()
  {
    this.clusterManager.shutdown();
  }
  
  public SpiEbeanServer createServer(String name)
  {
    ConfigBuilder b = new ConfigBuilder();
    ServerConfig config = b.build(name);
    
    return createServer(config);
  }
  
  private SpiBackgroundExecutor createBackgroundExecutor(ServerConfig serverConfig, int uniqueServerId)
  {
    String namePrefix = "Ebean-" + serverConfig.getName();
    
    int schedulePoolSize = GlobalProperties.getInt("backgroundExecutor.schedulePoolsize", 1);
    
    int minPoolSize = GlobalProperties.getInt("backgroundExecutor.minPoolSize", 1);
    int poolSize = GlobalProperties.getInt("backgroundExecutor.poolsize", 20);
    int maxPoolSize = GlobalProperties.getInt("backgroundExecutor.maxPoolSize", poolSize);
    
    int idleSecs = GlobalProperties.getInt("backgroundExecutor.idlesecs", 60);
    int shutdownSecs = GlobalProperties.getInt("backgroundExecutor.shutdownSecs", 30);
    
    boolean useTrad = GlobalProperties.getBoolean("backgroundExecutor.traditional", true);
    if (useTrad)
    {
      ThreadPool pool = ThreadPoolManager.getThreadPool(namePrefix);
      pool.setMinSize(minPoolSize);
      pool.setMaxSize(maxPoolSize);
      pool.setMaxIdleTime(idleSecs * 1000);
      return new TraditionalBackgroundExecutor(pool, schedulePoolSize, shutdownSecs, namePrefix);
    }
    return new DefaultBackgroundExecutor(poolSize, schedulePoolSize, idleSecs, shutdownSecs, namePrefix);
  }
  
  public SpiEbeanServer createServer(ServerConfig serverConfig)
  {
    synchronized (this)
    {
      setNamingConvention(serverConfig);
      
      BootupClasses bootupClasses = getBootupClasses(serverConfig);
      
      setDataSource(serverConfig);
      
      boolean online = checkDataSource(serverConfig);
      
      setDatabasePlatform(serverConfig);
      if (serverConfig.getDbEncrypt() != null) {
        serverConfig.getDatabasePlatform().setDbEncrypt(serverConfig.getDbEncrypt());
      }
      DatabasePlatform dbPlatform = serverConfig.getDatabasePlatform();
      
      PstmtBatch pstmtBatch = null;
      if (dbPlatform.getName().startsWith("oracle"))
      {
        PstmtDelegate pstmtDelegate = serverConfig.getPstmtDelegate();
        if (pstmtDelegate == null) {
          pstmtDelegate = getOraclePstmtDelegate(serverConfig.getDataSource());
        }
        if (pstmtDelegate != null) {
          pstmtBatch = new OraclePstmtBatch(pstmtDelegate);
        }
        if (pstmtBatch == null)
        {
          logger.warning("Can not support JDBC batching with Oracle without a PstmtDelegate");
          serverConfig.setPersistBatching(false);
        }
      }
      serverConfig.getNamingConvention().setDatabasePlatform(serverConfig.getDatabasePlatform());
      
      ServerCacheManager cacheManager = getCacheManager(serverConfig);
      
      int uniqueServerId = this.serverId.incrementAndGet();
      SpiBackgroundExecutor bgExecutor = createBackgroundExecutor(serverConfig, uniqueServerId);
      
      InternalConfiguration c = new InternalConfiguration(this.xmlConfig, this.clusterManager, cacheManager, bgExecutor, serverConfig, bootupClasses, pstmtBatch);
      
      DefaultServer server = new DefaultServer(c, cacheManager);
      
      cacheManager.init(server);
      
      ArrayList<?> list = MBeanServerFactory.findMBeanServer(null);
      MBeanServer mbeanServer;
      MBeanServer mbeanServer;
      if (list.size() == 0) {
        mbeanServer = MBeanServerFactory.createMBeanServer();
      } else {
        mbeanServer = (MBeanServer)list.get(0);
      }
      server.registerMBeans(mbeanServer, uniqueServerId);
      
      executeDDL(server, online);
      
      server.initialise();
      if (online)
      {
        if (this.clusterManager.isClustering()) {
          this.clusterManager.registerServer(server);
        }
        int delaySecs = GlobalProperties.getInt("ebean.cacheWarmingDelay", 30);
        long sleepMillis = 1000 * delaySecs;
        if (sleepMillis > 0L)
        {
          Timer t = new Timer("EbeanCacheWarmer", true);
          t.schedule(new CacheWarmer(sleepMillis, server), sleepMillis);
        }
      }
      server.start();
      return server;
    }
  }
  
  private PstmtDelegate getOraclePstmtDelegate(DataSource ds)
  {
    if ((ds instanceof DataSourcePool)) {
      return new StandardPstmtDelegate();
    }
    return null;
  }
  
  private ServerCacheManager getCacheManager(ServerConfig serverConfig)
  {
    ServerCacheManager serverCacheManager = serverConfig.getServerCacheManager();
    if (serverCacheManager != null) {
      return serverCacheManager;
    }
    ServerCacheOptions beanOptions = new ServerCacheOptions();
    beanOptions.setMaxSize(GlobalProperties.getInt("cache.maxSize", 1000));
    
    beanOptions.setMaxIdleSecs(GlobalProperties.getInt("cache.maxIdleTime", 600));
    
    beanOptions.setMaxSecsToLive(GlobalProperties.getInt("cache.maxTimeToLive", 21600));
    
    ServerCacheOptions queryOptions = new ServerCacheOptions();
    queryOptions.setMaxSize(GlobalProperties.getInt("querycache.maxSize", 100));
    
    queryOptions.setMaxIdleSecs(GlobalProperties.getInt("querycache.maxIdleTime", 600));
    
    queryOptions.setMaxSecsToLive(GlobalProperties.getInt("querycache.maxTimeToLive", 21600));
    
    ServerCacheFactory cacheFactory = serverConfig.getServerCacheFactory();
    if (cacheFactory == null) {
      cacheFactory = new DefaultServerCacheFactory();
    }
    return new DefaultServerCacheManager(cacheFactory, beanOptions, queryOptions);
  }
  
  private BootupClasses getBootupClasses(ServerConfig serverConfig)
  {
    BootupClasses bootupClasses = getBootupClasses1(serverConfig);
    bootupClasses.addPersistControllers(serverConfig.getPersistControllers());
    bootupClasses.addTransactionEventListeners(serverConfig.getTransactionEventListeners());
    bootupClasses.addPersistListeners(serverConfig.getPersistListeners());
    bootupClasses.addQueryAdapters(serverConfig.getQueryAdapters());
    bootupClasses.addServerConfigStartup(serverConfig.getServerConfigStartupListeners());
    
    bootupClasses.runServerConfigStartup(serverConfig);
    return bootupClasses;
  }
  
  private BootupClasses getBootupClasses1(ServerConfig serverConfig)
  {
    List<Class<?>> entityClasses = serverConfig.getClasses();
    if ((entityClasses != null) && (entityClasses.size() > 0)) {
      return new BootupClasses(serverConfig.getClasses());
    }
    List<String> jars = serverConfig.getJars();
    List<String> packages = serverConfig.getPackages();
    if (((packages != null) && (!packages.isEmpty())) || ((jars != null) && (!jars.isEmpty())))
    {
      BootupClassPathSearch search = new BootupClassPathSearch(null, packages, jars);
      return search.getBootupClasses();
    }
    return this.bootupClassSearch.getBootupClasses().createCopy();
  }
  
  private void executeDDL(SpiEbeanServer server, boolean online)
  {
    server.getDdlGenerator().execute(online);
  }
  
  private void setNamingConvention(ServerConfig config)
  {
    if (config.getNamingConvention() == null)
    {
      UnderscoreNamingConvention nc = new UnderscoreNamingConvention();
      config.setNamingConvention(nc);
      
      String v = config.getProperty("namingConvention.useForeignKeyPrefix");
      if (v != null)
      {
        boolean useForeignKeyPrefix = Boolean.valueOf(v).booleanValue();
        nc.setUseForeignKeyPrefix(useForeignKeyPrefix);
      }
      String sequenceFormat = config.getProperty("namingConvention.sequenceFormat");
      if (sequenceFormat != null) {
        nc.setSequenceFormat(sequenceFormat);
      }
    }
  }
  
  private void setDatabasePlatform(ServerConfig config)
  {
    DatabasePlatform dbPlatform = config.getDatabasePlatform();
    if (dbPlatform == null)
    {
      DatabasePlatformFactory factory = new DatabasePlatformFactory();
      
      DatabasePlatform db = factory.create(config);
      config.setDatabasePlatform(db);
      logger.info("DatabasePlatform name:" + config.getName() + " platform:" + db.getName());
    }
  }
  
  private void setDataSource(ServerConfig config)
  {
    if (config.getDataSource() == null)
    {
      DataSource ds = getDataSourceFromConfig(config);
      config.setDataSource(ds);
    }
  }
  
  private DataSource getDataSourceFromConfig(ServerConfig config)
  {
    DataSource ds = null;
    if (config.getDataSourceJndiName() != null)
    {
      ds = this.jndiDataSourceFactory.lookup(config.getDataSourceJndiName());
      if (ds == null)
      {
        String m = "JNDI lookup for DataSource " + config.getDataSourceJndiName() + " returned null.";
        throw new PersistenceException(m);
      }
      return ds;
    }
    DataSourceConfig dsConfig = config.getDataSourceConfig();
    if (dsConfig == null)
    {
      String m = "No DataSourceConfig definded for " + config.getName();
      throw new PersistenceException(m);
    }
    if (dsConfig.isOffline())
    {
      if (config.getDatabasePlatformName() == null)
      {
        String m = "You MUST specify a DatabasePlatformName on ServerConfig when offline";
        throw new PersistenceException(m);
      }
      return null;
    }
    if (dsConfig.getHeartbeatSql() == null)
    {
      String heartbeatSql = getHeartbeatSql(dsConfig.getDriver());
      dsConfig.setHeartbeatSql(heartbeatSql);
    }
    return DataSourceGlobalManager.getDataSource(config.getName(), dsConfig);
  }
  
  private String getHeartbeatSql(String driver)
  {
    if (driver != null)
    {
      String d = driver.toLowerCase();
      if (d.contains("oracle")) {
        return "select 'x' from dual";
      }
      if ((d.contains(".h2.")) || (d.contains(".mysql.")) || (d.contains("postgre"))) {
        return "select 1";
      }
    }
    return null;
  }
  
  private boolean checkDataSource(ServerConfig serverConfig)
  {
    if (serverConfig.getDataSource() == null)
    {
      if (serverConfig.getDataSourceConfig().isOffline()) {
        return false;
      }
      throw new RuntimeException("DataSource not set?");
    }
    Connection c = null;
    try
    {
      c = serverConfig.getDataSource().getConnection();
      String m;
      if (c.getAutoCommit())
      {
        m = "DataSource [" + serverConfig.getName() + "] has autoCommit defaulting to true!";
        logger.warning(m);
      }
      return 1;
    }
    catch (SQLException ex)
    {
      throw new PersistenceException(ex);
    }
    finally
    {
      if (c != null) {
        try
        {
          c.close();
        }
        catch (SQLException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }
  
  private static class CacheWarmer
    extends TimerTask
  {
    private static final Logger log = Logger.getLogger(CacheWarmer.class.getName());
    private final long sleepMillis;
    private final EbeanServer server;
    
    CacheWarmer(long sleepMillis, EbeanServer server)
    {
      this.sleepMillis = sleepMillis;
      this.server = server;
    }
    
    public void run()
    {
      try
      {
        Thread.sleep(this.sleepMillis);
      }
      catch (InterruptedException e)
      {
        String msg = "Error while sleeping prior to cache warming";
        log.log(Level.SEVERE, msg, e);
      }
      this.server.runCacheWarming();
    }
  }
}
