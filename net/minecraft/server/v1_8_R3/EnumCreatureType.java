package net.minecraft.server.v1_8_R3;

public enum EnumCreatureType
{
  private final Class<? extends IAnimal> e;
  private final int f;
  private final Material g;
  private final boolean h;
  private final boolean i;
  
  private EnumCreatureType(Class<? extends IAnimal> ☃, int ☃, Material ☃, boolean ☃, boolean ☃)
  {
    this.e = ☃;
    this.f = ☃;
    this.g = ☃;
    this.h = ☃;
    this.i = ☃;
  }
  
  public Class<? extends IAnimal> a()
  {
    return this.e;
  }
  
  public int b()
  {
    return this.f;
  }
  
  public boolean d()
  {
    return this.h;
  }
  
  public boolean e()
  {
    return this.i;
  }
}
