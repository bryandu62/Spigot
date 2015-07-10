package com.avaje.ebean.event;

public abstract interface BulkTableEvent
{
  public abstract String getTableName();
  
  public abstract boolean isInsert();
  
  public abstract boolean isUpdate();
  
  public abstract boolean isDelete();
}
