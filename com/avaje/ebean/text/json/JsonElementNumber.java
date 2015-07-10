package com.avaje.ebean.text.json;

public class JsonElementNumber
  implements JsonElement
{
  private final String value;
  
  public JsonElementNumber(String value)
  {
    this.value = value;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public String toString()
  {
    return this.value;
  }
  
  public boolean isPrimitive()
  {
    return true;
  }
  
  public String toPrimitiveString()
  {
    return this.value;
  }
  
  public Object eval(String exp)
  {
    if (exp != null) {
      throw new IllegalArgumentException("expression [" + exp + "] not allowed on number");
    }
    return Double.valueOf(Double.parseDouble(this.value));
  }
  
  public int evalInt(String exp)
  {
    if (exp != null) {
      throw new IllegalArgumentException("expression [" + exp + "] not allowed on number");
    }
    return Integer.parseInt(this.value);
  }
  
  public String evalString(String exp)
  {
    if (exp != null) {
      throw new IllegalArgumentException("expression [" + exp + "] not allowed on number");
    }
    return this.value;
  }
  
  public boolean evalBoolean(String exp)
  {
    if (exp != null) {
      throw new IllegalArgumentException("expression [" + exp + "] not allowed on number");
    }
    return Boolean.parseBoolean(this.value);
  }
}
