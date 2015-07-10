package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenForest
  extends WorldGenTreeAbstract
{
  private static final IBlockData a = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.BIRCH);
  private static final IBlockData b = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.BIRCH).set(BlockLeaves1.CHECK_DECAY, Boolean.valueOf(false));
  private boolean c;
  
  public WorldGenForest(boolean ☃, boolean ☃)
  {
    super(☃);
    this.c = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = ☃.nextInt(3) + 5;
    if (this.c) {
      ☃ += ☃.nextInt(7);
    }
    boolean ☃ = true;
    if ((☃.getY() < 1) || (☃.getY() + ☃ + 1 > 256)) {
      return false;
    }
    for (int ☃ = ☃.getY(); ☃ <= ☃.getY() + 1 + ☃; ☃++)
    {
      int ☃ = 1;
      if (☃ == ☃.getY()) {
        ☃ = 0;
      }
      if (☃ >= ☃.getY() + 1 + ☃ - 2) {
        ☃ = 2;
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
    if (((☃ != Blocks.GRASS) && (☃ != Blocks.DIRT) && (☃ != Blocks.FARMLAND)) || (☃.getY() >= 256 - ☃ - 1)) {
      return false;
    }
    a(☃, ☃.down());
    for (int ☃ = ☃.getY() - 3 + ☃; ☃ <= ☃.getY() + ☃; ☃++)
    {
      int ☃ = ☃ - (☃.getY() + ☃);
      int ☃ = 1 - ☃ / 2;
      for (int ☃ = ☃.getX() - ☃; ☃ <= ☃.getX() + ☃; ☃++)
      {
        int ☃ = ☃ - ☃.getX();
        for (int ☃ = ☃.getZ() - ☃; ☃ <= ☃.getZ() + ☃; ☃++)
        {
          int ☃ = ☃ - ☃.getZ();
          if ((Math.abs(☃) != ☃) || (Math.abs(☃) != ☃) || ((☃.nextInt(2) != 0) && (☃ != 0)))
          {
            BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
            Block ☃ = ☃.getType(☃).getBlock();
            if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
              a(☃, ☃, b);
            }
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      Block ☃ = ☃.getType(☃.up(☃)).getBlock();
      if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
        a(☃, ☃.up(☃), a);
      }
    }
    return true;
  }
}
