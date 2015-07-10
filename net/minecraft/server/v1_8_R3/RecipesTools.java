package net.minecraft.server.v1_8_R3;

public class RecipesTools
{
  private String[][] a = { { "XXX", " # ", " # " }, { "X", "#", "#" }, { "XX", "X#", " #" }, { "XX", " #", " #" } };
  private Object[][] b = { { Blocks.PLANKS, Blocks.COBBLESTONE, Items.IRON_INGOT, Items.DIAMOND, Items.GOLD_INGOT }, { Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE }, { Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL }, { Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.GOLDEN_AXE }, { Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.DIAMOND_HOE, Items.GOLDEN_HOE } };
  
  public void a(CraftingManager ☃)
  {
    for (int ☃ = 0; ☃ < this.b[0].length; ☃++)
    {
      Object ☃ = this.b[0][☃];
      for (int ☃ = 0; ☃ < this.b.length - 1; ☃++)
      {
        Item ☃ = (Item)this.b[(☃ + 1)][☃];
        ☃.registerShapedRecipe(new ItemStack(☃), new Object[] { this.a[☃], Character.valueOf('#'), Items.STICK, Character.valueOf('X'), ☃ });
      }
    }
    ☃.registerShapedRecipe(new ItemStack(Items.SHEARS), new Object[] { " #", "# ", Character.valueOf('#'), Items.IRON_INGOT });
  }
}
