package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.MapMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaMap
  extends CraftMetaItem
  implements MapMeta
{
  static final CraftMetaItem.ItemMetaKey MAP_SCALING = new CraftMetaItem.ItemMetaKey("map_is_scaling", "scaling");
  static final byte SCALING_EMPTY = 0;
  static final byte SCALING_TRUE = 1;
  static final byte SCALING_FALSE = 2;
  private byte scaling = 0;
  
  CraftMetaMap(CraftMetaItem meta)
  {
    super(meta);
    if (!(meta instanceof CraftMetaMap)) {
      return;
    }
    CraftMetaMap map = (CraftMetaMap)meta;
    this.scaling = map.scaling;
  }
  
  CraftMetaMap(NBTTagCompound tag)
  {
    super(tag);
    if (tag.hasKey(MAP_SCALING.NBT)) {
      this.scaling = (tag.getBoolean(MAP_SCALING.NBT) ? 1 : 2);
    }
  }
  
  CraftMetaMap(Map<String, Object> map)
  {
    super(map);
    
    Boolean scaling = (Boolean)CraftMetaItem.SerializableMeta.getObject(Boolean.class, map, MAP_SCALING.BUKKIT, true);
    if (scaling != null) {
      setScaling(scaling.booleanValue());
    }
  }
  
  void applyToItem(NBTTagCompound tag)
  {
    super.applyToItem(tag);
    if (hasScaling()) {
      tag.setBoolean(MAP_SCALING.NBT, isScaling());
    }
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case SADDLE: 
      return true;
    }
    return false;
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (isMapEmpty());
  }
  
  boolean isMapEmpty()
  {
    return !hasScaling();
  }
  
  boolean hasScaling()
  {
    return this.scaling != 0;
  }
  
  public boolean isScaling()
  {
    return this.scaling == 1;
  }
  
  public void setScaling(boolean scaling)
  {
    this.scaling = (scaling ? 1 : 2);
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaMap))
    {
      CraftMetaMap that = (CraftMetaMap)meta;
      
      return this.scaling == that.scaling;
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaMap)) || (isMapEmpty()));
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasScaling()) {
      hash ^= 572662306 << (isScaling() ? 1 : -1);
    }
    return original != hash ? CraftMetaMap.class.hashCode() ^ hash : hash;
  }
  
  public CraftMetaMap clone()
  {
    return (CraftMetaMap)super.clone();
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (hasScaling()) {
      builder.put(MAP_SCALING.BUKKIT, Boolean.valueOf(isScaling()));
    }
    return builder;
  }
}
