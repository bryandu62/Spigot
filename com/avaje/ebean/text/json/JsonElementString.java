package com.avaje.ebean.text.json;

public class JsonElementString
  implements JsonElement
{
  private final String value;
  
  public JsonElementString(String value)
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
    return this.value;
  }
  
  public int evalInt(String exp)
  {
    if (exp != null) {
      throw new IllegalArgumentException("expression [" + exp + "] not allowed on number");
    }
    try
    {
      return Integer.parseInt(this.value);
    }
    catch (NumberFormatException e) {}
    return 0;
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
    return Boolean.parseBoolean(exp);
  }
}
