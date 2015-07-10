package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenCactus
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 10; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if (☃.isEmpty(☃))
      {
        int ☃ = 1 + ☃.nextInt(☃.nextInt(3) + 1);
        for (int ☃ = 0; ☃ < ☃; ☃++) {
          if (Blocks.CACTUS.e(☃, ☃)) {
            ☃.setTypeAndData(☃.up(☃), Blocks.CACTUS.getBlockData(), 2);
          }
        }
      }
    }
    return true;
  }
}
