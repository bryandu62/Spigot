package com.avaje.ebean.bean;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

public abstract interface EntityBean
  extends Serializable
{
  public abstract String _ebean_getMarker();
  
  public abstract Object _ebean_newInstance();
  
  public abstract void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener);
  
  public abstract void _ebean_setEmbeddedLoaded();
  
  public abstract boolean _ebean_isEmbeddedNewOrDirty();
  
  public abstract EntityBeanIntercept _ebean_getIntercept();
  
  public abstract EntityBeanIntercept _ebean_intercept();
  
  public abstract Object _ebean_createCopy();
  
  public abstract String[] _ebean_getFieldNames();
  
  public abstract void _ebean_setField(int paramInt, Object paramObject1, Object paramObject2);
  
  public abstract void _ebean_setFieldIntercept(int paramInt, Object paramObject1, Object paramObject2);
  
  public abstract Object _ebean_getField(int paramInt, Object paramObject);
  
  public abstract Object _ebean_getFieldIntercept(int paramInt, Object paramObject);
}
