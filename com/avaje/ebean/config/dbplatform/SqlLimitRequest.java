package com.avaje.ebean.config.dbplatform;

import com.avaje.ebeaninternal.api.SpiQuery;

public abstract interface SqlLimitRequest
{
  public abstract boolean isDistinct();
  
  public abstract int getFirstRow();
  
  public abstract int getMaxRows();
  
  public abstract String getDbSql();
  
  public abstract String getDbOrderBy();
  
  public abstract SpiQuery<?> getOrmQuery();
  
  public abstract DatabasePlatform getDbPlatform();
}
