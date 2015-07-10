package net.minecraft.server.v1_8_R3;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryView;
import org.spigotmc.SpigotWorldConfig;

public class EntityPlayer
  extends EntityHuman
  implements ICrafting
{
  private static final Logger bH = ;
  public String locale = "en_US";
  public PlayerConnection playerConnection;
  public final MinecraftServer server;
  public final PlayerInteractManager playerInteractManager;
  public double d;
  public double e;
  public final List<ChunkCoordIntPair> chunkCoordIntPairQueue = Lists.newLinkedList();
  public final List<Integer> removeQueue = Lists.newLinkedList();
  private final ServerStatisticManager bK;
  private float bL = Float.MIN_VALUE;
  private float bM = -1.0E8F;
  private int bN = -99999999;
  private boolean bO = true;
  public int lastSentExp = -99999999;
  public int invulnerableTicks = 60;
  private EntityHuman.EnumChatVisibility bR;
  private boolean bS = true;
  private long bT = System.currentTimeMillis();
  private Entity bU = null;
  private int containerCounter;
  public boolean g;
  public int ping;
  public boolean viewingCredits;
  public String displayName;
  public IChatBaseComponent listName;
  public Location compassTarget;
  public int newExp = 0;
  public int newLevel = 0;
  public int newTotalExp = 0;
  public boolean keepLevel = false;
  public double maxHealthCache;
  public boolean joining = true;
  public boolean collidesWithEntities = true;
  
  public boolean ad()
  {
    return (this.collidesWithEntities) && (super.ad());
  }
  
  public boolean ae()
  {
    return (this.collidesWithEntities) && (super.ae());
  }
  
  public EntityPlayer(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager)
  {
    super(worldserver, gameprofile);
    playerinteractmanager.player = this;
    this.playerInteractManager = playerinteractmanager;
    BlockPosition blockposition = worldserver.getSpawn();
    if ((!worldserver.worldProvider.o()) && (worldserver.getWorldData().getGameType() != WorldSettings.EnumGamemode.ADVENTURE))
    {
      int i = Math.max(5, minecraftserver.getSpawnProtection() - 6);
      int j = MathHelper.floor(worldserver.getWorldBorder().b(blockposition.getX(), blockposition.getZ()));
      if (j < i) {
        i = j;
      }
      if (j <= 1) {
        i = 1;
      }
      blockposition = worldserver.r(blockposition.a(this.random.nextInt(i * 2) - i, 0, this.random.nextInt(i * 2) - i));
    }
    this.server = minecraftserver;
    this.bK = minecraftserver.getPlayerList().a(this);
    this.S = 0.0F;
    setPositionRotation(blockposition, 0.0F, 0.0F);
    while ((!worldserver.getCubes(this, getBoundingBox()).isEmpty()) && (this.locY < 255.0D)) {
      setPosition(this.locX, this.locY + 1.0D, this.locZ);
    }
    this.displayName = getName();
    
    this.maxHealthCache = getMaxHealth();
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    if (nbttagcompound.hasKeyOfType("playerGameType", 99)) {
      if (MinecraftServer.getServer().getForceGamemode()) {
        this.playerInteractManager.setGameMode(MinecraftServer.getServer().getGamemode());
      } else {
        this.playerInteractManager.setGameMode(WorldSettings.EnumGamemode.getById(nbttagcompound.getInt("playerGameType")));
      }
    }
    getBukkitEntity().readExtraData(nbttagcompound);
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("playerGameType", this.playerInteractManager.getGameMode().getId());
    
    getBukkitEntity().setExtraData(nbttagcompound);
  }
  
  public void spawnIn(World world)
  {
    super.spawnIn(world);
    if (world == null)
    {
      this.dead = false;
      BlockPosition position = null;
      if ((this.spawnWorld != null) && (!this.spawnWorld.equals("")))
      {
        CraftWorld cworld = (CraftWorld)Bukkit.getServer().getWorld(this.spawnWorld);
        if ((cworld != null) && (getBed() != null))
        {
          world = cworld.getHandle();
          position = EntityHuman.getBed(cworld.getHandle(), getBed(), false);
        }
      }
      if ((world == null) || (position == null))
      {
        world = ((CraftWorld)Bukkit.getServer().getWorlds().get(0)).getHandle();
        position = world.getSpawn();
      }
      this.world = world;
      setPosition(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D);
    }
    this.dimension = ((WorldServer)this.world).dimension;
    this.playerInteractManager.a((WorldServer)world);
  }
  
  public void levelDown(int i)
  {
    super.levelDown(i);
    this.lastSentExp = -1;
  }
  
  public void b(int i)
  {
    super.b(i);
    this.lastSentExp = -1;
  }
  
  public void syncInventory()
  {
    this.activeContainer.addSlotListener(this);
  }
  
  public void enterCombat()
  {
    super.enterCombat();
    this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(bs(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTER_COMBAT));
  }
  
  public void exitCombat()
  {
    super.exitCombat();
    this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(bs(), PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT));
  }
  
  public void t_()
  {
    if (this.joining) {
      this.joining = false;
    }
    this.playerInteractManager.a();
    this.invulnerableTicks -= 1;
    if (this.noDamageTicks > 0) {
      this.noDamageTicks -= 1;
    }
    this.activeContainer.b();
    if ((!this.world.isClientSide) && (!this.activeContainer.a(this)))
    {
      closeInventory();
      this.activeContainer = this.defaultContainer;
    }
    while (!this.removeQueue.isEmpty())
    {
      int i = Math.min(this.removeQueue.size(), Integer.MAX_VALUE);
      int[] aint = new int[i];
      Iterator iterator = this.removeQueue.iterator();
      int j = 0;
      while ((iterator.hasNext()) && (j < i))
      {
        aint[(j++)] = ((Integer)iterator.next()).intValue();
        iterator.remove();
      }
      this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(aint));
    }
    if (!this.chunkCoordIntPairQueue.isEmpty())
    {
      ArrayList arraylist = Lists.newArrayList();
      Iterator iterator1 = this.chunkCoordIntPairQueue.iterator();
      ArrayList arraylist1 = Lists.newArrayList();
      while ((iterator1.hasNext()) && (arraylist.size() < this.world.spigotConfig.maxBulkChunk))
      {
        ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator1.next();
        if (chunkcoordintpair != null)
        {
          if (this.world.isLoaded(new BlockPosition(chunkcoordintpair.x << 4, 0, chunkcoordintpair.z << 4)))
          {
            Chunk chunk = this.world.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z);
            if (chunk.isReady())
            {
              arraylist.add(chunk);
              arraylist1.addAll(chunk.tileEntities.values());
              iterator1.remove();
            }
          }
        }
        else {
          iterator1.remove();
        }
      }
      if (!arraylist.isEmpty())
      {
        if (arraylist.size() == 1) {
          this.playerConnection.sendPacket(new PacketPlayOutMapChunk((Chunk)arraylist.get(0), true, 65535));
        } else {
          this.playerConnection.sendPacket(new PacketPlayOutMapChunkBulk(arraylist));
        }
        Iterator iterator2 = arraylist1.iterator();
        while (iterator2.hasNext())
        {
          TileEntity tileentity = (TileEntity)iterator2.next();
          
          a(tileentity);
        }
        iterator2 = arraylist.iterator();
        while (iterator2.hasNext())
        {
          Chunk chunk = (Chunk)iterator2.next();
          u().getTracker().a(this, chunk);
        }
      }
    }
    Entity entity = C();
    if (entity != this) {
      if (!entity.isAlive())
      {
        setSpectatorTarget(this);
      }
      else
      {
        setLocation(entity.locX, entity.locY, entity.locZ, entity.yaw, entity.pitch);
        this.server.getPlayerList().d(this);
        if (isSneaking()) {
          setSpectatorTarget(this);
        }
      }
    }
  }
  
  public void l()
  {
    try
    {
      super.t_();
      for (int i = 0; i < this.inventory.getSize(); i++)
      {
        ItemStack itemstack = this.inventory.getItem(i);
        if ((itemstack != null) && (itemstack.getItem().f()))
        {
          Packet packet = ((ItemWorldMapBase)itemstack.getItem()).c(itemstack, this.world, this);
          if (packet != null) {
            this.playerConnection.sendPacket(packet);
          }
        }
      }
      if ((getHealth() == this.bM) && (this.bN == this.foodData.getFoodLevel()))
      {
        if ((this.foodData.getSaturationLevel() == 0.0F) == this.bO) {}
      }
      else
      {
        this.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(getBukkitEntity().getScaledHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
        this.bM = getHealth();
        this.bN = this.foodData.getFoodLevel();
        this.bO = (this.foodData.getSaturationLevel() == 0.0F);
      }
      if (getHealth() + getAbsorptionHearts() != this.bL)
      {
        this.bL = (getHealth() + getAbsorptionHearts());
        Collection collection = getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.g);
        Iterator iterator = collection.iterator();
        while (iterator.hasNext())
        {
          ScoreboardObjective scoreboardobjective = (ScoreboardObjective)iterator.next();
          
          getScoreboard().getPlayerScoreForObjective(getName(), scoreboardobjective).updateForList(Arrays.asList(new EntityHuman[] { this }));
        }
        this.world.getServer().getScoreboardManager().updateAllScoresForList(IScoreboardCriteria.g, getName(), ImmutableList.of(this));
      }
      if (this.maxHealthCache != getMaxHealth()) {
        getBukkitEntity().updateScaledHealth();
      }
      if (this.expTotal != this.lastSentExp)
      {
        this.lastSentExp = this.expTotal;
        this.playerConnection.sendPacket(new PacketPlayOutExperience(this.exp, this.expTotal, this.expLevel));
      }
      if ((this.ticksLived % 20 * 5 == 0) && (!getStatisticManager().hasAchievement(AchievementList.L))) {
        i_();
      }
      if (this.oldLevel == -1) {
        this.oldLevel = this.expLevel;
      }
      if (this.oldLevel != this.expLevel)
      {
        CraftEventFactory.callPlayerLevelChangeEvent(this.world.getServer().getPlayer(this), this.oldLevel, this.expLevel);
        this.oldLevel = this.expLevel;
      }
    }
    catch (Throwable throwable)
    {
      CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
      CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Player being ticked");
      
      appendEntityCrashDetails(crashreportsystemdetails);
      throw new ReportedException(crashreport);
    }
  }
  
  protected void i_()
  {
    BiomeBase biomebase = this.world.getBiome(new BlockPosition(MathHelper.floor(this.locX), 0, MathHelper.floor(this.locZ)));
    String s = biomebase.ah;
    AchievementSet achievementset = (AchievementSet)getStatisticManager().b(AchievementList.L);
    if (achievementset == null) {
      achievementset = (AchievementSet)getStatisticManager().a(AchievementList.L, new AchievementSet());
    }
    achievementset.add(s);
    if ((getStatisticManager().b(AchievementList.L)) && (achievementset.size() >= BiomeBase.n.size()))
    {
      HashSet hashset = Sets.newHashSet(BiomeBase.n);
      Iterator iterator = achievementset.iterator();
      while (iterator.hasNext())
      {
        String s1 = (String)iterator.next();
        Iterator iterator1 = hashset.iterator();
        while (iterator1.hasNext())
        {
          BiomeBase biomebase1 = (BiomeBase)iterator1.next();
          if (biomebase1.ah.equals(s1)) {
            iterator1.remove();
          }
        }
        if (hashset.isEmpty()) {
          break;
        }
      }
      if (hashset.isEmpty()) {
        b(AchievementList.L);
      }
    }
  }
  
  public void die(DamageSource damagesource)
  {
    if (this.dead) {
      return;
    }
    List<org.bukkit.inventory.ItemStack> loot = new ArrayList();
    boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");
    if (!keepInventory)
    {
      for (int i = 0; i < this.inventory.items.length; i++) {
        if (this.inventory.items[i] != null) {
          loot.add(CraftItemStack.asCraftMirror(this.inventory.items[i]));
        }
      }
      for (int i = 0; i < this.inventory.armor.length; i++) {
        if (this.inventory.armor[i] != null) {
          loot.add(CraftItemStack.asCraftMirror(this.inventory.armor[i]));
        }
      }
    }
    IChatBaseComponent chatmessage = bs().b();
    
    String deathmessage = chatmessage.c();
    PlayerDeathEvent event = CraftEventFactory.callPlayerDeathEvent(this, loot, deathmessage, keepInventory);
    
    String deathMessage = event.getDeathMessage();
    if ((deathMessage != null) && (deathMessage.length() > 0) && (this.world.getGameRules().getBoolean("showDeathMessages"))) {
      if (deathMessage.equals(deathmessage)) {
        this.server.getPlayerList().sendMessage(chatmessage);
      } else {
        this.server.getPlayerList().sendMessage(CraftChatMessage.fromString(deathMessage));
      }
    }
    if (!event.getKeepInventory())
    {
      for (int i = 0; i < this.inventory.items.length; i++) {
        this.inventory.items[i] = null;
      }
      for (int i = 0; i < this.inventory.armor.length; i++) {
        this.inventory.armor[i] = null;
      }
    }
    closeInventory();
    setSpectatorTarget(this);
    
    Collection collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreboardCriteria.d, getName(), new ArrayList());
    Iterator iterator = collection.iterator();
    while (iterator.hasNext())
    {
      ScoreboardScore scoreboardscore = (ScoreboardScore)iterator.next();
      
      scoreboardscore.incrementScore();
    }
    EntityLiving entityliving = bt();
    if (entityliving != null)
    {
      EntityTypes.MonsterEggInfo entitytypes_monsteregginfo = (EntityTypes.MonsterEggInfo)EntityTypes.eggInfo.get(Integer.valueOf(EntityTypes.a(entityliving)));
      if (entitytypes_monsteregginfo != null) {
        b(entitytypes_monsteregginfo.e);
      }
      entityliving.b(this, this.aW);
    }
    b(StatisticList.y);
    a(StatisticList.h);
    bs().g();
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    boolean flag = (this.server.ae()) && (cr()) && ("fall".equals(damagesource.translationIndex));
    if ((!flag) && (this.invulnerableTicks > 0) && (damagesource != DamageSource.OUT_OF_WORLD)) {
      return false;
    }
    if ((damagesource instanceof EntityDamageSource))
    {
      Entity entity = damagesource.getEntity();
      if (((entity instanceof EntityHuman)) && (!a((EntityHuman)entity))) {
        return false;
      }
      if ((entity instanceof EntityArrow))
      {
        EntityArrow entityarrow = (EntityArrow)entity;
        if (((entityarrow.shooter instanceof EntityHuman)) && (!a((EntityHuman)entityarrow.shooter))) {
          return false;
        }
      }
    }
    return super.damageEntity(damagesource, f);
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return !cr() ? false : super.a(entityhuman);
  }
  
  private boolean cr()
  {
    return this.world.pvpMode;
  }
  
  public void c(int i)
  {
    if ((this.dimension == 1) && (i == 1))
    {
      b(AchievementList.D);
      this.world.kill(this);
      this.viewingCredits = true;
      this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4, 0.0F));
    }
    else
    {
      if ((this.dimension == 0) && (i == 1)) {
        b(AchievementList.C);
      } else {
        b(AchievementList.y);
      }
      PlayerTeleportEvent.TeleportCause cause = (this.dimension == 1) || (i == 1) ? PlayerTeleportEvent.TeleportCause.END_PORTAL : PlayerTeleportEvent.TeleportCause.NETHER_PORTAL;
      this.server.getPlayerList().changeDimension(this, i, cause);
      
      this.lastSentExp = -1;
      this.bM = -1.0F;
      this.bN = -1;
    }
  }
  
  public boolean a(EntityPlayer entityplayer)
  {
    return isSpectator() ? false : entityplayer.isSpectator() ? false : C() == this ? true : super.a(entityplayer);
  }
  
  private void a(TileEntity tileentity)
  {
    if (tileentity != null)
    {
      Packet packet = tileentity.getUpdatePacket();
      if (packet != null) {
        this.playerConnection.sendPacket(packet);
      }
    }
  }
  
  public void receive(Entity entity, int i)
  {
    super.receive(entity, i);
    this.activeContainer.b();
  }
  
  public EntityHuman.EnumBedResult a(BlockPosition blockposition)
  {
    EntityHuman.EnumBedResult entityhuman_enumbedresult = super.a(blockposition);
    if (entityhuman_enumbedresult == EntityHuman.EnumBedResult.OK)
    {
      PacketPlayOutBed packetplayoutbed = new PacketPlayOutBed(this, blockposition);
      
      u().getTracker().a(this, packetplayoutbed);
      this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
      this.playerConnection.sendPacket(packetplayoutbed);
    }
    return entityhuman_enumbedresult;
  }
  
  public void a(boolean flag, boolean flag1, boolean flag2)
  {
    if (!this.sleeping) {
      return;
    }
    if (isSleeping()) {
      u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 2));
    }
    super.a(flag, flag1, flag2);
    if (this.playerConnection != null) {
      this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
    }
  }
  
  public void mount(Entity entity)
  {
    Entity entity1 = this.vehicle;
    
    super.mount(entity);
    if (this.vehicle != entity1)
    {
      this.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this, this.vehicle));
      this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
    }
  }
  
  protected void a(double d0, boolean flag, Block block, BlockPosition blockposition) {}
  
  public void a(double d0, boolean flag)
  {
    int i = MathHelper.floor(this.locX);
    int j = MathHelper.floor(this.locY - 0.20000000298023224D);
    int k = MathHelper.floor(this.locZ);
    BlockPosition blockposition = new BlockPosition(i, j, k);
    Block block = this.world.getType(blockposition).getBlock();
    if (block.getMaterial() == Material.AIR)
    {
      Block block1 = this.world.getType(blockposition.down()).getBlock();
      if (((block1 instanceof BlockFence)) || ((block1 instanceof BlockCobbleWall)) || ((block1 instanceof BlockFenceGate)))
      {
        blockposition = blockposition.down();
        block = this.world.getType(blockposition).getBlock();
      }
    }
    super.a(d0, flag, block, blockposition);
  }
  
  public void openSign(TileEntitySign tileentitysign)
  {
    tileentitysign.a(this);
    this.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(tileentitysign.getPosition()));
  }
  
  public int nextContainerCounter()
  {
    this.containerCounter = (this.containerCounter % 100 + 1);
    return this.containerCounter;
  }
  
  public void openTileEntity(ITileEntityContainer itileentitycontainer)
  {
    Container container = CraftEventFactory.callInventoryOpenEvent(this, itileentitycontainer.createContainer(this.inventory, this));
    if (container == null) {
      return;
    }
    nextContainerCounter();
    this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, itileentitycontainer.getContainerName(), itileentitycontainer.getScoreboardDisplayName()));
    this.activeContainer = container;
    this.activeContainer.windowId = this.containerCounter;
    this.activeContainer.addSlotListener(this);
  }
  
  public void openContainer(IInventory iinventory)
  {
    boolean cancelled = false;
    if ((iinventory instanceof ITileInventory))
    {
      ITileInventory itileinventory = (ITileInventory)iinventory;
      cancelled = (itileinventory.r_()) && (!a(itileinventory.i())) && (!isSpectator());
    }
    Container container;
    if ((iinventory instanceof ITileEntityContainer)) {
      container = ((ITileEntityContainer)iinventory).createContainer(this.inventory, this);
    } else {
      container = new ContainerChest(this.inventory, iinventory, this);
    }
    Container container = CraftEventFactory.callInventoryOpenEvent(this, container, cancelled);
    if ((container == null) && (!cancelled))
    {
      iinventory.closeContainer(this);
      return;
    }
    if (this.activeContainer != this.defaultContainer) {
      closeInventory();
    }
    if ((iinventory instanceof ITileInventory))
    {
      ITileInventory itileinventory = (ITileInventory)iinventory;
      if ((itileinventory.r_()) && (!a(itileinventory.i())) && (!isSpectator()) && (container == null))
      {
        this.playerConnection.sendPacket(new PacketPlayOutChat(new ChatMessage("container.isLocked", new Object[] { iinventory.getScoreboardDisplayName() }), (byte)2));
        this.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("random.door_close", this.locX, this.locY, this.locZ, 1.0F, 1.0F));
        
        iinventory.closeContainer(this);
        return;
      }
    }
    nextContainerCounter();
    if ((iinventory instanceof ITileEntityContainer))
    {
      this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, ((ITileEntityContainer)iinventory).getContainerName(), iinventory.getScoreboardDisplayName(), iinventory.getSize()));
      this.activeContainer = container;
    }
    else
    {
      this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:container", iinventory.getScoreboardDisplayName(), iinventory.getSize()));
      this.activeContainer = container;
    }
    this.activeContainer.windowId = this.containerCounter;
    this.activeContainer.addSlotListener(this);
  }
  
  public void openTrade(IMerchant imerchant)
  {
    Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerMerchant(this.inventory, imerchant, this.world));
    if (container == null) {
      return;
    }
    nextContainerCounter();
    this.activeContainer = container;
    this.activeContainer.windowId = this.containerCounter;
    this.activeContainer.addSlotListener(this);
    InventoryMerchant inventorymerchant = ((ContainerMerchant)this.activeContainer).e();
    IChatBaseComponent ichatbasecomponent = imerchant.getScoreboardDisplayName();
    
    this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:villager", ichatbasecomponent, inventorymerchant.getSize()));
    MerchantRecipeList merchantrecipelist = imerchant.getOffers(this);
    if (merchantrecipelist != null)
    {
      PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
      
      packetdataserializer.writeInt(this.containerCounter);
      merchantrecipelist.a(packetdataserializer);
      this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", packetdataserializer));
    }
  }
  
  public void openHorseInventory(EntityHorse entityhorse, IInventory iinventory)
  {
    Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHorse(this.inventory, iinventory, entityhorse, this));
    if (container == null)
    {
      iinventory.closeContainer(this);
      return;
    }
    if (this.activeContainer != this.defaultContainer) {
      closeInventory();
    }
    nextContainerCounter();
    this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "EntityHorse", iinventory.getScoreboardDisplayName(), iinventory.getSize(), entityhorse.getId()));
    this.activeContainer = container;
    this.activeContainer.windowId = this.containerCounter;
    this.activeContainer.addSlotListener(this);
  }
  
  public void openBook(ItemStack itemstack)
  {
    Item item = itemstack.getItem();
    if (item == Items.WRITTEN_BOOK) {
      this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.buffer())));
    }
  }
  
  public void a(Container container, int i, ItemStack itemstack)
  {
    if ((!(container.getSlot(i) instanceof SlotResult)) && 
      (!this.g)) {
      this.playerConnection.sendPacket(new PacketPlayOutSetSlot(container.windowId, i, itemstack));
    }
  }
  
  public void updateInventory(Container container)
  {
    a(container, container.a());
  }
  
  public void a(Container container, List<ItemStack> list)
  {
    this.playerConnection.sendPacket(new PacketPlayOutWindowItems(container.windowId, list));
    this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
    if (EnumSet.of(InventoryType.CRAFTING, InventoryType.WORKBENCH).contains(container.getBukkitView().getType())) {
      this.playerConnection.sendPacket(new PacketPlayOutSetSlot(container.windowId, 0, container.getSlot(0).getItem()));
    }
  }
  
  public void setContainerData(Container container, int i, int j)
  {
    this.playerConnection.sendPacket(new PacketPlayOutWindowData(container.windowId, i, j));
  }
  
  public void setContainerData(Container container, IInventory iinventory)
  {
    for (int i = 0; i < iinventory.g(); i++) {
      this.playerConnection.sendPacket(new PacketPlayOutWindowData(container.windowId, i, iinventory.getProperty(i)));
    }
  }
  
  public void closeInventory()
  {
    CraftEventFactory.handleInventoryCloseEvent(this);
    this.playerConnection.sendPacket(new PacketPlayOutCloseWindow(this.activeContainer.windowId));
    p();
  }
  
  public void broadcastCarriedItem()
  {
    if (!this.g) {
      this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
    }
  }
  
  public void p()
  {
    this.activeContainer.b(this);
    this.activeContainer = this.defaultContainer;
  }
  
  public void a(float f, float f1, boolean flag, boolean flag1)
  {
    if (this.vehicle != null)
    {
      if ((f >= -1.0F) && (f <= 1.0F)) {
        this.aZ = f;
      }
      if ((f1 >= -1.0F) && (f1 <= 1.0F)) {
        this.ba = f1;
      }
      this.aY = flag;
      setSneaking(flag1);
    }
  }
  
  public void a(Statistic statistic, int i)
  {
    if (statistic != null)
    {
      this.bK.b(this, statistic, i);
      Iterator iterator = getScoreboard().getObjectivesForCriteria(statistic.k()).iterator();
      while (iterator.hasNext())
      {
        ScoreboardObjective scoreboardobjective = (ScoreboardObjective)iterator.next();
        
        getScoreboard().getPlayerScoreForObjective(getName(), scoreboardobjective).addScore(i);
      }
      if (this.bK.e()) {
        this.bK.a(this);
      }
    }
  }
  
  public void a(Statistic statistic)
  {
    if (statistic != null)
    {
      this.bK.setStatistic(this, statistic, 0);
      Iterator iterator = getScoreboard().getObjectivesForCriteria(statistic.k()).iterator();
      while (iterator.hasNext())
      {
        ScoreboardObjective scoreboardobjective = (ScoreboardObjective)iterator.next();
        
        getScoreboard().getPlayerScoreForObjective(getName(), scoreboardobjective).setScore(0);
      }
      if (this.bK.e()) {
        this.bK.a(this);
      }
    }
  }
  
  public void q()
  {
    if (this.passenger != null) {
      this.passenger.mount(this);
    }
    if (this.sleeping) {
      a(true, false, false);
    }
  }
  
  public void triggerHealthUpdate()
  {
    this.bM = -1.0E8F;
    this.lastSentExp = -1;
  }
  
  public void sendMessage(IChatBaseComponent[] ichatbasecomponent)
  {
    IChatBaseComponent[] arrayOfIChatBaseComponent;
    int i = (arrayOfIChatBaseComponent = ichatbasecomponent).length;
    for (int j = 0; j < i; j++)
    {
      IChatBaseComponent component = arrayOfIChatBaseComponent[j];
      sendMessage(component);
    }
  }
  
  public void b(IChatBaseComponent ichatbasecomponent)
  {
    this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent));
  }
  
  protected void s()
  {
    this.playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte)9));
    super.s();
  }
  
  public void a(ItemStack itemstack, int i)
  {
    super.a(itemstack, i);
    if ((itemstack != null) && (itemstack.getItem() != null) && (itemstack.getItem().e(itemstack) == EnumAnimation.EAT)) {
      u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 3));
    }
  }
  
  public void copyTo(EntityHuman entityhuman, boolean flag)
  {
    super.copyTo(entityhuman, flag);
    this.lastSentExp = -1;
    this.bM = -1.0F;
    this.bN = -1;
    this.removeQueue.addAll(((EntityPlayer)entityhuman).removeQueue);
  }
  
  protected void a(MobEffect mobeffect)
  {
    super.a(mobeffect);
    this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(getId(), mobeffect));
  }
  
  protected void a(MobEffect mobeffect, boolean flag)
  {
    super.a(mobeffect, flag);
    this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(getId(), mobeffect));
  }
  
  protected void b(MobEffect mobeffect)
  {
    super.b(mobeffect);
    this.playerConnection.sendPacket(new PacketPlayOutRemoveEntityEffect(getId(), mobeffect));
  }
  
  public void enderTeleportTo(double d0, double d1, double d2)
  {
    this.playerConnection.a(d0, d1, d2, this.yaw, this.pitch);
  }
  
  public void b(Entity entity)
  {
    u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 4));
  }
  
  public void c(Entity entity)
  {
    u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 5));
  }
  
  public void updateAbilities()
  {
    if (this.playerConnection != null)
    {
      this.playerConnection.sendPacket(new PacketPlayOutAbilities(this.abilities));
      B();
    }
  }
  
  public WorldServer u()
  {
    return (WorldServer)this.world;
  }
  
  public void a(WorldSettings.EnumGamemode worldsettings_enumgamemode)
  {
    getBukkitEntity().setGameMode(GameMode.getByValue(worldsettings_enumgamemode.getId()));
  }
  
  public boolean isSpectator()
  {
    return this.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.SPECTATOR;
  }
  
  public void sendMessage(IChatBaseComponent ichatbasecomponent)
  {
    this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent));
  }
  
  public boolean a(int i, String s)
  {
    if ("@".equals(s)) {
      return getBukkitEntity().hasPermission("minecraft.command.selector");
    }
    return true;
  }
  
  public String w()
  {
    String s = this.playerConnection.networkManager.getSocketAddress().toString();
    
    s = s.substring(s.indexOf("/") + 1);
    s = s.substring(0, s.indexOf(":"));
    return s;
  }
  
  public void a(PacketPlayInSettings packetplayinsettings)
  {
    this.locale = packetplayinsettings.a();
    this.bR = packetplayinsettings.c();
    this.bS = packetplayinsettings.d();
    getDataWatcher().watch(10, Byte.valueOf((byte)packetplayinsettings.e()));
  }
  
  public EntityHuman.EnumChatVisibility getChatFlags()
  {
    return this.bR;
  }
  
  public void setResourcePack(String s, String s1)
  {
    this.playerConnection.sendPacket(new PacketPlayOutResourcePackSend(s, s1));
  }
  
  public BlockPosition getChunkCoordinates()
  {
    return new BlockPosition(this.locX, this.locY + 0.5D, this.locZ);
  }
  
  public void resetIdleTimer()
  {
    this.bT = MinecraftServer.az();
  }
  
  public ServerStatisticManager getStatisticManager()
  {
    return this.bK;
  }
  
  public void d(Entity entity)
  {
    if ((entity instanceof EntityHuman)) {
      this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { entity.getId() }));
    } else {
      this.removeQueue.add(Integer.valueOf(entity.getId()));
    }
  }
  
  protected void B()
  {
    if (isSpectator())
    {
      bj();
      setInvisible(true);
    }
    else
    {
      super.B();
    }
    u().getTracker().a(this);
  }
  
  public Entity C()
  {
    return this.bU == null ? this : this.bU;
  }
  
  public void setSpectatorTarget(Entity entity)
  {
    Entity entity1 = C();
    
    this.bU = (entity == null ? this : entity);
    if (entity1 != this.bU)
    {
      this.playerConnection.sendPacket(new PacketPlayOutCamera(this.bU));
      enderTeleportTo(this.bU.locX, this.bU.locY, this.bU.locZ);
    }
  }
  
  public void attack(Entity entity)
  {
    if (this.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.SPECTATOR) {
      setSpectatorTarget(entity);
    } else {
      super.attack(entity);
    }
  }
  
  public long D()
  {
    return this.bT;
  }
  
  public IChatBaseComponent getPlayerListName()
  {
    return this.listName;
  }
  
  public long timeOffset = 0L;
  public boolean relativeTime = true;
  
  public long getPlayerTime()
  {
    if (this.relativeTime) {
      return this.world.getDayTime() + this.timeOffset;
    }
    return this.world.getDayTime() - this.world.getDayTime() % 24000L + this.timeOffset;
  }
  
  public WeatherType weather = null;
  private float pluginRainPosition;
  private float pluginRainPositionPrevious;
  
  public WeatherType getPlayerWeather()
  {
    return this.weather;
  }
  
  public void setPlayerWeather(WeatherType type, boolean plugin)
  {
    if ((!plugin) && (this.weather != null)) {
      return;
    }
    if (plugin) {
      this.weather = type;
    }
    if (type == WeatherType.DOWNFALL) {
      this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(2, 0.0F));
    } else {
      this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(1, 0.0F));
    }
  }
  
  public void updateWeather(float oldRain, float newRain, float oldThunder, float newThunder)
  {
    if (this.weather == null)
    {
      if (oldRain != newRain) {
        this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(7, newRain));
      }
    }
    else if (this.pluginRainPositionPrevious != this.pluginRainPosition) {
      this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(7, this.pluginRainPosition));
    }
    if (oldThunder != newThunder) {
      if ((this.weather == WeatherType.DOWNFALL) || (this.weather == null)) {
        this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(8, newThunder));
      } else {
        this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(8, 0.0F));
      }
    }
  }
  
  public void tickWeather()
  {
    if (this.weather == null) {
      return;
    }
    this.pluginRainPositionPrevious = this.pluginRainPosition;
    if (this.weather == WeatherType.DOWNFALL) {
      this.pluginRainPosition = ((float)(this.pluginRainPosition + 0.01D));
    } else {
      this.pluginRainPosition = ((float)(this.pluginRainPosition - 0.01D));
    }
    this.pluginRainPosition = MathHelper.a(this.pluginRainPosition, 0.0F, 1.0F);
  }
  
  public void resetPlayerWeather()
  {
    this.weather = null;
    setPlayerWeather(this.world.getWorldData().hasStorm() ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
  }
  
  public String toString()
  {
    return super.toString() + "(" + getName() + " at " + this.locX + "," + this.locY + "," + this.locZ + ")";
  }
  
  public void reset()
  {
    float exp = 0.0F;
    boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");
    if ((this.keepLevel) || (keepInventory))
    {
      exp = this.exp;
      this.newTotalExp = this.expTotal;
      this.newLevel = this.expLevel;
    }
    setHealth(getMaxHealth());
    this.fireTicks = 0;
    this.fallDistance = 0.0F;
    this.foodData = new FoodMetaData(this);
    this.expLevel = this.newLevel;
    this.expTotal = this.newTotalExp;
    this.exp = 0.0F;
    this.deathTicks = 0;
    removeAllEffects();
    this.updateEffects = true;
    this.activeContainer = this.defaultContainer;
    this.killer = null;
    this.lastDamager = null;
    this.combatTracker = new CombatTracker(this);
    this.lastSentExp = -1;
    if ((this.keepLevel) || (keepInventory)) {
      this.exp = exp;
    } else {
      giveExp(this.newExp);
    }
    this.keepLevel = false;
  }
  
  public CraftPlayer getBukkitEntity()
  {
    return (CraftPlayer)super.getBukkitEntity();
  }
}
