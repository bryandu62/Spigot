package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class ChunkProviderTheEnd
  implements IChunkProvider
{
  private Random h;
  private NoiseGeneratorOctaves i;
  private NoiseGeneratorOctaves j;
  private NoiseGeneratorOctaves k;
  public NoiseGeneratorOctaves a;
  public NoiseGeneratorOctaves b;
  private World l;
  private double[] m;
  private BiomeBase[] n;
  double[] c;
  double[] d;
  double[] e;
  double[] f;
  double[] g;
  
  public ChunkProviderTheEnd(World ☃, long ☃)
  {
    this.l = ☃;
    
    this.h = new Random(☃);
    this.i = new NoiseGeneratorOctaves(this.h, 16);
    this.j = new NoiseGeneratorOctaves(this.h, 16);
    this.k = new NoiseGeneratorOctaves(this.h, 8);
    
    this.a = new NoiseGeneratorOctaves(this.h, 10);
    this.b = new NoiseGeneratorOctaves(this.h, 16);
  }
  
  public void a(int ☃, int ☃, ChunkSnapshot ☃)
  {
    int ☃ = 2;
    
    int ☃ = ☃ + 1;
    int ☃ = 33;
    int ☃ = ☃ + 1;
    this.m = a(this.m, ☃ * ☃, 0, ☃ * ☃, ☃, ☃, ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        for (int ☃ = 0; ☃ < 32; ☃++)
        {
          double ☃ = 0.25D;
          double ☃ = this.m[(((☃ + 0) * ☃ + ☃ + 0) * ☃ + ☃ + 0)];
          double ☃ = this.m[(((☃ + 0) * ☃ + ☃ + 1) * ☃ + ☃ + 0)];
          double ☃ = this.m[(((☃ + 1) * ☃ + ☃ + 0) * ☃ + ☃ + 0)];
          double ☃ = this.m[(((☃ + 1) * ☃ + ☃ + 1) * ☃ + ☃ + 0)];
          
          double ☃ = (this.m[(((☃ + 0) * ☃ + ☃ + 0) * ☃ + ☃ + 1)] - ☃) * ☃;
          double ☃ = (this.m[(((☃ + 0) * ☃ + ☃ + 1) * ☃ + ☃ + 1)] - ☃) * ☃;
          double ☃ = (this.m[(((☃ + 1) * ☃ + ☃ + 0) * ☃ + ☃ + 1)] - ☃) * ☃;
          double ☃ = (this.m[(((☃ + 1) * ☃ + ☃ + 1) * ☃ + ☃ + 1)] - ☃) * ☃;
          for (int ☃ = 0; ☃ < 4; ☃++)
          {
            double ☃ = 0.125D;
            
            double ☃ = ☃;
            double ☃ = ☃;
            double ☃ = (☃ - ☃) * ☃;
            double ☃ = (☃ - ☃) * ☃;
            for (int ☃ = 0; ☃ < 8; ☃++)
            {
              double ☃ = 0.125D;
              
              double ☃ = ☃;
              double ☃ = (☃ - ☃) * ☃;
              for (int ☃ = 0; ☃ < 8; ☃++)
              {
                IBlockData ☃ = null;
                if (☃ > 0.0D) {
                  ☃ = Blocks.END_STONE.getBlockData();
                }
                int ☃ = ☃ + ☃ * 8;
                int ☃ = ☃ + ☃ * 4;
                int ☃ = ☃ + ☃ * 8;
                ☃.a(☃, ☃, ☃, ☃);
                ☃ += ☃;
              }
              ☃ += ☃;
              ☃ += ☃;
            }
            ☃ += ☃;
            ☃ += ☃;
            ☃ += ☃;
            ☃ += ☃;
          }
        }
      }
    }
  }
  
  public void a(ChunkSnapshot ☃)
  {
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++)
      {
        int ☃ = 1;
        int ☃ = -1;
        
        IBlockData ☃ = Blocks.END_STONE.getBlockData();
        IBlockData ☃ = Blocks.END_STONE.getBlockData();
        for (int ☃ = 127; ☃ >= 0; ☃--)
        {
          IBlockData ☃ = ☃.a(☃, ☃, ☃);
          if (☃.getBlock().getMaterial() == Material.AIR) {
            ☃ = -1;
          } else if (☃.getBlock() == Blocks.STONE) {
            if (☃ == -1)
            {
              if (☃ <= 0)
              {
                ☃ = Blocks.AIR.getBlockData();
                ☃ = Blocks.END_STONE.getBlockData();
              }
              ☃ = ☃;
              if (☃ >= 0) {
                ☃.a(☃, ☃, ☃, ☃);
              } else {
                ☃.a(☃, ☃, ☃, ☃);
              }
            }
            else if (☃ > 0)
            {
              ☃--;
              ☃.a(☃, ☃, ☃, ☃);
            }
          }
        }
      }
    }
  }
  
  public Chunk getOrCreateChunk(int ☃, int ☃)
  {
    this.h.setSeed(☃ * 341873128712L + ☃ * 132897987541L);
    
    ChunkSnapshot ☃ = new ChunkSnapshot();
    this.n = this.l.getWorldChunkManager().getBiomeBlock(this.n, ☃ * 16, ☃ * 16, 16, 16);
    
    a(☃, ☃, ☃);
    a(☃);
    
    Chunk ☃ = new Chunk(this.l, ☃, ☃, ☃);
    byte[] ☃ = ☃.getBiomeIndex();
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      ☃[☃] = ((byte)this.n[☃].id);
    }
    ☃.initLighting();
    
    return ☃;
  }
  
  private double[] a(double[] ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if (☃ == null) {
      ☃ = new double[☃ * ☃ * ☃];
    }
    double ☃ = 684.412D;
    double ☃ = 684.412D;
    
    this.f = this.a.a(this.f, ☃, ☃, ☃, ☃, 1.121D, 1.121D, 0.5D);
    this.g = this.b.a(this.g, ☃, ☃, ☃, ☃, 200.0D, 200.0D, 0.5D);
    
    ☃ *= 2.0D;
    
    this.c = this.k.a(this.c, ☃, ☃, ☃, ☃, ☃, ☃, ☃ / 80.0D, ☃ / 160.0D, ☃ / 80.0D);
    this.d = this.i.a(this.d, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
    this.e = this.j.a(this.e, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
    
    int ☃ = 0;
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        float ☃ = (☃ + ☃) / 1.0F;
        float ☃ = (☃ + ☃) / 1.0F;
        float ☃ = 100.0F - MathHelper.c(☃ * ☃ + ☃ * ☃) * 8.0F;
        if (☃ > 80.0F) {
          ☃ = 80.0F;
        }
        if (☃ < -100.0F) {
          ☃ = -100.0F;
        }
        for (int ☃ = 0; ☃ < ☃; ☃++)
        {
          double ☃ = 0.0D;
          double ☃ = this.d[☃] / 512.0D;
          double ☃ = this.e[☃] / 512.0D;
          
          double ☃ = (this.c[☃] / 10.0D + 1.0D) / 2.0D;
          if (☃ < 0.0D) {
            ☃ = ☃;
          } else if (☃ > 1.0D) {
            ☃ = ☃;
          } else {
            ☃ = ☃ + (☃ - ☃) * ☃;
          }
          ☃ -= 8.0D;
          
          ☃ += ☃;
          
          int ☃ = 2;
          if (☃ > ☃ / 2 - ☃)
          {
            double ☃ = (☃ - (☃ / 2 - ☃)) / 64.0F;
            ☃ = MathHelper.a(☃, 0.0D, 1.0D);
            ☃ = ☃ * (1.0D - ☃) + -3000.0D * ☃;
          }
          ☃ = 8;
          if (☃ < ☃)
          {
            double ☃ = (☃ - ☃) / (☃ - 1.0F);
            ☃ = ☃ * (1.0D - ☃) + -30.0D * ☃;
          }
          ☃[☃] = ☃;
          ☃++;
        }
      }
    }
    return ☃;
  }
  
  public boolean isChunkLoaded(int ☃, int ☃)
  {
    return true;
  }
  
  public void getChunkAt(IChunkProvider ☃, int ☃, int ☃)
  {
    BlockFalling.instaFall = true;
    
    BlockPosition ☃ = new BlockPosition(☃ * 16, 0, ☃ * 16);
    this.l.getBiome(☃.a(16, 0, 16)).a(this.l, this.l.random, ☃);
    
    BlockFalling.instaFall = false;
  }
  
  public boolean a(IChunkProvider ☃, Chunk ☃, int ☃, int ☃)
  {
    return false;
  }
  
  public boolean saveChunks(boolean ☃, IProgressUpdate ☃)
  {
    return true;
  }
  
  public void c() {}
  
  public boolean unloadChunks()
  {
    return false;
  }
  
  public boolean canSave()
  {
    return true;
  }
  
  public String getName()
  {
    return "RandomLevelSource";
  }
  
  public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType ☃, BlockPosition ☃)
  {
    return this.l.getBiome(☃).getMobs(☃);
  }
  
  public BlockPosition findNearestMapFeature(World ☃, String ☃, BlockPosition ☃)
  {
    return null;
  }
  
  public int getLoadedChunks()
  {
    return 0;
  }
  
  public void recreateStructures(Chunk ☃, int ☃, int ☃) {}
  
  public Chunk getChunkAt(BlockPosition ☃)
  {
    return getOrCreateChunk(☃.getX() >> 4, ☃.getZ() >> 4);
  }
}
