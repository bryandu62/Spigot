package net.minecraft.server.v1_8_R3;

public abstract interface ITileEntityContainer
  extends INamableTileEntity
{
  public abstract Container createContainer(PlayerInventory paramPlayerInventory, EntityHuman paramEntityHuman);
  
  public abstract String getContainerName();
}
