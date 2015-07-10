package org.bukkit.event.entity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityDamageEvent
  extends EntityEvent
  implements Cancellable
{
  private static final HandlerList handlers = new HandlerList();
  private static final DamageModifier[] MODIFIERS = DamageModifier.values();
  private static final Function<? super Double, Double> ZERO = Functions.constant(Double.valueOf(-0.0D));
  private final Map<DamageModifier, Double> modifiers;
  private final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions;
  private final Map<DamageModifier, Double> originals;
  private boolean cancelled;
  private final DamageCause cause;
  
  @Deprecated
  public EntityDamageEvent(Entity damagee, DamageCause cause, int damage)
  {
    this(damagee, cause, damage);
  }
  
  @Deprecated
  public EntityDamageEvent(Entity damagee, DamageCause cause, double damage)
  {
    this(damagee, cause, new EnumMap(ImmutableMap.of(DamageModifier.BASE, Double.valueOf(damage))), new EnumMap(ImmutableMap.of(DamageModifier.BASE, ZERO)));
  }
  
  public EntityDamageEvent(Entity damagee, DamageCause cause, Map<DamageModifier, Double> modifiers, Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions)
  {
    super(damagee);
    Validate.isTrue(modifiers.containsKey(DamageModifier.BASE), "BASE DamageModifier missing");
    Validate.isTrue(!modifiers.containsKey(null), "Cannot have null DamageModifier");
    Validate.noNullElements(modifiers.values(), "Cannot have null modifier values");
    Validate.isTrue(modifiers.keySet().equals(modifierFunctions.keySet()), "Must have a modifier function for each DamageModifier");
    Validate.noNullElements(modifierFunctions.values(), "Cannot have null modifier function");
    this.originals = new EnumMap(modifiers);
    this.cause = cause;
    this.modifiers = modifiers;
    this.modifierFunctions = modifierFunctions;
  }
  
  public boolean isCancelled()
  {
    return this.cancelled;
  }
  
  public void setCancelled(boolean cancel)
  {
    this.cancelled = cancel;
  }
  
  public double getOriginalDamage(DamageModifier type)
    throws IllegalArgumentException
  {
    Double damage = (Double)this.originals.get(type);
    if (damage != null) {
      return damage.doubleValue();
    }
    if (type == null) {
      throw new IllegalArgumentException("Cannot have null DamageModifier");
    }
    return 0.0D;
  }
  
  public void setDamage(DamageModifier type, double damage)
    throws IllegalArgumentException, UnsupportedOperationException
  {
    if (!this.modifiers.containsKey(type)) {
      throw (type == null ? new IllegalArgumentException("Cannot have null DamageModifier") : new UnsupportedOperationException(type + " is not applicable to " + getEntity()));
    }
    this.modifiers.put(type, Double.valueOf(damage));
  }
  
  public double getDamage(DamageModifier type)
    throws IllegalArgumentException
  {
    Validate.notNull(type, "Cannot have null DamageModifier");
    Double damage = (Double)this.modifiers.get(type);
    return damage == null ? 0.0D : damage.doubleValue();
  }
  
  public boolean isApplicable(DamageModifier type)
    throws IllegalArgumentException
  {
    Validate.notNull(type, "Cannot have null DamageModifier");
    return this.modifiers.containsKey(type);
  }
  
  public double getDamage()
  {
    return getDamage(DamageModifier.BASE);
  }
  
  public final double getFinalDamage()
  {
    double damage = 0.0D;
    DamageModifier[] arrayOfDamageModifier;
    int i = (arrayOfDamageModifier = MODIFIERS).length;
    for (int j = 0; j < i; j++)
    {
      DamageModifier modifier = arrayOfDamageModifier[j];
      damage += getDamage(modifier);
    }
    return damage;
  }
  
  public void setDamage(double damage)
  {
    double remaining = damage;
    double oldRemaining = getDamage(DamageModifier.BASE);
    DamageModifier[] arrayOfDamageModifier;
    int i = (arrayOfDamageModifier = MODIFIERS).length;
    for (int j = 0; j < i; j++)
    {
      DamageModifier modifier = arrayOfDamageModifier[j];
      if (isApplicable(modifier))
      {
        Function<? super Double, Double> modifierFunction = (Function)this.modifierFunctions.get(modifier);
        double newVanilla = ((Double)modifierFunction.apply(Double.valueOf(remaining))).doubleValue();
        double oldVanilla = ((Double)modifierFunction.apply(Double.valueOf(oldRemaining))).doubleValue();
        double difference = oldVanilla - newVanilla;
        
        double old = getDamage(modifier);
        if (old > 0.0D) {
          setDamage(modifier, Math.max(0.0D, old - difference));
        } else {
          setDamage(modifier, Math.min(0.0D, old - difference));
        }
        remaining += newVanilla;
        oldRemaining += oldVanilla;
      }
    }
    setDamage(DamageModifier.BASE, damage);
  }
  
  public DamageCause getCause()
  {
    return this.cause;
  }
  
  public HandlerList getHandlers()
  {
    return handlers;
  }
  
  public static HandlerList getHandlerList()
  {
    return handlers;
  }
  
  public static enum DamageModifier
  {
    BASE,  HARD_HAT,  BLOCKING,  ARMOR,  RESISTANCE,  MAGIC,  ABSORPTION;
  }
  
  public static enum DamageCause
  {
    CONTACT,  ENTITY_ATTACK,  PROJECTILE,  SUFFOCATION,  FALL,  FIRE,  FIRE_TICK,  MELTING,  LAVA,  DROWNING,  BLOCK_EXPLOSION,  ENTITY_EXPLOSION,  VOID,  LIGHTNING,  SUICIDE,  STARVATION,  POISON,  MAGIC,  WITHER,  FALLING_BLOCK,  THORNS,  CUSTOM;
  }
}
