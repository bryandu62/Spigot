package net.minecraft.server.v1_8_R3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings.WorldTimingsHandler;
import org.spigotmc.CustomTimingsHandler;

public class ChunkRegionLoader
  implements IChunkLoader, IAsyncChunkSaver
{
  private static final Logger a = ;
  private Map<ChunkCoordIntPair, NBTTagCompound> b = new ConcurrentHashMap();
  private Set<ChunkCoordIntPair> c = Collections.newSetFromMap(new ConcurrentHashMap());
  private final File d;
  
  public ChunkRegionLoader(File file)
  {
    this.d = file;
  }
  
  public boolean chunkExists(World world, int i, int j)
  {
    ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
    if ((this.c.contains(chunkcoordintpair)) && 
      (this.b.containsKey(chunkcoordintpair))) {
      return true;
    }
    return RegionFileCache.a(this.d, i, j).chunkExists(i & 0x1F, j & 0x1F);
  }
  
  public Chunk a(World world, int i, int j)
    throws IOException
  {
    world.timings.syncChunkLoadDataTimer.startTiming();
    Object[] data = loadChunk(world, i, j);
    world.timings.syncChunkLoadDataTimer.stopTiming();
    if (data != null)
    {
      Chunk chunk = (Chunk)data[0];
      NBTTagCompound nbttagcompound = (NBTTagCompound)data[1];
      loadEntities(chunk, nbttagcompound.getCompound("Level"), world);
      return chunk;
    }
    return null;
  }
  
  public Object[] loadChunk(World world, int i, int j)
    throws IOException
  {
    ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i, j);
    NBTTagCompound nbttagcompound = (NBTTagCompound)this.b.get(chunkcoordintpair);
    if (nbttagcompound == null)
    {
      DataInputStream datainputstream = RegionFileCache.c(this.d, i, j);
      if (datainputstream == null) {
        return null;
      }
      nbttagcompound = NBTCompressedStreamTools.a(datainputstream);
    }
    return a(world, i, j, nbttagcompound);
  }
  
  protected Object[] a(World world, int i, int j, NBTTagCompound nbttagcompound)
  {
    if (!nbttagcompound.hasKeyOfType("Level", 10))
    {
      a.error("Chunk file at " + i + "," + j + " is missing level data, skipping");
      return null;
    }
    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Level");
    if (!nbttagcompound1.hasKeyOfType("Sections", 9))
    {
      a.error("Chunk file at " + i + "," + j + " is missing block data, skipping");
      return null;
    }
    Chunk chunk = a(world, nbttagcompound1);
    if (!chunk.a(i, j))
    {
      a.error("Chunk file at " + i + "," + j + " is in the wrong location; relocating. (Expected " + i + ", " + j + ", got " + chunk.locX + ", " + chunk.locZ + ")");
      nbttagcompound1.setInt("xPos", i);
      nbttagcompound1.setInt("zPos", j);
      
      NBTTagList tileEntities = nbttagcompound.getCompound("Level").getList("TileEntities", 10);
      if (tileEntities != null) {
        for (int te = 0; te < tileEntities.size(); te++)
        {
          NBTTagCompound tileEntity = tileEntities.get(te);
          int x = tileEntity.getInt("x") - chunk.locX * 16;
          int z = tileEntity.getInt("z") - chunk.locZ * 16;
          tileEntity.setInt("x", i * 16 + x);
          tileEntity.setInt("z", j * 16 + z);
        }
      }
      chunk = a(world, nbttagcompound1);
    }
    Object[] data = new Object[2];
    data[0] = chunk;
    data[1] = nbttagcompound;
    return data;
  }
  
  public void a(World world, Chunk chunk)
    throws IOException, ExceptionWorldConflict
  {
    world.checkSession();
    try
    {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      
      nbttagcompound.set("Level", nbttagcompound1);
      a(chunk, world, nbttagcompound1);
      a(chunk.j(), nbttagcompound);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
  }
  
  protected void a(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound)
  {
    if (!this.c.contains(chunkcoordintpair)) {
      this.b.put(chunkcoordintpair, nbttagcompound);
    }
    FileIOThread.a().a(this);
  }
  
  /* Error */
  public boolean c()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 43	net/minecraft/server/v1_8_R3/ChunkRegionLoader:b	Ljava/util/Map;
    //   4: invokeinterface 289 1 0
    //   9: ifeq +5 -> 14
    //   12: iconst_0
    //   13: ireturn
    //   14: aload_0
    //   15: getfield 43	net/minecraft/server/v1_8_R3/ChunkRegionLoader:b	Ljava/util/Map;
    //   18: invokeinterface 293 1 0
    //   23: invokeinterface 297 1 0
    //   28: invokeinterface 303 1 0
    //   33: checkcast 60	net/minecraft/server/v1_8_R3/ChunkCoordIntPair
    //   36: astore_1
    //   37: aload_0
    //   38: getfield 51	net/minecraft/server/v1_8_R3/ChunkRegionLoader:c	Ljava/util/Set;
    //   41: aload_1
    //   42: invokeinterface 306 2 0
    //   47: pop
    //   48: aload_0
    //   49: getfield 43	net/minecraft/server/v1_8_R3/ChunkRegionLoader:b	Ljava/util/Map;
    //   52: aload_1
    //   53: invokeinterface 309 2 0
    //   58: checkcast 120	net/minecraft/server/v1_8_R3/NBTTagCompound
    //   61: astore_2
    //   62: aload_2
    //   63: ifnull +17 -> 80
    //   66: aload_0
    //   67: aload_1
    //   68: aload_2
    //   69: invokespecial 311	net/minecraft/server/v1_8_R3/ChunkRegionLoader:b	(Lnet/minecraft/server/v1_8_R3/ChunkCoordIntPair;Lnet/minecraft/server/v1_8_R3/NBTTagCompound;)V
    //   72: goto +8 -> 80
    //   75: astore_3
    //   76: aload_3
    //   77: invokevirtual 271	java/lang/Exception:printStackTrace	()V
    //   80: iconst_1
    //   81: istore 4
    //   83: goto +19 -> 102
    //   86: astore 5
    //   88: aload_0
    //   89: getfield 51	net/minecraft/server/v1_8_R3/ChunkRegionLoader:c	Ljava/util/Set;
    //   92: aload_1
    //   93: invokeinterface 315 2 0
    //   98: pop
    //   99: aload 5
    //   101: athrow
    //   102: aload_0
    //   103: getfield 51	net/minecraft/server/v1_8_R3/ChunkRegionLoader:c	Ljava/util/Set;
    //   106: aload_1
    //   107: invokeinterface 315 2 0
    //   112: pop
    //   113: iload 4
    //   115: ireturn
    // Line number table:
    //   Java source line #143	-> byte code offset #0
    //   Java source line #144	-> byte code offset #12
    //   Java source line #146	-> byte code offset #14
    //   Java source line #151	-> byte code offset #37
    //   Java source line #152	-> byte code offset #48
    //   Java source line #154	-> byte code offset #62
    //   Java source line #156	-> byte code offset #66
    //   Java source line #157	-> byte code offset #72
    //   Java source line #158	-> byte code offset #76
    //   Java source line #162	-> byte code offset #80
    //   Java source line #163	-> byte code offset #83
    //   Java source line #164	-> byte code offset #88
    //   Java source line #165	-> byte code offset #99
    //   Java source line #164	-> byte code offset #102
    //   Java source line #167	-> byte code offset #113
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	116	0	this	ChunkRegionLoader
    //   36	71	1	chunkcoordintpair	ChunkCoordIntPair
    //   61	8	2	nbttagcompound	NBTTagCompound
    //   75	2	3	exception	Exception
    //   81	3	4	flag	boolean
    //   102	12	4	flag	boolean
    //   86	14	5	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   66	72	75	java/lang/Exception
    //   37	86	86	finally
  }
  
  private void b(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound)
    throws IOException
  {
    DataOutputStream dataoutputstream = RegionFileCache.d(this.d, chunkcoordintpair.x, chunkcoordintpair.z);
    
    NBTCompressedStreamTools.a(nbttagcompound, dataoutputstream);
    dataoutputstream.close();
  }
  
  public void b(World world, Chunk chunk)
    throws IOException
  {}
  
  public void a() {}
  
  public void b()
  {
    while (c()) {}
  }
  
  private void a(Chunk chunk, World world, NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setByte("V", (byte)1);
    nbttagcompound.setInt("xPos", chunk.locX);
    nbttagcompound.setInt("zPos", chunk.locZ);
    nbttagcompound.setLong("LastUpdate", world.getTime());
    nbttagcompound.setIntArray("HeightMap", chunk.q());
    nbttagcompound.setBoolean("TerrainPopulated", chunk.isDone());
    nbttagcompound.setBoolean("LightPopulated", chunk.u());
    nbttagcompound.setLong("InhabitedTime", chunk.w());
    ChunkSection[] achunksection = chunk.getSections();
    NBTTagList nbttaglist = new NBTTagList();
    boolean flag = !world.worldProvider.o();
    ChunkSection[] achunksection1 = achunksection;
    int i = achunksection.length;
    for (int j = 0; j < i; j++)
    {
      ChunkSection chunksection = achunksection1[j];
      if (chunksection != null)
      {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setByte("Y", (byte)(chunksection.getYPosition() >> 4 & 0xFF));
        byte[] abyte = new byte[chunksection.getIdArray().length];
        NibbleArray nibblearray = new NibbleArray();
        NibbleArray nibblearray1 = null;
        for (int k = 0; k < chunksection.getIdArray().length; k++)
        {
          char c0 = chunksection.getIdArray()[k];
          int l = k & 0xF;
          int i1 = k >> 8 & 0xF;
          int j1 = k >> 4 & 0xF;
          if (c0 >> '\f' != 0)
          {
            if (nibblearray1 == null) {
              nibblearray1 = new NibbleArray();
            }
            nibblearray1.a(l, i1, j1, c0 >> '\f');
          }
          abyte[k] = ((byte)(c0 >> '\004' & 0xFF));
          nibblearray.a(l, i1, j1, c0 & 0xF);
        }
        nbttagcompound1.setByteArray("Blocks", abyte);
        nbttagcompound1.setByteArray("Data", nibblearray.a());
        if (nibblearray1 != null) {
          nbttagcompound1.setByteArray("Add", nibblearray1.a());
        }
        nbttagcompound1.setByteArray("BlockLight", chunksection.getEmittedLightArray().a());
        if (flag) {
          nbttagcompound1.setByteArray("SkyLight", chunksection.getSkyLightArray().a());
        } else {
          nbttagcompound1.setByteArray("SkyLight", new byte[chunksection.getEmittedLightArray().a().length]);
        }
        nbttaglist.add(nbttagcompound1);
      }
    }
    nbttagcompound.set("Sections", nbttaglist);
    nbttagcompound.setByteArray("Biomes", chunk.getBiomeIndex());
    chunk.g(false);
    NBTTagList nbttaglist1 = new NBTTagList();
    for (i = 0; i < chunk.getEntitySlices().length; i++)
    {
      Iterator iterator = chunk.getEntitySlices()[i].iterator();
      while (iterator.hasNext())
      {
        Entity entity = (Entity)iterator.next();
        
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        if (entity.d(nbttagcompound1))
        {
          chunk.g(true);
          nbttaglist1.add(nbttagcompound1);
        }
      }
    }
    nbttagcompound.set("Entities", nbttaglist1);
    NBTTagList nbttaglist2 = new NBTTagList();
    
    Iterator iterator = chunk.getTileEntities().values().iterator();
    while (iterator.hasNext())
    {
      TileEntity tileentity = (TileEntity)iterator.next();
      
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      tileentity.b(nbttagcompound1);
      nbttaglist2.add(nbttagcompound1);
    }
    nbttagcompound.set("TileEntities", nbttaglist2);
    List list = world.a(chunk, false);
    if (list != null)
    {
      long k1 = world.getTime();
      NBTTagList nbttaglist3 = new NBTTagList();
      Iterator iterator1 = list.iterator();
      while (iterator1.hasNext())
      {
        NextTickListEntry nextticklistentry = (NextTickListEntry)iterator1.next();
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();
        MinecraftKey minecraftkey = (MinecraftKey)Block.REGISTRY.c(nextticklistentry.a());
        
        nbttagcompound2.setString("i", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound2.setInt("x", nextticklistentry.a.getX());
        nbttagcompound2.setInt("y", nextticklistentry.a.getY());
        nbttagcompound2.setInt("z", nextticklistentry.a.getZ());
        nbttagcompound2.setInt("t", (int)(nextticklistentry.b - k1));
        nbttagcompound2.setInt("p", nextticklistentry.c);
        nbttaglist3.add(nbttagcompound2);
      }
      nbttagcompound.set("TileTicks", nbttaglist3);
    }
  }
  
  private Chunk a(World world, NBTTagCompound nbttagcompound)
  {
    int i = nbttagcompound.getInt("xPos");
    int j = nbttagcompound.getInt("zPos");
    Chunk chunk = new Chunk(world, i, j);
    
    chunk.a(nbttagcompound.getIntArray("HeightMap"));
    chunk.d(nbttagcompound.getBoolean("TerrainPopulated"));
    chunk.e(nbttagcompound.getBoolean("LightPopulated"));
    chunk.c(nbttagcompound.getLong("InhabitedTime"));
    NBTTagList nbttaglist = nbttagcompound.getList("Sections", 10);
    byte b0 = 16;
    ChunkSection[] achunksection = new ChunkSection[b0];
    boolean flag = !world.worldProvider.o();
    for (int k = 0; k < nbttaglist.size(); k++)
    {
      NBTTagCompound nbttagcompound1 = nbttaglist.get(k);
      byte b1 = nbttagcompound1.getByte("Y");
      ChunkSection chunksection = new ChunkSection(b1 << 4, flag);
      byte[] abyte = nbttagcompound1.getByteArray("Blocks");
      NibbleArray nibblearray = new NibbleArray(nbttagcompound1.getByteArray("Data"));
      NibbleArray nibblearray1 = nbttagcompound1.hasKeyOfType("Add", 7) ? new NibbleArray(nbttagcompound1.getByteArray("Add")) : null;
      char[] achar = new char[abyte.length];
      for (int l = 0; l < achar.length; l++)
      {
        int i1 = l & 0xF;
        int j1 = l >> 8 & 0xF;
        int k1 = l >> 4 & 0xF;
        int l1 = nibblearray1 != null ? nibblearray1.a(i1, j1, k1) : 0;
        
        int ex = l1;
        int id = abyte[l] & 0xFF;
        int data = nibblearray.a(i1, j1, k1);
        int packed = ex << 12 | id << 4 | data;
        if (Block.d.a(packed) == null)
        {
          Block block = Block.getById(ex << 8 | id);
          if (block != null)
          {
            try
            {
              data = block.toLegacyData(block.fromLegacyData(data));
            }
            catch (Exception localException)
            {
              data = block.toLegacyData(block.getBlockData());
            }
            packed = ex << 12 | id << 4 | data;
          }
        }
        achar[l] = ((char)packed);
      }
      chunksection.a(achar);
      chunksection.a(new NibbleArray(nbttagcompound1.getByteArray("BlockLight")));
      if (flag) {
        chunksection.b(new NibbleArray(nbttagcompound1.getByteArray("SkyLight")));
      }
      chunksection.recalcBlockCounts();
      achunksection[b1] = chunksection;
    }
    chunk.a(achunksection);
    if (nbttagcompound.hasKeyOfType("Biomes", 7)) {
      chunk.a(nbttagcompound.getByteArray("Biomes"));
    }
    return chunk;
  }
  
  public void loadEntities(Chunk chunk, NBTTagCompound nbttagcompound, World world)
  {
    world.timings.syncChunkLoadEntitiesTimer.startTiming();
    NBTTagList nbttaglist1 = nbttagcompound.getList("Entities", 10);
    if (nbttaglist1 != null) {
      for (int i2 = 0; i2 < nbttaglist1.size(); i2++)
      {
        NBTTagCompound nbttagcompound2 = nbttaglist1.get(i2);
        Entity entity = EntityTypes.a(nbttagcompound2, world);
        
        chunk.g(true);
        if (entity != null)
        {
          chunk.a(entity);
          Entity entity1 = entity;
          for (NBTTagCompound nbttagcompound3 = nbttagcompound2; nbttagcompound3.hasKeyOfType("Riding", 10); nbttagcompound3 = nbttagcompound3.getCompound("Riding"))
          {
            Entity entity2 = EntityTypes.a(nbttagcompound3.getCompound("Riding"), world);
            if (entity2 != null)
            {
              chunk.a(entity2);
              entity1.mount(entity2);
            }
            entity1 = entity2;
          }
        }
      }
    }
    world.timings.syncChunkLoadEntitiesTimer.stopTiming();
    world.timings.syncChunkLoadTileEntitiesTimer.startTiming();
    NBTTagList nbttaglist2 = nbttagcompound.getList("TileEntities", 10);
    if (nbttaglist2 != null) {
      for (int j2 = 0; j2 < nbttaglist2.size(); j2++)
      {
        NBTTagCompound nbttagcompound4 = nbttaglist2.get(j2);
        TileEntity tileentity = TileEntity.c(nbttagcompound4);
        if (tileentity != null) {
          chunk.a(tileentity);
        }
      }
    }
    world.timings.syncChunkLoadTileEntitiesTimer.stopTiming();
    world.timings.syncChunkLoadTileTicksTimer.startTiming();
    if (nbttagcompound.hasKeyOfType("TileTicks", 9))
    {
      NBTTagList nbttaglist3 = nbttagcompound.getList("TileTicks", 10);
      if (nbttaglist3 != null) {
        for (int k2 = 0; k2 < nbttaglist3.size(); k2++)
        {
          NBTTagCompound nbttagcompound5 = nbttaglist3.get(k2);
          Block block;
          Block block;
          if (nbttagcompound5.hasKeyOfType("i", 8)) {
            block = Block.getByName(nbttagcompound5.getString("i"));
          } else {
            block = Block.getById(nbttagcompound5.getInt("i"));
          }
          world.b(new BlockPosition(nbttagcompound5.getInt("x"), nbttagcompound5.getInt("y"), nbttagcompound5.getInt("z")), block, nbttagcompound5.getInt("t"), nbttagcompound5.getInt("p"));
        }
      }
    }
    world.timings.syncChunkLoadTileTicksTimer.stopTiming();
  }
}
