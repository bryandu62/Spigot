package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.IRecipe;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.Java15Compat;

public class CraftInventoryCrafting
  extends CraftInventory
  implements CraftingInventory
{
  private final IInventory resultInventory;
  
  public CraftInventoryCrafting(InventoryCrafting inventory, IInventory resultInventory)
  {
    super(inventory);
    this.resultInventory = resultInventory;
  }
  
  public IInventory getResultInventory()
  {
    return this.resultInventory;
  }
  
  public IInventory getMatrixInventory()
  {
    return this.inventory;
  }
  
  public int getSize()
  {
    return getResultInventory().getSize() + getMatrixInventory().getSize();
  }
  
  public void setContents(org.bukkit.inventory.ItemStack[] items)
  {
    int resultLen = getResultInventory().getContents().length;
    int len = getMatrixInventory().getContents().length + resultLen;
    if (len > items.length) {
      throw new IllegalArgumentException("Invalid inventory size; expected " + len + " or less");
    }
    setContents(items[0], (org.bukkit.inventory.ItemStack[])Java15Compat.Arrays_copyOfRange(items, 1, items.length));
  }
  
  public org.bukkit.inventory.ItemStack[] getContents()
  {
    org.bukkit.inventory.ItemStack[] items = new org.bukkit.inventory.ItemStack[getSize()];
    net.minecraft.server.v1_8_R3.ItemStack[] mcResultItems = getResultInventory().getContents();
    
    int i = 0;
    for (i = 0; i < mcResultItems.length; i++) {
      items[i] = CraftItemStack.asCraftMirror(mcResultItems[i]);
    }
    net.minecraft.server.v1_8_R3.ItemStack[] mcItems = getMatrixInventory().getContents();
    for (int j = 0; j < mcItems.length; j++) {
      items[(i + j)] = CraftItemStack.asCraftMirror(mcItems[j]);
    }
    return items;
  }
  
  public void setContents(org.bukkit.inventory.ItemStack result, org.bukkit.inventory.ItemStack[] contents)
  {
    setResult(result);
    setMatrix(contents);
  }
  
  public CraftItemStack getItem(int index)
  {
    if (index < getResultInventory().getSize())
    {
      net.minecraft.server.v1_8_R3.ItemStack item = getResultInventory().getItem(index);
      return item == null ? null : CraftItemStack.asCraftMirror(item);
    }
    net.minecraft.server.v1_8_R3.ItemStack item = getMatrixInventory().getItem(index - getResultInventory().getSize());
    return item == null ? null : CraftItemStack.asCraftMirror(item);
  }
  
  public void setItem(int index, org.bukkit.inventory.ItemStack item)
  {
    if (index < getResultInventory().getSize()) {
      getResultInventory().setItem(index, item == null ? null : CraftItemStack.asNMSCopy(item));
    } else {
      getMatrixInventory().setItem(index - getResultInventory().getSize(), item == null ? null : CraftItemStack.asNMSCopy(item));
    }
  }
  
  public org.bukkit.inventory.ItemStack[] getMatrix()
  {
    org.bukkit.inventory.ItemStack[] items = new org.bukkit.inventory.ItemStack[getSize()];
    net.minecraft.server.v1_8_R3.ItemStack[] matrix = getMatrixInventory().getContents();
    for (int i = 0; i < matrix.length; i++) {
      items[i] = CraftItemStack.asCraftMirror(matrix[i]);
    }
    return items;
  }
  
  public org.bukkit.inventory.ItemStack getResult()
  {
    net.minecraft.server.v1_8_R3.ItemStack item = getResultInventory().getItem(0);
    if (item != null) {
      return CraftItemStack.asCraftMirror(item);
    }
    return null;
  }
  
  public void setMatrix(org.bukkit.inventory.ItemStack[] contents)
  {
    if (getMatrixInventory().getContents().length > contents.length) {
      throw new IllegalArgumentException("Invalid inventory size; expected " + getMatrixInventory().getContents().length + " or less");
    }
    net.minecraft.server.v1_8_R3.ItemStack[] mcItems = getMatrixInventory().getContents();
    for (int i = 0; i < mcItems.length; i++) {
      if (i < contents.length)
      {
        org.bukkit.inventory.ItemStack item = contents[i];
        if ((item == null) || (item.getTypeId() <= 0)) {
          mcItems[i] = null;
        } else {
          mcItems[i] = CraftItemStack.asNMSCopy(item);
        }
      }
      else
      {
        mcItems[i] = null;
      }
    }
  }
  
  public void setResult(org.bukkit.inventory.ItemStack item)
  {
    net.minecraft.server.v1_8_R3.ItemStack[] contents = getResultInventory().getContents();
    if ((item == null) || (item.getTypeId() <= 0)) {
      contents[0] = null;
    } else {
      contents[0] = CraftItemStack.asNMSCopy(item);
    }
  }
  
  public Recipe getRecipe()
  {
    IRecipe recipe = ((InventoryCrafting)getInventory()).currentRecipe;
    return recipe == null ? null : recipe.toBukkitRecipe();
  }
}
