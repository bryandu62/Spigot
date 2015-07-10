package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanPathUpdate
{
  private final Map<String, BeanPathUpdateIds> map = new LinkedHashMap();
  
  public void add(BeanDescriptor<?> desc, String path, Object id)
  {
    String key = desc.getFullName() + ":" + path;
    BeanPathUpdateIds pathIds = (BeanPathUpdateIds)this.map.get(key);
    if (pathIds == null)
    {
      pathIds = new BeanPathUpdateIds(desc, path);
      this.map.put(key, pathIds);
    }
    pathIds.addId((Serializable)id);
  }
}
