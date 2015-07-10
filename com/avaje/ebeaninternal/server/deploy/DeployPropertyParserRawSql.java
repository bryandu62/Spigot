package com.avaje.ebeaninternal.server.deploy;

import java.util.Set;

public final class DeployPropertyParserRawSql
  extends DeployParser
{
  private final DRawSqlSelect rawSqlSelect;
  
  public DeployPropertyParserRawSql(DRawSqlSelect rawSqlSelect)
  {
    this.rawSqlSelect = rawSqlSelect;
  }
  
  public Set<String> getIncludes()
  {
    return null;
  }
  
  public String convertWord()
  {
    String r = getDeployWord(this.word);
    return r == null ? this.word : r;
  }
  
  public String getDeployWord(String expression)
  {
    DRawSqlColumnInfo columnInfo = this.rawSqlSelect.getRawSqlColumnInfo(expression);
    if (columnInfo == null) {
      return null;
    }
    return columnInfo.getName();
  }
}
