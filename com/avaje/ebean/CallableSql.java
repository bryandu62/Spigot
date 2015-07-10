package com.avaje.ebean;

import java.sql.CallableStatement;
import java.sql.SQLException;

public abstract interface CallableSql
{
  public abstract String getLabel();
  
  public abstract CallableSql setLabel(String paramString);
  
  public abstract int getTimeout();
  
  public abstract String getSql();
  
  public abstract CallableSql setTimeout(int paramInt);
  
  public abstract CallableSql setSql(String paramString);
  
  public abstract CallableSql bind(int paramInt, Object paramObject);
  
  public abstract CallableSql setParameter(int paramInt, Object paramObject);
  
  public abstract CallableSql registerOut(int paramInt1, int paramInt2);
  
  public abstract Object getObject(int paramInt);
  
  public abstract boolean executeOverride(CallableStatement paramCallableStatement)
    throws SQLException;
  
  public abstract CallableSql addModification(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
}
