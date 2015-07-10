package org.bukkit.event.inventory;

import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

public class CraftItemEvent
  extends InventoryClickEvent
{
  private Recipe recipe;
  
  @Deprecated
  public CraftItemEvent(Recipe recipe, InventoryView what, InventoryType.SlotType type, int slot, boolean right, boolean shift)
  {
    this(recipe, what, type, slot, shift ? ClickType.SHIFT_LEFT : right ? ClickType.RIGHT : shift ? ClickType.SHIFT_RIGHT : ClickType.LEFT, InventoryAction.PICKUP_ALL);
  }
  
  public CraftItemEvent(Recipe recipe, InventoryView what, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action)
  {
    super(what, type, slot, click, action);
    this.recipe = recipe;
  }
  
  public CraftItemEvent(Recipe recipe, InventoryView what, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, int key)
  {
    super(what, type, slot, click, action, key);
    this.recipe = recipe;
  }
  
  public Recipe getRecipe()
  {
    return this.recipe;
  }
  
  public CraftingInventory getInventory()
  {
    return (CraftingInventory)super.getInventory();
  }
}
