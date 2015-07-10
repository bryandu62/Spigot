package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenTaiga1
  extends WorldGenTreeAbstract
{
  private static final IBlockData a = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.SPRUCE);
  private static final IBlockData b = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.SPRUCE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  
  public WorldGenTaiga1()
  {
    super(false);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = ☃.nextInt(5) + 7;
    int ☃ = ☃ - ☃.nextInt(2) - 3;
    int ☃ = ☃ - ☃;
    int ☃ = 1 + ☃.nextInt(☃ + 1);
    
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
            if (!a(☃.getType(☃.c(☃, ☃, ☃)).getBlock())) {
              ☃ = false;
            }
          }
          else {
            ☃ = false;
          }
        }
      }
    }
    if (!☃) {
      return false;
    }
    Block ☃ = ☃.getType(☃.down()).getBlock();
    if (((☃ != Blocks.GRASS) && (☃ != Blocks.DIRT)) || (☃.getY() >= 256 - ☃ - 1)) {
      return false;
    }
    a(☃, ☃.down());
    
    int ☃ = 0;
    for (int ☃ = ☃.getY() + ☃; ☃ >= ☃.getY() + ☃; ☃--)
    {
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
      if ((☃ >= 1) && (☃ == ☃.getY() + ☃ + 1)) {
        ☃--;
      } else if (☃ < ☃) {
        ☃++;
      }
    }
    for (int ☃ = 0; ☃ < ☃ - 1; ☃++)
    {
      Block ☃ = ☃.getType(☃.up(☃)).getBlock();
      if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
        a(☃, ☃.up(☃), a);
      }
    }
    return true;
  }
}
