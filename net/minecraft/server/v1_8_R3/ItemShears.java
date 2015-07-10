package net.minecraft.server.v1_8_R3;

public class ItemShears
  extends Item
{
  public ItemShears()
  {
    c(1);
    setMaxDurability(238);
    a(CreativeModeTab.i);
  }
  
  public boolean a(ItemStack ☃, World ☃, Block ☃, BlockPosition ☃, EntityLiving ☃)
  {
    if ((☃.getMaterial() == Material.LEAVES) || (☃ == Blocks.WEB) || (☃ == Blocks.TALLGRASS) || (☃ == Blocks.VINE) || (☃ == Blocks.TRIPWIRE) || (☃ == Blocks.WOOL))
    {
      ☃.damage(1, ☃);
      return true;
    }
    return super.a(☃, ☃, ☃, ☃, ☃);
  }
  
  public boolean canDestroySpecialBlock(Block ☃)
  {
    return (☃ == Blocks.WEB) || (☃ == Blocks.REDSTONE_WIRE) || (☃ == Blocks.TRIPWIRE);
  }
  
  public float getDestroySpeed(ItemStack ☃, Block ☃)
  {
    if ((☃ == Blocks.WEB) || (☃.getMaterial() == Material.LEAVES)) {
      return 15.0F;
    }
    if (☃ == Blocks.WOOL) {
      return 5.0F;
    }
    return super.getDestroySpeed(☃, ☃);
  }
}
