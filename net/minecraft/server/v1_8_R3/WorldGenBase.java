package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class WorldGenBase
{
  protected int a = 8;
  protected Random b = new Random();
  protected World c;
  
  public void a(IChunkProvider ☃, World ☃, int ☃, int ☃, ChunkSnapshot ☃)
  {
    int ☃ = this.a;
    this.c = ☃;
    
    this.b.setSeed(☃.getSeed());
    long ☃ = this.b.nextLong();
    long ☃ = this.b.nextLong();
    for (int ☃ = ☃ - ☃; ☃ <= ☃ + ☃; ☃++) {
      for (int ☃ = ☃ - ☃; ☃ <= ☃ + ☃; ☃++)
      {
        long ☃ = ☃ * ☃;
        long ☃ = ☃ * ☃;
        this.b.setSeed(☃ ^ ☃ ^ ☃.getSeed());
        a(☃, ☃, ☃, ☃, ☃, ☃);
      }
    }
  }
  
  protected void a(World ☃, int ☃, int ☃, int ☃, int ☃, ChunkSnapshot ☃) {}
}
