package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.server.core.PersistRequest.Type;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BeanPersistIdMap
{
  private final Map<String, BeanPersistIds> beanMap = new LinkedHashMap();
  
  public String toString()
  {
    return this.beanMap.toString();
  }
  
  public boolean isEmpty()
  {
    return this.beanMap.isEmpty();
  }
  
  public Collection<BeanPersistIds> values()
  {
    return this.beanMap.values();
  }
  
  public void add(BeanDescriptor<?> desc, PersistRequest.Type type, Object id)
  {
    BeanPersistIds r = getPersistIds(desc);
    r.addId(type, (Serializable)id);
  }
  
  private BeanPersistIds getPersistIds(BeanDescriptor<?> desc)
  {
    String beanType = desc.getFullName();
    BeanPersistIds r = (BeanPersistIds)this.beanMap.get(beanType);
    if (r == null)
    {
      r = new BeanPersistIds(desc);
      this.beanMap.put(beanType, r);
    }
    return r;
  }
}
