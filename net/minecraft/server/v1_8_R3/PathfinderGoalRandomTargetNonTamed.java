package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public class PathfinderGoalRandomTargetNonTamed<T extends EntityLiving>
  extends PathfinderGoalNearestAttackableTarget
{
  private EntityTameableAnimal g;
  
  public PathfinderGoalRandomTargetNonTamed(EntityTameableAnimal ☃, Class<T> ☃, boolean ☃, Predicate<? super T> ☃)
  {
    super(☃, ☃, 10, ☃, false, ☃);
    this.g = ☃;
  }
  
  public boolean a()
  {
    return (!this.g.isTamed()) && (super.a());
  }
}
