package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.event.BeanPersistListener;
import com.avaje.ebeaninternal.server.core.BootupClasses;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanDescriptor;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class PersistListenerManager
{
  private static final Logger logger = Logger.getLogger(PersistListenerManager.class.getName());
  private final List<BeanPersistListener<?>> list;
  
  public PersistListenerManager(BootupClasses bootupClasses)
  {
    this.list = bootupClasses.getBeanPersistListeners();
  }
  
  public int getRegisterCount()
  {
    return this.list.size();
  }
  
  public <T> void addPersistListeners(DeployBeanDescriptor<T> deployDesc)
  {
    for (int i = 0; i < this.list.size(); i++)
    {
      BeanPersistListener<?> c = (BeanPersistListener)this.list.get(i);
      if (isRegisterFor(deployDesc.getBeanType(), c))
      {
        logger.fine("BeanPersistListener on[" + deployDesc.getFullName() + "] " + c.getClass().getName());
        deployDesc.addPersistListener(c);
      }
    }
  }
  
  public static boolean isRegisterFor(Class<?> beanType, BeanPersistListener<?> c)
  {
    Class<?> listenerEntity = getEntityClass(c.getClass());
    return beanType.equals(listenerEntity);
  }
  
  private static Class<?> getEntityClass(Class<?> controller)
  {
    Class<?> cls = ParamTypeUtil.findParamType(controller, BeanPersistListener.class);
    if (cls == null)
    {
      String msg = "Could not determine the entity class (generics parameter type) from " + controller + " using reflection.";
      
      throw new PersistenceException(msg);
    }
    return cls;
  }
}
