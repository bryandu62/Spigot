package com.avaje.ebeaninternal.server.util;

import com.avaje.ebeaninternal.api.SpiQuery.Type;

public class BeanCollectionParams
{
  private final SpiQuery.Type manyType;
  
  public BeanCollectionParams(SpiQuery.Type manyType)
  {
    this.manyType = manyType;
  }
  
  public SpiQuery.Type getManyType()
  {
    return this.manyType;
  }
}
