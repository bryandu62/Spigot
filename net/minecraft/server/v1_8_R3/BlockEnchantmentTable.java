package net.minecraft.server.v1_8_R3;

public class BlockEnchantmentTable
  extends BlockContainer
{
  protected BlockEnchantmentTable()
  {
    super(Material.STONE, MaterialMapColor.D);
    a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
    e(0);
    a(CreativeModeTab.c);
  }
  
  public boolean d()
  {
    return false;
  }
  
  public boolean c()
  {
    return false;
  }
  
  public int b()
  {
    return 3;
  }
  
  public TileEntity a(World ☃, int ☃)
  {
    return new TileEntityEnchantTable();
  }
  
  public boolean interact(World ☃, BlockPosition ☃, IBlockData ☃, EntityHuman ☃, EnumDirection ☃, float ☃, float ☃, float ☃)
  {
    if (☃.isClientSide) {
      return true;
    }
    TileEntity ☃ = ☃.getTileEntity(☃);
    if ((☃ instanceof TileEntityEnchantTable)) {
      ☃.openTileEntity((TileEntityEnchantTable)☃);
    }
    return true;
  }
  
  public void postPlace(World ☃, BlockPosition ☃, IBlockData ☃, EntityLiving ☃, ItemStack ☃)
  {
    super.postPlace(☃, ☃, ☃, ☃, ☃);
    if (☃.hasName())
    {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if ((☃ instanceof TileEntityEnchantTable)) {
        ((TileEntityEnchantTable)☃).a(☃.getName());
      }
    }
  }
}
