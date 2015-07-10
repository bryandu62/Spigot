package org.bukkit.command.defaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class PluginsCommand
  extends BukkitCommand
{
  public PluginsCommand(String name)
  {
    super(name);
    this.description = "Gets a list of plugins running on the server";
    this.usageMessage = "/plugins";
    setPermission("bukkit.command.plugins");
    setAliases(Arrays.asList(new String[] { "pl" }));
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    sender.sendMessage("Plugins " + getPluginList());
    return true;
  }
  
  private String getPluginList()
  {
    StringBuilder pluginList = new StringBuilder();
    Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
    Plugin[] arrayOfPlugin1;
    int i = (arrayOfPlugin1 = plugins).length;
    for (int j = 0; j < i; j++)
    {
      Plugin plugin = arrayOfPlugin1[j];
      if (pluginList.length() > 0)
      {
        pluginList.append(ChatColor.WHITE);
        pluginList.append(", ");
      }
      pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
      pluginList.append(plugin.getDescription().getName());
    }
    return "(" + plugins.length + "): " + pluginList.toString();
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws IllegalArgumentException
  {
    return Collections.emptyList();
  }
}
