package org.bukkit.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginCommandYamlParser
{
  public static List<Command> parse(Plugin plugin)
  {
    List<Command> pluginCmds = new ArrayList();
    
    Map<String, Map<String, Object>> map = plugin.getDescription().getCommands();
    if (map == null) {
      return pluginCmds;
    }
    for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
      if (((String)entry.getKey()).contains(":"))
      {
        Bukkit.getServer().getLogger().severe("Could not load command " + (String)entry.getKey() + " for plugin " + plugin.getName() + ": Illegal Characters");
      }
      else
      {
        Command newCmd = new PluginCommand((String)entry.getKey(), plugin);
        Object description = ((Map)entry.getValue()).get("description");
        Object usage = ((Map)entry.getValue()).get("usage");
        Object aliases = ((Map)entry.getValue()).get("aliases");
        Object permission = ((Map)entry.getValue()).get("permission");
        Object permissionMessage = ((Map)entry.getValue()).get("permission-message");
        if (description != null) {
          newCmd.setDescription(description.toString());
        }
        if (usage != null) {
          newCmd.setUsage(usage.toString());
        }
        if (aliases != null)
        {
          List<String> aliasList = new ArrayList();
          if ((aliases instanceof List)) {
            for (Object o : (List)aliases) {
              if (o.toString().contains(":")) {
                Bukkit.getServer().getLogger().severe("Could not load alias " + o.toString() + " for plugin " + plugin.getName() + ": Illegal Characters");
              } else {
                aliasList.add(o.toString());
              }
            }
          } else if (aliases.toString().contains(":")) {
            Bukkit.getServer().getLogger().severe("Could not load alias " + aliases.toString() + " for plugin " + plugin.getName() + ": Illegal Characters");
          } else {
            aliasList.add(aliases.toString());
          }
          newCmd.setAliases(aliasList);
        }
        if (permission != null) {
          newCmd.setPermission(permission.toString());
        }
        if (permissionMessage != null) {
          newCmd.setPermissionMessage(permissionMessage.toString());
        }
        pluginCmds.add(newCmd);
      }
    }
    return pluginCmds;
  }
}
