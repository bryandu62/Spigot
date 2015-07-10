package net.minecraft.server.v1_8_R3;

public abstract class WorldProvider
{
  public static final float[] a = { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
  protected World b;
  private WorldType type;
  private String i;
  protected WorldChunkManager c;
  protected boolean d;
  protected boolean e;
  protected final float[] f = new float[16];
  protected int dimension;
  private final float[] j = new float[4];
  
  public final void a(World ☃)
  {
    this.b = ☃;
    this.type = ☃.getWorldData().getType();
    this.i = ☃.getWorldData().getGeneratorOptions();
    b();
    a();
  }
  
  protected void a()
  {
    float ☃ = 0.0F;
    for (int ☃ = 0; ☃ <= 15; ☃++)
    {
      float ☃ = 1.0F - ☃ / 15.0F;
      this.f[☃] = ((1.0F - ☃) / (☃ * 3.0F + 1.0F) * (1.0F - ☃) + ☃);
    }
  }
  
  protected void b()
  {
    WorldType ☃ = this.b.getWorldData().getType();
    if (☃ == WorldType.FLAT)
    {
      WorldGenFlatInfo ☃ = WorldGenFlatInfo.a(this.b.getWorldData().getGeneratorOptions());
      this.c = new WorldChunkManagerHell(BiomeBase.getBiome(☃.a(), BiomeBase.ad), 0.5F);
    }
    else if (☃ == WorldType.DEBUG_ALL_BLOCK_STATES)
    {
      this.c = new WorldChunkManagerHell(BiomeBase.PLAINS, 0.0F);
    }
    else
    {
      this.c = new WorldChunkManager(this.b);
    }
  }
  
  public IChunkProvider getChunkProvider()
  {
    if (this.type == WorldType.FLAT) {
      return new ChunkProviderFlat(this.b, this.b.getSeed(), this.b.getWorldData().shouldGenerateMapFeatures(), this.i);
    }
    if (this.type == WorldType.DEBUG_ALL_BLOCK_STATES) {
      return new ChunkProviderDebug(this.b);
    }
    if (this.type == WorldType.CUSTOMIZED) {
      return new ChunkProviderGenerate(this.b, this.b.getSeed(), this.b.getWorldData().shouldGenerateMapFeatures(), this.i);
    }
    return new ChunkProviderGenerate(this.b, this.b.getSeed(), this.b.getWorldData().shouldGenerateMapFeatures(), this.i);
  }
  
  public boolean canSpawn(int ☃, int ☃)
  {
    return this.b.c(new BlockPosition(☃, 0, ☃)) == Blocks.GRASS;
  }
  
  public float a(long ☃, float ☃)
  {
    int ☃ = (int)(☃ % 24000L);
    float ☃ = (☃ + ☃) / 24000.0F - 0.25F;
    if (☃ < 0.0F) {
      ☃ += 1.0F;
    }
    if (☃ > 1.0F) {
      ☃ -= 1.0F;
    }
    float ☃ = ☃;
    ☃ = 1.0F - (float)((Math.cos(☃ * 3.141592653589793D) + 1.0D) / 2.0D);
    ☃ = ☃ + (☃ - ☃) / 3.0F;
    return ☃;
  }
  
  public int a(long ☃)
  {
    return (int)(☃ / 24000L % 8L + 8L) % 8;
  }
  
  public boolean d()
  {
    return true;
  }
  
  public boolean e()
  {
    return true;
  }
  
  public static WorldProvider byDimension(int ☃)
  {
    if (☃ == -1) {
      return new WorldProviderHell();
    }
    if (☃ == 0) {
      return new WorldProviderNormal();
    }
    if (☃ == 1) {
      return new WorldProviderTheEnd();
    }
    return null;
  }
  
  public BlockPosition h()
  {
    return null;
  }
  
  public int getSeaLevel()
  {
    if (this.type == WorldType.FLAT) {
      return 4;
    }
    return this.b.F() + 1;
  }
  
  public abstract String getName();
  
  public abstract String getSuffix();
  
  public WorldChunkManager m()
  {
    return this.c;
  }
  
  public boolean n()
  {
    return this.d;
  }
  
  public boolean o()
  {
    return this.e;
  }
  
  public float[] p()
  {
    return this.f;
  }
  
  public int getDimension()
  {
    return this.dimension;
  }
  
  public WorldBorder getWorldBorder()
  {
    return new WorldBorder();
  }
}
