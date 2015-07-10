package org.bukkit.event.entity;

import com.google.common.base.Function;
import java.util.Map;
import org.bukkit.entity.Entity;

public class EntityDamageByEntityEvent
  extends EntityDamageEvent
{
  private final Entity damager;
  
  @Deprecated
  public EntityDamageByEntityEvent(Entity damager, Entity damagee, EntityDamageEvent.DamageCause cause, int damage)
  {
    this(damager, damagee, cause, damage);
  }
  
  @Deprecated
  public EntityDamageByEntityEvent(Entity damager, Entity damagee, EntityDamageEvent.DamageCause cause, double damage)
  {
    super(damagee, cause, damage);
    this.damager = damager;
  }
  
  public EntityDamageByEntityEvent(Entity damager, Entity damagee, EntityDamageEvent.DamageCause cause, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions)
  {
    super(damagee, cause, modifiers, modifierFunctions);
    this.damager = damager;
  }
  
  public Entity getDamager()
  {
    return this.damager;
  }
}
