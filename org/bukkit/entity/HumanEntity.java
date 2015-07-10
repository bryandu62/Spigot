package org.bukkit.entity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;

public abstract interface HumanEntity
  extends LivingEntity, AnimalTamer, Permissible, InventoryHolder
{
  public abstract String getName();
  
  public abstract PlayerInventory getInventory();
  
  public abstract Inventory getEnderChest();
  
  public abstract boolean setWindowProperty(InventoryView.Property paramProperty, int paramInt);
  
  public abstract InventoryView getOpenInventory();
  
  public abstract InventoryView openInventory(Inventory paramInventory);
  
  public abstract InventoryView openWorkbench(Location paramLocation, boolean paramBoolean);
  
  public abstract InventoryView openEnchanting(Location paramLocation, boolean paramBoolean);
  
  public abstract void openInventory(InventoryView paramInventoryView);
  
  public abstract void closeInventory();
  
  public abstract ItemStack getItemInHand();
  
  public abstract void setItemInHand(ItemStack paramItemStack);
  
  public abstract ItemStack getItemOnCursor();
  
  public abstract void setItemOnCursor(ItemStack paramItemStack);
  
  public abstract boolean isSleeping();
  
  public abstract int getSleepTicks();
  
  public abstract GameMode getGameMode();
  
  public abstract void setGameMode(GameMode paramGameMode);
  
  public abstract boolean isBlocking();
  
  public abstract int getExpToLevel();
}
