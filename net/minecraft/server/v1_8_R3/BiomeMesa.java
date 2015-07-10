package net.minecraft.server.v1_8_R3;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BiomeMesa
  extends BiomeBase
{
  private IBlockData[] aD;
  private long aE;
  private NoiseGenerator3 aF;
  private NoiseGenerator3 aG;
  private NoiseGenerator3 aH;
  private boolean aI;
  private boolean aJ;
  
  public BiomeMesa(int ☃, boolean ☃, boolean ☃)
  {
    super(☃);
    this.aI = ☃;
    this.aJ = ☃;
    
    b();
    a(2.0F, 0.0F);
    
    this.au.clear();
    this.ak = Blocks.SAND.getBlockData().set(BlockSand.VARIANT, BlockSand.EnumSandVariant.RED_SAND);
    this.al = Blocks.STAINED_HARDENED_CLAY.getBlockData();
    
    this.as.A = 64537;
    this.as.D = 20;
    this.as.F = 3;
    this.as.G = 5;
    this.as.B = 0;
    
    this.au.clear();
    if (☃) {
      this.as.A = 5;
    }
  }
  
  public WorldGenTreeAbstract a(Random ☃)
  {
    return this.aA;
  }
  
  public void a(World ☃, Random ☃, BlockPosition ☃)
  {
    super.a(☃, ☃, ☃);
  }
  
  public void a(World ☃, Random ☃, ChunkSnapshot ☃, int ☃, int ☃, double ☃)
  {
    if ((this.aD == null) || (this.aE != ☃.getSeed())) {
      a(☃.getSeed());
    }
    if ((this.aF == null) || (this.aG == null) || (this.aE != ☃.getSeed()))
    {
      Random ☃ = new Random(this.aE);
      this.aF = new NoiseGenerator3(☃, 4);
      this.aG = new NoiseGenerator3(☃, 1);
    }
    this.aE = ☃.getSeed();
    
    double ☃ = 0.0D;
    if (this.aI)
    {
      int ☃ = (☃ & 0xFFFFFFF0) + (☃ & 0xF);
      int ☃ = (☃ & 0xFFFFFFF0) + (☃ & 0xF);
      
      double ☃ = Math.min(Math.abs(☃), this.aF.a(☃ * 0.25D, ☃ * 0.25D));
      if (☃ > 0.0D)
      {
        double ☃ = 0.001953125D;
        double ☃ = Math.abs(this.aG.a(☃ * ☃, ☃ * ☃));
        ☃ = ☃ * ☃ * 2.5D;
        double ☃ = Math.ceil(☃ * 50.0D) + 14.0D;
        if (☃ > ☃) {
          ☃ = ☃;
        }
        ☃ += 64.0D;
      }
    }
    int ☃ = ☃ & 0xF;
    int ☃ = ☃ & 0xF;
    
    int ☃ = ☃.F();
    
    IBlockData ☃ = Blocks.STAINED_HARDENED_CLAY.getBlockData();
    IBlockData ☃ = this.al;
    
    int ☃ = (int)(☃ / 3.0D + 3.0D + ☃.nextDouble() * 0.25D);
    boolean ☃ = Math.cos(☃ / 3.0D * 3.141592653589793D) > 0.0D;
    int ☃ = -1;
    boolean ☃ = false;
    for (int ☃ = 255; ☃ >= 0; ☃--)
    {
      if ((☃.a(☃, ☃, ☃).getBlock().getMaterial() == Material.AIR) && (☃ < (int)☃)) {
        ☃.a(☃, ☃, ☃, Blocks.STONE.getBlockData());
      }
      if (☃ <= ☃.nextInt(5))
      {
        ☃.a(☃, ☃, ☃, Blocks.BEDROCK.getBlockData());
      }
      else
      {
        IBlockData ☃ = ☃.a(☃, ☃, ☃);
        if (☃.getBlock().getMaterial() == Material.AIR) {
          ☃ = -1;
        } else if (☃.getBlock() == Blocks.STONE) {
          if (☃ == -1)
          {
            ☃ = false;
            if (☃ <= 0)
            {
              ☃ = null;
              ☃ = Blocks.STONE.getBlockData();
            }
            else if ((☃ >= ☃ - 4) && (☃ <= ☃ + 1))
            {
              ☃ = Blocks.STAINED_HARDENED_CLAY.getBlockData();
              ☃ = this.al;
            }
            if ((☃ < ☃) && ((☃ == null) || (☃.getBlock().getMaterial() == Material.AIR))) {
              ☃ = Blocks.WATER.getBlockData();
            }
            ☃ = ☃ + Math.max(0, ☃ - ☃);
            if (☃ >= ☃ - 1)
            {
              if ((this.aJ) && (☃ > 86 + ☃ * 2))
              {
                if (☃) {
                  ☃.a(☃, ☃, ☃, Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.COARSE_DIRT));
                } else {
                  ☃.a(☃, ☃, ☃, Blocks.GRASS.getBlockData());
                }
              }
              else if (☃ > ☃ + 3 + ☃)
              {
                IBlockData ☃;
                IBlockData ☃;
                if ((☃ < 64) || (☃ > 127))
                {
                  ☃ = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.ORANGE);
                }
                else
                {
                  IBlockData ☃;
                  if (☃) {
                    ☃ = Blocks.HARDENED_CLAY.getBlockData();
                  } else {
                    ☃ = a(☃, ☃, ☃);
                  }
                }
                ☃.a(☃, ☃, ☃, ☃);
              }
              else
              {
                ☃.a(☃, ☃, ☃, this.ak);
                ☃ = true;
              }
            }
            else
            {
              ☃.a(☃, ☃, ☃, ☃);
              if (☃.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
                ☃.a(☃, ☃, ☃, ☃.getBlock().getBlockData().set(BlockCloth.COLOR, EnumColor.ORANGE));
              }
            }
          }
          else if (☃ > 0)
          {
            ☃--;
            if (☃)
            {
              ☃.a(☃, ☃, ☃, Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.ORANGE));
            }
            else
            {
              IBlockData ☃ = a(☃, ☃, ☃);
              ☃.a(☃, ☃, ☃, ☃);
            }
          }
        }
      }
    }
  }
  
  private void a(long ☃)
  {
    this.aD = new IBlockData[64];
    Arrays.fill(this.aD, Blocks.HARDENED_CLAY.getBlockData());
    
    Random ☃ = new Random(☃);
    this.aH = new NoiseGenerator3(☃, 1);
    for (int ☃ = 0; ☃ < 64; ☃++)
    {
      ☃ += ☃.nextInt(5) + 1;
      if (☃ < 64) {
        this.aD[☃] = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.ORANGE);
      }
    }
    int ☃ = ☃.nextInt(4) + 2;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = ☃.nextInt(3) + 1;
      int ☃ = ☃.nextInt(64);
      for (int ☃ = 0; (☃ + ☃ < 64) && (☃ < ☃); ☃++) {
        this.aD[(☃ + ☃)] = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.YELLOW);
      }
    }
    int ☃ = ☃.nextInt(4) + 2;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = ☃.nextInt(3) + 2;
      int ☃ = ☃.nextInt(64);
      for (int ☃ = 0; (☃ + ☃ < 64) && (☃ < ☃); ☃++) {
        this.aD[(☃ + ☃)] = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.BROWN);
      }
    }
    int ☃ = ☃.nextInt(4) + 2;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = ☃.nextInt(3) + 1;
      int ☃ = ☃.nextInt(64);
      for (int ☃ = 0; (☃ + ☃ < 64) && (☃ < ☃); ☃++) {
        this.aD[(☃ + ☃)] = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.RED);
      }
    }
    int ☃ = ☃.nextInt(3) + 3;
    int ☃ = 0;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = 1;
      ☃ += ☃.nextInt(16) + 4;
      for (int ☃ = 0; (☃ + ☃ < 64) && (☃ < ☃); ☃++)
      {
        this.aD[(☃ + ☃)] = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.WHITE);
        if ((☃ + ☃ > 1) && (☃.nextBoolean())) {
          this.aD[(☃ + ☃ - 1)] = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.SILVER);
        }
        if ((☃ + ☃ < 63) && (☃.nextBoolean())) {
          this.aD[(☃ + ☃ + 1)] = Blocks.STAINED_HARDENED_CLAY.getBlockData().set(BlockCloth.COLOR, EnumColor.SILVER);
        }
      }
    }
  }
  
  private IBlockData a(int ☃, int ☃, int ☃)
  {
    int ☃ = (int)Math.round(this.aH.a(☃ * 1.0D / 512.0D, ☃ * 1.0D / 512.0D) * 2.0D);
    return this.aD[((☃ + ☃ + 64) % 64)];
  }
  
  protected BiomeBase d(int ☃)
  {
    boolean ☃ = this.id == BiomeBase.MESA.id;
    
    BiomeMesa ☃ = new BiomeMesa(☃, ☃, this.aJ);
    if (!☃)
    {
      ☃.a(g);
      ☃.a(this.ah + " M");
    }
    else
    {
      ☃.a(this.ah + " (Bryce)");
    }
    ☃.a(this.ai, true);
    
    return ☃;
  }
}
