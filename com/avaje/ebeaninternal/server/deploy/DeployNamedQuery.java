package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.RawSql;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

public class DeployNamedQuery
{
  private final String name;
  private final String query;
  private final QueryHint[] hints;
  private final DRawSqlSelect sqlSelect;
  private final RawSql rawSql;
  
  public DeployNamedQuery(NamedQuery namedQuery)
  {
    this.name = namedQuery.name();
    this.query = namedQuery.query();
    this.hints = namedQuery.hints();
    this.sqlSelect = null;
    this.rawSql = null;
  }
  
  public DeployNamedQuery(String name, String query, QueryHint[] hints)
  {
    this.name = name;
    this.query = query;
    this.hints = hints;
    this.sqlSelect = null;
    this.rawSql = null;
  }
  
  public DeployNamedQuery(String name, RawSql rawSql)
  {
    this.name = name;
    this.query = null;
    this.hints = null;
    this.sqlSelect = null;
    this.rawSql = rawSql;
  }
  
  public DeployNamedQuery(DRawSqlSelect sqlSelect)
  {
    this.name = sqlSelect.getName();
    this.query = null;
    this.hints = null;
    this.sqlSelect = sqlSelect;
    this.rawSql = null;
  }
  
  public boolean isRawSql()
  {
    return this.rawSql != null;
  }
  
  public boolean isSqlSelect()
  {
    return this.sqlSelect != null;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getQuery()
  {
    return this.query;
  }
  
  public QueryHint[] getHints()
  {
    return this.hints;
  }
  
  public RawSql getRawSql()
  {
    return this.rawSql;
  }
  
  public DRawSqlSelect getSqlSelect()
  {
    return this.sqlSelect;
  }
}
