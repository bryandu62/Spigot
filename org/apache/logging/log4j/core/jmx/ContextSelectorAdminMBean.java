package org.apache.logging.log4j.core.jmx;

public abstract interface ContextSelectorAdminMBean
{
  public static final String NAME = "org.apache.logging.log4j2:type=ContextSelector";
  
  public abstract String getImplementationClassName();
}
