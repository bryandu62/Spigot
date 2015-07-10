package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebeaninternal.api.SpiQuery.Mode;
import com.avaje.ebeaninternal.server.type.DataReader;
import java.util.Map;

public abstract interface DbReadContext
{
  public abstract Boolean isReadOnly();
  
  public abstract void propagateState(Object paramObject);
  
  public abstract DataReader getDataReader();
  
  public abstract boolean isVanillaMode();
  
  public abstract boolean isRawSql();
  
  public abstract void setCurrentPrefix(String paramString, Map<String, String> paramMap);
  
  public abstract boolean isAutoFetchProfiling();
  
  public abstract void profileBean(EntityBeanIntercept paramEntityBeanIntercept, String paramString);
  
  public abstract PersistenceContext getPersistenceContext();
  
  public abstract void register(String paramString, EntityBeanIntercept paramEntityBeanIntercept);
  
  public abstract void register(String paramString, BeanCollection<?> paramBeanCollection);
  
  public abstract BeanPropertyAssocMany<?> getManyProperty();
  
  public abstract void setLoadedBean(Object paramObject1, Object paramObject2);
  
  public abstract void setLoadedManyBean(Object paramObject);
  
  public abstract SpiQuery.Mode getQueryMode();
}
