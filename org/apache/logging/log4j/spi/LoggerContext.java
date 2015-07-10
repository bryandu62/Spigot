package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;

public abstract interface LoggerContext
{
  public abstract Object getExternalContext();
  
  public abstract Logger getLogger(String paramString);
  
  public abstract Logger getLogger(String paramString, MessageFactory paramMessageFactory);
  
  public abstract boolean hasLogger(String paramString);
}
