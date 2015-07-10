package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class SetWorldSpawnCommand
  extends VanillaCommand
{
  public SetWorldSpawnCommand()
  {
    super("setworldspawn");
    this.description = "Sets a worlds's spawn point. If no coordinates are specified, the player's coordinates will be used.";
    this.usageMessage = "/setworldspawn OR /setworldspawn <x> <y> <z>";
    setPermission("bukkit.command.setworldspawn");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    Player player = null;
    World world;
    World world;
    if ((sender instanceof Player))
    {
      player = (Player)sender;
      world = player.getWorld();
    }
    else
    {
      world = (World)Bukkit.getWorlds().get(0);
    }
    int z;
    if (args.length == 0)
    {
      if (player == null)
      {
        sender.sendMessage("You can only perform this command as a player");
        return true;
      }
      Location location = player.getLocation();
      
      int x = location.getBlockX();
      int y = location.getBlockY();
      z = location.getBlockZ();
    }
    else if (args.length == 3)
    {
      try
      {
        int x = getInteger(sender, args[0], -30000000, 30000000, true);
        int y = getInteger(sender, args[1], 0, world.getMaxHeight(), true);
        z = getInteger(sender, args[2], -30000000, 30000000, true);
      }
      catch (NumberFormatException ex)
      {
        int z;
        sender.sendMessage(ex.getMessage());
        return true;
      }
    }
    else
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    int z;
    int y;
    int x;
    world.setSpawnLocation(x, y, z);
    
    Command.broadcastCommandMessage(sender, "Set world " + world.getName() + "'s spawnpoint to (" + x + ", " + y + ", " + z + ")");
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    
    return ImmutableList.of();
  }
}
