package org.apache.logging.log4j.core.jmx;

import java.util.List;
import org.apache.logging.log4j.status.StatusData;

public abstract interface StatusLoggerAdminMBean
{
  public static final String NAME = "org.apache.logging.log4j2:type=StatusLogger";
  public static final String NOTIF_TYPE_DATA = "com.apache.logging.log4j.core.jmx.statuslogger.data";
  public static final String NOTIF_TYPE_MESSAGE = "com.apache.logging.log4j.core.jmx.statuslogger.message";
  
  public abstract List<StatusData> getStatusData();
  
  public abstract String[] getStatusDataHistory();
  
  public abstract String getLevel();
  
  public abstract void setLevel(String paramString);
}
