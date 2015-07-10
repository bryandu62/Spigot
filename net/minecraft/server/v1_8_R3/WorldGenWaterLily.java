package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenWaterLily
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 10; ☃++)
    {
      int ☃ = ☃.getX() + ☃.nextInt(8) - ☃.nextInt(8);
      int ☃ = ☃.getY() + ☃.nextInt(4) - ☃.nextInt(4);
      int ☃ = ☃.getZ() + ☃.nextInt(8) - ☃.nextInt(8);
      if ((☃.isEmpty(new BlockPosition(☃, ☃, ☃))) && 
        (Blocks.WATERLILY.canPlace(☃, new BlockPosition(☃, ☃, ☃)))) {
        ☃.setTypeAndData(new BlockPosition(☃, ☃, ☃), Blocks.WATERLILY.getBlockData(), 2);
      }
    }
    return true;
  }
}
