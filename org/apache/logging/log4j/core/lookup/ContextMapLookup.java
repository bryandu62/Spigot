package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name="ctx", category="Lookup")
public class ContextMapLookup
  implements StrLookup
{
  public String lookup(String key)
  {
    return ThreadContext.get(key);
  }
  
  public String lookup(LogEvent event, String key)
  {
    return (String)event.getContextMap().get(key);
  }
}
