package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.InventorySubcontainer;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryEnchanting
  extends CraftInventory
  implements EnchantingInventory
{
  public CraftInventoryEnchanting(InventorySubcontainer inventory)
  {
    super(inventory);
  }
  
  public void setItem(ItemStack item)
  {
    setItem(0, item);
  }
  
  public ItemStack getItem()
  {
    return getItem(0);
  }
  
  public InventorySubcontainer getInventory()
  {
    return (InventorySubcontainer)this.inventory;
  }
  
  public void setSecondary(ItemStack item)
  {
    setItem(1, item);
  }
  
  public ItemStack getSecondary()
  {
    return getItem(1);
  }
}
