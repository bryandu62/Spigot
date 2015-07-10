package net.minecraft.server.v1_8_R3;

import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;

public class ContainerHopper
  extends Container
{
  private final IInventory hopper;
  private CraftInventoryView bukkitEntity = null;
  private PlayerInventory player;
  
  public CraftInventoryView getBukkitView()
  {
    if (this.bukkitEntity != null) {
      return this.bukkitEntity;
    }
    CraftInventory inventory = new CraftInventory(this.hopper);
    this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
    return this.bukkitEntity;
  }
  
  public ContainerHopper(PlayerInventory playerinventory, IInventory iinventory, EntityHuman entityhuman)
  {
    this.hopper = iinventory;
    this.player = playerinventory;
    iinventory.startOpen(entityhuman);
    byte b0 = 51;
    for (int i = 0; i < iinventory.getSize(); i++) {
      a(new Slot(iinventory, i, 44 + i * 18, 20));
    }
    for (i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        a(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
      }
    }
    for (i = 0; i < 9; i++) {
      a(new Slot(playerinventory, i, 8 + i * 18, 58 + b0));
    }
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    if (!this.checkReachable) {
      return true;
    }
    return this.hopper.a(entityhuman);
  }
  
  public ItemStack b(EntityHuman entityhuman, int i)
  {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.c.get(i);
    if ((slot != null) && (slot.hasItem()))
    {
      ItemStack itemstack1 = slot.getItem();
      
      itemstack = itemstack1.cloneItemStack();
      if (i < this.hopper.getSize())
      {
        if (!a(itemstack1, this.hopper.getSize(), this.c.size(), true)) {
          return null;
        }
      }
      else if (!a(itemstack1, 0, this.hopper.getSize(), false)) {
        return null;
      }
      if (itemstack1.count == 0) {
        slot.set(null);
      } else {
        slot.f();
      }
    }
    return itemstack;
  }
  
  public void b(EntityHuman entityhuman)
  {
    super.b(entityhuman);
    this.hopper.closeContainer(entityhuman);
  }
}
