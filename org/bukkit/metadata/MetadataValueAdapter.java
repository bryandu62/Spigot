package org.bukkit.metadata;

import java.lang.ref.WeakReference;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

public abstract class MetadataValueAdapter
  implements MetadataValue
{
  protected final WeakReference<Plugin> owningPlugin;
  
  protected MetadataValueAdapter(Plugin owningPlugin)
  {
    Validate.notNull(owningPlugin, "owningPlugin cannot be null");
    this.owningPlugin = new WeakReference(owningPlugin);
  }
  
  public Plugin getOwningPlugin()
  {
    return (Plugin)this.owningPlugin.get();
  }
  
  public int asInt()
  {
    return NumberConversions.toInt(value());
  }
  
  public float asFloat()
  {
    return NumberConversions.toFloat(value());
  }
  
  public double asDouble()
  {
    return NumberConversions.toDouble(value());
  }
  
  public long asLong()
  {
    return NumberConversions.toLong(value());
  }
  
  public short asShort()
  {
    return NumberConversions.toShort(value());
  }
  
  public byte asByte()
  {
    return NumberConversions.toByte(value());
  }
  
  public boolean asBoolean()
  {
    Object value = value();
    if ((value instanceof Boolean)) {
      return ((Boolean)value).booleanValue();
    }
    if ((value instanceof Number)) {
      return ((Number)value).intValue() != 0;
    }
    if ((value instanceof String)) {
      return Boolean.parseBoolean((String)value);
    }
    return value != null;
  }
  
  public String asString()
  {
    Object value = value();
    if (value == null) {
      return "";
    }
    return value.toString();
  }
}
