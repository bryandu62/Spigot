package com.avaje.ebeaninternal.server.persist.dmlbind;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;

public abstract interface BindableId
  extends Bindable
{
  public abstract boolean isEmpty();
  
  public abstract boolean isConcatenated();
  
  public abstract String getIdentityColumn();
  
  public abstract boolean deriveConcatenatedId(PersistRequestBean<?> paramPersistRequestBean);
}
