package org.bukkit.inventory.meta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public abstract interface ItemMeta
  extends Cloneable, ConfigurationSerializable
{
  public abstract boolean hasDisplayName();
  
  public abstract String getDisplayName();
  
  public abstract void setDisplayName(String paramString);
  
  public abstract boolean hasLore();
  
  public abstract List<String> getLore();
  
  public abstract void setLore(List<String> paramList);
  
  public abstract boolean hasEnchants();
  
  public abstract boolean hasEnchant(Enchantment paramEnchantment);
  
  public abstract int getEnchantLevel(Enchantment paramEnchantment);
  
  public abstract Map<Enchantment, Integer> getEnchants();
  
  public abstract boolean addEnchant(Enchantment paramEnchantment, int paramInt, boolean paramBoolean);
  
  public abstract boolean removeEnchant(Enchantment paramEnchantment);
  
  public abstract boolean hasConflictingEnchant(Enchantment paramEnchantment);
  
  public abstract void addItemFlags(ItemFlag... paramVarArgs);
  
  public abstract void removeItemFlags(ItemFlag... paramVarArgs);
  
  public abstract Set<ItemFlag> getItemFlags();
  
  public abstract boolean hasItemFlag(ItemFlag paramItemFlag);
  
  public abstract ItemMeta clone();
  
  public abstract Spigot spigot();
  
  public static class Spigot
  {
    public void setUnbreakable(boolean unbreakable)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean isUnbreakable()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
