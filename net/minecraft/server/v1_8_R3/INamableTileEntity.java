package net.minecraft.server.v1_8_R3;

public abstract interface INamableTileEntity
{
  public abstract String getName();
  
  public abstract boolean hasCustomName();
  
  public abstract IChatBaseComponent getScoreboardDisplayName();
}
