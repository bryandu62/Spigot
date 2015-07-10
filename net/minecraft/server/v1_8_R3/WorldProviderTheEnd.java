package net.minecraft.server.v1_8_R3;

public class WorldProviderTheEnd
  extends WorldProvider
{
  public void b()
  {
    this.c = new WorldChunkManagerHell(BiomeBase.SKY, 0.0F);
    this.dimension = 1;
    this.e = true;
  }
  
  public IChunkProvider getChunkProvider()
  {
    return new ChunkProviderTheEnd(this.b, this.b.getSeed());
  }
  
  public float a(long ☃, float ☃)
  {
    return 0.0F;
  }
  
  public boolean e()
  {
    return false;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean canSpawn(int ☃, int ☃)
  {
    return this.b.c(new BlockPosition(☃, 0, ☃)).getMaterial().isSolid();
  }
  
  public BlockPosition h()
  {
    return new BlockPosition(100, 50, 0);
  }
  
  public int getSeaLevel()
  {
    return 50;
  }
  
  public String getName()
  {
    return "The End";
  }
  
  public String getSuffix()
  {
    return "_end";
  }
}
