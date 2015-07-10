package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.event.BeanFinder;
import java.util.HashMap;
import java.util.List;
import javax.persistence.PersistenceException;

public class DefaultBeanFinderManager
  implements BeanFinderManager
{
  HashMap<Class<?>, BeanFinder<?>> registerFor = new HashMap();
  
  public int createBeanFinders(List<Class<?>> finderClassList)
  {
    for (Class<?> cls : finderClassList)
    {
      Class<?> entityType = getEntityClass(cls);
      try
      {
        BeanFinder<?> beanFinder = (BeanFinder)cls.newInstance();
        this.registerFor.put(entityType, beanFinder);
      }
      catch (Exception ex)
      {
        throw new PersistenceException(ex);
      }
    }
    return this.registerFor.size();
  }
  
  public int getRegisterCount()
  {
    return this.registerFor.size();
  }
  
  public <T> BeanFinder<T> getBeanFinder(Class<T> entityType)
  {
    return (BeanFinder)this.registerFor.get(entityType);
  }
  
  private Class<?> getEntityClass(Class<?> controller)
  {
    Class<?> cls = ParamTypeUtil.findParamType(controller, BeanFinder.class);
    if (cls == null)
    {
      String msg = "Could not determine the entity class (generics parameter type) from " + controller + " using reflection.";
      throw new PersistenceException(msg);
    }
    return cls;
  }
}
