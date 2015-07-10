package com.avaje.ebeaninternal.api;

import com.avaje.ebean.SqlUpdate;

public abstract interface SpiSqlUpdate
  extends SqlUpdate
{
  public abstract BindParams getBindParams();
}
