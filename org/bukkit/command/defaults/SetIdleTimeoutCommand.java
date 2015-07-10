package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@Deprecated
public class SetIdleTimeoutCommand
  extends VanillaCommand
{
  public SetIdleTimeoutCommand()
  {
    super("setidletimeout");
    this.description = "Sets the server's idle timeout";
    this.usageMessage = "/setidletimeout <Minutes until kick>";
    setPermission("bukkit.command.setidletimeout");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length == 1)
    {
      try
      {
        minutes = getInteger(sender, args[0], 0, Integer.MAX_VALUE, true);
      }
      catch (NumberFormatException ex)
      {
        int minutes;
        sender.sendMessage(ex.getMessage());
        return true;
      }
      int minutes;
      Bukkit.getServer().setIdleTimeout(minutes);
      
      Command.broadcastCommandMessage(sender, "Successfully set the idle timeout to " + minutes + " minutes.");
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
    return false;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    
    return ImmutableList.of();
  }
}
