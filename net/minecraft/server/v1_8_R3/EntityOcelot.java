package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTameEvent;

public class EntityOcelot
  extends EntityTameableAnimal
{
  private PathfinderGoalAvoidTarget<EntityHuman> bo;
  private PathfinderGoalTempt bp;
  public boolean spawnBonus = true;
  
  public EntityOcelot(World world)
  {
    super(world);
    setSize(0.6F, 0.7F);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(1, new PathfinderGoalFloat(this));
    this.goalSelector.a(2, this.bm);
    this.goalSelector.a(3, this.bp = new PathfinderGoalTempt(this, 0.6D, Items.FISH, true));
    this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 5.0F));
    this.goalSelector.a(6, new PathfinderGoalJumpOnBlock(this, 0.8D));
    this.goalSelector.a(7, new PathfinderGoalLeapAtTarget(this, 0.3F));
    this.goalSelector.a(8, new PathfinderGoalOcelotAttack(this));
    this.goalSelector.a(9, new PathfinderGoalBreed(this, 0.8D));
    this.goalSelector.a(10, new PathfinderGoalRandomStroll(this, 0.8D));
    this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
    this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed(this, EntityChicken.class, false, null));
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(18, Byte.valueOf((byte)0));
  }
  
  public void E()
  {
    if (getControllerMove().a())
    {
      double d0 = getControllerMove().b();
      if (d0 == 0.6D)
      {
        setSneaking(true);
        setSprinting(false);
      }
      else if (d0 == 1.33D)
      {
        setSneaking(false);
        setSprinting(true);
      }
      else
      {
        setSneaking(false);
        setSprinting(false);
      }
    }
    else
    {
      setSneaking(false);
      setSprinting(false);
    }
  }
  
  protected boolean isTypeNotPersistent()
  {
    return !isTamed();
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
  }
  
  public void e(float f, float f1) {}
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("CatType", getCatType());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    setCatType(nbttagcompound.getInt("CatType"));
  }
  
  protected String z()
  {
    return isTamed() ? "mob.cat.meow" : this.random.nextInt(4) == 0 ? "mob.cat.purreow" : isInLove() ? "mob.cat.purr" : "";
  }
  
  protected String bo()
  {
    return "mob.cat.hitt";
  }
  
  protected String bp()
  {
    return "mob.cat.hitt";
  }
  
  protected float bB()
  {
    return 0.4F;
  }
  
  protected Item getLoot()
  {
    return Items.LEATHER;
  }
  
  public boolean r(Entity entity)
  {
    return entity.damageEntity(DamageSource.mobAttack(this), 3.0F);
  }
  
  protected void dropDeathLoot(boolean flag, int i) {}
  
  public boolean a(EntityHuman entityhuman)
  {
    ItemStack itemstack = entityhuman.inventory.getItemInHand();
    if (isTamed())
    {
      if ((e(entityhuman)) && (!this.world.isClientSide) && (!d(itemstack))) {
        this.bm.setSitting(!isSitting());
      }
    }
    else if ((this.bp.f()) && (itemstack != null) && (itemstack.getItem() == Items.FISH) && (entityhuman.h(this) < 9.0D))
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
          setCatType(1 + this.world.random.nextInt(3));
          setOwnerUUID(entityhuman.getUniqueID().toString());
          l(true);
          this.bm.setSitting(true);
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
  
  public EntityOcelot b(EntityAgeable entityageable)
  {
    EntityOcelot entityocelot = new EntityOcelot(this.world);
    if (isTamed())
    {
      entityocelot.setOwnerUUID(getOwnerUUID());
      entityocelot.setTamed(true);
      entityocelot.setCatType(getCatType());
    }
    return entityocelot;
  }
  
  public boolean d(ItemStack itemstack)
  {
    return (itemstack != null) && (itemstack.getItem() == Items.FISH);
  }
  
  public boolean mate(EntityAnimal entityanimal)
  {
    if (entityanimal == this) {
      return false;
    }
    if (!isTamed()) {
      return false;
    }
    if (!(entityanimal instanceof EntityOcelot)) {
      return false;
    }
    EntityOcelot entityocelot = (EntityOcelot)entityanimal;
    
    return entityocelot.isTamed();
  }
  
  public int getCatType()
  {
    return this.datawatcher.getByte(18);
  }
  
  public void setCatType(int i)
  {
    this.datawatcher.watch(18, Byte.valueOf((byte)i));
  }
  
  public boolean bR()
  {
    return this.world.random.nextInt(3) != 0;
  }
  
  public boolean canSpawn()
  {
    if ((this.world.a(getBoundingBox(), this)) && (this.world.getCubes(this, getBoundingBox()).isEmpty()) && (!this.world.containsLiquid(getBoundingBox())))
    {
      BlockPosition blockposition = new BlockPosition(this.locX, getBoundingBox().b, this.locZ);
      if (blockposition.getY() < this.world.F()) {
        return false;
      }
      Block block = this.world.getType(blockposition.down()).getBlock();
      if ((block == Blocks.GRASS) || (block.getMaterial() == Material.LEAVES)) {
        return true;
      }
    }
    return false;
  }
  
  public String getName()
  {
    return isTamed() ? LocaleI18n.get("entity.Cat.name") : hasCustomName() ? getCustomName() : super.getName();
  }
  
  public void setTamed(boolean flag)
  {
    super.setTamed(flag);
  }
  
  protected void cm()
  {
    if (this.bo == null) {
      this.bo = new PathfinderGoalAvoidTarget(this, EntityHuman.class, 16.0F, 0.8D, 1.33D);
    }
    this.goalSelector.a(this.bo);
    if (!isTamed()) {
      this.goalSelector.a(4, this.bo);
    }
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, GroupDataEntity groupdataentity)
  {
    groupdataentity = super.prepare(difficultydamagescaler, groupdataentity);
    if ((this.spawnBonus) && (this.world.random.nextInt(7) == 0)) {
      for (int i = 0; i < 2; i++)
      {
        EntityOcelot entityocelot = new EntityOcelot(this.world);
        
        entityocelot.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
        entityocelot.setAgeRaw(41536);
        this.world.addEntity(entityocelot, CreatureSpawnEvent.SpawnReason.OCELOT_BABY);
      }
    }
    return groupdataentity;
  }
  
  public EntityAgeable createChild(EntityAgeable entityageable)
  {
    return b(entityageable);
  }
}
