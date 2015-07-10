package net.minecraft.server.v1_8_R3;

import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class PathfinderGoalOwnerHurtTarget
  extends PathfinderGoalTarget
{
  EntityTameableAnimal a;
  EntityLiving b;
  private int c;
  
  public PathfinderGoalOwnerHurtTarget(EntityTameableAnimal entitytameableanimal)
  {
    super(entitytameableanimal, false);
    this.a = entitytameableanimal;
    a(1);
  }
  
  public boolean a()
  {
    if (!this.a.isTamed()) {
      return false;
    }
    EntityLiving entityliving = this.a.getOwner();
    if (entityliving == null) {
      return false;
    }
    this.b = entityliving.bf();
    int i = entityliving.bg();
    
    return (i != this.c) && (a(this.b, false)) && (this.a.a(this.b, entityliving));
  }
  
  public void c()
  {
    this.e.setGoalTarget(this.b, EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true);
    EntityLiving entityliving = this.a.getOwner();
    if (entityliving != null) {
      this.c = entityliving.bg();
    }
    super.c();
  }
}
