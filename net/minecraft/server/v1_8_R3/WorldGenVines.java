package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenVines
  extends WorldGenerator
{
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    while (☃.getY() < 128)
    {
      if (☃.isEmpty(☃)) {
        for (EnumDirection ☃ : EnumDirection.EnumDirectionLimit.HORIZONTAL.a()) {
          if (Blocks.VINE.canPlace(☃, ☃, ☃))
          {
            IBlockData ☃ = Blocks.VINE.getBlockData().set(BlockVine.NORTH, Boolean.valueOf(☃ == EnumDirection.NORTH)).set(BlockVine.EAST, Boolean.valueOf(☃ == EnumDirection.EAST)).set(BlockVine.SOUTH, Boolean.valueOf(☃ == EnumDirection.SOUTH)).set(BlockVine.WEST, Boolean.valueOf(☃ == EnumDirection.WEST));
            
            ☃.setTypeAndData(☃, ☃, 2);
            break;
          }
        }
      } else {
        ☃ = ☃.a(☃.nextInt(4) - ☃.nextInt(4), 0, ☃.nextInt(4) - ☃.nextInt(4));
      }
      ☃ = ☃.up();
    }
    return true;
  }
}
