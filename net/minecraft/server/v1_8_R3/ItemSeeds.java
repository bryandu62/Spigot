package net.minecraft.server.v1_8_R3;

public class ItemSeeds
  extends Item
{
  private Block a;
  private Block b;
  
  public ItemSeeds(Block ☃, Block ☃)
  {
    this.a = ☃;
    this.b = ☃;
    a(CreativeModeTab.l);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃ != EnumDirection.UP) {
      return false;
    }
    if (!☃.a(☃.shift(☃), ☃, ☃)) {
      return false;
    }
    if ((☃.getType(☃).getBlock() == this.b) && (☃.isEmpty(☃.up())))
    {
      ☃.setTypeUpdate(☃.up(), this.a.getBlockData());
      ☃.count -= 1;
      return true;
    }
    return false;
  }
}
