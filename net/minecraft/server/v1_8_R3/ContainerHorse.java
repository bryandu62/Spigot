package net.minecraft.server.v1_8_R3;

import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryHorse;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;

public class ContainerHorse
  extends Container
{
  private IInventory a;
  private EntityHorse f;
  CraftInventoryView bukkitEntity;
  PlayerInventory player;
  
  public InventoryView getBukkitView()
  {
    if (this.bukkitEntity != null) {
      return this.bukkitEntity;
    }
    CraftInventory inventory = new CraftInventoryHorse(this.a);
    return this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
  }
  
  public ContainerHorse(IInventory iinventory, IInventory iinventory1, final EntityHorse entityhorse, EntityHuman entityhuman)
  {
    this.player = ((PlayerInventory)iinventory);
    
    this.a = iinventory1;
    this.f = entityhorse;
    byte b0 = 3;
    
    iinventory1.startOpen(entityhuman);
    int i = (b0 - 4) * 18;
    
    a(new Slot(iinventory1, 0, 8, 18)
    {
      public boolean isAllowed(ItemStack itemstack)
      {
        return (super.isAllowed(itemstack)) && (itemstack.getItem() == Items.SADDLE) && (!hasItem());
      }
    });
    a(new Slot(iinventory1, 1, 8, 36)
    {
      public boolean isAllowed(ItemStack itemstack)
      {
        return (super.isAllowed(itemstack)) && (entityhorse.cO()) && (EntityHorse.a(itemstack.getItem()));
      }
    });
    if (entityhorse.hasChest()) {
      for (int j = 0; j < b0; j++) {
        for (int k = 0; k < 5; k++) {
          a(new Slot(iinventory1, 2 + k + j * 5, 80 + k * 18, 18 + j * 18));
        }
      }
    }
    for (int j = 0; j < 3; j++) {
      for (int k = 0; k < 9; k++) {
        a(new Slot(iinventory, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 + i));
      }
    }
    for (j = 0; j < 9; j++) {
      a(new Slot(iinventory, j, 8 + j * 18, 160 + i));
    }
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return (this.a.a(entityhuman)) && (this.f.isAlive()) && (this.f.g(entityhuman) < 8.0F);
  }
  
  public ItemStack b(EntityHuman entityhuman, int i)
  {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.c.get(i);
    if ((slot != null) && (slot.hasItem()))
    {
      ItemStack itemstack1 = slot.getItem();
      
      itemstack = itemstack1.cloneItemStack();
      if (i < this.a.getSize())
      {
        if (!a(itemstack1, this.a.getSize(), this.c.size(), true)) {
          return null;
        }
      }
      else if ((getSlot(1).isAllowed(itemstack1)) && (!getSlot(1).hasItem()))
      {
        if (!a(itemstack1, 1, 2, false)) {
          return null;
        }
      }
      else if (getSlot(0).isAllowed(itemstack1))
      {
        if (!a(itemstack1, 0, 1, false)) {
          return null;
        }
      }
      else if ((this.a.getSize() <= 2) || (!a(itemstack1, 2, this.a.getSize(), false))) {
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
    this.a.closeContainer(entityhuman);
  }
}
