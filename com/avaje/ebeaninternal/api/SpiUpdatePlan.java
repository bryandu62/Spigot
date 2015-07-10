package com.avaje.ebeaninternal.api;

import com.avaje.ebeaninternal.server.core.ConcurrencyMode;
import com.avaje.ebeaninternal.server.persist.dml.DmlHandler;
import com.avaje.ebeaninternal.server.persist.dmlbind.Bindable;
import java.sql.SQLException;
import java.util.Set;

public abstract interface SpiUpdatePlan
{
  public abstract boolean isEmptySetClause();
  
  public abstract void bindSet(DmlHandler paramDmlHandler, Object paramObject)
    throws SQLException;
  
  public abstract long getTimeCreated();
  
  public abstract Long getTimeLastUsed();
  
  public abstract Integer getKey();
  
  public abstract ConcurrencyMode getMode();
  
  public abstract String getSql();
  
  public abstract Bindable getSet();
  
  public abstract Set<String> getProperties();
}
