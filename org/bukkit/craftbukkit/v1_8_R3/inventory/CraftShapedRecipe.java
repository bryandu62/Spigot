package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R3.CraftingManager;
import net.minecraft.server.v1_8_R3.ShapedRecipes;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ShapedRecipe;

public class CraftShapedRecipe
  extends ShapedRecipe
  implements CraftRecipe
{
  private ShapedRecipes recipe;
  
  public CraftShapedRecipe(org.bukkit.inventory.ItemStack result)
  {
    super(result);
  }
  
  public CraftShapedRecipe(org.bukkit.inventory.ItemStack result, ShapedRecipes recipe)
  {
    this(result);
    this.recipe = recipe;
  }
  
  public static CraftShapedRecipe fromBukkitRecipe(ShapedRecipe recipe)
  {
    if ((recipe instanceof CraftShapedRecipe)) {
      return (CraftShapedRecipe)recipe;
    }
    CraftShapedRecipe ret = new CraftShapedRecipe(recipe.getResult());
    String[] shape = recipe.getShape();
    ret.shape(shape);
    Map<Character, org.bukkit.inventory.ItemStack> ingredientMap = recipe.getIngredientMap();
    for (Iterator localIterator = ingredientMap.keySet().iterator(); localIterator.hasNext();)
    {
      char c = ((Character)localIterator.next()).charValue();
      org.bukkit.inventory.ItemStack stack = (org.bukkit.inventory.ItemStack)ingredientMap.get(Character.valueOf(c));
      if (stack != null) {
        ret.setIngredient(c, stack.getType(), stack.getDurability());
      }
    }
    return ret;
  }
  
  public void addToCraftingManager()
  {
    String[] shape = getShape();
    Map<Character, org.bukkit.inventory.ItemStack> ingred = getIngredientMap();
    int datalen = shape.length;
    datalen += ingred.size() * 2;
    int i = 0;
    Object[] data = new Object[datalen];
    for (; i < shape.length; i++) {
      data[i] = shape[i];
    }
    for (Iterator localIterator = ingred.keySet().iterator(); localIterator.hasNext();)
    {
      char c = ((Character)localIterator.next()).charValue();
      org.bukkit.inventory.ItemStack mdata = (org.bukkit.inventory.ItemStack)ingred.get(Character.valueOf(c));
      if (mdata != null)
      {
        data[i] = Character.valueOf(c);
        i++;
        int id = mdata.getTypeId();
        short dmg = mdata.getDurability();
        data[i] = new net.minecraft.server.v1_8_R3.ItemStack(CraftMagicNumbers.getItem(id), 1, dmg);
        i++;
      }
    }
    CraftingManager.getInstance().registerShapedRecipe(CraftItemStack.asNMSCopy(getResult()), data);
  }
}
