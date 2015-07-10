package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.common.BeanMap;
import com.avaje.ebeaninternal.server.text.json.WriteJsonContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class BeanMapHelp<T>
  implements BeanCollectionHelp<T>
{
  private final BeanPropertyAssocMany<T> many;
  private final BeanDescriptor<T> targetDescriptor;
  private final BeanProperty beanProperty;
  private BeanCollectionLoader loader;
  
  public BeanMapHelp(BeanDescriptor<T> targetDescriptor, String mapKey)
  {
    this(null, targetDescriptor, mapKey);
  }
  
  public BeanMapHelp(BeanPropertyAssocMany<T> many)
  {
    this(many, many.getTargetDescriptor(), many.getMapKey());
  }
  
  private BeanMapHelp(BeanPropertyAssocMany<T> many, BeanDescriptor<T> targetDescriptor, String mapKey)
  {
    this.many = many;
    this.targetDescriptor = targetDescriptor;
    
    this.beanProperty = targetDescriptor.getBeanProperty(mapKey);
  }
  
  public Iterator<?> getIterator(Object collection)
  {
    return ((Map)collection).values().iterator();
  }
  
  public void setLoader(BeanCollectionLoader loader)
  {
    this.loader = loader;
  }
  
  public BeanCollectionAdd getBeanCollectionAdd(Object bc, String mapKey)
  {
    if (mapKey == null) {
      mapKey = this.many.getMapKey();
    }
    BeanProperty beanProp = this.targetDescriptor.getBeanProperty(mapKey);
    if ((bc instanceof BeanMap))
    {
      BeanMap<Object, Object> bm = (BeanMap)bc;
      Map<Object, Object> actualMap = bm.getActualMap();
      if (actualMap == null)
      {
        actualMap = new LinkedHashMap();
        bm.setActualMap(actualMap);
      }
      return new Adder(beanProp, actualMap);
    }
    if ((bc instanceof Map)) {
      return new Adder(beanProp, (Map)bc);
    }
    throw new RuntimeException("Unhandled type " + bc);
  }
  
  static class Adder
    implements BeanCollectionAdd
  {
    private final BeanProperty beanProperty;
    private final Map<Object, Object> map;
    
    Adder(BeanProperty beanProperty, Map<Object, Object> map)
    {
      this.beanProperty = beanProperty;
      this.map = map;
    }
    
    public void addBean(Object bean)
    {
      Object keyValue = this.beanProperty.getValue(bean);
      this.map.put(keyValue, bean);
    }
  }
  
  public Object createEmpty(boolean vanilla)
  {
    return vanilla ? new LinkedHashMap() : new BeanMap();
  }
  
  public void add(BeanCollection<?> collection, Object bean)
  {
    Object keyValue = this.beanProperty.getValueIntercept(bean);
    
    Map<Object, Object> map = (Map)collection;
    map.put(keyValue, bean);
  }
  
  public BeanCollection<T> createReference(Object parentBean, String propertyName)
  {
    return new BeanMap(this.loader, parentBean, propertyName);
  }
  
  public ArrayList<InvalidValue> validate(Object manyValue)
  {
    ArrayList<InvalidValue> errs = null;
    
    Map<?, ?> m = (Map)manyValue;
    Iterator<?> it = m.values().iterator();
    while (it.hasNext())
    {
      Object detailBean = it.next();
      InvalidValue invalid = this.targetDescriptor.validate(true, detailBean);
      if (invalid != null)
      {
        if (errs == null) {
          errs = new ArrayList();
        }
        errs.add(invalid);
      }
    }
    return errs;
  }
  
  public void refresh(EbeanServer server, Query<?> query, Transaction t, Object parentBean)
  {
    BeanMap<?, ?> newBeanMap = (BeanMap)server.findMap(query, t);
    refresh(newBeanMap, parentBean);
  }
  
  public void refresh(BeanCollection<?> bc, Object parentBean)
  {
    BeanMap<?, ?> newBeanMap = (BeanMap)bc;
    Map<?, ?> current = (Map)this.many.getValueUnderlying(parentBean);
    
    newBeanMap.setModifyListening(this.many.getModifyListenMode());
    if (current == null)
    {
      this.many.setValue(parentBean, newBeanMap);
    }
    else if ((current instanceof BeanMap))
    {
      BeanMap<?, ?> currentBeanMap = (BeanMap)current;
      currentBeanMap.setActualMap(newBeanMap.getActualMap());
      currentBeanMap.setModifyListening(this.many.getModifyListenMode());
    }
    else
    {
      this.many.setValue(parentBean, newBeanMap);
    }
  }
  
  public void jsonWrite(WriteJsonContext ctx, String name, Object collection, boolean explicitInclude)
  {
    Map<?, ?> map;
    Map<?, ?> map;
    if ((collection instanceof BeanCollection))
    {
      BeanMap<?, ?> bc = (BeanMap)collection;
      if (!bc.isPopulated()) {
        if (explicitInclude) {
          bc.size();
        } else {
          return;
        }
      }
      map = bc.getActualMap();
    }
    else
    {
      map = (Map)collection;
    }
    int count = 0;
    ctx.beginAssocMany(name);
    Iterator<?> it = map.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<?, ?> entry = (Map.Entry)it.next();
      if (count++ > 0) {
        ctx.appendComma();
      }
      Object detailBean = entry.getValue();
      this.targetDescriptor.jsonWrite(ctx, detailBean);
    }
    ctx.endAssocMany();
  }
}
