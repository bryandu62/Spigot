package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebean.text.json.JsonElement;
import com.avaje.ebean.text.json.JsonElementArray;
import com.avaje.ebean.text.json.JsonElementBoolean;
import com.avaje.ebean.text.json.JsonElementNull;
import com.avaje.ebean.text.json.JsonElementNumber;
import com.avaje.ebean.text.json.JsonElementObject;
import com.avaje.ebean.text.json.JsonElementString;

public class ReadJsonRawReader
{
  private final ReadJsonInterface ctx;
  
  public static JsonElement readJsonElement(ReadJsonInterface ctx)
  {
    return new ReadJsonRawReader(ctx).readJsonElement();
  }
  
  private ReadJsonRawReader(ReadJsonInterface ctx)
  {
    this.ctx = ctx;
  }
  
  private JsonElement readJsonElement()
  {
    return readValue();
  }
  
  private JsonElement readValue()
  {
    this.ctx.ignoreWhiteSpace();
    
    char c = this.ctx.nextChar();
    switch (c)
    {
    case '{': 
      return readObject();
    case '[': 
      return readArray();
    case '"': 
      return readString();
    }
    return readUnquoted(c);
  }
  
  private JsonElement readArray()
  {
    JsonElementArray a = new JsonElementArray();
    for (;;)
    {
      JsonElement value = readValue();
      a.add(value);
      if (!this.ctx.readArrayNext()) {
        break;
      }
    }
    return a;
  }
  
  private JsonElement readObject()
  {
    JsonElementObject o = new JsonElementObject();
    while (this.ctx.readKeyNext())
    {
      String key = this.ctx.getTokenKey();
      JsonElement value = readValue();
      
      o.put(key, value);
      if (!this.ctx.readValueNext()) {
        break;
      }
    }
    return o;
  }
  
  private JsonElement readString()
  {
    String s = this.ctx.readQuotedValue();
    return new JsonElementString(s);
  }
  
  private JsonElement readUnquoted(char c)
  {
    String s = this.ctx.readUnquotedValue(c);
    if ("null".equals(s)) {
      return JsonElementNull.NULL;
    }
    if ("true".equals(s)) {
      return JsonElementBoolean.TRUE;
    }
    if ("false".equals(s)) {
      return JsonElementBoolean.FALSE;
    }
    return new JsonElementNumber(s);
  }
}
