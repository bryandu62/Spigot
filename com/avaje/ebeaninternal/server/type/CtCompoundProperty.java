package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.config.CompoundTypeProperty;

public class CtCompoundProperty
{
  private final String relativeName;
  private final CtCompoundProperty parent;
  private final CtCompoundType<?> compoundType;
  private final CompoundTypeProperty property;
  
  public CtCompoundProperty(String relativeName, CtCompoundProperty parent, CtCompoundType<?> ctType, CompoundTypeProperty<?, ?> property)
  {
    this.relativeName = relativeName;
    this.parent = parent;
    this.compoundType = ctType;
    this.property = property;
  }
  
  public String getRelativeName()
  {
    return this.relativeName;
  }
  
  public String getPropertyName()
  {
    return this.property.getName();
  }
  
  public String toString()
  {
    return this.relativeName;
  }
  
  public Object getValue(Object valueObject)
  {
    if (valueObject == null) {
      return null;
    }
    if (this.parent != null) {
      valueObject = this.parent.getValue(valueObject);
    }
    return this.property.getValue(valueObject);
  }
  
  public Object setValue(Object bean, Object value)
  {
    Object compoundValue = ImmutableCompoundTypeBuilder.set(this.compoundType, this.property.getName(), value);
    if ((compoundValue != null) && (this.parent != null)) {
      return this.parent.setValue(bean, compoundValue);
    }
    return compoundValue;
  }
}
