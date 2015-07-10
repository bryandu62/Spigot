package com.avaje.ebeaninternal.server.lib.util;

public abstract interface MailListener
{
  public abstract void handleEvent(MailEvent paramMailEvent);
}
