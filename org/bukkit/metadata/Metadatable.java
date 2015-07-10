package org.bukkit.metadata;

import java.util.List;
import org.bukkit.plugin.Plugin;

public abstract interface Metadatable
{
  public abstract void setMetadata(String paramString, MetadataValue paramMetadataValue);
  
  public abstract List<MetadataValue> getMetadata(String paramString);
  
  public abstract boolean hasMetadata(String paramString);
  
  public abstract void removeMetadata(String paramString, Plugin paramPlugin);
}
