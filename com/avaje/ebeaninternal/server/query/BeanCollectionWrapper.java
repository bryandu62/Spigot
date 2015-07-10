package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.api.SpiSqlQuery;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.core.RelationalQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.ManyType;
import com.avaje.ebeaninternal.server.util.BeanCollectionFactory;
import com.avaje.ebeaninternal.server.util.BeanCollectionParams;
import java.util.Collection;
import java.util.Map;

public final class BeanCollectionWrapper
{
  private final boolean isMap;
  private final SpiQuery.Type queryType;
  private final String mapKey;
  private final BeanCollection<?> beanCollection;
  private final Collection<Object> collection;
  private final Map<Object, Object> map;
  private final BeanDescriptor<?> desc;
  private int rowCount;
  
  public BeanCollectionWrapper(RelationalQueryRequest request)
  {
    this.desc = null;
    this.queryType = request.getQueryType();
    this.mapKey = request.getQuery().getMapKey();
    this.isMap = SpiQuery.Type.MAP.equals(this.queryType);
    
    this.beanCollection = createBeanCollection(this.queryType);
    this.collection = getCollection(this.isMap);
    this.map = getMap(this.isMap);
  }
  
  public BeanCollectionWrapper(OrmQueryRequest<?> request)
  {
    this.desc = request.getBeanDescriptor();
    this.queryType = request.getQueryType();
    this.mapKey = request.getQuery().getMapKey();
    this.isMap = SpiQuery.Type.MAP.equals(this.queryType);
    
    this.beanCollection = createBeanCollection(this.queryType);
    this.collection = getCollection(this.isMap);
    this.map = getMap(this.isMap);
  }
  
  public BeanCollectionWrapper(BeanPropertyAssocMany<?> manyProp)
  {
    this.queryType = manyProp.getManyType().getQueryType();
    this.mapKey = manyProp.getMapKey();
    this.desc = manyProp.getTargetDescriptor();
    this.isMap = SpiQuery.Type.MAP.equals(this.queryType);
    
    this.beanCollection = createBeanCollection(this.queryType);
    this.collection = getCollection(this.isMap);
    this.map = getMap(this.isMap);
  }
  
  private Map<Object, Object> getMap(boolean isMap)
  {
    return isMap ? (Map)this.beanCollection : null;
  }
  
  private Collection<Object> getCollection(boolean isMap)
  {
    return isMap ? null : (Collection)this.beanCollection;
  }
  
  public BeanCollection<?> getBeanCollection()
  {
    return this.beanCollection;
  }
  
  private BeanCollection<?> createBeanCollection(SpiQuery.Type manyType)
  {
    BeanCollectionParams p = new BeanCollectionParams(manyType);
    return BeanCollectionFactory.create(p);
  }
  
  public boolean isMap()
  {
    return this.isMap;
  }
  
  public int size()
  {
    return this.rowCount;
  }
  
  public void add(Object bean)
  {
    add(bean, this.beanCollection);
  }
  
  public void add(Object bean, Object collection)
  {
    if (bean == null) {
      return;
    }
    this.rowCount += 1;
    if (this.isMap)
    {
      Object keyValue = null;
      if (this.mapKey != null) {
        keyValue = this.desc.getValue(bean, this.mapKey);
      } else {
        keyValue = this.desc.getId(bean);
      }
      Map mapColl = (Map)collection;
      mapColl.put(keyValue, bean);
    }
    else
    {
      ((Collection)collection).add(bean);
    }
  }
  
  public void addToCollection(Object bean)
  {
    this.collection.add(bean);
  }
  
  public void addToMap(Object bean, Object key)
  {
    this.map.put(key, bean);
  }
}
