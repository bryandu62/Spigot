package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

public class InventoryMerchant
  implements IInventory
{
  private final IMerchant merchant;
  private ItemStack[] itemsInSlots = new ItemStack[3];
  private final EntityHuman player;
  private MerchantRecipe recipe;
  private int e;
  public List<HumanEntity> transaction = new ArrayList();
  private int maxStack = 64;
  
  public ItemStack[] getContents()
  {
    return this.itemsInSlots;
  }
  
  public void onOpen(CraftHumanEntity who)
  {
    this.transaction.add(who);
  }
  
  public void onClose(CraftHumanEntity who)
  {
    this.transaction.remove(who);
  }
  
  public List<HumanEntity> getViewers()
  {
    return this.transaction;
  }
  
  public void setMaxStackSize(int i)
  {
    this.maxStack = i;
  }
  
  public InventoryHolder getOwner()
  {
    return (CraftVillager)((EntityVillager)this.merchant).getBukkitEntity();
  }
  
  public InventoryMerchant(EntityHuman entityhuman, IMerchant imerchant)
  {
    this.player = entityhuman;
    this.merchant = imerchant;
  }
  
  public int getSize()
  {
    return this.itemsInSlots.length;
  }
  
  public ItemStack getItem(int i)
  {
    return this.itemsInSlots[i];
  }
  
  public ItemStack splitStack(int i, int j)
  {
    if (this.itemsInSlots[i] != null)
    {
      if (i == 2)
      {
        ItemStack itemstack = this.itemsInSlots[i];
        this.itemsInSlots[i] = null;
        return itemstack;
      }
      if (this.itemsInSlots[i].count <= j)
      {
        ItemStack itemstack = this.itemsInSlots[i];
        this.itemsInSlots[i] = null;
        if (e(i)) {
          h();
        }
        return itemstack;
      }
      ItemStack itemstack = this.itemsInSlots[i].a(j);
      if (this.itemsInSlots[i].count == 0) {
        this.itemsInSlots[i] = null;
      }
      if (e(i)) {
        h();
      }
      return itemstack;
    }
    return null;
  }
  
  private boolean e(int i)
  {
    return (i == 0) || (i == 1);
  }
  
  public ItemStack splitWithoutUpdate(int i)
  {
    if (this.itemsInSlots[i] != null)
    {
      ItemStack itemstack = this.itemsInSlots[i];
      
      this.itemsInSlots[i] = null;
      return itemstack;
    }
    return null;
  }
  
  public void setItem(int i, ItemStack itemstack)
  {
    this.itemsInSlots[i] = itemstack;
    if ((itemstack != null) && (itemstack.count > getMaxStackSize())) {
      itemstack.count = getMaxStackSize();
    }
    if (e(i)) {
      h();
    }
  }
  
  public String getName()
  {
    return "mob.villager";
  }
  
  public boolean hasCustomName()
  {
    return false;
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    return hasCustomName() ? new ChatComponentText(getName()) : new ChatMessage(getName(), new Object[0]);
  }
  
  public int getMaxStackSize()
  {
    return this.maxStack;
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return this.merchant.v_() == entityhuman;
  }
  
  public void startOpen(EntityHuman entityhuman) {}
  
  public void closeContainer(EntityHuman entityhuman) {}
  
  public boolean b(int i, ItemStack itemstack)
  {
    return true;
  }
  
  public void update()
  {
    h();
  }
  
  public void h()
  {
    this.recipe = null;
    ItemStack itemstack = this.itemsInSlots[0];
    ItemStack itemstack1 = this.itemsInSlots[1];
    if (itemstack == null)
    {
      itemstack = itemstack1;
      itemstack1 = null;
    }
    if (itemstack == null)
    {
      setItem(2, null);
    }
    else
    {
      MerchantRecipeList merchantrecipelist = this.merchant.getOffers(this.player);
      if (merchantrecipelist != null)
      {
        MerchantRecipe merchantrecipe = merchantrecipelist.a(itemstack, itemstack1, this.e);
        if ((merchantrecipe != null) && (!merchantrecipe.h()))
        {
          this.recipe = merchantrecipe;
          setItem(2, merchantrecipe.getBuyItem3().cloneItemStack());
        }
        else if (itemstack1 != null)
        {
          merchantrecipe = merchantrecipelist.a(itemstack1, itemstack, this.e);
          if ((merchantrecipe != null) && (!merchantrecipe.h()))
          {
            this.recipe = merchantrecipe;
            setItem(2, merchantrecipe.getBuyItem3().cloneItemStack());
          }
          else
          {
            setItem(2, null);
          }
        }
        else
        {
          setItem(2, null);
        }
      }
    }
    this.merchant.a_(getItem(2));
  }
  
  public MerchantRecipe getRecipe()
  {
    return this.recipe;
  }
  
  public void d(int i)
  {
    this.e = i;
    h();
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
    for (int i = 0; i < this.itemsInSlots.length; i++) {
      this.itemsInSlots[i] = null;
    }
  }
}
