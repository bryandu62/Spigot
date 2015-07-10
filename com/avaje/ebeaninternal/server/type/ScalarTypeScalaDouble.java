package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.config.ScalarTypeConverter;

public class ScalarTypeScalaDouble
  extends ScalarTypeWrapper<Object, Double>
{
  public ScalarTypeScalaDouble()
  {
    super(Object.class, new ScalarTypeDouble(), new Converter());
  }
  
  static class Converter
    implements ScalarTypeConverter<Object, Double>
  {
    public scala.Double getNullValue()
    {
      return null;
    }
    
    public Object wrapValue(Double scalarType)
    {
      return scalarType;
    }
    
    public Double unwrapValue(Object beanType)
    {
      return Double.valueOf(((scala.Double)beanType).toDouble());
    }
  }
}
