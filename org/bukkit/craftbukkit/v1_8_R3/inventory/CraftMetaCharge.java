package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.FireworkEffectMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaCharge
  extends CraftMetaItem
  implements FireworkEffectMeta
{
  static final CraftMetaItem.ItemMetaKey EXPLOSION = new CraftMetaItem.ItemMetaKey("Explosion", "firework-effect");
  private FireworkEffect effect;
  
  CraftMetaCharge(CraftMetaItem meta)
  {
    super(meta);
    if ((meta instanceof CraftMetaCharge)) {
      this.effect = ((CraftMetaCharge)meta).effect;
    }
  }
  
  CraftMetaCharge(Map<String, Object> map)
  {
    super(map);
    
    setEffect((FireworkEffect)CraftMetaItem.SerializableMeta.getObject(FireworkEffect.class, map, EXPLOSION.BUKKIT, true));
  }
  
  CraftMetaCharge(NBTTagCompound tag)
  {
    super(tag);
    if (tag.hasKey(EXPLOSION.NBT)) {
      this.effect = CraftMetaFirework.getEffect(tag.getCompound(EXPLOSION.NBT));
    }
  }
  
  public void setEffect(FireworkEffect effect)
  {
    this.effect = effect;
  }
  
  public boolean hasEffect()
  {
    return this.effect != null;
  }
  
  public FireworkEffect getEffect()
  {
    return this.effect;
  }
  
  void applyToItem(NBTTagCompound itemTag)
  {
    super.applyToItem(itemTag);
    if (hasEffect()) {
      itemTag.set(EXPLOSION.NBT, CraftMetaFirework.getExplosion(this.effect));
    }
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case STONE_SPADE: 
      return true;
    }
    return false;
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (!hasChargeMeta());
  }
  
  boolean hasChargeMeta()
  {
    return hasEffect();
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaCharge))
    {
      CraftMetaCharge that = (CraftMetaCharge)meta;
      
      return (that.hasEffect()) && (this.effect.equals(that.effect));
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaCharge)) || (!hasChargeMeta()));
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasEffect()) {
      hash = 61 * hash + this.effect.hashCode();
    }
    return hash != original ? CraftMetaCharge.class.hashCode() ^ hash : hash;
  }
  
  public CraftMetaCharge clone()
  {
    return (CraftMetaCharge)super.clone();
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (hasEffect()) {
      builder.put(EXPLOSION.BUKKIT, this.effect);
    }
    return builder;
  }
}
