package org.bukkit;

import java.util.List;
import org.bukkit.inventory.ItemStack;

@Deprecated
public abstract interface UnsafeValues
{
  public abstract Material getMaterialFromInternalName(String paramString);
  
  public abstract List<String> tabCompleteInternalMaterialName(String paramString, List<String> paramList);
  
  public abstract ItemStack modifyItemStack(ItemStack paramItemStack, String paramString);
  
  public abstract Statistic getStatisticFromInternalName(String paramString);
  
  public abstract Achievement getAchievementFromInternalName(String paramString);
  
  public abstract List<String> tabCompleteInternalStatisticOrAchievementName(String paramString, List<String> paramList);
}
