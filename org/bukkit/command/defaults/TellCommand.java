package org.bukkit.command.defaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class TellCommand
  extends VanillaCommand
{
  public TellCommand()
  {
    super("tell");
    this.description = "Sends a private message to the given player";
    this.usageMessage = "/tell <player> <message>";
    setAliases(Arrays.asList(new String[] { "w", "msg" }));
    setPermission("bukkit.command.tell");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length < 2)
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    Player player = Bukkit.getPlayerExact(args[0]);
    if ((player == null) || (((sender instanceof Player)) && (!((Player)sender).canSee(player))))
    {
      sender.sendMessage("There's no player by that name online.");
    }
    else
    {
      StringBuilder message = new StringBuilder();
      for (int i = 1; i < args.length; i++)
      {
        if (i > 1) {
          message.append(" ");
        }
        message.append(args[i]);
      }
      String result = ChatColor.GRAY + sender.getName() + " whispers " + message;
      
      sender.sendMessage("[" + sender.getName() + "->" + player.getName() + "] " + message);
      player.sendMessage(result);
    }
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws IllegalArgumentException
  {
    if (args.length == 0) {
      return super.tabComplete(sender, alias, args);
    }
    return Collections.emptyList();
  }
}
