package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R3.CraftingManager;
import net.minecraft.server.v1_8_R3.IRecipe;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.RecipesFurnace;
import org.bukkit.inventory.Recipe;

public class RecipeIterator
  implements Iterator<Recipe>
{
  private final Iterator<IRecipe> recipes;
  private final Iterator<ItemStack> smeltingCustom;
  private final Iterator<ItemStack> smeltingVanilla;
  private Iterator<?> removeFrom = null;
  
  public RecipeIterator()
  {
    this.recipes = CraftingManager.getInstance().getRecipes().iterator();
    this.smeltingCustom = RecipesFurnace.getInstance().customRecipes.keySet().iterator();
    this.smeltingVanilla = RecipesFurnace.getInstance().recipes.keySet().iterator();
  }
  
  public boolean hasNext()
  {
    return (this.recipes.hasNext()) || (this.smeltingCustom.hasNext()) || (this.smeltingVanilla.hasNext());
  }
  
  public Recipe next()
  {
    if (this.recipes.hasNext())
    {
      this.removeFrom = this.recipes;
      return ((IRecipe)this.recipes.next()).toBukkitRecipe();
    }
    ItemStack item;
    ItemStack item;
    if (this.smeltingCustom.hasNext())
    {
      this.removeFrom = this.smeltingCustom;
      item = (ItemStack)this.smeltingCustom.next();
    }
    else
    {
      this.removeFrom = this.smeltingVanilla;
      item = (ItemStack)this.smeltingVanilla.next();
    }
    CraftItemStack stack = CraftItemStack.asCraftMirror(RecipesFurnace.getInstance().getResult(item));
    
    return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
  }
  
  public void remove()
  {
    if (this.removeFrom == null) {
      throw new IllegalStateException();
    }
    this.removeFrom.remove();
  }
}
