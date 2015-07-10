package org.bukkit.command.defaults;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.StringUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VersionCommand
  extends BukkitCommand
{
  public VersionCommand(String name)
  {
    super(name);
    
    this.description = "Gets the version of this server including any plugins in use";
    this.usageMessage = "/version [plugin name]";
    setPermission("bukkit.command.version");
    setAliases(Arrays.asList(new String[] { "ver", "about" }));
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length == 0)
    {
      sender.sendMessage("This server is running " + Bukkit.getName() + " version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")");
      sendVersion(sender);
    }
    else
    {
      StringBuilder name = new StringBuilder();
      String[] arrayOfString;
      int i = (arrayOfString = args).length;
      for (int j = 0; j < i; j++)
      {
        String arg = arrayOfString[j];
        if (name.length() > 0) {
          name.append(' ');
        }
        name.append(arg);
      }
      String pluginName = name.toString();
      Plugin exactPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
      if (exactPlugin != null)
      {
        describeToSender(exactPlugin, sender);
        return true;
      }
      boolean found = false;
      pluginName = pluginName.toLowerCase();
      Plugin[] arrayOfPlugin;
      int k = (arrayOfPlugin = Bukkit.getPluginManager().getPlugins()).length;
      for (int m = 0; m < k; m++)
      {
        Plugin plugin = arrayOfPlugin[m];
        if (plugin.getName().toLowerCase().contains(pluginName))
        {
          describeToSender(plugin, sender);
          found = true;
        }
      }
      if (!found)
      {
        sender.sendMessage("This server is not running any plugin by that name.");
        sender.sendMessage("Use /plugins to get a list of plugins.");
      }
    }
    return true;
  }
  
  private void describeToSender(Plugin plugin, CommandSender sender)
  {
    PluginDescriptionFile desc = plugin.getDescription();
    sender.sendMessage(ChatColor.GREEN + desc.getName() + ChatColor.WHITE + " version " + ChatColor.GREEN + desc.getVersion());
    if (desc.getDescription() != null) {
      sender.sendMessage(desc.getDescription());
    }
    if (desc.getWebsite() != null) {
      sender.sendMessage("Website: " + ChatColor.GREEN + desc.getWebsite());
    }
    if (!desc.getAuthors().isEmpty()) {
      if (desc.getAuthors().size() == 1) {
        sender.sendMessage("Author: " + getAuthors(desc));
      } else {
        sender.sendMessage("Authors: " + getAuthors(desc));
      }
    }
  }
  
  private String getAuthors(PluginDescriptionFile desc)
  {
    StringBuilder result = new StringBuilder();
    List<String> authors = desc.getAuthors();
    for (int i = 0; i < authors.size(); i++)
    {
      if (result.length() > 0)
      {
        result.append(ChatColor.WHITE);
        if (i < authors.size() - 1) {
          result.append(", ");
        } else {
          result.append(" and ");
        }
      }
      result.append(ChatColor.GREEN);
      result.append((String)authors.get(i));
    }
    return result.toString();
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    if (args.length == 1)
    {
      List<String> completions = new ArrayList();
      String toComplete = args[0].toLowerCase();
      Plugin[] arrayOfPlugin;
      int i = (arrayOfPlugin = Bukkit.getPluginManager().getPlugins()).length;
      for (int j = 0; j < i; j++)
      {
        Plugin plugin = arrayOfPlugin[j];
        if (StringUtil.startsWithIgnoreCase(plugin.getName(), toComplete)) {
          completions.add(plugin.getName());
        }
      }
      return completions;
    }
    return ImmutableList.of();
  }
  
  private final ReentrantLock versionLock = new ReentrantLock();
  private boolean hasVersion = false;
  private String versionMessage = null;
  private final Set<CommandSender> versionWaiters = new HashSet();
  private boolean versionTaskStarted = false;
  private long lastCheck = 0L;
  
  private void sendVersion(CommandSender sender)
  {
    if (this.hasVersion) {
      if (System.currentTimeMillis() - this.lastCheck > 21600000L)
      {
        this.lastCheck = System.currentTimeMillis();
        this.hasVersion = false;
      }
      else
      {
        sender.sendMessage(this.versionMessage);
        return;
      }
    }
    this.versionLock.lock();
    try
    {
      if (this.hasVersion)
      {
        sender.sendMessage(this.versionMessage);
        return;
      }
      this.versionWaiters.add(sender);
      sender.sendMessage("Checking version, please wait...");
      if (!this.versionTaskStarted)
      {
        this.versionTaskStarted = true;
        new Thread(new Runnable()
        {
          public void run()
          {
            VersionCommand.this.obtainVersion();
          }
        })
        
          .start();
      }
    }
    finally
    {
      this.versionLock.unlock();
    }
    this.versionLock.unlock();
  }
  
  private void obtainVersion()
  {
    String version = Bukkit.getVersion();
    if (version == null) {
      version = "Custom";
    }
    if (version.startsWith("git-Spigot-"))
    {
      String[] parts = version.substring("git-Spigot-".length()).split("-");
      int cbVersions = getDistance("craftbukkit", parts[1].substring(0, parts[1].indexOf(' ')));
      int spigotVersions = getDistance("spigot", parts[0]);
      if ((cbVersions == -1) || (spigotVersions == -1)) {
        setVersionMessage("Error obtaining version information");
      } else if ((cbVersions == 0) && (spigotVersions == 0)) {
        setVersionMessage("You are running the latest version");
      } else {
        setVersionMessage("You are " + (cbVersions + spigotVersions) + " version(s) behind");
      }
    }
    else if (version.startsWith("git-Bukkit-"))
    {
      version = version.substring("git-Bukkit-".length());
      int cbVersions = getDistance("craftbukkit", version.substring(0, version.indexOf(' ')));
      if (cbVersions == -1) {
        setVersionMessage("Error obtaining version information");
      } else if (cbVersions == 0) {
        setVersionMessage("You are running the latest version");
      } else {
        setVersionMessage("You are " + cbVersions + " version(s) behind");
      }
    }
    else
    {
      setVersionMessage("Unknown version, custom build?");
    }
  }
  
  /* Error */
  private void setVersionMessage(String msg)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 282	java/lang/System:currentTimeMillis	()J
    //   4: putfield 43	org/bukkit/command/defaults/VersionCommand:lastCheck	J
    //   7: aload_0
    //   8: aload_1
    //   9: putfield 34	org/bukkit/command/defaults/VersionCommand:versionMessage	Ljava/lang/String;
    //   12: aload_0
    //   13: getfield 30	org/bukkit/command/defaults/VersionCommand:versionLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   16: invokevirtual 287	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   19: aload_0
    //   20: iconst_1
    //   21: putfield 32	org/bukkit/command/defaults/VersionCommand:hasVersion	Z
    //   24: aload_0
    //   25: iconst_0
    //   26: putfield 41	org/bukkit/command/defaults/VersionCommand:versionTaskStarted	Z
    //   29: aload_0
    //   30: getfield 39	org/bukkit/command/defaults/VersionCommand:versionWaiters	Ljava/util/Set;
    //   33: invokeinterface 369 1 0
    //   38: astore_2
    //   39: goto +23 -> 62
    //   42: aload_2
    //   43: invokeinterface 375 1 0
    //   48: checkcast 115	org/bukkit/command/CommandSender
    //   51: astore_3
    //   52: aload_3
    //   53: aload_0
    //   54: getfield 34	org/bukkit/command/defaults/VersionCommand:versionMessage	Ljava/lang/String;
    //   57: invokeinterface 118 2 0
    //   62: aload_2
    //   63: invokeinterface 378 1 0
    //   68: ifne -26 -> 42
    //   71: aload_0
    //   72: getfield 39	org/bukkit/command/defaults/VersionCommand:versionWaiters	Ljava/util/Set;
    //   75: invokeinterface 381 1 0
    //   80: goto +15 -> 95
    //   83: astore 4
    //   85: aload_0
    //   86: getfield 30	org/bukkit/command/defaults/VersionCommand:versionLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   89: invokevirtual 290	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   92: aload 4
    //   94: athrow
    //   95: aload_0
    //   96: getfield 30	org/bukkit/command/defaults/VersionCommand:versionLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   99: invokevirtual 290	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   102: return
    // Line number table:
    //   Java source line #220	-> byte code offset #0
    //   Java source line #221	-> byte code offset #7
    //   Java source line #222	-> byte code offset #12
    //   Java source line #224	-> byte code offset #19
    //   Java source line #225	-> byte code offset #24
    //   Java source line #226	-> byte code offset #29
    //   Java source line #227	-> byte code offset #52
    //   Java source line #226	-> byte code offset #62
    //   Java source line #229	-> byte code offset #71
    //   Java source line #230	-> byte code offset #80
    //   Java source line #231	-> byte code offset #85
    //   Java source line #232	-> byte code offset #92
    //   Java source line #231	-> byte code offset #95
    //   Java source line #233	-> byte code offset #102
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	103	0	this	VersionCommand
    //   0	103	1	msg	String
    //   38	25	2	localIterator	java.util.Iterator
    //   51	2	3	sender	CommandSender
    //   83	10	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   19	83	83	finally
  }
  
  private static int getDistance(String repo, String hash)
  {
    try
    {
      BufferedReader reader = Resources.asCharSource(
        new URL("https://hub.spigotmc.org/stash/rest/api/1.0/projects/SPIGOT/repos/" + repo + "/commits?since=" + URLEncoder.encode(hash, "UTF-8") + "&withCounts=true"), 
        Charsets.UTF_8)
        .openBufferedStream();
      try
      {
        JSONObject obj = (JSONObject)new JSONParser().parse(reader);
        return ((Number)obj.get("totalCount")).intValue();
      }
      catch (ParseException ex)
      {
        ex.printStackTrace();
        return -1;
      }
      finally
      {
        reader.close();
      }
      return -1;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
