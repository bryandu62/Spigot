package org.bukkit.craftbukkit.v1_8_R3.help;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.HelpTopic;

public class MultipleCommandAliasHelpTopic
  extends HelpTopic
{
  private final MultipleCommandAlias alias;
  
  public MultipleCommandAliasHelpTopic(MultipleCommandAlias alias)
  {
    this.alias = alias;
    
    this.name = ("/" + alias.getLabel());
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < alias.getCommands().length; i++)
    {
      if (i != 0) {
        sb.append(ChatColor.GOLD + " > " + ChatColor.WHITE);
      }
      sb.append("/");
      sb.append(alias.getCommands()[i].getLabel());
    }
    this.shortText = sb.toString();
    
    this.fullText = (ChatColor.GOLD + "Alias for: " + ChatColor.WHITE + getShortText());
  }
  
  public boolean canSee(CommandSender sender)
  {
    if (this.amendedPermission == null)
    {
      if ((sender instanceof ConsoleCommandSender)) {
        return true;
      }
      Command[] arrayOfCommand;
      int i = (arrayOfCommand = this.alias.getCommands()).length;
      for (int j = 0; j < i; j++)
      {
        Command command = arrayOfCommand[j];
        if (!command.testPermissionSilent(sender)) {
          return false;
        }
      }
      return true;
    }
    return sender.hasPermission(this.amendedPermission);
  }
}
