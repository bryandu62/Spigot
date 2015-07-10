package net.minecraft.server.v1_8_R3;

import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

public abstract interface IInventory
  extends INamableTileEntity
{
  public static final int MAX_STACK = 64;
  
  public abstract int getSize();
  
  public abstract ItemStack getItem(int paramInt);
  
  public abstract ItemStack splitStack(int paramInt1, int paramInt2);
  
  public abstract ItemStack splitWithoutUpdate(int paramInt);
  
  public abstract void setItem(int paramInt, ItemStack paramItemStack);
  
  public abstract int getMaxStackSize();
  
  public abstract void update();
  
  public abstract boolean a(EntityHuman paramEntityHuman);
  
  public abstract void startOpen(EntityHuman paramEntityHuman);
  
  public abstract void closeContainer(EntityHuman paramEntityHuman);
  
  public abstract boolean b(int paramInt, ItemStack paramItemStack);
  
  public abstract int getProperty(int paramInt);
  
  public abstract void b(int paramInt1, int paramInt2);
  
  public abstract int g();
  
  public abstract void l();
  
  public abstract ItemStack[] getContents();
  
  public abstract void onOpen(CraftHumanEntity paramCraftHumanEntity);
  
  public abstract void onClose(CraftHumanEntity paramCraftHumanEntity);
  
  public abstract List<HumanEntity> getViewers();
  
  public abstract InventoryHolder getOwner();
  
  public abstract void setMaxStackSize(int paramInt);
}
