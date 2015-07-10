package com.avaje.ebean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract interface SqlQuery
  extends Serializable
{
  public abstract void cancel();
  
  public abstract List<SqlRow> findList();
  
  public abstract Set<SqlRow> findSet();
  
  public abstract Map<?, SqlRow> findMap();
  
  public abstract SqlRow findUnique();
  
  public abstract SqlFutureList findFutureList();
  
  public abstract SqlQuery setParameter(String paramString, Object paramObject);
  
  public abstract SqlQuery setParameter(int paramInt, Object paramObject);
  
  public abstract SqlQuery setListener(SqlQueryListener paramSqlQueryListener);
  
  public abstract SqlQuery setFirstRow(int paramInt);
  
  public abstract SqlQuery setMaxRows(int paramInt);
  
  public abstract SqlQuery setBackgroundFetchAfter(int paramInt);
  
  public abstract SqlQuery setMapKey(String paramString);
  
  public abstract SqlQuery setTimeout(int paramInt);
  
  public abstract SqlQuery setBufferFetchSizeHint(int paramInt);
}
