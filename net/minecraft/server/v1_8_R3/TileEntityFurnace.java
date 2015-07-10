package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.plugin.PluginManager;

public class TileEntityFurnace
  extends TileEntityContainer
  implements IUpdatePlayerListBox, IWorldInventory
{
  private static final int[] a = new int[1];
  private static final int[] f = { 2, 1 };
  private static final int[] g = { 1 };
  private ItemStack[] items = new ItemStack[3];
  public int burnTime;
  private int ticksForCurrentFuel;
  public int cookTime;
  private int cookTimeTotal;
  private String m;
  private int lastTick = MinecraftServer.currentTick;
  private int maxStack = 64;
  public List<HumanEntity> transaction = new ArrayList();
  
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
    return this.items.length;
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
        return itemstack;
      }
      ItemStack itemstack = this.items[i].a(j);
      if (this.items[i].count == 0) {
        this.items[i] = null;
      }
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
    boolean flag = (itemstack != null) && (itemstack.doMaterialsMatch(this.items[i])) && (ItemStack.equals(itemstack, this.items[i]));
    
    this.items[i] = itemstack;
    if ((itemstack != null) && (itemstack.count > getMaxStackSize())) {
      itemstack.count = getMaxStackSize();
    }
    if ((i == 0) && (!flag))
    {
      this.cookTimeTotal = a(itemstack);
      this.cookTime = 0;
      update();
    }
  }
  
  public String getName()
  {
    return hasCustomName() ? this.m : "container.furnace";
  }
  
  public boolean hasCustomName()
  {
    return (this.m != null) && (this.m.length() > 0);
  }
  
  public void a(String s)
  {
    this.m = s;
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    NBTTagList nbttaglist = nbttagcompound.getList("Items", 10);
    
    this.items = new ItemStack[getSize()];
    for (int i = 0; i < nbttaglist.size(); i++)
    {
      NBTTagCompound nbttagcompound1 = nbttaglist.get(i);
      byte b0 = nbttagcompound1.getByte("Slot");
      if ((b0 >= 0) && (b0 < this.items.length)) {
        this.items[b0] = ItemStack.createStack(nbttagcompound1);
      }
    }
    this.burnTime = nbttagcompound.getShort("BurnTime");
    this.cookTime = nbttagcompound.getShort("CookTime");
    this.cookTimeTotal = nbttagcompound.getShort("CookTimeTotal");
    this.ticksForCurrentFuel = fuelTime(this.items[1]);
    if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
      this.m = nbttagcompound.getString("CustomName");
    }
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setShort("BurnTime", (short)this.burnTime);
    nbttagcompound.setShort("CookTime", (short)this.cookTime);
    nbttagcompound.setShort("CookTimeTotal", (short)this.cookTimeTotal);
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
      nbttagcompound.setString("CustomName", this.m);
    }
  }
  
  public int getMaxStackSize()
  {
    return this.maxStack;
  }
  
  public boolean isBurning()
  {
    return this.burnTime > 0;
  }
  
  public void c()
  {
    boolean flag = w() == Blocks.LIT_FURNACE;
    boolean flag1 = false;
    
    int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
    this.lastTick = MinecraftServer.currentTick;
    if ((isBurning()) && (canBurn()))
    {
      this.cookTime += elapsedTicks;
      if (this.cookTime >= this.cookTimeTotal)
      {
        this.cookTime = 0;
        this.cookTimeTotal = a(this.items[0]);
        burn();
        flag1 = true;
      }
    }
    else
    {
      this.cookTime = 0;
    }
    if (isBurning()) {
      this.burnTime -= elapsedTicks;
    }
    if (!this.world.isClientSide)
    {
      if ((!isBurning()) && ((this.items[1] == null) || (this.items[0] == null)))
      {
        if ((!isBurning()) && (this.cookTime > 0)) {
          this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
        }
      }
      else if ((this.burnTime <= 0) && (canBurn()))
      {
        CraftItemStack fuel = CraftItemStack.asCraftMirror(this.items[1]);
        
        FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(this.world.getWorld().getBlockAt(this.position.getX(), this.position.getY(), this.position.getZ()), fuel, fuelTime(this.items[1]));
        this.world.getServer().getPluginManager().callEvent(furnaceBurnEvent);
        if (furnaceBurnEvent.isCancelled()) {
          return;
        }
        this.ticksForCurrentFuel = furnaceBurnEvent.getBurnTime();
        this.burnTime += this.ticksForCurrentFuel;
        if ((this.burnTime > 0) && (furnaceBurnEvent.isBurning()))
        {
          flag1 = true;
          if (this.items[1] != null)
          {
            this.items[1].count -= 1;
            if (this.items[1].count == 0)
            {
              Item item = this.items[1].getItem().q();
              
              this.items[1] = (item != null ? new ItemStack(item) : null);
            }
          }
        }
      }
      if (flag != isBurning())
      {
        flag1 = true;
        BlockFurnace.a(isBurning(), this.world, this.position);
        E();
      }
    }
    if (flag1) {
      update();
    }
  }
  
  public int a(ItemStack itemstack)
  {
    return 200;
  }
  
  private boolean canBurn()
  {
    if (this.items[0] == null) {
      return false;
    }
    ItemStack itemstack = RecipesFurnace.getInstance().getResult(this.items[0]);
    
    return itemstack != null;
  }
  
  public void burn()
  {
    if (canBurn())
    {
      ItemStack itemstack = RecipesFurnace.getInstance().getResult(this.items[0]);
      
      CraftItemStack source = CraftItemStack.asCraftMirror(this.items[0]);
      org.bukkit.inventory.ItemStack result = CraftItemStack.asBukkitCopy(itemstack);
      
      FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(this.world.getWorld().getBlockAt(this.position.getX(), this.position.getY(), this.position.getZ()), source, result);
      this.world.getServer().getPluginManager().callEvent(furnaceSmeltEvent);
      if (furnaceSmeltEvent.isCancelled()) {
        return;
      }
      result = furnaceSmeltEvent.getResult();
      itemstack = CraftItemStack.asNMSCopy(result);
      if (itemstack != null) {
        if (this.items[2] == null) {
          this.items[2] = itemstack;
        } else if (CraftItemStack.asCraftMirror(this.items[2]).isSimilar(result)) {
          this.items[2].count += itemstack.count;
        } else {
          return;
        }
      }
      if ((this.items[0].getItem() == Item.getItemOf(Blocks.SPONGE)) && (this.items[0].getData() == 1) && (this.items[1] != null) && (this.items[1].getItem() == Items.BUCKET)) {
        this.items[1] = new ItemStack(Items.WATER_BUCKET);
      }
      this.items[0].count -= 1;
      if (this.items[0].count <= 0) {
        this.items[0] = null;
      }
    }
  }
  
  public static int fuelTime(ItemStack itemstack)
  {
    if (itemstack == null) {
      return 0;
    }
    Item item = itemstack.getItem();
    if (((item instanceof ItemBlock)) && (Block.asBlock(item) != Blocks.AIR))
    {
      Block block = Block.asBlock(item);
      if (block == Blocks.WOODEN_SLAB) {
        return 150;
      }
      if (block.getMaterial() == Material.WOOD) {
        return 300;
      }
      if (block == Blocks.COAL_BLOCK) {
        return 16000;
      }
    }
    return item == Items.BLAZE_ROD ? 2400 : item == Item.getItemOf(Blocks.SAPLING) ? 100 : item == Items.LAVA_BUCKET ? 20000 : item == Items.COAL ? 1600 : item == Items.STICK ? 100 : ((item instanceof ItemHoe)) && (((ItemHoe)item).g().equals("WOOD")) ? 200 : ((item instanceof ItemSword)) && (((ItemSword)item).h().equals("WOOD")) ? 200 : ((item instanceof ItemTool)) && (((ItemTool)item).h().equals("WOOD")) ? 200 : 0;
  }
  
  public static boolean isFuel(ItemStack itemstack)
  {
    return fuelTime(itemstack) > 0;
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return this.world.getTileEntity(this.position) == this;
  }
  
  public void startOpen(EntityHuman entityhuman) {}
  
  public void closeContainer(EntityHuman entityhuman) {}
  
  public boolean b(int i, ItemStack itemstack)
  {
    return i != 2;
  }
  
  public int[] getSlotsForFace(EnumDirection enumdirection)
  {
    return enumdirection == EnumDirection.UP ? a : enumdirection == EnumDirection.DOWN ? f : g;
  }
  
  public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection)
  {
    return b(i, itemstack);
  }
  
  public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection)
  {
    if ((enumdirection == EnumDirection.DOWN) && (i == 1))
    {
      Item item = itemstack.getItem();
      if ((item != Items.WATER_BUCKET) && (item != Items.BUCKET)) {
        return false;
      }
    }
    return true;
  }
  
  public String getContainerName()
  {
    return "minecraft:furnace";
  }
  
  public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman)
  {
    return new ContainerFurnace(playerinventory, this);
  }
  
  public int getProperty(int i)
  {
    switch (i)
    {
    case 0: 
      return this.burnTime;
    case 1: 
      return this.ticksForCurrentFuel;
    case 2: 
      return this.cookTime;
    case 3: 
      return this.cookTimeTotal;
    }
    return 0;
  }
  
  public void b(int i, int j)
  {
    switch (i)
    {
    case 0: 
      this.burnTime = j;
      break;
    case 1: 
      this.ticksForCurrentFuel = j;
      break;
    case 2: 
      this.cookTime = j;
      break;
    case 3: 
      this.cookTimeTotal = j;
    }
  }
  
  public int g()
  {
    return 4;
  }
  
  public void l()
  {
    for (int i = 0; i < this.items.length; i++) {
      this.items[i] = null;
    }
  }
}
