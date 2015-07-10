package net.minecraft.server.v1_8_R3;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public class BaseBlockPosition
  implements Comparable<BaseBlockPosition>
{
  public static final BaseBlockPosition ZERO = new BaseBlockPosition(0, 0, 0);
  private final int a;
  private final int c;
  private final int d;
  
  public BaseBlockPosition(int ☃, int ☃, int ☃)
  {
    this.a = ☃;
    this.c = ☃;
    this.d = ☃;
  }
  
  public BaseBlockPosition(double ☃, double ☃, double ☃)
  {
    this(MathHelper.floor(☃), MathHelper.floor(☃), MathHelper.floor(☃));
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if (!(☃ instanceof BaseBlockPosition)) {
      return false;
    }
    BaseBlockPosition ☃ = (BaseBlockPosition)☃;
    if (getX() != ☃.getX()) {
      return false;
    }
    if (getY() != ☃.getY()) {
      return false;
    }
    if (getZ() != ☃.getZ()) {
      return false;
    }
    return true;
  }
  
  public int hashCode()
  {
    return (getY() + getZ() * 31) * 31 + getX();
  }
  
  public int g(BaseBlockPosition ☃)
  {
    if (getY() == ☃.getY())
    {
      if (getZ() == ☃.getZ()) {
        return getX() - ☃.getX();
      }
      return getZ() - ☃.getZ();
    }
    return getY() - ☃.getY();
  }
  
  public int getX()
  {
    return this.a;
  }
  
  public int getY()
  {
    return this.c;
  }
  
  public int getZ()
  {
    return this.d;
  }
  
  public BaseBlockPosition d(BaseBlockPosition ☃)
  {
    return new BaseBlockPosition(getY() * ☃.getZ() - getZ() * ☃.getY(), getZ() * ☃.getX() - getX() * ☃.getZ(), getX() * ☃.getY() - getY() * ☃.getX());
  }
  
  public double c(double ☃, double ☃, double ☃)
  {
    double ☃ = getX() - ☃;
    double ☃ = getY() - ☃;
    double ☃ = getZ() - ☃;
    return ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
  }
  
  public double d(double ☃, double ☃, double ☃)
  {
    double ☃ = getX() + 0.5D - ☃;
    double ☃ = getY() + 0.5D - ☃;
    double ☃ = getZ() + 0.5D - ☃;
    return ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
  }
  
  public double i(BaseBlockPosition ☃)
  {
    return c(☃.getX(), ☃.getY(), ☃.getZ());
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("x", getX()).add("y", getY()).add("z", getZ()).toString();
  }
}
