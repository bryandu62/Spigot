package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaPotion
  extends CraftMetaItem
  implements PotionMeta
{
  static final CraftMetaItem.ItemMetaKey AMPLIFIER = new CraftMetaItem.ItemMetaKey("Amplifier", "amplifier");
  static final CraftMetaItem.ItemMetaKey AMBIENT = new CraftMetaItem.ItemMetaKey("Ambient", "ambient");
  static final CraftMetaItem.ItemMetaKey DURATION = new CraftMetaItem.ItemMetaKey("Duration", "duration");
  static final CraftMetaItem.ItemMetaKey SHOW_PARTICLES = new CraftMetaItem.ItemMetaKey("ShowParticles", "has-particles");
  static final CraftMetaItem.ItemMetaKey POTION_EFFECTS = new CraftMetaItem.ItemMetaKey("CustomPotionEffects", "custom-effects");
  static final CraftMetaItem.ItemMetaKey ID = new CraftMetaItem.ItemMetaKey("Id", "potion-id");
  private List<PotionEffect> customEffects;
  
  CraftMetaPotion(CraftMetaItem meta)
  {
    super(meta);
    if (!(meta instanceof CraftMetaPotion)) {
      return;
    }
    CraftMetaPotion potionMeta = (CraftMetaPotion)meta;
    if (potionMeta.hasCustomEffects()) {
      this.customEffects = new ArrayList(potionMeta.customEffects);
    }
  }
  
  CraftMetaPotion(NBTTagCompound tag)
  {
    super(tag);
    if (tag.hasKey(POTION_EFFECTS.NBT))
    {
      NBTTagList list = tag.getList(POTION_EFFECTS.NBT, 10);
      int length = list.size();
      this.customEffects = new ArrayList(length);
      for (int i = 0; i < length; i++)
      {
        NBTTagCompound effect = list.get(i);
        PotionEffectType type = PotionEffectType.getById(effect.getByte(ID.NBT));
        int amp = effect.getByte(AMPLIFIER.NBT);
        int duration = effect.getInt(DURATION.NBT);
        boolean ambient = effect.getBoolean(AMBIENT.NBT);
        boolean particles = effect.getBoolean(SHOW_PARTICLES.NBT);
        this.customEffects.add(new PotionEffect(type, duration, amp, ambient, particles));
      }
    }
  }
  
  CraftMetaPotion(Map<String, Object> map)
  {
    super(map);
    
    Iterable<?> rawEffectList = (Iterable)CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, POTION_EFFECTS.BUKKIT, true);
    if (rawEffectList == null) {
      return;
    }
    for (Object obj : rawEffectList)
    {
      if (!(obj instanceof PotionEffect)) {
        throw new IllegalArgumentException("Object in effect list is not valid. " + obj.getClass());
      }
      addCustomEffect((PotionEffect)obj, true);
    }
  }
  
  void applyToItem(NBTTagCompound tag)
  {
    super.applyToItem(tag);
    if (this.customEffects != null)
    {
      NBTTagList effectList = new NBTTagList();
      tag.set(POTION_EFFECTS.NBT, effectList);
      for (PotionEffect effect : this.customEffects)
      {
        NBTTagCompound effectData = new NBTTagCompound();
        effectData.setByte(ID.NBT, (byte)effect.getType().getId());
        effectData.setByte(AMPLIFIER.NBT, (byte)effect.getAmplifier());
        effectData.setInt(DURATION.NBT, effect.getDuration());
        effectData.setBoolean(AMBIENT.NBT, effect.isAmbient());
        effectData.setBoolean(SHOW_PARTICLES.NBT, effect.hasParticles());
        effectList.add(effectData);
      }
    }
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (isPotionEmpty());
  }
  
  boolean isPotionEmpty()
  {
    return !hasCustomEffects();
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case SMOOTH_STAIRS: 
      return true;
    }
    return false;
  }
  
  public CraftMetaPotion clone()
  {
    CraftMetaPotion clone = (CraftMetaPotion)super.clone();
    if (this.customEffects != null) {
      clone.customEffects = new ArrayList(this.customEffects);
    }
    return clone;
  }
  
  public boolean hasCustomEffects()
  {
    return this.customEffects != null;
  }
  
  public List<PotionEffect> getCustomEffects()
  {
    if (hasCustomEffects()) {
      return ImmutableList.copyOf(this.customEffects);
    }
    return ImmutableList.of();
  }
  
  public boolean addCustomEffect(PotionEffect effect, boolean overwrite)
  {
    Validate.notNull(effect, "Potion effect must not be null");
    
    int index = indexOfEffect(effect.getType());
    if (index != -1)
    {
      if (overwrite)
      {
        PotionEffect old = (PotionEffect)this.customEffects.get(index);
        if ((old.getAmplifier() == effect.getAmplifier()) && (old.getDuration() == effect.getDuration()) && (old.isAmbient() == effect.isAmbient())) {
          return false;
        }
        this.customEffects.set(index, effect);
        return true;
      }
      return false;
    }
    if (this.customEffects == null) {
      this.customEffects = new ArrayList();
    }
    this.customEffects.add(effect);
    return true;
  }
  
  public boolean removeCustomEffect(PotionEffectType type)
  {
    Validate.notNull(type, "Potion effect type must not be null");
    if (!hasCustomEffects()) {
      return false;
    }
    boolean changed = false;
    Iterator<PotionEffect> iterator = this.customEffects.iterator();
    while (iterator.hasNext())
    {
      PotionEffect effect = (PotionEffect)iterator.next();
      if (effect.getType() == type)
      {
        iterator.remove();
        changed = true;
      }
    }
    if (this.customEffects.isEmpty()) {
      this.customEffects = null;
    }
    return changed;
  }
  
  public boolean hasCustomEffect(PotionEffectType type)
  {
    Validate.notNull(type, "Potion effect type must not be null");
    return indexOfEffect(type) != -1;
  }
  
  public boolean setMainEffect(PotionEffectType type)
  {
    Validate.notNull(type, "Potion effect type must not be null");
    int index = indexOfEffect(type);
    if ((index == -1) || (index == 0)) {
      return false;
    }
    PotionEffect old = (PotionEffect)this.customEffects.get(0);
    this.customEffects.set(0, (PotionEffect)this.customEffects.get(index));
    this.customEffects.set(index, old);
    return true;
  }
  
  private int indexOfEffect(PotionEffectType type)
  {
    if (!hasCustomEffects()) {
      return -1;
    }
    for (int i = 0; i < this.customEffects.size(); i++) {
      if (((PotionEffect)this.customEffects.get(i)).getType().equals(type)) {
        return i;
      }
    }
    return -1;
  }
  
  public boolean clearCustomEffects()
  {
    boolean changed = hasCustomEffects();
    this.customEffects = null;
    return changed;
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasCustomEffects()) {
      hash = 73 * hash + this.customEffects.hashCode();
    }
    return original != hash ? CraftMetaPotion.class.hashCode() ^ hash : hash;
  }
  
  public boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaPotion))
    {
      CraftMetaPotion that = (CraftMetaPotion)meta;
      
      return (that.hasCustomEffects()) && (this.customEffects.equals(that.customEffects));
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaPotion)) || (isPotionEmpty()));
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (hasCustomEffects()) {
      builder.put(POTION_EFFECTS.BUKKIT, ImmutableList.copyOf(this.customEffects));
    }
    return builder;
  }
}
