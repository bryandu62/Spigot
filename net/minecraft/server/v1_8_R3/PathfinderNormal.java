package net.minecraft.server.v1_8_R3;

public class PathfinderNormal
  extends PathfinderAbstract
{
  private boolean f;
  private boolean g;
  private boolean h;
  private boolean i;
  private boolean j;
  
  public void a(IBlockAccess ☃, Entity ☃)
  {
    super.a(☃, ☃);
    this.j = this.h;
  }
  
  public void a()
  {
    super.a();
    this.h = this.j;
  }
  
  public PathPoint a(Entity ☃)
  {
    int ☃;
    if ((this.i) && (☃.V()))
    {
      int ☃ = (int)☃.getBoundingBox().b;
      BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition(MathHelper.floor(☃.locX), ☃, MathHelper.floor(☃.locZ));
      Block ☃ = this.a.getType(☃).getBlock();
      while ((☃ == Blocks.FLOWING_WATER) || (☃ == Blocks.WATER))
      {
        ☃++;
        ☃.c(MathHelper.floor(☃.locX), ☃, MathHelper.floor(☃.locZ));
        ☃ = this.a.getType(☃).getBlock();
      }
      this.h = false;
    }
    else
    {
      ☃ = MathHelper.floor(☃.getBoundingBox().b + 0.5D);
    }
    return a(MathHelper.floor(☃.getBoundingBox().a), ☃, MathHelper.floor(☃.getBoundingBox().c));
  }
  
  public PathPoint a(Entity ☃, double ☃, double ☃, double ☃)
  {
    return a(MathHelper.floor(☃ - ☃.width / 2.0F), MathHelper.floor(☃), MathHelper.floor(☃ - ☃.width / 2.0F));
  }
  
  public int a(PathPoint[] ☃, Entity ☃, PathPoint ☃, PathPoint ☃, float ☃)
  {
    int ☃ = 0;
    
    int ☃ = 0;
    if (a(☃, ☃.a, ☃.b + 1, ☃.c) == 1) {
      ☃ = 1;
    }
    PathPoint ☃ = a(☃, ☃.a, ☃.b, ☃.c + 1, ☃);
    PathPoint ☃ = a(☃, ☃.a - 1, ☃.b, ☃.c, ☃);
    PathPoint ☃ = a(☃, ☃.a + 1, ☃.b, ☃.c, ☃);
    PathPoint ☃ = a(☃, ☃.a, ☃.b, ☃.c - 1, ☃);
    if ((☃ != null) && (!☃.i) && (☃.a(☃) < ☃)) {
      ☃[(☃++)] = ☃;
    }
    if ((☃ != null) && (!☃.i) && (☃.a(☃) < ☃)) {
      ☃[(☃++)] = ☃;
    }
    if ((☃ != null) && (!☃.i) && (☃.a(☃) < ☃)) {
      ☃[(☃++)] = ☃;
    }
    if ((☃ != null) && (!☃.i) && (☃.a(☃) < ☃)) {
      ☃[(☃++)] = ☃;
    }
    return ☃;
  }
  
  private PathPoint a(Entity ☃, int ☃, int ☃, int ☃, int ☃)
  {
    PathPoint ☃ = null;
    int ☃ = a(☃, ☃, ☃, ☃);
    if (☃ == 2) {
      return a(☃, ☃, ☃);
    }
    if (☃ == 1) {
      ☃ = a(☃, ☃, ☃);
    }
    if ((☃ == null) && (☃ > 0) && (☃ != -3) && (☃ != -4) && (a(☃, ☃, ☃ + ☃, ☃) == 1))
    {
      ☃ = a(☃, ☃ + ☃, ☃);
      ☃ += ☃;
    }
    if (☃ != null)
    {
      int ☃ = 0;
      int ☃ = 0;
      while (☃ > 0)
      {
        ☃ = a(☃, ☃, ☃ - 1, ☃);
        if ((this.h) && (☃ == -1)) {
          return null;
        }
        if (☃ == 1)
        {
          if (☃++ >= ☃.aE()) {
            return null;
          }
          ☃--;
          if (☃ > 0) {
            ☃ = a(☃, ☃, ☃);
          } else {
            return null;
          }
        }
      }
      if (☃ == -2) {
        return null;
      }
    }
    return ☃;
  }
  
  private int a(Entity ☃, int ☃, int ☃, int ☃)
  {
    return a(this.a, ☃, ☃, ☃, ☃, this.c, this.d, this.e, this.h, this.g, this.f);
  }
  
  public static int a(IBlockAccess ☃, Entity ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, boolean ☃, boolean ☃, boolean ☃)
  {
    boolean ☃ = false;
    BlockPosition ☃ = new BlockPosition(☃);
    
    BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
    for (int ☃ = ☃; ☃ < ☃ + ☃; ☃++) {
      for (int ☃ = ☃; ☃ < ☃ + ☃; ☃++) {
        for (int ☃ = ☃; ☃ < ☃ + ☃; ☃++)
        {
          ☃.c(☃, ☃, ☃);
          Block ☃ = ☃.getType(☃).getBlock();
          if (☃.getMaterial() != Material.AIR)
          {
            if ((☃ == Blocks.TRAPDOOR) || (☃ == Blocks.IRON_TRAPDOOR))
            {
              ☃ = true;
            }
            else if ((☃ == Blocks.FLOWING_WATER) || (☃ == Blocks.WATER))
            {
              if (☃) {
                return -1;
              }
              ☃ = true;
            }
            else if ((!☃) && ((☃ instanceof BlockDoor)) && (☃.getMaterial() == Material.WOOD))
            {
              return 0;
            }
            if ((☃.world.getType(☃).getBlock() instanceof BlockMinecartTrackAbstract))
            {
              if ((!(☃.world.getType(☃).getBlock() instanceof BlockMinecartTrackAbstract)) && (!(☃.world.getType(☃.down()).getBlock() instanceof BlockMinecartTrackAbstract))) {
                return -3;
              }
            }
            else if (!☃.b(☃, ☃)) {
              if ((!☃) || (!(☃ instanceof BlockDoor)) || (☃.getMaterial() != Material.WOOD))
              {
                if (((☃ instanceof BlockFence)) || ((☃ instanceof BlockFenceGate)) || ((☃ instanceof BlockCobbleWall))) {
                  return -3;
                }
                if ((☃ == Blocks.TRAPDOOR) || (☃ == Blocks.IRON_TRAPDOOR)) {
                  return -4;
                }
                Material ☃ = ☃.getMaterial();
                if (☃ == Material.LAVA)
                {
                  if (!☃.ab()) {
                    return -2;
                  }
                }
                else {
                  return 0;
                }
              }
            }
          }
        }
      }
    }
    return ☃ ? 2 : 1;
  }
  
  public void a(boolean ☃)
  {
    this.f = ☃;
  }
  
  public void b(boolean ☃)
  {
    this.g = ☃;
  }
  
  public void c(boolean ☃)
  {
    this.h = ☃;
  }
  
  public void d(boolean ☃)
  {
    this.i = ☃;
  }
  
  public boolean b()
  {
    return this.f;
  }
  
  public boolean d()
  {
    return this.i;
  }
  
  public boolean e()
  {
    return this.h;
  }
}
