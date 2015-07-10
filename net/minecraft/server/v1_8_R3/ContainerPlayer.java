package net.minecraft.server.v1_8_R3;

import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;

public class ContainerPlayer
  extends Container
{
  public InventoryCrafting craftInventory = new InventoryCrafting(this, 2, 2);
  public IInventory resultInventory = new InventoryCraftResult();
  public boolean g;
  private final EntityHuman h;
  private CraftInventoryView bukkitEntity = null;
  private PlayerInventory player;
  
  public ContainerPlayer(PlayerInventory playerinventory, boolean flag, EntityHuman entityhuman)
  {
    this.g = flag;
    this.h = entityhuman;
    this.resultInventory = new InventoryCraftResult();
    this.craftInventory = new InventoryCrafting(this, 2, 2, playerinventory.player);
    this.craftInventory.resultInventory = this.resultInventory;
    this.player = playerinventory;
    a(new SlotResult(playerinventory.player, this.craftInventory, this.resultInventory, 0, 144, 36));
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        a(new Slot(this.craftInventory, j + i * 2, 88 + j * 18, 26 + i * 18));
      }
    }
    for (int ii = 0; ii < 4; ii++)
    {
      final int i = ii;
      a(new Slot(playerinventory, playerinventory.getSize() - 1 - ii, 8, 8 + ii * 18)
      {
        public int getMaxStackSize()
        {
          return 1;
        }
        
        public boolean isAllowed(ItemStack itemstack)
        {
          return itemstack != null;
        }
      });
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        a(new Slot(playerinventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      a(new Slot(playerinventory, i, 8 + i * 18, 142));
    }
  }
  
  public void a(IInventory iinventory)
  {
    CraftingManager.getInstance().lastCraftView = getBukkitView();
    ItemStack craftResult = CraftingManager.getInstance().craft(this.craftInventory, this.h.world);
    this.resultInventory.setItem(0, craftResult);
    if (this.listeners.size() < 1) {
      return;
    }
    EntityPlayer player = (EntityPlayer)this.listeners.get(0);
    player.playerConnection.sendPacket(new PacketPlayOutSetSlot(player.activeContainer.windowId, 0, craftResult));
  }
  
  public void b(EntityHuman entityhuman)
  {
    super.b(entityhuman);
    for (int i = 0; i < 4; i++)
    {
      ItemStack itemstack = this.craftInventory.splitWithoutUpdate(i);
      if (itemstack != null) {
        entityhuman.drop(itemstack, false);
      }
    }
    this.resultInventory.setItem(0, null);
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return true;
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
        if (!a(itemstack1, 9, 45, true)) {
          return null;
        }
        slot.a(itemstack1, itemstack);
      }
      else if ((i >= 1) && (i < 5))
      {
        if (!a(itemstack1, 9, 45, false)) {
          return null;
        }
      }
      else if ((i >= 5) && (i < 9))
      {
        if (!a(itemstack1, 9, 45, false)) {
          return null;
        }
      }
      else if (((itemstack.getItem() instanceof ItemArmor)) && (!((Slot)this.c.get(5 + ((ItemArmor)itemstack.getItem()).b)).hasItem()))
      {
        int j = 5 + ((ItemArmor)itemstack.getItem()).b;
        if (!a(itemstack1, j, j + 1, false)) {
          return null;
        }
      }
      else if ((i >= 9) && (i < 36))
      {
        if (!a(itemstack1, 36, 45, false)) {
          return null;
        }
      }
      else if ((i >= 36) && (i < 45))
      {
        if (!a(itemstack1, 9, 36, false)) {
          return null;
        }
      }
      else if (!a(itemstack1, 9, 45, false))
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
  
  public boolean a(ItemStack itemstack, Slot slot)
  {
    return (slot.inventory != this.resultInventory) && (super.a(itemstack, slot));
  }
  
  public CraftInventoryView getBukkitView()
  {
    if (this.bukkitEntity != null) {
      return this.bukkitEntity;
    }
    CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftInventory, this.resultInventory);
    this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
    return this.bukkitEntity;
  }
}
