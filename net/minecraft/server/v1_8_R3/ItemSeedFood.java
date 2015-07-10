package net.minecraft.server.v1_8_R3;

public class ItemSeedFood
  extends ItemFood
{
  private Block b;
  private Block c;
  
  public ItemSeedFood(int ☃, float ☃, Block ☃, Block ☃)
  {
    super(☃, ☃, false);
    
    this.b = ☃;
    this.c = ☃;
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃ != EnumDirection.UP) {
      return false;
    }
    if (!☃.a(☃.shift(☃), ☃, ☃)) {
      return false;
    }
    if ((☃.getType(☃).getBlock() == this.c) && (☃.isEmpty(☃.up())))
    {
      ☃.setTypeUpdate(☃.up(), this.b.getBlockData());
      ☃.count -= 1;
      return true;
    }
    return false;
  }
}
