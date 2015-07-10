package net.minecraft.server.v1_8_R3;

public class BlockHay
  extends BlockRotatable
{
  public BlockHay()
  {
    super(Material.GRASS, MaterialMapColor.t);
    j(this.blockStateList.getBlockData().set(AXIS, EnumDirection.EnumAxis.Y));
    a(CreativeModeTab.b);
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    EnumDirection.EnumAxis ☃ = EnumDirection.EnumAxis.Y;
    int ☃ = ☃ & 0xC;
    if (☃ == 4) {
      ☃ = EnumDirection.EnumAxis.X;
    } else if (☃ == 8) {
      ☃ = EnumDirection.EnumAxis.Z;
    }
    return getBlockData().set(AXIS, ☃);
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    int ☃ = 0;
    
    EnumDirection.EnumAxis ☃ = (EnumDirection.EnumAxis)☃.get(AXIS);
    if (☃ == EnumDirection.EnumAxis.X) {
      ☃ |= 0x4;
    } else if (☃ == EnumDirection.EnumAxis.Z) {
      ☃ |= 0x8;
    }
    return ☃;
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { AXIS });
  }
  
  protected ItemStack i(IBlockData ☃)
  {
    return new ItemStack(Item.getItemOf(this), 1, 0);
  }
  
  public IBlockData getPlacedState(World ☃, BlockPosition ☃, EnumDirection ☃, float ☃, float ☃, float ☃, int ☃, EntityLiving ☃)
  {
    return super.getPlacedState(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃).set(AXIS, ☃.k());
  }
}
