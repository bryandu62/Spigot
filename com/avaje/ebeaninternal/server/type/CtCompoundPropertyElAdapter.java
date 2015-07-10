package com.avaje.ebeaninternal.server.type;

import com.avaje.ebean.text.StringFormatter;
import com.avaje.ebean.text.StringParser;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;

public class CtCompoundPropertyElAdapter
  implements ElPropertyValue
{
  private final CtCompoundProperty prop;
  private int deployOrder;
  
  public CtCompoundPropertyElAdapter(CtCompoundProperty prop)
  {
    this.prop = prop;
  }
  
  public void setDeployOrder(int deployOrder)
  {
    this.deployOrder = deployOrder;
  }
  
  public Object elConvertType(Object value)
  {
    return value;
  }
  
  public Object elGetReference(Object bean)
  {
    return bean;
  }
  
  public Object elGetValue(Object bean)
  {
    return this.prop.getValue(bean);
  }
  
  public void elSetReference(Object bean) {}
  
  public void elSetValue(Object bean, Object value, boolean populate, boolean reference)
  {
    this.prop.setValue(bean, value);
  }
  
  public int getDeployOrder()
  {
    return this.deployOrder;
  }
  
  public String getAssocOneIdExpr(String prefix, String operator)
  {
    throw new RuntimeException("Not Supported or Expected");
  }
  
  public Object[] getAssocOneIdValues(Object bean)
  {
    throw new RuntimeException("Not Supported or Expected");
  }
  
  public String getAssocIdInExpr(String prefix)
  {
    throw new RuntimeException("Not Supported or Expected");
  }
  
  public String getAssocIdInValueExpr(int size)
  {
    throw new RuntimeException("Not Supported or Expected");
  }
  
  public BeanProperty getBeanProperty()
  {
    return null;
  }
  
  public StringFormatter getStringFormatter()
  {
    return null;
  }
  
  public StringParser getStringParser()
  {
    return null;
  }
  
  public boolean isDbEncrypted()
  {
    return false;
  }
  
  public boolean isLocalEncrypted()
  {
    return false;
  }
  
  public boolean isAssocId()
  {
    return false;
  }
  
  public boolean isAssocProperty()
  {
    return false;
  }
  
  public boolean isDateTimeCapable()
  {
    return false;
  }
  
  public int getJdbcType()
  {
    return 0;
  }
  
  public Object parseDateTime(long systemTimeMillis)
  {
    throw new RuntimeException("Not Supported or Expected");
  }
  
  public boolean containsMany()
  {
    return false;
  }
  
  public boolean containsManySince(String sinceProperty)
  {
    return containsMany();
  }
  
  public String getDbColumn()
  {
    return null;
  }
  
  public String getElPlaceholder(boolean encrypted)
  {
    return null;
  }
  
  public String getElPrefix()
  {
    return null;
  }
  
  public String getName()
  {
    return this.prop.getPropertyName();
  }
  
  public String getElName()
  {
    return this.prop.getPropertyName();
  }
}
