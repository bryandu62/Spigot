package net.minecraft.server.v1_8_R3;

import java.util.Calendar;
import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.PluginManager;

public class EntitySkeleton
  extends EntityMonster
  implements IRangedEntity
{
  private PathfinderGoalArrowAttack a = new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F);
  private PathfinderGoalMeleeAttack b = new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.2D, false);
  
  public EntitySkeleton(World world)
  {
    super(world);
    this.goalSelector.a(1, new PathfinderGoalFloat(this));
    this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
    this.goalSelector.a(3, new PathfinderGoalFleeSun(this, 1.0D));
    this.goalSelector.a(3, new PathfinderGoalAvoidTarget(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
    this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityIronGolem.class, true));
    if ((world != null) && (!world.isClientSide)) {
      n();
    }
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(13, new Byte((byte)0));
  }
  
  protected String z()
  {
    return "mob.skeleton.say";
  }
  
  protected String bo()
  {
    return "mob.skeleton.hurt";
  }
  
  protected String bp()
  {
    return "mob.skeleton.death";
  }
  
  protected void a(BlockPosition blockposition, Block block)
  {
    makeSound("mob.skeleton.step", 0.15F, 1.0F);
  }
  
  public boolean r(Entity entity)
  {
    if (super.r(entity))
    {
      if ((getSkeletonType() == 1) && ((entity instanceof EntityLiving))) {
        ((EntityLiving)entity).addEffect(new MobEffect(MobEffectList.WITHER.id, 200));
      }
      return true;
    }
    return false;
  }
  
  public EnumMonsterType getMonsterType()
  {
    return EnumMonsterType.UNDEAD;
  }
  
  public void m()
  {
    if ((this.world.w()) && (!this.world.isClientSide))
    {
      float f = c(1.0F);
      BlockPosition blockposition = new BlockPosition(this.locX, Math.round(this.locY), this.locZ);
      if ((f > 0.5F) && (this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) && (this.world.i(blockposition)))
      {
        boolean flag = true;
        ItemStack itemstack = getEquipment(4);
        if (itemstack != null)
        {
          if (itemstack.e())
          {
            itemstack.setData(itemstack.h() + this.random.nextInt(2));
            if (itemstack.h() >= itemstack.j())
            {
              b(itemstack);
              setEquipment(4, null);
            }
          }
          flag = false;
        }
        if (flag)
        {
          EntityCombustEvent event = new EntityCombustEvent(getBukkitEntity(), 8);
          this.world.getServer().getPluginManager().callEvent(event);
          if (!event.isCancelled()) {
            setOnFire(event.getDuration());
          }
        }
      }
    }
    if ((this.world.isClientSide) && (getSkeletonType() == 1)) {
      setSize(0.72F, 2.535F);
    }
    super.m();
  }
  
  public void ak()
  {
    super.ak();
    if ((this.vehicle instanceof EntityCreature))
    {
      EntityCreature entitycreature = (EntityCreature)this.vehicle;
      
      this.aI = entitycreature.aI;
    }
  }
  
  public void die(DamageSource damagesource)
  {
    if (((damagesource.i() instanceof EntityArrow)) && ((damagesource.getEntity() instanceof EntityHuman)))
    {
      EntityHuman entityhuman = (EntityHuman)damagesource.getEntity();
      double d0 = entityhuman.locX - this.locX;
      double d1 = entityhuman.locZ - this.locZ;
      if (d0 * d0 + d1 * d1 >= 2500.0D) {
        entityhuman.b(AchievementList.v);
      }
    }
    else if (((damagesource.getEntity() instanceof EntityCreeper)) && (((EntityCreeper)damagesource.getEntity()).isPowered()) && (((EntityCreeper)damagesource.getEntity()).cp()))
    {
      ((EntityCreeper)damagesource.getEntity()).cq();
      
      this.headDrop = new ItemStack(Items.SKULL, 1, getSkeletonType() == 1 ? 1 : 0);
    }
    super.die(damagesource);
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    super.dropDeathLoot(flag, i);
    if (getSkeletonType() == 1)
    {
      int j = this.random.nextInt(3 + i) - 1;
      for (int k = 0; k < j; k++) {
        a(Items.COAL, 1);
      }
    }
    else
    {
      j = this.random.nextInt(3 + i);
      for (k = 0; k < j; k++) {
        a(Items.ARROW, 1);
      }
    }
    int j = this.random.nextInt(3 + i);
    for (int k = 0; k < j; k++) {
      a(Items.BONE, 1);
    }
  }
  
  protected void getRareDrop()
  {
    if (getSkeletonType() == 1) {
      a(new ItemStack(Items.SKULL, 1, 1), 0.0F);
    }
  }
  
  protected void a(DifficultyDamageScaler difficultydamagescaler)
  {
    super.a(difficultydamagescaler);
    setEquipment(0, new ItemStack(Items.BOW));
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, GroupDataEntity groupdataentity)
  {
    groupdataentity = super.prepare(difficultydamagescaler, groupdataentity);
    if (((this.world.worldProvider instanceof WorldProviderHell)) && (bc().nextInt(5) > 0))
    {
      this.goalSelector.a(4, this.b);
      setSkeletonType(1);
      setEquipment(0, new ItemStack(Items.STONE_SWORD));
      getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(4.0D);
    }
    else
    {
      this.goalSelector.a(4, this.a);
      a(difficultydamagescaler);
      b(difficultydamagescaler);
    }
    j(this.random.nextFloat() < 0.55F * difficultydamagescaler.c());
    if (getEquipment(4) == null)
    {
      Calendar calendar = this.world.Y();
      if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31) && (this.random.nextFloat() < 0.25F))
      {
        setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
        this.dropChances[4] = 0.0F;
      }
    }
    return groupdataentity;
  }
  
  public void n()
  {
    this.goalSelector.a(this.b);
    this.goalSelector.a(this.a);
    ItemStack itemstack = bA();
    if ((itemstack != null) && (itemstack.getItem() == Items.BOW)) {
      this.goalSelector.a(4, this.a);
    } else {
      this.goalSelector.a(4, this.b);
    }
  }
  
  public void a(EntityLiving entityliving, float f)
  {
    EntityArrow entityarrow = new EntityArrow(this.world, this, entityliving, 1.6F, 14 - this.world.getDifficulty().a() * 4);
    int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, bA());
    int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, bA());
    
    entityarrow.b(f * 2.0F + this.random.nextGaussian() * 0.25D + this.world.getDifficulty().a() * 0.11F);
    if (i > 0) {
      entityarrow.b(entityarrow.j() + i * 0.5D + 0.5D);
    }
    if (j > 0) {
      entityarrow.setKnockbackStrength(j);
    }
    if ((EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, bA()) > 0) || (getSkeletonType() == 1))
    {
      EntityCombustEvent event = new EntityCombustEvent(entityarrow.getBukkitEntity(), 100);
      this.world.getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
        entityarrow.setOnFire(event.getDuration());
      }
    }
    EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, bA(), entityarrow, 0.8F);
    if (event.isCancelled())
    {
      event.getProjectile().remove();
      return;
    }
    if (event.getProjectile() == entityarrow.getBukkitEntity()) {
      this.world.addEntity(entityarrow);
    }
    makeSound("random.bow", 1.0F, 1.0F / (bc().nextFloat() * 0.4F + 0.8F));
  }
  
  public int getSkeletonType()
  {
    return this.datawatcher.getByte(13);
  }
  
  public void setSkeletonType(int i)
  {
    this.datawatcher.watch(13, Byte.valueOf((byte)i));
    this.fireProof = (i == 1);
    if (i == 1) {
      setSize(0.72F, 2.535F);
    } else {
      setSize(0.6F, 1.95F);
    }
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    if (nbttagcompound.hasKeyOfType("SkeletonType", 99))
    {
      byte b0 = nbttagcompound.getByte("SkeletonType");
      
      setSkeletonType(b0);
    }
    n();
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setByte("SkeletonType", (byte)getSkeletonType());
  }
  
  public void setEquipment(int i, ItemStack itemstack)
  {
    super.setEquipment(i, itemstack);
    if ((!this.world.isClientSide) && (i == 0)) {
      n();
    }
  }
  
  public float getHeadHeight()
  {
    return getSkeletonType() == 1 ? super.getHeadHeight() : 1.74F;
  }
  
  public double am()
  {
    return isBaby() ? 0.0D : -0.35D;
  }
}
