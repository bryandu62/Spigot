package net.minecraft.server.v1_8_R3;

public class VillageDoor
{
  private final BlockPosition a;
  private final BlockPosition b;
  private final EnumDirection c;
  private int d;
  private boolean e;
  private int f;
  
  public VillageDoor(BlockPosition ☃, int ☃, int ☃, int ☃)
  {
    this(☃, a(☃, ☃), ☃);
  }
  
  private static EnumDirection a(int ☃, int ☃)
  {
    if (☃ < 0) {
      return EnumDirection.WEST;
    }
    if (☃ > 0) {
      return EnumDirection.EAST;
    }
    if (☃ < 0) {
      return EnumDirection.NORTH;
    }
    return EnumDirection.SOUTH;
  }
  
  public VillageDoor(BlockPosition ☃, EnumDirection ☃, int ☃)
  {
    this.a = ☃;
    this.c = ☃;
    this.b = ☃.shift(☃, 2);
    this.d = ☃;
  }
  
  public int b(int ☃, int ☃, int ☃)
  {
    return (int)this.a.c(☃, ☃, ☃);
  }
  
  public int a(BlockPosition ☃)
  {
    return (int)☃.i(d());
  }
  
  public int b(BlockPosition ☃)
  {
    return (int)this.b.i(☃);
  }
  
  public boolean c(BlockPosition ☃)
  {
    int ☃ = ☃.getX() - this.a.getX();
    int ☃ = ☃.getZ() - this.a.getY();
    return ☃ * this.c.getAdjacentX() + ☃ * this.c.getAdjacentZ() >= 0;
  }
  
  public void a()
  {
    this.f = 0;
  }
  
  public void b()
  {
    this.f += 1;
  }
  
  public int c()
  {
    return this.f;
  }
  
  public BlockPosition d()
  {
    return this.a;
  }
  
  public BlockPosition e()
  {
    return this.b;
  }
  
  public int f()
  {
    return this.c.getAdjacentX() * 2;
  }
  
  public int g()
  {
    return this.c.getAdjacentZ() * 2;
  }
  
  public int h()
  {
    return this.d;
  }
  
  public void a(int ☃)
  {
    this.d = ☃;
  }
  
  public boolean i()
  {
    return this.e;
  }
  
  public void a(boolean ☃)
  {
    this.e = ☃;
  }
}
