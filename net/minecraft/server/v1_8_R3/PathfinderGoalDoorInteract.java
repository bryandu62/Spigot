package net.minecraft.server.v1_8_R3;

public abstract class PathfinderGoalDoorInteract
  extends PathfinderGoal
{
  protected EntityInsentient a;
  protected BlockPosition b = BlockPosition.ZERO;
  protected BlockDoor c;
  boolean d;
  float e;
  float f;
  
  public PathfinderGoalDoorInteract(EntityInsentient ☃)
  {
    this.a = ☃;
    if (!(☃.getNavigation() instanceof Navigation)) {
      throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
    }
  }
  
  public boolean a()
  {
    if (!this.a.positionChanged) {
      return false;
    }
    Navigation ☃ = (Navigation)this.a.getNavigation();
    PathEntity ☃ = ☃.j();
    if ((☃ == null) || (☃.b()) || (!☃.g())) {
      return false;
    }
    for (int ☃ = 0; ☃ < Math.min(☃.e() + 2, ☃.d()); ☃++)
    {
      PathPoint ☃ = ☃.a(☃);
      this.b = new BlockPosition(☃.a, ☃.b + 1, ☃.c);
      if (this.a.e(this.b.getX(), this.a.locY, this.b.getZ()) <= 2.25D)
      {
        this.c = a(this.b);
        if (this.c != null) {
          return true;
        }
      }
    }
    this.b = new BlockPosition(this.a).up();
    this.c = a(this.b);
    return this.c != null;
  }
  
  public boolean b()
  {
    return !this.d;
  }
  
  public void c()
  {
    this.d = false;
    this.e = ((float)(this.b.getX() + 0.5F - this.a.locX));
    this.f = ((float)(this.b.getZ() + 0.5F - this.a.locZ));
  }
  
  public void e()
  {
    float ☃ = (float)(this.b.getX() + 0.5F - this.a.locX);
    float ☃ = (float)(this.b.getZ() + 0.5F - this.a.locZ);
    float ☃ = this.e * ☃ + this.f * ☃;
    if (☃ < 0.0F) {
      this.d = true;
    }
  }
  
  private BlockDoor a(BlockPosition ☃)
  {
    Block ☃ = this.a.world.getType(☃).getBlock();
    if (((☃ instanceof BlockDoor)) && (☃.getMaterial() == Material.WOOD)) {
      return (BlockDoor)☃;
    }
    return null;
  }
}
