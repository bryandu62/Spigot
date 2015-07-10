package com.avaje.ebeaninternal.server.deploy.generatedproperty;

import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import java.math.BigDecimal;
import java.util.HashSet;

public class GeneratedPropertyFactory
{
  CounterFactory counterFactory;
  InsertTimestampFactory insertFactory;
  UpdateTimestampFactory updateFactory;
  HashSet<String> numberTypes = new HashSet();
  
  public GeneratedPropertyFactory()
  {
    this.counterFactory = new CounterFactory();
    this.insertFactory = new InsertTimestampFactory();
    this.updateFactory = new UpdateTimestampFactory();
    
    this.numberTypes.add(Integer.class.getName());
    this.numberTypes.add(Integer.TYPE.getName());
    this.numberTypes.add(Long.class.getName());
    this.numberTypes.add(Long.TYPE.getName());
    this.numberTypes.add(Short.class.getName());
    this.numberTypes.add(Short.TYPE.getName());
    this.numberTypes.add(Double.class.getName());
    this.numberTypes.add(Double.TYPE.getName());
    this.numberTypes.add(BigDecimal.class.getName());
  }
  
  private boolean isNumberType(String typeClassName)
  {
    return this.numberTypes.contains(typeClassName);
  }
  
  public void setVersion(DeployBeanProperty property)
  {
    if (isNumberType(property.getPropertyType().getName())) {
      setCounter(property);
    } else {
      setUpdateTimestamp(property);
    }
  }
  
  public void setCounter(DeployBeanProperty property)
  {
    this.counterFactory.setCounter(property);
  }
  
  public void setInsertTimestamp(DeployBeanProperty property)
  {
    this.insertFactory.setInsertTimestamp(property);
  }
  
  public void setUpdateTimestamp(DeployBeanProperty property)
  {
    this.updateFactory.setUpdateTimestamp(property);
  }
}
