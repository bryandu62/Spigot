package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@Deprecated
public class SaveOnCommand
  extends VanillaCommand
{
  public SaveOnCommand()
  {
    super("save-on");
    this.description = "Enables server autosaving";
    this.usageMessage = "/save-on";
    setPermission("bukkit.command.save.enable");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    for (World world : Bukkit.getWorlds()) {
      world.setAutoSave(true);
    }
    Command.broadcastCommandMessage(sender, "Enabled level saving..");
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws IllegalArgumentException
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    
    return ImmutableList.of();
  }
}
