package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EntityEndermite
  extends EntityMonster
{
  private int a = 0;
  private boolean b = false;
  
  public EntityEndermite(World ☃)
  {
    super(☃);
    this.b_ = 3;
    
    setSize(0.4F, 0.3F);
    
    this.goalSelector.a(1, new PathfinderGoalFloat(this));
    this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
    this.goalSelector.a(3, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    
    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
  }
  
  public float getHeadHeight()
  {
    return 0.1F;
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    
    getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
    getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(2.0D);
  }
  
  protected boolean s_()
  {
    return false;
  }
  
  protected String z()
  {
    return "mob.silverfish.say";
  }
  
  protected String bo()
  {
    return "mob.silverfish.hit";
  }
  
  protected String bp()
  {
    return "mob.silverfish.kill";
  }
  
  protected void a(BlockPosition ☃, Block ☃)
  {
    makeSound("mob.silverfish.step", 0.15F, 1.0F);
  }
  
  protected Item getLoot()
  {
    return null;
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    this.a = ☃.getInt("Lifetime");
    this.b = ☃.getBoolean("PlayerSpawned");
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    ☃.setInt("Lifetime", this.a);
    ☃.setBoolean("PlayerSpawned", this.b);
  }
  
  public void t_()
  {
    this.aI = this.yaw;
    
    super.t_();
  }
  
  public boolean n()
  {
    return this.b;
  }
  
  public void a(boolean ☃)
  {
    this.b = ☃;
  }
  
  public void m()
  {
    super.m();
    if (this.world.isClientSide)
    {
      for (int ☃ = 0; ☃ < 2; ☃++) {
        this.world.addParticle(EnumParticle.PORTAL, this.locX + (this.random.nextDouble() - 0.5D) * this.width, this.locY + this.random.nextDouble() * this.length, this.locZ + (this.random.nextDouble() - 0.5D) * this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D, new int[0]);
      }
    }
    else
    {
      if (!isPersistent()) {
        this.a += 1;
      }
      if (this.a >= 2400) {
        die();
      }
    }
  }
  
  protected boolean n_()
  {
    return true;
  }
  
  public boolean bR()
  {
    if (super.bR())
    {
      EntityHuman ☃ = this.world.findNearbyPlayer(this, 5.0D);
      return ☃ == null;
    }
    return false;
  }
  
  public EnumMonsterType getMonsterType()
  {
    return EnumMonsterType.ARTHROPOD;
  }
}
