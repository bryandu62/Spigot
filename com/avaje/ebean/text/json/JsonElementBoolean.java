package com.avaje.ebean.text.json;

public class JsonElementBoolean
  implements JsonElement
{
  public static final JsonElementBoolean TRUE = new JsonElementBoolean(Boolean.valueOf(true));
  public static final JsonElementBoolean FALSE = new JsonElementBoolean(Boolean.valueOf(false));
  private final Boolean value;
  
  private JsonElementBoolean(Boolean value)
  {
    this.value = value;
  }
  
  public Boolean getValue()
  {
    return this.value;
  }
  
  public String toString()
  {
    return Boolean.toString(this.value.booleanValue());
  }
  
  public boolean isPrimitive()
  {
    return true;
  }
  
  public String toPrimitiveString()
  {
    return this.value.toString();
  }
  
  public Object eval(String exp)
  {
    if (exp != null) {
      throw new IllegalArgumentException("expression [" + exp + "] not allowed on boolean");
    }
    return this.value;
  }
  
  public int evalInt(String exp)
  {
    return this.value.booleanValue() ? 1 : 0;
  }
  
  public String evalString(String exp)
  {
    return toString();
  }
  
  public boolean evalBoolean(String exp)
  {
    return this.value.booleanValue();
  }
}
