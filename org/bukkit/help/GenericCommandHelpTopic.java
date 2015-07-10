package org.bukkit.help;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.VanillaCommand;

public class GenericCommandHelpTopic
  extends HelpTopic
{
  protected Command command;
  
  public GenericCommandHelpTopic(Command command)
  {
    this.command = command;
    if (command.getLabel().startsWith("/")) {
      this.name = command.getLabel();
    } else {
      this.name = ("/" + command.getLabel());
    }
    int i = command.getDescription().indexOf("\n");
    if (i > 1) {
      this.shortText = command.getDescription().substring(0, i - 1);
    } else {
      this.shortText = command.getDescription();
    }
    StringBuffer sb = new StringBuffer();
    
    sb.append(ChatColor.GOLD);
    sb.append("Description: ");
    sb.append(ChatColor.WHITE);
    sb.append(command.getDescription());
    
    sb.append("\n");
    
    sb.append(ChatColor.GOLD);
    sb.append("Usage: ");
    sb.append(ChatColor.WHITE);
    sb.append(command.getUsage().replace("<command>", this.name.substring(1)));
    if (command.getAliases().size() > 0)
    {
      sb.append("\n");
      sb.append(ChatColor.GOLD);
      sb.append("Aliases: ");
      sb.append(ChatColor.WHITE);
      sb.append(ChatColor.WHITE + StringUtils.join(command.getAliases(), ", "));
    }
    this.fullText = sb.toString();
  }
  
  public boolean canSee(CommandSender sender)
  {
    if ((!this.command.isRegistered()) && (!(this.command instanceof VanillaCommand))) {
      return false;
    }
    if ((sender instanceof ConsoleCommandSender)) {
      return true;
    }
    if (this.amendedPermission != null) {
      return sender.hasPermission(this.amendedPermission);
    }
    return this.command.testPermissionSilent(sender);
  }
}
