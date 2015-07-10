package net.minecraft.server.v1_8_R3;

public class RecipeMapExtend
  extends ShapedRecipes
{
  public RecipeMapExtend()
  {
    super(3, 3, new ItemStack[] { new ItemStack(Items.PAPER), new ItemStack(Items.PAPER), new ItemStack(Items.PAPER), new ItemStack(Items.PAPER), new ItemStack(Items.FILLED_MAP, 0, 32767), new ItemStack(Items.PAPER), new ItemStack(Items.PAPER), new ItemStack(Items.PAPER), new ItemStack(Items.PAPER) }, new ItemStack(Items.MAP, 0, 0));
  }
  
  public boolean a(InventoryCrafting ☃, World ☃)
  {
    if (!super.a(☃, ☃)) {
      return false;
    }
    ItemStack ☃ = null;
    for (int ☃ = 0; (☃ < ☃.getSize()) && (☃ == null); ☃++)
    {
      ItemStack ☃ = ☃.getItem(☃);
      if ((☃ != null) && (☃.getItem() == Items.FILLED_MAP)) {
        ☃ = ☃;
      }
    }
    if (☃ == null) {
      return false;
    }
    WorldMap ☃ = Items.FILLED_MAP.getSavedMap(☃, ☃);
    if (☃ == null) {
      return false;
    }
    return ☃.scale < 4;
  }
  
  public ItemStack a(InventoryCrafting ☃)
  {
    ItemStack ☃ = null;
    for (int ☃ = 0; (☃ < ☃.getSize()) && (☃ == null); ☃++)
    {
      ItemStack ☃ = ☃.getItem(☃);
      if ((☃ != null) && (☃.getItem() == Items.FILLED_MAP)) {
        ☃ = ☃;
      }
    }
    ☃ = ☃.cloneItemStack();
    ☃.count = 1;
    if (☃.getTag() == null) {
      ☃.setTag(new NBTTagCompound());
    }
    ☃.getTag().setBoolean("map_is_scaling", true);
    
    return ☃;
  }
}
