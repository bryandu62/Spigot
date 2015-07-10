package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.config.ExternalTransactionManager;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.ldap.LdapConfig;
import com.avaje.ebean.config.ldap.LdapContextFactory;
import com.avaje.ebean.text.json.JsonContext;
import com.avaje.ebean.text.json.JsonValueAdapter;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.api.SpiBackgroundExecutor;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManagerFactory;
import com.avaje.ebeaninternal.server.cluster.ClusterManager;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import com.avaje.ebeaninternal.server.deploy.DeployOrmXml;
import com.avaje.ebeaninternal.server.deploy.parse.DeployCreateProperties;
import com.avaje.ebeaninternal.server.deploy.parse.DeployInherit;
import com.avaje.ebeaninternal.server.deploy.parse.DeployUtil;
import com.avaje.ebeaninternal.server.expression.DefaultExpressionFactory;
import com.avaje.ebeaninternal.server.jmx.MAdminLogging;
import com.avaje.ebeaninternal.server.persist.Binder;
import com.avaje.ebeaninternal.server.persist.DefaultPersister;
import com.avaje.ebeaninternal.server.query.CQueryEngine;
import com.avaje.ebeaninternal.server.query.DefaultOrmQueryEngine;
import com.avaje.ebeaninternal.server.query.DefaultRelationalQueryEngine;
import com.avaje.ebeaninternal.server.resource.ResourceManager;
import com.avaje.ebeaninternal.server.resource.ResourceManagerFactory;
import com.avaje.ebeaninternal.server.subclass.SubClassManager;
import com.avaje.ebeaninternal.server.text.json.DJsonContext;
import com.avaje.ebeaninternal.server.text.json.DefaultJsonValueAdapter;
import com.avaje.ebeaninternal.server.transaction.DefaultTransactionScopeManager;
import com.avaje.ebeaninternal.server.transaction.ExternalTransactionScopeManager;
import com.avaje.ebeaninternal.server.transaction.JtaTransactionManager;
import com.avaje.ebeaninternal.server.transaction.TransactionManager;
import com.avaje.ebeaninternal.server.transaction.TransactionScopeManager;
import com.avaje.ebeaninternal.server.type.DefaultTypeManager;
import com.avaje.ebeaninternal.server.type.TypeManager;
import java.util.logging.Logger;

public class InternalConfiguration
{
  private static final Logger logger = Logger.getLogger(InternalConfiguration.class.getName());
  private final ServerConfig serverConfig;
  private final BootupClasses bootupClasses;
  private final SubClassManager subClassManager;
  private final DeployInherit deployInherit;
  private final ResourceManager resourceManager;
  private final DeployOrmXml deployOrmXml;
  private final TypeManager typeManager;
  private final Binder binder;
  private final DeployCreateProperties deployCreateProperties;
  private final DeployUtil deployUtil;
  private final BeanDescriptorManager beanDescriptorManager;
  private final MAdminLogging logControl;
  private final DebugLazyLoad debugLazyLoad;
  private final TransactionManager transactionManager;
  private final TransactionScopeManager transactionScopeManager;
  private final CQueryEngine cQueryEngine;
  private final ClusterManager clusterManager;
  private final ServerCacheManager cacheManager;
  private final ExpressionFactory expressionFactory;
  private final SpiBackgroundExecutor backgroundExecutor;
  private final PstmtBatch pstmtBatch;
  private final XmlConfig xmlConfig;
  
  public InternalConfiguration(XmlConfig xmlConfig, ClusterManager clusterManager, ServerCacheManager cacheManager, SpiBackgroundExecutor backgroundExecutor, ServerConfig serverConfig, BootupClasses bootupClasses, PstmtBatch pstmtBatch)
  {
    this.xmlConfig = xmlConfig;
    this.pstmtBatch = pstmtBatch;
    this.clusterManager = clusterManager;
    this.backgroundExecutor = backgroundExecutor;
    this.cacheManager = cacheManager;
    this.serverConfig = serverConfig;
    this.bootupClasses = bootupClasses;
    this.expressionFactory = new DefaultExpressionFactory();
    
    this.subClassManager = new SubClassManager(serverConfig);
    
    this.typeManager = new DefaultTypeManager(serverConfig, bootupClasses);
    this.binder = new Binder(this.typeManager);
    
    this.resourceManager = ResourceManagerFactory.createResourceManager(serverConfig);
    this.deployOrmXml = new DeployOrmXml(this.resourceManager.getResourceSource());
    this.deployInherit = new DeployInherit(bootupClasses);
    
    this.deployCreateProperties = new DeployCreateProperties(this.typeManager);
    this.deployUtil = new DeployUtil(this.typeManager, serverConfig);
    
    this.beanDescriptorManager = new BeanDescriptorManager(this);
    this.beanDescriptorManager.deploy();
    
    this.debugLazyLoad = new DebugLazyLoad(serverConfig.isDebugLazyLoad());
    
    this.transactionManager = new TransactionManager(clusterManager, backgroundExecutor, serverConfig, this.beanDescriptorManager, getBootupClasses());
    
    this.logControl = new MAdminLogging(serverConfig, this.transactionManager);
    
    this.cQueryEngine = new CQueryEngine(serverConfig.getDatabasePlatform(), this.logControl, this.binder, backgroundExecutor);
    
    ExternalTransactionManager externalTransactionManager = serverConfig.getExternalTransactionManager();
    if ((externalTransactionManager == null) && (serverConfig.isUseJtaTransactionManager())) {
      externalTransactionManager = new JtaTransactionManager();
    }
    if (externalTransactionManager != null)
    {
      externalTransactionManager.setTransactionManager(this.transactionManager);
      this.transactionScopeManager = new ExternalTransactionScopeManager(this.transactionManager, externalTransactionManager);
      logger.info("Using Transaction Manager [" + externalTransactionManager.getClass() + "]");
    }
    else
    {
      this.transactionScopeManager = new DefaultTransactionScopeManager(this.transactionManager);
    }
  }
  
  public JsonContext createJsonContext(SpiEbeanServer server)
  {
    String s = this.serverConfig.getProperty("json.pretty", "false");
    boolean dfltPretty = "true".equalsIgnoreCase(s);
    
    s = this.serverConfig.getProperty("json.jsonValueAdapter", null);
    
    JsonValueAdapter va = new DefaultJsonValueAdapter();
    if (s != null) {
      va = (JsonValueAdapter)ClassUtil.newInstance(s, getClass());
    }
    return new DJsonContext(server, va, dfltPretty);
  }
  
  public XmlConfig getXmlConfig()
  {
    return this.xmlConfig;
  }
  
  public AutoFetchManager createAutoFetchManager(SpiEbeanServer server)
  {
    return AutoFetchManagerFactory.create(server, this.serverConfig, this.resourceManager);
  }
  
  public RelationalQueryEngine createRelationalQueryEngine()
  {
    return new DefaultRelationalQueryEngine(this.logControl, this.binder, this.serverConfig.getDatabaseBooleanTrue());
  }
  
  public OrmQueryEngine createOrmQueryEngine()
  {
    return new DefaultOrmQueryEngine(this.beanDescriptorManager, this.cQueryEngine);
  }
  
  public Persister createPersister(SpiEbeanServer server)
  {
    LdapContextFactory ldapCtxFactory = null;
    LdapConfig ldapConfig = this.serverConfig.getLdapConfig();
    if (ldapConfig != null) {
      ldapCtxFactory = ldapConfig.getContextFactory();
    }
    return new DefaultPersister(server, this.serverConfig.isValidateOnSave(), this.binder, this.beanDescriptorManager, this.pstmtBatch, ldapCtxFactory);
  }
  
  public PstmtBatch getPstmtBatch()
  {
    return this.pstmtBatch;
  }
  
  public ServerCacheManager getCacheManager()
  {
    return this.cacheManager;
  }
  
  public BootupClasses getBootupClasses()
  {
    return this.bootupClasses;
  }
  
  public DatabasePlatform getDatabasePlatform()
  {
    return this.serverConfig.getDatabasePlatform();
  }
  
  public ServerConfig getServerConfig()
  {
    return this.serverConfig;
  }
  
  public ExpressionFactory getExpressionFactory()
  {
    return this.expressionFactory;
  }
  
  public TypeManager getTypeManager()
  {
    return this.typeManager;
  }
  
  public Binder getBinder()
  {
    return this.binder;
  }
  
  public BeanDescriptorManager getBeanDescriptorManager()
  {
    return this.beanDescriptorManager;
  }
  
  public SubClassManager getSubClassManager()
  {
    return this.subClassManager;
  }
  
  public DeployInherit getDeployInherit()
  {
    return this.deployInherit;
  }
  
  public ResourceManager getResourceManager()
  {
    return this.resourceManager;
  }
  
  public DeployOrmXml getDeployOrmXml()
  {
    return this.deployOrmXml;
  }
  
  public DeployCreateProperties getDeployCreateProperties()
  {
    return this.deployCreateProperties;
  }
  
  public DeployUtil getDeployUtil()
  {
    return this.deployUtil;
  }
  
  public MAdminLogging getLogControl()
  {
    return this.logControl;
  }
  
  public TransactionManager getTransactionManager()
  {
    return this.transactionManager;
  }
  
  public TransactionScopeManager getTransactionScopeManager()
  {
    return this.transactionScopeManager;
  }
  
  public CQueryEngine getCQueryEngine()
  {
    return this.cQueryEngine;
  }
  
  public ClusterManager getClusterManager()
  {
    return this.clusterManager;
  }
  
  public DebugLazyLoad getDebugLazyLoad()
  {
    return this.debugLazyLoad;
  }
  
  public SpiBackgroundExecutor getBackgroundExecutor()
  {
    return this.backgroundExecutor;
  }
}
