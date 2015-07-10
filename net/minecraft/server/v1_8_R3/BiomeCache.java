package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class BiomeCache
{
  private final WorldChunkManager a;
  private long b;
  
  public class BiomeCacheBlock
  {
    public float[] a = new float['Ā'];
    public BiomeBase[] b = new BiomeBase['Ā'];
    public int c;
    public int d;
    public long e;
    
    public BiomeCacheBlock(int ☃, int ☃)
    {
      this.c = ☃;
      this.d = ☃;
      BiomeCache.a(BiomeCache.this).getWetness(this.a, ☃ << 4, ☃ << 4, 16, 16);
      BiomeCache.a(BiomeCache.this).a(this.b, ☃ << 4, ☃ << 4, 16, 16, false);
    }
    
    public BiomeBase a(int ☃, int ☃)
    {
      return this.b[(☃ & 0xF | (☃ & 0xF) << 4)];
    }
  }
  
  private LongHashMap<BiomeCacheBlock> c = new LongHashMap();
  private List<BiomeCacheBlock> d = Lists.newArrayList();
  
  public BiomeCache(WorldChunkManager ☃)
  {
    this.a = ☃;
  }
  
  public BiomeCacheBlock a(int ☃, int ☃)
  {
    ☃ >>= 4;
    ☃ >>= 4;
    long ☃ = ☃ & 0xFFFFFFFF | (☃ & 0xFFFFFFFF) << 32;
    BiomeCacheBlock ☃ = (BiomeCacheBlock)this.c.getEntry(☃);
    if (☃ == null)
    {
      ☃ = new BiomeCacheBlock(☃, ☃);
      this.c.put(☃, ☃);
      this.d.add(☃);
    }
    ☃.e = MinecraftServer.az();
    return ☃;
  }
  
  public BiomeBase a(int ☃, int ☃, BiomeBase ☃)
  {
    BiomeBase ☃ = a(☃, ☃).a(☃, ☃);
    if (☃ == null) {
      return ☃;
    }
    return ☃;
  }
  
  public void a()
  {
    long ☃ = MinecraftServer.az();
    long ☃ = ☃ - this.b;
    if ((☃ > 7500L) || (☃ < 0L))
    {
      this.b = ☃;
      for (int ☃ = 0; ☃ < this.d.size(); ☃++)
      {
        BiomeCacheBlock ☃ = (BiomeCacheBlock)this.d.get(☃);
        long ☃ = ☃ - ☃.e;
        if ((☃ > 30000L) || (☃ < 0L))
        {
          this.d.remove(☃--);
          long ☃ = ☃.c & 0xFFFFFFFF | (☃.d & 0xFFFFFFFF) << 32;
          this.c.remove(☃);
        }
      }
    }
  }
  
  public BiomeBase[] c(int ☃, int ☃)
  {
    return a(☃, ☃).b;
  }
}
