package com.avaje.ebean.config;

import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import java.util.Map;

public class DataSourceConfig
{
  private String url;
  private String username;
  private String password;
  private String driver;
  private int minConnections = 2;
  private int maxConnections = 20;
  private int isolationLevel = 2;
  private String heartbeatSql;
  private boolean captureStackTrace;
  private int maxStackTraceSize = 5;
  private int leakTimeMinutes = 30;
  private int maxInactiveTimeSecs = 900;
  private int pstmtCacheSize = 20;
  private int cstmtCacheSize = 20;
  private int waitTimeoutMillis = 1000;
  private String poolListener;
  private boolean offline;
  Map<String, String> customProperties;
  
  public String getUrl()
  {
    return this.url;
  }
  
  public void setUrl(String url)
  {
    this.url = url;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public String getDriver()
  {
    return this.driver;
  }
  
  public void setDriver(String driver)
  {
    this.driver = driver;
  }
  
  public int getIsolationLevel()
  {
    return this.isolationLevel;
  }
  
  public void setIsolationLevel(int isolationLevel)
  {
    this.isolationLevel = isolationLevel;
  }
  
  public int getMinConnections()
  {
    return this.minConnections;
  }
  
  public void setMinConnections(int minConnections)
  {
    this.minConnections = minConnections;
  }
  
  public int getMaxConnections()
  {
    return this.maxConnections;
  }
  
  public void setMaxConnections(int maxConnections)
  {
    this.maxConnections = maxConnections;
  }
  
  public String getHeartbeatSql()
  {
    return this.heartbeatSql;
  }
  
  public void setHeartbeatSql(String heartbeatSql)
  {
    this.heartbeatSql = heartbeatSql;
  }
  
  public boolean isCaptureStackTrace()
  {
    return this.captureStackTrace;
  }
  
  public void setCaptureStackTrace(boolean captureStackTrace)
  {
    this.captureStackTrace = captureStackTrace;
  }
  
  public int getMaxStackTraceSize()
  {
    return this.maxStackTraceSize;
  }
  
  public void setMaxStackTraceSize(int maxStackTraceSize)
  {
    this.maxStackTraceSize = maxStackTraceSize;
  }
  
  public int getLeakTimeMinutes()
  {
    return this.leakTimeMinutes;
  }
  
  public void setLeakTimeMinutes(int leakTimeMinutes)
  {
    this.leakTimeMinutes = leakTimeMinutes;
  }
  
  public int getPstmtCacheSize()
  {
    return this.pstmtCacheSize;
  }
  
  public void setPstmtCacheSize(int pstmtCacheSize)
  {
    this.pstmtCacheSize = pstmtCacheSize;
  }
  
  public int getCstmtCacheSize()
  {
    return this.cstmtCacheSize;
  }
  
  public void setCstmtCacheSize(int cstmtCacheSize)
  {
    this.cstmtCacheSize = cstmtCacheSize;
  }
  
  public int getWaitTimeoutMillis()
  {
    return this.waitTimeoutMillis;
  }
  
  public void setWaitTimeoutMillis(int waitTimeoutMillis)
  {
    this.waitTimeoutMillis = waitTimeoutMillis;
  }
  
  public int getMaxInactiveTimeSecs()
  {
    return this.maxInactiveTimeSecs;
  }
  
  public void setMaxInactiveTimeSecs(int maxInactiveTimeSecs)
  {
    this.maxInactiveTimeSecs = maxInactiveTimeSecs;
  }
  
  public String getPoolListener()
  {
    return this.poolListener;
  }
  
  public void setPoolListener(String poolListener)
  {
    this.poolListener = poolListener;
  }
  
  public boolean isOffline()
  {
    return this.offline;
  }
  
  public void setOffline(boolean offline)
  {
    this.offline = offline;
  }
  
  public Map<String, String> getCustomProperties()
  {
    return this.customProperties;
  }
  
  public void setCustomProperties(Map<String, String> customProperties)
  {
    this.customProperties = customProperties;
  }
  
  public void loadSettings(String serverName)
  {
    String prefix = "datasource." + serverName + ".";
    
    this.username = GlobalProperties.get(prefix + "username", null);
    this.password = GlobalProperties.get(prefix + "password", null);
    
    String v = GlobalProperties.get(prefix + "databaseDriver", null);
    this.driver = GlobalProperties.get(prefix + "driver", v);
    
    v = GlobalProperties.get(prefix + "databaseUrl", null);
    this.url = GlobalProperties.get(prefix + "url", v);
    
    this.captureStackTrace = GlobalProperties.getBoolean(prefix + "captureStackTrace", false);
    this.maxStackTraceSize = GlobalProperties.getInt(prefix + "maxStackTraceSize", 5);
    this.leakTimeMinutes = GlobalProperties.getInt(prefix + "leakTimeMinutes", 30);
    this.maxInactiveTimeSecs = GlobalProperties.getInt(prefix + "maxInactiveTimeSecs", 900);
    
    this.minConnections = GlobalProperties.getInt(prefix + "minConnections", 0);
    this.maxConnections = GlobalProperties.getInt(prefix + "maxConnections", 20);
    this.pstmtCacheSize = GlobalProperties.getInt(prefix + "pstmtCacheSize", 20);
    this.cstmtCacheSize = GlobalProperties.getInt(prefix + "cstmtCacheSize", 20);
    
    this.waitTimeoutMillis = GlobalProperties.getInt(prefix + "waitTimeout", 1000);
    
    this.heartbeatSql = GlobalProperties.get(prefix + "heartbeatSql", null);
    this.poolListener = GlobalProperties.get(prefix + "poolListener", null);
    this.offline = GlobalProperties.getBoolean(prefix + "offline", false);
    
    String isoLevel = GlobalProperties.get(prefix + "isolationlevel", "READ_COMMITTED");
    this.isolationLevel = TransactionIsolation.getLevel(isoLevel);
    
    String customProperties = GlobalProperties.get(prefix + "customProperties", null);
    if ((customProperties != null) && (customProperties.length() > 0))
    {
      Map<String, String> custProps = StringHelper.delimitedToMap(customProperties, ";", "=");
      this.customProperties = custProps;
    }
  }
}
