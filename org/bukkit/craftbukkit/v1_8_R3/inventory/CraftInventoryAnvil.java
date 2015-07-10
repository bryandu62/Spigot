package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.IInventory;
import org.bukkit.inventory.AnvilInventory;

public class CraftInventoryAnvil
  extends CraftInventory
  implements AnvilInventory
{
  private final IInventory resultInventory;
  
  public CraftInventoryAnvil(IInventory inventory, IInventory resultInventory)
  {
    super(inventory);
    this.resultInventory = resultInventory;
  }
  
  public IInventory getResultInventory()
  {
    return this.resultInventory;
  }
  
  public IInventory getIngredientsInventory()
  {
    return this.inventory;
  }
  
  public org.bukkit.inventory.ItemStack getItem(int slot)
  {
    if (slot < getIngredientsInventory().getSize())
    {
      net.minecraft.server.v1_8_R3.ItemStack item = getIngredientsInventory().getItem(slot);
      return item == null ? null : CraftItemStack.asCraftMirror(item);
    }
    net.minecraft.server.v1_8_R3.ItemStack item = getResultInventory().getItem(slot - getIngredientsInventory().getSize());
    return item == null ? null : CraftItemStack.asCraftMirror(item);
  }
  
  public void setItem(int index, org.bukkit.inventory.ItemStack item)
  {
    if (index < getIngredientsInventory().getSize()) {
      getIngredientsInventory().setItem(index, item == null ? null : CraftItemStack.asNMSCopy(item));
    } else {
      getResultInventory().setItem(index - getIngredientsInventory().getSize(), item == null ? null : CraftItemStack.asNMSCopy(item));
    }
  }
  
  public int getSize()
  {
    return getResultInventory().getSize() + getIngredientsInventory().getSize();
  }
}
