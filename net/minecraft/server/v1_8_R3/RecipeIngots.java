package net.minecraft.server.v1_8_R3;

public class RecipeIngots
{
  private Object[][] a = { { Blocks.GOLD_BLOCK, new ItemStack(Items.GOLD_INGOT, 9) }, { Blocks.IRON_BLOCK, new ItemStack(Items.IRON_INGOT, 9) }, { Blocks.DIAMOND_BLOCK, new ItemStack(Items.DIAMOND, 9) }, { Blocks.EMERALD_BLOCK, new ItemStack(Items.EMERALD, 9) }, { Blocks.LAPIS_BLOCK, new ItemStack(Items.DYE, 9, EnumColor.BLUE.getInvColorIndex()) }, { Blocks.REDSTONE_BLOCK, new ItemStack(Items.REDSTONE, 9) }, { Blocks.COAL_BLOCK, new ItemStack(Items.COAL, 9, 0) }, { Blocks.HAY_BLOCK, new ItemStack(Items.WHEAT, 9) }, { Blocks.SLIME, new ItemStack(Items.SLIME, 9) } };
  
  public void a(CraftingManager ☃)
  {
    for (int ☃ = 0; ☃ < this.a.length; ☃++)
    {
      Block ☃ = (Block)this.a[☃][0];
      ItemStack ☃ = (ItemStack)this.a[☃][1];
      ☃.registerShapedRecipe(new ItemStack(☃), new Object[] { "###", "###", "###", Character.valueOf('#'), ☃ });
      
      ☃.registerShapedRecipe(☃, new Object[] { "#", Character.valueOf('#'), ☃ });
    }
    ☃.registerShapedRecipe(new ItemStack(Items.GOLD_INGOT), new Object[] { "###", "###", "###", Character.valueOf('#'), Items.GOLD_NUGGET });
    
    ☃.registerShapedRecipe(new ItemStack(Items.GOLD_NUGGET, 9), new Object[] { "#", Character.valueOf('#'), Items.GOLD_INGOT });
  }
}
