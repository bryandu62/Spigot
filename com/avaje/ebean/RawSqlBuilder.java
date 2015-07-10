package com.avaje.ebean;

public class RawSqlBuilder
{
  public static final String IGNORE_COLUMN = "$$_IGNORE_COLUMN_$$";
  private final RawSql.Sql sql;
  private final RawSql.ColumnMapping columnMapping;
  
  public static RawSqlBuilder unparsed(String sql)
  {
    RawSql.Sql s = new RawSql.Sql(sql);
    return new RawSqlBuilder(s, new RawSql.ColumnMapping());
  }
  
  public static RawSqlBuilder parse(String sql)
  {
    RawSql.Sql sql2 = DRawSqlParser.parse(sql);
    String select = sql2.getPreFrom();
    
    RawSql.ColumnMapping mapping = DRawSqlColumnsParser.parse(select);
    return new RawSqlBuilder(sql2, mapping);
  }
  
  private RawSqlBuilder(RawSql.Sql sql, RawSql.ColumnMapping columnMapping)
  {
    this.sql = sql;
    this.columnMapping = columnMapping;
  }
  
  public RawSqlBuilder columnMapping(String dbColumn, String propertyName)
  {
    this.columnMapping.columnMapping(dbColumn, propertyName);
    return this;
  }
  
  public RawSqlBuilder columnMappingIgnore(String dbColumn)
  {
    return columnMapping(dbColumn, "$$_IGNORE_COLUMN_$$");
  }
  
  public RawSql create()
  {
    return new RawSql(this.sql, this.columnMapping.createImmutableCopy());
  }
  
  protected RawSql.Sql getSql()
  {
    return this.sql;
  }
}
