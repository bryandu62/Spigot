package com.avaje.ebean;

public abstract interface SqlQueryListener
{
  public abstract void process(SqlRow paramSqlRow);
}
