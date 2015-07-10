package org.bukkit.block;

import org.bukkit.inventory.InventoryHolder;

public abstract interface Dropper
  extends BlockState, InventoryHolder
{
  public abstract void drop();
}
