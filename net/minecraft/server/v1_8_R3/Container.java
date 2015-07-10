package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginManager;

public abstract class Container
{
  public List<ItemStack> b = Lists.newArrayList();
  public List<Slot> c = Lists.newArrayList();
  public int windowId;
  private int dragType = -1;
  private int g;
  private final Set<Slot> h = Sets.newHashSet();
  protected List<ICrafting> listeners = Lists.newArrayList();
  private Set<EntityHuman> i = Sets.newHashSet();
  private int tickCount;
  public boolean checkReachable = true;
  
  public abstract InventoryView getBukkitView();
  
  public void transferTo(Container other, CraftHumanEntity player)
  {
    InventoryView source = getBukkitView();InventoryView destination = other.getBukkitView();
    ((CraftInventory)source.getTopInventory()).getInventory().onClose(player);
    ((CraftInventory)source.getBottomInventory()).getInventory().onClose(player);
    ((CraftInventory)destination.getTopInventory()).getInventory().onOpen(player);
    ((CraftInventory)destination.getBottomInventory()).getInventory().onOpen(player);
  }
  
  protected Slot a(Slot slot)
  {
    slot.rawSlotIndex = this.c.size();
    this.c.add(slot);
    this.b.add(null);
    return slot;
  }
  
  public void addSlotListener(ICrafting icrafting)
  {
    if (this.listeners.contains(icrafting)) {
      throw new IllegalArgumentException("Listener already listening");
    }
    this.listeners.add(icrafting);
    icrafting.a(this, a());
    b();
  }
  
  public List<ItemStack> a()
  {
    ArrayList arraylist = Lists.newArrayList();
    for (int i = 0; i < this.c.size(); i++) {
      arraylist.add(((Slot)this.c.get(i)).getItem());
    }
    return arraylist;
  }
  
  public void b()
  {
    for (int i = 0; i < this.c.size(); i++)
    {
      ItemStack itemstack = ((Slot)this.c.get(i)).getItem();
      ItemStack itemstack1 = (ItemStack)this.b.get(i);
      if ((!ItemStack.fastMatches(itemstack1, itemstack)) || ((this.tickCount % 20 == 0) && (!ItemStack.matches(itemstack1, itemstack))))
      {
        itemstack1 = itemstack == null ? null : itemstack.cloneItemStack();
        this.b.set(i, itemstack1);
        for (int j = 0; j < this.listeners.size(); j++) {
          ((ICrafting)this.listeners.get(j)).a(this, i, itemstack1);
        }
      }
    }
    this.tickCount += 1;
  }
  
  public boolean a(EntityHuman entityhuman, int i)
  {
    return false;
  }
  
  public Slot getSlot(IInventory iinventory, int i)
  {
    for (int j = 0; j < this.c.size(); j++)
    {
      Slot slot = (Slot)this.c.get(j);
      if (slot.a(iinventory, i)) {
        return slot;
      }
    }
    return null;
  }
  
  public Slot getSlot(int i)
  {
    return (Slot)this.c.get(i);
  }
  
  public ItemStack b(EntityHuman entityhuman, int i)
  {
    Slot slot = (Slot)this.c.get(i);
    
    return slot != null ? slot.getItem() : null;
  }
  
  public ItemStack clickItem(int i, int j, int k, EntityHuman entityhuman)
  {
    ItemStack itemstack = null;
    PlayerInventory playerinventory = entityhuman.inventory;
    if (k == 5)
    {
      int i1 = this.g;
      
      this.g = c(j);
      if (((i1 != 1) || (this.g != 2)) && (i1 != this.g))
      {
        d();
      }
      else if (playerinventory.getCarried() == null)
      {
        d();
      }
      else if (this.g == 0)
      {
        this.dragType = b(j);
        if (a(this.dragType, entityhuman))
        {
          this.g = 1;
          this.h.clear();
        }
        else
        {
          d();
        }
      }
      else if (this.g == 1)
      {
        Slot slot = (Slot)this.c.get(i);
        if ((slot != null) && (a(slot, playerinventory.getCarried(), true)) && (slot.isAllowed(playerinventory.getCarried())) && (playerinventory.getCarried().count > this.h.size()) && (b(slot))) {
          this.h.add(slot);
        }
      }
      else if (this.g == 2)
      {
        if (!this.h.isEmpty())
        {
          ItemStack itemstack1 = playerinventory.getCarried().cloneItemStack();
          int l = playerinventory.getCarried().count;
          Iterator iterator = this.h.iterator();
          
          Map<Integer, ItemStack> draggedSlots = new HashMap();
          while (iterator.hasNext())
          {
            Slot slot1 = (Slot)iterator.next();
            if ((slot1 != null) && (a(slot1, playerinventory.getCarried(), true)) && (slot1.isAllowed(playerinventory.getCarried())) && (playerinventory.getCarried().count >= this.h.size()) && (b(slot1)))
            {
              ItemStack itemstack2 = itemstack1.cloneItemStack();
              int j1 = slot1.hasItem() ? slot1.getItem().count : 0;
              
              a(this.h, this.dragType, itemstack2, j1);
              if (itemstack2.count > itemstack2.getMaxStackSize()) {
                itemstack2.count = itemstack2.getMaxStackSize();
              }
              if (itemstack2.count > slot1.getMaxStackSize(itemstack2)) {
                itemstack2.count = slot1.getMaxStackSize(itemstack2);
              }
              l -= itemstack2.count - j1;
              
              draggedSlots.put(Integer.valueOf(slot1.rawSlotIndex), itemstack2);
            }
          }
          InventoryView view = getBukkitView();
          org.bukkit.inventory.ItemStack newcursor = CraftItemStack.asCraftMirror(itemstack1);
          newcursor.setAmount(l);
          Map<Integer, org.bukkit.inventory.ItemStack> eventmap = new HashMap();
          for (Map.Entry<Integer, ItemStack> ditem : draggedSlots.entrySet()) {
            eventmap.put((Integer)ditem.getKey(), CraftItemStack.asBukkitCopy((ItemStack)ditem.getValue()));
          }
          ItemStack oldCursor = playerinventory.getCarried();
          playerinventory.setCarried(CraftItemStack.asNMSCopy(newcursor));
          
          InventoryDragEvent event = new InventoryDragEvent(view, newcursor.getType() != Material.AIR ? newcursor : null, CraftItemStack.asBukkitCopy(oldCursor), this.dragType == 1, eventmap);
          entityhuman.world.getServer().getPluginManager().callEvent(event);
          
          boolean needsUpdate = event.getResult() != Event.Result.DEFAULT;
          if (event.getResult() != Event.Result.DENY)
          {
            for (Map.Entry<Integer, ItemStack> dslot : draggedSlots.entrySet()) {
              view.setItem(((Integer)dslot.getKey()).intValue(), CraftItemStack.asBukkitCopy((ItemStack)dslot.getValue()));
            }
            if (playerinventory.getCarried() != null)
            {
              playerinventory.setCarried(CraftItemStack.asNMSCopy(event.getCursor()));
              needsUpdate = true;
            }
          }
          else
          {
            playerinventory.setCarried(oldCursor);
          }
          if ((needsUpdate) && ((entityhuman instanceof EntityPlayer))) {
            ((EntityPlayer)entityhuman).updateInventory(this);
          }
        }
        d();
      }
      else
      {
        d();
      }
    }
    else if (this.g != 0)
    {
      d();
    }
    else if (((k == 0) || (k == 1)) && ((j == 0) || (j == 1)))
    {
      if (i == 64537)
      {
        if (playerinventory.getCarried() != null)
        {
          if (j == 0)
          {
            entityhuman.drop(playerinventory.getCarried(), true);
            playerinventory.setCarried(null);
          }
          if (j == 1)
          {
            ItemStack itemstack4 = playerinventory.getCarried();
            if (itemstack4.count > 0) {
              entityhuman.drop(itemstack4.a(1), true);
            }
            if (itemstack4.count == 0) {
              playerinventory.setCarried(null);
            }
          }
        }
      }
      else if (k == 1)
      {
        if (i < 0) {
          return null;
        }
        Slot slot2 = (Slot)this.c.get(i);
        if ((slot2 != null) && (slot2.isAllowed(entityhuman)))
        {
          ItemStack itemstack1 = b(entityhuman, i);
          if (itemstack1 != null)
          {
            Item item = itemstack1.getItem();
            
            itemstack = itemstack1.cloneItemStack();
            if ((slot2.getItem() != null) && (slot2.getItem().getItem() == item)) {
              a(i, j, true, entityhuman);
            }
          }
        }
      }
      else
      {
        if (i < 0) {
          return null;
        }
        Slot slot2 = (Slot)this.c.get(i);
        if (slot2 != null)
        {
          ItemStack itemstack1 = slot2.getItem();
          ItemStack itemstack4 = playerinventory.getCarried();
          if (itemstack1 != null) {
            itemstack = itemstack1.cloneItemStack();
          }
          if (itemstack1 == null)
          {
            if ((itemstack4 != null) && (slot2.isAllowed(itemstack4)))
            {
              int k1 = j == 0 ? itemstack4.count : 1;
              if (k1 > slot2.getMaxStackSize(itemstack4)) {
                k1 = slot2.getMaxStackSize(itemstack4);
              }
              if (itemstack4.count >= k1) {
                slot2.set(itemstack4.a(k1));
              }
              if (itemstack4.count == 0) {
                playerinventory.setCarried(null);
              } else if ((entityhuman instanceof EntityPlayer)) {
                ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, entityhuman.inventory.getCarried()));
              }
            }
          }
          else if (slot2.isAllowed(entityhuman)) {
            if (itemstack4 == null)
            {
              int k1 = j == 0 ? itemstack1.count : (itemstack1.count + 1) / 2;
              ItemStack itemstack3 = slot2.a(k1);
              playerinventory.setCarried(itemstack3);
              if (itemstack1.count == 0) {
                slot2.set(null);
              }
              slot2.a(entityhuman, playerinventory.getCarried());
            }
            else if (slot2.isAllowed(itemstack4))
            {
              if ((itemstack1.getItem() == itemstack4.getItem()) && (itemstack1.getData() == itemstack4.getData()) && (ItemStack.equals(itemstack1, itemstack4)))
              {
                int k1 = j == 0 ? itemstack4.count : 1;
                if (k1 > slot2.getMaxStackSize(itemstack4) - itemstack1.count) {
                  k1 = slot2.getMaxStackSize(itemstack4) - itemstack1.count;
                }
                if (k1 > itemstack4.getMaxStackSize() - itemstack1.count) {
                  k1 = itemstack4.getMaxStackSize() - itemstack1.count;
                }
                itemstack4.a(k1);
                if (itemstack4.count == 0) {
                  playerinventory.setCarried(null);
                } else if ((entityhuman instanceof EntityPlayer)) {
                  ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, entityhuman.inventory.getCarried()));
                }
                itemstack1.count += k1;
              }
              else if (itemstack4.count <= slot2.getMaxStackSize(itemstack4))
              {
                slot2.set(itemstack4);
                playerinventory.setCarried(itemstack1);
              }
            }
            else if ((itemstack1.getItem() == itemstack4.getItem()) && (itemstack4.getMaxStackSize() > 1) && ((!itemstack1.usesData()) || (itemstack1.getData() == itemstack4.getData())) && (ItemStack.equals(itemstack1, itemstack4)))
            {
              int k1 = itemstack1.count;
              
              int maxStack = Math.min(itemstack4.getMaxStackSize(), slot2.getMaxStackSize());
              if ((k1 > 0) && (k1 + itemstack4.count <= maxStack))
              {
                itemstack4.count += k1;
                itemstack1 = slot2.a(k1);
                if (itemstack1.count == 0) {
                  slot2.set(null);
                }
                slot2.a(entityhuman, playerinventory.getCarried());
              }
              else if ((entityhuman instanceof EntityPlayer))
              {
                ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, entityhuman.inventory.getCarried()));
              }
            }
          }
          slot2.f();
          if (((entityhuman instanceof EntityPlayer)) && (slot2.getMaxStackSize() != 64))
          {
            ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutSetSlot(this.windowId, slot2.rawSlotIndex, slot2.getItem()));
            if ((getBukkitView().getType() == InventoryType.WORKBENCH) || (getBukkitView().getType() == InventoryType.CRAFTING)) {
              ((EntityPlayer)entityhuman).playerConnection.sendPacket(new PacketPlayOutSetSlot(this.windowId, 0, getSlot(0).getItem()));
            }
          }
        }
      }
    }
    else if ((k == 2) && (j >= 0) && (j < 9))
    {
      Slot slot2 = (Slot)this.c.get(i);
      if (slot2.isAllowed(entityhuman))
      {
        ItemStack itemstack1 = playerinventory.getItem(j);
        boolean flag = (itemstack1 == null) || ((slot2.inventory == playerinventory) && (slot2.isAllowed(itemstack1)));
        
        int k1 = -1;
        if (!flag)
        {
          k1 = playerinventory.getFirstEmptySlotIndex();
          flag |= k1 > -1;
        }
        if ((slot2.hasItem()) && (flag))
        {
          ItemStack itemstack3 = slot2.getItem();
          playerinventory.setItem(j, itemstack3.cloneItemStack());
          if (((slot2.inventory != playerinventory) || (!slot2.isAllowed(itemstack1))) && (itemstack1 != null))
          {
            if (k1 > -1)
            {
              playerinventory.pickup(itemstack1);
              slot2.a(itemstack3.count);
              slot2.set(null);
              slot2.a(entityhuman, itemstack3);
            }
          }
          else
          {
            slot2.a(itemstack3.count);
            slot2.set(itemstack1);
            slot2.a(entityhuman, itemstack3);
          }
        }
        else if ((!slot2.hasItem()) && (itemstack1 != null) && (slot2.isAllowed(itemstack1)))
        {
          playerinventory.setItem(j, null);
          slot2.set(itemstack1);
        }
      }
    }
    else if ((k == 3) && (entityhuman.abilities.canInstantlyBuild) && (playerinventory.getCarried() == null) && (i >= 0))
    {
      Slot slot2 = (Slot)this.c.get(i);
      if ((slot2 != null) && (slot2.hasItem()))
      {
        ItemStack itemstack1 = slot2.getItem().cloneItemStack();
        itemstack1.count = itemstack1.getMaxStackSize();
        playerinventory.setCarried(itemstack1);
      }
    }
    else if ((k == 4) && (playerinventory.getCarried() == null) && (i >= 0))
    {
      Slot slot2 = (Slot)this.c.get(i);
      if ((slot2 != null) && (slot2.hasItem()) && (slot2.isAllowed(entityhuman)))
      {
        ItemStack itemstack1 = slot2.a(j == 0 ? 1 : slot2.getItem().count);
        slot2.a(entityhuman, itemstack1);
        entityhuman.drop(itemstack1, true);
      }
    }
    else if ((k == 6) && (i >= 0))
    {
      Slot slot2 = (Slot)this.c.get(i);
      ItemStack itemstack1 = playerinventory.getCarried();
      if ((itemstack1 != null) && ((slot2 == null) || (!slot2.hasItem()) || (!slot2.isAllowed(entityhuman))))
      {
        int l = j == 0 ? 0 : this.c.size() - 1;
        int k1 = j == 0 ? 1 : -1;
        for (int l1 = 0; l1 < 2; l1++) {
          for (int i2 = l; (i2 >= 0) && (i2 < this.c.size()) && (itemstack1.count < itemstack1.getMaxStackSize()); i2 += k1)
          {
            Slot slot3 = (Slot)this.c.get(i2);
            if ((slot3.hasItem()) && (a(slot3, itemstack1, true)) && (slot3.isAllowed(entityhuman)) && (a(itemstack1, slot3)) && ((l1 != 0) || (slot3.getItem().count != slot3.getItem().getMaxStackSize())))
            {
              int j2 = Math.min(itemstack1.getMaxStackSize() - itemstack1.count, slot3.getItem().count);
              ItemStack itemstack5 = slot3.a(j2);
              
              itemstack1.count += j2;
              if (itemstack5.count <= 0) {
                slot3.set(null);
              }
              slot3.a(entityhuman, itemstack5);
            }
          }
        }
      }
      b();
    }
    return itemstack;
  }
  
  public boolean a(ItemStack itemstack, Slot slot)
  {
    return true;
  }
  
  protected void a(int i, int j, boolean flag, EntityHuman entityhuman)
  {
    clickItem(i, j, 1, entityhuman);
  }
  
  public void b(EntityHuman entityhuman)
  {
    PlayerInventory playerinventory = entityhuman.inventory;
    if (playerinventory.getCarried() != null)
    {
      entityhuman.drop(playerinventory.getCarried(), false);
      playerinventory.setCarried(null);
    }
  }
  
  public void a(IInventory iinventory)
  {
    b();
  }
  
  public void setItem(int i, ItemStack itemstack)
  {
    getSlot(i).set(itemstack);
  }
  
  public boolean c(EntityHuman entityhuman)
  {
    return !this.i.contains(entityhuman);
  }
  
  public void a(EntityHuman entityhuman, boolean flag)
  {
    if (flag) {
      this.i.remove(entityhuman);
    } else {
      this.i.add(entityhuman);
    }
  }
  
  public abstract boolean a(EntityHuman paramEntityHuman);
  
  protected boolean a(ItemStack itemstack, int i, int j, boolean flag)
  {
    boolean flag1 = false;
    int k = i;
    if (flag) {
      k = j - 1;
    }
    if (itemstack.isStackable()) {
      while ((itemstack.count > 0) && (((!flag) && (k < j)) || ((flag) && (k >= i))))
      {
        Slot slot = (Slot)this.c.get(k);
        ItemStack itemstack1 = slot.getItem();
        if ((itemstack1 != null) && (itemstack1.getItem() == itemstack.getItem()) && ((!itemstack.usesData()) || (itemstack.getData() == itemstack1.getData())) && (ItemStack.equals(itemstack, itemstack1)))
        {
          int l = itemstack1.count + itemstack.count;
          
          int maxStack = Math.min(itemstack.getMaxStackSize(), slot.getMaxStackSize());
          if (l <= maxStack)
          {
            itemstack.count = 0;
            itemstack1.count = l;
            slot.f();
            flag1 = true;
          }
          else if (itemstack1.count < maxStack)
          {
            itemstack.count -= maxStack - itemstack1.count;
            itemstack1.count = maxStack;
            slot.f();
            flag1 = true;
          }
        }
        if (flag) {
          k--;
        } else {
          k++;
        }
      }
    }
    if (itemstack.count > 0)
    {
      if (flag) {
        k = j - 1;
      } else {
        k = i;
      }
      while (((!flag) && (k < j)) || ((flag) && (k >= i)))
      {
        Slot slot = (Slot)this.c.get(k);
        ItemStack itemstack1 = slot.getItem();
        if (itemstack1 == null)
        {
          slot.set(itemstack.cloneItemStack());
          slot.f();
          itemstack.count = 0;
          flag1 = true;
          break;
        }
        if (flag) {
          k--;
        } else {
          k++;
        }
      }
    }
    return flag1;
  }
  
  public static int b(int i)
  {
    return i >> 2 & 0x3;
  }
  
  public static int c(int i)
  {
    return i & 0x3;
  }
  
  public static boolean a(int i, EntityHuman entityhuman)
  {
    return i == 0;
  }
  
  protected void d()
  {
    this.g = 0;
    this.h.clear();
  }
  
  public static boolean a(Slot slot, ItemStack itemstack, boolean flag)
  {
    boolean flag1 = (slot == null) || (!slot.hasItem());
    if ((slot != null) && (slot.hasItem()) && (itemstack != null) && (itemstack.doMaterialsMatch(slot.getItem())) && (ItemStack.equals(slot.getItem(), itemstack))) {
      flag1 |= slot.getItem().count + (flag ? 0 : itemstack.count) <= itemstack.getMaxStackSize();
    }
    return flag1;
  }
  
  public static void a(Set<Slot> set, int i, ItemStack itemstack, int j)
  {
    switch (i)
    {
    case 0: 
      itemstack.count = MathHelper.d(itemstack.count / set.size());
      break;
    case 1: 
      itemstack.count = 1;
      break;
    case 2: 
      itemstack.count = itemstack.getItem().getMaxStackSize();
    }
    ItemStack tmp71_70 = itemstack;
    
    tmp71_70.count = (tmp71_70.count + j);
  }
  
  public boolean b(Slot slot)
  {
    return true;
  }
  
  public static int a(TileEntity tileentity)
  {
    return (tileentity instanceof IInventory) ? b((IInventory)tileentity) : 0;
  }
  
  public static int b(IInventory iinventory)
  {
    if (iinventory == null) {
      return 0;
    }
    int i = 0;
    float f = 0.0F;
    for (int j = 0; j < iinventory.getSize(); j++)
    {
      ItemStack itemstack = iinventory.getItem(j);
      if (itemstack != null)
      {
        f += itemstack.count / Math.min(iinventory.getMaxStackSize(), itemstack.getMaxStackSize());
        i++;
      }
    }
    f /= iinventory.getSize();
    return MathHelper.d(f * 14.0F) + (i > 0 ? 1 : 0);
  }
}
