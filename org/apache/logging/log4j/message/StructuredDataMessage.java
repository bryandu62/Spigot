package org.apache.logging.log4j.message;

import java.util.Map;
import org.apache.logging.log4j.util.EnglishEnums;

public class StructuredDataMessage
  extends MapMessage
{
  private static final long serialVersionUID = 1703221292892071920L;
  private static final int MAX_LENGTH = 32;
  private static final int HASHVAL = 31;
  private StructuredDataId id;
  private String message;
  private String type;
  
  public static enum Format
  {
    XML,  FULL;
    
    private Format() {}
  }
  
  public StructuredDataMessage(String id, String msg, String type)
  {
    this.id = new StructuredDataId(id, null, null);
    this.message = msg;
    this.type = type;
  }
  
  public StructuredDataMessage(String id, String msg, String type, Map<String, String> data)
  {
    super(data);
    this.id = new StructuredDataId(id, null, null);
    this.message = msg;
    this.type = type;
  }
  
  public StructuredDataMessage(StructuredDataId id, String msg, String type)
  {
    this.id = id;
    this.message = msg;
    this.type = type;
  }
  
  public StructuredDataMessage(StructuredDataId id, String msg, String type, Map<String, String> data)
  {
    super(data);
    this.id = id;
    this.message = msg;
    this.type = type;
  }
  
  private StructuredDataMessage(StructuredDataMessage msg, Map<String, String> map)
  {
    super(map);
    this.id = msg.id;
    this.message = msg.message;
    this.type = msg.type;
  }
  
  protected StructuredDataMessage() {}
  
  public String[] getFormats()
  {
    String[] formats = new String[Format.values().length];
    int i = 0;
    for (Format format : Format.values()) {
      formats[(i++)] = format.name();
    }
    return formats;
  }
  
  public StructuredDataId getId()
  {
    return this.id;
  }
  
  protected void setId(String id)
  {
    this.id = new StructuredDataId(id, null, null);
  }
  
  protected void setId(StructuredDataId id)
  {
    this.id = id;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  protected void setType(String type)
  {
    if (type.length() > 32) {
      throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + type);
    }
    this.type = type;
  }
  
  public String getFormat()
  {
    return this.message;
  }
  
  protected void setMessageFormat(String msg)
  {
    this.message = msg;
  }
  
  protected void validate(String key, String value)
  {
    validateKey(key);
  }
  
  private void validateKey(String key)
  {
    if (key.length() > 32) {
      throw new IllegalArgumentException("Structured data keys are limited to 32 characters. key: " + key);
    }
    char[] chars = key.toCharArray();
    for (char c : chars) {
      if ((c < '!') || (c > '~') || (c == '=') || (c == ']') || (c == '"')) {
        throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \"");
      }
    }
  }
  
  public String asString()
  {
    return asString(Format.FULL, null);
  }
  
  public String asString(String format)
  {
    try
    {
      return asString((Format)EnglishEnums.valueOf(Format.class, format), null);
    }
    catch (IllegalArgumentException ex) {}
    return asString();
  }
  
  public final String asString(Format format, StructuredDataId structuredDataId)
  {
    StringBuilder sb = new StringBuilder();
    boolean full = Format.FULL.equals(format);
    if (full)
    {
      String type = getType();
      if (type == null) {
        return sb.toString();
      }
      sb.append(getType()).append(" ");
    }
    StructuredDataId id = getId();
    if (id != null) {
      id = id.makeId(structuredDataId);
    } else {
      id = structuredDataId;
    }
    if ((id == null) || (id.getName() == null)) {
      return sb.toString();
    }
    sb.append("[");
    sb.append(id);
    sb.append(" ");
    appendMap(sb);
    sb.append("]");
    if (full)
    {
      String msg = getFormat();
      if (msg != null) {
        sb.append(" ").append(msg);
      }
    }
    return sb.toString();
  }
  
  public String getFormattedMessage()
  {
    return asString(Format.FULL, null);
  }
  
  public String getFormattedMessage(String[] formats)
  {
    if ((formats != null) && (formats.length > 0))
    {
      for (String format : formats)
      {
        if (Format.XML.name().equalsIgnoreCase(format)) {
          return asXML();
        }
        if (Format.FULL.name().equalsIgnoreCase(format)) {
          return asString(Format.FULL, null);
        }
      }
      return asString(null, null);
    }
    return asString(Format.FULL, null);
  }
  
  private String asXML()
  {
    StringBuilder sb = new StringBuilder();
    StructuredDataId id = getId();
    if ((id == null) || (id.getName() == null) || (this.type == null)) {
      return sb.toString();
    }
    sb.append("<StructuredData>\n");
    sb.append("<type>").append(this.type).append("</type>\n");
    sb.append("<id>").append(id).append("</id>\n");
    super.asXML(sb);
    sb.append("</StructuredData>\n");
    return sb.toString();
  }
  
  public String toString()
  {
    return asString(null, null);
  }
  
  public MapMessage newInstance(Map<String, String> map)
  {
    return new StructuredDataMessage(this, map);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    StructuredDataMessage that = (StructuredDataMessage)o;
    if (!super.equals(o)) {
      return false;
    }
    if (this.type != null ? !this.type.equals(that.type) : that.type != null) {
      return false;
    }
    if (this.id != null ? !this.id.equals(that.id) : that.id != null) {
      return false;
    }
    if (this.message != null ? !this.message.equals(that.message) : that.message != null) {
      return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = super.hashCode();
    result = 31 * result + (this.type != null ? this.type.hashCode() : 0);
    result = 31 * result + (this.id != null ? this.id.hashCode() : 0);
    result = 31 * result + (this.message != null ? this.message.hashCode() : 0);
    return result;
  }
}
