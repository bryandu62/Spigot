package net.minecraft.server.v1_8_R3;

import java.io.IOException;

public abstract interface IChunkLoader
{
  public abstract Chunk a(World paramWorld, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract void a(World paramWorld, Chunk paramChunk)
    throws IOException, ExceptionWorldConflict;
  
  public abstract void b(World paramWorld, Chunk paramChunk)
    throws IOException;
  
  public abstract void a();
  
  public abstract void b();
}
