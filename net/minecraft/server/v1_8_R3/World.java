package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gnu.trove.map.hash.TLongShortHashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings.WorldTimingsHandler;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.ActivationRange;
import org.spigotmc.AntiXray;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotWorldConfig;
import org.spigotmc.TickLimiter;

public abstract class World
  implements IBlockAccess
{
  private int a = 63;
  protected boolean e;
  public final List<Entity> entityList = new ArrayList()
  {
    public Entity remove(int index)
    {
      guard();
      return (Entity)super.remove(index);
    }
    
    public boolean remove(Object o)
    {
      guard();
      return super.remove(o);
    }
    
    private void guard()
    {
      if (World.this.guardEntityList) {
        throw new ConcurrentModificationException();
      }
    }
  };
  protected final List<Entity> g = Lists.newArrayList();
  public final List<TileEntity> h = Lists.newArrayList();
  public final List<TileEntity> tileEntityList = Lists.newArrayList();
  private final List<TileEntity> b = Lists.newArrayList();
  private final List<TileEntity> c = Lists.newArrayList();
  public final List<EntityHuman> players = Lists.newArrayList();
  public final List<Entity> k = Lists.newArrayList();
  protected final IntHashMap<Entity> entitiesById = new IntHashMap();
  private long d = 16777215L;
  private int I;
  protected int m = new Random().nextInt();
  protected final int n = 1013904223;
  protected float o;
  protected float p;
  protected float q;
  protected float r;
  private int J;
  public final Random random = new Random();
  public WorldProvider worldProvider;
  protected List<IWorldAccess> u = Lists.newArrayList();
  protected IChunkProvider chunkProvider;
  protected final IDataManager dataManager;
  public WorldData worldData;
  protected boolean isLoading;
  public PersistentCollection worldMaps;
  protected PersistentVillage villages;
  public final MethodProfiler methodProfiler;
  private final Calendar K = Calendar.getInstance();
  public Scoreboard scoreboard = new Scoreboard();
  public final boolean isClientSide;
  private int L;
  public boolean allowMonsters;
  public boolean allowAnimals;
  private boolean M;
  private final WorldBorder N;
  int[] H;
  private final CraftWorld world;
  public boolean pvpMode;
  public boolean keepSpawnInMemory = true;
  public ChunkGenerator generator;
  public boolean captureBlockStates = false;
  public boolean captureTreeGeneration = false;
  public ArrayList<BlockState> capturedBlockStates = new ArrayList()
  {
    public boolean add(BlockState blockState)
    {
      Iterator<BlockState> blockStateIterator = iterator();
      while (blockStateIterator.hasNext())
      {
        BlockState blockState1 = (BlockState)blockStateIterator.next();
        if (blockState1.getLocation().equals(blockState.getLocation())) {
          return false;
        }
      }
      return super.add(blockState);
    }
  };
  public long ticksPerAnimalSpawns;
  public long ticksPerMonsterSpawns;
  public boolean populating;
  private int tickPosition;
  private boolean guardEntityList;
  protected final TLongShortHashMap chunkTickList;
  protected float growthOdds = 100.0F;
  protected float modifiedOdds = 100.0F;
  private final byte chunkTickRadius;
  public static boolean haveWeSilencedAPhysicsCrash;
  public static String blockLocation;
  private TickLimiter entityLimiter;
  private TickLimiter tileLimiter;
  private int tileTickPosition;
  public final SpigotWorldConfig spigotConfig;
  public final SpigotTimings.WorldTimingsHandler timings;
  
  public static long chunkToKey(int x, int z)
  {
    long k = (x & 0xFFFF0000) << 16 | (x & 0xFFFF) << 0;
    k |= (z & 0xFFFF0000) << 32 | (z & 0xFFFF) << 16;
    return k;
  }
  
  public static int keyToX(long k)
  {
    return (int)(k >> 16 & 0xFFFFFFFFFFFF0000 | k & 0xFFFF);
  }
  
  public static int keyToZ(long k)
  {
    return (int)(k >> 32 & 0xFFFF0000 | k >> 16 & 0xFFFF);
  }
  
  public CraftWorld getWorld()
  {
    return this.world;
  }
  
  public CraftServer getServer()
  {
    return (CraftServer)Bukkit.getServer();
  }
  
  public Chunk getChunkIfLoaded(int x, int z)
  {
    return ((ChunkProviderServer)this.chunkProvider).getChunkIfLoaded(x, z);
  }
  
  protected World(IDataManager idatamanager, WorldData worlddata, WorldProvider worldprovider, MethodProfiler methodprofiler, boolean flag, ChunkGenerator gen, World.Environment env)
  {
    this.spigotConfig = new SpigotWorldConfig(worlddata.getName());
    this.generator = gen;
    this.world = new CraftWorld((WorldServer)this, gen, env);
    this.ticksPerAnimalSpawns = getServer().getTicksPerAnimalSpawns();
    this.ticksPerMonsterSpawns = getServer().getTicksPerMonsterSpawns();
    
    this.chunkTickRadius = ((byte)(getServer().getViewDistance() < 7 ? getServer().getViewDistance() : 7));
    this.chunkTickList = new TLongShortHashMap(this.spigotConfig.chunksPerTick * 5, 0.7F, Long.MIN_VALUE, (short)Short.MIN_VALUE);
    this.chunkTickList.setAutoCompactionFactor(0.0F);
    
    this.L = this.random.nextInt(12000);
    this.allowMonsters = true;
    this.allowAnimals = true;
    this.H = new int[32768];
    this.dataManager = idatamanager;
    this.methodProfiler = methodprofiler;
    this.worldData = worlddata;
    this.worldProvider = worldprovider;
    this.isClientSide = flag;
    this.N = worldprovider.getWorldBorder();
    
    this.N.a(new IWorldBorderListener()
    {
      public void a(WorldBorder worldborder, double d0)
      {
        World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE), World.this);
      }
      
      public void a(WorldBorder worldborder, double d0, double d1, long i)
      {
        World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE), World.this);
      }
      
      public void a(WorldBorder worldborder, double d0, double d1)
      {
        World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER), World.this);
      }
      
      public void a(WorldBorder worldborder, int i)
      {
        World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_TIME), World.this);
      }
      
      public void b(WorldBorder worldborder, int i)
      {
        World.this.getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS), World.this);
      }
      
      public void b(WorldBorder worldborder, double d0) {}
      
      public void c(WorldBorder worldborder, double d0) {}
    });
    getServer().addWorld(this.world);
    
    this.timings = new SpigotTimings.WorldTimingsHandler(this);
    this.entityLimiter = new TickLimiter(this.spigotConfig.entityMaxTickTime);
    this.tileLimiter = new TickLimiter(this.spigotConfig.tileMaxTickTime);
  }
  
  public World b()
  {
    return this;
  }
  
  public BiomeBase getBiome(final BlockPosition blockposition)
  {
    if (isLoaded(blockposition))
    {
      Chunk chunk = getChunkAtWorldCoords(blockposition);
      try
      {
        return chunk.getBiome(blockposition, this.worldProvider.m());
      }
      catch (Throwable throwable)
      {
        CrashReport crashreport = CrashReport.a(throwable, "Getting biome");
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Coordinates of biome request");
        
        crashreportsystemdetails.a("Location", new Callable()
        {
          public String a()
            throws Exception
          {
            return CrashReportSystemDetails.a(blockposition);
          }
          
          public Object call()
            throws Exception
          {
            return a();
          }
        });
        throw new ReportedException(crashreport);
      }
    }
    return this.worldProvider.m().getBiome(blockposition, BiomeBase.PLAINS);
  }
  
  public WorldChunkManager getWorldChunkManager()
  {
    return this.worldProvider.m();
  }
  
  protected abstract IChunkProvider k();
  
  public void a(WorldSettings worldsettings)
  {
    this.worldData.d(true);
  }
  
  public Block c(BlockPosition blockposition)
  {
    for (BlockPosition blockposition1 = new BlockPosition(blockposition.getX(), F(), blockposition.getZ()); !isEmpty(blockposition1.up()); blockposition1 = blockposition1.up()) {}
    return getType(blockposition1).getBlock();
  }
  
  private boolean isValidLocation(BlockPosition blockposition)
  {
    return (blockposition.getX() >= -30000000) && (blockposition.getZ() >= -30000000) && (blockposition.getX() < 30000000) && (blockposition.getZ() < 30000000) && (blockposition.getY() >= 0) && (blockposition.getY() < 256);
  }
  
  public boolean isEmpty(BlockPosition blockposition)
  {
    return getType(blockposition).getBlock().getMaterial() == Material.AIR;
  }
  
  public boolean isLoaded(BlockPosition blockposition)
  {
    return a(blockposition, true);
  }
  
  public boolean a(BlockPosition blockposition, boolean flag)
  {
    return !isValidLocation(blockposition) ? false : isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4, flag);
  }
  
  public boolean areChunksLoaded(BlockPosition blockposition, int i)
  {
    return areChunksLoaded(blockposition, i, true);
  }
  
  public boolean areChunksLoaded(BlockPosition blockposition, int i, boolean flag)
  {
    return isAreaLoaded(blockposition.getX() - i, blockposition.getY() - i, blockposition.getZ() - i, blockposition.getX() + i, blockposition.getY() + i, blockposition.getZ() + i, flag);
  }
  
  public boolean areChunksLoadedBetween(BlockPosition blockposition, BlockPosition blockposition1)
  {
    return areChunksLoadedBetween(blockposition, blockposition1, true);
  }
  
  public boolean areChunksLoadedBetween(BlockPosition blockposition, BlockPosition blockposition1, boolean flag)
  {
    return isAreaLoaded(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), flag);
  }
  
  public boolean a(StructureBoundingBox structureboundingbox)
  {
    return b(structureboundingbox, true);
  }
  
  public boolean b(StructureBoundingBox structureboundingbox, boolean flag)
  {
    return isAreaLoaded(structureboundingbox.a, structureboundingbox.b, structureboundingbox.c, structureboundingbox.d, structureboundingbox.e, structureboundingbox.f, flag);
  }
  
  private boolean isAreaLoaded(int i, int j, int k, int l, int i1, int j1, boolean flag)
  {
    if ((i1 >= 0) && (j < 256))
    {
      i >>= 4;
      k >>= 4;
      l >>= 4;
      j1 >>= 4;
      for (int k1 = i; k1 <= l; k1++) {
        for (int l1 = k; l1 <= j1; l1++) {
          if (!isChunkLoaded(k1, l1, flag)) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }
  
  protected boolean isChunkLoaded(int i, int j, boolean flag)
  {
    return (this.chunkProvider.isChunkLoaded(i, j)) && ((flag) || (!this.chunkProvider.getOrCreateChunk(i, j).isEmpty()));
  }
  
  public Chunk getChunkAtWorldCoords(BlockPosition blockposition)
  {
    return getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
  }
  
  public Chunk getChunkAt(int i, int j)
  {
    return this.chunkProvider.getOrCreateChunk(i, j);
  }
  
  public boolean setTypeAndData(BlockPosition blockposition, IBlockData iblockdata, int i)
  {
    if (this.captureTreeGeneration)
    {
      BlockState blockstate = null;
      Iterator<BlockState> it = this.capturedBlockStates.iterator();
      while (it.hasNext())
      {
        BlockState previous = (BlockState)it.next();
        if ((previous.getX() == blockposition.getX()) && (previous.getY() == blockposition.getY()) && (previous.getZ() == blockposition.getZ()))
        {
          blockstate = previous;
          it.remove();
          break;
        }
      }
      if (blockstate == null) {
        blockstate = CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i);
      }
      blockstate.setTypeId(CraftMagicNumbers.getId(iblockdata.getBlock()));
      blockstate.setRawData((byte)iblockdata.getBlock().toLegacyData(iblockdata));
      this.capturedBlockStates.add(blockstate);
      return true;
    }
    if (!isValidLocation(blockposition)) {
      return false;
    }
    if ((!this.isClientSide) && (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES)) {
      return false;
    }
    Chunk chunk = getChunkAtWorldCoords(blockposition);
    Block block = iblockdata.getBlock();
    
    BlockState blockstate = null;
    if (this.captureBlockStates)
    {
      blockstate = CraftBlockState.getBlockState(this, blockposition.getX(), blockposition.getY(), blockposition.getZ(), i);
      this.capturedBlockStates.add(blockstate);
    }
    IBlockData iblockdata1 = chunk.a(blockposition, iblockdata);
    if (iblockdata1 == null)
    {
      if (this.captureBlockStates) {
        this.capturedBlockStates.remove(blockstate);
      }
      return false;
    }
    Block block1 = iblockdata1.getBlock();
    if ((block.p() != block1.p()) || (block.r() != block1.r()))
    {
      this.methodProfiler.a("checkLight");
      x(blockposition);
      this.methodProfiler.b();
    }
    if (!this.captureBlockStates) {
      notifyAndUpdatePhysics(blockposition, chunk, block1, block, i);
    }
    return true;
  }
  
  public void notifyAndUpdatePhysics(BlockPosition blockposition, Chunk chunk, Block oldBlock, Block newBLock, int flag)
  {
    if (((flag & 0x2) != 0) && ((chunk == null) || (chunk.isReady()))) {
      notify(blockposition);
    }
    if ((!this.isClientSide) && ((flag & 0x1) != 0))
    {
      update(blockposition, oldBlock);
      if (newBLock.isComplexRedstone()) {
        updateAdjacentComparators(blockposition, newBLock);
      }
    }
  }
  
  public boolean setAir(BlockPosition blockposition)
  {
    return setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
  }
  
  public boolean setAir(BlockPosition blockposition, boolean flag)
  {
    IBlockData iblockdata = getType(blockposition);
    Block block = iblockdata.getBlock();
    if (block.getMaterial() == Material.AIR) {
      return false;
    }
    triggerEffect(2001, blockposition, Block.getCombinedId(iblockdata));
    if (flag) {
      block.b(this, blockposition, iblockdata, 0);
    }
    return setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
  }
  
  public boolean setTypeUpdate(BlockPosition blockposition, IBlockData iblockdata)
  {
    return setTypeAndData(blockposition, iblockdata, 3);
  }
  
  public void notify(BlockPosition blockposition)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).a(blockposition);
    }
  }
  
  public void update(BlockPosition blockposition, Block block)
  {
    if (this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES)
    {
      if (this.populating) {
        return;
      }
      applyPhysics(blockposition, block);
    }
  }
  
  public void a(int i, int j, int k, int l)
  {
    if (k > l)
    {
      int i1 = l;
      l = k;
      k = i1;
    }
    if (!this.worldProvider.o()) {
      for (int i1 = k; i1 <= l; i1++) {
        c(EnumSkyBlock.SKY, new BlockPosition(i, i1, j));
      }
    }
    b(i, k, j, i, l, j);
  }
  
  public void b(BlockPosition blockposition, BlockPosition blockposition1)
  {
    b(blockposition.getX(), blockposition.getY(), blockposition.getZ(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
  }
  
  public void b(int i, int j, int k, int l, int i1, int j1)
  {
    for (int k1 = 0; k1 < this.u.size(); k1++) {
      ((IWorldAccess)this.u.get(k1)).a(i, j, k, l, i1, j1);
    }
  }
  
  public void applyPhysics(BlockPosition blockposition, Block block)
  {
    d(blockposition.west(), block);
    d(blockposition.east(), block);
    d(blockposition.down(), block);
    d(blockposition.up(), block);
    d(blockposition.north(), block);
    d(blockposition.south(), block);
    this.spigotConfig.antiXrayInstance.updateNearbyBlocks(this, blockposition);
  }
  
  public void a(BlockPosition blockposition, Block block, EnumDirection enumdirection)
  {
    if (enumdirection != EnumDirection.WEST) {
      d(blockposition.west(), block);
    }
    if (enumdirection != EnumDirection.EAST) {
      d(blockposition.east(), block);
    }
    if (enumdirection != EnumDirection.DOWN) {
      d(blockposition.down(), block);
    }
    if (enumdirection != EnumDirection.UP) {
      d(blockposition.up(), block);
    }
    if (enumdirection != EnumDirection.NORTH) {
      d(blockposition.north(), block);
    }
    if (enumdirection != EnumDirection.SOUTH) {
      d(blockposition.south(), block);
    }
  }
  
  public void d(BlockPosition blockposition, final Block block)
  {
    if (!this.isClientSide)
    {
      IBlockData iblockdata = getType(blockposition);
      try
      {
        CraftWorld world = ((WorldServer)this).getWorld();
        if (world != null)
        {
          BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block));
          getServer().getPluginManager().callEvent(event);
          if (event.isCancelled()) {
            return;
          }
        }
        iblockdata.getBlock().doPhysics(this, blockposition, iblockdata, block);
      }
      catch (StackOverflowError localStackOverflowError)
      {
        haveWeSilencedAPhysicsCrash = true;
        blockLocation = blockposition.getX() + ", " + blockposition.getY() + ", " + blockposition.getZ();
      }
      catch (Throwable throwable)
      {
        CrashReport crashreport = CrashReport.a(throwable, "Exception while updating neighbours");
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being updated");
        
        crashreportsystemdetails.a("Source block type", new Callable()
        {
          public String a()
            throws Exception
          {
            try
            {
              return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.getId(block)), block.a(), block.getClass().getCanonicalName() });
            }
            catch (Throwable localThrowable) {}
            return "ID #" + Block.getId(block);
          }
          
          public Object call()
            throws Exception
          {
            return a();
          }
        });
        CrashReportSystemDetails.a(crashreportsystemdetails, blockposition, iblockdata);
        throw new ReportedException(crashreport);
      }
    }
  }
  
  public boolean a(BlockPosition blockposition, Block block)
  {
    return false;
  }
  
  public boolean i(BlockPosition blockposition)
  {
    return getChunkAtWorldCoords(blockposition).d(blockposition);
  }
  
  public boolean j(BlockPosition blockposition)
  {
    if (blockposition.getY() >= F()) {
      return i(blockposition);
    }
    BlockPosition blockposition1 = new BlockPosition(blockposition.getX(), F(), blockposition.getZ());
    if (!i(blockposition1)) {
      return false;
    }
    for (blockposition1 = blockposition1.down(); blockposition1.getY() > blockposition.getY(); blockposition1 = blockposition1.down())
    {
      Block block = getType(blockposition1).getBlock();
      if ((block.p() > 0) && (!block.getMaterial().isLiquid())) {
        return false;
      }
    }
    return true;
  }
  
  public int k(BlockPosition blockposition)
  {
    if (blockposition.getY() < 0) {
      return 0;
    }
    if (blockposition.getY() >= 256) {
      blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
    }
    return getChunkAtWorldCoords(blockposition).a(blockposition, 0);
  }
  
  public int getLightLevel(BlockPosition blockposition)
  {
    return c(blockposition, true);
  }
  
  public int c(BlockPosition blockposition, boolean flag)
  {
    if ((blockposition.getX() >= -30000000) && (blockposition.getZ() >= -30000000) && (blockposition.getX() < 30000000) && (blockposition.getZ() < 30000000))
    {
      if ((flag) && (getType(blockposition).getBlock().s()))
      {
        int i = c(blockposition.up(), false);
        int j = c(blockposition.east(), false);
        int k = c(blockposition.west(), false);
        int l = c(blockposition.south(), false);
        int i1 = c(blockposition.north(), false);
        if (j > i) {
          i = j;
        }
        if (k > i) {
          i = k;
        }
        if (l > i) {
          i = l;
        }
        if (i1 > i) {
          i = i1;
        }
        return i;
      }
      if (blockposition.getY() < 0) {
        return 0;
      }
      if (blockposition.getY() >= 256) {
        blockposition = new BlockPosition(blockposition.getX(), 255, blockposition.getZ());
      }
      Chunk chunk = getChunkAtWorldCoords(blockposition);
      
      return chunk.a(blockposition, this.I);
    }
    return 15;
  }
  
  public BlockPosition getHighestBlockYAt(BlockPosition blockposition)
  {
    int i;
    int i;
    if ((blockposition.getX() >= -30000000) && (blockposition.getZ() >= -30000000) && (blockposition.getX() < 30000000) && (blockposition.getZ() < 30000000))
    {
      int i;
      if (isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4, true)) {
        i = getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4).b(blockposition.getX() & 0xF, blockposition.getZ() & 0xF);
      } else {
        i = 0;
      }
    }
    else
    {
      i = F() + 1;
    }
    return new BlockPosition(blockposition.getX(), i, blockposition.getZ());
  }
  
  public int b(int i, int j)
  {
    if ((i >= -30000000) && (j >= -30000000) && (i < 30000000) && (j < 30000000))
    {
      if (!isChunkLoaded(i >> 4, j >> 4, true)) {
        return 0;
      }
      Chunk chunk = getChunkAt(i >> 4, j >> 4);
      
      return chunk.v();
    }
    return F() + 1;
  }
  
  public int b(EnumSkyBlock enumskyblock, BlockPosition blockposition)
  {
    if (blockposition.getY() < 0) {
      blockposition = new BlockPosition(blockposition.getX(), 0, blockposition.getZ());
    }
    if (!isValidLocation(blockposition)) {
      return enumskyblock.c;
    }
    if (!isLoaded(blockposition)) {
      return enumskyblock.c;
    }
    Chunk chunk = getChunkAtWorldCoords(blockposition);
    
    return chunk.getBrightness(enumskyblock, blockposition);
  }
  
  public void a(EnumSkyBlock enumskyblock, BlockPosition blockposition, int i)
  {
    if ((isValidLocation(blockposition)) && 
      (isLoaded(blockposition)))
    {
      Chunk chunk = getChunkAtWorldCoords(blockposition);
      
      chunk.a(enumskyblock, blockposition, i);
      n(blockposition);
    }
  }
  
  public void n(BlockPosition blockposition)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).b(blockposition);
    }
  }
  
  public float o(BlockPosition blockposition)
  {
    return this.worldProvider.p()[getLightLevel(blockposition)];
  }
  
  public IBlockData getType(BlockPosition blockposition)
  {
    return getType(blockposition, true);
  }
  
  public IBlockData getType(BlockPosition blockposition, boolean useCaptured)
  {
    if ((this.captureTreeGeneration) && (useCaptured))
    {
      Iterator<BlockState> it = this.capturedBlockStates.iterator();
      while (it.hasNext())
      {
        BlockState previous = (BlockState)it.next();
        if ((previous.getX() == blockposition.getX()) && (previous.getY() == blockposition.getY()) && (previous.getZ() == blockposition.getZ())) {
          return CraftMagicNumbers.getBlock(previous.getTypeId()).fromLegacyData(previous.getRawData());
        }
      }
    }
    if (!isValidLocation(blockposition)) {
      return Blocks.AIR.getBlockData();
    }
    Chunk chunk = getChunkAtWorldCoords(blockposition);
    
    return chunk.getBlockData(blockposition);
  }
  
  public boolean w()
  {
    return this.I < 4;
  }
  
  public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1)
  {
    return rayTrace(vec3d, vec3d1, false, false, false);
  }
  
  public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag)
  {
    return rayTrace(vec3d, vec3d1, flag, false, false);
  }
  
  public MovingObjectPosition rayTrace(Vec3D vec3d, Vec3D vec3d1, boolean flag, boolean flag1, boolean flag2)
  {
    if ((!Double.isNaN(vec3d.a)) && (!Double.isNaN(vec3d.b)) && (!Double.isNaN(vec3d.c)))
    {
      if ((!Double.isNaN(vec3d1.a)) && (!Double.isNaN(vec3d1.b)) && (!Double.isNaN(vec3d1.c)))
      {
        int i = MathHelper.floor(vec3d1.a);
        int j = MathHelper.floor(vec3d1.b);
        int k = MathHelper.floor(vec3d1.c);
        int l = MathHelper.floor(vec3d.a);
        int i1 = MathHelper.floor(vec3d.b);
        int j1 = MathHelper.floor(vec3d.c);
        BlockPosition blockposition = new BlockPosition(l, i1, j1);
        IBlockData iblockdata = getType(blockposition);
        Block block = iblockdata.getBlock();
        if (((!flag1) || (block.a(this, blockposition, iblockdata) != null)) && (block.a(iblockdata, flag)))
        {
          MovingObjectPosition movingobjectposition = block.a(this, blockposition, vec3d, vec3d1);
          if (movingobjectposition != null) {
            return movingobjectposition;
          }
        }
        MovingObjectPosition movingobjectposition1 = null;
        int k1 = 200;
        while (k1-- >= 0)
        {
          if ((Double.isNaN(vec3d.a)) || (Double.isNaN(vec3d.b)) || (Double.isNaN(vec3d.c))) {
            return null;
          }
          if ((l == i) && (i1 == j) && (j1 == k)) {
            return flag2 ? movingobjectposition1 : null;
          }
          boolean flag3 = true;
          boolean flag4 = true;
          boolean flag5 = true;
          double d0 = 999.0D;
          double d1 = 999.0D;
          double d2 = 999.0D;
          if (i > l) {
            d0 = l + 1.0D;
          } else if (i < l) {
            d0 = l + 0.0D;
          } else {
            flag3 = false;
          }
          if (j > i1) {
            d1 = i1 + 1.0D;
          } else if (j < i1) {
            d1 = i1 + 0.0D;
          } else {
            flag4 = false;
          }
          if (k > j1) {
            d2 = j1 + 1.0D;
          } else if (k < j1) {
            d2 = j1 + 0.0D;
          } else {
            flag5 = false;
          }
          double d3 = 999.0D;
          double d4 = 999.0D;
          double d5 = 999.0D;
          double d6 = vec3d1.a - vec3d.a;
          double d7 = vec3d1.b - vec3d.b;
          double d8 = vec3d1.c - vec3d.c;
          if (flag3) {
            d3 = (d0 - vec3d.a) / d6;
          }
          if (flag4) {
            d4 = (d1 - vec3d.b) / d7;
          }
          if (flag5) {
            d5 = (d2 - vec3d.c) / d8;
          }
          if (d3 == -0.0D) {
            d3 = -1.0E-4D;
          }
          if (d4 == -0.0D) {
            d4 = -1.0E-4D;
          }
          if (d5 == -0.0D) {
            d5 = -1.0E-4D;
          }
          EnumDirection enumdirection;
          if ((d3 < d4) && (d3 < d5))
          {
            EnumDirection enumdirection = i > l ? EnumDirection.WEST : EnumDirection.EAST;
            vec3d = new Vec3D(d0, vec3d.b + d7 * d3, vec3d.c + d8 * d3);
          }
          else if (d4 < d5)
          {
            EnumDirection enumdirection = j > i1 ? EnumDirection.DOWN : EnumDirection.UP;
            vec3d = new Vec3D(vec3d.a + d6 * d4, d1, vec3d.c + d8 * d4);
          }
          else
          {
            enumdirection = k > j1 ? EnumDirection.NORTH : EnumDirection.SOUTH;
            vec3d = new Vec3D(vec3d.a + d6 * d5, vec3d.b + d7 * d5, d2);
          }
          l = MathHelper.floor(vec3d.a) - (enumdirection == EnumDirection.EAST ? 1 : 0);
          i1 = MathHelper.floor(vec3d.b) - (enumdirection == EnumDirection.UP ? 1 : 0);
          j1 = MathHelper.floor(vec3d.c) - (enumdirection == EnumDirection.SOUTH ? 1 : 0);
          blockposition = new BlockPosition(l, i1, j1);
          IBlockData iblockdata1 = getType(blockposition);
          Block block1 = iblockdata1.getBlock();
          if ((!flag1) || (block1.a(this, blockposition, iblockdata1) != null)) {
            if (block1.a(iblockdata1, flag))
            {
              MovingObjectPosition movingobjectposition2 = block1.a(this, blockposition, vec3d, vec3d1);
              if (movingobjectposition2 != null) {
                return movingobjectposition2;
              }
            }
            else
            {
              movingobjectposition1 = new MovingObjectPosition(MovingObjectPosition.EnumMovingObjectType.MISS, vec3d, enumdirection, blockposition);
            }
          }
        }
        return flag2 ? movingobjectposition1 : null;
      }
      return null;
    }
    return null;
  }
  
  public void makeSound(Entity entity, String s, float f, float f1)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).a(s, entity.locX, entity.locY, entity.locZ, f, f1);
    }
  }
  
  public void a(EntityHuman entityhuman, String s, float f, float f1)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).a(entityhuman, s, entityhuman.locX, entityhuman.locY, entityhuman.locZ, f, f1);
    }
  }
  
  public void makeSound(double d0, double d1, double d2, String s, float f, float f1)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).a(s, d0, d1, d2, f, f1);
    }
  }
  
  public void a(double d0, double d1, double d2, String s, float f, float f1, boolean flag) {}
  
  public void a(BlockPosition blockposition, String s)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).a(s, blockposition);
    }
  }
  
  public void addParticle(EnumParticle enumparticle, double d0, double d1, double d2, double d3, double d4, double d5, int... aint)
  {
    a(enumparticle.c(), enumparticle.e(), d0, d1, d2, d3, d4, d5, aint);
  }
  
  private void a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint)
  {
    for (int j = 0; j < this.u.size(); j++) {
      ((IWorldAccess)this.u.get(j)).a(i, flag, d0, d1, d2, d3, d4, d5, aint);
    }
  }
  
  public boolean strikeLightning(Entity entity)
  {
    this.k.add(entity);
    return true;
  }
  
  public boolean addEntity(Entity entity)
  {
    return addEntity(entity, CreatureSpawnEvent.SpawnReason.DEFAULT);
  }
  
  public boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason)
  {
    AsyncCatcher.catchOp("entity add");
    if (entity == null) {
      return false;
    }
    int i = MathHelper.floor(entity.locX / 16.0D);
    int j = MathHelper.floor(entity.locZ / 16.0D);
    boolean flag = entity.attachedToPlayer;
    if ((entity instanceof EntityHuman)) {
      flag = true;
    }
    Cancellable event = null;
    if (((entity instanceof EntityLiving)) && (!(entity instanceof EntityPlayer)))
    {
      boolean isAnimal = ((entity instanceof EntityAnimal)) || ((entity instanceof EntityWaterAnimal)) || ((entity instanceof EntityGolem));
      boolean isMonster = ((entity instanceof EntityMonster)) || ((entity instanceof EntityGhast)) || ((entity instanceof EntitySlime));
      if ((spawnReason != CreatureSpawnEvent.SpawnReason.CUSTOM) && (
        ((isAnimal) && (!this.allowAnimals)) || ((isMonster) && (!this.allowMonsters))))
      {
        entity.dead = true;
        return false;
      }
      event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving)entity, spawnReason);
    }
    else if ((entity instanceof EntityItem))
    {
      event = CraftEventFactory.callItemSpawnEvent((EntityItem)entity);
    }
    else if ((entity.getBukkitEntity() instanceof Projectile))
    {
      event = CraftEventFactory.callProjectileLaunchEvent(entity);
    }
    else if ((entity instanceof EntityExperienceOrb))
    {
      EntityExperienceOrb xp = (EntityExperienceOrb)entity;
      double radius = this.spigotConfig.expMerge;
      if (radius > 0.0D)
      {
        List<Entity> entities = getEntities(entity, entity.getBoundingBox().grow(radius, radius, radius));
        for (Entity e : entities) {
          if ((e instanceof EntityExperienceOrb))
          {
            EntityExperienceOrb loopItem = (EntityExperienceOrb)e;
            if (!loopItem.dead)
            {
              xp.value += loopItem.value;
              loopItem.die();
            }
          }
        }
      }
    }
    if ((event != null) && ((event.isCancelled()) || (entity.dead)))
    {
      entity.dead = true;
      return false;
    }
    if ((!flag) && (!isChunkLoaded(i, j, true)))
    {
      entity.dead = true;
      return false;
    }
    if ((entity instanceof EntityHuman))
    {
      EntityHuman entityhuman = (EntityHuman)entity;
      
      this.players.add(entityhuman);
      everyoneSleeping();
    }
    getChunkAt(i, j).a(entity);
    this.entityList.add(entity);
    a(entity);
    return true;
  }
  
  protected void a(Entity entity)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).a(entity);
    }
    entity.valid = true;
  }
  
  protected void b(Entity entity)
  {
    for (int i = 0; i < this.u.size(); i++) {
      ((IWorldAccess)this.u.get(i)).b(entity);
    }
    entity.valid = false;
  }
  
  public void kill(Entity entity)
  {
    if (entity.passenger != null) {
      entity.passenger.mount(null);
    }
    if (entity.vehicle != null) {
      entity.mount(null);
    }
    entity.die();
    if ((entity instanceof EntityHuman))
    {
      this.players.remove(entity);
      for (Object o : this.worldMaps.c) {
        if ((o instanceof WorldMap))
        {
          WorldMap map = (WorldMap)o;
          map.i.remove(entity);
          for (Iterator<WorldMap.WorldMapHumanTracker> iter = map.g.iterator(); iter.hasNext();) {
            if (((WorldMap.WorldMapHumanTracker)iter.next()).trackee == entity) {
              iter.remove();
            }
          }
        }
      }
      everyoneSleeping();
      b(entity);
    }
  }
  
  public void removeEntity(Entity entity)
  {
    AsyncCatcher.catchOp("entity remove");
    entity.die();
    if ((entity instanceof EntityHuman))
    {
      this.players.remove(entity);
      everyoneSleeping();
    }
    if (!this.guardEntityList)
    {
      int i = entity.ae;
      int j = entity.ag;
      if ((entity.ad) && (isChunkLoaded(i, j, true))) {
        getChunkAt(i, j).b(entity);
      }
      int index = this.entityList.indexOf(entity);
      if (index != -1)
      {
        if (index <= this.tickPosition) {
          this.tickPosition -= 1;
        }
        this.entityList.remove(index);
      }
    }
    b(entity);
  }
  
  public void addIWorldAccess(IWorldAccess iworldaccess)
  {
    this.u.add(iworldaccess);
  }
  
  public List<AxisAlignedBB> getCubes(Entity entity, AxisAlignedBB axisalignedbb)
  {
    ArrayList arraylist = Lists.newArrayList();
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d + 1.0D);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e + 1.0D);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
    WorldBorder worldborder = getWorldBorder();
    boolean flag = entity.aT();
    boolean flag1 = a(worldborder, entity);
    Blocks.STONE.getBlockData();
    new BlockPosition.MutableBlockPosition();
    
    int ystart = k - 1 < 0 ? 0 : k - 1;
    for (int chunkx = i >> 4; chunkx <= j - 1 >> 4; chunkx++)
    {
      int cx = chunkx << 4;
      for (int chunkz = i1 >> 4; chunkz <= j1 - 1 >> 4; chunkz++) {
        if (isChunkLoaded(chunkx, chunkz, true))
        {
          int cz = chunkz << 4;
          Chunk chunk = getChunkAt(chunkx, chunkz);
          
          int xstart = i < cx ? cx : i;
          int xend = j < cx + 16 ? j : cx + 16;
          int zstart = i1 < cz ? cz : i1;
          int zend = j1 < cz + 16 ? j1 : cz + 16;
          for (int x = xstart; x < xend; x++) {
            for (int z = zstart; z < zend; z++) {
              for (int y = ystart; y < l; y++)
              {
                BlockPosition blockposition = new BlockPosition(x, y, z);
                if ((flag) && (flag1)) {
                  entity.h(false);
                } else if ((!flag) && (!flag1)) {
                  entity.h(true);
                }
                IBlockData block;
                IBlockData block;
                if ((!getWorldBorder().a(blockposition)) && (flag1)) {
                  block = Blocks.STONE.getBlockData();
                } else {
                  block = chunk.getBlockData(blockposition);
                }
                if (block != null) {
                  block.getBlock().a(this, blockposition, block, axisalignedbb, arraylist, entity);
                }
              }
            }
          }
        }
      }
    }
    double d0 = 0.25D;
    List list = getEntities(entity, axisalignedbb.grow(d0, d0, d0));
    for (int j2 = 0; j2 < list.size(); j2++) {
      if ((entity.passenger != list) && (entity.vehicle != list))
      {
        AxisAlignedBB axisalignedbb1 = ((Entity)list.get(j2)).S();
        if ((axisalignedbb1 != null) && (axisalignedbb1.b(axisalignedbb))) {
          arraylist.add(axisalignedbb1);
        }
        axisalignedbb1 = entity.j((Entity)list.get(j2));
        if ((axisalignedbb1 != null) && (axisalignedbb1.b(axisalignedbb))) {
          arraylist.add(axisalignedbb1);
        }
      }
    }
    return arraylist;
  }
  
  public boolean a(WorldBorder worldborder, Entity entity)
  {
    double d0 = worldborder.b();
    double d1 = worldborder.c();
    double d2 = worldborder.d();
    double d3 = worldborder.e();
    if (entity.aT())
    {
      d0 += 1.0D;
      d1 += 1.0D;
      d2 -= 1.0D;
      d3 -= 1.0D;
    }
    else
    {
      d0 -= 1.0D;
      d1 -= 1.0D;
      d2 += 1.0D;
      d3 += 1.0D;
    }
    return (entity.locX > d0) && (entity.locX < d2) && (entity.locZ > d1) && (entity.locZ < d3);
  }
  
  public List<AxisAlignedBB> a(AxisAlignedBB axisalignedbb)
  {
    ArrayList arraylist = Lists.newArrayList();
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d + 1.0D);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e + 1.0D);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int k1 = i; k1 < j; k1++) {
      for (int l1 = i1; l1 < j1; l1++) {
        if (isLoaded(blockposition_mutableblockposition.c(k1, 64, l1))) {
          for (int i2 = k - 1; i2 < l; i2++)
          {
            blockposition_mutableblockposition.c(k1, i2, l1);
            IBlockData iblockdata;
            IBlockData iblockdata;
            if ((k1 >= -30000000) && (k1 < 30000000) && (l1 >= -30000000) && (l1 < 30000000)) {
              iblockdata = getType(blockposition_mutableblockposition);
            } else {
              iblockdata = Blocks.BEDROCK.getBlockData();
            }
            iblockdata.getBlock().a(this, blockposition_mutableblockposition, iblockdata, axisalignedbb, arraylist, null);
          }
        }
      }
    }
    return arraylist;
  }
  
  public int a(float f)
  {
    float f1 = c(f);
    float f2 = 1.0F - (MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F);
    
    f2 = MathHelper.a(f2, 0.0F, 1.0F);
    f2 = 1.0F - f2;
    f2 = (float)(f2 * (1.0D - j(f) * 5.0F / 16.0D));
    f2 = (float)(f2 * (1.0D - h(f) * 5.0F / 16.0D));
    f2 = 1.0F - f2;
    return (int)(f2 * 11.0F);
  }
  
  public float c(float f)
  {
    return this.worldProvider.a(this.worldData.getDayTime(), f);
  }
  
  public float y()
  {
    return WorldProvider.a[this.worldProvider.a(this.worldData.getDayTime())];
  }
  
  public float d(float f)
  {
    float f1 = c(f);
    
    return f1 * 3.1415927F * 2.0F;
  }
  
  public BlockPosition q(BlockPosition blockposition)
  {
    return getChunkAtWorldCoords(blockposition).h(blockposition);
  }
  
  public BlockPosition r(BlockPosition blockposition)
  {
    Chunk chunk = getChunkAtWorldCoords(blockposition);
    BlockPosition blockposition2;
    for (BlockPosition blockposition1 = new BlockPosition(blockposition.getX(), chunk.g() + 16, blockposition.getZ()); blockposition1.getY() >= 0; blockposition1 = blockposition2)
    {
      blockposition2 = blockposition1.down();
      Material material = chunk.getType(blockposition2).getMaterial();
      if ((material.isSolid()) && (material != Material.LEAVES)) {
        break;
      }
    }
    return blockposition1;
  }
  
  public void a(BlockPosition blockposition, Block block, int i) {}
  
  public void a(BlockPosition blockposition, Block block, int i, int j) {}
  
  public void b(BlockPosition blockposition, Block block, int i, int j) {}
  
  /* Error */
  public void tickEntities()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   4: ldc_w 1398
    //   7: invokevirtual 685	net/minecraft/server/v1_8_R3/MethodProfiler:a	(Ljava/lang/String;)V
    //   10: aload_0
    //   11: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   14: ldc_w 1400
    //   17: invokevirtual 685	net/minecraft/server/v1_8_R3/MethodProfiler:a	(Ljava/lang/String;)V
    //   20: iconst_0
    //   21: istore_1
    //   22: goto +119 -> 141
    //   25: aload_0
    //   26: getfield 236	net/minecraft/server/v1_8_R3/World:k	Ljava/util/List;
    //   29: iload_1
    //   30: invokeinterface 753 2 0
    //   35: checkcast 1045	net/minecraft/server/v1_8_R3/Entity
    //   38: astore_2
    //   39: aload_2
    //   40: ifnonnull +6 -> 46
    //   43: goto +95 -> 138
    //   46: aload_2
    //   47: dup
    //   48: getfield 1403	net/minecraft/server/v1_8_R3/Entity:ticksLived	I
    //   51: iconst_1
    //   52: iadd
    //   53: putfield 1403	net/minecraft/server/v1_8_R3/Entity:ticksLived	I
    //   56: aload_2
    //   57: invokevirtual 1406	net/minecraft/server/v1_8_R3/Entity:t_	()V
    //   60: goto +57 -> 117
    //   63: astore_3
    //   64: aload_3
    //   65: ldc_w 1408
    //   68: invokestatic 449	net/minecraft/server/v1_8_R3/CrashReport:a	(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/server/v1_8_R3/CrashReport;
    //   71: astore 4
    //   73: aload 4
    //   75: ldc_w 1410
    //   78: invokevirtual 454	net/minecraft/server/v1_8_R3/CrashReport:a	(Ljava/lang/String;)Lnet/minecraft/server/v1_8_R3/CrashReportSystemDetails;
    //   81: astore 5
    //   83: aload_2
    //   84: ifnonnull +17 -> 101
    //   87: aload 5
    //   89: ldc_w 1412
    //   92: ldc_w 1414
    //   95: invokevirtual 1417	net/minecraft/server/v1_8_R3/CrashReportSystemDetails:a	(Ljava/lang/String;Ljava/lang/Object;)V
    //   98: goto +9 -> 107
    //   101: aload_2
    //   102: aload 5
    //   104: invokevirtual 1421	net/minecraft/server/v1_8_R3/Entity:appendEntityCrashDetails	(Lnet/minecraft/server/v1_8_R3/CrashReportSystemDetails;)V
    //   107: new 466	net/minecraft/server/v1_8_R3/ReportedException
    //   110: dup
    //   111: aload 4
    //   113: invokespecial 469	net/minecraft/server/v1_8_R3/ReportedException:<init>	(Lnet/minecraft/server/v1_8_R3/CrashReport;)V
    //   116: athrow
    //   117: aload_2
    //   118: getfield 1139	net/minecraft/server/v1_8_R3/Entity:dead	Z
    //   121: ifeq +17 -> 138
    //   124: aload_0
    //   125: getfield 236	net/minecraft/server/v1_8_R3/World:k	Ljava/util/List;
    //   128: iload_1
    //   129: iinc 1 -1
    //   132: invokeinterface 1268 2 0
    //   137: pop
    //   138: iinc 1 1
    //   141: iload_1
    //   142: aload_0
    //   143: getfield 236	net/minecraft/server/v1_8_R3/World:k	Ljava/util/List;
    //   146: invokeinterface 760 1 0
    //   151: if_icmplt -126 -> 25
    //   154: aload_0
    //   155: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   158: ldc_w 1422
    //   161: invokevirtual 1424	net/minecraft/server/v1_8_R3/MethodProfiler:c	(Ljava/lang/String;)V
    //   164: aload_0
    //   165: getfield 216	net/minecraft/server/v1_8_R3/World:entityList	Ljava/util/List;
    //   168: aload_0
    //   169: getfield 224	net/minecraft/server/v1_8_R3/World:g	Ljava/util/List;
    //   172: invokeinterface 1428 2 0
    //   177: pop
    //   178: iconst_0
    //   179: istore_1
    //   180: goto +60 -> 240
    //   183: aload_0
    //   184: getfield 224	net/minecraft/server/v1_8_R3/World:g	Ljava/util/List;
    //   187: iload_1
    //   188: invokeinterface 753 2 0
    //   193: checkcast 1045	net/minecraft/server/v1_8_R3/Entity
    //   196: astore_2
    //   197: aload_2
    //   198: getfield 1253	net/minecraft/server/v1_8_R3/Entity:ae	I
    //   201: istore_3
    //   202: aload_2
    //   203: getfield 1256	net/minecraft/server/v1_8_R3/Entity:ag	I
    //   206: istore 6
    //   208: aload_2
    //   209: getfield 1259	net/minecraft/server/v1_8_R3/Entity:ad	Z
    //   212: ifeq +25 -> 237
    //   215: aload_0
    //   216: iload_3
    //   217: iload 6
    //   219: iconst_1
    //   220: invokevirtual 555	net/minecraft/server/v1_8_R3/World:isChunkLoaded	(IIZ)Z
    //   223: ifeq +14 -> 237
    //   226: aload_0
    //   227: iload_3
    //   228: iload 6
    //   230: invokevirtual 610	net/minecraft/server/v1_8_R3/World:getChunkAt	(II)Lnet/minecraft/server/v1_8_R3/Chunk;
    //   233: aload_2
    //   234: invokevirtual 1260	net/minecraft/server/v1_8_R3/Chunk:b	(Lnet/minecraft/server/v1_8_R3/Entity;)V
    //   237: iinc 1 1
    //   240: iload_1
    //   241: aload_0
    //   242: getfield 224	net/minecraft/server/v1_8_R3/World:g	Ljava/util/List;
    //   245: invokeinterface 760 1 0
    //   250: if_icmplt -67 -> 183
    //   253: iconst_0
    //   254: istore_1
    //   255: goto +23 -> 278
    //   258: aload_0
    //   259: aload_0
    //   260: getfield 224	net/minecraft/server/v1_8_R3/World:g	Ljava/util/List;
    //   263: iload_1
    //   264: invokeinterface 753 2 0
    //   269: checkcast 1045	net/minecraft/server/v1_8_R3/Entity
    //   272: invokevirtual 1240	net/minecraft/server/v1_8_R3/World:b	(Lnet/minecraft/server/v1_8_R3/Entity;)V
    //   275: iinc 1 1
    //   278: iload_1
    //   279: aload_0
    //   280: getfield 224	net/minecraft/server/v1_8_R3/World:g	Ljava/util/List;
    //   283: invokeinterface 760 1 0
    //   288: if_icmplt -30 -> 258
    //   291: aload_0
    //   292: getfield 224	net/minecraft/server/v1_8_R3/World:g	Ljava/util/List;
    //   295: invokeinterface 1431 1 0
    //   300: aload_0
    //   301: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   304: ldc_w 1433
    //   307: invokevirtual 1424	net/minecraft/server/v1_8_R3/MethodProfiler:c	(Ljava/lang/String;)V
    //   310: aload_0
    //   311: invokestatic 1438	org/spigotmc/ActivationRange:activateEntities	(Lnet/minecraft/server/v1_8_R3/World;)V
    //   314: aload_0
    //   315: getfield 400	net/minecraft/server/v1_8_R3/World:timings	Lorg/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler;
    //   318: getfield 1442	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler:entityTick	Lorg/spigotmc/CustomTimingsHandler;
    //   321: invokevirtual 1447	org/spigotmc/CustomTimingsHandler:startTiming	()V
    //   324: aload_0
    //   325: iconst_1
    //   326: putfield 1250	net/minecraft/server/v1_8_R3/World:guardEntityList	Z
    //   329: iconst_0
    //   330: istore 7
    //   332: aload_0
    //   333: getfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   336: ifge +8 -> 344
    //   339: aload_0
    //   340: iconst_0
    //   341: putfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   344: aload_0
    //   345: getfield 410	net/minecraft/server/v1_8_R3/World:entityLimiter	Lorg/spigotmc/TickLimiter;
    //   348: invokevirtual 1450	org/spigotmc/TickLimiter:initTick	()V
    //   351: goto +287 -> 638
    //   354: aload_0
    //   355: aload_0
    //   356: getfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   359: aload_0
    //   360: getfield 216	net/minecraft/server/v1_8_R3/World:entityList	Ljava/util/List;
    //   363: invokeinterface 760 1 0
    //   368: if_icmpge +10 -> 378
    //   371: aload_0
    //   372: getfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   375: goto +4 -> 379
    //   378: iconst_0
    //   379: putfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   382: aload_0
    //   383: getfield 216	net/minecraft/server/v1_8_R3/World:entityList	Ljava/util/List;
    //   386: aload_0
    //   387: getfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   390: invokeinterface 753 2 0
    //   395: checkcast 1045	net/minecraft/server/v1_8_R3/Entity
    //   398: astore_2
    //   399: aload_2
    //   400: getfield 1221	net/minecraft/server/v1_8_R3/Entity:vehicle	Lnet/minecraft/server/v1_8_R3/Entity;
    //   403: ifnull +40 -> 443
    //   406: aload_2
    //   407: getfield 1221	net/minecraft/server/v1_8_R3/Entity:vehicle	Lnet/minecraft/server/v1_8_R3/Entity;
    //   410: getfield 1139	net/minecraft/server/v1_8_R3/Entity:dead	Z
    //   413: ifne +17 -> 430
    //   416: aload_2
    //   417: getfield 1221	net/minecraft/server/v1_8_R3/Entity:vehicle	Lnet/minecraft/server/v1_8_R3/Entity;
    //   420: getfield 1215	net/minecraft/server/v1_8_R3/Entity:passenger	Lnet/minecraft/server/v1_8_R3/Entity;
    //   423: aload_2
    //   424: if_acmpne +6 -> 430
    //   427: goto +198 -> 625
    //   430: aload_2
    //   431: getfield 1221	net/minecraft/server/v1_8_R3/Entity:vehicle	Lnet/minecraft/server/v1_8_R3/Entity;
    //   434: aconst_null
    //   435: putfield 1215	net/minecraft/server/v1_8_R3/Entity:passenger	Lnet/minecraft/server/v1_8_R3/Entity;
    //   438: aload_2
    //   439: aconst_null
    //   440: putfield 1221	net/minecraft/server/v1_8_R3/Entity:vehicle	Lnet/minecraft/server/v1_8_R3/Entity;
    //   443: aload_0
    //   444: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   447: ldc_w 1452
    //   450: invokevirtual 685	net/minecraft/server/v1_8_R3/MethodProfiler:a	(Ljava/lang/String;)V
    //   453: aload_2
    //   454: getfield 1139	net/minecraft/server/v1_8_R3/Entity:dead	Z
    //   457: ifne +61 -> 518
    //   460: getstatic 1455	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings:tickEntityTimer	Lorg/spigotmc/CustomTimingsHandler;
    //   463: invokevirtual 1447	org/spigotmc/CustomTimingsHandler:startTiming	()V
    //   466: aload_0
    //   467: aload_2
    //   468: invokevirtual 1457	net/minecraft/server/v1_8_R3/World:g	(Lnet/minecraft/server/v1_8_R3/Entity;)V
    //   471: getstatic 1455	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings:tickEntityTimer	Lorg/spigotmc/CustomTimingsHandler;
    //   474: invokevirtual 1460	org/spigotmc/CustomTimingsHandler:stopTiming	()V
    //   477: goto +41 -> 518
    //   480: astore 8
    //   482: aload 8
    //   484: ldc_w 1408
    //   487: invokestatic 449	net/minecraft/server/v1_8_R3/CrashReport:a	(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/server/v1_8_R3/CrashReport;
    //   490: astore 4
    //   492: aload 4
    //   494: ldc_w 1410
    //   497: invokevirtual 454	net/minecraft/server/v1_8_R3/CrashReport:a	(Ljava/lang/String;)Lnet/minecraft/server/v1_8_R3/CrashReportSystemDetails;
    //   500: astore 5
    //   502: aload_2
    //   503: aload 5
    //   505: invokevirtual 1421	net/minecraft/server/v1_8_R3/Entity:appendEntityCrashDetails	(Lnet/minecraft/server/v1_8_R3/CrashReportSystemDetails;)V
    //   508: new 466	net/minecraft/server/v1_8_R3/ReportedException
    //   511: dup
    //   512: aload 4
    //   514: invokespecial 469	net/minecraft/server/v1_8_R3/ReportedException:<init>	(Lnet/minecraft/server/v1_8_R3/CrashReport;)V
    //   517: athrow
    //   518: aload_0
    //   519: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   522: invokevirtual 689	net/minecraft/server/v1_8_R3/MethodProfiler:b	()V
    //   525: aload_0
    //   526: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   529: ldc_w 1422
    //   532: invokevirtual 685	net/minecraft/server/v1_8_R3/MethodProfiler:a	(Ljava/lang/String;)V
    //   535: aload_2
    //   536: getfield 1139	net/minecraft/server/v1_8_R3/Entity:dead	Z
    //   539: ifeq +79 -> 618
    //   542: aload_2
    //   543: getfield 1253	net/minecraft/server/v1_8_R3/Entity:ae	I
    //   546: istore_3
    //   547: aload_2
    //   548: getfield 1256	net/minecraft/server/v1_8_R3/Entity:ag	I
    //   551: istore 6
    //   553: aload_2
    //   554: getfield 1259	net/minecraft/server/v1_8_R3/Entity:ad	Z
    //   557: ifeq +25 -> 582
    //   560: aload_0
    //   561: iload_3
    //   562: iload 6
    //   564: iconst_1
    //   565: invokevirtual 555	net/minecraft/server/v1_8_R3/World:isChunkLoaded	(IIZ)Z
    //   568: ifeq +14 -> 582
    //   571: aload_0
    //   572: iload_3
    //   573: iload 6
    //   575: invokevirtual 610	net/minecraft/server/v1_8_R3/World:getChunkAt	(II)Lnet/minecraft/server/v1_8_R3/Chunk;
    //   578: aload_2
    //   579: invokevirtual 1260	net/minecraft/server/v1_8_R3/Chunk:b	(Lnet/minecraft/server/v1_8_R3/Entity;)V
    //   582: aload_0
    //   583: iconst_0
    //   584: putfield 1250	net/minecraft/server/v1_8_R3/World:guardEntityList	Z
    //   587: aload_0
    //   588: getfield 216	net/minecraft/server/v1_8_R3/World:entityList	Ljava/util/List;
    //   591: aload_0
    //   592: dup
    //   593: getfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   596: dup_x1
    //   597: iconst_1
    //   598: isub
    //   599: putfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   602: invokeinterface 1268 2 0
    //   607: pop
    //   608: aload_0
    //   609: iconst_1
    //   610: putfield 1250	net/minecraft/server/v1_8_R3/World:guardEntityList	Z
    //   613: aload_0
    //   614: aload_2
    //   615: invokevirtual 1240	net/minecraft/server/v1_8_R3/World:b	(Lnet/minecraft/server/v1_8_R3/Entity;)V
    //   618: aload_0
    //   619: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   622: invokevirtual 689	net/minecraft/server/v1_8_R3/MethodProfiler:b	()V
    //   625: aload_0
    //   626: dup
    //   627: getfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   630: iconst_1
    //   631: iadd
    //   632: putfield 1266	net/minecraft/server/v1_8_R3/World:tickPosition	I
    //   635: iinc 7 1
    //   638: iload 7
    //   640: aload_0
    //   641: getfield 216	net/minecraft/server/v1_8_R3/World:entityList	Ljava/util/List;
    //   644: invokeinterface 760 1 0
    //   649: if_icmpge +21 -> 670
    //   652: iload 7
    //   654: bipush 10
    //   656: irem
    //   657: ifeq -303 -> 354
    //   660: aload_0
    //   661: getfield 410	net/minecraft/server/v1_8_R3/World:entityLimiter	Lorg/spigotmc/TickLimiter;
    //   664: invokevirtual 1463	org/spigotmc/TickLimiter:shouldContinue	()Z
    //   667: ifne -313 -> 354
    //   670: aload_0
    //   671: iconst_0
    //   672: putfield 1250	net/minecraft/server/v1_8_R3/World:guardEntityList	Z
    //   675: aload_0
    //   676: getfield 400	net/minecraft/server/v1_8_R3/World:timings	Lorg/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler;
    //   679: getfield 1442	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler:entityTick	Lorg/spigotmc/CustomTimingsHandler;
    //   682: invokevirtual 1460	org/spigotmc/CustomTimingsHandler:stopTiming	()V
    //   685: aload_0
    //   686: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   689: ldc_w 1465
    //   692: invokevirtual 1424	net/minecraft/server/v1_8_R3/MethodProfiler:c	(Ljava/lang/String;)V
    //   695: aload_0
    //   696: getfield 400	net/minecraft/server/v1_8_R3/World:timings	Lorg/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler;
    //   699: getfield 1468	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler:tileEntityTick	Lorg/spigotmc/CustomTimingsHandler;
    //   702: invokevirtual 1447	org/spigotmc/CustomTimingsHandler:startTiming	()V
    //   705: aload_0
    //   706: iconst_1
    //   707: putfield 1470	net/minecraft/server/v1_8_R3/World:M	Z
    //   710: aload_0
    //   711: getfield 232	net/minecraft/server/v1_8_R3/World:c	Ljava/util/List;
    //   714: invokeinterface 1471 1 0
    //   719: ifne +40 -> 759
    //   722: aload_0
    //   723: getfield 228	net/minecraft/server/v1_8_R3/World:tileEntityList	Ljava/util/List;
    //   726: aload_0
    //   727: getfield 232	net/minecraft/server/v1_8_R3/World:c	Ljava/util/List;
    //   730: invokeinterface 1428 2 0
    //   735: pop
    //   736: aload_0
    //   737: getfield 226	net/minecraft/server/v1_8_R3/World:h	Ljava/util/List;
    //   740: aload_0
    //   741: getfield 232	net/minecraft/server/v1_8_R3/World:c	Ljava/util/List;
    //   744: invokeinterface 1428 2 0
    //   749: pop
    //   750: aload_0
    //   751: getfield 232	net/minecraft/server/v1_8_R3/World:c	Ljava/util/List;
    //   754: invokeinterface 1431 1 0
    //   759: iconst_0
    //   760: istore 8
    //   762: aload_0
    //   763: getfield 415	net/minecraft/server/v1_8_R3/World:tileLimiter	Lorg/spigotmc/TickLimiter;
    //   766: invokevirtual 1450	org/spigotmc/TickLimiter:initTick	()V
    //   769: goto +305 -> 1074
    //   772: aload_0
    //   773: aload_0
    //   774: getfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   777: aload_0
    //   778: getfield 228	net/minecraft/server/v1_8_R3/World:tileEntityList	Ljava/util/List;
    //   781: invokeinterface 760 1 0
    //   786: if_icmpge +10 -> 796
    //   789: aload_0
    //   790: getfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   793: goto +4 -> 797
    //   796: iconst_0
    //   797: putfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   800: aload_0
    //   801: getfield 228	net/minecraft/server/v1_8_R3/World:tileEntityList	Ljava/util/List;
    //   804: aload_0
    //   805: getfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   808: invokeinterface 753 2 0
    //   813: checkcast 1475	net/minecraft/server/v1_8_R3/TileEntity
    //   816: astore 9
    //   818: aload 9
    //   820: ifnonnull +43 -> 863
    //   823: aload_0
    //   824: invokevirtual 319	net/minecraft/server/v1_8_R3/World:getServer	()Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   827: invokevirtual 1479	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getLogger	()Ljava/util/logging/Logger;
    //   830: ldc_w 1481
    //   833: invokevirtual 1486	java/util/logging/Logger:severe	(Ljava/lang/String;)V
    //   836: iinc 8 -1
    //   839: aload_0
    //   840: getfield 228	net/minecraft/server/v1_8_R3/World:tileEntityList	Ljava/util/List;
    //   843: aload_0
    //   844: dup
    //   845: getfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   848: dup_x1
    //   849: iconst_1
    //   850: isub
    //   851: putfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   854: invokeinterface 1268 2 0
    //   859: pop
    //   860: goto +201 -> 1061
    //   863: aload 9
    //   865: invokevirtual 1488	net/minecraft/server/v1_8_R3/TileEntity:x	()Z
    //   868: ifne +120 -> 988
    //   871: aload 9
    //   873: invokevirtual 1491	net/minecraft/server/v1_8_R3/TileEntity:t	()Z
    //   876: ifeq +112 -> 988
    //   879: aload 9
    //   881: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   884: astore 10
    //   886: aload_0
    //   887: aload 10
    //   889: invokevirtual 432	net/minecraft/server/v1_8_R3/World:isLoaded	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)Z
    //   892: ifeq +96 -> 988
    //   895: aload_0
    //   896: getfield 387	net/minecraft/server/v1_8_R3/World:N	Lnet/minecraft/server/v1_8_R3/WorldBorder;
    //   899: aload 10
    //   901: invokevirtual 1298	net/minecraft/server/v1_8_R3/WorldBorder:a	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)Z
    //   904: ifeq +84 -> 988
    //   907: aload 9
    //   909: getfield 1497	net/minecraft/server/v1_8_R3/TileEntity:tickTimer	Lorg/spigotmc/CustomTimingsHandler;
    //   912: invokevirtual 1447	org/spigotmc/CustomTimingsHandler:startTiming	()V
    //   915: aload 9
    //   917: checkcast 1499	net/minecraft/server/v1_8_R3/IUpdatePlayerListBox
    //   920: invokeinterface 1501 1 0
    //   925: goto +55 -> 980
    //   928: astore 11
    //   930: aload 11
    //   932: ldc_w 1503
    //   935: invokestatic 449	net/minecraft/server/v1_8_R3/CrashReport:a	(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/server/v1_8_R3/CrashReport;
    //   938: astore 12
    //   940: aload 12
    //   942: ldc_w 1505
    //   945: invokevirtual 454	net/minecraft/server/v1_8_R3/CrashReport:a	(Ljava/lang/String;)Lnet/minecraft/server/v1_8_R3/CrashReportSystemDetails;
    //   948: astore 13
    //   950: aload 9
    //   952: aload 13
    //   954: invokevirtual 1507	net/minecraft/server/v1_8_R3/TileEntity:a	(Lnet/minecraft/server/v1_8_R3/CrashReportSystemDetails;)V
    //   957: new 466	net/minecraft/server/v1_8_R3/ReportedException
    //   960: dup
    //   961: aload 12
    //   963: invokespecial 469	net/minecraft/server/v1_8_R3/ReportedException:<init>	(Lnet/minecraft/server/v1_8_R3/CrashReport;)V
    //   966: athrow
    //   967: astore 14
    //   969: aload 9
    //   971: getfield 1497	net/minecraft/server/v1_8_R3/TileEntity:tickTimer	Lorg/spigotmc/CustomTimingsHandler;
    //   974: invokevirtual 1460	org/spigotmc/CustomTimingsHandler:stopTiming	()V
    //   977: aload 14
    //   979: athrow
    //   980: aload 9
    //   982: getfield 1497	net/minecraft/server/v1_8_R3/TileEntity:tickTimer	Lorg/spigotmc/CustomTimingsHandler;
    //   985: invokevirtual 1460	org/spigotmc/CustomTimingsHandler:stopTiming	()V
    //   988: aload 9
    //   990: invokevirtual 1488	net/minecraft/server/v1_8_R3/TileEntity:x	()Z
    //   993: ifeq +68 -> 1061
    //   996: iinc 8 -1
    //   999: aload_0
    //   1000: getfield 228	net/minecraft/server/v1_8_R3/World:tileEntityList	Ljava/util/List;
    //   1003: aload_0
    //   1004: dup
    //   1005: getfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   1008: dup_x1
    //   1009: iconst_1
    //   1010: isub
    //   1011: putfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   1014: invokeinterface 1268 2 0
    //   1019: pop
    //   1020: aload_0
    //   1021: getfield 226	net/minecraft/server/v1_8_R3/World:h	Ljava/util/List;
    //   1024: aload 9
    //   1026: invokeinterface 1223 2 0
    //   1031: pop
    //   1032: aload_0
    //   1033: aload 9
    //   1035: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   1038: invokevirtual 432	net/minecraft/server/v1_8_R3/World:isLoaded	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)Z
    //   1041: ifeq +20 -> 1061
    //   1044: aload_0
    //   1045: aload 9
    //   1047: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   1050: invokevirtual 436	net/minecraft/server/v1_8_R3/World:getChunkAtWorldCoords	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)Lnet/minecraft/server/v1_8_R3/Chunk;
    //   1053: aload 9
    //   1055: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   1058: invokevirtual 1509	net/minecraft/server/v1_8_R3/Chunk:e	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)V
    //   1061: aload_0
    //   1062: dup
    //   1063: getfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   1066: iconst_1
    //   1067: iadd
    //   1068: putfield 1473	net/minecraft/server/v1_8_R3/World:tileTickPosition	I
    //   1071: iinc 8 1
    //   1074: iload 8
    //   1076: aload_0
    //   1077: getfield 228	net/minecraft/server/v1_8_R3/World:tileEntityList	Ljava/util/List;
    //   1080: invokeinterface 760 1 0
    //   1085: if_icmpge +21 -> 1106
    //   1088: iload 8
    //   1090: bipush 10
    //   1092: irem
    //   1093: ifeq -321 -> 772
    //   1096: aload_0
    //   1097: getfield 415	net/minecraft/server/v1_8_R3/World:tileLimiter	Lorg/spigotmc/TickLimiter;
    //   1100: invokevirtual 1463	org/spigotmc/TickLimiter:shouldContinue	()Z
    //   1103: ifne -331 -> 772
    //   1106: aload_0
    //   1107: getfield 400	net/minecraft/server/v1_8_R3/World:timings	Lorg/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler;
    //   1110: getfield 1468	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler:tileEntityTick	Lorg/spigotmc/CustomTimingsHandler;
    //   1113: invokevirtual 1460	org/spigotmc/CustomTimingsHandler:stopTiming	()V
    //   1116: aload_0
    //   1117: getfield 400	net/minecraft/server/v1_8_R3/World:timings	Lorg/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler;
    //   1120: getfield 1512	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler:tileEntityPending	Lorg/spigotmc/CustomTimingsHandler;
    //   1123: invokevirtual 1447	org/spigotmc/CustomTimingsHandler:startTiming	()V
    //   1126: aload_0
    //   1127: iconst_0
    //   1128: putfield 1470	net/minecraft/server/v1_8_R3/World:M	Z
    //   1131: aload_0
    //   1132: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   1135: ldc_w 1514
    //   1138: invokevirtual 1424	net/minecraft/server/v1_8_R3/MethodProfiler:c	(Ljava/lang/String;)V
    //   1141: aload_0
    //   1142: getfield 230	net/minecraft/server/v1_8_R3/World:b	Ljava/util/List;
    //   1145: invokeinterface 1471 1 0
    //   1150: ifne +99 -> 1249
    //   1153: iconst_0
    //   1154: istore 9
    //   1156: goto +70 -> 1226
    //   1159: aload_0
    //   1160: getfield 230	net/minecraft/server/v1_8_R3/World:b	Ljava/util/List;
    //   1163: iload 9
    //   1165: invokeinterface 753 2 0
    //   1170: checkcast 1475	net/minecraft/server/v1_8_R3/TileEntity
    //   1173: astore 10
    //   1175: aload 10
    //   1177: invokevirtual 1488	net/minecraft/server/v1_8_R3/TileEntity:x	()Z
    //   1180: ifne +43 -> 1223
    //   1183: aload_0
    //   1184: aload 10
    //   1186: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   1189: invokevirtual 432	net/minecraft/server/v1_8_R3/World:isLoaded	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)Z
    //   1192: ifeq +22 -> 1214
    //   1195: aload_0
    //   1196: aload 10
    //   1198: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   1201: invokevirtual 436	net/minecraft/server/v1_8_R3/World:getChunkAtWorldCoords	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)Lnet/minecraft/server/v1_8_R3/Chunk;
    //   1204: aload 10
    //   1206: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   1209: aload 10
    //   1211: invokevirtual 1517	net/minecraft/server/v1_8_R3/Chunk:a	(Lnet/minecraft/server/v1_8_R3/BlockPosition;Lnet/minecraft/server/v1_8_R3/TileEntity;)V
    //   1214: aload_0
    //   1215: aload 10
    //   1217: invokevirtual 1494	net/minecraft/server/v1_8_R3/TileEntity:getPosition	()Lnet/minecraft/server/v1_8_R3/BlockPosition;
    //   1220: invokevirtual 712	net/minecraft/server/v1_8_R3/World:notify	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)V
    //   1223: iinc 9 1
    //   1226: iload 9
    //   1228: aload_0
    //   1229: getfield 230	net/minecraft/server/v1_8_R3/World:b	Ljava/util/List;
    //   1232: invokeinterface 760 1 0
    //   1237: if_icmplt -78 -> 1159
    //   1240: aload_0
    //   1241: getfield 230	net/minecraft/server/v1_8_R3/World:b	Ljava/util/List;
    //   1244: invokeinterface 1431 1 0
    //   1249: aload_0
    //   1250: getfield 400	net/minecraft/server/v1_8_R3/World:timings	Lorg/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler;
    //   1253: getfield 1512	org/bukkit/craftbukkit/v1_8_R3/SpigotTimings$WorldTimingsHandler:tileEntityPending	Lorg/spigotmc/CustomTimingsHandler;
    //   1256: invokevirtual 1460	org/spigotmc/CustomTimingsHandler:stopTiming	()V
    //   1259: aload_0
    //   1260: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   1263: invokevirtual 689	net/minecraft/server/v1_8_R3/MethodProfiler:b	()V
    //   1266: aload_0
    //   1267: getfield 375	net/minecraft/server/v1_8_R3/World:methodProfiler	Lnet/minecraft/server/v1_8_R3/MethodProfiler;
    //   1270: invokevirtual 689	net/minecraft/server/v1_8_R3/MethodProfiler:b	()V
    //   1273: return
    // Line number table:
    //   Java source line #1342	-> byte code offset #0
    //   Java source line #1343	-> byte code offset #10
    //   Java source line #1350	-> byte code offset #20
    //   Java source line #1351	-> byte code offset #25
    //   Java source line #1353	-> byte code offset #39
    //   Java source line #1354	-> byte code offset #43
    //   Java source line #1359	-> byte code offset #46
    //   Java source line #1360	-> byte code offset #56
    //   Java source line #1361	-> byte code offset #60
    //   Java source line #1362	-> byte code offset #64
    //   Java source line #1363	-> byte code offset #73
    //   Java source line #1364	-> byte code offset #83
    //   Java source line #1365	-> byte code offset #87
    //   Java source line #1366	-> byte code offset #98
    //   Java source line #1367	-> byte code offset #101
    //   Java source line #1370	-> byte code offset #107
    //   Java source line #1373	-> byte code offset #117
    //   Java source line #1374	-> byte code offset #124
    //   Java source line #1350	-> byte code offset #138
    //   Java source line #1378	-> byte code offset #154
    //   Java source line #1379	-> byte code offset #164
    //   Java source line #1384	-> byte code offset #178
    //   Java source line #1385	-> byte code offset #183
    //   Java source line #1386	-> byte code offset #197
    //   Java source line #1387	-> byte code offset #202
    //   Java source line #1388	-> byte code offset #208
    //   Java source line #1389	-> byte code offset #226
    //   Java source line #1384	-> byte code offset #237
    //   Java source line #1393	-> byte code offset #253
    //   Java source line #1394	-> byte code offset #258
    //   Java source line #1393	-> byte code offset #275
    //   Java source line #1397	-> byte code offset #291
    //   Java source line #1398	-> byte code offset #300
    //   Java source line #1400	-> byte code offset #310
    //   Java source line #1401	-> byte code offset #314
    //   Java source line #1402	-> byte code offset #324
    //   Java source line #1404	-> byte code offset #329
    //   Java source line #1405	-> byte code offset #332
    //   Java source line #1406	-> byte code offset #344
    //   Java source line #1407	-> byte code offset #351
    //   Java source line #1409	-> byte code offset #354
    //   Java source line #1410	-> byte code offset #382
    //   Java source line #1412	-> byte code offset #399
    //   Java source line #1413	-> byte code offset #406
    //   Java source line #1414	-> byte code offset #427
    //   Java source line #1417	-> byte code offset #430
    //   Java source line #1418	-> byte code offset #438
    //   Java source line #1421	-> byte code offset #443
    //   Java source line #1422	-> byte code offset #453
    //   Java source line #1424	-> byte code offset #460
    //   Java source line #1425	-> byte code offset #466
    //   Java source line #1426	-> byte code offset #471
    //   Java source line #1427	-> byte code offset #477
    //   Java source line #1428	-> byte code offset #482
    //   Java source line #1429	-> byte code offset #492
    //   Java source line #1430	-> byte code offset #502
    //   Java source line #1431	-> byte code offset #508
    //   Java source line #1435	-> byte code offset #518
    //   Java source line #1436	-> byte code offset #525
    //   Java source line #1437	-> byte code offset #535
    //   Java source line #1438	-> byte code offset #542
    //   Java source line #1439	-> byte code offset #547
    //   Java source line #1440	-> byte code offset #553
    //   Java source line #1441	-> byte code offset #571
    //   Java source line #1444	-> byte code offset #582
    //   Java source line #1445	-> byte code offset #587
    //   Java source line #1446	-> byte code offset #608
    //   Java source line #1447	-> byte code offset #613
    //   Java source line #1450	-> byte code offset #618
    //   Java source line #1408	-> byte code offset #625
    //   Java source line #1407	-> byte code offset #638
    //   Java source line #1452	-> byte code offset #670
    //   Java source line #1454	-> byte code offset #675
    //   Java source line #1455	-> byte code offset #685
    //   Java source line #1456	-> byte code offset #695
    //   Java source line #1457	-> byte code offset #705
    //   Java source line #1459	-> byte code offset #710
    //   Java source line #1460	-> byte code offset #722
    //   Java source line #1461	-> byte code offset #736
    //   Java source line #1462	-> byte code offset #750
    //   Java source line #1467	-> byte code offset #759
    //   Java source line #1468	-> byte code offset #762
    //   Java source line #1469	-> byte code offset #769
    //   Java source line #1471	-> byte code offset #772
    //   Java source line #1472	-> byte code offset #800
    //   Java source line #1474	-> byte code offset #818
    //   Java source line #1475	-> byte code offset #823
    //   Java source line #1476	-> byte code offset #836
    //   Java source line #1477	-> byte code offset #839
    //   Java source line #1478	-> byte code offset #860
    //   Java source line #1482	-> byte code offset #863
    //   Java source line #1483	-> byte code offset #879
    //   Java source line #1485	-> byte code offset #886
    //   Java source line #1487	-> byte code offset #907
    //   Java source line #1488	-> byte code offset #915
    //   Java source line #1489	-> byte code offset #925
    //   Java source line #1490	-> byte code offset #930
    //   Java source line #1491	-> byte code offset #940
    //   Java source line #1493	-> byte code offset #950
    //   Java source line #1494	-> byte code offset #957
    //   Java source line #1497	-> byte code offset #967
    //   Java source line #1498	-> byte code offset #969
    //   Java source line #1499	-> byte code offset #977
    //   Java source line #1498	-> byte code offset #980
    //   Java source line #1504	-> byte code offset #988
    //   Java source line #1505	-> byte code offset #996
    //   Java source line #1506	-> byte code offset #999
    //   Java source line #1507	-> byte code offset #1020
    //   Java source line #1508	-> byte code offset #1032
    //   Java source line #1509	-> byte code offset #1044
    //   Java source line #1470	-> byte code offset #1061
    //   Java source line #1469	-> byte code offset #1074
    //   Java source line #1514	-> byte code offset #1106
    //   Java source line #1515	-> byte code offset #1116
    //   Java source line #1516	-> byte code offset #1126
    //   Java source line #1525	-> byte code offset #1131
    //   Java source line #1526	-> byte code offset #1141
    //   Java source line #1527	-> byte code offset #1153
    //   Java source line #1528	-> byte code offset #1159
    //   Java source line #1530	-> byte code offset #1175
    //   Java source line #1537	-> byte code offset #1183
    //   Java source line #1538	-> byte code offset #1195
    //   Java source line #1541	-> byte code offset #1214
    //   Java source line #1527	-> byte code offset #1223
    //   Java source line #1545	-> byte code offset #1240
    //   Java source line #1548	-> byte code offset #1249
    //   Java source line #1549	-> byte code offset #1259
    //   Java source line #1550	-> byte code offset #1266
    //   Java source line #1551	-> byte code offset #1273
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1274	0	this	World
    //   21	258	1	i	int
    //   38	80	2	entity	Entity
    //   196	38	2	entity	Entity
    //   398	217	2	entity	Entity
    //   63	2	3	throwable	Throwable
    //   201	27	3	j	int
    //   546	27	3	j	int
    //   71	41	4	crashreport	CrashReport
    //   490	23	4	crashreport	CrashReport
    //   81	22	5	crashreportsystemdetails	CrashReportSystemDetails
    //   500	4	5	crashreportsystemdetails	CrashReportSystemDetails
    //   206	23	6	k	int
    //   551	23	6	k	int
    //   330	323	7	entitiesThisCycle	int
    //   480	3	8	throwable1	Throwable
    //   760	329	8	tilesThisCycle	int
    //   816	238	9	tileentity	TileEntity
    //   1154	73	9	l	int
    //   884	16	10	blockposition	BlockPosition
    //   1173	43	10	tileentity1	TileEntity
    //   928	3	11	throwable2	Throwable
    //   938	24	12	crashreport1	CrashReport
    //   948	5	13	crashreportsystemdetails1	CrashReportSystemDetails
    //   967	11	14	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   46	60	63	java/lang/Throwable
    //   460	477	480	java/lang/Throwable
    //   907	925	928	java/lang/Throwable
    //   907	967	967	finally
  }
  
  public boolean a(TileEntity tileentity)
  {
    boolean flag = this.h.add(tileentity);
    if ((flag) && ((tileentity instanceof IUpdatePlayerListBox))) {
      this.tileEntityList.add(tileentity);
    }
    return flag;
  }
  
  public void a(Collection<TileEntity> collection)
  {
    if (this.M)
    {
      this.b.addAll(collection);
    }
    else
    {
      Iterator iterator = collection.iterator();
      while (iterator.hasNext())
      {
        TileEntity tileentity = (TileEntity)iterator.next();
        
        this.h.add(tileentity);
        if ((tileentity instanceof IUpdatePlayerListBox)) {
          this.tileEntityList.add(tileentity);
        }
      }
    }
  }
  
  public void g(Entity entity)
  {
    entityJoinedWorld(entity, true);
  }
  
  public void entityJoinedWorld(Entity entity, boolean flag)
  {
    MathHelper.floor(entity.locX);
    MathHelper.floor(entity.locZ);
    if (!ActivationRange.checkIfActive(entity))
    {
      entity.ticksLived += 1;
      entity.inactiveTick();
    }
    else
    {
      entity.tickTimer.startTiming();
      
      entity.P = entity.locX;
      entity.Q = entity.locY;
      entity.R = entity.locZ;
      entity.lastYaw = entity.yaw;
      entity.lastPitch = entity.pitch;
      if ((flag) && (entity.ad))
      {
        entity.ticksLived += 1;
        if (entity.vehicle != null) {
          entity.ak();
        } else {
          entity.t_();
        }
      }
      this.methodProfiler.a("chunkCheck");
      if ((Double.isNaN(entity.locX)) || (Double.isInfinite(entity.locX))) {
        entity.locX = entity.P;
      }
      if ((Double.isNaN(entity.locY)) || (Double.isInfinite(entity.locY))) {
        entity.locY = entity.Q;
      }
      if ((Double.isNaN(entity.locZ)) || (Double.isInfinite(entity.locZ))) {
        entity.locZ = entity.R;
      }
      if ((Double.isNaN(entity.pitch)) || (Double.isInfinite(entity.pitch))) {
        entity.pitch = entity.lastPitch;
      }
      if ((Double.isNaN(entity.yaw)) || (Double.isInfinite(entity.yaw))) {
        entity.yaw = entity.lastYaw;
      }
      int k = MathHelper.floor(entity.locX / 16.0D);
      int l = MathHelper.floor(entity.locY / 16.0D);
      int i1 = MathHelper.floor(entity.locZ / 16.0D);
      if ((!entity.ad) || (entity.ae != k) || (entity.af != l) || (entity.ag != i1))
      {
        if ((entity.ad) && (isChunkLoaded(entity.ae, entity.ag, true))) {
          getChunkAt(entity.ae, entity.ag).a(entity, entity.af);
        }
        if (isChunkLoaded(k, i1, true))
        {
          entity.ad = true;
          getChunkAt(k, i1).a(entity);
        }
        else
        {
          entity.ad = false;
        }
      }
      this.methodProfiler.b();
      if ((flag) && (entity.ad) && (entity.passenger != null)) {
        if ((!entity.passenger.dead) && (entity.passenger.vehicle == entity))
        {
          g(entity.passenger);
        }
        else
        {
          entity.passenger.vehicle = null;
          entity.passenger = null;
        }
      }
      entity.tickTimer.stopTiming();
    }
  }
  
  public boolean b(AxisAlignedBB axisalignedbb)
  {
    return a(axisalignedbb, null);
  }
  
  public boolean a(AxisAlignedBB axisalignedbb, Entity entity)
  {
    List list = getEntities(null, axisalignedbb);
    for (int i = 0; i < list.size(); i++)
    {
      Entity entity1 = (Entity)list.get(i);
      if ((!entity1.dead) && (entity1.k) && (entity1 != entity) && ((entity == null) || ((entity.vehicle != entity1) && (entity.passenger != entity1)))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean c(AxisAlignedBB axisalignedbb)
  {
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f);
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int k1 = i; k1 <= j; k1++) {
      for (int l1 = k; l1 <= l; l1++) {
        for (int i2 = i1; i2 <= j1; i2++)
        {
          Block block = getType(blockposition_mutableblockposition.c(k1, l1, i2)).getBlock();
          if (block.getMaterial() != Material.AIR) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public boolean containsLiquid(AxisAlignedBB axisalignedbb)
  {
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f);
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int k1 = i; k1 <= j; k1++) {
      for (int l1 = k; l1 <= l; l1++) {
        for (int i2 = i1; i2 <= j1; i2++)
        {
          Block block = getType(blockposition_mutableblockposition.c(k1, l1, i2)).getBlock();
          if (block.getMaterial().isLiquid()) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public boolean e(AxisAlignedBB axisalignedbb)
  {
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d + 1.0D);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e + 1.0D);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
    if (isAreaLoaded(i, k, i1, j, l, j1, true))
    {
      BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
      for (int k1 = i; k1 < j; k1++) {
        for (int l1 = k; l1 < l; l1++) {
          for (int i2 = i1; i2 < j1; i2++)
          {
            Block block = getType(blockposition_mutableblockposition.c(k1, l1, i2)).getBlock();
            if ((block == Blocks.FIRE) || (block == Blocks.FLOWING_LAVA) || (block == Blocks.LAVA)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  public boolean a(AxisAlignedBB axisalignedbb, Material material, Entity entity)
  {
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d + 1.0D);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e + 1.0D);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
    if (!isAreaLoaded(i, k, i1, j, l, j1, true)) {
      return false;
    }
    boolean flag = false;
    Vec3D vec3d = new Vec3D(0.0D, 0.0D, 0.0D);
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int k1 = i; k1 < j; k1++) {
      for (int l1 = k; l1 < l; l1++) {
        for (int i2 = i1; i2 < j1; i2++)
        {
          blockposition_mutableblockposition.c(k1, l1, i2);
          IBlockData iblockdata = getType(blockposition_mutableblockposition);
          Block block = iblockdata.getBlock();
          if (block.getMaterial() == material)
          {
            double d0 = l1 + 1 - BlockFluids.b(((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue());
            if (l >= d0)
            {
              flag = true;
              vec3d = block.a(this, blockposition_mutableblockposition, entity, vec3d);
            }
          }
        }
      }
    }
    if ((vec3d.b() > 0.0D) && (entity.aL()))
    {
      vec3d = vec3d.a();
      double d1 = 0.014D;
      
      entity.motX += vec3d.a * d1;
      entity.motY += vec3d.b * d1;
      entity.motZ += vec3d.c * d1;
    }
    return flag;
  }
  
  public boolean a(AxisAlignedBB axisalignedbb, Material material)
  {
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d + 1.0D);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e + 1.0D);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int k1 = i; k1 < j; k1++) {
      for (int l1 = k; l1 < l; l1++) {
        for (int i2 = i1; i2 < j1; i2++) {
          if (getType(blockposition_mutableblockposition.c(k1, l1, i2)).getBlock().getMaterial() == material) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public boolean b(AxisAlignedBB axisalignedbb, Material material)
  {
    int i = MathHelper.floor(axisalignedbb.a);
    int j = MathHelper.floor(axisalignedbb.d + 1.0D);
    int k = MathHelper.floor(axisalignedbb.b);
    int l = MathHelper.floor(axisalignedbb.e + 1.0D);
    int i1 = MathHelper.floor(axisalignedbb.c);
    int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int k1 = i; k1 < j; k1++) {
      for (int l1 = k; l1 < l; l1++) {
        for (int i2 = i1; i2 < j1; i2++)
        {
          IBlockData iblockdata = getType(blockposition_mutableblockposition.c(k1, l1, i2));
          Block block = iblockdata.getBlock();
          if (block.getMaterial() == material)
          {
            int j2 = ((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue();
            double d0 = l1 + 1;
            if (j2 < 8) {
              d0 = l1 + 1 - j2 / 8.0D;
            }
            if (d0 >= axisalignedbb.b) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  public Explosion explode(Entity entity, double d0, double d1, double d2, float f, boolean flag)
  {
    return createExplosion(entity, d0, d1, d2, f, false, flag);
  }
  
  public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1)
  {
    Explosion explosion = new Explosion(this, entity, d0, d1, d2, f, flag, flag1);
    
    explosion.a();
    explosion.a(true);
    return explosion;
  }
  
  public float a(Vec3D vec3d, AxisAlignedBB axisalignedbb)
  {
    double d0 = 1.0D / ((axisalignedbb.d - axisalignedbb.a) * 2.0D + 1.0D);
    double d1 = 1.0D / ((axisalignedbb.e - axisalignedbb.b) * 2.0D + 1.0D);
    double d2 = 1.0D / ((axisalignedbb.f - axisalignedbb.c) * 2.0D + 1.0D);
    double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
    double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
    if ((d0 >= 0.0D) && (d1 >= 0.0D) && (d2 >= 0.0D))
    {
      int i = 0;
      int j = 0;
      for (float f = 0.0F; f <= 1.0F; f = (float)(f + d0)) {
        for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)(f1 + d1)) {
          for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)(f2 + d2))
          {
            double d5 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * f;
            double d6 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * f1;
            double d7 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * f2;
            if (rayTrace(new Vec3D(d5 + d3, d6, d7 + d4), vec3d) == null) {
              i++;
            }
            j++;
          }
        }
      }
      return i / j;
    }
    return 0.0F;
  }
  
  public boolean douseFire(EntityHuman entityhuman, BlockPosition blockposition, EnumDirection enumdirection)
  {
    blockposition = blockposition.shift(enumdirection);
    if (getType(blockposition).getBlock() == Blocks.FIRE)
    {
      a(entityhuman, 1004, blockposition, 0);
      setAir(blockposition);
      return true;
    }
    return false;
  }
  
  public Map<BlockPosition, TileEntity> capturedTileEntities = Maps.newHashMap();
  
  public TileEntity getTileEntity(BlockPosition blockposition)
  {
    if (!isValidLocation(blockposition)) {
      return null;
    }
    if (this.capturedTileEntities.containsKey(blockposition)) {
      return (TileEntity)this.capturedTileEntities.get(blockposition);
    }
    TileEntity tileentity = null;
    if (this.M) {
      for (int i = 0; i < this.b.size(); i++)
      {
        TileEntity tileentity1 = (TileEntity)this.b.get(i);
        if ((!tileentity1.x()) && (tileentity1.getPosition().equals(blockposition)))
        {
          tileentity = tileentity1;
          break;
        }
      }
    }
    if (tileentity == null) {
      tileentity = getChunkAtWorldCoords(blockposition).a(blockposition, Chunk.EnumTileEntityState.IMMEDIATE);
    }
    if (tileentity == null) {
      for (int i = 0; i < this.b.size(); i++)
      {
        TileEntity tileentity1 = (TileEntity)this.b.get(i);
        if ((!tileentity1.x()) && (tileentity1.getPosition().equals(blockposition)))
        {
          tileentity = tileentity1;
          break;
        }
      }
    }
    return tileentity;
  }
  
  public void setTileEntity(BlockPosition blockposition, TileEntity tileentity)
  {
    if ((tileentity != null) && (!tileentity.x()))
    {
      if (this.captureBlockStates)
      {
        tileentity.a(this);
        tileentity.a(blockposition);
        this.capturedTileEntities.put(blockposition, tileentity);
        return;
      }
      if (this.M)
      {
        tileentity.a(blockposition);
        Iterator iterator = this.b.iterator();
        while (iterator.hasNext())
        {
          TileEntity tileentity1 = (TileEntity)iterator.next();
          if (tileentity1.getPosition().equals(blockposition))
          {
            tileentity1.y();
            iterator.remove();
          }
        }
        tileentity.a(this);
        this.b.add(tileentity);
      }
      else
      {
        a(tileentity);
        getChunkAtWorldCoords(blockposition).a(blockposition, tileentity);
      }
    }
  }
  
  public void t(BlockPosition blockposition)
  {
    TileEntity tileentity = getTileEntity(blockposition);
    if ((tileentity != null) && (this.M))
    {
      tileentity.y();
      this.b.remove(tileentity);
    }
    else
    {
      if (tileentity != null)
      {
        this.b.remove(tileentity);
        this.h.remove(tileentity);
        this.tileEntityList.remove(tileentity);
      }
      getChunkAtWorldCoords(blockposition).e(blockposition);
    }
  }
  
  public void b(TileEntity tileentity)
  {
    this.c.add(tileentity);
  }
  
  public boolean u(BlockPosition blockposition)
  {
    IBlockData iblockdata = getType(blockposition);
    AxisAlignedBB axisalignedbb = iblockdata.getBlock().a(this, blockposition, iblockdata);
    
    return (axisalignedbb != null) && (axisalignedbb.a() >= 1.0D);
  }
  
  public static boolean a(IBlockAccess iblockaccess, BlockPosition blockposition)
  {
    IBlockData iblockdata = iblockaccess.getType(blockposition);
    Block block = iblockdata.getBlock();
    
    return (block.getMaterial().k()) && (block.d());
  }
  
  public boolean d(BlockPosition blockposition, boolean flag)
  {
    if (!isValidLocation(blockposition)) {
      return flag;
    }
    Chunk chunk = this.chunkProvider.getChunkAt(blockposition);
    if (chunk.isEmpty()) {
      return flag;
    }
    Block block = getType(blockposition).getBlock();
    
    return (block.getMaterial().k()) && (block.d());
  }
  
  public void B()
  {
    int i = a(1.0F);
    if (i != this.I) {
      this.I = i;
    }
  }
  
  public void setSpawnFlags(boolean flag, boolean flag1)
  {
    this.allowMonsters = flag;
    this.allowAnimals = flag1;
  }
  
  public void doTick()
  {
    p();
  }
  
  protected void C()
  {
    if (this.worldData.hasStorm())
    {
      this.p = 1.0F;
      if (this.worldData.isThundering()) {
        this.r = 1.0F;
      }
    }
  }
  
  protected void p()
  {
    if ((!this.worldProvider.o()) && 
      (!this.isClientSide))
    {
      int i = this.worldData.A();
      if (i > 0)
      {
        i--;
        this.worldData.i(i);
        this.worldData.setThunderDuration(this.worldData.isThundering() ? 1 : 2);
        this.worldData.setWeatherDuration(this.worldData.hasStorm() ? 1 : 2);
      }
      int j = this.worldData.getThunderDuration();
      if (j <= 0)
      {
        if (this.worldData.isThundering()) {
          this.worldData.setThunderDuration(this.random.nextInt(12000) + 3600);
        } else {
          this.worldData.setThunderDuration(this.random.nextInt(168000) + 12000);
        }
      }
      else
      {
        j--;
        this.worldData.setThunderDuration(j);
        if (j <= 0) {
          this.worldData.setThundering(!this.worldData.isThundering());
        }
      }
      this.q = this.r;
      if (this.worldData.isThundering()) {
        this.r = ((float)(this.r + 0.01D));
      } else {
        this.r = ((float)(this.r - 0.01D));
      }
      this.r = MathHelper.a(this.r, 0.0F, 1.0F);
      int k = this.worldData.getWeatherDuration();
      if (k <= 0)
      {
        if (this.worldData.hasStorm()) {
          this.worldData.setWeatherDuration(this.random.nextInt(12000) + 12000);
        } else {
          this.worldData.setWeatherDuration(this.random.nextInt(168000) + 12000);
        }
      }
      else
      {
        k--;
        this.worldData.setWeatherDuration(k);
        if (k <= 0) {
          this.worldData.setStorm(!this.worldData.hasStorm());
        }
      }
      this.o = this.p;
      if (this.worldData.hasStorm()) {
        this.p = ((float)(this.p + 0.01D));
      } else {
        this.p = ((float)(this.p - 0.01D));
      }
      this.p = MathHelper.a(this.p, 0.0F, 1.0F);
      for (int idx = 0; idx < this.players.size(); idx++) {
        if (((EntityPlayer)this.players.get(idx)).world == this) {
          ((EntityPlayer)this.players.get(idx)).tickWeather();
        }
      }
    }
  }
  
  protected void D()
  {
    this.methodProfiler.a("buildList");
    
    int optimalChunks = this.spigotConfig.chunksPerTick;
    if (optimalChunks > 0)
    {
      int chunksPerPlayer = Math.min(200, Math.max(1, (int)((optimalChunks - this.players.size()) / this.players.size() + 0.5D)));
      int randRange = 3 + chunksPerPlayer / 30;
      
      randRange = randRange > this.chunkTickRadius ? this.chunkTickRadius : randRange;
      
      this.growthOdds = (this.modifiedOdds = Math.max(35.0F, Math.min(100.0F, (chunksPerPlayer + 1) * 100.0F / 15.0F)));
      for (int i = 0; i < this.players.size(); i++)
      {
        EntityHuman entityhuman = (EntityHuman)this.players.get(i);
        int j = MathHelper.floor(entityhuman.locX / 16.0D);
        int k = MathHelper.floor(entityhuman.locZ / 16.0D);
        int l = q();
        
        long key = chunkToKey(j, k);
        int existingPlayers = Math.max(0, this.chunkTickList.get(key));
        this.chunkTickList.put(key, (short)(existingPlayers + 1));
        for (int chunk = 0; chunk < chunksPerPlayer; chunk++)
        {
          int dx = (this.random.nextBoolean() ? 1 : -1) * this.random.nextInt(randRange);
          int dz = (this.random.nextBoolean() ? 1 : -1) * this.random.nextInt(randRange);
          long hash = chunkToKey(dx + j, dz + k);
          if ((!this.chunkTickList.contains(hash)) && (this.chunkProvider.isChunkLoaded(dx + j, dz + k))) {
            this.chunkTickList.put(hash, (short)-1);
          }
        }
      }
    }
    this.methodProfiler.b();
    if (this.L > 0) {
      this.L -= 1;
    }
    this.methodProfiler.a("playerCheckLight");
    if ((this.spigotConfig.randomLightUpdates) && (!this.players.isEmpty()))
    {
      int i = this.random.nextInt(this.players.size());
      EntityHuman entityhuman = (EntityHuman)this.players.get(i);
      int j = MathHelper.floor(entityhuman.locX) + this.random.nextInt(11) - 5;
      int k = MathHelper.floor(entityhuman.locY) + this.random.nextInt(11) - 5;
      int l = MathHelper.floor(entityhuman.locZ) + this.random.nextInt(11) - 5;
      x(new BlockPosition(j, k, l));
    }
    this.methodProfiler.b();
  }
  
  protected abstract int q();
  
  protected void a(int i, int j, Chunk chunk)
  {
    this.methodProfiler.c("moodSound");
    if ((this.L == 0) && (!this.isClientSide))
    {
      this.m = (this.m * 3 + 1013904223);
      int k = this.m >> 2;
      int l = k & 0xF;
      int i1 = k >> 8 & 0xF;
      int j1 = k >> 16 & 0xFF;
      BlockPosition blockposition = new BlockPosition(l, j1, i1);
      Block block = chunk.getType(blockposition);
      
      l += i;
      i1 += j;
      if ((block.getMaterial() == Material.AIR) && (k(blockposition) <= this.random.nextInt(8)) && (b(EnumSkyBlock.SKY, blockposition) <= 0))
      {
        EntityHuman entityhuman = findNearbyPlayer(l + 0.5D, j1 + 0.5D, i1 + 0.5D, 8.0D);
        if ((entityhuman != null) && (entityhuman.e(l + 0.5D, j1 + 0.5D, i1 + 0.5D) > 4.0D))
        {
          makeSound(l + 0.5D, j1 + 0.5D, i1 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.random.nextFloat() * 0.2F);
          this.L = (this.random.nextInt(12000) + 6000);
        }
      }
    }
    this.methodProfiler.c("checkLight");
    chunk.m();
  }
  
  protected void h()
  {
    D();
  }
  
  public void a(Block block, BlockPosition blockposition, Random random)
  {
    this.e = true;
    block.b(this, blockposition, getType(blockposition), random);
    this.e = false;
  }
  
  public boolean v(BlockPosition blockposition)
  {
    return e(blockposition, false);
  }
  
  public boolean w(BlockPosition blockposition)
  {
    return e(blockposition, true);
  }
  
  public boolean e(BlockPosition blockposition, boolean flag)
  {
    BiomeBase biomebase = getBiome(blockposition);
    float f = biomebase.a(blockposition);
    if (f > 0.15F) {
      return false;
    }
    if ((blockposition.getY() >= 0) && (blockposition.getY() < 256) && (b(EnumSkyBlock.BLOCK, blockposition) < 10))
    {
      IBlockData iblockdata = getType(blockposition);
      Block block = iblockdata.getBlock();
      if (((block == Blocks.WATER) || (block == Blocks.FLOWING_WATER)) && (((Integer)iblockdata.get(BlockFluids.LEVEL)).intValue() == 0))
      {
        if (!flag) {
          return true;
        }
        boolean flag1 = (F(blockposition.west())) && (F(blockposition.east())) && (F(blockposition.north())) && (F(blockposition.south()));
        if (!flag1) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean F(BlockPosition blockposition)
  {
    return getType(blockposition).getBlock().getMaterial() == Material.WATER;
  }
  
  public boolean f(BlockPosition blockposition, boolean flag)
  {
    BiomeBase biomebase = getBiome(blockposition);
    float f = biomebase.a(blockposition);
    if (f > 0.15F) {
      return false;
    }
    if (!flag) {
      return true;
    }
    if ((blockposition.getY() >= 0) && (blockposition.getY() < 256) && (b(EnumSkyBlock.BLOCK, blockposition) < 10))
    {
      Block block = getType(blockposition).getBlock();
      if ((block.getMaterial() == Material.AIR) && (Blocks.SNOW_LAYER.canPlace(this, blockposition))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean x(BlockPosition blockposition)
  {
    boolean flag = false;
    if (!this.worldProvider.o()) {
      flag |= c(EnumSkyBlock.SKY, blockposition);
    }
    flag |= c(EnumSkyBlock.BLOCK, blockposition);
    return flag;
  }
  
  private int a(BlockPosition blockposition, EnumSkyBlock enumskyblock)
  {
    if ((enumskyblock == EnumSkyBlock.SKY) && (i(blockposition))) {
      return 15;
    }
    Block block = getType(blockposition).getBlock();
    int i = enumskyblock == EnumSkyBlock.SKY ? 0 : block.r();
    int j = block.p();
    if ((j >= 15) && (block.r() > 0)) {
      j = 1;
    }
    if (j < 1) {
      j = 1;
    }
    if (j >= 15) {
      return 0;
    }
    if (i >= 14) {
      return i;
    }
    EnumDirection[] aenumdirection = EnumDirection.values();
    int k = aenumdirection.length;
    for (int l = 0; l < k; l++)
    {
      EnumDirection enumdirection = aenumdirection[l];
      BlockPosition blockposition1 = blockposition.shift(enumdirection);
      int i1 = b(enumskyblock, blockposition1) - j;
      if (i1 > i) {
        i = i1;
      }
      if (i >= 14) {
        return i;
      }
    }
    return i;
  }
  
  public boolean c(EnumSkyBlock enumskyblock, BlockPosition blockposition)
  {
    Chunk chunk = getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    if ((chunk == null) || (!chunk.areNeighborsLoaded(1))) {
      return false;
    }
    int i = 0;
    int j = 0;
    
    this.methodProfiler.a("getBrightness");
    int k = b(enumskyblock, blockposition);
    int l = a(blockposition, enumskyblock);
    int i1 = blockposition.getX();
    int j1 = blockposition.getY();
    int k1 = blockposition.getZ();
    if (l > k)
    {
      this.H[(j++)] = 133152;
    }
    else if (l < k)
    {
      this.H[(j++)] = (0x20820 | k << 18);
      while (i < j)
      {
        int l1 = this.H[(i++)];
        int i2 = (l1 & 0x3F) - 32 + i1;
        int j2 = (l1 >> 6 & 0x3F) - 32 + j1;
        int k2 = (l1 >> 12 & 0x3F) - 32 + k1;
        int l3 = l1 >> 18 & 0xF;
        BlockPosition blockposition1 = new BlockPosition(i2, j2, k2);
        
        int l2 = b(enumskyblock, blockposition1);
        if (l2 == l3)
        {
          a(enumskyblock, blockposition1, 0);
          if (l3 > 0)
          {
            int i3 = MathHelper.a(i2 - i1);
            int j3 = MathHelper.a(j2 - j1);
            int k3 = MathHelper.a(k2 - k1);
            if (i3 + j3 + k3 < 17)
            {
              BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
              EnumDirection[] aenumdirection = EnumDirection.values();
              int i4 = aenumdirection.length;
              for (int j4 = 0; j4 < i4; j4++)
              {
                EnumDirection enumdirection = aenumdirection[j4];
                int k4 = i2 + enumdirection.getAdjacentX();
                int l4 = j2 + enumdirection.getAdjacentY();
                int i5 = k2 + enumdirection.getAdjacentZ();
                
                blockposition_mutableblockposition.c(k4, l4, i5);
                int j5 = Math.max(1, getType(blockposition_mutableblockposition).getBlock().p());
                
                l2 = b(enumskyblock, blockposition_mutableblockposition);
                if ((l2 == l3 - j5) && (j < this.H.length)) {
                  this.H[(j++)] = (k4 - i1 + 32 | l4 - j1 + 32 << 6 | i5 - k1 + 32 << 12 | l3 - j5 << 18);
                }
              }
            }
          }
        }
      }
      i = 0;
    }
    this.methodProfiler.b();
    this.methodProfiler.a("checkedPosition < toCheckCount");
    while (i < j)
    {
      int l1 = this.H[(i++)];
      int i2 = (l1 & 0x3F) - 32 + i1;
      int j2 = (l1 >> 6 & 0x3F) - 32 + j1;
      int k2 = (l1 >> 12 & 0x3F) - 32 + k1;
      BlockPosition blockposition2 = new BlockPosition(i2, j2, k2);
      int k5 = b(enumskyblock, blockposition2);
      
      int l2 = a(blockposition2, enumskyblock);
      if (l2 != k5)
      {
        a(enumskyblock, blockposition2, l2);
        if (l2 > k5)
        {
          int i3 = Math.abs(i2 - i1);
          int j3 = Math.abs(j2 - j1);
          int k3 = Math.abs(k2 - k1);
          boolean flag = j < this.H.length - 6;
          if ((i3 + j3 + k3 < 17) && (flag))
          {
            if (b(enumskyblock, blockposition2.west()) < l2) {
              this.H[(j++)] = (i2 - 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12));
            }
            if (b(enumskyblock, blockposition2.east()) < l2) {
              this.H[(j++)] = (i2 + 1 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - k1 + 32 << 12));
            }
            if (b(enumskyblock, blockposition2.down()) < l2) {
              this.H[(j++)] = (i2 - i1 + 32 + (j2 - 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12));
            }
            if (b(enumskyblock, blockposition2.up()) < l2) {
              this.H[(j++)] = (i2 - i1 + 32 + (j2 + 1 - j1 + 32 << 6) + (k2 - k1 + 32 << 12));
            }
            if (b(enumskyblock, blockposition2.north()) < l2) {
              this.H[(j++)] = (i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 - 1 - k1 + 32 << 12));
            }
            if (b(enumskyblock, blockposition2.south()) < l2) {
              this.H[(j++)] = (i2 - i1 + 32 + (j2 - j1 + 32 << 6) + (k2 + 1 - k1 + 32 << 12));
            }
          }
        }
      }
    }
    this.methodProfiler.b();
    return true;
  }
  
  public boolean a(boolean flag)
  {
    return false;
  }
  
  public List<NextTickListEntry> a(Chunk chunk, boolean flag)
  {
    return null;
  }
  
  public List<NextTickListEntry> a(StructureBoundingBox structureboundingbox, boolean flag)
  {
    return null;
  }
  
  public List<Entity> getEntities(Entity entity, AxisAlignedBB axisalignedbb)
  {
    return a(entity, axisalignedbb, IEntitySelector.d);
  }
  
  public List<Entity> a(Entity entity, AxisAlignedBB axisalignedbb, Predicate<? super Entity> predicate)
  {
    ArrayList arraylist = Lists.newArrayList();
    int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
    int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
    int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
    int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);
    if ((j - i > 10) || (l - k > 10))
    {
      getServer().getLogger().log(Level.WARNING, "Filtered out large getEntities call {0},{1} {2},{3}", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k), Integer.valueOf(j) });
      return arraylist;
    }
    for (int i1 = i; i1 <= j; i1++) {
      for (int j1 = k; j1 <= l; j1++) {
        if (isChunkLoaded(i1, j1, true)) {
          getChunkAt(i1, j1).a(entity, axisalignedbb, arraylist, predicate);
        }
      }
    }
    return arraylist;
  }
  
  public <T extends Entity> List<T> a(Class<? extends T> oclass, Predicate<? super T> predicate)
  {
    ArrayList arraylist = Lists.newArrayList();
    Iterator iterator = this.entityList.iterator();
    while (iterator.hasNext())
    {
      Entity entity = (Entity)iterator.next();
      if ((oclass.isAssignableFrom(entity.getClass())) && (predicate.apply(entity))) {
        arraylist.add(entity);
      }
    }
    return arraylist;
  }
  
  public <T extends Entity> List<T> b(Class<? extends T> oclass, Predicate<? super T> predicate)
  {
    ArrayList arraylist = Lists.newArrayList();
    Iterator iterator = this.players.iterator();
    while (iterator.hasNext())
    {
      Entity entity = (Entity)iterator.next();
      if ((oclass.isAssignableFrom(entity.getClass())) && (predicate.apply(entity))) {
        arraylist.add(entity);
      }
    }
    return arraylist;
  }
  
  public <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb)
  {
    return a(oclass, axisalignedbb, IEntitySelector.d);
  }
  
  public <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, Predicate<? super T> predicate)
  {
    int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
    int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
    int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
    int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);
    ArrayList arraylist = Lists.newArrayList();
    for (int i1 = i; i1 <= j; i1++) {
      for (int j1 = k; j1 <= l; j1++) {
        if (isChunkLoaded(i1, j1, true)) {
          getChunkAt(i1, j1).a(oclass, axisalignedbb, arraylist, predicate);
        }
      }
    }
    return arraylist;
  }
  
  public <T extends Entity> T a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, T t0)
  {
    List list = a(oclass, axisalignedbb);
    Entity entity = null;
    double d0 = Double.MAX_VALUE;
    for (int i = 0; i < list.size(); i++)
    {
      Entity entity1 = (Entity)list.get(i);
      if ((entity1 != t0) && (IEntitySelector.d.apply(entity1)))
      {
        double d1 = t0.h(entity1);
        if (d1 <= d0)
        {
          entity = entity1;
          d0 = d1;
        }
      }
    }
    return entity;
  }
  
  public Entity a(int i)
  {
    return (Entity)this.entitiesById.get(i);
  }
  
  public void b(BlockPosition blockposition, TileEntity tileentity)
  {
    if (isLoaded(blockposition)) {
      getChunkAtWorldCoords(blockposition).e();
    }
  }
  
  public int a(Class<?> oclass)
  {
    int i = 0;
    Iterator iterator = this.entityList.iterator();
    while (iterator.hasNext())
    {
      Entity entity = (Entity)iterator.next();
      if ((entity instanceof EntityInsentient))
      {
        EntityInsentient entityinsentient = (EntityInsentient)entity;
        if ((entityinsentient.isTypeNotPersistent()) && (entityinsentient.isPersistent())) {}
      }
      else if (oclass.isAssignableFrom(entity.getClass()))
      {
        i++;
      }
    }
    return i;
  }
  
  public void b(Collection<Entity> collection)
  {
    AsyncCatcher.catchOp("entity world add");
    
    Iterator iterator = collection.iterator();
    while (iterator.hasNext())
    {
      Entity entity = (Entity)iterator.next();
      if (entity != null)
      {
        this.entityList.add(entity);
        
        a(entity);
      }
    }
  }
  
  public void c(Collection<Entity> collection)
  {
    this.g.addAll(collection);
  }
  
  public boolean a(Block block, BlockPosition blockposition, boolean flag, EnumDirection enumdirection, Entity entity, ItemStack itemstack)
  {
    Block block1 = getType(blockposition).getBlock();
    AxisAlignedBB axisalignedbb = flag ? null : block.a(this, blockposition, block.getBlockData());
    
    boolean defaultReturn = (axisalignedbb == null) || (a(axisalignedbb, entity));
    BlockCanBuildEvent event = new BlockCanBuildEvent(getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftMagicNumbers.getId(block), defaultReturn);
    getServer().getPluginManager().callEvent(event);
    
    return event.isBuildable();
  }
  
  public int F()
  {
    return this.a;
  }
  
  public void b(int i)
  {
    this.a = i;
  }
  
  public int getBlockPower(BlockPosition blockposition, EnumDirection enumdirection)
  {
    IBlockData iblockdata = getType(blockposition);
    
    return iblockdata.getBlock().b(this, blockposition, iblockdata, enumdirection);
  }
  
  public WorldType G()
  {
    return this.worldData.getType();
  }
  
  public int getBlockPower(BlockPosition blockposition)
  {
    byte b0 = 0;
    int i = Math.max(b0, getBlockPower(blockposition.down(), EnumDirection.DOWN));
    if (i >= 15) {
      return i;
    }
    i = Math.max(i, getBlockPower(blockposition.up(), EnumDirection.UP));
    if (i >= 15) {
      return i;
    }
    i = Math.max(i, getBlockPower(blockposition.north(), EnumDirection.NORTH));
    if (i >= 15) {
      return i;
    }
    i = Math.max(i, getBlockPower(blockposition.south(), EnumDirection.SOUTH));
    if (i >= 15) {
      return i;
    }
    i = Math.max(i, getBlockPower(blockposition.west(), EnumDirection.WEST));
    if (i >= 15) {
      return i;
    }
    i = Math.max(i, getBlockPower(blockposition.east(), EnumDirection.EAST));
    return i >= 15 ? i : i;
  }
  
  public boolean isBlockFacePowered(BlockPosition blockposition, EnumDirection enumdirection)
  {
    return getBlockFacePower(blockposition, enumdirection) > 0;
  }
  
  public int getBlockFacePower(BlockPosition blockposition, EnumDirection enumdirection)
  {
    IBlockData iblockdata = getType(blockposition);
    Block block = iblockdata.getBlock();
    
    return block.isOccluding() ? getBlockPower(blockposition) : block.a(this, blockposition, iblockdata, enumdirection);
  }
  
  public boolean isBlockIndirectlyPowered(BlockPosition blockposition)
  {
    return getBlockFacePower(blockposition.down(), EnumDirection.DOWN) > 0;
  }
  
  public int A(BlockPosition blockposition)
  {
    int i = 0;
    EnumDirection[] aenumdirection = EnumDirection.values();
    int j = aenumdirection.length;
    for (int k = 0; k < j; k++)
    {
      EnumDirection enumdirection = aenumdirection[k];
      int l = getBlockFacePower(blockposition.shift(enumdirection), enumdirection);
      if (l >= 15) {
        return 15;
      }
      if (l > i) {
        i = l;
      }
    }
    return i;
  }
  
  public EntityHuman findNearbyPlayer(Entity entity, double d0)
  {
    return findNearbyPlayer(entity.locX, entity.locY, entity.locZ, d0);
  }
  
  public EntityHuman findNearbyPlayer(double d0, double d1, double d2, double d3)
  {
    double d4 = -1.0D;
    EntityHuman entityhuman = null;
    for (int i = 0; i < this.players.size(); i++)
    {
      EntityHuman entityhuman1 = (EntityHuman)this.players.get(i);
      if ((entityhuman1 != null) && (!entityhuman1.dead)) {
        if (IEntitySelector.d.apply(entityhuman1))
        {
          double d5 = entityhuman1.e(d0, d1, d2);
          if (((d3 < 0.0D) || (d5 < d3 * d3)) && ((d4 == -1.0D) || (d5 < d4)))
          {
            d4 = d5;
            entityhuman = entityhuman1;
          }
        }
      }
    }
    return entityhuman;
  }
  
  public boolean isPlayerNearby(double d0, double d1, double d2, double d3)
  {
    for (int i = 0; i < this.players.size(); i++)
    {
      EntityHuman entityhuman = (EntityHuman)this.players.get(i);
      if (IEntitySelector.d.apply(entityhuman))
      {
        double d4 = entityhuman.e(d0, d1, d2);
        if ((d3 < 0.0D) || (d4 < d3 * d3)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public EntityHuman a(String s)
  {
    for (int i = 0; i < this.players.size(); i++)
    {
      EntityHuman entityhuman = (EntityHuman)this.players.get(i);
      if (s.equals(entityhuman.getName())) {
        return entityhuman;
      }
    }
    return null;
  }
  
  public EntityHuman b(UUID uuid)
  {
    for (int i = 0; i < this.players.size(); i++)
    {
      EntityHuman entityhuman = (EntityHuman)this.players.get(i);
      if (uuid.equals(entityhuman.getUniqueID())) {
        return entityhuman;
      }
    }
    return null;
  }
  
  public void checkSession()
    throws ExceptionWorldConflict
  {
    this.dataManager.checkSession();
  }
  
  public long getSeed()
  {
    return this.worldData.getSeed();
  }
  
  public long getTime()
  {
    return this.worldData.getTime();
  }
  
  public long getDayTime()
  {
    return this.worldData.getDayTime();
  }
  
  public void setDayTime(long i)
  {
    this.worldData.setDayTime(i);
  }
  
  public BlockPosition getSpawn()
  {
    BlockPosition blockposition = new BlockPosition(this.worldData.c(), this.worldData.d(), this.worldData.e());
    if (!getWorldBorder().a(blockposition)) {
      blockposition = getHighestBlockYAt(new BlockPosition(getWorldBorder().getCenterX(), 0.0D, getWorldBorder().getCenterZ()));
    }
    return blockposition;
  }
  
  public void B(BlockPosition blockposition)
  {
    this.worldData.setSpawn(blockposition);
  }
  
  public boolean a(EntityHuman entityhuman, BlockPosition blockposition)
  {
    return true;
  }
  
  public void broadcastEntityEffect(Entity entity, byte b0) {}
  
  public IChunkProvider N()
  {
    return this.chunkProvider;
  }
  
  public void playBlockAction(BlockPosition blockposition, Block block, int i, int j)
  {
    block.a(this, blockposition, getType(blockposition), i, j);
  }
  
  public IDataManager getDataManager()
  {
    return this.dataManager;
  }
  
  public WorldData getWorldData()
  {
    return this.worldData;
  }
  
  public GameRules getGameRules()
  {
    return this.worldData.x();
  }
  
  public void everyoneSleeping() {}
  
  public void checkSleepStatus()
  {
    if (!this.isClientSide) {
      everyoneSleeping();
    }
  }
  
  public float h(float f)
  {
    return (this.q + (this.r - this.q) * f) * j(f);
  }
  
  public float j(float f)
  {
    return this.o + (this.p - this.o) * f;
  }
  
  public boolean R()
  {
    return h(1.0F) > 0.9D;
  }
  
  public boolean S()
  {
    return j(1.0F) > 0.2D;
  }
  
  public boolean isRainingAt(BlockPosition blockposition)
  {
    if (!S()) {
      return false;
    }
    if (!i(blockposition)) {
      return false;
    }
    if (q(blockposition).getY() > blockposition.getY()) {
      return false;
    }
    BiomeBase biomebase = getBiome(blockposition);
    
    return f(blockposition, false) ? false : biomebase.d() ? false : biomebase.e();
  }
  
  public boolean D(BlockPosition blockposition)
  {
    BiomeBase biomebase = getBiome(blockposition);
    
    return biomebase.f();
  }
  
  public PersistentCollection T()
  {
    return this.worldMaps;
  }
  
  public void a(String s, PersistentBase persistentbase)
  {
    this.worldMaps.a(s, persistentbase);
  }
  
  public PersistentBase a(Class<? extends PersistentBase> oclass, String s)
  {
    return this.worldMaps.get(oclass, s);
  }
  
  public int b(String s)
  {
    return this.worldMaps.a(s);
  }
  
  public void a(int i, BlockPosition blockposition, int j)
  {
    for (int k = 0; k < this.u.size(); k++) {
      ((IWorldAccess)this.u.get(k)).a(i, blockposition, j);
    }
  }
  
  public void triggerEffect(int i, BlockPosition blockposition, int j)
  {
    a(null, i, blockposition, j);
  }
  
  public void a(EntityHuman entityhuman, int i, BlockPosition blockposition, int j)
  {
    try
    {
      for (int k = 0; k < this.u.size(); k++) {
        ((IWorldAccess)this.u.get(k)).a(entityhuman, i, blockposition, j);
      }
    }
    catch (Throwable throwable)
    {
      CrashReport crashreport = CrashReport.a(throwable, "Playing level event");
      CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Level event being played");
      
      crashreportsystemdetails.a("Block coordinates", CrashReportSystemDetails.a(blockposition));
      crashreportsystemdetails.a("Event source", entityhuman);
      crashreportsystemdetails.a("Event type", Integer.valueOf(i));
      crashreportsystemdetails.a("Event data", Integer.valueOf(j));
      throw new ReportedException(crashreport);
    }
  }
  
  public int getHeight()
  {
    return 256;
  }
  
  public int V()
  {
    return this.worldProvider.o() ? 128 : 256;
  }
  
  public Random a(int i, int j, int k)
  {
    long l = i * 341873128712L + j * 132897987541L + getWorldData().getSeed() + k;
    
    this.random.setSeed(l);
    return this.random;
  }
  
  public BlockPosition a(String s, BlockPosition blockposition)
  {
    return N().findNearestMapFeature(this, s, blockposition);
  }
  
  public CrashReportSystemDetails a(CrashReport crashreport)
  {
    CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Affected level", 1);
    
    crashreportsystemdetails.a("Level name", this.worldData == null ? "????" : this.worldData.getName());
    crashreportsystemdetails.a("All players", new Callable()
    {
      public String a()
      {
        return World.this.players.size() + " total; " + World.this.players.toString();
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    crashreportsystemdetails.a("Chunk stats", new Callable()
    {
      public String a()
      {
        return World.this.chunkProvider.getName();
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    try
    {
      this.worldData.a(crashreportsystemdetails);
    }
    catch (Throwable throwable)
    {
      crashreportsystemdetails.a("Level Data Unobtainable", throwable);
    }
    return crashreportsystemdetails;
  }
  
  public void c(int i, BlockPosition blockposition, int j)
  {
    for (int k = 0; k < this.u.size(); k++)
    {
      IWorldAccess iworldaccess = (IWorldAccess)this.u.get(k);
      
      iworldaccess.b(i, blockposition, j);
    }
  }
  
  public Calendar Y()
  {
    if (getTime() % 600L == 0L) {
      this.K.setTimeInMillis(MinecraftServer.az());
    }
    return this.K;
  }
  
  public Scoreboard getScoreboard()
  {
    return this.scoreboard;
  }
  
  public void updateAdjacentComparators(BlockPosition blockposition, Block block)
  {
    Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();
    while (iterator.hasNext())
    {
      EnumDirection enumdirection = (EnumDirection)iterator.next();
      BlockPosition blockposition1 = blockposition.shift(enumdirection);
      if (isLoaded(blockposition1))
      {
        IBlockData iblockdata = getType(blockposition1);
        if (Blocks.UNPOWERED_COMPARATOR.e(iblockdata.getBlock()))
        {
          iblockdata.getBlock().doPhysics(this, blockposition1, iblockdata, block);
        }
        else if (iblockdata.getBlock().isOccluding())
        {
          blockposition1 = blockposition1.shift(enumdirection);
          iblockdata = getType(blockposition1);
          if (Blocks.UNPOWERED_COMPARATOR.e(iblockdata.getBlock())) {
            iblockdata.getBlock().doPhysics(this, blockposition1, iblockdata, block);
          }
        }
      }
    }
  }
  
  public DifficultyDamageScaler E(BlockPosition blockposition)
  {
    long i = 0L;
    float f = 0.0F;
    if (isLoaded(blockposition))
    {
      f = y();
      i = getChunkAtWorldCoords(blockposition).w();
    }
    return new DifficultyDamageScaler(getDifficulty(), getDayTime(), i, f);
  }
  
  public EnumDifficulty getDifficulty()
  {
    return getWorldData().getDifficulty();
  }
  
  public int ab()
  {
    return this.I;
  }
  
  public void c(int i)
  {
    this.I = i;
  }
  
  public void d(int i)
  {
    this.J = i;
  }
  
  public boolean ad()
  {
    return this.isLoading;
  }
  
  public PersistentVillage ae()
  {
    return this.villages;
  }
  
  public WorldBorder getWorldBorder()
  {
    return this.N;
  }
  
  public boolean c(int i, int j)
  {
    BlockPosition blockposition = getSpawn();
    int k = i * 16 + 8 - blockposition.getX();
    int l = j * 16 + 8 - blockposition.getZ();
    short short0 = 128;
    
    return (k >= -short0) && (k <= short0) && (l >= -short0) && (l <= short0) && (this.keepSpawnInMemory);
  }
}
