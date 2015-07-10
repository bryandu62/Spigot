package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.server.v1_8_R3.IHopper;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import net.minecraft.server.v1_8_R3.InventoryEnderChest;
import net.minecraft.server.v1_8_R3.InventoryMerchant;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import net.minecraft.server.v1_8_R3.TileEntityBeacon;
import net.minecraft.server.v1_8_R3.TileEntityBrewingStand;
import net.minecraft.server.v1_8_R3.TileEntityDispenser;
import net.minecraft.server.v1_8_R3.TileEntityDropper;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftInventory
  implements Inventory
{
  protected final IInventory inventory;
  
  public CraftInventory(IInventory inventory)
  {
    this.inventory = inventory;
  }
  
  public IInventory getInventory()
  {
    return this.inventory;
  }
  
  public int getSize()
  {
    return getInventory().getSize();
  }
  
  public String getName()
  {
    return getInventory().getName();
  }
  
  public org.bukkit.inventory.ItemStack getItem(int index)
  {
    net.minecraft.server.v1_8_R3.ItemStack item = getInventory().getItem(index);
    return item == null ? null : CraftItemStack.asCraftMirror(item);
  }
  
  public org.bukkit.inventory.ItemStack[] getContents()
  {
    org.bukkit.inventory.ItemStack[] items = new org.bukkit.inventory.ItemStack[getSize()];
    net.minecraft.server.v1_8_R3.ItemStack[] mcItems = getInventory().getContents();
    
    int size = Math.min(items.length, mcItems.length);
    for (int i = 0; i < size; i++) {
      items[i] = (mcItems[i] == null ? null : CraftItemStack.asCraftMirror(mcItems[i]));
    }
    return items;
  }
  
  public void setContents(org.bukkit.inventory.ItemStack[] items)
  {
    if (getInventory().getContents().length < items.length) {
      throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getContents().length + " or less");
    }
    net.minecraft.server.v1_8_R3.ItemStack[] mcItems = getInventory().getContents();
    for (int i = 0; i < mcItems.length; i++) {
      if (i >= items.length) {
        mcItems[i] = null;
      } else {
        mcItems[i] = CraftItemStack.asNMSCopy(items[i]);
      }
    }
  }
  
  public void setItem(int index, org.bukkit.inventory.ItemStack item)
  {
    getInventory().setItem(index, (item == null) || (item.getTypeId() == 0) ? null : CraftItemStack.asNMSCopy(item));
  }
  
  public boolean contains(int materialId)
  {
    org.bukkit.inventory.ItemStack[] arrayOfItemStack;
    int i = (arrayOfItemStack = getContents()).length;
    for (int j = 0; j < i; j++)
    {
      org.bukkit.inventory.ItemStack item = arrayOfItemStack[j];
      if ((item != null) && (item.getTypeId() == materialId)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean contains(Material material)
  {
    Validate.notNull(material, "Material cannot be null");
    return contains(material.getId());
  }
  
  public boolean contains(org.bukkit.inventory.ItemStack item)
  {
    if (item == null) {
      return false;
    }
    org.bukkit.inventory.ItemStack[] arrayOfItemStack;
    int i = (arrayOfItemStack = getContents()).length;
    for (int j = 0; j < i; j++)
    {
      org.bukkit.inventory.ItemStack i = arrayOfItemStack[j];
      if (item.equals(i)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean contains(int materialId, int amount)
  {
    if (amount <= 0) {
      return true;
    }
    org.bukkit.inventory.ItemStack[] arrayOfItemStack;
    int i = (arrayOfItemStack = getContents()).length;
    for (int j = 0; j < i; j++)
    {
      org.bukkit.inventory.ItemStack item = arrayOfItemStack[j];
      if ((item != null) && (item.getTypeId() == materialId) && 
        (amount -= item.getAmount() <= 0)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean contains(Material material, int amount)
  {
    Validate.notNull(material, "Material cannot be null");
    return contains(material.getId(), amount);
  }
  
  public boolean contains(org.bukkit.inventory.ItemStack item, int amount)
  {
    if (item == null) {
      return false;
    }
    if (amount <= 0) {
      return true;
    }
    org.bukkit.inventory.ItemStack[] arrayOfItemStack;
    int i = (arrayOfItemStack = getContents()).length;
    for (int j = 0; j < i; j++)
    {
      org.bukkit.inventory.ItemStack i = arrayOfItemStack[j];
      if (item.equals(i))
      {
        amount--;
        if (amount <= 0) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean containsAtLeast(org.bukkit.inventory.ItemStack item, int amount)
  {
    if (item == null) {
      return false;
    }
    if (amount <= 0) {
      return true;
    }
    org.bukkit.inventory.ItemStack[] arrayOfItemStack;
    int i = (arrayOfItemStack = getContents()).length;
    for (int j = 0; j < i; j++)
    {
      org.bukkit.inventory.ItemStack i = arrayOfItemStack[j];
      if ((item.isSimilar(i)) && (amount -= i.getAmount() <= 0)) {
        return true;
      }
    }
    return false;
  }
  
  public HashMap<Integer, org.bukkit.inventory.ItemStack> all(int materialId)
  {
    HashMap<Integer, org.bukkit.inventory.ItemStack> slots = new HashMap();
    
    org.bukkit.inventory.ItemStack[] inventory = getContents();
    for (int i = 0; i < inventory.length; i++)
    {
      org.bukkit.inventory.ItemStack item = inventory[i];
      if ((item != null) && (item.getTypeId() == materialId)) {
        slots.put(Integer.valueOf(i), item);
      }
    }
    return slots;
  }
  
  public HashMap<Integer, org.bukkit.inventory.ItemStack> all(Material material)
  {
    Validate.notNull(material, "Material cannot be null");
    return all(material.getId());
  }
  
  public HashMap<Integer, org.bukkit.inventory.ItemStack> all(org.bukkit.inventory.ItemStack item)
  {
    HashMap<Integer, org.bukkit.inventory.ItemStack> slots = new HashMap();
    if (item != null)
    {
      org.bukkit.inventory.ItemStack[] inventory = getContents();
      for (int i = 0; i < inventory.length; i++) {
        if (item.equals(inventory[i])) {
          slots.put(Integer.valueOf(i), inventory[i]);
        }
      }
    }
    return slots;
  }
  
  public int first(int materialId)
  {
    org.bukkit.inventory.ItemStack[] inventory = getContents();
    for (int i = 0; i < inventory.length; i++)
    {
      org.bukkit.inventory.ItemStack item = inventory[i];
      if ((item != null) && (item.getTypeId() == materialId)) {
        return i;
      }
    }
    return -1;
  }
  
  public int first(Material material)
  {
    Validate.notNull(material, "Material cannot be null");
    return first(material.getId());
  }
  
  public int first(org.bukkit.inventory.ItemStack item)
  {
    return first(item, true);
  }
  
  private int first(org.bukkit.inventory.ItemStack item, boolean withAmount)
  {
    if (item == null) {
      return -1;
    }
    org.bukkit.inventory.ItemStack[] inventory = getContents();
    for (int i = 0; i < inventory.length; i++) {
      if (inventory[i] != null) {
        if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
          return i;
        }
      }
    }
    return -1;
  }
  
  public int firstEmpty()
  {
    org.bukkit.inventory.ItemStack[] inventory = getContents();
    for (int i = 0; i < inventory.length; i++) {
      if (inventory[i] == null) {
        return i;
      }
    }
    return -1;
  }
  
  public int firstPartial(int materialId)
  {
    org.bukkit.inventory.ItemStack[] inventory = getContents();
    for (int i = 0; i < inventory.length; i++)
    {
      org.bukkit.inventory.ItemStack item = inventory[i];
      if ((item != null) && (item.getTypeId() == materialId) && (item.getAmount() < item.getMaxStackSize())) {
        return i;
      }
    }
    return -1;
  }
  
  public int firstPartial(Material material)
  {
    Validate.notNull(material, "Material cannot be null");
    return firstPartial(material.getId());
  }
  
  private int firstPartial(org.bukkit.inventory.ItemStack item)
  {
    org.bukkit.inventory.ItemStack[] inventory = getContents();
    org.bukkit.inventory.ItemStack filteredItem = CraftItemStack.asCraftCopy(item);
    if (item == null) {
      return -1;
    }
    for (int i = 0; i < inventory.length; i++)
    {
      org.bukkit.inventory.ItemStack cItem = inventory[i];
      if ((cItem != null) && (cItem.getAmount() < cItem.getMaxStackSize()) && (cItem.isSimilar(filteredItem))) {
        return i;
      }
    }
    return -1;
  }
  
  public HashMap<Integer, org.bukkit.inventory.ItemStack> addItem(org.bukkit.inventory.ItemStack... items)
  {
    Validate.noNullElements(items, "Item cannot be null");
    HashMap<Integer, org.bukkit.inventory.ItemStack> leftover = new HashMap();
    for (int i = 0; i < items.length; i++)
    {
      org.bukkit.inventory.ItemStack item = items[i];
      for (;;)
      {
        int firstPartial = firstPartial(item);
        if (firstPartial == -1)
        {
          int firstFree = firstEmpty();
          if (firstFree == -1)
          {
            leftover.put(Integer.valueOf(i), item);
            break;
          }
          if (item.getAmount() > getMaxItemStack())
          {
            CraftItemStack stack = CraftItemStack.asCraftCopy(item);
            stack.setAmount(getMaxItemStack());
            setItem(firstFree, stack);
            item.setAmount(item.getAmount() - getMaxItemStack());
          }
          else
          {
            setItem(firstFree, item);
            break;
          }
        }
        else
        {
          org.bukkit.inventory.ItemStack partialItem = getItem(firstPartial);
          
          int amount = item.getAmount();
          int partialAmount = partialItem.getAmount();
          int maxAmount = partialItem.getMaxStackSize();
          if (amount + partialAmount <= maxAmount)
          {
            partialItem.setAmount(amount + partialAmount);
            
            setItem(firstPartial, partialItem);
            break;
          }
          partialItem.setAmount(maxAmount);
          
          setItem(firstPartial, partialItem);
          item.setAmount(amount + partialAmount - maxAmount);
        }
      }
    }
    return leftover;
  }
  
  public HashMap<Integer, org.bukkit.inventory.ItemStack> removeItem(org.bukkit.inventory.ItemStack... items)
  {
    Validate.notNull(items, "Items cannot be null");
    HashMap<Integer, org.bukkit.inventory.ItemStack> leftover = new HashMap();
    for (int i = 0; i < items.length; i++)
    {
      org.bukkit.inventory.ItemStack item = items[i];
      int toDelete = item.getAmount();
      do
      {
        int first = first(item, false);
        if (first == -1)
        {
          item.setAmount(toDelete);
          leftover.put(Integer.valueOf(i), item);
          break;
        }
        org.bukkit.inventory.ItemStack itemStack = getItem(first);
        int amount = itemStack.getAmount();
        if (amount <= toDelete)
        {
          toDelete -= amount;
          
          clear(first);
        }
        else
        {
          itemStack.setAmount(amount - toDelete);
          setItem(first, itemStack);
          toDelete = 0;
        }
      } while (toDelete > 0);
    }
    return leftover;
  }
  
  private int getMaxItemStack()
  {
    return getInventory().getMaxStackSize();
  }
  
  public void remove(int materialId)
  {
    org.bukkit.inventory.ItemStack[] items = getContents();
    for (int i = 0; i < items.length; i++) {
      if ((items[i] != null) && (items[i].getTypeId() == materialId)) {
        clear(i);
      }
    }
  }
  
  public void remove(Material material)
  {
    Validate.notNull(material, "Material cannot be null");
    remove(material.getId());
  }
  
  public void remove(org.bukkit.inventory.ItemStack item)
  {
    org.bukkit.inventory.ItemStack[] items = getContents();
    for (int i = 0; i < items.length; i++) {
      if ((items[i] != null) && (items[i].equals(item))) {
        clear(i);
      }
    }
  }
  
  public void clear(int index)
  {
    setItem(index, null);
  }
  
  public void clear()
  {
    for (int i = 0; i < getSize(); i++) {
      clear(i);
    }
  }
  
  public ListIterator<org.bukkit.inventory.ItemStack> iterator()
  {
    return new InventoryIterator(this);
  }
  
  public ListIterator<org.bukkit.inventory.ItemStack> iterator(int index)
  {
    if (index < 0) {
      index += getSize() + 1;
    }
    return new InventoryIterator(this, index);
  }
  
  public List<HumanEntity> getViewers()
  {
    return this.inventory.getViewers();
  }
  
  public String getTitle()
  {
    return this.inventory.getName();
  }
  
  public InventoryType getType()
  {
    if ((this.inventory instanceof InventoryCrafting)) {
      return this.inventory.getSize() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
    }
    if ((this.inventory instanceof PlayerInventory)) {
      return InventoryType.PLAYER;
    }
    if ((this.inventory instanceof TileEntityDropper)) {
      return InventoryType.DROPPER;
    }
    if ((this.inventory instanceof TileEntityDispenser)) {
      return InventoryType.DISPENSER;
    }
    if ((this.inventory instanceof TileEntityFurnace)) {
      return InventoryType.FURNACE;
    }
    if ((this instanceof CraftInventoryEnchanting)) {
      return InventoryType.ENCHANTING;
    }
    if ((this.inventory instanceof TileEntityBrewingStand)) {
      return InventoryType.BREWING;
    }
    if ((this.inventory instanceof CraftInventoryCustom.MinecraftInventory)) {
      return ((CraftInventoryCustom.MinecraftInventory)this.inventory).getType();
    }
    if ((this.inventory instanceof InventoryEnderChest)) {
      return InventoryType.ENDER_CHEST;
    }
    if ((this.inventory instanceof InventoryMerchant)) {
      return InventoryType.MERCHANT;
    }
    if ((this.inventory instanceof TileEntityBeacon)) {
      return InventoryType.BEACON;
    }
    if ((this instanceof CraftInventoryAnvil)) {
      return InventoryType.ANVIL;
    }
    if ((this.inventory instanceof IHopper)) {
      return InventoryType.HOPPER;
    }
    return InventoryType.CHEST;
  }
  
  public InventoryHolder getHolder()
  {
    return this.inventory.getOwner();
  }
  
  public int getMaxStackSize()
  {
    return this.inventory.getMaxStackSize();
  }
  
  public void setMaxStackSize(int size)
  {
    this.inventory.setMaxStackSize(size);
  }
  
  public int hashCode()
  {
    return this.inventory.hashCode();
  }
  
  public boolean equals(Object obj)
  {
    return ((obj instanceof CraftInventory)) && (((CraftInventory)obj).inventory.equals(this.inventory));
  }
}
