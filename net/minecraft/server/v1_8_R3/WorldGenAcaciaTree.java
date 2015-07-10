package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenAcaciaTree
  extends WorldGenTreeAbstract
{
  private static final IBlockData a = Blocks.LOG2.getBlockData().set(BlockLog2.VARIANT, BlockWood.EnumLogVariant.ACACIA);
  private static final IBlockData b = Blocks.LEAVES2.getBlockData().set(BlockLeaves2.VARIANT, BlockWood.EnumLogVariant.ACACIA).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  
  public WorldGenAcaciaTree(boolean ☃)
  {
    super(☃);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = ☃.nextInt(3) + ☃.nextInt(3) + 5;
    
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
    if (((☃ != Blocks.GRASS) && (☃ != Blocks.DIRT)) || (☃.getY() >= 256 - ☃ - 1)) {
      return false;
    }
    a(☃, ☃.down());
    
    EnumDirection ☃ = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(☃);
    int ☃ = ☃ - ☃.nextInt(4) - 1;
    int ☃ = 3 - ☃.nextInt(3);
    
    int ☃ = ☃.getX();int ☃ = ☃.getZ();
    int ☃ = 0;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = ☃.getY() + ☃;
      if ((☃ >= ☃) && (☃ > 0))
      {
        ☃ += ☃.getAdjacentX();
        ☃ += ☃.getAdjacentZ();
        ☃--;
      }
      BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
      Material ☃ = ☃.getType(☃).getBlock().getMaterial();
      if ((☃ == Material.AIR) || (☃ == Material.LEAVES))
      {
        b(☃, ☃);
        ☃ = ☃;
      }
    }
    BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
    for (int ☃ = -3; ☃ <= 3; ☃++) {
      for (int ☃ = -3; ☃ <= 3; ☃++) {
        if ((Math.abs(☃) != 3) || (Math.abs(☃) != 3)) {
          c(☃, ☃.a(☃, 0, ☃));
        }
      }
    }
    ☃ = ☃.up();
    for (int ☃ = -1; ☃ <= 1; ☃++) {
      for (int ☃ = -1; ☃ <= 1; ☃++) {
        c(☃, ☃.a(☃, 0, ☃));
      }
    }
    c(☃, ☃.east(2));
    c(☃, ☃.west(2));
    c(☃, ☃.south(2));
    c(☃, ☃.north(2));
    
    ☃ = ☃.getX();
    ☃ = ☃.getZ();
    EnumDirection ☃ = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(☃);
    if (☃ != ☃)
    {
      int ☃ = ☃ - ☃.nextInt(2) - 1;
      int ☃ = 1 + ☃.nextInt(3);
      
      ☃ = 0;
      for (int ☃ = ☃; (☃ < ☃) && (☃ > 0); ☃--)
      {
        if (☃ >= 1)
        {
          int ☃ = ☃.getY() + ☃;
          ☃ += ☃.getAdjacentX();
          ☃ += ☃.getAdjacentZ();
          BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
          Material ☃ = ☃.getType(☃).getBlock().getMaterial();
          if ((☃ == Material.AIR) || (☃ == Material.LEAVES))
          {
            b(☃, ☃);
            ☃ = ☃;
          }
        }
        ☃++;
      }
      if (☃ > 0)
      {
        BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
        for (int ☃ = -2; ☃ <= 2; ☃++) {
          for (int ☃ = -2; ☃ <= 2; ☃++) {
            if ((Math.abs(☃) != 2) || (Math.abs(☃) != 2)) {
              c(☃, ☃.a(☃, 0, ☃));
            }
          }
        }
        ☃ = ☃.up();
        for (int ☃ = -1; ☃ <= 1; ☃++) {
          for (int ☃ = -1; ☃ <= 1; ☃++) {
            c(☃, ☃.a(☃, 0, ☃));
          }
        }
      }
    }
    return true;
  }
  
  private void b(World ☃, BlockPosition ☃)
  {
    a(☃, ☃, a);
  }
  
  private void c(World ☃, BlockPosition ☃)
  {
    Material ☃ = ☃.getType(☃).getBlock().getMaterial();
    if ((☃ == Material.AIR) || (☃ == Material.LEAVES)) {
      a(☃, ☃, b);
    }
  }
}
