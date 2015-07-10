package net.minecraft.server.v1_8_R3;

public class ItemReed
  extends Item
{
  private Block a;
  
  public ItemReed(Block ☃)
  {
    this.a = ☃;
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    if ((☃ == Blocks.SNOW_LAYER) && (((Integer)☃.get(BlockSnow.LAYERS)).intValue() < 1)) {
      ☃ = EnumDirection.UP;
    } else if (!☃.a(☃, ☃)) {
      ☃ = ☃.shift(☃);
    }
    if (!☃.a(☃, ☃, ☃)) {
      return false;
    }
    if (☃.count == 0) {
      return false;
    }
    if (☃.a(this.a, ☃, false, ☃, null, ☃))
    {
      IBlockData ☃ = this.a.getPlacedState(☃, ☃, ☃, ☃, ☃, ☃, 0, ☃);
      if (☃.setTypeAndData(☃, ☃, 3))
      {
        ☃ = ☃.getType(☃);
        if (☃.getBlock() == this.a)
        {
          ItemBlock.a(☃, ☃, ☃, ☃);
          ☃.getBlock().postPlace(☃, ☃, ☃, ☃, ☃);
        }
        ☃.makeSound(☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, this.a.stepSound.getPlaceSound(), (this.a.stepSound.getVolume1() + 1.0F) / 2.0F, this.a.stepSound.getVolume2() * 0.8F);
        ☃.count -= 1;
        return true;
      }
    }
    return false;
  }
}
