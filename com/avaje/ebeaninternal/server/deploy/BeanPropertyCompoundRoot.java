package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.reflect.BeanReflectSetter;
import com.avaje.ebeaninternal.server.type.CtCompoundProperty;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BeanPropertyCompoundRoot
{
  private final BeanReflectSetter setter;
  private final Method writeMethod;
  private final String name;
  private final String fullBeanName;
  private final LinkedHashMap<String, BeanPropertyCompoundScalar> propMap;
  private final ArrayList<BeanPropertyCompoundScalar> propList;
  private List<CtCompoundProperty> nonScalarProperties;
  
  public BeanPropertyCompoundRoot(DeployBeanProperty deploy)
  {
    this.fullBeanName = deploy.getFullBeanName();
    this.name = deploy.getName();
    this.setter = deploy.getSetter();
    this.writeMethod = deploy.getWriteMethod();
    this.propList = new ArrayList();
    this.propMap = new LinkedHashMap();
  }
  
  public BeanProperty[] getScalarProperties()
  {
    return (BeanProperty[])this.propList.toArray(new BeanProperty[this.propList.size()]);
  }
  
  public void register(BeanPropertyCompoundScalar prop)
  {
    this.propList.add(prop);
    this.propMap.put(prop.getName(), prop);
  }
  
  public BeanPropertyCompoundScalar getCompoundScalarProperty(String propName)
  {
    return (BeanPropertyCompoundScalar)this.propMap.get(propName);
  }
  
  public List<CtCompoundProperty> getNonScalarProperties()
  {
    return this.nonScalarProperties;
  }
  
  public void setNonScalarProperties(List<CtCompoundProperty> nonScalarProperties)
  {
    this.nonScalarProperties = nonScalarProperties;
  }
  
  public void setRootValue(Object bean, Object value)
  {
    try
    {
      if ((bean instanceof EntityBean))
      {
        this.setter.set(bean, value);
      }
      else
      {
        Object[] args = new Object[1];
        args[0] = value;
        this.writeMethod.invoke(bean, args);
      }
    }
    catch (Exception ex)
    {
      String beanType = bean == null ? "null" : bean.getClass().getName();
      String msg = "set " + this.name + " with arg[" + value + "] on [" + this.fullBeanName + "] with type[" + beanType + "] threw error";
      throw new RuntimeException(msg, ex);
    }
  }
  
  public void setRootValueIntercept(Object bean, Object value)
  {
    try
    {
      if ((bean instanceof EntityBean))
      {
        this.setter.setIntercept(bean, value);
      }
      else
      {
        Object[] args = new Object[1];
        args[0] = value;
        this.writeMethod.invoke(bean, args);
      }
    }
    catch (Exception ex)
    {
      String beanType = bean == null ? "null" : bean.getClass().getName();
      String msg = "setIntercept " + this.name + " arg[" + value + "] on [" + this.fullBeanName + "] with type[" + beanType + "] threw error";
      throw new RuntimeException(msg, ex);
    }
  }
}
