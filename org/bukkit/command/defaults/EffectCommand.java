package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

@Deprecated
public class EffectCommand
  extends VanillaCommand
{
  private static final List<String> effects;
  
  public EffectCommand()
  {
    super("effect");
    this.description = "Adds/Removes effects on players";
    this.usageMessage = "/effect <player> <effect|clear> [seconds] [amplifier]";
    setPermission("bukkit.command.effect");
  }
  
  static
  {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    PotionEffectType[] arrayOfPotionEffectType;
    int i = (arrayOfPotionEffectType = PotionEffectType.values()).length;
    for (int j = 0; j < i; j++)
    {
      PotionEffectType type = arrayOfPotionEffectType[j];
      if (type != null) {
        builder.add(type.getName());
      }
    }
    effects = builder.build();
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length < 2)
    {
      sender.sendMessage(getUsage());
      return true;
    }
    Player player = sender.getServer().getPlayer(args[0]);
    if (player == null)
    {
      sender.sendMessage(ChatColor.RED + String.format("Player, %s, not found", new Object[] { args[0] }));
      return true;
    }
    if ("clear".equalsIgnoreCase(args[1]))
    {
      for (PotionEffect effect : player.getActivePotionEffects()) {
        player.removePotionEffect(effect.getType());
      }
      sender.sendMessage(String.format("Took all effects from %s", new Object[] { args[0] }));
      return true;
    }
    PotionEffectType effect = PotionEffectType.getByName(args[1]);
    if (effect == null) {
      effect = PotionEffectType.getById(getInteger(sender, args[1], 0));
    }
    if (effect == null)
    {
      sender.sendMessage(ChatColor.RED + String.format("Effect, %s, not found", new Object[] { args[1] }));
      return true;
    }
    int duration = 600;
    int duration_temp = 30;
    int amplification = 0;
    if (args.length >= 3)
    {
      duration_temp = getInteger(sender, args[2], 0, 1000000);
      if (effect.isInstant()) {
        duration = duration_temp;
      } else {
        duration = duration_temp * 20;
      }
    }
    else if (effect.isInstant())
    {
      duration = 1;
    }
    if (args.length >= 4) {
      amplification = getInteger(sender, args[3], 0, 255);
    }
    if (duration_temp == 0)
    {
      if (!player.hasPotionEffect(effect))
      {
        sender.sendMessage(String.format("Couldn't take %s from %s as they do not have the effect", new Object[] { effect.getName(), args[0] }));
        return true;
      }
      player.removePotionEffect(effect);
      broadcastCommandMessage(sender, String.format("Took %s from %s", new Object[] { effect.getName(), args[0] }));
    }
    else
    {
      PotionEffect applyEffect = new PotionEffect(effect, duration, amplification);
      
      player.addPotionEffect(applyEffect, true);
      broadcastCommandMessage(sender, String.format("Given %s (ID %d) * %d to %s for %d seconds", new Object[] { effect.getName(), Integer.valueOf(effect.getId()), Integer.valueOf(amplification), args[0], Integer.valueOf(duration_temp) }));
    }
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, String commandLabel, String[] args)
  {
    if (args.length == 1) {
      return super.tabComplete(sender, commandLabel, args);
    }
    if (args.length == 2) {
      return (List)StringUtil.copyPartialMatches(args[1], effects, new ArrayList(effects.size()));
    }
    return ImmutableList.of();
  }
}
