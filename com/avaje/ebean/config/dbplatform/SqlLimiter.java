package com.avaje.ebean.config.dbplatform;

public abstract interface SqlLimiter
{
  public static final char NEW_LINE = '\n';
  public static final char CARRIAGE_RETURN = '\r';
  
  public abstract SqlLimitResponse limit(SqlLimitRequest paramSqlLimitRequest);
}
