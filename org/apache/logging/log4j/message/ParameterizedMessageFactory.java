package org.apache.logging.log4j.message;

public final class ParameterizedMessageFactory
  extends AbstractMessageFactory
{
  public static final ParameterizedMessageFactory INSTANCE = new ParameterizedMessageFactory();
  
  public Message newMessage(String message, Object... params)
  {
    return new ParameterizedMessage(message, params);
  }
}
