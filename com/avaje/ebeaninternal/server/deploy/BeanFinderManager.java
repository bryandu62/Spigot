package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.event.BeanFinder;
import java.util.List;

public abstract interface BeanFinderManager
{
  public abstract int getRegisterCount();
  
  public abstract int createBeanFinders(List<Class<?>> paramList);
  
  public abstract <T> BeanFinder<T> getBeanFinder(Class<T> paramClass);
}
