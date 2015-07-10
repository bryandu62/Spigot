package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenTrees
  extends WorldGenTreeAbstract
{
  private static final IBlockData a = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.OAK);
  private static final IBlockData b = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  private final int c;
  private final boolean d;
  private final IBlockData e;
  private final IBlockData f;
  
  public WorldGenTrees(boolean ☃)
  {
    this(☃, 4, a, b, false);
  }
  
  public WorldGenTrees(boolean ☃, int ☃, IBlockData ☃, IBlockData ☃, boolean ☃)
  {
    super(☃);
    this.c = ☃;
    this.e = ☃;
    this.f = ☃;
    this.d = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = ☃.nextInt(3) + this.c;
    
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
    
    int ☃ = 3;
    int ☃ = 0;
    for (int ☃ = ☃.getY() - ☃ + ☃; ☃ <= ☃.getY() + ☃; ☃++)
    {
      int ☃ = ☃ - (☃.getY() + ☃);
      int ☃ = ☃ + 1 - ☃ / 2;
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
            if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES) || (☃.getMaterial() == Material.REPLACEABLE_PLANT)) {
              a(☃, ☃, this.f);
            }
          }
        }
      }
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      Block ☃ = ☃.getType(☃.up(☃)).getBlock();
      if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES) || (☃.getMaterial() == Material.REPLACEABLE_PLANT))
      {
        a(☃, ☃.up(☃), this.e);
        if ((this.d) && (☃ > 0))
        {
          if ((☃.nextInt(3) > 0) && (☃.isEmpty(☃.a(-1, ☃, 0)))) {
            a(☃, ☃.a(-1, ☃, 0), BlockVine.EAST);
          }
          if ((☃.nextInt(3) > 0) && (☃.isEmpty(☃.a(1, ☃, 0)))) {
            a(☃, ☃.a(1, ☃, 0), BlockVine.WEST);
          }
          if ((☃.nextInt(3) > 0) && (☃.isEmpty(☃.a(0, ☃, -1)))) {
            a(☃, ☃.a(0, ☃, -1), BlockVine.SOUTH);
          }
          if ((☃.nextInt(3) > 0) && (☃.isEmpty(☃.a(0, ☃, 1)))) {
            a(☃, ☃.a(0, ☃, 1), BlockVine.NORTH);
          }
        }
      }
    }
    if (this.d)
    {
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
                b(☃, ☃, BlockVine.EAST);
              }
              if ((☃.nextInt(4) == 0) && (☃.getType(☃).getBlock().getMaterial() == Material.AIR)) {
                b(☃, ☃, BlockVine.WEST);
              }
              if ((☃.nextInt(4) == 0) && (☃.getType(☃).getBlock().getMaterial() == Material.AIR)) {
                b(☃, ☃, BlockVine.SOUTH);
              }
              if ((☃.nextInt(4) == 0) && (☃.getType(☃).getBlock().getMaterial() == Material.AIR)) {
                b(☃, ☃, BlockVine.NORTH);
              }
            }
          }
        }
      }
      if ((☃.nextInt(5) == 0) && (☃ > 5)) {
        for (int ☃ = 0; ☃ < 2; ☃++) {
          for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
            if (☃.nextInt(4 - ☃) == 0)
            {
              EnumDirection ☃ = ☃.opposite();
              a(☃, ☃.nextInt(3), ☃.a(☃.getAdjacentX(), ☃ - 5 + ☃, ☃.getAdjacentZ()), ☃);
            }
          }
        }
      }
    }
    return true;
  }
  
  private void a(World ☃, int ☃, BlockPosition ☃, EnumDirection ☃)
  {
    a(☃, ☃, Blocks.COCOA.getBlockData().set(BlockCocoa.AGE, Integer.valueOf(☃)).set(BlockCocoa.FACING, ☃));
  }
  
  private void a(World ☃, BlockPosition ☃, BlockStateBoolean ☃)
  {
    a(☃, ☃, Blocks.VINE.getBlockData().set(☃, Boolean.valueOf(true)));
  }
  
  private void b(World ☃, BlockPosition ☃, BlockStateBoolean ☃)
  {
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
