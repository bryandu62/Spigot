package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenDeadBush
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    Block ☃;
    while ((((☃ = ☃.getType(☃).getBlock()).getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) && (☃.getY() > 0)) {
      ☃ = ☃.down();
    }
    for (int ☃ = 0; ☃ < 4; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if ((☃.isEmpty(☃)) && 
        (Blocks.DEADBUSH.f(☃, ☃, Blocks.DEADBUSH.getBlockData()))) {
        ☃.setTypeAndData(☃, Blocks.DEADBUSH.getBlockData(), 2);
      }
    }
    return true;
  }
}
