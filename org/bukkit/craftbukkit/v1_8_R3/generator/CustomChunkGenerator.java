package org.bukkit.craftbukkit.v1_8_R3.generator;

import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.RegistryID;
import net.minecraft.server.v1_8_R3.WorldChunkManager;
import net.minecraft.server.v1_8_R3.WorldGenStronghold;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class CustomChunkGenerator
  extends InternalChunkGenerator
{
  private final ChunkGenerator generator;
  private final WorldServer world;
  private final Random random;
  private final WorldGenStronghold strongholdGen = new WorldGenStronghold();
  
  private static class CustomBiomeGrid
    implements ChunkGenerator.BiomeGrid
  {
    BiomeBase[] biome;
    
    public Biome getBiome(int x, int z)
    {
      return CraftBlock.biomeBaseToBiome(this.biome[(z << 4 | x)]);
    }
    
    public void setBiome(int x, int z, Biome bio)
    {
      this.biome[(z << 4 | x)] = CraftBlock.biomeToBiomeBase(bio);
    }
  }
  
  public CustomChunkGenerator(net.minecraft.server.v1_8_R3.World world, long seed, ChunkGenerator generator)
  {
    this.world = ((WorldServer)world);
    this.generator = generator;
    
    this.random = new Random(seed);
  }
  
  public boolean isChunkLoaded(int x, int z)
  {
    return true;
  }
  
  public Chunk getOrCreateChunk(int x, int z)
  {
    this.random.setSeed(x * 341873128712L + z * 132897987541L);
    
    CustomBiomeGrid biomegrid = new CustomBiomeGrid(null);
    biomegrid.biome = new BiomeBase['Ā'];
    this.world.getWorldChunkManager().getBiomeBlock(biomegrid.biome, x << 4, z << 4, 16, 16);
    
    short[][] xbtypes = this.generator.generateExtBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
    Chunk chunk;
    if (xbtypes != null)
    {
      Chunk chunk = new Chunk(this.world, x, z);
      
      ChunkSection[] csect = chunk.getSections();
      int scnt = Math.min(csect.length, xbtypes.length);
      for (int sec = 0; sec < scnt; sec++) {
        if (xbtypes[sec] != null)
        {
          char[] secBlkID = new char['က'];
          short[] bdata = xbtypes[sec];
          for (int i = 0; i < bdata.length; i++)
          {
            Block b = Block.getById(bdata[i]);
            secBlkID[i] = ((char)Block.d.b(b.getBlockData()));
          }
          csect[sec] = new ChunkSection(sec << 4, true, secBlkID);
        }
      }
    }
    else
    {
      byte[][] btypes = this.generator.generateBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
      if (btypes != null)
      {
        Chunk chunk = new Chunk(this.world, x, z);
        
        ChunkSection[] csect = chunk.getSections();
        int scnt = Math.min(csect.length, btypes.length);
        for (int sec = 0; sec < scnt; sec++) {
          if (btypes[sec] != null)
          {
            char[] secBlkID = new char['က'];
            for (int i = 0; i < secBlkID.length; i++)
            {
              Block b = Block.getById(btypes[sec][i]);
              secBlkID[i] = ((char)Block.d.b(b.getBlockData()));
            }
            csect[sec] = new ChunkSection(sec << 4, true, secBlkID);
          }
        }
      }
      else
      {
        byte[] types = this.generator.generate(this.world.getWorld(), this.random, x, z);
        int ydim = types.length / 256;
        int scnt = ydim / 16;
        
        chunk = new Chunk(this.world, x, z);
        
        ChunkSection[] csect = chunk.getSections();
        
        scnt = Math.min(scnt, csect.length);
        for (int sec = 0; sec < scnt; sec++)
        {
          ChunkSection cs = null;
          char[] csbytes = null;
          for (int cy = 0; cy < 16; cy++)
          {
            int cyoff = cy | sec << 4;
            for (int cx = 0; cx < 16; cx++)
            {
              int cxyoff = cx * ydim * 16 + cyoff;
              for (int cz = 0; cz < 16; cz++)
              {
                byte blk = types[(cxyoff + cz * ydim)];
                if (blk != 0)
                {
                  if (cs == null)
                  {
                    cs = csect[sec] = new ChunkSection(sec << 4, true);
                    csbytes = cs.getIdArray();
                  }
                  Block b = Block.getById(blk);
                  csbytes[(cy << 8 | cz << 4 | cx)] = ((char)Block.d.b(b.getBlockData()));
                }
              }
            }
          }
          if (cs != null) {
            cs.recalcBlockCounts();
          }
        }
      }
    }
    byte[] biomeIndex = chunk.getBiomeIndex();
    for (int i = 0; i < biomeIndex.length; i++) {
      biomeIndex[i] = ((byte)(biomegrid.biome[i].id & 0xFF));
    }
    chunk.initLighting();
    
    return chunk;
  }
  
  public Chunk getChunkAt(BlockPosition blockPosition)
  {
    return getChunkAt(blockPosition.getX() >> 4, blockPosition.getZ() >> 4);
  }
  
  public void getChunkAt(IChunkProvider icp, int i, int i1) {}
  
  public boolean a(IChunkProvider iChunkProvider, Chunk chunk, int i, int i1)
  {
    return false;
  }
  
  public boolean saveChunks(boolean bln, IProgressUpdate ipu)
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
  
  public byte[] generate(org.bukkit.World world, Random random, int x, int z)
  {
    return this.generator.generate(world, random, x, z);
  }
  
  public byte[][] generateBlockSections(org.bukkit.World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes)
  {
    return this.generator.generateBlockSections(world, random, x, z, biomes);
  }
  
  public short[][] generateExtBlockSections(org.bukkit.World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes)
  {
    return this.generator.generateExtBlockSections(world, random, x, z, biomes);
  }
  
  public Chunk getChunkAt(int x, int z)
  {
    return getOrCreateChunk(x, z);
  }
  
  public boolean canSpawn(org.bukkit.World world, int x, int z)
  {
    return this.generator.canSpawn(world, x, z);
  }
  
  public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world)
  {
    return this.generator.getDefaultPopulators(world);
  }
  
  public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType type, BlockPosition position)
  {
    BiomeBase biomebase = this.world.getBiome(position);
    
    return biomebase == null ? null : biomebase.getMobs(type);
  }
  
  public BlockPosition findNearestMapFeature(net.minecraft.server.v1_8_R3.World world, String type, BlockPosition position)
  {
    return ("Stronghold".equals(type)) && (this.strongholdGen != null) ? this.strongholdGen.getNearestGeneratedFeature(world, position) : null;
  }
  
  public void recreateStructures(int i, int j) {}
  
  public int getLoadedChunks()
  {
    return 0;
  }
  
  public void recreateStructures(Chunk chunk, int i, int i1) {}
  
  public String getName()
  {
    return "CustomChunkGenerator";
  }
  
  public void c() {}
}
