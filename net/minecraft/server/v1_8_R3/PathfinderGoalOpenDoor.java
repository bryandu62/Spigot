package net.minecraft.server.v1_8_R3;

public class PathfinderGoalOpenDoor
  extends PathfinderGoalDoorInteract
{
  boolean g;
  int h;
  
  public PathfinderGoalOpenDoor(EntityInsentient ☃, boolean ☃)
  {
    super(☃);
    this.a = ☃;
    this.g = ☃;
  }
  
  public boolean b()
  {
    return (this.g) && (this.h > 0) && (super.b());
  }
  
  public void c()
  {
    this.h = 20;
    this.c.setDoor(this.a.world, this.b, true);
  }
  
  public void d()
  {
    if (this.g) {
      this.c.setDoor(this.a.world, this.b, false);
    }
  }
  
  public void e()
  {
    this.h -= 1;
    super.e();
  }
}
