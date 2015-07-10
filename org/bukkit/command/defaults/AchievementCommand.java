package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.UnsafeValues;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.plugin.PluginManager;

@Deprecated
public class AchievementCommand
  extends VanillaCommand
{
  public AchievementCommand()
  {
    super("achievement");
    this.description = "Gives the specified player an achievement or changes a statistic value. Use '*' to give all achievements.";
    this.usageMessage = "/achievement give <stat_name> [player]";
    setPermission("bukkit.command.achievement");
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
    if (!args[0].equalsIgnoreCase("give"))
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    String statisticString = args[1];
    Player player = null;
    if (args.length > 2) {
      player = Bukkit.getPlayer(args[1]);
    } else if ((sender instanceof Player)) {
      player = (Player)sender;
    }
    if (player == null)
    {
      sender.sendMessage("You must specify which player you wish to perform this action on.");
      return true;
    }
    if (statisticString.equals("*"))
    {
      Achievement[] arrayOfAchievement;
      int i = (arrayOfAchievement = Achievement.values()).length;
      for (int j = 0; j < i; j++)
      {
        Achievement achievement = arrayOfAchievement[j];
        if (!player.hasAchievement(achievement))
        {
          PlayerAchievementAwardedEvent event = new PlayerAchievementAwardedEvent(player, achievement);
          Bukkit.getServer().getPluginManager().callEvent(event);
          if (!event.isCancelled()) {
            player.awardAchievement(achievement);
          }
        }
      }
      Command.broadcastCommandMessage(sender, String.format("Successfully given all achievements to %s", new Object[] { player.getName() }));
      return true;
    }
    Achievement achievement = Bukkit.getUnsafe().getAchievementFromInternalName(statisticString);
    Statistic statistic = Bukkit.getUnsafe().getStatisticFromInternalName(statisticString);
    if (achievement != null)
    {
      if (player.hasAchievement(achievement))
      {
        sender.sendMessage(String.format("%s already has achievement %s", new Object[] { player.getName(), statisticString }));
        return true;
      }
      PlayerAchievementAwardedEvent event = new PlayerAchievementAwardedEvent(player, achievement);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled())
      {
        sender.sendMessage(String.format("Unable to award %s the achievement %s", new Object[] { player.getName(), statisticString }));
        return true;
      }
      player.awardAchievement(achievement);
      
      Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", new Object[] { player.getName(), statisticString }));
      return true;
    }
    if (statistic == null)
    {
      sender.sendMessage(String.format("Unknown achievement or statistic '%s'", new Object[] { statisticString }));
      return true;
    }
    if (statistic.getType() == Statistic.Type.UNTYPED)
    {
      PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled())
      {
        sender.sendMessage(String.format("Unable to increment %s for %s", new Object[] { statisticString, player.getName() }));
        return true;
      }
      player.incrementStatistic(statistic);
      Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", new Object[] { player.getName(), statisticString }));
      return true;
    }
    if (statistic.getType() == Statistic.Type.ENTITY)
    {
      EntityType entityType = EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
      if (entityType == null)
      {
        sender.sendMessage(String.format("Unknown achievement or statistic '%s'", new Object[] { statisticString }));
        return true;
      }
      PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1, entityType);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled())
      {
        sender.sendMessage(String.format("Unable to increment %s for %s", new Object[] { statisticString, player.getName() }));
        return true;
      }
      try
      {
        player.incrementStatistic(statistic, entityType);
      }
      catch (IllegalArgumentException localIllegalArgumentException1)
      {
        sender.sendMessage(String.format("Unknown achievement or statistic '%s'", new Object[] { statisticString }));
        return true;
      }
    }
    else
    {
      try
      {
        id = getInteger(sender, statisticString.substring(statisticString.lastIndexOf(".") + 1), 0, Integer.MAX_VALUE, true);
      }
      catch (NumberFormatException e)
      {
        int id;
        sender.sendMessage(e.getMessage());
        return true;
      }
      int id;
      Material material = Material.getMaterial(id);
      if (material == null)
      {
        sender.sendMessage(String.format("Unknown achievement or statistic '%s'", new Object[] { statisticString }));
        return true;
      }
      PlayerStatisticIncrementEvent event = new PlayerStatisticIncrementEvent(player, statistic, player.getStatistic(statistic), player.getStatistic(statistic) + 1, material);
      Bukkit.getServer().getPluginManager().callEvent(event);
      if (event.isCancelled())
      {
        sender.sendMessage(String.format("Unable to increment %s for %s", new Object[] { statisticString, player.getName() }));
        return true;
      }
      try
      {
        player.incrementStatistic(statistic, material);
      }
      catch (IllegalArgumentException localIllegalArgumentException2)
      {
        sender.sendMessage(String.format("Unknown achievement or statistic '%s'", new Object[] { statisticString }));
        return true;
      }
    }
    Command.broadcastCommandMessage(sender, String.format("Successfully given %s the stat %s", new Object[] { player.getName(), statisticString }));
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String alias, String[] args)
    throws IllegalArgumentException
  {
    Validate.notNull(sender, "Sender cannot be null");
    Validate.notNull(args, "Arguments cannot be null");
    Validate.notNull(alias, "Alias cannot be null");
    if (args.length == 1) {
      return Arrays.asList(new String[] { "give" });
    }
    if (args.length == 2) {
      return Bukkit.getUnsafe().tabCompleteInternalStatisticOrAchievementName(args[1], new ArrayList());
    }
    if (args.length == 3) {
      return super.tabComplete(sender, alias, args);
    }
    return ImmutableList.of();
  }
}
