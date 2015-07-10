package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.BeanState;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Set;

public class DefaultBeanState
  implements BeanState
{
  final EntityBean entityBean;
  final EntityBeanIntercept intercept;
  
  public DefaultBeanState(EntityBean entityBean)
  {
    this.entityBean = entityBean;
    this.intercept = entityBean._ebean_getIntercept();
  }
  
  public boolean isReference()
  {
    return this.intercept.isReference();
  }
  
  public boolean isNew()
  {
    return this.intercept.isNew();
  }
  
  public boolean isNewOrDirty()
  {
    return this.intercept.isNewOrDirty();
  }
  
  public boolean isDirty()
  {
    return this.intercept.isDirty();
  }
  
  public Set<String> getLoadedProps()
  {
    Set<String> props = this.intercept.getLoadedProps();
    return props == null ? null : Collections.unmodifiableSet(props);
  }
  
  public Set<String> getChangedProps()
  {
    Set<String> props = this.intercept.getChangedProps();
    return props == null ? null : Collections.unmodifiableSet(props);
  }
  
  public boolean isReadOnly()
  {
    return this.intercept.isReadOnly();
  }
  
  public void setReadOnly(boolean readOnly)
  {
    this.intercept.setReadOnly(readOnly);
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    this.entityBean.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    this.entityBean.removePropertyChangeListener(listener);
  }
  
  public void setLoaded(Set<String> loadedProperties)
  {
    this.intercept.setLoadedProps(loadedProperties);
    this.intercept.setLoaded();
  }
  
  public void setReference()
  {
    this.intercept.setReference();
  }
}
