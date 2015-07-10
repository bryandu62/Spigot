package org.apache.logging.log4j.core.jmx;

public abstract interface AppenderAdminMBean
{
  public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s";
  
  public abstract String getName();
  
  public abstract String getLayout();
  
  public abstract boolean isExceptionSuppressed();
  
  public abstract String getErrorHandler();
}
