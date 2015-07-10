package net.minecraft.server.v1_8_R3;

public class RecipesWeapons
{
  private String[][] a = { { "X", "X", "#" } };
  private Object[][] b = { { Blocks.PLANKS, Blocks.COBBLESTONE, Items.IRON_INGOT, Items.DIAMOND, Items.GOLD_INGOT }, { Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD, Items.GOLDEN_SWORD } };
  
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
    ☃.registerShapedRecipe(new ItemStack(Items.BOW, 1), new Object[] { " #X", "# X", " #X", Character.valueOf('X'), Items.STRING, Character.valueOf('#'), Items.STICK });
    
    ☃.registerShapedRecipe(new ItemStack(Items.ARROW, 4), new Object[] { "X", "#", "Y", Character.valueOf('Y'), Items.FEATHER, Character.valueOf('X'), Items.FLINT, Character.valueOf('#'), Items.STICK });
  }
}
