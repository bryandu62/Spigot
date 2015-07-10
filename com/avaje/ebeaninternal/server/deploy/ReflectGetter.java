package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.reflect.BeanReflectGetter;
import java.lang.reflect.Method;

public class ReflectGetter
{
  public static BeanReflectGetter create(DeployBeanProperty prop)
  {
    if (!prop.isId()) {
      return new NonIdGetter(prop.getFullBeanName());
    }
    String property = prop.getFullBeanName();
    Method readMethod = prop.getReadMethod();
    if (readMethod == null)
    {
      String m = "Abstract class with no readMethod for " + property;
      throw new RuntimeException(m);
    }
    return new IdGetter(property, readMethod);
  }
  
  public static class IdGetter
    implements BeanReflectGetter
  {
    public static final Object[] NO_ARGS = new Object[0];
    private final Method readMethod;
    private final String property;
    
    public IdGetter(String property, Method readMethod)
    {
      this.property = property;
      this.readMethod = readMethod;
    }
    
    public Object get(Object bean)
    {
      try
      {
        return this.readMethod.invoke(bean, NO_ARGS);
      }
      catch (Exception e)
      {
        String m = "Error on [" + this.property + "] using readMethod " + this.readMethod;
        throw new RuntimeException(m, e);
      }
    }
    
    public Object getIntercept(Object bean)
    {
      return get(bean);
    }
  }
  
  public static class NonIdGetter
    implements BeanReflectGetter
  {
    private final String property;
    
    public NonIdGetter(String property)
    {
      this.property = property;
    }
    
    public Object get(Object bean)
    {
      String m = "Not expecting this method to be called on [" + this.property + "] as it is a NON ID property on an abstract class";
      
      throw new RuntimeException(m);
    }
    
    public Object getIntercept(Object bean)
    {
      return get(bean);
    }
  }
}
