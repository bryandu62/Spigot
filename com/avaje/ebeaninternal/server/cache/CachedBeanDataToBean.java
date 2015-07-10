package com.avaje.ebeaninternal.server.cache;

import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import java.util.HashSet;
import java.util.Set;

public class CachedBeanDataToBean
{
  private final BeanDescriptor<?> desc;
  private final Object bean;
  private final EntityBeanIntercept ebi;
  private final CachedBeanData cacheBeandata;
  private final Set<String> cacheLoadedProperties;
  private final Set<String> loadedProps;
  private final Set<String> excludeProps;
  private final Object oldValuesBean;
  private final boolean readOnly;
  
  public static void load(BeanDescriptor<?> desc, Object bean, CachedBeanData cacheBeandata)
  {
    if ((bean instanceof EntityBean)) {
      load(desc, bean, ((EntityBean)bean)._ebean_getIntercept(), cacheBeandata);
    } else {
      load(desc, bean, null, cacheBeandata);
    }
  }
  
  public static void load(BeanDescriptor<?> desc, Object bean, EntityBeanIntercept ebi, CachedBeanData cacheBeandata)
  {
    new CachedBeanDataToBean(desc, bean, ebi, cacheBeandata).load();
  }
  
  private CachedBeanDataToBean(BeanDescriptor<?> desc, Object bean, EntityBeanIntercept ebi, CachedBeanData cacheBeandata)
  {
    this.desc = desc;
    this.bean = bean;
    this.ebi = ebi;
    this.cacheBeandata = cacheBeandata;
    this.cacheLoadedProperties = cacheBeandata.getLoadedProperties();
    this.loadedProps = (this.cacheLoadedProperties == null ? null : new HashSet());
    if (ebi != null)
    {
      this.excludeProps = ebi.getLoadedProps();
      this.oldValuesBean = ebi.getOldValues();
      this.readOnly = ebi.isReadOnly();
    }
    else
    {
      this.excludeProps = null;
      this.oldValuesBean = null;
      this.readOnly = false;
    }
  }
  
  private boolean load()
  {
    BeanProperty[] propertiesNonTransient = this.desc.propertiesNonMany();
    for (int i = 0; i < propertiesNonTransient.length; i++)
    {
      BeanProperty prop = propertiesNonTransient[i];
      if (includeNonManyProperty(prop.getName()))
      {
        Object data = this.cacheBeandata.getData(i);
        prop.setCacheDataValue(this.bean, data, this.oldValuesBean, this.readOnly);
      }
    }
    BeanPropertyAssocMany<?>[] manys = this.desc.propertiesMany();
    for (int i = 0; i < manys.length; i++)
    {
      BeanPropertyAssocMany<?> prop = manys[i];
      if (includeManyProperty(prop.getName())) {
        prop.createReference(this.bean);
      }
    }
    if (this.ebi != null)
    {
      if (this.loadedProps == null)
      {
        this.ebi.setLoadedProps(null);
      }
      else
      {
        HashSet<String> mergeProps = new HashSet();
        if (this.excludeProps != null) {
          mergeProps.addAll(this.excludeProps);
        }
        mergeProps.addAll(this.loadedProps);
        this.ebi.setLoadedProps(mergeProps);
      }
      this.ebi.setLoadedLazy();
    }
    return true;
  }
  
  private boolean includeManyProperty(String name)
  {
    if ((this.excludeProps != null) && (this.excludeProps.contains(name))) {
      return false;
    }
    if (this.loadedProps != null) {
      this.loadedProps.add(name);
    }
    return true;
  }
  
  private boolean includeNonManyProperty(String name)
  {
    if ((this.excludeProps != null) && (this.excludeProps.contains(name))) {
      return false;
    }
    if ((this.cacheLoadedProperties != null) && (!this.cacheLoadedProperties.contains(name))) {
      return false;
    }
    if (this.loadedProps != null) {
      this.loadedProps.add(name);
    }
    return true;
  }
}
