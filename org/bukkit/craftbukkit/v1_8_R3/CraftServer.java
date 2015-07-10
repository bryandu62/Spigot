package org.bukkit.craftbukkit.v1_8_R3;

import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.DbDdlSyntax;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChunkProviderServer;
import net.minecraft.server.v1_8_R3.CommandAbstract;
import net.minecraft.server.v1_8_R3.CommandDispatcher;
import net.minecraft.server.v1_8_R3.Convertable;
import net.minecraft.server.v1_8_R3.CraftingManager;
import net.minecraft.server.v1_8_R3.DedicatedPlayerList;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityTracker;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.GameProfileBanList;
import net.minecraft.server.v1_8_R3.ICommand;
import net.minecraft.server.v1_8_R3.ICommandListener;
import net.minecraft.server.v1_8_R3.IDataManager;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.IpBanList;
import net.minecraft.server.v1_8_R3.ItemWorldMap;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.JsonListEntry;
import net.minecraft.server.v1_8_R3.LocaleI18n;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.OpList;
import net.minecraft.server.v1_8_R3.PersistentCollection;
import net.minecraft.server.v1_8_R3.PlayerList;
import net.minecraft.server.v1_8_R3.PropertyManager;
import net.minecraft.server.v1_8_R3.RecipesFurnace;
import net.minecraft.server.v1_8_R3.RegionFile;
import net.minecraft.server.v1_8_R3.RegionFileCache;
import net.minecraft.server.v1_8_R3.ServerCommand;
import net.minecraft.server.v1_8_R3.ServerNBTManager;
import net.minecraft.server.v1_8_R3.UserCache;
import net.minecraft.server.v1_8_R3.WhiteList;
import net.minecraft.server.v1_8_R3.WorldData;
import net.minecraft.server.v1_8_R3.WorldLoaderServer;
import net.minecraft.server.v1_8_R3.WorldManager;
import net.minecraft.server.v1_8_R3.WorldMap;
import net.minecraft.server.v1_8_R3.WorldNBTStorage;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.WorldSettings;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Server.Spigot;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.v1_8_R3.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_8_R3.inventory.RecipeIterator;
import org.bukkit.craftbukkit.v1_8_R3.map.CraftMapView;
import org.bukkit.craftbukkit.v1_8_R3.metadata.EntityMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.v1_8_R3.potion.CraftPotionBrewer;
import org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftIconCache;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_8_R3.util.DatFileFilter;
import org.bukkit.craftbukkit.v1_8_R3.util.Versioning;
import org.bukkit.craftbukkit.v1_8_R3.util.permissions.CraftDefaultPermissions;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitWorker;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.spigotmc.SpigotConfig;
import org.spigotmc.SpigotWorldConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public final class CraftServer
  implements Server
{
  private static final Player[] EMPTY_PLAYER_ARRAY = new Player[0];
  private final String serverName = "CraftBukkit";
  private final String serverVersion;
  private final String bukkitVersion = Versioning.getBukkitVersion();
  private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Minecraft");
  private final ServicesManager servicesManager = new SimpleServicesManager();
  private final CraftScheduler scheduler = new CraftScheduler();
  private final SimpleCommandMap commandMap = new SimpleCommandMap(this);
  private final SimpleHelpMap helpMap = new SimpleHelpMap(this);
  private final StandardMessenger messenger = new StandardMessenger();
  private final PluginManager pluginManager = new SimplePluginManager(this, this.commandMap);
  protected final MinecraftServer console;
  protected final DedicatedPlayerList playerList;
  private final Map<String, World> worlds = new LinkedHashMap();
  private YamlConfiguration configuration;
  private YamlConfiguration commandsConfiguration;
  private final Yaml yaml = new Yaml(new SafeConstructor());
  private final Map<UUID, OfflinePlayer> offlinePlayers = new MapMaker().softValues().makeMap();
  private final EntityMetadataStore entityMetadata = new EntityMetadataStore();
  private final PlayerMetadataStore playerMetadata = new PlayerMetadataStore();
  private final WorldMetadataStore worldMetadata = new WorldMetadataStore();
  private int monsterSpawn = -1;
  private int animalSpawn = -1;
  private int waterAnimalSpawn = -1;
  private int ambientSpawn = -1;
  public int chunkGCPeriod = -1;
  public int chunkGCLoadThresh = 0;
  private File container;
  private Warning.WarningState warningState = Warning.WarningState.DEFAULT;
  private final BooleanWrapper online = new BooleanWrapper(null);
  public CraftScoreboardManager scoreboardManager;
  public boolean playerCommandState;
  private boolean printSaveWarning;
  private CraftIconCache icon;
  private boolean overrideAllCommandBlockCommands = false;
  private final Pattern validUserPattern = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
  private final UUID invalidUserUUID = UUID.nameUUIDFromBytes("InvalidUsername".getBytes(Charsets.UTF_8));
  private final List<CraftPlayer> playerView;
  public int reloadCount;
  
  private final class BooleanWrapper
  {
    private boolean value = true;
    
    private BooleanWrapper() {}
  }
  
  static
  {
    ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
    CraftItemFactory.instance();
  }
  
  public CraftServer(MinecraftServer console, PlayerList playerList)
  {
    this.console = console;
    this.playerList = ((DedicatedPlayerList)playerList);
    this.playerView = Collections.unmodifiableList(Lists.transform(playerList.players, new Function()
    {
      public CraftPlayer apply(EntityPlayer player)
      {
        return player.getBukkitEntity();
      }
    }));
    this.serverVersion = CraftServer.class.getPackage().getImplementationVersion();
    this.online.value = console.getPropertyManager().getBoolean("online-mode", true);
    
    Bukkit.setServer(this);
    
    net.minecraft.server.v1_8_R3.Enchantment.DAMAGE_ALL.getClass();
    org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    
    Potion.setPotionBrewer(new CraftPotionBrewer());
    MobEffectList.BLINDNESS.getClass();
    PotionEffectType.stopAcceptingRegistrations();
    if (!Main.useConsole) {
      getLogger().info("Console input is disabled due to --noconsole command argument");
    }
    this.configuration = YamlConfiguration.loadConfiguration(getConfigFile());
    this.configuration.options().copyDefaults(true);
    this.configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), Charsets.UTF_8)));
    ConfigurationSection legacyAlias = null;
    if (!this.configuration.isString("aliases"))
    {
      legacyAlias = this.configuration.getConfigurationSection("aliases");
      this.configuration.set("aliases", "now-in-commands.yml");
    }
    saveConfig();
    if (getCommandsConfigFile().isFile()) {
      legacyAlias = null;
    }
    this.commandsConfiguration = YamlConfiguration.loadConfiguration(getCommandsConfigFile());
    this.commandsConfiguration.options().copyDefaults(true);
    this.commandsConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), Charsets.UTF_8)));
    saveCommandsConfig();
    if (legacyAlias != null)
    {
      ConfigurationSection aliases = this.commandsConfiguration.createSection("aliases");
      for (String key : legacyAlias.getKeys(false))
      {
        ArrayList<String> commands = new ArrayList();
        if (legacyAlias.isList(key)) {
          for (String command : legacyAlias.getStringList(key)) {
            commands.add(command + " $1-");
          }
        } else {
          commands.add(legacyAlias.getString(key) + " $1-");
        }
        aliases.set(key, commands);
      }
    }
    saveCommandsConfig();
    this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
    ((SimplePluginManager)this.pluginManager).useTimings(this.configuration.getBoolean("settings.plugin-profiling"));
    this.monsterSpawn = this.configuration.getInt("spawn-limits.monsters");
    this.animalSpawn = this.configuration.getInt("spawn-limits.animals");
    this.waterAnimalSpawn = this.configuration.getInt("spawn-limits.water-animals");
    this.ambientSpawn = this.configuration.getInt("spawn-limits.ambient");
    console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
    this.warningState = Warning.WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
    this.chunkGCPeriod = this.configuration.getInt("chunk-gc.period-in-ticks");
    this.chunkGCLoadThresh = this.configuration.getInt("chunk-gc.load-threshold");
    loadIcon();
  }
  
  public boolean getCommandBlockOverride(String command)
  {
    return (this.overrideAllCommandBlockCommands) || (this.commandsConfiguration.getStringList("command-block-overrides").contains(command));
  }
  
  private File getConfigFile()
  {
    return (File)this.console.options.valueOf("bukkit-settings");
  }
  
  private File getCommandsConfigFile()
  {
    return (File)this.console.options.valueOf("commands-settings");
  }
  
  private void saveConfig()
  {
    try
    {
      this.configuration.save(getConfigFile());
    }
    catch (IOException ex)
    {
      java.util.logging.Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + getConfigFile(), ex);
    }
  }
  
  private void saveCommandsConfig()
  {
    try
    {
      this.commandsConfiguration.save(getCommandsConfigFile());
    }
    catch (IOException ex)
    {
      java.util.logging.Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + getCommandsConfigFile(), ex);
    }
  }
  
  public void loadPlugins()
  {
    this.pluginManager.registerInterface(JavaPluginLoader.class);
    
    File pluginFolder = (File)this.console.options.valueOf("plugins");
    if (pluginFolder.exists())
    {
      Plugin[] plugins = this.pluginManager.loadPlugins(pluginFolder);
      Plugin[] arrayOfPlugin1;
      int i = (arrayOfPlugin1 = plugins).length;
      for (int j = 0; j < i; j++)
      {
        Plugin plugin = arrayOfPlugin1[j];
        try
        {
          String message = String.format("Loading %s", new Object[] { plugin.getDescription().getFullName() });
          plugin.getLogger().info(message);
          plugin.onLoad();
        }
        catch (Throwable ex)
        {
          java.util.logging.Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
      }
    }
    else
    {
      pluginFolder.mkdir();
    }
  }
  
  public void enablePlugins(PluginLoadOrder type)
  {
    if (type == PluginLoadOrder.STARTUP)
    {
      this.helpMap.clear();
      this.helpMap.initializeGeneralTopics();
    }
    Plugin[] plugins = this.pluginManager.getPlugins();
    Plugin[] arrayOfPlugin1;
    int i = (arrayOfPlugin1 = plugins).length;
    for (int j = 0; j < i; j++)
    {
      Plugin plugin = arrayOfPlugin1[j];
      if ((!plugin.isEnabled()) && (plugin.getDescription().getLoad() == type)) {
        loadPlugin(plugin);
      }
    }
    if (type == PluginLoadOrder.POSTWORLD)
    {
      setVanillaCommands(true);
      this.commandMap.setFallbackCommands();
      setVanillaCommands(false);
      
      this.commandMap.registerServerAliases();
      loadCustomPermissions();
      DefaultPermissions.registerCorePermissions();
      CraftDefaultPermissions.registerCorePermissions();
      this.helpMap.initializeCommands();
    }
  }
  
  public void disablePlugins()
  {
    this.pluginManager.disablePlugins();
  }
  
  private void setVanillaCommands(boolean first)
  {
    Map<String, ICommand> commands = new CommandDispatcher().getCommands();
    for (ICommand cmd : commands.values())
    {
      VanillaCommandWrapper wrapper = new VanillaCommandWrapper((CommandAbstract)cmd, LocaleI18n.get(cmd.getUsage(null)));
      if (SpigotConfig.replaceCommands.contains(wrapper.getName()))
      {
        if (first) {
          this.commandMap.register("minecraft", wrapper);
        }
      }
      else if (!first) {
        this.commandMap.register("minecraft", wrapper);
      }
    }
  }
  
  private void loadPlugin(Plugin plugin)
  {
    try
    {
      this.pluginManager.enablePlugin(plugin);
      
      List<Permission> perms = plugin.getDescription().getPermissions();
      for (Permission perm : perms) {
        try
        {
          this.pluginManager.addPermission(perm);
        }
        catch (IllegalArgumentException ex)
        {
          getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
        }
      }
    }
    catch (Throwable ex)
    {
      java.util.logging.Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
    }
  }
  
  public String getName()
  {
    return "CraftBukkit";
  }
  
  public String getVersion()
  {
    return this.serverVersion + " (MC: " + this.console.getVersion() + ")";
  }
  
  public String getBukkitVersion()
  {
    return this.bukkitVersion;
  }
  
  public List<CraftPlayer> getOnlinePlayers()
  {
    return this.playerView;
  }
  
  @Deprecated
  public Player getPlayer(String name)
  {
    Validate.notNull(name, "Name cannot be null");
    
    Player found = null;
    String lowerName = name.toLowerCase();
    int delta = Integer.MAX_VALUE;
    for (Player player : getOnlinePlayers()) {
      if (player.getName().toLowerCase().startsWith(lowerName))
      {
        int curDelta = player.getName().length() - lowerName.length();
        if (curDelta < delta)
        {
          found = player;
          delta = curDelta;
        }
        if (curDelta == 0) {
          break;
        }
      }
    }
    return found;
  }
  
  @Deprecated
  public Player getPlayerExact(String name)
  {
    Validate.notNull(name, "Name cannot be null");
    
    String lname = name.toLowerCase();
    for (Player player : getOnlinePlayers()) {
      if (player.getName().equalsIgnoreCase(lname)) {
        return player;
      }
    }
    return null;
  }
  
  public Player getPlayer(UUID id)
  {
    EntityPlayer player = this.playerList.a(id);
    if (player != null) {
      return player.getBukkitEntity();
    }
    return null;
  }
  
  public int broadcastMessage(String message)
  {
    return broadcast(message, "bukkit.broadcast.user");
  }
  
  public Player getPlayer(EntityPlayer entity)
  {
    return entity.getBukkitEntity();
  }
  
  @Deprecated
  public List<Player> matchPlayer(String partialName)
  {
    Validate.notNull(partialName, "PartialName cannot be null");
    
    List<Player> matchedPlayers = new ArrayList();
    for (Player iterPlayer : getOnlinePlayers())
    {
      String iterPlayerName = iterPlayer.getName();
      if (partialName.equalsIgnoreCase(iterPlayerName))
      {
        matchedPlayers.clear();
        matchedPlayers.add(iterPlayer);
        break;
      }
      if (iterPlayerName.toLowerCase().contains(partialName.toLowerCase())) {
        matchedPlayers.add(iterPlayer);
      }
    }
    return matchedPlayers;
  }
  
  public int getMaxPlayers()
  {
    return this.playerList.getMaxPlayers();
  }
  
  public int getPort()
  {
    return getConfigInt("server-port", 25565);
  }
  
  public int getViewDistance()
  {
    return getConfigInt("view-distance", 10);
  }
  
  public String getIp()
  {
    return getConfigString("server-ip", "");
  }
  
  public String getServerName()
  {
    return getConfigString("server-name", "Unknown Server");
  }
  
  public String getServerId()
  {
    return getConfigString("server-id", "unnamed");
  }
  
  public String getWorldType()
  {
    return getConfigString("level-type", "DEFAULT");
  }
  
  public boolean getGenerateStructures()
  {
    return getConfigBoolean("generate-structures", true);
  }
  
  public boolean getAllowEnd()
  {
    return this.configuration.getBoolean("settings.allow-end");
  }
  
  public boolean getAllowNether()
  {
    return getConfigBoolean("allow-nether", true);
  }
  
  public boolean getWarnOnOverload()
  {
    return this.configuration.getBoolean("settings.warn-on-overload");
  }
  
  public boolean getQueryPlugins()
  {
    return this.configuration.getBoolean("settings.query-plugins");
  }
  
  public boolean hasWhitelist()
  {
    return getConfigBoolean("white-list", false);
  }
  
  private String getConfigString(String variable, String defaultValue)
  {
    return this.console.getPropertyManager().getString(variable, defaultValue);
  }
  
  private int getConfigInt(String variable, int defaultValue)
  {
    return this.console.getPropertyManager().getInt(variable, defaultValue);
  }
  
  private boolean getConfigBoolean(String variable, boolean defaultValue)
  {
    return this.console.getPropertyManager().getBoolean(variable, defaultValue);
  }
  
  public String getUpdateFolder()
  {
    return this.configuration.getString("settings.update-folder", "update");
  }
  
  public File getUpdateFolderFile()
  {
    return new File((File)this.console.options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
  }
  
  public long getConnectionThrottle()
  {
    if (SpigotConfig.bungee) {
      return -1L;
    }
    return this.configuration.getInt("settings.connection-throttle");
  }
  
  public int getTicksPerAnimalSpawns()
  {
    return this.configuration.getInt("ticks-per.animal-spawns");
  }
  
  public int getTicksPerMonsterSpawns()
  {
    return this.configuration.getInt("ticks-per.monster-spawns");
  }
  
  public PluginManager getPluginManager()
  {
    return this.pluginManager;
  }
  
  public CraftScheduler getScheduler()
  {
    return this.scheduler;
  }
  
  public ServicesManager getServicesManager()
  {
    return this.servicesManager;
  }
  
  public List<World> getWorlds()
  {
    return new ArrayList(this.worlds.values());
  }
  
  public DedicatedPlayerList getHandle()
  {
    return this.playerList;
  }
  
  public boolean dispatchServerCommand(CommandSender sender, ServerCommand serverCommand)
  {
    if ((sender instanceof Conversable))
    {
      Conversable conversable = (Conversable)sender;
      if (conversable.isConversing())
      {
        conversable.acceptConversationInput(serverCommand.command);
        return true;
      }
    }
    try
    {
      this.playerCommandState = true;
      return dispatchCommand(sender, serverCommand.command);
    }
    catch (Exception ex)
    {
      getLogger().log(Level.WARNING, "Unexpected exception while parsing console command \"" + serverCommand.command + '"', ex);
      return false;
    }
    finally
    {
      this.playerCommandState = false;
    }
  }
  
  public boolean dispatchCommand(CommandSender sender, String commandLine)
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(commandLine, "CommandLine cannot be null");
    if (this.commandMap.dispatch(sender, commandLine)) {
      return true;
    }
    sender.sendMessage(SpigotConfig.unknownCommandMessage);
    
    return false;
  }
  
  public void reload()
  {
    this.reloadCount += 1;
    this.configuration = YamlConfiguration.loadConfiguration(getConfigFile());
    this.commandsConfiguration = YamlConfiguration.loadConfiguration(getCommandsConfigFile());
    PropertyManager config = new PropertyManager(this.console.options);
    
    ((DedicatedServer)this.console).propertyManager = config;
    
    boolean animals = config.getBoolean("spawn-animals", this.console.getSpawnAnimals());
    boolean monsters = config.getBoolean("spawn-monsters", ((WorldServer)this.console.worlds.get(0)).getDifficulty() != EnumDifficulty.PEACEFUL);
    EnumDifficulty difficulty = EnumDifficulty.getById(config.getInt("difficulty", ((WorldServer)this.console.worlds.get(0)).getDifficulty().ordinal()));
    
    this.online.value = config.getBoolean("online-mode", this.console.getOnlineMode());
    this.console.setSpawnAnimals(config.getBoolean("spawn-animals", this.console.getSpawnAnimals()));
    this.console.setPVP(config.getBoolean("pvp", this.console.getPVP()));
    this.console.setAllowFlight(config.getBoolean("allow-flight", this.console.getAllowFlight()));
    this.console.setMotd(config.getString("motd", this.console.getMotd()));
    this.monsterSpawn = this.configuration.getInt("spawn-limits.monsters");
    this.animalSpawn = this.configuration.getInt("spawn-limits.animals");
    this.waterAnimalSpawn = this.configuration.getInt("spawn-limits.water-animals");
    this.ambientSpawn = this.configuration.getInt("spawn-limits.ambient");
    this.warningState = Warning.WarningState.value(this.configuration.getString("settings.deprecated-verbose"));
    this.printSaveWarning = false;
    this.console.autosavePeriod = this.configuration.getInt("ticks-per.autosave");
    this.chunkGCPeriod = this.configuration.getInt("chunk-gc.period-in-ticks");
    this.chunkGCLoadThresh = this.configuration.getInt("chunk-gc.load-threshold");
    loadIcon();
    try
    {
      this.playerList.getIPBans().load();
    }
    catch (IOException ex)
    {
      this.logger.log(Level.WARNING, "Failed to load banned-ips.json, " + ex.getMessage());
    }
    try
    {
      this.playerList.getProfileBans().load();
    }
    catch (IOException ex)
    {
      this.logger.log(Level.WARNING, "Failed to load banned-players.json, " + ex.getMessage());
    }
    SpigotConfig.init((File)this.console.options.valueOf("spigot-settings"));
    for (WorldServer world : this.console.worlds)
    {
      world.worldData.setDifficulty(difficulty);
      world.setSpawnFlags(monsters, animals);
      if (getTicksPerAnimalSpawns() < 0) {
        world.ticksPerAnimalSpawns = 400L;
      } else {
        world.ticksPerAnimalSpawns = getTicksPerAnimalSpawns();
      }
      if (getTicksPerMonsterSpawns() < 0) {
        world.ticksPerMonsterSpawns = 1L;
      } else {
        world.ticksPerMonsterSpawns = getTicksPerMonsterSpawns();
      }
      world.spigotConfig.init();
    }
    this.pluginManager.clearPlugins();
    this.commandMap.clearCommands();
    resetRecipes();
    SpigotConfig.registerCommands();
    
    this.overrideAllCommandBlockCommands = this.commandsConfiguration.getStringList("command-block-overrides").contains("*");
    
    int pollCount = 0;
    while ((pollCount < 50) && (getScheduler().getActiveWorkers().size() > 0))
    {
      try
      {
        Thread.sleep(50L);
      }
      catch (InterruptedException localInterruptedException) {}
      pollCount++;
    }
    Object overdueWorkers = getScheduler().getActiveWorkers();
    for (BukkitWorker worker : (List)overdueWorkers)
    {
      Plugin plugin = worker.getOwner();
      String author = "<NoAuthorGiven>";
      if (plugin.getDescription().getAuthors().size() > 0) {
        author = (String)plugin.getDescription().getAuthors().get(0);
      }
      getLogger().log(Level.SEVERE, String.format(
        "Nag author: '%s' of '%s' about the following: %s", new Object[] {
        author, 
        plugin.getDescription().getName(), 
        "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin" }));
    }
    loadPlugins();
    enablePlugins(PluginLoadOrder.STARTUP);
    enablePlugins(PluginLoadOrder.POSTWORLD);
  }
  
  private void loadIcon()
  {
    this.icon = new CraftIconCache(null);
    try
    {
      File file = new File(new File("."), "server-icon.png");
      if (file.isFile()) {
        this.icon = loadServerIcon0(file);
      }
    }
    catch (Exception ex)
    {
      getLogger().log(Level.WARNING, "Couldn't load server icon", ex);
    }
  }
  
  private void loadCustomPermissions()
  {
    File file = new File(this.configuration.getString("settings.permissions-file"));
    FileInputStream stream;
    try
    {
      stream = new FileInputStream(file);
    }
    catch (FileNotFoundException localFileNotFoundException) {}
    FileInputStream stream;
    try
    {
      file.createNewFile();
    }
    finally
    {
      return;
      try
      {
        perms = (Map)this.yaml.load(stream);
      }
      catch (MarkedYAMLException ex)
      {
        Map<String, Map<String, Object>> perms;
        getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML: " + ex.toString());
        return;
      }
      catch (Throwable ex)
      {
        getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML.", ex);
        return;
      }
      finally
      {
        try
        {
          stream.close();
        }
        catch (IOException localIOException3) {}
      }
    }
    Map<String, Map<String, Object>> perms;
    try
    {
      stream.close();
    }
    catch (IOException localIOException4) {}
    if (perms == null)
    {
      getLogger().log(Level.INFO, "Server permissions file " + file + " is empty, ignoring it");
      return;
    }
    List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);
    for (Permission perm : permsList) {
      try
      {
        this.pluginManager.addPermission(perm);
      }
      catch (IllegalArgumentException ex)
      {
        getLogger().log(Level.SEVERE, "Permission in " + file + " was already defined", ex);
      }
    }
  }
  
  public String toString()
  {
    return "CraftServer{serverName=CraftBukkit,serverVersion=" + this.serverVersion + ",minecraftVersion=" + this.console.getVersion() + '}';
  }
  
  public World createWorld(String name, World.Environment environment)
  {
    return WorldCreator.name(name).environment(environment).createWorld();
  }
  
  public World createWorld(String name, World.Environment environment, long seed)
  {
    return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
  }
  
  public World createWorld(String name, World.Environment environment, ChunkGenerator generator)
  {
    return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
  }
  
  public World createWorld(String name, World.Environment environment, long seed, ChunkGenerator generator)
  {
    return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
  }
  
  public World createWorld(WorldCreator creator)
  {
    Validate.notNull(creator, "Creator may not be null");
    
    String name = creator.name();
    ChunkGenerator generator = creator.generator();
    File folder = new File(getWorldContainer(), name);
    World world = getWorld(name);
    net.minecraft.server.v1_8_R3.WorldType type = net.minecraft.server.v1_8_R3.WorldType.getType(creator.type().getName());
    boolean generateStructures = creator.generateStructures();
    if (world != null) {
      return world;
    }
    if ((folder.exists()) && (!folder.isDirectory())) {
      throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
    }
    if (generator == null) {
      generator = getGenerator(name);
    }
    Convertable converter = new WorldLoaderServer(getWorldContainer());
    if (converter.isConvertable(name))
    {
      getLogger().info("Converting world '" + name + "'");
      converter.convert(name, new IProgressUpdate()
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
    int dimension = 10 + this.console.worlds.size();
    boolean used = false;
    do
    {
      for (WorldServer server : this.console.worlds)
      {
        used = server.dimension == dimension;
        if (used)
        {
          dimension++;
          break;
        }
      }
    } while (used);
    boolean hardcore = false;
    
    Object sdm = new ServerNBTManager(getWorldContainer(), name, true);
    WorldData worlddata = ((IDataManager)sdm).getWorldData();
    if (worlddata == null)
    {
      WorldSettings worldSettings = new WorldSettings(creator.seed(), WorldSettings.EnumGamemode.getById(getDefaultGameMode().getValue()), generateStructures, hardcore, type);
      worldSettings.setGeneratorSettings(creator.generatorSettings());
      worlddata = new WorldData(worldSettings, name);
    }
    worlddata.checkName(name);
    WorldServer internal = (WorldServer)new WorldServer(this.console, (IDataManager)sdm, worlddata, dimension, this.console.methodProfiler, creator.environment(), generator).b();
    if (!this.worlds.containsKey(name.toLowerCase())) {
      return null;
    }
    internal.scoreboard = getScoreboardManager().getMainScoreboard().getHandle();
    
    internal.tracker = new EntityTracker(internal);
    internal.addIWorldAccess(new WorldManager(this.console, internal));
    internal.worldData.setDifficulty(EnumDifficulty.EASY);
    internal.setSpawnFlags(true, true);
    this.console.worlds.add(internal);
    if (generator != null) {
      internal.getWorld().getPopulators().addAll(generator.getDefaultPopulators(internal.getWorld()));
    }
    this.pluginManager.callEvent(new WorldInitEvent(internal.getWorld()));
    System.out.print("Preparing start region for level " + (this.console.worlds.size() - 1) + " (Seed: " + internal.getSeed() + ")");
    if (internal.getWorld().getKeepSpawnInMemory())
    {
      short short1 = 196;
      long i = System.currentTimeMillis();
      for (int j = -short1; j <= short1; j += 16) {
        for (int k = -short1; k <= short1; k += 16)
        {
          long l = System.currentTimeMillis();
          if (l < i) {
            i = l;
          }
          if (l > i + 1000L)
          {
            int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
            int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;
            
            System.out.println("Preparing spawn area for " + name + ", " + j1 * 100 / i1 + "%");
            i = l;
          }
          BlockPosition chunkcoordinates = internal.getSpawn();
          internal.chunkProviderServer.getChunkAt(chunkcoordinates.getX() + j >> 4, chunkcoordinates.getZ() + k >> 4);
        }
      }
    }
    this.pluginManager.callEvent(new WorldLoadEvent(internal.getWorld()));
    return internal.getWorld();
  }
  
  public boolean unloadWorld(String name, boolean save)
  {
    return unloadWorld(getWorld(name), save);
  }
  
  public boolean unloadWorld(World world, boolean save)
  {
    if (world == null) {
      return false;
    }
    WorldServer handle = ((CraftWorld)world).getHandle();
    if (!this.console.worlds.contains(handle)) {
      return false;
    }
    if (handle.dimension <= 1) {
      return false;
    }
    if (handle.players.size() > 0) {
      return false;
    }
    WorldUnloadEvent e = new WorldUnloadEvent(handle.getWorld());
    this.pluginManager.callEvent(e);
    if (e.isCancelled()) {
      return false;
    }
    if (save) {
      try
      {
        handle.save(true, null);
        handle.saveLevel();
      }
      catch (ExceptionWorldConflict ex)
      {
        getLogger().log(Level.SEVERE, null, ex);
      }
    }
    this.worlds.remove(world.getName().toLowerCase());
    this.console.worlds.remove(this.console.worlds.indexOf(handle));
    
    File parentFolder = world.getWorldFolder().getAbsoluteFile();
    synchronized (RegionFileCache.class)
    {
      Iterator<Map.Entry<File, RegionFile>> i = RegionFileCache.a.entrySet().iterator();
      File child;
      for (; i.hasNext(); child != null)
      {
        Map.Entry<File, RegionFile> entry = (Map.Entry)i.next();
        child = ((File)entry.getKey()).getAbsoluteFile();
        continue;
        if (child.equals(parentFolder))
        {
          i.remove();
          try
          {
            ((RegionFile)entry.getValue()).c();
          }
          catch (IOException ex)
          {
            getLogger().log(Level.SEVERE, null, ex);
          }
        }
        child = child.getParentFile();
      }
    }
    return true;
  }
  
  public MinecraftServer getServer()
  {
    return this.console;
  }
  
  public World getWorld(String name)
  {
    Validate.notNull(name, "Name cannot be null");
    
    return (World)this.worlds.get(name.toLowerCase());
  }
  
  public World getWorld(UUID uid)
  {
    for (World world : this.worlds.values()) {
      if (world.getUID().equals(uid)) {
        return world;
      }
    }
    return null;
  }
  
  public void addWorld(World world)
  {
    if (getWorld(world.getUID()) != null)
    {
      System.out.println("World " + world.getName() + " is a duplicate of another world and has been prevented from loading. Please delete the uid.dat file from " + world.getName() + "'s world directory if you want to be able to load the duplicate world.");
      return;
    }
    this.worlds.put(world.getName().toLowerCase(), world);
  }
  
  public java.util.logging.Logger getLogger()
  {
    return this.logger;
  }
  
  public ConsoleReader getReader()
  {
    return this.console.reader;
  }
  
  public PluginCommand getPluginCommand(String name)
  {
    Command command = this.commandMap.getCommand(name);
    if ((command instanceof PluginCommand)) {
      return (PluginCommand)command;
    }
    return null;
  }
  
  public void savePlayers()
  {
    checkSaveState();
    this.playerList.savePlayers();
  }
  
  public void configureDbConfig(ServerConfig config)
  {
    Validate.notNull(config, "Config cannot be null");
    
    DataSourceConfig ds = new DataSourceConfig();
    ds.setDriver(this.configuration.getString("database.driver"));
    ds.setUrl(this.configuration.getString("database.url"));
    ds.setUsername(this.configuration.getString("database.username"));
    ds.setPassword(this.configuration.getString("database.password"));
    ds.setIsolationLevel(TransactionIsolation.getLevel(this.configuration.getString("database.isolation")));
    if (ds.getDriver().contains("sqlite"))
    {
      config.setDatabasePlatform(new SQLitePlatform());
      config.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
    }
    config.setDataSourceConfig(ds);
  }
  
  public boolean addRecipe(Recipe recipe)
  {
    CraftRecipe toAdd;
    if ((recipe instanceof CraftRecipe))
    {
      toAdd = (CraftRecipe)recipe;
    }
    else
    {
      CraftRecipe toAdd;
      if ((recipe instanceof ShapedRecipe))
      {
        toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe)recipe);
      }
      else
      {
        CraftRecipe toAdd;
        if ((recipe instanceof ShapelessRecipe))
        {
          toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe)recipe);
        }
        else
        {
          CraftRecipe toAdd;
          if ((recipe instanceof FurnaceRecipe)) {
            toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe)recipe);
          } else {
            return false;
          }
        }
      }
    }
    CraftRecipe toAdd;
    toAdd.addToCraftingManager();
    CraftingManager.getInstance().sort();
    return true;
  }
  
  public List<Recipe> getRecipesFor(org.bukkit.inventory.ItemStack result)
  {
    Validate.notNull(result, "Result cannot be null");
    
    List<Recipe> results = new ArrayList();
    Iterator<Recipe> iter = recipeIterator();
    while (iter.hasNext())
    {
      Recipe recipe = (Recipe)iter.next();
      org.bukkit.inventory.ItemStack stack = recipe.getResult();
      if (stack.getType() == result.getType()) {
        if ((result.getDurability() == -1) || (result.getDurability() == stack.getDurability())) {
          results.add(recipe);
        }
      }
    }
    return results;
  }
  
  public Iterator<Recipe> recipeIterator()
  {
    return new RecipeIterator();
  }
  
  public void clearRecipes()
  {
    CraftingManager.getInstance().recipes.clear();
    RecipesFurnace.getInstance().recipes.clear();
    RecipesFurnace.getInstance().customRecipes.clear();
  }
  
  public void resetRecipes()
  {
    CraftingManager.getInstance().recipes = new CraftingManager().recipes;
    RecipesFurnace.getInstance().recipes = new RecipesFurnace().recipes;
    RecipesFurnace.getInstance().customRecipes.clear();
  }
  
  public Map<String, String[]> getCommandAliases()
  {
    ConfigurationSection section = this.commandsConfiguration.getConfigurationSection("aliases");
    Map<String, String[]> result = new LinkedHashMap();
    if (section != null) {
      for (String key : section.getKeys(false))
      {
        List<String> commands;
        List<String> commands;
        if (section.isList(key)) {
          commands = section.getStringList(key);
        } else {
          commands = ImmutableList.of(section.getString(key));
        }
        result.put(key, (String[])commands.toArray(new String[commands.size()]));
      }
    }
    return result;
  }
  
  public void removeBukkitSpawnRadius()
  {
    this.configuration.set("settings.spawn-radius", null);
    saveConfig();
  }
  
  public int getBukkitSpawnRadius()
  {
    return this.configuration.getInt("settings.spawn-radius", -1);
  }
  
  public String getShutdownMessage()
  {
    return this.configuration.getString("settings.shutdown-message");
  }
  
  public int getSpawnRadius()
  {
    return ((DedicatedServer)this.console).propertyManager.getInt("spawn-protection", 16);
  }
  
  public void setSpawnRadius(int value)
  {
    this.configuration.set("settings.spawn-radius", Integer.valueOf(value));
    saveConfig();
  }
  
  public boolean getOnlineMode()
  {
    return this.online.value;
  }
  
  public boolean getAllowFlight()
  {
    return this.console.getAllowFlight();
  }
  
  public boolean isHardcore()
  {
    return this.console.isHardcore();
  }
  
  public boolean useExactLoginLocation()
  {
    return this.configuration.getBoolean("settings.use-exact-login-location");
  }
  
  public ChunkGenerator getGenerator(String world)
  {
    ConfigurationSection section = this.configuration.getConfigurationSection("worlds");
    ChunkGenerator result = null;
    if (section != null)
    {
      section = section.getConfigurationSection(world);
      if (section != null)
      {
        String name = section.getString("generator");
        if ((name != null) && (!name.equals("")))
        {
          String[] split = name.split(":", 2);
          String id = split.length > 1 ? split[1] : null;
          Plugin plugin = this.pluginManager.getPlugin(split[0]);
          if (plugin == null) {
            getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
          } else if (!plugin.isEnabled()) {
            getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled yet (is it load:STARTUP?)");
          } else {
            try
            {
              result = plugin.getDefaultWorldGenerator(world, id);
              if (result == null) {
                getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' lacks a default world generator");
              }
            }
            catch (Throwable t)
            {
              plugin.getLogger().log(Level.SEVERE, "Could not set generator for default world '" + world + "': Plugin '" + plugin.getDescription().getFullName(), t);
            }
          }
        }
      }
    }
    return result;
  }
  
  @Deprecated
  public CraftMapView getMap(short id)
  {
    PersistentCollection collection = ((WorldServer)this.console.worlds.get(0)).worldMaps;
    WorldMap worldmap = (WorldMap)collection.get(WorldMap.class, "map_" + id);
    if (worldmap == null) {
      return null;
    }
    return worldmap.mapView;
  }
  
  public CraftMapView createMap(World world)
  {
    Validate.notNull(world, "World cannot be null");
    
    net.minecraft.server.v1_8_R3.ItemStack stack = new net.minecraft.server.v1_8_R3.ItemStack(Items.MAP, 1, -1);
    WorldMap worldmap = Items.FILLED_MAP.getSavedMap(stack, ((CraftWorld)world).getHandle());
    return worldmap.mapView;
  }
  
  public void shutdown()
  {
    this.console.safeShutdown();
  }
  
  public int broadcast(String message, String permission)
  {
    int count = 0;
    Set<Permissible> permissibles = getPluginManager().getPermissionSubscriptions(permission);
    for (Permissible permissible : permissibles) {
      if (((permissible instanceof CommandSender)) && (permissible.hasPermission(permission)))
      {
        CommandSender user = (CommandSender)permissible;
        user.sendMessage(message);
        count++;
      }
    }
    return count;
  }
  
  @Deprecated
  public OfflinePlayer getOfflinePlayer(String name)
  {
    Validate.notNull(name, "Name cannot be null");
    Preconditions.checkArgument(!StringUtils.isBlank(name), "Name cannot be blank");
    
    OfflinePlayer result = getPlayerExact(name);
    if (result == null)
    {
      GameProfile profile = null;
      if ((MinecraftServer.getServer().getOnlineMode()) || (SpigotConfig.bungee)) {
        profile = MinecraftServer.getServer().getUserCache().getProfile(name);
      }
      if (profile == null) {
        result = getOfflinePlayer(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name));
      } else {
        result = getOfflinePlayer(profile);
      }
    }
    else
    {
      this.offlinePlayers.remove(result.getUniqueId());
    }
    return result;
  }
  
  public OfflinePlayer getOfflinePlayer(UUID id)
  {
    Validate.notNull(id, "UUID cannot be null");
    
    OfflinePlayer result = getPlayer(id);
    if (result == null)
    {
      result = (OfflinePlayer)this.offlinePlayers.get(id);
      if (result == null)
      {
        result = new CraftOfflinePlayer(this, new GameProfile(id, null));
        this.offlinePlayers.put(id, result);
      }
    }
    else
    {
      this.offlinePlayers.remove(id);
    }
    return result;
  }
  
  public OfflinePlayer getOfflinePlayer(GameProfile profile)
  {
    OfflinePlayer player = new CraftOfflinePlayer(this, profile);
    this.offlinePlayers.put(profile.getId(), player);
    return player;
  }
  
  public Set<String> getIPBans()
  {
    return new HashSet(Arrays.asList(this.playerList.getIPBans().getEntries()));
  }
  
  public void banIP(String address)
  {
    Validate.notNull(address, "Address cannot be null.");
    
    getBanList(BanList.Type.IP).addBan(address, null, null, null);
  }
  
  public void unbanIP(String address)
  {
    Validate.notNull(address, "Address cannot be null.");
    
    getBanList(BanList.Type.IP).pardon(address);
  }
  
  public Set<OfflinePlayer> getBannedPlayers()
  {
    Set<OfflinePlayer> result = new HashSet();
    for (JsonListEntry entry : this.playerList.getProfileBans().getValues()) {
      result.add(getOfflinePlayer((GameProfile)entry.getKey()));
    }
    return result;
  }
  
  public BanList getBanList(BanList.Type type)
  {
    Validate.notNull(type, "Type cannot be null");
    switch (type)
    {
    case NAME: 
      return new CraftIpBanList(this.playerList.getIPBans());
    }
    return new CraftProfileBanList(this.playerList.getProfileBans());
  }
  
  public void setWhitelist(boolean value)
  {
    this.playerList.setHasWhitelist(value);
    this.console.getPropertyManager().setProperty("white-list", Boolean.valueOf(value));
  }
  
  public Set<OfflinePlayer> getWhitelistedPlayers()
  {
    Set<OfflinePlayer> result = new LinkedHashSet();
    for (JsonListEntry entry : this.playerList.getWhitelist().getValues()) {
      result.add(getOfflinePlayer((GameProfile)entry.getKey()));
    }
    return result;
  }
  
  public Set<OfflinePlayer> getOperators()
  {
    Set<OfflinePlayer> result = new HashSet();
    for (JsonListEntry entry : this.playerList.getOPs().getValues()) {
      result.add(getOfflinePlayer((GameProfile)entry.getKey()));
    }
    return result;
  }
  
  public void reloadWhitelist()
  {
    this.playerList.reloadWhitelist();
  }
  
  public GameMode getDefaultGameMode()
  {
    return GameMode.getByValue(((WorldServer)this.console.worlds.get(0)).getWorldData().getGameType().getId());
  }
  
  public void setDefaultGameMode(GameMode mode)
  {
    Validate.notNull(mode, "Mode cannot be null");
    for (World world : getWorlds()) {
      ((CraftWorld)world).getHandle().worldData.setGameType(WorldSettings.EnumGamemode.getById(mode.getValue()));
    }
  }
  
  public ConsoleCommandSender getConsoleSender()
  {
    return this.console.console;
  }
  
  public EntityMetadataStore getEntityMetadata()
  {
    return this.entityMetadata;
  }
  
  public PlayerMetadataStore getPlayerMetadata()
  {
    return this.playerMetadata;
  }
  
  public WorldMetadataStore getWorldMetadata()
  {
    return this.worldMetadata;
  }
  
  public File getWorldContainer()
  {
    if (getServer().universe != null) {
      return getServer().universe;
    }
    if (this.container == null) {
      this.container = new File(this.configuration.getString("settings.world-container", "."));
    }
    return this.container;
  }
  
  public OfflinePlayer[] getOfflinePlayers()
  {
    WorldNBTStorage storage = (WorldNBTStorage)((WorldServer)this.console.worlds.get(0)).getDataManager();
    String[] files = storage.getPlayerDir().list(new DatFileFilter());
    Set<OfflinePlayer> players = new HashSet();
    String[] arrayOfString1;
    int i = (arrayOfString1 = files).length;
    for (int j = 0; j < i; j++)
    {
      String file = arrayOfString1[j];
      try
      {
        players.add(getOfflinePlayer(UUID.fromString(file.substring(0, file.length() - 4))));
      }
      catch (IllegalArgumentException localIllegalArgumentException) {}
    }
    players.addAll(getOnlinePlayers());
    
    return (OfflinePlayer[])players.toArray(new OfflinePlayer[players.size()]);
  }
  
  public Messenger getMessenger()
  {
    return this.messenger;
  }
  
  public void sendPluginMessage(Plugin source, String channel, byte[] message)
  {
    StandardMessenger.validatePluginMessage(getMessenger(), source, channel, message);
    for (Player player : getOnlinePlayers()) {
      player.sendPluginMessage(source, channel, message);
    }
  }
  
  public Set<String> getListeningPluginChannels()
  {
    Set<String> result = new HashSet();
    for (Player player : getOnlinePlayers()) {
      result.addAll(player.getListeningPluginChannels());
    }
    return result;
  }
  
  public Inventory createInventory(InventoryHolder owner, InventoryType type)
  {
    return new CraftInventoryCustom(owner, type);
  }
  
  public Inventory createInventory(InventoryHolder owner, InventoryType type, String title)
  {
    return new CraftInventoryCustom(owner, type, title);
  }
  
  public Inventory createInventory(InventoryHolder owner, int size)
    throws IllegalArgumentException
  {
    Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
    return new CraftInventoryCustom(owner, size);
  }
  
  public Inventory createInventory(InventoryHolder owner, int size, String title)
    throws IllegalArgumentException
  {
    Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
    return new CraftInventoryCustom(owner, size, title);
  }
  
  public HelpMap getHelpMap()
  {
    return this.helpMap;
  }
  
  public SimpleCommandMap getCommandMap()
  {
    return this.commandMap;
  }
  
  public int getMonsterSpawnLimit()
  {
    return this.monsterSpawn;
  }
  
  public int getAnimalSpawnLimit()
  {
    return this.animalSpawn;
  }
  
  public int getWaterAnimalSpawnLimit()
  {
    return this.waterAnimalSpawn;
  }
  
  public int getAmbientSpawnLimit()
  {
    return this.ambientSpawn;
  }
  
  public boolean isPrimaryThread()
  {
    return Thread.currentThread().equals(this.console.primaryThread);
  }
  
  public String getMotd()
  {
    return this.console.getMotd();
  }
  
  public Warning.WarningState getWarningState()
  {
    return this.warningState;
  }
  
  public List<String> tabComplete(ICommandListener sender, String message)
  {
    if (!(sender instanceof EntityPlayer)) {
      return ImmutableList.of();
    }
    Player player = ((EntityPlayer)sender).getBukkitEntity();
    if (message.startsWith("/")) {
      return tabCompleteCommand(player, message);
    }
    return tabCompleteChat(player, message);
  }
  
  public List<String> tabCompleteCommand(Player player, String message)
  {
    if (((SpigotConfig.tabComplete < 0) || (message.length() <= SpigotConfig.tabComplete)) && (!message.contains(" "))) {
      return ImmutableList.of();
    }
    List<String> completions = null;
    try
    {
      completions = getCommandMap().tabComplete(player, message.substring(1));
    }
    catch (CommandException ex)
    {
      player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to tab-complete this command");
      getLogger().log(Level.SEVERE, "Exception when " + player.getName() + " attempted to tab complete " + message, ex);
    }
    return completions == null ? ImmutableList.of() : completions;
  }
  
  public List<String> tabCompleteChat(Player player, String message)
  {
    List<String> completions = new ArrayList();
    PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
    String token = event.getLastToken();
    for (Player p : getOnlinePlayers()) {
      if ((player.canSee(p)) && (StringUtil.startsWithIgnoreCase(p.getName(), token))) {
        completions.add(p.getName());
      }
    }
    this.pluginManager.callEvent(event);
    
    Iterator<?> it = completions.iterator();
    while (it.hasNext())
    {
      Object current = it.next();
      if (!(current instanceof String)) {
        it.remove();
      }
    }
    Collections.sort(completions, String.CASE_INSENSITIVE_ORDER);
    return completions;
  }
  
  public CraftItemFactory getItemFactory()
  {
    return CraftItemFactory.instance();
  }
  
  public CraftScoreboardManager getScoreboardManager()
  {
    return this.scoreboardManager;
  }
  
  public void checkSaveState()
  {
    if ((this.playerCommandState) || (this.printSaveWarning) || (this.console.autosavePeriod <= 0)) {
      return;
    }
    this.printSaveWarning = true;
    getLogger().log(Level.WARNING, "A manual (plugin-induced) save has been detected while server is configured to auto-save. This may affect performance.", this.warningState == Warning.WarningState.ON ? new Throwable() : null);
  }
  
  public CraftIconCache getServerIcon()
  {
    return this.icon;
  }
  
  public CraftIconCache loadServerIcon(File file)
    throws Exception
  {
    Validate.notNull(file, "File cannot be null");
    if (!file.isFile()) {
      throw new IllegalArgumentException(file + " is not a file");
    }
    return loadServerIcon0(file);
  }
  
  static CraftIconCache loadServerIcon0(File file)
    throws Exception
  {
    return loadServerIcon0(ImageIO.read(file));
  }
  
  public CraftIconCache loadServerIcon(BufferedImage image)
    throws Exception
  {
    Validate.notNull(image, "Image cannot be null");
    return loadServerIcon0(image);
  }
  
  static CraftIconCache loadServerIcon0(BufferedImage image)
    throws Exception
  {
    ByteBuf bytebuf = Unpooled.buffer();
    
    Validate.isTrue(image.getWidth() == 64, "Must be 64 pixels wide");
    Validate.isTrue(image.getHeight() == 64, "Must be 64 pixels high");
    ImageIO.write(image, "PNG", new ByteBufOutputStream(bytebuf));
    ByteBuf bytebuf1 = Base64.encode(bytebuf);
    
    return new CraftIconCache("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
  }
  
  public void setIdleTimeout(int threshold)
  {
    this.console.setIdleTimeout(threshold);
  }
  
  public int getIdleTimeout()
  {
    return this.console.getIdleTimeout();
  }
  
  @Deprecated
  public UnsafeValues getUnsafe()
  {
    return CraftMagicNumbers.INSTANCE;
  }
  
  private final Server.Spigot spigot = new Server.Spigot()
  {
    public YamlConfiguration getConfig()
    {
      return SpigotConfig.config;
    }
    
    public void broadcast(BaseComponent component)
    {
      for (Player player : CraftServer.this.getOnlinePlayers()) {
        player.spigot().sendMessage(component);
      }
    }
    
    public void broadcast(BaseComponent... components)
    {
      for (Player player : CraftServer.this.getOnlinePlayers()) {
        player.spigot().sendMessage(components);
      }
    }
  };
  
  public Server.Spigot spigot()
  {
    return this.spigot;
  }
}
