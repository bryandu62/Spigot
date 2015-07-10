package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenTallPlant
  extends WorldGenerator
{
  private BlockTallPlant.EnumTallFlowerVariants a;
  
  public void a(BlockTallPlant.EnumTallFlowerVariants ☃)
  {
    this.a = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    boolean ☃ = false;
    for (int ☃ = 0; ☃ < 64; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if ((☃.isEmpty(☃)) && ((!☃.worldProvider.o()) || (☃.getY() < 254)) && 
        (Blocks.DOUBLE_PLANT.canPlace(☃, ☃)))
      {
        Blocks.DOUBLE_PLANT.a(☃, ☃, this.a, 2);
        ☃ = true;
      }
    }
    return ☃;
  }
}
