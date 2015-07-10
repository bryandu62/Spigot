package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

@Deprecated
public class EnchantCommand
  extends VanillaCommand
{
  private static final List<String> ENCHANTMENT_NAMES = new ArrayList();
  
  public EnchantCommand()
  {
    super("enchant");
    this.description = "Adds enchantments to the item the player is currently holding. Specify 0 for the level to remove an enchantment. Specify force to ignore normal enchantment restrictions";
    this.usageMessage = "/enchant <player> <enchantment> [level|max|0] [force]";
    setPermission("bukkit.command.enchant");
  }
  
  public boolean execute(CommandSender sender, String commandLabel, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    if (args.length < 2)
    {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    boolean force = false;
    if (args.length > 2) {
      force = args[2].equalsIgnoreCase("force");
    }
    Player player = Bukkit.getPlayerExact(args[0]);
    if (player == null)
    {
      sender.sendMessage("Can't find player " + args[0]);
    }
    else
    {
      ItemStack item = player.getItemInHand();
      if (item.getType() == Material.AIR)
      {
        sender.sendMessage("The player isn't holding an item");
      }
      else
      {
        String itemName = item.getType().toString().replaceAll("_", " ");
        itemName = WordUtils.capitalizeFully(itemName);
        
        Enchantment enchantment = getEnchantment(args[1].toUpperCase());
        if (enchantment == null)
        {
          sender.sendMessage(String.format("Enchantment does not exist: %s", new Object[] { args[1] }));
        }
        else
        {
          String enchantmentName = enchantment.getName().replaceAll("_", " ");
          enchantmentName = WordUtils.capitalizeFully(enchantmentName);
          if ((!force) && (!enchantment.canEnchantItem(item)))
          {
            sender.sendMessage(String.format("%s cannot be applied to %s", new Object[] { enchantmentName, itemName }));
          }
          else
          {
            int level = 1;
            if (args.length > 2)
            {
              Integer integer = getInteger(args[2]);
              int minLevel = enchantment.getStartLevel();
              int maxLevel = force ? 32767 : enchantment.getMaxLevel();
              if (integer != null)
              {
                if (integer.intValue() == 0)
                {
                  item.removeEnchantment(enchantment);
                  Command.broadcastCommandMessage(sender, String.format("Removed %s on %s's %s", new Object[] { enchantmentName, player.getName(), itemName }));
                  return true;
                }
                if ((integer.intValue() < minLevel) || (integer.intValue() > maxLevel))
                {
                  sender.sendMessage(String.format("Level for enchantment %s must be between %d and %d", new Object[] { enchantmentName, Integer.valueOf(minLevel), Integer.valueOf(maxLevel) }));
                  sender.sendMessage("Specify 0 for level to remove an enchantment");
                  return true;
                }
                level = integer.intValue();
              }
              if ("max".equals(args[2])) {
                level = maxLevel;
              }
            }
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            boolean conflicts = false;
            if ((!force) && (!enchantments.isEmpty())) {
              for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet())
              {
                Enchantment enchant = (Enchantment)entry.getKey();
                if ((!enchant.equals(enchantment)) && 
                  (enchant.conflictsWith(enchantment)))
                {
                  sender.sendMessage(String.format("Can't apply the enchantment %s on an item with the enchantment %s", new Object[] { enchantmentName, WordUtils.capitalizeFully(enchant.getName().replaceAll("_", " ")) }));
                  conflicts = true;
                  break;
                }
              }
            }
            if (!conflicts)
            {
              item.addUnsafeEnchantment(enchantment, level);
              
              Command.broadcastCommandMessage(sender, String.format("Applied %s (Lvl %d) on %s's %s", new Object[] { enchantmentName, Integer.valueOf(level), player.getName(), itemName }), false);
              sender.sendMessage(String.format("Enchanting succeeded, applied %s (Lvl %d) onto your %s", new Object[] { enchantmentName, Integer.valueOf(level), itemName }));
            }
          }
        }
      }
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
      return super.tabComplete(sender, alias, args);
    }
    if (args.length == 2) {
      return (List)StringUtil.copyPartialMatches(args[1], ENCHANTMENT_NAMES, new ArrayList(ENCHANTMENT_NAMES.size()));
    }
    if (((args.length == 3) || (args.length == 4)) && 
      (!args[(args.length - 2)].equalsIgnoreCase("force"))) {
      return ImmutableList.of("force");
    }
    return ImmutableList.of();
  }
  
  private Enchantment getEnchantment(String lookup)
  {
    Enchantment enchantment = Enchantment.getByName(lookup);
    if (enchantment == null)
    {
      Integer id = getInteger(lookup);
      if (id != null) {
        enchantment = Enchantment.getById(id.intValue());
      }
    }
    return enchantment;
  }
  
  public static void buildEnchantments()
  {
    if (!ENCHANTMENT_NAMES.isEmpty()) {
      throw new IllegalStateException("Enchantments have already been built!");
    }
    Enchantment[] arrayOfEnchantment;
    int i = (arrayOfEnchantment = Enchantment.values()).length;
    for (int j = 0; j < i; j++)
    {
      Enchantment enchantment = arrayOfEnchantment[j];
      ENCHANTMENT_NAMES.add(enchantment.getName());
    }
    Collections.sort(ENCHANTMENT_NAMES);
  }
}
