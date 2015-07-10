package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.RecipesFurnace;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftFurnaceRecipe
  extends FurnaceRecipe
  implements CraftRecipe
{
  public CraftFurnaceRecipe(ItemStack result, ItemStack source)
  {
    super(result, source.getType(), source.getDurability());
  }
  
  public static CraftFurnaceRecipe fromBukkitRecipe(FurnaceRecipe recipe)
  {
    if ((recipe instanceof CraftFurnaceRecipe)) {
      return (CraftFurnaceRecipe)recipe;
    }
    return new CraftFurnaceRecipe(recipe.getResult(), recipe.getInput());
  }
  
  public void addToCraftingManager()
  {
    ItemStack result = getResult();
    ItemStack input = getInput();
    RecipesFurnace.getInstance().registerRecipe(CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result));
  }
}
