package com.avaje.ebean;

import java.beans.PropertyChangeListener;
import java.util.Set;

public abstract interface BeanState
{
  public abstract boolean isReference();
  
  public abstract boolean isNew();
  
  public abstract boolean isNewOrDirty();
  
  public abstract boolean isDirty();
  
  public abstract Set<String> getLoadedProps();
  
  public abstract Set<String> getChangedProps();
  
  public abstract boolean isReadOnly();
  
  public abstract void setReadOnly(boolean paramBoolean);
  
  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void setReference();
  
  public abstract void setLoaded(Set<String> paramSet);
}
