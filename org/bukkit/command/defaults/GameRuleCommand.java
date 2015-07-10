package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.StringUtil;

@Deprecated
public class GameRuleCommand
  extends VanillaCommand
{
  private static final List<String> GAMERULE_STATES = ImmutableList.of("true", "false");
  
  public GameRuleCommand()
  {
    super("gamerule");
    this.description = "Sets a server's game rules";
    this.usageMessage = "/gamerule <rule name> <value> OR /gamerule <rule name>";
    setPermission("bukkit.command.gamerule");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length > 0)
    {
      String rule = args[0];
      World world = getGameWorld(sender);
      if (world.isGameRule(rule))
      {
        if (args.length > 1)
        {
          String value = args[1];
          
          world.setGameRuleValue(rule, value);
          Command.broadcastCommandMessage(sender, "Game rule " + rule + " has been set to: " + value);
        }
        else
        {
          String value = world.getGameRuleValue(rule);
          sender.sendMessage(rule + " = " + value);
        }
      }
      else {
        sender.sendMessage(ChatColor.RED + "No game rule called " + rule + " is available");
      }
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
    sender.sendMessage("Rules: " + createString(getGameWorld(sender).getGameRules(), 0, ", "));
    
    return true;
  }
  
  private World getGameWorld(CommandSender sender)
  {
    if ((sender instanceof HumanEntity))
    {
      World world = ((HumanEntity)sender).getWorld();
      if (world != null) {
        return world;
      }
    }
    else if ((sender instanceof BlockCommandSender))
    {
      return ((BlockCommandSender)sender).getBlock().getWorld();
    }
    return (World)Bukkit.getWorlds().get(0);
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws IllegalArgumentException
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    if (args.length == 1) {
      return (List)StringUtil.copyPartialMatches(args[0], Arrays.asList(getGameWorld(sender).getGameRules()), new ArrayList());
    }
    if (args.length == 2) {
      return (List)StringUtil.copyPartialMatches(args[1], GAMERULE_STATES, new ArrayList(GAMERULE_STATES.size()));
    }
    return ImmutableList.of();
  }
}