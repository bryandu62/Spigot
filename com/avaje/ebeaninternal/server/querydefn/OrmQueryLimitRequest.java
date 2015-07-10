package com.avaje.ebeaninternal.server.querydefn;

import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.SqlLimitRequest;
import com.avaje.ebeaninternal.api.SpiQuery;

public class OrmQueryLimitRequest
  implements SqlLimitRequest
{
  final SpiQuery<?> ormQuery;
  final DatabasePlatform dbPlatform;
  final String sql;
  final String sqlOrderBy;
  
  public OrmQueryLimitRequest(String sql, String sqlOrderBy, SpiQuery<?> ormQuery, DatabasePlatform dbPlatform)
  {
    this.sql = sql;
    this.sqlOrderBy = sqlOrderBy;
    this.ormQuery = ormQuery;
    this.dbPlatform = dbPlatform;
  }
  
  public String getDbOrderBy()
  {
    return this.sqlOrderBy;
  }
  
  public String getDbSql()
  {
    return this.sql;
  }
  
  public int getFirstRow()
  {
    return this.ormQuery.getFirstRow();
  }
  
  public int getMaxRows()
  {
    return this.ormQuery.getMaxRows();
  }
  
  public boolean isDistinct()
  {
    return this.ormQuery.isDistinct();
  }
  
  public SpiQuery<?> getOrmQuery()
  {
    return this.ormQuery;
  }
  
  public DatabasePlatform getDbPlatform()
  {
    return this.dbPlatform;
  }
}
