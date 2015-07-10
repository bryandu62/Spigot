package net.minecraft.server.v1_8_R3;

import java.util.List;

public abstract class NavigationAbstract
{
  protected EntityInsentient b;
  protected World c;
  protected PathEntity d;
  protected double e;
  private final AttributeInstance a;
  private int f;
  private int g;
  private Vec3D h = new Vec3D(0.0D, 0.0D, 0.0D);
  private float i = 1.0F;
  private final Pathfinder j;
  
  public NavigationAbstract(EntityInsentient ☃, World ☃)
  {
    this.b = ☃;
    this.c = ☃;
    this.a = ☃.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
    this.j = a();
  }
  
  protected abstract Pathfinder a();
  
  public void a(double ☃)
  {
    this.e = ☃;
  }
  
  public float i()
  {
    return (float)this.a.getValue();
  }
  
  public final PathEntity a(double ☃, double ☃, double ☃)
  {
    return a(new BlockPosition(MathHelper.floor(☃), (int)☃, MathHelper.floor(☃)));
  }
  
  public PathEntity a(BlockPosition ☃)
  {
    if (!b()) {
      return null;
    }
    float ☃ = i();
    this.c.methodProfiler.a("pathfind");
    BlockPosition ☃ = new BlockPosition(this.b);
    int ☃ = (int)(☃ + 8.0F);
    
    ChunkCache ☃ = new ChunkCache(this.c, ☃.a(-☃, -☃, -☃), ☃.a(☃, ☃, ☃), 0);
    PathEntity ☃ = this.j.a(☃, this.b, ☃, ☃);
    this.c.methodProfiler.b();
    return ☃;
  }
  
  public boolean a(double ☃, double ☃, double ☃, double ☃)
  {
    PathEntity ☃ = a(MathHelper.floor(☃), (int)☃, MathHelper.floor(☃));
    return a(☃, ☃);
  }
  
  public void a(float ☃)
  {
    this.i = ☃;
  }
  
  public PathEntity a(Entity ☃)
  {
    if (!b()) {
      return null;
    }
    float ☃ = i();
    this.c.methodProfiler.a("pathfind");
    BlockPosition ☃ = new BlockPosition(this.b).up();
    int ☃ = (int)(☃ + 16.0F);
    
    ChunkCache ☃ = new ChunkCache(this.c, ☃.a(-☃, -☃, -☃), ☃.a(☃, ☃, ☃), 0);
    PathEntity ☃ = this.j.a(☃, this.b, ☃, ☃);
    this.c.methodProfiler.b();
    return ☃;
  }
  
  public boolean a(Entity ☃, double ☃)
  {
    PathEntity ☃ = a(☃);
    if (☃ != null) {
      return a(☃, ☃);
    }
    return false;
  }
  
  public boolean a(PathEntity ☃, double ☃)
  {
    if (☃ == null)
    {
      this.d = null;
      return false;
    }
    if (!☃.a(this.d)) {
      this.d = ☃;
    }
    d();
    if (this.d.d() == 0) {
      return false;
    }
    this.e = ☃;
    Vec3D ☃ = c();
    this.g = this.f;
    this.h = ☃;
    return true;
  }
  
  public PathEntity j()
  {
    return this.d;
  }
  
  public void k()
  {
    this.f += 1;
    if (m()) {
      return;
    }
    if (b())
    {
      l();
    }
    else if ((this.d != null) && (this.d.e() < this.d.d()))
    {
      Vec3D ☃ = c();
      Vec3D ☃ = this.d.a(this.b, this.d.e());
      if ((☃.b > ☃.b) && (!this.b.onGround) && (MathHelper.floor(☃.a) == MathHelper.floor(☃.a)) && (MathHelper.floor(☃.c) == MathHelper.floor(☃.c))) {
        this.d.c(this.d.e() + 1);
      }
    }
    if (m()) {
      return;
    }
    Vec3D ☃ = this.d.a(this.b);
    if (☃ == null) {
      return;
    }
    AxisAlignedBB ☃ = new AxisAlignedBB(☃.a, ☃.b, ☃.c, ☃.a, ☃.b, ☃.c).grow(0.5D, 0.5D, 0.5D);
    List<AxisAlignedBB> ☃ = this.c.getCubes(this.b, ☃.a(0.0D, -1.0D, 0.0D));
    double ☃ = -1.0D;
    ☃ = ☃.c(0.0D, 1.0D, 0.0D);
    for (AxisAlignedBB ☃ : ☃) {
      ☃ = ☃.b(☃, ☃);
    }
    this.b.getControllerMove().a(☃.a, ☃.b + ☃, ☃.c, this.e);
  }
  
  protected void l()
  {
    Vec3D ☃ = c();
    
    int ☃ = this.d.d();
    for (int ☃ = this.d.e(); ☃ < this.d.d(); ☃++) {
      if (this.d.a(☃).b != (int)☃.b)
      {
        ☃ = ☃;
        break;
      }
    }
    float ☃ = this.b.width * this.b.width * this.i;
    for (int ☃ = this.d.e(); ☃ < ☃; ☃++)
    {
      Vec3D ☃ = this.d.a(this.b, ☃);
      if (☃.distanceSquared(☃) < ☃) {
        this.d.c(☃ + 1);
      }
    }
    int ☃ = MathHelper.f(this.b.width);
    int ☃ = (int)this.b.length + 1;
    int ☃ = ☃;
    for (int ☃ = ☃ - 1; ☃ >= this.d.e(); ☃--) {
      if (a(☃, this.d.a(this.b, ☃), ☃, ☃, ☃))
      {
        this.d.c(☃);
        break;
      }
    }
    a(☃);
  }
  
  protected void a(Vec3D ☃)
  {
    if (this.f - this.g > 100)
    {
      if (☃.distanceSquared(this.h) < 2.25D) {
        n();
      }
      this.g = this.f;
      this.h = ☃;
    }
  }
  
  public boolean m()
  {
    return (this.d == null) || (this.d.b());
  }
  
  public void n()
  {
    this.d = null;
  }
  
  protected abstract Vec3D c();
  
  protected abstract boolean b();
  
  protected boolean o()
  {
    return (this.b.V()) || (this.b.ab());
  }
  
  protected void d() {}
  
  protected abstract boolean a(Vec3D paramVec3D1, Vec3D paramVec3D2, int paramInt1, int paramInt2, int paramInt3);
}
