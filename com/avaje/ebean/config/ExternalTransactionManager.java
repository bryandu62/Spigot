package com.avaje.ebean.config;

public abstract interface ExternalTransactionManager
{
  public abstract void setTransactionManager(Object paramObject);
  
  public abstract Object getCurrentTransaction();
}
