package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenFire
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 64; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if (☃.isEmpty(☃)) {
        if (☃.getType(☃.down()).getBlock() == Blocks.NETHERRACK) {
          ☃.setTypeAndData(☃, Blocks.FIRE.getBlockData(), 2);
        }
      }
    }
    return true;
  }
}
