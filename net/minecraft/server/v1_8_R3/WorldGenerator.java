package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class WorldGenerator
{
  private final boolean a;
  
  public WorldGenerator()
  {
    this(false);
  }
  
  public WorldGenerator(boolean ☃)
  {
    this.a = ☃;
  }
  
  public abstract boolean generate(World paramWorld, Random paramRandom, BlockPosition paramBlockPosition);
  
  public void e() {}
  
  protected void a(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (this.a) {
      ☃.setTypeAndData(☃, ☃, 3);
    } else {
      ☃.setTypeAndData(☃, ☃, 2);
    }
  }
}
