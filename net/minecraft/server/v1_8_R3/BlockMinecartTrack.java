package net.minecraft.server.v1_8_R3;

public class BlockMinecartTrack
  extends BlockMinecartTrackAbstract
{
  public static final BlockStateEnum<BlockMinecartTrackAbstract.EnumTrackPosition> SHAPE = BlockStateEnum.of("shape", BlockMinecartTrackAbstract.EnumTrackPosition.class);
  
  protected BlockMinecartTrack()
  {
    super(false);
    j(this.blockStateList.getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH));
  }
  
  protected void b(World ☃, BlockPosition ☃, IBlockData ☃, Block ☃)
  {
    if ((☃.isPowerSource()) && 
      (new BlockMinecartTrackAbstract.MinecartTrackLogic(this, ☃, ☃, ☃).a() == 3)) {
      a(☃, ☃, ☃, false);
    }
  }
  
  public IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> n()
  {
    return SHAPE;
  }
  
  public IBlockData fromLegacyData(int ☃)
  {
    return getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.a(☃));
  }
  
  public int toLegacyData(IBlockData ☃)
  {
    return ((BlockMinecartTrackAbstract.EnumTrackPosition)☃.get(SHAPE)).a();
  }
  
  protected BlockStateList getStateList()
  {
    return new BlockStateList(this, new IBlockState[] { SHAPE });
  }
}
