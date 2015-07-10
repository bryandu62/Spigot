package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class SeedCommand
  extends VanillaCommand
{
  public SeedCommand()
  {
    super("seed");
    this.description = "Shows the world seed";
    this.usageMessage = "/seed";
    setPermission("bukkit.command.seed");
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    long seed;
    long seed;
    if ((sender instanceof Player)) {
      seed = ((Player)sender).getWorld().getSeed();
    } else {
      seed = ((World)Bukkit.getWorlds().get(0)).getSeed();
    }
    sender.sendMessage("Seed: " + seed);
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
