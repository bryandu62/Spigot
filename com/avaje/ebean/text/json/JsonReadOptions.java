package com.avaje.ebean.text.json;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonReadOptions
{
  protected JsonValueAdapter valueAdapter;
  protected Map<String, JsonReadBeanVisitor<?>> visitorMap;
  
  public JsonReadOptions()
  {
    this.visitorMap = new LinkedHashMap();
  }
  
  public JsonValueAdapter getValueAdapter()
  {
    return this.valueAdapter;
  }
  
  public Map<String, JsonReadBeanVisitor<?>> getVisitorMap()
  {
    return this.visitorMap;
  }
  
  public JsonReadOptions setValueAdapter(JsonValueAdapter valueAdapter)
  {
    this.valueAdapter = valueAdapter;
    return this;
  }
  
  public JsonReadOptions addRootVisitor(JsonReadBeanVisitor<?> visitor)
  {
    return addVisitor(null, visitor);
  }
  
  public JsonReadOptions addVisitor(String path, JsonReadBeanVisitor<?> visitor)
  {
    this.visitorMap.put(path, visitor);
    return this;
  }
}
