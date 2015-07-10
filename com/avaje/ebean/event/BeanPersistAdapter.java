package com.avaje.ebean.event;

import java.util.Set;

public abstract class BeanPersistAdapter
  implements BeanPersistController
{
  public abstract boolean isRegisterFor(Class<?> paramClass);
  
  public int getExecutionOrder()
  {
    return 10;
  }
  
  public boolean preDelete(BeanPersistRequest<?> request)
  {
    return true;
  }
  
  public boolean preInsert(BeanPersistRequest<?> request)
  {
    return true;
  }
  
  public boolean preUpdate(BeanPersistRequest<?> request)
  {
    return true;
  }
  
  public void postDelete(BeanPersistRequest<?> request) {}
  
  public void postInsert(BeanPersistRequest<?> request) {}
  
  public void postUpdate(BeanPersistRequest<?> request) {}
  
  public void postLoad(Object bean, Set<String> includedProperties) {}
}
