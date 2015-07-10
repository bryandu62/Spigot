package com.avaje.ebeaninternal.server.lib.util;

public class MailEvent
{
  Throwable error;
  MailMessage message;
  
  public MailEvent(MailMessage message, Throwable error)
  {
    this.message = message;
    this.error = error;
  }
  
  public MailMessage getMailMessage()
  {
    return this.message;
  }
  
  public boolean wasSuccessful()
  {
    return this.error == null;
  }
  
  public Throwable getError()
  {
    return this.error;
  }
}
