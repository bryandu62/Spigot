package net.minecraft.server.v1_8_R3;

import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryBeacon;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;

public class ContainerBeacon
  extends Container
{
  private IInventory a;
  private final SlotBeacon f;
  private CraftInventoryView bukkitEntity = null;
  private PlayerInventory player;
  
  public ContainerBeacon(IInventory iinventory, IInventory iinventory1)
  {
    this.player = ((PlayerInventory)iinventory);
    this.a = iinventory1;
    a(this.f = new SlotBeacon(iinventory1, 0, 136, 110));
    byte b0 = 36;
    short short0 = 137;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        a(new Slot(iinventory, j + i * 9 + 9, b0 + j * 18, short0 + i * 18));
      }
    }
    for (i = 0; i < 9; i++) {
      a(new Slot(iinventory, i, b0 + i * 18, 58 + short0));
    }
  }
  
  public void addSlotListener(ICrafting icrafting)
  {
    super.addSlotListener(icrafting);
    icrafting.setContainerData(this, this.a);
  }
  
  public IInventory e()
  {
    return this.a;
  }
  
  public void b(EntityHuman entityhuman)
  {
    super.b(entityhuman);
    if ((entityhuman != null) && (!entityhuman.world.isClientSide))
    {
      ItemStack itemstack = this.f.a(this.f.getMaxStackSize());
      if (itemstack != null) {
        entityhuman.drop(itemstack, false);
      }
    }
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    if (!this.checkReachable) {
      return true;
    }
    return this.a.a(entityhuman);
  }
  
  public ItemStack b(EntityHuman entityhuman, int i)
  {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.c.get(i);
    if ((slot != null) && (slot.hasItem()))
    {
      ItemStack itemstack1 = slot.getItem();
      
      itemstack = itemstack1.cloneItemStack();
      if (i == 0)
      {
        if (!a(itemstack1, 1, 37, true)) {
          return null;
        }
        slot.a(itemstack1, itemstack);
      }
      else if ((!this.f.hasItem()) && (this.f.isAllowed(itemstack1)) && (itemstack1.count == 1))
      {
        if (!a(itemstack1, 0, 1, false)) {
          return null;
        }
      }
      else if ((i >= 1) && (i < 28))
      {
        if (!a(itemstack1, 28, 37, false)) {
          return null;
        }
      }
      else if ((i >= 28) && (i < 37))
      {
        if (!a(itemstack1, 1, 28, false)) {
          return null;
        }
      }
      else if (!a(itemstack1, 1, 37, false))
      {
        return null;
      }
      if (itemstack1.count == 0) {
        slot.set(null);
      } else {
        slot.f();
      }
      if (itemstack1.count == itemstack.count) {
        return null;
      }
      slot.a(entityhuman, itemstack1);
    }
    return itemstack;
  }
  
  class SlotBeacon
    extends Slot
  {
    public SlotBeacon(IInventory iinventory, int i, int j, int k)
    {
      super(i, j, k);
    }
    
    public boolean isAllowed(ItemStack itemstack)
    {
      return itemstack != null;
    }
    
    public int getMaxStackSize()
    {
      return 1;
    }
  }
  
  public CraftInventoryView getBukkitView()
  {
    if (this.bukkitEntity != null) {
      return this.bukkitEntity;
    }
    CraftInventory inventory = new CraftInventoryBeacon((TileEntityBeacon)this.a);
    this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
    return this.bukkitEntity;
  }
}
