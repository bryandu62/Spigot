package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenSwampTree
  extends WorldGenTreeAbstract
{
  private static final IBlockData a = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.OAK);
  private static final IBlockData b = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves1.CHECK_DECAY, Boolean.valueOf(false));
  
  public WorldGenSwampTree()
  {
    super(false);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = ☃.nextInt(4) + 5;
    while (☃.getType(☃.down()).getBlock().getMaterial() == Material.WATER) {
      ☃ = ☃.down();
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
        ☃ = 3;
      }
      BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
      for (int ☃ = ☃.getX() - ☃; (☃ <= ☃.getX() + ☃) && (☃); ☃++) {
        for (int ☃ = ☃.getZ() - ☃; (☃ <= ☃.getZ() + ☃) && (☃); ☃++) {
          if ((☃ >= 0) && (☃ < 256))
          {
            Block ☃ = ☃.getType(☃.c(☃, ☃, ☃)).getBlock();
            if ((☃.getMaterial() != Material.AIR) && (☃.getMaterial() != Material.LEAVES)) {
              if ((☃ == Blocks.WATER) || (☃ == Blocks.FLOWING_WATER))
              {
                if (☃ > ☃.getY()) {
                  ☃ = false;
                }
              }
              else {
                ☃ = false;
              }
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
    if (((☃ != Blocks.GRASS) && (☃ != Blocks.DIRT)) || (☃.getY() >= 256 - ☃ - 1)) {
      return false;
    }
    a(☃, ☃.down());
    for (int ☃ = ☃.getY() - 3 + ☃; ☃ <= ☃.getY() + ☃; ☃++)
    {
      int ☃ = ☃ - (☃.getY() + ☃);
      int ☃ = 2 - ☃ / 2;
      for (int ☃ = ☃.getX() - ☃; ☃ <= ☃.getX() + ☃; ☃++)
      {
        int ☃ = ☃ - ☃.getX();
        for (int ☃ = ☃.getZ() - ☃; ☃ <= ☃.getZ() + ☃; ☃++)
        {
          int ☃ = ☃ - ☃.getZ();
          if ((Math.abs(☃) != ☃) || (Math.abs(☃) != ☃) || ((☃.nextInt(2) != 0) && (☃ != 0)))
          {
            BlockPosition ☃ = new BlockPosition(☃, ☃, ☃);
            if (!☃.getType(☃).getBlock().o()) {
              a(☃, ☃, b);
            }
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      Block ☃ = ☃.getType(☃.up(☃)).getBlock();
      if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES) || (☃ == Blocks.FLOWING_WATER) || (☃ == Blocks.WATER)) {
        a(☃, ☃.up(☃), a);
      }
    }
    for (int ☃ = ☃.getY() - 3 + ☃; ☃ <= ☃.getY() + ☃; ☃++)
    {
      int ☃ = ☃ - (☃.getY() + ☃);
      int ☃ = 2 - ☃ / 2;
      BlockPosition.MutableBlockPosition ☃ = new BlockPosition.MutableBlockPosition();
      for (int ☃ = ☃.getX() - ☃; ☃ <= ☃.getX() + ☃; ☃++) {
        for (int ☃ = ☃.getZ() - ☃; ☃ <= ☃.getZ() + ☃; ☃++)
        {
          ☃.c(☃, ☃, ☃);
          if (☃.getType(☃).getBlock().getMaterial() == Material.LEAVES)
          {
            BlockPosition ☃ = ☃.west();
            BlockPosition ☃ = ☃.east();
            BlockPosition ☃ = ☃.north();
            BlockPosition ☃ = ☃.south();
            if ((☃.nextInt(4) == 0) && (☃.getType(☃).getBlock().getMaterial() == Material.AIR)) {
              a(☃, ☃, BlockVine.EAST);
            }
            if ((☃.nextInt(4) == 0) && (☃.getType(☃).getBlock().getMaterial() == Material.AIR)) {
              a(☃, ☃, BlockVine.WEST);
            }
            if ((☃.nextInt(4) == 0) && (☃.getType(☃).getBlock().getMaterial() == Material.AIR)) {
              a(☃, ☃, BlockVine.SOUTH);
            }
            if ((☃.nextInt(4) == 0) && (☃.getType(☃).getBlock().getMaterial() == Material.AIR)) {
              a(☃, ☃, BlockVine.NORTH);
            }
          }
        }
      }
    }
    return true;
  }
  
  private void a(World ☃, BlockPosition ☃, BlockStateBoolean ☃)
  {
    IBlockData ☃ = Blocks.VINE.getBlockData().set(☃, Boolean.valueOf(true));
    a(☃, ☃, ☃);
    int ☃ = 4;
    
    ☃ = ☃.down();
    while ((☃.getType(☃).getBlock().getMaterial() == Material.AIR) && (☃ > 0))
    {
      a(☃, ☃, ☃);
      ☃ = ☃.down();
      ☃--;
    }
  }
}
