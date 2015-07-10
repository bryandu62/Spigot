package org.bukkit.craftbukkit.v1_8_R3;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.EmptyChunk;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.NibbleArray;
import net.minecraft.server.v1_8_R3.RegistryID;
import net.minecraft.server.v1_8_R3.WorldChunkManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.ChunkSnapshot;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;

public class CraftChunk
  implements org.bukkit.Chunk
{
  private WeakReference<net.minecraft.server.v1_8_R3.Chunk> weakChunk;
  private final WorldServer worldServer;
  private final int x;
  private final int z;
  private static final byte[] emptyData = new byte['ࠀ'];
  private static final short[] emptyBlockIDs = new short['က'];
  private static final byte[] emptySkyLight = new byte['ࠀ'];
  
  public CraftChunk(net.minecraft.server.v1_8_R3.Chunk chunk)
  {
    if (!(chunk instanceof EmptyChunk)) {
      this.weakChunk = new WeakReference(chunk);
    }
    this.worldServer = ((WorldServer)getHandle().world);
    this.x = getHandle().locX;
    this.z = getHandle().locZ;
  }
  
  public org.bukkit.World getWorld()
  {
    return this.worldServer.getWorld();
  }
  
  public CraftWorld getCraftWorld()
  {
    return (CraftWorld)getWorld();
  }
  
  public net.minecraft.server.v1_8_R3.Chunk getHandle()
  {
    net.minecraft.server.v1_8_R3.Chunk c = (net.minecraft.server.v1_8_R3.Chunk)this.weakChunk.get();
    if (c == null)
    {
      c = this.worldServer.getChunkAt(this.x, this.z);
      if (!(c instanceof EmptyChunk)) {
        this.weakChunk = new WeakReference(c);
      }
    }
    return c;
  }
  
  void breakLink()
  {
    this.weakChunk.clear();
  }
  
  public int getX()
  {
    return this.x;
  }
  
  public int getZ()
  {
    return this.z;
  }
  
  public String toString()
  {
    return "CraftChunk{x=" + getX() + "z=" + getZ() + '}';
  }
  
  public org.bukkit.block.Block getBlock(int x, int y, int z)
  {
    return new CraftBlock(this, getX() << 4 | x & 0xF, y, getZ() << 4 | z & 0xF);
  }
  
  public org.bukkit.entity.Entity[] getEntities()
  {
    int count = 0;int index = 0;
    net.minecraft.server.v1_8_R3.Chunk chunk = getHandle();
    for (int i = 0; i < 16; i++) {
      count += chunk.entitySlices[i].size();
    }
    org.bukkit.entity.Entity[] entities = new org.bukkit.entity.Entity[count];
    for (int i = 0; i < 16; i++)
    {
      Object[] arrayOfObject;
      int i = (arrayOfObject = chunk.entitySlices[i].toArray()).length;
      for (int j = 0; j < i; j++)
      {
        Object obj = arrayOfObject[j];
        if ((obj instanceof net.minecraft.server.v1_8_R3.Entity)) {
          entities[(index++)] = ((net.minecraft.server.v1_8_R3.Entity)obj).getBukkitEntity();
        }
      }
    }
    return entities;
  }
  
  public BlockState[] getTileEntities()
  {
    int index = 0;
    net.minecraft.server.v1_8_R3.Chunk chunk = getHandle();
    
    BlockState[] entities = new BlockState[chunk.tileEntities.size()];
    Object[] arrayOfObject;
    int i = (arrayOfObject = chunk.tileEntities.keySet().toArray()).length;
    for (int j = 0; j < i; j++)
    {
      Object obj = arrayOfObject[j];
      if ((obj instanceof BlockPosition))
      {
        BlockPosition position = (BlockPosition)obj;
        entities[(index++)] = this.worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
      }
    }
    return entities;
  }
  
  public boolean isLoaded()
  {
    return getWorld().isChunkLoaded(this);
  }
  
  public boolean load()
  {
    return getWorld().loadChunk(getX(), getZ(), true);
  }
  
  public boolean load(boolean generate)
  {
    return getWorld().loadChunk(getX(), getZ(), generate);
  }
  
  public boolean unload()
  {
    return getWorld().unloadChunk(getX(), getZ());
  }
  
  public boolean unload(boolean save)
  {
    return getWorld().unloadChunk(getX(), getZ(), save);
  }
  
  public boolean unload(boolean save, boolean safe)
  {
    return getWorld().unloadChunk(getX(), getZ(), save, safe);
  }
  
  public ChunkSnapshot getChunkSnapshot()
  {
    return getChunkSnapshot(true, false, false);
  }
  
  public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain)
  {
    net.minecraft.server.v1_8_R3.Chunk chunk = getHandle();
    
    ChunkSection[] cs = chunk.getSections();
    short[][] sectionBlockIDs = new short[cs.length][];
    byte[][] sectionBlockData = new byte[cs.length][];
    byte[][] sectionSkyLights = new byte[cs.length][];
    byte[][] sectionEmitLights = new byte[cs.length][];
    boolean[] sectionEmpty = new boolean[cs.length];
    for (int i = 0; i < cs.length; i++) {
      if (cs[i] == null)
      {
        sectionBlockIDs[i] = emptyBlockIDs;
        sectionBlockData[i] = emptyData;
        sectionSkyLights[i] = emptySkyLight;
        sectionEmitLights[i] = emptyData;
        sectionEmpty[i] = true;
      }
      else
      {
        short[] blockids = new short['က'];
        char[] baseids = cs[i].getIdArray();
        byte[] dataValues = sectionBlockData[i] = new byte['ࠀ'];
        for (int j = 0; j < 4096; j++) {
          if (baseids[j] != 0)
          {
            IBlockData blockData = (IBlockData)net.minecraft.server.v1_8_R3.Block.d.a(baseids[j]);
            if (blockData != null)
            {
              blockids[j] = ((short)net.minecraft.server.v1_8_R3.Block.getId(blockData.getBlock()));
              int data = blockData.getBlock().toLegacyData(blockData);
              int jj = j >> 1;
              if ((j & 0x1) == 0) {
                dataValues[jj] = ((byte)(dataValues[jj] & 0xF0 | data & 0xF));
              } else {
                dataValues[jj] = ((byte)(dataValues[jj] & 0xF | (data & 0xF) << 4));
              }
            }
          }
        }
        sectionBlockIDs[i] = blockids;
        if (cs[i].getSkyLightArray() == null)
        {
          sectionSkyLights[i] = emptyData;
        }
        else
        {
          sectionSkyLights[i] = new byte['ࠀ'];
          System.arraycopy(cs[i].getSkyLightArray().a(), 0, sectionSkyLights[i], 0, 2048);
        }
        sectionEmitLights[i] = new byte['ࠀ'];
        System.arraycopy(cs[i].getEmittedLightArray().a(), 0, sectionEmitLights[i], 0, 2048);
      }
    }
    int[] hmap = null;
    if (includeMaxBlockY)
    {
      hmap = new int['Ā'];
      System.arraycopy(chunk.heightMap, 0, hmap, 0, 256);
    }
    BiomeBase[] biome = null;
    double[] biomeTemp = null;
    double[] biomeRain = null;
    if ((includeBiome) || (includeBiomeTempRain))
    {
      WorldChunkManager wcm = chunk.world.getWorldChunkManager();
      if (includeBiome)
      {
        biome = new BiomeBase['Ā'];
        for (int i = 0; i < 256; i++) {
          biome[i] = chunk.getBiome(new BlockPosition(i & 0xF, 0, i >> 4), wcm);
        }
      }
      if (includeBiomeTempRain)
      {
        biomeTemp = new double['Ā'];
        biomeRain = new double['Ā'];
        float[] dat = getTemperatures(wcm, getX() << 4, getZ() << 4);
        for (int i = 0; i < 256; i++) {
          biomeTemp[i] = dat[i];
        }
        dat = wcm.getWetness(null, getX() << 4, getZ() << 4, 16, 16);
        for (int i = 0; i < 256; i++) {
          biomeRain[i] = dat[i];
        }
      }
    }
    org.bukkit.World world = getWorld();
    return new CraftChunkSnapshot(getX(), getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionBlockData, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, biome, biomeTemp, biomeRain);
  }
  
  public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain)
  {
    BiomeBase[] biome = null;
    double[] biomeTemp = null;
    double[] biomeRain = null;
    if ((includeBiome) || (includeBiomeTempRain))
    {
      WorldChunkManager wcm = world.getHandle().getWorldChunkManager();
      if (includeBiome)
      {
        biome = new BiomeBase['Ā'];
        for (int i = 0; i < 256; i++) {
          biome[i] = world.getHandle().getBiome(new BlockPosition((x << 4) + (i & 0xF), 0, (z << 4) + (i >> 4)));
        }
      }
      if (includeBiomeTempRain)
      {
        biomeTemp = new double['Ā'];
        biomeRain = new double['Ā'];
        float[] dat = getTemperatures(wcm, x << 4, z << 4);
        for (int i = 0; i < 256; i++) {
          biomeTemp[i] = dat[i];
        }
        dat = wcm.getWetness(null, x << 4, z << 4, 16, 16);
        for (int i = 0; i < 256; i++) {
          biomeRain[i] = dat[i];
        }
      }
    }
    int hSection = world.getMaxHeight() >> 4;
    short[][] blockIDs = new short[hSection][];
    byte[][] skyLight = new byte[hSection][];
    byte[][] emitLight = new byte[hSection][];
    byte[][] blockData = new byte[hSection][];
    boolean[] empty = new boolean[hSection];
    for (int i = 0; i < hSection; i++)
    {
      blockIDs[i] = emptyBlockIDs;
      skyLight[i] = emptySkyLight;
      emitLight[i] = emptyData;
      blockData[i] = emptyData;
      empty[i] = true;
    }
    return new CraftChunkSnapshot(x, z, world.getName(), world.getFullTime(), blockIDs, blockData, skyLight, emitLight, empty, new int['Ā'], biome, biomeTemp, biomeRain);
  }
  
  private static float[] getTemperatures(WorldChunkManager chunkmanager, int chunkX, int chunkZ)
  {
    BiomeBase[] biomes = chunkmanager.getBiomes(null, chunkX, chunkZ, 16, 16);
    float[] temps = new float[biomes.length];
    for (int i = 0; i < biomes.length; i++)
    {
      float temp = biomes[i].temperature;
      if (temp > 1.0F) {
        temp = 1.0F;
      }
      temps[i] = temp;
    }
    return temps;
  }
  
  static
  {
    Arrays.fill(emptySkyLight, (byte)-1);
  }
}
