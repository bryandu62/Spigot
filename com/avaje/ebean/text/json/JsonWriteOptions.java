package com.avaje.ebean.text.json;

import com.avaje.ebean.text.PathProperties;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class JsonWriteOptions
{
  protected String callback;
  protected JsonValueAdapter valueAdapter;
  protected Map<String, JsonWriteBeanVisitor<?>> visitorMap;
  protected PathProperties pathProperties;
  
  public static JsonWriteOptions parsePath(String pathProperties)
  {
    PathProperties p = PathProperties.parse(pathProperties);
    JsonWriteOptions o = new JsonWriteOptions();
    o.setPathProperties(p);
    return o;
  }
  
  public JsonWriteOptions copy()
  {
    JsonWriteOptions copy = new JsonWriteOptions();
    copy.callback = this.callback;
    copy.valueAdapter = this.valueAdapter;
    copy.pathProperties = this.pathProperties;
    if (this.visitorMap != null) {
      copy.visitorMap = new HashMap(this.visitorMap);
    }
    return copy;
  }
  
  public String getCallback()
  {
    return this.callback;
  }
  
  public JsonWriteOptions setCallback(String callback)
  {
    this.callback = callback;
    return this;
  }
  
  public JsonValueAdapter getValueAdapter()
  {
    return this.valueAdapter;
  }
  
  public JsonWriteOptions setValueAdapter(JsonValueAdapter valueAdapter)
  {
    this.valueAdapter = valueAdapter;
    return this;
  }
  
  public JsonWriteOptions setRootPathVisitor(JsonWriteBeanVisitor<?> visitor)
  {
    return setPathVisitor(null, visitor);
  }
  
  public JsonWriteOptions setPathVisitor(String path, JsonWriteBeanVisitor<?> visitor)
  {
    if (this.visitorMap == null) {
      this.visitorMap = new HashMap();
    }
    this.visitorMap.put(path, visitor);
    return this;
  }
  
  public JsonWriteOptions setPathProperties(String path, Set<String> propertiesToInclude)
  {
    if (this.pathProperties == null) {
      this.pathProperties = new PathProperties();
    }
    this.pathProperties.put(path, propertiesToInclude);
    return this;
  }
  
  public JsonWriteOptions setPathProperties(String path, String propertiesToInclude)
  {
    return setPathProperties(path, parseProps(propertiesToInclude));
  }
  
  public JsonWriteOptions setRootPathProperties(String propertiesToInclude)
  {
    return setPathProperties(null, parseProps(propertiesToInclude));
  }
  
  public JsonWriteOptions setRootPathProperties(Set<String> propertiesToInclude)
  {
    return setPathProperties(null, propertiesToInclude);
  }
  
  private Set<String> parseProps(String propertiesToInclude)
  {
    LinkedHashSet<String> props = new LinkedHashSet();
    
    String[] split = propertiesToInclude.split(",");
    for (int i = 0; i < split.length; i++)
    {
      String s = split[i].trim();
      if (s.length() > 0) {
        props.add(s);
      }
    }
    return props;
  }
  
  public Map<String, JsonWriteBeanVisitor<?>> getVisitorMap()
  {
    return this.visitorMap;
  }
  
  public void setPathProperties(PathProperties pathProperties)
  {
    this.pathProperties = pathProperties;
  }
  
  public PathProperties getPathProperties()
  {
    return this.pathProperties;
  }
}
