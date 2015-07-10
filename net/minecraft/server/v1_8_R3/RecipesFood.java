package net.minecraft.server.v1_8_R3;

public class RecipesFood
{
  public void a(CraftingManager ☃)
  {
    ☃.registerShapelessRecipe(new ItemStack(Items.MUSHROOM_STEW), new Object[] { Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Items.BOWL });
    
    ☃.registerShapedRecipe(new ItemStack(Items.COOKIE, 8), new Object[] { "#X#", Character.valueOf('X'), new ItemStack(Items.DYE, 1, EnumColor.BROWN.getInvColorIndex()), Character.valueOf('#'), Items.WHEAT });
    
    ☃.registerShapedRecipe(new ItemStack(Items.RABBIT_STEW), new Object[] { " R ", "CPM", " B ", Character.valueOf('R'), new ItemStack(Items.COOKED_RABBIT), Character.valueOf('C'), Items.CARROT, Character.valueOf('P'), Items.BAKED_POTATO, Character.valueOf('M'), Blocks.BROWN_MUSHROOM, Character.valueOf('B'), Items.BOWL });
    
    ☃.registerShapedRecipe(new ItemStack(Items.RABBIT_STEW), new Object[] { " R ", "CPD", " B ", Character.valueOf('R'), new ItemStack(Items.COOKED_RABBIT), Character.valueOf('C'), Items.CARROT, Character.valueOf('P'), Items.BAKED_POTATO, Character.valueOf('D'), Blocks.RED_MUSHROOM, Character.valueOf('B'), Items.BOWL });
    
    ☃.registerShapedRecipe(new ItemStack(Blocks.MELON_BLOCK), new Object[] { "MMM", "MMM", "MMM", Character.valueOf('M'), Items.MELON });
    
    ☃.registerShapedRecipe(new ItemStack(Items.MELON_SEEDS), new Object[] { "M", Character.valueOf('M'), Items.MELON });
    
    ☃.registerShapedRecipe(new ItemStack(Items.PUMPKIN_SEEDS, 4), new Object[] { "M", Character.valueOf('M'), Blocks.PUMPKIN });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.PUMPKIN_PIE), new Object[] { Blocks.PUMPKIN, Items.SUGAR, Items.EGG });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.FERMENTED_SPIDER_EYE), new Object[] { Items.SPIDER_EYE, Blocks.BROWN_MUSHROOM, Items.SUGAR });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.BLAZE_POWDER, 2), new Object[] { Items.BLAZE_ROD });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.MAGMA_CREAM), new Object[] { Items.BLAZE_POWDER, Items.SLIME });
  }
}
