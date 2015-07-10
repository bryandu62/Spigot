package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomeDesert
  extends BiomeBase
{
  public BiomeDesert(int ☃)
  {
    super(☃);
    
    this.au.clear();
    this.ak = Blocks.SAND.getBlockData();
    this.al = Blocks.SAND.getBlockData();
    
    this.as.A = 64537;
    this.as.D = 2;
    this.as.F = 50;
    this.as.G = 10;
    
    this.au.clear();
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    super.a(☃, ☃, ☃);
    if (☃.nextInt(1000) == 0)
    {
      int ☃ = ☃.nextInt(16) + 8;
      int ☃ = ☃.nextInt(16) + 8;
      BlockPosition ☃ = ☃.getHighestBlockYAt(☃.a(☃, 0, ☃)).up();
      
      new WorldGenDesertWell().generate(☃, ☃, ☃);
    }
  }
}
