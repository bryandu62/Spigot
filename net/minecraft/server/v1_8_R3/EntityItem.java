package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.SpigotWorldConfig;

public class EntityItem
  extends Entity
{
  private static final Logger b = ;
  private int age;
  public int pickupDelay;
  private int e;
  private String f;
  private String g;
  public float a;
  private int lastTick = MinecraftServer.currentTick;
  
  public EntityItem(World world, double d0, double d1, double d2)
  {
    super(world);
    this.e = 5;
    this.a = ((float)(Math.random() * 3.141592653589793D * 2.0D));
    setSize(0.25F, 0.25F);
    setPosition(d0, d1, d2);
    this.yaw = ((float)(Math.random() * 360.0D));
    this.motX = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    this.motY = 0.20000000298023224D;
    this.motZ = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
  }
  
  public EntityItem(World world, double d0, double d1, double d2, ItemStack itemstack)
  {
    this(world, d0, d1, d2);
    if ((itemstack == null) || (itemstack.getItem() == null)) {
      return;
    }
    setItemStack(itemstack);
  }
  
  protected boolean s_()
  {
    return false;
  }
  
  public EntityItem(World world)
  {
    super(world);
    this.e = 5;
    this.a = ((float)(Math.random() * 3.141592653589793D * 2.0D));
    setSize(0.25F, 0.25F);
    setItemStack(new ItemStack(Blocks.AIR, 0));
  }
  
  protected void h()
  {
    getDataWatcher().add(10, 5);
  }
  
  public void t_()
  {
    if (getItemStack() == null)
    {
      die();
    }
    else
    {
      super.t_();
      
      int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
      if (this.pickupDelay != 32767) {
        this.pickupDelay -= elapsedTicks;
      }
      if (this.age != 32768) {
        this.age += elapsedTicks;
      }
      this.lastTick = MinecraftServer.currentTick;
      
      this.lastX = this.locX;
      this.lastY = this.locY;
      this.lastZ = this.locZ;
      this.motY -= 0.03999999910593033D;
      this.noclip = j(this.locX, (getBoundingBox().b + getBoundingBox().e) / 2.0D, this.locZ);
      move(this.motX, this.motY, this.motZ);
      boolean flag = ((int)this.lastX != (int)this.locX) || ((int)this.lastY != (int)this.locY) || ((int)this.lastZ != (int)this.locZ);
      if ((flag) || (this.ticksLived % 25 == 0))
      {
        if (this.world.getType(new BlockPosition(this)).getBlock().getMaterial() == Material.LAVA)
        {
          this.motY = 0.20000000298023224D;
          this.motX = ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
          this.motZ = ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
          makeSound("random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }
        if (!this.world.isClientSide) {
          w();
        }
      }
      float f = 0.98F;
      if (this.onGround) {
        f = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.98F;
      }
      this.motX *= f;
      this.motY *= 0.9800000190734863D;
      this.motZ *= f;
      if (this.onGround) {
        this.motY *= -0.5D;
      }
      W();
      if ((!this.world.isClientSide) && (this.age >= this.world.spigotConfig.itemDespawnRate))
      {
        if (CraftEventFactory.callItemDespawnEvent(this).isCancelled())
        {
          this.age = 0;
          return;
        }
        die();
      }
    }
  }
  
  public void inactiveTick()
  {
    int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
    this.pickupDelay -= elapsedTicks;
    this.age += elapsedTicks;
    this.lastTick = MinecraftServer.currentTick;
    if ((!this.world.isClientSide) && (this.age >= this.world.spigotConfig.itemDespawnRate))
    {
      if (CraftEventFactory.callItemDespawnEvent(this).isCancelled())
      {
        this.age = 0;
        return;
      }
      die();
    }
  }
  
  private void w()
  {
    double radius = this.world.spigotConfig.itemMerge;
    Iterator iterator = this.world.a(EntityItem.class, getBoundingBox().grow(radius, radius, radius)).iterator();
    while (iterator.hasNext())
    {
      EntityItem entityitem = (EntityItem)iterator.next();
      
      a(entityitem);
    }
  }
  
  private boolean a(EntityItem entityitem)
  {
    if (entityitem == this) {
      return false;
    }
    if ((entityitem.isAlive()) && (isAlive()))
    {
      ItemStack itemstack = getItemStack();
      ItemStack itemstack1 = entityitem.getItemStack();
      if ((this.pickupDelay != 32767) && (entityitem.pickupDelay != 32767))
      {
        if ((this.age != 32768) && (entityitem.age != 32768))
        {
          if (itemstack1.getItem() != itemstack.getItem()) {
            return false;
          }
          if ((itemstack1.hasTag() ^ itemstack.hasTag())) {
            return false;
          }
          if ((itemstack1.hasTag()) && (!itemstack1.getTag().equals(itemstack.getTag()))) {
            return false;
          }
          if (itemstack1.getItem() == null) {
            return false;
          }
          if ((itemstack1.getItem().k()) && (itemstack1.getData() != itemstack.getData())) {
            return false;
          }
          if (itemstack1.count < itemstack.count) {
            return entityitem.a(this);
          }
          if (itemstack1.count + itemstack.count > itemstack1.getMaxStackSize()) {
            return false;
          }
          itemstack.count += itemstack1.count;
          this.pickupDelay = Math.max(entityitem.pickupDelay, this.pickupDelay);
          this.age = Math.min(entityitem.age, this.age);
          setItemStack(itemstack);
          entityitem.die();
          
          return true;
        }
        return false;
      }
      return false;
    }
    return false;
  }
  
  public void j()
  {
    this.age = 4800;
  }
  
  public boolean W()
  {
    if (this.world.a(getBoundingBox(), Material.WATER, this))
    {
      if ((!this.inWater) && (!this.justCreated)) {
        X();
      }
      this.inWater = true;
    }
    else
    {
      this.inWater = false;
    }
    return this.inWater;
  }
  
  protected void burn(int i)
  {
    damageEntity(DamageSource.FIRE, i);
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    if ((getItemStack() != null) && (getItemStack().getItem() == Items.NETHER_STAR) && (damagesource.isExplosion())) {
      return false;
    }
    if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
      return false;
    }
    ac();
    this.e = ((int)(this.e - f));
    if (this.e <= 0) {
      die();
    }
    return false;
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setShort("Health", (short)(byte)this.e);
    nbttagcompound.setShort("Age", (short)this.age);
    nbttagcompound.setShort("PickupDelay", (short)this.pickupDelay);
    if (n() != null) {
      nbttagcompound.setString("Thrower", this.f);
    }
    if (m() != null) {
      nbttagcompound.setString("Owner", this.g);
    }
    if (getItemStack() != null) {
      nbttagcompound.set("Item", getItemStack().save(new NBTTagCompound()));
    }
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    this.e = (nbttagcompound.getShort("Health") & 0xFF);
    this.age = nbttagcompound.getShort("Age");
    if (nbttagcompound.hasKey("PickupDelay")) {
      this.pickupDelay = nbttagcompound.getShort("PickupDelay");
    }
    if (nbttagcompound.hasKey("Owner")) {
      this.g = nbttagcompound.getString("Owner");
    }
    if (nbttagcompound.hasKey("Thrower")) {
      this.f = nbttagcompound.getString("Thrower");
    }
    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Item");
    if (nbttagcompound1 != null)
    {
      ItemStack itemstack = ItemStack.createStack(nbttagcompound1);
      if (itemstack != null) {
        setItemStack(itemstack);
      } else {
        die();
      }
    }
    else
    {
      die();
    }
    if (getItemStack() == null) {
      die();
    }
  }
  
  public void d(EntityHuman entityhuman)
  {
    if (!this.world.isClientSide)
    {
      ItemStack itemstack = getItemStack();
      int i = itemstack.count;
      
      int canHold = entityhuman.inventory.canHold(itemstack);
      int remaining = itemstack.count - canHold;
      if ((this.pickupDelay <= 0) && (canHold > 0))
      {
        itemstack.count = canHold;
        PlayerPickupItemEvent event = new PlayerPickupItemEvent((Player)entityhuman.getBukkitEntity(), (org.bukkit.entity.Item)getBukkitEntity(), remaining);
        
        this.world.getServer().getPluginManager().callEvent(event);
        itemstack.count = (canHold + remaining);
        if (event.isCancelled()) {
          return;
        }
        this.pickupDelay = 0;
      }
      if ((this.pickupDelay == 0) && ((this.g == null) || (6000 - this.age <= 200) || (this.g.equals(entityhuman.getName()))) && (entityhuman.inventory.pickup(itemstack)))
      {
        if (itemstack.getItem() == Item.getItemOf(Blocks.LOG)) {
          entityhuman.b(AchievementList.g);
        }
        if (itemstack.getItem() == Item.getItemOf(Blocks.LOG2)) {
          entityhuman.b(AchievementList.g);
        }
        if (itemstack.getItem() == Items.LEATHER) {
          entityhuman.b(AchievementList.t);
        }
        if (itemstack.getItem() == Items.DIAMOND) {
          entityhuman.b(AchievementList.w);
        }
        if (itemstack.getItem() == Items.BLAZE_ROD) {
          entityhuman.b(AchievementList.A);
        }
        if ((itemstack.getItem() == Items.DIAMOND) && (n() != null))
        {
          EntityHuman entityhuman1 = this.world.a(n());
          if ((entityhuman1 != null) && (entityhuman1 != entityhuman)) {
            entityhuman1.b(AchievementList.x);
          }
        }
        if (!R()) {
          this.world.makeSound(entityhuman, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
        entityhuman.receive(this, i);
        if (itemstack.count <= 0) {
          die();
        }
      }
    }
  }
  
  public String getName()
  {
    return hasCustomName() ? getCustomName() : LocaleI18n.get("item." + getItemStack().a());
  }
  
  public boolean aD()
  {
    return false;
  }
  
  public void c(int i)
  {
    super.c(i);
    if (!this.world.isClientSide) {
      w();
    }
  }
  
  public ItemStack getItemStack()
  {
    ItemStack itemstack = getDataWatcher().getItemStack(10);
    if (itemstack == null)
    {
      if (this.world != null) {
        b.error("Item entity " + getId() + " has no item?!");
      }
      return new ItemStack(Blocks.STONE);
    }
    return itemstack;
  }
  
  public void setItemStack(ItemStack itemstack)
  {
    getDataWatcher().watch(10, itemstack);
    getDataWatcher().update(10);
  }
  
  public String m()
  {
    return this.g;
  }
  
  public void b(String s)
  {
    this.g = s;
  }
  
  public String n()
  {
    return this.f;
  }
  
  public void c(String s)
  {
    this.f = s;
  }
  
  public void p()
  {
    this.pickupDelay = 10;
  }
  
  public void q()
  {
    this.pickupDelay = 0;
  }
  
  public void r()
  {
    this.pickupDelay = 32767;
  }
  
  public void a(int i)
  {
    this.pickupDelay = i;
  }
  
  public boolean s()
  {
    return this.pickupDelay > 0;
  }
  
  public void u()
  {
    this.age = 59536;
  }
  
  public void v()
  {
    r();
    this.age = 5999;
  }
}
