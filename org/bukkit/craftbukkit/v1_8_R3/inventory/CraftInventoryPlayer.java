package org.bukkit.craftbukkit.v1_8_R3.inventory;

import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.material.MaterialData;

public class CraftInventoryPlayer
  extends CraftInventory
  implements org.bukkit.inventory.PlayerInventory, EntityEquipment
{
  public CraftInventoryPlayer(net.minecraft.server.v1_8_R3.PlayerInventory inventory)
  {
    super(inventory);
  }
  
  public net.minecraft.server.v1_8_R3.PlayerInventory getInventory()
  {
    return (net.minecraft.server.v1_8_R3.PlayerInventory)this.inventory;
  }
  
  public int getSize()
  {
    return super.getSize() - 4;
  }
  
  public org.bukkit.inventory.ItemStack getItemInHand()
  {
    return CraftItemStack.asCraftMirror(getInventory().getItemInHand());
  }
  
  public void setItemInHand(org.bukkit.inventory.ItemStack stack)
  {
    setItem(getHeldItemSlot(), stack);
  }
  
  public void setItem(int index, org.bukkit.inventory.ItemStack item)
  {
    super.setItem(index, item);
    EntityPlayer player = ((CraftPlayer)getHolder()).getHandle();
    if (index < net.minecraft.server.v1_8_R3.PlayerInventory.getHotbarSize()) {
      index += 36;
    } else if (index > 35) {
      index = 8 - (index - 36);
    }
    player.playerConnection.sendPacket(new PacketPlayOutSetSlot(player.defaultContainer.windowId, index, CraftItemStack.asNMSCopy(item)));
  }
  
  public int getHeldItemSlot()
  {
    return getInventory().itemInHandIndex;
  }
  
  public void setHeldItemSlot(int slot)
  {
    Validate.isTrue((slot >= 0) && (slot < net.minecraft.server.v1_8_R3.PlayerInventory.getHotbarSize()), "Slot is not between 0 and 8 inclusive");
    getInventory().itemInHandIndex = slot;
    ((CraftPlayer)getHolder()).getHandle().playerConnection.sendPacket(new PacketPlayOutHeldItemSlot(slot));
  }
  
  public org.bukkit.inventory.ItemStack getHelmet()
  {
    return getItem(getSize() + 3);
  }
  
  public org.bukkit.inventory.ItemStack getChestplate()
  {
    return getItem(getSize() + 2);
  }
  
  public org.bukkit.inventory.ItemStack getLeggings()
  {
    return getItem(getSize() + 1);
  }
  
  public org.bukkit.inventory.ItemStack getBoots()
  {
    return getItem(getSize() + 0);
  }
  
  public void setHelmet(org.bukkit.inventory.ItemStack helmet)
  {
    setItem(getSize() + 3, helmet);
  }
  
  public void setChestplate(org.bukkit.inventory.ItemStack chestplate)
  {
    setItem(getSize() + 2, chestplate);
  }
  
  public void setLeggings(org.bukkit.inventory.ItemStack leggings)
  {
    setItem(getSize() + 1, leggings);
  }
  
  public void setBoots(org.bukkit.inventory.ItemStack boots)
  {
    setItem(getSize() + 0, boots);
  }
  
  public org.bukkit.inventory.ItemStack[] getArmorContents()
  {
    net.minecraft.server.v1_8_R3.ItemStack[] mcItems = getInventory().getArmorContents();
    org.bukkit.inventory.ItemStack[] ret = new org.bukkit.inventory.ItemStack[mcItems.length];
    for (int i = 0; i < mcItems.length; i++) {
      ret[i] = CraftItemStack.asCraftMirror(mcItems[i]);
    }
    return ret;
  }
  
  public void setArmorContents(org.bukkit.inventory.ItemStack[] items)
  {
    int cnt = getSize();
    if (items == null) {
      items = new org.bukkit.inventory.ItemStack[4];
    }
    org.bukkit.inventory.ItemStack[] arrayOfItemStack;
    int i = (arrayOfItemStack = items).length;
    for (int j = 0; j < i; j++)
    {
      org.bukkit.inventory.ItemStack item = arrayOfItemStack[j];
      if ((item == null) || (item.getTypeId() == 0)) {
        clear(cnt++);
      } else {
        setItem(cnt++, item);
      }
    }
  }
  
  public int clear(int id, int data)
  {
    int count = 0;
    org.bukkit.inventory.ItemStack[] items = getContents();
    org.bukkit.inventory.ItemStack[] armor = getArmorContents();
    int armorSlot = getSize();
    for (int i = 0; i < items.length; i++)
    {
      item = items[i];
      if ((item != null) && 
        ((id <= -1) || (item.getTypeId() == id)) && (
        (data <= -1) || (item.getData().getData() == data)))
      {
        count += item.getAmount();
        setItem(i, null);
      }
    }
    org.bukkit.inventory.ItemStack[] arrayOfItemStack1;
    org.bukkit.inventory.ItemStack localItemStack1 = (arrayOfItemStack1 = armor).length;
    for (org.bukkit.inventory.ItemStack item = 0; item < localItemStack1; item++)
    {
      org.bukkit.inventory.ItemStack item = arrayOfItemStack1[item];
      if ((item != null) && 
        ((id <= -1) || (item.getTypeId() == id)) && (
        (data <= -1) || (item.getData().getData() == data)))
      {
        count += item.getAmount();
        setItem(armorSlot++, null);
      }
    }
    return count;
  }
  
  public HumanEntity getHolder()
  {
    return (HumanEntity)this.inventory.getOwner();
  }
  
  public float getItemInHandDropChance()
  {
    return 1.0F;
  }
  
  public void setItemInHandDropChance(float chance)
  {
    throw new UnsupportedOperationException();
  }
  
  public float getHelmetDropChance()
  {
    return 1.0F;
  }
  
  public void setHelmetDropChance(float chance)
  {
    throw new UnsupportedOperationException();
  }
  
  public float getChestplateDropChance()
  {
    return 1.0F;
  }
  
  public void setChestplateDropChance(float chance)
  {
    throw new UnsupportedOperationException();
  }
  
  public float getLeggingsDropChance()
  {
    return 1.0F;
  }
  
  public void setLeggingsDropChance(float chance)
  {
    throw new UnsupportedOperationException();
  }
  
  public float getBootsDropChance()
  {
    return 1.0F;
  }
  
  public void setBootsDropChance(float chance)
  {
    throw new UnsupportedOperationException();
  }
}
