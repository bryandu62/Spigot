package org.bukkit.potion;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Potion
{
  private boolean extended = false;
  private boolean splash = false;
  private int level = 1;
  private int name = -1;
  private PotionType type;
  private static PotionBrewer brewer;
  private static final int EXTENDED_BIT = 64;
  private static final int POTION_BIT = 15;
  private static final int SPLASH_BIT = 16384;
  private static final int TIER_BIT = 32;
  private static final int TIER_SHIFT = 5;
  private static final int NAME_BIT = 63;
  
  public Potion(PotionType type)
  {
    this.type = type;
    if (type != null) {
      this.name = type.getDamageValue();
    }
    if ((type == null) || (type == PotionType.WATER)) {
      this.level = 0;
    }
  }
  
  @Deprecated
  public Potion(PotionType type, Tier tier)
  {
    this(type, tier == Tier.TWO ? 2 : 1);
    Validate.notNull(type, "Type cannot be null");
  }
  
  @Deprecated
  public Potion(PotionType type, Tier tier, boolean splash)
  {
    this(type, tier == Tier.TWO ? 2 : 1, splash);
  }
  
  @Deprecated
  public Potion(PotionType type, Tier tier, boolean splash, boolean extended)
  {
    this(type, tier, splash);
  }
  
  public Potion(PotionType type, int level)
  {
    this(type);
    Validate.notNull(type, "Type cannot be null");
    Validate.isTrue(type != PotionType.WATER, "Water bottles don't have a level!");
    Validate.isTrue((level > 0) && (level < 3), "Level must be 1 or 2");
    this.level = level;
  }
  
  @Deprecated
  public Potion(PotionType type, int level, boolean splash)
  {
    this(type, level);
    this.splash = splash;
  }
  
  @Deprecated
  public Potion(PotionType type, int level, boolean splash, boolean extended)
  {
    this(type, level, splash);
  }
  
  public Potion(int name)
  {
    this(PotionType.getByDamageValue(name & 0xF));
    this.name = (name & 0x3F);
    if ((name & 0xF) == 0) {
      this.type = null;
    }
  }
  
  public Potion splash()
  {
    setSplash(true);
    return this;
  }
  
  public Potion extend()
  {
    setHasExtendedDuration(true);
    return this;
  }
  
  public void apply(ItemStack to)
  {
    Validate.notNull(to, "itemstack cannot be null");
    Validate.isTrue(to.getType() == Material.POTION, "given itemstack is not a potion");
    to.setDurability(toDamageValue());
  }
  
  public void apply(LivingEntity to)
  {
    Validate.notNull(to, "entity cannot be null");
    to.addPotionEffects(getEffects());
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    Potion other = (Potion)obj;
    return (this.extended == other.extended) && (this.splash == other.splash) && (this.level == other.level) && (this.type == other.type);
  }
  
  public Collection<PotionEffect> getEffects()
  {
    if (this.type == null) {
      return ImmutableList.of();
    }
    return getBrewer().getEffectsFromDamage(toDamageValue());
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  @Deprecated
  public Tier getTier()
  {
    return this.level == 2 ? Tier.TWO : Tier.ONE;
  }
  
  public PotionType getType()
  {
    return this.type;
  }
  
  public boolean hasExtendedDuration()
  {
    return this.extended;
  }
  
  public int hashCode()
  {
    int result = 31 + this.level;
    result = 31 * result + (this.extended ? 1231 : 1237);
    result = 31 * result + (this.splash ? 1231 : 1237);
    result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
    return result;
  }
  
  public boolean isSplash()
  {
    return this.splash;
  }
  
  public void setHasExtendedDuration(boolean isExtended)
  {
    Validate.isTrue((this.type == null) || (!this.type.isInstant()), "Instant potions cannot be extended");
    this.extended = isExtended;
  }
  
  public void setSplash(boolean isSplash)
  {
    this.splash = isSplash;
  }
  
  @Deprecated
  public void setTier(Tier tier)
  {
    Validate.notNull(tier, "tier cannot be null");
    this.level = (tier == Tier.TWO ? 2 : 1);
  }
  
  public void setType(PotionType type)
  {
    this.type = type;
  }
  
  public void setLevel(int level)
  {
    Validate.notNull(this.type, "No-effect potions don't have a level.");
    int max = this.type.getMaxLevel();
    Validate.isTrue((level > 0) && (level <= max), "Level must be " + (max == 1 ? "" : "between 1 and ") + max + " for this potion");
    this.level = level;
  }
  
  @Deprecated
  public short toDamageValue()
  {
    if (this.type == PotionType.WATER) {
      return 0;
    }
    short damage;
    short damage;
    if (this.type == null)
    {
      damage = (short)(this.name == 0 ? 8192 : this.name);
    }
    else
    {
      damage = (short)(this.level - 1);
      damage = (short)(damage << 5);
      damage = (short)(damage | (short)this.type.getDamageValue());
    }
    if (this.splash) {
      damage = (short)(damage | 0x4000);
    }
    if (this.extended) {
      damage = (short)(damage | 0x40);
    }
    return damage;
  }
  
  public ItemStack toItemStack(int amount)
  {
    return new ItemStack(Material.POTION, amount, toDamageValue());
  }
  
  @Deprecated
  public static enum Tier
  {
    ONE(0),  TWO(32);
    
    private int damageBit;
    
    private Tier(int bit)
    {
      this.damageBit = bit;
    }
    
    public int getDamageBit()
    {
      return this.damageBit;
    }
    
    public static Tier getByDamageBit(int damageBit)
    {
      Tier[] arrayOfTier;
      int i = (arrayOfTier = values()).length;
      for (int j = 0; j < i; j++)
      {
        Tier tier = arrayOfTier[j];
        if (tier.damageBit == damageBit) {
          return tier;
        }
      }
      return null;
    }
  }
  
  @Deprecated
  public static Potion fromDamage(int damage)
  {
    PotionType type = PotionType.getByDamageValue(damage & 0xF);
    Potion potion;
    Potion potion;
    if ((type == null) || (type == PotionType.WATER))
    {
      potion = new Potion(damage & 0x3F);
    }
    else
    {
      int level = (damage & 0x20) >> 5;
      level++;
      potion = new Potion(type, level);
    }
    if ((damage & 0x4000) > 0) {
      potion = potion.splash();
    }
    if (((!type.equals(PotionType.INSTANT_DAMAGE)) || (type.equals(PotionType.FIRE_RESISTANCE))) && ((damage & 0x40) > 0)) {
      potion = potion.extend();
    }
    return potion;
  }
  
  public static Potion fromItemStack(ItemStack item)
  {
    Validate.notNull(item, "item cannot be null");
    if (item.getType() != Material.POTION) {
      throw new IllegalArgumentException("item is not a potion");
    }
    return fromDamage(item.getDurability());
  }
  
  public static PotionBrewer getBrewer()
  {
    return brewer;
  }
  
  public static void setPotionBrewer(PotionBrewer other)
  {
    if (brewer != null) {
      throw new IllegalArgumentException("brewer can only be set internally");
    }
    brewer = other;
  }
  
  @Deprecated
  public int getNameId()
  {
    return this.name;
  }
}
