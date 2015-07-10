package org.bukkit.craftbukkit.v1_8_R3.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.RegistryMaterials;
import net.minecraft.server.v1_8_R3.StatisticList;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.craftbukkit.v1_8_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.util.StringUtil;

public final class CraftMagicNumbers
  implements UnsafeValues
{
  public static final UnsafeValues INSTANCE = new CraftMagicNumbers();
  
  public static net.minecraft.server.v1_8_R3.Block getBlock(org.bukkit.block.Block block)
  {
    return getBlock(block.getType());
  }
  
  @Deprecated
  public static net.minecraft.server.v1_8_R3.Block getBlock(int id)
  {
    return getBlock(Material.getMaterial(id));
  }
  
  @Deprecated
  public static int getId(net.minecraft.server.v1_8_R3.Block block)
  {
    return net.minecraft.server.v1_8_R3.Block.getId(block);
  }
  
  public static Material getMaterial(net.minecraft.server.v1_8_R3.Block block)
  {
    return Material.getMaterial(net.minecraft.server.v1_8_R3.Block.getId(block));
  }
  
  public static Item getItem(Material material)
  {
    Item item = Item.getById(material.getId());
    return item;
  }
  
  @Deprecated
  public static Item getItem(int id)
  {
    return Item.getById(id);
  }
  
  @Deprecated
  public static int getId(Item item)
  {
    return Item.getId(item);
  }
  
  public static Material getMaterial(Item item)
  {
    Material material = Material.getMaterial(Item.getId(item));
    if (material == null) {
      return Material.AIR;
    }
    return material;
  }
  
  public static net.minecraft.server.v1_8_R3.Block getBlock(Material material)
  {
    net.minecraft.server.v1_8_R3.Block block = net.minecraft.server.v1_8_R3.Block.getById(material.getId());
    if (block == null) {
      return Blocks.AIR;
    }
    return block;
  }
  
  public Material getMaterialFromInternalName(String name)
  {
    return getMaterial((Item)Item.REGISTRY.get(new MinecraftKey(name)));
  }
  
  public List<String> tabCompleteInternalMaterialName(String token, List<String> completions)
  {
    ArrayList<String> results = Lists.newArrayList();
    for (MinecraftKey key : Item.REGISTRY.keySet()) {
      results.add(key.toString());
    }
    return (List)StringUtil.copyPartialMatches(token, results, completions);
  }
  
  public org.bukkit.inventory.ItemStack modifyItemStack(org.bukkit.inventory.ItemStack stack, String arguments)
  {
    net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
    try
    {
      nmsStack.setTag(MojangsonParser.parse(arguments));
    }
    catch (MojangsonParseException ex)
    {
      Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, null, ex);
    }
    stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
    
    return stack;
  }
  
  public org.bukkit.Statistic getStatisticFromInternalName(String name)
  {
    return CraftStatistic.getBukkitStatisticByName(name);
  }
  
  public Achievement getAchievementFromInternalName(String name)
  {
    return CraftStatistic.getBukkitAchievementByName(name);
  }
  
  public List<String> tabCompleteInternalStatisticOrAchievementName(String token, List<String> completions)
  {
    List<String> matches = new ArrayList();
    Iterator iterator = StatisticList.stats.iterator();
    while (iterator.hasNext())
    {
      String statistic = ((net.minecraft.server.v1_8_R3.Statistic)iterator.next()).name;
      if (statistic.startsWith(token)) {
        matches.add(statistic);
      }
    }
    return matches;
  }
}
