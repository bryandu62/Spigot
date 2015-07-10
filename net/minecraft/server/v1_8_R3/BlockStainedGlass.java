package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockStainedGlass
  extends BlockHalfTransparent
{
  public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);
  
  public BlockStainedGlass(Material ☃)
  {
    super(☃, false);
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
  
  public int a(Random ☃)
  {
    return 0;
  }
  
  protected boolean I()
  {
    return true;
  }
  
  public boolean d()
  {
    return false;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(COLOR, EnumColor.fromColorIndex(☃));
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
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((EnumColor)☃.get(COLOR)).getColorIndex();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { COLOR });
  }
}
