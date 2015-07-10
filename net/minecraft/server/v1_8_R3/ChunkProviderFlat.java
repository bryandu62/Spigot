package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChunkProviderFlat
  implements IChunkProvider
{
  private World a;
  private Random b;
  private final IBlockData[] c = new IBlockData['Ā'];
  private final WorldGenFlatInfo d;
  private final List<StructureGenerator> e = Lists.newArrayList();
  private final boolean f;
  private final boolean g;
  private WorldGenLakes h;
  private WorldGenLakes i;
  
  public ChunkProviderFlat(World ☃, long ☃, boolean ☃, String ☃)
  {
    this.a = ☃;
    this.b = new Random(☃);
    this.d = WorldGenFlatInfo.a(☃);
    if (☃)
    {
      Map<String, Map<String, String>> ☃ = this.d.b();
      if (☃.containsKey("village"))
      {
        Map<String, String> ☃ = (Map)☃.get("village");
        if (!☃.containsKey("size")) {
          ☃.put("size", "1");
        }
        this.e.add(new WorldGenVillage(☃));
      }
      if (☃.containsKey("biome_1")) {
        this.e.add(new WorldGenLargeFeature((Map)☃.get("biome_1")));
      }
      if (☃.containsKey("mineshaft")) {
        this.e.add(new WorldGenMineshaft((Map)☃.get("mineshaft")));
      }
      if (☃.containsKey("stronghold")) {
        this.e.add(new WorldGenStronghold((Map)☃.get("stronghold")));
      }
      if (☃.containsKey("oceanmonument")) {
        this.e.add(new WorldGenMonument((Map)☃.get("oceanmonument")));
      }
    }
    if (this.d.b().containsKey("lake")) {
      this.h = new WorldGenLakes(Blocks.WATER);
    }
    if (this.d.b().containsKey("lava_lake")) {
      this.i = new WorldGenLakes(Blocks.LAVA);
    }
    this.g = this.d.b().containsKey("dungeon");
    
    int ☃ = 0;
    int ☃ = 0;
    boolean ☃ = true;
    for (WorldGenFlatLayerInfo ☃ : this.d.c())
    {
      for (int ☃ = ☃.d(); ☃ < ☃.d() + ☃.b(); ☃++)
      {
        IBlockData ☃ = ☃.c();
        if (☃.getBlock() != Blocks.AIR)
        {
          ☃ = false;
          this.c[☃] = ☃;
        }
      }
      if (☃.c().getBlock() == Blocks.AIR)
      {
        ☃ += ☃.b();
      }
      else
      {
        ☃ += ☃.b() + ☃;
        ☃ = 0;
      }
    }
    ☃.b(☃);
    
    this.f = (☃ ? false : this.d.b().containsKey("decoration"));
  }
  
  public Chunk getOrCreateChunk(int ☃, int ☃)
  {
    ChunkSnapshot ☃ = new ChunkSnapshot();
    for (int ☃ = 0; ☃ < this.c.length; ☃++)
    {
      IBlockData ☃ = this.c[☃];
      if (☃ != null) {
        for (int ☃ = 0; ☃ < 16; ☃++) {
          for (int ☃ = 0; ☃ < 16; ☃++) {
            ☃.a(☃, ☃, ☃, ☃);
          }
        }
      }
    }
    for (WorldGenBase ☃ : this.e) {
      ☃.a(this, this.a, ☃, ☃, ☃);
    }
    Chunk ☃ = new Chunk(this.a, ☃, ☃, ☃);
    BiomeBase[] ☃ = this.a.getWorldChunkManager().getBiomeBlock(null, ☃ * 16, ☃ * 16, 16, 16);
    byte[] ☃ = ☃.getBiomeIndex();
    for (int ☃ = 0; ☃ < ☃.length; ☃++) {
      ☃[☃] = ((byte)☃[☃].id);
    }
    ☃.initLighting();
    
    return ☃;
  }
  
  public boolean isChunkLoaded(int ☃, int ☃)
  {
    return true;
  }
  
  public void getChunkAt(IChunkProvider ☃, int ☃, int ☃)
  {
    int ☃ = ☃ * 16;
    int ☃ = ☃ * 16;
    BlockPosition ☃ = new BlockPosition(☃, 0, ☃);
    BiomeBase ☃ = this.a.getBiome(new BlockPosition(☃ + 16, 0, ☃ + 16));
    boolean ☃ = false;
    
    this.b.setSeed(this.a.getSeed());
    long ☃ = this.b.nextLong() / 2L * 2L + 1L;
    long ☃ = this.b.nextLong() / 2L * 2L + 1L;
    this.b.setSeed(☃ * ☃ + ☃ * ☃ ^ this.a.getSeed());
    
    ChunkCoordIntPair ☃ = new ChunkCoordIntPair(☃, ☃);
    for (StructureGenerator ☃ : this.e)
    {
      boolean ☃ = ☃.a(this.a, this.b, ☃);
      if ((☃ instanceof WorldGenVillage)) {
        ☃ |= ☃;
      }
    }
    if ((this.h != null) && (!☃) && (this.b.nextInt(4) == 0)) {
      this.h.generate(this.a, this.b, ☃.a(this.b.nextInt(16) + 8, this.b.nextInt(256), this.b.nextInt(16) + 8));
    }
    if ((this.i != null) && (!☃) && (this.b.nextInt(8) == 0))
    {
      BlockPosition ☃ = ☃.a(this.b.nextInt(16) + 8, this.b.nextInt(this.b.nextInt(248) + 8), this.b.nextInt(16) + 8);
      if ((☃.getY() < this.a.F()) || (this.b.nextInt(10) == 0)) {
        this.i.generate(this.a, this.b, ☃);
      }
    }
    if (this.g) {
      for (int ☃ = 0; ☃ < 8; ☃++) {
        new WorldGenDungeons().generate(this.a, this.b, ☃.a(this.b.nextInt(16) + 8, this.b.nextInt(256), this.b.nextInt(16) + 8));
      }
    }
    if (this.f) {
      ☃.a(this.a, this.b, ☃);
    }
  }
  
  public boolean a(IChunkProvider ☃, Chunk ☃, int ☃, int ☃)
  {
    return false;
  }
  
  public boolean saveChunks(boolean ☃, IProgressUpdate ☃)
  {
    return true;
  }
  
  public void c() {}
  
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
    return "FlatLevelSource";
  }
  
  public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType ☃, BlockPosition ☃)
  {
    BiomeBase ☃ = this.a.getBiome(☃);
    return ☃.getMobs(☃);
  }
  
  public BlockPosition findNearestMapFeature(World ☃, String ☃, BlockPosition ☃)
  {
    if ("Stronghold".equals(☃)) {
      for (StructureGenerator ☃ : this.e) {
        if ((☃ instanceof WorldGenStronghold)) {
          return ☃.getNearestGeneratedFeature(☃, ☃);
        }
      }
    }
    return null;
  }
  
  public int getLoadedChunks()
  {
    return 0;
  }
  
  public void recreateStructures(Chunk ☃, int ☃, int ☃)
  {
    for (StructureGenerator ☃ : this.e) {
      ☃.a(this, this.a, ☃, ☃, null);
    }
  }
  
  public Chunk getChunkAt(BlockPosition ☃)
  {
    return getOrCreateChunk(☃.getX() >> 4, ☃.getZ() >> 4);
  }
}
