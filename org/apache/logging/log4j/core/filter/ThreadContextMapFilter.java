package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.Message;

@Plugin(name="ThreadContextMapFilter", category="Core", elementType="filter", printObject=true)
public class ThreadContextMapFilter
  extends MapFilter
{
  private final String key;
  private final String value;
  private final boolean useMap;
  
  public ThreadContextMapFilter(Map<String, List<String>> pairs, boolean oper, Filter.Result onMatch, Filter.Result onMismatch)
  {
    super(pairs, oper, onMatch, onMismatch);
    if (pairs.size() == 1)
    {
      Iterator<Map.Entry<String, List<String>>> iter = pairs.entrySet().iterator();
      Map.Entry<String, List<String>> entry = (Map.Entry)iter.next();
      if (((List)entry.getValue()).size() == 1)
      {
        this.key = ((String)entry.getKey());
        this.value = ((String)((List)entry.getValue()).get(0));
        this.useMap = false;
      }
      else
      {
        this.key = null;
        this.value = null;
        this.useMap = true;
      }
    }
    else
    {
      this.key = null;
      this.value = null;
      this.useMap = true;
    }
  }
  
  public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String msg, Object... params)
  {
    return filter();
  }
  
  public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Object msg, Throwable t)
  {
    return filter();
  }
  
  public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Message msg, Throwable t)
  {
    return filter();
  }
  
  private Filter.Result filter()
  {
    boolean match = false;
    if (this.useMap) {
      for (Map.Entry<String, List<String>> entry : getMap().entrySet())
      {
        String toMatch = ThreadContext.get((String)entry.getKey());
        if (toMatch != null) {
          match = ((List)entry.getValue()).contains(toMatch);
        } else {
          match = false;
        }
        if (((!isAnd()) && (match)) || ((isAnd()) && (!match))) {
          break;
        }
      }
    } else {
      match = this.value.equals(ThreadContext.get(this.key));
    }
    return match ? this.onMatch : this.onMismatch;
  }
  
  public Filter.Result filter(LogEvent event)
  {
    return super.filter(event.getContextMap()) ? this.onMatch : this.onMismatch;
  }
  
  @PluginFactory
  public static ThreadContextMapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") String match, @PluginAttribute("onMismatch") String mismatch)
  {
    if ((pairs == null) || (pairs.length == 0))
    {
      LOGGER.error("key and value pairs must be specified for the ThreadContextMapFilter");
      return null;
    }
    Map<String, List<String>> map = new HashMap();
    for (KeyValuePair pair : pairs)
    {
      String key = pair.getKey();
      if (key == null)
      {
        LOGGER.error("A null key is not valid in MapFilter");
      }
      else
      {
        String value = pair.getValue();
        if (value == null)
        {
          LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
        }
        else
        {
          List<String> list = (List)map.get(pair.getKey());
          if (list != null)
          {
            list.add(value);
          }
          else
          {
            list = new ArrayList();
            list.add(value);
            map.put(pair.getKey(), list);
          }
        }
      }
    }
    if (map.size() == 0)
    {
      LOGGER.error("ThreadContextMapFilter is not configured with any valid key value pairs");
      return null;
    }
    boolean isAnd = (oper == null) || (!oper.equalsIgnoreCase("or"));
    Filter.Result onMatch = Filter.Result.toResult(match);
    Filter.Result onMismatch = Filter.Result.toResult(mismatch);
    return new ThreadContextMapFilter(map, isAnd, onMatch, onMismatch);
  }
}
