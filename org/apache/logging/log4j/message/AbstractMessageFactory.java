package org.apache.logging.log4j.message;

public abstract class AbstractMessageFactory
  implements MessageFactory
{
  public Message newMessage(Object message)
  {
    return new ObjectMessage(message);
  }
  
  public Message newMessage(String message)
  {
    return new SimpleMessage(message);
  }
  
  public abstract Message newMessage(String paramString, Object... paramVarArgs);
}
