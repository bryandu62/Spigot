package org.apache.logging.log4j.message;

public class MessageFormatMessageFactory
  extends AbstractMessageFactory
{
  public Message newMessage(String message, Object... params)
  {
    return new MessageFormatMessage(message, params);
  }
}
