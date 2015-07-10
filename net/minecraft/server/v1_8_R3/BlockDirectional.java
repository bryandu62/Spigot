package net.minecraft.server.v1_8_R3;

public abstract class BlockDirectional
  extends Block
{
  public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
  
  protected BlockDirectional(Material ☃)
  {
    super(☃);
  }
  
  protected BlockDirectional(Material ☃, MaterialMapColor ☃)
  {
    super(☃, ☃);
  }
}
