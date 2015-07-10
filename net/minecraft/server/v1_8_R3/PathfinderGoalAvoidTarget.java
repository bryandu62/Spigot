package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;

public class PathfinderGoalAvoidTarget<T extends Entity>
  extends PathfinderGoal
{
  private final Predicate<Entity> c = new Predicate()
  {
    public boolean a(Entity ☃)
    {
      return (☃.isAlive()) && (PathfinderGoalAvoidTarget.this.a.getEntitySenses().a(☃));
    }
  };
  protected EntityCreature a;
  private double d;
  private double e;
  protected T b;
  private float f;
  private PathEntity g;
  private NavigationAbstract h;
  private Class<T> i;
  private Predicate<? super T> j;
  
  public PathfinderGoalAvoidTarget(EntityCreature ☃, Class<T> ☃, float ☃, double ☃, double ☃)
  {
    this(☃, ☃, Predicates.alwaysTrue(), ☃, ☃, ☃);
  }
  
  public PathfinderGoalAvoidTarget(EntityCreature ☃, Class<T> ☃, Predicate<? super T> ☃, float ☃, double ☃, double ☃)
  {
    this.a = ☃;
    this.i = ☃;
    this.j = ☃;
    this.f = ☃;
    this.d = ☃;
    this.e = ☃;
    this.h = ☃.getNavigation();
    a(1);
  }
  
  public boolean a()
  {
    List<T> ☃ = this.a.world.a(this.i, this.a.getBoundingBox().grow(this.f, 3.0D, this.f), Predicates.and(new Predicate[] { IEntitySelector.d, this.c, this.j }));
    if (☃.isEmpty()) {
      return false;
    }
    this.b = ((Entity)☃.get(0));
    
    Vec3D ☃ = RandomPositionGenerator.b(this.a, 16, 7, new Vec3D(this.b.locX, this.b.locY, this.b.locZ));
    if (☃ == null) {
      return false;
    }
    if (this.b.e(☃.a, ☃.b, ☃.c) < this.b.h(this.a)) {
      return false;
    }
    this.g = this.h.a(☃.a, ☃.b, ☃.c);
    if (this.g == null) {
      return false;
    }
    if (!this.g.b(☃)) {
      return false;
    }
    return true;
  }
  
  public boolean b()
  {
    return !this.h.m();
  }
  
  public void c()
  {
    this.h.a(this.g, this.d);
  }
  
  public void d()
  {
    this.b = null;
  }
  
  public void e()
  {
    if (this.a.h(this.b) < 49.0D) {
      this.a.getNavigation().a(this.e);
    } else {
      this.a.getNavigation().a(this.d);
    }
  }
}
