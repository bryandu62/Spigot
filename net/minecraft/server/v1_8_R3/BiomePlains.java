package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class BiomePlains
  extends BiomeBase
{
  protected boolean aD;
  
  protected BiomePlains(int ☃)
  {
    super(☃);
    
    a(0.8F, 0.4F);
    a(e);
    
    this.au.add(new BiomeBase.BiomeMeta(EntityHorse.class, 5, 2, 6));
    
    this.as.A = 64537;
    this.as.B = 4;
    this.as.C = 10;
  }
  
  public BlockFlowers.EnumFlowerVarient a(Random ☃, BlockPosition ☃)
  {
    double ☃ = af.a(☃.getX() / 200.0D, ☃.getZ() / 200.0D);
    if (☃ < -0.8D)
    {
      int ☃ = ☃.nextInt(4);
      switch (☃)
      {
      case 0: 
        return BlockFlowers.EnumFlowerVarient.ORANGE_TULIP;
      case 1: 
        return BlockFlowers.EnumFlowerVarient.RED_TULIP;
      case 2: 
        return BlockFlowers.EnumFlowerVarient.PINK_TULIP;
      }
      return BlockFlowers.EnumFlowerVarient.WHITE_TULIP;
    }
    if (☃.nextInt(3) > 0)
    {
      int ☃ = ☃.nextInt(3);
      if (☃ == 0) {
        return BlockFlowers.EnumFlowerVarient.POPPY;
      }
      if (☃ == 1) {
        return BlockFlowers.EnumFlowerVarient.HOUSTONIA;
      }
      return BlockFlowers.EnumFlowerVarient.OXEYE_DAISY;
    }
    return BlockFlowers.EnumFlowerVarient.DANDELION;
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    double ☃ = af.a((☃.getX() + 8) / 200.0D, (☃.getZ() + 8) / 200.0D);
    if (☃ < -0.8D)
    {
      this.as.B = 15;
      this.as.C = 5;
    }
    else
    {
      this.as.B = 4;
      this.as.C = 10;
      
      ag.a(BlockTallPlant.EnumTallFlowerVariants.GRASS);
      for (int ☃ = 0; ☃ < 7; ☃++)
      {
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(☃.getHighestBlockYAt(☃.a(☃, 0, ☃)).getY() + 32);
        ag.generate(☃, ☃, ☃.a(☃, ☃, ☃));
      }
    }
    if (this.aD)
    {
      ag.a(BlockTallPlant.EnumTallFlowerVariants.SUNFLOWER);
      for (int ☃ = 0; ☃ < 10; ☃++)
      {
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(16) + 8;
        int ☃ = ☃.nextInt(☃.getHighestBlockYAt(☃.a(☃, 0, ☃)).getY() + 32);
        ag.generate(☃, ☃, ☃.a(☃, ☃, ☃));
      }
    }
    super.a(☃, ☃, ☃);
  }
  
  protected BiomeBase d(int ☃)
  {
    BiomePlains ☃ = new BiomePlains(☃);
    ☃.a("Sunflower Plains");
    ☃.aD = true;
    ☃.b(9286496);
    ☃.aj = 14273354;
    return ☃;
  }
}
