package com.avaje.ebeaninternal.api;

import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlQueryListener;
import java.sql.PreparedStatement;

public abstract interface SpiSqlQuery
  extends SqlQuery
{
  public abstract BindParams getBindParams();
  
  public abstract String getQuery();
  
  public abstract SqlQueryListener getListener();
  
  public abstract int getFirstRow();
  
  public abstract int getMaxRows();
  
  public abstract int getBackgroundFetchAfter();
  
  public abstract String getMapKey();
  
  public abstract int getTimeout();
  
  public abstract int getBufferFetchSizeHint();
  
  public abstract boolean isFutureFetch();
  
  public abstract void setFutureFetch(boolean paramBoolean);
  
  public abstract void setPreparedStatement(PreparedStatement paramPreparedStatement);
  
  public abstract boolean isCancelled();
}
