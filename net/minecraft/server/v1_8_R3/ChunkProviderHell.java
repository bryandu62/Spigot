package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class ChunkProviderHell
  implements IChunkProvider
{
  private final World h;
  private final boolean i;
  private final Random j;
  private double[] k = new double['Ā'];
  private double[] l = new double['Ā'];
  private double[] m = new double['Ā'];
  private double[] n;
  private final NoiseGeneratorOctaves o;
  private final NoiseGeneratorOctaves p;
  private final NoiseGeneratorOctaves q;
  private final NoiseGeneratorOctaves r;
  private final NoiseGeneratorOctaves s;
  public final NoiseGeneratorOctaves a;
  public final NoiseGeneratorOctaves b;
  private final WorldGenFire t = new WorldGenFire();
  private final WorldGenLightStone1 u = new WorldGenLightStone1();
  private final WorldGenLightStone2 v = new WorldGenLightStone2();
  private final WorldGenerator w = new WorldGenMinable(Blocks.QUARTZ_ORE.getBlockData(), 14, BlockPredicate.a(Blocks.NETHERRACK));
  private final WorldGenHellLava x = new WorldGenHellLava(Blocks.FLOWING_LAVA, true);
  private final WorldGenHellLava y = new WorldGenHellLava(Blocks.FLOWING_LAVA, false);
  private final WorldGenMushrooms z = new WorldGenMushrooms(Blocks.BROWN_MUSHROOM);
  private final WorldGenMushrooms A = new WorldGenMushrooms(Blocks.RED_MUSHROOM);
  private final WorldGenNether B = new WorldGenNether();
  private final WorldGenBase C = new WorldGenCavesHell();
  double[] c;
  double[] d;
  double[] e;
  double[] f;
  double[] g;
  
  public ChunkProviderHell(World ☃, boolean ☃, long ☃)
  {
    this.h = ☃;
    this.i = ☃;
    
    this.j = new Random(☃);
    this.o = new NoiseGeneratorOctaves(this.j, 16);
    this.p = new NoiseGeneratorOctaves(this.j, 16);
    this.q = new NoiseGeneratorOctaves(this.j, 8);
    this.r = new NoiseGeneratorOctaves(this.j, 4);
    this.s = new NoiseGeneratorOctaves(this.j, 4);
    
    this.a = new NoiseGeneratorOctaves(this.j, 10);
    this.b = new NoiseGeneratorOctaves(this.j, 16);
    
    ☃.b(63);
  }
  
  public void a(int ☃, int ☃, ChunkSnapshot ☃)
  {
    int ☃ = 4;
    int ☃ = this.h.F() / 2 + 1;
    
    int ☃ = ☃ + 1;
    int ☃ = 17;
    int ☃ = ☃ + 1;
    this.n = a(this.n, ☃ * ☃, 0, ☃ * ☃, ☃, ☃, ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        for (int ☃ = 0; ☃ < 16; ☃++)
        {
          double ☃ = 0.125D;
          double ☃ = this.n[(((☃ + 0) * ☃ + (☃ + 0)) * ☃ + (☃ + 0))];
          double ☃ = this.n[(((☃ + 0) * ☃ + (☃ + 1)) * ☃ + (☃ + 0))];
          double ☃ = this.n[(((☃ + 1) * ☃ + (☃ + 0)) * ☃ + (☃ + 0))];
          double ☃ = this.n[(((☃ + 1) * ☃ + (☃ + 1)) * ☃ + (☃ + 0))];
          
          double ☃ = (this.n[(((☃ + 0) * ☃ + (☃ + 0)) * ☃ + (☃ + 1))] - ☃) * ☃;
          double ☃ = (this.n[(((☃ + 0) * ☃ + (☃ + 1)) * ☃ + (☃ + 1))] - ☃) * ☃;
          double ☃ = (this.n[(((☃ + 1) * ☃ + (☃ + 0)) * ☃ + (☃ + 1))] - ☃) * ☃;
          double ☃ = (this.n[(((☃ + 1) * ☃ + (☃ + 1)) * ☃ + (☃ + 1))] - ☃) * ☃;
          for (int ☃ = 0; ☃ < 8; ☃++)
          {
            double ☃ = 0.25D;
            
            double ☃ = ☃;
            double ☃ = ☃;
            double ☃ = (☃ - ☃) * ☃;
            double ☃ = (☃ - ☃) * ☃;
            for (int ☃ = 0; ☃ < 4; ☃++)
            {
              double ☃ = 0.25D;
              
              double ☃ = ☃;
              double ☃ = (☃ - ☃) * ☃;
              for (int ☃ = 0; ☃ < 4; ☃++)
              {
                IBlockData ☃ = null;
                if (☃ * 8 + ☃ < ☃) {
                  ☃ = Blocks.LAVA.getBlockData();
                }
                if (☃ > 0.0D) {
                  ☃ = Blocks.NETHERRACK.getBlockData();
                }
                int ☃ = ☃ + ☃ * 4;
                int ☃ = ☃ + ☃ * 8;
                int ☃ = ☃ + ☃ * 4;
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
  
  public void b(int ☃, int ☃, ChunkSnapshot ☃)
  {
    int ☃ = this.h.F() + 1;
    
    double ☃ = 0.03125D;
    this.k = this.r.a(this.k, ☃ * 16, ☃ * 16, 0, 16, 16, 1, ☃, ☃, 1.0D);
    this.l = this.r.a(this.l, ☃ * 16, 109, ☃ * 16, 16, 1, 16, ☃, 1.0D, ☃);
    this.m = this.s.a(this.m, ☃ * 16, ☃ * 16, 0, 16, 16, 1, ☃ * 2.0D, ☃ * 2.0D, ☃ * 2.0D);
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++)
      {
        boolean ☃ = this.k[(☃ + ☃ * 16)] + this.j.nextDouble() * 0.2D > 0.0D;
        boolean ☃ = this.l[(☃ + ☃ * 16)] + this.j.nextDouble() * 0.2D > 0.0D;
        int ☃ = (int)(this.m[(☃ + ☃ * 16)] / 3.0D + 3.0D + this.j.nextDouble() * 0.25D);
        
        int ☃ = -1;
        
        IBlockData ☃ = Blocks.NETHERRACK.getBlockData();
        IBlockData ☃ = Blocks.NETHERRACK.getBlockData();
        for (int ☃ = 127; ☃ >= 0; ☃--) {
          if ((☃ >= 127 - this.j.nextInt(5)) || (☃ <= this.j.nextInt(5)))
          {
            ☃.a(☃, ☃, ☃, Blocks.BEDROCK.getBlockData());
          }
          else
          {
            IBlockData ☃ = ☃.a(☃, ☃, ☃);
            if ((☃.getBlock() == null) || (☃.getBlock().getMaterial() == Material.AIR)) {
              ☃ = -1;
            } else if (☃.getBlock() == Blocks.NETHERRACK) {
              if (☃ == -1)
              {
                if (☃ <= 0)
                {
                  ☃ = null;
                  ☃ = Blocks.NETHERRACK.getBlockData();
                }
                else if ((☃ >= ☃ - 4) && (☃ <= ☃ + 1))
                {
                  ☃ = Blocks.NETHERRACK.getBlockData();
                  ☃ = Blocks.NETHERRACK.getBlockData();
                  if (☃)
                  {
                    ☃ = Blocks.GRAVEL.getBlockData();
                    ☃ = Blocks.NETHERRACK.getBlockData();
                  }
                  if (☃)
                  {
                    ☃ = Blocks.SOUL_SAND.getBlockData();
                    ☃ = Blocks.SOUL_SAND.getBlockData();
                  }
                }
                if ((☃ < ☃) && ((☃ == null) || (☃.getBlock().getMaterial() == Material.AIR))) {
                  ☃ = Blocks.LAVA.getBlockData();
                }
                ☃ = ☃;
                if (☃ >= ☃ - 1) {
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
  }
  
  public Chunk getOrCreateChunk(int ☃, int ☃)
  {
    this.j.setSeed(☃ * 341873128712L + ☃ * 132897987541L);
    
    ChunkSnapshot ☃ = new ChunkSnapshot();
    
    a(☃, ☃, ☃);
    b(☃, ☃, ☃);
    
    this.C.a(this, this.h, ☃, ☃, ☃);
    if (this.i) {
      this.B.a(this, this.h, ☃, ☃, ☃);
    }
    Chunk ☃ = new Chunk(this.h, ☃, ☃, ☃);
    BiomeBase[] ☃ = this.h.getWorldChunkManager().getBiomeBlock(null, ☃ * 16, ☃ * 16, 16, 16);
    byte[] ☃ = ☃.getBiomeIndex();
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      ☃[☃] = ((byte)☃[☃].id);
    }
    ☃.l();
    
    return ☃;
  }
  
  private double[] a(double[] ☃, int ☃, int ☃, int ☃, int ☃, int ☃, int ☃)
  {
    if (☃ == null) {
      ☃ = new double[☃ * ☃ * ☃];
    }
    double ☃ = 684.412D;
    double ☃ = 2053.236D;
    
    this.f = this.a.a(this.f, ☃, ☃, ☃, ☃, 1, ☃, 1.0D, 0.0D, 1.0D);
    this.g = this.b.a(this.g, ☃, ☃, ☃, ☃, 1, ☃, 100.0D, 0.0D, 100.0D);
    
    this.c = this.q.a(this.c, ☃, ☃, ☃, ☃, ☃, ☃, ☃ / 80.0D, ☃ / 60.0D, ☃ / 80.0D);
    this.d = this.o.a(this.d, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
    this.e = this.p.a(this.e, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
    
    int ☃ = 0;
    double[] ☃ = new double[☃];
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      ☃[☃] = (Math.cos(☃ * 3.141592653589793D * 6.0D / ☃) * 2.0D);
      
      double ☃ = ☃;
      if (☃ > ☃ / 2) {
        ☃ = ☃ - 1 - ☃;
      }
      if (☃ < 4.0D)
      {
        ☃ = 4.0D - ☃;
        ☃[☃] -= ☃ * ☃ * ☃ * 10.0D;
      }
    }
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      for (int ☃ = 0; ☃ < ☃; ☃++)
      {
        double ☃ = 0.0D;
        for (int ☃ = 0; ☃ < ☃; ☃++)
        {
          double ☃ = 0.0D;
          double ☃ = ☃[☃];
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
          ☃ -= ☃;
          if (☃ > ☃ - 4)
          {
            double ☃ = (☃ - (☃ - 4)) / 3.0F;
            ☃ = ☃ * (1.0D - ☃) + -10.0D * ☃;
          }
          if (☃ < ☃)
          {
            double ☃ = (☃ - ☃) / 4.0D;
            ☃ = MathHelper.a(☃, 0.0D, 1.0D);
            ☃ = ☃ * (1.0D - ☃) + -10.0D * ☃;
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
    
    ChunkCoordIntPair ☃ = new ChunkCoordIntPair(☃, ☃);
    
    this.B.a(this.h, this.j, ☃);
    for (int ☃ = 0; ☃ < 8; ☃++) {
      this.y.generate(this.h, this.j, ☃.a(this.j.nextInt(16) + 8, this.j.nextInt(120) + 4, this.j.nextInt(16) + 8));
    }
    for (int ☃ = 0; ☃ < this.j.nextInt(this.j.nextInt(10) + 1) + 1; ☃++) {
      this.t.generate(this.h, this.j, ☃.a(this.j.nextInt(16) + 8, this.j.nextInt(120) + 4, this.j.nextInt(16) + 8));
    }
    for (int ☃ = 0; ☃ < this.j.nextInt(this.j.nextInt(10) + 1); ☃++) {
      this.u.generate(this.h, this.j, ☃.a(this.j.nextInt(16) + 8, this.j.nextInt(120) + 4, this.j.nextInt(16) + 8));
    }
    for (int ☃ = 0; ☃ < 10; ☃++) {
      this.v.generate(this.h, this.j, ☃.a(this.j.nextInt(16) + 8, this.j.nextInt(128), this.j.nextInt(16) + 8));
    }
    if (this.j.nextBoolean()) {
      this.z.generate(this.h, this.j, ☃.a(this.j.nextInt(16) + 8, this.j.nextInt(128), this.j.nextInt(16) + 8));
    }
    if (this.j.nextBoolean()) {
      this.A.generate(this.h, this.j, ☃.a(this.j.nextInt(16) + 8, this.j.nextInt(128), this.j.nextInt(16) + 8));
    }
    for (int ☃ = 0; ☃ < 16; ☃++) {
      this.w.generate(this.h, this.j, ☃.a(this.j.nextInt(16), this.j.nextInt(108) + 10, this.j.nextInt(16)));
    }
    for (int ☃ = 0; ☃ < 16; ☃++) {
      this.x.generate(this.h, this.j, ☃.a(this.j.nextInt(16), this.j.nextInt(108) + 10, this.j.nextInt(16)));
    }
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
    return "HellRandomLevelSource";
  }
  
  public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType ☃, BlockPosition ☃)
  {
    if (☃ == EnumCreatureType.MONSTER)
    {
      if (this.B.b(☃)) {
        return this.B.b();
      }
      if ((this.B.a(this.h, ☃)) && (this.h.getType(☃.down()).getBlock() == Blocks.NETHER_BRICK)) {
        return this.B.b();
      }
    }
    BiomeBase ☃ = this.h.getBiome(☃);
    return ☃.getMobs(☃);
  }
  
  public BlockPosition findNearestMapFeature(World ☃, String ☃, BlockPosition ☃)
  {
    return null;
  }
  
  public int getLoadedChunks()
  {
    return 0;
  }
  
  public void recreateStructures(Chunk ☃, int ☃, int ☃)
  {
    this.B.a(this, this.h, ☃, ☃, null);
  }
  
  public Chunk getChunkAt(BlockPosition ☃)
  {
    return getOrCreateChunk(☃.getX() >> 4, ☃.getZ() >> 4);
  }
}
