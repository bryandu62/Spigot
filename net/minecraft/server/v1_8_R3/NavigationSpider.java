package net.minecraft.server.v1_8_R3;

public class NavigationSpider
  extends Navigation
{
  private BlockPosition f;
  
  public NavigationSpider(EntityInsentient ☃, World ☃)
  {
    super(☃, ☃);
  }
  
  public PathEntity a(BlockPosition ☃)
  {
    this.f = ☃;
    return super.a(☃);
  }
  
  public PathEntity a(Entity ☃)
  {
    this.f = new BlockPosition(☃);
    return super.a(☃);
  }
  
  public boolean a(Entity ☃, double ☃)
  {
    PathEntity ☃ = a(☃);
    if (☃ != null) {
      return a(☃, ☃);
    }
    this.f = new BlockPosition(☃);
    this.e = ☃;
    return true;
  }
  
  public void k()
  {
    if (m())
    {
      if (this.f != null)
      {
        double ☃ = this.b.width * this.b.width;
        if ((this.b.c(this.f) < ☃) || ((this.b.locY > this.f.getY()) && (this.b.c(new BlockPosition(this.f.getX(), MathHelper.floor(this.b.locY), this.f.getZ())) < ☃))) {
          this.f = null;
        } else {
          this.b.getControllerMove().a(this.f.getX(), this.f.getY(), this.f.getZ(), this.e);
        }
      }
      return;
    }
    super.k();
  }
}
