package org.apache.logging.log4j.message;

public class FormattedMessageFactory
  extends AbstractMessageFactory
{
  public Message newMessage(String message, Object... params)
  {
    return new FormattedMessage(message, params);
  }
}
