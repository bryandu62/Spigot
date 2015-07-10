package org.bukkit.inventory.meta;

import org.bukkit.FireworkEffect;

public abstract interface FireworkEffectMeta
  extends ItemMeta
{
  public abstract void setEffect(FireworkEffect paramFireworkEffect);
  
  public abstract boolean hasEffect();
  
  public abstract FireworkEffect getEffect();
  
  public abstract FireworkEffectMeta clone();
}
