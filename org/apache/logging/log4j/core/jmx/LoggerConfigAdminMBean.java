package org.apache.logging.log4j.core.jmx;

public abstract interface LoggerConfigAdminMBean
{
  public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s";
  
  public abstract String getName();
  
  public abstract String getLevel();
  
  public abstract void setLevel(String paramString);
  
  public abstract boolean isAdditive();
  
  public abstract void setAdditive(boolean paramBoolean);
  
  public abstract boolean isIncludeLocation();
  
  public abstract String getFilter();
  
  public abstract String[] getAppenderRefs();
}
