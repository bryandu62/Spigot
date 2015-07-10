package net.minecraft.server.v1_8_R3;

public class PathfinderGoalRestrictOpenDoor
  extends PathfinderGoal
{
  private EntityCreature a;
  private VillageDoor b;
  
  public PathfinderGoalRestrictOpenDoor(EntityCreature ☃)
  {
    this.a = ☃;
    if (!(☃.getNavigation() instanceof Navigation)) {
      throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
    }
  }
  
  public boolean a()
  {
    if (this.a.world.w()) {
      return false;
    }
    BlockPosition ☃ = new BlockPosition(this.a);
    
    Village ☃ = this.a.world.ae().getClosestVillage(☃, 16);
    if (☃ == null) {
      return false;
    }
    this.b = ☃.b(☃);
    if (this.b == null) {
      return false;
    }
    return this.b.b(☃) < 2.25D;
  }
  
  public boolean b()
  {
    if (this.a.world.w()) {
      return false;
    }
    return (!this.b.i()) && (this.b.c(new BlockPosition(this.a)));
  }
  
  public void c()
  {
    ((Navigation)this.a.getNavigation()).b(false);
    ((Navigation)this.a.getNavigation()).c(false);
  }
  
  public void d()
  {
    ((Navigation)this.a.getNavigation()).b(true);
    ((Navigation)this.a.getNavigation()).c(true);
    this.b = null;
  }
  
  public void e()
  {
    this.b.b();
  }
}
