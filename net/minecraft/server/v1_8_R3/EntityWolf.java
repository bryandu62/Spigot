package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Random;
import java.util.UUID;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class EntityWolf
  extends EntityTameableAnimal
{
  private float bo;
  private float bp;
  private boolean bq;
  private boolean br;
  private float bs;
  private float bt;
  
  public EntityWolf(World world)
  {
    super(world);
    setSize(0.6F, 0.8F);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(1, new PathfinderGoalFloat(this));
    this.goalSelector.a(2, this.bm);
    this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
    this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));
    this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
    this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
    this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
    this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
    this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
    this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
    this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
    this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntityAnimal.class, false, new Predicate()
    {
      public boolean a(Entity entity)
      {
        return ((entity instanceof EntitySheep)) || ((entity instanceof EntityRabbit));
      }
      
      public boolean apply(Object object)
      {
        return a((Entity)object);
      }
    }));
    this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget(this, EntitySkeleton.class, false));
    setTamed(false);
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
    if (isTamed()) {
      getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
    } else {
      getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
    }
    getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE);
    getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(2.0D);
  }
  
  public void setGoalTarget(EntityLiving entityliving)
  {
    super.setGoalTarget(entityliving);
    if (entityliving == null) {
      setAngry(false);
    } else if (!isTamed()) {
      setAngry(true);
    }
  }
  
  public void setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fire)
  {
    super.setGoalTarget(entityliving, reason, fire);
    if (entityliving == null) {
      setAngry(false);
    } else if (!isTamed()) {
      setAngry(true);
    }
  }
  
  protected void E()
  {
    this.datawatcher.watch(18, Float.valueOf(getHealth()));
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(18, new Float(getHealth()));
    this.datawatcher.a(19, new Byte((byte)0));
    this.datawatcher.a(20, new Byte((byte)EnumColor.RED.getColorIndex()));
  }
  
  protected void a(BlockPosition blockposition, Block block)
  {
    makeSound("mob.wolf.step", 0.15F, 1.0F);
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setBoolean("Angry", isAngry());
    nbttagcompound.setByte("CollarColor", (byte)getCollarColor().getInvColorIndex());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    setAngry(nbttagcompound.getBoolean("Angry"));
    if (nbttagcompound.hasKeyOfType("CollarColor", 99)) {
      setCollarColor(EnumColor.fromInvColorIndex(nbttagcompound.getByte("CollarColor")));
    }
  }
  
  protected String z()
  {
    return this.random.nextInt(3) == 0 ? "mob.wolf.panting" : (isTamed()) && (this.datawatcher.getFloat(18) < getMaxHealth() / 2.0F) ? "mob.wolf.whine" : isAngry() ? "mob.wolf.growl" : "mob.wolf.bark";
  }
  
  protected String bo()
  {
    return "mob.wolf.hurt";
  }
  
  protected String bp()
  {
    return "mob.wolf.death";
  }
  
  protected float bB()
  {
    return 0.4F;
  }
  
  protected Item getLoot()
  {
    return Item.getById(-1);
  }
  
  public void m()
  {
    super.m();
    if ((!this.world.isClientSide) && (this.bq) && (!this.br) && (!cf()) && (this.onGround))
    {
      this.br = true;
      this.bs = 0.0F;
      this.bt = 0.0F;
      this.world.broadcastEntityEffect(this, (byte)8);
    }
    if ((!this.world.isClientSide) && (getGoalTarget() == null) && (isAngry())) {
      setAngry(false);
    }
  }
  
  public void t_()
  {
    super.t_();
    this.bp = this.bo;
    if (cx()) {
      this.bo += (1.0F - this.bo) * 0.4F;
    } else {
      this.bo += (0.0F - this.bo) * 0.4F;
    }
    if (U())
    {
      this.bq = true;
      this.br = false;
      this.bs = 0.0F;
      this.bt = 0.0F;
    }
    else if (((this.bq) || (this.br)) && (this.br))
    {
      if (this.bs == 0.0F) {
        makeSound("mob.wolf.shake", bB(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
      }
      this.bt = this.bs;
      this.bs += 0.05F;
      if (this.bt >= 2.0F)
      {
        this.bq = false;
        this.br = false;
        this.bt = 0.0F;
        this.bs = 0.0F;
      }
      if (this.bs > 0.4F)
      {
        float f = (float)getBoundingBox().b;
        int i = (int)(MathHelper.sin((this.bs - 0.4F) * 3.1415927F) * 7.0F);
        for (int j = 0; j < i; j++)
        {
          float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
          float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
          
          this.world.addParticle(EnumParticle.WATER_SPLASH, this.locX + f1, f + 0.8F, this.locZ + f2, this.motX, this.motY, this.motZ, new int[0]);
        }
      }
    }
  }
  
  public float getHeadHeight()
  {
    return this.length * 0.8F;
  }
  
  public int bQ()
  {
    return isSitting() ? 20 : super.bQ();
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    if (isInvulnerable(damagesource)) {
      return false;
    }
    Entity entity = damagesource.getEntity();
    if ((entity != null) && (!(entity instanceof EntityHuman)) && (!(entity instanceof EntityArrow))) {
      f = (f + 1.0F) / 2.0F;
    }
    return super.damageEntity(damagesource, f);
  }
  
  public boolean r(Entity entity)
  {
    boolean flag = entity.damageEntity(DamageSource.mobAttack(this), (int)getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue());
    if (flag) {
      a(this, entity);
    }
    return flag;
  }
  
  public void setTamed(boolean flag)
  {
    super.setTamed(flag);
    if (flag) {
      getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
    } else {
      getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
    }
    getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(4.0D);
  }
  
  public boolean a(EntityHuman entityhuman)
  {
    ItemStack itemstack = entityhuman.inventory.getItemInHand();
    if (isTamed())
    {
      if (itemstack != null) {
        if ((itemstack.getItem() instanceof ItemFood))
        {
          ItemFood itemfood = (ItemFood)itemstack.getItem();
          if ((itemfood.g()) && (this.datawatcher.getFloat(18) < 20.0F))
          {
            if (!entityhuman.abilities.canInstantlyBuild) {
              itemstack.count -= 1;
            }
            heal(itemfood.getNutrition(itemstack), EntityRegainHealthEvent.RegainReason.EATING);
            if (itemstack.count <= 0) {
              entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
            }
            return true;
          }
        }
        else if (itemstack.getItem() == Items.DYE)
        {
          EnumColor enumcolor = EnumColor.fromInvColorIndex(itemstack.getData());
          if (enumcolor != getCollarColor())
          {
            setCollarColor(enumcolor);
            if (!entityhuman.abilities.canInstantlyBuild) {
              if (--itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
              }
            }
            return true;
          }
        }
      }
      if ((e(entityhuman)) && (!this.world.isClientSide) && (!d(itemstack)))
      {
        this.bm.setSitting(!isSitting());
        this.aY = false;
        this.navigation.n();
        setGoalTarget(null, EntityTargetEvent.TargetReason.FORGOT_TARGET, true);
      }
    }
    else if ((itemstack != null) && (itemstack.getItem() == Items.BONE) && (!isAngry()))
    {
      if (!entityhuman.abilities.canInstantlyBuild) {
        itemstack.count -= 1;
      }
      if (itemstack.count <= 0) {
        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
      }
      if (!this.world.isClientSide) {
        if ((this.random.nextInt(3) == 0) && (!CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()))
        {
          setTamed(true);
          this.navigation.n();
          setGoalTarget(null, EntityTargetEvent.TargetReason.FORGOT_TARGET, true);
          this.bm.setSitting(true);
          setHealth(getMaxHealth());
          setOwnerUUID(entityhuman.getUniqueID().toString());
          l(true);
          this.world.broadcastEntityEffect(this, (byte)7);
        }
        else
        {
          l(false);
          this.world.broadcastEntityEffect(this, (byte)6);
        }
      }
      return true;
    }
    return super.a(entityhuman);
  }
  
  public boolean d(ItemStack itemstack)
  {
    return !(itemstack.getItem() instanceof ItemFood) ? false : itemstack == null ? false : ((ItemFood)itemstack.getItem()).g();
  }
  
  public int bV()
  {
    return 8;
  }
  
  public boolean isAngry()
  {
    return (this.datawatcher.getByte(16) & 0x2) != 0;
  }
  
  public void setAngry(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(16);
    if (flag) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(b0 | 0x2)));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFD)));
    }
  }
  
  public EnumColor getCollarColor()
  {
    return EnumColor.fromInvColorIndex(this.datawatcher.getByte(20) & 0xF);
  }
  
  public void setCollarColor(EnumColor enumcolor)
  {
    this.datawatcher.watch(20, Byte.valueOf((byte)(enumcolor.getInvColorIndex() & 0xF)));
  }
  
  public EntityWolf b(EntityAgeable entityageable)
  {
    EntityWolf entitywolf = new EntityWolf(this.world);
    String s = getOwnerUUID();
    if ((s != null) && (s.trim().length() > 0))
    {
      entitywolf.setOwnerUUID(s);
      entitywolf.setTamed(true);
    }
    return entitywolf;
  }
  
  public void p(boolean flag)
  {
    if (flag) {
      this.datawatcher.watch(19, Byte.valueOf((byte)1));
    } else {
      this.datawatcher.watch(19, Byte.valueOf((byte)0));
    }
  }
  
  public boolean mate(EntityAnimal entityanimal)
  {
    if (entityanimal == this) {
      return false;
    }
    if (!isTamed()) {
      return false;
    }
    if (!(entityanimal instanceof EntityWolf)) {
      return false;
    }
    EntityWolf entitywolf = (EntityWolf)entityanimal;
    
    return entitywolf.isTamed();
  }
  
  public boolean cx()
  {
    return this.datawatcher.getByte(19) == 1;
  }
  
  protected boolean isTypeNotPersistent()
  {
    return !isTamed();
  }
  
  public boolean a(EntityLiving entityliving, EntityLiving entityliving1)
  {
    if ((!(entityliving instanceof EntityCreeper)) && (!(entityliving instanceof EntityGhast)))
    {
      if ((entityliving instanceof EntityWolf))
      {
        EntityWolf entitywolf = (EntityWolf)entityliving;
        if ((entitywolf.isTamed()) && (entitywolf.getOwner() == entityliving1)) {
          return false;
        }
      }
      return (!(entityliving instanceof EntityHuman)) || (!(entityliving1 instanceof EntityHuman)) || (((EntityHuman)entityliving1).a((EntityHuman)entityliving));
    }
    return false;
  }
  
  public boolean cb()
  {
    return (!isAngry()) && (super.cb());
  }
  
  public EntityAgeable createChild(EntityAgeable entityageable)
  {
    return b(entityageable);
  }
}
