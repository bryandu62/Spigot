package org.bukkit.craftbukkit.v1_8_R3.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.FireworkMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaFirework
  extends CraftMetaItem
  implements FireworkMeta
{
  static final CraftMetaItem.ItemMetaKey FIREWORKS = new CraftMetaItem.ItemMetaKey("Fireworks");
  static final CraftMetaItem.ItemMetaKey FLIGHT = new CraftMetaItem.ItemMetaKey("Flight", "power");
  static final CraftMetaItem.ItemMetaKey EXPLOSIONS = new CraftMetaItem.ItemMetaKey("Explosions", "firework-effects");
  static final CraftMetaItem.ItemMetaKey EXPLOSION_COLORS = new CraftMetaItem.ItemMetaKey("Colors");
  static final CraftMetaItem.ItemMetaKey EXPLOSION_TYPE = new CraftMetaItem.ItemMetaKey("Type");
  static final CraftMetaItem.ItemMetaKey EXPLOSION_TRAIL = new CraftMetaItem.ItemMetaKey("Trail");
  static final CraftMetaItem.ItemMetaKey EXPLOSION_FLICKER = new CraftMetaItem.ItemMetaKey("Flicker");
  static final CraftMetaItem.ItemMetaKey EXPLOSION_FADE = new CraftMetaItem.ItemMetaKey("FadeColors");
  private List<FireworkEffect> effects;
  private int power;
  
  CraftMetaFirework(CraftMetaItem meta)
  {
    super(meta);
    if (!(meta instanceof CraftMetaFirework)) {
      return;
    }
    CraftMetaFirework that = (CraftMetaFirework)meta;
    
    this.power = that.power;
    if (that.hasEffects()) {
      this.effects = new ArrayList(that.effects);
    }
  }
  
  CraftMetaFirework(NBTTagCompound tag)
  {
    super(tag);
    if (!tag.hasKey(FIREWORKS.NBT)) {
      return;
    }
    NBTTagCompound fireworks = tag.getCompound(FIREWORKS.NBT);
    
    this.power = (0xFF & fireworks.getByte(FLIGHT.NBT));
    if (!fireworks.hasKey(EXPLOSIONS.NBT)) {
      return;
    }
    NBTTagList fireworkEffects = fireworks.getList(EXPLOSIONS.NBT, 10);
    List<FireworkEffect> effects = this.effects = new ArrayList(fireworkEffects.size());
    for (int i = 0; i < fireworkEffects.size(); i++) {
      effects.add(getEffect(fireworkEffects.get(i)));
    }
  }
  
  static FireworkEffect getEffect(NBTTagCompound explosion)
  {
    FireworkEffect.Builder effect = FireworkEffect.builder()
      .flicker(explosion.getBoolean(EXPLOSION_FLICKER.NBT))
      .trail(explosion.getBoolean(EXPLOSION_TRAIL.NBT))
      .with(getEffectType(0xFF & explosion.getByte(EXPLOSION_TYPE.NBT)));
    int[] arrayOfInt;
    int i = (arrayOfInt = explosion.getIntArray(EXPLOSION_COLORS.NBT)).length;
    for (int j = 0; j < i; j++)
    {
      int color = arrayOfInt[j];
      effect.withColor(Color.fromRGB(color));
    }
    i = (arrayOfInt = explosion.getIntArray(EXPLOSION_FADE.NBT)).length;
    for (j = 0; j < i; j++)
    {
      int color = arrayOfInt[j];
      effect.withFade(Color.fromRGB(color));
    }
    return effect.build();
  }
  
  static NBTTagCompound getExplosion(FireworkEffect effect)
  {
    NBTTagCompound explosion = new NBTTagCompound();
    if (effect.hasFlicker()) {
      explosion.setBoolean(EXPLOSION_FLICKER.NBT, true);
    }
    if (effect.hasTrail()) {
      explosion.setBoolean(EXPLOSION_TRAIL.NBT, true);
    }
    addColors(explosion, EXPLOSION_COLORS, effect.getColors());
    addColors(explosion, EXPLOSION_FADE, effect.getFadeColors());
    
    explosion.setByte(EXPLOSION_TYPE.NBT, (byte)getNBT(effect.getType()));
    
    return explosion;
  }
  
  static int getNBT(FireworkEffect.Type type)
  {
    switch (type)
    {
    case BALL: 
      return 0;
    case BALL_LARGE: 
      return 1;
    case BURST: 
      return 2;
    case STAR: 
      return 3;
    case CREEPER: 
      return 4;
    }
    throw new IllegalStateException(type.toString());
  }
  
  static FireworkEffect.Type getEffectType(int nbt)
  {
    switch (nbt)
    {
    case 0: 
      return FireworkEffect.Type.BALL;
    case 1: 
      return FireworkEffect.Type.BALL_LARGE;
    case 2: 
      return FireworkEffect.Type.STAR;
    case 3: 
      return FireworkEffect.Type.CREEPER;
    case 4: 
      return FireworkEffect.Type.BURST;
    }
    throw new IllegalStateException(Integer.toString(nbt));
  }
  
  CraftMetaFirework(Map<String, Object> map)
  {
    super(map);
    
    Integer power = (Integer)CraftMetaItem.SerializableMeta.getObject(Integer.class, map, FLIGHT.BUKKIT, true);
    if (power != null) {
      setPower(power.intValue());
    }
    Iterable<?> effects = (Iterable)CraftMetaItem.SerializableMeta.getObject(Iterable.class, map, EXPLOSIONS.BUKKIT, true);
    safelyAddEffects(effects);
  }
  
  public boolean hasEffects()
  {
    return (this.effects != null) && (!this.effects.isEmpty());
  }
  
  void safelyAddEffects(Iterable<?> collection)
  {
    if ((collection == null) || (((collection instanceof Collection)) && (((Collection)collection).isEmpty()))) {
      return;
    }
    List<FireworkEffect> effects = this.effects;
    if (effects == null) {
      effects = this.effects = new ArrayList();
    }
    for (Object obj : collection) {
      if ((obj instanceof FireworkEffect)) {
        effects.add((FireworkEffect)obj);
      } else {
        throw new IllegalArgumentException(obj + " in " + collection + " is not a FireworkEffect");
      }
    }
  }
  
  void applyToItem(NBTTagCompound itemTag)
  {
    super.applyToItem(itemTag);
    if (isFireworkEmpty()) {
      return;
    }
    NBTTagCompound fireworks = itemTag.getCompound(FIREWORKS.NBT);
    itemTag.set(FIREWORKS.NBT, fireworks);
    if (hasEffects())
    {
      NBTTagList effects = new NBTTagList();
      for (FireworkEffect effect : this.effects) {
        effects.add(getExplosion(effect));
      }
      if (effects.size() > 0) {
        fireworks.set(EXPLOSIONS.NBT, effects);
      }
    }
    if (hasPower()) {
      fireworks.setByte(FLIGHT.NBT, (byte)this.power);
    }
  }
  
  static void addColors(NBTTagCompound compound, CraftMetaItem.ItemMetaKey key, List<Color> colors)
  {
    if (colors.isEmpty()) {
      return;
    }
    int[] colorArray = new int[colors.size()];
    int i = 0;
    for (Color color : colors) {
      colorArray[(i++)] = color.asRGB();
    }
    compound.setIntArray(key.NBT, colorArray);
  }
  
  boolean applicableTo(Material type)
  {
    switch (type)
    {
    case STONE_SLAB2: 
      return true;
    }
    return false;
  }
  
  boolean isEmpty()
  {
    return (super.isEmpty()) && (isFireworkEmpty());
  }
  
  boolean isFireworkEmpty()
  {
    return (!hasEffects()) && (!hasPower());
  }
  
  boolean hasPower()
  {
    return this.power != 0;
  }
  
  boolean equalsCommon(CraftMetaItem meta)
  {
    if (!super.equalsCommon(meta)) {
      return false;
    }
    if ((meta instanceof CraftMetaFirework))
    {
      CraftMetaFirework that = (CraftMetaFirework)meta;
      
      return (hasPower() ? (that.hasPower()) && (this.power == that.power) : !that.hasPower()) && 
        (hasEffects() ? (that.hasEffects()) && (this.effects.equals(that.effects)) : !that.hasEffects());
    }
    return true;
  }
  
  boolean notUncommon(CraftMetaItem meta)
  {
    return (super.notUncommon(meta)) && (((meta instanceof CraftMetaFirework)) || (isFireworkEmpty()));
  }
  
  int applyHash()
  {
    int original;
    int hash = original = super.applyHash();
    if (hasPower()) {
      hash = 61 * hash + this.power;
    }
    if (hasEffects()) {
      hash = 61 * hash + 13 * this.effects.hashCode();
    }
    return hash != original ? CraftMetaFirework.class.hashCode() ^ hash : hash;
  }
  
  ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder)
  {
    super.serialize(builder);
    if (hasEffects()) {
      builder.put(EXPLOSIONS.BUKKIT, ImmutableList.copyOf(this.effects));
    }
    if (hasPower()) {
      builder.put(FLIGHT.BUKKIT, Integer.valueOf(this.power));
    }
    return builder;
  }
  
  public CraftMetaFirework clone()
  {
    CraftMetaFirework meta = (CraftMetaFirework)super.clone();
    if (this.effects != null) {
      meta.effects = new ArrayList(this.effects);
    }
    return meta;
  }
  
  public void addEffect(FireworkEffect effect)
  {
    Validate.notNull(effect, "Effect cannot be null");
    if (this.effects == null) {
      this.effects = new ArrayList();
    }
    this.effects.add(effect);
  }
  
  public void addEffects(FireworkEffect... effects)
  {
    Validate.notNull(effects, "Effects cannot be null");
    if (effects.length == 0) {
      return;
    }
    List<FireworkEffect> list = this.effects;
    if (list == null) {
      list = this.effects = new ArrayList();
    }
    FireworkEffect[] arrayOfFireworkEffect;
    int i = (arrayOfFireworkEffect = effects).length;
    for (int j = 0; j < i; j++)
    {
      FireworkEffect effect = arrayOfFireworkEffect[j];
      Validate.notNull(effect, "Effect cannot be null");
      list.add(effect);
    }
  }
  
  public void addEffects(Iterable<FireworkEffect> effects)
  {
    Validate.notNull(effects, "Effects cannot be null");
    safelyAddEffects(effects);
  }
  
  public List<FireworkEffect> getEffects()
  {
    return this.effects == null ? ImmutableList.of() : ImmutableList.copyOf(this.effects);
  }
  
  public int getEffectsSize()
  {
    return this.effects == null ? 0 : this.effects.size();
  }
  
  public void removeEffect(int index)
  {
    if (this.effects == null) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
    }
    this.effects.remove(index);
  }
  
  public void clearEffects()
  {
    this.effects = null;
  }
  
  public int getPower()
  {
    return this.power;
  }
  
  public void setPower(int power)
  {
    Validate.isTrue(power >= 0, "Power cannot be less than zero: ", power);
    Validate.isTrue(power < 128, "Power cannot be more than 127: ", power);
    this.power = power;
  }
}
