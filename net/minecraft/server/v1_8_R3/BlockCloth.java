package net.minecraft.server.v1_8_R3;

public class BlockCloth
  extends Block
{
  public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);
  
  public BlockCloth(Material ☃)
  {
    super(☃);
    j(this.blockStateList.getBlockData().set(COLOR, EnumColor.WHITE));
    a(CreativeModeTab.b);
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
    return new BlockStateList(this, new IBlockState[] { COLOR });
  }
}
