package net.minecraft.server.v1_8_R3;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldChunkManagerHell
  extends WorldChunkManager
{
  private BiomeBase b;
  private float c;
  
  public WorldChunkManagerHell(BiomeBase ☃, float ☃)
  {
    this.b = ☃;
    this.c = ☃;
  }
  
  public BiomeBase getBiome(BlockPosition ☃)
  {
    return this.b;
  }
  
  public BiomeBase[] getBiomes(BiomeBase[] ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if ((☃ == null) || (☃.length < ☃ * ☃)) {
      ☃ = new BiomeBase[☃ * ☃];
    }
    Arrays.fill(☃, 0, ☃ * ☃, this.b);
    
    return ☃;
  }
  
  public float[] getWetness(float[] ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if ((☃ == null) || (☃.length < ☃ * ☃)) {
      ☃ = new float[☃ * ☃];
    }
    Arrays.fill(☃, 0, ☃ * ☃, this.c);
    
    return ☃;
  }
  
  public BiomeBase[] getBiomeBlock(BiomeBase[] ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if ((☃ == null) || (☃.length < ☃ * ☃)) {
      ☃ = new BiomeBase[☃ * ☃];
    }
    Arrays.fill(☃, 0, ☃ * ☃, this.b);
    
    return ☃;
  }
  
  public BiomeBase[] a(BiomeBase[] ☃, int ☃, int ☃, int ☃, int ☃, boolean ☃)
  {
    return getBiomeBlock(☃, ☃, ☃, ☃, ☃);
  }
  
  public BlockPosition a(int ☃, int ☃, int ☃, List<BiomeBase> ☃, Random ☃)
  {
    if (☃.contains(this.b)) {
      return new BlockPosition(☃ - ☃ + ☃.nextInt(☃ * 2 + 1), 0, ☃ - ☃ + ☃.nextInt(☃ * 2 + 1));
    }
    return null;
  }
  
  public boolean a(int ☃, int ☃, int ☃, List<BiomeBase> ☃)
  {
    return ☃.contains(this.b);
  }
}
