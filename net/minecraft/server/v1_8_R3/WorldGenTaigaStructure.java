package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenTaigaStructure
  extends WorldGenerator
{
  private final Block a;
  private final int b;
  
  public WorldGenTaigaStructure(Block ☃, int ☃)
  {
    super(false);
    this.a = ☃;
    this.b = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    while (☃.getY() > 3)
    {
      if (!☃.isEmpty(☃.down()))
      {
        Block ☃ = ☃.getType(☃.down()).getBlock();
        if ((☃ == Blocks.GRASS) || (☃ == Blocks.DIRT) || (☃ == Blocks.STONE)) {
          break;
        }
      }
      ☃ = ☃.down();
    }
    if (☃.getY() <= 3) {
      return false;
    }
    int ☃ = this.b;
    int ☃ = 0;
    while ((☃ >= 0) && (☃ < 3))
    {
      int ☃ = ☃ + ☃.nextInt(2);
      int ☃ = ☃ + ☃.nextInt(2);
      int ☃ = ☃ + ☃.nextInt(2);
      float ☃ = (☃ + ☃ + ☃) * 0.333F + 0.5F;
      for (BlockPosition ☃ : BlockPosition.a(☃.a(-☃, -☃, -☃), ☃.a(☃, ☃, ☃))) {
        if (☃.i(☃) <= ☃ * ☃) {
          ☃.setTypeAndData(☃, this.a.getBlockData(), 4);
        }
      }
      ☃ = ☃.a(-(☃ + 1) + ☃.nextInt(2 + ☃ * 2), 0 - ☃.nextInt(2), -(☃ + 1) + ☃.nextInt(2 + ☃ * 2));
      ☃++;
    }
    return true;
  }
}
