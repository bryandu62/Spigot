package org.bukkit.craftbukkit.v1_8_R3.potion;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.MobEffect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftPotionBrewer
  implements org.bukkit.potion.PotionBrewer
{
  private static final Map<Integer, Collection<PotionEffect>> cache = ;
  
  public Collection<PotionEffect> getEffectsFromDamage(int damage)
  {
    if (cache.containsKey(Integer.valueOf(damage))) {
      return (Collection)cache.get(Integer.valueOf(damage));
    }
    List<?> mcEffects = net.minecraft.server.v1_8_R3.PotionBrewer.getEffects(damage, false);
    List<PotionEffect> effects = new ArrayList();
    if (mcEffects == null) {
      return effects;
    }
    for (Object raw : mcEffects) {
      if ((raw != null) && ((raw instanceof MobEffect)))
      {
        MobEffect mcEffect = (MobEffect)raw;
        PotionEffect effect = new PotionEffect(PotionEffectType.getById(mcEffect.getEffectId()), 
          mcEffect.getDuration(), mcEffect.getAmplifier());
        
        effects.add(effect);
      }
    }
    cache.put(Integer.valueOf(damage), effects);
    
    return effects;
  }
  
  public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier)
  {
    return new PotionEffect(potion, potion.isInstant() ? 1 : (int)(duration * potion.getDurationModifier()), 
      amplifier);
  }
}
