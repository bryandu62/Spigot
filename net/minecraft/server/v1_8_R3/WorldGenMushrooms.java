package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenMushrooms
  extends WorldGenerator
{
  private BlockPlant a;
  
  public WorldGenMushrooms(BlockPlant ☃)
  {
    this.a = ☃;
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 64; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if ((☃.isEmpty(☃)) && ((!☃.worldProvider.o()) || (☃.getY() < 255)) && 
        (this.a.f(☃, ☃, this.a.getBlockData()))) {
        ☃.setTypeAndData(☃, this.a.getBlockData(), 2);
      }
    }
    return true;
  }
}
