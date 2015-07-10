package org.apache.logging.log4j.message;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.logging.log4j.util.EnglishEnums;

public class MapMessage
  implements MultiformatMessage
{
  private static final long serialVersionUID = -5031471831131487120L;
  private final SortedMap<String, String> data;
  
  public static enum MapFormat
  {
    XML,  JSON,  JAVA;
    
    private MapFormat() {}
  }
  
  public MapMessage()
  {
    this.data = new TreeMap();
  }
  
  public MapMessage(Map<String, String> map)
  {
    this.data = ((map instanceof SortedMap) ? (SortedMap)map : new TreeMap(map));
  }
  
  public String[] getFormats()
  {
    String[] formats = new String[MapFormat.values().length];
    int i = 0;
    for (MapFormat format : MapFormat.values()) {
      formats[(i++)] = format.name();
    }
    return formats;
  }
  
  public Object[] getParameters()
  {
    return this.data.values().toArray();
  }
  
  public String getFormat()
  {
    return "";
  }
  
  public Map<String, String> getData()
  {
    return Collections.unmodifiableMap(this.data);
  }
  
  public void clear()
  {
    this.data.clear();
  }
  
  public void put(String key, String value)
  {
    if (value == null) {
      throw new IllegalArgumentException("No value provided for key " + key);
    }
    validate(key, value);
    this.data.put(key, value);
  }
  
  protected void validate(String key, String value) {}
  
  public void putAll(Map<String, String> map)
  {
    this.data.putAll(map);
  }
  
  public String get(String key)
  {
    return (String)this.data.get(key);
  }
  
  public String remove(String key)
  {
    return (String)this.data.remove(key);
  }
  
  public String asString()
  {
    return asString((MapFormat)null);
  }
  
  public String asString(String format)
  {
    try
    {
      return asString((MapFormat)EnglishEnums.valueOf(MapFormat.class, format));
    }
    catch (IllegalArgumentException ex) {}
    return asString();
  }
  
  private String asString(MapFormat format)
  {
    StringBuilder sb = new StringBuilder();
    if (format == null) {
      appendMap(sb);
    } else {
      switch (format)
      {
      case XML: 
        asXML(sb);
        break;
      case JSON: 
        asJSON(sb);
        break;
      case JAVA: 
        asJava(sb);
        break;
      default: 
        appendMap(sb);
      }
    }
    return sb.toString();
  }
  
  public void asXML(StringBuilder sb)
  {
    sb.append("<Map>\n");
    for (Map.Entry<String, String> entry : this.data.entrySet()) {
      sb.append("  <Entry key=\"").append((String)entry.getKey()).append("\">").append((String)entry.getValue()).append("</Entry>\n");
    }
    sb.append("</Map>");
  }
  
  public String getFormattedMessage()
  {
    return asString();
  }
  
  public String getFormattedMessage(String[] formats)
  {
    if ((formats == null) || (formats.length == 0)) {
      return asString();
    }
    for (String format : formats) {
      for (MapFormat mapFormat : MapFormat.values()) {
        if (mapFormat.name().equalsIgnoreCase(format)) {
          return asString(mapFormat);
        }
      }
    }
    return asString();
  }
  
  protected void appendMap(StringBuilder sb)
  {
    boolean first = true;
    for (Map.Entry<String, String> entry : this.data.entrySet())
    {
      if (!first) {
        sb.append(" ");
      }
      first = false;
      sb.append((String)entry.getKey()).append("=\"").append((String)entry.getValue()).append("\"");
    }
  }
  
  protected void asJSON(StringBuilder sb)
  {
    boolean first = true;
    sb.append("{");
    for (Map.Entry<String, String> entry : this.data.entrySet())
    {
      if (!first) {
        sb.append(", ");
      }
      first = false;
      sb.append("\"").append((String)entry.getKey()).append("\":");
      sb.append("\"").append((String)entry.getValue()).append("\"");
    }
    sb.append("}");
  }
  
  protected void asJava(StringBuilder sb)
  {
    boolean first = true;
    sb.append("{");
    for (Map.Entry<String, String> entry : this.data.entrySet())
    {
      if (!first) {
        sb.append(", ");
      }
      first = false;
      sb.append((String)entry.getKey()).append("=\"").append((String)entry.getValue()).append("\"");
    }
    sb.append("}");
  }
  
  public MapMessage newInstance(Map<String, String> map)
  {
    return new MapMessage(map);
  }
  
  public String toString()
  {
    return asString();
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    MapMessage that = (MapMessage)o;
    
    return this.data.equals(that.data);
  }
  
  public int hashCode()
  {
    return this.data.hashCode();
  }
  
  public Throwable getThrowable()
  {
    return null;
  }
}
