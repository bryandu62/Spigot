package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;

public abstract interface MetadataValue
{
  public abstract Object value();
  
  public abstract int asInt();
  
  public abstract float asFloat();
  
  public abstract double asDouble();
  
  public abstract long asLong();
  
  public abstract short asShort();
  
  public abstract byte asByte();
  
  public abstract boolean asBoolean();
  
  public abstract String asString();
  
  public abstract Plugin getOwningPlugin();
  
  public abstract void invalidate();
}
