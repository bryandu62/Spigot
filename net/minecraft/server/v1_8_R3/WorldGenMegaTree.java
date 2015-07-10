package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenMegaTree
  extends WorldGenMegaTreeAbstract
{
  private static final IBlockData e = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.SPRUCE);
  private static final IBlockData f = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.SPRUCE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  private static final IBlockData g = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.PODZOL);
  private boolean h;
  
  public WorldGenMegaTree(boolean ☃, boolean ☃)
  {
    super(☃, 13, 15, e, f);
    this.h = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    int ☃ = a(☃);
    if (!a(☃, ☃, ☃, ☃)) {
      return false;
    }
    a(☃, ☃.getX(), ☃.getZ(), ☃.getY() + ☃, 0, ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      Block ☃ = ☃.getType(☃.up(☃)).getBlock();
      if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
        a(☃, ☃.up(☃), this.b);
      }
      if (☃ < ☃ - 1)
      {
        ☃ = ☃.getType(☃.a(1, ☃, 0)).getBlock();
        if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
          a(☃, ☃.a(1, ☃, 0), this.b);
        }
        ☃ = ☃.getType(☃.a(1, ☃, 1)).getBlock();
        if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
          a(☃, ☃.a(1, ☃, 1), this.b);
        }
        ☃ = ☃.getType(☃.a(0, ☃, 1)).getBlock();
        if ((☃.getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) {
          a(☃, ☃.a(0, ☃, 1), this.b);
        }
      }
    }
    return true;
  }
  
  private void a(World ☃, int ☃, int ☃, int ☃, int ☃, Random ☃)
  {
    int ☃ = ☃.nextInt(5) + (this.h ? this.a : 3);
    
    int ☃ = 0;
    for (int ☃ = ☃ - ☃; ☃ <= ☃; ☃++)
    {
      int ☃ = ☃ - ☃;
      int ☃ = ☃ + MathHelper.d(☃ / ☃ * 3.5F);
      a(☃, new BlockPosition(☃, ☃, ☃), ☃ + ((☃ > 0) && (☃ == ☃) && ((☃ & 0x1) == 0) ? 1 : 0));
      ☃ = ☃;
    }
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    b(☃, ☃.west().north());
    b(☃, ☃.east(2).north());
    b(☃, ☃.west().south(2));
    b(☃, ☃.east(2).south(2));
    for (int ☃ = 0; ☃ < 5; ☃++)
    {
      int ☃ = ☃.nextInt(64);
      int ☃ = ☃ % 8;
      int ☃ = ☃ / 8;
      if ((☃ == 0) || (☃ == 7) || (☃ == 0) || (☃ == 7)) {
        b(☃, ☃.a(-3 + ☃, 0, -3 + ☃));
      }
    }
  }
  
  private void b(World ☃, BlockPosition ☃)
  {
    for (int ☃ = -2; ☃ <= 2; ☃++) {
      for (int ☃ = -2; ☃ <= 2; ☃++) {
        if ((Math.abs(☃) != 2) || (Math.abs(☃) != 2)) {
          c(☃, ☃.a(☃, 0, ☃));
        }
      }
    }
  }
  
  private void c(World ☃, BlockPosition ☃)
  {
    for (int ☃ = 2; ☃ >= -3; ☃--)
    {
      BlockPosition ☃ = ☃.up(☃);
      Block ☃ = ☃.getType(☃).getBlock();
      if ((☃ == Blocks.GRASS) || (☃ == Blocks.DIRT)) {
        a(☃, ☃, g);
      } else {
        if ((☃.getMaterial() != Material.AIR) && (☃ < 0)) {
          break;
        }
      }
    }
  }
}
