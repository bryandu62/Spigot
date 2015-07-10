package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_8_R3.chunkio.ChunkIOExecutor;

public class PlayerChunkMap
{
  private static final Logger a = ;
  private final WorldServer world;
  private final List<EntityPlayer> managedPlayers = Lists.newArrayList();
  private final LongHashMap<PlayerChunk> d = new LongHashMap();
  private final Queue<PlayerChunk> e = new ConcurrentLinkedQueue();
  private final Queue<PlayerChunk> f = new ConcurrentLinkedQueue();
  private int g;
  private long h;
  private final int[][] i = { { 1 }, { 0, 1 }, { -1 }, { 0, -1 } };
  private boolean wasNotEmpty;
  
  public PlayerChunkMap(WorldServer worldserver, int viewDistance)
  {
    this.world = worldserver;
    a(viewDistance);
  }
  
  public WorldServer a()
  {
    return this.world;
  }
  
  public void flush()
  {
    long i = this.world.getTime();
    if (i - this.h > 8000L)
    {
      this.h = i;
      
      Iterator iterator = this.f.iterator();
      while (iterator.hasNext())
      {
        PlayerChunk playerchunkmap_playerchunk = (PlayerChunk)iterator.next();
        playerchunkmap_playerchunk.b();
        playerchunkmap_playerchunk.a();
      }
    }
    else
    {
      Iterator iterator = this.e.iterator();
      while (iterator.hasNext())
      {
        PlayerChunk playerchunkmap_playerchunk = (PlayerChunk)iterator.next();
        playerchunkmap_playerchunk.b();
        iterator.remove();
      }
    }
    if (this.managedPlayers.isEmpty())
    {
      if (!this.wasNotEmpty) {
        return;
      }
      WorldProvider worldprovider = this.world.worldProvider;
      if (!worldprovider.e()) {
        this.world.chunkProviderServer.b();
      }
      this.wasNotEmpty = false;
    }
    else
    {
      this.wasNotEmpty = true;
    }
  }
  
  public boolean a(int i, int j)
  {
    long k = i + 2147483647L | j + 2147483647L << 32;
    
    return this.d.getEntry(k) != null;
  }
  
  private PlayerChunk a(int i, int j, boolean flag)
  {
    long k = i + 2147483647L | j + 2147483647L << 32;
    PlayerChunk playerchunkmap_playerchunk = (PlayerChunk)this.d.getEntry(k);
    if ((playerchunkmap_playerchunk == null) && (flag))
    {
      playerchunkmap_playerchunk = new PlayerChunk(i, j);
      this.d.put(k, playerchunkmap_playerchunk);
      this.f.add(playerchunkmap_playerchunk);
    }
    return playerchunkmap_playerchunk;
  }
  
  public final boolean isChunkInUse(int x, int z)
  {
    PlayerChunk pi = a(x, z, false);
    if (pi != null) {
      return pi.b.size() > 0;
    }
    return false;
  }
  
  public void flagDirty(BlockPosition blockposition)
  {
    int i = blockposition.getX() >> 4;
    int j = blockposition.getZ() >> 4;
    PlayerChunk playerchunkmap_playerchunk = a(i, j, false);
    if (playerchunkmap_playerchunk != null) {
      playerchunkmap_playerchunk.a(blockposition.getX() & 0xF, blockposition.getY(), blockposition.getZ() & 0xF);
    }
  }
  
  public void addPlayer(EntityPlayer entityplayer)
  {
    int i = (int)entityplayer.locX >> 4;
    int j = (int)entityplayer.locZ >> 4;
    
    entityplayer.d = entityplayer.locX;
    entityplayer.e = entityplayer.locZ;
    
    List<ChunkCoordIntPair> chunkList = new LinkedList();
    int l;
    for (int k = i - this.g; k <= i + this.g; k++) {
      for (l = j - this.g; l <= j + this.g; l++) {
        chunkList.add(new ChunkCoordIntPair(k, l));
      }
    }
    Collections.sort(chunkList, new ChunkCoordComparator(entityplayer));
    for (ChunkCoordIntPair pair : chunkList) {
      a(pair.x, pair.z, true).a(entityplayer);
    }
    this.managedPlayers.add(entityplayer);
    b(entityplayer);
  }
  
  public void b(EntityPlayer entityplayer)
  {
    ArrayList arraylist = Lists.newArrayList(entityplayer.chunkCoordIntPairQueue);
    int i = 0;
    int j = this.g;
    int k = (int)entityplayer.locX >> 4;
    int l = (int)entityplayer.locZ >> 4;
    int i1 = 0;
    int j1 = 0;
    ChunkCoordIntPair chunkcoordintpair = a(k, l, true).location;
    
    entityplayer.chunkCoordIntPairQueue.clear();
    if (arraylist.contains(chunkcoordintpair)) {
      entityplayer.chunkCoordIntPairQueue.add(chunkcoordintpair);
    }
    for (int k1 = 1; k1 <= j * 2; k1++) {
      for (int l1 = 0; l1 < 2; l1++)
      {
        int[] aint = this.i[(i++ % 4)];
        for (int i2 = 0; i2 < k1; i2++)
        {
          i1 += aint[0];
          j1 += aint[1];
          chunkcoordintpair = a(k + i1, l + j1, true).location;
          if (arraylist.contains(chunkcoordintpair)) {
            entityplayer.chunkCoordIntPairQueue.add(chunkcoordintpair);
          }
        }
      }
    }
    i %= 4;
    for (k1 = 0; k1 < j * 2; k1++)
    {
      i1 += this.i[i][0];
      j1 += this.i[i][1];
      chunkcoordintpair = a(k + i1, l + j1, true).location;
      if (arraylist.contains(chunkcoordintpair)) {
        entityplayer.chunkCoordIntPairQueue.add(chunkcoordintpair);
      }
    }
  }
  
  public void removePlayer(EntityPlayer entityplayer)
  {
    int i = (int)entityplayer.d >> 4;
    int j = (int)entityplayer.e >> 4;
    for (int k = i - this.g; k <= i + this.g; k++) {
      for (int l = j - this.g; l <= j + this.g; l++)
      {
        PlayerChunk playerchunkmap_playerchunk = a(k, l, false);
        if (playerchunkmap_playerchunk != null) {
          playerchunkmap_playerchunk.b(entityplayer);
        }
      }
    }
    this.managedPlayers.remove(entityplayer);
  }
  
  private boolean a(int i, int j, int k, int l, int i1)
  {
    int j1 = i - k;
    int k1 = j - l;
    
    return (k1 >= -i1) && (k1 <= i1);
  }
  
  public void movePlayer(EntityPlayer entityplayer)
  {
    int i = (int)entityplayer.locX >> 4;
    int j = (int)entityplayer.locZ >> 4;
    double d0 = entityplayer.d - entityplayer.locX;
    double d1 = entityplayer.e - entityplayer.locZ;
    double d2 = d0 * d0 + d1 * d1;
    if (d2 >= 64.0D)
    {
      int k = (int)entityplayer.d >> 4;
      int l = (int)entityplayer.e >> 4;
      int i1 = this.g;
      int j1 = i - k;
      int k1 = j - l;
      List<ChunkCoordIntPair> chunksToLoad = new LinkedList();
      if ((j1 != 0) || (k1 != 0))
      {
        int i2;
        for (int l1 = i - i1; l1 <= i + i1; l1++) {
          for (i2 = j - i1; i2 <= j + i1; i2++)
          {
            if (!a(l1, i2, k, l, i1)) {
              chunksToLoad.add(new ChunkCoordIntPair(l1, i2));
            }
            if (!a(l1 - j1, i2 - k1, i, j, i1))
            {
              PlayerChunk playerchunkmap_playerchunk = a(l1 - j1, i2 - k1, false);
              if (playerchunkmap_playerchunk != null) {
                playerchunkmap_playerchunk.b(entityplayer);
              }
            }
          }
        }
        b(entityplayer);
        entityplayer.d = entityplayer.locX;
        entityplayer.e = entityplayer.locZ;
        
        Collections.sort(chunksToLoad, new ChunkCoordComparator(entityplayer));
        for (ChunkCoordIntPair pair : chunksToLoad) {
          a(pair.x, pair.z, true).a(entityplayer);
        }
        if ((j1 > 1) || (j1 < -1) || (k1 > 1) || (k1 < -1)) {
          Collections.sort(entityplayer.chunkCoordIntPairQueue, new ChunkCoordComparator(entityplayer));
        }
      }
    }
  }
  
  public boolean a(EntityPlayer entityplayer, int i, int j)
  {
    PlayerChunk playerchunkmap_playerchunk = a(i, j, false);
    
    return (playerchunkmap_playerchunk != null) && (playerchunkmap_playerchunk.b.contains(entityplayer)) && (!entityplayer.chunkCoordIntPairQueue.contains(playerchunkmap_playerchunk.location));
  }
  
  public void a(int i)
  {
    i = MathHelper.clamp(i, 3, 32);
    if (i != this.g)
    {
      int j = i - this.g;
      ArrayList arraylist = Lists.newArrayList(this.managedPlayers);
      Iterator iterator = arraylist.iterator();
      while (iterator.hasNext())
      {
        EntityPlayer entityplayer = (EntityPlayer)iterator.next();
        int k = (int)entityplayer.locX >> 4;
        int l = (int)entityplayer.locZ >> 4;
        if (j > 0) {
          for (int i1 = k - i; i1 <= k + i; i1++) {
            for (int j1 = l - i; j1 <= l + i; j1++)
            {
              PlayerChunk playerchunkmap_playerchunk = a(i1, j1, true);
              if (!playerchunkmap_playerchunk.b.contains(entityplayer)) {
                playerchunkmap_playerchunk.a(entityplayer);
              }
            }
          }
        } else {
          for (int i1 = k - this.g; i1 <= k + this.g; i1++) {
            for (int j1 = l - this.g; j1 <= l + this.g; j1++) {
              if (!a(i1, j1, k, l, i)) {
                a(i1, j1, true).b(entityplayer);
              }
            }
          }
        }
      }
      this.g = i;
    }
  }
  
  public static int getFurthestViewableBlock(int i)
  {
    return i * 16 - 16;
  }
  
  class PlayerChunk
  {
    private final List<EntityPlayer> b = Lists.newArrayList();
    private final ChunkCoordIntPair location;
    private short[] dirtyBlocks = new short[64];
    private int dirtyCount;
    private int f;
    private long g;
    private final HashMap<EntityPlayer, Runnable> players = new HashMap();
    private boolean loaded = false;
    private Runnable loadedRunnable = new Runnable()
    {
      public void run()
      {
        PlayerChunkMap.PlayerChunk.this.loaded = true;
      }
    };
    
    public PlayerChunk(int i, int j)
    {
      this.location = new ChunkCoordIntPair(i, j);
      PlayerChunkMap.this.a().chunkProviderServer.getChunkAt(i, j, this.loadedRunnable);
    }
    
    public void a(final EntityPlayer entityplayer)
    {
      if (this.b.contains(entityplayer))
      {
        PlayerChunkMap.a.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] { entityplayer, Integer.valueOf(this.location.x), Integer.valueOf(this.location.z) });
      }
      else
      {
        if (this.b.isEmpty()) {
          this.g = PlayerChunkMap.this.world.getTime();
        }
        this.b.add(entityplayer);
        Runnable playerRunnable;
        if (this.loaded)
        {
          Runnable playerRunnable = null;
          entityplayer.chunkCoordIntPairQueue.add(this.location);
        }
        else
        {
          playerRunnable = new Runnable()
          {
            public void run()
            {
              entityplayer.chunkCoordIntPairQueue.add(PlayerChunkMap.PlayerChunk.this.location);
            }
          };
          PlayerChunkMap.this.a().chunkProviderServer.getChunkAt(this.location.x, this.location.z, playerRunnable);
        }
        this.players.put(entityplayer, playerRunnable);
      }
    }
    
    public void b(EntityPlayer entityplayer)
    {
      if (this.b.contains(entityplayer))
      {
        if (!this.loaded)
        {
          ChunkIOExecutor.dropQueuedChunkLoad(PlayerChunkMap.this.a(), this.location.x, this.location.z, (Runnable)this.players.get(entityplayer));
          this.b.remove(entityplayer);
          this.players.remove(entityplayer);
          if (this.b.isEmpty())
          {
            ChunkIOExecutor.dropQueuedChunkLoad(PlayerChunkMap.this.a(), this.location.x, this.location.z, this.loadedRunnable);
            long i = this.location.x + 2147483647L | this.location.z + 2147483647L << 32;
            PlayerChunkMap.this.d.remove(i);
            PlayerChunkMap.this.f.remove(this);
          }
          return;
        }
        Chunk chunk = PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z);
        if (chunk.isReady()) {
          entityplayer.playerConnection.sendPacket(new PacketPlayOutMapChunk(chunk, true, 0));
        }
        this.players.remove(entityplayer);
        this.b.remove(entityplayer);
        entityplayer.chunkCoordIntPairQueue.remove(this.location);
        if (this.b.isEmpty())
        {
          long i = this.location.x + 2147483647L | this.location.z + 2147483647L << 32;
          
          a(chunk);
          PlayerChunkMap.this.d.remove(i);
          PlayerChunkMap.this.f.remove(this);
          if (this.dirtyCount > 0) {
            PlayerChunkMap.this.e.remove(this);
          }
          PlayerChunkMap.this.a().chunkProviderServer.queueUnload(this.location.x, this.location.z);
        }
      }
    }
    
    public void a()
    {
      a(PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z));
    }
    
    private void a(Chunk chunk)
    {
      chunk.c(chunk.w() + PlayerChunkMap.this.world.getTime() - this.g);
      this.g = PlayerChunkMap.this.world.getTime();
    }
    
    public void a(int i, int j, int k)
    {
      if (this.dirtyCount == 0) {
        PlayerChunkMap.this.e.add(this);
      }
      this.f |= 1 << (j >> 4);
      if (this.dirtyCount < 64)
      {
        short short0 = (short)(i << 12 | k << 8 | j);
        for (int l = 0; l < this.dirtyCount; l++) {
          if (this.dirtyBlocks[l] == short0) {
            return;
          }
        }
        this.dirtyBlocks[(this.dirtyCount++)] = short0;
      }
    }
    
    public void a(Packet packet)
    {
      for (int i = 0; i < this.b.size(); i++)
      {
        EntityPlayer entityplayer = (EntityPlayer)this.b.get(i);
        if (!entityplayer.chunkCoordIntPairQueue.contains(this.location)) {
          entityplayer.playerConnection.sendPacket(packet);
        }
      }
    }
    
    public void b()
    {
      if (this.dirtyCount != 0)
      {
        if (this.dirtyCount == 1)
        {
          int i = (this.dirtyBlocks[0] >> 12 & 0xF) + this.location.x * 16;
          int j = this.dirtyBlocks[0] & 0xFF;
          int k = (this.dirtyBlocks[0] >> 8 & 0xF) + this.location.z * 16;
          BlockPosition blockposition = new BlockPosition(i, j, k);
          
          a(new PacketPlayOutBlockChange(PlayerChunkMap.this.world, blockposition));
          if (PlayerChunkMap.this.world.getType(blockposition).getBlock().isTileEntity()) {
            a(PlayerChunkMap.this.world.getTileEntity(blockposition));
          }
        }
        else if (this.dirtyCount == 64)
        {
          int i = this.location.x * 16;
          int j = this.location.z * 16;
          a(new PacketPlayOutMapChunk(PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z), false, this.f));
          for (int k = 0; k < 16; k++) {
            if ((this.f & 1 << k) != 0)
            {
              int l = k << 4;
              List list = PlayerChunkMap.this.world.getTileEntities(i, l, j, i + 16, l + 16, j + 16);
              for (int i1 = 0; i1 < list.size(); i1++) {
                a((TileEntity)list.get(i1));
              }
            }
          }
        }
        else
        {
          a(new PacketPlayOutMultiBlockChange(this.dirtyCount, this.dirtyBlocks, PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z)));
          for (int i = 0; i < this.dirtyCount; i++)
          {
            int j = (this.dirtyBlocks[i] >> 12 & 0xF) + this.location.x * 16;
            int k = this.dirtyBlocks[i] & 0xFF;
            int l = (this.dirtyBlocks[i] >> 8 & 0xF) + this.location.z * 16;
            BlockPosition blockposition1 = new BlockPosition(j, k, l);
            if (PlayerChunkMap.this.world.getType(blockposition1).getBlock().isTileEntity()) {
              a(PlayerChunkMap.this.world.getTileEntity(blockposition1));
            }
          }
        }
        this.dirtyCount = 0;
        this.f = 0;
      }
    }
    
    private void a(TileEntity tileentity)
    {
      if (tileentity != null)
      {
        Packet packet = tileentity.getUpdatePacket();
        if (packet != null) {
          a(packet);
        }
      }
    }
  }
  
  private static class ChunkCoordComparator
    implements Comparator<ChunkCoordIntPair>
  {
    private int x;
    private int z;
    
    public ChunkCoordComparator(EntityPlayer entityplayer)
    {
      this.x = ((int)entityplayer.locX >> 4);
      this.z = ((int)entityplayer.locZ >> 4);
    }
    
    public int compare(ChunkCoordIntPair a, ChunkCoordIntPair b)
    {
      if (a.equals(b)) {
        return 0;
      }
      int ax = a.x - this.x;
      int az = a.z - this.z;
      int bx = b.x - this.x;
      int bz = b.z - this.z;
      
      int result = (ax - bx) * (ax + bx) + (az - bz) * (az + bz);
      if (result != 0) {
        return result;
      }
      if (ax < 0)
      {
        if (bx < 0) {
          return bz - az;
        }
        return -1;
      }
      if (bx < 0) {
        return 1;
      }
      return az - bz;
    }
  }
}
