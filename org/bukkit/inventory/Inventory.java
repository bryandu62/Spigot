package org.bukkit.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;

public abstract interface Inventory
  extends Iterable<ItemStack>
{
  public abstract int getSize();
  
  public abstract int getMaxStackSize();
  
  public abstract void setMaxStackSize(int paramInt);
  
  public abstract String getName();
  
  public abstract ItemStack getItem(int paramInt);
  
  public abstract void setItem(int paramInt, ItemStack paramItemStack);
  
  public abstract HashMap<Integer, ItemStack> addItem(ItemStack... paramVarArgs)
    throws IllegalArgumentException;
  
  public abstract HashMap<Integer, ItemStack> removeItem(ItemStack... paramVarArgs)
    throws IllegalArgumentException;
  
  public abstract ItemStack[] getContents();
  
  public abstract void setContents(ItemStack[] paramArrayOfItemStack)
    throws IllegalArgumentException;
  
  @Deprecated
  public abstract boolean contains(int paramInt);
  
  public abstract boolean contains(Material paramMaterial)
    throws IllegalArgumentException;
  
  public abstract boolean contains(ItemStack paramItemStack);
  
  @Deprecated
  public abstract boolean contains(int paramInt1, int paramInt2);
  
  public abstract boolean contains(Material paramMaterial, int paramInt)
    throws IllegalArgumentException;
  
  public abstract boolean contains(ItemStack paramItemStack, int paramInt);
  
  public abstract boolean containsAtLeast(ItemStack paramItemStack, int paramInt);
  
  @Deprecated
  public abstract HashMap<Integer, ? extends ItemStack> all(int paramInt);
  
  public abstract HashMap<Integer, ? extends ItemStack> all(Material paramMaterial)
    throws IllegalArgumentException;
  
  public abstract HashMap<Integer, ? extends ItemStack> all(ItemStack paramItemStack);
  
  @Deprecated
  public abstract int first(int paramInt);
  
  public abstract int first(Material paramMaterial)
    throws IllegalArgumentException;
  
  public abstract int first(ItemStack paramItemStack);
  
  public abstract int firstEmpty();
  
  @Deprecated
  public abstract void remove(int paramInt);
  
  public abstract void remove(Material paramMaterial)
    throws IllegalArgumentException;
  
  public abstract void remove(ItemStack paramItemStack);
  
  public abstract void clear(int paramInt);
  
  public abstract void clear();
  
  public abstract List<HumanEntity> getViewers();
  
  public abstract String getTitle();
  
  public abstract InventoryType getType();
  
  public abstract InventoryHolder getHolder();
  
  public abstract ListIterator<ItemStack> iterator();
  
  public abstract ListIterator<ItemStack> iterator(int paramInt);
}
