package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomeSavanna
  extends BiomeBase
{
  private static final WorldGenAcaciaTree aD = new WorldGenAcaciaTree(false);
  
  protected BiomeSavanna(int ☃)
  {
    super(☃);
    
    this.au.add(new BiomeBase.BiomeMeta(EntityHorse.class, 1, 2, 6));
    
    this.as.A = 1;
    this.as.B = 4;
    this.as.C = 20;
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    if (☃.nextInt(5) > 0) {
      return aD;
    }
    return this.aA;
  }
  
  protected BiomeBase d(int ☃)
  {
    BiomeBase ☃ = new BiomeSavannaSub(☃, this);
    
    ☃.temperature = ((this.temperature + 1.0F) * 0.5F);
    ☃.an = (this.an * 0.5F + 0.3F);
    ☃.ao = (this.ao * 0.5F + 1.2F);
    
    return ☃;
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    ag.a(BlockTallPlant.EnumTallFlowerVariants.GRASS);
    for (int ☃ = 0; ☃ < 7; ☃++)
    {
      int ☃ = ☃.nextInt(16) + 8;
      int ☃ = ☃.nextInt(16) + 8;
      int ☃ = ☃.nextInt(☃.getHighestBlockYAt(☃.a(☃, 0, ☃)).getY() + 32);
      ag.generate(☃, ☃, ☃.a(☃, ☃, ☃));
    }
    super.a(☃, ☃, ☃);
  }
  
  public static class BiomeSavannaSub
    extends BiomeBaseSub
  {
    public BiomeSavannaSub(int ☃, BiomeBase ☃)
    {
      super(☃);
      
      this.as.A = 2;
      this.as.B = 2;
      this.as.C = 5;
    }
    
    public void a(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
    {
      this.ak = Blocks.GRASS.getBlockData();
      this.al = Blocks.DIRT.getBlockData();
      if (☃ > 1.75D)
      {
        this.ak = Blocks.STONE.getBlockData();
        this.al = Blocks.STONE.getBlockData();
      }
      else if (☃ > -0.5D)
      {
        this.ak = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.COARSE_DIRT);
      }
      b(☃, ☃, ☃, ☃, ☃, ☃);
    }
    
    public void a(World ☃, Random ☃, BlockPosition ☃)
    {
      this.as.a(☃, ☃, this, ☃);
    }
  }
}
