package net.minecraft.server.v1_8_R3;

public class RecipesDyes
{
  public void a(CraftingManager ☃)
  {
    for (int ☃ = 0; ☃ < 16; ☃++)
    {
      ☃.registerShapelessRecipe(new ItemStack(Blocks.WOOL, 1, ☃), new Object[] { new ItemStack(Items.DYE, 1, 15 - ☃), new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 0) });
      
      ☃.registerShapedRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 8, 15 - ☃), new Object[] { "###", "#X#", "###", Character.valueOf('#'), new ItemStack(Blocks.HARDENED_CLAY), Character.valueOf('X'), new ItemStack(Items.DYE, 1, ☃) });
      
      ☃.registerShapedRecipe(new ItemStack(Blocks.STAINED_GLASS, 8, 15 - ☃), new Object[] { "###", "#X#", "###", Character.valueOf('#'), new ItemStack(Blocks.GLASS), Character.valueOf('X'), new ItemStack(Items.DYE, 1, ☃) });
      
      ☃.registerShapedRecipe(new ItemStack(Blocks.STAINED_GLASS_PANE, 16, ☃), new Object[] { "###", "###", Character.valueOf('#'), new ItemStack(Blocks.STAINED_GLASS, 1, ☃) });
    }
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.YELLOW.getInvColorIndex()), new Object[] { new ItemStack(Blocks.YELLOW_FLOWER, 1, BlockFlowers.EnumFlowerVarient.DANDELION.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.POPPY.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 3, EnumColor.WHITE.getInvColorIndex()), new Object[] { Items.BONE });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.PINK.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.ORANGE.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.YELLOW.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.LIME.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.GREEN.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.GRAY.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.BLACK.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.SILVER.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.GRAY.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 3, EnumColor.SILVER.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.BLACK.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.LIGHT_BLUE.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.CYAN.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.GREEN.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.PURPLE.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.MAGENTA.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.PURPLE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.PINK.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 3, EnumColor.MAGENTA.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.PINK.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 4, EnumColor.MAGENTA.getInvColorIndex()), new Object[] { new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.LIGHT_BLUE.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.BLUE_ORCHID.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.MAGENTA.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.ALLIUM.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.SILVER.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.HOUSTONIA.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.RED_TULIP.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.ORANGE.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.ORANGE_TULIP.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.SILVER.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.WHITE_TULIP.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.PINK.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.PINK_TULIP.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.SILVER.getInvColorIndex()), new Object[] { new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.OXEYE_DAISY.b()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.YELLOW.getInvColorIndex()), new Object[] { new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.SUNFLOWER.a()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.MAGENTA.getInvColorIndex()), new Object[] { new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.SYRINGA.a()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.RED.getInvColorIndex()), new Object[] { new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.ROSE.a()) });
    
    ☃.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.PINK.getInvColorIndex()), new Object[] { new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.PAEONIA.a()) });
    for (int ☃ = 0; ☃ < 16; ☃++) {
      ☃.registerShapedRecipe(new ItemStack(Blocks.CARPET, 3, ☃), new Object[] { "##", Character.valueOf('#'), new ItemStack(Blocks.WOOL, 1, ☃) });
    }
  }
}
