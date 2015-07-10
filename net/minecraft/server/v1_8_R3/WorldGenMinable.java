package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Random;

public class WorldGenMinable
  extends WorldGenerator
{
  private final IBlockData a;
  private final int b;
  private final Predicate<IBlockData> c;
  
  public WorldGenMinable(IBlockData ☃, int ☃)
  {
    this(☃, ☃, BlockPredicate.a(Blocks.STONE));
  }
  
  public WorldGenMinable(IBlockData ☃, int ☃, Predicate<IBlockData> ☃)
  {
    this.a = ☃;
    this.b = ☃;
    this.c = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    float ☃ = ☃.nextFloat() * 3.1415927F;
    
    double ☃ = ☃.getX() + 8 + MathHelper.sin(☃) * this.b / 8.0F;
    double ☃ = ☃.getX() + 8 - MathHelper.sin(☃) * this.b / 8.0F;
    double ☃ = ☃.getZ() + 8 + MathHelper.cos(☃) * this.b / 8.0F;
    double ☃ = ☃.getZ() + 8 - MathHelper.cos(☃) * this.b / 8.0F;
    
    double ☃ = ☃.getY() + ☃.nextInt(3) - 2;
    double ☃ = ☃.getY() + ☃.nextInt(3) - 2;
    for (int ☃ = 0; ☃ < this.b; ☃++)
    {
      float ☃ = ☃ / this.b;
      double ☃ = ☃ + (☃ - ☃) * ☃;
      double ☃ = ☃ + (☃ - ☃) * ☃;
      double ☃ = ☃ + (☃ - ☃) * ☃;
      
      double ☃ = ☃.nextDouble() * this.b / 16.0D;
      double ☃ = (MathHelper.sin(3.1415927F * ☃) + 1.0F) * ☃ + 1.0D;
      double ☃ = (MathHelper.sin(3.1415927F * ☃) + 1.0F) * ☃ + 1.0D;
      
      int ☃ = MathHelper.floor(☃ - ☃ / 2.0D);
      int ☃ = MathHelper.floor(☃ - ☃ / 2.0D);
      int ☃ = MathHelper.floor(☃ - ☃ / 2.0D);
      
      int ☃ = MathHelper.floor(☃ + ☃ / 2.0D);
      int ☃ = MathHelper.floor(☃ + ☃ / 2.0D);
      int ☃ = MathHelper.floor(☃ + ☃ / 2.0D);
      for (int ☃ = ☃; ☃ <= ☃; ☃++)
      {
        double ☃ = (☃ + 0.5D - ☃) / (☃ / 2.0D);
        if (☃ * ☃ < 1.0D) {
          for (int ☃ = ☃; ☃ <= ☃; ☃++)
          {
            double ☃ = (☃ + 0.5D - ☃) / (☃ / 2.0D);
            if (☃ * ☃ + ☃ * ☃ < 1.0D) {
              for (int ☃ = ☃; ☃ <= ☃; ☃++)
              {
                double ☃ = (☃ + 0.5D - ☃) / (☃ / 2.0D);
                if (☃ * ☃ + ☃ * ☃ + ☃ * ☃ < 1.0D)
                {
                  BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
                  if (this.c.apply(☃.getType(☃))) {
                    ☃.setTypeAndData(☃, this.a, 2);
                  }
                }
              }
            }
          }
        }
      }
    }
    return true;
  }
}
