package net.minecraft.server.v1_8_R3;

public class NavigationGuardian
  extends NavigationAbstract
{
  public NavigationGuardian(EntityInsentient ☃, World ☃)
  {
    super(☃, ☃);
  }
  
  protected Pathfinder a()
  {
    return new Pathfinder(new PathfinderWater());
  }
  
  protected boolean b()
  {
    return o();
  }
  
  protected Vec3D c()
  {
    return new Vec3D(this.b.locX, this.b.locY + this.b.length * 0.5D, this.b.locZ);
  }
  
  protected void l()
  {
    Vec3D ☃ = c();
    
    float ☃ = this.b.width * this.b.width;
    int ☃ = 6;
    if (☃.distanceSquared(this.d.a(this.b, this.d.e())) < ☃) {
      this.d.a();
    }
    for (int ☃ = Math.min(this.d.e() + ☃, this.d.d() - 1); ☃ > this.d.e(); ☃--)
    {
      Vec3D ☃ = this.d.a(this.b, ☃);
      if (☃.distanceSquared(☃) <= 36.0D) {
        if (a(☃, ☃, 0, 0, 0))
        {
          this.d.c(☃);
          break;
        }
      }
    }
    a(☃);
  }
  
  protected void d()
  {
    super.d();
  }
  
  protected boolean a(Vec3D ☃, Vec3D ☃, int ☃, int ☃, int ☃)
  {
    MovingObjectPosition ☃ = this.c.rayTrace(☃, new Vec3D(☃.a, ☃.b + this.b.length * 0.5D, ☃.c), false, true, false);
    return (☃ == null) || (☃.type == MovingObjectPosition.EnumMovingObjectType.MISS);
  }
}
