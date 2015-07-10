package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

@DelegateDeserialization(org.bukkit.inventory.ItemStack.class)
public final class CraftItemStack
  extends org.bukkit.inventory.ItemStack
{
  net.minecraft.server.v1_8_R3.ItemStack handle;
  
  public static net.minecraft.server.v1_8_R3.ItemStack asNMSCopy(org.bukkit.inventory.ItemStack original)
  {
    if ((original instanceof CraftItemStack))
    {
      CraftItemStack stack = (CraftItemStack)original;
      return stack.handle == null ? null : stack.handle.cloneItemStack();
    }
    if ((original == null) || (original.getTypeId() <= 0)) {
      return null;
    }
    Item item = CraftMagicNumbers.getItem(original.getType());
    if (item == null) {
      return null;
    }
    net.minecraft.server.v1_8_R3.ItemStack stack = new net.minecraft.server.v1_8_R3.ItemStack(item, original.getAmount(), original.getDurability());
    if (original.hasItemMeta()) {
      setItemMeta(stack, original.getItemMeta());
    }
    return stack;
  }
  
  public static net.minecraft.server.v1_8_R3.ItemStack copyNMSStack(net.minecraft.server.v1_8_R3.ItemStack original, int amount)
  {
    net.minecraft.server.v1_8_R3.ItemStack stack = original.cloneItemStack();
    stack.count = amount;
    return stack;
  }
  
  public static org.bukkit.inventory.ItemStack asBukkitCopy(net.minecraft.server.v1_8_R3.ItemStack original)
  {
    if (original == null) {
      return new org.bukkit.inventory.ItemStack(Material.AIR);
    }
    org.bukkit.inventory.ItemStack stack = new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(original.getItem()), original.count, (short)original.getData());
    if (hasItemMeta(original)) {
      stack.setItemMeta(getItemMeta(original));
    }
    return stack;
  }
  
  public static CraftItemStack asCraftMirror(net.minecraft.server.v1_8_R3.ItemStack original)
  {
    return new CraftItemStack(original);
  }
  
  public static CraftItemStack asCraftCopy(org.bukkit.inventory.ItemStack original)
  {
    if ((original instanceof CraftItemStack))
    {
      CraftItemStack stack = (CraftItemStack)original;
      return new CraftItemStack(stack.handle == null ? null : stack.handle.cloneItemStack());
    }
    return new CraftItemStack(original);
  }
  
  public static CraftItemStack asNewCraftStack(Item item)
  {
    return asNewCraftStack(item, 1);
  }
  
  public static CraftItemStack asNewCraftStack(Item item, int amount)
  {
    return new CraftItemStack(CraftMagicNumbers.getMaterial(item), amount, (short)0, null);
  }
  
  private CraftItemStack(net.minecraft.server.v1_8_R3.ItemStack item)
  {
    this.handle = item;
  }
  
  private CraftItemStack(org.bukkit.inventory.ItemStack item)
  {
    this(item.getTypeId(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
  }
  
  private CraftItemStack(Material type, int amount, short durability, ItemMeta itemMeta)
  {
    setType(type);
    setAmount(amount);
    setDurability(durability);
    setItemMeta(itemMeta);
  }
  
  private CraftItemStack(int typeId, int amount, short durability, ItemMeta itemMeta)
  {
    this(Material.getMaterial(typeId), amount, durability, itemMeta);
  }
  
  public int getTypeId()
  {
    return this.handle != null ? CraftMagicNumbers.getId(this.handle.getItem()) : 0;
  }
  
  public void setTypeId(int type)
  {
    if (getTypeId() == type) {
      return;
    }
    if (type == 0)
    {
      this.handle = null;
    }
    else if (CraftMagicNumbers.getItem(type) == null)
    {
      this.handle = null;
    }
    else if (this.handle == null)
    {
      this.handle = new net.minecraft.server.v1_8_R3.ItemStack(CraftMagicNumbers.getItem(type), 1, 0);
    }
    else
    {
      this.handle.setItem(CraftMagicNumbers.getItem(type));
      if (hasItemMeta()) {
        setItemMeta(this.handle, getItemMeta(this.handle));
      }
    }
    setData(null);
  }
  
  public int getAmount()
  {
    return this.handle != null ? this.handle.count : 0;
  }
  
  public void setAmount(int amount)
  {
    if (this.handle == null) {
      return;
    }
    if (amount == 0) {
      this.handle = null;
    } else {
      this.handle.count = amount;
    }
  }
  
  public void setDurability(short durability)
  {
    if (this.handle != null) {
      this.handle.setData(durability);
    }
  }
  
  public short getDurability()
  {
    if (this.handle != null) {
      return (short)this.handle.getData();
    }
    return -1;
  }
  
  public int getMaxStackSize()
  {
    return this.handle == null ? Material.AIR.getMaxStackSize() : this.handle.getItem().getMaxStackSize();
  }
  
  public void addUnsafeEnchantment(Enchantment ench, int level)
  {
    Validate.notNull(ench, "Cannot add null enchantment");
    if (!makeTag(this.handle)) {
      return;
    }
    NBTTagList list = getEnchantmentList(this.handle);
    if (list == null)
    {
      list = new NBTTagList();
      this.handle.getTag().set(CraftMetaItem.ENCHANTMENTS.NBT, list);
    }
    int size = list.size();
    for (int i = 0; i < size; i++)
    {
      NBTTagCompound tag = list.get(i);
      short id = tag.getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
      if (id == ench.getId())
      {
        tag.setShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT, (short)level);
        return;
      }
    }
    NBTTagCompound tag = new NBTTagCompound();
    tag.setShort(CraftMetaItem.ENCHANTMENTS_ID.NBT, (short)ench.getId());
    tag.setShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT, (short)level);
    list.add(tag);
  }
  
  static boolean makeTag(net.minecraft.server.v1_8_R3.ItemStack item)
  {
    if (item == null) {
      return false;
    }
    if (item.getTag() == null) {
      item.setTag(new NBTTagCompound());
    }
    return true;
  }
  
  public boolean containsEnchantment(Enchantment ench)
  {
    return getEnchantmentLevel(ench) > 0;
  }
  
  public int getEnchantmentLevel(Enchantment ench)
  {
    Validate.notNull(ench, "Cannot find null enchantment");
    if (this.handle == null) {
      return 0;
    }
    return EnchantmentManager.getEnchantmentLevel(ench.getId(), this.handle);
  }
  
  public int removeEnchantment(Enchantment ench)
  {
    Validate.notNull(ench, "Cannot remove null enchantment");
    
    NBTTagList list = getEnchantmentList(this.handle);
    if (list == null) {
      return 0;
    }
    int index = Integer.MIN_VALUE;
    int level = Integer.MIN_VALUE;
    int size = list.size();
    for (int i = 0; i < size; i++)
    {
      NBTTagCompound enchantment = list.get(i);
      int id = 0xFFFF & enchantment.getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
      if (id == ench.getId())
      {
        index = i;
        level = 0xFFFF & enchantment.getShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
        break;
      }
    }
    if (index == Integer.MIN_VALUE) {
      return 0;
    }
    if (size == 1)
    {
      this.handle.getTag().remove(CraftMetaItem.ENCHANTMENTS.NBT);
      if (this.handle.getTag().isEmpty()) {
        this.handle.setTag(null);
      }
      return level;
    }
    NBTTagList listCopy = new NBTTagList();
    for (int i = 0; i < size; i++) {
      if (i != index) {
        listCopy.add(list.get(i));
      }
    }
    this.handle.getTag().set(CraftMetaItem.ENCHANTMENTS.NBT, listCopy);
    
    return level;
  }
  
  public Map<Enchantment, Integer> getEnchantments()
  {
    return getEnchantments(this.handle);
  }
  
  static Map<Enchantment, Integer> getEnchantments(net.minecraft.server.v1_8_R3.ItemStack item)
  {
    NBTTagList list = (item != null) && (item.hasEnchantments()) ? item.getEnchantments() : null;
    if ((list == null) || (list.size() == 0)) {
      return ImmutableMap.of();
    }
    ImmutableMap.Builder<Enchantment, Integer> result = ImmutableMap.builder();
    for (int i = 0; i < list.size(); i++)
    {
      int id = 0xFFFF & list.get(i).getShort(CraftMetaItem.ENCHANTMENTS_ID.NBT);
      int level = 0xFFFF & list.get(i).getShort(CraftMetaItem.ENCHANTMENTS_LVL.NBT);
      
      result.put(Enchantment.getById(id), Integer.valueOf(level));
    }
    return result.build();
  }
  
  static NBTTagList getEnchantmentList(net.minecraft.server.v1_8_R3.ItemStack item)
  {
    return (item != null) && (item.hasEnchantments()) ? item.getEnchantments() : null;
  }
  
  public CraftItemStack clone()
  {
    CraftItemStack itemStack = (CraftItemStack)super.clone();
    if (this.handle != null) {
      itemStack.handle = this.handle.cloneItemStack();
    }
    return itemStack;
  }
  
  public ItemMeta getItemMeta()
  {
    return getItemMeta(this.handle);
  }
  
  public static ItemMeta getItemMeta(net.minecraft.server.v1_8_R3.ItemStack item)
  {
    if (!hasItemMeta(item)) {
      return CraftItemFactory.instance().getItemMeta(getType(item));
    }
    switch (getType(item))
    {
    case STAINED_CLAY: 
      return new CraftMetaBookSigned(item.getTag());
    case SPRUCE_WOOD_STAIRS: 
      return new CraftMetaBook(item.getTag());
    case STONE_BUTTON: 
      return new CraftMetaSkull(item.getTag());
    case PACKED_ICE: 
    case PAINTING: 
    case PAPER: 
    case PISTON_BASE: 
      return new CraftMetaLeatherArmor(item.getTag());
    case SMOOTH_STAIRS: 
      return new CraftMetaPotion(item.getTag());
    case SADDLE: 
      return new CraftMetaMap(item.getTag());
    case STONE_SLAB2: 
      return new CraftMetaFirework(item.getTag());
    case STONE_SPADE: 
      return new CraftMetaCharge(item.getTag());
    case STONE_SWORD: 
      return new CraftMetaEnchantedBook(item.getTag());
    case WHEAT: 
      return new CraftMetaBanner(item.getTag());
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
      return new CraftMetaBlockState(item.getTag(), CraftMagicNumbers.getMaterial(item.getItem()));
    }
    return new CraftMetaItem(item.getTag());
  }
  
  static Material getType(net.minecraft.server.v1_8_R3.ItemStack item)
  {
    Material material = Material.getMaterial(item == null ? 0 : CraftMagicNumbers.getId(item.getItem()));
    return material == null ? Material.AIR : material;
  }
  
  public boolean setItemMeta(ItemMeta itemMeta)
  {
    return setItemMeta(this.handle, itemMeta);
  }
  
  public static boolean setItemMeta(net.minecraft.server.v1_8_R3.ItemStack item, ItemMeta itemMeta)
  {
    if (item == null) {
      return false;
    }
    if (CraftItemFactory.instance().equals(itemMeta, null))
    {
      item.setTag(null);
      return true;
    }
    if (!CraftItemFactory.instance().isApplicable(itemMeta, getType(item))) {
      return false;
    }
    itemMeta = CraftItemFactory.instance().asMetaFor(itemMeta, getType(item));
    if (itemMeta == null) {
      return true;
    }
    NBTTagCompound tag = new NBTTagCompound();
    item.setTag(tag);
    
    ((CraftMetaItem)itemMeta).applyToItem(tag);
    
    return true;
  }
  
  public boolean isSimilar(org.bukkit.inventory.ItemStack stack)
  {
    if (stack == null) {
      return false;
    }
    if (stack == this) {
      return true;
    }
    if (!(stack instanceof CraftItemStack)) {
      return (stack.getClass() == org.bukkit.inventory.ItemStack.class) && (stack.isSimilar(this));
    }
    CraftItemStack that = (CraftItemStack)stack;
    if (this.handle == that.handle) {
      return true;
    }
    if ((this.handle == null) || (that.handle == null)) {
      return false;
    }
    if ((that.getTypeId() != getTypeId()) || (getDurability() != that.getDurability())) {
      return false;
    }
    return (that.hasItemMeta()) && (this.handle.getTag().equals(that.handle.getTag()));
  }
  
  public boolean hasItemMeta()
  {
    return hasItemMeta(this.handle);
  }
  
  static boolean hasItemMeta(net.minecraft.server.v1_8_R3.ItemStack item)
  {
    return (item != null) && (item.getTag() != null) && (!item.getTag().isEmpty());
  }
}
