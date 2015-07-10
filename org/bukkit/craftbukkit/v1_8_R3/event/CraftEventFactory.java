package org.bukkit.craftbukkit.v1_8_R3.event;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.Achievement;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.ChatModifier;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.DedicatedPlayerList;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityDamageSource;
import net.minecraft.server.v1_8_R3.EntityDamageSourceIndirect;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.Explosion;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.OpList;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import net.minecraft.server.v1_8_R3.Slot;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Statistic.Type;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftDamageSource;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.CreeperPowerEvent.PowerCause;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginManager;

public class CraftEventFactory
{
  public static final DamageSource MELTING = CraftDamageSource.copyOf(DamageSource.BURN);
  public static final DamageSource POISON = CraftDamageSource.copyOf(DamageSource.MAGIC);
  public static org.bukkit.block.Block blockDamage;
  public static net.minecraft.server.v1_8_R3.Entity entityDamage;
  
  private static boolean canBuild(CraftWorld world, Player player, int x, int z)
  {
    WorldServer worldServer = world.getHandle();
    int spawnSize = Bukkit.getServer().getSpawnRadius();
    if (world.getHandle().dimension != 0) {
      return true;
    }
    if (spawnSize <= 0) {
      return true;
    }
    if (((CraftServer)Bukkit.getServer()).getHandle().getOPs().isEmpty()) {
      return true;
    }
    if (player.isOp()) {
      return true;
    }
    BlockPosition chunkcoordinates = worldServer.getSpawn();
    
    int distanceFromSpawn = Math.max(Math.abs(x - chunkcoordinates.getX()), Math.abs(z - chunkcoordinates.getY()));
    return distanceFromSpawn > spawnSize;
  }
  
  public static <T extends Event> T callEvent(T event)
  {
    Bukkit.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static BlockMultiPlaceEvent callBlockMultiPlaceEvent(net.minecraft.server.v1_8_R3.World world, EntityHuman who, List<BlockState> blockStates, int clickedX, int clickedY, int clickedZ)
  {
    CraftWorld craftWorld = world.getWorld();
    CraftServer craftServer = world.getServer();
    Player player = who == null ? null : (Player)who.getBukkitEntity();
    
    org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
    
    boolean canBuild = true;
    for (int i = 0; i < blockStates.size(); i++) {
      if (!canBuild(craftWorld, player, ((BlockState)blockStates.get(i)).getX(), ((BlockState)blockStates.get(i)).getZ()))
      {
        canBuild = false;
        break;
      }
    }
    BlockMultiPlaceEvent event = new BlockMultiPlaceEvent(blockStates, blockClicked, player.getItemInHand(), player, canBuild);
    craftServer.getPluginManager().callEvent(event);
    
    return event;
  }
  
  public static BlockPlaceEvent callBlockPlaceEvent(net.minecraft.server.v1_8_R3.World world, EntityHuman who, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ)
  {
    CraftWorld craftWorld = world.getWorld();
    CraftServer craftServer = world.getServer();
    
    Player player = who == null ? null : (Player)who.getBukkitEntity();
    
    org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
    org.bukkit.block.Block placedBlock = replacedBlockState.getBlock();
    
    boolean canBuild = canBuild(craftWorld, player, placedBlock.getX(), placedBlock.getZ());
    
    BlockPlaceEvent event = new BlockPlaceEvent(placedBlock, replacedBlockState, blockClicked, player.getItemInHand(), player, canBuild);
    craftServer.getPluginManager().callEvent(event);
    
    return event;
  }
  
  public static SpawnerSpawnEvent callSpawnerSpawnEvent(net.minecraft.server.v1_8_R3.Entity spawnee, int spawnerX, int spawnerY, int spawnerZ)
  {
    CraftEntity entity = spawnee.getBukkitEntity();
    BlockState state = entity.getWorld().getBlockAt(spawnerX, spawnerY, spawnerZ).getState();
    if (!(state instanceof CreatureSpawner)) {
      state = null;
    }
    SpawnerSpawnEvent event = new SpawnerSpawnEvent(entity, (CreatureSpawner)state);
    entity.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(EntityHuman who, int clickedX, int clickedY, int clickedZ, EnumDirection clickedFace, net.minecraft.server.v1_8_R3.ItemStack itemInHand)
  {
    return (PlayerBucketEmptyEvent)getPlayerBucketEvent(false, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, Items.BUCKET);
  }
  
  public static PlayerBucketFillEvent callPlayerBucketFillEvent(EntityHuman who, int clickedX, int clickedY, int clickedZ, EnumDirection clickedFace, net.minecraft.server.v1_8_R3.ItemStack itemInHand, net.minecraft.server.v1_8_R3.Item bucket)
  {
    return (PlayerBucketFillEvent)getPlayerBucketEvent(true, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, bucket);
  }
  
  private static PlayerEvent getPlayerBucketEvent(boolean isFilling, EntityHuman who, int clickedX, int clickedY, int clickedZ, EnumDirection clickedFace, net.minecraft.server.v1_8_R3.ItemStack itemstack, net.minecraft.server.v1_8_R3.Item item)
  {
    Player player = who == null ? null : (Player)who.getBukkitEntity();
    CraftItemStack itemInHand = CraftItemStack.asNewCraftStack(item);
    Material bucket = CraftMagicNumbers.getMaterial(itemstack.getItem());
    
    CraftWorld craftWorld = (CraftWorld)player.getWorld();
    CraftServer craftServer = (CraftServer)player.getServer();
    
    org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
    BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);
    
    PlayerEvent event = null;
    if (isFilling)
    {
      event = new PlayerBucketFillEvent(player, blockClicked, blockFace, bucket, itemInHand);
      ((PlayerBucketFillEvent)event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
    }
    else
    {
      event = new PlayerBucketEmptyEvent(player, blockClicked, blockFace, bucket, itemInHand);
      ((PlayerBucketEmptyEvent)event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
    }
    craftServer.getPluginManager().callEvent(event);
    
    return event;
  }
  
  public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, net.minecraft.server.v1_8_R3.ItemStack itemstack)
  {
    if ((action != Action.LEFT_CLICK_AIR) && (action != Action.RIGHT_CLICK_AIR)) {
      throw new IllegalArgumentException(String.format("%s performing %s with %s", new Object[] { who, action, itemstack }));
    }
    return callPlayerInteractEvent(who, action, new BlockPosition(0, 256, 0), EnumDirection.SOUTH, itemstack);
  }
  
  public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, BlockPosition position, EnumDirection direction, net.minecraft.server.v1_8_R3.ItemStack itemstack)
  {
    return callPlayerInteractEvent(who, action, position, direction, itemstack, false);
  }
  
  public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, BlockPosition position, EnumDirection direction, net.minecraft.server.v1_8_R3.ItemStack itemstack, boolean cancelledBlock)
  {
    Player player = who == null ? null : (Player)who.getBukkitEntity();
    CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
    
    CraftWorld craftWorld = (CraftWorld)player.getWorld();
    CraftServer craftServer = (CraftServer)player.getServer();
    
    org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(position.getX(), position.getY(), position.getZ());
    BlockFace blockFace = CraftBlock.notchToBlockFace(direction);
    if (position.getY() > 255)
    {
      blockClicked = null;
      switch (action)
      {
      case LEFT_CLICK_AIR: 
        action = Action.LEFT_CLICK_AIR;
        break;
      case LEFT_CLICK_BLOCK: 
        action = Action.RIGHT_CLICK_AIR;
      }
    }
    if ((itemInHand.getType() == Material.AIR) || (itemInHand.getAmount() == 0)) {
      itemInHand = null;
    }
    PlayerInteractEvent event = new PlayerInteractEvent(player, action, itemInHand, blockClicked, blockFace);
    if (cancelledBlock) {
      event.setUseInteractedBlock(Event.Result.DENY);
    }
    craftServer.getPluginManager().callEvent(event);
    
    return event;
  }
  
  public static EntityShootBowEvent callEntityShootBowEvent(EntityLiving who, net.minecraft.server.v1_8_R3.ItemStack itemstack, EntityArrow entityArrow, float force)
  {
    LivingEntity shooter = (LivingEntity)who.getBukkitEntity();
    CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
    Arrow arrow = (Arrow)entityArrow.getBukkitEntity();
    if ((itemInHand != null) && ((itemInHand.getType() == Material.AIR) || (itemInHand.getAmount() == 0))) {
      itemInHand = null;
    }
    EntityShootBowEvent event = new EntityShootBowEvent(shooter, itemInHand, arrow, force);
    Bukkit.getPluginManager().callEvent(event);
    
    return event;
  }
  
  public static BlockDamageEvent callBlockDamageEvent(EntityHuman who, int x, int y, int z, net.minecraft.server.v1_8_R3.ItemStack itemstack, boolean instaBreak)
  {
    Player player = who == null ? null : (Player)who.getBukkitEntity();
    CraftItemStack itemInHand = CraftItemStack.asCraftMirror(itemstack);
    
    CraftWorld craftWorld = (CraftWorld)player.getWorld();
    CraftServer craftServer = (CraftServer)player.getServer();
    
    org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(x, y, z);
    
    BlockDamageEvent event = new BlockDamageEvent(player, blockClicked, itemInHand, instaBreak);
    craftServer.getPluginManager().callEvent(event);
    
    return event;
  }
  
  public static CreatureSpawnEvent callCreatureSpawnEvent(EntityLiving entityliving, CreatureSpawnEvent.SpawnReason spawnReason)
  {
    LivingEntity entity = (LivingEntity)entityliving.getBukkitEntity();
    CraftServer craftServer = (CraftServer)entity.getServer();
    
    CreatureSpawnEvent event = new CreatureSpawnEvent(entity, spawnReason);
    craftServer.getPluginManager().callEvent(event);
    return event;
  }
  
  public static EntityTameEvent callEntityTameEvent(EntityInsentient entity, EntityHuman tamer)
  {
    org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
    AnimalTamer bukkitTamer = tamer != null ? tamer.getBukkitEntity() : null;
    CraftServer craftServer = (CraftServer)bukkitEntity.getServer();
    
    entity.persistent = true;
    
    EntityTameEvent event = new EntityTameEvent((LivingEntity)bukkitEntity, bukkitTamer);
    craftServer.getPluginManager().callEvent(event);
    return event;
  }
  
  public static ItemSpawnEvent callItemSpawnEvent(EntityItem entityitem)
  {
    org.bukkit.entity.Item entity = (org.bukkit.entity.Item)entityitem.getBukkitEntity();
    CraftServer craftServer = (CraftServer)entity.getServer();
    
    ItemSpawnEvent event = new ItemSpawnEvent(entity, entity.getLocation());
    
    craftServer.getPluginManager().callEvent(event);
    return event;
  }
  
  public static ItemDespawnEvent callItemDespawnEvent(EntityItem entityitem)
  {
    org.bukkit.entity.Item entity = (org.bukkit.entity.Item)entityitem.getBukkitEntity();
    
    ItemDespawnEvent event = new ItemDespawnEvent(entity, entity.getLocation());
    
    entity.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static PotionSplashEvent callPotionSplashEvent(EntityPotion potion, Map<LivingEntity, Double> affectedEntities)
  {
    ThrownPotion thrownPotion = (ThrownPotion)potion.getBukkitEntity();
    
    PotionSplashEvent event = new PotionSplashEvent(thrownPotion, affectedEntities);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
  
  public static BlockFadeEvent callBlockFadeEvent(org.bukkit.block.Block block, net.minecraft.server.v1_8_R3.Block type)
  {
    BlockState state = block.getState();
    state.setTypeId(net.minecraft.server.v1_8_R3.Block.getId(type));
    
    BlockFadeEvent event = new BlockFadeEvent(block, state);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
  
  public static void handleBlockSpreadEvent(org.bukkit.block.Block block, org.bukkit.block.Block source, net.minecraft.server.v1_8_R3.Block type, int data)
  {
    BlockState state = block.getState();
    state.setTypeId(net.minecraft.server.v1_8_R3.Block.getId(type));
    state.setRawData((byte)data);
    
    BlockSpreadEvent event = new BlockSpreadEvent(block, source, state);
    Bukkit.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      state.update(true);
    }
  }
  
  public static EntityDeathEvent callEntityDeathEvent(EntityLiving victim)
  {
    return callEntityDeathEvent(victim, new ArrayList(0));
  }
  
  public static EntityDeathEvent callEntityDeathEvent(EntityLiving victim, List<org.bukkit.inventory.ItemStack> drops)
  {
    CraftLivingEntity entity = (CraftLivingEntity)victim.getBukkitEntity();
    EntityDeathEvent event = new EntityDeathEvent(entity, drops, victim.getExpReward());
    CraftWorld world = (CraftWorld)entity.getWorld();
    Bukkit.getServer().getPluginManager().callEvent(event);
    
    victim.expToDrop = event.getDroppedExp();
    for (org.bukkit.inventory.ItemStack stack : event.getDrops()) {
      if ((stack != null) && (stack.getType() != Material.AIR) && (stack.getAmount() != 0)) {
        world.dropItemNaturally(entity.getLocation(), stack);
      }
    }
    return event;
  }
  
  public static PlayerDeathEvent callPlayerDeathEvent(EntityPlayer victim, List<org.bukkit.inventory.ItemStack> drops, String deathMessage, boolean keepInventory)
  {
    CraftPlayer entity = victim.getBukkitEntity();
    PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, victim.getExpReward(), 0, deathMessage);
    event.setKeepInventory(keepInventory);
    org.bukkit.World world = entity.getWorld();
    Bukkit.getServer().getPluginManager().callEvent(event);
    
    victim.keepLevel = event.getKeepLevel();
    victim.newLevel = event.getNewLevel();
    victim.newTotalExp = event.getNewTotalExp();
    victim.expToDrop = event.getDroppedExp();
    victim.newExp = event.getNewExp();
    if (event.getKeepInventory()) {
      return event;
    }
    for (org.bukkit.inventory.ItemStack stack : event.getDrops()) {
      if ((stack != null) && (stack.getType() != Material.AIR)) {
        world.dropItemNaturally(entity.getLocation(), stack);
      }
    }
    return event;
  }
  
  public static ServerListPingEvent callServerListPingEvent(Server craftServer, InetAddress address, String motd, int numPlayers, int maxPlayers)
  {
    ServerListPingEvent event = new ServerListPingEvent(address, motd, numPlayers, maxPlayers);
    craftServer.getPluginManager().callEvent(event);
    return event;
  }
  
  private static EntityDamageEvent handleEntityDamageEvent(net.minecraft.server.v1_8_R3.Entity entity, DamageSource source, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions)
  {
    if (source.isExplosion())
    {
      net.minecraft.server.v1_8_R3.Entity damager = entityDamage;
      entityDamage = null;
      EntityDamageEvent event;
      EntityDamageEvent event;
      if (damager == null)
      {
        event = new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, modifiers, modifierFunctions);
      }
      else
      {
        EntityDamageEvent event;
        if (((entity instanceof EntityEnderDragon)) && (((EntityEnderDragon)entity).bA == damager))
        {
          event = new EntityDamageEvent(entity.getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, modifiers, modifierFunctions);
        }
        else
        {
          EntityDamageEvent.DamageCause damageCause;
          EntityDamageEvent.DamageCause damageCause;
          if ((damager instanceof TNTPrimed)) {
            damageCause = EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
          } else {
            damageCause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
          }
          event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), entity.getBukkitEntity(), damageCause, modifiers, modifierFunctions);
        }
      }
      callEvent(event);
      if (!event.isCancelled()) {
        event.getEntity().setLastDamageCause(event);
      }
      return event;
    }
    if ((source instanceof EntityDamageSource))
    {
      net.minecraft.server.v1_8_R3.Entity damager = source.getEntity();
      EntityDamageEvent.DamageCause cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
      if ((source instanceof EntityDamageSourceIndirect))
      {
        damager = ((EntityDamageSourceIndirect)source).getProximateDamageSource();
        if ((damager.getBukkitEntity() instanceof ThrownPotion)) {
          cause = EntityDamageEvent.DamageCause.MAGIC;
        } else if ((damager.getBukkitEntity() instanceof Projectile)) {
          cause = EntityDamageEvent.DamageCause.PROJECTILE;
        }
      }
      else if ("thorns".equals(source.translationIndex))
      {
        cause = EntityDamageEvent.DamageCause.THORNS;
      }
      return callEntityDamageEvent(damager, entity, cause, modifiers, modifierFunctions);
    }
    if (source == DamageSource.OUT_OF_WORLD)
    {
      EntityDamageEvent event = (EntityDamageEvent)callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, modifiers, modifierFunctions));
      if (!event.isCancelled()) {
        event.getEntity().setLastDamageCause(event);
      }
      return event;
    }
    if (source == DamageSource.LAVA)
    {
      EntityDamageEvent event = (EntityDamageEvent)callEvent(new EntityDamageByBlockEvent(null, entity.getBukkitEntity(), EntityDamageEvent.DamageCause.LAVA, modifiers, modifierFunctions));
      if (!event.isCancelled()) {
        event.getEntity().setLastDamageCause(event);
      }
      return event;
    }
    if (blockDamage != null)
    {
      EntityDamageEvent.DamageCause cause = null;
      org.bukkit.block.Block damager = blockDamage;
      blockDamage = null;
      if (source == DamageSource.CACTUS) {
        cause = EntityDamageEvent.DamageCause.CONTACT;
      } else {
        throw new RuntimeException(String.format("Unhandled damage of %s by %s from %s", new Object[] { entity, damager, source.translationIndex }));
      }
      EntityDamageEvent event = (EntityDamageEvent)callEvent(new EntityDamageByBlockEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
      if (!event.isCancelled()) {
        event.getEntity().setLastDamageCause(event);
      }
      return event;
    }
    if (entityDamage != null)
    {
      EntityDamageEvent.DamageCause cause = null;
      CraftEntity damager = entityDamage.getBukkitEntity();
      entityDamage = null;
      if ((source == DamageSource.ANVIL) || (source == DamageSource.FALLING_BLOCK)) {
        cause = EntityDamageEvent.DamageCause.FALLING_BLOCK;
      } else if ((damager instanceof LightningStrike)) {
        cause = EntityDamageEvent.DamageCause.LIGHTNING;
      } else if (source == DamageSource.FALL) {
        cause = EntityDamageEvent.DamageCause.FALL;
      } else {
        throw new RuntimeException(String.format("Unhandled damage of %s by %s from %s", new Object[] { entity, damager.getHandle(), source.translationIndex }));
      }
      EntityDamageEvent event = (EntityDamageEvent)callEvent(new EntityDamageByEntityEvent(damager, entity.getBukkitEntity(), cause, modifiers, modifierFunctions));
      if (!event.isCancelled()) {
        event.getEntity().setLastDamageCause(event);
      }
      return event;
    }
    EntityDamageEvent.DamageCause cause = null;
    if (source == DamageSource.FIRE) {
      cause = EntityDamageEvent.DamageCause.FIRE;
    } else if (source == DamageSource.STARVE) {
      cause = EntityDamageEvent.DamageCause.STARVATION;
    } else if (source == DamageSource.WITHER) {
      cause = EntityDamageEvent.DamageCause.WITHER;
    } else if (source == DamageSource.STUCK) {
      cause = EntityDamageEvent.DamageCause.SUFFOCATION;
    } else if (source == DamageSource.DROWN) {
      cause = EntityDamageEvent.DamageCause.DROWNING;
    } else if (source == DamageSource.BURN) {
      cause = EntityDamageEvent.DamageCause.FIRE_TICK;
    } else if (source == MELTING) {
      cause = EntityDamageEvent.DamageCause.MELTING;
    } else if (source == POISON) {
      cause = EntityDamageEvent.DamageCause.POISON;
    } else if (source == DamageSource.MAGIC) {
      cause = EntityDamageEvent.DamageCause.MAGIC;
    } else if (source == DamageSource.FALL) {
      cause = EntityDamageEvent.DamageCause.FALL;
    } else if (source == DamageSource.GENERIC) {
      return new EntityDamageEvent(entity.getBukkitEntity(), null, modifiers, modifierFunctions);
    }
    if (cause != null) {
      return callEntityDamageEvent(null, entity, cause, modifiers, modifierFunctions);
    }
    throw new RuntimeException(String.format("Unhandled damage of %s from %s", new Object[] { entity, source.translationIndex }));
  }
  
  private static EntityDamageEvent callEntityDamageEvent(net.minecraft.server.v1_8_R3.Entity damager, net.minecraft.server.v1_8_R3.Entity damagee, EntityDamageEvent.DamageCause cause, Map<EntityDamageEvent.DamageModifier, Double> modifiers, Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions)
  {
    EntityDamageEvent event;
    EntityDamageEvent event;
    if (damager != null) {
      event = new EntityDamageByEntityEvent(damager.getBukkitEntity(), damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
    } else {
      event = new EntityDamageEvent(damagee.getBukkitEntity(), cause, modifiers, modifierFunctions);
    }
    callEvent(event);
    if (!event.isCancelled()) {
      event.getEntity().setLastDamageCause(event);
    }
    return event;
  }
  
  private static final Function<? super Double, Double> ZERO = Functions.constant(Double.valueOf(-0.0D));
  
  public static EntityDamageEvent handleLivingEntityDamageEvent(net.minecraft.server.v1_8_R3.Entity damagee, DamageSource source, double rawDamage, double hardHatModifier, double blockingModifier, double armorModifier, double resistanceModifier, double magicModifier, double absorptionModifier, Function<Double, Double> hardHat, Function<Double, Double> blocking, Function<Double, Double> armor, Function<Double, Double> resistance, Function<Double, Double> magic, Function<Double, Double> absorption)
  {
    Map<EntityDamageEvent.DamageModifier, Double> modifiers = new EnumMap(EntityDamageEvent.DamageModifier.class);
    Map<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> modifierFunctions = new EnumMap(EntityDamageEvent.DamageModifier.class);
    modifiers.put(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(rawDamage));
    modifierFunctions.put(EntityDamageEvent.DamageModifier.BASE, ZERO);
    if ((source == DamageSource.FALLING_BLOCK) || (source == DamageSource.ANVIL))
    {
      modifiers.put(EntityDamageEvent.DamageModifier.HARD_HAT, Double.valueOf(hardHatModifier));
      modifierFunctions.put(EntityDamageEvent.DamageModifier.HARD_HAT, hardHat);
    }
    if ((damagee instanceof EntityHuman))
    {
      modifiers.put(EntityDamageEvent.DamageModifier.BLOCKING, Double.valueOf(blockingModifier));
      modifierFunctions.put(EntityDamageEvent.DamageModifier.BLOCKING, blocking);
    }
    modifiers.put(EntityDamageEvent.DamageModifier.ARMOR, Double.valueOf(armorModifier));
    modifierFunctions.put(EntityDamageEvent.DamageModifier.ARMOR, armor);
    modifiers.put(EntityDamageEvent.DamageModifier.RESISTANCE, Double.valueOf(resistanceModifier));
    modifierFunctions.put(EntityDamageEvent.DamageModifier.RESISTANCE, resistance);
    modifiers.put(EntityDamageEvent.DamageModifier.MAGIC, Double.valueOf(magicModifier));
    modifierFunctions.put(EntityDamageEvent.DamageModifier.MAGIC, magic);
    modifiers.put(EntityDamageEvent.DamageModifier.ABSORPTION, Double.valueOf(absorptionModifier));
    modifierFunctions.put(EntityDamageEvent.DamageModifier.ABSORPTION, absorption);
    return handleEntityDamageEvent(damagee, source, modifiers, modifierFunctions);
  }
  
  public static boolean handleNonLivingEntityDamageEvent(net.minecraft.server.v1_8_R3.Entity entity, DamageSource source, double damage)
  {
    return handleNonLivingEntityDamageEvent(entity, source, damage, true);
  }
  
  public static boolean handleNonLivingEntityDamageEvent(net.minecraft.server.v1_8_R3.Entity entity, DamageSource source, double damage, boolean cancelOnZeroDamage)
  {
    if (((entity instanceof EntityEnderCrystal)) && (!(source instanceof EntityDamageSource))) {
      return false;
    }
    EnumMap<EntityDamageEvent.DamageModifier, Double> modifiers = new EnumMap(EntityDamageEvent.DamageModifier.class);
    EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>> functions = new EnumMap(EntityDamageEvent.DamageModifier.class);
    
    modifiers.put(EntityDamageEvent.DamageModifier.BASE, Double.valueOf(damage));
    functions.put(EntityDamageEvent.DamageModifier.BASE, ZERO);
    
    EntityDamageEvent event = handleEntityDamageEvent(entity, source, modifiers, functions);
    if (event == null) {
      return false;
    }
    return (event.isCancelled()) || ((cancelOnZeroDamage) && (event.getDamage() == 0.0D));
  }
  
  public static PlayerLevelChangeEvent callPlayerLevelChangeEvent(Player player, int oldLevel, int newLevel)
  {
    PlayerLevelChangeEvent event = new PlayerLevelChangeEvent(player, oldLevel, newLevel);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
  
  public static PlayerExpChangeEvent callPlayerExpChangeEvent(EntityHuman entity, int expAmount)
  {
    Player player = (Player)entity.getBukkitEntity();
    PlayerExpChangeEvent event = new PlayerExpChangeEvent(player, expAmount);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
  
  public static void handleBlockGrowEvent(net.minecraft.server.v1_8_R3.World world, int x, int y, int z, net.minecraft.server.v1_8_R3.Block type, int data)
  {
    org.bukkit.block.Block block = world.getWorld().getBlockAt(x, y, z);
    CraftBlockState state = (CraftBlockState)block.getState();
    state.setTypeId(net.minecraft.server.v1_8_R3.Block.getId(type));
    state.setRawData((byte)data);
    
    BlockGrowEvent event = new BlockGrowEvent(block, state);
    Bukkit.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      state.update(true);
    }
  }
  
  public static FoodLevelChangeEvent callFoodLevelChangeEvent(EntityHuman entity, int level)
  {
    FoodLevelChangeEvent event = new FoodLevelChangeEvent(entity.getBukkitEntity(), level);
    entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static PigZapEvent callPigZapEvent(net.minecraft.server.v1_8_R3.Entity pig, net.minecraft.server.v1_8_R3.Entity lightning, net.minecraft.server.v1_8_R3.Entity pigzombie)
  {
    PigZapEvent event = new PigZapEvent((Pig)pig.getBukkitEntity(), (LightningStrike)lightning.getBukkitEntity(), (PigZombie)pigzombie.getBukkitEntity());
    pig.getBukkitEntity().getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static HorseJumpEvent callHorseJumpEvent(net.minecraft.server.v1_8_R3.Entity horse, float power)
  {
    HorseJumpEvent event = new HorseJumpEvent((Horse)horse.getBukkitEntity(), power);
    horse.getBukkitEntity().getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, org.bukkit.block.Block block, Material material)
  {
    return callEntityChangeBlockEvent(entity, block, material, 0);
  }
  
  public static EntityChangeBlockEvent callEntityChangeBlockEvent(net.minecraft.server.v1_8_R3.Entity entity, org.bukkit.block.Block block, Material material)
  {
    return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0);
  }
  
  public static EntityChangeBlockEvent callEntityChangeBlockEvent(net.minecraft.server.v1_8_R3.Entity entity, org.bukkit.block.Block block, Material material, boolean cancelled)
  {
    return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, 0, cancelled);
  }
  
  public static EntityChangeBlockEvent callEntityChangeBlockEvent(net.minecraft.server.v1_8_R3.Entity entity, int x, int y, int z, net.minecraft.server.v1_8_R3.Block type, int data)
  {
    org.bukkit.block.Block block = entity.world.getWorld().getBlockAt(x, y, z);
    Material material = CraftMagicNumbers.getMaterial(type);
    
    return callEntityChangeBlockEvent(entity.getBukkitEntity(), block, material, data);
  }
  
  public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, org.bukkit.block.Block block, Material material, int data)
  {
    return callEntityChangeBlockEvent(entity, block, material, data, false);
  }
  
  public static EntityChangeBlockEvent callEntityChangeBlockEvent(org.bukkit.entity.Entity entity, org.bukkit.block.Block block, Material material, int data, boolean cancelled)
  {
    EntityChangeBlockEvent event = new EntityChangeBlockEvent(entity, block, material, (byte)data);
    event.setCancelled(cancelled);
    entity.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static CreeperPowerEvent callCreeperPowerEvent(net.minecraft.server.v1_8_R3.Entity creeper, net.minecraft.server.v1_8_R3.Entity lightning, CreeperPowerEvent.PowerCause cause)
  {
    CreeperPowerEvent event = new CreeperPowerEvent((Creeper)creeper.getBukkitEntity(), (LightningStrike)lightning.getBukkitEntity(), cause);
    creeper.getBukkitEntity().getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static EntityTargetEvent callEntityTargetEvent(net.minecraft.server.v1_8_R3.Entity entity, net.minecraft.server.v1_8_R3.Entity target, EntityTargetEvent.TargetReason reason)
  {
    EntityTargetEvent event = new EntityTargetEvent(entity.getBukkitEntity(), target == null ? null : target.getBukkitEntity(), reason);
    entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static EntityTargetLivingEntityEvent callEntityTargetLivingEvent(net.minecraft.server.v1_8_R3.Entity entity, EntityLiving target, EntityTargetEvent.TargetReason reason)
  {
    EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(entity.getBukkitEntity(), (LivingEntity)target.getBukkitEntity(), reason);
    entity.getBukkitEntity().getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static EntityBreakDoorEvent callEntityBreakDoorEvent(net.minecraft.server.v1_8_R3.Entity entity, int x, int y, int z)
  {
    org.bukkit.entity.Entity entity1 = entity.getBukkitEntity();
    org.bukkit.block.Block block = entity1.getWorld().getBlockAt(x, y, z);
    
    EntityBreakDoorEvent event = new EntityBreakDoorEvent((LivingEntity)entity1, block);
    entity1.getServer().getPluginManager().callEvent(event);
    
    return event;
  }
  
  public static Container callInventoryOpenEvent(EntityPlayer player, Container container)
  {
    return callInventoryOpenEvent(player, container, false);
  }
  
  public static Container callInventoryOpenEvent(EntityPlayer player, Container container, boolean cancelled)
  {
    if (player.activeContainer != player.defaultContainer) {
      player.playerConnection.a(new PacketPlayInCloseWindow(player.activeContainer.windowId));
    }
    CraftServer server = player.world.getServer();
    CraftPlayer craftPlayer = player.getBukkitEntity();
    player.activeContainer.transferTo(container, craftPlayer);
    
    InventoryOpenEvent event = new InventoryOpenEvent(container.getBukkitView());
    event.setCancelled(cancelled);
    server.getPluginManager().callEvent(event);
    if (event.isCancelled())
    {
      container.transferTo(player.activeContainer, craftPlayer);
      return null;
    }
    return container;
  }
  
  public static net.minecraft.server.v1_8_R3.ItemStack callPreCraftEvent(InventoryCrafting matrix, net.minecraft.server.v1_8_R3.ItemStack result, InventoryView lastCraftView, boolean isRepair)
  {
    CraftInventoryCrafting inventory = new CraftInventoryCrafting(matrix, matrix.resultInventory);
    inventory.setResult(CraftItemStack.asCraftMirror(result));
    
    PrepareItemCraftEvent event = new PrepareItemCraftEvent(inventory, lastCraftView, isRepair);
    Bukkit.getPluginManager().callEvent(event);
    
    org.bukkit.inventory.ItemStack bitem = event.getInventory().getResult();
    
    return CraftItemStack.asNMSCopy(bitem);
  }
  
  public static ProjectileLaunchEvent callProjectileLaunchEvent(net.minecraft.server.v1_8_R3.Entity entity)
  {
    Projectile bukkitEntity = (Projectile)entity.getBukkitEntity();
    ProjectileLaunchEvent event = new ProjectileLaunchEvent(bukkitEntity);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
  
  public static ProjectileHitEvent callProjectileHitEvent(net.minecraft.server.v1_8_R3.Entity entity)
  {
    ProjectileHitEvent event = new ProjectileHitEvent((Projectile)entity.getBukkitEntity());
    entity.world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static ExpBottleEvent callExpBottleEvent(net.minecraft.server.v1_8_R3.Entity entity, int exp)
  {
    ThrownExpBottle bottle = (ThrownExpBottle)entity.getBukkitEntity();
    ExpBottleEvent event = new ExpBottleEvent(bottle, exp);
    Bukkit.getPluginManager().callEvent(event);
    return event;
  }
  
  public static BlockRedstoneEvent callRedstoneChange(net.minecraft.server.v1_8_R3.World world, int x, int y, int z, int oldCurrent, int newCurrent)
  {
    BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(x, y, z), oldCurrent, newCurrent);
    world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static NotePlayEvent callNotePlayEvent(net.minecraft.server.v1_8_R3.World world, int x, int y, int z, byte instrument, byte note)
  {
    NotePlayEvent event = new NotePlayEvent(world.getWorld().getBlockAt(x, y, z), Instrument.getByType(instrument), new Note(note));
    world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static void callPlayerItemBreakEvent(EntityHuman human, net.minecraft.server.v1_8_R3.ItemStack brokenItem)
  {
    CraftItemStack item = CraftItemStack.asCraftMirror(brokenItem);
    PlayerItemBreakEvent event = new PlayerItemBreakEvent((Player)human.getBukkitEntity(), item);
    Bukkit.getPluginManager().callEvent(event);
  }
  
  public static BlockIgniteEvent callBlockIgniteEvent(net.minecraft.server.v1_8_R3.World world, int x, int y, int z, int igniterX, int igniterY, int igniterZ)
  {
    org.bukkit.World bukkitWorld = world.getWorld();
    org.bukkit.block.Block igniter = bukkitWorld.getBlockAt(igniterX, igniterY, igniterZ);
    BlockIgniteEvent.IgniteCause cause;
    BlockIgniteEvent.IgniteCause cause;
    BlockIgniteEvent.IgniteCause cause;
    switch (igniter.getType())
    {
    case ARROW: 
    case BAKED_POTATO: 
      cause = BlockIgniteEvent.IgniteCause.LAVA;
      break;
    case BLAZE_POWDER: 
      cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
      break;
    case CHAINMAIL_HELMET: 
    default: 
      cause = BlockIgniteEvent.IgniteCause.SPREAD;
    }
    BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, igniter);
    world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static BlockIgniteEvent callBlockIgniteEvent(net.minecraft.server.v1_8_R3.World world, int x, int y, int z, net.minecraft.server.v1_8_R3.Entity igniter)
  {
    org.bukkit.World bukkitWorld = world.getWorld();
    org.bukkit.entity.Entity bukkitIgniter = igniter.getBukkitEntity();
    BlockIgniteEvent.IgniteCause cause;
    BlockIgniteEvent.IgniteCause cause;
    BlockIgniteEvent.IgniteCause cause;
    BlockIgniteEvent.IgniteCause cause;
    switch (bukkitIgniter.getType())
    {
    case THROWN_EXP_BOTTLE: 
      cause = BlockIgniteEvent.IgniteCause.ENDER_CRYSTAL;
      break;
    case WITCH: 
      cause = BlockIgniteEvent.IgniteCause.LIGHTNING;
      break;
    case CHICKEN: 
    case COMPLEX_PART: 
      cause = BlockIgniteEvent.IgniteCause.FIREBALL;
      break;
    default: 
      cause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
    }
    BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), cause, bukkitIgniter);
    world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static BlockIgniteEvent callBlockIgniteEvent(net.minecraft.server.v1_8_R3.World world, int x, int y, int z, Explosion explosion)
  {
    org.bukkit.World bukkitWorld = world.getWorld();
    org.bukkit.entity.Entity igniter = explosion.source == null ? null : explosion.source.getBukkitEntity();
    
    BlockIgniteEvent event = new BlockIgniteEvent(bukkitWorld.getBlockAt(x, y, z), BlockIgniteEvent.IgniteCause.EXPLOSION, igniter);
    world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static BlockIgniteEvent callBlockIgniteEvent(net.minecraft.server.v1_8_R3.World world, int x, int y, int z, BlockIgniteEvent.IgniteCause cause, net.minecraft.server.v1_8_R3.Entity igniter)
  {
    BlockIgniteEvent event = new BlockIgniteEvent(world.getWorld().getBlockAt(x, y, z), cause, igniter.getBukkitEntity());
    world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static void handleInventoryCloseEvent(EntityHuman human)
  {
    InventoryCloseEvent event = new InventoryCloseEvent(human.activeContainer.getBukkitView());
    human.world.getServer().getPluginManager().callEvent(event);
    human.activeContainer.transferTo(human.defaultContainer, human.getBukkitEntity());
  }
  
  public static void handleEditBookEvent(EntityPlayer player, net.minecraft.server.v1_8_R3.ItemStack newBookItem)
  {
    int itemInHandIndex = player.inventory.itemInHandIndex;
    
    PlayerEditBookEvent editBookEvent = new PlayerEditBookEvent(player.getBukkitEntity(), player.inventory.itemInHandIndex, (BookMeta)CraftItemStack.getItemMeta(player.inventory.getItemInHand()), (BookMeta)CraftItemStack.getItemMeta(newBookItem), newBookItem.getItem() == Items.WRITTEN_BOOK);
    player.world.getServer().getPluginManager().callEvent(editBookEvent);
    net.minecraft.server.v1_8_R3.ItemStack itemInHand = player.inventory.getItem(itemInHandIndex);
    if ((itemInHand != null) && (itemInHand.getItem() == Items.WRITABLE_BOOK))
    {
      if (!editBookEvent.isCancelled())
      {
        if (editBookEvent.isSigning()) {
          itemInHand.setItem(Items.WRITTEN_BOOK);
        }
        CraftMetaBook meta = (CraftMetaBook)editBookEvent.getNewBookMeta();
        List<IChatBaseComponent> pages = meta.pages;
        for (int i = 0; i < pages.size(); i++) {
          pages.set(i, stripEvents((IChatBaseComponent)pages.get(i)));
        }
        CraftItemStack.setItemMeta(itemInHand, meta);
      }
      Slot slot = player.activeContainer.getSlot(player.inventory, itemInHandIndex);
      player.playerConnection.sendPacket(new PacketPlayOutSetSlot(player.activeContainer.windowId, slot.rawSlotIndex, itemInHand));
    }
  }
  
  private static IChatBaseComponent stripEvents(IChatBaseComponent c)
  {
    ChatModifier modi = c.getChatModifier();
    if (modi != null)
    {
      modi.setChatClickable(null);
      modi.setChatHoverable(null);
    }
    c.setChatModifier(modi);
    if ((c instanceof ChatMessage))
    {
      ChatMessage cm = (ChatMessage)c;
      Object[] oo = cm.j();
      for (int i = 0; i < oo.length; i++)
      {
        Object o = oo[i];
        if ((o instanceof IChatBaseComponent)) {
          oo[i] = stripEvents((IChatBaseComponent)o);
        }
      }
    }
    List<IChatBaseComponent> ls = c.a();
    if (ls != null) {
      for (int i = 0; i < ls.size(); i++) {
        ls.set(i, stripEvents((IChatBaseComponent)ls.get(i)));
      }
    }
    return c;
  }
  
  public static PlayerUnleashEntityEvent callPlayerUnleashEntityEvent(EntityInsentient entity, EntityHuman player)
  {
    PlayerUnleashEntityEvent event = new PlayerUnleashEntityEvent(entity.getBukkitEntity(), (Player)player.getBukkitEntity());
    entity.world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static PlayerLeashEntityEvent callPlayerLeashEntityEvent(EntityInsentient entity, net.minecraft.server.v1_8_R3.Entity leashHolder, EntityHuman player)
  {
    PlayerLeashEntityEvent event = new PlayerLeashEntityEvent(entity.getBukkitEntity(), leashHolder.getBukkitEntity(), (Player)player.getBukkitEntity());
    entity.world.getServer().getPluginManager().callEvent(event);
    return event;
  }
  
  public static Cancellable handleStatisticsIncrease(EntityHuman entityHuman, net.minecraft.server.v1_8_R3.Statistic statistic, int current, int incrementation)
  {
    Player player = ((EntityPlayer)entityHuman).getBukkitEntity();
    Event event;
    Event event;
    if ((statistic instanceof Achievement))
    {
      if (current != 0) {
        return null;
      }
      event = new PlayerAchievementAwardedEvent(player, CraftStatistic.getBukkitAchievement((Achievement)statistic));
    }
    else
    {
      org.bukkit.Statistic stat = CraftStatistic.getBukkitStatistic(statistic);
      if (stat == null)
      {
        System.err.println("Unhandled statistic: " + statistic);
        return null;
      }
      switch (stat)
      {
      case CRAFTING_TABLE_INTERACTION: 
      case CRAFT_ITEM: 
      case CROUCH_ONE_CM: 
      case DAMAGE_DEALT: 
      case DAMAGE_TAKEN: 
      case DEATHS: 
      case DISPENSER_INSPECTED: 
      case DIVE_ONE_CM: 
      case DROP: 
      case DROPPER_INSPECTED: 
      case ENDERCHEST_OPENED: 
      case ENTITY_KILLED_BY: 
      case FALL_ONE_CM: 
      case ITEM_ENCHANTED: 
        return null;
      }
      Event event;
      if (stat.getType() == Statistic.Type.UNTYPED)
      {
        event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation);
      }
      else
      {
        Event event;
        if (stat.getType() == Statistic.Type.ENTITY)
        {
          EntityType entityType = CraftStatistic.getEntityTypeFromStatistic(statistic);
          event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, entityType);
        }
        else
        {
          Material material = CraftStatistic.getMaterialFromStatistic(statistic);
          event = new PlayerStatisticIncrementEvent(player, stat, current, current + incrementation, material);
        }
      }
    }
    entityHuman.world.getServer().getPluginManager().callEvent(event);
    return (Cancellable)event;
  }
}
