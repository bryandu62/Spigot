package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.config.ScalarTypeConverter;
import com.avaje.ebeaninternal.server.deploy.meta.DeployBeanProperty;
import com.avaje.ebeaninternal.server.type.CtCompoundProperty;

public class BeanPropertyCompoundScalar
  extends BeanProperty
{
  private final BeanPropertyCompoundRoot rootProperty;
  private final CtCompoundProperty ctProperty;
  private final ScalarTypeConverter typeConverter;
  
  public BeanPropertyCompoundScalar(BeanPropertyCompoundRoot rootProperty, DeployBeanProperty scalarDeploy, CtCompoundProperty ctProperty, ScalarTypeConverter<?, ?> typeConverter)
  {
    super(scalarDeploy);
    this.rootProperty = rootProperty;
    this.ctProperty = ctProperty;
    this.typeConverter = typeConverter;
  }
  
  public Object getValue(Object valueObject)
  {
    if (this.typeConverter != null) {
      valueObject = this.typeConverter.unwrapValue(valueObject);
    }
    return this.ctProperty.getValue(valueObject);
  }
  
  public void setValue(Object bean, Object value)
  {
    setValueInCompound(bean, value, false);
  }
  
  public void setValueInCompound(Object bean, Object value, boolean intercept)
  {
    Object compoundValue = this.ctProperty.setValue(bean, value);
    if (compoundValue != null)
    {
      if (this.typeConverter != null) {
        compoundValue = this.typeConverter.wrapValue(compoundValue);
      }
      if (intercept) {
        this.rootProperty.setRootValueIntercept(bean, compoundValue);
      } else {
        this.rootProperty.setRootValue(bean, compoundValue);
      }
    }
  }
  
  public void setValueIntercept(Object bean, Object value)
  {
    setValueInCompound(bean, value, true);
  }
  
  public Object getValueIntercept(Object bean)
  {
    return getValue(bean);
  }
  
  public Object elGetReference(Object bean)
  {
    return getValue(bean);
  }
  
  public Object elGetValue(Object bean)
  {
    return getValue(bean);
  }
  
  public void elSetReference(Object bean)
  {
    super.elSetReference(bean);
  }
  
  public void elSetValue(Object bean, Object value, boolean populate, boolean reference)
  {
    super.elSetValue(bean, value, populate, reference);
  }
}
