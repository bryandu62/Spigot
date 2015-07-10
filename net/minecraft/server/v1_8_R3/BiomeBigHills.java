package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BiomeBigHills
  extends BiomeBase
{
  private WorldGenerator aD = new WorldGenMinable(Blocks.MONSTER_EGG.getBlockData().set(BlockMonsterEggs.VARIANT, BlockMonsterEggs.EnumMonsterEggVarient.STONE), 9);
  private WorldGenTaiga2 aE = new WorldGenTaiga2(false);
  private int aF = 0;
  private int aG = 1;
  private int aH = 2;
  private int aI;
  
  protected BiomeBigHills(int ☃, boolean ☃)
  {
    super(☃);
    this.aI = this.aF;
    if (☃)
    {
      this.as.A = 3;
      this.aI = this.aG;
    }
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    if (☃.nextInt(3) > 0) {
      return this.aE;
    }
    return super.a(☃);
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    super.a(☃, ☃, ☃);
    
    int ☃ = 3 + ☃.nextInt(6);
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = ☃.nextInt(16);
      int ☃ = ☃.nextInt(28) + 4;
      int ☃ = ☃.nextInt(16);
      
      BlockPosition ☃ = ☃.a(☃, ☃, ☃);
      if (☃.getType(☃).getBlock() == Blocks.STONE) {
        ☃.setTypeAndData(☃, Blocks.EMERALD_ORE.getBlockData(), 2);
      }
    }
    for (int ☃ = 0; ☃ < 7; ☃++)
    {
      int ☃ = ☃.nextInt(16);
      int ☃ = ☃.nextInt(64);
      int ☃ = ☃.nextInt(16);
      this.aD.generate(☃, ☃, ☃.a(☃, ☃, ☃));
    }
  }
  
  public void a(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
  {
    this.ak = Blocks.GRASS.getBlockData();
    this.al = Blocks.DIRT.getBlockData();
    if (((☃ < -1.0D) || (☃ > 2.0D)) && (this.aI == this.aH))
    {
      this.ak = Blocks.GRAVEL.getBlockData();
      this.al = Blocks.GRAVEL.getBlockData();
    }
    else if ((☃ > 1.0D) && (this.aI != this.aG))
    {
      this.ak = Blocks.STONE.getBlockData();
      this.al = Blocks.STONE.getBlockData();
    }
    b(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  private BiomeBigHills b(BiomeBase ☃)
  {
    this.aI = this.aH;
    
    a(☃.ai, true);
    a(☃.ah + " M");
    a(new BiomeBase.BiomeTemperature(☃.an, ☃.ao));
    a(☃.temperature, ☃.humidity);
    return this;
  }
  
  protected BiomeBase d(int ☃)
  {
    return new BiomeBigHills(☃, false).b(this);
  }
}
