package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

@Deprecated
public class PardonIpCommand
  extends VanillaCommand
{
  public PardonIpCommand()
  {
    super("pardon-ip");
    this.description = "Allows the specified IP address to use this server";
    this.usageMessage = "/pardon-ip <address>";
    setPermission("bukkit.command.unban.ip");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length != 1)
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    if (BanIpCommand.ipValidity.matcher(args[0]).matches())
    {
      Bukkit.unbanIP(args[0]);
      Command.broadcastCommandMessage(sender, "Pardoned ip " + args[0]);
    }
    else
    {
      sender.sendMessage("Invalid ip");
    }
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws IllegalArgumentException
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    if (args.length == 1) {
      return (List)StringUtil.copyPartialMatches(args[0], Bukkit.getIPBans(), new ArrayList());
    }
    return ImmutableList.of();
  }
}
