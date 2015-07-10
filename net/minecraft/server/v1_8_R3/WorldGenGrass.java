package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenGrass
  extends WorldGenerator
{
  private final IBlockData a;
  
  public WorldGenGrass(BlockLongGrass.EnumTallGrassType ☃)
  {
    this.a = Blocks.TALLGRASS.getBlockData().set(BlockLongGrass.TYPE, ☃);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    Block ☃;
    while ((((☃ = ☃.getType(☃).getBlock()).getMaterial() == Material.AIR) || (☃.getMaterial() == Material.LEAVES)) && (☃.getY() > 0)) {
      ☃ = ☃.down();
    }
    for (int ☃ = 0; ☃ < 128; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if ((☃.isEmpty(☃)) && 
        (Blocks.TALLGRASS.f(☃, ☃, this.a))) {
        ☃.setTypeAndData(☃, this.a, 2);
      }
    }
    return true;
  }
}
