package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class PathfinderGoalDefendVillage
  extends PathfinderGoalTarget
{
  EntityIronGolem a;
  EntityLiving b;
  
  public PathfinderGoalDefendVillage(EntityIronGolem entityirongolem)
  {
    super(entityirongolem, false, true);
    this.a = entityirongolem;
    a(1);
  }
  
  public boolean a()
  {
    Village village = this.a.n();
    if (village == null) {
      return false;
    }
    this.b = village.b(this.a);
    if ((this.b instanceof EntityCreeper)) {
      return false;
    }
    if (!a(this.b, false))
    {
      if (this.e.bc().nextInt(20) == 0)
      {
        this.b = village.c(this.a);
        return a(this.b, false);
      }
      return false;
    }
    return true;
  }
  
  public void c()
  {
    this.a.setGoalTarget(this.b, EntityTargetEvent.TargetReason.DEFEND_VILLAGE, true);
    super.c();
  }
}
