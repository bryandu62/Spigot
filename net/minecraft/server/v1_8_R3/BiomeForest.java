package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomeForest
  extends BiomeBase
{
  private int aG;
  protected static final WorldGenForest aD = new WorldGenForest(false, true);
  protected static final WorldGenForest aE = new WorldGenForest(false, false);
  protected static final WorldGenForestTree aF = new WorldGenForestTree(false);
  
  public BiomeForest(int ☃, int ☃)
  {
    super(☃);
    this.aG = ☃;
    this.as.A = 10;
    this.as.C = 2;
    if (this.aG == 1)
    {
      this.as.A = 6;
      this.as.B = 100;
      this.as.C = 1;
    }
    a(5159473);
    a(0.7F, 0.8F);
    if (this.aG == 2)
    {
      this.aj = 353825;
      this.ai = 3175492;
      a(0.6F, 0.6F);
    }
    if (this.aG == 0) {
      this.au.add(new BiomeBase.BiomeMeta(EntityWolf.class, 5, 4, 4));
    }
    if (this.aG == 3) {
      this.as.A = 64537;
    }
  }
  
  protected BiomeBase a(int ☃, boolean ☃)
  {
    if (this.aG == 2)
    {
      this.aj = 353825;
      this.ai = ☃;
      if (☃) {
        this.aj = ((this.aj & 0xFEFEFE) >> 1);
      }
      return this;
    }
    return super.a(☃, ☃);
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    if ((this.aG == 3) && (☃.nextInt(3) > 0)) {
      return aF;
    }
    if ((this.aG == 2) || (☃.nextInt(5) == 0)) {
      return aE;
    }
    return this.aA;
  }
  
  public BlockFlowers.EnumFlowerVarient a(Random ☃, BlockPosition ☃)
  {
    if (this.aG == 1)
    {
      double ☃ = MathHelper.a((1.0D + af.a(☃.getX() / 48.0D, ☃.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
      BlockFlowers.EnumFlowerVarient ☃ = BlockFlowers.EnumFlowerVarient.values()[((int)(☃ * BlockFlowers.EnumFlowerVarient.values().length))];
      if (☃ == BlockFlowers.EnumFlowerVarient.BLUE_ORCHID) {
        return BlockFlowers.EnumFlowerVarient.POPPY;
      }
      return ☃;
    }
    return super.a(☃, ☃);
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    if (this.aG == 3) {
      for (int ☃ = 0; ☃ < 4; ☃++) {
        for (int ☃ = 0; ☃ < 4; ☃++)
        {
          int ☃ = ☃ * 4 + 1 + 8 + ☃.nextInt(3);
          int ☃ = ☃ * 4 + 1 + 8 + ☃.nextInt(3);
          
          BlockPosition ☃ = ☃.getHighestBlockYAt(☃.a(☃, 0, ☃));
          if (☃.nextInt(20) == 0)
          {
            WorldGenHugeMushroom ☃ = new WorldGenHugeMushroom();
            ☃.generate(☃, ☃, ☃);
          }
          else
          {
            WorldGenTreeAbstract ☃ = a(☃);
            ☃.e();
            if (☃.generate(☃, ☃, ☃)) {
              ☃.a(☃, ☃, ☃);
            }
          }
        }
      }
    }
    int ☃ = ☃.nextInt(5) - 3;
    if (this.aG == 1) {
      ☃ += 2;
    }
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = ☃.nextInt(3);
      if (☃ == 0) {
        ag.a(BlockTallPlant.EnumTallFlowerVariants.SYRINGA);
      } else if (☃ == 1) {
        ag.a(BlockTallPlant.EnumTallFlowerVariants.ROSE);
      } else if (☃ == 2) {
        ag.a(BlockTallPlant.EnumTallFlowerVariants.PAEONIA);
      }
      for (int ☃ = 0; ☃ < 5; ☃++)
      {
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(☃.getHighestBlockYAt(☃.a(☃, 0, ☃)).getY() + 32);
        if (ag.generate(☃, ☃, new BlockPosition(☃.getX() + ☃, ☃, ☃.getZ() + ☃))) {
          break;
        }
      }
    }
    super.a(☃, ☃, ☃);
  }
  
  protected BiomeBase d(int ☃)
  {
    if (this.id == BiomeBase.FOREST.id)
    {
      BiomeForest ☃ = new BiomeForest(☃, 1);
      ☃.a(new BiomeBase.BiomeTemperature(this.an, this.ao + 0.2F));
      ☃.a("Flower Forest");
      ☃.a(6976549, true);
      ☃.a(8233509);
      return ☃;
    }
    if ((this.id == BiomeBase.BIRCH_FOREST.id) || (this.id == BiomeBase.BIRCH_FOREST_HILLS.id)) {
      new BiomeBaseSub(☃, this)
      {
        public WorldGenTreeAbstract a(Random ☃)
        {
          if (☃.nextBoolean()) {
            return BiomeForest.aD;
          }
          return BiomeForest.aE;
        }
      };
    }
    new BiomeBaseSub(☃, this)
    {
      public void a(World ☃, Random ☃, BlockPosition ☃)
      {
        this.aE.a(☃, ☃, ☃);
      }
    };
  }
}
