package net.minecraft.server.v1_8_R3;

public class ItemStep
  extends ItemBlock
{
  private final BlockStepAbstract b;
  private final BlockStepAbstract c;
  
  public ItemStep(Block ☃, BlockStepAbstract ☃, BlockStepAbstract ☃)
  {
    super(☃);
    this.b = ☃;
    this.c = ☃;
    
    setMaxDurability(0);
    a(true);
  }
  
  public int filterData(int ☃)
  {
    return ☃;
  }
  
  public String e_(ItemStack ☃)
  {
    return this.b.b(☃.getData());
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.count == 0) {
      return false;
    }
    if (!☃.a(☃.shift(☃), ☃, ☃)) {
      return false;
    }
    Object ☃ = this.b.a(☃);
    IBlockData ☃ = ☃.getType(☃);
    if (☃.getBlock() == this.b)
    {
      IBlockState ☃ = this.b.n();
      Comparable ☃ = ☃.get(☃);
      BlockStepAbstract.EnumSlabHalf ☃ = (BlockStepAbstract.EnumSlabHalf)☃.get(BlockStepAbstract.HALF);
      if (((☃ == EnumDirection.UP) && (☃ == BlockStepAbstract.EnumSlabHalf.BOTTOM)) || ((☃ == EnumDirection.DOWN) && (☃ == BlockStepAbstract.EnumSlabHalf.TOP) && (☃ == ☃)))
      {
        IBlockData ☃ = this.c.getBlockData().set(☃, ☃);
        if ((☃.b(this.c.a(☃, ☃, ☃))) && (☃.setTypeAndData(☃, ☃, 3)))
        {
          ☃.makeSound(☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, this.c.stepSound.getPlaceSound(), (this.c.stepSound.getVolume1() + 1.0F) / 2.0F, this.c.stepSound.getVolume2() * 0.8F);
          ☃.count -= 1;
        }
        return true;
      }
    }
    if (a(☃, ☃, ☃.shift(☃), ☃)) {
      return true;
    }
    return super.interactWith(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
  }
  
  private boolean a(ItemStack ☃, World ☃, BlockPosition ☃, Object ☃)
  {
    IBlockData ☃ = ☃.getType(☃);
    if (☃.getBlock() == this.b)
    {
      Comparable ☃ = ☃.get(this.b.n());
      if (☃ == ☃)
      {
        IBlockData ☃ = this.c.getBlockData().set(this.b.n(), ☃);
        if ((☃.b(this.c.a(☃, ☃, ☃))) && (☃.setTypeAndData(☃, ☃, 3)))
        {
          ☃.makeSound(☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, this.c.stepSound.getPlaceSound(), (this.c.stepSound.getVolume1() + 1.0F) / 2.0F, this.c.stepSound.getVolume2() * 0.8F);
          ☃.count -= 1;
        }
        return true;
      }
    }
    return false;
  }
}
