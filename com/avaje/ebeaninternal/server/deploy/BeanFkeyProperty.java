package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.text.StringFormatter;
import com.avaje.ebean.text.StringParser;
import com.avaje.ebeaninternal.server.el.ElPropertyValue;

public final class BeanFkeyProperty
  implements ElPropertyValue
{
  private final String placeHolder;
  private final String prefix;
  private final String name;
  private final String dbColumn;
  private int deployOrder;
  
  public BeanFkeyProperty(String prefix, String name, String dbColumn, int deployOrder)
  {
    this.prefix = prefix;
    this.name = name;
    this.dbColumn = dbColumn;
    this.deployOrder = deployOrder;
    this.placeHolder = calcPlaceHolder(prefix, dbColumn);
  }
  
  public int getDeployOrder()
  {
    return this.deployOrder;
  }
  
  private String calcPlaceHolder(String prefix, String dbColumn)
  {
    if (prefix != null) {
      return "${" + prefix + "}" + dbColumn;
    }
    return "${}" + dbColumn;
  }
  
  public BeanFkeyProperty create(String expression)
  {
    int len = expression.length() - this.name.length() - 1;
    String prefix = expression.substring(0, len);
    
    return new BeanFkeyProperty(prefix, this.name, this.dbColumn, this.deployOrder);
  }
  
  public boolean isDbEncrypted()
  {
    return false;
  }
  
  public boolean isLocalEncrypted()
  {
    return false;
  }
  
  public boolean isDeployOnly()
  {
    return true;
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
    return this.dbColumn;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getElName()
  {
    return this.name;
  }
  
  public Object[] getAssocOneIdValues(Object value)
  {
    return null;
  }
  
  public String getAssocOneIdExpr(String prefix, String operator)
  {
    return null;
  }
  
  public String getAssocIdInExpr(String prefix)
  {
    return null;
  }
  
  public String getAssocIdInValueExpr(int size)
  {
    return null;
  }
  
  public boolean isAssocId()
  {
    return false;
  }
  
  public boolean isAssocProperty()
  {
    return false;
  }
  
  public String getElPlaceholder(boolean encrypted)
  {
    return this.placeHolder;
  }
  
  public String getElPrefix()
  {
    return this.prefix;
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
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public StringFormatter getStringFormatter()
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public StringParser getStringParser()
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public void elSetReference(Object bean)
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public Object elConvertType(Object value)
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public void elSetValue(Object bean, Object value, boolean populate, boolean reference)
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public Object elGetValue(Object bean)
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public Object elGetReference(Object bean)
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public BeanProperty getBeanProperty()
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
  
  public String getDeployProperty()
  {
    throw new RuntimeException("ElPropertyDeploy only - not implemented");
  }
}
