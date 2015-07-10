package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomeTaiga
  extends BiomeBase
{
  private static final WorldGenTaiga1 aD = new WorldGenTaiga1();
  private static final WorldGenTaiga2 aE = new WorldGenTaiga2(false);
  private static final WorldGenMegaTree aF = new WorldGenMegaTree(false, false);
  private static final WorldGenMegaTree aG = new WorldGenMegaTree(false, true);
  private static final WorldGenTaigaStructure aH = new WorldGenTaigaStructure(Blocks.MOSSY_COBBLESTONE, 0);
  private int aI;
  
  public BiomeTaiga(int ☃, int ☃)
  {
    super(☃);
    this.aI = ☃;
    
    this.au.add(new BiomeBase.BiomeMeta(EntityWolf.class, 8, 4, 4));
    
    this.as.A = 10;
    if ((☃ == 1) || (☃ == 2))
    {
      this.as.C = 7;
      this.as.D = 1;
      this.as.E = 3;
    }
    else
    {
      this.as.C = 1;
      this.as.E = 1;
    }
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    if (((this.aI == 1) || (this.aI == 2)) && (☃.nextInt(3) == 0))
    {
      if ((this.aI == 2) || (☃.nextInt(13) == 0)) {
        return aG;
      }
      return aF;
    }
    if (☃.nextInt(3) == 0) {
      return aD;
    }
    return aE;
  }
  
  public WorldGenerator b(Random ☃)
  {
    if (☃.nextInt(5) > 0) {
      return new WorldGenGrass(BlockLongGrass.EnumTallGrassType.FERN);
    }
    return new WorldGenGrass(BlockLongGrass.EnumTallGrassType.GRASS);
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    if ((this.aI == 1) || (this.aI == 2))
    {
      int ☃ = ☃.nextInt(3);
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(16) + 8;
        BlockPosition ☃ = ☃.getHighestBlockYAt(☃.a(☃, 0, ☃));
        aH.generate(☃, ☃, ☃);
      }
    }
    ag.a(BlockTallPlant.EnumTallFlowerVariants.FERN);
    for (int ☃ = 0; ☃ < 7; ☃++)
    {
      int ☃ = ☃.nextInt(16) + 8;
      int ☃ = ☃.nextInt(16) + 8;
      int ☃ = ☃.nextInt(☃.getHighestBlockYAt(☃.a(☃, 0, ☃)).getY() + 32);
      ag.generate(☃, ☃, ☃.a(☃, ☃, ☃));
    }
    super.a(☃, ☃, ☃);
  }
  
  public void a(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
  {
    if ((this.aI == 1) || (this.aI == 2))
    {
      this.ak = Blocks.GRASS.getBlockData();
      this.al = Blocks.DIRT.getBlockData();
      if (☃ > 1.75D) {
        this.ak = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.COARSE_DIRT);
      } else if (☃ > -0.95D) {
        this.ak = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.PODZOL);
      }
    }
    b(☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  protected BiomeBase d(int ☃)
  {
    if (this.id == BiomeBase.MEGA_TAIGA.id) {
      return new BiomeTaiga(☃, 2).a(5858897, true).a("Mega Spruce Taiga").a(5159473).a(0.25F, 0.8F).a(new BiomeBase.BiomeTemperature(this.an, this.ao));
    }
    return super.d(☃);
  }
}
