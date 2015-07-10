package org.bukkit.craftbukkit.v1_8_R3.inventory;

import org.bukkit.inventory.Recipe;

public abstract interface CraftRecipe
  extends Recipe
{
  public abstract void addToCraftingManager();
}
