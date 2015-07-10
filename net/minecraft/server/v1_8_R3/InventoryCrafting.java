package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class InventoryCrafting
  implements IInventory
{
  private final ItemStack[] items;
  private final int b;
  private final int c;
  private final Container d;
  public List<HumanEntity> transaction = new ArrayList();
  public IRecipe currentRecipe;
  public IInventory resultInventory;
  private EntityHuman owner;
  private int maxStack = 64;
  
  public ItemStack[] getContents()
  {
    return this.items;
  }
  
  public void onOpen(CraftHumanEntity who)
  {
    this.transaction.add(who);
  }
  
  public InventoryType getInvType()
  {
    return this.items.length == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
  }
  
  public void onClose(CraftHumanEntity who)
  {
    this.transaction.remove(who);
  }
  
  public List<HumanEntity> getViewers()
  {
    return this.transaction;
  }
  
  public InventoryHolder getOwner()
  {
    return this.owner == null ? null : this.owner.getBukkitEntity();
  }
  
  public void setMaxStackSize(int size)
  {
    this.maxStack = size;
    this.resultInventory.setMaxStackSize(size);
  }
  
  public InventoryCrafting(Container container, int i, int j, EntityHuman player)
  {
    this(container, i, j);
    this.owner = player;
  }
  
  public InventoryCrafting(Container container, int i, int j)
  {
    int k = i * j;
    
    this.items = new ItemStack[k];
    this.d = container;
    this.b = i;
    this.c = j;
  }
  
  public int getSize()
  {
    return this.items.length;
  }
  
  public ItemStack getItem(int i)
  {
    return i >= getSize() ? null : this.items[i];
  }
  
  public ItemStack c(int i, int j)
  {
    return (i >= 0) && (i < this.b) && (j >= 0) && (j <= this.c) ? getItem(i + j * this.b) : null;
  }
  
  public String getName()
  {
    return "container.crafting";
  }
  
  public boolean hasCustomName()
  {
    return false;
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    return hasCustomName() ? new ChatComponentText(getName()) : new ChatMessage(getName(), new Object[0]);
  }
  
  public ItemStack splitWithoutUpdate(int i)
  {
    if (this.items[i] != null)
    {
      ItemStack itemstack = this.items[i];
      
      this.items[i] = null;
      return itemstack;
    }
    return null;
  }
  
  public ItemStack splitStack(int i, int j)
  {
    if (this.items[i] != null)
    {
      if (this.items[i].count <= j)
      {
        ItemStack itemstack = this.items[i];
        this.items[i] = null;
        this.d.a(this);
        return itemstack;
      }
      ItemStack itemstack = this.items[i].a(j);
      if (this.items[i].count == 0) {
        this.items[i] = null;
      }
      this.d.a(this);
      return itemstack;
    }
    return null;
  }
  
  public void setItem(int i, ItemStack itemstack)
  {
    this.items[i] = itemstack;
    this.d.a(this);
  }
  
  public int getMaxStackSize()
  {
    return 64;
  }
  
  public void update() {}
  
  public boolean a(EntityHuman entityhuman)
  {
    return true;
  }
  
  public void startOpen(EntityHuman entityhuman) {}
  
  public void closeContainer(EntityHuman entityhuman) {}
  
  public boolean b(int i, ItemStack itemstack)
  {
    return true;
  }
  
  public int getProperty(int i)
  {
    return 0;
  }
  
  public void b(int i, int j) {}
  
  public int g()
  {
    return 0;
  }
  
  public void l()
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i] = null;
    }
  }
  
  public int h()
  {
    return this.c;
  }
  
  public int i()
  {
    return this.b;
  }
}
