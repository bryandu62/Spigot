package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class WorldGenStronghold
  extends StructureGenerator
{
  private List<BiomeBase> d;
  private boolean f;
  private ChunkCoordIntPair[] g = new ChunkCoordIntPair[3];
  private double h = 32.0D;
  private int i = 3;
  
  public WorldGenStronghold()
  {
    this.d = Lists.newArrayList();
    for (BiomeBase ☃ : BiomeBase.getBiomes()) {
      if ((☃ != null) && (☃.an > 0.0F)) {
        this.d.add(☃);
      }
    }
  }
  
  public WorldGenStronghold(Map<String, String> ☃)
  {
    this();
    for (Map.Entry<String, String> ☃ : ☃.entrySet()) {
      if (((String)☃.getKey()).equals("distance")) {
        this.h = MathHelper.a((String)☃.getValue(), this.h, 1.0D);
      } else if (((String)☃.getKey()).equals("count")) {
        this.g = new ChunkCoordIntPair[MathHelper.a((String)☃.getValue(), this.g.length, 1)];
      } else if (((String)☃.getKey()).equals("spread")) {
        this.i = MathHelper.a((String)☃.getValue(), this.i, 1);
      }
    }
  }
  
  public String a()
  {
    return "Stronghold";
  }
  
  protected boolean a(int ☃, int ☃)
  {
    if (!this.f)
    {
      Random ☃ = new Random();
      
      ☃.setSeed(this.c.getSeed());
      
      double ☃ = ☃.nextDouble() * 3.141592653589793D * 2.0D;
      int ☃ = 1;
      for (int ☃ = 0; ☃ < this.g.length; ☃++)
      {
        double ☃ = (1.25D * ☃ + ☃.nextDouble()) * (this.h * ☃);
        int ☃ = (int)Math.round(Math.cos(☃) * ☃);
        int ☃ = (int)Math.round(Math.sin(☃) * ☃);
        
        BlockPosition ☃ = this.c.getWorldChunkManager().a((☃ << 4) + 8, (☃ << 4) + 8, 112, this.d, ☃);
        if (☃ != null)
        {
          ☃ = ☃.getX() >> 4;
          ☃ = ☃.getZ() >> 4;
        }
        this.g[☃] = new ChunkCoordIntPair(☃, ☃);
        
        ☃ += 6.283185307179586D * ☃ / this.i;
        if (☃ == this.i)
        {
          ☃ += 2 + ☃.nextInt(5);
          this.i += 1 + ☃.nextInt(2);
        }
      }
      this.f = true;
    }
    for (ChunkCoordIntPair ☃ : this.g) {
      if ((☃ == ☃.x) && (☃ == ☃.z)) {
        return true;
      }
    }
    return false;
  }
  
  protected List<BlockPosition> z_()
  {
    List<BlockPosition> ☃ = Lists.newArrayList();
    for (ChunkCoordIntPair ☃ : this.g) {
      if (☃ != null) {
        ☃.add(☃.a(64));
      }
    }
    return ☃;
  }
  
  protected StructureStart b(int ☃, int ☃)
  {
    WorldGenStronghold2Start ☃ = new WorldGenStronghold2Start(this.c, this.b, ☃, ☃);
    while ((☃.b().isEmpty()) || (((WorldGenStrongholdPieces.WorldGenStrongholdStart)☃.b().get(0)).b == null)) {
      ☃ = new WorldGenStronghold2Start(this.c, this.b, ☃, ☃);
    }
    return ☃;
  }
  
  public static class WorldGenStronghold2Start
    extends StructureStart
  {
    public WorldGenStronghold2Start() {}
    
    public WorldGenStronghold2Start(World ☃, Random ☃, int ☃, int ☃)
    {
      super(☃);
      
      WorldGenStrongholdPieces.b();
      
      WorldGenStrongholdPieces.WorldGenStrongholdStart ☃ = new WorldGenStrongholdPieces.WorldGenStrongholdStart(0, ☃, (☃ << 4) + 2, (☃ << 4) + 2);
      this.a.add(☃);
      ☃.a(☃, this.a, ☃);
      
      List<StructurePiece> ☃ = ☃.c;
      while (!☃.isEmpty())
      {
        int ☃ = ☃.nextInt(☃.size());
        StructurePiece ☃ = (StructurePiece)☃.remove(☃);
        ☃.a(☃, this.a, ☃);
      }
      c();
      a(☃, ☃, 10);
    }
  }
}
