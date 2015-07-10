package net.minecraft.server.v1_8_R3;

public class Pathfinder
{
  private Path a = new Path();
  private PathPoint[] b = new PathPoint[32];
  private PathfinderAbstract c;
  
  public Pathfinder(PathfinderAbstract ☃)
  {
    this.c = ☃;
  }
  
  public PathEntity a(IBlockAccess ☃, Entity ☃, Entity ☃, float ☃)
  {
    return a(☃, ☃, ☃.locX, ☃.getBoundingBox().b, ☃.locZ, ☃);
  }
  
  public PathEntity a(IBlockAccess ☃, Entity ☃, BlockPosition ☃, float ☃)
  {
    return a(☃, ☃, ☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, ☃);
  }
  
  private PathEntity a(IBlockAccess ☃, Entity ☃, double ☃, double ☃, double ☃, float ☃)
  {
    this.a.a();
    this.c.a(☃, ☃);
    PathPoint ☃ = this.c.a(☃);
    PathPoint ☃ = this.c.a(☃, ☃, ☃, ☃);
    
    PathEntity ☃ = a(☃, ☃, ☃, ☃);
    
    this.c.a();
    return ☃;
  }
  
  private PathEntity a(Entity ☃, PathPoint ☃, PathPoint ☃, float ☃)
  {
    ☃.e = 0.0F;
    ☃.f = ☃.b(☃);
    ☃.g = ☃.f;
    
    this.a.a();
    this.a.a(☃);
    
    PathPoint ☃ = ☃;
    while (!this.a.e())
    {
      PathPoint ☃ = this.a.c();
      if (☃.equals(☃)) {
        return a(☃, ☃);
      }
      if (☃.b(☃) < ☃.b(☃)) {
        ☃ = ☃;
      }
      ☃.i = true;
      
      int ☃ = this.c.a(this.b, ☃, ☃, ☃, ☃);
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        PathPoint ☃ = this.b[☃];
        
        float ☃ = ☃.e + ☃.b(☃);
        if ((☃ < ☃ * 2.0F) && ((!☃.a()) || (☃ < ☃.e)))
        {
          ☃.h = ☃;
          ☃.e = ☃;
          ☃.f = ☃.b(☃);
          if (☃.a())
          {
            this.a.a(☃, ☃.e + ☃.f);
          }
          else
          {
            ☃.g = (☃.e + ☃.f);
            this.a.a(☃);
          }
        }
      }
    }
    if (☃ == ☃) {
      return null;
    }
    return a(☃, ☃);
  }
  
  private PathEntity a(PathPoint ☃, PathPoint ☃)
  {
    int ☃ = 1;
    PathPoint ☃ = ☃;
    while (☃.h != null)
    {
      ☃++;
      ☃ = ☃.h;
    }
    PathPoint[] ☃ = new PathPoint[☃];
    ☃ = ☃;
    ☃[(--☃)] = ☃;
    while (☃.h != null)
    {
      ☃ = ☃.h;
      ☃[(--☃)] = ☃;
    }
    return new PathEntity(☃);
  }
}
