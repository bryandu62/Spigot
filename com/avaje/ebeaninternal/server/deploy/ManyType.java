package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.api.SpiQuery.Type;

public class ManyType
{
  public static final ManyType JAVA_LIST = new ManyType(Underlying.LIST);
  public static final ManyType JAVA_SET = new ManyType(Underlying.SET);
  public static final ManyType JAVA_MAP = new ManyType(Underlying.MAP);
  private final SpiQuery.Type queryType;
  private final Underlying underlying;
  private final CollectionTypeConverter typeConverter;
  
  public static enum Underlying
  {
    LIST,  SET,  MAP;
    
    private Underlying() {}
  }
  
  private ManyType(Underlying underlying)
  {
    this(underlying, null);
  }
  
  public ManyType(Underlying underlying, CollectionTypeConverter typeConverter)
  {
    this.underlying = underlying;
    this.typeConverter = typeConverter;
    switch (underlying)
    {
    case LIST: 
      this.queryType = SpiQuery.Type.LIST;
      break;
    case SET: 
      this.queryType = SpiQuery.Type.SET;
      break;
    default: 
      this.queryType = SpiQuery.Type.MAP;
    }
  }
  
  public SpiQuery.Type getQueryType()
  {
    return this.queryType;
  }
  
  public Underlying getUnderlying()
  {
    return this.underlying;
  }
  
  public CollectionTypeConverter getTypeConverter()
  {
    return this.typeConverter;
  }
}
