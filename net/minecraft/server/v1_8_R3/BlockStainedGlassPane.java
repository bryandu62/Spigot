package net.minecraft.server.v1_8_R3;

public class BlockStainedGlassPane
  extends BlockThin
{
  public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);
  
  public BlockStainedGlassPane()
  {
    super(Material.SHATTERABLE, false);
    j(this.blockStateList.getBlockData().set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)).set(COLOR, EnumColor.WHITE));
    a(CreativeModeTab.c);
  }
  
  public int getDropData(IBlockData ☃)
  {
    return ((EnumColor)☃.get(COLOR)).getColorIndex();
  }
  
  public MaterialMapColor g(IBlockData ☃)
  {
    return ((EnumColor)☃.get(COLOR)).e();
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(COLOR, EnumColor.fromColorIndex(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumColor)☃.get(COLOR)).getColorIndex();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { NORTH, EAST, WEST, SOUTH, COLOR });
  }
  
  public void onPlace(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (!☃.isClientSide) {
      BlockBeacon.f(☃, ☃);
    }
  }
  
  public void remove(World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if (!☃.isClientSide) {
      BlockBeacon.f(☃, ☃);
    }
  }
}
