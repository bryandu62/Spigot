package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class RandomPositionGenerator
{
  private static Vec3D a = new Vec3D(0.0D, 0.0D, 0.0D);
  
  public static Vec3D a(EntityCreature ☃, int ☃, int ☃)
  {
    return c(☃, ☃, ☃, null);
  }
  
  public static Vec3D a(EntityCreature ☃, int ☃, int ☃, Vec3D ☃)
  {
    a = ☃.a(☃.locX, ☃.locY, ☃.locZ);
    return c(☃, ☃, ☃, a);
  }
  
  public static Vec3D b(EntityCreature ☃, int ☃, int ☃, Vec3D ☃)
  {
    a = new Vec3D(☃.locX, ☃.locY, ☃.locZ).d(☃);
    return c(☃, ☃, ☃, a);
  }
  
  private static Vec3D c(EntityCreature ☃, int ☃, int ☃, Vec3D ☃)
  {
    Random ☃ = ☃.bc();
    boolean ☃ = false;
    int ☃ = 0;int ☃ = 0;int ☃ = 0;
    float ☃ = -99999.0F;
    boolean ☃;
    boolean ☃;
    if (☃.ck())
    {
      double ☃ = ☃.ch().c(MathHelper.floor(☃.locX), MathHelper.floor(☃.locY), MathHelper.floor(☃.locZ)) + 4.0D;
      double ☃ = ☃.ci() + ☃;
      ☃ = ☃ < ☃ * ☃;
    }
    else
    {
      ☃ = false;
    }
    for (int ☃ = 0; ☃ < 10; ☃++)
    {
      int ☃ = ☃.nextInt(2 * ☃ + 1) - ☃;
      int ☃ = ☃.nextInt(2 * ☃ + 1) - ☃;
      int ☃ = ☃.nextInt(2 * ☃ + 1) - ☃;
      if ((☃ == null) || (☃ * ☃.a + ☃ * ☃.c >= 0.0D))
      {
        if ((☃.ck()) && (☃ > 1))
        {
          BlockPosition ☃ = ☃.ch();
          if (☃.locX > ☃.getX()) {
            ☃ -= ☃.nextInt(☃ / 2);
          } else {
            ☃ += ☃.nextInt(☃ / 2);
          }
          if (☃.locZ > ☃.getZ()) {
            ☃ -= ☃.nextInt(☃ / 2);
          } else {
            ☃ += ☃.nextInt(☃ / 2);
          }
        }
        ☃ += MathHelper.floor(☃.locX);
        ☃ += MathHelper.floor(☃.locY);
        ☃ += MathHelper.floor(☃.locZ);
        
        BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
        if ((!☃) || (☃.e(☃)))
        {
          float ☃ = ☃.a(☃);
          if (☃ > ☃)
          {
            ☃ = ☃;
            ☃ = ☃;
            ☃ = ☃;
            ☃ = ☃;
            ☃ = true;
          }
        }
      }
    }
    if (☃) {
      return new Vec3D(☃, ☃, ☃);
    }
    return null;
  }
}
