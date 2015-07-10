package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenLakes
  extends WorldGenerator
{
  private Block a;
  
  public WorldGenLakes(Block ☃)
  {
    this.a = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    ☃ = ☃.a(-8, 0, -8);
    while ((☃.getY() > 5) && (☃.isEmpty(☃))) {
      ☃ = ☃.down();
    }
    if (☃.getY() <= 4) {
      return false;
    }
    ☃ = ☃.down(4);
    
    boolean[] ☃ = new boolean['ࠀ'];
    
    int ☃ = ☃.nextInt(4) + 4;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      double ☃ = ☃.nextDouble() * 6.0D + 3.0D;
      double ☃ = ☃.nextDouble() * 4.0D + 2.0D;
      double ☃ = ☃.nextDouble() * 6.0D + 3.0D;
      
      double ☃ = ☃.nextDouble() * (16.0D - ☃ - 2.0D) + 1.0D + ☃ / 2.0D;
      double ☃ = ☃.nextDouble() * (8.0D - ☃ - 4.0D) + 2.0D + ☃ / 2.0D;
      double ☃ = ☃.nextDouble() * (16.0D - ☃ - 2.0D) + 1.0D + ☃ / 2.0D;
      for (int ☃ = 1; ☃ < 15; ☃++) {
        for (int ☃ = 1; ☃ < 15; ☃++) {
          for (int ☃ = 1; ☃ < 7; ☃++)
          {
            double ☃ = (☃ - ☃) / (☃ / 2.0D);
            double ☃ = (☃ - ☃) / (☃ / 2.0D);
            double ☃ = (☃ - ☃) / (☃ / 2.0D);
            double ☃ = ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
            if (☃ < 1.0D) {
              ☃[((☃ * 16 + ☃) * 8 + ☃)] = true;
            }
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++) {
        for (int ☃ = 0; ☃ < 8; ☃++)
        {
          boolean ☃ = (☃[((☃ * 16 + ☃) * 8 + ☃)] == 0) && (((☃ < 15) && (☃[(((☃ + 1) * 16 + ☃) * 8 + ☃)] != 0)) || ((☃ > 0) && (☃[(((☃ - 1) * 16 + ☃) * 8 + ☃)] != 0)) || ((☃ < 15) && (☃[((☃ * 16 + (☃ + 1)) * 8 + ☃)] != 0)) || ((☃ > 0) && (☃[((☃ * 16 + (☃ - 1)) * 8 + ☃)] != 0)) || ((☃ < 7) && (☃[((☃ * 16 + ☃) * 8 + (☃ + 1))] != 0)) || ((☃ > 0) && (☃[((☃ * 16 + ☃) * 8 + (☃ - 1))] != 0)));
          if (☃)
          {
            Material ☃ = ☃.getType(☃.a(☃, ☃, ☃)).getBlock().getMaterial();
            if ((☃ >= 4) && (☃.isLiquid())) {
              return false;
            }
            if ((☃ < 4) && (!☃.isBuildable()) && (☃.getType(☃.a(☃, ☃, ☃)).getBlock() != this.a)) {
              return false;
            }
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++) {
        for (int ☃ = 0; ☃ < 8; ☃++) {
          if (☃[((☃ * 16 + ☃) * 8 + ☃)] != 0) {
            ☃.setTypeAndData(☃.a(☃, ☃, ☃), ☃ >= 4 ? Blocks.AIR.getBlockData() : this.a.getBlockData(), 2);
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++) {
        for (int ☃ = 4; ☃ < 8; ☃++) {
          if (☃[((☃ * 16 + ☃) * 8 + ☃)] != 0)
          {
            BlockPosition ☃ = ☃.a(☃, ☃ - 1, ☃);
            if ((☃.getType(☃).getBlock() == Blocks.DIRT) && (☃.b(EnumSkyBlock.SKY, ☃.a(☃, ☃, ☃)) > 0))
            {
              BiomeBase ☃ = ☃.getBiome(☃);
              if (☃.ak.getBlock() == Blocks.MYCELIUM) {
                ☃.setTypeAndData(☃, Blocks.MYCELIUM.getBlockData(), 2);
              } else {
                ☃.setTypeAndData(☃, Blocks.GRASS.getBlockData(), 2);
              }
            }
          }
        }
      }
    }
    if (this.a.getMaterial() == Material.LAVA) {
      for (int ☃ = 0; ☃ < 16; ☃++) {
        for (int ☃ = 0; ☃ < 16; ☃++) {
          for (int ☃ = 0; ☃ < 8; ☃++)
          {
            boolean ☃ = (☃[((☃ * 16 + ☃) * 8 + ☃)] == 0) && (((☃ < 15) && (☃[(((☃ + 1) * 16 + ☃) * 8 + ☃)] != 0)) || ((☃ > 0) && (☃[(((☃ - 1) * 16 + ☃) * 8 + ☃)] != 0)) || ((☃ < 15) && (☃[((☃ * 16 + (☃ + 1)) * 8 + ☃)] != 0)) || ((☃ > 0) && (☃[((☃ * 16 + (☃ - 1)) * 8 + ☃)] != 0)) || ((☃ < 7) && (☃[((☃ * 16 + ☃) * 8 + (☃ + 1))] != 0)) || ((☃ > 0) && (☃[((☃ * 16 + ☃) * 8 + (☃ - 1))] != 0)));
            if ((☃) && 
              ((☃ < 4) || (☃.nextInt(2) != 0)) && (☃.getType(☃.a(☃, ☃, ☃)).getBlock().getMaterial().isBuildable())) {
              ☃.setTypeAndData(☃.a(☃, ☃, ☃), Blocks.STONE.getBlockData(), 2);
            }
          }
        }
      }
    }
    if (this.a.getMaterial() == Material.WATER) {
      for (int ☃ = 0; ☃ < 16; ☃++) {
        for (int ☃ = 0; ☃ < 16; ☃++)
        {
          int ☃ = 4;
          if (☃.v(☃.a(☃, ☃, ☃))) {
            ☃.setTypeAndData(☃.a(☃, ☃, ☃), Blocks.ICE.getBlockData(), 2);
          }
        }
      }
    }
    return true;
  }
}
