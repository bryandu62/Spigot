package com.avaje.ebeaninternal.api;

import com.avaje.ebeaninternal.server.core.OrmQueryRequest;

public abstract interface LoadSecondaryQuery
{
  public abstract void loadSecondaryQuery(OrmQueryRequest<?> paramOrmQueryRequest, int paramInt, boolean paramBoolean);
}
