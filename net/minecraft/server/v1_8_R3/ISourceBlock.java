package net.minecraft.server.v1_8_R3;

public abstract interface ISourceBlock
  extends ILocationSource
{
  public abstract double getX();
  
  public abstract double getY();
  
  public abstract double getZ();
  
  public abstract BlockPosition getBlockPosition();
  
  public abstract int f();
  
  public abstract <T extends TileEntity> T getTileEntity();
}
