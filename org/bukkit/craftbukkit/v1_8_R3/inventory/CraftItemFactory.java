package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CraftItemFactory
  implements ItemFactory
{
  static final Color DEFAULT_LEATHER_COLOR = Color.fromRGB(10511680);
  private static final CraftItemFactory instance = new CraftItemFactory();
  
  static
  {
    ConfigurationSerialization.registerClass(CraftMetaItem.SerializableMeta.class);
  }
  
  static final Collection<String> KNOWN_NBT_ATTRIBUTE_NAMES = ImmutableSet.builder()
    .add("generic.attackDamage")
    .add("generic.followRange")
    .add("generic.knockbackResistance")
    .add("generic.maxHealth")
    .add("generic.movementSpeed")
    .add("horse.jumpStrength")
    .add("zombie.spawnReinforcements")
    .build();
  
  public boolean isApplicable(ItemMeta meta, ItemStack itemstack)
  {
    if (itemstack == null) {
      return false;
    }
    return isApplicable(meta, itemstack.getType());
  }
  
  public boolean isApplicable(ItemMeta meta, Material type)
  {
    if ((type == null) || (meta == null)) {
      return false;
    }
    if (!(meta instanceof CraftMetaItem)) {
      throw new IllegalArgumentException("Meta of " + meta.getClass().toString() + " not created by " + CraftItemFactory.class.getName());
    }
    return ((CraftMetaItem)meta).applicableTo(type);
  }
  
  public ItemMeta getItemMeta(Material material)
  {
    Validate.notNull(material, "Material cannot be null");
    return getItemMeta(material, null);
  }
  
  private ItemMeta getItemMeta(Material material, CraftMetaItem meta)
  {
    switch (material)
    {
    case ACACIA_DOOR: 
      return null;
    case STAINED_CLAY: 
      return (meta instanceof CraftMetaBookSigned) ? meta : new CraftMetaBookSigned(meta);
    case SPRUCE_WOOD_STAIRS: 
      return (meta != null) && (meta.getClass().equals(CraftMetaBook.class)) ? meta : new CraftMetaBook(meta);
    case STONE_BUTTON: 
      return (meta instanceof CraftMetaSkull) ? meta : new CraftMetaSkull(meta);
    case PACKED_ICE: 
    case PAINTING: 
    case PAPER: 
    case PISTON_BASE: 
      return (meta instanceof CraftMetaLeatherArmor) ? meta : new CraftMetaLeatherArmor(meta);
    case SMOOTH_STAIRS: 
      return (meta instanceof CraftMetaPotion) ? meta : new CraftMetaPotion(meta);
    case SADDLE: 
      return (meta instanceof CraftMetaMap) ? meta : new CraftMetaMap(meta);
    case STONE_SLAB2: 
      return (meta instanceof CraftMetaFirework) ? meta : new CraftMetaFirework(meta);
    case STONE_SPADE: 
      return (meta instanceof CraftMetaCharge) ? meta : new CraftMetaCharge(meta);
    case STONE_SWORD: 
      return (meta instanceof CraftMetaEnchantedBook) ? meta : new CraftMetaEnchantedBook(meta);
    case WHEAT: 
      return (meta instanceof CraftMetaBanner) ? meta : new CraftMetaBanner(meta);
    case BLAZE_POWDER: 
    case BOAT: 
    case BREWING_STAND: 
    case CHAINMAIL_LEGGINGS: 
    case CLAY: 
    case COBBLESTONE_STAIRS: 
    case DIAMOND_AXE: 
    case ENDER_PORTAL_FRAME: 
    case GLASS_BOTTLE: 
    case GLOWING_REDSTONE_ORE: 
    case GOLD_BOOTS: 
    case GOLD_LEGGINGS: 
    case GOLD_PICKAXE: 
    case GOLD_SWORD: 
    case IRON_FENCE: 
    case QUARTZ_STAIRS: 
    case SPECKLED_MELON: 
    case STANDING_BANNER: 
    case STORAGE_MINECART: 
      return new CraftMetaBlockState(meta, material);
    }
    return new CraftMetaItem(meta);
  }
  
  public boolean equals(ItemMeta meta1, ItemMeta meta2)
  {
    if (meta1 == meta2) {
      return true;
    }
    if ((meta1 != null) && (!(meta1 instanceof CraftMetaItem))) {
      throw new IllegalArgumentException("First meta of " + meta1.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
    }
    if ((meta2 != null) && (!(meta2 instanceof CraftMetaItem))) {
      throw new IllegalArgumentException("Second meta " + meta2.getClass().getName() + " does not belong to " + CraftItemFactory.class.getName());
    }
    if (meta1 == null) {
      return ((CraftMetaItem)meta2).isEmpty();
    }
    if (meta2 == null) {
      return ((CraftMetaItem)meta1).isEmpty();
    }
    return equals((CraftMetaItem)meta1, (CraftMetaItem)meta2);
  }
  
  boolean equals(CraftMetaItem meta1, CraftMetaItem meta2)
  {
    return (meta1.equalsCommon(meta2)) && (meta1.notUncommon(meta2)) && (meta2.notUncommon(meta1));
  }
  
  public static CraftItemFactory instance()
  {
    return instance;
  }
  
  public ItemMeta asMetaFor(ItemMeta meta, ItemStack stack)
  {
    Validate.notNull(stack, "Stack cannot be null");
    return asMetaFor(meta, stack.getType());
  }
  
  public ItemMeta asMetaFor(ItemMeta meta, Material material)
  {
    Validate.notNull(material, "Material cannot be null");
    if (!(meta instanceof CraftMetaItem)) {
      throw new IllegalArgumentException("Meta of " + (meta != null ? meta.getClass().toString() : "null") + " not created by " + CraftItemFactory.class.getName());
    }
    return getItemMeta(material, (CraftMetaItem)meta);
  }
  
  public Color getDefaultLeatherColor()
  {
    return DEFAULT_LEATHER_COLOR;
  }
}
