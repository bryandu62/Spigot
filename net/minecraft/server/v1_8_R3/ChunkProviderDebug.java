package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;

public class ChunkProviderDebug
  implements IChunkProvider
{
  private static final List<IBlockData> a = ;
  
  static
  {
    for (Block ☃ : Block.REGISTRY) {
      a.addAll(☃.P().a());
    }
  }
  
  private static final int b = MathHelper.f(MathHelper.c(a.size()));
  private static final int c = MathHelper.f(a.size() / b);
  private final World d;
  
  public ChunkProviderDebug(World ☃)
  {
    this.d = ☃;
  }
  
  public Chunk getOrCreateChunk(int ☃, int ☃)
  {
    ChunkSnapshot ☃ = new ChunkSnapshot();
    for (int ☃ = 0; ☃ < 16; ☃++) {
      for (int ☃ = 0; ☃ < 16; ☃++)
      {
        int ☃ = ☃ * 16 + ☃;
        int ☃ = ☃ * 16 + ☃;
        
        ☃.a(☃, 60, ☃, Blocks.BARRIER.getBlockData());
        
        IBlockData ☃ = b(☃, ☃);
        if (☃ != null) {
          ☃.a(☃, 70, ☃, ☃);
        }
      }
    }
    Chunk ☃ = new Chunk(this.d, ☃, ☃, ☃);
    ☃.initLighting();
    
    BiomeBase[] ☃ = this.d.getWorldChunkManager().getBiomeBlock(null, ☃ * 16, ☃ * 16, 16, 16);
    byte[] ☃ = ☃.getBiomeIndex();
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      ☃[☃] = ((byte)☃[☃].id);
    }
    ☃.initLighting();
    
    return ☃;
  }
  
  public static IBlockData b(int ☃, int ☃)
  {
    IBlockData ☃ = null;
    if ((☃ > 0) && (☃ > 0) && (☃ % 2 != 0) && (☃ % 2 != 0))
    {
      ☃ /= 2;
      ☃ /= 2;
      if ((☃ <= b) && (☃ <= c))
      {
        int ☃ = MathHelper.a(☃ * b + ☃);
        if (☃ < a.size()) {
          ☃ = (IBlockData)a.get(☃);
        }
      }
    }
    return ☃;
  }
  
  public boolean isChunkLoaded(int ☃, int ☃)
  {
    return true;
  }
  
  public boolean a(IChunkProvider ☃, Chunk ☃, int ☃, int ☃)
  {
    return false;
  }
  
  public boolean saveChunks(boolean ☃, IProgressUpdate ☃)
  {
    return true;
  }
  
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
    return "DebugLevelSource";
  }
  
  public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType ☃, BlockPosition ☃)
  {
    BiomeBase ☃ = this.d.getBiome(☃);
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
  
  public Chunk getChunkAt(BlockPosition ☃)
  {
    return getOrCreateChunk(☃.getX() >> 4, ☃.getZ() >> 4);
  }
  
  public void getChunkAt(IChunkProvider ☃, int ☃, int ☃) {}
  
  public void c() {}
  
  public void recreateStructures(Chunk ☃, int ☃, int ☃) {}
}
