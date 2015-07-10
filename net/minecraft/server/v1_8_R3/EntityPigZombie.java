package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EntityPigZombie
  extends EntityZombie
{
  private static final UUID b = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
  private static final AttributeModifier c = new AttributeModifier(b, "Attacking speed boost", 0.05D, 0).a(false);
  public int angerLevel;
  private int soundDelay;
  private UUID hurtBy;
  
  public EntityPigZombie(World ☃)
  {
    super(☃);
    this.fireProof = true;
  }
  
  public void b(EntityLiving ☃)
  {
    super.b(☃);
    if (☃ != null) {
      this.hurtBy = ☃.getUniqueID();
    }
  }
  
  protected void n()
  {
    this.targetSelector.a(1, new PathfinderGoalAngerOther(this));
    this.targetSelector.a(2, new PathfinderGoalAnger(this));
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    
    getAttributeInstance(a).setValue(0.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D);
    getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(5.0D);
  }
  
  public void t_()
  {
    super.t_();
  }
  
  protected void E()
  {
    AttributeInstance ☃ = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
    if (cm())
    {
      if ((!isBaby()) && (!☃.a(c))) {
        ☃.b(c);
      }
      this.angerLevel -= 1;
    }
    else if (☃.a(c))
    {
      ☃.c(c);
    }
    if ((this.soundDelay > 0) && 
      (--this.soundDelay == 0)) {
      makeSound("mob.zombiepig.zpigangry", bB() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
    }
    if ((this.angerLevel > 0) && 
      (this.hurtBy != null) && (getLastDamager() == null))
    {
      EntityHuman ☃ = this.world.b(this.hurtBy);
      b(☃);
      this.killer = ☃;
      this.lastDamageByPlayerTime = be();
    }
    super.E();
  }
  
  public boolean bR()
  {
    return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
  }
  
  public boolean canSpawn()
  {
    return (this.world.a(getBoundingBox(), this)) && (this.world.getCubes(this, getBoundingBox()).isEmpty()) && (!this.world.containsLiquid(getBoundingBox()));
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    ☃.setShort("Anger", (short)this.angerLevel);
    if (this.hurtBy != null) {
      ☃.setString("HurtBy", this.hurtBy.toString());
    } else {
      ☃.setString("HurtBy", "");
    }
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    this.angerLevel = ☃.getShort("Anger");
    String ☃ = ☃.getString("HurtBy");
    if (☃.length() > 0)
    {
      this.hurtBy = UUID.fromString(☃);
      
      EntityHuman ☃ = this.world.b(this.hurtBy);
      b(☃);
      if (☃ != null)
      {
        this.killer = ☃;
        this.lastDamageByPlayerTime = be();
      }
    }
  }
  
  public boolean damageEntity(DamageSource ☃, float ☃)
  {
    if (isInvulnerable(☃)) {
      return false;
    }
    Entity ☃ = ☃.getEntity();
    if ((☃ instanceof EntityHuman)) {
      b(☃);
    }
    return super.damageEntity(☃, ☃);
  }
  
  private void b(Entity ☃)
  {
    this.angerLevel = (400 + this.random.nextInt(400));
    this.soundDelay = this.random.nextInt(40);
    if ((☃ instanceof EntityLiving)) {
      b((EntityLiving)☃);
    }
  }
  
  public boolean cm()
  {
    return this.angerLevel > 0;
  }
  
  protected String z()
  {
    return "mob.zombiepig.zpig";
  }
  
  protected String bo()
  {
    return "mob.zombiepig.zpighurt";
  }
  
  protected String bp()
  {
    return "mob.zombiepig.zpigdeath";
  }
  
  protected void dropDeathLoot(boolean ☃, int ☃)
  {
    int ☃ = this.random.nextInt(2 + ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      a(Items.ROTTEN_FLESH, 1);
    }
    ☃ = this.random.nextInt(2 + ☃);
    for (int ☃ = 0; ☃ < ☃; ☃++) {
      a(Items.GOLD_NUGGET, 1);
    }
  }
  
  public boolean a(EntityHuman ☃)
  {
    return false;
  }
  
  protected void getRareDrop()
  {
    a(Items.GOLD_INGOT, 1);
  }
  
  protected void a(DifficultyDamageScaler ☃)
  {
    setEquipment(0, new ItemStack(Items.GOLDEN_SWORD));
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler ☃, GroupDataEntity ☃)
  {
    super.prepare(☃, ☃);
    setVillager(false);
    return ☃;
  }
  
  static class PathfinderGoalAngerOther
    extends PathfinderGoalHurtByTarget
  {
    public PathfinderGoalAngerOther(EntityPigZombie ☃)
    {
      super(true, new Class[0]);
    }
    
    protected void a(EntityCreature ☃, EntityLiving ☃)
    {
      super.a(☃, ☃);
      if ((☃ instanceof EntityPigZombie)) {
        EntityPigZombie.a((EntityPigZombie)☃, ☃);
      }
    }
  }
  
  static class PathfinderGoalAnger
    extends PathfinderGoalNearestAttackableTarget<EntityHuman>
  {
    public PathfinderGoalAnger(EntityPigZombie ☃)
    {
      super(EntityHuman.class, true);
    }
    
    public boolean a()
    {
      return (((EntityPigZombie)this.e).cm()) && (super.a());
    }
  }
}
