package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalMoveIndoors
  extends PathfinderGoal
{
  private int d = -1;
  private int c = -1;
  private VillageDoor b;
  private EntityCreature a;
  
  public PathfinderGoalMoveIndoors(EntityCreature ☃)
  {
    this.a = ☃;
    a(1);
  }
  
  public boolean a()
  {
    BlockPosition ☃ = new BlockPosition(this.a);
    if (((this.a.world.w()) && ((!this.a.world.S()) || (this.a.world.getBiome(☃).e()))) || (this.a.world.worldProvider.o())) {
      return false;
    }
    if (this.a.bc().nextInt(50) != 0) {
      return false;
    }
    if ((this.c != -1) && (this.a.e(this.c, this.a.locY, this.d) < 4.0D)) {
      return false;
    }
    Village ☃ = this.a.world.ae().getClosestVillage(☃, 14);
    if (☃ == null) {
      return false;
    }
    this.b = ☃.c(☃);
    return this.b != null;
  }
  
  public boolean b()
  {
    return !this.a.getNavigation().m();
  }
  
  public void c()
  {
    this.c = -1;
    BlockPosition ☃ = this.b.e();
    int ☃ = ☃.getX();
    int ☃ = ☃.getY();
    int ☃ = ☃.getZ();
    if (this.a.b(☃) > 256.0D)
    {
      Vec3D ☃ = RandomPositionGenerator.a(this.a, 14, 3, new Vec3D(☃ + 0.5D, ☃, ☃ + 0.5D));
      if (☃ != null) {
        this.a.getNavigation().a(☃.a, ☃.b, ☃.c, 1.0D);
      }
    }
    else
    {
      this.a.getNavigation().a(☃ + 0.5D, ☃, ☃ + 0.5D, 1.0D);
    }
  }
  
  public void d()
  {
    this.c = this.b.e().getX();
    this.d = this.b.e().getZ();
    this.b = null;
  }
}
