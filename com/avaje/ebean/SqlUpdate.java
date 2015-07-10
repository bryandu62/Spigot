package com.avaje.ebean;

public abstract interface SqlUpdate
{
  public abstract int execute();
  
  public abstract boolean isAutoTableMod();
  
  public abstract SqlUpdate setAutoTableMod(boolean paramBoolean);
  
  public abstract String getLabel();
  
  public abstract SqlUpdate setLabel(String paramString);
  
  public abstract String getSql();
  
  public abstract int getTimeout();
  
  public abstract SqlUpdate setTimeout(int paramInt);
  
  public abstract SqlUpdate setParameter(int paramInt, Object paramObject);
  
  public abstract SqlUpdate setNull(int paramInt1, int paramInt2);
  
  public abstract SqlUpdate setNullParameter(int paramInt1, int paramInt2);
  
  public abstract SqlUpdate setParameter(String paramString, Object paramObject);
  
  public abstract SqlUpdate setNull(String paramString, int paramInt);
  
  public abstract SqlUpdate setNullParameter(String paramString, int paramInt);
}
