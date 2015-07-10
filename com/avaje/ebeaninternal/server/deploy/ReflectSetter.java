package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.reflect.BeanReflectSetter;
import java.lang.reflect.Method;

public class ReflectSetter
{
  public static BeanReflectSetter create(DeployBeanProperty prop)
  {
    String fullName = prop.getFullBeanName();
    Method writeMethod = prop.getWriteMethod();
    return new RefCalled(fullName, writeMethod);
  }
  
  static class RefCalled
    implements BeanReflectSetter
  {
    final String fullName;
    final Method writeMethod;
    
    RefCalled(String fullName, Method writeMethod)
    {
      this.fullName = fullName;
      this.writeMethod = writeMethod;
    }
    
    public void set(Object bean, Object value)
    {
      Object[] a = new Object[1];
      a[0] = value;
      try
      {
        this.writeMethod.invoke(bean, a);
      }
      catch (Exception e)
      {
        String beanType = bean == null ? "null" : bean.getClass().toString();
        String msg = "Error setting value on " + this.fullName + " value[" + value + "] on type[" + beanType + "]";
        throw new RuntimeException(msg, e);
      }
    }
    
    public void setIntercept(Object bean, Object value)
    {
      String msg = "Not expecting setIntercept to be called. Refer Bug 368";
      throw new RuntimeException(msg);
    }
  }
}
