package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.util.ResourceLeakDetector;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.World.Environment;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings.WorldTimingsHandler;
import org.bukkit.craftbukkit.v1_8_R3.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_8_R3.util.ServerShutdownThread;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotConfig;
import org.spigotmc.WatchdogThread;

public abstract class MinecraftServer
  implements Runnable, ICommandListener, IAsyncTaskHandler, IMojangStatistics
{
  public static final Logger LOGGER = ;
  public static final File a = new File("usercache.json");
  private static MinecraftServer l;
  public Convertable convertable;
  private final MojangStatisticsGenerator n = new MojangStatisticsGenerator("server", this, az());
  public File universe;
  private final List<IUpdatePlayerListBox> p = Lists.newArrayList();
  protected final ICommandHandler b;
  public final MethodProfiler methodProfiler = new MethodProfiler();
  private ServerConnection q;
  private final ServerPing r = new ServerPing();
  private final Random s = new Random();
  private String serverIp;
  private int u = -1;
  public WorldServer[] worldServer;
  private PlayerList v;
  private boolean isRunning = true;
  private boolean isStopped;
  private int ticks;
  protected final Proxy e;
  public String f;
  public int g;
  private boolean onlineMode;
  private boolean spawnAnimals;
  private boolean spawnNPCs;
  private boolean pvpMode;
  private boolean allowFlight;
  private String motd;
  private int F;
  private int G = 0;
  public final long[] h = new long[100];
  public long[][] i;
  private KeyPair H;
  private String I;
  private String J;
  private boolean demoMode;
  private boolean M;
  private boolean N;
  private String O = "";
  private String P = "";
  private boolean Q;
  private long R;
  private String S;
  private boolean T;
  private boolean U;
  private final YggdrasilAuthenticationService V;
  private final MinecraftSessionService W;
  private long X = 0L;
  private final GameProfileRepository Y;
  private final UserCache Z;
  protected final Queue<FutureTask<?>> j = new ConcurrentLinkedQueue();
  private Thread serverThread;
  private long ab = az();
  public List<WorldServer> worlds = new ArrayList();
  public CraftServer server;
  public OptionSet options;
  public ConsoleCommandSender console;
  public RemoteConsoleCommandSender remoteConsole;
  public ConsoleReader reader;
  public static int currentTick = (int)(System.currentTimeMillis() / 50L);
  public final Thread primaryThread;
  public Queue<Runnable> processQueue = new ConcurrentLinkedQueue();
  public int autosavePeriod;
  private static final int TPS = 20;
  private static final int TICK_TIME = 50000000;
  private static final int SAMPLE_INTERVAL = 100;
  public final double[] recentTps = new double[3];
  
  public MinecraftServer(OptionSet options, Proxy proxy, File file1)
  {
    ResourceLeakDetector.setEnabled(false);
    this.e = proxy;
    l = this;
    
    this.Z = new UserCache(this, file1);
    this.b = h();
    
    this.V = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
    this.W = this.V.createMinecraftSessionService();
    this.Y = this.V.createProfileRepository();
    
    this.options = options;
    if ((System.console() == null) && (System.getProperty("org.bukkit.craftbukkit.libs.jline.terminal") == null))
    {
      System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", "org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal");
      org.bukkit.craftbukkit.Main.useJline = false;
    }
    try
    {
      this.reader = new ConsoleReader(System.in, System.out);
      this.reader.setExpandEvents(false);
    }
    catch (Throwable localThrowable)
    {
      try
      {
        System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", "org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal");
        System.setProperty("user.language", "en");
        org.bukkit.craftbukkit.Main.useJline = false;
        this.reader = new ConsoleReader(System.in, System.out);
        this.reader.setExpandEvents(false);
      }
      catch (IOException ex)
      {
        LOGGER.warn(null, ex);
      }
    }
    Runtime.getRuntime().addShutdownHook(new ServerShutdownThread(this));
    
    this.serverThread = (this.primaryThread = new Thread(this, "Server thread"));
  }
  
  public abstract PropertyManager getPropertyManager();
  
  protected CommandDispatcher h()
  {
    return new CommandDispatcher();
  }
  
  protected abstract boolean init()
    throws IOException;
  
  protected void a(String s)
  {
    if (getConvertable().isConvertable(s))
    {
      LOGGER.info("Converting map!");
      b("menu.convertingLevel");
      getConvertable().convert(s, new IProgressUpdate()
      {
        private long b = System.currentTimeMillis();
        
        public void a(String s) {}
        
        public void a(int i)
        {
          if (System.currentTimeMillis() - this.b >= 1000L)
          {
            this.b = System.currentTimeMillis();
            MinecraftServer.LOGGER.info("Converting... " + i + "%");
          }
        }
        
        public void c(String s) {}
      });
    }
  }
  
  protected synchronized void b(String s)
  {
    this.S = s;
  }
  
  protected void a(String s, String s1, long i, WorldType worldtype, String s2)
  {
    a(s);
    b("menu.loadingLevel");
    this.worldServer = new WorldServer[3];
    
    int worldCount = 3;
    for (int j = 0; j < worldCount; j++)
    {
      byte dimension = 0;
      if (j == 1)
      {
        if (getAllowNether()) {
          dimension = -1;
        }
      }
      else if (j == 2)
      {
        if (this.server.getAllowEnd()) {
          dimension = 1;
        }
      }
      else
      {
        String worldType = World.Environment.getEnvironment(dimension).toString().toLowerCase();
        String name = s + "_" + worldType;
        
        ChunkGenerator gen = this.server.getGenerator(name);
        WorldSettings worldsettings = new WorldSettings(i, getGamemode(), getGenerateStructures(), isHardcore(), worldtype);
        worldsettings.setGeneratorSettings(s2);
        WorldServer world;
        if (j == 0)
        {
          IDataManager idatamanager = new ServerNBTManager(this.server.getWorldContainer(), s1, true);
          WorldData worlddata = idatamanager.getWorldData();
          if (worlddata == null) {
            worlddata = new WorldData(worldsettings, s1);
          }
          worlddata.checkName(s1);
          WorldServer world;
          WorldServer world;
          if (X()) {
            world = (WorldServer)new DemoWorldServer(this, idatamanager, worlddata, dimension, this.methodProfiler).b();
          } else {
            world = (WorldServer)new WorldServer(this, idatamanager, worlddata, dimension, this.methodProfiler, World.Environment.getEnvironment(dimension), gen).b();
          }
          world.a(worldsettings);
          this.server.scoreboardManager = new CraftScoreboardManager(this, world.getScoreboard());
        }
        else
        {
          String dim = "DIM" + dimension;
          
          File newWorld = new File(new File(name), dim);
          File oldWorld = new File(new File(s), dim);
          if ((!newWorld.isDirectory()) && (oldWorld.isDirectory()))
          {
            LOGGER.info("---- Migration of old " + worldType + " folder required ----");
            LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
            LOGGER.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
            LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");
            if (newWorld.exists())
            {
              LOGGER.warn("A file or folder already exists at " + newWorld + "!");
              LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
            }
            else if (newWorld.getParentFile().mkdirs())
            {
              if (oldWorld.renameTo(newWorld))
              {
                LOGGER.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                try
                {
                  Files.copy(new File(new File(s), "level.dat"), new File(new File(name), "level.dat"));
                }
                catch (IOException localIOException)
                {
                  LOGGER.warn("Unable to migrate world data.");
                }
                LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
              }
              else
              {
                LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
              }
            }
            else
            {
              LOGGER.warn("Could not create path for " + newWorld + "!");
              LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
            }
          }
          IDataManager idatamanager = new ServerNBTManager(this.server.getWorldContainer(), name, true);
          
          WorldData worlddata = idatamanager.getWorldData();
          if (worlddata == null) {
            worlddata = new WorldData(worldsettings, name);
          }
          worlddata.checkName(name);
          world = (WorldServer)new SecondaryWorldServer(this, idatamanager, dimension, (WorldServer)this.worlds.get(0), this.methodProfiler, worlddata, World.Environment.getEnvironment(dimension), gen).b();
        }
        this.server.getPluginManager().callEvent(new WorldInitEvent(world.getWorld()));
        
        world.addIWorldAccess(new WorldManager(this, world));
        if (!T()) {
          world.getWorldData().setGameType(getGamemode());
        }
        this.worlds.add(world);
        getPlayerList().setPlayerFileData((WorldServer[])this.worlds.toArray(new WorldServer[this.worlds.size()]));
      }
    }
    a(getDifficulty());
    k();
  }
  
  protected void k()
  {
    int i = 0;
    
    b("menu.generatingTerrain");
    WorldServer worldserver;
    for (int m = 0; m < this.worlds.size(); m++)
    {
      worldserver = (WorldServer)this.worlds.get(m);
      LOGGER.info("Preparing start region for level " + m + " (Seed: " + worldserver.getSeed() + ")");
      if (worldserver.getWorld().getKeepSpawnInMemory())
      {
        BlockPosition blockposition = worldserver.getSpawn();
        long j = az();
        i = 0;
        for (int k = 65344; (k <= 192) && (isRunning()); k += 16) {
          for (int l = 65344; (l <= 192) && (isRunning()); l += 16)
          {
            long i1 = az();
            if (i1 - j > 1000L)
            {
              a_("Preparing spawn area", i * 100 / 625);
              j = i1;
            }
            i++;
            worldserver.chunkProviderServer.getChunkAt(blockposition.getX() + k >> 4, blockposition.getZ() + l >> 4);
          }
        }
      }
    }
    for (WorldServer world : this.worlds) {
      this.server.getPluginManager().callEvent(new WorldLoadEvent(world.getWorld()));
    }
    s();
  }
  
  protected void a(String s, IDataManager idatamanager)
  {
    File file = new File(idatamanager.getDirectory(), "resources.zip");
    if (file.isFile()) {
      setResourcePack("level://" + s + "/" + file.getName(), "");
    }
  }
  
  public abstract boolean getGenerateStructures();
  
  public abstract WorldSettings.EnumGamemode getGamemode();
  
  public abstract EnumDifficulty getDifficulty();
  
  public abstract boolean isHardcore();
  
  public abstract int p();
  
  public abstract boolean q();
  
  public abstract boolean r();
  
  protected void a_(String s, int i)
  {
    this.f = s;
    this.g = i;
    LOGGER.info(s + ": " + i + "%");
  }
  
  protected void s()
  {
    this.f = null;
    this.g = 0;
    
    this.server.enablePlugins(PluginLoadOrder.POSTWORLD);
  }
  
  protected void saveChunks(boolean flag)
    throws ExceptionWorldConflict
  {
    if (!this.N)
    {
      WorldServer[] aworldserver = this.worldServer;
      aworldserver.length;
      for (int j = 0; j < this.worlds.size(); j++)
      {
        WorldServer worldserver = (WorldServer)this.worlds.get(j);
        if (worldserver != null)
        {
          if (!flag) {
            LOGGER.info("Saving chunks for level '" + worldserver.getWorldData().getName() + "'/" + worldserver.worldProvider.getName());
          }
          try
          {
            worldserver.save(true, null);
            worldserver.saveLevel();
          }
          catch (ExceptionWorldConflict exceptionworldconflict)
          {
            LOGGER.warn(exceptionworldconflict.getMessage());
          }
        }
      }
    }
  }
  
  private boolean hasStopped = false;
  private final Object stopLock = new Object();
  
  public void stop()
    throws ExceptionWorldConflict
  {
    synchronized (this.stopLock)
    {
      if (this.hasStopped) {
        return;
      }
      this.hasStopped = true;
    }
    if (!this.N)
    {
      LOGGER.info("Stopping server");
      if (this.server != null) {
        this.server.disablePlugins();
      }
      if (aq() != null) {
        aq().b();
      }
      if (this.v != null)
      {
        LOGGER.info("Saving players");
        this.v.savePlayers();
        this.v.u();
      }
      if (this.worldServer != null)
      {
        LOGGER.info("Saving worlds");
        saveChunks(false);
      }
      if (this.n.d()) {
        this.n.e();
      }
      if (SpigotConfig.saveUserCacheOnStopOnly)
      {
        LOGGER.info("Saving usercache.json");
        this.Z.c();
      }
    }
  }
  
  public String getServerIp()
  {
    return this.serverIp;
  }
  
  public void c(String s)
  {
    this.serverIp = s;
  }
  
  public boolean isRunning()
  {
    return this.isRunning;
  }
  
  public void safeShutdown()
  {
    this.isRunning = false;
  }
  
  private static double calcTps(double avg, double exp, double tps)
  {
    return avg * exp + tps * (1.0D - exp);
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 923	net/minecraft/server/v1_8_R3/MinecraftServer:init	()Z
    //   4: ifeq +244 -> 248
    //   7: aload_0
    //   8: invokestatic 197	net/minecraft/server/v1_8_R3/MinecraftServer:az	()J
    //   11: putfield 246	net/minecraft/server/v1_8_R3/MinecraftServer:ab	J
    //   14: aload_0
    //   15: getfield 218	net/minecraft/server/v1_8_R3/MinecraftServer:r	Lnet/minecraft/server/v1_8_R3/ServerPing;
    //   18: new 925	net/minecraft/server/v1_8_R3/ChatComponentText
    //   21: dup
    //   22: aload_0
    //   23: getfield 927	net/minecraft/server/v1_8_R3/MinecraftServer:motd	Ljava/lang/String;
    //   26: invokespecial 928	net/minecraft/server/v1_8_R3/ChatComponentText:<init>	(Ljava/lang/String;)V
    //   29: invokevirtual 932	net/minecraft/server/v1_8_R3/ServerPing:setMOTD	(Lnet/minecraft/server/v1_8_R3/IChatBaseComponent;)V
    //   32: aload_0
    //   33: getfield 218	net/minecraft/server/v1_8_R3/MinecraftServer:r	Lnet/minecraft/server/v1_8_R3/ServerPing;
    //   36: new 26	net/minecraft/server/v1_8_R3/ServerPing$ServerData
    //   39: dup
    //   40: ldc_w 934
    //   43: bipush 47
    //   45: invokespecial 936	net/minecraft/server/v1_8_R3/ServerPing$ServerData:<init>	(Ljava/lang/String;I)V
    //   48: invokevirtual 940	net/minecraft/server/v1_8_R3/ServerPing:setServerInfo	(Lnet/minecraft/server/v1_8_R3/ServerPing$ServerData;)V
    //   51: aload_0
    //   52: aload_0
    //   53: getfield 218	net/minecraft/server/v1_8_R3/MinecraftServer:r	Lnet/minecraft/server/v1_8_R3/ServerPing;
    //   56: invokespecial 943	net/minecraft/server/v1_8_R3/MinecraftServer:a	(Lnet/minecraft/server/v1_8_R3/ServerPing;)V
    //   59: aload_0
    //   60: getfield 255	net/minecraft/server/v1_8_R3/MinecraftServer:recentTps	[D
    //   63: ldc2_w 944
    //   66: invokestatic 951	java/util/Arrays:fill	([DD)V
    //   69: invokestatic 954	java/lang/System:nanoTime	()J
    //   72: lstore_1
    //   73: lconst_0
    //   74: lstore_3
    //   75: lload_1
    //   76: lstore 5
    //   78: goto +160 -> 238
    //   81: invokestatic 954	java/lang/System:nanoTime	()J
    //   84: lstore 7
    //   86: ldc2_w 955
    //   89: lload 7
    //   91: lload_1
    //   92: lsub
    //   93: lsub
    //   94: lload_3
    //   95: lsub
    //   96: lstore 9
    //   98: lload 9
    //   100: lconst_0
    //   101: lcmp
    //   102: ifle +17 -> 119
    //   105: lload 9
    //   107: ldc2_w 957
    //   110: ldiv
    //   111: invokestatic 962	java/lang/Thread:sleep	(J)V
    //   114: lconst_0
    //   115: lstore_3
    //   116: goto +122 -> 238
    //   119: ldc2_w 963
    //   122: lload 9
    //   124: invokestatic 970	java/lang/Math:abs	(J)J
    //   127: invokestatic 974	java/lang/Math:min	(JJ)J
    //   130: lstore_3
    //   131: getstatic 184	net/minecraft/server/v1_8_R3/MinecraftServer:currentTick	I
    //   134: dup
    //   135: iconst_1
    //   136: iadd
    //   137: putstatic 184	net/minecraft/server/v1_8_R3/MinecraftServer:currentTick	I
    //   140: bipush 100
    //   142: irem
    //   143: ifne +83 -> 226
    //   146: ldc2_w 975
    //   149: lload 7
    //   151: lload 5
    //   153: lsub
    //   154: l2d
    //   155: ddiv
    //   156: ldc2_w 977
    //   159: dmul
    //   160: dstore 11
    //   162: aload_0
    //   163: getfield 255	net/minecraft/server/v1_8_R3/MinecraftServer:recentTps	[D
    //   166: iconst_0
    //   167: aload_0
    //   168: getfield 255	net/minecraft/server/v1_8_R3/MinecraftServer:recentTps	[D
    //   171: iconst_0
    //   172: daload
    //   173: ldc2_w 979
    //   176: dload 11
    //   178: invokestatic 982	net/minecraft/server/v1_8_R3/MinecraftServer:calcTps	(DDD)D
    //   181: dastore
    //   182: aload_0
    //   183: getfield 255	net/minecraft/server/v1_8_R3/MinecraftServer:recentTps	[D
    //   186: iconst_1
    //   187: aload_0
    //   188: getfield 255	net/minecraft/server/v1_8_R3/MinecraftServer:recentTps	[D
    //   191: iconst_1
    //   192: daload
    //   193: ldc2_w 983
    //   196: dload 11
    //   198: invokestatic 982	net/minecraft/server/v1_8_R3/MinecraftServer:calcTps	(DDD)D
    //   201: dastore
    //   202: aload_0
    //   203: getfield 255	net/minecraft/server/v1_8_R3/MinecraftServer:recentTps	[D
    //   206: iconst_2
    //   207: aload_0
    //   208: getfield 255	net/minecraft/server/v1_8_R3/MinecraftServer:recentTps	[D
    //   211: iconst_2
    //   212: daload
    //   213: ldc2_w 985
    //   216: dload 11
    //   218: invokestatic 982	net/minecraft/server/v1_8_R3/MinecraftServer:calcTps	(DDD)D
    //   221: dastore
    //   222: lload 7
    //   224: lstore 5
    //   226: lload 7
    //   228: lstore_1
    //   229: aload_0
    //   230: invokevirtual 989	net/minecraft/server/v1_8_R3/MinecraftServer:A	()V
    //   233: aload_0
    //   234: iconst_1
    //   235: putfield 991	net/minecraft/server/v1_8_R3/MinecraftServer:Q	Z
    //   238: aload_0
    //   239: getfield 227	net/minecraft/server/v1_8_R3/MinecraftServer:isRunning	Z
    //   242: ifne -161 -> 81
    //   245: goto +424 -> 669
    //   248: aload_0
    //   249: aconst_null
    //   250: invokevirtual 994	net/minecraft/server/v1_8_R3/MinecraftServer:a	(Lnet/minecraft/server/v1_8_R3/CrashReport;)V
    //   253: goto +416 -> 669
    //   256: astore 13
    //   258: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   261: ldc_w 996
    //   264: aload 13
    //   266: invokeinterface 999 3 0
    //   271: aload 13
    //   273: invokevirtual 1003	java/lang/Throwable:getCause	()Ljava/lang/Throwable;
    //   276: ifnull +19 -> 295
    //   279: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   282: ldc_w 1005
    //   285: aload 13
    //   287: invokevirtual 1003	java/lang/Throwable:getCause	()Ljava/lang/Throwable;
    //   290: invokeinterface 999 3 0
    //   295: aconst_null
    //   296: astore 14
    //   298: aload 13
    //   300: instanceof 1007
    //   303: ifeq +20 -> 323
    //   306: aload_0
    //   307: aload 13
    //   309: checkcast 1007	net/minecraft/server/v1_8_R3/ReportedException
    //   312: invokevirtual 1010	net/minecraft/server/v1_8_R3/ReportedException:a	()Lnet/minecraft/server/v1_8_R3/CrashReport;
    //   315: invokevirtual 1013	net/minecraft/server/v1_8_R3/MinecraftServer:b	(Lnet/minecraft/server/v1_8_R3/CrashReport;)Lnet/minecraft/server/v1_8_R3/CrashReport;
    //   318: astore 14
    //   320: goto +21 -> 341
    //   323: aload_0
    //   324: new 1015	net/minecraft/server/v1_8_R3/CrashReport
    //   327: dup
    //   328: ldc_w 1017
    //   331: aload 13
    //   333: invokespecial 1019	net/minecraft/server/v1_8_R3/CrashReport:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   336: invokevirtual 1013	net/minecraft/server/v1_8_R3/MinecraftServer:b	(Lnet/minecraft/server/v1_8_R3/CrashReport;)Lnet/minecraft/server/v1_8_R3/CrashReport;
    //   339: astore 14
    //   341: new 166	java/io/File
    //   344: dup
    //   345: new 166	java/io/File
    //   348: dup
    //   349: aload_0
    //   350: invokevirtual 1022	net/minecraft/server/v1_8_R3/MinecraftServer:y	()Ljava/io/File;
    //   353: ldc_w 1024
    //   356: invokespecial 557	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   359: new 459	java/lang/StringBuilder
    //   362: dup
    //   363: ldc_w 1026
    //   366: invokespecial 464	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   369: new 1028	java/text/SimpleDateFormat
    //   372: dup
    //   373: ldc_w 1030
    //   376: invokespecial 1031	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   379: new 1033	java/util/Date
    //   382: dup
    //   383: invokespecial 1034	java/util/Date:<init>	()V
    //   386: invokevirtual 1038	java/text/SimpleDateFormat:format	(Ljava/util/Date;)Ljava/lang/String;
    //   389: invokevirtual 470	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   392: ldc_w 1040
    //   395: invokevirtual 470	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   398: invokevirtual 471	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   401: invokespecial 557	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   404: astore 15
    //   406: aload 14
    //   408: aload 15
    //   410: invokevirtual 1042	net/minecraft/server/v1_8_R3/CrashReport:a	(Ljava/io/File;)Z
    //   413: ifeq +35 -> 448
    //   416: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   419: new 459	java/lang/StringBuilder
    //   422: dup
    //   423: ldc_w 1044
    //   426: invokespecial 464	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   429: aload 15
    //   431: invokevirtual 1047	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   434: invokevirtual 470	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   437: invokevirtual 471	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   440: invokeinterface 1049 2 0
    //   445: goto +14 -> 459
    //   448: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   451: ldc_w 1051
    //   454: invokeinterface 1049 2 0
    //   459: aload_0
    //   460: aload 14
    //   462: invokevirtual 994	net/minecraft/server/v1_8_R3/MinecraftServer:a	(Lnet/minecraft/server/v1_8_R3/CrashReport;)V
    //   465: invokestatic 1056	org/spigotmc/WatchdogThread:doStop	()V
    //   468: aload_0
    //   469: iconst_1
    //   470: putfield 1058	net/minecraft/server/v1_8_R3/MinecraftServer:isStopped	Z
    //   473: aload_0
    //   474: invokevirtual 1060	net/minecraft/server/v1_8_R3/MinecraftServer:stop	()V
    //   477: goto +66 -> 543
    //   480: astore 16
    //   482: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   485: ldc_w 1062
    //   488: aload 16
    //   490: invokeinterface 999 3 0
    //   495: aload_0
    //   496: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   499: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   502: invokeinterface 1071 1 0
    //   507: goto +4 -> 511
    //   510: pop
    //   511: aload_0
    //   512: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   515: goto +252 -> 767
    //   518: astore 17
    //   520: aload_0
    //   521: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   524: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   527: invokeinterface 1071 1 0
    //   532: goto +4 -> 536
    //   535: pop
    //   536: aload_0
    //   537: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   540: aload 17
    //   542: athrow
    //   543: aload_0
    //   544: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   547: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   550: invokeinterface 1071 1 0
    //   555: goto +4 -> 559
    //   558: pop
    //   559: aload_0
    //   560: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   563: goto +204 -> 767
    //   566: astore 18
    //   568: invokestatic 1056	org/spigotmc/WatchdogThread:doStop	()V
    //   571: aload_0
    //   572: iconst_1
    //   573: putfield 1058	net/minecraft/server/v1_8_R3/MinecraftServer:isStopped	Z
    //   576: aload_0
    //   577: invokevirtual 1060	net/minecraft/server/v1_8_R3/MinecraftServer:stop	()V
    //   580: goto +66 -> 646
    //   583: astore 16
    //   585: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   588: ldc_w 1062
    //   591: aload 16
    //   593: invokeinterface 999 3 0
    //   598: aload_0
    //   599: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   602: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   605: invokeinterface 1071 1 0
    //   610: goto +4 -> 614
    //   613: pop
    //   614: aload_0
    //   615: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   618: goto +48 -> 666
    //   621: astore 17
    //   623: aload_0
    //   624: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   627: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   630: invokeinterface 1071 1 0
    //   635: goto +4 -> 639
    //   638: pop
    //   639: aload_0
    //   640: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   643: aload 17
    //   645: athrow
    //   646: aload_0
    //   647: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   650: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   653: invokeinterface 1071 1 0
    //   658: goto +4 -> 662
    //   661: pop
    //   662: aload_0
    //   663: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   666: aload 18
    //   668: athrow
    //   669: invokestatic 1056	org/spigotmc/WatchdogThread:doStop	()V
    //   672: aload_0
    //   673: iconst_1
    //   674: putfield 1058	net/minecraft/server/v1_8_R3/MinecraftServer:isStopped	Z
    //   677: aload_0
    //   678: invokevirtual 1060	net/minecraft/server/v1_8_R3/MinecraftServer:stop	()V
    //   681: goto +66 -> 747
    //   684: astore 16
    //   686: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   689: ldc_w 1062
    //   692: aload 16
    //   694: invokeinterface 999 3 0
    //   699: aload_0
    //   700: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   703: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   706: invokeinterface 1071 1 0
    //   711: goto +4 -> 715
    //   714: pop
    //   715: aload_0
    //   716: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   719: goto +48 -> 767
    //   722: astore 17
    //   724: aload_0
    //   725: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   728: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   731: invokeinterface 1071 1 0
    //   736: goto +4 -> 740
    //   739: pop
    //   740: aload_0
    //   741: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   744: aload 17
    //   746: athrow
    //   747: aload_0
    //   748: getfield 351	net/minecraft/server/v1_8_R3/MinecraftServer:reader	Lorg/bukkit/craftbukkit/libs/jline/console/ConsoleReader;
    //   751: invokevirtual 1066	org/bukkit/craftbukkit/libs/jline/console/ConsoleReader:getTerminal	()Lorg/bukkit/craftbukkit/libs/jline/Terminal;
    //   754: invokeinterface 1071 1 0
    //   759: goto +4 -> 763
    //   762: pop
    //   763: aload_0
    //   764: invokevirtual 1074	net/minecraft/server/v1_8_R3/MinecraftServer:z	()V
    //   767: return
    // Line number table:
    //   Java source line #528	-> byte code offset #0
    //   Java source line #529	-> byte code offset #7
    //   Java source line #532	-> byte code offset #14
    //   Java source line #533	-> byte code offset #32
    //   Java source line #534	-> byte code offset #51
    //   Java source line #537	-> byte code offset #59
    //   Java source line #538	-> byte code offset #69
    //   Java source line #539	-> byte code offset #78
    //   Java source line #540	-> byte code offset #81
    //   Java source line #541	-> byte code offset #86
    //   Java source line #542	-> byte code offset #98
    //   Java source line #543	-> byte code offset #105
    //   Java source line #544	-> byte code offset #114
    //   Java source line #545	-> byte code offset #116
    //   Java source line #547	-> byte code offset #119
    //   Java source line #550	-> byte code offset #131
    //   Java source line #552	-> byte code offset #146
    //   Java source line #553	-> byte code offset #162
    //   Java source line #554	-> byte code offset #182
    //   Java source line #555	-> byte code offset #202
    //   Java source line #556	-> byte code offset #222
    //   Java source line #558	-> byte code offset #226
    //   Java source line #560	-> byte code offset #229
    //   Java source line #561	-> byte code offset #233
    //   Java source line #539	-> byte code offset #238
    //   Java source line #564	-> byte code offset #245
    //   Java source line #565	-> byte code offset #248
    //   Java source line #567	-> byte code offset #253
    //   Java source line #568	-> byte code offset #258
    //   Java source line #570	-> byte code offset #271
    //   Java source line #572	-> byte code offset #279
    //   Java source line #575	-> byte code offset #295
    //   Java source line #577	-> byte code offset #298
    //   Java source line #578	-> byte code offset #306
    //   Java source line #579	-> byte code offset #320
    //   Java source line #580	-> byte code offset #323
    //   Java source line #583	-> byte code offset #341
    //   Java source line #585	-> byte code offset #406
    //   Java source line #586	-> byte code offset #416
    //   Java source line #587	-> byte code offset #445
    //   Java source line #588	-> byte code offset #448
    //   Java source line #591	-> byte code offset #459
    //   Java source line #594	-> byte code offset #465
    //   Java source line #595	-> byte code offset #468
    //   Java source line #596	-> byte code offset #473
    //   Java source line #597	-> byte code offset #477
    //   Java source line #598	-> byte code offset #482
    //   Java source line #602	-> byte code offset #495
    //   Java source line #603	-> byte code offset #507
    //   Java source line #606	-> byte code offset #511
    //   Java source line #599	-> byte code offset #518
    //   Java source line #602	-> byte code offset #520
    //   Java source line #603	-> byte code offset #532
    //   Java source line #606	-> byte code offset #536
    //   Java source line #607	-> byte code offset #540
    //   Java source line #602	-> byte code offset #543
    //   Java source line #603	-> byte code offset #555
    //   Java source line #606	-> byte code offset #559
    //   Java source line #607	-> byte code offset #563
    //   Java source line #592	-> byte code offset #566
    //   Java source line #594	-> byte code offset #568
    //   Java source line #595	-> byte code offset #571
    //   Java source line #596	-> byte code offset #576
    //   Java source line #597	-> byte code offset #580
    //   Java source line #598	-> byte code offset #585
    //   Java source line #602	-> byte code offset #598
    //   Java source line #603	-> byte code offset #610
    //   Java source line #606	-> byte code offset #614
    //   Java source line #599	-> byte code offset #621
    //   Java source line #602	-> byte code offset #623
    //   Java source line #603	-> byte code offset #635
    //   Java source line #606	-> byte code offset #639
    //   Java source line #607	-> byte code offset #643
    //   Java source line #602	-> byte code offset #646
    //   Java source line #603	-> byte code offset #658
    //   Java source line #606	-> byte code offset #662
    //   Java source line #609	-> byte code offset #666
    //   Java source line #594	-> byte code offset #669
    //   Java source line #595	-> byte code offset #672
    //   Java source line #596	-> byte code offset #677
    //   Java source line #597	-> byte code offset #681
    //   Java source line #598	-> byte code offset #686
    //   Java source line #602	-> byte code offset #699
    //   Java source line #603	-> byte code offset #711
    //   Java source line #606	-> byte code offset #715
    //   Java source line #599	-> byte code offset #722
    //   Java source line #602	-> byte code offset #724
    //   Java source line #603	-> byte code offset #736
    //   Java source line #606	-> byte code offset #740
    //   Java source line #607	-> byte code offset #744
    //   Java source line #602	-> byte code offset #747
    //   Java source line #603	-> byte code offset #759
    //   Java source line #606	-> byte code offset #763
    //   Java source line #611	-> byte code offset #767
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	768	0	this	MinecraftServer
    //   72	157	1	lastTick	long
    //   74	57	3	catchupTime	long
    //   76	149	5	tickSection	long
    //   84	143	7	curTime	long
    //   96	27	9	wait	long
    //   160	57	11	currentTps	double
    //   256	76	13	throwable	Throwable
    //   296	165	14	crashreport	CrashReport
    //   404	26	15	file	File
    //   480	9	16	throwable1	Throwable
    //   583	9	16	throwable1	Throwable
    //   684	9	16	throwable1	Throwable
    //   510	1	17	localException1	Exception
    //   518	23	17	localObject1	Object
    //   621	23	17	localObject2	Object
    //   722	23	17	localObject3	Object
    //   535	1	18	localException2	Exception
    //   566	101	18	localObject4	Object
    //   558	1	19	localException3	Exception
    //   613	1	20	localException4	Exception
    //   638	1	21	localException5	Exception
    //   661	1	22	localException6	Exception
    //   714	1	23	localException7	Exception
    //   739	1	24	localException8	Exception
    //   762	1	25	localException9	Exception
    // Exception table:
    //   from	to	target	type
    //   0	253	256	java/lang/Throwable
    //   465	477	480	java/lang/Throwable
    //   495	507	510	java/lang/Exception
    //   465	495	518	finally
    //   520	532	535	java/lang/Exception
    //   543	555	558	java/lang/Exception
    //   0	465	566	finally
    //   568	580	583	java/lang/Throwable
    //   598	610	613	java/lang/Exception
    //   568	598	621	finally
    //   623	635	638	java/lang/Exception
    //   646	658	661	java/lang/Exception
    //   669	681	684	java/lang/Throwable
    //   699	711	714	java/lang/Exception
    //   669	699	722	finally
    //   724	736	739	java/lang/Exception
    //   747	759	762	java/lang/Exception
  }
  
  /* Error */
  private void a(ServerPing serverping)
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc_w 1087
    //   4: invokevirtual 1090	net/minecraft/server/v1_8_R3/MinecraftServer:d	(Ljava/lang/String;)Ljava/io/File;
    //   7: astore_2
    //   8: aload_2
    //   9: invokevirtual 805	java/io/File:isFile	()Z
    //   12: ifeq +155 -> 167
    //   15: invokestatic 1096	io/netty/buffer/Unpooled:buffer	()Lio/netty/buffer/ByteBuf;
    //   18: astore_3
    //   19: aload_2
    //   20: invokestatic 1102	javax/imageio/ImageIO:read	(Ljava/io/File;)Ljava/awt/image/BufferedImage;
    //   23: astore 4
    //   25: aload 4
    //   27: invokevirtual 1107	java/awt/image/BufferedImage:getWidth	()I
    //   30: bipush 64
    //   32: if_icmpne +7 -> 39
    //   35: iconst_1
    //   36: goto +4 -> 40
    //   39: iconst_0
    //   40: ldc_w 1111
    //   43: iconst_0
    //   44: anewarray 4	java/lang/Object
    //   47: invokestatic 1117	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
    //   50: aload 4
    //   52: invokevirtual 1120	java/awt/image/BufferedImage:getHeight	()I
    //   55: bipush 64
    //   57: if_icmpne +7 -> 64
    //   60: iconst_1
    //   61: goto +4 -> 65
    //   64: iconst_0
    //   65: ldc_w 1122
    //   68: iconst_0
    //   69: anewarray 4	java/lang/Object
    //   72: invokestatic 1117	org/apache/commons/lang3/Validate:validState	(ZLjava/lang/String;[Ljava/lang/Object;)V
    //   75: aload 4
    //   77: ldc_w 1124
    //   80: new 1126	io/netty/buffer/ByteBufOutputStream
    //   83: dup
    //   84: aload_3
    //   85: invokespecial 1129	io/netty/buffer/ByteBufOutputStream:<init>	(Lio/netty/buffer/ByteBuf;)V
    //   88: invokestatic 1133	javax/imageio/ImageIO:write	(Ljava/awt/image/RenderedImage;Ljava/lang/String;Ljava/io/OutputStream;)Z
    //   91: pop
    //   92: aload_3
    //   93: invokestatic 1139	io/netty/handler/codec/base64/Base64:encode	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
    //   96: astore 5
    //   98: aload_1
    //   99: new 459	java/lang/StringBuilder
    //   102: dup
    //   103: ldc_w 1141
    //   106: invokespecial 464	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   109: aload 5
    //   111: getstatic 1147	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
    //   114: invokevirtual 1150	io/netty/buffer/ByteBuf:toString	(Ljava/nio/charset/Charset;)Ljava/lang/String;
    //   117: invokevirtual 470	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: invokevirtual 471	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   123: invokevirtual 1153	net/minecraft/server/v1_8_R3/ServerPing:setFavicon	(Ljava/lang/String;)V
    //   126: goto +36 -> 162
    //   129: astore 4
    //   131: getstatic 164	net/minecraft/server/v1_8_R3/MinecraftServer:LOGGER	Lorg/apache/logging/log4j/Logger;
    //   134: ldc_w 1155
    //   137: aload 4
    //   139: invokeinterface 999 3 0
    //   144: aload_3
    //   145: invokevirtual 1158	io/netty/buffer/ByteBuf:release	()Z
    //   148: pop
    //   149: goto +18 -> 167
    //   152: astore 6
    //   154: aload_3
    //   155: invokevirtual 1158	io/netty/buffer/ByteBuf:release	()Z
    //   158: pop
    //   159: aload 6
    //   161: athrow
    //   162: aload_3
    //   163: invokevirtual 1158	io/netty/buffer/ByteBuf:release	()Z
    //   166: pop
    //   167: return
    // Line number table:
    //   Java source line #614	-> byte code offset #0
    //   Java source line #616	-> byte code offset #8
    //   Java source line #617	-> byte code offset #15
    //   Java source line #620	-> byte code offset #19
    //   Java source line #622	-> byte code offset #25
    //   Java source line #623	-> byte code offset #50
    //   Java source line #624	-> byte code offset #75
    //   Java source line #625	-> byte code offset #92
    //   Java source line #627	-> byte code offset #98
    //   Java source line #628	-> byte code offset #126
    //   Java source line #629	-> byte code offset #131
    //   Java source line #631	-> byte code offset #144
    //   Java source line #630	-> byte code offset #152
    //   Java source line #631	-> byte code offset #154
    //   Java source line #632	-> byte code offset #159
    //   Java source line #631	-> byte code offset #162
    //   Java source line #635	-> byte code offset #167
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	168	0	this	MinecraftServer
    //   0	168	1	serverping	ServerPing
    //   7	13	2	file	File
    //   18	145	3	bytebuf	io.netty.buffer.ByteBuf
    //   23	53	4	bufferedimage	java.awt.image.BufferedImage
    //   129	9	4	exception	Exception
    //   96	14	5	bytebuf1	io.netty.buffer.ByteBuf
    //   152	8	6	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   19	126	129	java/lang/Exception
    //   19	144	152	finally
  }
  
  public File y()
  {
    return new File(".");
  }
  
  protected void a(CrashReport crashreport) {}
  
  protected void z() {}
  
  protected void A()
    throws ExceptionWorldConflict
  {
    SpigotTimings.serverTickTimer.startTiming();
    long i = System.nanoTime();
    
    this.ticks += 1;
    if (this.T)
    {
      this.T = false;
      this.methodProfiler.a = true;
      this.methodProfiler.a();
    }
    this.methodProfiler.a("root");
    B();
    int j;
    if (i - this.X >= 5000000000L)
    {
      this.X = i;
      this.r.setPlayerSample(new ServerPing.ServerPingPlayerSample(J(), I()));
      GameProfile[] agameprofile = new GameProfile[Math.min(I(), 12)];
      j = MathHelper.nextInt(this.s, 0, I() - agameprofile.length);
      for (int k = 0; k < agameprofile.length; k++) {
        agameprofile[k] = ((EntityPlayer)this.v.v().get(j + k)).getProfile();
      }
      Collections.shuffle(Arrays.asList(agameprofile));
      this.r.b().a(agameprofile);
    }
    if ((this.autosavePeriod > 0) && (this.ticks % this.autosavePeriod == 0))
    {
      SpigotTimings.worldSaveTimer.startTiming();
      this.methodProfiler.a("save");
      this.v.savePlayers();
      
      this.server.playerCommandState = true;
      for (World world : this.worlds) {
        world.getWorld().save(false);
      }
      this.server.playerCommandState = false;
      
      this.methodProfiler.b();
      SpigotTimings.worldSaveTimer.stopTiming();
    }
    this.methodProfiler.a("tallying");
    this.h[(this.ticks % 100)] = (System.nanoTime() - i);
    this.methodProfiler.b();
    this.methodProfiler.a("snooper");
    if ((getSnooperEnabled()) && (!this.n.d()) && (this.ticks > 100)) {
      this.n.a();
    }
    if ((getSnooperEnabled()) && (this.ticks % 6000 == 0)) {
      this.n.b();
    }
    this.methodProfiler.b();
    this.methodProfiler.b();
    WatchdogThread.tick();
    SpigotTimings.serverTickTimer.stopTiming();
    CustomTimingsHandler.tick();
  }
  
  public void B()
  {
    this.methodProfiler.a("jobs");
    
    int count = this.j.size();
    FutureTask<?> entry;
    while ((count-- > 0) && ((entry = (FutureTask)this.j.poll()) != null))
    {
      FutureTask<?> entry;
      SystemUtils.a(entry, LOGGER);
    }
    this.methodProfiler.c("levels");
    
    SpigotTimings.schedulerTimer.startTiming();
    
    this.server.getScheduler().mainThreadHeartbeat(this.ticks);
    SpigotTimings.schedulerTimer.stopTiming();
    
    SpigotTimings.processQueueTimer.startTiming();
    while (!this.processQueue.isEmpty()) {
      ((Runnable)this.processQueue.remove()).run();
    }
    SpigotTimings.processQueueTimer.stopTiming();
    
    SpigotTimings.chunkIOTickTimer.startTiming();
    ChunkIOExecutor.tick();
    SpigotTimings.chunkIOTickTimer.stopTiming();
    
    SpigotTimings.timeUpdateTimer.startTiming();
    if (this.ticks % 20 == 0) {
      for (int i = 0; i < getPlayerList().players.size(); i++)
      {
        EntityPlayer entityplayer = (EntityPlayer)getPlayerList().players.get(i);
        entityplayer.playerConnection.sendPacket(new PacketPlayOutUpdateTime(entityplayer.world.getTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean("doDaylightCycle")));
      }
    }
    SpigotTimings.timeUpdateTimer.stopTiming();
    for (int i = 0; i < this.worlds.size(); i++)
    {
      System.nanoTime();
      
      WorldServer worldserver = (WorldServer)this.worlds.get(i);
      
      this.methodProfiler.a(worldserver.getWorldData().getName());
      
      this.methodProfiler.a("tick");
      try
      {
        worldserver.timings.doTick.startTiming();
        worldserver.doTick();
        worldserver.timings.doTick.stopTiming();
      }
      catch (Throwable throwable)
      {
        try
        {
          crashreport = CrashReport.a(throwable, "Exception ticking world");
        }
        catch (Throwable t)
        {
          CrashReport crashreport;
          throw new RuntimeException("Error generating crash report", t);
        }
        CrashReport crashreport;
        worldserver.a(crashreport);
        throw new ReportedException(crashreport);
      }
      try
      {
        worldserver.timings.tickEntities.startTiming();
        worldserver.tickEntities();
        worldserver.timings.tickEntities.stopTiming();
      }
      catch (Throwable throwable1)
      {
        try
        {
          crashreport = CrashReport.a(throwable1, "Exception ticking world entities");
        }
        catch (Throwable t)
        {
          CrashReport crashreport;
          throw new RuntimeException("Error generating crash report", t);
        }
        CrashReport crashreport;
        worldserver.a(crashreport);
        throw new ReportedException(crashreport);
      }
      this.methodProfiler.b();
      this.methodProfiler.a("tracker");
      worldserver.timings.tracker.startTiming();
      worldserver.getTracker().updatePlayers();
      worldserver.timings.tracker.stopTiming();
      this.methodProfiler.b();
      this.methodProfiler.b();
    }
    this.methodProfiler.c("connection");
    SpigotTimings.connectionTimer.startTiming();
    aq().c();
    SpigotTimings.connectionTimer.stopTiming();
    this.methodProfiler.c("players");
    SpigotTimings.playerListTimer.startTiming();
    this.v.tick();
    SpigotTimings.playerListTimer.stopTiming();
    this.methodProfiler.c("tickables");
    
    SpigotTimings.tickablesTimer.startTiming();
    for (i = 0; i < this.p.size(); i++) {
      ((IUpdatePlayerListBox)this.p.get(i)).c();
    }
    SpigotTimings.tickablesTimer.stopTiming();
    
    this.methodProfiler.b();
  }
  
  public boolean getAllowNether()
  {
    return true;
  }
  
  public void a(IUpdatePlayerListBox iupdateplayerlistbox)
  {
    this.p.add(iupdateplayerlistbox);
  }
  
  public static void main(OptionSet options)
  {
    
    try
    {
      DedicatedServer dedicatedserver = new DedicatedServer(options);
      if (options.has("port"))
      {
        int port = ((Integer)options.valueOf("port")).intValue();
        if (port > 0) {
          dedicatedserver.setPort(port);
        }
      }
      if (options.has("universe")) {
        dedicatedserver.universe = ((File)options.valueOf("universe"));
      }
      if (options.has("world")) {
        dedicatedserver.setWorld((String)options.valueOf("world"));
      }
      dedicatedserver.primaryThread.start();
    }
    catch (Exception exception)
    {
      LOGGER.fatal("Failed to start the minecraft server", exception);
    }
  }
  
  public void C() {}
  
  public File d(String s)
  {
    return new File(y(), s);
  }
  
  public void info(String s)
  {
    LOGGER.info(s);
  }
  
  public void warning(String s)
  {
    LOGGER.warn(s);
  }
  
  public WorldServer getWorldServer(int i)
  {
    for (WorldServer world : this.worlds) {
      if (world.dimension == i) {
        return world;
      }
    }
    return (WorldServer)this.worlds.get(0);
  }
  
  public String E()
  {
    return this.serverIp;
  }
  
  public int F()
  {
    return this.u;
  }
  
  public String G()
  {
    return this.motd;
  }
  
  public String getVersion()
  {
    return "1.8.7";
  }
  
  public int I()
  {
    return this.v.getPlayerCount();
  }
  
  public int J()
  {
    return this.v.getMaxPlayers();
  }
  
  public String[] getPlayers()
  {
    return this.v.f();
  }
  
  public GameProfile[] L()
  {
    return this.v.g();
  }
  
  public boolean isDebugging()
  {
    return getPropertyManager().getBoolean("debug", false);
  }
  
  public void g(String s)
  {
    LOGGER.error(s);
  }
  
  public void h(String s)
  {
    if (isDebugging()) {
      LOGGER.info(s);
    }
  }
  
  public String getServerModName()
  {
    return "Spigot";
  }
  
  public CrashReport b(CrashReport crashreport)
  {
    crashreport.g().a("Profiler Position", new Callable()
    {
      public String a()
        throws Exception
      {
        return MinecraftServer.this.methodProfiler.a ? MinecraftServer.this.methodProfiler.c() : "N/A (disabled)";
      }
      
      public Object call()
        throws Exception
      {
        return a();
      }
    });
    if (this.v != null) {
      crashreport.g().a("Player Count", new Callable()
      {
        public String a()
        {
          return MinecraftServer.this.v.getPlayerCount() + " / " + MinecraftServer.this.v.getMaxPlayers() + "; " + MinecraftServer.this.v.v();
        }
        
        public Object call()
          throws Exception
        {
          return a();
        }
      });
    }
    return crashreport;
  }
  
  public List<String> tabCompleteCommand(ICommandListener icommandlistener, String s, BlockPosition blockposition)
  {
    return this.server.tabComplete(icommandlistener, s);
  }
  
  public static MinecraftServer getServer()
  {
    return l;
  }
  
  public boolean O()
  {
    return true;
  }
  
  public String getName()
  {
    return "Server";
  }
  
  public void sendMessage(IChatBaseComponent ichatbasecomponent)
  {
    LOGGER.info(ichatbasecomponent.c());
  }
  
  public boolean a(int i, String s)
  {
    return true;
  }
  
  public ICommandHandler getCommandHandler()
  {
    return this.b;
  }
  
  public KeyPair Q()
  {
    return this.H;
  }
  
  public int R()
  {
    return this.u;
  }
  
  public void setPort(int i)
  {
    this.u = i;
  }
  
  public String S()
  {
    return this.I;
  }
  
  public void i(String s)
  {
    this.I = s;
  }
  
  public boolean T()
  {
    return this.I != null;
  }
  
  public String U()
  {
    return this.J;
  }
  
  public void setWorld(String s)
  {
    this.J = s;
  }
  
  public void a(KeyPair keypair)
  {
    this.H = keypair;
  }
  
  public void a(EnumDifficulty enumdifficulty)
  {
    for (int i = 0; i < this.worlds.size(); i++)
    {
      WorldServer worldserver = (WorldServer)this.worlds.get(i);
      if (worldserver != null) {
        if (worldserver.getWorldData().isHardcore())
        {
          worldserver.getWorldData().setDifficulty(EnumDifficulty.HARD);
          worldserver.setSpawnFlags(true, true);
        }
        else if (T())
        {
          worldserver.getWorldData().setDifficulty(enumdifficulty);
          worldserver.setSpawnFlags(worldserver.getDifficulty() != EnumDifficulty.PEACEFUL, true);
        }
        else
        {
          worldserver.getWorldData().setDifficulty(enumdifficulty);
          worldserver.setSpawnFlags(getSpawnMonsters(), this.spawnAnimals);
        }
      }
    }
  }
  
  protected boolean getSpawnMonsters()
  {
    return true;
  }
  
  public boolean X()
  {
    return this.demoMode;
  }
  
  public void b(boolean flag)
  {
    this.demoMode = flag;
  }
  
  public void c(boolean flag)
  {
    this.M = flag;
  }
  
  public Convertable getConvertable()
  {
    return this.convertable;
  }
  
  public void aa()
  {
    this.N = true;
    getConvertable().d();
    for (int i = 0; i < this.worlds.size(); i++)
    {
      WorldServer worldserver = (WorldServer)this.worlds.get(i);
      if (worldserver != null) {
        worldserver.saveLevel();
      }
    }
    getConvertable().e(((WorldServer)this.worlds.get(0)).getDataManager().g());
    safeShutdown();
  }
  
  public String getResourcePack()
  {
    return this.O;
  }
  
  public String getResourcePackHash()
  {
    return this.P;
  }
  
  public void setResourcePack(String s, String s1)
  {
    this.O = s;
    this.P = s1;
  }
  
  public void a(MojangStatisticsGenerator mojangstatisticsgenerator)
  {
    mojangstatisticsgenerator.a("whitelist_enabled", Boolean.valueOf(false));
    mojangstatisticsgenerator.a("whitelist_count", Integer.valueOf(0));
    if (this.v != null)
    {
      mojangstatisticsgenerator.a("players_current", Integer.valueOf(I()));
      mojangstatisticsgenerator.a("players_max", Integer.valueOf(J()));
      mojangstatisticsgenerator.a("players_seen", Integer.valueOf(this.v.getSeenPlayers().length));
    }
    mojangstatisticsgenerator.a("uses_auth", Boolean.valueOf(this.onlineMode));
    mojangstatisticsgenerator.a("gui_state", as() ? "enabled" : "disabled");
    mojangstatisticsgenerator.a("run_time", Long.valueOf((az() - mojangstatisticsgenerator.g()) / 60L * 1000L));
    mojangstatisticsgenerator.a("avg_tick_ms", Integer.valueOf((int)(MathHelper.a(this.h) * 1.0E-6D)));
    int i = 0;
    if (this.worldServer != null) {
      for (int j = 0; j < this.worlds.size(); j++)
      {
        WorldServer worldserver = (WorldServer)this.worlds.get(j);
        if (worldserver != null)
        {
          WorldData worlddata = worldserver.getWorldData();
          
          mojangstatisticsgenerator.a("world[" + i + "][dimension]", Integer.valueOf(worldserver.worldProvider.getDimension()));
          mojangstatisticsgenerator.a("world[" + i + "][mode]", worlddata.getGameType());
          mojangstatisticsgenerator.a("world[" + i + "][difficulty]", worldserver.getDifficulty());
          mojangstatisticsgenerator.a("world[" + i + "][hardcore]", Boolean.valueOf(worlddata.isHardcore()));
          mojangstatisticsgenerator.a("world[" + i + "][generator_name]", worlddata.getType().name());
          mojangstatisticsgenerator.a("world[" + i + "][generator_version]", Integer.valueOf(worlddata.getType().getVersion()));
          mojangstatisticsgenerator.a("world[" + i + "][height]", Integer.valueOf(this.F));
          mojangstatisticsgenerator.a("world[" + i + "][chunks_loaded]", Integer.valueOf(worldserver.N().getLoadedChunks()));
          i++;
        }
      }
    }
    mojangstatisticsgenerator.a("worlds", Integer.valueOf(i));
  }
  
  public void b(MojangStatisticsGenerator mojangstatisticsgenerator)
  {
    mojangstatisticsgenerator.b("singleplayer", Boolean.valueOf(T()));
    mojangstatisticsgenerator.b("server_brand", getServerModName());
    mojangstatisticsgenerator.b("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
    mojangstatisticsgenerator.b("dedicated", Boolean.valueOf(ae()));
  }
  
  public boolean getSnooperEnabled()
  {
    return true;
  }
  
  public abstract boolean ae();
  
  public boolean getOnlineMode()
  {
    return this.server.getOnlineMode();
  }
  
  public void setOnlineMode(boolean flag)
  {
    this.onlineMode = flag;
  }
  
  public boolean getSpawnAnimals()
  {
    return this.spawnAnimals;
  }
  
  public void setSpawnAnimals(boolean flag)
  {
    this.spawnAnimals = flag;
  }
  
  public boolean getSpawnNPCs()
  {
    return this.spawnNPCs;
  }
  
  public abstract boolean ai();
  
  public void setSpawnNPCs(boolean flag)
  {
    this.spawnNPCs = flag;
  }
  
  public boolean getPVP()
  {
    return this.pvpMode;
  }
  
  public void setPVP(boolean flag)
  {
    this.pvpMode = flag;
  }
  
  public boolean getAllowFlight()
  {
    return this.allowFlight;
  }
  
  public void setAllowFlight(boolean flag)
  {
    this.allowFlight = flag;
  }
  
  public abstract boolean getEnableCommandBlock();
  
  public String getMotd()
  {
    return this.motd;
  }
  
  public void setMotd(String s)
  {
    this.motd = s;
  }
  
  public int getMaxBuildHeight()
  {
    return this.F;
  }
  
  public void c(int i)
  {
    this.F = i;
  }
  
  public boolean isStopped()
  {
    return this.isStopped;
  }
  
  public PlayerList getPlayerList()
  {
    return this.v;
  }
  
  public void a(PlayerList playerlist)
  {
    this.v = playerlist;
  }
  
  public void setGamemode(WorldSettings.EnumGamemode worldsettings_enumgamemode)
  {
    for (int i = 0; i < this.worlds.size(); i++) {
      ((WorldServer)getServer().worlds.get(i)).getWorldData().setGameType(worldsettings_enumgamemode);
    }
  }
  
  public ServerConnection getServerConnection()
  {
    return this.q;
  }
  
  public ServerConnection aq()
  {
    return this.q == null ? (this.q = new ServerConnection(this)) : this.q;
  }
  
  public boolean as()
  {
    return false;
  }
  
  public abstract String a(WorldSettings.EnumGamemode paramEnumGamemode, boolean paramBoolean);
  
  public int at()
  {
    return this.ticks;
  }
  
  public void au()
  {
    this.T = true;
  }
  
  public BlockPosition getChunkCoordinates()
  {
    return BlockPosition.ZERO;
  }
  
  public Vec3D d()
  {
    return new Vec3D(0.0D, 0.0D, 0.0D);
  }
  
  public World getWorld()
  {
    return (World)this.worlds.get(0);
  }
  
  public Entity f()
  {
    return null;
  }
  
  public int getSpawnProtection()
  {
    return 16;
  }
  
  public boolean a(World world, BlockPosition blockposition, EntityHuman entityhuman)
  {
    return false;
  }
  
  public void setForceGamemode(boolean flag)
  {
    this.U = flag;
  }
  
  public boolean getForceGamemode()
  {
    return this.U;
  }
  
  public Proxy ay()
  {
    return this.e;
  }
  
  public static long az()
  {
    return System.currentTimeMillis();
  }
  
  public int getIdleTimeout()
  {
    return this.G;
  }
  
  public void setIdleTimeout(int i)
  {
    this.G = i;
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    return new ChatComponentText(getName());
  }
  
  public boolean aB()
  {
    return true;
  }
  
  public MinecraftSessionService aD()
  {
    return this.W;
  }
  
  public GameProfileRepository getGameProfileRepository()
  {
    return this.Y;
  }
  
  public UserCache getUserCache()
  {
    return this.Z;
  }
  
  public ServerPing aG()
  {
    return this.r;
  }
  
  public void aH()
  {
    this.X = 0L;
  }
  
  public Entity a(UUID uuid)
  {
    WorldServer[] aworldserver = this.worldServer;
    aworldserver.length;
    for (int j = 0; j < this.worlds.size(); j++)
    {
      WorldServer worldserver = (WorldServer)this.worlds.get(j);
      if (worldserver != null)
      {
        Entity entity = worldserver.getEntity(uuid);
        if (entity != null) {
          return entity;
        }
      }
    }
    return null;
  }
  
  public boolean getSendCommandFeedback()
  {
    return ((WorldServer)getServer().worlds.get(0)).getGameRules().getBoolean("sendCommandFeedback");
  }
  
  public void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i) {}
  
  public int aI()
  {
    return 29999984;
  }
  
  public <V> ListenableFuture<V> a(Callable<V> callable)
  {
    Validate.notNull(callable);
    if (!isMainThread())
    {
      ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);
      
      this.j.add(listenablefuturetask);
      return listenablefuturetask;
    }
    try
    {
      return Futures.immediateFuture(callable.call());
    }
    catch (Exception exception)
    {
      return Futures.immediateFailedCheckedFuture(exception);
    }
  }
  
  public ListenableFuture<Object> postToMainThread(Runnable runnable)
  {
    Validate.notNull(runnable);
    return a(Executors.callable(runnable));
  }
  
  public boolean isMainThread()
  {
    return Thread.currentThread() == this.serverThread;
  }
  
  public int aK()
  {
    return 256;
  }
  
  public long aL()
  {
    return this.ab;
  }
  
  public Thread aM()
  {
    return this.serverThread;
  }
}
