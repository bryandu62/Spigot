package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class PathfinderGoalBreed
  extends PathfinderGoal
{
  private EntityAnimal d;
  World a;
  private EntityAnimal e;
  int b;
  double c;
  
  public PathfinderGoalBreed(EntityAnimal entityanimal, double d0)
  {
    this.d = entityanimal;
    this.a = entityanimal.world;
    this.c = d0;
    a(3);
  }
  
  public boolean a()
  {
    if (!this.d.isInLove()) {
      return false;
    }
    this.e = f();
    return this.e != null;
  }
  
  public boolean b()
  {
    return (this.e.isAlive()) && (this.e.isInLove()) && (this.b < 60);
  }
  
  public void d()
  {
    this.e = null;
    this.b = 0;
  }
  
  public void e()
  {
    this.d.getControllerLook().a(this.e, 10.0F, this.d.bQ());
    this.d.getNavigation().a(this.e, this.c);
    this.b += 1;
    if ((this.b >= 60) && (this.d.h(this.e) < 9.0D)) {
      g();
    }
  }
  
  private EntityAnimal f()
  {
    float f = 8.0F;
    List list = this.a.a(this.d.getClass(), this.d.getBoundingBox().grow(f, f, f));
    double d0 = Double.MAX_VALUE;
    EntityAnimal entityanimal = null;
    Iterator iterator = list.iterator();
    while (iterator.hasNext())
    {
      EntityAnimal entityanimal1 = (EntityAnimal)iterator.next();
      if ((this.d.mate(entityanimal1)) && (this.d.h(entityanimal1) < d0))
      {
        entityanimal = entityanimal1;
        d0 = this.d.h(entityanimal1);
      }
    }
    return entityanimal;
  }
  
  private void g()
  {
    EntityAgeable entityageable = this.d.createChild(this.e);
    if (entityageable != null)
    {
      if (((entityageable instanceof EntityTameableAnimal)) && (((EntityTameableAnimal)entityageable).isTamed())) {
        entityageable.persistent = true;
      }
      EntityHuman entityhuman = this.d.cq();
      if ((entityhuman == null) && (this.e.cq() != null)) {
        entityhuman = this.e.cq();
      }
      if (entityhuman != null)
      {
        entityhuman.b(StatisticList.A);
        if ((this.d instanceof EntityCow)) {
          entityhuman.b(AchievementList.H);
        }
      }
      this.d.setAgeRaw(6000);
      this.e.setAgeRaw(6000);
      this.d.cs();
      this.e.cs();
      entityageable.setAgeRaw(41536);
      entityageable.setPositionRotation(this.d.locX, this.d.locY, this.d.locZ, 0.0F, 0.0F);
      this.a.addEntity(entityageable, CreatureSpawnEvent.SpawnReason.BREEDING);
      Random random = this.d.bc();
      for (int i = 0; i < 7; i++)
      {
        double d0 = random.nextGaussian() * 0.02D;
        double d1 = random.nextGaussian() * 0.02D;
        double d2 = random.nextGaussian() * 0.02D;
        double d3 = random.nextDouble() * this.d.width * 2.0D - this.d.width;
        double d4 = 0.5D + random.nextDouble() * this.d.length;
        double d5 = random.nextDouble() * this.d.width * 2.0D - this.d.width;
        
        this.a.addParticle(EnumParticle.HEART, this.d.locX + d3, this.d.locY + d4, this.d.locZ + d5, d0, d1, d2, new int[0]);
      }
      if (this.a.getGameRules().getBoolean("doMobLoot")) {
        this.a.addEntity(new EntityExperienceOrb(this.a, this.d.locX, this.d.locY, this.d.locZ, random.nextInt(7) + 1));
      }
    }
  }
}