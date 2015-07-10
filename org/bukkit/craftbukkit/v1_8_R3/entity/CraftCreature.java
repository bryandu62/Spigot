package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class CraftCreature
  extends CraftLivingEntity
  implements Creature
{
  public CraftCreature(CraftServer server, EntityCreature entity)
  {
    super(server, entity);
  }
  
  public void setTarget(LivingEntity target)
  {
    EntityCreature entity = getHandle();
    if (target == null) {
      entity.setGoalTarget(null, null, false);
    } else if ((target instanceof CraftLivingEntity)) {
      entity.setGoalTarget(((CraftLivingEntity)target).getHandle(), null, false);
    }
  }
  
  public CraftLivingEntity getTarget()
  {
    if (getHandle().getGoalTarget() == null) {
      return null;
    }
    return (CraftLivingEntity)getHandle().getGoalTarget().getBukkitEntity();
  }
  
  public EntityCreature getHandle()
  {
    return (EntityCreature)this.entity;
  }
  
  public String toString()
  {
    return "CraftCreature";
  }
}
