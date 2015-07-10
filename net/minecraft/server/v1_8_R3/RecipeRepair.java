package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;

public class RecipeRepair
  extends ShapelessRecipes
  implements IRecipe
{
  public RecipeRepair()
  {
    super(new ItemStack(Items.LEATHER_HELMET), Arrays.asList(new ItemStack[] { new ItemStack(Items.LEATHER_HELMET) }));
  }
  
  public boolean a(InventoryCrafting inventorycrafting, World world)
  {
    ArrayList arraylist = Lists.newArrayList();
    for (int i = 0; i < inventorycrafting.getSize(); i++)
    {
      ItemStack itemstack = inventorycrafting.getItem(i);
      if (itemstack != null)
      {
        arraylist.add(itemstack);
        if (arraylist.size() > 1)
        {
          ItemStack itemstack1 = (ItemStack)arraylist.get(0);
          if ((itemstack.getItem() != itemstack1.getItem()) || (itemstack1.count != 1) || (itemstack.count != 1) || (!itemstack1.getItem().usesDurability())) {
            return false;
          }
        }
      }
    }
    return arraylist.size() == 2;
  }
  
  public ItemStack a(InventoryCrafting inventorycrafting)
  {
    ArrayList arraylist = Lists.newArrayList();
    for (int i = 0; i < inventorycrafting.getSize(); i++)
    {
      ItemStack itemstack = inventorycrafting.getItem(i);
      if (itemstack != null)
      {
        arraylist.add(itemstack);
        if (arraylist.size() > 1)
        {
          ItemStack itemstack1 = (ItemStack)arraylist.get(0);
          if ((itemstack.getItem() != itemstack1.getItem()) || (itemstack1.count != 1) || (itemstack.count != 1) || (!itemstack1.getItem().usesDurability())) {
            return null;
          }
        }
      }
    }
    if (arraylist.size() == 2)
    {
      ItemStack itemstack2 = (ItemStack)arraylist.get(0);
      
      ItemStack itemstack = (ItemStack)arraylist.get(1);
      if ((itemstack2.getItem() == itemstack.getItem()) && (itemstack2.count == 1) && (itemstack.count == 1) && (itemstack2.getItem().usesDurability()))
      {
        Item item = itemstack2.getItem();
        int j = item.getMaxDurability() - itemstack2.h();
        int k = item.getMaxDurability() - itemstack.h();
        int l = j + k + item.getMaxDurability() * 5 / 100;
        int i1 = item.getMaxDurability() - l;
        if (i1 < 0) {
          i1 = 0;
        }
        ItemStack result = new ItemStack(itemstack.getItem(), 1, i1);
        List<ItemStack> ingredients = new ArrayList();
        ingredients.add(itemstack2.cloneItemStack());
        ingredients.add(itemstack.cloneItemStack());
        ShapelessRecipes recipe = new ShapelessRecipes(result.cloneItemStack(), ingredients);
        inventorycrafting.currentRecipe = recipe;
        result = CraftEventFactory.callPreCraftEvent(inventorycrafting, result, CraftingManager.getInstance().lastCraftView, true);
        return result;
      }
    }
    return null;
  }
  
  public int a()
  {
    return 4;
  }
  
  public ItemStack b()
  {
    return null;
  }
  
  public ItemStack[] b(InventoryCrafting inventorycrafting)
  {
    ItemStack[] aitemstack = new ItemStack[inventorycrafting.getSize()];
    for (int i = 0; i < aitemstack.length; i++)
    {
      ItemStack itemstack = inventorycrafting.getItem(i);
      if ((itemstack != null) && (itemstack.getItem().r())) {
        aitemstack[i] = new ItemStack(itemstack.getItem().q());
      }
    }
    return aitemstack;
  }
}
