package com.avaje.ebeaninternal.server.reflect;

import com.avaje.ebean.bean.EntityBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import javax.persistence.PersistenceException;

public final class EnhanceBeanReflect
  implements BeanReflect
{
  private static final Object[] constuctorArgs = new Object[0];
  private final Class<?> clazz;
  private final EntityBean entityBean;
  private final Constructor<?> constructor;
  private final Constructor<?> vanillaConstructor;
  private final boolean hasNewInstanceMethod;
  private final boolean vanillaOnly;
  
  public EnhanceBeanReflect(Class<?> vanillaType, Class<?> clazz)
  {
    try
    {
      this.clazz = clazz;
      if (Modifier.isAbstract(clazz.getModifiers()))
      {
        this.entityBean = null;
        this.constructor = null;
        this.vanillaConstructor = null;
        this.hasNewInstanceMethod = false;
        this.vanillaOnly = false;
      }
      else
      {
        this.vanillaConstructor = defaultConstructor(vanillaType);
        this.constructor = defaultConstructor(clazz);
        
        Object newInstance = clazz.newInstance();
        if ((newInstance instanceof EntityBean))
        {
          this.entityBean = ((EntityBean)newInstance);
          this.vanillaOnly = false;
          this.hasNewInstanceMethod = hasNewInstanceMethod(clazz);
        }
        else
        {
          this.entityBean = null;
          this.vanillaOnly = true;
          this.hasNewInstanceMethod = false;
        }
      }
    }
    catch (InstantiationException e)
    {
      throw new PersistenceException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  private Constructor<?> defaultConstructor(Class<?> cls)
  {
    try
    {
      Class<?>[] params = new Class[0];
      return cls.getDeclaredConstructor(params);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  private boolean hasNewInstanceMethod(Class<?> clazz)
  {
    Class<?>[] params = new Class[0];
    try
    {
      Method method = clazz.getMethod("_ebean_newInstance", params);
      if (method == null) {
        return false;
      }
      try
      {
        Object o = this.constructor.newInstance(constuctorArgs);
        method.invoke(o, new Object[0]);
        return true;
      }
      catch (AbstractMethodError e)
      {
        return false;
      }
      catch (InvocationTargetException e)
      {
        return false;
      }
      catch (Exception e)
      {
        throw new RuntimeException("Unexpected? ", e);
      }
      return false;
    }
    catch (SecurityException e)
    {
      return false;
    }
    catch (NoSuchMethodException e) {}
  }
  
  public boolean isVanillaOnly()
  {
    return this.vanillaOnly;
  }
  
  public Object createEntityBean()
  {
    if (this.hasNewInstanceMethod) {
      return this.entityBean._ebean_newInstance();
    }
    try
    {
      return this.constructor.newInstance(constuctorArgs);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  public Object createVanillaBean()
  {
    try
    {
      return this.vanillaConstructor.newInstance(constuctorArgs);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  private int getFieldIndex(String fieldName)
  {
    if (this.entityBean == null) {
      throw new RuntimeException("Trying to get fieldName on abstract class " + this.clazz);
    }
    String[] fields = this.entityBean._ebean_getFieldNames();
    for (int i = 0; i < fields.length; i++) {
      if (fieldName.equals(fields[i])) {
        return i;
      }
    }
    String fieldList = Arrays.toString(fields);
    String msg = "field [" + fieldName + "] not found in [" + this.clazz.getName() + "]" + fieldList;
    throw new IllegalArgumentException(msg);
  }
  
  public BeanReflectGetter getGetter(String name)
  {
    int i = getFieldIndex(name);
    return new Getter(i, this.entityBean);
  }
  
  public BeanReflectSetter getSetter(String name)
  {
    int i = getFieldIndex(name);
    return new Setter(i, this.entityBean);
  }
  
  static final class Getter
    implements BeanReflectGetter
  {
    private final int fieldIndex;
    private final EntityBean entityBean;
    
    Getter(int fieldIndex, EntityBean entityBean)
    {
      this.fieldIndex = fieldIndex;
      this.entityBean = entityBean;
    }
    
    public Object get(Object bean)
    {
      return this.entityBean._ebean_getField(this.fieldIndex, bean);
    }
    
    public Object getIntercept(Object bean)
    {
      return this.entityBean._ebean_getFieldIntercept(this.fieldIndex, bean);
    }
  }
  
  static final class Setter
    implements BeanReflectSetter
  {
    private final int fieldIndex;
    private final EntityBean entityBean;
    
    Setter(int fieldIndex, EntityBean entityBean)
    {
      this.fieldIndex = fieldIndex;
      this.entityBean = entityBean;
    }
    
    public void set(Object bean, Object value)
    {
      this.entityBean._ebean_setField(this.fieldIndex, bean, value);
    }
    
    public void setIntercept(Object bean, Object value)
    {
      this.entityBean._ebean_setFieldIntercept(this.fieldIndex, bean, value);
    }
  }
}
