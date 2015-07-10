package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.server.core.PersistRequest.Type;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DeleteByIdMap
{
  private final Map<String, BeanPersistIds> beanMap = new LinkedHashMap();
  
  public String toString()
  {
    return this.beanMap.toString();
  }
  
  public void notifyCache()
  {
    for (BeanPersistIds deleteIds : this.beanMap.values())
    {
      BeanDescriptor<?> d = deleteIds.getBeanDescriptor();
      List<Serializable> idValues = deleteIds.getDeleteIds();
      if (idValues != null)
      {
        d.queryCacheClear();
        for (int i = 0; i < idValues.size(); i++) {
          d.cacheRemove(idValues.get(i));
        }
      }
    }
  }
  
  public boolean isEmpty()
  {
    return this.beanMap.isEmpty();
  }
  
  public Collection<BeanPersistIds> values()
  {
    return this.beanMap.values();
  }
  
  public void add(BeanDescriptor<?> desc, Object id)
  {
    BeanPersistIds r = getPersistIds(desc);
    r.addId(PersistRequest.Type.DELETE, (Serializable)id);
  }
  
  public void addList(BeanDescriptor<?> desc, List<Object> idList)
  {
    BeanPersistIds r = getPersistIds(desc);
    for (int i = 0; i < idList.size(); i++) {
      r.addId(PersistRequest.Type.DELETE, (Serializable)idList.get(i));
    }
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
