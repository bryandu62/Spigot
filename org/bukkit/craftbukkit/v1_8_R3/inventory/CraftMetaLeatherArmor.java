package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.LeatherArmorMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaLeatherArmor
  extends CraftMetaItem
  implements LeatherArmorMeta
{
  static final CraftMetaItem.ItemMetaKey COLOR = new CraftMetaItem.ItemMetaKey("color");
  private Color color = CraftItemFactory.DEFAULT_LEATHER_COLOR;
  
  CraftMetaLeatherArmor(CraftMetaItem meta)
  {
    super(meta);
    if (!(meta instanceof CraftMetaLeatherArmor)) {
      return;
    }
    CraftMetaLeatherArmor armorMeta = (CraftMetaLeatherArmor)meta;
    this.color = armorMeta.color;
  }
  
  CraftMetaLeatherArmor(NBTTagCompound tag)
  {
    super(tag);
    if (tag.hasKey(DISPLAY.NBT))
    {
      NBTTagCompound display = tag.getCompound(DISPLAY.NBT);
      if (display.hasKey(COLOR.NBT)) {
        this.color = Color.fromRGB(display.getInt(COLOR.NBT));
      }
    }
  }
  
  CraftMetaLeatherArmor(Map<String, Object> map)
  {
    super(map);
    setColor((Color)CraftMetaItem.SerializableMeta.getObject(Color.class, map, COLOR.BUKKIT, true));
  }
  
  void applyToItem(NBTTagCompound itemTag)
  {
    super.applyToItem(itemTag);
    if (hasColor()) {
      setDisplayTag(itemTag, COLOR.NBT, new NBTTagInt(this.color.asRGB()));
    }
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (isLeatherArmorEmpty());
  }
  
  boolean isLeatherArmorEmpty()
  {
    return !hasColor();
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case PACKED_ICE: 
    case PAINTING: 
    case PAPER: 
    case PISTON_BASE: 
      return true;
    }
    return false;
  }
  
  public CraftMetaLeatherArmor clone()
  {
    return (CraftMetaLeatherArmor)super.clone();
  }
  
  public Color getColor()
  {
    return this.color;
  }
  
  public void setColor(Color color)
  {
    this.color = (color == null ? CraftItemFactory.DEFAULT_LEATHER_COLOR : color);
  }
  
  boolean hasColor()
  {
    return !CraftItemFactory.DEFAULT_LEATHER_COLOR.equals(this.color);
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (hasColor()) {
      builder.put(COLOR.BUKKIT, this.color);
    }
    return builder;
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaLeatherArmor))
    {
      CraftMetaLeatherArmor that = (CraftMetaLeatherArmor)meta;
      
      return this.color.equals(that.color);
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaLeatherArmor)) || (isLeatherArmorEmpty()));
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasColor()) {
      hash ^= this.color.hashCode();
    }
    return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
  }
}
