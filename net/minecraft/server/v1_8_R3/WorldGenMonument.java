package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class WorldGenMonument
  extends StructureGenerator
{
  private int f = 32;
  private int g = 5;
  public static final List<BiomeBase> d = Arrays.asList(new BiomeBase[] { BiomeBase.OCEAN, BiomeBase.DEEP_OCEAN, BiomeBase.RIVER, BiomeBase.FROZEN_OCEAN, BiomeBase.FROZEN_RIVER });
  private static final List<BiomeBase.BiomeMeta> h = Lists.newArrayList();
  
  static
  {
    h.add(new BiomeBase.BiomeMeta(EntityGuardian.class, 1, 2, 4));
  }
  
  public WorldGenMonument(Map<String, String> ☃)
  {
    this();
    for (Map.Entry<String, String> ☃ : ☃.entrySet()) {
      if (((String)☃.getKey()).equals("spacing")) {
        this.f = MathHelper.a((String)☃.getValue(), this.f, 1);
      } else if (((String)☃.getKey()).equals("separation")) {
        this.g = MathHelper.a((String)☃.getValue(), this.g, 1);
      }
    }
  }
  
  public String a()
  {
    return "Monument";
  }
  
  protected boolean a(int ☃, int ☃)
  {
    int ☃ = ☃;
    int ☃ = ☃;
    if (☃ < 0) {
      ☃ -= this.f - 1;
    }
    if (☃ < 0) {
      ☃ -= this.f - 1;
    }
    int ☃ = ☃ / this.f;
    int ☃ = ☃ / this.f;
    
    Random ☃ = this.c.a(☃, ☃, 10387313);
    ☃ *= this.f;
    ☃ *= this.f;
    ☃ += (☃.nextInt(this.f - this.g) + ☃.nextInt(this.f - this.g)) / 2;
    ☃ += (☃.nextInt(this.f - this.g) + ☃.nextInt(this.f - this.g)) / 2;
    
    ☃ = ☃;
    ☃ = ☃;
    if ((☃ == ☃) && (☃ == ☃))
    {
      if (this.c.getWorldChunkManager().getBiome(new BlockPosition(☃ * 16 + 8, 64, ☃ * 16 + 8), null) != BiomeBase.DEEP_OCEAN) {
        return false;
      }
      boolean ☃ = this.c.getWorldChunkManager().a(☃ * 16 + 8, ☃ * 16 + 8, 29, d);
      if (☃) {
        return true;
      }
    }
    return false;
  }
  
  protected StructureStart b(int ☃, int ☃)
  {
    return new WorldGenMonumentStart(this.c, this.b, ☃, ☃);
  }
  
  public static class WorldGenMonumentStart
    extends StructureStart
  {
    private Set<ChunkCoordIntPair> c = Sets.newHashSet();
    private boolean d;
    
    public WorldGenMonumentStart() {}
    
    public WorldGenMonumentStart(World ☃, Random ☃, int ☃, int ☃)
    {
      super(☃);
      b(☃, ☃, ☃, ☃);
    }
    
    private void b(World ☃, Random ☃, int ☃, int ☃)
    {
      ☃.setSeed(☃.getSeed());
      long ☃ = ☃.nextLong();
      long ☃ = ☃.nextLong();
      long ☃ = ☃ * ☃;
      long ☃ = ☃ * ☃;
      ☃.setSeed(☃ ^ ☃ ^ ☃.getSeed());
      
      int ☃ = ☃ * 16 + 8 - 29;
      int ☃ = ☃ * 16 + 8 - 29;
      EnumDirection ☃ = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(☃);
      
      this.a.add(new WorldGenMonumentPieces.WorldGenMonumentPiece1(☃, ☃, ☃, ☃));
      c();
      
      this.d = true;
    }
    
    public void a(World ☃, Random ☃, StructureBoundingBox ☃)
    {
      if (!this.d)
      {
        this.a.clear();
        b(☃, ☃, e(), f());
      }
      super.a(☃, ☃, ☃);
    }
    
    public boolean a(ChunkCoordIntPair ☃)
    {
      if (this.c.contains(☃)) {
        return false;
      }
      return super.a(☃);
    }
    
    public void b(ChunkCoordIntPair ☃)
    {
      super.b(☃);
      
      this.c.add(☃);
    }
    
    public void a(NBTTagCompound ☃)
    {
      super.a(☃);
      
      NBTTagList ☃ = new NBTTagList();
      for (ChunkCoordIntPair ☃ : this.c)
      {
        NBTTagCompound ☃ = new NBTTagCompound();
        ☃.setInt("X", ☃.x);
        ☃.setInt("Z", ☃.z);
        ☃.add(☃);
      }
      ☃.set("Processed", ☃);
    }
    
    public void b(NBTTagCompound ☃)
    {
      super.b(☃);
      if (☃.hasKeyOfType("Processed", 9))
      {
        NBTTagList ☃ = ☃.getList("Processed", 10);
        for (int ☃ = 0; ☃ < ☃.size(); ☃++)
        {
          NBTTagCompound ☃ = ☃.get(☃);
          this.c.add(new ChunkCoordIntPair(☃.getInt("X"), ☃.getInt("Z")));
        }
      }
    }
  }
  
  public List<BiomeBase.BiomeMeta> b()
  {
    return h;
  }
  
  public WorldGenMonument() {}
}
