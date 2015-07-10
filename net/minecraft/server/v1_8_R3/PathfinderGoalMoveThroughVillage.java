package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class PathfinderGoalMoveThroughVillage
  extends PathfinderGoal
{
  private EntityCreature a;
  private double b;
  private PathEntity c;
  private VillageDoor d;
  private boolean e;
  private List<VillageDoor> f = Lists.newArrayList();
  
  public PathfinderGoalMoveThroughVillage(EntityCreature ☃, double ☃, boolean ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.e = ☃;
    a(1);
    if (!(☃.getNavigation() instanceof Navigation)) {
      throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
    }
  }
  
  public boolean a()
  {
    f();
    if ((this.e) && (this.a.world.w())) {
      return false;
    }
    Village ☃ = this.a.world.ae().getClosestVillage(new BlockPosition(this.a), 0);
    if (☃ == null) {
      return false;
    }
    this.d = a(☃);
    if (this.d == null) {
      return false;
    }
    Navigation ☃ = (Navigation)this.a.getNavigation();
    boolean ☃ = ☃.g();
    ☃.b(false);
    this.c = ☃.a(this.d.d());
    ☃.b(☃);
    if (this.c != null) {
      return true;
    }
    Vec3D ☃ = RandomPositionGenerator.a(this.a, 10, 7, new Vec3D(this.d.d().getX(), this.d.d().getY(), this.d.d().getZ()));
    if (☃ == null) {
      return false;
    }
    ☃.b(false);
    this.c = this.a.getNavigation().a(☃.a, ☃.b, ☃.c);
    ☃.b(☃);
    return this.c != null;
  }
  
  public boolean b()
  {
    if (this.a.getNavigation().m()) {
      return false;
    }
    float ☃ = this.a.width + 4.0F;
    return this.a.b(this.d.d()) > ☃ * ☃;
  }
  
  public void c()
  {
    this.a.getNavigation().a(this.c, this.b);
  }
  
  public void d()
  {
    if ((this.a.getNavigation().m()) || (this.a.b(this.d.d()) < 16.0D)) {
      this.f.add(this.d);
    }
  }
  
  private VillageDoor a(Village ☃)
  {
    VillageDoor ☃ = null;
    int ☃ = Integer.MAX_VALUE;
    List<VillageDoor> ☃ = ☃.f();
    for (VillageDoor ☃ : ☃)
    {
      int ☃ = ☃.b(MathHelper.floor(this.a.locX), MathHelper.floor(this.a.locY), MathHelper.floor(this.a.locZ));
      if (☃ < ☃) {
        if (!a(☃))
        {
          ☃ = ☃;
          ☃ = ☃;
        }
      }
    }
    return ☃;
  }
  
  private boolean a(VillageDoor ☃)
  {
    for (VillageDoor ☃ : this.f) {
      if (☃.d().equals(☃.d())) {
        return true;
      }
    }
    return false;
  }
  
  private void f()
  {
    if (this.f.size() > 15) {
      this.f.remove(0);
    }
  }
}
