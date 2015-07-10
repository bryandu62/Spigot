package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenFlowers
  extends WorldGenerator
{
  private BlockFlowers a;
  private IBlockData b;
  
  public WorldGenFlowers(BlockFlowers ☃, BlockFlowers.EnumFlowerVarient ☃)
  {
    a(☃, ☃);
  }
  
  public void a(BlockFlowers ☃, BlockFlowers.EnumFlowerVarient ☃)
  {
    this.a = ☃;
    this.b = ☃.getBlockData().set(☃.n(), ☃);
  }
  
  public boolean generate(World ☃, Random ☃, BlockPosition ☃)
  {
    for (int ☃ = 0; ☃ < 64; ☃++)
    {
      BlockPosition ☃ = ☃.a(☃.nextInt(8) - ☃.nextInt(8), ☃.nextInt(4) - ☃.nextInt(4), ☃.nextInt(8) - ☃.nextInt(8));
      if ((☃.isEmpty(☃)) && ((!☃.worldProvider.o()) || (☃.getY() < 255)) && 
        (this.a.f(☃, ☃, this.b))) {
        ☃.setTypeAndData(☃, this.b, 2);
      }
    }
    return true;
  }
}
