package net.minecraft.server.v1_8_R3;

public class Navigation
  extends NavigationAbstract
{
  protected PathfinderNormal a;
  private boolean f;
  
  public Navigation(EntityInsentient ☃, World ☃)
  {
    super(☃, ☃);
  }
  
  protected Pathfinder a()
  {
    this.a = new PathfinderNormal();
    this.a.a(true);
    return new Pathfinder(this.a);
  }
  
  protected boolean b()
  {
    return (this.b.onGround) || ((h()) && (o())) || ((this.b.au()) && ((this.b instanceof EntityZombie)) && ((this.b.vehicle instanceof EntityChicken)));
  }
  
  protected Vec3D c()
  {
    return new Vec3D(this.b.locX, p(), this.b.locZ);
  }
  
  private int p()
  {
    if ((!this.b.V()) || (!h())) {
      return (int)(this.b.getBoundingBox().b + 0.5D);
    }
    int ☃ = (int)this.b.getBoundingBox().b;
    Block ☃ = this.c.getType(new BlockPosition(MathHelper.floor(this.b.locX), ☃, MathHelper.floor(this.b.locZ))).getBlock();
    int ☃ = 0;
    while ((☃ == Blocks.FLOWING_WATER) || (☃ == Blocks.WATER))
    {
      ☃++;
      ☃ = this.c.getType(new BlockPosition(MathHelper.floor(this.b.locX), ☃, MathHelper.floor(this.b.locZ))).getBlock();
      ☃++;
      if (☃ > 16) {
        return (int)this.b.getBoundingBox().b;
      }
    }
    return ☃;
  }
  
  protected void d()
  {
    super.d();
    if (this.f)
    {
      if (this.c.i(new BlockPosition(MathHelper.floor(this.b.locX), (int)(this.b.getBoundingBox().b + 0.5D), MathHelper.floor(this.b.locZ)))) {
        return;
      }
      for (int ☃ = 0; ☃ < this.d.d(); ☃++)
      {
        PathPoint ☃ = this.d.a(☃);
        if (this.c.i(new BlockPosition(☃.a, ☃.b, ☃.c)))
        {
          this.d.b(☃ - 1);
          return;
        }
      }
    }
  }
  
  protected boolean a(Vec3D ☃, Vec3D ☃, int ☃, int ☃, int ☃)
  {
    int ☃ = MathHelper.floor(☃.a);
    int ☃ = MathHelper.floor(☃.c);
    
    double ☃ = ☃.a - ☃.a;
    double ☃ = ☃.c - ☃.c;
    double ☃ = ☃ * ☃ + ☃ * ☃;
    if (☃ < 1.0E-8D) {
      return false;
    }
    double ☃ = 1.0D / Math.sqrt(☃);
    ☃ *= ☃;
    ☃ *= ☃;
    
    ☃ += 2;
    ☃ += 2;
    if (!a(☃, (int)☃.b, ☃, ☃, ☃, ☃, ☃, ☃, ☃)) {
      return false;
    }
    ☃ -= 2;
    ☃ -= 2;
    
    double ☃ = 1.0D / Math.abs(☃);
    double ☃ = 1.0D / Math.abs(☃);
    
    double ☃ = ☃ * 1 - ☃.a;
    double ☃ = ☃ * 1 - ☃.c;
    if (☃ >= 0.0D) {
      ☃ += 1.0D;
    }
    if (☃ >= 0.0D) {
      ☃ += 1.0D;
    }
    ☃ /= ☃;
    ☃ /= ☃;
    
    int ☃ = ☃ < 0.0D ? -1 : 1;
    int ☃ = ☃ < 0.0D ? -1 : 1;
    int ☃ = MathHelper.floor(☃.a);
    int ☃ = MathHelper.floor(☃.c);
    int ☃ = ☃ - ☃;
    int ☃ = ☃ - ☃;
    while ((☃ * ☃ > 0) || (☃ * ☃ > 0))
    {
      if (☃ < ☃)
      {
        ☃ += ☃;
        ☃ += ☃;
        ☃ = ☃ - ☃;
      }
      else
      {
        ☃ += ☃;
        ☃ += ☃;
        ☃ = ☃ - ☃;
      }
      if (!a(☃, (int)☃.b, ☃, ☃, ☃, ☃, ☃, ☃, ☃)) {
        return false;
      }
    }
    return true;
  }
  
  private boolean a(int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, Vec3D ☃, double ☃, double ☃)
  {
    int ☃ = ☃ - ☃ / 2;
    int ☃ = ☃ - ☃ / 2;
    if (!b(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃)) {
      return false;
    }
    for (int ☃ = ☃; ☃ < ☃ + ☃; ☃++) {
      for (int ☃ = ☃; ☃ < ☃ + ☃; ☃++)
      {
        double ☃ = ☃ + 0.5D - ☃.a;
        double ☃ = ☃ + 0.5D - ☃.c;
        if (☃ * ☃ + ☃ * ☃ >= 0.0D)
        {
          Block ☃ = this.c.getType(new BlockPosition(☃, ☃ - 1, ☃)).getBlock();
          Material ☃ = ☃.getMaterial();
          if (☃ == Material.AIR) {
            return false;
          }
          if ((☃ == Material.WATER) && (!this.b.V())) {
            return false;
          }
          if (☃ == Material.LAVA) {
            return false;
          }
        }
      }
    }
    return true;
  }
  
  private boolean b(int ☃, int ☃, int ☃, int ☃, int ☃, int ☃, Vec3D ☃, double ☃, double ☃)
  {
    for (BlockPosition ☃ : BlockPosition.a(new BlockPosition(☃, ☃, ☃), new BlockPosition(☃ + ☃ - 1, ☃ + ☃ - 1, ☃ + ☃ - 1)))
    {
      double ☃ = ☃.getX() + 0.5D - ☃.a;
      double ☃ = ☃.getZ() + 0.5D - ☃.c;
      if (☃ * ☃ + ☃ * ☃ >= 0.0D)
      {
        Block ☃ = this.c.getType(☃).getBlock();
        if (!☃.b(this.c, ☃)) {
          return false;
        }
      }
    }
    return true;
  }
  
  public void a(boolean ☃)
  {
    this.a.c(☃);
  }
  
  public boolean e()
  {
    return this.a.e();
  }
  
  public void b(boolean ☃)
  {
    this.a.b(☃);
  }
  
  public void c(boolean ☃)
  {
    this.a.a(☃);
  }
  
  public boolean g()
  {
    return this.a.b();
  }
  
  public void d(boolean ☃)
  {
    this.a.d(☃);
  }
  
  public boolean h()
  {
    return this.a.d();
  }
  
  public void e(boolean ☃)
  {
    this.f = ☃;
  }
}
