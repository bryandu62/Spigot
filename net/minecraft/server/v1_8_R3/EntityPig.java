package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.PigZapEvent;

public class EntityPig
  extends EntityAnimal
{
  private final PathfinderGoalPassengerCarrotStick bm;
  
  public EntityPig(World world)
  {
    super(world);
    setSize(0.9F, 0.9F);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
    this.goalSelector.a(2, this.bm = new PathfinderGoalPassengerCarrotStick(this, 0.3F));
    this.goalSelector.a(3, new PathfinderGoalBreed(this, 1.0D));
    this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT_ON_A_STICK, false));
    this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT, false));
    this.goalSelector.a(5, new PathfinderGoalFollowParent(this, 1.1D));
    this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
  }
  
  public boolean bW()
  {
    ItemStack itemstack = ((EntityHuman)this.passenger).bA();
    
    return (itemstack != null) && (itemstack.getItem() == Items.CARROT_ON_A_STICK);
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, Byte.valueOf((byte)0));
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setBoolean("Saddle", hasSaddle());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    setSaddle(nbttagcompound.getBoolean("Saddle"));
  }
  
  protected String z()
  {
    return "mob.pig.say";
  }
  
  protected String bo()
  {
    return "mob.pig.say";
  }
  
  protected String bp()
  {
    return "mob.pig.death";
  }
  
  protected void a(BlockPosition blockposition, Block block)
  {
    makeSound("mob.pig.step", 0.15F, 1.0F);
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    if (super.a(entityhuman)) {
      return true;
    }
    if ((hasSaddle()) && (!this.world.isClientSide) && ((this.passenger == null) || (this.passenger == entityhuman)))
    {
      entityhuman.mount(this);
      return true;
    }
    return false;
  }
  
  protected Item getLoot()
  {
    return isBurning() ? Items.COOKED_PORKCHOP : Items.PORKCHOP;
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    int j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);
    for (int k = 0; k < j; k++) {
      if (isBurning()) {
        a(Items.COOKED_PORKCHOP, 1);
      } else {
        a(Items.PORKCHOP, 1);
      }
    }
    if (hasSaddle()) {
      a(Items.SADDLE, 1);
    }
  }
  
  public boolean hasSaddle()
  {
    return (this.datawatcher.getByte(16) & 0x1) != 0;
  }
  
  public void setSaddle(boolean flag)
  {
    if (flag) {
      this.datawatcher.watch(16, Byte.valueOf((byte)1));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)0));
    }
  }
  
  public void onLightningStrike(EntityLightning entitylightning)
  {
    if ((!this.world.isClientSide) && (!this.dead))
    {
      EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);
      if (CraftEventFactory.callPigZapEvent(this, entitylightning, entitypigzombie).isCancelled()) {
        return;
      }
      entitypigzombie.setEquipment(0, new ItemStack(Items.GOLDEN_SWORD));
      entitypigzombie.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
      entitypigzombie.k(ce());
      if (hasCustomName())
      {
        entitypigzombie.setCustomName(getCustomName());
        entitypigzombie.setCustomNameVisible(getCustomNameVisible());
      }
      this.world.addEntity(entitypigzombie, CreatureSpawnEvent.SpawnReason.LIGHTNING);
      die();
    }
  }
  
  public void e(float f, float f1)
  {
    super.e(f, f1);
    if ((f > 5.0F) && ((this.passenger instanceof EntityHuman))) {
      ((EntityHuman)this.passenger).b(AchievementList.u);
    }
  }
  
  public EntityPig b(EntityAgeable entityageable)
  {
    return new EntityPig(this.world);
  }
  
  public boolean d(ItemStack itemstack)
  {
    return (itemstack != null) && (itemstack.getItem() == Items.CARROT);
  }
  
  public PathfinderGoalPassengerCarrotStick cm()
  {
    return this.bm;
  }
  
  public EntityAgeable createChild(EntityAgeable entityageable)
  {
    return b(entityageable);
  }
}
