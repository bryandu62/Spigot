package net.minecraft.server.v1_8_R3;

import java.util.List;

public abstract interface ICrafting
{
  public abstract void a(Container paramContainer, List<ItemStack> paramList);
  
  public abstract void a(Container paramContainer, int paramInt, ItemStack paramItemStack);
  
  public abstract void setContainerData(Container paramContainer, int paramInt1, int paramInt2);
  
  public abstract void setContainerData(Container paramContainer, IInventory paramIInventory);
}
