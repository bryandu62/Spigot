package net.minecraft.server.v1_8_R3;

public abstract class PathfinderAbstract
{
  protected IBlockAccess a;
  protected IntHashMap<PathPoint> b = new IntHashMap();
  protected int c;
  protected int d;
  protected int e;
  
  public void a(IBlockAccess ☃, Entity ☃)
  {
    this.a = ☃;
    this.b.c();
    
    this.c = MathHelper.d(☃.width + 1.0F);
    this.d = MathHelper.d(☃.length + 1.0F);
    this.e = MathHelper.d(☃.width + 1.0F);
  }
  
  public void a() {}
  
  protected PathPoint a(int ☃, int ☃, int ☃)
  {
    int ☃ = PathPoint.a(☃, ☃, ☃);
    PathPoint ☃ = (PathPoint)this.b.get(☃);
    if (☃ == null)
    {
      ☃ = new PathPoint(☃, ☃, ☃);
      this.b.a(☃, ☃);
    }
    return ☃;
  }
  
  public abstract PathPoint a(Entity paramEntity);
  
  public abstract PathPoint a(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3);
  
  public abstract int a(PathPoint[] paramArrayOfPathPoint, Entity paramEntity, PathPoint paramPathPoint1, PathPoint paramPathPoint2, float paramFloat);
}
