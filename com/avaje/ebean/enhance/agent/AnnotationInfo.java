package com.avaje.ebean.enhance.agent;

import java.util.ArrayList;
import java.util.HashMap;

public class AnnotationInfo
{
  final HashMap<String, Object> valueMap = new HashMap();
  AnnotationInfo parent;
  
  public AnnotationInfo(AnnotationInfo parent)
  {
    this.parent = parent;
  }
  
  public String toString()
  {
    return this.valueMap.toString();
  }
  
  public AnnotationInfo getParent()
  {
    return this.parent;
  }
  
  public void setParent(AnnotationInfo parent)
  {
    this.parent = parent;
  }
  
  public void add(String prefix, String name, Object value)
  {
    if (name == null)
    {
      ArrayList<Object> list = (ArrayList)this.valueMap.get(prefix);
      if (list == null)
      {
        list = new ArrayList();
        this.valueMap.put(prefix, list);
      }
      list.add(value);
    }
    else
    {
      String key = getKey(prefix, name);
      
      this.valueMap.put(key, value);
    }
  }
  
  public void addEnum(String prefix, String name, String desc, String value)
  {
    add(prefix, name, value);
  }
  
  private String getKey(String prefix, String name)
  {
    if (prefix == null) {
      return name;
    }
    return prefix + "." + name;
  }
  
  public Object getValue(String key)
  {
    Object o = this.valueMap.get(key);
    if ((o == null) && (this.parent != null)) {
      o = this.parent.getValue(key);
    }
    return o;
  }
}
