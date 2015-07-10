package net.minecraft.server.v1_8_R3;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.codec.Charsets;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;

public class EntityItemFrame
  extends EntityHanging
{
  private float c = 1.0F;
  
  public EntityItemFrame(World world)
  {
    super(world);
  }
  
  public EntityItemFrame(World world, BlockPosition blockposition, EnumDirection enumdirection)
  {
    super(world, blockposition);
    setDirection(enumdirection);
  }
  
  protected void h()
  {
    getDataWatcher().add(8, 5);
    getDataWatcher().a(9, Byte.valueOf((byte)0));
  }
  
  public float ao()
  {
    return 0.0F;
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    if ((!damagesource.isExplosion()) && (getItem() != null))
    {
      if (!this.world.isClientSide)
      {
        if ((CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f, false)) || (this.dead)) {
          return true;
        }
        a(damagesource.getEntity(), false);
        setItem(null);
      }
      return true;
    }
    return super.damageEntity(damagesource, f);
  }
  
  public int l()
  {
    return 12;
  }
  
  public int m()
  {
    return 12;
  }
  
  public void b(Entity entity)
  {
    a(entity, true);
  }
  
  public void a(Entity entity, boolean flag)
  {
    if (this.world.getGameRules().getBoolean("doEntityDrops"))
    {
      ItemStack itemstack = getItem();
      if ((entity instanceof EntityHuman))
      {
        EntityHuman entityhuman = (EntityHuman)entity;
        if (entityhuman.abilities.canInstantlyBuild)
        {
          b(itemstack);
          return;
        }
      }
      if (flag) {
        a(new ItemStack(Items.ITEM_FRAME), 0.0F);
      }
      if ((itemstack != null) && (this.random.nextFloat() < this.c))
      {
        itemstack = itemstack.cloneItemStack();
        b(itemstack);
        a(itemstack, 0.0F);
      }
    }
  }
  
  private void b(ItemStack itemstack)
  {
    if (itemstack != null)
    {
      if (itemstack.getItem() == Items.FILLED_MAP)
      {
        WorldMap worldmap = ((ItemWorldMap)itemstack.getItem()).getSavedMap(itemstack, this.world);
        
        worldmap.decorations.remove(UUID.nameUUIDFromBytes(("frame-" + getId()).getBytes(Charsets.US_ASCII)));
      }
      itemstack.a(null);
    }
  }
  
  public ItemStack getItem()
  {
    return getDataWatcher().getItemStack(8);
  }
  
  public void setItem(ItemStack itemstack)
  {
    setItem(itemstack, true);
  }
  
  private void setItem(ItemStack itemstack, boolean flag)
  {
    if (itemstack != null)
    {
      itemstack = itemstack.cloneItemStack();
      itemstack.count = 1;
      itemstack.a(this);
    }
    getDataWatcher().watch(8, itemstack);
    getDataWatcher().update(8);
    if ((flag) && (this.blockPosition != null)) {
      this.world.updateAdjacentComparators(this.blockPosition, Blocks.AIR);
    }
  }
  
  public int getRotation()
  {
    return getDataWatcher().getByte(9);
  }
  
  public void setRotation(int i)
  {
    setRotation(i, true);
  }
  
  private void setRotation(int i, boolean flag)
  {
    getDataWatcher().watch(9, Byte.valueOf((byte)(i % 8)));
    if ((flag) && (this.blockPosition != null)) {
      this.world.updateAdjacentComparators(this.blockPosition, Blocks.AIR);
    }
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    if (getItem() != null)
    {
      nbttagcompound.set("Item", getItem().save(new NBTTagCompound()));
      nbttagcompound.setByte("ItemRotation", (byte)getRotation());
      nbttagcompound.setFloat("ItemDropChance", this.c);
    }
    super.b(nbttagcompound);
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Item");
    if ((nbttagcompound1 != null) && (!nbttagcompound1.isEmpty()))
    {
      setItem(ItemStack.createStack(nbttagcompound1), false);
      setRotation(nbttagcompound.getByte("ItemRotation"), false);
      if (nbttagcompound.hasKeyOfType("ItemDropChance", 99)) {
        this.c = nbttagcompound.getFloat("ItemDropChance");
      }
      if (nbttagcompound.hasKey("Direction")) {
        setRotation(getRotation() * 2, false);
      }
    }
    super.a(nbttagcompound);
  }
  
  public boolean e(EntityHuman entityhuman)
  {
    if (getItem() == null)
    {
      ItemStack itemstack = entityhuman.bA();
      if ((itemstack != null) && (!this.world.isClientSide))
      {
        setItem(itemstack);
        if (!entityhuman.abilities.canInstantlyBuild) {
          if (--itemstack.count <= 0) {
            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
          }
        }
      }
    }
    else if (!this.world.isClientSide)
    {
      setRotation(getRotation() + 1);
    }
    return true;
  }
  
  public int q()
  {
    return getItem() == null ? 0 : getRotation() % 8 + 1;
  }
}
