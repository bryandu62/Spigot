package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenTaiga2
  extends WorldGenTreeAbstract
{
  private static final IBlockData a = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.SPRUCE);
  private static final IBlockData b = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.SPRUCE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  
  public WorldGenTaiga2(boolean ☃)
  {
    super(☃);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = ☃.nextInt(4) + 6;
    int ☃ = 1 + ☃.nextInt(2);
    int ☃ = ☃ - ☃;
    int ☃ = 2 + ☃.nextInt(2);
    
    boolean ☃ = true;
    if ((☃.getY() < 1) || (☃.getY() + ☃ + 1 > 256)) {
      return false;
    }
    for (int ☃ = ☃.getY(); (☃ <= ☃.getY() + 1 + ☃) && (☃); ☃++)
    {
      int ☃ = 1;
      if (☃ - ☃.getY() < ☃) {
        ☃ = 0;
      } else {
        ☃ = ☃;
      }
      BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
      for (int ☃ = ☃.getX() - ☃; (☃ <= ☃.getX() + ☃) && (☃); ☃++) {
        for (int ☃ = ☃.getZ() - ☃; (☃ <= ☃.getZ() + ☃) && (☃); ☃++) {
          if ((☃ >= 0) && (☃ < 256))
          {
            Block ☃ = ☃.getType(☃.c(☃, ☃, ☃)).getBlock();
            if ((☃.getMaterial() != Material.AIR) && (☃.getMaterial() != Material.LEAVES)) {
              ☃ = false;
            }
          }
          else
          {
            ☃ = false;
          }
        }
      }
    }
    if (!☃) {
      return false;
    }
    Block ☃ = ☃.getType(☃.down()).getBlock();
    if (((☃ != Blocks.GRASS) && (☃ != Blocks.DIRT) && (☃ != Blocks.FARMLAND)) || (☃.getY() >= 256 - ☃ - 1)) {
      return false;
    }
    a(☃, ☃.down());
    
    int ☃ = ☃.nextInt(2);
    int ☃ = 1;
    int ☃ = 0;
    for (int ☃ = 0; ☃ <= ☃; ☃++)
    {
      int ☃ = ☃.getY() + ☃ - ☃;
      for (int ☃ = ☃.getX() - ☃; ☃ <= ☃.getX() + ☃; ☃++)
      {
        int ☃ = ☃ - ☃.getX();
        for (int ☃ = ☃.getZ() - ☃; ☃ <= ☃.getZ() + ☃; ☃++)
        {
          int ☃ = ☃ - ☃.getZ();
          if ((Math.abs(☃) != ☃) || (Math.abs(☃) != ☃) || (☃ <= 0))
          {
            BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
            if (!☃.getType(☃).getBlock().o()) {
              a(☃, ☃, b);
            }
          }
        }
      }
      if (☃ >= ☃)
      {
        ☃ = ☃;
        ☃ = 1;
        ☃++;
        if (☃ > ☃) {
          ☃ = ☃;
        }
      }
      else
      {
        ☃++;
      }
    }
    int ☃ = ☃.nextInt(3);
    for (int ☃ = 0; ☃ < ☃ - ☃; ☃++)
    {
      Block ☃ = ☃.getType(☃.up(☃)).getBlock();
      if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
        a(☃, ☃.up(☃), a);
      }
    }
    return true;
  }
}
