package net.minecraft.server.v1_8_R3;

import java.util.List;

public abstract interface IChunkProvider
{
  public abstract boolean isChunkLoaded(int paramInt1, int paramInt2);
  
  public abstract Chunk getOrCreateChunk(int paramInt1, int paramInt2);
  
  public abstract Chunk getChunkAt(BlockPosition paramBlockPosition);
  
  public abstract void getChunkAt(IChunkProvider paramIChunkProvider, int paramInt1, int paramInt2);
  
  public abstract boolean a(IChunkProvider paramIChunkProvider, Chunk paramChunk, int paramInt1, int paramInt2);
  
  public abstract boolean saveChunks(boolean paramBoolean, IProgressUpdate paramIProgressUpdate);
  
  public abstract boolean unloadChunks();
  
  public abstract boolean canSave();
  
  public abstract String getName();
  
  public abstract List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType paramEnumCreatureType, BlockPosition paramBlockPosition);
  
  public abstract BlockPosition findNearestMapFeature(World paramWorld, String paramString, BlockPosition paramBlockPosition);
  
  public abstract int getLoadedChunks();
  
  public abstract void recreateStructures(Chunk paramChunk, int paramInt1, int paramInt2);
  
  public abstract void c();
}
