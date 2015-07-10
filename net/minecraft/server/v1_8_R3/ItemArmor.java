package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicates;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

public class ItemArmor
  extends Item
{
  private static final int[] k = { 11, 16, 15, 13 };
  public static final String[] a = { "minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots" };
  private static final IDispenseBehavior l = new DispenseBehaviorItem()
  {
    protected ItemStack b(ISourceBlock isourceblock, ItemStack itemstack)
    {
      BlockPosition blockposition = isourceblock.getBlockPosition().shift(BlockDispenser.b(isourceblock.f()));
      int i = blockposition.getX();
      int j = blockposition.getY();
      int k = blockposition.getZ();
      AxisAlignedBB axisalignedbb = new AxisAlignedBB(i, j, k, i + 1, j + 1, k + 1);
      List list = isourceblock.i().a(EntityLiving.class, axisalignedbb, Predicates.and(IEntitySelector.d, new IEntitySelector.EntitySelectorEquipable(itemstack)));
      if (list.size() > 0)
      {
        EntityLiving entityliving = (EntityLiving)list.get(0);
        int l = (entityliving instanceof EntityHuman) ? 1 : 0;
        int i1 = EntityInsentient.c(itemstack);
        
        ItemStack itemstack1 = itemstack.a(1);
        World world = isourceblock.i();
        Block block = world.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
        
        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new Vector(0, 0, 0));
        if (!BlockDispenser.eventFired) {
          world.getServer().getPluginManager().callEvent(event);
        }
        if (event.isCancelled())
        {
          itemstack.count += 1;
          return itemstack;
        }
        if (!event.getItem().equals(craftItem))
        {
          itemstack.count += 1;
          
          ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
          IDispenseBehavior idispensebehavior = (IDispenseBehavior)BlockDispenser.N.get(eventStack.getItem());
          if ((idispensebehavior != IDispenseBehavior.a) && (idispensebehavior != this))
          {
            idispensebehavior.a(isourceblock, eventStack);
            return itemstack;
          }
        }
        itemstack1.count = 1;
        entityliving.setEquipment(i1 - l, itemstack1);
        if ((entityliving instanceof EntityInsentient)) {
          ((EntityInsentient)entityliving).a(i1, 2.0F);
        }
        return itemstack;
      }
      return super.b(isourceblock, itemstack);
    }
  };
  public final int b;
  public final int c;
  public final int d;
  private final EnumArmorMaterial m;
  
  public ItemArmor(EnumArmorMaterial itemarmor_enumarmormaterial, int i, int j)
  {
    this.m = itemarmor_enumarmormaterial;
    this.b = j;
    this.d = i;
    this.c = itemarmor_enumarmormaterial.b(j);
    setMaxDurability(itemarmor_enumarmormaterial.a(j));
    this.maxStackSize = 1;
    a(CreativeModeTab.j);
    BlockDispenser.N.a(this, l);
  }
  
  public int b()
  {
    return this.m.a();
  }
  
  public EnumArmorMaterial x_()
  {
    return this.m;
  }
  
  public boolean d_(ItemStack itemstack)
  {
    return !itemstack.getTag().hasKeyOfType("display", 10) ? false : !itemstack.hasTag() ? false : this.m != EnumArmorMaterial.LEATHER ? false : itemstack.getTag().getCompound("display").hasKeyOfType("color", 3);
  }
  
  public int b(ItemStack itemstack)
  {
    if (this.m != EnumArmorMaterial.LEATHER) {
      return -1;
    }
    NBTTagCompound nbttagcompound = itemstack.getTag();
    if (nbttagcompound != null)
    {
      NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("display");
      if ((nbttagcompound1 != null) && (nbttagcompound1.hasKeyOfType("color", 3))) {
        return nbttagcompound1.getInt("color");
      }
    }
    return 10511680;
  }
  
  public void c(ItemStack itemstack)
  {
    if (this.m == EnumArmorMaterial.LEATHER)
    {
      NBTTagCompound nbttagcompound = itemstack.getTag();
      if (nbttagcompound != null)
      {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("display");
        if (nbttagcompound1.hasKey("color")) {
          nbttagcompound1.remove("color");
        }
      }
    }
  }
  
  public void b(ItemStack itemstack, int i)
  {
    if (this.m != EnumArmorMaterial.LEATHER) {
      throw new UnsupportedOperationException("Can't dye non-leather!");
    }
    NBTTagCompound nbttagcompound = itemstack.getTag();
    if (nbttagcompound == null)
    {
      nbttagcompound = new NBTTagCompound();
      itemstack.setTag(nbttagcompound);
    }
    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("display");
    if (!nbttagcompound.hasKeyOfType("display", 10)) {
      nbttagcompound.set("display", nbttagcompound1);
    }
    nbttagcompound1.setInt("color", i);
  }
  
  public boolean a(ItemStack itemstack, ItemStack itemstack1)
  {
    return this.m.b() == itemstack1.getItem() ? true : super.a(itemstack, itemstack1);
  }
  
  public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman)
  {
    int i = EntityInsentient.c(itemstack) - 1;
    ItemStack itemstack1 = entityhuman.q(i);
    if (itemstack1 == null)
    {
      entityhuman.setEquipment(i, itemstack.cloneItemStack());
      itemstack.count = 0;
    }
    return itemstack;
  }
  
  public static enum EnumArmorMaterial
  {
    LEATHER("leather", 5, new int[] { 1, 3, 2, 1 }, 15),  CHAIN("chainmail", 15, new int[] { 2, 5, 4, 1 }, 12),  IRON("iron", 15, new int[] { 2, 6, 5, 2 }, 9),  GOLD("gold", 7, new int[] { 2, 5, 3, 1 }, 25),  DIAMOND("diamond", 33, new int[] { 3, 8, 6, 3 }, 10);
    
    private final String f;
    private final int g;
    private final int[] h;
    private final int i;
    
    private EnumArmorMaterial(String s, int i, int[] aint, int j)
    {
      this.f = s;
      this.g = i;
      this.h = aint;
      this.i = j;
    }
    
    public int a(int i)
    {
      return ItemArmor.k[i] * this.g;
    }
    
    public int b(int i)
    {
      return this.h[i];
    }
    
    public int a()
    {
      return this.i;
    }
    
    public Item b()
    {
      return this == DIAMOND ? Items.DIAMOND : this == IRON ? Items.IRON_INGOT : this == GOLD ? Items.GOLD_INGOT : this == CHAIN ? Items.IRON_INGOT : this == LEATHER ? Items.LEATHER : null;
    }
  }
}