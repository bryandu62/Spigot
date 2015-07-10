package org.apache.logging.log4j.message;

import java.io.Serializable;

public abstract interface Message
  extends Serializable
{
  public abstract String getFormattedMessage();
  
  public abstract String getFormat();
  
  public abstract Object[] getParameters();
  
  public abstract Throwable getThrowable();
}
