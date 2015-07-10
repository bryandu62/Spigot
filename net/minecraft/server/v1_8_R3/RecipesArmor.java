package net.minecraft.server.v1_8_R3;

public class RecipesArmor
{
  private String[][] a = { { "XXX", "X X" }, { "X X", "XXX", "XXX" }, { "XXX", "X X", "X X" }, { "X X", "X X" } };
  private Item[][] b = { { Items.LEATHER, Items.IRON_INGOT, Items.DIAMOND, Items.GOLD_INGOT }, { Items.LEATHER_HELMET, Items.IRON_HELMET, Items.DIAMOND_HELMET, Items.GOLDEN_HELMET }, { Items.LEATHER_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.GOLDEN_CHESTPLATE }, { Items.LEATHER_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.GOLDEN_LEGGINGS }, { Items.LEATHER_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS, Items.GOLDEN_BOOTS } };
  
  public void a(CraftingManager ☃)
  {
    for (int ☃ = 0; ☃ < this.b[0].length; ☃++)
    {
      Item ☃ = this.b[0][☃];
      for (int ☃ = 0; ☃ < this.b.length - 1; ☃++)
      {
        Item ☃ = this.b[(☃ + 1)][☃];
        ☃.registerShapedRecipe(new ItemStack(☃), new Object[] { this.a[☃], Character.valueOf('X'), ☃ });
      }
    }
  }
}
