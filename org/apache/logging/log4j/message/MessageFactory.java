package org.apache.logging.log4j.message;

public abstract interface MessageFactory
{
  public abstract Message newMessage(Object paramObject);
  
  public abstract Message newMessage(String paramString);
  
  public abstract Message newMessage(String paramString, Object... paramVarArgs);
}
