package com.avaje.ebean.event;

import java.util.Set;

public abstract interface BulkTableEventListener
{
  public abstract Set<String> registeredTables();
  
  public abstract void process(BulkTableEvent paramBulkTableEvent);
}
