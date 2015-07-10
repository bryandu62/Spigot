package org.apache.logging.log4j.core.lookup;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.status.StatusLogger;

public class Interpolator
  implements StrLookup
{
  private static final Logger LOGGER = ;
  private static final char PREFIX_SEPARATOR = ':';
  private final Map<String, StrLookup> lookups = new HashMap();
  private final StrLookup defaultLookup;
  
  public Interpolator(StrLookup defaultLookup)
  {
    this.defaultLookup = (defaultLookup == null ? new MapLookup(new HashMap()) : defaultLookup);
    PluginManager manager = new PluginManager("Lookup");
    manager.collectPlugins();
    Map<String, PluginType<?>> plugins = manager.getPlugins();
    for (Map.Entry<String, PluginType<?>> entry : plugins.entrySet())
    {
      Class<? extends StrLookup> clazz = ((PluginType)entry.getValue()).getPluginClass();
      try
      {
        this.lookups.put(entry.getKey(), clazz.newInstance());
      }
      catch (Exception ex)
      {
        LOGGER.error("Unable to create Lookup for " + (String)entry.getKey(), ex);
      }
    }
  }
  
  public Interpolator()
  {
    this.defaultLookup = new MapLookup(new HashMap());
    this.lookups.put("sys", new SystemPropertiesLookup());
    this.lookups.put("env", new EnvironmentLookup());
    this.lookups.put("jndi", new JndiLookup());
    try
    {
      if (Class.forName("javax.servlet.ServletContext") != null) {
        this.lookups.put("web", new WebLookup());
      }
    }
    catch (ClassNotFoundException ex)
    {
      LOGGER.debug("ServletContext not present - WebLookup not added");
    }
    catch (Exception ex)
    {
      LOGGER.error("Unable to locate ServletContext", ex);
    }
  }
  
  public String lookup(String var)
  {
    return lookup(null, var);
  }
  
  public String lookup(LogEvent event, String var)
  {
    if (var == null) {
      return null;
    }
    int prefixPos = var.indexOf(':');
    if (prefixPos >= 0)
    {
      String prefix = var.substring(0, prefixPos);
      String name = var.substring(prefixPos + 1);
      StrLookup lookup = (StrLookup)this.lookups.get(prefix);
      String value = null;
      if (lookup != null) {
        value = event == null ? lookup.lookup(name) : lookup.lookup(event, name);
      }
      if (value != null) {
        return value;
      }
      var = var.substring(prefixPos + 1);
    }
    if (this.defaultLookup != null) {
      return event == null ? this.defaultLookup.lookup(var) : this.defaultLookup.lookup(event, var);
    }
    return null;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (String name : this.lookups.keySet())
    {
      if (sb.length() == 0) {
        sb.append("{");
      } else {
        sb.append(", ");
      }
      sb.append(name);
    }
    if (sb.length() > 0) {
      sb.append("}");
    }
    return sb.toString();
  }
}
