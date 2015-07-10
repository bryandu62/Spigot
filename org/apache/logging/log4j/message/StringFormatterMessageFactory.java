package org.apache.logging.log4j.message;

public final class StringFormatterMessageFactory
  extends AbstractMessageFactory
{
  public static final StringFormatterMessageFactory INSTANCE = new StringFormatterMessageFactory();
  
  public Message newMessage(String message, Object... params)
  {
    return new StringFormattedMessage(message, params);
  }
}
