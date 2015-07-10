package net.minecraft.server.v1_8_R3;

public class WorldProviderHell
  extends WorldProvider
{
  public void b()
  {
    this.c = new WorldChunkManagerHell(BiomeBase.HELL, 0.0F);
    this.d = true;
    this.e = true;
    this.dimension = -1;
  }
  
  protected void a()
  {
    float ☃ = 0.1F;
    for (int ☃ = 0; ☃ <= 15; ☃++)
    {
      float ☃ = 1.0F - ☃ / 15.0F;
      this.f[☃] = ((1.0F - ☃) / (☃ * 3.0F + 1.0F) * (1.0F - ☃) + ☃);
    }
  }
  
  public IChunkProvider getChunkProvider()
  {
    return new ChunkProviderHell(this.b, this.b.getWorldData().shouldGenerateMapFeatures(), this.b.getSeed());
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canSpawn(int ☃, int ☃)
  {
    return false;
  }
  
  public float a(long ☃, float ☃)
  {
    return 0.5F;
  }
  
  public boolean e()
  {
    return false;
  }
  
  public String getName()
  {
    return "Nether";
  }
  
  public String getSuffix()
  {
    return "_nether";
  }
  
  public WorldBorder getWorldBorder()
  {
    new WorldBorder()
    {
      public double getCenterX()
      {
        return super.getCenterX() / 8.0D;
      }
      
      public double getCenterZ()
      {
        return super.getCenterZ() / 8.0D;
      }
    };
  }
}
