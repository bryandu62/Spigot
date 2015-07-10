package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.StringUtil;

@Deprecated
public class ClearCommand
  extends VanillaCommand
{
  private static List<String> materials;
  
  static
  {
    ArrayList<String> materialList = new ArrayList();
    Material[] arrayOfMaterial;
    int i = (arrayOfMaterial = Material.values()).length;
    for (int j = 0; j < i; j++)
    {
      Material material = arrayOfMaterial[j];
      materialList.add(material.name());
    }
    Collections.sort(materialList);
    materials = ImmutableList.copyOf(materialList);
  }
  
  public ClearCommand()
  {
    super("clear");
    this.description = "Clears the player's inventory. Can specify item and data filters too.";
    this.usageMessage = "/clear <player> [item] [data]";
    setPermission("bukkit.command.clear");
  }
  
  public boolean execute(CommandSender sender, String currentAlias, String[] args)
  {
    if (!testPermission(sender)) {
      return true;
    }
    Player player = null;
    if (args.length > 0) {
      player = Bukkit.getPlayer(args[0]);
    } else if ((sender instanceof Player)) {
      player = (Player)sender;
    }
    if (player != null)
    {
      int id;
      int id;
      if ((args.length > 1) && (!args[1].equals("-1")))
      {
        Material material = Material.matchMaterial(args[1]);
        if (material == null)
        {
          sender.sendMessage(ChatColor.RED + "There's no item called " + args[1]);
          return false;
        }
        id = material.getId();
      }
      else
      {
        id = -1;
      }
      int data = args.length >= 3 ? getInteger(sender, args[2], 0) : -1;
      int count = player.getInventory().clear(id, data);
      
      Command.broadcastCommandMessage(sender, "Cleared the inventory of " + player.getDisplayName() + ", removing " + count + " items");
    }
    else if (args.length == 0)
    {
      sender.sendMessage(ChatColor.RED + "Please provide a player!");
    }
    else
    {
      sender.sendMessage(ChatColor.RED + "Can't find player " + args[0]);
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
    if (args.length == 2)
    {
      String arg = args[1];
      List<String> materials = materials;
      List<String> completion = null;
      
      int size = materials.size();
      int i = Collections.binarySearch(materials, arg, String.CASE_INSENSITIVE_ORDER);
      if (i < 0) {}
      for (i = -1 - i; i < size; i++)
      {
        String material = (String)materials.get(i);
        if (!StringUtil.startsWithIgnoreCase(material, arg)) {
          break;
        }
        if (completion == null) {
          completion = new ArrayList();
        }
        completion.add(material);
      }
      if (completion != null) {
        return completion;
      }
    }
    return ImmutableList.of();
  }
}
