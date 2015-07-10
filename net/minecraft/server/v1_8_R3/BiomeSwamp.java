package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomeSwamp
  extends BiomeBase
{
  protected BiomeSwamp(int ☃)
  {
    super(☃);
    this.as.A = 2;
    this.as.B = 1;
    this.as.D = 1;
    this.as.E = 8;
    this.as.F = 10;
    this.as.J = 1;
    this.as.z = 4;
    this.as.I = 0;
    this.as.H = 0;
    this.as.C = 5;
    
    this.ar = 14745518;
    
    this.at.add(new BiomeBase.BiomeMeta(EntitySlime.class, 1, 1, 1));
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    return this.aC;
  }
  
  public BlockFlowers.EnumFlowerVarient a(Random ☃, BlockPosition ☃)
  {
    return BlockFlowers.EnumFlowerVarient.BLUE_ORCHID;
  }
  
  public void a(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
  {
    double ☃ = af.a(☃ * 0.25D, ☃ * 0.25D);
    if (☃ > 0.0D)
    {
      int ☃ = ☃ & 0xF;
      int ☃ = ☃ & 0xF;
      for (int ☃ = 255; ☃ >= 0; ☃--) {
        if (☃.a(☃, ☃, ☃).getBlock().getMaterial() != Material.AIR)
        {
          if ((☃ != 62) || (☃.a(☃, ☃, ☃).getBlock() == Blocks.WATER)) {
            break;
          }
          ☃.a(☃, ☃, ☃, Blocks.WATER.getBlockData());
          if (☃ >= 0.12D) {
            break;
          }
          ☃.a(☃, ☃ + 1, ☃, Blocks.WATERLILY.getBlockData()); break;
        }
      }
    }
    b(☃, ☃, ☃, ☃, ☃, ☃);
  }
}
