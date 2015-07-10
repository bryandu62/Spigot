package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract interface PotionMeta
  extends ItemMeta
{
  public abstract boolean hasCustomEffects();
  
  public abstract List<PotionEffect> getCustomEffects();
  
  public abstract boolean addCustomEffect(PotionEffect paramPotionEffect, boolean paramBoolean);
  
  public abstract boolean removeCustomEffect(PotionEffectType paramPotionEffectType);
  
  public abstract boolean hasCustomEffect(PotionEffectType paramPotionEffectType);
  
  public abstract boolean setMainEffect(PotionEffectType paramPotionEffectType);
  
  public abstract boolean clearCustomEffects();
  
  public abstract PotionMeta clone();
}
