package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataId;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(name="StructuredDataFilter", category="Core", elementType="filter", printObject=true)
public final class StructuredDataFilter
  extends MapFilter
{
  private StructuredDataFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch)
  {
    super(map, oper, onMatch, onMismatch);
  }
  
  public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Message msg, Throwable t)
  {
    if ((msg instanceof StructuredDataMessage)) {
      return filter((StructuredDataMessage)msg);
    }
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(LogEvent event)
  {
    Message msg = event.getMessage();
    if ((msg instanceof StructuredDataMessage)) {
      return filter((StructuredDataMessage)msg);
    }
    return super.filter(event);
  }
  
  protected Filter.Result filter(StructuredDataMessage message)
  {
    boolean match = false;
    for (Map.Entry<String, List<String>> entry : getMap().entrySet())
    {
      String toMatch = getValue(message, (String)entry.getKey());
      if (toMatch != null) {
        match = ((List)entry.getValue()).contains(toMatch);
      } else {
        match = false;
      }
      if (((!isAnd()) && (match)) || ((isAnd()) && (!match))) {
        break;
      }
    }
    return match ? this.onMatch : this.onMismatch;
  }
  
  private String getValue(StructuredDataMessage data, String key)
  {
    if (key.equalsIgnoreCase("id")) {
      return data.getId().toString();
    }
    if (key.equalsIgnoreCase("id.name")) {
      return data.getId().getName();
    }
    if (key.equalsIgnoreCase("type")) {
      return data.getType();
    }
    if (key.equalsIgnoreCase("message")) {
      return data.getFormattedMessage();
    }
    return (String)data.getData().get(key);
  }
  
  @PluginFactory
  public static StructuredDataFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") String match, @PluginAttribute("onMismatch") String mismatch)
  {
    if ((pairs == null) || (pairs.length == 0))
    {
      LOGGER.error("keys and values must be specified for the StructuredDataFilter");
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
      LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
      return null;
    }
    boolean isAnd = (oper == null) || (!oper.equalsIgnoreCase("or"));
    Filter.Result onMatch = Filter.Result.toResult(match);
    Filter.Result onMismatch = Filter.Result.toResult(mismatch);
    return new StructuredDataFilter(map, isAnd, onMatch, onMismatch);
  }
}
