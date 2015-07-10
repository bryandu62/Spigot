package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.PersistenceException;

public class CounterFactory
{
  final GeneratedCounterInteger integerCounter = new GeneratedCounterInteger();
  final GeneratedCounterLong longCounter = new GeneratedCounterLong();
  
  public void setCounter(DeployBeanProperty property)
  {
    property.setGeneratedProperty(createCounter(property));
  }
  
  private GeneratedProperty createCounter(DeployBeanProperty property)
  {
    Class<?> propType = property.getPropertyType();
    if ((propType.equals(Integer.class)) || (propType.equals(Integer.TYPE))) {
      return this.integerCounter;
    }
    if ((propType.equals(Long.class)) || (propType.equals(Long.TYPE))) {
      return this.longCounter;
    }
    int type = getType(propType);
    return new GeneratedCounter(type);
  }
  
  private int getType(Class<?> propType)
  {
    if ((propType.equals(Short.class)) || (propType.equals(Short.TYPE))) {
      return -6;
    }
    if (propType.equals(BigDecimal.class)) {
      return 3;
    }
    if ((propType.equals(Double.class)) || (propType.equals(Double.TYPE))) {
      return 8;
    }
    if ((propType.equals(Float.class)) || (propType.equals(Float.TYPE))) {
      return 7;
    }
    if (propType.equals(BigInteger.class)) {
      return -5;
    }
    String msg = "Can not support Counter for type " + propType.getName();
    throw new PersistenceException(msg);
  }
}
