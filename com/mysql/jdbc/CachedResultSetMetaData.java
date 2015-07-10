package com.mysql.jdbc;

import java.sql.ResultSetMetaData;
import java.util.Map;

public class CachedResultSetMetaData
{
  Map columnNameToIndex = null;
  Field[] fields;
  Map fullColumnNameToIndex = null;
  ResultSetMetaData metadata;
  
  public Map getColumnNameToIndex()
  {
    return this.columnNameToIndex;
  }
  
  public Field[] getFields()
  {
    return this.fields;
  }
  
  public Map getFullColumnNameToIndex()
  {
    return this.fullColumnNameToIndex;
  }
  
  public ResultSetMetaData getMetadata()
  {
    return this.metadata;
  }
}
