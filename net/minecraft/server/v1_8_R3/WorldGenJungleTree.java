package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenJungleTree
  extends WorldGenMegaTreeAbstract
{
  public WorldGenJungleTree(boolean ☃, int ☃, int ☃, IBlockData ☃, IBlockData ☃)
  {
    super(☃, ☃, ☃, ☃, ☃);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = a(☃);
    if (!a(☃, ☃, ☃, ☃)) {
      return false;
    }
    c(☃, ☃.up(☃), 2);
    
    int ☃ = ☃.getY() + ☃ - 2 - ☃.nextInt(4);
    while (☃ > ☃.getY() + ☃ / 2)
    {
      float ☃ = ☃.nextFloat() * 3.1415927F * 2.0F;
      int ☃ = ☃.getX() + (int)(0.5F + MathHelper.cos(☃) * 4.0F);
      int ☃ = ☃.getZ() + (int)(0.5F + MathHelper.sin(☃) * 4.0F);
      for (int ☃ = 0; ☃ < 5; ☃++)
      {
        ☃ = ☃.getX() + (int)(1.5F + MathHelper.cos(☃) * ☃);
        ☃ = ☃.getZ() + (int)(1.5F + MathHelper.sin(☃) * ☃);
        a(☃, new BlockPosition(☃, ☃ - 3 + ☃ / 2, ☃), this.b);
      }
      int ☃ = 1 + ☃.nextInt(2);
      int ☃ = ☃;
      for (int ☃ = ☃ - ☃; ☃ <= ☃; ☃++)
      {
        int ☃ = ☃ - ☃;
        b(☃, new BlockPosition(☃, ☃, ☃), 1 - ☃);
      }
      ☃ -= 2 + ☃.nextInt(4);
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      BlockPosition ☃ = ☃.up(☃);
      if (a(☃.getType(☃).getBlock()))
      {
        a(☃, ☃, this.b);
        if (☃ > 0)
        {
          a(☃, ☃, ☃.west(), BlockVine.EAST);
          a(☃, ☃, ☃.north(), BlockVine.SOUTH);
        }
      }
      if (☃ < ☃ - 1)
      {
        BlockPosition ☃ = ☃.east();
        if (a(☃.getType(☃).getBlock()))
        {
          a(☃, ☃, this.b);
          if (☃ > 0)
          {
            a(☃, ☃, ☃.east(), BlockVine.WEST);
            a(☃, ☃, ☃.north(), BlockVine.SOUTH);
          }
        }
        BlockPosition ☃ = ☃.south().east();
        if (a(☃.getType(☃).getBlock()))
        {
          a(☃, ☃, this.b);
          if (☃ > 0)
          {
            a(☃, ☃, ☃.east(), BlockVine.WEST);
            a(☃, ☃, ☃.south(), BlockVine.NORTH);
          }
        }
        BlockPosition ☃ = ☃.south();
        if (a(☃.getType(☃).getBlock()))
        {
          a(☃, ☃, this.b);
          if (☃ > 0)
          {
            a(☃, ☃, ☃.west(), BlockVine.EAST);
            a(☃, ☃, ☃.south(), BlockVine.NORTH);
          }
        }
      }
    }
    return true;
  }
  
  private void a(World ☃, Random ☃, BlockPosition ☃, BlockStateBoolean ☃)
  {
    if ((☃.nextInt(3) > 0) && (☃.isEmpty(☃))) {
      a(☃, ☃, Blocks.VINE.getBlockData().set(☃, Boolean.valueOf(true)));
    }
  }
  
  private void c(World ☃, BlockPosition ☃, int ☃)
  {
    int ☃ = 2;
    for (int ☃ = -☃; ☃ <= 0; ☃++) {
      a(☃, ☃.up(☃), ☃ + 1 - ☃);
    }
  }
}
