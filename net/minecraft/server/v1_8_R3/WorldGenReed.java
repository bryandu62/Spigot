package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenReed
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 20; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(4) - ☃.nextInt(4), 0, ☃.nextInt(4) - ☃.nextInt(4));
      if (☃.isEmpty(☃))
      {
        BlockPosition ☃ = ☃.down();
        if ((☃.getType(☃.west()).getBlock().getMaterial() == Material.WATER) || (☃.getType(☃.east()).getBlock().getMaterial() == Material.WATER) || (☃.getType(☃.north()).getBlock().getMaterial() == Material.WATER) || (☃.getType(☃.south()).getBlock().getMaterial() == Material.WATER))
        {
          int ☃ = 2 + ☃.nextInt(☃.nextInt(3) + 1);
          for (int ☃ = 0; ☃ < ☃; ☃++) {
            if (Blocks.REEDS.e(☃, ☃)) {
              ☃.setTypeAndData(☃.up(☃), Blocks.REEDS.getBlockData(), 2);
            }
          }
        }
      }
    }
    return true;
  }
}
