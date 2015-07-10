package com.avaje.ebeaninternal.server.cache;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import java.util.HashSet;
import java.util.Set;

public class CachedBeanDataFromBean
{
  private final BeanDescriptor<?> desc;
  private final Object bean;
  private final EntityBeanIntercept ebi;
  private final Set<String> loadedProps;
  private final Set<String> extractProps;
  
  public static CachedBeanData extract(BeanDescriptor<?> desc, Object bean)
  {
    if ((bean instanceof EntityBean)) {
      return new CachedBeanDataFromBean(desc, bean, ((EntityBean)bean)._ebean_getIntercept()).extract();
    }
    return new CachedBeanDataFromBean(desc, bean, null).extract();
  }
  
  public static CachedBeanData extract(BeanDescriptor<?> desc, Object bean, EntityBeanIntercept ebi)
  {
    return new CachedBeanDataFromBean(desc, bean, ebi).extract();
  }
  
  private CachedBeanDataFromBean(BeanDescriptor<?> desc, Object bean, EntityBeanIntercept ebi)
  {
    this.desc = desc;
    this.bean = bean;
    this.ebi = ebi;
    if (ebi != null)
    {
      this.loadedProps = ebi.getLoadedProps();
      this.extractProps = (this.loadedProps == null ? null : new HashSet());
    }
    else
    {
      this.extractProps = new HashSet();
      this.loadedProps = null;
    }
  }
  
  private CachedBeanData extract()
  {
    BeanProperty[] props = this.desc.propertiesNonMany();
    
    Object[] data = new Object[props.length];
    
    int naturalKeyUpdate = -1;
    for (int i = 0; i < props.length; i++)
    {
      BeanProperty prop = props[i];
      if (includeNonManyProperty(prop.getName()))
      {
        data[i] = prop.getCacheDataValue(this.bean);
        if (prop.isNaturalKey()) {
          naturalKeyUpdate = i;
        }
        if (this.ebi != null)
        {
          if (this.extractProps != null) {
            this.extractProps.add(prop.getName());
          }
        }
        else if ((data[i] != null) && 
          (this.extractProps != null)) {
          this.extractProps.add(prop.getName());
        }
      }
    }
    Object sharableBean = null;
    if ((this.desc.isCacheSharableBeans()) && (this.ebi != null) && (this.loadedProps == null)) {
      if (this.ebi.isReadOnly())
      {
        sharableBean = this.bean;
      }
      else
      {
        sharableBean = this.desc.createBean(false);
        BeanProperty[] propertiesId = this.desc.propertiesId();
        for (int i = 0; i < propertiesId.length; i++)
        {
          Object v = propertiesId[i].getValue(this.bean);
          propertiesId[i].setValue(sharableBean, v);
        }
        BeanProperty[] propertiesNonTransient = this.desc.propertiesNonTransient();
        for (int i = 0; i < propertiesNonTransient.length; i++)
        {
          Object v = propertiesNonTransient[i].getValue(this.bean);
          propertiesNonTransient[i].setValue(sharableBean, v);
        }
        EntityBeanIntercept ebi = ((EntityBean)sharableBean)._ebean_intercept();
        ebi.setReadOnly(true);
        ebi.setLoaded();
      }
    }
    return new CachedBeanData(sharableBean, this.extractProps, data, naturalKeyUpdate);
  }
  
  private boolean includeNonManyProperty(String name)
  {
    return (this.loadedProps == null) || (this.loadedProps.contains(name));
  }
}
