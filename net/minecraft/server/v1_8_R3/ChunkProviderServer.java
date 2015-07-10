package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings.WorldTimingsHandler;
import org.bukkit.craftbukkit.v1_8_R3.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHashSet;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.CustomTimingsHandler;

public class ChunkProviderServer
  implements IChunkProvider
{
  private static final Logger b = ;
  public LongHashSet unloadQueue = new LongHashSet();
  public Chunk emptyChunk;
  public IChunkProvider chunkProvider;
  private IChunkLoader chunkLoader;
  public boolean forceChunkLoad = false;
  public LongObjectHashMap<Chunk> chunks = new LongObjectHashMap();
  public WorldServer world;
  
  public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider)
  {
    this.emptyChunk = new EmptyChunk(worldserver, 0, 0);
    this.world = worldserver;
    this.chunkLoader = ichunkloader;
    this.chunkProvider = ichunkprovider;
  }
  
  public boolean isChunkLoaded(int i, int j)
  {
    return this.chunks.containsKey(LongHash.toLong(i, j));
  }
  
  public Collection a()
  {
    return this.chunks.values();
  }
  
  public void queueUnload(int i, int j)
  {
    if (this.world.worldProvider.e())
    {
      if (!this.world.c(i, j))
      {
        this.unloadQueue.add(i, j);
        
        Chunk c = (Chunk)this.chunks.get(LongHash.toLong(i, j));
        if (c != null) {
          c.mustSave = true;
        }
      }
    }
    else
    {
      this.unloadQueue.add(i, j);
      
      Chunk c = (Chunk)this.chunks.get(LongHash.toLong(i, j));
      if (c != null) {
        c.mustSave = true;
      }
    }
  }
  
  public void b()
  {
    Iterator iterator = this.chunks.values().iterator();
    while (iterator.hasNext())
    {
      Chunk chunk = (Chunk)iterator.next();
      
      queueUnload(chunk.locX, chunk.locZ);
    }
  }
  
  public Chunk getChunkIfLoaded(int x, int z)
  {
    return (Chunk)this.chunks.get(LongHash.toLong(x, z));
  }
  
  public Chunk getChunkAt(int i, int j)
  {
    return getChunkAt(i, j, null);
  }
  
  public Chunk getChunkAt(int i, int j, Runnable runnable)
  {
    this.unloadQueue.remove(i, j);
    Chunk chunk = (Chunk)this.chunks.get(LongHash.toLong(i, j));
    ChunkRegionLoader loader = null;
    if ((this.chunkLoader instanceof ChunkRegionLoader)) {
      loader = (ChunkRegionLoader)this.chunkLoader;
    }
    if ((chunk == null) && (loader != null) && (loader.chunkExists(this.world, i, j)))
    {
      if (runnable != null)
      {
        ChunkIOExecutor.queueChunkLoad(this.world, loader, this, i, j, runnable);
        return null;
      }
      chunk = ChunkIOExecutor.syncChunkLoad(this.world, loader, this, i, j);
    }
    else if (chunk == null)
    {
      chunk = originalGetChunkAt(i, j);
    }
    if (runnable != null) {
      runnable.run();
    }
    return chunk;
  }
  
  public Chunk originalGetChunkAt(int i, int j)
  {
    this.unloadQueue.remove(i, j);
    Chunk chunk = (Chunk)this.chunks.get(LongHash.toLong(i, j));
    boolean newChunk = false;
    if (chunk == null)
    {
      this.world.timings.syncChunkLoadTimer.startTiming();
      chunk = loadChunk(i, j);
      if (chunk == null)
      {
        if (this.chunkProvider == null) {
          chunk = this.emptyChunk;
        } else {
          try
          {
            chunk = this.chunkProvider.getOrCreateChunk(i, j);
          }
          catch (Throwable throwable)
          {
            CrashReport crashreport = CrashReport.a(throwable, "Exception generating new chunk");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");
            
            crashreportsystemdetails.a("Location", String.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));
            crashreportsystemdetails.a("Position hash", Long.valueOf(LongHash.toLong(i, j)));
            crashreportsystemdetails.a("Generator", this.chunkProvider.getName());
            throw new ReportedException(crashreport);
          }
        }
        newChunk = true;
      }
      this.chunks.put(LongHash.toLong(i, j), chunk);
      
      chunk.addEntities();
      
      Server server = this.world.getServer();
      if (server != null) {
        server.getPluginManager().callEvent(new ChunkLoadEvent(chunk.bukkitChunk, newChunk));
      }
      for (int x = -2; x < 3; x++) {
        for (int z = -2; z < 3; z++) {
          if ((x != 0) || (z != 0))
          {
            Chunk neighbor = getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
            if (neighbor != null)
            {
              neighbor.setNeighborLoaded(-x, -z);
              chunk.setNeighborLoaded(x, z);
            }
          }
        }
      }
      chunk.loadNearby(this, this, i, j);
      this.world.timings.syncChunkLoadTimer.stopTiming();
    }
    return chunk;
  }
  
  public Chunk getOrCreateChunk(int i, int j)
  {
    Chunk chunk = (Chunk)this.chunks.get(LongHash.toLong(i, j));
    
    chunk = chunk == null ? getChunkAt(i, j) : (!this.world.ad()) && (!this.forceChunkLoad) ? this.emptyChunk : chunk;
    if (chunk == this.emptyChunk) {
      return chunk;
    }
    if ((i != chunk.locX) || (j != chunk.locZ))
    {
      b.error("Chunk (" + chunk.locX + ", " + chunk.locZ + ") stored at  (" + i + ", " + j + ") in world '" + this.world.getWorld().getName() + "'");
      b.error(chunk.getClass().getName());
      Throwable ex = new Throwable();
      ex.fillInStackTrace();
      ex.printStackTrace();
    }
    return chunk;
  }
  
  public Chunk loadChunk(int i, int j)
  {
    if (this.chunkLoader == null) {
      return null;
    }
    try
    {
      Chunk chunk = this.chunkLoader.a(this.world, i, j);
      if (chunk != null)
      {
        chunk.setLastSaved(this.world.getTime());
        if (this.chunkProvider != null)
        {
          this.world.timings.syncChunkLoadStructuresTimer.startTiming();
          this.chunkProvider.recreateStructures(chunk, i, j);
          this.world.timings.syncChunkLoadStructuresTimer.stopTiming();
        }
      }
      return chunk;
    }
    catch (Exception exception)
    {
      b.error("Couldn't load chunk", exception);
    }
    return null;
  }
  
  public void saveChunkNOP(Chunk chunk)
  {
    if (this.chunkLoader != null) {
      try
      {
        this.chunkLoader.b(this.world, chunk);
      }
      catch (Exception exception)
      {
        b.error("Couldn't save entities", exception);
      }
    }
  }
  
  public void saveChunk(Chunk chunk)
  {
    if (this.chunkLoader != null) {
      try
      {
        chunk.setLastSaved(this.world.getTime());
        this.chunkLoader.a(this.world, chunk);
      }
      catch (IOException ioexception)
      {
        b.error("Couldn't save chunk", ioexception);
      }
      catch (ExceptionWorldConflict exceptionworldconflict)
      {
        b.error("Couldn't save chunk; already in use by another instance of Minecraft?", exceptionworldconflict);
      }
    }
  }
  
  /* Error */
  public void getChunkAt(IChunkProvider ichunkprovider, int i, int j)
  {
    // Byte code:
    //   0: aload_0
    //   1: iload_2
    //   2: iload_3
    //   3: invokevirtual 428	net/minecraft/server/v1_8_R3/ChunkProviderServer:getOrCreateChunk	(II)Lnet/minecraft/server/v1_8_R3/Chunk;
    //   6: astore 4
    //   8: aload 4
    //   10: invokevirtual 431	net/minecraft/server/v1_8_R3/Chunk:isDone	()Z
    //   13: ifne +245 -> 258
    //   16: aload 4
    //   18: invokevirtual 434	net/minecraft/server/v1_8_R3/Chunk:n	()V
    //   21: aload_0
    //   22: getfield 73	net/minecraft/server/v1_8_R3/ChunkProviderServer:chunkProvider	Lnet/minecraft/server/v1_8_R3/IChunkProvider;
    //   25: ifnull +233 -> 258
    //   28: aload_0
    //   29: getfield 73	net/minecraft/server/v1_8_R3/ChunkProviderServer:chunkProvider	Lnet/minecraft/server/v1_8_R3/IChunkProvider;
    //   32: aload_1
    //   33: iload_2
    //   34: iload_3
    //   35: invokeinterface 436 4 0
    //   40: iconst_1
    //   41: putstatic 441	net/minecraft/server/v1_8_R3/BlockSand:instaFall	Z
    //   44: new 443	java/util/Random
    //   47: dup
    //   48: invokespecial 444	java/util/Random:<init>	()V
    //   51: astore 5
    //   53: aload 5
    //   55: aload_0
    //   56: getfield 69	net/minecraft/server/v1_8_R3/ChunkProviderServer:world	Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   59: invokevirtual 447	net/minecraft/server/v1_8_R3/WorldServer:getSeed	()J
    //   62: invokevirtual 450	java/util/Random:setSeed	(J)V
    //   65: aload 5
    //   67: invokevirtual 453	java/util/Random:nextLong	()J
    //   70: ldc2_w 454
    //   73: ldiv
    //   74: ldc2_w 454
    //   77: lmul
    //   78: lconst_1
    //   79: ladd
    //   80: lstore 6
    //   82: aload 5
    //   84: invokevirtual 453	java/util/Random:nextLong	()J
    //   87: ldc2_w 454
    //   90: ldiv
    //   91: ldc2_w 454
    //   94: lmul
    //   95: lconst_1
    //   96: ladd
    //   97: lstore 8
    //   99: aload 5
    //   101: iload_2
    //   102: i2l
    //   103: lload 6
    //   105: lmul
    //   106: iload_3
    //   107: i2l
    //   108: lload 8
    //   110: lmul
    //   111: ladd
    //   112: aload_0
    //   113: getfield 69	net/minecraft/server/v1_8_R3/ChunkProviderServer:world	Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   116: invokevirtual 447	net/minecraft/server/v1_8_R3/WorldServer:getSeed	()J
    //   119: lxor
    //   120: invokevirtual 450	java/util/Random:setSeed	(J)V
    //   123: aload_0
    //   124: getfield 69	net/minecraft/server/v1_8_R3/ChunkProviderServer:world	Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   127: invokevirtual 346	net/minecraft/server/v1_8_R3/WorldServer:getWorld	()Lorg/bukkit/craftbukkit/v1_8_R3/CraftWorld;
    //   130: astore 10
    //   132: aload 10
    //   134: ifnull +88 -> 222
    //   137: aload_0
    //   138: getfield 69	net/minecraft/server/v1_8_R3/ChunkProviderServer:world	Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   141: iconst_1
    //   142: putfield 458	net/minecraft/server/v1_8_R3/WorldServer:populating	Z
    //   145: aload 10
    //   147: invokeinterface 464 1 0
    //   152: invokeinterface 467 1 0
    //   157: astore 11
    //   159: goto +29 -> 188
    //   162: aload 11
    //   164: invokeinterface 139 1 0
    //   169: checkcast 469	org/bukkit/generator/BlockPopulator
    //   172: astore 12
    //   174: aload 12
    //   176: aload 10
    //   178: aload 5
    //   180: aload 4
    //   182: getfield 286	net/minecraft/server/v1_8_R3/Chunk:bukkitChunk	Lorg/bukkit/Chunk;
    //   185: invokevirtual 473	org/bukkit/generator/BlockPopulator:populate	(Lorg/bukkit/World;Ljava/util/Random;Lorg/bukkit/Chunk;)V
    //   188: aload 11
    //   190: invokeinterface 150 1 0
    //   195: ifne -33 -> 162
    //   198: goto +16 -> 214
    //   201: astore 13
    //   203: aload_0
    //   204: getfield 69	net/minecraft/server/v1_8_R3/ChunkProviderServer:world	Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   207: iconst_0
    //   208: putfield 458	net/minecraft/server/v1_8_R3/WorldServer:populating	Z
    //   211: aload 13
    //   213: athrow
    //   214: aload_0
    //   215: getfield 69	net/minecraft/server/v1_8_R3/ChunkProviderServer:world	Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   218: iconst_0
    //   219: putfield 458	net/minecraft/server/v1_8_R3/WorldServer:populating	Z
    //   222: iconst_0
    //   223: putstatic 441	net/minecraft/server/v1_8_R3/BlockSand:instaFall	Z
    //   226: aload_0
    //   227: getfield 69	net/minecraft/server/v1_8_R3/ChunkProviderServer:world	Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   230: invokevirtual 274	net/minecraft/server/v1_8_R3/WorldServer:getServer	()Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   233: invokevirtual 476	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getPluginManager	()Lorg/bukkit/plugin/PluginManager;
    //   236: new 478	org/bukkit/event/world/ChunkPopulateEvent
    //   239: dup
    //   240: aload 4
    //   242: getfield 286	net/minecraft/server/v1_8_R3/Chunk:bukkitChunk	Lorg/bukkit/Chunk;
    //   245: invokespecial 481	org/bukkit/event/world/ChunkPopulateEvent:<init>	(Lorg/bukkit/Chunk;)V
    //   248: invokeinterface 295 2 0
    //   253: aload 4
    //   255: invokevirtual 483	net/minecraft/server/v1_8_R3/Chunk:e	()V
    //   258: return
    // Line number table:
    //   Java source line #262	-> byte code offset #0
    //   Java source line #264	-> byte code offset #8
    //   Java source line #265	-> byte code offset #16
    //   Java source line #266	-> byte code offset #21
    //   Java source line #267	-> byte code offset #28
    //   Java source line #270	-> byte code offset #40
    //   Java source line #271	-> byte code offset #44
    //   Java source line #272	-> byte code offset #53
    //   Java source line #273	-> byte code offset #65
    //   Java source line #274	-> byte code offset #82
    //   Java source line #275	-> byte code offset #99
    //   Java source line #277	-> byte code offset #123
    //   Java source line #278	-> byte code offset #132
    //   Java source line #279	-> byte code offset #137
    //   Java source line #281	-> byte code offset #145
    //   Java source line #282	-> byte code offset #174
    //   Java source line #281	-> byte code offset #188
    //   Java source line #284	-> byte code offset #198
    //   Java source line #285	-> byte code offset #203
    //   Java source line #286	-> byte code offset #211
    //   Java source line #285	-> byte code offset #214
    //   Java source line #288	-> byte code offset #222
    //   Java source line #289	-> byte code offset #226
    //   Java source line #292	-> byte code offset #253
    //   Java source line #296	-> byte code offset #258
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	259	0	this	ChunkProviderServer
    //   0	259	1	ichunkprovider	IChunkProvider
    //   0	259	2	i	int
    //   0	259	3	j	int
    //   6	248	4	chunk	Chunk
    //   51	128	5	random	java.util.Random
    //   80	24	6	xRand	long
    //   97	12	8	zRand	long
    //   130	47	10	world	org.bukkit.World
    //   157	32	11	localIterator	Iterator
    //   172	3	12	populator	org.bukkit.generator.BlockPopulator
    //   201	11	13	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   145	201	201	finally
  }
  
  public boolean a(IChunkProvider ichunkprovider, Chunk chunk, int i, int j)
  {
    if ((this.chunkProvider != null) && (this.chunkProvider.a(ichunkprovider, chunk, i, j)))
    {
      Chunk chunk1 = getOrCreateChunk(i, j);
      
      chunk1.e();
      return true;
    }
    return false;
  }
  
  public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
  {
    int i = 0;
    
    Iterator iterator = this.chunks.values().iterator();
    while (iterator.hasNext())
    {
      Chunk chunk = (Chunk)iterator.next();
      if (flag) {
        saveChunkNOP(chunk);
      }
      if (chunk.a(flag))
      {
        saveChunk(chunk);
        chunk.f(false);
        i++;
        if (i != 24) {}
      }
    }
    return true;
  }
  
  public void c()
  {
    if (this.chunkLoader != null) {
      this.chunkLoader.b();
    }
  }
  
  public boolean unloadChunks()
  {
    if (!this.world.savingDisabled)
    {
      Server server = this.world.getServer();
      for (int i = 0; (i < 100) && (!this.unloadQueue.isEmpty()); i++)
      {
        long chunkcoordinates = this.unloadQueue.popFirst();
        Chunk chunk = (Chunk)this.chunks.get(chunkcoordinates);
        if (chunk != null)
        {
          ChunkUnloadEvent event = new ChunkUnloadEvent(chunk.bukkitChunk);
          server.getPluginManager().callEvent(event);
          if (!event.isCancelled())
          {
            if (chunk != null)
            {
              chunk.removeEntities();
              saveChunk(chunk);
              saveChunkNOP(chunk);
              this.chunks.remove(chunkcoordinates);
            }
            for (int x = -2; x < 3; x++) {
              for (int z = -2; z < 3; z++) {
                if ((x != 0) || (z != 0))
                {
                  Chunk neighbor = getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
                  if (neighbor != null)
                  {
                    neighbor.setNeighborUnloaded(-x, -z);
                    chunk.setNeighborUnloaded(x, z);
                  }
                }
              }
            }
          }
        }
      }
      if (this.chunkLoader != null) {
        this.chunkLoader.a();
      }
    }
    return this.chunkProvider.unloadChunks();
  }
  
  public boolean canSave()
  {
    return !this.world.savingDisabled;
  }
  
  public String getName()
  {
    return "ServerChunkCache: " + this.chunks.size() + " Drop: " + this.unloadQueue.size();
  }
  
  public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumcreaturetype, BlockPosition blockposition)
  {
    return this.chunkProvider.getMobsFor(enumcreaturetype, blockposition);
  }
  
  public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockposition)
  {
    return this.chunkProvider.findNearestMapFeature(world, s, blockposition);
  }
  
  public int getLoadedChunks()
  {
    return this.chunks.size();
  }
  
  public void recreateStructures(Chunk chunk, int i, int j) {}
  
  public Chunk getChunkAt(BlockPosition blockposition)
  {
    return getOrCreateChunk(blockposition.getX() >> 4, blockposition.getZ() >> 4);
  }
}
