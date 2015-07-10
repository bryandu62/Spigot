package org.bukkit.command.defaults;

import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Deprecated
public class TestForCommand
  extends VanillaCommand
{
  public TestForCommand()
  {
    super("testfor");
    this.description = "Tests whether a specifed player is online";
    this.usageMessage = "/testfor <player>";
    setPermission("bukkit.command.testfor");
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
    sender.sendMessage(ChatColor.RED + "/testfor is only usable by commandblocks with analog output.");
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
