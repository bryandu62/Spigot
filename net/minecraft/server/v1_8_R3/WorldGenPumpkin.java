package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenPumpkin
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 64; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if ((☃.isEmpty(☃)) && (☃.getType(☃.down()).getBlock() == Blocks.GRASS) && 
        (Blocks.PUMPKIN.canPlace(☃, ☃))) {
        ☃.setTypeAndData(☃, Blocks.PUMPKIN.getBlockData().set(BlockPumpkin.FACING, EnumDirection.EnumDirectionLimit.HORIZONTAL.a(☃)), 2);
      }
    }
    return true;
  }
}
