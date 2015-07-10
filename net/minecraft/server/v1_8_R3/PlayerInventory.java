package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

public class PlayerInventory
  implements IInventory
{
  public ItemStack[] items = new ItemStack[36];
  public ItemStack[] armor = new ItemStack[4];
  public int itemInHandIndex;
  public EntityHuman player;
  private ItemStack f;
  public boolean e;
  public List<HumanEntity> transaction = new ArrayList();
  private int maxStack = 64;
  
  public ItemStack[] getContents()
  {
    return this.items;
  }
  
  public ItemStack[] getArmorContents()
  {
    return this.armor;
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
  
  public InventoryHolder getOwner()
  {
    return this.player.getBukkitEntity();
  }
  
  public void setMaxStackSize(int size)
  {
    this.maxStack = size;
  }
  
  public PlayerInventory(EntityHuman entityhuman)
  {
    this.player = entityhuman;
  }
  
  public ItemStack getItemInHand()
  {
    return (this.itemInHandIndex < 9) && (this.itemInHandIndex >= 0) ? this.items[this.itemInHandIndex] : null;
  }
  
  public static int getHotbarSize()
  {
    return 9;
  }
  
  private int c(Item item)
  {
    for (int i = 0; i < this.items.length; i++) {
      if ((this.items[i] != null) && (this.items[i].getItem() == item)) {
        return i;
      }
    }
    return -1;
  }
  
  private int firstPartial(ItemStack itemstack)
  {
    for (int i = 0; i < this.items.length; i++) {
      if ((this.items[i] != null) && (this.items[i].getItem() == itemstack.getItem()) && (this.items[i].isStackable()) && (this.items[i].count < this.items[i].getMaxStackSize()) && (this.items[i].count < getMaxStackSize()) && ((!this.items[i].usesData()) || (this.items[i].getData() == itemstack.getData())) && (ItemStack.equals(this.items[i], itemstack))) {
        return i;
      }
    }
    return -1;
  }
  
  public int canHold(ItemStack itemstack)
  {
    int remains = itemstack.count;
    for (int i = 0; i < this.items.length; i++)
    {
      if (this.items[i] == null) {
        return itemstack.count;
      }
      if ((this.items[i] != null) && (this.items[i].getItem() == itemstack.getItem()) && (this.items[i].isStackable()) && (this.items[i].count < this.items[i].getMaxStackSize()) && (this.items[i].count < getMaxStackSize()) && ((!this.items[i].usesData()) || (this.items[i].getData() == itemstack.getData())) && (ItemStack.equals(this.items[i], itemstack))) {
        remains -= (this.items[i].getMaxStackSize() < getMaxStackSize() ? this.items[i].getMaxStackSize() : getMaxStackSize()) - this.items[i].count;
      }
      if (remains <= 0) {
        return itemstack.count;
      }
    }
    return itemstack.count - remains;
  }
  
  public int getFirstEmptySlotIndex()
  {
    for (int i = 0; i < this.items.length; i++) {
      if (this.items[i] == null) {
        return i;
      }
    }
    return -1;
  }
  
  public int a(Item item, int i, int j, NBTTagCompound nbttagcompound)
  {
    int k = 0;
    for (int l = 0; l < this.items.length; l++)
    {
      ItemStack itemstack = this.items[l];
      if ((itemstack != null) && ((item == null) || (itemstack.getItem() == item)) && ((i <= -1) || (itemstack.getData() == i)) && ((nbttagcompound == null) || (GameProfileSerializer.a(nbttagcompound, itemstack.getTag(), true))))
      {
        int i1 = j <= 0 ? itemstack.count : Math.min(j - k, itemstack.count);
        k += i1;
        if (j != 0)
        {
          this.items[l].count -= i1;
          if (this.items[l].count == 0) {
            this.items[l] = null;
          }
          if ((j > 0) && (k >= j)) {
            return k;
          }
        }
      }
    }
    for (l = 0; l < this.armor.length; l++)
    {
      ItemStack itemstack = this.armor[l];
      if ((itemstack != null) && ((item == null) || (itemstack.getItem() == item)) && ((i <= -1) || (itemstack.getData() == i)) && ((nbttagcompound == null) || (GameProfileSerializer.a(nbttagcompound, itemstack.getTag(), false))))
      {
        int i1 = j <= 0 ? itemstack.count : Math.min(j - k, itemstack.count);
        k += i1;
        if (j != 0)
        {
          this.armor[l].count -= i1;
          if (this.armor[l].count == 0) {
            this.armor[l] = null;
          }
          if ((j > 0) && (k >= j)) {
            return k;
          }
        }
      }
    }
    if (this.f != null)
    {
      if ((item != null) && (this.f.getItem() != item)) {
        return k;
      }
      if ((i > -1) && (this.f.getData() != i)) {
        return k;
      }
      if ((nbttagcompound != null) && (!GameProfileSerializer.a(nbttagcompound, this.f.getTag(), false))) {
        return k;
      }
      l = j <= 0 ? this.f.count : Math.min(j - k, this.f.count);
      k += l;
      if (j != 0)
      {
        this.f.count -= l;
        if (this.f.count == 0) {
          this.f = null;
        }
        if ((j > 0) && (k >= j)) {
          return k;
        }
      }
    }
    return k;
  }
  
  private int e(ItemStack itemstack)
  {
    Item item = itemstack.getItem();
    int i = itemstack.count;
    int j = firstPartial(itemstack);
    if (j < 0) {
      j = getFirstEmptySlotIndex();
    }
    if (j < 0) {
      return i;
    }
    if (this.items[j] == null)
    {
      this.items[j] = new ItemStack(item, 0, itemstack.getData());
      if (itemstack.hasTag()) {
        this.items[j].setTag((NBTTagCompound)itemstack.getTag().clone());
      }
    }
    int k = i;
    if (i > this.items[j].getMaxStackSize() - this.items[j].count) {
      k = this.items[j].getMaxStackSize() - this.items[j].count;
    }
    if (k > getMaxStackSize() - this.items[j].count) {
      k = getMaxStackSize() - this.items[j].count;
    }
    if (k == 0) {
      return i;
    }
    i -= k;
    this.items[j].count += k;
    this.items[j].c = 5;
    return i;
  }
  
  public void k()
  {
    for (int i = 0; i < this.items.length; i++) {
      if (this.items[i] != null) {
        this.items[i].a(this.player.world, this.player, i, this.itemInHandIndex == i);
      }
    }
  }
  
  public boolean a(Item item)
  {
    int i = c(item);
    if (i < 0) {
      return false;
    }
    if (--this.items[i].count <= 0) {
      this.items[i] = null;
    }
    return true;
  }
  
  public boolean b(Item item)
  {
    int i = c(item);
    
    return i >= 0;
  }
  
  public boolean pickup(final ItemStack itemstack)
  {
    if ((itemstack != null) && (itemstack.count != 0) && (itemstack.getItem() != null)) {
      try
      {
        if (itemstack.g())
        {
          int i = getFirstEmptySlotIndex();
          if (i >= 0)
          {
            this.items[i] = ItemStack.b(itemstack);
            this.items[i].c = 5;
            itemstack.count = 0;
            return true;
          }
          if (this.player.abilities.canInstantlyBuild)
          {
            itemstack.count = 0;
            return true;
          }
          return false;
        }
        int i;
        do
        {
          i = itemstack.count;
          itemstack.count = e(itemstack);
        } while ((itemstack.count > 0) && (itemstack.count < i));
        if ((itemstack.count == i) && (this.player.abilities.canInstantlyBuild))
        {
          itemstack.count = 0;
          return true;
        }
        return itemstack.count < i;
      }
      catch (Throwable throwable)
      {
        CrashReport crashreport = CrashReport.a(throwable, "Adding item to inventory");
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Item being added");
        
        crashreportsystemdetails.a("Item ID", Integer.valueOf(Item.getId(itemstack.getItem())));
        crashreportsystemdetails.a("Item data", Integer.valueOf(itemstack.getData()));
        crashreportsystemdetails.a("Item name", new Callable()
        {
          public String a()
            throws Exception
          {
            return itemstack.getName();
          }
          
          public Object call()
            throws Exception
          {
            return a();
          }
        });
        throw new ReportedException(crashreport);
      }
    }
    return false;
  }
  
  public ItemStack splitStack(int i, int j)
  {
    ItemStack[] aitemstack = this.items;
    if (i >= this.items.length)
    {
      aitemstack = this.armor;
      i -= this.items.length;
    }
    if (aitemstack[i] != null)
    {
      if (aitemstack[i].count <= j)
      {
        ItemStack itemstack = aitemstack[i];
        aitemstack[i] = null;
        return itemstack;
      }
      ItemStack itemstack = aitemstack[i].a(j);
      if (aitemstack[i].count == 0) {
        aitemstack[i] = null;
      }
      return itemstack;
    }
    return null;
  }
  
  public ItemStack splitWithoutUpdate(int i)
  {
    ItemStack[] aitemstack = this.items;
    if (i >= this.items.length)
    {
      aitemstack = this.armor;
      i -= this.items.length;
    }
    if (aitemstack[i] != null)
    {
      ItemStack itemstack = aitemstack[i];
      
      aitemstack[i] = null;
      return itemstack;
    }
    return null;
  }
  
  public void setItem(int i, ItemStack itemstack)
  {
    ItemStack[] aitemstack = this.items;
    if (i >= aitemstack.length)
    {
      i -= aitemstack.length;
      aitemstack = this.armor;
    }
    aitemstack[i] = itemstack;
  }
  
  public float a(Block block)
  {
    float f = 1.0F;
    if (this.items[this.itemInHandIndex] != null) {
      f *= this.items[this.itemInHandIndex].a(block);
    }
    return f;
  }
  
  public NBTTagList a(NBTTagList nbttaglist)
  {
    for (int i = 0; i < this.items.length; i++) {
      if (this.items[i] != null)
      {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setByte("Slot", (byte)i);
        this.items[i].save(nbttagcompound);
        nbttaglist.add(nbttagcompound);
      }
    }
    for (i = 0; i < this.armor.length; i++) {
      if (this.armor[i] != null)
      {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setByte("Slot", (byte)(i + 100));
        this.armor[i].save(nbttagcompound);
        nbttaglist.add(nbttagcompound);
      }
    }
    return nbttaglist;
  }
  
  public void b(NBTTagList nbttaglist)
  {
    this.items = new ItemStack[36];
    this.armor = new ItemStack[4];
    for (int i = 0; i < nbttaglist.size(); i++)
    {
      NBTTagCompound nbttagcompound = nbttaglist.get(i);
      int j = nbttagcompound.getByte("Slot") & 0xFF;
      ItemStack itemstack = ItemStack.createStack(nbttagcompound);
      if (itemstack != null)
      {
        if ((j >= 0) && (j < this.items.length)) {
          this.items[j] = itemstack;
        }
        if ((j >= 100) && (j < this.armor.length + 100)) {
          this.armor[(j - 100)] = itemstack;
        }
      }
    }
  }
  
  public int getSize()
  {
    return this.items.length + 4;
  }
  
  public ItemStack getItem(int i)
  {
    ItemStack[] aitemstack = this.items;
    if (i >= aitemstack.length)
    {
      i -= aitemstack.length;
      aitemstack = this.armor;
    }
    return aitemstack[i];
  }
  
  public String getName()
  {
    return "container.inventory";
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
  
  public boolean b(Block block)
  {
    if (block.getMaterial().isAlwaysDestroyable()) {
      return true;
    }
    ItemStack itemstack = getItem(this.itemInHandIndex);
    
    return itemstack != null ? itemstack.b(block) : false;
  }
  
  public ItemStack e(int i)
  {
    return this.armor[i];
  }
  
  public int m()
  {
    int i = 0;
    for (int j = 0; j < this.armor.length; j++) {
      if ((this.armor[j] != null) && ((this.armor[j].getItem() instanceof ItemArmor)))
      {
        int k = ((ItemArmor)this.armor[j].getItem()).c;
        
        i += k;
      }
    }
    return i;
  }
  
  public void a(float f)
  {
    f /= 4.0F;
    if (f < 1.0F) {
      f = 1.0F;
    }
    for (int i = 0; i < this.armor.length; i++) {
      if ((this.armor[i] != null) && ((this.armor[i].getItem() instanceof ItemArmor)))
      {
        this.armor[i].damage((int)f, this.player);
        if (this.armor[i].count == 0) {
          this.armor[i] = null;
        }
      }
    }
  }
  
  public void n()
  {
    for (int i = 0; i < this.items.length; i++) {
      if (this.items[i] != null)
      {
        this.player.a(this.items[i], true, false);
        this.items[i] = null;
      }
    }
    for (i = 0; i < this.armor.length; i++) {
      if (this.armor[i] != null)
      {
        this.player.a(this.armor[i], true, false);
        this.armor[i] = null;
      }
    }
  }
  
  public void update()
  {
    this.e = true;
  }
  
  public void setCarried(ItemStack itemstack)
  {
    this.f = itemstack;
  }
  
  public ItemStack getCarried()
  {
    if ((this.f != null) && (this.f.count == 0)) {
      setCarried(null);
    }
    return this.f;
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return !this.player.dead;
  }
  
  public boolean c(ItemStack itemstack)
  {
    for (int i = 0; i < this.armor.length; i++) {
      if ((this.armor[i] != null) && (this.armor[i].doMaterialsMatch(itemstack))) {
        return true;
      }
    }
    for (i = 0; i < this.items.length; i++) {
      if ((this.items[i] != null) && (this.items[i].doMaterialsMatch(itemstack))) {
        return true;
      }
    }
    return false;
  }
  
  public void startOpen(EntityHuman entityhuman) {}
  
  public void closeContainer(EntityHuman entityhuman) {}
  
  public boolean b(int i, ItemStack itemstack)
  {
    return true;
  }
  
  public void b(PlayerInventory playerinventory)
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i] = ItemStack.b(playerinventory.items[i]);
    }
    for (i = 0; i < this.armor.length; i++) {
      this.armor[i] = ItemStack.b(playerinventory.armor[i]);
    }
    this.itemInHandIndex = playerinventory.itemInHandIndex;
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
    for (i = 0; i < this.armor.length; i++) {
      this.armor[i] = null;
    }
  }
}
