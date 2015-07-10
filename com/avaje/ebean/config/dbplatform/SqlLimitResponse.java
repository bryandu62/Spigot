package com.avaje.ebean.config.dbplatform;

public class SqlLimitResponse
{
  final String sql;
  final boolean includesRowNumberColumn;
  
  public SqlLimitResponse(String sql, boolean includesRowNumberColumn)
  {
    this.sql = sql;
    this.includesRowNumberColumn = includesRowNumberColumn;
  }
  
  public String getSql()
  {
    return this.sql;
  }
  
  public boolean isIncludesRowNumberColumn()
  {
    return this.includesRowNumberColumn;
  }
}
