package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomeJungle
  extends BiomeBase
{
  private boolean aD;
  private static final IBlockData aE = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
  private static final IBlockData aF = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  private static final IBlockData aG = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
  
  public BiomeJungle(int ☃, boolean ☃)
  {
    super(☃);
    this.aD = ☃;
    if (☃) {
      this.as.A = 2;
    } else {
      this.as.A = 50;
    }
    this.as.C = 25;
    this.as.B = 4;
    if (!☃) {
      this.at.add(new BiomeBase.BiomeMeta(EntityOcelot.class, 2, 1, 1));
    }
    this.au.add(new BiomeBase.BiomeMeta(EntityChicken.class, 10, 4, 4));
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    if (☃.nextInt(10) == 0) {
      return this.aB;
    }
    if (☃.nextInt(2) == 0) {
      return new WorldGenGroundBush(aE, aG);
    }
    if ((!this.aD) && (☃.nextInt(3) == 0)) {
      return new WorldGenJungleTree(false, 10, 20, aE, aF);
    }
    return new WorldGenTrees(false, 4 + ☃.nextInt(7), aE, aF, true);
  }
  
  public WorldGenerator b(Random ☃)
  {
    if (☃.nextInt(4) == 0) {
      return new WorldGenGrass(BlockLongGrass.EnumTallGrassType.FERN);
    }
    return new WorldGenGrass(BlockLongGrass.EnumTallGrassType.GRASS);
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    super.a(☃, ☃, ☃);
    
    int ☃ = ☃.nextInt(16) + 8;
    int ☃ = ☃.nextInt(16) + 8;
    int ☃ = ☃.nextInt(☃.getHighestBlockYAt(☃.a(☃, 0, ☃)).getY() * 2);
    new WorldGenMelon().generate(☃, ☃, ☃.a(☃, ☃, ☃));
    
    WorldGenVines ☃ = new WorldGenVines();
    for (int ☃ = 0; ☃ < 50; ☃++)
    {
      int ☃ = ☃.nextInt(16) + 8;
      int ☃ = 128;
      int ☃ = ☃.nextInt(16) + 8;
      
      ☃.generate(☃, ☃, ☃.a(☃, 128, ☃));
    }
  }
}
