package org.bukkit.potion;

import java.util.Collection;

public abstract interface PotionBrewer
{
  public abstract PotionEffect createEffect(PotionEffectType paramPotionEffectType, int paramInt1, int paramInt2);
  
  @Deprecated
  public abstract Collection<PotionEffect> getEffectsFromDamage(int paramInt);
}
