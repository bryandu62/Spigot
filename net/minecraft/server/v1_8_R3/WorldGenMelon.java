package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenMelon
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 64; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if ((Blocks.MELON_BLOCK.canPlace(☃, ☃)) && (☃.getType(☃.down()).getBlock() == Blocks.GRASS)) {
        ☃.setTypeAndData(☃, Blocks.MELON_BLOCK.getBlockData(), 2);
      }
    }
    return true;
  }
}
