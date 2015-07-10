package net.minecraft.server.v1_8_R3;

public class PathfinderWater
  extends PathfinderAbstract
{
  public void a(IBlockAccess ☃, Entity ☃)
  {
    super.a(☃, ☃);
  }
  
  public void a()
  {
    super.a();
  }
  
  public PathPoint a(Entity ☃)
  {
    return a(MathHelper.floor(☃.getBoundingBox().a), MathHelper.floor(☃.getBoundingBox().b + 0.5D), MathHelper.floor(☃.getBoundingBox().c));
  }
  
  public PathPoint a(Entity ☃, double ☃, double ☃, double ☃)
  {
    return a(MathHelper.floor(☃ - ☃.width / 2.0F), MathHelper.floor(☃ + 0.5D), MathHelper.floor(☃ - ☃.width / 2.0F));
  }
  
  public int a(PathPoint[] ☃, Entity ☃, PathPoint ☃, PathPoint ☃, float ☃)
  {
    int ☃ = 0;
    for (EnumDirection ☃ : EnumDirection.values())
    {
      PathPoint ☃ = a(☃, ☃.a + ☃.getAdjacentX(), ☃.b + ☃.getAdjacentY(), ☃.c + ☃.getAdjacentZ());
      if ((☃ != null) && (!☃.i) && (☃.a(☃) < ☃)) {
        ☃[(☃++)] = ☃;
      }
    }
    return ☃;
  }
  
  private PathPoint a(Entity ☃, int ☃, int ☃, int ☃)
  {
    int ☃ = b(☃, ☃, ☃, ☃);
    if (☃ == -1) {
      return a(☃, ☃, ☃);
    }
    return null;
  }
  
  private int b(Entity ☃, int ☃, int ☃, int ☃)
  {
    BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
    for (int ☃ = ☃; ☃ < ☃ + this.c; ☃++) {
      for (int ☃ = ☃; ☃ < ☃ + this.d; ☃++) {
        for (int ☃ = ☃; ☃ < ☃ + this.e; ☃++)
        {
          Block ☃ = this.a.getType(☃.c(☃, ☃, ☃)).getBlock();
          if (☃.getMaterial() != Material.WATER) {
            return 0;
          }
        }
      }
    }
    return -1;
  }
}
