package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class PathfinderGoalNearestAttackableTargetInsentient
  extends PathfinderGoal
{
  private static final Logger a = ;
  private EntityInsentient b;
  private final Predicate<EntityLiving> c;
  private final PathfinderGoalNearestAttackableTarget.DistanceComparator d;
  private EntityLiving e;
  private Class<? extends EntityLiving> f;
  
  public PathfinderGoalNearestAttackableTargetInsentient(EntityInsentient entityinsentient, Class<? extends EntityLiving> oclass)
  {
    this.b = entityinsentient;
    this.f = oclass;
    if ((entityinsentient instanceof EntityCreature)) {
      a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
    }
    this.c = new Predicate()
    {
      public boolean a(EntityLiving entityliving)
      {
        double d0 = PathfinderGoalNearestAttackableTargetInsentient.this.f();
        if (entityliving.isSneaking()) {
          d0 *= 0.800000011920929D;
        }
        return entityliving.g(PathfinderGoalNearestAttackableTargetInsentient.this.b) > d0 ? false : entityliving.isInvisible() ? false : PathfinderGoalTarget.a(PathfinderGoalNearestAttackableTargetInsentient.this.b, entityliving, false, true);
      }
      
      public boolean apply(Object object)
      {
        return a((EntityLiving)object);
      }
    };
    this.d = new PathfinderGoalNearestAttackableTarget.DistanceComparator(entityinsentient);
  }
  
  public boolean a()
  {
    double d0 = f();
    List list = this.b.world.a(this.f, this.b.getBoundingBox().grow(d0, 4.0D, d0), this.c);
    
    Collections.sort(list, this.d);
    if (list.isEmpty()) {
      return false;
    }
    this.e = ((EntityLiving)list.get(0));
    return true;
  }
  
  public boolean b()
  {
    EntityLiving entityliving = this.b.getGoalTarget();
    if (entityliving == null) {
      return false;
    }
    if (!entityliving.isAlive()) {
      return false;
    }
    double d0 = f();
    
    return this.b.h(entityliving) <= d0 * d0;
  }
  
  public void c()
  {
    this.b.setGoalTarget(this.e, EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
    super.c();
  }
  
  public void d()
  {
    this.b.setGoalTarget(null);
    super.c();
  }
  
  protected double f()
  {
    AttributeInstance attributeinstance = this.b.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
    
    return attributeinstance == null ? 16.0D : attributeinstance.getValue();
  }
}
