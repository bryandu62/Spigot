package net.minecraft.server.v1_8_R3;

import com.google.common.base.Objects;
import java.util.Random;

public class WorldGenCaves
  extends WorldGenBase
{
  protected void a(long ☃, int ☃, int ☃, ChunkSnapshot ☃, double ☃, double ☃, double ☃)
  {
    a(☃, ☃, ☃, ☃, ☃, ☃, ☃, 1.0F + this.b.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
  }
  
  protected void a(long ☃, int ☃, int ☃, ChunkSnapshot ☃, double ☃, double ☃, double ☃, float ☃, float ☃, float ☃, int ☃, int ☃, double ☃)
  {
    double ☃ = ☃ * 16 + 8;
    double ☃ = ☃ * 16 + 8;
    
    float ☃ = 0.0F;
    float ☃ = 0.0F;
    Random ☃ = new Random(☃);
    if (☃ <= 0)
    {
      int ☃ = this.a * 16 - 16;
      ☃ = ☃ - ☃.nextInt(☃ / 4);
    }
    boolean ☃ = false;
    if (☃ == -1)
    {
      ☃ = ☃ / 2;
      ☃ = true;
    }
    int ☃ = ☃.nextInt(☃ / 2) + ☃ / 4;
    boolean ☃ = ☃.nextInt(6) == 0;
    for (; ☃ < ☃; ☃++)
    {
      double ☃ = 1.5D + MathHelper.sin(☃ * 3.1415927F / ☃) * ☃ * 1.0F;
      double ☃ = ☃ * ☃;
      
      float ☃ = MathHelper.cos(☃);
      float ☃ = MathHelper.sin(☃);
      ☃ += MathHelper.cos(☃) * ☃;
      ☃ += ☃;
      ☃ += MathHelper.sin(☃) * ☃;
      if (☃) {
        ☃ *= 0.92F;
      } else {
        ☃ *= 0.7F;
      }
      ☃ += ☃ * 0.1F;
      ☃ += ☃ * 0.1F;
      
      ☃ *= 0.9F;
      ☃ *= 0.75F;
      ☃ += (☃.nextFloat() - ☃.nextFloat()) * ☃.nextFloat() * 2.0F;
      ☃ += (☃.nextFloat() - ☃.nextFloat()) * ☃.nextFloat() * 4.0F;
      if ((!☃) && (☃ == ☃) && (☃ > 1.0F) && (☃ > 0))
      {
        a(☃.nextLong(), ☃, ☃, ☃, ☃, ☃, ☃, ☃.nextFloat() * 0.5F + 0.5F, ☃ - 1.5707964F, ☃ / 3.0F, ☃, ☃, 1.0D);
        a(☃.nextLong(), ☃, ☃, ☃, ☃, ☃, ☃, ☃.nextFloat() * 0.5F + 0.5F, ☃ + 1.5707964F, ☃ / 3.0F, ☃, ☃, 1.0D);
        return;
      }
      if ((☃) || (☃.nextInt(4) != 0))
      {
        double ☃ = ☃ - ☃;
        double ☃ = ☃ - ☃;
        double ☃ = ☃ - ☃;
        double ☃ = ☃ + 2.0F + 16.0F;
        if (☃ * ☃ + ☃ * ☃ - ☃ * ☃ > ☃ * ☃) {
          return;
        }
        if ((☃ >= ☃ - 16.0D - ☃ * 2.0D) && (☃ >= ☃ - 16.0D - ☃ * 2.0D) && (☃ <= ☃ + 16.0D + ☃ * 2.0D) && (☃ <= ☃ + 16.0D + ☃ * 2.0D))
        {
          int ☃ = MathHelper.floor(☃ - ☃) - ☃ * 16 - 1;
          int ☃ = MathHelper.floor(☃ + ☃) - ☃ * 16 + 1;
          
          int ☃ = MathHelper.floor(☃ - ☃) - 1;
          int ☃ = MathHelper.floor(☃ + ☃) + 1;
          
          int ☃ = MathHelper.floor(☃ - ☃) - ☃ * 16 - 1;
          int ☃ = MathHelper.floor(☃ + ☃) - ☃ * 16 + 1;
          if (☃ < 0) {
            ☃ = 0;
          }
          if (☃ > 16) {
            ☃ = 16;
          }
          if (☃ < 1) {
            ☃ = 1;
          }
          if (☃ > 248) {
            ☃ = 248;
          }
          if (☃ < 0) {
            ☃ = 0;
          }
          if (☃ > 16) {
            ☃ = 16;
          }
          boolean ☃ = false;
          for (int ☃ = ☃; (!☃) && (☃ < ☃); ☃++) {
            for (int ☃ = ☃; (!☃) && (☃ < ☃); ☃++) {
              for (int ☃ = ☃ + 1; (!☃) && (☃ >= ☃ - 1); ☃--) {
                if ((☃ >= 0) && (☃ < 256))
                {
                  IBlockData ☃ = ☃.a(☃, ☃, ☃);
                  if ((☃.getBlock() == Blocks.FLOWING_WATER) || (☃.getBlock() == Blocks.WATER)) {
                    ☃ = true;
                  }
                  if ((☃ != ☃ - 1) && (☃ != ☃) && (☃ != ☃ - 1) && (☃ != ☃) && (☃ != ☃ - 1)) {
                    ☃ = ☃;
                  }
                }
              }
            }
          }
          if (!☃)
          {
            BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
            for (int ☃ = ☃; ☃ < ☃; ☃++)
            {
              double ☃ = (☃ + ☃ * 16 + 0.5D - ☃) / ☃;
              for (int ☃ = ☃; ☃ < ☃; ☃++)
              {
                double ☃ = (☃ + ☃ * 16 + 0.5D - ☃) / ☃;
                boolean ☃ = false;
                if (☃ * ☃ + ☃ * ☃ < 1.0D) {
                  for (int ☃ = ☃; ☃ > ☃; ☃--)
                  {
                    double ☃ = (☃ - 1 + 0.5D - ☃) / ☃;
                    if ((☃ > -0.7D) && (☃ * ☃ + ☃ * ☃ + ☃ * ☃ < 1.0D))
                    {
                      IBlockData ☃ = ☃.a(☃, ☃, ☃);
                      IBlockData ☃ = (IBlockData)Objects.firstNonNull(☃.a(☃, ☃ + 1, ☃), Blocks.AIR.getBlockData());
                      if ((☃.getBlock() == Blocks.GRASS) || (☃.getBlock() == Blocks.MYCELIUM)) {
                        ☃ = true;
                      }
                      if (a(☃, ☃)) {
                        if (☃ - 1 < 10)
                        {
                          ☃.a(☃, ☃, ☃, Blocks.LAVA.getBlockData());
                        }
                        else
                        {
                          ☃.a(☃, ☃, ☃, Blocks.AIR.getBlockData());
                          if (☃.getBlock() == Blocks.SAND) {
                            ☃.a(☃, ☃ + 1, ☃, ☃.get(BlockSand.VARIANT) == BlockSand.EnumSandVariant.RED_SAND ? Blocks.RED_SANDSTONE.getBlockData() : Blocks.SANDSTONE.getBlockData());
                          }
                          if ((☃) && (☃.a(☃, ☃ - 1, ☃).getBlock() == Blocks.DIRT))
                          {
                            ☃.c(☃ + ☃ * 16, 0, ☃ + ☃ * 16);
                            ☃.a(☃, ☃ - 1, ☃, this.c.getBiome(☃).ak.getBlock().getBlockData());
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            if (☃) {
              break;
            }
          }
        }
      }
    }
  }
  
  protected boolean a(IBlockData ☃, IBlockData ☃)
  {
    if (☃.getBlock() == Blocks.STONE) {
      return true;
    }
    if (☃.getBlock() == Blocks.DIRT) {
      return true;
    }
    if (☃.getBlock() == Blocks.GRASS) {
      return true;
    }
    if (☃.getBlock() == Blocks.HARDENED_CLAY) {
      return true;
    }
    if (☃.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
      return true;
    }
    if (☃.getBlock() == Blocks.SANDSTONE) {
      return true;
    }
    if (☃.getBlock() == Blocks.RED_SANDSTONE) {
      return true;
    }
    if (☃.getBlock() == Blocks.MYCELIUM) {
      return true;
    }
    if (☃.getBlock() == Blocks.SNOW_LAYER) {
      return true;
    }
    if (((☃.getBlock() == Blocks.SAND) || (☃.getBlock() == Blocks.GRAVEL)) && (☃.getBlock().getMaterial() != Material.WATER)) {
      return true;
    }
    return false;
  }
  
  protected void a(World ☃, int ☃, int ☃, int ☃, int ☃, ChunkSnapshot ☃)
  {
    int ☃ = this.b.nextInt(this.b.nextInt(this.b.nextInt(15) + 1) + 1);
    if (this.b.nextInt(7) != 0) {
      ☃ = 0;
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      double ☃ = ☃ * 16 + this.b.nextInt(16);
      double ☃ = this.b.nextInt(this.b.nextInt(120) + 8);
      double ☃ = ☃ * 16 + this.b.nextInt(16);
      
      int ☃ = 1;
      if (this.b.nextInt(4) == 0)
      {
        a(this.b.nextLong(), ☃, ☃, ☃, ☃, ☃, ☃);
        ☃ += this.b.nextInt(4);
      }
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        float ☃ = this.b.nextFloat() * 3.1415927F * 2.0F;
        float ☃ = (this.b.nextFloat() - 0.5F) * 2.0F / 8.0F;
        float ☃ = this.b.nextFloat() * 2.0F + this.b.nextFloat();
        if (this.b.nextInt(10) == 0) {
          ☃ *= (this.b.nextFloat() * this.b.nextFloat() * 3.0F + 1.0F);
        }
        a(this.b.nextLong(), ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, 0, 0, 1.0D);
      }
    }
  }
}
