package org.bukkit.craftbukkit.v1_8_R3;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockDiodeAbstract;
import net.minecraft.server.v1_8_R3.BlockLeaves;
import net.minecraft.server.v1_8_R3.BlockLeaves1;
import net.minecraft.server.v1_8_R3.BlockLog1;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockWood.EnumLogVariant;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChunkProviderServer;
import net.minecraft.server.v1_8_R3.EmptyChunk;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityEnderSignal;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityEndermite;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHanging;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLeash;
import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntityMinecartChest;
import net.minecraft.server.v1_8_R3.EntityMinecartFurnace;
import net.minecraft.server.v1_8_R3.EntityMinecartHopper;
import net.minecraft.server.v1_8_R3.EntityMinecartMobSpawner;
import net.minecraft.server.v1_8_R3.EntityMinecartRideable;
import net.minecraft.server.v1_8_R3.EntityMinecartTNT;
import net.minecraft.server.v1_8_R3.EntityMushroomCow;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.Explosion;
import net.minecraft.server.v1_8_R3.GameRules;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.IDataManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateTime;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PlayerChunkMap;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.WorldData;
import net.minecraft.server.v1_8_R3.WorldGenAcaciaTree;
import net.minecraft.server.v1_8_R3.WorldGenBigTree;
import net.minecraft.server.v1_8_R3.WorldGenForest;
import net.minecraft.server.v1_8_R3.WorldGenForestTree;
import net.minecraft.server.v1_8_R3.WorldGenGroundBush;
import net.minecraft.server.v1_8_R3.WorldGenHugeMushroom;
import net.minecraft.server.v1_8_R3.WorldGenJungleTree;
import net.minecraft.server.v1_8_R3.WorldGenMegaTree;
import net.minecraft.server.v1_8_R3.WorldGenSwampTree;
import net.minecraft.server.v1_8_R3.WorldGenTaiga1;
import net.minecraft.server.v1_8_R3.WorldGenTaiga2;
import net.minecraft.server.v1_8_R3.WorldGenTrees;
import net.minecraft.server.v1_8_R3.WorldGenerator;
import net.minecraft.server.v1_8_R3.WorldNBTStorage;
import net.minecraft.server.v1_8_R3.WorldProvider;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang.Validate;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Effect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World.Environment;
import org.bukkit.World.Spigot;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLightningStrike;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.metadata.BlockMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHashSet;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Cow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Horse;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Weather;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.util.Vector;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.CustomTimingsHandler;

public class CraftWorld
  implements org.bukkit.World
{
  public static final int CUSTOM_DIMENSION_OFFSET = 10;
  private final WorldServer world;
  private WorldBorder worldBorder;
  private World.Environment environment;
  private final CraftServer server = (CraftServer)Bukkit.getServer();
  private final ChunkGenerator generator;
  private final List<BlockPopulator> populators = new ArrayList();
  private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
  private int monsterSpawn = -1;
  private int animalSpawn = -1;
  private int waterAnimalSpawn = -1;
  private int ambientSpawn = -1;
  private int chunkLoadCount = 0;
  private int chunkGCTickCount;
  private static final Random rand = new Random();
  
  public CraftWorld(WorldServer world, ChunkGenerator gen, World.Environment env)
  {
    this.world = world;
    this.generator = gen;
    
    this.environment = env;
    if (this.server.chunkGCPeriod > 0) {
      this.chunkGCTickCount = rand.nextInt(this.server.chunkGCPeriod);
    }
  }
  
  public org.bukkit.block.Block getBlockAt(int x, int y, int z)
  {
    return getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y, z & 0xF);
  }
  
  public int getBlockTypeIdAt(int x, int y, int z)
  {
    return CraftMagicNumbers.getId(this.world.getType(new BlockPosition(x, y, z)).getBlock());
  }
  
  public int getHighestBlockYAt(int x, int z)
  {
    if (!isChunkLoaded(x >> 4, z >> 4)) {
      loadChunk(x >> 4, z >> 4);
    }
    return this.world.getHighestBlockYAt(new BlockPosition(x, 0, z)).getY();
  }
  
  public Location getSpawnLocation()
  {
    BlockPosition spawn = this.world.getSpawn();
    return new Location(this, spawn.getX(), spawn.getY(), spawn.getZ());
  }
  
  public boolean setSpawnLocation(int x, int y, int z)
  {
    try
    {
      Location previousLocation = getSpawnLocation();
      this.world.worldData.setSpawn(new BlockPosition(x, y, z));
      
      SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
      this.server.getPluginManager().callEvent(event);
      
      return true;
    }
    catch (Exception localException) {}
    return false;
  }
  
  public org.bukkit.Chunk getChunkAt(int x, int z)
  {
    return this.world.chunkProviderServer.getChunkAt(x, z).bukkitChunk;
  }
  
  public org.bukkit.Chunk getChunkAt(org.bukkit.block.Block block)
  {
    return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
  }
  
  public boolean isChunkLoaded(int x, int z)
  {
    return this.world.chunkProviderServer.isChunkLoaded(x, z);
  }
  
  public org.bukkit.Chunk[] getLoadedChunks()
  {
    Object[] chunks = this.world.chunkProviderServer.chunks.values().toArray();
    org.bukkit.Chunk[] craftChunks = new CraftChunk[chunks.length];
    for (int i = 0; i < chunks.length; i++)
    {
      net.minecraft.server.v1_8_R3.Chunk chunk = (net.minecraft.server.v1_8_R3.Chunk)chunks[i];
      craftChunks[i] = chunk.bukkitChunk;
    }
    return craftChunks;
  }
  
  public void loadChunk(int x, int z)
  {
    loadChunk(x, z, true);
  }
  
  public boolean unloadChunk(org.bukkit.Chunk chunk)
  {
    return unloadChunk(chunk.getX(), chunk.getZ());
  }
  
  public boolean unloadChunk(int x, int z)
  {
    return unloadChunk(x, z, true);
  }
  
  public boolean unloadChunk(int x, int z, boolean save)
  {
    return unloadChunk(x, z, save, false);
  }
  
  public boolean unloadChunkRequest(int x, int z)
  {
    return unloadChunkRequest(x, z, true);
  }
  
  public boolean unloadChunkRequest(int x, int z, boolean safe)
  {
    AsyncCatcher.catchOp("chunk unload");
    if ((safe) && (isChunkInUse(x, z))) {
      return false;
    }
    this.world.chunkProviderServer.queueUnload(x, z);
    
    return true;
  }
  
  public boolean unloadChunk(int x, int z, boolean save, boolean safe)
  {
    AsyncCatcher.catchOp("chunk unload");
    if ((safe) && (isChunkInUse(x, z))) {
      return false;
    }
    net.minecraft.server.v1_8_R3.Chunk chunk = this.world.chunkProviderServer.getOrCreateChunk(x, z);
    if (chunk.mustSave) {
      save = true;
    }
    chunk.removeEntities();
    if ((save) && (!(chunk instanceof EmptyChunk)))
    {
      this.world.chunkProviderServer.saveChunk(chunk);
      this.world.chunkProviderServer.saveChunkNOP(chunk);
    }
    this.world.chunkProviderServer.unloadQueue.remove(x, z);
    this.world.chunkProviderServer.chunks.remove(LongHash.toLong(x, z));
    
    return true;
  }
  
  public boolean regenerateChunk(int x, int z)
  {
    unloadChunk(x, z, false, false);
    
    this.world.chunkProviderServer.unloadQueue.remove(x, z);
    
    net.minecraft.server.v1_8_R3.Chunk chunk = null;
    if (this.world.chunkProviderServer.chunkProvider == null) {
      chunk = this.world.chunkProviderServer.emptyChunk;
    } else {
      chunk = this.world.chunkProviderServer.chunkProvider.getOrCreateChunk(x, z);
    }
    chunkLoadPostProcess(chunk, x, z);
    
    refreshChunk(x, z);
    
    return chunk != null;
  }
  
  public boolean refreshChunk(int x, int z)
  {
    if (!isChunkLoaded(x, z)) {
      return false;
    }
    int px = x << 4;
    int pz = z << 4;
    
    int height = getMaxHeight() / 16;
    for (int idx = 0; idx < 64; idx++) {
      this.world.notify(new BlockPosition(px + idx / height, idx % height * 16, pz));
    }
    this.world.notify(new BlockPosition(px + 15, height * 16 - 1, pz + 15));
    
    return true;
  }
  
  public boolean isChunkInUse(int x, int z)
  {
    return this.world.getPlayerChunkMap().isChunkInUse(x, z);
  }
  
  public boolean loadChunk(int x, int z, boolean generate)
  {
    AsyncCatcher.catchOp("chunk load");
    this.chunkLoadCount += 1;
    if (generate) {
      return this.world.chunkProviderServer.getChunkAt(x, z) != null;
    }
    this.world.chunkProviderServer.unloadQueue.remove(x, z);
    net.minecraft.server.v1_8_R3.Chunk chunk = (net.minecraft.server.v1_8_R3.Chunk)this.world.chunkProviderServer.chunks.get(LongHash.toLong(x, z));
    if (chunk == null)
    {
      this.world.timings.syncChunkLoadTimer.startTiming();
      chunk = this.world.chunkProviderServer.loadChunk(x, z);
      
      chunkLoadPostProcess(chunk, x, z);
      this.world.timings.syncChunkLoadTimer.stopTiming();
    }
    return chunk != null;
  }
  
  private void chunkLoadPostProcess(net.minecraft.server.v1_8_R3.Chunk chunk, int cx, int cz)
  {
    if (chunk != null)
    {
      this.world.chunkProviderServer.chunks.put(LongHash.toLong(cx, cz), chunk);
      
      chunk.addEntities();
      for (int x = -2; x < 3; x++) {
        for (int z = -2; z < 3; z++) {
          if ((x != 0) || (z != 0))
          {
            net.minecraft.server.v1_8_R3.Chunk neighbor = this.world.chunkProviderServer.getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
            if (neighbor != null)
            {
              neighbor.setNeighborLoaded(-x, -z);
              chunk.setNeighborLoaded(x, z);
            }
          }
        }
      }
      chunk.loadNearby(this.world.chunkProviderServer, this.world.chunkProviderServer, cx, cz);
    }
  }
  
  public boolean isChunkLoaded(org.bukkit.Chunk chunk)
  {
    return isChunkLoaded(chunk.getX(), chunk.getZ());
  }
  
  public void loadChunk(org.bukkit.Chunk chunk)
  {
    loadChunk(chunk.getX(), chunk.getZ());
    ((CraftChunk)getChunkAt(chunk.getX(), chunk.getZ())).getHandle().bukkitChunk = chunk;
  }
  
  public WorldServer getHandle()
  {
    return this.world;
  }
  
  public Item dropItem(Location loc, ItemStack item)
  {
    Validate.notNull(item, "Cannot drop a Null item.");
    Validate.isTrue(item.getTypeId() != 0, "Cannot drop AIR.");
    EntityItem entity = new EntityItem(this.world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item));
    entity.pickupDelay = 10;
    this.world.addEntity(entity);
    
    return new CraftItem(this.world.getServer(), entity);
  }
  
  private static void randomLocationWithinBlock(Location loc, double xs, double ys, double zs)
  {
    double prevX = loc.getX();
    double prevY = loc.getY();
    double prevZ = loc.getZ();
    loc.add(xs, ys, zs);
    if (loc.getX() < Math.floor(prevX)) {
      loc.setX(Math.floor(prevX));
    }
    if (loc.getX() >= Math.ceil(prevX)) {
      loc.setX(Math.ceil(prevX - 0.01D));
    }
    if (loc.getY() < Math.floor(prevY)) {
      loc.setY(Math.floor(prevY));
    }
    if (loc.getY() >= Math.ceil(prevY)) {
      loc.setY(Math.ceil(prevY - 0.01D));
    }
    if (loc.getZ() < Math.floor(prevZ)) {
      loc.setZ(Math.floor(prevZ));
    }
    if (loc.getZ() >= Math.ceil(prevZ)) {
      loc.setZ(Math.ceil(prevZ - 0.01D));
    }
  }
  
  public Item dropItemNaturally(Location loc, ItemStack item)
  {
    double xs = this.world.random.nextFloat() * 0.7F - 0.35D;
    double ys = this.world.random.nextFloat() * 0.7F - 0.35D;
    double zs = this.world.random.nextFloat() * 0.7F - 0.35D;
    loc = loc.clone();
    
    randomLocationWithinBlock(loc, xs, ys, zs);
    return dropItem(loc, item);
  }
  
  public Arrow spawnArrow(Location loc, Vector velocity, float speed, float spread)
  {
    Validate.notNull(loc, "Can not spawn arrow with a null location");
    Validate.notNull(velocity, "Can not spawn arrow with a null velocity");
    
    EntityArrow arrow = new EntityArrow(this.world);
    arrow.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    arrow.shoot(velocity.getX(), velocity.getY(), velocity.getZ(), speed, spread);
    this.world.addEntity(arrow);
    return (Arrow)arrow.getBukkitEntity();
  }
  
  @Deprecated
  public LivingEntity spawnCreature(Location loc, CreatureType creatureType)
  {
    return spawnCreature(loc, creatureType.toEntityType());
  }
  
  @Deprecated
  public LivingEntity spawnCreature(Location loc, EntityType creatureType)
  {
    Validate.isTrue(creatureType.isAlive(), "EntityType not instance of LivingEntity");
    return (LivingEntity)spawnEntity(loc, creatureType);
  }
  
  public org.bukkit.entity.Entity spawnEntity(Location loc, EntityType entityType)
  {
    return spawn(loc, entityType.getEntityClass());
  }
  
  public LightningStrike strikeLightning(Location loc)
  {
    EntityLightning lightning = new EntityLightning(this.world, loc.getX(), loc.getY(), loc.getZ());
    this.world.strikeLightning(lightning);
    return new CraftLightningStrike(this.server, lightning);
  }
  
  public LightningStrike strikeLightningEffect(Location loc)
  {
    EntityLightning lightning = new EntityLightning(this.world, loc.getX(), loc.getY(), loc.getZ(), true);
    this.world.strikeLightning(lightning);
    return new CraftLightningStrike(this.server, lightning);
  }
  
  public boolean generateTree(Location loc, TreeType type)
  {
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    WorldGenerator gen;
    switch (type)
    {
    case BIG_TREE: 
      gen = new WorldGenBigTree(true);
      break;
    case COCOA_TREE: 
      gen = new WorldGenForest(true, false);
      break;
    case BIRCH: 
      gen = new WorldGenTaiga2(true);
      break;
    case BROWN_MUSHROOM: 
      gen = new WorldGenTaiga1();
      break;
    case DARK_OAK: 
      IBlockData iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
      IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
      gen = new WorldGenJungleTree(true, 10, 20, iblockdata1, iblockdata2);
      break;
    case JUNGLE: 
      IBlockData iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
      IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
      gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, false);
      break;
    case JUNGLE_BUSH: 
      IBlockData iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
      IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
      gen = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockdata1, iblockdata2, true);
      break;
    case MEGA_REDWOOD: 
      IBlockData iblockdata1 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
      IBlockData iblockdata2 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
      gen = new WorldGenGroundBush(iblockdata1, iblockdata2);
      break;
    case REDWOOD: 
      gen = new WorldGenHugeMushroom(Blocks.RED_MUSHROOM_BLOCK);
      break;
    case RED_MUSHROOM: 
      gen = new WorldGenHugeMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
      break;
    case SMALL_JUNGLE: 
      gen = new WorldGenSwampTree();
      break;
    case SWAMP: 
      gen = new WorldGenAcaciaTree(true);
      break;
    case TALL_BIRCH: 
      gen = new WorldGenForestTree(true);
      break;
    case TALL_REDWOOD: 
      gen = new WorldGenMegaTree(false, rand.nextBoolean());
      break;
    case TREE: 
      gen = new WorldGenForest(true, true);
      break;
    case ACACIA: 
    default: 
      gen = new WorldGenTrees(true);
    }
    return gen.generate(this.world, rand, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
  }
  
  public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate)
  {
    this.world.captureTreeGeneration = true;
    this.world.captureBlockStates = true;
    boolean grownTree = generateTree(loc, type);
    this.world.captureBlockStates = false;
    this.world.captureTreeGeneration = false;
    if (grownTree)
    {
      for (BlockState blockstate : this.world.capturedBlockStates)
      {
        int x = blockstate.getX();
        int y = blockstate.getY();
        int z = blockstate.getZ();
        BlockPosition position = new BlockPosition(x, y, z);
        net.minecraft.server.v1_8_R3.Block oldBlock = this.world.getType(position).getBlock();
        int typeId = blockstate.getTypeId();
        int data = blockstate.getRawData();
        int flag = ((CraftBlockState)blockstate).getFlag();
        delegate.setTypeIdAndData(x, y, z, typeId, data);
        net.minecraft.server.v1_8_R3.Block newBlock = this.world.getType(position).getBlock();
        this.world.notifyAndUpdatePhysics(position, null, oldBlock, newBlock, flag);
      }
      this.world.capturedBlockStates.clear();
      return true;
    }
    this.world.capturedBlockStates.clear();
    return false;
  }
  
  public TileEntity getTileEntityAt(int x, int y, int z)
  {
    return this.world.getTileEntity(new BlockPosition(x, y, z));
  }
  
  public String getName()
  {
    return this.world.worldData.getName();
  }
  
  @Deprecated
  public long getId()
  {
    return this.world.worldData.getSeed();
  }
  
  public UUID getUID()
  {
    return this.world.getDataManager().getUUID();
  }
  
  public String toString()
  {
    return "CraftWorld{name=" + getName() + '}';
  }
  
  public long getTime()
  {
    long time = getFullTime() % 24000L;
    if (time < 0L) {
      time += 24000L;
    }
    return time;
  }
  
  public void setTime(long time)
  {
    long margin = (time - getFullTime()) % 24000L;
    if (margin < 0L) {
      margin += 24000L;
    }
    setFullTime(getFullTime() + margin);
  }
  
  public long getFullTime()
  {
    return this.world.getDayTime();
  }
  
  public void setFullTime(long time)
  {
    this.world.setDayTime(time);
    for (Player p : getPlayers())
    {
      CraftPlayer cp = (CraftPlayer)p;
      if (cp.getHandle().playerConnection != null) {
        cp.getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateTime(cp.getHandle().world.getTime(), cp.getHandle().getPlayerTime(), cp.getHandle().world.getGameRules().getBoolean("doDaylightCycle")));
      }
    }
  }
  
  public boolean createExplosion(double x, double y, double z, float power)
  {
    return createExplosion(x, y, z, power, false, true);
  }
  
  public boolean createExplosion(double x, double y, double z, float power, boolean setFire)
  {
    return createExplosion(x, y, z, power, setFire, true);
  }
  
  public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks)
  {
    return !this.world.createExplosion(null, x, y, z, power, setFire, breakBlocks).wasCanceled;
  }
  
  public boolean createExplosion(Location loc, float power)
  {
    return createExplosion(loc, power, false);
  }
  
  public boolean createExplosion(Location loc, float power, boolean setFire)
  {
    return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
  }
  
  public World.Environment getEnvironment()
  {
    return this.environment;
  }
  
  public void setEnvironment(World.Environment env)
  {
    if (this.environment != env)
    {
      this.environment = env;
      this.world.worldProvider = WorldProvider.byDimension(this.environment.getId());
    }
  }
  
  public org.bukkit.block.Block getBlockAt(Location location)
  {
    return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }
  
  public int getBlockTypeIdAt(Location location)
  {
    return getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }
  
  public int getHighestBlockYAt(Location location)
  {
    return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
  }
  
  public org.bukkit.Chunk getChunkAt(Location location)
  {
    return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
  }
  
  public ChunkGenerator getGenerator()
  {
    return this.generator;
  }
  
  public List<BlockPopulator> getPopulators()
  {
    return this.populators;
  }
  
  public org.bukkit.block.Block getHighestBlockAt(int x, int z)
  {
    return getBlockAt(x, getHighestBlockYAt(x, z), z);
  }
  
  public org.bukkit.block.Block getHighestBlockAt(Location location)
  {
    return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
  }
  
  public Biome getBiome(int x, int z)
  {
    return CraftBlock.biomeBaseToBiome(this.world.getBiome(new BlockPosition(x, 0, z)));
  }
  
  public void setBiome(int x, int z, Biome bio)
  {
    BiomeBase bb = CraftBlock.biomeToBiomeBase(bio);
    if (this.world.isLoaded(new BlockPosition(x, 0, z)))
    {
      net.minecraft.server.v1_8_R3.Chunk chunk = this.world.getChunkAtWorldCoords(new BlockPosition(x, 0, z));
      if (chunk != null)
      {
        byte[] biomevals = chunk.getBiomeIndex();
        biomevals[((z & 0xF) << 4 | x & 0xF)] = ((byte)bb.id);
      }
    }
  }
  
  public double getTemperature(int x, int z)
  {
    return this.world.getBiome(new BlockPosition(x, 0, z)).temperature;
  }
  
  public double getHumidity(int x, int z)
  {
    return this.world.getBiome(new BlockPosition(x, 0, z)).humidity;
  }
  
  public List<org.bukkit.entity.Entity> getEntities()
  {
    List<org.bukkit.entity.Entity> list = new ArrayList();
    for (Object o : this.world.entityList) {
      if ((o instanceof net.minecraft.server.v1_8_R3.Entity))
      {
        net.minecraft.server.v1_8_R3.Entity mcEnt = (net.minecraft.server.v1_8_R3.Entity)o;
        org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();
        if (bukkitEntity != null) {
          list.add(bukkitEntity);
        }
      }
    }
    return list;
  }
  
  public List<LivingEntity> getLivingEntities()
  {
    List<LivingEntity> list = new ArrayList();
    for (Object o : this.world.entityList) {
      if ((o instanceof net.minecraft.server.v1_8_R3.Entity))
      {
        net.minecraft.server.v1_8_R3.Entity mcEnt = (net.minecraft.server.v1_8_R3.Entity)o;
        org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();
        if ((bukkitEntity != null) && ((bukkitEntity instanceof LivingEntity))) {
          list.add((LivingEntity)bukkitEntity);
        }
      }
    }
    return list;
  }
  
  @Deprecated
  public <T extends org.bukkit.entity.Entity> Collection<T> getEntitiesByClass(Class<T>... classes)
  {
    return getEntitiesByClasses(classes);
  }
  
  public <T extends org.bukkit.entity.Entity> Collection<T> getEntitiesByClass(Class<T> clazz)
  {
    Collection<T> list = new ArrayList();
    for (Object entity : this.world.entityList) {
      if ((entity instanceof net.minecraft.server.v1_8_R3.Entity))
      {
        org.bukkit.entity.Entity bukkitEntity = ((net.minecraft.server.v1_8_R3.Entity)entity).getBukkitEntity();
        if (bukkitEntity != null)
        {
          Class<?> bukkitClass = bukkitEntity.getClass();
          if (clazz.isAssignableFrom(bukkitClass)) {
            list.add(bukkitEntity);
          }
        }
      }
    }
    return list;
  }
  
  public Collection<org.bukkit.entity.Entity> getEntitiesByClasses(Class<?>... classes)
  {
    Collection<org.bukkit.entity.Entity> list = new ArrayList();
    for (Object entity : this.world.entityList) {
      if ((entity instanceof net.minecraft.server.v1_8_R3.Entity))
      {
        org.bukkit.entity.Entity bukkitEntity = ((net.minecraft.server.v1_8_R3.Entity)entity).getBukkitEntity();
        if (bukkitEntity != null)
        {
          Class<?> bukkitClass = bukkitEntity.getClass();
          Class[] arrayOfClass;
          int i = (arrayOfClass = classes).length;
          for (int j = 0; j < i; j++)
          {
            Class<?> clazz = arrayOfClass[j];
            if (clazz.isAssignableFrom(bukkitClass))
            {
              list.add(bukkitEntity);
              break;
            }
          }
        }
      }
    }
    return list;
  }
  
  public Collection<org.bukkit.entity.Entity> getNearbyEntities(Location location, double x, double y, double z)
  {
    if ((location == null) || (!location.getWorld().equals(this))) {
      return Collections.emptyList();
    }
    AxisAlignedBB bb = new AxisAlignedBB(location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z);
    List<net.minecraft.server.v1_8_R3.Entity> entityList = getHandle().getEntities(null, bb);
    List<org.bukkit.entity.Entity> bukkitEntityList = new ArrayList(entityList.size());
    for (Object entity : entityList) {
      bukkitEntityList.add(((net.minecraft.server.v1_8_R3.Entity)entity).getBukkitEntity());
    }
    return bukkitEntityList;
  }
  
  public List<Player> getPlayers()
  {
    List<Player> list = new ArrayList();
    for (Object o : this.world.entityList) {
      if ((o instanceof net.minecraft.server.v1_8_R3.Entity))
      {
        net.minecraft.server.v1_8_R3.Entity mcEnt = (net.minecraft.server.v1_8_R3.Entity)o;
        org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();
        if ((bukkitEntity != null) && ((bukkitEntity instanceof Player))) {
          list.add((Player)bukkitEntity);
        }
      }
    }
    return list;
  }
  
  public void save()
  {
    save(true);
  }
  
  public void save(boolean forceSave)
  {
    this.server.checkSaveState();
    try
    {
      boolean oldSave = this.world.savingDisabled;
      
      this.world.savingDisabled = false;
      this.world.save(forceSave, null);
      
      this.world.savingDisabled = oldSave;
    }
    catch (ExceptionWorldConflict ex)
    {
      ex.printStackTrace();
    }
  }
  
  public boolean isAutoSave()
  {
    return !this.world.savingDisabled;
  }
  
  public void setAutoSave(boolean value)
  {
    this.world.savingDisabled = (!value);
  }
  
  public void setDifficulty(Difficulty difficulty)
  {
    getHandle().worldData.setDifficulty(EnumDifficulty.getById(difficulty.getValue()));
  }
  
  public Difficulty getDifficulty()
  {
    return Difficulty.getByValue(getHandle().getDifficulty().ordinal());
  }
  
  public BlockMetadataStore getBlockMetadata()
  {
    return this.blockMetadata;
  }
  
  public boolean hasStorm()
  {
    return this.world.worldData.hasStorm();
  }
  
  public void setStorm(boolean hasStorm)
  {
    this.world.worldData.setStorm(hasStorm);
  }
  
  public int getWeatherDuration()
  {
    return this.world.worldData.getWeatherDuration();
  }
  
  public void setWeatherDuration(int duration)
  {
    this.world.worldData.setWeatherDuration(duration);
  }
  
  public boolean isThundering()
  {
    return this.world.worldData.isThundering();
  }
  
  public void setThundering(boolean thundering)
  {
    this.world.worldData.setThundering(thundering);
  }
  
  public int getThunderDuration()
  {
    return this.world.worldData.getThunderDuration();
  }
  
  public void setThunderDuration(int duration)
  {
    this.world.worldData.setThunderDuration(duration);
  }
  
  public long getSeed()
  {
    return this.world.worldData.getSeed();
  }
  
  public boolean getPVP()
  {
    return this.world.pvpMode;
  }
  
  public void setPVP(boolean pvp)
  {
    this.world.pvpMode = pvp;
  }
  
  public void playEffect(Player player, Effect effect, int data)
  {
    playEffect(player.getLocation(), effect, data, 0);
  }
  
  public void playEffect(Location location, Effect effect, int data)
  {
    playEffect(location, effect, data, 64);
  }
  
  public <T> void playEffect(Location loc, Effect effect, T data)
  {
    playEffect(loc, effect, data, 64);
  }
  
  public <T> void playEffect(Location loc, Effect effect, T data, int radius)
  {
    if (data != null) {
      Validate.isTrue(data.getClass().equals(effect.getData()), "Wrong kind of data for this effect!");
    } else {
      Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
    }
    if ((data != null) && (data.getClass().equals(MaterialData.class)))
    {
      MaterialData materialData = (MaterialData)data;
      Validate.isTrue(materialData.getItemType().isBlock(), "Material must be block");
      spigot().playEffect(loc, effect, materialData.getItemType().getId(), materialData.getData(), 0.0F, 0.0F, 0.0F, 1.0F, 1, radius);
    }
    else
    {
      int dataValue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
      playEffect(loc, effect, dataValue, radius);
    }
  }
  
  public void playEffect(Location location, Effect effect, int data, int radius)
  {
    spigot().playEffect(location, effect, data, 0, 0.0F, 0.0F, 0.0F, 1.0F, 1, radius);
  }
  
  public <T extends org.bukkit.entity.Entity> T spawn(Location location, Class<T> clazz)
    throws IllegalArgumentException
  {
    return spawn(location, clazz, CreatureSpawnEvent.SpawnReason.CUSTOM);
  }
  
  public FallingBlock spawnFallingBlock(Location location, org.bukkit.Material material, byte data)
    throws IllegalArgumentException
  {
    Validate.notNull(location, "Location cannot be null");
    Validate.notNull(material, "Material cannot be null");
    Validate.isTrue(material.isBlock(), "Material must be a block");
    
    double x = location.getBlockX() + 0.5D;
    double y = location.getBlockY() + 0.5D;
    double z = location.getBlockZ() + 0.5D;
    
    EntityFallingBlock entity = new EntityFallingBlock(this.world, x, y, z, net.minecraft.server.v1_8_R3.Block.getById(material.getId()).fromLegacyData(data));
    entity.ticksLived = 1;
    
    this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    return (FallingBlock)entity.getBukkitEntity();
  }
  
  public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData)
    throws IllegalArgumentException
  {
    return spawnFallingBlock(location, org.bukkit.Material.getMaterial(blockId), blockData);
  }
  
  public net.minecraft.server.v1_8_R3.Entity createEntity(Location location, Class<? extends org.bukkit.entity.Entity> clazz)
    throws IllegalArgumentException
  {
    if ((location == null) || (clazz == null)) {
      throw new IllegalArgumentException("Location or entity class cannot be null");
    }
    net.minecraft.server.v1_8_R3.Entity entity = null;
    
    double x = location.getX();
    double y = location.getY();
    double z = location.getZ();
    float pitch = location.getPitch();
    float yaw = location.getYaw();
    if (Boat.class.isAssignableFrom(clazz))
    {
      entity = new EntityBoat(this.world, x, y, z);
    }
    else if (FallingBlock.class.isAssignableFrom(clazz))
    {
      x = location.getBlockX();
      y = location.getBlockY();
      z = location.getBlockZ();
      IBlockData blockData = this.world.getType(new BlockPosition(x, y, z));
      int type = CraftMagicNumbers.getId(blockData.getBlock());
      int data = blockData.getBlock().toLegacyData(blockData);
      
      entity = new EntityFallingBlock(this.world, x + 0.5D, y + 0.5D, z + 0.5D, net.minecraft.server.v1_8_R3.Block.getById(type).fromLegacyData(data));
    }
    else if (Projectile.class.isAssignableFrom(clazz))
    {
      if (Snowball.class.isAssignableFrom(clazz))
      {
        entity = new EntitySnowball(this.world, x, y, z);
      }
      else if (Egg.class.isAssignableFrom(clazz))
      {
        entity = new EntityEgg(this.world, x, y, z);
      }
      else if (Arrow.class.isAssignableFrom(clazz))
      {
        entity = new EntityArrow(this.world);
        entity.setPositionRotation(x, y, z, 0.0F, 0.0F);
      }
      else if (ThrownExpBottle.class.isAssignableFrom(clazz))
      {
        entity = new EntityThrownExpBottle(this.world);
        entity.setPositionRotation(x, y, z, 0.0F, 0.0F);
      }
      else if (EnderPearl.class.isAssignableFrom(clazz))
      {
        entity = new EntityEnderPearl(this.world, null);
        entity.setPositionRotation(x, y, z, 0.0F, 0.0F);
      }
      else if (ThrownPotion.class.isAssignableFrom(clazz))
      {
        entity = new EntityPotion(this.world, x, y, z, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.POTION, 1)));
      }
      else if (Fireball.class.isAssignableFrom(clazz))
      {
        if (SmallFireball.class.isAssignableFrom(clazz)) {
          entity = new EntitySmallFireball(this.world);
        } else if (WitherSkull.class.isAssignableFrom(clazz)) {
          entity = new EntityWitherSkull(this.world);
        } else {
          entity = new EntityLargeFireball(this.world);
        }
        entity.setPositionRotation(x, y, z, yaw, pitch);
        Vector direction = location.getDirection().multiply(10);
        ((EntityFireball)entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
      }
    }
    else if (Minecart.class.isAssignableFrom(clazz))
    {
      if (PoweredMinecart.class.isAssignableFrom(clazz)) {
        entity = new EntityMinecartFurnace(this.world, x, y, z);
      } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
        entity = new EntityMinecartChest(this.world, x, y, z);
      } else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
        entity = new EntityMinecartTNT(this.world, x, y, z);
      } else if (HopperMinecart.class.isAssignableFrom(clazz)) {
        entity = new EntityMinecartHopper(this.world, x, y, z);
      } else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
        entity = new EntityMinecartMobSpawner(this.world, x, y, z);
      } else {
        entity = new EntityMinecartRideable(this.world, x, y, z);
      }
    }
    else if (EnderSignal.class.isAssignableFrom(clazz))
    {
      entity = new EntityEnderSignal(this.world, x, y, z);
    }
    else if (EnderCrystal.class.isAssignableFrom(clazz))
    {
      entity = new EntityEnderCrystal(this.world);
      entity.setPositionRotation(x, y, z, 0.0F, 0.0F);
    }
    else if (LivingEntity.class.isAssignableFrom(clazz))
    {
      if (Chicken.class.isAssignableFrom(clazz)) {
        entity = new EntityChicken(this.world);
      } else if (Cow.class.isAssignableFrom(clazz))
      {
        if (MushroomCow.class.isAssignableFrom(clazz)) {
          entity = new EntityMushroomCow(this.world);
        } else {
          entity = new EntityCow(this.world);
        }
      }
      else if (Golem.class.isAssignableFrom(clazz))
      {
        if (Snowman.class.isAssignableFrom(clazz)) {
          entity = new EntitySnowman(this.world);
        } else if (IronGolem.class.isAssignableFrom(clazz)) {
          entity = new EntityIronGolem(this.world);
        }
      }
      else if (Creeper.class.isAssignableFrom(clazz)) {
        entity = new EntityCreeper(this.world);
      } else if (Ghast.class.isAssignableFrom(clazz)) {
        entity = new EntityGhast(this.world);
      } else if (Pig.class.isAssignableFrom(clazz)) {
        entity = new EntityPig(this.world);
      } else if (!Player.class.isAssignableFrom(clazz)) {
        if (Sheep.class.isAssignableFrom(clazz)) {
          entity = new EntitySheep(this.world);
        } else if (Horse.class.isAssignableFrom(clazz)) {
          entity = new EntityHorse(this.world);
        } else if (Skeleton.class.isAssignableFrom(clazz)) {
          entity = new EntitySkeleton(this.world);
        } else if (Slime.class.isAssignableFrom(clazz))
        {
          if (MagmaCube.class.isAssignableFrom(clazz)) {
            entity = new EntityMagmaCube(this.world);
          } else {
            entity = new EntitySlime(this.world);
          }
        }
        else if (Spider.class.isAssignableFrom(clazz))
        {
          if (CaveSpider.class.isAssignableFrom(clazz)) {
            entity = new EntityCaveSpider(this.world);
          } else {
            entity = new EntitySpider(this.world);
          }
        }
        else if (Squid.class.isAssignableFrom(clazz)) {
          entity = new EntitySquid(this.world);
        } else if (Tameable.class.isAssignableFrom(clazz))
        {
          if (Wolf.class.isAssignableFrom(clazz)) {
            entity = new EntityWolf(this.world);
          } else if (Ocelot.class.isAssignableFrom(clazz)) {
            entity = new EntityOcelot(this.world);
          }
        }
        else if (PigZombie.class.isAssignableFrom(clazz)) {
          entity = new EntityPigZombie(this.world);
        } else if (Zombie.class.isAssignableFrom(clazz)) {
          entity = new EntityZombie(this.world);
        } else if (Giant.class.isAssignableFrom(clazz)) {
          entity = new EntityGiantZombie(this.world);
        } else if (Silverfish.class.isAssignableFrom(clazz)) {
          entity = new EntitySilverfish(this.world);
        } else if (Enderman.class.isAssignableFrom(clazz)) {
          entity = new EntityEnderman(this.world);
        } else if (Blaze.class.isAssignableFrom(clazz)) {
          entity = new EntityBlaze(this.world);
        } else if (Villager.class.isAssignableFrom(clazz)) {
          entity = new EntityVillager(this.world);
        } else if (Witch.class.isAssignableFrom(clazz)) {
          entity = new EntityWitch(this.world);
        } else if (Wither.class.isAssignableFrom(clazz)) {
          entity = new EntityWither(this.world);
        } else if (ComplexLivingEntity.class.isAssignableFrom(clazz))
        {
          if (EnderDragon.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderDragon(this.world);
          }
        }
        else if (Ambient.class.isAssignableFrom(clazz))
        {
          if (Bat.class.isAssignableFrom(clazz)) {
            entity = new EntityBat(this.world);
          }
        }
        else if (Rabbit.class.isAssignableFrom(clazz)) {
          entity = new EntityRabbit(this.world);
        } else if (Endermite.class.isAssignableFrom(clazz)) {
          entity = new EntityEndermite(this.world);
        } else if (Guardian.class.isAssignableFrom(clazz)) {
          entity = new EntityGuardian(this.world);
        } else if (ArmorStand.class.isAssignableFrom(clazz)) {
          entity = new EntityArmorStand(this.world, x, y, z);
        }
      }
      if (entity != null) {
        entity.setLocation(x, y, z, yaw, pitch);
      }
    }
    else if (Hanging.class.isAssignableFrom(clazz))
    {
      org.bukkit.block.Block block = getBlockAt(location);
      BlockFace face = BlockFace.SELF;
      
      int width = 16;
      int height = 16;
      if (ItemFrame.class.isAssignableFrom(clazz))
      {
        width = 12;
        height = 12;
      }
      else if (LeashHitch.class.isAssignableFrom(clazz))
      {
        width = 9;
        height = 9;
      }
      BlockFace[] faces = { BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH };
      BlockPosition pos = new BlockPosition((int)x, (int)y, (int)z);
      BlockFace[] arrayOfBlockFace1;
      int i = (arrayOfBlockFace1 = faces).length;
      for (int j = 0; j < i; j++)
      {
        BlockFace dir = arrayOfBlockFace1[j];
        net.minecraft.server.v1_8_R3.Block nmsBlock = CraftMagicNumbers.getBlock(block.getRelative(dir));
        if ((nmsBlock.getMaterial().isBuildable()) || (BlockDiodeAbstract.d(nmsBlock)))
        {
          boolean taken = false;
          AxisAlignedBB bb = EntityHanging.calculateBoundingBox(pos, CraftBlock.blockFaceToNotch(dir).opposite(), width, height);
          List<net.minecraft.server.v1_8_R3.Entity> list = this.world.getEntities(null, bb);
          for (Iterator<net.minecraft.server.v1_8_R3.Entity> it = list.iterator(); (!taken) && (it.hasNext());)
          {
            net.minecraft.server.v1_8_R3.Entity e = (net.minecraft.server.v1_8_R3.Entity)it.next();
            if ((e instanceof EntityHanging)) {
              taken = true;
            }
          }
          if (!taken)
          {
            face = dir;
            break;
          }
        }
      }
      EnumDirection dir = CraftBlock.blockFaceToNotch(face).opposite();
      if (Painting.class.isAssignableFrom(clazz))
      {
        entity = new EntityPainting(this.world, new BlockPosition((int)x, (int)y, (int)z), dir);
      }
      else if (ItemFrame.class.isAssignableFrom(clazz))
      {
        entity = new EntityItemFrame(this.world, new BlockPosition((int)x, (int)y, (int)z), dir);
      }
      else if (LeashHitch.class.isAssignableFrom(clazz))
      {
        entity = new EntityLeash(this.world, new BlockPosition((int)x, (int)y, (int)z));
        entity.attachedToPlayer = true;
      }
      if ((entity != null) && (!((EntityHanging)entity).survives())) {
        throw new IllegalArgumentException("Cannot spawn hanging entity for " + clazz.getName() + " at " + location);
      }
    }
    else if (TNTPrimed.class.isAssignableFrom(clazz))
    {
      entity = new EntityTNTPrimed(this.world, x, y, z, null);
    }
    else if (ExperienceOrb.class.isAssignableFrom(clazz))
    {
      entity = new EntityExperienceOrb(this.world, x, y, z, 0);
    }
    else if (Weather.class.isAssignableFrom(clazz))
    {
      if (LightningStrike.class.isAssignableFrom(clazz)) {
        entity = new EntityLightning(this.world, x, y, z);
      }
    }
    else if (Firework.class.isAssignableFrom(clazz))
    {
      entity = new EntityFireworks(this.world, x, y, z, null);
    }
    if (entity != null)
    {
      if ((entity instanceof EntityOcelot)) {
        ((EntityOcelot)entity).spawnBonus = false;
      }
      return entity;
    }
    throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
  }
  
  public <T extends org.bukkit.entity.Entity> T addEntity(net.minecraft.server.v1_8_R3.Entity entity, CreatureSpawnEvent.SpawnReason reason)
    throws IllegalArgumentException
  {
    Preconditions.checkArgument(entity != null, "Cannot spawn null entity");
    if ((entity instanceof EntityInsentient)) {
      ((EntityInsentient)entity).prepare(getHandle().E(new BlockPosition(entity)), null);
    }
    this.world.addEntity(entity, reason);
    return entity.getBukkitEntity();
  }
  
  public <T extends org.bukkit.entity.Entity> T spawn(Location location, Class<T> clazz, CreatureSpawnEvent.SpawnReason reason)
    throws IllegalArgumentException
  {
    net.minecraft.server.v1_8_R3.Entity entity = createEntity(location, clazz);
    
    return addEntity(entity, reason);
  }
  
  public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain)
  {
    return CraftChunk.getEmptyChunkSnapshot(x, z, this, includeBiome, includeBiomeTempRain);
  }
  
  public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals)
  {
    this.world.setSpawnFlags(allowMonsters, allowAnimals);
  }
  
  public boolean getAllowAnimals()
  {
    return this.world.allowAnimals;
  }
  
  public boolean getAllowMonsters()
  {
    return this.world.allowMonsters;
  }
  
  public int getMaxHeight()
  {
    return this.world.getHeight();
  }
  
  public int getSeaLevel()
  {
    return 64;
  }
  
  public boolean getKeepSpawnInMemory()
  {
    return this.world.keepSpawnInMemory;
  }
  
  public void setKeepSpawnInMemory(boolean keepLoaded)
  {
    this.world.keepSpawnInMemory = keepLoaded;
    
    BlockPosition chunkcoordinates = this.world.getSpawn();
    int chunkCoordX = chunkcoordinates.getX() >> 4;
    int chunkCoordZ = chunkcoordinates.getZ() >> 4;
    for (int x = -12; x <= 12; x++) {
      for (int z = -12; z <= 12; z++) {
        if (keepLoaded) {
          loadChunk(chunkCoordX + x, chunkCoordZ + z);
        } else if (isChunkLoaded(chunkCoordX + x, chunkCoordZ + z)) {
          if ((getHandle().getChunkAt(chunkCoordX + x, chunkCoordZ + z) instanceof EmptyChunk)) {
            unloadChunk(chunkCoordX + x, chunkCoordZ + z, false);
          } else {
            unloadChunk(chunkCoordX + x, chunkCoordZ + z);
          }
        }
      }
    }
  }
  
  public int hashCode()
  {
    return getUID().hashCode();
  }
  
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CraftWorld other = (CraftWorld)obj;
    
    return getUID() == other.getUID();
  }
  
  public File getWorldFolder()
  {
    return ((WorldNBTStorage)this.world.getDataManager()).getDirectory();
  }
  
  public void sendPluginMessage(Plugin source, String channel, byte[] message)
  {
    StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
    for (Player player : getPlayers()) {
      player.sendPluginMessage(source, channel, message);
    }
  }
  
  public Set<String> getListeningPluginChannels()
  {
    Set<String> result = new HashSet();
    for (Player player : getPlayers()) {
      result.addAll(player.getListeningPluginChannels());
    }
    return result;
  }
  
  public org.bukkit.WorldType getWorldType()
  {
    return org.bukkit.WorldType.getByName(this.world.getWorldData().getType().name());
  }
  
  public boolean canGenerateStructures()
  {
    return this.world.getWorldData().shouldGenerateMapFeatures();
  }
  
  public long getTicksPerAnimalSpawns()
  {
    return this.world.ticksPerAnimalSpawns;
  }
  
  public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns)
  {
    this.world.ticksPerAnimalSpawns = ticksPerAnimalSpawns;
  }
  
  public long getTicksPerMonsterSpawns()
  {
    return this.world.ticksPerMonsterSpawns;
  }
  
  public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns)
  {
    this.world.ticksPerMonsterSpawns = ticksPerMonsterSpawns;
  }
  
  public void setMetadata(String metadataKey, MetadataValue newMetadataValue)
  {
    this.server.getWorldMetadata().setMetadata(this, metadataKey, newMetadataValue);
  }
  
  public List<MetadataValue> getMetadata(String metadataKey)
  {
    return this.server.getWorldMetadata().getMetadata(this, metadataKey);
  }
  
  public boolean hasMetadata(String metadataKey)
  {
    return this.server.getWorldMetadata().hasMetadata(this, metadataKey);
  }
  
  public void removeMetadata(String metadataKey, Plugin owningPlugin)
  {
    this.server.getWorldMetadata().removeMetadata(this, metadataKey, owningPlugin);
  }
  
  public int getMonsterSpawnLimit()
  {
    if (this.monsterSpawn < 0) {
      return this.server.getMonsterSpawnLimit();
    }
    return this.monsterSpawn;
  }
  
  public void setMonsterSpawnLimit(int limit)
  {
    this.monsterSpawn = limit;
  }
  
  public int getAnimalSpawnLimit()
  {
    if (this.animalSpawn < 0) {
      return this.server.getAnimalSpawnLimit();
    }
    return this.animalSpawn;
  }
  
  public void setAnimalSpawnLimit(int limit)
  {
    this.animalSpawn = limit;
  }
  
  public int getWaterAnimalSpawnLimit()
  {
    if (this.waterAnimalSpawn < 0) {
      return this.server.getWaterAnimalSpawnLimit();
    }
    return this.waterAnimalSpawn;
  }
  
  public void setWaterAnimalSpawnLimit(int limit)
  {
    this.waterAnimalSpawn = limit;
  }
  
  public int getAmbientSpawnLimit()
  {
    if (this.ambientSpawn < 0) {
      return this.server.getAmbientSpawnLimit();
    }
    return this.ambientSpawn;
  }
  
  public void setAmbientSpawnLimit(int limit)
  {
    this.ambientSpawn = limit;
  }
  
  public void playSound(Location loc, Sound sound, float volume, float pitch)
  {
    if ((loc == null) || (sound == null)) {
      return;
    }
    double x = loc.getX();
    double y = loc.getY();
    double z = loc.getZ();
    
    getHandle().makeSound(x, y, z, CraftSound.getSound(sound), volume, pitch);
  }
  
  public String getGameRuleValue(String rule)
  {
    return getHandle().getGameRules().get(rule);
  }
  
  public boolean setGameRuleValue(String rule, String value)
  {
    if ((rule == null) || (value == null)) {
      return false;
    }
    if (!isGameRule(rule)) {
      return false;
    }
    getHandle().getGameRules().set(rule, value);
    return true;
  }
  
  public String[] getGameRules()
  {
    return getHandle().getGameRules().getGameRules();
  }
  
  public boolean isGameRule(String rule)
  {
    return getHandle().getGameRules().contains(rule);
  }
  
  public WorldBorder getWorldBorder()
  {
    if (this.worldBorder == null) {
      this.worldBorder = new CraftWorldBorder(this);
    }
    return this.worldBorder;
  }
  
  public void processChunkGC()
  {
    this.chunkGCTickCount += 1;
    if ((this.chunkLoadCount >= this.server.chunkGCLoadThresh) && (this.server.chunkGCLoadThresh > 0)) {
      this.chunkLoadCount = 0;
    } else if ((this.chunkGCTickCount >= this.server.chunkGCPeriod) && (this.server.chunkGCPeriod > 0)) {
      this.chunkGCTickCount = 0;
    } else {
      return;
    }
    ChunkProviderServer cps = this.world.chunkProviderServer;
    for (net.minecraft.server.v1_8_R3.Chunk chunk : cps.chunks.values()) {
      if (!isChunkInUse(chunk.locX, chunk.locZ)) {
        if (!cps.unloadQueue.contains(chunk.locX, chunk.locZ)) {
          cps.queueUnload(chunk.locX, chunk.locZ);
        }
      }
    }
  }
  
  private final World.Spigot spigot = new World.Spigot()
  {
    public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius)
    {
      Validate.notNull(location, "Location cannot be null");
      Validate.notNull(effect, "Effect cannot be null");
      Validate.notNull(location.getWorld(), "World cannot be null");
      Packet packet;
      EnumParticle p;
      Packet packet;
      if (effect.getType() != Effect.Type.PARTICLE)
      {
        int packetData = effect.getId();
        packet = new PacketPlayOutWorldEvent(packetData, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), id, false);
      }
      else
      {
        EnumParticle particle = null;
        int[] extra = null;
        EnumParticle[] arrayOfEnumParticle;
        int i = (arrayOfEnumParticle = EnumParticle.values()).length;
        for (int j = 0; j < i; j++)
        {
          p = arrayOfEnumParticle[j];
          if (effect.getName().startsWith(p.b().replace("_", "")))
          {
            particle = p;
            if (effect.getData() == null) {
              break;
            }
            if (effect.getData().equals(org.bukkit.Material.class))
            {
              extra = new int[] { id };
              break;
            }
            extra = new int[] { data << 12 | id & 0xFFF };
            
            break;
          }
        }
        if (extra == null) {
          extra = new int[0];
        }
        packet = new PacketPlayOutWorldParticles(particle, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offsetX, offsetY, offsetZ, speed, particleCount, extra);
      }
      radius *= radius;
      for (Player player : CraftWorld.this.getPlayers()) {
        if (((CraftPlayer)player).getHandle().playerConnection != null) {
          if (location.getWorld().equals(player.getWorld()))
          {
            int distance = (int)player.getLocation().distanceSquared(location);
            if (distance <= radius) {
              ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
            }
          }
        }
      }
    }
    
    public void playEffect(Location location, Effect effect)
    {
      CraftWorld.this.playEffect(location, effect, 0);
    }
    
    public LightningStrike strikeLightning(Location loc, boolean isSilent)
    {
      EntityLightning lightning = new EntityLightning(CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), false, isSilent);
      CraftWorld.this.world.strikeLightning(lightning);
      return new CraftLightningStrike(CraftWorld.this.server, lightning);
    }
    
    public LightningStrike strikeLightningEffect(Location loc, boolean isSilent)
    {
      EntityLightning lightning = new EntityLightning(CraftWorld.this.world, loc.getX(), loc.getY(), loc.getZ(), true, isSilent);
      CraftWorld.this.world.strikeLightning(lightning);
      return new CraftLightningStrike(CraftWorld.this.server, lightning);
    }
  };
  
  public World.Spigot spigot()
  {
    return this.spigot;
  }
}
