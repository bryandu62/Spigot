package net.minecraft.server.v1_8_R3;

public abstract interface IBlockAccess
{
  public abstract TileEntity getTileEntity(BlockPosition paramBlockPosition);
  
  public abstract IBlockData getType(BlockPosition paramBlockPosition);
  
  public abstract boolean isEmpty(BlockPosition paramBlockPosition);
  
  public abstract int getBlockPower(BlockPosition paramBlockPosition, EnumDirection paramEnumDirection);
}
