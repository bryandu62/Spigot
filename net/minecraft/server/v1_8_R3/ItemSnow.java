package net.minecraft.server.v1_8_R3;

public class ItemSnow
  extends ItemBlock
{
  public ItemSnow(Block ☃)
  {
    super(☃);
    
    setMaxDurability(0);
    a(true);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.count == 0) {
      return false;
    }
    if (!☃.a(☃, ☃, ☃)) {
      return false;
    }
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    BlockPosition ☃ = ☃;
    if (((☃ != EnumDirection.UP) || (☃ != this.a)) && (!☃.a(☃, ☃)))
    {
      ☃ = ☃.shift(☃);
      ☃ = ☃.getType(☃);
      ☃ = ☃.getBlock();
    }
    if (☃ == this.a)
    {
      int ☃ = ((Integer)☃.get(BlockSnow.LAYERS)).intValue();
      if (☃ <= 7)
      {
        IBlockData ☃ = ☃.set(BlockSnow.LAYERS, Integer.valueOf(☃ + 1));
        AxisAlignedBB ☃ = this.a.a(☃, ☃, ☃);
        if ((☃ != null) && (☃.b(☃)) && (☃.setTypeAndData(☃, ☃, 2)))
        {
          ☃.makeSound(☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, this.a.stepSound.getPlaceSound(), (this.a.stepSound.getVolume1() + 1.0F) / 2.0F, this.a.stepSound.getVolume2() * 0.8F);
          ☃.count -= 1;
          return true;
        }
      }
    }
    return super.interactWith(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  public int filterData(int ☃)
  {
    return ☃;
  }
}
