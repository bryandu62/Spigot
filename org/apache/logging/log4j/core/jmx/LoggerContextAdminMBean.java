package org.apache.logging.log4j.core.jmx;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public abstract interface LoggerContextAdminMBean
{
  public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s";
  public static final String NOTIF_TYPE_RECONFIGURED = "com.apache.logging.log4j.core.jmx.config.reconfigured";
  
  public abstract String getStatus();
  
  public abstract String getName();
  
  public abstract String getConfigLocationURI();
  
  public abstract void setConfigLocationURI(String paramString)
    throws URISyntaxException, IOException;
  
  public abstract String getConfigText()
    throws IOException;
  
  public abstract String getConfigText(String paramString)
    throws IOException;
  
  public abstract void setConfigText(String paramString1, String paramString2);
  
  public abstract String getConfigName();
  
  public abstract String getConfigClassName();
  
  public abstract String getConfigFilter();
  
  public abstract String getConfigMonitorClassName();
  
  public abstract Map<String, String> getConfigProperties();
}
