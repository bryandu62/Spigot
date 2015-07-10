package com.avaje.ebeaninternal.server.util;

import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.common.BeanList;
import com.avaje.ebean.common.BeanMap;
import com.avaje.ebean.common.BeanSet;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class BeanCollectionFactory
{
  private static final int defaultListInitialCapacity = 20;
  private static final int defaultSetInitialCapacity = 32;
  private static final int defaultMapInitialCapacity = 32;
  
  private static class BeanCollectionFactoryHolder
  {
    private static BeanCollectionFactory me = new BeanCollectionFactory(null);
  }
  
  public static BeanCollection<?> create(BeanCollectionParams params)
  {
    return BeanCollectionFactoryHolder.me.createMany(params);
  }
  
  private BeanCollection<?> createMany(BeanCollectionParams params)
  {
    SpiQuery.Type manyType = params.getManyType();
    switch (manyType)
    {
    case MAP: 
      return createMap(params);
    case LIST: 
      return createList(params);
    case SET: 
      return createSet(params);
    }
    throw new RuntimeException("Invalid Arg " + manyType);
  }
  
  private BeanMap createMap(BeanCollectionParams params)
  {
    return new BeanMap(new LinkedHashMap(32));
  }
  
  private BeanSet createSet(BeanCollectionParams params)
  {
    return new BeanSet(new LinkedHashSet(32));
  }
  
  private BeanList createList(BeanCollectionParams params)
  {
    return new BeanList(new ArrayList(20));
  }
}
