package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class ChunkProviderGenerate
  implements IChunkProvider
{
  private Random h;
  private NoiseGeneratorOctaves i;
  private NoiseGeneratorOctaves j;
  private NoiseGeneratorOctaves k;
  private NoiseGenerator3 l;
  public NoiseGeneratorOctaves a;
  public NoiseGeneratorOctaves b;
  public NoiseGeneratorOctaves c;
  private World m;
  private final boolean n;
  private WorldType o;
  private final double[] p;
  private final float[] q;
  private CustomWorldSettingsFinal r;
  private Block s = Blocks.WATER;
  
  public ChunkProviderGenerate(World ☃, long ☃, boolean ☃, String ☃)
  {
    this.m = ☃;
    this.n = ☃;
    this.o = ☃.getWorldData().getType();
    
    this.h = new Random(☃);
    this.i = new NoiseGeneratorOctaves(this.h, 16);
    this.j = new NoiseGeneratorOctaves(this.h, 16);
    this.k = new NoiseGeneratorOctaves(this.h, 8);
    this.l = new NoiseGenerator3(this.h, 4);
    
    this.a = new NoiseGeneratorOctaves(this.h, 10);
    this.b = new NoiseGeneratorOctaves(this.h, 16);
    
    this.c = new NoiseGeneratorOctaves(this.h, 8);
    
    this.p = new double['̹'];
    
    this.q = new float[25];
    for (int ☃ = -2; ☃ <= 2; ☃++) {
      for (int ☃ = -2; ☃ <= 2; ☃++)
      {
        float ☃ = 10.0F / MathHelper.c(☃ * ☃ + ☃ * ☃ + 0.2F);
        this.q[(☃ + 2 + (☃ + 2) * 5)] = ☃;
      }
    }
    if (☃ != null)
    {
      this.r = CustomWorldSettingsFinal.CustomWorldSettings.a(☃).b();
      this.s = (this.r.E ? Blocks.LAVA : Blocks.WATER);
      ☃.b(this.r.q);
    }
  }
  
  public void a(int ☃, int ☃, ChunkSnapshot ☃)
  {
    this.B = this.m.getWorldChunkManager().getBiomes(this.B, ☃ * 4 - 2, ☃ * 4 - 2, 10, 10);
    a(☃ * 4, 0, ☃ * 4);
    for (int ☃ = 0; ☃ < 4; ☃++)
    {
      int ☃ = ☃ * 5;
      int ☃ = (☃ + 1) * 5;
      for (int ☃ = 0; ☃ < 4; ☃++)
      {
        int ☃ = (☃ + ☃) * 33;
        int ☃ = (☃ + ☃ + 1) * 33;
        int ☃ = (☃ + ☃) * 33;
        int ☃ = (☃ + ☃ + 1) * 33;
        for (int ☃ = 0; ☃ < 32; ☃++)
        {
          double ☃ = 0.125D;
          double ☃ = this.p[(☃ + ☃)];
          double ☃ = this.p[(☃ + ☃)];
          double ☃ = this.p[(☃ + ☃)];
          double ☃ = this.p[(☃ + ☃)];
          
          double ☃ = (this.p[(☃ + ☃ + 1)] - ☃) * ☃;
          double ☃ = (this.p[(☃ + ☃ + 1)] - ☃) * ☃;
          double ☃ = (this.p[(☃ + ☃ + 1)] - ☃) * ☃;
          double ☃ = (this.p[(☃ + ☃ + 1)] - ☃) * ☃;
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
              ☃ -= ☃;
              for (int ☃ = 0; ☃ < 4; ☃++) {
                if (☃ += ☃ > 0.0D) {
                  ☃.a(☃ * 4 + ☃, ☃ * 8 + ☃, ☃ * 4 + ☃, Blocks.STONE.getBlockData());
                } else if (☃ * 8 + ☃ < this.r.q) {
                  ☃.a(☃ * 4 + ☃, ☃ * 8 + ☃, ☃ * 4 + ☃, this.s.getBlockData());
                }
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
  
  private double[] t = new double['Ā'];
  
  public void a(int ☃, int ☃, ChunkSnapshot ☃, BiomeBase[] ☃)
  {
    double ☃ = 0.03125D;
    this.t = this.l.a(this.t, ☃ * 16, ☃ * 16, 16, 16, ☃ * 2.0D, ☃ * 2.0D, 1.0D);
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++)
      {
        BiomeBase ☃ = ☃[(☃ + ☃ * 16)];
        ☃.a(this.m, this.h, ☃, ☃ * 16 + ☃, ☃ * 16 + ☃, this.t[(☃ + ☃ * 16)]);
      }
    }
  }
  
  private WorldGenBase u = new WorldGenCaves();
  private WorldGenStronghold v = new WorldGenStronghold();
  private WorldGenVillage w = new WorldGenVillage();
  private WorldGenMineshaft x = new WorldGenMineshaft();
  private WorldGenLargeFeature y = new WorldGenLargeFeature();
  private WorldGenBase z = new WorldGenCanyon();
  private WorldGenMonument A = new WorldGenMonument();
  private BiomeBase[] B;
  double[] d;
  double[] e;
  double[] f;
  double[] g;
  
  public Chunk getOrCreateChunk(int ☃, int ☃)
  {
    this.h.setSeed(☃ * 341873128712L + ☃ * 132897987541L);
    
    ChunkSnapshot ☃ = new ChunkSnapshot();
    
    a(☃, ☃, ☃);
    this.B = this.m.getWorldChunkManager().getBiomeBlock(this.B, ☃ * 16, ☃ * 16, 16, 16);
    a(☃, ☃, ☃, this.B);
    if (this.r.r) {
      this.u.a(this, this.m, ☃, ☃, ☃);
    }
    if (this.r.z) {
      this.z.a(this, this.m, ☃, ☃, ☃);
    }
    if ((this.r.w) && (this.n)) {
      this.x.a(this, this.m, ☃, ☃, ☃);
    }
    if ((this.r.v) && (this.n)) {
      this.w.a(this, this.m, ☃, ☃, ☃);
    }
    if ((this.r.u) && (this.n)) {
      this.v.a(this, this.m, ☃, ☃, ☃);
    }
    if ((this.r.x) && (this.n)) {
      this.y.a(this, this.m, ☃, ☃, ☃);
    }
    if ((this.r.y) && (this.n)) {
      this.A.a(this, this.m, ☃, ☃, ☃);
    }
    Chunk ☃ = new Chunk(this.m, ☃, ☃, ☃);
    byte[] ☃ = ☃.getBiomeIndex();
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      ☃[☃] = ((byte)this.B[☃].id);
    }
    ☃.initLighting();
    
    return ☃;
  }
  
  private void a(int ☃, int ☃, int ☃)
  {
    this.g = this.b.a(this.g, ☃, ☃, 5, 5, this.r.e, this.r.f, this.r.g);
    
    float ☃ = this.r.a;
    float ☃ = this.r.b;
    this.d = this.k.a(this.d, ☃, ☃, ☃, 5, 33, 5, ☃ / this.r.h, ☃ / this.r.i, ☃ / this.r.j);
    this.e = this.i.a(this.e, ☃, ☃, ☃, 5, 33, 5, ☃, ☃, ☃);
    this.f = this.j.a(this.f, ☃, ☃, ☃, 5, 33, 5, ☃, ☃, ☃);
    ☃ = ☃ = 0;
    
    int ☃ = 0;
    int ☃ = 0;
    for (int ☃ = 0; ☃ < 5; ☃++) {
      for (int ☃ = 0; ☃ < 5; ☃++)
      {
        float ☃ = 0.0F;
        float ☃ = 0.0F;
        float ☃ = 0.0F;
        
        int ☃ = 2;
        
        BiomeBase ☃ = this.B[(☃ + 2 + (☃ + 2) * 10)];
        for (int ☃ = -☃; ☃ <= ☃; ☃++) {
          for (int ☃ = -☃; ☃ <= ☃; ☃++)
          {
            BiomeBase ☃ = this.B[(☃ + ☃ + 2 + (☃ + ☃ + 2) * 10)];
            float ☃ = this.r.n + ☃.an * this.r.m;
            float ☃ = this.r.p + ☃.ao * this.r.o;
            if ((this.o == WorldType.AMPLIFIED) && (☃ > 0.0F))
            {
              ☃ = 1.0F + ☃ * 2.0F;
              ☃ = 1.0F + ☃ * 4.0F;
            }
            float ☃ = this.q[(☃ + 2 + (☃ + 2) * 5)] / (☃ + 2.0F);
            if (☃.an > ☃.an) {
              ☃ /= 2.0F;
            }
            ☃ += ☃ * ☃;
            ☃ += ☃ * ☃;
            ☃ += ☃;
          }
        }
        ☃ /= ☃;
        ☃ /= ☃;
        
        ☃ = ☃ * 0.9F + 0.1F;
        ☃ = (☃ * 4.0F - 1.0F) / 8.0F;
        
        double ☃ = this.g[☃] / 8000.0D;
        if (☃ < 0.0D) {
          ☃ = -☃ * 0.3D;
        }
        ☃ = ☃ * 3.0D - 2.0D;
        if (☃ < 0.0D)
        {
          ☃ /= 2.0D;
          if (☃ < -1.0D) {
            ☃ = -1.0D;
          }
          ☃ /= 1.4D;
          ☃ /= 2.0D;
        }
        else
        {
          if (☃ > 1.0D) {
            ☃ = 1.0D;
          }
          ☃ /= 8.0D;
        }
        ☃++;
        
        double ☃ = ☃;
        double ☃ = ☃;
        ☃ += ☃ * 0.2D;
        ☃ = ☃ * this.r.k / 8.0D;
        
        double ☃ = this.r.k + ☃ * 4.0D;
        for (int ☃ = 0; ☃ < 33; ☃++)
        {
          double ☃ = (☃ - ☃) * this.r.l * 128.0D / 256.0D / ☃;
          if (☃ < 0.0D) {
            ☃ *= 4.0D;
          }
          double ☃ = this.e[☃] / this.r.d;
          double ☃ = this.f[☃] / this.r.c;
          
          double ☃ = (this.d[☃] / 10.0D + 1.0D) / 2.0D;
          double ☃ = MathHelper.b(☃, ☃, ☃) - ☃;
          if (☃ > 29)
          {
            double ☃ = (☃ - 29) / 3.0F;
            ☃ = ☃ * (1.0D - ☃) + -10.0D * ☃;
          }
          this.p[☃] = ☃;
          ☃++;
        }
      }
    }
  }
  
  public boolean isChunkLoaded(int ☃, int ☃)
  {
    return true;
  }
  
  public void getChunkAt(IChunkProvider ☃, int ☃, int ☃)
  {
    BlockFalling.instaFall = true;
    int ☃ = ☃ * 16;
    int ☃ = ☃ * 16;
    BlockPosition ☃ = new BlockPosition(☃, 0, ☃);
    BiomeBase ☃ = this.m.getBiome(☃.a(16, 0, 16));
    
    this.h.setSeed(this.m.getSeed());
    long ☃ = this.h.nextLong() / 2L * 2L + 1L;
    long ☃ = this.h.nextLong() / 2L * 2L + 1L;
    this.h.setSeed(☃ * ☃ + ☃ * ☃ ^ this.m.getSeed());
    
    boolean ☃ = false;
    
    ChunkCoordIntPair ☃ = new ChunkCoordIntPair(☃, ☃);
    if ((this.r.w) && (this.n)) {
      this.x.a(this.m, this.h, ☃);
    }
    if ((this.r.v) && (this.n)) {
      ☃ = this.w.a(this.m, this.h, ☃);
    }
    if ((this.r.u) && (this.n)) {
      this.v.a(this.m, this.h, ☃);
    }
    if ((this.r.x) && (this.n)) {
      this.y.a(this.m, this.h, ☃);
    }
    if ((this.r.y) && (this.n)) {
      this.A.a(this.m, this.h, ☃);
    }
    if ((☃ != BiomeBase.DESERT) && (☃ != BiomeBase.DESERT_HILLS) && (this.r.A) && 
      (!☃) && (this.h.nextInt(this.r.B) == 0))
    {
      int ☃ = this.h.nextInt(16) + 8;
      int ☃ = this.h.nextInt(256);
      int ☃ = this.h.nextInt(16) + 8;
      new WorldGenLakes(Blocks.WATER).generate(this.m, this.h, ☃.a(☃, ☃, ☃));
    }
    if ((!☃) && (this.h.nextInt(this.r.D / 10) == 0) && (this.r.C))
    {
      int ☃ = this.h.nextInt(16) + 8;
      int ☃ = this.h.nextInt(this.h.nextInt(248) + 8);
      int ☃ = this.h.nextInt(16) + 8;
      if ((☃ < this.m.F()) || (this.h.nextInt(this.r.D / 8) == 0)) {
        new WorldGenLakes(Blocks.LAVA).generate(this.m, this.h, ☃.a(☃, ☃, ☃));
      }
    }
    if (this.r.s) {
      for (int ☃ = 0; ☃ < this.r.t; ☃++)
      {
        int ☃ = this.h.nextInt(16) + 8;
        int ☃ = this.h.nextInt(256);
        int ☃ = this.h.nextInt(16) + 8;
        new WorldGenDungeons().generate(this.m, this.h, ☃.a(☃, ☃, ☃));
      }
    }
    ☃.a(this.m, this.h, new BlockPosition(☃, 0, ☃));
    
    SpawnerCreature.a(this.m, ☃, ☃ + 8, ☃ + 8, 16, 16, this.h);
    
    ☃ = ☃.a(8, 0, 8);
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++)
      {
        BlockPosition ☃ = this.m.q(☃.a(☃, 0, ☃));
        BlockPosition ☃ = ☃.down();
        if (this.m.v(☃)) {
          this.m.setTypeAndData(☃, Blocks.ICE.getBlockData(), 2);
        }
        if (this.m.f(☃, true)) {
          this.m.setTypeAndData(☃, Blocks.SNOW_LAYER.getBlockData(), 2);
        }
      }
    }
    BlockFalling.instaFall = false;
  }
  
  public boolean a(IChunkProvider ☃, Chunk ☃, int ☃, int ☃)
  {
    boolean ☃ = false;
    if ((this.r.y) && (this.n)) {
      if (☃.w() < 3600L) {
        ☃ |= this.A.a(this.m, this.h, new ChunkCoordIntPair(☃, ☃));
      }
    }
    return ☃;
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
    BiomeBase ☃ = this.m.getBiome(☃);
    if (this.n)
    {
      if ((☃ == EnumCreatureType.MONSTER) && (this.y.a(☃))) {
        return this.y.b();
      }
      if ((☃ == EnumCreatureType.MONSTER) && (this.r.y) && (this.A.a(this.m, ☃))) {
        return this.A.b();
      }
    }
    return ☃.getMobs(☃);
  }
  
  public BlockPosition findNearestMapFeature(World ☃, String ☃, BlockPosition ☃)
  {
    if (("Stronghold".equals(☃)) && (this.v != null)) {
      return this.v.getNearestGeneratedFeature(☃, ☃);
    }
    return null;
  }
  
  public int getLoadedChunks()
  {
    return 0;
  }
  
  public void recreateStructures(Chunk ☃, int ☃, int ☃)
  {
    if ((this.r.w) && (this.n)) {
      this.x.a(this, this.m, ☃, ☃, null);
    }
    if ((this.r.v) && (this.n)) {
      this.w.a(this, this.m, ☃, ☃, null);
    }
    if ((this.r.u) && (this.n)) {
      this.v.a(this, this.m, ☃, ☃, null);
    }
    if ((this.r.x) && (this.n)) {
      this.y.a(this, this.m, ☃, ☃, null);
    }
    if ((this.r.y) && (this.n)) {
      this.A.a(this, this.m, ☃, ☃, null);
    }
  }
  
  public Chunk getChunkAt(BlockPosition ☃)
  {
    return getOrCreateChunk(☃.getX() >> 4, ☃.getZ() >> 4);
  }
}
