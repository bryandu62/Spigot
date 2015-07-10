package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaEnchantedBook
  extends CraftMetaItem
  implements EnchantmentStorageMeta
{
  static final CraftMetaItem.ItemMetaKey STORED_ENCHANTMENTS = new CraftMetaItem.ItemMetaKey("StoredEnchantments", "stored-enchants");
  private Map<Enchantment, Integer> enchantments;
  
  CraftMetaEnchantedBook(CraftMetaItem meta)
  {
    super(meta);
    if (!(meta instanceof CraftMetaEnchantedBook)) {
      return;
    }
    CraftMetaEnchantedBook that = (CraftMetaEnchantedBook)meta;
    if (that.hasEnchants()) {
      this.enchantments = new HashMap(that.enchantments);
    }
  }
  
  CraftMetaEnchantedBook(NBTTagCompound tag)
  {
    super(tag);
    if (!tag.hasKey(STORED_ENCHANTMENTS.NBT)) {
      return;
    }
    this.enchantments = buildEnchantments(tag, STORED_ENCHANTMENTS);
  }
  
  CraftMetaEnchantedBook(Map<String, Object> map)
  {
    super(map);
    
    this.enchantments = buildEnchantments(map, STORED_ENCHANTMENTS);
  }
  
  void applyToItem(NBTTagCompound itemTag)
  {
    super.applyToItem(itemTag);
    
    applyEnchantments(this.enchantments, itemTag, STORED_ENCHANTMENTS);
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case STONE_SWORD: 
      return true;
    }
    return false;
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (isEnchantedEmpty());
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaEnchantedBook))
    {
      CraftMetaEnchantedBook that = (CraftMetaEnchantedBook)meta;
      
      return (that.hasStoredEnchants()) && (this.enchantments.equals(that.enchantments));
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaEnchantedBook)) || (isEnchantedEmpty()));
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasStoredEnchants()) {
      hash = 61 * hash + this.enchantments.hashCode();
    }
    return original != hash ? CraftMetaEnchantedBook.class.hashCode() ^ hash : hash;
  }
  
  public CraftMetaEnchantedBook clone()
  {
    CraftMetaEnchantedBook meta = (CraftMetaEnchantedBook)super.clone();
    if (this.enchantments != null) {
      meta.enchantments = new HashMap(this.enchantments);
    }
    return meta;
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    
    serializeEnchantments(this.enchantments, builder, STORED_ENCHANTMENTS);
    
    return builder;
  }
  
  boolean isEnchantedEmpty()
  {
    return !hasStoredEnchants();
  }
  
  public boolean hasStoredEnchant(Enchantment ench)
  {
    return (hasStoredEnchants()) && (this.enchantments.containsKey(ench));
  }
  
  public int getStoredEnchantLevel(Enchantment ench)
  {
    Integer level = hasStoredEnchants() ? (Integer)this.enchantments.get(ench) : null;
    if (level == null) {
      return 0;
    }
    return level.intValue();
  }
  
  public Map<Enchantment, Integer> getStoredEnchants()
  {
    return hasStoredEnchants() ? ImmutableMap.copyOf(this.enchantments) : ImmutableMap.of();
  }
  
  public boolean addStoredEnchant(Enchantment ench, int level, boolean ignoreRestrictions)
  {
    if (this.enchantments == null) {
      this.enchantments = new HashMap(4);
    }
    if ((ignoreRestrictions) || ((level >= ench.getStartLevel()) && (level <= ench.getMaxLevel())))
    {
      Integer old = (Integer)this.enchantments.put(ench, Integer.valueOf(level));
      return (old == null) || (old.intValue() != level);
    }
    return false;
  }
  
  public boolean removeStoredEnchant(Enchantment ench)
  {
    return (hasStoredEnchants()) && (this.enchantments.remove(ench) != null);
  }
  
  public boolean hasStoredEnchants()
  {
    return (this.enchantments != null) && (!this.enchantments.isEmpty());
  }
  
  public boolean hasConflictingStoredEnchant(Enchantment ench)
  {
    return checkConflictingEnchants(this.enchantments, ench);
  }
}
