package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.HumanEntity;

public class TileEntityChest
  extends TileEntityContainer
  implements IUpdatePlayerListBox, IInventory
{
  private ItemStack[] items = new ItemStack[27];
  public boolean a;
  public TileEntityChest f;
  public TileEntityChest g;
  public TileEntityChest h;
  public TileEntityChest i;
  public float j;
  public float k;
  public int l;
  private int n;
  private int o = -1;
  private String p;
  public List<HumanEntity> transaction = new ArrayList();
  private int maxStack = 64;
  
  public ItemStack[] getContents()
  {
    return this.items;
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
  
  public void setMaxStackSize(int size)
  {
    this.maxStack = size;
  }
  
  public int getSize()
  {
    return 27;
  }
  
  public ItemStack getItem(int i)
  {
    return this.items[i];
  }
  
  public ItemStack splitStack(int i, int j)
  {
    if (this.items[i] != null)
    {
      if (this.items[i].count <= j)
      {
        ItemStack itemstack = this.items[i];
        this.items[i] = null;
        update();
        return itemstack;
      }
      ItemStack itemstack = this.items[i].a(j);
      if (this.items[i].count == 0) {
        this.items[i] = null;
      }
      update();
      return itemstack;
    }
    return null;
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
  
  public void setItem(int i, ItemStack itemstack)
  {
    this.items[i] = itemstack;
    if ((itemstack != null) && (itemstack.count > getMaxStackSize())) {
      itemstack.count = getMaxStackSize();
    }
    update();
  }
  
  public String getName()
  {
    return hasCustomName() ? this.p : "container.chest";
  }
  
  public boolean hasCustomName()
  {
    return (this.p != null) && (this.p.length() > 0);
  }
  
  public void a(String s)
  {
    this.p = s;
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    NBTTagList nbttaglist = nbttagcompound.getList("Items", 10);
    
    this.items = new ItemStack[getSize()];
    if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
      this.p = nbttagcompound.getString("CustomName");
    }
    for (int i = 0; i < nbttaglist.size(); i++)
    {
      NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
      int j = nbttagcompound1.getByte("Slot") & 0xFF;
      if ((j >= 0) && (j < this.items.length)) {
        this.items[j] = ItemStack.createStack(nbttagcompound1);
      }
    }
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < this.items.length; i++) {
      if (this.items[i] != null)
      {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        
        nbttagcompound1.setByte("Slot", (byte)i);
        this.items[i].save(nbttagcompound1);
        nbttaglist.add(nbttagcompound1);
      }
    }
    nbttagcompound.set("Items", nbttaglist);
    if (hasCustomName()) {
      nbttagcompound.setString("CustomName", this.p);
    }
  }
  
  public int getMaxStackSize()
  {
    return this.maxStack;
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    if (this.world == null) {
      return true;
    }
    return this.world.getTileEntity(this.position) == this;
  }
  
  public void E()
  {
    super.E();
    this.a = false;
  }
  
  private void a(TileEntityChest tileentitychest, EnumDirection enumdirection)
  {
    if (tileentitychest.x()) {
      this.a = false;
    } else if (this.a) {
      switch (SyntheticClass_1.a[enumdirection.ordinal()])
      {
      case 1: 
        if (this.f != tileentitychest) {
          this.a = false;
        }
        break;
      case 2: 
        if (this.i != tileentitychest) {
          this.a = false;
        }
        break;
      case 3: 
        if (this.g != tileentitychest) {
          this.a = false;
        }
        break;
      case 4: 
        if (this.h != tileentitychest) {
          this.a = false;
        }
        break;
      }
    }
  }
  
  public void m()
  {
    if (!this.a)
    {
      this.a = true;
      this.h = a(EnumDirection.WEST);
      this.g = a(EnumDirection.EAST);
      this.f = a(EnumDirection.NORTH);
      this.i = a(EnumDirection.SOUTH);
    }
  }
  
  protected TileEntityChest a(EnumDirection enumdirection)
  {
    BlockPosition blockposition = this.position.shift(enumdirection);
    if (b(blockposition))
    {
      TileEntity tileentity = this.world.getTileEntity(blockposition);
      if ((tileentity instanceof TileEntityChest))
      {
        TileEntityChest tileentitychest = (TileEntityChest)tileentity;
        
        tileentitychest.a(this, enumdirection.opposite());
        return tileentitychest;
      }
    }
    return null;
  }
  
  private boolean b(BlockPosition blockposition)
  {
    if (this.world == null) {
      return false;
    }
    Block block = this.world.getType(blockposition).getBlock();
    
    return ((block instanceof BlockChest)) && (((BlockChest)block).b == n());
  }
  
  public void c()
  {
    m();
    int i = this.position.getX();
    int j = this.position.getY();
    int k = this.position.getZ();
    
    this.n += 1;
    if ((!this.world.isClientSide) && (this.l != 0) && ((this.n + i + j + k) % 200 == 0))
    {
      this.l = 0;
      float f = 5.0F;
      List list = this.world.a(EntityHuman.class, new AxisAlignedBB(i - f, j - f, k - f, i + 1 + f, j + 1 + f, k + 1 + f));
      Iterator iterator = list.iterator();
      while (iterator.hasNext())
      {
        EntityHuman entityhuman = (EntityHuman)iterator.next();
        if ((entityhuman.activeContainer instanceof ContainerChest))
        {
          IInventory iinventory = ((ContainerChest)entityhuman.activeContainer).e();
          if ((iinventory == this) || (((iinventory instanceof InventoryLargeChest)) && (((InventoryLargeChest)iinventory).a(this)))) {
            this.l += 1;
          }
        }
      }
    }
    this.k = this.j;
    float f = 0.1F;
    if ((this.l > 0) && (this.j == 0.0F) && (this.f == null) && (this.h == null))
    {
      double d1 = i + 0.5D;
      
      double d0 = k + 0.5D;
      if (this.i != null) {
        d0 += 0.5D;
      }
      if (this.g != null) {
        d1 += 0.5D;
      }
      this.world.makeSound(d1, j + 0.5D, d0, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
    if (((this.l == 0) && (this.j > 0.0F)) || ((this.l > 0) && (this.j < 1.0F)))
    {
      float f1 = this.j;
      if (this.l > 0) {
        this.j += f;
      } else {
        this.j -= f;
      }
      if (this.j > 1.0F) {
        this.j = 1.0F;
      }
      float f2 = 0.5F;
      if ((this.j < f2) && (f1 >= f2) && (this.f == null) && (this.h == null))
      {
        double d0 = i + 0.5D;
        double d2 = k + 0.5D;
        if (this.i != null) {
          d2 += 0.5D;
        }
        if (this.g != null) {
          d0 += 0.5D;
        }
        this.world.makeSound(d0, j + 0.5D, d2, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
      }
      if (this.j < 0.0F) {
        this.j = 0.0F;
      }
    }
  }
  
  public boolean c(int i, int j)
  {
    if (i == 1)
    {
      this.l = j;
      return true;
    }
    return super.c(i, j);
  }
  
  public void startOpen(EntityHuman entityhuman)
  {
    if (!entityhuman.isSpectator())
    {
      if (this.l < 0) {
        this.l = 0;
      }
      int oldPower = Math.max(0, Math.min(15, this.l));
      
      this.l += 1;
      if (this.world == null) {
        return;
      }
      this.world.playBlockAction(this.position, w(), 1, this.l);
      if (w() == Blocks.TRAPPED_CHEST)
      {
        int newPower = Math.max(0, Math.min(15, this.l));
        if (oldPower != newPower) {
          CraftEventFactory.callRedstoneChange(this.world, this.position.getX(), this.position.getY(), this.position.getZ(), oldPower, newPower);
        }
      }
      this.world.applyPhysics(this.position, w());
      this.world.applyPhysics(this.position.down(), w());
    }
  }
  
  public void closeContainer(EntityHuman entityhuman)
  {
    if ((!entityhuman.isSpectator()) && ((w() instanceof BlockChest)))
    {
      int oldPower = Math.max(0, Math.min(15, this.l));
      this.l -= 1;
      if (this.world == null) {
        return;
      }
      this.world.playBlockAction(this.position, w(), 1, this.l);
      if (w() == Blocks.TRAPPED_CHEST)
      {
        int newPower = Math.max(0, Math.min(15, this.l));
        if (oldPower != newPower) {
          CraftEventFactory.callRedstoneChange(this.world, this.position.getX(), this.position.getY(), this.position.getZ(), oldPower, newPower);
        }
      }
      this.world.applyPhysics(this.position, w());
      this.world.applyPhysics(this.position.down(), w());
    }
  }
  
  public boolean b(int i, ItemStack itemstack)
  {
    return true;
  }
  
  public void y()
  {
    super.y();
    E();
    m();
  }
  
  public int n()
  {
    if (this.o == -1)
    {
      if ((this.world == null) || (!(w() instanceof BlockChest))) {
        return 0;
      }
      this.o = ((BlockChest)w()).b;
    }
    return this.o;
  }
  
  public String getContainerName()
  {
    return "minecraft:chest";
  }
  
  public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman)
  {
    return new ContainerChest(playerinventory, this, entityhuman);
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
  
  public boolean F()
  {
    return true;
  }
  
  static class SyntheticClass_1
  {
    static final int[] a = new int[EnumDirection.values().length];
    
    static
    {
      try
      {
        a[EnumDirection.NORTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        a[EnumDirection.SOUTH.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        a[EnumDirection.EAST.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        a[EnumDirection.WEST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
    }
  }
}
