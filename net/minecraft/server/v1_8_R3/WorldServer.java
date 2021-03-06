package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import gnu.trove.iterator.TLongShortIterator;
import gnu.trove.map.hash.TLongShortHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftTravelAgent;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings.WorldTimingsHandler;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.generator.CustomChunkGenerator;
import org.bukkit.craftbukkit.v1_8_R3.generator.InternalChunkGenerator;
import org.bukkit.craftbukkit.v1_8_R3.generator.NetherChunkGenerator;
import org.bukkit.craftbukkit.v1_8_R3.generator.NormalChunkGenerator;
import org.bukkit.craftbukkit.v1_8_R3.generator.SkyLandsChunkGenerator;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_8_R3.util.HashTreeSet;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHashSet;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotWorldConfig;

public class WorldServer
  extends World
  implements IAsyncTaskHandler
{
  private static final org.apache.logging.log4j.Logger a = ;
  private final MinecraftServer server;
  public EntityTracker tracker;
  private final PlayerChunkMap manager;
  private final HashTreeSet<NextTickListEntry> M = new HashTreeSet();
  private final Map<UUID, Entity> entitiesByUUID = Maps.newHashMap();
  public ChunkProviderServer chunkProviderServer;
  public boolean savingDisabled;
  private boolean O;
  private int emptyTime;
  private final PortalTravelAgent Q;
  private final SpawnerCreature R = new SpawnerCreature();
  protected final VillageSiege siegeManager = new VillageSiege(this);
  private BlockActionDataList[] S = { new BlockActionDataList(null), new BlockActionDataList(null) };
  private int T;
  private static final List<StructurePieceTreasure> U = Lists.newArrayList(new StructurePieceTreasure[] { new StructurePieceTreasure(Items.STICK, 0, 1, 3, 10), new StructurePieceTreasure(Item.getItemOf(Blocks.PLANKS), 0, 1, 3, 10), new StructurePieceTreasure(Item.getItemOf(Blocks.LOG), 0, 1, 3, 10), new StructurePieceTreasure(Items.STONE_AXE, 0, 1, 1, 3), new StructurePieceTreasure(Items.WOODEN_AXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.STONE_PICKAXE, 0, 1, 1, 3), new StructurePieceTreasure(Items.WOODEN_PICKAXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.APPLE, 0, 2, 3, 5), new StructurePieceTreasure(Items.BREAD, 0, 2, 3, 3), new StructurePieceTreasure(Item.getItemOf(Blocks.LOG2), 0, 1, 3, 10) });
  private List<NextTickListEntry> V = Lists.newArrayList();
  public final int dimension;
  
  public WorldServer(MinecraftServer minecraftserver, IDataManager idatamanager, WorldData worlddata, int i, MethodProfiler methodprofiler, World.Environment env, ChunkGenerator gen)
  {
    super(idatamanager, worlddata, WorldProvider.byDimension(env.getId()), methodprofiler, false, gen, env);
    this.dimension = i;
    this.pvpMode = minecraftserver.getPVP();
    worlddata.world = this;
    
    this.server = minecraftserver;
    this.tracker = new EntityTracker(this);
    this.manager = new PlayerChunkMap(this, this.spigotConfig.viewDistance);
    this.worldProvider.a(this);
    this.chunkProvider = k();
    this.Q = new CraftTravelAgent(this);
    B();
    C();
    getWorldBorder().a(minecraftserver.aI());
  }
  
  public World b()
  {
    this.worldMaps = new PersistentCollection(this.dataManager);
    String s = PersistentVillage.a(this.worldProvider);
    PersistentVillage persistentvillage = (PersistentVillage)this.worldMaps.get(PersistentVillage.class, s);
    if (persistentvillage == null)
    {
      this.villages = new PersistentVillage(this);
      this.worldMaps.a(s, this.villages);
    }
    else
    {
      this.villages = persistentvillage;
      this.villages.a(this);
    }
    if (getServer().getScoreboardManager() == null)
    {
      this.scoreboard = new ScoreboardServer(this.server);
      PersistentScoreboard persistentscoreboard = (PersistentScoreboard)this.worldMaps.get(PersistentScoreboard.class, "scoreboard");
      if (persistentscoreboard == null)
      {
        persistentscoreboard = new PersistentScoreboard();
        this.worldMaps.a("scoreboard", persistentscoreboard);
      }
      persistentscoreboard.a(this.scoreboard);
      ((ScoreboardServer)this.scoreboard).a(persistentscoreboard);
    }
    else
    {
      this.scoreboard = getServer().getScoreboardManager().getMainScoreboard().getHandle();
    }
    getWorldBorder().setCenter(this.worldData.C(), this.worldData.D());
    getWorldBorder().setDamageAmount(this.worldData.I());
    getWorldBorder().setDamageBuffer(this.worldData.H());
    getWorldBorder().setWarningDistance(this.worldData.J());
    getWorldBorder().setWarningTime(this.worldData.K());
    if (this.worldData.F() > 0L) {
      getWorldBorder().transitionSizeBetween(this.worldData.E(), this.worldData.G(), this.worldData.F());
    } else {
      getWorldBorder().setSize(this.worldData.E());
    }
    if (this.generator != null) {
      getWorld().getPopulators().addAll(this.generator.getDefaultPopulators(getWorld()));
    }
    return this;
  }
  
  public TileEntity getTileEntity(BlockPosition pos)
  {
    TileEntity result = super.getTileEntity(pos);
    Block type = getType(pos).getBlock();
    if ((type == Blocks.CHEST) || (type == Blocks.TRAPPED_CHEST))
    {
      if (!(result instanceof TileEntityChest)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.FURNACE)
    {
      if (!(result instanceof TileEntityFurnace)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.DROPPER)
    {
      if (!(result instanceof TileEntityDropper)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.DISPENSER)
    {
      if (!(result instanceof TileEntityDispenser)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.JUKEBOX)
    {
      if (!(result instanceof BlockJukeBox.TileEntityRecordPlayer)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.NOTEBLOCK)
    {
      if (!(result instanceof TileEntityNote)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.MOB_SPAWNER)
    {
      if (!(result instanceof TileEntityMobSpawner)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if ((type == Blocks.STANDING_SIGN) || (type == Blocks.WALL_SIGN))
    {
      if (!(result instanceof TileEntitySign)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.ENDER_CHEST)
    {
      if (!(result instanceof TileEntityEnderChest)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.BREWING_STAND)
    {
      if (!(result instanceof TileEntityBrewingStand)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if (type == Blocks.BEACON)
    {
      if (!(result instanceof TileEntityBeacon)) {
        result = fixTileEntity(pos, type, result);
      }
    }
    else if ((type == Blocks.HOPPER) && 
      (!(result instanceof TileEntityHopper))) {
      result = fixTileEntity(pos, type, result);
    }
    return result;
  }
  
  private TileEntity fixTileEntity(BlockPosition pos, Block type, TileEntity found)
  {
    getServer().getLogger().log(Level.SEVERE, "Block at {0},{1},{2} is {3} but has {4}. Bukkit will attempt to fix this, but there may be additional damage that we cannot recover.", 
      new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), org.bukkit.Material.getMaterial(Block.getId(type)).toString(), found });
    if ((type instanceof IContainer))
    {
      TileEntity replacement = ((IContainer)type).a(this, type.toLegacyData(getType(pos)));
      replacement.world = this;
      setTileEntity(pos, replacement);
      return replacement;
    }
    getServer().getLogger().severe("Don't know how to fix for this type... Can't do anything! :(");
    return found;
  }
  
  private boolean canSpawn(int x, int z)
  {
    if (this.generator != null) {
      return this.generator.canSpawn(getWorld(), x, z);
    }
    return this.worldProvider.canSpawn(x, z);
  }
  
  public void doTick()
  {
    super.doTick();
    if ((getWorldData().isHardcore()) && (getDifficulty() != EnumDifficulty.HARD)) {
      getWorldData().setDifficulty(EnumDifficulty.HARD);
    }
    this.worldProvider.m().b();
    if (everyoneDeeplySleeping())
    {
      if (getGameRules().getBoolean("doDaylightCycle"))
      {
        long i = this.worldData.getDayTime() + 24000L;
        
        this.worldData.setDayTime(i - i % 24000L);
      }
      e();
    }
    long time = this.worldData.getTime();
    if ((getGameRules().getBoolean("doMobSpawning")) && (this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES) && ((this.allowMonsters) || (this.allowAnimals)) && ((this instanceof WorldServer)) && (this.players.size() > 0))
    {
      this.timings.mobSpawn.startTiming();
      this.R.a(this, (this.allowMonsters) && (this.ticksPerMonsterSpawns != 0L) && (time % this.ticksPerMonsterSpawns == 0L), (this.allowAnimals) && (this.ticksPerAnimalSpawns != 0L) && (time % this.ticksPerAnimalSpawns == 0L), this.worldData.getTime() % 400L == 0L);
      this.timings.mobSpawn.stopTiming();
    }
    this.timings.doChunkUnload.startTiming();
    this.methodProfiler.c("chunkSource");
    this.chunkProvider.unloadChunks();
    int j = a(1.0F);
    if (j != ab()) {
      c(j);
    }
    this.worldData.setTime(this.worldData.getTime() + 1L);
    if (getGameRules().getBoolean("doDaylightCycle")) {
      this.worldData.setDayTime(this.worldData.getDayTime() + 1L);
    }
    this.timings.doChunkUnload.stopTiming();
    this.methodProfiler.c("tickPending");
    this.timings.doTickPending.startTiming();
    a(false);
    this.timings.doTickPending.stopTiming();
    this.methodProfiler.c("tickBlocks");
    this.timings.doTickTiles.startTiming();
    h();
    this.timings.doTickTiles.stopTiming();
    this.methodProfiler.c("chunkMap");
    this.timings.doChunkMap.startTiming();
    this.manager.flush();
    this.timings.doChunkMap.stopTiming();
    this.methodProfiler.c("village");
    this.timings.doVillages.startTiming();
    this.villages.tick();
    this.siegeManager.a();
    this.timings.doVillages.stopTiming();
    this.methodProfiler.c("portalForcer");
    this.timings.doPortalForcer.startTiming();
    this.Q.a(getTime());
    this.timings.doPortalForcer.stopTiming();
    this.methodProfiler.b();
    this.timings.doSounds.startTiming();
    ak();
    
    getWorld().processChunkGC();
    this.timings.doChunkGC.stopTiming();
  }
  
  public BiomeBase.BiomeMeta a(EnumCreatureType enumcreaturetype, BlockPosition blockposition)
  {
    List list = N().getMobsFor(enumcreaturetype, blockposition);
    
    return (list != null) && (!list.isEmpty()) ? (BiomeBase.BiomeMeta)WeightedRandom.a(this.random, list) : null;
  }
  
  public boolean a(EnumCreatureType enumcreaturetype, BiomeBase.BiomeMeta biomebase_biomemeta, BlockPosition blockposition)
  {
    List list = N().getMobsFor(enumcreaturetype, blockposition);
    
    return (list != null) && (!list.isEmpty()) ? list.contains(biomebase_biomemeta) : false;
  }
  
  public void everyoneSleeping()
  {
    this.O = false;
    if (!this.players.isEmpty())
    {
      int i = 0;
      int j = 0;
      Iterator iterator = this.players.iterator();
      while (iterator.hasNext())
      {
        EntityHuman entityhuman = (EntityHuman)iterator.next();
        if (entityhuman.isSpectator()) {
          i++;
        } else if ((entityhuman.isSleeping()) || (entityhuman.fauxSleeping)) {
          j++;
        }
      }
      this.O = ((j > 0) && (j >= this.players.size() - i));
    }
  }
  
  protected void e()
  {
    this.O = false;
    Iterator iterator = this.players.iterator();
    while (iterator.hasNext())
    {
      EntityHuman entityhuman = (EntityHuman)iterator.next();
      if (entityhuman.isSleeping()) {
        entityhuman.a(false, false, true);
      }
    }
    ag();
  }
  
  private void ag()
  {
    this.worldData.setStorm(false);
    if (!this.worldData.hasStorm()) {
      this.worldData.setWeatherDuration(0);
    }
    this.worldData.setThundering(false);
    if (!this.worldData.isThundering()) {
      this.worldData.setThunderDuration(0);
    }
  }
  
  public boolean everyoneDeeplySleeping()
  {
    if ((this.O) && (!this.isClientSide))
    {
      Iterator iterator = this.players.iterator();
      
      boolean foundActualSleepers = false;
      EntityHuman entityhuman;
      do
      {
        if (!iterator.hasNext()) {
          return foundActualSleepers;
        }
        entityhuman = (EntityHuman)iterator.next();
        if (entityhuman.isDeeplySleeping()) {
          foundActualSleepers = true;
        }
      } while ((!entityhuman.isSpectator()) && ((entityhuman.isDeeplySleeping()) || (entityhuman.fauxSleeping)));
      return false;
    }
    return false;
  }
  
  protected void h()
  {
    super.h();
    if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES)
    {
      TLongShortIterator iterator = this.chunkTickList.iterator();
      while (iterator.hasNext())
      {
        iterator.advance();
        long chunkCoord = iterator.key();
        
        getChunkAt(World.keyToX(chunkCoord), World.keyToZ(chunkCoord)).b(false);
      }
    }
    else
    {
      for (TLongShortIterator iter = this.chunkTickList.iterator(); iter.hasNext();)
      {
        iter.advance();
        long chunkCoord = iter.key();
        int chunkX = World.keyToX(chunkCoord);
        int chunkZ = World.keyToZ(chunkCoord);
        if ((!this.chunkProvider.isChunkLoaded(chunkX, chunkZ)) || (this.chunkProviderServer.unloadQueue.contains(chunkX, chunkZ)))
        {
          iter.remove();
        }
        else
        {
          int k = chunkX * 16;
          int l = chunkZ * 16;
          
          this.methodProfiler.a("getChunk");
          Chunk chunk = getChunkAt(chunkX, chunkZ);
          
          a(k, l, chunk);
          this.methodProfiler.c("tickChunk");
          chunk.b(false);
          this.methodProfiler.c("thunder");
          if ((this.random.nextInt(100000) == 0) && (S()) && (R()))
          {
            this.m = (this.m * 3 + 1013904223);
            int i1 = this.m >> 2;
            BlockPosition blockposition = a(new BlockPosition(k + (i1 & 0xF), 0, l + (i1 >> 8 & 0xF)));
            if (isRainingAt(blockposition)) {
              strikeLightning(new EntityLightning(this, blockposition.getX(), blockposition.getY(), blockposition.getZ()));
            }
          }
          this.methodProfiler.c("iceandsnow");
          if (this.random.nextInt(16) == 0)
          {
            this.m = (this.m * 3 + 1013904223);
            int i1 = this.m >> 2;
            BlockPosition blockposition = q(new BlockPosition(k + (i1 & 0xF), 0, l + (i1 >> 8 & 0xF)));
            BlockPosition blockposition1 = blockposition.down();
            if (w(blockposition1))
            {
              BlockState blockState = getWorld().getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()).getState();
              blockState.setTypeId(Block.getId(Blocks.ICE));
              
              BlockFormEvent iceBlockForm = new BlockFormEvent(blockState.getBlock(), blockState);
              getServer().getPluginManager().callEvent(iceBlockForm);
              if (!iceBlockForm.isCancelled()) {
                blockState.update(true);
              }
            }
            if ((S()) && (f(blockposition, true)))
            {
              BlockState blockState = getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getState();
              blockState.setTypeId(Block.getId(Blocks.SNOW_LAYER));
              
              BlockFormEvent snow = new BlockFormEvent(blockState.getBlock(), blockState);
              getServer().getPluginManager().callEvent(snow);
              if (!snow.isCancelled()) {
                blockState.update(true);
              }
            }
            if ((S()) && (getBiome(blockposition1).e())) {
              getType(blockposition1).getBlock().k(this, blockposition1);
            }
          }
          this.methodProfiler.c("tickBlocks");
          int i1 = getGameRules().c("randomTickSpeed");
          if (i1 > 0)
          {
            ChunkSection[] achunksection = chunk.getSections();
            int j1 = achunksection.length;
            for (int k1 = 0; k1 < j1; k1++)
            {
              ChunkSection chunksection = achunksection[k1];
              if ((chunksection != null) && (chunksection.shouldTick())) {
                for (int l1 = 0; l1 < i1; l1++)
                {
                  this.m = (this.m * 3 + 1013904223);
                  int i2 = this.m >> 2;
                  int j2 = i2 & 0xF;
                  int k2 = i2 >> 8 & 0xF;
                  int l2 = i2 >> 16 & 0xF;
                  
                  IBlockData iblockdata = chunksection.getType(j2, l2, k2);
                  Block block = iblockdata.getBlock();
                  if (block.isTicking()) {
                    block.a(this, new BlockPosition(j2 + k, l2 + chunksection.getYPosition(), k2 + l), iblockdata, this.random);
                  }
                }
              }
            }
          }
        }
      }
    }
    if (this.spigotConfig.clearChunksOnTick) {
      this.chunkTickList.clear();
    }
  }
  
  protected BlockPosition a(BlockPosition blockposition)
  {
    BlockPosition blockposition1 = q(blockposition);
    AxisAlignedBB axisalignedbb = new AxisAlignedBB(blockposition1, new BlockPosition(blockposition1.getX(), getHeight(), blockposition1.getZ())).grow(3.0D, 3.0D, 3.0D);
    List list = a(EntityLiving.class, axisalignedbb, new Predicate()
    {
      public boolean a(EntityLiving entityliving)
      {
        return (entityliving != null) && (entityliving.isAlive()) && (WorldServer.this.i(entityliving.getChunkCoordinates()));
      }
      
      public boolean apply(Object object)
      {
        return a((EntityLiving)object);
      }
    });
    return !list.isEmpty() ? ((EntityLiving)list.get(this.random.nextInt(list.size()))).getChunkCoordinates() : blockposition1;
  }
  
  public boolean a(BlockPosition blockposition, Block block)
  {
    NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
    
    return this.V.contains(nextticklistentry);
  }
  
  public void a(BlockPosition blockposition, Block block, int i)
  {
    a(blockposition, block, i, 0);
  }
  
  public void a(BlockPosition blockposition, Block block, int i, int j)
  {
    NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
    byte b0 = 0;
    if ((this.e) && (block.getMaterial() != Material.AIR))
    {
      if (block.N())
      {
        b0 = 8;
        if (areChunksLoadedBetween(nextticklistentry.a.a(-b0, -b0, -b0), nextticklistentry.a.a(b0, b0, b0)))
        {
          IBlockData iblockdata = getType(nextticklistentry.a);
          if ((iblockdata.getBlock().getMaterial() != Material.AIR) && (iblockdata.getBlock() == nextticklistentry.a())) {
            iblockdata.getBlock().b(this, nextticklistentry.a, iblockdata, this.random);
          }
        }
        return;
      }
      i = 1;
    }
    if (areChunksLoadedBetween(blockposition.a(-b0, -b0, -b0), blockposition.a(b0, b0, b0)))
    {
      if (block.getMaterial() != Material.AIR)
      {
        nextticklistentry.a(i + this.worldData.getTime());
        nextticklistentry.a(j);
      }
      if (!this.M.contains(nextticklistentry)) {
        this.M.add(nextticklistentry);
      }
    }
  }
  
  public void b(BlockPosition blockposition, Block block, int i, int j)
  {
    NextTickListEntry nextticklistentry = new NextTickListEntry(blockposition, block);
    
    nextticklistentry.a(j);
    if (block.getMaterial() != Material.AIR) {
      nextticklistentry.a(i + this.worldData.getTime());
    }
    if (!this.M.contains(nextticklistentry)) {
      this.M.add(nextticklistentry);
    }
  }
  
  public void tickEntities()
  {
    j();
    
    super.tickEntities();
    this.spigotConfig.currentPrimedTnt = 0;
  }
  
  public void j()
  {
    this.emptyTime = 0;
  }
  
  public boolean a(boolean flag)
  {
    if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
      return false;
    }
    int i = this.M.size();
    if (i > 1000) {
      if (i > 20000) {
        i /= 20;
      } else {
        i = 1000;
      }
    }
    this.methodProfiler.a("cleaning");
    for (int j = 0; j < i; j++)
    {
      NextTickListEntry nextticklistentry = (NextTickListEntry)this.M.first();
      if ((!flag) && (nextticklistentry.b > this.worldData.getTime())) {
        break;
      }
      this.M.remove(nextticklistentry);
      this.V.add(nextticklistentry);
    }
    this.methodProfiler.b();
    this.methodProfiler.a("ticking");
    Iterator iterator = this.V.iterator();
    while (iterator.hasNext())
    {
      NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();
      iterator.remove();
      byte b0 = 0;
      if (areChunksLoadedBetween(nextticklistentry.a.a(-b0, -b0, -b0), nextticklistentry.a.a(b0, b0, b0)))
      {
        IBlockData iblockdata = getType(nextticklistentry.a);
        if ((iblockdata.getBlock().getMaterial() != Material.AIR) && (Block.a(iblockdata.getBlock(), nextticklistentry.a()))) {
          try
          {
            iblockdata.getBlock().b(this, nextticklistentry.a, iblockdata, this.random);
          }
          catch (Throwable throwable)
          {
            CrashReport crashreport = CrashReport.a(throwable, "Exception while ticking a block");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being ticked");
            
            CrashReportSystemDetails.a(crashreportsystemdetails, nextticklistentry.a, iblockdata);
            throw new ReportedException(crashreport);
          }
        }
      }
      else
      {
        a(nextticklistentry.a, nextticklistentry.a(), 0);
      }
    }
    this.methodProfiler.b();
    this.V.clear();
    return !this.M.isEmpty();
  }
  
  public List<NextTickListEntry> a(Chunk chunk, boolean flag)
  {
    ChunkCoordIntPair chunkcoordintpair = chunk.j();
    int i = (chunkcoordintpair.x << 4) - 2;
    int j = i + 16 + 2;
    int k = (chunkcoordintpair.z << 4) - 2;
    int l = k + 16 + 2;
    
    return a(new StructureBoundingBox(i, 0, k, j, 256, l), flag);
  }
  
  public List<NextTickListEntry> a(StructureBoundingBox structureboundingbox, boolean flag)
  {
    ArrayList arraylist = null;
    for (int i = 0; i < 2; i++)
    {
      Iterator iterator;
      Iterator iterator;
      if (i == 0) {
        iterator = this.M.iterator();
      } else {
        iterator = this.V.iterator();
      }
      while (iterator.hasNext())
      {
        NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();
        BlockPosition blockposition = nextticklistentry.a;
        if ((blockposition.getX() >= structureboundingbox.a) && (blockposition.getX() < structureboundingbox.d) && (blockposition.getZ() >= structureboundingbox.c) && (blockposition.getZ() < structureboundingbox.f))
        {
          if (flag) {
            iterator.remove();
          }
          if (arraylist == null) {
            arraylist = Lists.newArrayList();
          }
          arraylist.add(nextticklistentry);
        }
      }
    }
    return arraylist;
  }
  
  private boolean getSpawnNPCs()
  {
    return this.server.getSpawnNPCs();
  }
  
  private boolean getSpawnAnimals()
  {
    return this.server.getSpawnAnimals();
  }
  
  protected IChunkProvider k()
  {
    IChunkLoader ichunkloader = this.dataManager.createChunkLoader(this.worldProvider);
    InternalChunkGenerator gen;
    InternalChunkGenerator gen;
    if (this.generator != null)
    {
      gen = new CustomChunkGenerator(this, getSeed(), this.generator);
    }
    else
    {
      InternalChunkGenerator gen;
      if ((this.worldProvider instanceof WorldProviderHell))
      {
        gen = new NetherChunkGenerator(this, getSeed());
      }
      else
      {
        InternalChunkGenerator gen;
        if ((this.worldProvider instanceof WorldProviderTheEnd)) {
          gen = new SkyLandsChunkGenerator(this, getSeed());
        } else {
          gen = new NormalChunkGenerator(this, getSeed());
        }
      }
    }
    this.chunkProviderServer = new ChunkProviderServer(this, ichunkloader, gen);
    
    return this.chunkProviderServer;
  }
  
  public List<TileEntity> getTileEntities(int i, int j, int k, int l, int i1, int j1)
  {
    ArrayList arraylist = Lists.newArrayList();
    for (int chunkX = i >> 4; chunkX <= l - 1 >> 4; chunkX++) {
      for (int chunkZ = k >> 4; chunkZ <= j1 - 1 >> 4; chunkZ++)
      {
        Chunk chunk = getChunkAt(chunkX, chunkZ);
        if (chunk != null) {
          for (Object te : chunk.tileEntities.values())
          {
            TileEntity tileentity = (TileEntity)te;
            if ((tileentity.position.getX() >= i) && (tileentity.position.getY() >= j) && (tileentity.position.getZ() >= k) && (tileentity.position.getX() < l) && (tileentity.position.getY() < i1) && (tileentity.position.getZ() < j1)) {
              arraylist.add(tileentity);
            }
          }
        }
      }
    }
    return arraylist;
  }
  
  public boolean a(EntityHuman entityhuman, BlockPosition blockposition)
  {
    return (!this.server.a(this, blockposition, entityhuman)) && (getWorldBorder().a(blockposition));
  }
  
  public void a(WorldSettings worldsettings)
  {
    if (!this.worldData.w())
    {
      try
      {
        b(worldsettings);
        if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
          aj();
        }
        super.a(worldsettings);
      }
      catch (Throwable throwable)
      {
        CrashReport crashreport = CrashReport.a(throwable, "Exception initializing level");
        try
        {
          a(crashreport);
        }
        catch (Throwable localThrowable1) {}
        throw new ReportedException(crashreport);
      }
      this.worldData.d(true);
    }
  }
  
  private void aj()
  {
    this.worldData.f(false);
    this.worldData.c(true);
    this.worldData.setStorm(false);
    this.worldData.setThundering(false);
    this.worldData.i(1000000000);
    this.worldData.setDayTime(6000L);
    this.worldData.setGameType(WorldSettings.EnumGamemode.SPECTATOR);
    this.worldData.g(false);
    this.worldData.setDifficulty(EnumDifficulty.PEACEFUL);
    this.worldData.e(true);
    getGameRules().set("doDaylightCycle", "false");
  }
  
  private void b(WorldSettings worldsettings)
  {
    if (!this.worldProvider.e())
    {
      this.worldData.setSpawn(BlockPosition.ZERO.up(this.worldProvider.getSeaLevel()));
    }
    else if (this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES)
    {
      this.worldData.setSpawn(BlockPosition.ZERO.up());
    }
    else
    {
      this.isLoading = true;
      WorldChunkManager worldchunkmanager = this.worldProvider.m();
      List list = worldchunkmanager.a();
      Random random = new Random(getSeed());
      BlockPosition blockposition = worldchunkmanager.a(0, 0, 256, list, random);
      int i = 0;
      int j = this.worldProvider.getSeaLevel();
      int k = 0;
      if (this.generator != null)
      {
        Random rand = new Random(getSeed());
        Location spawn = this.generator.getFixedSpawnLocation(getWorld(), rand);
        if (spawn != null)
        {
          if (spawn.getWorld() != getWorld()) {
            throw new IllegalStateException("Cannot set spawn point for " + this.worldData.getName() + " to be in another world (" + spawn.getWorld().getName() + ")");
          }
          this.worldData.setSpawn(new BlockPosition(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ()));
          this.isLoading = false;
          return;
        }
      }
      if (blockposition != null)
      {
        i = blockposition.getX();
        k = blockposition.getZ();
      }
      else
      {
        a.warn("Unable to find spawn biome");
      }
      int l = 0;
      while (!canSpawn(i, k))
      {
        i += random.nextInt(64) - random.nextInt(64);
        k += random.nextInt(64) - random.nextInt(64);
        l++;
        if (l == 1000) {
          break;
        }
      }
      this.worldData.setSpawn(new BlockPosition(i, j, k));
      this.isLoading = false;
      if (worldsettings.c()) {
        l();
      }
    }
  }
  
  protected void l()
  {
    WorldGenBonusChest worldgenbonuschest = new WorldGenBonusChest(U, 10);
    for (int i = 0; i < 10; i++)
    {
      int j = this.worldData.c() + this.random.nextInt(6) - this.random.nextInt(6);
      int k = this.worldData.e() + this.random.nextInt(6) - this.random.nextInt(6);
      BlockPosition blockposition = r(new BlockPosition(j, 0, k)).up();
      if (worldgenbonuschest.generate(this, this.random, blockposition)) {
        break;
      }
    }
  }
  
  public BlockPosition getDimensionSpawn()
  {
    return this.worldProvider.h();
  }
  
  public void save(boolean flag, IProgressUpdate iprogressupdate)
    throws ExceptionWorldConflict
  {
    if (this.chunkProvider.canSave())
    {
      Bukkit.getPluginManager().callEvent(new WorldSaveEvent(getWorld()));
      if (iprogressupdate != null) {
        iprogressupdate.a("Saving level");
      }
      a();
      if (iprogressupdate != null) {
        iprogressupdate.c("Saving chunks");
      }
      this.chunkProvider.saveChunks(flag, iprogressupdate);
      
      Collection arraylist = this.chunkProviderServer.a();
      Iterator iterator = arraylist.iterator();
      while (iterator.hasNext())
      {
        Chunk chunk = (Chunk)iterator.next();
        if ((chunk != null) && (!this.manager.a(chunk.locX, chunk.locZ))) {
          this.chunkProviderServer.queueUnload(chunk.locX, chunk.locZ);
        }
      }
    }
  }
  
  public void flushSave()
  {
    if (this.chunkProvider.canSave()) {
      this.chunkProvider.c();
    }
  }
  
  protected void a()
    throws ExceptionWorldConflict
  {
    checkSession();
    this.worldData.a(getWorldBorder().getSize());
    this.worldData.d(getWorldBorder().getCenterX());
    this.worldData.c(getWorldBorder().getCenterZ());
    this.worldData.e(getWorldBorder().getDamageBuffer());
    this.worldData.f(getWorldBorder().getDamageAmount());
    this.worldData.j(getWorldBorder().getWarningDistance());
    this.worldData.k(getWorldBorder().getWarningTime());
    this.worldData.b(getWorldBorder().j());
    this.worldData.e(getWorldBorder().i());
    if (!(this instanceof SecondaryWorldServer)) {
      this.worldMaps.a();
    }
    this.dataManager.saveWorldData(this.worldData, this.server.getPlayerList().t());
  }
  
  protected void a(Entity entity)
  {
    super.a(entity);
    this.entitiesById.a(entity.getId(), entity);
    this.entitiesByUUID.put(entity.getUniqueID(), entity);
    Entity[] aentity = entity.aB();
    if (aentity != null) {
      for (int i = 0; i < aentity.length; i++) {
        this.entitiesById.a(aentity[i].getId(), aentity[i]);
      }
    }
  }
  
  protected void b(Entity entity)
  {
    super.b(entity);
    this.entitiesById.d(entity.getId());
    this.entitiesByUUID.remove(entity.getUniqueID());
    Entity[] aentity = entity.aB();
    if (aentity != null) {
      for (int i = 0; i < aentity.length; i++) {
        this.entitiesById.d(aentity[i].getId());
      }
    }
  }
  
  public boolean strikeLightning(Entity entity)
  {
    LightningStrikeEvent lightning = new LightningStrikeEvent(getWorld(), (LightningStrike)entity.getBukkitEntity());
    getServer().getPluginManager().callEvent(lightning);
    if (lightning.isCancelled()) {
      return false;
    }
    if (super.strikeLightning(entity))
    {
      this.server.getPlayerList().sendPacketNearby(entity.locX, entity.locY, entity.locZ, 512.0D, this.dimension, new PacketPlayOutSpawnEntityWeather(entity));
      
      return true;
    }
    return false;
  }
  
  public void broadcastEntityEffect(Entity entity, byte b0)
  {
    getTracker().sendPacketToEntity(entity, new PacketPlayOutEntityStatus(entity, b0));
  }
  
  public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1)
  {
    Explosion explosion = super.createExplosion(entity, d0, d1, d2, f, flag, flag1);
    if (explosion.wasCanceled) {
      return explosion;
    }
    if (!flag1) {
      explosion.clearBlocks();
    }
    Iterator iterator = this.players.iterator();
    while (iterator.hasNext())
    {
      EntityHuman entityhuman = (EntityHuman)iterator.next();
      if (entityhuman.e(d0, d1, d2) < 4096.0D) {
        ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutExplosion(d0, d1, d2, f, explosion.getBlocks(), (Vec3D)explosion.b().get(entityhuman)));
      }
    }
    return explosion;
  }
  
  public void playBlockAction(BlockPosition blockposition, Block block, int i, int j)
  {
    BlockActionData blockactiondata = new BlockActionData(blockposition, block, i, j);
    Iterator iterator = this.S[this.T].iterator();
    BlockActionData blockactiondata1;
    do
    {
      if (!iterator.hasNext())
      {
        this.S[this.T].add(blockactiondata);
        return;
      }
      blockactiondata1 = (BlockActionData)iterator.next();
    } while (!blockactiondata1.equals(blockactiondata));
  }
  
  private void ak()
  {
    while (!this.S[this.T].isEmpty())
    {
      int i = this.T;
      
      this.T ^= 0x1;
      Iterator iterator = this.S[i].iterator();
      while (iterator.hasNext())
      {
        BlockActionData blockactiondata = (BlockActionData)iterator.next();
        if (a(blockactiondata)) {
          this.server.getPlayerList().sendPacketNearby(blockactiondata.a().getX(), blockactiondata.a().getY(), blockactiondata.a().getZ(), 64.0D, this.dimension, new PacketPlayOutBlockAction(blockactiondata.a(), blockactiondata.d(), blockactiondata.b(), blockactiondata.c()));
        }
      }
      this.S[i].clear();
    }
  }
  
  private boolean a(BlockActionData blockactiondata)
  {
    IBlockData iblockdata = getType(blockactiondata.a());
    
    return iblockdata.getBlock() == blockactiondata.d() ? iblockdata.getBlock().a(this, blockactiondata.a(), iblockdata, blockactiondata.b(), blockactiondata.c()) : false;
  }
  
  public void saveLevel()
  {
    this.dataManager.a();
  }
  
  protected void p()
  {
    boolean flag = S();
    
    super.p();
    if (flag != S()) {
      for (int i = 0; i < this.players.size(); i++) {
        if (((EntityPlayer)this.players.get(i)).world == this) {
          ((EntityPlayer)this.players.get(i)).setPlayerWeather(!flag ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
        }
      }
    }
    for (int i = 0; i < this.players.size(); i++) {
      if (((EntityPlayer)this.players.get(i)).world == this) {
        ((EntityPlayer)this.players.get(i)).updateWeather(this.o, this.p, this.q, this.r);
      }
    }
  }
  
  protected int q()
  {
    return this.server.getPlayerList().s();
  }
  
  public MinecraftServer getMinecraftServer()
  {
    return this.server;
  }
  
  public EntityTracker getTracker()
  {
    return this.tracker;
  }
  
  public PlayerChunkMap getPlayerChunkMap()
  {
    return this.manager;
  }
  
  public PortalTravelAgent getTravelAgent()
  {
    return this.Q;
  }
  
  public void a(EnumParticle enumparticle, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint)
  {
    a(enumparticle, false, d0, d1, d2, i, d3, d4, d5, d6, aint);
  }
  
  public void a(EnumParticle enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint)
  {
    sendParticles(null, enumparticle, flag, d0, d1, d2, i, d3, d4, d5, d6, aint);
  }
  
  public void sendParticles(EntityPlayer sender, EnumParticle enumparticle, boolean flag, double d0, double d1, double d2, int i, double d3, double d4, double d5, double d6, int... aint)
  {
    PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(enumparticle, flag, (float)d0, (float)d1, (float)d2, (float)d3, (float)d4, (float)d5, (float)d6, i, aint);
    for (int j = 0; j < this.players.size(); j++)
    {
      EntityPlayer entityplayer = (EntityPlayer)this.players.get(j);
      if ((sender == null) || (entityplayer.getBukkitEntity().canSee(sender.getBukkitEntity())))
      {
        BlockPosition blockposition = entityplayer.getChunkCoordinates();
        double d7 = blockposition.c(d0, d1, d2);
        if ((d7 <= 256.0D) || ((flag) && (d7 <= 65536.0D))) {
          entityplayer.playerConnection.sendPacket(packetplayoutworldparticles);
        }
      }
    }
  }
  
  public Entity getEntity(UUID uuid)
  {
    return (Entity)this.entitiesByUUID.get(uuid);
  }
  
  public ListenableFuture<Object> postToMainThread(Runnable runnable)
  {
    return this.server.postToMainThread(runnable);
  }
  
  public boolean isMainThread()
  {
    return this.server.isMainThread();
  }
  
  static class BlockActionDataList
    extends ArrayList<BlockActionData>
  {
    private BlockActionDataList() {}
    
    BlockActionDataList(Object object)
    {
      this();
    }
  }
}
