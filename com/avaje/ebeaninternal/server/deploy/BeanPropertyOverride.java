package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.core.InternString;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;

public class BeanPropertyOverride
{
  private final String dbColumn;
  private final String sqlFormulaSelect;
  private final String sqlFormulaJoin;
  
  public BeanPropertyOverride(String dbColumn)
  {
    this(dbColumn, null, null);
  }
  
  public BeanPropertyOverride(String dbColumn, String sqlFormulaSelect, String sqlFormulaJoin)
  {
    this.dbColumn = InternString.intern(dbColumn);
    this.sqlFormulaSelect = InternString.intern(sqlFormulaSelect);
    this.sqlFormulaJoin = InternString.intern(sqlFormulaJoin);
  }
  
  public String getDbColumn()
  {
    return this.dbColumn;
  }
  
  public String getSqlFormulaSelect()
  {
    return this.sqlFormulaSelect;
  }
  
  public String getSqlFormulaJoin()
  {
    return this.sqlFormulaJoin;
  }
  
  public String replace(String src, String srcDbColumn)
  {
    return StringHelper.replaceString(src, srcDbColumn, this.dbColumn);
  }
}
