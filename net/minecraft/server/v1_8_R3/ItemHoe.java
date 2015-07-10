package net.minecraft.server.v1_8_R3;

public class ItemHoe
  extends Item
{
  protected Item.EnumToolMaterial a;
  
  public ItemHoe(Item.EnumToolMaterial ☃)
  {
    this.a = ☃;
    this.maxStackSize = 1;
    setMaxDurability(☃.a());
    a(CreativeModeTab.i);
  }
  
  public boolean interactWith(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (!☃.a(☃.shift(☃), ☃, ☃)) {
      return false;
    }
    IBlockData ☃ = ☃.getType(☃);
    Block ☃ = ☃.getBlock();
    if ((☃ != EnumDirection.DOWN) && (☃.getType(☃.up()).getBlock().getMaterial() == Material.AIR))
    {
      if (☃ == Blocks.GRASS) {
        return a(☃, ☃, ☃, ☃, Blocks.FARMLAND.getBlockData());
      }
      if (☃ == Blocks.DIRT) {
        switch (1.a[((BlockDirt.EnumDirtVariant)☃.get(BlockDirt.VARIANT)).ordinal()])
        {
        case 1: 
          return a(☃, ☃, ☃, ☃, Blocks.FARMLAND.getBlockData());
        case 2: 
          return a(☃, ☃, ☃, ☃, Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT));
        }
      }
    }
    return false;
  }
  
  protected boolean a(ItemStack ☃, EntityHuman ☃, World ☃, BlockPosition ☃, IBlockData ☃)
  {
    ☃.makeSound(☃.getX() + 0.5F, ☃.getY() + 0.5F, ☃.getZ() + 0.5F, ☃.getBlock().stepSound.getStepSound(), (☃.getBlock().stepSound.getVolume1() + 1.0F) / 2.0F, ☃.getBlock().stepSound.getVolume2() * 0.8F);
    if (☃.isClientSide) {
      return true;
    }
    ☃.setTypeUpdate(☃, ☃);
    ☃.damage(1, ☃);
    
    return true;
  }
  
  public String g()
  {
    return this.a.toString();
  }
}
