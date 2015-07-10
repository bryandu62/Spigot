package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;

public class BeanCollectionHelpFactory
{
  public static <T> BeanCollectionHelp<T> create(BeanPropertyAssocMany<T> manyProperty)
  {
    ManyType manyType = manyProperty.getManyType();
    switch (manyType.getUnderlying())
    {
    case LIST: 
      return new BeanListHelp(manyProperty);
    case SET: 
      return new BeanSetHelp(manyProperty);
    case MAP: 
      return new BeanMapHelp(manyProperty);
    }
    throw new RuntimeException("Invalid type " + manyType);
  }
  
  public static <T> BeanCollectionHelp<T> create(OrmQueryRequest<T> request)
  {
    SpiQuery.Type manyType = request.getQuery().getType();
    if (manyType.equals(SpiQuery.Type.LIST)) {
      return new BeanListHelp();
    }
    if (manyType.equals(SpiQuery.Type.SET)) {
      return new BeanSetHelp();
    }
    BeanDescriptor<T> target = request.getBeanDescriptor();
    String mapKey = request.getQuery().getMapKey();
    return new BeanMapHelp(target, mapKey);
  }
}
