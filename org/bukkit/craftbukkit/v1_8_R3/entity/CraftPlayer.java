package org.bukkit.craftbukkit.v1_8_R3.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.AttributeMapServer;
import net.minecraft.server.v1_8_R3.AttributeModifiable;
import net.minecraft.server.v1_8_R3.AttributeRanged;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.DedicatedPlayerList;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EntityTrackerEntry;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.FoodMetaData;
import net.minecraft.server.v1_8_R3.IAttribute;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IPlayerFileData;
import net.minecraft.server.v1_8_R3.IntHashMap;
import net.minecraft.server.v1_8_R3.MapIcon;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateAttributes;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateHealth;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PlayerAbilities;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.ServerStatisticManager;
import net.minecraft.server.v1_8_R3.WhiteList;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.Achievement;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Effect;
import org.bukkit.Effect.Type;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.Statistic.Type;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.v1_8_R3.CraftEffect;
import org.bukkit.craftbukkit.v1_8_R3.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_8_R3.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_8_R3.map.CraftMapView;
import org.bukkit.craftbukkit.v1_8_R3.map.RenderData;
import org.bukkit.craftbukkit.v1_8_R3.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;
import org.spigotmc.AsyncCatcher;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer
  extends CraftHumanEntity
  implements Player
{
  private long firstPlayed = 0L;
  private long lastPlayed = 0L;
  private boolean hasPlayedBefore = false;
  private final ConversationTracker conversationTracker = new ConversationTracker();
  private final Set<String> channels = new HashSet();
  private final Set<UUID> hiddenPlayers = new HashSet();
  private int hash = 0;
  private double health = 20.0D;
  private boolean scaledHealth = false;
  private double healthScale = 20.0D;
  
  public CraftPlayer(CraftServer server, EntityPlayer entity)
  {
    super(server, entity);
    
    this.firstPlayed = System.currentTimeMillis();
  }
  
  public GameProfile getProfile()
  {
    return getHandle().getProfile();
  }
  
  public boolean isOp()
  {
    return this.server.getHandle().isOp(getProfile());
  }
  
  public void setOp(boolean value)
  {
    if (value == isOp()) {
      return;
    }
    if (value) {
      this.server.getHandle().addOp(getProfile());
    } else {
      this.server.getHandle().removeOp(getProfile());
    }
    this.perm.recalculatePermissions();
  }
  
  public boolean isOnline()
  {
    for (Object obj : this.server.getHandle().players)
    {
      EntityPlayer player = (EntityPlayer)obj;
      if (player.getName().equalsIgnoreCase(getName())) {
        return true;
      }
    }
    return false;
  }
  
  public InetSocketAddress getAddress()
  {
    if (getHandle().playerConnection == null) {
      return null;
    }
    SocketAddress addr = getHandle().playerConnection.networkManager.getSocketAddress();
    if ((addr instanceof InetSocketAddress)) {
      return (InetSocketAddress)addr;
    }
    return null;
  }
  
  public double getEyeHeight()
  {
    return getEyeHeight(false);
  }
  
  public double getEyeHeight(boolean ignoreSneaking)
  {
    if (ignoreSneaking) {
      return 1.62D;
    }
    if (isSneaking()) {
      return 1.54D;
    }
    return 1.62D;
  }
  
  public void sendRawMessage(String message)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    IChatBaseComponent[] arrayOfIChatBaseComponent;
    int i = (arrayOfIChatBaseComponent = CraftChatMessage.fromString(message)).length;
    for (int j = 0; j < i; j++)
    {
      IChatBaseComponent component = arrayOfIChatBaseComponent[j];
      getHandle().playerConnection.sendPacket(new PacketPlayOutChat(component));
    }
  }
  
  public void sendMessage(String message)
  {
    if (!this.conversationTracker.isConversingModaly()) {
      sendRawMessage(message);
    }
  }
  
  public void sendMessage(String[] messages)
  {
    String[] arrayOfString;
    int i = (arrayOfString = messages).length;
    for (int j = 0; j < i; j++)
    {
      String message = arrayOfString[j];
      sendMessage(message);
    }
  }
  
  public String getDisplayName()
  {
    return getHandle().displayName;
  }
  
  public void setDisplayName(String name)
  {
    getHandle().displayName = (name == null ? getName() : name);
  }
  
  public String getPlayerListName()
  {
    return getHandle().listName == null ? getName() : CraftChatMessage.fromComponent(getHandle().listName);
  }
  
  public void setPlayerListName(String name)
  {
    if (name == null) {
      name = getName();
    }
    getHandle().listName = (name.equals(getName()) ? null : CraftChatMessage.fromString(name)[0]);
    for (EntityPlayer player : this.server.getHandle().players) {
      if (player.getBukkitEntity().canSee(this)) {
        player.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, new EntityPlayer[] { getHandle() }));
      }
    }
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof OfflinePlayer)) {
      return false;
    }
    OfflinePlayer other = (OfflinePlayer)obj;
    if ((getUniqueId() == null) || (other.getUniqueId() == null)) {
      return false;
    }
    boolean uuidEquals = getUniqueId().equals(other.getUniqueId());
    boolean idEquals = true;
    if ((other instanceof CraftPlayer)) {
      idEquals = getEntityId() == ((CraftPlayer)other).getEntityId();
    }
    return (uuidEquals) && (idEquals);
  }
  
  public void kickPlayer(String message)
  {
    AsyncCatcher.catchOp("player kick");
    if (getHandle().playerConnection == null) {
      return;
    }
    getHandle().playerConnection.disconnect(message == null ? "" : message);
  }
  
  public void setCompassTarget(Location loc)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnPosition(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
  }
  
  public Location getCompassTarget()
  {
    return getHandle().compassTarget;
  }
  
  public void chat(String msg)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    getHandle().playerConnection.chat(msg, false);
  }
  
  public boolean performCommand(String command)
  {
    return this.server.dispatchCommand(this, command);
  }
  
  public void playNote(Location loc, byte instrument, byte note)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    String instrumentName = null;
    switch (instrument)
    {
    case 0: 
      instrumentName = "harp";
      break;
    case 1: 
      instrumentName = "bd";
      break;
    case 2: 
      instrumentName = "snare";
      break;
    case 3: 
      instrumentName = "hat";
      break;
    case 4: 
      instrumentName = "bassattack";
    }
    float f = (float)Math.pow(2.0D, (note - 12.0D) / 12.0D);
    getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("note." + instrumentName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0F, f));
  }
  
  public void playNote(Location loc, Instrument instrument, Note note)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    String instrumentName = null;
    switch (instrument.ordinal())
    {
    case 0: 
      instrumentName = "harp";
      break;
    case 1: 
      instrumentName = "bd";
      break;
    case 2: 
      instrumentName = "snare";
      break;
    case 3: 
      instrumentName = "hat";
      break;
    case 4: 
      instrumentName = "bassattack";
    }
    float f = (float)Math.pow(2.0D, (note.getId() - 12.0D) / 12.0D);
    getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("note." + instrumentName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0F, f));
  }
  
  public void playSound(Location loc, Sound sound, float volume, float pitch)
  {
    if (sound == null) {
      return;
    }
    playSound(loc, CraftSound.getSound(sound), volume, pitch);
  }
  
  public void playSound(Location loc, String sound, float volume, float pitch)
  {
    if ((loc == null) || (sound == null) || (getHandle().playerConnection == null)) {
      return;
    }
    double x = loc.getBlockX() + 0.5D;
    double y = loc.getBlockY() + 0.5D;
    double z = loc.getBlockZ() + 0.5D;
    
    PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(sound, x, y, z, volume, pitch);
    getHandle().playerConnection.sendPacket(packet);
  }
  
  public void playEffect(Location loc, Effect effect, int data)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    spigot().playEffect(loc, effect, data, 0, 0.0F, 0.0F, 0.0F, 1.0F, 1, 64);
  }
  
  public <T> void playEffect(Location loc, Effect effect, T data)
  {
    if (data != null) {
      Validate.isTrue(data.getClass().equals(effect.getData()), "Wrong kind of data for this effect!");
    } else {
      Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
    }
    int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
    playEffect(loc, effect, datavalue);
  }
  
  public void sendBlockChange(Location loc, Material material, byte data)
  {
    sendBlockChange(loc, material.getId(), data);
  }
  
  public void sendBlockChange(Location loc, int material, byte data)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld)loc.getWorld()).getHandle(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    
    packet.block = CraftMagicNumbers.getBlock(material).fromLegacyData(data);
    getHandle().playerConnection.sendPacket(packet);
  }
  
  public void sendSignChange(Location loc, String[] lines)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    if (lines == null) {
      lines = new String[4];
    }
    Validate.notNull(loc, "Location can not be null");
    if (lines.length < 4) {
      throw new IllegalArgumentException("Must have at least 4 lines");
    }
    IChatBaseComponent[] components = CraftSign.sanitizeLines(lines);
    
    getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateSign(getHandle().world, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), components));
  }
  
  public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data)
  {
    if (getHandle().playerConnection == null) {
      return false;
    }
    throw new NotImplementedException("Chunk changes do not yet work");
  }
  
  public void sendMap(MapView map)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    RenderData data = ((CraftMapView)map).render(this);
    Collection<MapIcon> icons = new ArrayList();
    for (MapCursor cursor : data.cursors) {
      if (cursor.isVisible()) {
        icons.add(new MapIcon(cursor.getRawType(), cursor.getX(), cursor.getY(), cursor.getDirection()));
      }
    }
    PacketPlayOutMap packet = new PacketPlayOutMap(map.getId(), map.getScale().getValue(), icons, data.buffer, 0, 0, 0, 0);
    getHandle().playerConnection.sendPacket(packet);
  }
  
  public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause)
  {
    EntityPlayer entity = getHandle();
    if ((getHealth() == 0.0D) || (entity.dead)) {
      return false;
    }
    if ((entity.playerConnection == null) || (entity.playerConnection.isDisconnected())) {
      return false;
    }
    if (entity.passenger != null) {
      return false;
    }
    Location from = getLocation();
    
    Location to = location;
    
    PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause);
    this.server.getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return false;
    }
    entity.mount(null);
    
    from = event.getFrom();
    
    to = event.getTo();
    
    WorldServer fromWorld = ((CraftWorld)from.getWorld()).getHandle();
    WorldServer toWorld = ((CraftWorld)to.getWorld()).getHandle();
    if (getHandle().activeContainer != getHandle().defaultContainer) {
      getHandle().closeInventory();
    }
    if (fromWorld == toWorld) {
      entity.playerConnection.teleport(to);
    } else {
      this.server.getHandle().moveToWorld(entity, toWorld.dimension, true, to, true);
    }
    return true;
  }
  
  public void setSneaking(boolean sneak)
  {
    getHandle().setSneaking(sneak);
  }
  
  public boolean isSneaking()
  {
    return getHandle().isSneaking();
  }
  
  public boolean isSprinting()
  {
    return getHandle().isSprinting();
  }
  
  public void setSprinting(boolean sprinting)
  {
    getHandle().setSprinting(sprinting);
  }
  
  public void loadData()
  {
    this.server.getHandle().playerFileData.load(getHandle());
  }
  
  public void saveData()
  {
    this.server.getHandle().playerFileData.save(getHandle());
  }
  
  @Deprecated
  public void updateInventory()
  {
    getHandle().updateInventory(getHandle().activeContainer);
  }
  
  public void setSleepingIgnored(boolean isSleeping)
  {
    getHandle().fauxSleeping = isSleeping;
    ((CraftWorld)getWorld()).getHandle().checkSleepStatus();
  }
  
  public boolean isSleepingIgnored()
  {
    return getHandle().fauxSleeping;
  }
  
  public void awardAchievement(Achievement achievement)
  {
    Validate.notNull(achievement, "Achievement cannot be null");
    if ((achievement.hasParent()) && (!hasAchievement(achievement.getParent()))) {
      awardAchievement(achievement.getParent());
    }
    getHandle().getStatisticManager().setStatistic(getHandle(), CraftStatistic.getNMSAchievement(achievement), 1);
    getHandle().getStatisticManager().updateStatistics(getHandle());
  }
  
  public void removeAchievement(Achievement achievement)
  {
    Validate.notNull(achievement, "Achievement cannot be null");
    Achievement[] arrayOfAchievement;
    int i = (arrayOfAchievement = Achievement.values()).length;
    for (int j = 0; j < i; j++)
    {
      Achievement achieve = arrayOfAchievement[j];
      if ((achieve.getParent() == achievement) && (hasAchievement(achieve))) {
        removeAchievement(achieve);
      }
    }
    getHandle().getStatisticManager().setStatistic(getHandle(), CraftStatistic.getNMSAchievement(achievement), 0);
  }
  
  public boolean hasAchievement(Achievement achievement)
  {
    Validate.notNull(achievement, "Achievement cannot be null");
    return getHandle().getStatisticManager().hasAchievement(CraftStatistic.getNMSAchievement(achievement));
  }
  
  public void incrementStatistic(org.bukkit.Statistic statistic)
  {
    incrementStatistic(statistic, 1);
  }
  
  public void decrementStatistic(org.bukkit.Statistic statistic)
  {
    decrementStatistic(statistic, 1);
  }
  
  public int getStatistic(org.bukkit.Statistic statistic)
  {
    Validate.notNull(statistic, "Statistic cannot be null");
    Validate.isTrue(statistic.getType() == Statistic.Type.UNTYPED, "Must supply additional paramater for this statistic");
    return getHandle().getStatisticManager().getStatisticValue(CraftStatistic.getNMSStatistic(statistic));
  }
  
  public void incrementStatistic(org.bukkit.Statistic statistic, int amount)
  {
    Validate.isTrue(amount > 0, "Amount must be greater than 0");
    setStatistic(statistic, getStatistic(statistic) + amount);
  }
  
  public void decrementStatistic(org.bukkit.Statistic statistic, int amount)
  {
    Validate.isTrue(amount > 0, "Amount must be greater than 0");
    setStatistic(statistic, getStatistic(statistic) - amount);
  }
  
  public void setStatistic(org.bukkit.Statistic statistic, int newValue)
  {
    Validate.notNull(statistic, "Statistic cannot be null");
    Validate.isTrue(statistic.getType() == Statistic.Type.UNTYPED, "Must supply additional paramater for this statistic");
    Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
    net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
    getHandle().getStatisticManager().setStatistic(getHandle(), nmsStatistic, newValue);
  }
  
  public void incrementStatistic(org.bukkit.Statistic statistic, Material material)
  {
    incrementStatistic(statistic, material, 1);
  }
  
  public void decrementStatistic(org.bukkit.Statistic statistic, Material material)
  {
    decrementStatistic(statistic, material, 1);
  }
  
  public int getStatistic(org.bukkit.Statistic statistic, Material material)
  {
    Validate.notNull(statistic, "Statistic cannot be null");
    Validate.notNull(material, "Material cannot be null");
    Validate.isTrue((statistic.getType() == Statistic.Type.BLOCK) || (statistic.getType() == Statistic.Type.ITEM), "This statistic does not take a Material parameter");
    net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
    Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
    return getHandle().getStatisticManager().getStatisticValue(nmsStatistic);
  }
  
  public void incrementStatistic(org.bukkit.Statistic statistic, Material material, int amount)
  {
    Validate.isTrue(amount > 0, "Amount must be greater than 0");
    setStatistic(statistic, material, getStatistic(statistic, material) + amount);
  }
  
  public void decrementStatistic(org.bukkit.Statistic statistic, Material material, int amount)
  {
    Validate.isTrue(amount > 0, "Amount must be greater than 0");
    setStatistic(statistic, material, getStatistic(statistic, material) - amount);
  }
  
  public void setStatistic(org.bukkit.Statistic statistic, Material material, int newValue)
  {
    Validate.notNull(statistic, "Statistic cannot be null");
    Validate.notNull(material, "Material cannot be null");
    Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
    Validate.isTrue((statistic.getType() == Statistic.Type.BLOCK) || (statistic.getType() == Statistic.Type.ITEM), "This statistic does not take a Material parameter");
    net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
    Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
    getHandle().getStatisticManager().setStatistic(getHandle(), nmsStatistic, newValue);
  }
  
  public void incrementStatistic(org.bukkit.Statistic statistic, EntityType entityType)
  {
    incrementStatistic(statistic, entityType, 1);
  }
  
  public void decrementStatistic(org.bukkit.Statistic statistic, EntityType entityType)
  {
    decrementStatistic(statistic, entityType, 1);
  }
  
  public int getStatistic(org.bukkit.Statistic statistic, EntityType entityType)
  {
    Validate.notNull(statistic, "Statistic cannot be null");
    Validate.notNull(entityType, "EntityType cannot be null");
    Validate.isTrue(statistic.getType() == Statistic.Type.ENTITY, "This statistic does not take an EntityType parameter");
    net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
    Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
    return getHandle().getStatisticManager().getStatisticValue(nmsStatistic);
  }
  
  public void incrementStatistic(org.bukkit.Statistic statistic, EntityType entityType, int amount)
  {
    Validate.isTrue(amount > 0, "Amount must be greater than 0");
    setStatistic(statistic, entityType, getStatistic(statistic, entityType) + amount);
  }
  
  public void decrementStatistic(org.bukkit.Statistic statistic, EntityType entityType, int amount)
  {
    Validate.isTrue(amount > 0, "Amount must be greater than 0");
    setStatistic(statistic, entityType, getStatistic(statistic, entityType) - amount);
  }
  
  public void setStatistic(org.bukkit.Statistic statistic, EntityType entityType, int newValue)
  {
    Validate.notNull(statistic, "Statistic cannot be null");
    Validate.notNull(entityType, "EntityType cannot be null");
    Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
    Validate.isTrue(statistic.getType() == Statistic.Type.ENTITY, "This statistic does not take an EntityType parameter");
    net.minecraft.server.v1_8_R3.Statistic nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
    Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
    getHandle().getStatisticManager().setStatistic(getHandle(), nmsStatistic, newValue);
  }
  
  public void setPlayerTime(long time, boolean relative)
  {
    getHandle().timeOffset = time;
    getHandle().relativeTime = relative;
  }
  
  public long getPlayerTimeOffset()
  {
    return getHandle().timeOffset;
  }
  
  public long getPlayerTime()
  {
    return getHandle().getPlayerTime();
  }
  
  public boolean isPlayerTimeRelative()
  {
    return getHandle().relativeTime;
  }
  
  public void resetPlayerTime()
  {
    setPlayerTime(0L, true);
  }
  
  public void setPlayerWeather(WeatherType type)
  {
    getHandle().setPlayerWeather(type, true);
  }
  
  public WeatherType getPlayerWeather()
  {
    return getHandle().getPlayerWeather();
  }
  
  public void resetPlayerWeather()
  {
    getHandle().resetPlayerWeather();
  }
  
  public boolean isBanned()
  {
    return this.server.getBanList(BanList.Type.NAME).isBanned(getName());
  }
  
  public void setBanned(boolean value)
  {
    if (value) {
      this.server.getBanList(BanList.Type.NAME).addBan(getName(), null, null, null);
    } else {
      this.server.getBanList(BanList.Type.NAME).pardon(getName());
    }
  }
  
  public boolean isWhitelisted()
  {
    return this.server.getHandle().getWhitelist().isWhitelisted(getProfile());
  }
  
  public void setWhitelisted(boolean value)
  {
    if (value) {
      this.server.getHandle().addWhitelist(getProfile());
    } else {
      this.server.getHandle().removeWhitelist(getProfile());
    }
  }
  
  public void setGameMode(GameMode mode)
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    if (mode == null) {
      throw new IllegalArgumentException("Mode cannot be null");
    }
    if (mode != getGameMode())
    {
      PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(this, mode);
      this.server.getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        return;
      }
      getHandle().setSpectatorTarget(getHandle());
      getHandle().playerInteractManager.setGameMode(WorldSettings.EnumGamemode.getById(mode.getValue()));
      getHandle().fallDistance = 0.0F;
      getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, mode.getValue()));
    }
  }
  
  public GameMode getGameMode()
  {
    return GameMode.getByValue(getHandle().playerInteractManager.getGameMode().getId());
  }
  
  public void giveExp(int exp)
  {
    getHandle().giveExp(exp);
  }
  
  public void giveExpLevels(int levels)
  {
    getHandle().levelDown(levels);
  }
  
  public float getExp()
  {
    return getHandle().exp;
  }
  
  public void setExp(float exp)
  {
    getHandle().exp = exp;
    getHandle().lastSentExp = -1;
  }
  
  public int getLevel()
  {
    return getHandle().expLevel;
  }
  
  public void setLevel(int level)
  {
    getHandle().expLevel = level;
    getHandle().lastSentExp = -1;
  }
  
  public int getTotalExperience()
  {
    return getHandle().expTotal;
  }
  
  public void setTotalExperience(int exp)
  {
    getHandle().expTotal = exp;
  }
  
  public float getExhaustion()
  {
    return getHandle().getFoodData().exhaustionLevel;
  }
  
  public void setExhaustion(float value)
  {
    getHandle().getFoodData().exhaustionLevel = value;
  }
  
  public float getSaturation()
  {
    return getHandle().getFoodData().saturationLevel;
  }
  
  public void setSaturation(float value)
  {
    getHandle().getFoodData().saturationLevel = value;
  }
  
  public int getFoodLevel()
  {
    return getHandle().getFoodData().foodLevel;
  }
  
  public void setFoodLevel(int value)
  {
    getHandle().getFoodData().foodLevel = value;
  }
  
  public Location getBedSpawnLocation()
  {
    World world = getServer().getWorld(getHandle().spawnWorld);
    BlockPosition bed = getHandle().getBed();
    if ((world != null) && (bed != null))
    {
      bed = EntityHuman.getBed(((CraftWorld)world).getHandle(), bed, getHandle().isRespawnForced());
      if (bed != null) {
        return new Location(world, bed.getX(), bed.getY(), bed.getZ());
      }
    }
    return null;
  }
  
  public void setBedSpawnLocation(Location location)
  {
    setBedSpawnLocation(location, false);
  }
  
  public void setBedSpawnLocation(Location location, boolean override)
  {
    if (location == null)
    {
      getHandle().setRespawnPosition(null, override);
    }
    else
    {
      getHandle().setRespawnPosition(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
      getHandle().spawnWorld = location.getWorld().getName();
    }
  }
  
  public void hidePlayer(Player player)
  {
    Validate.notNull(player, "hidden player cannot be null");
    if (getHandle().playerConnection == null) {
      return;
    }
    if (equals(player)) {
      return;
    }
    if (this.hiddenPlayers.contains(player.getUniqueId())) {
      return;
    }
    this.hiddenPlayers.add(player.getUniqueId());
    
    EntityTracker tracker = ((WorldServer)this.entity.world).tracker;
    EntityPlayer other = ((CraftPlayer)player).getHandle();
    EntityTrackerEntry entry = (EntityTrackerEntry)tracker.trackedEntities.get(other.getId());
    if (entry != null) {
      entry.clear(getHandle());
    }
    getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { other }));
  }
  
  public void showPlayer(Player player)
  {
    Validate.notNull(player, "shown player cannot be null");
    if (getHandle().playerConnection == null) {
      return;
    }
    if (equals(player)) {
      return;
    }
    if (!this.hiddenPlayers.contains(player.getUniqueId())) {
      return;
    }
    this.hiddenPlayers.remove(player.getUniqueId());
    
    EntityTracker tracker = ((WorldServer)this.entity.world).tracker;
    EntityPlayer other = ((CraftPlayer)player).getHandle();
    
    getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { other }));
    
    EntityTrackerEntry entry = (EntityTrackerEntry)tracker.trackedEntities.get(other.getId());
    if ((entry != null) && (!entry.trackedPlayers.contains(getHandle()))) {
      entry.updatePlayer(getHandle());
    }
  }
  
  public void removeDisconnectingPlayer(Player player)
  {
    this.hiddenPlayers.remove(player.getUniqueId());
  }
  
  public boolean canSee(Player player)
  {
    return !this.hiddenPlayers.contains(player.getUniqueId());
  }
  
  public Map<String, Object> serialize()
  {
    Map<String, Object> result = new LinkedHashMap();
    
    result.put("name", getName());
    
    return result;
  }
  
  public Player getPlayer()
  {
    return this;
  }
  
  public EntityPlayer getHandle()
  {
    return (EntityPlayer)this.entity;
  }
  
  public void setHandle(EntityPlayer entity)
  {
    super.setHandle(entity);
  }
  
  public String toString()
  {
    return "CraftPlayer{name=" + getName() + '}';
  }
  
  public int hashCode()
  {
    if ((this.hash == 0) || (this.hash == 485)) {
      this.hash = ('ǥ' + (getUniqueId() != null ? getUniqueId().hashCode() : 0));
    }
    return this.hash;
  }
  
  public long getFirstPlayed()
  {
    return this.firstPlayed;
  }
  
  public long getLastPlayed()
  {
    return this.lastPlayed;
  }
  
  public boolean hasPlayedBefore()
  {
    return this.hasPlayedBefore;
  }
  
  public void setFirstPlayed(long firstPlayed)
  {
    this.firstPlayed = firstPlayed;
  }
  
  public void readExtraData(NBTTagCompound nbttagcompound)
  {
    this.hasPlayedBefore = true;
    if (nbttagcompound.hasKey("bukkit"))
    {
      NBTTagCompound data = nbttagcompound.getCompound("bukkit");
      if (data.hasKey("firstPlayed"))
      {
        this.firstPlayed = data.getLong("firstPlayed");
        this.lastPlayed = data.getLong("lastPlayed");
      }
      if (data.hasKey("newExp"))
      {
        EntityPlayer handle = getHandle();
        handle.newExp = data.getInt("newExp");
        handle.newTotalExp = data.getInt("newTotalExp");
        handle.newLevel = data.getInt("newLevel");
        handle.expToDrop = data.getInt("expToDrop");
        handle.keepLevel = data.getBoolean("keepLevel");
      }
    }
  }
  
  public void setExtraData(NBTTagCompound nbttagcompound)
  {
    if (!nbttagcompound.hasKey("bukkit")) {
      nbttagcompound.set("bukkit", new NBTTagCompound());
    }
    NBTTagCompound data = nbttagcompound.getCompound("bukkit");
    EntityPlayer handle = getHandle();
    data.setInt("newExp", handle.newExp);
    data.setInt("newTotalExp", handle.newTotalExp);
    data.setInt("newLevel", handle.newLevel);
    data.setInt("expToDrop", handle.expToDrop);
    data.setBoolean("keepLevel", handle.keepLevel);
    data.setLong("firstPlayed", getFirstPlayed());
    data.setLong("lastPlayed", System.currentTimeMillis());
    data.setString("lastKnownName", handle.getName());
  }
  
  public boolean beginConversation(Conversation conversation)
  {
    return this.conversationTracker.beginConversation(conversation);
  }
  
  public void abandonConversation(Conversation conversation)
  {
    this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
  }
  
  public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details)
  {
    this.conversationTracker.abandonConversation(conversation, details);
  }
  
  public void acceptConversationInput(String input)
  {
    this.conversationTracker.acceptConversationInput(input);
  }
  
  public boolean isConversing()
  {
    return this.conversationTracker.isConversing();
  }
  
  public void sendPluginMessage(Plugin source, String channel, byte[] message)
  {
    StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
    if (getHandle().playerConnection == null) {
      return;
    }
    if (this.channels.contains(channel))
    {
      PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(channel, new PacketDataSerializer(Unpooled.wrappedBuffer(message)));
      getHandle().playerConnection.sendPacket(packet);
    }
  }
  
  public void setTexturePack(String url)
  {
    setResourcePack(url);
  }
  
  public void setResourcePack(String url)
  {
    Validate.notNull(url, "Resource pack URL cannot be null");
    
    getHandle().setResourcePack(url, "null");
  }
  
  public void addChannel(String channel)
  {
    Preconditions.checkState(this.channels.size() < 128, "Too many channels registered");
    if (this.channels.add(channel)) {
      this.server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
    }
  }
  
  public void removeChannel(String channel)
  {
    if (this.channels.remove(channel)) {
      this.server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
    }
  }
  
  public Set<String> getListeningPluginChannels()
  {
    return ImmutableSet.copyOf(this.channels);
  }
  
  public void sendSupportedChannels()
  {
    if (getHandle().playerConnection == null) {
      return;
    }
    Set<String> listening = this.server.getMessenger().getIncomingChannels();
    if (!listening.isEmpty())
    {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      for (String channel : listening) {
        try
        {
          stream.write(channel.getBytes("UTF8"));
          stream.write(0);
        }
        catch (IOException ex)
        {
          Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + getName(), ex);
        }
      }
      getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer(stream.toByteArray()))));
    }
  }
  
  public EntityType getType()
  {
    return EntityType.PLAYER;
  }
  
  public void setMetadata(String metadataKey, MetadataValue newMetadataValue)
  {
    this.server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
  }
  
  public List<MetadataValue> getMetadata(String metadataKey)
  {
    return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
  }
  
  public boolean hasMetadata(String metadataKey)
  {
    return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
  }
  
  public void removeMetadata(String metadataKey, Plugin owningPlugin)
  {
    this.server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
  }
  
  public boolean setWindowProperty(InventoryView.Property prop, int value)
  {
    Container container = getHandle().activeContainer;
    if (container.getBukkitView().getType() != prop.getType()) {
      return false;
    }
    getHandle().setContainerData(container, prop.getId(), value);
    return true;
  }
  
  public void disconnect(String reason)
  {
    this.conversationTracker.abandonAllConversations();
    this.perm.clearPermissions();
  }
  
  public boolean isFlying()
  {
    return getHandle().abilities.isFlying;
  }
  
  public void setFlying(boolean value)
  {
    if ((!getAllowFlight()) && (value)) {
      throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
    }
    getHandle().abilities.isFlying = value;
    getHandle().updateAbilities();
  }
  
  public boolean getAllowFlight()
  {
    return getHandle().abilities.canFly;
  }
  
  public void setAllowFlight(boolean value)
  {
    if ((isFlying()) && (!value)) {
      getHandle().abilities.isFlying = false;
    }
    getHandle().abilities.canFly = value;
    getHandle().updateAbilities();
  }
  
  public int getNoDamageTicks()
  {
    if (getHandle().invulnerableTicks > 0) {
      return Math.max(getHandle().invulnerableTicks, getHandle().noDamageTicks);
    }
    return getHandle().noDamageTicks;
  }
  
  public void setFlySpeed(float value)
  {
    validateSpeed(value);
    EntityPlayer player = getHandle();
    player.abilities.flySpeed = (Math.max(value, 1.0E-4F) / 2.0F);
    player.updateAbilities();
  }
  
  public void setWalkSpeed(float value)
  {
    validateSpeed(value);
    EntityPlayer player = getHandle();
    player.abilities.walkSpeed = (Math.max(value, 1.0E-4F) / 2.0F);
    player.updateAbilities();
  }
  
  public float getFlySpeed()
  {
    return getHandle().abilities.flySpeed * 2.0F;
  }
  
  public float getWalkSpeed()
  {
    return getHandle().abilities.walkSpeed * 2.0F;
  }
  
  private void validateSpeed(float value)
  {
    if (value < 0.0F)
    {
      if (value < -1.0F) {
        throw new IllegalArgumentException(value + " is too low");
      }
    }
    else if (value > 1.0F) {
      throw new IllegalArgumentException(value + " is too high");
    }
  }
  
  public void setMaxHealth(double amount)
  {
    super.setMaxHealth(amount);
    this.health = Math.min(this.health, this.health);
    getHandle().triggerHealthUpdate();
  }
  
  public void resetMaxHealth()
  {
    super.resetMaxHealth();
    getHandle().triggerHealthUpdate();
  }
  
  public CraftScoreboard getScoreboard()
  {
    return this.server.getScoreboardManager().getPlayerBoard(this);
  }
  
  public void setScoreboard(Scoreboard scoreboard)
  {
    Validate.notNull(scoreboard, "Scoreboard cannot be null");
    PlayerConnection playerConnection = getHandle().playerConnection;
    if (playerConnection == null) {
      throw new IllegalStateException("Cannot set scoreboard yet");
    }
    playerConnection.isDisconnected();
    
    this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
  }
  
  public void setHealthScale(double value)
  {
    Validate.isTrue((float)value > 0.0F, "Must be greater than 0");
    this.healthScale = value;
    this.scaledHealth = true;
    updateScaledHealth();
  }
  
  public double getHealthScale()
  {
    return this.healthScale;
  }
  
  public void setHealthScaled(boolean scale)
  {
    if (this.scaledHealth != (this.scaledHealth = scale)) {
      updateScaledHealth();
    }
  }
  
  public boolean isHealthScaled()
  {
    return this.scaledHealth;
  }
  
  public float getScaledHealth()
  {
    return (float)(isHealthScaled() ? getHealth() * getHealthScale() / getMaxHealth() : getHealth());
  }
  
  public double getHealth()
  {
    return this.health;
  }
  
  public void setRealHealth(double health)
  {
    this.health = health;
  }
  
  public void updateScaledHealth()
  {
    AttributeMapServer attributemapserver = (AttributeMapServer)getHandle().getAttributeMap();
    Set set = attributemapserver.getAttributes();
    
    injectScaledMaxHealth(set, true);
    
    getHandle().getDataWatcher().watch(6, Float.valueOf(getScaledHealth()));
    getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateHealth(getScaledHealth(), getHandle().getFoodData().getFoodLevel(), getHandle().getFoodData().getSaturationLevel()));
    getHandle().playerConnection.sendPacket(new PacketPlayOutUpdateAttributes(getHandle().getId(), set));
    
    set.clear();
    getHandle().maxHealthCache = getMaxHealth();
  }
  
  public void injectScaledMaxHealth(Collection collection, boolean force)
  {
    if ((!this.scaledHealth) && (!force)) {
      return;
    }
    for (Object genericInstance : collection)
    {
      IAttribute attribute = ((AttributeInstance)genericInstance).getAttribute();
      if (attribute.getName().equals("generic.maxHealth"))
      {
        collection.remove(genericInstance);
        break;
      }
    }
    double healthMod = this.scaledHealth ? this.healthScale : getMaxHealth();
    if ((healthMod >= 3.4028234663852886E38D) || (healthMod <= 0.0D))
    {
      healthMod = 20.0D;
      getServer().getLogger().warning(getName() + " tried to crash the server with a large health attribute");
    }
    collection.add(new AttributeModifiable(getHandle().getAttributeMap(), new AttributeRanged(null, "generic.maxHealth", healthMod, 0.0D, 3.4028234663852886E38D).a("Max Health").a(true)));
  }
  
  public org.bukkit.entity.Entity getSpectatorTarget()
  {
    net.minecraft.server.v1_8_R3.Entity followed = getHandle().C();
    return followed == getHandle() ? null : followed.getBukkitEntity();
  }
  
  public void setSpectatorTarget(org.bukkit.entity.Entity entity)
  {
    Preconditions.checkArgument(getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
    getHandle().setSpectatorTarget(entity == null ? null : ((CraftEntity)entity).getHandle());
  }
  
  private final Player.Spigot spigot = new Player.Spigot()
  {
    public InetSocketAddress getRawAddress()
    {
      return (InetSocketAddress)CraftPlayer.this.getHandle().playerConnection.networkManager.getRawAddress();
    }
    
    public boolean getCollidesWithEntities()
    {
      return CraftPlayer.this.getHandle().collidesWithEntities;
    }
    
    public void setCollidesWithEntities(boolean collides)
    {
      CraftPlayer.this.getHandle().collidesWithEntities = collides;
      CraftPlayer.this.getHandle().k = collides;
    }
    
    public void respawn()
    {
      if ((CraftPlayer.this.getHealth() <= 0.0D) && (CraftPlayer.this.isOnline())) {
        CraftPlayer.this.server.getServer().getPlayerList().moveToWorld(CraftPlayer.this.getHandle(), 0, false);
      }
    }
    
    public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius)
    {
      Validate.notNull(location, "Location cannot be null");
      Validate.notNull(effect, "Effect cannot be null");
      Validate.notNull(location.getWorld(), "World cannot be null");
      Packet packet;
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
          EnumParticle p = arrayOfEnumParticle[j];
          if (effect.getName().startsWith(p.b().replace("_", "")))
          {
            particle = p;
            if (effect.getData() == null) {
              break;
            }
            if (effect.getData().equals(Material.class))
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
      if (CraftPlayer.this.getHandle().playerConnection == null) {
        return;
      }
      if (!location.getWorld().equals(CraftPlayer.this.getWorld())) {
        return;
      }
      int distance = (int)CraftPlayer.this.getLocation().distanceSquared(location);
      if (distance <= radius) {
        CraftPlayer.this.getHandle().playerConnection.sendPacket(packet);
      }
    }
    
    public String getLocale()
    {
      return CraftPlayer.this.getHandle().locale;
    }
    
    public Set<Player> getHiddenPlayers()
    {
      Set<Player> ret = new HashSet();
      for (UUID u : CraftPlayer.this.hiddenPlayers) {
        ret.add(CraftPlayer.this.getServer().getPlayer(u));
      }
      return Collections.unmodifiableSet(ret);
    }
    
    public void sendMessage(BaseComponent component)
    {
      sendMessage(new BaseComponent[] { component });
    }
    
    public void sendMessage(BaseComponent... components)
    {
      if (CraftPlayer.this.getHandle().playerConnection == null) {
        return;
      }
      PacketPlayOutChat packet = new PacketPlayOutChat();
      packet.components = components;
      CraftPlayer.this.getHandle().playerConnection.sendPacket(packet);
    }
  };
  
  public Player.Spigot spigot()
  {
    return this.spigot;
  }
}
