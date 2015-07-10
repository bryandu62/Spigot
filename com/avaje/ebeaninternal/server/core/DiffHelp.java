package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.ValuePair;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.util.ValueUtil;
import java.util.LinkedHashMap;
import java.util.Map;

public class DiffHelp
{
  public Map<String, ValuePair> diff(Object a, Object b, BeanDescriptor<?> desc)
  {
    boolean oldValues = false;
    if (b == null) {
      if ((a instanceof EntityBean))
      {
        EntityBean eb = (EntityBean)a;
        b = eb._ebean_getIntercept().getOldValues();
        oldValues = true;
      }
    }
    Map<String, ValuePair> map = new LinkedHashMap();
    if (b == null) {
      return map;
    }
    BeanProperty[] base = desc.propertiesBaseScalar();
    for (int i = 0; i < base.length; i++)
    {
      Object aval = base[i].getValue(a);
      Object bval = base[i].getValue(b);
      if (!ValueUtil.areEqual(aval, bval)) {
        map.put(base[i].getName(), new ValuePair(aval, bval));
      }
    }
    diffAssocOne(a, b, desc, map);
    diffEmbedded(a, b, desc, map, oldValues);
    
    return map;
  }
  
  private void diffEmbedded(Object a, Object b, BeanDescriptor<?> desc, Map<String, ValuePair> map, boolean oldValues)
  {
    BeanPropertyAssocOne<?>[] emb = desc.propertiesEmbedded();
    for (int i = 0; i < emb.length; i++)
    {
      Object aval = emb[i].getValue(a);
      Object bval = emb[i].getValue(b);
      if (oldValues)
      {
        bval = ((EntityBean)bval)._ebean_getIntercept().getOldValues();
        if (bval == null) {}
      }
      else if (!isBothNull(aval, bval))
      {
        if (isDiffNull(aval, bval))
        {
          map.put(emb[i].getName(), new ValuePair(aval, bval));
        }
        else
        {
          BeanProperty[] props = emb[i].getProperties();
          for (int j = 0; j < props.length; j++)
          {
            Object aEmbPropVal = props[j].getValue(aval);
            Object bEmbPropVal = props[j].getValue(bval);
            if (!ValueUtil.areEqual(aEmbPropVal, bEmbPropVal)) {
              map.put(emb[i].getName(), new ValuePair(aval, bval));
            }
          }
        }
      }
    }
  }
  
  private void diffAssocOne(Object a, Object b, BeanDescriptor<?> desc, Map<String, ValuePair> map)
  {
    BeanPropertyAssocOne<?>[] ones = desc.propertiesOne();
    for (int i = 0; i < ones.length; i++)
    {
      Object aval = ones[i].getValue(a);
      Object bval = ones[i].getValue(b);
      if (!isBothNull(aval, bval)) {
        if (isDiffNull(aval, bval))
        {
          map.put(ones[i].getName(), new ValuePair(aval, bval));
        }
        else
        {
          BeanDescriptor<?> oneDesc = ones[i].getTargetDescriptor();
          Object aOneId = oneDesc.getId(aval);
          Object bOneId = oneDesc.getId(bval);
          if (!ValueUtil.areEqual(aOneId, bOneId)) {
            map.put(ones[i].getName(), new ValuePair(aval, bval));
          }
        }
      }
    }
  }
  
  private boolean isBothNull(Object aval, Object bval)
  {
    return (aval == null) && (bval == null);
  }
  
  private boolean isDiffNull(Object aval, Object bval)
  {
    if (aval == null) {
      return bval != null;
    }
    return bval == null;
  }
}
