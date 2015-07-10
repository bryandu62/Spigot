package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IAttribute;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.v1_8_R3.Overridden;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.ItemMeta.Spigot;
import org.bukkit.inventory.meta.Repairable;
import org.spigotmc.ValidateUtils;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaItem
  implements ItemMeta, Repairable
{
  static class ItemMetaKey
  {
    final String BUKKIT;
    final String NBT;
    
    @Retention(RetentionPolicy.SOURCE)
    @Target({java.lang.annotation.ElementType.FIELD})
    static @interface Specific
    {
      To value();
      
      public static enum To
      {
        BUKKIT,  NBT;
      }
    }
    
    ItemMetaKey(String both)
    {
      this(both, both);
    }
    
    ItemMetaKey(String nbt, String bukkit)
    {
      this.NBT = nbt;
      this.BUKKIT = bukkit;
    }
  }
  
  @SerializableAs("ItemMeta")
  public static class SerializableMeta
    implements ConfigurationSerializable
  {
    static final String TYPE_FIELD = "meta-type";
    static final ImmutableMap<Class<? extends CraftMetaItem>, String> classMap = ImmutableMap.builder()
      .put(CraftMetaBanner.class, "BANNER")
      .put(CraftMetaBlockState.class, "TILE_ENTITY")
      .put(CraftMetaBook.class, "BOOK")
      .put(CraftMetaBookSigned.class, "BOOK_SIGNED")
      .put(CraftMetaSkull.class, "SKULL")
      .put(CraftMetaLeatherArmor.class, "LEATHER_ARMOR")
      .put(CraftMetaMap.class, "MAP")
      .put(CraftMetaPotion.class, "POTION")
      .put(CraftMetaEnchantedBook.class, "ENCHANTED")
      .put(CraftMetaFirework.class, "FIREWORK")
      .put(CraftMetaCharge.class, "FIREWORK_EFFECT")
      .put(CraftMetaItem.class, "UNSPECIFIC")
      .build();
    static final ImmutableMap<String, Constructor<? extends CraftMetaItem>> constructorMap;
    
    static
    {
      ImmutableMap.Builder<String, Constructor<? extends CraftMetaItem>> classConstructorBuilder = ImmutableMap.builder();
      for (Map.Entry<Class<? extends CraftMetaItem>, String> mapping : classMap.entrySet()) {
        try
        {
          classConstructorBuilder.put((String)mapping.getValue(), ((Class)mapping.getKey()).getDeclaredConstructor(new Class[] { Map.class }));
        }
        catch (NoSuchMethodException e)
        {
          throw new AssertionError(e);
        }
      }
      constructorMap = classConstructorBuilder.build();
    }
    
    public static ItemMeta deserialize(Map<String, Object> map)
      throws Throwable
    {
      Validate.notNull(map, "Cannot deserialize null map");
      
      String type = getString(map, "meta-type", false);
      Constructor<? extends CraftMetaItem> constructor = (Constructor)constructorMap.get(type);
      if (constructor == null) {
        throw new IllegalArgumentException(type + " is not a valid " + "meta-type");
      }
      try
      {
        return (ItemMeta)constructor.newInstance(new Object[] { map });
      }
      catch (InstantiationException e)
      {
        throw new AssertionError(e);
      }
      catch (IllegalAccessException e)
      {
        throw new AssertionError(e);
      }
      catch (InvocationTargetException e)
      {
        throw e.getCause();
      }
    }
    
    public Map<String, Object> serialize()
    {
      throw new AssertionError();
    }
    
    static String getString(Map<?, ?> map, Object field, boolean nullable)
    {
      return (String)getObject(String.class, map, field, nullable);
    }
    
    static boolean getBoolean(Map<?, ?> map, Object field)
    {
      Boolean value = (Boolean)getObject(Boolean.class, map, field, true);
      return (value != null) && (value.booleanValue());
    }
    
    static <T> T getObject(Class<T> clazz, Map<?, ?> map, Object field, boolean nullable)
    {
      Object object = map.get(field);
      if (clazz.isInstance(object)) {
        return (T)clazz.cast(object);
      }
      if (object == null)
      {
        if (!nullable) {
          throw new NoSuchElementException(map + " does not contain " + field);
        }
        return null;
      }
      throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
    }
  }
  
  static final ItemMetaKey NAME = new ItemMetaKey("Name", "display-name");
  static final ItemMetaKey DISPLAY = new ItemMetaKey("display");
  static final ItemMetaKey LORE = new ItemMetaKey("Lore", "lore");
  static final ItemMetaKey ENCHANTMENTS = new ItemMetaKey("ench", "enchants");
  static final ItemMetaKey ENCHANTMENTS_ID = new ItemMetaKey("id");
  static final ItemMetaKey ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
  static final ItemMetaKey REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
  static final ItemMetaKey ATTRIBUTES = new ItemMetaKey("AttributeModifiers");
  static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
  static final ItemMetaKey ATTRIBUTES_NAME = new ItemMetaKey("Name");
  static final ItemMetaKey ATTRIBUTES_VALUE = new ItemMetaKey("Amount");
  static final ItemMetaKey ATTRIBUTES_TYPE = new ItemMetaKey("Operation");
  static final ItemMetaKey ATTRIBUTES_UUID_HIGH = new ItemMetaKey("UUIDMost");
  static final ItemMetaKey ATTRIBUTES_UUID_LOW = new ItemMetaKey("UUIDLeast");
  static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("HideFlags", "ItemFlags");
  static final ItemMetaKey UNBREAKABLE = new ItemMetaKey("Unbreakable");
  private String displayName;
  private List<String> lore;
  private Map<Enchantment, Integer> enchantments;
  private int repairCost;
  private int hideFlag;
  private static final Set<String> HANDLED_TAGS = Sets.newHashSet();
  private final Map<String, NBTBase> unhandledTags = new HashMap();
  
  CraftMetaItem(CraftMetaItem meta)
  {
    if (meta == null) {
      return;
    }
    this.displayName = meta.displayName;
    if (meta.hasLore()) {
      this.lore = new ArrayList(meta.lore);
    }
    if (meta.enchantments != null) {
      this.enchantments = new HashMap(meta.enchantments);
    }
    this.repairCost = meta.repairCost;
    this.hideFlag = meta.hideFlag;
    this.unhandledTags.putAll(meta.unhandledTags);
    this.spigot.setUnbreakable(meta.spigot.isUnbreakable());
  }
  
  CraftMetaItem(NBTTagCompound tag)
  {
    if (tag.hasKey(DISPLAY.NBT))
    {
      NBTTagCompound display = tag.getCompound(DISPLAY.NBT);
      if (display.hasKey(NAME.NBT)) {
        this.displayName = ValidateUtils.limit(display.getString(NAME.NBT), 1024);
      }
      if (display.hasKey(LORE.NBT))
      {
        NBTTagList list = display.getList(LORE.NBT, 8);
        this.lore = new ArrayList(list.size());
        for (int index = 0; index < list.size(); index++)
        {
          String line = ValidateUtils.limit(list.getString(index), 1024);
          this.lore.add(line);
        }
      }
    }
    this.enchantments = buildEnchantments(tag, ENCHANTMENTS);
    if (tag.hasKey(REPAIR.NBT)) {
      this.repairCost = tag.getInt(REPAIR.NBT);
    }
    if (tag.hasKey(HIDEFLAGS.NBT)) {
      this.hideFlag = tag.getInt(HIDEFLAGS.NBT);
    }
    TObjectDoubleHashMap<String> attributeTracker;
    if ((tag.get(ATTRIBUTES.NBT) instanceof NBTTagList))
    {
      NBTTagList save = null;
      NBTTagList nbttaglist = tag.getList(ATTRIBUTES.NBT, 10);
      
      attributeTracker = new TObjectDoubleHashMap();
      TObjectDoubleHashMap<String> attributeTrackerX = new TObjectDoubleHashMap();
      Map<String, IAttribute> attributesByName = new HashMap();
      attributeTracker.put("generic.maxHealth", 20.0D);
      attributesByName.put("generic.maxHealth", GenericAttributes.maxHealth);
      attributeTracker.put("generic.followRange", 32.0D);
      attributesByName.put("generic.followRange", GenericAttributes.FOLLOW_RANGE);
      attributeTracker.put("generic.knockbackResistance", 0.0D);
      attributesByName.put("generic.knockbackResistance", GenericAttributes.c);
      attributeTracker.put("generic.movementSpeed", 0.7D);
      attributesByName.put("generic.movementSpeed", GenericAttributes.MOVEMENT_SPEED);
      attributeTracker.put("generic.attackDamage", 1.0D);
      attributesByName.put("generic.attackDamage", GenericAttributes.ATTACK_DAMAGE);
      NBTTagList oldList = nbttaglist;
      nbttaglist = new NBTTagList();
      
      List<NBTTagCompound> op0 = new ArrayList();
      List<NBTTagCompound> op1 = new ArrayList();
      List<NBTTagCompound> op2 = new ArrayList();
      NBTTagCompound nbttagcompound;
      for (int i = 0; i < oldList.size(); i++)
      {
        nbttagcompound = oldList.get(i);
        if (nbttagcompound != null) {
          if (nbttagcompound.hasKeyOfType(ATTRIBUTES_UUID_HIGH.NBT, 99)) {
            if (nbttagcompound.hasKeyOfType(ATTRIBUTES_UUID_LOW.NBT, 99)) {
              if (((nbttagcompound.get(ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString)) && (CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(ATTRIBUTES_IDENTIFIER.NBT)))) {
                if (((nbttagcompound.get(ATTRIBUTES_NAME.NBT) instanceof NBTTagString)) && (!nbttagcompound.getString(ATTRIBUTES_NAME.NBT).isEmpty())) {
                  if (nbttagcompound.hasKeyOfType(ATTRIBUTES_VALUE.NBT, 99)) {
                    if ((nbttagcompound.hasKeyOfType(ATTRIBUTES_TYPE.NBT, 99)) && (nbttagcompound.getInt(ATTRIBUTES_TYPE.NBT) >= 0) && (nbttagcompound.getInt(ATTRIBUTES_TYPE.NBT) <= 2)) {
                      switch (nbttagcompound.getInt(ATTRIBUTES_TYPE.NBT))
                      {
                      case 0: 
                        op0.add(nbttagcompound);
                        break;
                      case 1: 
                        op1.add(nbttagcompound);
                        break;
                      case 2: 
                        op2.add(nbttagcompound);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      for (NBTTagCompound nbtTagCompound : op0)
      {
        String name = nbtTagCompound.getString(ATTRIBUTES_IDENTIFIER.NBT);
        if (attributeTracker.containsKey(name))
        {
          double val = attributeTracker.get(name);
          val += nbtTagCompound.getDouble(ATTRIBUTES_VALUE.NBT);
          if (val == ((IAttribute)attributesByName.get(name)).a(val)) {
            attributeTracker.put(name, val);
          }
        }
        else
        {
          nbttaglist.add(nbtTagCompound);
        }
      }
      for (String name : attributeTracker.keySet()) {
        attributeTrackerX.put(name, attributeTracker.get(name));
      }
      for (NBTTagCompound nbtTagCompound : op1)
      {
        String name = nbtTagCompound.getString(ATTRIBUTES_IDENTIFIER.NBT);
        if (attributeTracker.containsKey(name))
        {
          double val = attributeTracker.get(name);
          double valX = attributeTrackerX.get(name);
          val += valX * nbtTagCompound.getDouble(ATTRIBUTES_VALUE.NBT);
          if (val == ((IAttribute)attributesByName.get(name)).a(val)) {
            attributeTracker.put(name, val);
          }
        }
        else
        {
          nbttaglist.add(nbtTagCompound);
        }
      }
      for (NBTTagCompound nbtTagCompound : op2)
      {
        String name = nbtTagCompound.getString(ATTRIBUTES_IDENTIFIER.NBT);
        if (attributeTracker.containsKey(name))
        {
          double val = attributeTracker.get(name);
          val += val * nbtTagCompound.getDouble(ATTRIBUTES_VALUE.NBT);
          if (val == ((IAttribute)attributesByName.get(name)).a(val)) {
            attributeTracker.put(name, val);
          }
        }
        else
        {
          nbttaglist.add(nbtTagCompound);
        }
      }
      for (int i = 0; i < nbttaglist.size(); i++) {
        if ((nbttaglist.get(i) instanceof NBTTagCompound))
        {
          NBTTagCompound nbttagcompound = nbttaglist.get(i);
          if (nbttagcompound.hasKeyOfType(ATTRIBUTES_UUID_HIGH.NBT, 99)) {
            if (nbttagcompound.hasKeyOfType(ATTRIBUTES_UUID_LOW.NBT, 99)) {
              if (((nbttagcompound.get(ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString)) && (CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(ATTRIBUTES_IDENTIFIER.NBT)))) {
                if (((nbttagcompound.get(ATTRIBUTES_NAME.NBT) instanceof NBTTagString)) && (!nbttagcompound.getString(ATTRIBUTES_NAME.NBT).isEmpty())) {
                  if (nbttagcompound.hasKeyOfType(ATTRIBUTES_VALUE.NBT, 99)) {
                    if ((nbttagcompound.hasKeyOfType(ATTRIBUTES_TYPE.NBT, 99)) && (nbttagcompound.getInt(ATTRIBUTES_TYPE.NBT) >= 0) && (nbttagcompound.getInt(ATTRIBUTES_TYPE.NBT) <= 2))
                    {
                      if (save == null) {
                        save = new NBTTagList();
                      }
                      NBTTagCompound entry = new NBTTagCompound();
                      entry.set(ATTRIBUTES_UUID_HIGH.NBT, nbttagcompound.get(ATTRIBUTES_UUID_HIGH.NBT));
                      entry.set(ATTRIBUTES_UUID_LOW.NBT, nbttagcompound.get(ATTRIBUTES_UUID_LOW.NBT));
                      entry.set(ATTRIBUTES_IDENTIFIER.NBT, nbttagcompound.get(ATTRIBUTES_IDENTIFIER.NBT));
                      entry.set(ATTRIBUTES_NAME.NBT, nbttagcompound.get(ATTRIBUTES_NAME.NBT));
                      entry.set(ATTRIBUTES_VALUE.NBT, nbttagcompound.get(ATTRIBUTES_VALUE.NBT));
                      entry.set(ATTRIBUTES_TYPE.NBT, nbttagcompound.get(ATTRIBUTES_TYPE.NBT));
                      save.add(entry);
                    }
                  }
                }
              }
            }
          }
        }
      }
      this.unhandledTags.put(ATTRIBUTES.NBT, save);
    }
    Set<String> keys = tag.c();
    for (String key : keys) {
      if (!getHandledTags().contains(key)) {
        this.unhandledTags.put(key, tag.get(key));
      }
    }
    if (tag.hasKey(UNBREAKABLE.NBT)) {
      this.spigot.setUnbreakable(tag.getBoolean(UNBREAKABLE.NBT));
    }
  }
  
  static Map<Enchantment, Integer> buildEnchantments(NBTTagCompound tag, ItemMetaKey key)
  {
    if (!tag.hasKey(key.NBT)) {
      return null;
    }
    NBTTagList ench = tag.getList(key.NBT, 10);
    Map<Enchantment, Integer> enchantments = new HashMap(ench.size());
    for (int i = 0; i < ench.size(); i++)
    {
      int id = 0xFFFF & ench.get(i).getShort(ENCHANTMENTS_ID.NBT);
      int level = 0xFFFF & ench.get(i).getShort(ENCHANTMENTS_LVL.NBT);
      
      Enchantment e = Enchantment.getById(id);
      if (e != null) {
        enchantments.put(e, Integer.valueOf(level));
      }
    }
    return enchantments;
  }
  
  CraftMetaItem(Map<String, Object> map)
  {
    setDisplayName(SerializableMeta.getString(map, NAME.BUKKIT, true));
    
    Iterable<?> lore = (Iterable)SerializableMeta.getObject(Iterable.class, map, LORE.BUKKIT, true);
    if (lore != null) {
      safelyAdd(lore, this.lore = new ArrayList(), Integer.MAX_VALUE);
    }
    this.enchantments = buildEnchantments(map, ENCHANTMENTS);
    
    Integer repairCost = (Integer)SerializableMeta.getObject(Integer.class, map, REPAIR.BUKKIT, true);
    if (repairCost != null) {
      setRepairCost(repairCost.intValue());
    }
    Set hideFlags = (Set)SerializableMeta.getObject(Set.class, map, HIDEFLAGS.BUKKIT, true);
    if (hideFlags != null) {
      for (Object hideFlagObject : hideFlags)
      {
        String hideFlagString = (String)hideFlagObject;
        try
        {
          ItemFlag hideFlatEnum = ItemFlag.valueOf(hideFlagString);
          addItemFlags(new ItemFlag[] { hideFlatEnum });
        }
        catch (IllegalArgumentException localIllegalArgumentException) {}
      }
    }
    Boolean unbreakable = (Boolean)SerializableMeta.getObject(Boolean.class, map, UNBREAKABLE.BUKKIT, true);
    if (unbreakable != null) {
      this.spigot.setUnbreakable(unbreakable.booleanValue());
    }
    String internal = SerializableMeta.getString(map, "internal", true);
    if (internal != null)
    {
      ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64(internal));
      try
      {
        NBTTagCompound tag = NBTCompressedStreamTools.a(buf);
        deserializeInternal(tag);
        Set<String> keys = tag.c();
        for (String key : keys) {
          if (!getHandledTags().contains(key)) {
            this.unhandledTags.put(key, tag.get(key));
          }
        }
      }
      catch (IOException ex)
      {
        Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  void deserializeInternal(NBTTagCompound tag) {}
  
  static Map<Enchantment, Integer> buildEnchantments(Map<String, Object> map, ItemMetaKey key)
  {
    Map<?, ?> ench = (Map)SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
    if (ench == null) {
      return null;
    }
    Map<Enchantment, Integer> enchantments = new HashMap(ench.size());
    for (Map.Entry<?, ?> entry : ench.entrySet())
    {
      Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());
      if ((enchantment != null) && ((entry.getValue() instanceof Integer))) {
        enchantments.put(enchantment, (Integer)entry.getValue());
      }
    }
    return enchantments;
  }
  
  @Overridden
  void applyToItem(NBTTagCompound itemTag)
  {
    if (hasDisplayName()) {
      setDisplayTag(itemTag, NAME.NBT, new NBTTagString(this.displayName));
    }
    if (hasLore()) {
      setDisplayTag(itemTag, LORE.NBT, createStringList(this.lore));
    }
    if (this.hideFlag != 0) {
      itemTag.setInt(HIDEFLAGS.NBT, this.hideFlag);
    }
    applyEnchantments(this.enchantments, itemTag, ENCHANTMENTS);
    if (this.spigot.isUnbreakable()) {
      itemTag.setBoolean(UNBREAKABLE.NBT, true);
    }
    if (hasRepairCost()) {
      itemTag.setInt(REPAIR.NBT, this.repairCost);
    }
    for (Map.Entry<String, NBTBase> e : this.unhandledTags.entrySet()) {
      itemTag.set((String)e.getKey(), (NBTBase)e.getValue());
    }
  }
  
  static NBTTagList createStringList(List<String> list)
  {
    if ((list == null) || (list.isEmpty())) {
      return null;
    }
    NBTTagList tagList = new NBTTagList();
    for (String value : list) {
      tagList.add(new NBTTagString(value));
    }
    return tagList;
  }
  
  static void applyEnchantments(Map<Enchantment, Integer> enchantments, NBTTagCompound tag, ItemMetaKey key)
  {
    if (enchantments == null) {
      return;
    }
    NBTTagList list = new NBTTagList();
    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet())
    {
      NBTTagCompound subtag = new NBTTagCompound();
      
      subtag.setShort(ENCHANTMENTS_ID.NBT, (short)((Enchantment)entry.getKey()).getId());
      subtag.setShort(ENCHANTMENTS_LVL.NBT, ((Integer)entry.getValue()).shortValue());
      
      list.add(subtag);
    }
    tag.set(key.NBT, list);
  }
  
  void setDisplayTag(NBTTagCompound tag, String key, NBTBase value)
  {
    NBTTagCompound display = tag.getCompound(DISPLAY.NBT);
    if (!tag.hasKey(DISPLAY.NBT)) {
      tag.set(DISPLAY.NBT, display);
    }
    display.set(key, value);
  }
  
  @Overridden
  boolean applicableTo(Material type)
  {
    return type != Material.AIR;
  }
  
  @Overridden
  boolean isEmpty()
  {
    return (!hasDisplayName()) && (!hasEnchants()) && (!hasLore()) && (!hasRepairCost()) && (this.unhandledTags.isEmpty()) && (this.hideFlag == 0) && (!this.spigot.isUnbreakable());
  }
  
  public String getDisplayName()
  {
    return this.displayName;
  }
  
  public final void setDisplayName(String name)
  {
    this.displayName = name;
  }
  
  public boolean hasDisplayName()
  {
    return !Strings.isNullOrEmpty(this.displayName);
  }
  
  public boolean hasLore()
  {
    return (this.lore != null) && (!this.lore.isEmpty());
  }
  
  public boolean hasRepairCost()
  {
    return this.repairCost > 0;
  }
  
  public boolean hasEnchant(Enchantment ench)
  {
    return (hasEnchants()) && (this.enchantments.containsKey(ench));
  }
  
  public int getEnchantLevel(Enchantment ench)
  {
    Integer level = hasEnchants() ? (Integer)this.enchantments.get(ench) : null;
    if (level == null) {
      return 0;
    }
    return level.intValue();
  }
  
  public Map<Enchantment, Integer> getEnchants()
  {
    return hasEnchants() ? ImmutableMap.copyOf(this.enchantments) : ImmutableMap.of();
  }
  
  public boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions)
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
  
  public boolean removeEnchant(Enchantment ench)
  {
    boolean b = (hasEnchants()) && (this.enchantments.remove(ench) != null);
    if ((this.enchantments != null) && (this.enchantments.isEmpty())) {
      this.enchantments = null;
    }
    return b;
  }
  
  public boolean hasEnchants()
  {
    return (this.enchantments != null) && (!this.enchantments.isEmpty());
  }
  
  public boolean hasConflictingEnchant(Enchantment ench)
  {
    return checkConflictingEnchants(this.enchantments, ench);
  }
  
  public void addItemFlags(ItemFlag... hideFlags)
  {
    ItemFlag[] arrayOfItemFlag;
    int i = (arrayOfItemFlag = hideFlags).length;
    for (int j = 0; j < i; j++)
    {
      ItemFlag f = arrayOfItemFlag[j];
      this.hideFlag |= getBitModifier(f);
    }
  }
  
  public void removeItemFlags(ItemFlag... hideFlags)
  {
    ItemFlag[] arrayOfItemFlag;
    int i = (arrayOfItemFlag = hideFlags).length;
    for (int j = 0; j < i; j++)
    {
      ItemFlag f = arrayOfItemFlag[j];
      this.hideFlag &= (getBitModifier(f) ^ 0xFFFFFFFF);
    }
  }
  
  public Set<ItemFlag> getItemFlags()
  {
    Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);
    ItemFlag[] arrayOfItemFlag;
    int i = (arrayOfItemFlag = ItemFlag.values()).length;
    for (int j = 0; j < i; j++)
    {
      ItemFlag f = arrayOfItemFlag[j];
      if (hasItemFlag(f)) {
        currentFlags.add(f);
      }
    }
    return currentFlags;
  }
  
  public boolean hasItemFlag(ItemFlag flag)
  {
    int bitModifier = getBitModifier(flag);
    return (this.hideFlag & bitModifier) == bitModifier;
  }
  
  private byte getBitModifier(ItemFlag hideFlag)
  {
    return (byte)(1 << hideFlag.ordinal());
  }
  
  public List<String> getLore()
  {
    return this.lore == null ? null : new ArrayList(this.lore);
  }
  
  public void setLore(List<String> lore)
  {
    if (lore == null)
    {
      this.lore = null;
    }
    else if (this.lore == null)
    {
      safelyAdd(lore, this.lore = new ArrayList(lore.size()), Integer.MAX_VALUE);
    }
    else
    {
      this.lore.clear();
      safelyAdd(lore, this.lore, Integer.MAX_VALUE);
    }
  }
  
  public int getRepairCost()
  {
    return this.repairCost;
  }
  
  public void setRepairCost(int cost)
  {
    this.repairCost = cost;
  }
  
  public final boolean equals(Object object)
  {
    if (object == null) {
      return false;
    }
    if (object == this) {
      return true;
    }
    if (!(object instanceof CraftMetaItem)) {
      return false;
    }
    return CraftItemFactory.instance().equals(this, (ItemMeta)object);
  }
  
  @Overridden
  boolean equalsCommon(CraftMetaItem that)
  {
    return (hasDisplayName() ? (that.hasDisplayName()) && (this.displayName.equals(that.displayName)) : !that.hasDisplayName()) && 
      (hasEnchants() ? (that.hasEnchants()) && (this.enchantments.equals(that.enchantments)) : !that.hasEnchants()) && 
      (hasLore() ? (that.hasLore()) && (this.lore.equals(that.lore)) : !that.hasLore()) && 
      (hasRepairCost() ? (that.hasRepairCost()) && (this.repairCost == that.repairCost) : !that.hasRepairCost()) && 
      (this.unhandledTags.equals(that.unhandledTags)) && 
      (this.hideFlag == that.hideFlag) && 
      (this.spigot.isUnbreakable() == that.spigot.isUnbreakable());
  }
  
  @Overridden
  boolean notUncommon(CraftMetaItem meta)
  {
    return true;
  }
  
  public final int hashCode()
  {
    return applyHash();
  }
  
  @Overridden
  int applyHash()
  {
    int hash = 3;
    hash = 61 * hash + (hasDisplayName() ? this.displayName.hashCode() : 0);
    hash = 61 * hash + (hasLore() ? this.lore.hashCode() : 0);
    hash = 61 * hash + (hasEnchants() ? this.enchantments.hashCode() : 0);
    hash = 61 * hash + (hasRepairCost() ? this.repairCost : 0);
    hash = 61 * hash + this.unhandledTags.hashCode();
    hash = 61 * hash + this.hideFlag;
    hash = 61 * hash + (this.spigot.isUnbreakable() ? 1231 : 1237);
    return hash;
  }
  
  @Overridden
  public CraftMetaItem clone()
  {
    try
    {
      CraftMetaItem clone = (CraftMetaItem)super.clone();
      if (this.lore != null) {
        clone.lore = new ArrayList(this.lore);
      }
      if (this.enchantments != null) {
        clone.enchantments = new HashMap(this.enchantments);
      }
      clone.hideFlag = this.hideFlag;
      return clone;
    }
    catch (CloneNotSupportedException e)
    {
      throw new Error(e);
    }
  }
  
  public final Map<String, Object> serialize()
  {
    ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
    map.put("meta-type", SerializableMeta.classMap.get(getClass()));
    serialize(map);
    return map.build();
  }
  
  @Overridden
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    if (hasDisplayName()) {
      builder.put(NAME.BUKKIT, this.displayName);
    }
    if (hasLore()) {
      builder.put(LORE.BUKKIT, ImmutableList.copyOf(this.lore));
    }
    serializeEnchantments(this.enchantments, builder, ENCHANTMENTS);
    if (hasRepairCost()) {
      builder.put(REPAIR.BUKKIT, Integer.valueOf(this.repairCost));
    }
    if (this.spigot.isUnbreakable()) {
      builder.put(UNBREAKABLE.BUKKIT, Boolean.valueOf(true));
    }
    Set<String> hideFlags = new HashSet();
    for (ItemFlag hideFlagEnum : getItemFlags()) {
      hideFlags.add(hideFlagEnum.name());
    }
    if (!hideFlags.isEmpty()) {
      builder.put(HIDEFLAGS.BUKKIT, hideFlags);
    }
    Map<String, NBTBase> internalTags = new HashMap(this.unhandledTags);
    serializeInternal(internalTags);
    if (!internalTags.isEmpty())
    {
      NBTTagCompound internal = new NBTTagCompound();
      for (Map.Entry<String, NBTBase> e : internalTags.entrySet()) {
        internal.set((String)e.getKey(), (NBTBase)e.getValue());
      }
      try
      {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        NBTCompressedStreamTools.a(internal, buf);
        builder.put("internal", Base64.encodeBase64String(buf.toByteArray()));
      }
      catch (IOException ex)
      {
        Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return builder;
  }
  
  void serializeInternal(Map<String, NBTBase> unhandledTags) {}
  
  static void serializeEnchantments(Map<Enchantment, Integer> enchantments, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key)
  {
    if ((enchantments == null) || (enchantments.isEmpty())) {
      return;
    }
    ImmutableMap.Builder<String, Integer> enchants = ImmutableMap.builder();
    for (Map.Entry<? extends Enchantment, Integer> enchant : enchantments.entrySet()) {
      enchants.put(((Enchantment)enchant.getKey()).getName(), (Integer)enchant.getValue());
    }
    builder.put(key.BUKKIT, enchants.build());
  }
  
  static void safelyAdd(Iterable<?> addFrom, Collection<String> addTo, int maxItemLength)
  {
    if (addFrom == null) {
      return;
    }
    for (Object object : addFrom) {
      if (!(object instanceof String))
      {
        if (object != null) {
          throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
        }
        addTo.add("");
      }
      else
      {
        String page = object.toString();
        if (page.length() > maxItemLength) {
          page = page.substring(0, maxItemLength);
        }
        addTo.add(page);
      }
    }
  }
  
  static boolean checkConflictingEnchants(Map<Enchantment, Integer> enchantments, Enchantment ench)
  {
    if ((enchantments == null) || (enchantments.isEmpty())) {
      return false;
    }
    for (Enchantment enchant : enchantments.keySet()) {
      if (enchant.conflictsWith(ench)) {
        return true;
      }
    }
    return false;
  }
  
  public final String toString()
  {
    return (String)SerializableMeta.classMap.get(getClass()) + "_META:" + serialize();
  }
  
  public static Set<String> getHandledTags()
  {
    synchronized (HANDLED_TAGS)
    {
      if (HANDLED_TAGS.isEmpty()) {
        HANDLED_TAGS.addAll(Arrays.asList(new String[] {
          UNBREAKABLE.NBT, 
          DISPLAY.NBT, 
          REPAIR.NBT, 
          ENCHANTMENTS.NBT, 
          CraftMetaMap.MAP_SCALING.NBT, 
          CraftMetaPotion.POTION_EFFECTS.NBT, 
          CraftMetaSkull.SKULL_OWNER.NBT, 
          CraftMetaSkull.SKULL_PROFILE.NBT, 
          CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT, 
          CraftMetaBook.BOOK_TITLE.NBT, 
          CraftMetaBook.BOOK_AUTHOR.NBT, 
          CraftMetaBook.BOOK_PAGES.NBT, 
          CraftMetaBook.RESOLVED.NBT, 
          CraftMetaBook.GENERATION.NBT, 
          CraftMetaFirework.FIREWORKS.NBT, 
          CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT, 
          CraftMetaCharge.EXPLOSION.NBT, 
          CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT }));
      }
      return HANDLED_TAGS;
    }
  }
  
  private final ItemMeta.Spigot spigot = new ItemMeta.Spigot()
  {
    private boolean unbreakable;
    
    public void setUnbreakable(boolean setUnbreakable)
    {
      this.unbreakable = setUnbreakable;
    }
    
    public boolean isUnbreakable()
    {
      return this.unbreakable;
    }
  };
  
  public ItemMeta.Spigot spigot()
  {
    return this.spigot;
  }
}
