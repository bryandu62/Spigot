package net.minecraft.server.v1_8_R3;

public abstract interface IWorldInventory
  extends IInventory
{
  public abstract int[] getSlotsForFace(EnumDirection paramEnumDirection);
  
  public abstract boolean canPlaceItemThroughFace(int paramInt, ItemStack paramItemStack, EnumDirection paramEnumDirection);
  
  public abstract boolean canTakeItemThroughFace(int paramInt, ItemStack paramItemStack, EnumDirection paramEnumDirection);
}
