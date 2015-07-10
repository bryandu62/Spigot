package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenLightStone1
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    if (!☃.isEmpty(☃)) {
      return false;
    }
    if (☃.getType(☃.up()).getBlock() != Blocks.NETHERRACK) {
      return false;
    }
    ☃.setTypeAndData(☃, Blocks.GLOWSTONE.getBlockData(), 2);
    for (int ☃ = 0; ☃ < 1500; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), -☃.nextInt(12), ☃.nextInt(8) - ☃.nextInt(8));
      if (☃.getType(☃).getBlock().getMaterial() == Material.AIR)
      {
        int ☃ = 0;
        for (EnumDirection ☃ : EnumDirection.values())
        {
          if (☃.getType(☃.shift(☃)).getBlock() == Blocks.GLOWSTONE) {
            ☃++;
          }
          if (☃ > 1) {
            break;
          }
        }
        if (☃ == 1) {
          ☃.setTypeAndData(☃, Blocks.GLOWSTONE.getBlockData(), 2);
        }
      }
    }
    return true;
  }
}
