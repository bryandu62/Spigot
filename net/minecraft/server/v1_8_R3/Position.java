package net.minecraft.server.v1_8_R3;

public class Position
  implements IPosition
{
  protected final double a;
  protected final double b;
  protected final double c;
  
  public Position(double ☃, double ☃, double ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public double getX()
  {
    return this.a;
  }
  
  public double getY()
  {
    return this.b;
  }
  
  public double getZ()
  {
    return this.c;
  }
}
