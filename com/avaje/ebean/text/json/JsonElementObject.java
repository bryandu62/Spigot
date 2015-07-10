package com.avaje.ebean.text.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JsonElementObject
  implements JsonElement
{
  private final Map<String, JsonElement> map = new LinkedHashMap();
  
  public void put(String key, JsonElement value)
  {
    this.map.put(key, value);
  }
  
  private String[] split(String exp)
  {
    int pos = exp.indexOf('.');
    if (pos == -1) {
      return new String[] { exp, null };
    }
    String exp0 = exp.substring(0, pos);
    String exp1 = exp.substring(pos + 1);
    return new String[] { exp0, exp1 };
  }
  
  public Object eval(String exp)
  {
    String[] e = split(exp);
    return eval(e[0], e[1]);
  }
  
  public int evalInt(String exp)
  {
    String[] e = split(exp);
    return evalInt(e[0], e[1]);
  }
  
  public String evalString(String exp)
  {
    if (exp == null) {
      return this.map.toString();
    }
    String[] e = split(exp);
    return evalString(e[0], e[1]);
  }
  
  public boolean evalBoolean(String exp)
  {
    String[] e = split(exp);
    return evalBoolean(e[0], e[1]);
  }
  
  private Object eval(String exp0, String exp1)
  {
    JsonElement e = (JsonElement)this.map.get(exp0);
    return e == null ? null : e.eval(exp1);
  }
  
  private int evalInt(String exp0, String exp1)
  {
    JsonElement e = (JsonElement)this.map.get(exp0);
    return e == null ? 0 : e.evalInt(exp1);
  }
  
  private String evalString(String exp0, String exp1)
  {
    JsonElement e = (JsonElement)this.map.get(exp0);
    return e == null ? "" : e.evalString(exp1);
  }
  
  private boolean evalBoolean(String exp0, String exp1)
  {
    JsonElement e = (JsonElement)this.map.get(exp0);
    return e == null ? false : e.evalBoolean(exp1);
  }
  
  public JsonElement get(String key)
  {
    return (JsonElement)this.map.get(key);
  }
  
  public JsonElement getValue(String key)
  {
    return (JsonElement)this.map.get(key);
  }
  
  public Set<String> keySet()
  {
    return this.map.keySet();
  }
  
  public Set<Map.Entry<String, JsonElement>> entrySet()
  {
    return this.map.entrySet();
  }
  
  public String toString()
  {
    return this.map.toString();
  }
  
  public boolean isPrimitive()
  {
    return false;
  }
  
  public String toPrimitiveString()
  {
    return null;
  }
}
