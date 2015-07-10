package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Deprecated
public class MeCommand
  extends VanillaCommand
{
  public MeCommand()
  {
    super("me");
    this.description = "Performs the specified action in chat";
    this.usageMessage = "/me <action>";
    setPermission("bukkit.command.me");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length < 1)
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    StringBuilder message = new StringBuilder();
    message.append(sender.getName());
    String[] arrayOfString;
    int i = (arrayOfString = args).length;
    for (int j = 0; j < i; j++)
    {
      String arg = arrayOfString[j];
      message.append(" ");
      message.append(arg);
    }
    Bukkit.broadcastMessage("* " + message.toString());
    
    return true;
  }
}
