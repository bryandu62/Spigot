package com.avaje.ebean.config;

import com.avaje.ebean.LogLevel;
import com.avaje.ebean.cache.ServerCacheFactory;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.DbEncrypt;
import com.avaje.ebean.config.ldap.LdapConfig;
import com.avaje.ebean.config.ldap.LdapContextFactory;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebean.event.BeanPersistListener;
import com.avaje.ebean.event.BeanQueryAdapter;
import com.avaje.ebean.event.BulkTableEventListener;
import com.avaje.ebean.event.ServerConfigStartup;
import com.avaje.ebean.event.TransactionEventListener;
import com.avaje.ebeaninternal.api.ClassUtil;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ServerConfig
{
  private static final int DEFAULT_QUERY_BATCH_SIZE = 100;
  private String name;
  private String resourceDirectory;
  private int enhanceLogLevel;
  private boolean register = true;
  private boolean defaultServer;
  private boolean validateOnSave = true;
  private List<Class<?>> classes = new ArrayList();
  private List<String> packages = new ArrayList();
  private List<String> searchJars = new ArrayList();
  private AutofetchConfig autofetchConfig = new AutofetchConfig();
  private String databasePlatformName;
  private DatabasePlatform databasePlatform;
  private int databaseSequenceBatchSize = 20;
  private boolean persistBatching;
  private int persistBatchSize = 20;
  private int lazyLoadBatchSize = 1;
  private int queryBatchSize = -1;
  private boolean ddlGenerate;
  private boolean ddlRun;
  private boolean debugSql;
  private boolean debugLazyLoad;
  private boolean useJtaTransactionManager;
  private ExternalTransactionManager externalTransactionManager;
  private boolean loggingToJavaLogger;
  private String loggingDirectory = "logs";
  private LogLevel loggingLevel = LogLevel.NONE;
  private PstmtDelegate pstmtDelegate;
  private DataSource dataSource;
  private DataSourceConfig dataSourceConfig = new DataSourceConfig();
  private String dataSourceJndiName;
  private String databaseBooleanTrue;
  private String databaseBooleanFalse;
  private NamingConvention namingConvention;
  private boolean updateChangesOnly = true;
  private List<BeanPersistController> persistControllers = new ArrayList();
  private List<BeanPersistListener<?>> persistListeners = new ArrayList();
  private List<BeanQueryAdapter> queryAdapters = new ArrayList();
  private List<BulkTableEventListener> bulkTableEventListeners = new ArrayList();
  private List<ServerConfigStartup> configStartupListeners = new ArrayList();
  private List<TransactionEventListener> transactionEventListeners = new ArrayList();
  private EncryptKeyManager encryptKeyManager;
  private EncryptDeployManager encryptDeployManager;
  private Encryptor encryptor;
  private DbEncrypt dbEncrypt;
  private LdapConfig ldapConfig;
  private ServerCacheFactory serverCacheFactory;
  private ServerCacheManager serverCacheManager;
  private boolean vanillaMode;
  private boolean vanillaRefMode;
  private boolean allowSubclassing = true;
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public boolean isRegister()
  {
    return this.register;
  }
  
  public void setRegister(boolean register)
  {
    this.register = register;
  }
  
  public boolean isDefaultServer()
  {
    return this.defaultServer;
  }
  
  public void setDefaultServer(boolean defaultServer)
  {
    this.defaultServer = defaultServer;
  }
  
  public boolean isPersistBatching()
  {
    return this.persistBatching;
  }
  
  /**
   * @deprecated
   */
  public boolean isUsePersistBatching()
  {
    return this.persistBatching;
  }
  
  public void setPersistBatching(boolean persistBatching)
  {
    this.persistBatching = persistBatching;
  }
  
  /**
   * @deprecated
   */
  public void setUsePersistBatching(boolean persistBatching)
  {
    this.persistBatching = persistBatching;
  }
  
  public int getPersistBatchSize()
  {
    return this.persistBatchSize;
  }
  
  public void setPersistBatchSize(int persistBatchSize)
  {
    this.persistBatchSize = persistBatchSize;
  }
  
  public int getLazyLoadBatchSize()
  {
    return this.lazyLoadBatchSize;
  }
  
  public int getQueryBatchSize()
  {
    return this.queryBatchSize;
  }
  
  public void setQueryBatchSize(int queryBatchSize)
  {
    this.queryBatchSize = queryBatchSize;
  }
  
  public void setLazyLoadBatchSize(int lazyLoadBatchSize)
  {
    this.lazyLoadBatchSize = lazyLoadBatchSize;
  }
  
  public void setDatabaseSequenceBatchSize(int databaseSequenceBatchSize)
  {
    this.databaseSequenceBatchSize = databaseSequenceBatchSize;
  }
  
  public boolean isUseJtaTransactionManager()
  {
    return this.useJtaTransactionManager;
  }
  
  public void setUseJtaTransactionManager(boolean useJtaTransactionManager)
  {
    this.useJtaTransactionManager = useJtaTransactionManager;
  }
  
  public ExternalTransactionManager getExternalTransactionManager()
  {
    return this.externalTransactionManager;
  }
  
  public void setExternalTransactionManager(ExternalTransactionManager externalTransactionManager)
  {
    this.externalTransactionManager = externalTransactionManager;
  }
  
  public ServerCacheFactory getServerCacheFactory()
  {
    return this.serverCacheFactory;
  }
  
  public void setServerCacheFactory(ServerCacheFactory serverCacheFactory)
  {
    this.serverCacheFactory = serverCacheFactory;
  }
  
  public ServerCacheManager getServerCacheManager()
  {
    return this.serverCacheManager;
  }
  
  public void setServerCacheManager(ServerCacheManager serverCacheManager)
  {
    this.serverCacheManager = serverCacheManager;
  }
  
  public boolean isVanillaMode()
  {
    return this.vanillaMode;
  }
  
  public void setVanillaMode(boolean vanillaMode)
  {
    this.vanillaMode = vanillaMode;
  }
  
  public boolean isVanillaRefMode()
  {
    return this.vanillaRefMode;
  }
  
  public void setVanillaRefMode(boolean vanillaRefMode)
  {
    this.vanillaRefMode = vanillaRefMode;
  }
  
  public boolean isValidateOnSave()
  {
    return this.validateOnSave;
  }
  
  public void setValidateOnSave(boolean validateOnSave)
  {
    this.validateOnSave = validateOnSave;
  }
  
  public int getEnhanceLogLevel()
  {
    return this.enhanceLogLevel;
  }
  
  public void setEnhanceLogLevel(int enhanceLogLevel)
  {
    this.enhanceLogLevel = enhanceLogLevel;
  }
  
  public NamingConvention getNamingConvention()
  {
    return this.namingConvention;
  }
  
  public void setNamingConvention(NamingConvention namingConvention)
  {
    this.namingConvention = namingConvention;
  }
  
  public AutofetchConfig getAutofetchConfig()
  {
    return this.autofetchConfig;
  }
  
  public void setAutofetchConfig(AutofetchConfig autofetchConfig)
  {
    this.autofetchConfig = autofetchConfig;
  }
  
  public PstmtDelegate getPstmtDelegate()
  {
    return this.pstmtDelegate;
  }
  
  public void setPstmtDelegate(PstmtDelegate pstmtDelegate)
  {
    this.pstmtDelegate = pstmtDelegate;
  }
  
  public DataSource getDataSource()
  {
    return this.dataSource;
  }
  
  public void setDataSource(DataSource dataSource)
  {
    this.dataSource = dataSource;
  }
  
  public DataSourceConfig getDataSourceConfig()
  {
    return this.dataSourceConfig;
  }
  
  public void setDataSourceConfig(DataSourceConfig dataSourceConfig)
  {
    this.dataSourceConfig = dataSourceConfig;
  }
  
  public String getDataSourceJndiName()
  {
    return this.dataSourceJndiName;
  }
  
  public void setDataSourceJndiName(String dataSourceJndiName)
  {
    this.dataSourceJndiName = dataSourceJndiName;
  }
  
  public String getDatabaseBooleanTrue()
  {
    return this.databaseBooleanTrue;
  }
  
  public void setDatabaseBooleanTrue(String databaseTrue)
  {
    this.databaseBooleanTrue = databaseTrue;
  }
  
  public String getDatabaseBooleanFalse()
  {
    return this.databaseBooleanFalse;
  }
  
  public void setDatabaseBooleanFalse(String databaseFalse)
  {
    this.databaseBooleanFalse = databaseFalse;
  }
  
  public int getDatabaseSequenceBatchSize()
  {
    return this.databaseSequenceBatchSize;
  }
  
  public void setDatabaseSequenceBatch(int databaseSequenceBatchSize)
  {
    this.databaseSequenceBatchSize = databaseSequenceBatchSize;
  }
  
  public String getDatabasePlatformName()
  {
    return this.databasePlatformName;
  }
  
  public void setDatabasePlatformName(String databasePlatformName)
  {
    this.databasePlatformName = databasePlatformName;
  }
  
  public DatabasePlatform getDatabasePlatform()
  {
    return this.databasePlatform;
  }
  
  public void setDatabasePlatform(DatabasePlatform databasePlatform)
  {
    this.databasePlatform = databasePlatform;
  }
  
  public EncryptKeyManager getEncryptKeyManager()
  {
    return this.encryptKeyManager;
  }
  
  public void setEncryptKeyManager(EncryptKeyManager encryptKeyManager)
  {
    this.encryptKeyManager = encryptKeyManager;
  }
  
  public EncryptDeployManager getEncryptDeployManager()
  {
    return this.encryptDeployManager;
  }
  
  public void setEncryptDeployManager(EncryptDeployManager encryptDeployManager)
  {
    this.encryptDeployManager = encryptDeployManager;
  }
  
  public Encryptor getEncryptor()
  {
    return this.encryptor;
  }
  
  public void setEncryptor(Encryptor encryptor)
  {
    this.encryptor = encryptor;
  }
  
  public DbEncrypt getDbEncrypt()
  {
    return this.dbEncrypt;
  }
  
  public void setDbEncrypt(DbEncrypt dbEncrypt)
  {
    this.dbEncrypt = dbEncrypt;
  }
  
  public boolean isDebugSql()
  {
    return this.debugSql;
  }
  
  public void setDebugSql(boolean debugSql)
  {
    this.debugSql = debugSql;
  }
  
  public boolean isDebugLazyLoad()
  {
    return this.debugLazyLoad;
  }
  
  public void setDebugLazyLoad(boolean debugLazyLoad)
  {
    this.debugLazyLoad = debugLazyLoad;
  }
  
  public LogLevel getLoggingLevel()
  {
    return this.loggingLevel;
  }
  
  public void setLoggingLevel(LogLevel logLevel)
  {
    this.loggingLevel = logLevel;
  }
  
  public String getLoggingDirectory()
  {
    return this.loggingDirectory;
  }
  
  public String getLoggingDirectoryWithEval()
  {
    return GlobalProperties.evaluateExpressions(this.loggingDirectory);
  }
  
  public void setLoggingDirectory(String loggingDirectory)
  {
    this.loggingDirectory = loggingDirectory;
  }
  
  public boolean isLoggingToJavaLogger()
  {
    return this.loggingToJavaLogger;
  }
  
  public void setLoggingToJavaLogger(boolean transactionLogToJavaLogger)
  {
    this.loggingToJavaLogger = transactionLogToJavaLogger;
  }
  
  /**
   * @deprecated
   */
  public boolean isUseJuliTransactionLogger()
  {
    return isLoggingToJavaLogger();
  }
  
  /**
   * @deprecated
   */
  public void setUseJuliTransactionLogger(boolean transactionLogToJavaLogger)
  {
    setLoggingToJavaLogger(transactionLogToJavaLogger);
  }
  
  public void setDdlGenerate(boolean ddlGenerate)
  {
    this.ddlGenerate = ddlGenerate;
  }
  
  public void setDdlRun(boolean ddlRun)
  {
    this.ddlRun = ddlRun;
  }
  
  public boolean isDdlGenerate()
  {
    return this.ddlGenerate;
  }
  
  public boolean isDdlRun()
  {
    return this.ddlRun;
  }
  
  public LdapConfig getLdapConfig()
  {
    return this.ldapConfig;
  }
  
  public void setLdapConfig(LdapConfig ldapConfig)
  {
    this.ldapConfig = ldapConfig;
  }
  
  public void addClass(Class<?> cls)
  {
    if (this.classes == null) {
      this.classes = new ArrayList();
    }
    this.classes.add(cls);
  }
  
  public void addPackage(String packageName)
  {
    if (this.packages == null) {
      this.packages = new ArrayList();
    }
    this.packages.add(packageName);
  }
  
  public List<String> getPackages()
  {
    return this.packages;
  }
  
  public void setPackages(List<String> packages)
  {
    this.packages = packages;
  }
  
  public void addJar(String jarName)
  {
    if (this.searchJars == null) {
      this.searchJars = new ArrayList();
    }
    this.searchJars.add(jarName);
  }
  
  public List<String> getJars()
  {
    return this.searchJars;
  }
  
  public void setJars(List<String> searchJars)
  {
    this.searchJars = searchJars;
  }
  
  public void setClasses(List<Class<?>> classes)
  {
    this.classes = classes;
  }
  
  public List<Class<?>> getClasses()
  {
    return this.classes;
  }
  
  public boolean isUpdateChangesOnly()
  {
    return this.updateChangesOnly;
  }
  
  public void setUpdateChangesOnly(boolean updateChangesOnly)
  {
    this.updateChangesOnly = updateChangesOnly;
  }
  
  public void setAllowSubclassing(boolean allowSubclassing)
  {
    this.allowSubclassing = allowSubclassing;
  }
  
  public boolean isAllowSubclassing()
  {
    return this.allowSubclassing;
  }
  
  public String getResourceDirectory()
  {
    return this.resourceDirectory;
  }
  
  public void setResourceDirectory(String resourceDirectory)
  {
    this.resourceDirectory = resourceDirectory;
  }
  
  public void add(BeanQueryAdapter beanQueryAdapter)
  {
    this.queryAdapters.add(beanQueryAdapter);
  }
  
  public List<BeanQueryAdapter> getQueryAdapters()
  {
    return this.queryAdapters;
  }
  
  public void setQueryAdapters(List<BeanQueryAdapter> queryAdapters)
  {
    this.queryAdapters = queryAdapters;
  }
  
  public void add(BeanPersistController beanPersistController)
  {
    this.persistControllers.add(beanPersistController);
  }
  
  public List<BeanPersistController> getPersistControllers()
  {
    return this.persistControllers;
  }
  
  public void setPersistControllers(List<BeanPersistController> persistControllers)
  {
    this.persistControllers = persistControllers;
  }
  
  public void add(TransactionEventListener listener)
  {
    this.transactionEventListeners.add(listener);
  }
  
  public List<TransactionEventListener> getTransactionEventListeners()
  {
    return this.transactionEventListeners;
  }
  
  public void setTransactionEventListeners(List<TransactionEventListener> transactionEventListeners)
  {
    this.transactionEventListeners = transactionEventListeners;
  }
  
  public void add(BeanPersistListener<?> beanPersistListener)
  {
    this.persistListeners.add(beanPersistListener);
  }
  
  public List<BeanPersistListener<?>> getPersistListeners()
  {
    return this.persistListeners;
  }
  
  public void add(BulkTableEventListener bulkTableEventListener)
  {
    this.bulkTableEventListeners.add(bulkTableEventListener);
  }
  
  public List<BulkTableEventListener> getBulkTableEventListeners()
  {
    return this.bulkTableEventListeners;
  }
  
  public void addServerConfigStartup(ServerConfigStartup configStartupListener)
  {
    this.configStartupListeners.add(configStartupListener);
  }
  
  public List<ServerConfigStartup> getServerConfigStartupListeners()
  {
    return this.configStartupListeners;
  }
  
  public void setPersistListeners(List<BeanPersistListener<?>> persistListeners)
  {
    this.persistListeners = persistListeners;
  }
  
  public void loadFromProperties()
  {
    ConfigPropertyMap p = new ConfigPropertyMap(this.name);
    loadSettings(p);
  }
  
  public GlobalProperties.PropertySource getPropertySource()
  {
    return GlobalProperties.getPropertySource(this.name);
  }
  
  public String getProperty(String propertyName, String defaultValue)
  {
    GlobalProperties.PropertySource p = new ConfigPropertyMap(this.name);
    return p.get(propertyName, defaultValue);
  }
  
  public String getProperty(String propertyName)
  {
    return getProperty(propertyName, null);
  }
  
  private <T> T createInstance(GlobalProperties.PropertySource p, Class<T> type, String key)
  {
    String classname = p.get(key, null);
    if (classname == null) {
      return null;
    }
    try
    {
      Class<?> cls = ClassUtil.forName(classname, getClass());
      return (T)cls.newInstance();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  private void loadSettings(ConfigPropertyMap p)
  {
    if (this.autofetchConfig == null) {
      this.autofetchConfig = new AutofetchConfig();
    }
    this.autofetchConfig.loadSettings(p);
    if (this.dataSourceConfig == null) {
      this.dataSourceConfig = new DataSourceConfig();
    }
    this.dataSourceConfig.loadSettings(p.getServerName());
    if (this.ldapConfig == null)
    {
      LdapContextFactory ctxFact = (LdapContextFactory)createInstance(p, LdapContextFactory.class, "ldapContextFactory");
      if (ctxFact != null)
      {
        this.ldapConfig = new LdapConfig();
        this.ldapConfig.setContextFactory(ctxFact);
        this.ldapConfig.setVanillaMode(p.getBoolean("ldapVanillaMode", false));
      }
    }
    this.useJtaTransactionManager = p.getBoolean("useJtaTransactionManager", false);
    this.namingConvention = createNamingConvention(p);
    this.databasePlatform = ((DatabasePlatform)createInstance(p, DatabasePlatform.class, "databasePlatform"));
    this.encryptKeyManager = ((EncryptKeyManager)createInstance(p, EncryptKeyManager.class, "encryptKeyManager"));
    this.encryptDeployManager = ((EncryptDeployManager)createInstance(p, EncryptDeployManager.class, "encryptDeployManager"));
    this.encryptor = ((Encryptor)createInstance(p, Encryptor.class, "encryptor"));
    this.dbEncrypt = ((DbEncrypt)createInstance(p, DbEncrypt.class, "dbEncrypt"));
    this.serverCacheFactory = ((ServerCacheFactory)createInstance(p, ServerCacheFactory.class, "serverCacheFactory"));
    this.serverCacheManager = ((ServerCacheManager)createInstance(p, ServerCacheManager.class, "serverCacheManager"));
    
    String jarsProp = p.get("search.jars", p.get("jars", null));
    if (jarsProp != null) {
      this.searchJars = getSearchJarsPackages(jarsProp);
    }
    String packagesProp = p.get("search.packages", p.get("packages", null));
    if (this.packages != null) {
      this.packages = getSearchJarsPackages(packagesProp);
    }
    this.allowSubclassing = p.getBoolean("allowSubclassing", true);
    this.validateOnSave = p.getBoolean("validateOnSave", true);
    this.vanillaMode = p.getBoolean("vanillaMode", false);
    this.vanillaRefMode = p.getBoolean("vanillaRefMode", false);
    this.updateChangesOnly = p.getBoolean("updateChangesOnly", true);
    
    boolean batchMode = p.getBoolean("batch.mode", false);
    this.persistBatching = p.getBoolean("persistBatching", batchMode);
    
    int batchSize = p.getInt("batch.size", 20);
    this.persistBatchSize = p.getInt("persistBatchSize", batchSize);
    
    this.dataSourceJndiName = p.get("dataSourceJndiName", null);
    this.databaseSequenceBatchSize = p.getInt("databaseSequenceBatchSize", 20);
    this.databaseBooleanTrue = p.get("databaseBooleanTrue", null);
    this.databaseBooleanFalse = p.get("databaseBooleanFalse", null);
    this.databasePlatformName = p.get("databasePlatformName", null);
    
    this.lazyLoadBatchSize = p.getInt("lazyLoadBatchSize", 1);
    this.queryBatchSize = p.getInt("queryBatchSize", 100);
    
    this.ddlGenerate = p.getBoolean("ddl.generate", false);
    this.ddlRun = p.getBoolean("ddl.run", false);
    this.debugSql = p.getBoolean("debug.sql", false);
    this.debugLazyLoad = p.getBoolean("debug.lazyload", false);
    
    this.loggingLevel = getLogLevelValue(p);
    
    String s = p.get("useJuliTransactionLogger", null);
    s = p.get("loggingToJavaLogger", s);
    this.loggingToJavaLogger = "true".equalsIgnoreCase(s);
    
    s = p.get("log.directory", "logs");
    this.loggingDirectory = p.get("logging.directory", s);
    
    this.classes = getClasses(p);
  }
  
  private LogLevel getLogLevelValue(ConfigPropertyMap p)
  {
    String logValue = p.get("logging", "NONE");
    logValue = p.get("log.level", logValue);
    logValue = p.get("logging.level", logValue);
    if (logValue.trim().equalsIgnoreCase("ALL")) {
      logValue = "SQL";
    }
    return (LogLevel)Enum.valueOf(LogLevel.class, logValue.toUpperCase());
  }
  
  private NamingConvention createNamingConvention(GlobalProperties.PropertySource p)
  {
    NamingConvention nc = (NamingConvention)createInstance(p, NamingConvention.class, "namingconvention");
    if (nc == null) {
      return null;
    }
    if ((nc instanceof AbstractNamingConvention))
    {
      AbstractNamingConvention anc = (AbstractNamingConvention)nc;
      String v = p.get("namingConvention.useForeignKeyPrefix", null);
      if (v != null)
      {
        boolean useForeignKeyPrefix = Boolean.valueOf(v).booleanValue();
        anc.setUseForeignKeyPrefix(useForeignKeyPrefix);
      }
      String sequenceFormat = p.get("namingConvention.sequenceFormat", null);
      if (sequenceFormat != null) {
        anc.setSequenceFormat(sequenceFormat);
      }
    }
    return nc;
  }
  
  private ArrayList<Class<?>> getClasses(GlobalProperties.PropertySource p)
  {
    String classNames = p.get("classes", null);
    if (classNames == null) {
      return null;
    }
    ArrayList<Class<?>> classes = new ArrayList();
    
    String[] split = classNames.split("[ ,;]");
    for (int i = 0; i < split.length; i++)
    {
      String cn = split[i].trim();
      if ((cn.length() > 0) && (!"class".equalsIgnoreCase(cn))) {
        try
        {
          classes.add(ClassUtil.forName(cn, getClass()));
        }
        catch (ClassNotFoundException e)
        {
          String msg = "Error registering class [" + cn + "] from [" + classNames + "]";
          throw new RuntimeException(msg, e);
        }
      }
    }
    return classes;
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
}
