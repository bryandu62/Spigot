package com.avaje.ebeaninternal.server.jmx;

import com.avaje.ebean.AdminLogging;
import com.avaje.ebean.LogLevel;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.server.transaction.TransactionManager;

public class MAdminLogging
  implements MAdminLoggingMBean, AdminLogging
{
  private final TransactionManager transactionManager;
  private boolean debugSql;
  private boolean debugLazyLoad;
  
  public MAdminLogging(ServerConfig serverConfig, TransactionManager txManager)
  {
    this.transactionManager = txManager;
    this.debugSql = serverConfig.isDebugSql();
    this.debugLazyLoad = serverConfig.isDebugLazyLoad();
  }
  
  public void setLogLevel(LogLevel logLevel)
  {
    this.transactionManager.setTransactionLogLevel(logLevel);
  }
  
  public LogLevel getLogLevel()
  {
    return this.transactionManager.getTransactionLogLevel();
  }
  
  public boolean isDebugGeneratedSql()
  {
    return this.debugSql;
  }
  
  public void setDebugGeneratedSql(boolean debugSql)
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
}
