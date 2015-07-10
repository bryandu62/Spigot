package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(name="map", category="Lookup")
public class MapLookup
  implements StrLookup
{
  private final Map<String, String> map;
  
  public MapLookup(Map<String, String> map)
  {
    this.map = map;
  }
  
  public MapLookup()
  {
    this.map = null;
  }
  
  public String lookup(String key)
  {
    if (this.map == null) {
      return null;
    }
    String obj = (String)this.map.get(key);
    if (obj == null) {
      return null;
    }
    return obj;
  }
  
  public String lookup(LogEvent event, String key)
  {
    if ((this.map == null) && (!(event.getMessage() instanceof MapMessage))) {
      return null;
    }
    if ((this.map != null) && (this.map.containsKey(key)))
    {
      String obj = (String)this.map.get(key);
      if (obj != null) {
        return obj;
      }
    }
    if ((event.getMessage() instanceof MapMessage)) {
      return ((MapMessage)event.getMessage()).get(key);
    }
    return null;
  }
}
