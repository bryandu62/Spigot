package com.avaje.ebean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InvalidValue
  implements Serializable
{
  private static final long serialVersionUID = 2408556605456324913L;
  private static final Object[] EMPTY = new Object[0];
  private final String beanType;
  private final String propertyName;
  private final String validatorKey;
  private final Object value;
  private final InvalidValue[] children;
  private final Object[] validatorAttributes;
  private String message;
  
  public InvalidValue(String validatorKey, String beanType, Object bean, InvalidValue[] children)
  {
    this.validatorKey = validatorKey;
    this.validatorAttributes = EMPTY;
    this.beanType = beanType;
    this.propertyName = null;
    this.value = bean;
    this.children = children;
  }
  
  public InvalidValue(String validatorKey, Object[] validatorAttributes, String beanType, String propertyName, Object value)
  {
    this.validatorKey = validatorKey;
    this.validatorAttributes = validatorAttributes;
    this.beanType = beanType;
    this.propertyName = propertyName;
    this.value = value;
    this.children = null;
  }
  
  public String getBeanType()
  {
    return this.beanType;
  }
  
  public String getPropertyName()
  {
    return this.propertyName;
  }
  
  public String getValidatorKey()
  {
    return this.validatorKey;
  }
  
  public Object[] getValidatorAttributes()
  {
    return this.validatorAttributes;
  }
  
  public Object getValue()
  {
    return this.value;
  }
  
  public InvalidValue[] getChildren()
  {
    return this.children;
  }
  
  public String getMessage()
  {
    return this.message;
  }
  
  public void setMessage(String message)
  {
    this.message = message;
  }
  
  public boolean isBean()
  {
    return !isError();
  }
  
  public boolean isError()
  {
    return this.children == null;
  }
  
  public InvalidValue[] getErrors()
  {
    ArrayList<InvalidValue> list = new ArrayList();
    buildList(list);
    
    return toArray(list);
  }
  
  private void buildList(List<InvalidValue> list)
  {
    if (isError()) {
      list.add(this);
    } else {
      for (int i = 0; i < this.children.length; i++) {
        this.children[i].buildList(list);
      }
    }
  }
  
  public String toString()
  {
    if (isError()) {
      return "errorKey=" + this.validatorKey + " type=" + this.beanType + " property=" + this.propertyName + " value=" + this.value;
    }
    if (this.children.length == 1) {
      return this.children[0].toString();
    }
    StringBuilder sb = new StringBuilder(50);
    sb.append("\r\nCHILDREN[").append(this.children.length).append("]");
    for (int i = 0; i < this.children.length; i++) {
      sb.append(this.children[i].toString()).append(", ");
    }
    return sb.toString();
  }
  
  public static InvalidValue[] toArray(List<InvalidValue> list)
  {
    return (InvalidValue[])list.toArray(new InvalidValue[list.size()]);
  }
  
  public static List<InvalidValue> toList(InvalidValue invalid)
  {
    ArrayList<InvalidValue> list = new ArrayList();
    list.add(invalid);
    return list;
  }
}
