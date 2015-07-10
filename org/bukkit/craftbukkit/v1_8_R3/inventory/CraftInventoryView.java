package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.Slot;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftInventoryView
  extends InventoryView
{
  private final Container container;
  private final CraftHumanEntity player;
  private final CraftInventory viewing;
  
  public CraftInventoryView(HumanEntity player, Inventory viewing, Container container)
  {
    this.player = ((CraftHumanEntity)player);
    this.viewing = ((CraftInventory)viewing);
    this.container = container;
  }
  
  public Inventory getTopInventory()
  {
    return this.viewing;
  }
  
  public Inventory getBottomInventory()
  {
    return this.player.getInventory();
  }
  
  public HumanEntity getPlayer()
  {
    return this.player;
  }
  
  public InventoryType getType()
  {
    InventoryType type = this.viewing.getType();
    if ((type == InventoryType.CRAFTING) && (this.player.getGameMode() == GameMode.CREATIVE)) {
      return InventoryType.CREATIVE;
    }
    return type;
  }
  
  public void setItem(int slot, org.bukkit.inventory.ItemStack item)
  {
    net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
    if (slot != 64537) {
      this.container.getSlot(slot).set(stack);
    } else {
      this.player.getHandle().drop(stack, false);
    }
  }
  
  public org.bukkit.inventory.ItemStack getItem(int slot)
  {
    if (slot == 64537) {
      return null;
    }
    return CraftItemStack.asCraftMirror(this.container.getSlot(slot).getItem());
  }
  
  public boolean isInTop(int rawSlot)
  {
    return rawSlot < this.viewing.getSize();
  }
  
  public Container getHandle()
  {
    return this.container;
  }
  
  public static InventoryType.SlotType getSlotType(InventoryView inventory, int slot)
  {
    InventoryType.SlotType type = InventoryType.SlotType.CONTAINER;
    if ((slot >= 0) && (slot < inventory.getTopInventory().getSize())) {}
    switch (inventory.getType())
    {
    case CHEST: 
      if (slot == 2) {
        type = InventoryType.SlotType.RESULT;
      } else if (slot == 1) {
        type = InventoryType.SlotType.FUEL;
      } else {
        type = InventoryType.SlotType.CRAFTING;
      }
      break;
    case DROPPER: 
      if (slot == 3) {
        type = InventoryType.SlotType.FUEL;
      } else {
        type = InventoryType.SlotType.CRAFTING;
      }
      break;
    case DISPENSER: 
      type = InventoryType.SlotType.CRAFTING;
      break;
    case CRAFTING: 
    case CREATIVE: 
      if (slot == 0) {
        type = InventoryType.SlotType.RESULT;
      } else {
        type = InventoryType.SlotType.CRAFTING;
      }
      break;
    case FURNACE: 
      if (slot == 2) {
        type = InventoryType.SlotType.RESULT;
      } else {
        type = InventoryType.SlotType.CRAFTING;
      }
      break;
    case PLAYER: 
      type = InventoryType.SlotType.CRAFTING;
      break;
    case MERCHANT: 
      if (slot == 2) {
        type = InventoryType.SlotType.RESULT;
      } else {
        type = InventoryType.SlotType.CRAFTING;
      }
      break;
    case ENCHANTING: 
    case ENDER_CHEST: 
    case HOPPER: 
    default: 
      break;
      if (slot == 64537) {
        type = InventoryType.SlotType.OUTSIDE;
      } else if (inventory.getType() == InventoryType.CRAFTING)
      {
        if (slot < 9) {
          type = InventoryType.SlotType.ARMOR;
        } else if (slot > 35) {
          type = InventoryType.SlotType.QUICKBAR;
        }
      }
      else if (slot >= inventory.countSlots() - 9) {
        type = InventoryType.SlotType.QUICKBAR;
      }
      break;
    }
    return type;
  }
}
