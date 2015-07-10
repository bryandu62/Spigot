package net.minecraft.server.v1_8_R3;

public abstract interface ITileInventory
  extends IInventory, ITileEntityContainer
{
  public abstract boolean r_();
  
  public abstract void a(ChestLock paramChestLock);
  
  public abstract ChestLock i();
}
