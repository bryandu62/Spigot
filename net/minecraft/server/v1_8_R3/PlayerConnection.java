package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_8_R3.util.LazyPlayerSet;
import org.bukkit.craftbukkit.v1_8_R3.util.Waitable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotConfig;

public class PlayerConnection
  implements PacketListenerPlayIn, IUpdatePlayerListBox
{
  private static final org.apache.logging.log4j.Logger c = ;
  public final NetworkManager networkManager;
  private final MinecraftServer minecraftServer;
  public EntityPlayer player;
  private int e;
  private int f;
  private int g;
  private boolean h;
  private int i;
  private long j;
  private long k;
  private volatile int chatThrottle;
  private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(PlayerConnection.class, "chatThrottle");
  private int m;
  private IntHashMap<Short> n = new IntHashMap();
  private double o;
  private double p;
  private double q;
  private boolean checkMovement = true;
  private boolean processedDisconnect;
  private final CraftServer server;
  
  public PlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer)
  {
    this.minecraftServer = minecraftserver;
    this.networkManager = networkmanager;
    networkmanager.a(this);
    this.player = entityplayer;
    entityplayer.playerConnection = this;
    
    this.server = minecraftserver.server;
  }
  
  private int lastTick = MinecraftServer.currentTick;
  private int lastDropTick = MinecraftServer.currentTick;
  private int dropCount = 0;
  private static final int SURVIVAL_PLACE_DISTANCE_SQUARED = 36;
  private static final int CREATIVE_PLACE_DISTANCE_SQUARED = 49;
  private double lastPosX = Double.MAX_VALUE;
  private double lastPosY = Double.MAX_VALUE;
  private double lastPosZ = Double.MAX_VALUE;
  private float lastPitch = Float.MAX_VALUE;
  private float lastYaw = Float.MAX_VALUE;
  private boolean justTeleported = false;
  private boolean hasMoved;
  
  public CraftPlayer getPlayer()
  {
    return this.player == null ? null : this.player.getBukkitEntity();
  }
  
  private static final HashSet<Integer> invalidItems = new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(26), Integer.valueOf(34), Integer.valueOf(36), Integer.valueOf(43), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(55), Integer.valueOf(59), Integer.valueOf(60), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(68), Integer.valueOf(71), Integer.valueOf(74), Integer.valueOf(75), Integer.valueOf(83), Integer.valueOf(90), Integer.valueOf(92), Integer.valueOf(93), Integer.valueOf(94), Integer.valueOf(104), Integer.valueOf(105), Integer.valueOf(115), Integer.valueOf(117), Integer.valueOf(118), Integer.valueOf(119), Integer.valueOf(125), Integer.valueOf(127), Integer.valueOf(132), Integer.valueOf(140), Integer.valueOf(141), Integer.valueOf(142), Integer.valueOf(144) }));
  
  public void c()
  {
    this.h = false;
    this.e += 1;
    this.minecraftServer.methodProfiler.a("keepAlive");
    if (this.e - this.k > 40L)
    {
      this.k = this.e;
      this.j = d();
      this.i = ((int)this.j);
      sendPacket(new PacketPlayOutKeepAlive(this.i));
    }
    this.minecraftServer.methodProfiler.b();
    int spam;
    while (((spam = this.chatThrottle) > 0) && (!chatSpamField.compareAndSet(this, spam, spam - 1))) {}
    if (this.m > 0) {
      this.m -= 1;
    }
    if ((this.player.D() > 0L) && (this.minecraftServer.getIdleTimeout() > 0) && (MinecraftServer.az() - this.player.D() > this.minecraftServer.getIdleTimeout() * 1000 * 60))
    {
      this.player.resetIdleTimer();
      disconnect("You have been idle for too long!");
    }
  }
  
  public NetworkManager a()
  {
    return this.networkManager;
  }
  
  public void disconnect(String s)
  {
    String leaveMessage = EnumChatFormat.YELLOW + this.player.getName() + " left the game.";
    
    PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.player), s, leaveMessage);
    if (this.server.getServer().isRunning()) {
      this.server.getPluginManager().callEvent(event);
    }
    if (event.isCancelled()) {
      return;
    }
    s = event.getReason();
    
    final ChatComponentText chatcomponenttext = new ChatComponentText(s);
    
    this.networkManager.a(new PacketPlayOutKickDisconnect(chatcomponenttext), new GenericFutureListener()
    {
      public void operationComplete(Future future)
        throws Exception
      {
        PlayerConnection.this.networkManager.close(chatcomponenttext);
      }
    }, new GenericFutureListener[0]);
    a(chatcomponenttext);
    this.networkManager.k();
    
    this.minecraftServer.postToMainThread(new Runnable()
    {
      public void run()
      {
        PlayerConnection.this.networkManager.l();
      }
    });
  }
  
  public void a(PacketPlayInSteerVehicle packetplayinsteervehicle)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinsteervehicle, this, this.player.u());
    this.player.a(packetplayinsteervehicle.a(), packetplayinsteervehicle.b(), packetplayinsteervehicle.c(), packetplayinsteervehicle.d());
  }
  
  private boolean b(PacketPlayInFlying packetplayinflying)
  {
    return (!Doubles.isFinite(packetplayinflying.a())) || (!Doubles.isFinite(packetplayinflying.b())) || (!Doubles.isFinite(packetplayinflying.c())) || (!Floats.isFinite(packetplayinflying.e())) || (!Floats.isFinite(packetplayinflying.d()));
  }
  
  public void a(PacketPlayInFlying packetplayinflying)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinflying, this, this.player.u());
    if (b(packetplayinflying))
    {
      disconnect("Invalid move packet received");
    }
    else
    {
      WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
      
      this.h = true;
      if (!this.player.viewingCredits)
      {
        double d0 = this.player.locX;
        double d1 = this.player.locY;
        double d2 = this.player.locZ;
        double d3 = 0.0D;
        double d4 = packetplayinflying.a() - this.o;
        double d5 = packetplayinflying.b() - this.p;
        double d6 = packetplayinflying.c() - this.q;
        if (packetplayinflying.g())
        {
          d3 = d4 * d4 + d5 * d5 + d6 * d6;
          if ((!this.checkMovement) && (d3 < 0.25D)) {
            this.checkMovement = true;
          }
        }
        Player player = getPlayer();
        if (!this.hasMoved)
        {
          Location curPos = player.getLocation();
          this.lastPosX = curPos.getX();
          this.lastPosY = curPos.getY();
          this.lastPosZ = curPos.getZ();
          this.lastYaw = curPos.getYaw();
          this.lastPitch = curPos.getPitch();
          this.hasMoved = true;
        }
        Location from = new Location(player.getWorld(), this.lastPosX, this.lastPosY, this.lastPosZ, this.lastYaw, this.lastPitch);
        Location to = player.getLocation().clone();
        if ((packetplayinflying.hasPos) && ((!packetplayinflying.hasPos) || (packetplayinflying.y != -999.0D)))
        {
          to.setX(packetplayinflying.x);
          to.setY(packetplayinflying.y);
          to.setZ(packetplayinflying.z);
        }
        if (packetplayinflying.hasLook)
        {
          to.setYaw(packetplayinflying.yaw);
          to.setPitch(packetplayinflying.pitch);
        }
        double delta = Math.pow(this.lastPosX - to.getX(), 2.0D) + Math.pow(this.lastPosY - to.getY(), 2.0D) + Math.pow(this.lastPosZ - to.getZ(), 2.0D);
        float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());
        if (((delta > 0.00390625D) || (deltaAngle > 10.0F)) && (this.checkMovement) && (!this.player.dead))
        {
          this.lastPosX = to.getX();
          this.lastPosY = to.getY();
          this.lastPosZ = to.getZ();
          this.lastYaw = to.getYaw();
          this.lastPitch = to.getPitch();
          
          Location oldTo = to.clone();
          PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
          this.server.getPluginManager().callEvent(event);
          if (event.isCancelled())
          {
            this.player.playerConnection.sendPacket(new PacketPlayOutPosition(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch(), Collections.emptySet()));
            return;
          }
          if ((!oldTo.equals(event.getTo())) && (!event.isCancelled()))
          {
            this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            return;
          }
          if ((!from.equals(getPlayer().getLocation())) && (this.justTeleported))
          {
            this.justTeleported = false;
            return;
          }
        }
        if ((this.checkMovement) && (!this.player.dead))
        {
          this.f = this.e;
          if (this.player.vehicle != null)
          {
            float f = this.player.yaw;
            float f1 = this.player.pitch;
            
            this.player.vehicle.al();
            double d7 = this.player.locX;
            double d8 = this.player.locY;
            double d9 = this.player.locZ;
            if (packetplayinflying.h())
            {
              f = packetplayinflying.d();
              f1 = packetplayinflying.e();
            }
            this.player.onGround = packetplayinflying.f();
            this.player.l();
            this.player.setLocation(d7, d8, d9, f, f1);
            if (this.player.vehicle != null) {
              this.player.vehicle.al();
            }
            this.minecraftServer.getPlayerList().d(this.player);
            if (this.player.vehicle != null)
            {
              this.player.vehicle.ai = true;
              if (d3 > 4.0D)
              {
                Entity entity = this.player.vehicle;
                
                this.player.playerConnection.sendPacket(new PacketPlayOutEntityTeleport(entity));
                a(this.player.locX, this.player.locY, this.player.locZ, this.player.yaw, this.player.pitch);
              }
            }
            if (this.checkMovement)
            {
              this.o = this.player.locX;
              this.p = this.player.locY;
              this.q = this.player.locZ;
            }
            worldserver.g(this.player);
            return;
          }
          if (this.player.isSleeping())
          {
            this.player.l();
            this.player.setLocation(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
            worldserver.g(this.player);
            return;
          }
          double d10 = this.player.locY;
          
          this.o = this.player.locX;
          this.p = this.player.locY;
          this.q = this.player.locZ;
          double d7 = this.player.locX;
          double d8 = this.player.locY;
          double d9 = this.player.locZ;
          float f2 = this.player.yaw;
          float f3 = this.player.pitch;
          if ((packetplayinflying.g()) && (packetplayinflying.b() == -999.0D)) {
            packetplayinflying.a(false);
          }
          if (packetplayinflying.g())
          {
            d7 = packetplayinflying.a();
            d8 = packetplayinflying.b();
            d9 = packetplayinflying.c();
            if ((Math.abs(packetplayinflying.a()) > 3.0E7D) || (Math.abs(packetplayinflying.c()) > 3.0E7D))
            {
              disconnect("Illegal position");
              return;
            }
          }
          if (packetplayinflying.h())
          {
            f2 = packetplayinflying.d();
            f3 = packetplayinflying.e();
          }
          this.player.l();
          this.player.setLocation(this.o, this.p, this.q, f2, f3);
          if (!this.checkMovement) {
            return;
          }
          double d11 = d7 - this.player.locX;
          double d12 = d8 - this.player.locY;
          double d13 = d9 - this.player.locZ;
          double d14 = this.player.motX * this.player.motX + this.player.motY * this.player.motY + this.player.motZ * this.player.motZ;
          double d15 = d11 * d11 + d12 * d12 + d13 * d13;
          if ((d15 - d14 > SpigotConfig.movedTooQuicklyThreshold) && (this.checkMovement) && ((!this.minecraftServer.T()) || (!this.minecraftServer.S().equals(this.player.getName()))))
          {
            c.warn(this.player.getName() + " moved too quickly! " + d11 + "," + d12 + "," + d13 + " (" + d11 + ", " + d12 + ", " + d13 + ")");
            a(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
            return;
          }
          float f4 = 0.0625F;
          boolean flag = worldserver.getCubes(this.player, this.player.getBoundingBox().shrink(f4, f4, f4)).isEmpty();
          if ((this.player.onGround) && (!packetplayinflying.f()) && (d12 > 0.0D)) {
            this.player.bF();
          }
          this.player.move(d11, d12, d13);
          this.player.onGround = packetplayinflying.f();
          double d16 = d12;
          
          d11 = d7 - this.player.locX;
          d12 = d8 - this.player.locY;
          if ((d12 > -0.5D) || (d12 < 0.5D)) {
            d12 = 0.0D;
          }
          d13 = d9 - this.player.locZ;
          d15 = d11 * d11 + d12 * d12 + d13 * d13;
          boolean flag1 = false;
          if ((d15 > SpigotConfig.movedWronglyThreshold) && (!this.player.isSleeping()) && (!this.player.playerInteractManager.isCreative()))
          {
            flag1 = true;
            c.warn(this.player.getName() + " moved wrongly!");
          }
          this.player.setLocation(d7, d8, d9, f2, f3);
          this.player.checkMovement(this.player.locX - d0, this.player.locY - d1, this.player.locZ - d2);
          if (!this.player.noclip)
          {
            boolean flag2 = worldserver.getCubes(this.player, this.player.getBoundingBox().shrink(f4, f4, f4)).isEmpty();
            if ((flag) && ((flag1) || (!flag2)) && (!this.player.isSleeping()))
            {
              a(this.o, this.p, this.q, f2, f3);
              return;
            }
          }
          AxisAlignedBB axisalignedbb = this.player.getBoundingBox().grow(f4, f4, f4).a(0.0D, -0.55D, 0.0D);
          if ((!this.minecraftServer.getAllowFlight()) && (!this.player.abilities.canFly) && (!worldserver.c(axisalignedbb)))
          {
            if (d16 >= -0.03125D)
            {
              this.g += 1;
              if (this.g > 80)
              {
                c.warn(this.player.getName() + " was kicked for floating too long!");
                disconnect("Flying is not enabled on this server");
              }
            }
          }
          else {
            this.g = 0;
          }
          this.player.onGround = packetplayinflying.f();
          this.minecraftServer.getPlayerList().d(this.player);
          this.player.a(this.player.locY - d10, packetplayinflying.f());
        }
        else if (this.e - this.f > 20)
        {
          a(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
        }
      }
    }
  }
  
  public void a(double d0, double d1, double d2, float f, float f1)
  {
    a(d0, d1, d2, f, f1, Collections.emptySet());
  }
  
  public void a(double d0, double d1, double d2, float f, float f1, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set)
  {
    Player player = getPlayer();
    Location from = player.getLocation();
    Location to = new Location(getPlayer().getWorld(), d0, d1, d2, f, f1);
    PlayerTeleportEvent event = new PlayerTeleportEvent(player, from, to, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    this.server.getPluginManager().callEvent(event);
    
    from = event.getFrom();
    to = event.isCancelled() ? from : event.getTo();
    
    teleport(to, set);
  }
  
  public void teleport(Location dest)
  {
    teleport(dest, Collections.emptySet());
  }
  
  public void teleport(Location dest, Set set)
  {
    double d0 = dest.getX();
    double d1 = dest.getY();
    double d2 = dest.getZ();
    float f = dest.getYaw();
    float f1 = dest.getPitch();
    if (Float.isNaN(f)) {
      f = 0.0F;
    }
    if (Float.isNaN(f1)) {
      f1 = 0.0F;
    }
    this.lastPosX = d0;
    this.lastPosY = d1;
    this.lastPosZ = d2;
    this.lastYaw = f;
    this.lastPitch = f1;
    this.justTeleported = true;
    
    this.checkMovement = false;
    this.o = d0;
    this.p = d1;
    this.q = d2;
    if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X)) {
      this.o += this.player.locX;
    }
    if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y)) {
      this.p += this.player.locY;
    }
    if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z)) {
      this.q += this.player.locZ;
    }
    float f2 = f;
    float f3 = f1;
    if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT)) {
      f2 = f + this.player.yaw;
    }
    if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT)) {
      f3 = f1 + this.player.pitch;
    }
    this.player.setLocation(this.o, this.p, this.q, f2, f3);
    this.player.playerConnection.sendPacket(new PacketPlayOutPosition(d0, d1, d2, f, f1, set));
  }
  
  public void a(PacketPlayInBlockDig packetplayinblockdig)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinblockdig, this, this.player.u());
    if (this.player.dead) {
      return;
    }
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
    BlockPosition blockposition = packetplayinblockdig.a();
    
    this.player.resetIdleTimer();
    switch (SyntheticClass_1.a[packetplayinblockdig.c().ordinal()])
    {
    case 1: 
      if (!this.player.isSpectator())
      {
        if (this.lastDropTick != MinecraftServer.currentTick)
        {
          this.dropCount = 0;
          this.lastDropTick = MinecraftServer.currentTick;
        }
        else
        {
          this.dropCount += 1;
          if (this.dropCount >= 20)
          {
            c.warn(this.player.getName() + " dropped their items too quickly!");
            disconnect("You dropped your items too quickly (Hacking?)");
            return;
          }
        }
        this.player.a(false);
      }
      return;
    case 2: 
      if (!this.player.isSpectator()) {
        this.player.a(true);
      }
      return;
    case 3: 
      this.player.bU();
      return;
    case 4: 
    case 5: 
    case 6: 
      double d0 = this.player.locX - (blockposition.getX() + 0.5D);
      double d1 = this.player.locY - (blockposition.getY() + 0.5D) + 1.5D;
      double d2 = this.player.locZ - (blockposition.getZ() + 0.5D);
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;
      if (d3 > 36.0D) {
        return;
      }
      if (blockposition.getY() >= this.minecraftServer.getMaxBuildHeight()) {
        return;
      }
      if (packetplayinblockdig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK)
      {
        if ((!this.minecraftServer.a(worldserver, blockposition, this.player)) && (worldserver.getWorldBorder().a(blockposition)))
        {
          this.player.playerInteractManager.a(blockposition, packetplayinblockdig.b());
        }
        else
        {
          CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockposition, packetplayinblockdig.b(), this.player.inventory.getItemInHand());
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
          
          TileEntity tileentity = worldserver.getTileEntity(blockposition);
          if (tileentity != null) {
            this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
          }
        }
      }
      else
      {
        if (packetplayinblockdig.c() == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
          this.player.playerInteractManager.a(blockposition);
        } else if (packetplayinblockdig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
          this.player.playerInteractManager.e();
        }
        if (worldserver.getType(blockposition).getBlock().getMaterial() != Material.AIR) {
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
        }
      }
      return;
    }
    throw new IllegalArgumentException("Invalid player action");
  }
  
  private long lastPlace = -1L;
  private int packets = 0;
  
  public void a(PacketPlayInBlockPlace packetplayinblockplace)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinblockplace, this, this.player.u());
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
    boolean throttled = false;
    if ((this.lastPlace != -1L) && (packetplayinblockplace.timestamp - this.lastPlace < 30L) && (this.packets++ >= 4))
    {
      throttled = true;
    }
    else if ((packetplayinblockplace.timestamp - this.lastPlace >= 30L) || (this.lastPlace == -1L))
    {
      this.lastPlace = packetplayinblockplace.timestamp;
      this.packets = 0;
    }
    if (this.player.dead) {
      return;
    }
    boolean always = false;
    
    ItemStack itemstack = this.player.inventory.getItemInHand();
    boolean flag = false;
    BlockPosition blockposition = packetplayinblockplace.a();
    EnumDirection enumdirection = EnumDirection.fromType1(packetplayinblockplace.getFace());
    
    this.player.resetIdleTimer();
    if (packetplayinblockplace.getFace() == 255)
    {
      if (itemstack == null) {
        return;
      }
      int itemstackAmount = itemstack.count;
      if (!throttled)
      {
        float f1 = this.player.pitch;
        float f2 = this.player.yaw;
        double d0 = this.player.locX;
        double d1 = this.player.locY + this.player.getHeadHeight();
        double d2 = this.player.locZ;
        Vec3D vec3d = new Vec3D(d0, d1, d2);
        
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = this.player.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.CREATIVE ? 5.0D : 4.5D;
        Vec3D vec3d1 = vec3d.add(f7 * d3, f6 * d3, f8 * d3);
        MovingObjectPosition movingobjectposition = this.player.world.rayTrace(vec3d, vec3d1, false);
        
        boolean cancelled = false;
        if ((movingobjectposition == null) || (movingobjectposition.type != MovingObjectPosition.EnumMovingObjectType.BLOCK))
        {
          PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack);
          cancelled = event.useItemInHand() == Event.Result.DENY;
        }
        else if (this.player.playerInteractManager.firedInteract)
        {
          this.player.playerInteractManager.firedInteract = false;
          cancelled = this.player.playerInteractManager.interactResult;
        }
        else
        {
          PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_BLOCK, movingobjectposition.a(), movingobjectposition.direction, itemstack, true);
          cancelled = event.useItemInHand() == Event.Result.DENY;
        }
        if (!cancelled) {
          this.player.playerInteractManager.useItem(this.player, this.player.world, itemstack);
        }
      }
      always = (itemstack.count != itemstackAmount) || (itemstack.getItem() == Item.getItemOf(Blocks.WATERLILY));
    }
    else if ((blockposition.getY() >= this.minecraftServer.getMaxBuildHeight() - 1) && ((enumdirection == EnumDirection.UP) || (blockposition.getY() >= this.minecraftServer.getMaxBuildHeight())))
    {
      ChatMessage chatmessage = new ChatMessage("build.tooHigh", new Object[] { Integer.valueOf(this.minecraftServer.getMaxBuildHeight()) });
      
      chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
      this.player.playerConnection.sendPacket(new PacketPlayOutChat(chatmessage));
      flag = true;
    }
    else
    {
      Location eyeLoc = getPlayer().getEyeLocation();
      double reachDistance = NumberConversions.square(eyeLoc.getX() - blockposition.getX()) + NumberConversions.square(eyeLoc.getY() - blockposition.getY()) + NumberConversions.square(eyeLoc.getZ() - blockposition.getZ());
      if (reachDistance > (getPlayer().getGameMode() == GameMode.CREATIVE ? 49 : 36)) {
        return;
      }
      if (!worldserver.getWorldBorder().a(blockposition)) {
        return;
      }
      if ((this.checkMovement) && (this.player.e(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D) < 64.0D) && (!this.minecraftServer.a(worldserver, blockposition, this.player)) && (worldserver.getWorldBorder().a(blockposition))) {
        always = (throttled) || (!this.player.playerInteractManager.interact(this.player, worldserver, itemstack, blockposition, enumdirection, packetplayinblockplace.d(), packetplayinblockplace.e(), packetplayinblockplace.f()));
      }
      flag = true;
    }
    if (flag)
    {
      this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
      this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition.shift(enumdirection)));
    }
    itemstack = this.player.inventory.getItemInHand();
    if ((itemstack != null) && (itemstack.count == 0))
    {
      this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
      itemstack = null;
    }
    if ((itemstack == null) || (itemstack.l() == 0))
    {
      this.player.g = true;
      this.player.inventory.items[this.player.inventory.itemInHandIndex] = ItemStack.b(this.player.inventory.items[this.player.inventory.itemInHandIndex]);
      Slot slot = this.player.activeContainer.getSlot(this.player.inventory, this.player.inventory.itemInHandIndex);
      
      this.player.activeContainer.b();
      this.player.g = false;
      if ((!ItemStack.matches(this.player.inventory.getItemInHand(), packetplayinblockplace.getItemStack())) || (always)) {
        sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, slot.rawSlotIndex, this.player.inventory.getItemInHand()));
      }
    }
  }
  
  public void a(PacketPlayInSpectate packetplayinspectate)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinspectate, this, this.player.u());
    if (this.player.isSpectator())
    {
      Entity entity = null;
      WorldServer[] aworldserver = this.minecraftServer.worldServer;
      aworldserver.length;
      for (WorldServer worldserver : this.minecraftServer.worlds) {
        if (worldserver != null)
        {
          entity = packetplayinspectate.a(worldserver);
          if (entity != null) {
            break;
          }
        }
      }
      if (entity != null)
      {
        this.player.setSpectatorTarget(this.player);
        this.player.mount(null);
        
        this.player.getBukkitEntity().teleport(entity.getBukkitEntity(), PlayerTeleportEvent.TeleportCause.SPECTATE);
      }
    }
  }
  
  public void a(PacketPlayInResourcePackStatus packetplayinresourcepackstatus) {}
  
  public void a(IChatBaseComponent ichatbasecomponent)
  {
    if (this.processedDisconnect) {
      return;
    }
    this.processedDisconnect = true;
    
    c.info(this.player.getName() + " lost connection: " + ichatbasecomponent.c());
    
    this.player.q();
    String quitMessage = this.minecraftServer.getPlayerList().disconnect(this.player);
    if ((quitMessage != null) && (quitMessage.length() > 0)) {
      this.minecraftServer.getPlayerList().sendMessage(CraftChatMessage.fromString(quitMessage));
    }
    if ((this.minecraftServer.T()) && (this.player.getName().equals(this.minecraftServer.S())))
    {
      c.info("Stopping singleplayer server as player logged out");
      this.minecraftServer.safeShutdown();
    }
  }
  
  public void sendPacket(final Packet packet)
  {
    if ((packet instanceof PacketPlayOutChat))
    {
      PacketPlayOutChat packetplayoutchat = (PacketPlayOutChat)packet;
      EntityHuman.EnumChatVisibility entityhuman_enumchatvisibility = this.player.getChatFlags();
      if (entityhuman_enumchatvisibility == EntityHuman.EnumChatVisibility.HIDDEN) {
        return;
      }
      if ((entityhuman_enumchatvisibility == EntityHuman.EnumChatVisibility.SYSTEM) && (!packetplayoutchat.b())) {
        return;
      }
    }
    if (packet == null) {
      return;
    }
    if ((packet instanceof PacketPlayOutSpawnPosition))
    {
      PacketPlayOutSpawnPosition packet6 = (PacketPlayOutSpawnPosition)packet;
      this.player.compassTarget = new Location(getPlayer().getWorld(), packet6.position.getX(), packet6.position.getY(), packet6.position.getZ());
    }
    try
    {
      this.networkManager.handle(packet);
    }
    catch (Throwable throwable)
    {
      CrashReport crashreport = CrashReport.a(throwable, "Sending packet");
      CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Packet being sent");
      
      crashreportsystemdetails.a("Packet class", new Callable()
      {
        public String a()
          throws Exception
        {
          return packet.getClass().getCanonicalName();
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
  
  public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot)
  {
    if (this.player.dead) {
      return;
    }
    PlayerConnectionUtils.ensureMainThread(packetplayinhelditemslot, this, this.player.u());
    if ((packetplayinhelditemslot.a() >= 0) && (packetplayinhelditemslot.a() < PlayerInventory.getHotbarSize()))
    {
      PlayerItemHeldEvent event = new PlayerItemHeldEvent(getPlayer(), this.player.inventory.itemInHandIndex, packetplayinhelditemslot.a());
      this.server.getPluginManager().callEvent(event);
      if (event.isCancelled())
      {
        sendPacket(new PacketPlayOutHeldItemSlot(this.player.inventory.itemInHandIndex));
        this.player.resetIdleTimer();
        return;
      }
      this.player.inventory.itemInHandIndex = packetplayinhelditemslot.a();
      this.player.resetIdleTimer();
    }
    else
    {
      c.warn(this.player.getName() + " tried to set an invalid carried item");
      disconnect("Invalid hotbar selection (Hacking?)");
    }
  }
  
  public void a(PacketPlayInChat packetplayinchat)
  {
    boolean isSync = packetplayinchat.a().startsWith("/");
    if (packetplayinchat.a().startsWith("/")) {
      PlayerConnectionUtils.ensureMainThread(packetplayinchat, this, this.player.u());
    }
    if ((this.player.dead) || (this.player.getChatFlags() == EntityHuman.EnumChatVisibility.HIDDEN))
    {
      ChatMessage chatmessage = new ChatMessage("chat.cannotSend", new Object[0]);
      
      chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
      sendPacket(new PacketPlayOutChat(chatmessage));
    }
    else
    {
      this.player.resetIdleTimer();
      String s = packetplayinchat.a();
      
      s = StringUtils.normalizeSpace(s);
      for (i = 0; i < s.length(); i++) {
        if (!SharedConstants.isAllowedChatCharacter(s.charAt(i)))
        {
          if (!isSync)
          {
            Waitable waitable = new Waitable()
            {
              protected Object evaluate()
              {
                PlayerConnection.this.disconnect("Illegal characters in chat");
                return null;
              }
            };
            this.minecraftServer.processQueue.add(waitable);
            try
            {
              waitable.get();
            }
            catch (InterruptedException localInterruptedException1)
            {
              Thread.currentThread().interrupt();
            }
            catch (ExecutionException e)
            {
              throw new RuntimeException(e);
            }
          }
          else
          {
            disconnect("Illegal characters in chat");
          }
          return;
        }
      }
      if (isSync)
      {
        try
        {
          this.minecraftServer.server.playerCommandState = true;
          handleCommand(s);
        }
        finally
        {
          this.minecraftServer.server.playerCommandState = false;
        }
      }
      else if (s.isEmpty())
      {
        c.warn(this.player.getName() + " tried to send an empty message");
      }
      else if (getPlayer().isConversing())
      {
        final String message = s;
        this.minecraftServer.processQueue.add(new Waitable()
        {
          protected Object evaluate()
          {
            PlayerConnection.this.getPlayer().acceptConversationInput(message);
            return null;
          }
        });
      }
      else if (this.player.getChatFlags() == EntityHuman.EnumChatVisibility.SYSTEM)
      {
        ChatMessage chatmessage = new ChatMessage("chat.cannotSend", new Object[0]);
        
        chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
        sendPacket(new PacketPlayOutChat(chatmessage));
      }
      else
      {
        chat(s, true);
      }
      boolean counted = true;
      for (String exclude : SpigotConfig.spamExclusions) {
        if ((exclude != null) && (s.startsWith(exclude)))
        {
          counted = false;
          break;
        }
      }
      if ((counted) && (chatSpamField.addAndGet(this, 20) > 200) && (!this.minecraftServer.getPlayerList().isOp(this.player.getProfile()))) {
        if (!isSync)
        {
          Waitable waitable = new Waitable()
          {
            protected Object evaluate()
            {
              PlayerConnection.this.disconnect("disconnect.spam");
              return null;
            }
          };
          this.minecraftServer.processQueue.add(waitable);
          try
          {
            waitable.get();
          }
          catch (InterruptedException localInterruptedException2)
          {
            Thread.currentThread().interrupt();
          }
          catch (ExecutionException e)
          {
            throw new RuntimeException(e);
          }
        }
        else
        {
          disconnect("disconnect.spam");
        }
      }
    }
  }
  
  public void chat(String s, boolean async)
  {
    if ((s.isEmpty()) || (this.player.getChatFlags() == EntityHuman.EnumChatVisibility.HIDDEN)) {
      return;
    }
    if ((!async) && (s.startsWith("/")))
    {
      handleCommand(s);
    }
    else if (this.player.getChatFlags() != EntityHuman.EnumChatVisibility.SYSTEM)
    {
      Player player = getPlayer();
      AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet());
      this.server.getPluginManager().callEvent(event);
      Waitable waitable;
      if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0)
      {
        final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
        queueEvent.setCancelled(event.isCancelled());
        waitable = new Waitable()
        {
          protected Object evaluate()
          {
            Bukkit.getPluginManager().callEvent(queueEvent);
            if (queueEvent.isCancelled()) {
              return null;
            }
            String message = String.format(queueEvent.getFormat(), new Object[] { queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage() });
            PlayerConnection.this.minecraftServer.console.sendMessage(message);
            if (((LazyPlayerSet)queueEvent.getRecipients()).isLazy()) {
              for (Object player : PlayerConnection.this.minecraftServer.getPlayerList().players) {
                ((EntityPlayer)player).sendMessage(CraftChatMessage.fromString(message));
              }
            } else {
              for (Player player : queueEvent.getRecipients()) {
                player.sendMessage(message);
              }
            }
            return null;
          }
        };
        if (async) {
          this.minecraftServer.processQueue.add(waitable);
        } else {
          waitable.run();
        }
        try
        {
          waitable.get();
        }
        catch (InterruptedException localInterruptedException)
        {
          Thread.currentThread().interrupt();
        }
        catch (ExecutionException e)
        {
          throw new RuntimeException("Exception processing chat event", e.getCause());
        }
      }
      else
      {
        if (event.isCancelled()) {
          return;
        }
        s = String.format(event.getFormat(), new Object[] { event.getPlayer().getDisplayName(), event.getMessage() });
        this.minecraftServer.console.sendMessage(s);
        if (((LazyPlayerSet)event.getRecipients()).isLazy()) {
          for (Object recipient : this.minecraftServer.getPlayerList().players) {
            ((EntityPlayer)recipient).sendMessage(CraftChatMessage.fromString(s));
          }
        } else {
          for (Player recipient : event.getRecipients()) {
            recipient.sendMessage(s);
          }
        }
      }
    }
  }
  
  private void handleCommand(String s)
  {
    SpigotTimings.playerCommandTimer.startTiming();
    if (SpigotConfig.logCommands) {
      c.info(this.player.getName() + " issued server command: " + s);
    }
    CraftPlayer player = getPlayer();
    
    PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
    this.server.getPluginManager().callEvent(event);
    if (event.isCancelled())
    {
      SpigotTimings.playerCommandTimer.stopTiming();
      return;
    }
    try
    {
      if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1)))
      {
        SpigotTimings.playerCommandTimer.stopTiming();
        return;
      }
    }
    catch (CommandException ex)
    {
      player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
      java.util.logging.Logger.getLogger(PlayerConnection.class.getName()).log(Level.SEVERE, null, ex);
      SpigotTimings.playerCommandTimer.stopTiming();
      return;
    }
    SpigotTimings.playerCommandTimer.stopTiming();
  }
  
  public void a(PacketPlayInArmAnimation packetplayinarmanimation)
  {
    if (this.player.dead) {
      return;
    }
    PlayerConnectionUtils.ensureMainThread(packetplayinarmanimation, this, this.player.u());
    this.player.resetIdleTimer();
    
    float f1 = this.player.pitch;
    float f2 = this.player.yaw;
    double d0 = this.player.locX;
    double d1 = this.player.locY + this.player.getHeadHeight();
    double d2 = this.player.locZ;
    Vec3D vec3d = new Vec3D(d0, d1, d2);
    
    float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
    float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
    float f5 = -MathHelper.cos(-f1 * 0.017453292F);
    float f6 = MathHelper.sin(-f1 * 0.017453292F);
    float f7 = f4 * f5;
    float f8 = f3 * f5;
    double d3 = this.player.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.CREATIVE ? 5.0D : 4.5D;
    Vec3D vec3d1 = vec3d.add(f7 * d3, f6 * d3, f8 * d3);
    MovingObjectPosition movingobjectposition = this.player.world.rayTrace(vec3d, vec3d1, false);
    if ((movingobjectposition == null) || (movingobjectposition.type != MovingObjectPosition.EnumMovingObjectType.BLOCK)) {
      CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getItemInHand());
    }
    PlayerAnimationEvent event = new PlayerAnimationEvent(getPlayer());
    this.server.getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return;
    }
    this.player.bw();
  }
  
  public void a(PacketPlayInEntityAction packetplayinentityaction)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinentityaction, this, this.player.u());
    if (this.player.dead) {
      return;
    }
    switch (packetplayinentityaction.b())
    {
    case OPEN_INVENTORY: 
    case RIDING_JUMP: 
      PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(getPlayer(), packetplayinentityaction.b() == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING);
      this.server.getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        return;
      }
      break;
    case START_SPRINTING: 
    case STOP_SLEEPING: 
      PlayerToggleSprintEvent e2 = new PlayerToggleSprintEvent(getPlayer(), packetplayinentityaction.b() == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING);
      this.server.getPluginManager().callEvent(e2);
      if (e2.isCancelled()) {
        return;
      }
      break;
    }
    this.player.resetIdleTimer();
    switch (SyntheticClass_1.b[packetplayinentityaction.b().ordinal()])
    {
    case 1: 
      this.player.setSneaking(true);
      break;
    case 2: 
      this.player.setSneaking(false);
      break;
    case 3: 
      this.player.setSprinting(true);
      break;
    case 4: 
      this.player.setSprinting(false);
      break;
    case 5: 
      this.player.a(false, true, true);
      
      break;
    case 6: 
      if ((this.player.vehicle instanceof EntityHorse)) {
        ((EntityHorse)this.player.vehicle).v(packetplayinentityaction.c());
      }
      break;
    case 7: 
      if ((this.player.vehicle instanceof EntityHorse)) {
        ((EntityHorse)this.player.vehicle).g(this.player);
      }
      break;
    default: 
      throw new IllegalArgumentException("Invalid client command!");
    }
  }
  
  public void a(PacketPlayInUseEntity packetplayinuseentity)
  {
    if (this.player.dead) {
      return;
    }
    PlayerConnectionUtils.ensureMainThread(packetplayinuseentity, this, this.player.u());
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
    Entity entity = packetplayinuseentity.a(worldserver);
    if ((entity == this.player) && (!this.player.isSpectator()))
    {
      disconnect("Cannot interact with self!");
      return;
    }
    this.player.resetIdleTimer();
    if (entity != null)
    {
      boolean flag = this.player.hasLineOfSight(entity);
      double d0 = 36.0D;
      if (!flag) {
        d0 = 9.0D;
      }
      if (this.player.h(entity) < d0)
      {
        ItemStack itemInHand = this.player.inventory.getItemInHand();
        if ((packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) || 
          (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT))
        {
          boolean triggerLeashUpdate = (itemInHand != null) && (itemInHand.getItem() == Items.LEAD) && ((entity instanceof EntityInsentient));
          Item origItem = this.player.inventory.getItemInHand() == null ? null : this.player.inventory.getItemInHand().getItem();
          PlayerInteractEntityEvent event;
          PlayerInteractEntityEvent event;
          if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT)
          {
            event = new PlayerInteractEntityEvent(getPlayer(), entity.getBukkitEntity());
          }
          else
          {
            Vec3D target = packetplayinuseentity.b();
            event = new PlayerInteractAtEntityEvent(getPlayer(), entity.getBukkitEntity(), new Vector(target.a, target.b, target.c));
          }
          this.server.getPluginManager().callEvent(event);
          if ((triggerLeashUpdate) && ((event.isCancelled()) || (this.player.inventory.getItemInHand() == null) || (this.player.inventory.getItemInHand().getItem() != Items.LEAD))) {
            sendPacket(new PacketPlayOutAttachEntity(1, entity, ((EntityInsentient)entity).getLeashHolder()));
          }
          if ((event.isCancelled()) || (this.player.inventory.getItemInHand() == null) || (this.player.inventory.getItemInHand().getItem() != origItem)) {
            sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.datawatcher, true));
          }
          if (event.isCancelled()) {
            return;
          }
        }
        if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT)
        {
          this.player.u(entity);
          if ((itemInHand != null) && (itemInHand.count <= -1)) {
            this.player.updateInventory(this.player.activeContainer);
          }
        }
        else if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT)
        {
          entity.a(this.player, packetplayinuseentity.b());
          if ((itemInHand != null) && (itemInHand.count <= -1)) {
            this.player.updateInventory(this.player.activeContainer);
          }
        }
        else if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)
        {
          if (((entity instanceof EntityItem)) || ((entity instanceof EntityExperienceOrb)) || ((entity instanceof EntityArrow)) || ((entity == this.player) && (!this.player.isSpectator())))
          {
            disconnect("Attempting to attack an invalid entity");
            this.minecraftServer.warning("Player " + this.player.getName() + " tried to attack an invalid entity");
            return;
          }
          this.player.attack(entity);
          if ((itemInHand != null) && (itemInHand.count <= -1)) {
            this.player.updateInventory(this.player.activeContainer);
          }
        }
      }
    }
  }
  
  public void a(PacketPlayInClientCommand packetplayinclientcommand)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinclientcommand, this, this.player.u());
    this.player.resetIdleTimer();
    PacketPlayInClientCommand.EnumClientCommand packetplayinclientcommand_enumclientcommand = packetplayinclientcommand.a();
    switch (SyntheticClass_1.c[packetplayinclientcommand_enumclientcommand.ordinal()])
    {
    case 1: 
      if (this.player.viewingCredits)
      {
        this.minecraftServer.getPlayerList().changeDimension(this.player, 0, PlayerTeleportEvent.TeleportCause.END_PORTAL);
      }
      else if (this.player.u().getWorldData().isHardcore())
      {
        if ((this.minecraftServer.T()) && (this.player.getName().equals(this.minecraftServer.S())))
        {
          this.player.playerConnection.disconnect("You have died. Game over, man, it's game over!");
          this.minecraftServer.aa();
        }
        else
        {
          GameProfileBanEntry gameprofilebanentry = new GameProfileBanEntry(this.player.getProfile(), null, "(You just lost the game)", null, "Death in Hardcore");
          
          this.minecraftServer.getPlayerList().getProfileBans().add(gameprofilebanentry);
          this.player.playerConnection.disconnect("You have died. Game over, man, it's game over!");
        }
      }
      else
      {
        if (this.player.getHealth() > 0.0F) {
          return;
        }
        this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, false);
      }
      break;
    case 2: 
      this.player.getStatisticManager().a(this.player);
      break;
    case 3: 
      this.player.b(AchievementList.f);
    }
  }
  
  public void a(PacketPlayInCloseWindow packetplayinclosewindow)
  {
    if (this.player.dead) {
      return;
    }
    PlayerConnectionUtils.ensureMainThread(packetplayinclosewindow, this, this.player.u());
    
    CraftEventFactory.handleInventoryCloseEvent(this.player);
    
    this.player.p();
  }
  
  public void a(PacketPlayInWindowClick packetplayinwindowclick)
  {
    if (this.player.dead) {
      return;
    }
    PlayerConnectionUtils.ensureMainThread(packetplayinwindowclick, this, this.player.u());
    this.player.resetIdleTimer();
    if ((this.player.activeContainer.windowId == packetplayinwindowclick.a()) && (this.player.activeContainer.c(this.player)))
    {
      boolean cancelled = this.player.isSpectator();
      if ((packetplayinwindowclick.b() < -1) && (packetplayinwindowclick.b() != 64537)) {
        return;
      }
      InventoryView inventory = this.player.activeContainer.getBukkitView();
      InventoryType.SlotType type = CraftInventoryView.getSlotType(inventory, packetplayinwindowclick.b());
      
      InventoryClickEvent event = null;
      ClickType click = ClickType.UNKNOWN;
      InventoryAction action = InventoryAction.UNKNOWN;
      
      ItemStack itemstack = null;
      if (packetplayinwindowclick.b() == -1)
      {
        type = InventoryType.SlotType.OUTSIDE;
        click = packetplayinwindowclick.c() == 0 ? ClickType.WINDOW_BORDER_LEFT : ClickType.WINDOW_BORDER_RIGHT;
        action = InventoryAction.NOTHING;
      }
      else if (packetplayinwindowclick.f() == 0)
      {
        if (packetplayinwindowclick.c() == 0) {
          click = ClickType.LEFT;
        } else if (packetplayinwindowclick.c() == 1) {
          click = ClickType.RIGHT;
        }
        if ((packetplayinwindowclick.c() == 0) || (packetplayinwindowclick.c() == 1))
        {
          action = InventoryAction.NOTHING;
          if (packetplayinwindowclick.b() == 64537)
          {
            if (this.player.inventory.getCarried() != null) {
              action = packetplayinwindowclick.c() == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
            }
          }
          else
          {
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
            if (slot != null)
            {
              ItemStack clickedItem = slot.getItem();
              ItemStack cursor = this.player.inventory.getCarried();
              if (clickedItem == null)
              {
                if (cursor != null) {
                  action = packetplayinwindowclick.c() == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
                }
              }
              else if (slot.isAllowed(this.player)) {
                if (cursor == null) {
                  action = packetplayinwindowclick.c() == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
                } else if (slot.isAllowed(cursor))
                {
                  if ((clickedItem.doMaterialsMatch(cursor)) && (ItemStack.equals(clickedItem, cursor)))
                  {
                    int toPlace = packetplayinwindowclick.c() == 0 ? cursor.count : 1;
                    toPlace = Math.min(toPlace, clickedItem.getMaxStackSize() - clickedItem.count);
                    toPlace = Math.min(toPlace, slot.inventory.getMaxStackSize() - clickedItem.count);
                    if (toPlace == 1) {
                      action = InventoryAction.PLACE_ONE;
                    } else if (toPlace == cursor.count) {
                      action = InventoryAction.PLACE_ALL;
                    } else if (toPlace < 0) {
                      action = toPlace != -1 ? InventoryAction.PICKUP_SOME : InventoryAction.PICKUP_ONE;
                    } else if (toPlace != 0) {
                      action = InventoryAction.PLACE_SOME;
                    }
                  }
                  else if (cursor.count <= slot.getMaxStackSize())
                  {
                    action = InventoryAction.SWAP_WITH_CURSOR;
                  }
                }
                else if ((cursor.getItem() == clickedItem.getItem()) && ((!cursor.usesData()) || (cursor.getData() == clickedItem.getData())) && (ItemStack.equals(cursor, clickedItem)) && 
                  (clickedItem.count >= 0) && 
                  (clickedItem.count + cursor.count <= cursor.getMaxStackSize())) {
                  action = InventoryAction.PICKUP_ALL;
                }
              }
            }
          }
        }
      }
      else if (packetplayinwindowclick.f() == 1)
      {
        if (packetplayinwindowclick.c() == 0) {
          click = ClickType.SHIFT_LEFT;
        } else if (packetplayinwindowclick.c() == 1) {
          click = ClickType.SHIFT_RIGHT;
        }
        if ((packetplayinwindowclick.c() == 0) || (packetplayinwindowclick.c() == 1)) {
          if (packetplayinwindowclick.b() < 0)
          {
            action = InventoryAction.NOTHING;
          }
          else
          {
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
            if ((slot != null) && (slot.isAllowed(this.player)) && (slot.hasItem())) {
              action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
            } else {
              action = InventoryAction.NOTHING;
            }
          }
        }
      }
      else if (packetplayinwindowclick.f() == 2)
      {
        if ((packetplayinwindowclick.c() >= 0) && (packetplayinwindowclick.c() < 9))
        {
          click = ClickType.NUMBER_KEY;
          Slot clickedSlot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
          if (clickedSlot.isAllowed(this.player))
          {
            ItemStack hotbar = this.player.inventory.getItem(packetplayinwindowclick.c());
            boolean canCleanSwap = (hotbar == null) || ((clickedSlot.inventory == this.player.inventory) && (clickedSlot.isAllowed(hotbar)));
            if (clickedSlot.hasItem())
            {
              if (canCleanSwap)
              {
                action = InventoryAction.HOTBAR_SWAP;
              }
              else
              {
                int firstEmptySlot = this.player.inventory.getFirstEmptySlotIndex();
                if (firstEmptySlot > -1) {
                  action = InventoryAction.HOTBAR_MOVE_AND_READD;
                } else {
                  action = InventoryAction.NOTHING;
                }
              }
            }
            else if ((!clickedSlot.hasItem()) && (hotbar != null) && (clickedSlot.isAllowed(hotbar))) {
              action = InventoryAction.HOTBAR_SWAP;
            } else {
              action = InventoryAction.NOTHING;
            }
          }
          else
          {
            action = InventoryAction.NOTHING;
          }
          event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.b(), click, action, packetplayinwindowclick.c());
        }
      }
      else if (packetplayinwindowclick.f() == 3)
      {
        if (packetplayinwindowclick.c() == 2)
        {
          click = ClickType.MIDDLE;
          if (packetplayinwindowclick.b() == 64537)
          {
            action = InventoryAction.NOTHING;
          }
          else
          {
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
            if ((slot != null) && (slot.hasItem()) && (this.player.abilities.canInstantlyBuild) && (this.player.inventory.getCarried() == null)) {
              action = InventoryAction.CLONE_STACK;
            } else {
              action = InventoryAction.NOTHING;
            }
          }
        }
        else
        {
          click = ClickType.UNKNOWN;
          action = InventoryAction.UNKNOWN;
        }
      }
      else if (packetplayinwindowclick.f() == 4)
      {
        if (packetplayinwindowclick.b() >= 0)
        {
          if (packetplayinwindowclick.c() == 0)
          {
            click = ClickType.DROP;
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
            if ((slot != null) && (slot.hasItem()) && (slot.isAllowed(this.player)) && (slot.getItem() != null) && (slot.getItem().getItem() != Item.getItemOf(Blocks.AIR))) {
              action = InventoryAction.DROP_ONE_SLOT;
            } else {
              action = InventoryAction.NOTHING;
            }
          }
          else if (packetplayinwindowclick.c() == 1)
          {
            click = ClickType.CONTROL_DROP;
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
            if ((slot != null) && (slot.hasItem()) && (slot.isAllowed(this.player)) && (slot.getItem() != null) && (slot.getItem().getItem() != Item.getItemOf(Blocks.AIR))) {
              action = InventoryAction.DROP_ALL_SLOT;
            } else {
              action = InventoryAction.NOTHING;
            }
          }
        }
        else
        {
          click = ClickType.LEFT;
          if (packetplayinwindowclick.c() == 1) {
            click = ClickType.RIGHT;
          }
          action = InventoryAction.NOTHING;
        }
      }
      else if (packetplayinwindowclick.f() == 5)
      {
        itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.b(), packetplayinwindowclick.c(), 5, this.player);
      }
      else if (packetplayinwindowclick.f() == 6)
      {
        click = ClickType.DOUBLE_CLICK;
        action = InventoryAction.NOTHING;
        if ((packetplayinwindowclick.b() >= 0) && (this.player.inventory.getCarried() != null))
        {
          ItemStack cursor = this.player.inventory.getCarried();
          action = InventoryAction.NOTHING;
          if ((inventory.getTopInventory().contains(org.bukkit.Material.getMaterial(Item.getId(cursor.getItem())))) || (inventory.getBottomInventory().contains(org.bukkit.Material.getMaterial(Item.getId(cursor.getItem()))))) {
            action = InventoryAction.COLLECT_TO_CURSOR;
          }
        }
      }
      if (packetplayinwindowclick.f() != 5)
      {
        if (click == ClickType.NUMBER_KEY) {
          event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.b(), click, action, packetplayinwindowclick.c());
        } else {
          event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.b(), click, action);
        }
        Inventory top = inventory.getTopInventory();
        if ((packetplayinwindowclick.b() == 0) && ((top instanceof CraftingInventory)))
        {
          Recipe recipe = ((CraftingInventory)top).getRecipe();
          if (recipe != null) {
            if (click == ClickType.NUMBER_KEY) {
              event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.b(), click, action, packetplayinwindowclick.c());
            } else {
              event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.b(), click, action);
            }
          }
        }
        event.setCancelled(cancelled);
        this.server.getPluginManager().callEvent(event);
        switch (event.getResult())
        {
        case DEFAULT: 
        case DENY: 
          itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.b(), packetplayinwindowclick.c(), packetplayinwindowclick.f(), this.player);
          break;
        case ALLOW: 
          switch (action)
          {
          case COLLECT_TO_CURSOR: 
          case PICKUP_SOME: 
          case PLACE_ALL: 
          case PLACE_ONE: 
          case SWAP_WITH_CURSOR: 
          case UNKNOWN: 
            this.player.updateInventory(this.player.activeContainer);
            break;
          case DROP_ALL_CURSOR: 
          case DROP_ALL_SLOT: 
          case DROP_ONE_CURSOR: 
          case DROP_ONE_SLOT: 
          case HOTBAR_MOVE_AND_READD: 
          case HOTBAR_SWAP: 
          case MOVE_TO_OTHER_INVENTORY: 
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.b(), this.player.activeContainer.getSlot(packetplayinwindowclick.b()).getItem()));
            break;
          case PICKUP_HALF: 
          case PICKUP_ONE: 
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.b(), this.player.activeContainer.getSlot(packetplayinwindowclick.b()).getItem()));
            break;
          case NOTHING: 
          case PICKUP_ALL: 
          case PLACE_SOME: 
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
            break;
          }
          return;
        }
        if ((event instanceof CraftItemEvent)) {
          this.player.updateInventory(this.player.activeContainer);
        }
      }
      if (ItemStack.matches(packetplayinwindowclick.e(), itemstack))
      {
        this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.a(), packetplayinwindowclick.d(), true));
        this.player.g = true;
        this.player.activeContainer.b();
        this.player.broadcastCarriedItem();
        this.player.g = false;
      }
      else
      {
        this.n.a(this.player.activeContainer.windowId, Short.valueOf(packetplayinwindowclick.d()));
        this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.a(), packetplayinwindowclick.d(), false));
        this.player.activeContainer.a(this.player, false);
        ArrayList arraylist1 = Lists.newArrayList();
        for (int j = 0; j < this.player.activeContainer.c.size(); j++) {
          arraylist1.add(((Slot)this.player.activeContainer.c.get(j)).getItem());
        }
        this.player.a(this.player.activeContainer, arraylist1);
      }
    }
  }
  
  public void a(PacketPlayInEnchantItem packetplayinenchantitem)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinenchantitem, this, this.player.u());
    this.player.resetIdleTimer();
    if ((this.player.activeContainer.windowId == packetplayinenchantitem.a()) && (this.player.activeContainer.c(this.player)) && (!this.player.isSpectator()))
    {
      this.player.activeContainer.a(this.player, packetplayinenchantitem.b());
      this.player.activeContainer.b();
    }
  }
  
  public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinsetcreativeslot, this, this.player.u());
    if (this.player.playerInteractManager.isCreative())
    {
      boolean flag = packetplayinsetcreativeslot.a() < 0;
      ItemStack itemstack = packetplayinsetcreativeslot.getItemStack();
      if ((itemstack != null) && (itemstack.hasTag()) && (itemstack.getTag().hasKeyOfType("BlockEntityTag", 10)))
      {
        NBTTagCompound nbttagcompound = itemstack.getTag().getCompound("BlockEntityTag");
        if ((nbttagcompound.hasKey("x")) && (nbttagcompound.hasKey("y")) && (nbttagcompound.hasKey("z")))
        {
          BlockPosition blockposition = new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
          TileEntity tileentity = this.player.world.getTileEntity(blockposition);
          if (tileentity != null)
          {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            
            tileentity.b(nbttagcompound1);
            nbttagcompound1.remove("x");
            nbttagcompound1.remove("y");
            nbttagcompound1.remove("z");
            itemstack.a("BlockEntityTag", nbttagcompound1);
          }
        }
      }
      boolean flag1 = (packetplayinsetcreativeslot.a() >= 1) && (packetplayinsetcreativeslot.a() < 36 + PlayerInventory.getHotbarSize());
      
      boolean flag2 = (itemstack == null) || ((itemstack.getItem() != null) && ((!invalidItems.contains(Integer.valueOf(Item.getId(itemstack.getItem())))) || (!SpigotConfig.filterCreativeItems)));
      boolean flag3 = (itemstack == null) || ((itemstack.getData() >= 0) && (itemstack.count <= 64) && (itemstack.count > 0));
      if ((flag) || ((flag1) && (!ItemStack.matches(this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.a()).getItem(), packetplayinsetcreativeslot.getItemStack()))))
      {
        HumanEntity player = this.player.getBukkitEntity();
        InventoryView inventory = new CraftInventoryView(player, player.getInventory(), this.player.defaultContainer);
        org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(packetplayinsetcreativeslot.getItemStack());
        
        InventoryType.SlotType type = InventoryType.SlotType.QUICKBAR;
        if (flag) {
          type = InventoryType.SlotType.OUTSIDE;
        } else if (packetplayinsetcreativeslot.a() < 36) {
          if ((packetplayinsetcreativeslot.a() >= 5) && (packetplayinsetcreativeslot.a() < 9)) {
            type = InventoryType.SlotType.ARMOR;
          } else {
            type = InventoryType.SlotType.CONTAINER;
          }
        }
        InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type, flag ? 64537 : packetplayinsetcreativeslot.a(), item);
        this.server.getPluginManager().callEvent(event);
        
        itemstack = CraftItemStack.asNMSCopy(event.getCursor());
        switch (event.getResult())
        {
        case DENY: 
          flag2 = flag3 = 1;
          break;
        case DEFAULT: 
          break;
        case ALLOW: 
          if (packetplayinsetcreativeslot.a() >= 0)
          {
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.defaultContainer.windowId, packetplayinsetcreativeslot.a(), this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.a()).getItem()));
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, null));
          }
          return;
        }
      }
      if ((flag1) && (flag2) && (flag3))
      {
        if (itemstack == null) {
          this.player.defaultContainer.setItem(packetplayinsetcreativeslot.a(), null);
        } else {
          this.player.defaultContainer.setItem(packetplayinsetcreativeslot.a(), itemstack);
        }
        this.player.defaultContainer.a(this.player, true);
      }
      else if ((flag) && (flag2) && (flag3) && (this.m < 200))
      {
        this.m += 20;
        EntityItem entityitem = this.player.drop(itemstack, true);
        if (entityitem != null) {
          entityitem.j();
        }
      }
    }
  }
  
  public void a(PacketPlayInTransaction packetplayintransaction)
  {
    if (this.player.dead) {
      return;
    }
    PlayerConnectionUtils.ensureMainThread(packetplayintransaction, this, this.player.u());
    Short oshort = (Short)this.n.get(this.player.activeContainer.windowId);
    if ((oshort != null) && (packetplayintransaction.b() == oshort.shortValue()) && (this.player.activeContainer.windowId == packetplayintransaction.a()) && (!this.player.activeContainer.c(this.player)) && (!this.player.isSpectator())) {
      this.player.activeContainer.a(this.player, true);
    }
  }
  
  public void a(PacketPlayInUpdateSign packetplayinupdatesign)
  {
    if (this.player.dead) {
      return;
    }
    PlayerConnectionUtils.ensureMainThread(packetplayinupdatesign, this, this.player.u());
    this.player.resetIdleTimer();
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
    BlockPosition blockposition = packetplayinupdatesign.a();
    if (worldserver.isLoaded(blockposition))
    {
      TileEntity tileentity = worldserver.getTileEntity(blockposition);
      if (!(tileentity instanceof TileEntitySign)) {
        return;
      }
      TileEntitySign tileentitysign = (TileEntitySign)tileentity;
      if ((!tileentitysign.b()) || (tileentitysign.c() != this.player))
      {
        this.minecraftServer.warning("Player " + this.player.getName() + " just tried to change non-editable sign");
        sendPacket(new PacketPlayOutUpdateSign(tileentity.world, packetplayinupdatesign.a(), tileentitysign.lines));
        return;
      }
      IChatBaseComponent[] aichatbasecomponent = packetplayinupdatesign.b();
      
      Player player = this.server.getPlayer(this.player);
      int x = packetplayinupdatesign.a().getX();
      int y = packetplayinupdatesign.a().getY();
      int z = packetplayinupdatesign.a().getZ();
      String[] lines = new String[4];
      for (int i = 0; i < aichatbasecomponent.length; i++) {
        lines[i] = EnumChatFormat.a(aichatbasecomponent[i].c());
      }
      SignChangeEvent event = new SignChangeEvent((CraftBlock)player.getWorld().getBlockAt(x, y, z), this.server.getPlayer(this.player), lines);
      this.server.getPluginManager().callEvent(event);
      if (!event.isCancelled())
      {
        System.arraycopy(CraftSign.sanitizeLines(event.getLines()), 0, tileentitysign.lines, 0, 4);
        tileentitysign.isEditable = false;
      }
      tileentitysign.update();
      worldserver.notify(blockposition);
    }
  }
  
  public void a(PacketPlayInKeepAlive packetplayinkeepalive)
  {
    if (packetplayinkeepalive.a() == this.i)
    {
      int i = (int)(d() - this.j);
      
      this.player.ping = ((this.player.ping * 3 + i) / 4);
    }
  }
  
  private long d()
  {
    return System.nanoTime() / 1000000L;
  }
  
  public void a(PacketPlayInAbilities packetplayinabilities)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinabilities, this, this.player.u());
    if ((this.player.abilities.canFly) && (this.player.abilities.isFlying != packetplayinabilities.isFlying()))
    {
      PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player), packetplayinabilities.isFlying());
      this.server.getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
        this.player.abilities.isFlying = packetplayinabilities.isFlying();
      } else {
        this.player.updateAbilities();
      }
    }
  }
  
  public void a(PacketPlayInTabComplete packetplayintabcomplete)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayintabcomplete, this, this.player.u());
    if ((chatSpamField.addAndGet(this, 10) > 500) && (!this.minecraftServer.getPlayerList().isOp(this.player.getProfile())))
    {
      disconnect("disconnect.spam");
      return;
    }
    ArrayList arraylist = Lists.newArrayList();
    Iterator iterator = this.minecraftServer.tabCompleteCommand(this.player, packetplayintabcomplete.a(), packetplayintabcomplete.b()).iterator();
    while (iterator.hasNext())
    {
      String s = (String)iterator.next();
      
      arraylist.add(s);
    }
    this.player.playerConnection.sendPacket(new PacketPlayOutTabComplete((String[])arraylist.toArray(new String[arraylist.size()])));
  }
  
  public void a(PacketPlayInSettings packetplayinsettings)
  {
    PlayerConnectionUtils.ensureMainThread(packetplayinsettings, this, this.player.u());
    this.player.a(packetplayinsettings);
  }
  
  /* Error */
  public void a(PacketPlayInCustomPayload packetplayincustompayload)
  {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: aload_0
    //   3: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   6: invokevirtual 418	net/minecraft/server/v1_8_R3/EntityPlayer:u	()Lnet/minecraft/server/v1_8_R3/WorldServer;
    //   9: invokestatic 424	net/minecraft/server/v1_8_R3/PlayerConnectionUtils:ensureMainThread	(Lnet/minecraft/server/v1_8_R3/Packet;Lnet/minecraft/server/v1_8_R3/PacketListener;Lnet/minecraft/server/v1_8_R3/IAsyncTaskHandler;)V
    //   12: ldc_w 2590
    //   15: aload_1
    //   16: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   19: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   22: ifeq +184 -> 206
    //   25: new 2595	net/minecraft/server/v1_8_R3/PacketDataSerializer
    //   28: dup
    //   29: aload_1
    //   30: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   33: invokestatic 2604	io/netty/buffer/Unpooled:wrappedBuffer	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
    //   36: invokespecial 2607	net/minecraft/server/v1_8_R3/PacketDataSerializer:<init>	(Lio/netty/buffer/ByteBuf;)V
    //   39: astore_2
    //   40: aload_2
    //   41: invokevirtual 2609	net/minecraft/server/v1_8_R3/PacketDataSerializer:i	()Lnet/minecraft/server/v1_8_R3/ItemStack;
    //   44: astore_3
    //   45: aload_3
    //   46: ifnonnull +9 -> 55
    //   49: aload_2
    //   50: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   53: pop
    //   54: return
    //   55: aload_3
    //   56: invokevirtual 2327	net/minecraft/server/v1_8_R3/ItemStack:getTag	()Lnet/minecraft/server/v1_8_R3/NBTTagCompound;
    //   59: invokestatic 2617	net/minecraft/server/v1_8_R3/ItemBookAndQuill:b	(Lnet/minecraft/server/v1_8_R3/NBTTagCompound;)Z
    //   62: ifne +14 -> 76
    //   65: new 2619	java/io/IOException
    //   68: dup
    //   69: ldc_w 2621
    //   72: invokespecial 2622	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   75: athrow
    //   76: aload_0
    //   77: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   80: getfield 973	net/minecraft/server/v1_8_R3/EntityPlayer:inventory	Lnet/minecraft/server/v1_8_R3/PlayerInventory;
    //   83: invokevirtual 979	net/minecraft/server/v1_8_R3/PlayerInventory:getItemInHand	()Lnet/minecraft/server/v1_8_R3/ItemStack;
    //   86: astore 4
    //   88: aload 4
    //   90: ifnull +110 -> 200
    //   93: aload_3
    //   94: invokevirtual 1158	net/minecraft/server/v1_8_R3/ItemStack:getItem	()Lnet/minecraft/server/v1_8_R3/Item;
    //   97: getstatic 2625	net/minecraft/server/v1_8_R3/Items:WRITABLE_BOOK	Lnet/minecraft/server/v1_8_R3/Item;
    //   100: if_acmpne +56 -> 156
    //   103: aload_3
    //   104: invokevirtual 1158	net/minecraft/server/v1_8_R3/ItemStack:getItem	()Lnet/minecraft/server/v1_8_R3/Item;
    //   107: aload 4
    //   109: invokevirtual 1158	net/minecraft/server/v1_8_R3/ItemStack:getItem	()Lnet/minecraft/server/v1_8_R3/Item;
    //   112: if_acmpne +44 -> 156
    //   115: new 1064	net/minecraft/server/v1_8_R3/ItemStack
    //   118: dup
    //   119: getstatic 2625	net/minecraft/server/v1_8_R3/Items:WRITABLE_BOOK	Lnet/minecraft/server/v1_8_R3/Item;
    //   122: invokespecial 2628	net/minecraft/server/v1_8_R3/ItemStack:<init>	(Lnet/minecraft/server/v1_8_R3/Item;)V
    //   125: astore 4
    //   127: aload 4
    //   129: ldc_w 2630
    //   132: aload_3
    //   133: invokevirtual 2327	net/minecraft/server/v1_8_R3/ItemStack:getTag	()Lnet/minecraft/server/v1_8_R3/NBTTagCompound;
    //   136: ldc_w 2630
    //   139: bipush 8
    //   141: invokevirtual 2634	net/minecraft/server/v1_8_R3/NBTTagCompound:getList	(Ljava/lang/String;I)Lnet/minecraft/server/v1_8_R3/NBTTagList;
    //   144: invokevirtual 2363	net/minecraft/server/v1_8_R3/ItemStack:a	(Ljava/lang/String;Lnet/minecraft/server/v1_8_R3/NBTBase;)V
    //   147: aload_0
    //   148: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   151: aload 4
    //   153: invokestatic 2638	org/bukkit/craftbukkit/v1_8_R3/event/CraftEventFactory:handleEditBookEvent	(Lnet/minecraft/server/v1_8_R3/EntityPlayer;Lnet/minecraft/server/v1_8_R3/ItemStack;)V
    //   156: aload_2
    //   157: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   160: pop
    //   161: return
    //   162: astore 5
    //   164: getstatic 148	net/minecraft/server/v1_8_R3/PlayerConnection:c	Lorg/apache/logging/log4j/Logger;
    //   167: ldc_w 2640
    //   170: aload 5
    //   172: invokeinterface 2643 3 0
    //   177: aload_0
    //   178: ldc_w 2645
    //   181: invokevirtual 315	net/minecraft/server/v1_8_R3/PlayerConnection:disconnect	(Ljava/lang/String;)V
    //   184: aload_2
    //   185: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   188: pop
    //   189: return
    //   190: astore 6
    //   192: aload_2
    //   193: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   196: pop
    //   197: aload 6
    //   199: athrow
    //   200: aload_2
    //   201: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   204: pop
    //   205: return
    //   206: ldc_w 2647
    //   209: aload_1
    //   210: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   213: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   216: ifeq +238 -> 454
    //   219: new 2595	net/minecraft/server/v1_8_R3/PacketDataSerializer
    //   222: dup
    //   223: aload_1
    //   224: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   227: invokestatic 2604	io/netty/buffer/Unpooled:wrappedBuffer	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
    //   230: invokespecial 2607	net/minecraft/server/v1_8_R3/PacketDataSerializer:<init>	(Lio/netty/buffer/ByteBuf;)V
    //   233: astore_2
    //   234: aload_2
    //   235: invokevirtual 2609	net/minecraft/server/v1_8_R3/PacketDataSerializer:i	()Lnet/minecraft/server/v1_8_R3/ItemStack;
    //   238: astore_3
    //   239: aload_3
    //   240: ifnonnull +9 -> 249
    //   243: aload_2
    //   244: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   247: pop
    //   248: return
    //   249: aload_3
    //   250: invokevirtual 2327	net/minecraft/server/v1_8_R3/ItemStack:getTag	()Lnet/minecraft/server/v1_8_R3/NBTTagCompound;
    //   253: invokestatic 2650	net/minecraft/server/v1_8_R3/ItemWrittenBook:b	(Lnet/minecraft/server/v1_8_R3/NBTTagCompound;)Z
    //   256: ifne +14 -> 270
    //   259: new 2619	java/io/IOException
    //   262: dup
    //   263: ldc_w 2621
    //   266: invokespecial 2622	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   269: athrow
    //   270: aload_0
    //   271: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   274: getfield 973	net/minecraft/server/v1_8_R3/EntityPlayer:inventory	Lnet/minecraft/server/v1_8_R3/PlayerInventory;
    //   277: invokevirtual 979	net/minecraft/server/v1_8_R3/PlayerInventory:getItemInHand	()Lnet/minecraft/server/v1_8_R3/ItemStack;
    //   280: astore 4
    //   282: aload 4
    //   284: ifnull +164 -> 448
    //   287: aload_3
    //   288: invokevirtual 1158	net/minecraft/server/v1_8_R3/ItemStack:getItem	()Lnet/minecraft/server/v1_8_R3/Item;
    //   291: getstatic 2653	net/minecraft/server/v1_8_R3/Items:WRITTEN_BOOK	Lnet/minecraft/server/v1_8_R3/Item;
    //   294: if_acmpne +110 -> 404
    //   297: aload 4
    //   299: invokevirtual 1158	net/minecraft/server/v1_8_R3/ItemStack:getItem	()Lnet/minecraft/server/v1_8_R3/Item;
    //   302: getstatic 2625	net/minecraft/server/v1_8_R3/Items:WRITABLE_BOOK	Lnet/minecraft/server/v1_8_R3/Item;
    //   305: if_acmpne +99 -> 404
    //   308: new 1064	net/minecraft/server/v1_8_R3/ItemStack
    //   311: dup
    //   312: getstatic 2653	net/minecraft/server/v1_8_R3/Items:WRITTEN_BOOK	Lnet/minecraft/server/v1_8_R3/Item;
    //   315: invokespecial 2628	net/minecraft/server/v1_8_R3/ItemStack:<init>	(Lnet/minecraft/server/v1_8_R3/Item;)V
    //   318: astore 4
    //   320: aload 4
    //   322: ldc_w 2655
    //   325: new 2657	net/minecraft/server/v1_8_R3/NBTTagString
    //   328: dup
    //   329: aload_0
    //   330: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   333: invokevirtual 334	net/minecraft/server/v1_8_R3/EntityPlayer:getName	()Ljava/lang/String;
    //   336: invokespecial 2658	net/minecraft/server/v1_8_R3/NBTTagString:<init>	(Ljava/lang/String;)V
    //   339: invokevirtual 2363	net/minecraft/server/v1_8_R3/ItemStack:a	(Ljava/lang/String;Lnet/minecraft/server/v1_8_R3/NBTBase;)V
    //   342: aload 4
    //   344: ldc_w 2660
    //   347: new 2657	net/minecraft/server/v1_8_R3/NBTTagString
    //   350: dup
    //   351: aload_3
    //   352: invokevirtual 2327	net/minecraft/server/v1_8_R3/ItemStack:getTag	()Lnet/minecraft/server/v1_8_R3/NBTTagCompound;
    //   355: ldc_w 2660
    //   358: invokevirtual 2663	net/minecraft/server/v1_8_R3/NBTTagCompound:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   361: invokespecial 2658	net/minecraft/server/v1_8_R3/NBTTagString:<init>	(Ljava/lang/String;)V
    //   364: invokevirtual 2363	net/minecraft/server/v1_8_R3/ItemStack:a	(Ljava/lang/String;Lnet/minecraft/server/v1_8_R3/NBTBase;)V
    //   367: aload 4
    //   369: ldc_w 2630
    //   372: aload_3
    //   373: invokevirtual 2327	net/minecraft/server/v1_8_R3/ItemStack:getTag	()Lnet/minecraft/server/v1_8_R3/NBTTagCompound;
    //   376: ldc_w 2630
    //   379: bipush 8
    //   381: invokevirtual 2634	net/minecraft/server/v1_8_R3/NBTTagCompound:getList	(Ljava/lang/String;I)Lnet/minecraft/server/v1_8_R3/NBTTagList;
    //   384: invokevirtual 2363	net/minecraft/server/v1_8_R3/ItemStack:a	(Ljava/lang/String;Lnet/minecraft/server/v1_8_R3/NBTBase;)V
    //   387: aload 4
    //   389: getstatic 2653	net/minecraft/server/v1_8_R3/Items:WRITTEN_BOOK	Lnet/minecraft/server/v1_8_R3/Item;
    //   392: invokevirtual 2665	net/minecraft/server/v1_8_R3/ItemStack:setItem	(Lnet/minecraft/server/v1_8_R3/Item;)V
    //   395: aload_0
    //   396: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   399: aload 4
    //   401: invokestatic 2638	org/bukkit/craftbukkit/v1_8_R3/event/CraftEventFactory:handleEditBookEvent	(Lnet/minecraft/server/v1_8_R3/EntityPlayer;Lnet/minecraft/server/v1_8_R3/ItemStack;)V
    //   404: aload_2
    //   405: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   408: pop
    //   409: return
    //   410: astore 5
    //   412: getstatic 148	net/minecraft/server/v1_8_R3/PlayerConnection:c	Lorg/apache/logging/log4j/Logger;
    //   415: ldc_w 2667
    //   418: aload 5
    //   420: invokeinterface 2643 3 0
    //   425: aload_0
    //   426: ldc_w 2645
    //   429: invokevirtual 315	net/minecraft/server/v1_8_R3/PlayerConnection:disconnect	(Ljava/lang/String;)V
    //   432: aload_2
    //   433: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   436: pop
    //   437: return
    //   438: astore 6
    //   440: aload_2
    //   441: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   444: pop
    //   445: aload 6
    //   447: athrow
    //   448: aload_2
    //   449: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   452: pop
    //   453: return
    //   454: ldc_w 2669
    //   457: aload_1
    //   458: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   461: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   464: ifeq +67 -> 531
    //   467: aload_1
    //   468: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   471: invokevirtual 2672	net/minecraft/server/v1_8_R3/PacketDataSerializer:readInt	()I
    //   474: istore 5
    //   476: aload_0
    //   477: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   480: getfield 1247	net/minecraft/server/v1_8_R3/EntityPlayer:activeContainer	Lnet/minecraft/server/v1_8_R3/Container;
    //   483: astore 6
    //   485: aload 6
    //   487: instanceof 2674
    //   490: ifeq +813 -> 1303
    //   493: aload 6
    //   495: checkcast 2674	net/minecraft/server/v1_8_R3/ContainerMerchant
    //   498: iload 5
    //   500: invokevirtual 2676	net/minecraft/server/v1_8_R3/ContainerMerchant:d	(I)V
    //   503: goto +800 -> 1303
    //   506: astore 5
    //   508: getstatic 148	net/minecraft/server/v1_8_R3/PlayerConnection:c	Lorg/apache/logging/log4j/Logger;
    //   511: ldc_w 2678
    //   514: aload 5
    //   516: invokeinterface 2643 3 0
    //   521: aload_0
    //   522: ldc_w 2680
    //   525: invokevirtual 315	net/minecraft/server/v1_8_R3/PlayerConnection:disconnect	(Ljava/lang/String;)V
    //   528: goto +775 -> 1303
    //   531: ldc_w 2682
    //   534: aload_1
    //   535: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   538: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   541: ifeq +326 -> 867
    //   544: aload_0
    //   545: getfield 221	net/minecraft/server/v1_8_R3/PlayerConnection:minecraftServer	Lnet/minecraft/server/v1_8_R3/MinecraftServer;
    //   548: invokevirtual 2685	net/minecraft/server/v1_8_R3/MinecraftServer:getEnableCommandBlock	()Z
    //   551: ifne +27 -> 578
    //   554: aload_0
    //   555: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   558: new 1175	net/minecraft/server/v1_8_R3/ChatMessage
    //   561: dup
    //   562: ldc_w 2687
    //   565: iconst_0
    //   566: anewarray 4	java/lang/Object
    //   569: invokespecial 1180	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   572: invokevirtual 2689	net/minecraft/server/v1_8_R3/EntityPlayer:sendMessage	(Lnet/minecraft/server/v1_8_R3/IChatBaseComponent;)V
    //   575: goto +728 -> 1303
    //   578: aload_0
    //   579: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   582: invokevirtual 249	net/minecraft/server/v1_8_R3/EntityPlayer:getBukkitEntity	()Lorg/bukkit/craftbukkit/v1_8_R3/entity/CraftPlayer;
    //   585: invokevirtual 2691	org/bukkit/craftbukkit/v1_8_R3/entity/CraftPlayer:isOp	()Z
    //   588: ifeq +255 -> 843
    //   591: aload_0
    //   592: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   595: getfield 799	net/minecraft/server/v1_8_R3/EntityPlayer:abilities	Lnet/minecraft/server/v1_8_R3/PlayerAbilities;
    //   598: getfield 2166	net/minecraft/server/v1_8_R3/PlayerAbilities:canInstantlyBuild	Z
    //   601: ifeq +242 -> 843
    //   604: aload_1
    //   605: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   608: astore_2
    //   609: aload_2
    //   610: invokevirtual 2695	net/minecraft/server/v1_8_R3/PacketDataSerializer:readByte	()B
    //   613: istore 5
    //   615: aconst_null
    //   616: astore 6
    //   618: iload 5
    //   620: ifne +55 -> 675
    //   623: aload_0
    //   624: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   627: getfield 1104	net/minecraft/server/v1_8_R3/EntityPlayer:world	Lnet/minecraft/server/v1_8_R3/World;
    //   630: new 915	net/minecraft/server/v1_8_R3/BlockPosition
    //   633: dup
    //   634: aload_2
    //   635: invokevirtual 2672	net/minecraft/server/v1_8_R3/PacketDataSerializer:readInt	()I
    //   638: aload_2
    //   639: invokevirtual 2672	net/minecraft/server/v1_8_R3/PacketDataSerializer:readInt	()I
    //   642: aload_2
    //   643: invokevirtual 2672	net/minecraft/server/v1_8_R3/PacketDataSerializer:readInt	()I
    //   646: invokespecial 2352	net/minecraft/server/v1_8_R3/BlockPosition:<init>	(III)V
    //   649: invokevirtual 2353	net/minecraft/server/v1_8_R3/World:getTileEntity	(Lnet/minecraft/server/v1_8_R3/BlockPosition;)Lnet/minecraft/server/v1_8_R3/TileEntity;
    //   652: astore 7
    //   654: aload 7
    //   656: instanceof 2697
    //   659: ifeq +56 -> 715
    //   662: aload 7
    //   664: checkcast 2697	net/minecraft/server/v1_8_R3/TileEntityCommand
    //   667: invokevirtual 2701	net/minecraft/server/v1_8_R3/TileEntityCommand:getCommandBlock	()Lnet/minecraft/server/v1_8_R3/CommandBlockListenerAbstract;
    //   670: astore 6
    //   672: goto +43 -> 715
    //   675: iload 5
    //   677: iconst_1
    //   678: if_icmpne +37 -> 715
    //   681: aload_0
    //   682: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   685: getfield 1104	net/minecraft/server/v1_8_R3/EntityPlayer:world	Lnet/minecraft/server/v1_8_R3/World;
    //   688: aload_2
    //   689: invokevirtual 2672	net/minecraft/server/v1_8_R3/PacketDataSerializer:readInt	()I
    //   692: invokevirtual 2706	net/minecraft/server/v1_8_R3/World:a	(I)Lnet/minecraft/server/v1_8_R3/Entity;
    //   695: astore 7
    //   697: aload 7
    //   699: instanceof 2708
    //   702: ifeq +13 -> 715
    //   705: aload 7
    //   707: checkcast 2708	net/minecraft/server/v1_8_R3/EntityMinecartCommandBlock
    //   710: invokevirtual 2709	net/minecraft/server/v1_8_R3/EntityMinecartCommandBlock:getCommandBlock	()Lnet/minecraft/server/v1_8_R3/CommandBlockListenerAbstract;
    //   713: astore 6
    //   715: aload_2
    //   716: aload_2
    //   717: invokevirtual 2712	net/minecraft/server/v1_8_R3/PacketDataSerializer:readableBytes	()I
    //   720: invokevirtual 2714	net/minecraft/server/v1_8_R3/PacketDataSerializer:c	(I)Ljava/lang/String;
    //   723: astore 7
    //   725: aload_2
    //   726: invokevirtual 2717	net/minecraft/server/v1_8_R3/PacketDataSerializer:readBoolean	()Z
    //   729: istore 8
    //   731: aload 6
    //   733: ifnull +102 -> 835
    //   736: aload 6
    //   738: aload 7
    //   740: invokevirtual 2720	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:setCommand	(Ljava/lang/String;)V
    //   743: aload 6
    //   745: iload 8
    //   747: invokevirtual 2721	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:a	(Z)V
    //   750: iload 8
    //   752: ifne +9 -> 761
    //   755: aload 6
    //   757: aconst_null
    //   758: invokevirtual 2723	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:b	(Lnet/minecraft/server/v1_8_R3/IChatBaseComponent;)V
    //   761: aload 6
    //   763: invokevirtual 2725	net/minecraft/server/v1_8_R3/CommandBlockListenerAbstract:h	()V
    //   766: aload_0
    //   767: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   770: new 1175	net/minecraft/server/v1_8_R3/ChatMessage
    //   773: dup
    //   774: ldc_w 2727
    //   777: iconst_1
    //   778: anewarray 4	java/lang/Object
    //   781: dup
    //   782: iconst_0
    //   783: aload 7
    //   785: aastore
    //   786: invokespecial 1180	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   789: invokevirtual 2689	net/minecraft/server/v1_8_R3/EntityPlayer:sendMessage	(Lnet/minecraft/server/v1_8_R3/IChatBaseComponent;)V
    //   792: goto +43 -> 835
    //   795: astore 5
    //   797: getstatic 148	net/minecraft/server/v1_8_R3/PlayerConnection:c	Lorg/apache/logging/log4j/Logger;
    //   800: ldc_w 2729
    //   803: aload 5
    //   805: invokeinterface 2643 3 0
    //   810: aload_0
    //   811: ldc_w 2731
    //   814: invokevirtual 315	net/minecraft/server/v1_8_R3/PlayerConnection:disconnect	(Ljava/lang/String;)V
    //   817: aload_2
    //   818: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   821: pop
    //   822: goto +481 -> 1303
    //   825: astore 9
    //   827: aload_2
    //   828: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   831: pop
    //   832: aload 9
    //   834: athrow
    //   835: aload_2
    //   836: invokevirtual 2612	net/minecraft/server/v1_8_R3/PacketDataSerializer:release	()Z
    //   839: pop
    //   840: goto +463 -> 1303
    //   843: aload_0
    //   844: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   847: new 1175	net/minecraft/server/v1_8_R3/ChatMessage
    //   850: dup
    //   851: ldc_w 2733
    //   854: iconst_0
    //   855: anewarray 4	java/lang/Object
    //   858: invokespecial 1180	net/minecraft/server/v1_8_R3/ChatMessage:<init>	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   861: invokevirtual 2689	net/minecraft/server/v1_8_R3/EntityPlayer:sendMessage	(Lnet/minecraft/server/v1_8_R3/IChatBaseComponent;)V
    //   864: goto +439 -> 1303
    //   867: ldc_w 2735
    //   870: aload_1
    //   871: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   874: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   877: ifeq +130 -> 1007
    //   880: aload_0
    //   881: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   884: getfield 1247	net/minecraft/server/v1_8_R3/EntityPlayer:activeContainer	Lnet/minecraft/server/v1_8_R3/Container;
    //   887: instanceof 2737
    //   890: ifeq +413 -> 1303
    //   893: aload_1
    //   894: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   897: astore_2
    //   898: aload_2
    //   899: invokevirtual 2672	net/minecraft/server/v1_8_R3/PacketDataSerializer:readInt	()I
    //   902: istore 5
    //   904: aload_2
    //   905: invokevirtual 2672	net/minecraft/server/v1_8_R3/PacketDataSerializer:readInt	()I
    //   908: istore 6
    //   910: aload_0
    //   911: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   914: getfield 1247	net/minecraft/server/v1_8_R3/EntityPlayer:activeContainer	Lnet/minecraft/server/v1_8_R3/Container;
    //   917: checkcast 2737	net/minecraft/server/v1_8_R3/ContainerBeacon
    //   920: astore 7
    //   922: aload 7
    //   924: iconst_0
    //   925: invokevirtual 2738	net/minecraft/server/v1_8_R3/ContainerBeacon:getSlot	(I)Lnet/minecraft/server/v1_8_R3/Slot;
    //   928: astore 8
    //   930: aload 8
    //   932: invokevirtual 2139	net/minecraft/server/v1_8_R3/Slot:hasItem	()Z
    //   935: ifeq +368 -> 1303
    //   938: aload 8
    //   940: iconst_1
    //   941: invokevirtual 2740	net/minecraft/server/v1_8_R3/Slot:a	(I)Lnet/minecraft/server/v1_8_R3/ItemStack;
    //   944: pop
    //   945: aload 7
    //   947: invokevirtual 2743	net/minecraft/server/v1_8_R3/ContainerBeacon:e	()Lnet/minecraft/server/v1_8_R3/IInventory;
    //   950: astore 9
    //   952: aload 9
    //   954: iconst_1
    //   955: iload 5
    //   957: invokeinterface 2746 3 0
    //   962: aload 9
    //   964: iconst_2
    //   965: iload 6
    //   967: invokeinterface 2746 3 0
    //   972: aload 9
    //   974: invokeinterface 2747 1 0
    //   979: goto +324 -> 1303
    //   982: astore 5
    //   984: getstatic 148	net/minecraft/server/v1_8_R3/PlayerConnection:c	Lorg/apache/logging/log4j/Logger;
    //   987: ldc_w 2749
    //   990: aload 5
    //   992: invokeinterface 2643 3 0
    //   997: aload_0
    //   998: ldc_w 2751
    //   1001: invokevirtual 315	net/minecraft/server/v1_8_R3/PlayerConnection:disconnect	(Ljava/lang/String;)V
    //   1004: goto +299 -> 1303
    //   1007: ldc_w 2753
    //   1010: aload_1
    //   1011: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   1014: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1017: ifeq +92 -> 1109
    //   1020: aload_0
    //   1021: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   1024: getfield 1247	net/minecraft/server/v1_8_R3/EntityPlayer:activeContainer	Lnet/minecraft/server/v1_8_R3/Container;
    //   1027: instanceof 2755
    //   1030: ifeq +79 -> 1109
    //   1033: aload_0
    //   1034: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   1037: getfield 1247	net/minecraft/server/v1_8_R3/EntityPlayer:activeContainer	Lnet/minecraft/server/v1_8_R3/Container;
    //   1040: checkcast 2755	net/minecraft/server/v1_8_R3/ContainerAnvil
    //   1043: astore 5
    //   1045: aload_1
    //   1046: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   1049: ifnull +49 -> 1098
    //   1052: aload_1
    //   1053: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   1056: invokevirtual 2712	net/minecraft/server/v1_8_R3/PacketDataSerializer:readableBytes	()I
    //   1059: iconst_1
    //   1060: if_icmplt +38 -> 1098
    //   1063: aload_1
    //   1064: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   1067: sipush 32767
    //   1070: invokevirtual 2714	net/minecraft/server/v1_8_R3/PacketDataSerializer:c	(I)Ljava/lang/String;
    //   1073: invokestatic 2756	net/minecraft/server/v1_8_R3/SharedConstants:a	(Ljava/lang/String;)Ljava/lang/String;
    //   1076: astore 6
    //   1078: aload 6
    //   1080: invokevirtual 1367	java/lang/String:length	()I
    //   1083: bipush 30
    //   1085: if_icmpgt +218 -> 1303
    //   1088: aload 5
    //   1090: aload 6
    //   1092: invokevirtual 2757	net/minecraft/server/v1_8_R3/ContainerAnvil:a	(Ljava/lang/String;)V
    //   1095: goto +208 -> 1303
    //   1098: aload 5
    //   1100: ldc_w 2759
    //   1103: invokevirtual 2757	net/minecraft/server/v1_8_R3/ContainerAnvil:a	(Ljava/lang/String;)V
    //   1106: goto +197 -> 1303
    //   1109: aload_1
    //   1110: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   1113: ldc_w 2761
    //   1116: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1119: ifeq +64 -> 1183
    //   1122: aload_1
    //   1123: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   1126: getstatic 2767	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   1129: invokevirtual 2770	net/minecraft/server/v1_8_R3/PacketDataSerializer:toString	(Ljava/nio/charset/Charset;)Ljava/lang/String;
    //   1132: astore 5
    //   1134: aload 5
    //   1136: ldc_w 2772
    //   1139: invokevirtual 2776	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   1142: dup
    //   1143: astore 9
    //   1145: arraylength
    //   1146: istore 8
    //   1148: iconst_0
    //   1149: istore 7
    //   1151: goto +22 -> 1173
    //   1154: aload 9
    //   1156: iload 7
    //   1158: aaload
    //   1159: astore 6
    //   1161: aload_0
    //   1162: invokevirtual 505	net/minecraft/server/v1_8_R3/PlayerConnection:getPlayer	()Lorg/bukkit/craftbukkit/v1_8_R3/entity/CraftPlayer;
    //   1165: aload 6
    //   1167: invokevirtual 2779	org/bukkit/craftbukkit/v1_8_R3/entity/CraftPlayer:addChannel	(Ljava/lang/String;)V
    //   1170: iinc 7 1
    //   1173: iload 7
    //   1175: iload 8
    //   1177: if_icmplt -23 -> 1154
    //   1180: goto +123 -> 1303
    //   1183: aload_1
    //   1184: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   1187: ldc_w 2781
    //   1190: invokevirtual 713	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1193: ifeq +64 -> 1257
    //   1196: aload_1
    //   1197: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   1200: getstatic 2767	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   1203: invokevirtual 2770	net/minecraft/server/v1_8_R3/PacketDataSerializer:toString	(Ljava/nio/charset/Charset;)Ljava/lang/String;
    //   1206: astore 5
    //   1208: aload 5
    //   1210: ldc_w 2772
    //   1213: invokevirtual 2776	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   1216: dup
    //   1217: astore 9
    //   1219: arraylength
    //   1220: istore 8
    //   1222: iconst_0
    //   1223: istore 7
    //   1225: goto +22 -> 1247
    //   1228: aload 9
    //   1230: iload 7
    //   1232: aaload
    //   1233: astore 6
    //   1235: aload_0
    //   1236: invokevirtual 505	net/minecraft/server/v1_8_R3/PlayerConnection:getPlayer	()Lorg/bukkit/craftbukkit/v1_8_R3/entity/CraftPlayer;
    //   1239: aload 6
    //   1241: invokevirtual 2784	org/bukkit/craftbukkit/v1_8_R3/entity/CraftPlayer:removeChannel	(Ljava/lang/String;)V
    //   1244: iinc 7 1
    //   1247: iload 7
    //   1249: iload 8
    //   1251: if_icmplt -23 -> 1228
    //   1254: goto +49 -> 1303
    //   1257: aload_1
    //   1258: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   1261: invokevirtual 2712	net/minecraft/server/v1_8_R3/PacketDataSerializer:readableBytes	()I
    //   1264: newarray <illegal type>
    //   1266: astore 5
    //   1268: aload_1
    //   1269: invokevirtual 2598	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:b	()Lnet/minecraft/server/v1_8_R3/PacketDataSerializer;
    //   1272: aload 5
    //   1274: invokevirtual 2788	net/minecraft/server/v1_8_R3/PacketDataSerializer:readBytes	([B)Lio/netty/buffer/ByteBuf;
    //   1277: pop
    //   1278: aload_0
    //   1279: getfield 240	net/minecraft/server/v1_8_R3/PlayerConnection:server	Lorg/bukkit/craftbukkit/v1_8_R3/CraftServer;
    //   1282: invokevirtual 2792	org/bukkit/craftbukkit/v1_8_R3/CraftServer:getMessenger	()Lorg/bukkit/plugin/messaging/Messenger;
    //   1285: aload_0
    //   1286: getfield 231	net/minecraft/server/v1_8_R3/PlayerConnection:player	Lnet/minecraft/server/v1_8_R3/EntityPlayer;
    //   1289: invokevirtual 249	net/minecraft/server/v1_8_R3/EntityPlayer:getBukkitEntity	()Lorg/bukkit/craftbukkit/v1_8_R3/entity/CraftPlayer;
    //   1292: aload_1
    //   1293: invokevirtual 2593	net/minecraft/server/v1_8_R3/PacketPlayInCustomPayload:a	()Ljava/lang/String;
    //   1296: aload 5
    //   1298: invokeinterface 2798 4 0
    //   1303: return
    // Line number table:
    //   Java source line #1905	-> byte code offset #0
    //   Java source line #1910	-> byte code offset #12
    //   Java source line #1911	-> byte code offset #25
    //   Java source line #1914	-> byte code offset #40
    //   Java source line #1915	-> byte code offset #45
    //   Java source line #1938	-> byte code offset #49
    //   Java source line #1916	-> byte code offset #54
    //   Java source line #1919	-> byte code offset #55
    //   Java source line #1920	-> byte code offset #65
    //   Java source line #1923	-> byte code offset #76
    //   Java source line #1924	-> byte code offset #88
    //   Java source line #1925	-> byte code offset #93
    //   Java source line #1926	-> byte code offset #115
    //   Java source line #1927	-> byte code offset #127
    //   Java source line #1928	-> byte code offset #147
    //   Java source line #1938	-> byte code offset #156
    //   Java source line #1931	-> byte code offset #161
    //   Java source line #1933	-> byte code offset #162
    //   Java source line #1934	-> byte code offset #164
    //   Java source line #1935	-> byte code offset #177
    //   Java source line #1938	-> byte code offset #184
    //   Java source line #1936	-> byte code offset #189
    //   Java source line #1937	-> byte code offset #190
    //   Java source line #1938	-> byte code offset #192
    //   Java source line #1939	-> byte code offset #197
    //   Java source line #1938	-> byte code offset #200
    //   Java source line #1941	-> byte code offset #205
    //   Java source line #1942	-> byte code offset #206
    //   Java source line #1943	-> byte code offset #219
    //   Java source line #1946	-> byte code offset #234
    //   Java source line #1947	-> byte code offset #239
    //   Java source line #1975	-> byte code offset #243
    //   Java source line #1948	-> byte code offset #248
    //   Java source line #1951	-> byte code offset #249
    //   Java source line #1952	-> byte code offset #259
    //   Java source line #1955	-> byte code offset #270
    //   Java source line #1956	-> byte code offset #282
    //   Java source line #1957	-> byte code offset #287
    //   Java source line #1959	-> byte code offset #308
    //   Java source line #1960	-> byte code offset #320
    //   Java source line #1961	-> byte code offset #342
    //   Java source line #1962	-> byte code offset #367
    //   Java source line #1963	-> byte code offset #387
    //   Java source line #1964	-> byte code offset #395
    //   Java source line #1975	-> byte code offset #404
    //   Java source line #1968	-> byte code offset #409
    //   Java source line #1970	-> byte code offset #410
    //   Java source line #1971	-> byte code offset #412
    //   Java source line #1972	-> byte code offset #425
    //   Java source line #1975	-> byte code offset #432
    //   Java source line #1973	-> byte code offset #437
    //   Java source line #1974	-> byte code offset #438
    //   Java source line #1975	-> byte code offset #440
    //   Java source line #1976	-> byte code offset #445
    //   Java source line #1975	-> byte code offset #448
    //   Java source line #1978	-> byte code offset #453
    //   Java source line #1979	-> byte code offset #454
    //   Java source line #1981	-> byte code offset #467
    //   Java source line #1982	-> byte code offset #476
    //   Java source line #1984	-> byte code offset #485
    //   Java source line #1985	-> byte code offset #493
    //   Java source line #1987	-> byte code offset #503
    //   Java source line #1988	-> byte code offset #508
    //   Java source line #1989	-> byte code offset #521
    //   Java source line #1991	-> byte code offset #528
    //   Java source line #1992	-> byte code offset #544
    //   Java source line #1993	-> byte code offset #554
    //   Java source line #1994	-> byte code offset #575
    //   Java source line #1995	-> byte code offset #604
    //   Java source line #1998	-> byte code offset #609
    //   Java source line #1999	-> byte code offset #615
    //   Java source line #2001	-> byte code offset #618
    //   Java source line #2002	-> byte code offset #623
    //   Java source line #2004	-> byte code offset #654
    //   Java source line #2005	-> byte code offset #662
    //   Java source line #2007	-> byte code offset #672
    //   Java source line #2008	-> byte code offset #681
    //   Java source line #2010	-> byte code offset #697
    //   Java source line #2011	-> byte code offset #705
    //   Java source line #2015	-> byte code offset #715
    //   Java source line #2016	-> byte code offset #725
    //   Java source line #2018	-> byte code offset #731
    //   Java source line #2019	-> byte code offset #736
    //   Java source line #2020	-> byte code offset #743
    //   Java source line #2021	-> byte code offset #750
    //   Java source line #2022	-> byte code offset #755
    //   Java source line #2025	-> byte code offset #761
    //   Java source line #2026	-> byte code offset #766
    //   Java source line #2028	-> byte code offset #792
    //   Java source line #2029	-> byte code offset #797
    //   Java source line #2030	-> byte code offset #810
    //   Java source line #2032	-> byte code offset #817
    //   Java source line #2031	-> byte code offset #825
    //   Java source line #2032	-> byte code offset #827
    //   Java source line #2033	-> byte code offset #832
    //   Java source line #2032	-> byte code offset #835
    //   Java source line #2034	-> byte code offset #840
    //   Java source line #2035	-> byte code offset #843
    //   Java source line #2037	-> byte code offset #864
    //   Java source line #2038	-> byte code offset #880
    //   Java source line #2040	-> byte code offset #893
    //   Java source line #2041	-> byte code offset #898
    //   Java source line #2042	-> byte code offset #904
    //   Java source line #2043	-> byte code offset #910
    //   Java source line #2044	-> byte code offset #922
    //   Java source line #2046	-> byte code offset #930
    //   Java source line #2047	-> byte code offset #938
    //   Java source line #2048	-> byte code offset #945
    //   Java source line #2050	-> byte code offset #952
    //   Java source line #2051	-> byte code offset #962
    //   Java source line #2052	-> byte code offset #972
    //   Java source line #2054	-> byte code offset #979
    //   Java source line #2055	-> byte code offset #984
    //   Java source line #2056	-> byte code offset #997
    //   Java source line #2059	-> byte code offset #1004
    //   Java source line #2060	-> byte code offset #1033
    //   Java source line #2062	-> byte code offset #1045
    //   Java source line #2063	-> byte code offset #1063
    //   Java source line #2065	-> byte code offset #1078
    //   Java source line #2066	-> byte code offset #1088
    //   Java source line #2068	-> byte code offset #1095
    //   Java source line #2069	-> byte code offset #1098
    //   Java source line #2071	-> byte code offset #1106
    //   Java source line #2073	-> byte code offset #1109
    //   Java source line #2074	-> byte code offset #1122
    //   Java source line #2075	-> byte code offset #1134
    //   Java source line #2076	-> byte code offset #1161
    //   Java source line #2075	-> byte code offset #1170
    //   Java source line #2078	-> byte code offset #1180
    //   Java source line #2079	-> byte code offset #1196
    //   Java source line #2080	-> byte code offset #1208
    //   Java source line #2081	-> byte code offset #1235
    //   Java source line #2080	-> byte code offset #1244
    //   Java source line #2083	-> byte code offset #1254
    //   Java source line #2084	-> byte code offset #1257
    //   Java source line #2085	-> byte code offset #1268
    //   Java source line #2086	-> byte code offset #1278
    //   Java source line #2089	-> byte code offset #1303
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1304	0	this	PlayerConnection
    //   0	1304	1	packetplayincustompayload	PacketPlayInCustomPayload
    //   39	162	2	packetdataserializer	PacketDataSerializer
    //   233	216	2	packetdataserializer	PacketDataSerializer
    //   608	228	2	packetdataserializer	PacketDataSerializer
    //   897	8	2	packetdataserializer	PacketDataSerializer
    //   44	89	3	itemstack	ItemStack
    //   200	1	3	itemstack	ItemStack
    //   238	135	3	itemstack	ItemStack
    //   448	1	3	itemstack	ItemStack
    //   86	66	4	itemstack1	ItemStack
    //   200	1	4	itemstack1	ItemStack
    //   280	120	4	itemstack1	ItemStack
    //   448	1	4	itemstack1	ItemStack
    //   162	9	5	exception	Exception
    //   410	9	5	exception1	Exception
    //   474	25	5	i	int
    //   506	9	5	exception2	Exception
    //   613	63	5	b0	byte
    //   795	9	5	exception3	Exception
    //   902	54	5	j	int
    //   982	9	5	exception4	Exception
    //   1043	56	5	containeranvil	ContainerAnvil
    //   1132	3	5	channels	String
    //   1206	3	5	channels	String
    //   1266	31	5	data	byte[]
    //   190	8	6	localObject1	Object
    //   438	8	6	localObject2	Object
    //   483	11	6	container	Container
    //   616	146	6	commandblocklistenerabstract	CommandBlockListenerAbstract
    //   908	58	6	k	int
    //   1076	15	6	s1	String
    //   1159	7	6	channel	String
    //   1233	7	6	channel	String
    //   652	11	7	tileentity	TileEntity
    //   695	11	7	entity	Entity
    //   723	61	7	s	String
    //   920	328	7	containerbeacon	ContainerBeacon
    //   729	22	8	flag	boolean
    //   928	322	8	slot	Slot
    //   825	8	9	localObject3	Object
    //   950	279	9	iinventory	Object
    // Exception table:
    //   from	to	target	type
    //   40	49	162	java/lang/Exception
    //   55	156	162	java/lang/Exception
    //   40	49	190	finally
    //   55	156	190	finally
    //   162	184	190	finally
    //   234	243	410	java/lang/Exception
    //   249	404	410	java/lang/Exception
    //   234	243	438	finally
    //   249	404	438	finally
    //   410	432	438	finally
    //   467	503	506	java/lang/Exception
    //   609	792	795	java/lang/Exception
    //   609	817	825	finally
    //   893	979	982	java/lang/Exception
  }
  
  public boolean isDisconnected()
  {
    return (!this.player.joining) && (!this.networkManager.channel.config().isAutoRead());
  }
  
  static class SyntheticClass_1
  {
    static final int[] a;
    static final int[] b;
    static final int[] c = new int[PacketPlayInClientCommand.EnumClientCommand.values().length];
    
    static
    {
      try
      {
        c[PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        c[PacketPlayInClientCommand.EnumClientCommand.REQUEST_STATS.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        c[PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      b = new int[PacketPlayInEntityAction.EnumPlayerAction.values().length];
      try
      {
        b[PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
      try
      {
        b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SNEAKING.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError5) {}
      try
      {
        b[PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError6) {}
      try
      {
        b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError7) {}
      try
      {
        b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SLEEPING.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError8) {}
      try
      {
        b[PacketPlayInEntityAction.EnumPlayerAction.RIDING_JUMP.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError9) {}
      try
      {
        b[PacketPlayInEntityAction.EnumPlayerAction.OPEN_INVENTORY.ordinal()] = 7;
      }
      catch (NoSuchFieldError localNoSuchFieldError10) {}
      a = new int[PacketPlayInBlockDig.EnumPlayerDigType.values().length];
      try
      {
        a[PacketPlayInBlockDig.EnumPlayerDigType.DROP_ITEM.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError11) {}
      try
      {
        a[PacketPlayInBlockDig.EnumPlayerDigType.DROP_ALL_ITEMS.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError12) {}
      try
      {
        a[PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError13) {}
      try
      {
        a[PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError14) {}
      try
      {
        a[PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK.ordinal()] = 5;
      }
      catch (NoSuchFieldError localNoSuchFieldError15) {}
      try
      {
        a[PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK.ordinal()] = 6;
      }
      catch (NoSuchFieldError localNoSuchFieldError16) {}
    }
  }
}
