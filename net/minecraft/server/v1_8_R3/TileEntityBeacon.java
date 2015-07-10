package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

public class TileEntityBeacon
  extends TileEntityContainer
  implements IUpdatePlayerListBox, IInventory
{
  public static final MobEffectList[][] a = { { MobEffectList.FASTER_MOVEMENT, MobEffectList.FASTER_DIG }, { MobEffectList.RESISTANCE, MobEffectList.JUMP }, { MobEffectList.INCREASE_DAMAGE }, { MobEffectList.REGENERATION } };
  private final List<BeaconColorTracker> f = Lists.newArrayList();
  private boolean i;
  private int j = -1;
  private int k;
  private int l;
  private ItemStack inventorySlot;
  private String n;
  public List<HumanEntity> transaction = new ArrayList();
  private int maxStack = 64;
  
  public ItemStack[] getContents()
  {
    return new ItemStack[] { this.inventorySlot };
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
  
  public void c()
  {
    if (this.world.getTime() % 80L == 0L) {
      m();
    }
  }
  
  public void m()
  {
    B();
    A();
  }
  
  private void A()
  {
    if ((this.i) && (this.j > 0) && (!this.world.isClientSide) && (this.k > 0))
    {
      double d0 = this.j * 10 + 10;
      byte b0 = 0;
      if ((this.j >= 4) && (this.k == this.l)) {
        b0 = 1;
      }
      int i = this.position.getX();
      int j = this.position.getY();
      int k = this.position.getZ();
      AxisAlignedBB axisalignedbb = new AxisAlignedBB(i, j, k, i + 1, j + 1, k + 1).grow(d0, d0, d0).a(0.0D, this.world.getHeight(), 0.0D);
      List list = this.world.a(EntityHuman.class, axisalignedbb);
      Iterator iterator = list.iterator();
      while (iterator.hasNext())
      {
        EntityHuman entityhuman = (EntityHuman)iterator.next();
        entityhuman.addEffect(new MobEffect(this.k, 180, b0, true, true));
      }
      if ((this.j >= 4) && (this.k != this.l) && (this.l > 0))
      {
        iterator = list.iterator();
        while (iterator.hasNext())
        {
          EntityHuman entityhuman = (EntityHuman)iterator.next();
          entityhuman.addEffect(new MobEffect(this.l, 180, 0, true, true));
        }
      }
    }
  }
  
  private void B()
  {
    int i = this.j;
    int j = this.position.getX();
    int k = this.position.getY();
    int l = this.position.getZ();
    
    this.j = 0;
    this.f.clear();
    this.i = true;
    BeaconColorTracker tileentitybeacon_beaconcolortracker = new BeaconColorTracker(EntitySheep.a(EnumColor.WHITE));
    
    this.f.add(tileentitybeacon_beaconcolortracker);
    boolean flag = true;
    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
    for (int i1 = k + 1; i1 < 256; i1++)
    {
      IBlockData iblockdata = this.world.getType(blockposition_mutableblockposition.c(j, i1, l));
      float[] afloat;
      float[] afloat;
      if (iblockdata.getBlock() == Blocks.STAINED_GLASS)
      {
        afloat = EntitySheep.a((EnumColor)iblockdata.get(BlockStainedGlass.COLOR));
      }
      else
      {
        if (iblockdata.getBlock() != Blocks.STAINED_GLASS_PANE)
        {
          if ((iblockdata.getBlock().p() >= 15) && (iblockdata.getBlock() != Blocks.BEDROCK))
          {
            this.i = false;
            this.f.clear();
            break;
          }
          tileentitybeacon_beaconcolortracker.a();
          continue;
        }
        afloat = EntitySheep.a((EnumColor)iblockdata.get(BlockStainedGlassPane.COLOR));
      }
      if (!flag) {
        afloat = new float[] { (tileentitybeacon_beaconcolortracker.b()[0] + afloat[0]) / 2.0F, (tileentitybeacon_beaconcolortracker.b()[1] + afloat[1]) / 2.0F, (tileentitybeacon_beaconcolortracker.b()[2] + afloat[2]) / 2.0F };
      }
      if (Arrays.equals(afloat, tileentitybeacon_beaconcolortracker.b()))
      {
        tileentitybeacon_beaconcolortracker.a();
      }
      else
      {
        tileentitybeacon_beaconcolortracker = new BeaconColorTracker(afloat);
        this.f.add(tileentitybeacon_beaconcolortracker);
      }
      flag = false;
    }
    if (this.i)
    {
      for (i1 = 1; i1 <= 4; this.j = (i1++))
      {
        int j1 = k - i1;
        if (j1 < 0) {
          break;
        }
        boolean flag1 = true;
        for (int k1 = j - i1; (k1 <= j + i1) && (flag1); k1++) {
          for (int l1 = l - i1; l1 <= l + i1; l1++)
          {
            Block block = this.world.getType(new BlockPosition(k1, j1, l1)).getBlock();
            if ((block != Blocks.EMERALD_BLOCK) && (block != Blocks.GOLD_BLOCK) && (block != Blocks.DIAMOND_BLOCK) && (block != Blocks.IRON_BLOCK))
            {
              flag1 = false;
              break;
            }
          }
        }
        if (!flag1) {
          break;
        }
      }
      if (this.j == 0) {
        this.i = false;
      }
    }
    if ((!this.world.isClientSide) && (this.j == 4) && (i < this.j))
    {
      Iterator iterator = this.world.a(EntityHuman.class, new AxisAlignedBB(j, k, l, j, k - 4, l).grow(10.0D, 5.0D, 10.0D)).iterator();
      while (iterator.hasNext())
      {
        EntityHuman entityhuman = (EntityHuman)iterator.next();
        
        entityhuman.b(AchievementList.K);
      }
    }
  }
  
  public Packet getUpdatePacket()
  {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    
    b(nbttagcompound);
    return new PacketPlayOutTileEntityData(this.position, 3, nbttagcompound);
  }
  
  private int h(int i)
  {
    if ((i >= 0) && (i < MobEffectList.byId.length) && (MobEffectList.byId[i] != null))
    {
      MobEffectList mobeffectlist = MobEffectList.byId[i];
      
      return (mobeffectlist != MobEffectList.FASTER_MOVEMENT) && (mobeffectlist != MobEffectList.FASTER_DIG) && (mobeffectlist != MobEffectList.RESISTANCE) && (mobeffectlist != MobEffectList.JUMP) && (mobeffectlist != MobEffectList.INCREASE_DAMAGE) && (mobeffectlist != MobEffectList.REGENERATION) ? 0 : i;
    }
    return 0;
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    this.k = h(nbttagcompound.getInt("Primary"));
    this.l = h(nbttagcompound.getInt("Secondary"));
    this.j = nbttagcompound.getInt("Levels");
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("Primary", this.k);
    nbttagcompound.setInt("Secondary", this.l);
    nbttagcompound.setInt("Levels", this.j);
  }
  
  public int getSize()
  {
    return 1;
  }
  
  public ItemStack getItem(int i)
  {
    return i == 0 ? this.inventorySlot : null;
  }
  
  public ItemStack splitStack(int i, int j)
  {
    if ((i == 0) && (this.inventorySlot != null))
    {
      if (j >= this.inventorySlot.count)
      {
        ItemStack itemstack = this.inventorySlot;
        
        this.inventorySlot = null;
        return itemstack;
      }
      this.inventorySlot.count -= j;
      return new ItemStack(this.inventorySlot.getItem(), j, this.inventorySlot.getData());
    }
    return null;
  }
  
  public ItemStack splitWithoutUpdate(int i)
  {
    if ((i == 0) && (this.inventorySlot != null))
    {
      ItemStack itemstack = this.inventorySlot;
      
      this.inventorySlot = null;
      return itemstack;
    }
    return null;
  }
  
  public void setItem(int i, ItemStack itemstack)
  {
    if (i == 0) {
      this.inventorySlot = itemstack;
    }
  }
  
  public String getName()
  {
    return hasCustomName() ? this.n : "container.beacon";
  }
  
  public boolean hasCustomName()
  {
    return (this.n != null) && (this.n.length() > 0);
  }
  
  public void a(String s)
  {
    this.n = s;
  }
  
  public int getMaxStackSize()
  {
    return this.maxStack;
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    return this.world.getTileEntity(this.position) == this;
  }
  
  public void startOpen(EntityHuman entityhuman) {}
  
  public void closeContainer(EntityHuman entityhuman) {}
  
  public boolean b(int i, ItemStack itemstack)
  {
    return (itemstack.getItem() == Items.EMERALD) || (itemstack.getItem() == Items.DIAMOND) || (itemstack.getItem() == Items.GOLD_INGOT) || (itemstack.getItem() == Items.IRON_INGOT);
  }
  
  public String getContainerName()
  {
    return "minecraft:beacon";
  }
  
  public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman)
  {
    return new ContainerBeacon(playerinventory, this);
  }
  
  public int getProperty(int i)
  {
    switch (i)
    {
    case 0: 
      return this.j;
    case 1: 
      return this.k;
    case 2: 
      return this.l;
    }
    return 0;
  }
  
  public void b(int i, int j)
  {
    switch (i)
    {
    case 0: 
      this.j = j;
      break;
    case 1: 
      this.k = h(j);
      break;
    case 2: 
      this.l = h(j);
    }
  }
  
  public int g()
  {
    return 3;
  }
  
  public void l()
  {
    this.inventorySlot = null;
  }
  
  public boolean c(int i, int j)
  {
    if (i == 1)
    {
      m();
      return true;
    }
    return super.c(i, j);
  }
  
  public static class BeaconColorTracker
  {
    private final float[] a;
    private int b;
    
    public BeaconColorTracker(float[] afloat)
    {
      this.a = afloat;
      this.b = 1;
    }
    
    protected void a()
    {
      this.b += 1;
    }
    
    public float[] b()
    {
      return this.a;
    }
  }
}
