package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EntityBlaze
  extends EntityMonster
{
  private float a = 0.5F;
  private int b;
  
  public EntityBlaze(World ☃)
  {
    super(☃);
    
    this.fireProof = true;
    this.b_ = 10;
    
    this.goalSelector.a(4, new PathfinderGoalBlazeFireball(this));
    this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
    this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    
    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(6.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D);
    getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(48.0D);
  }
  
  protected void h()
  {
    super.h();
    
    this.datawatcher.a(16, new Byte((byte)0));
  }
  
  protected String z()
  {
    return "mob.blaze.breathe";
  }
  
  protected String bo()
  {
    return "mob.blaze.hit";
  }
  
  protected String bp()
  {
    return "mob.blaze.death";
  }
  
  public float c(float ☃)
  {
    return 1.0F;
  }
  
  public void m()
  {
    if ((!this.onGround) && (this.motY < 0.0D)) {
      this.motY *= 0.6D;
    }
    if (this.world.isClientSide)
    {
      if ((this.random.nextInt(24) == 0) && (!R())) {
        this.world.a(this.locX + 0.5D, this.locY + 0.5D, this.locZ + 0.5D, "fire.fire", 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
      }
      for (int ☃ = 0; ☃ < 2; ☃++) {
        this.world.addParticle(EnumParticle.SMOKE_LARGE, this.locX + (this.random.nextDouble() - 0.5D) * this.width, this.locY + this.random.nextDouble() * this.length, this.locZ + (this.random.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
      }
    }
    super.m();
  }
  
  protected void E()
  {
    if (U()) {
      damageEntity(DamageSource.DROWN, 1.0F);
    }
    this.b -= 1;
    if (this.b <= 0)
    {
      this.b = 100;
      this.a = (0.5F + (float)this.random.nextGaussian() * 3.0F);
    }
    EntityLiving ☃ = getGoalTarget();
    if ((☃ != null) && (☃.locY + ☃.getHeadHeight() > this.locY + getHeadHeight() + this.a))
    {
      this.motY += (0.30000001192092896D - this.motY) * 0.30000001192092896D;
      this.ai = true;
    }
    super.E();
  }
  
  public void e(float ☃, float ☃) {}
  
  protected Item getLoot()
  {
    return Items.BLAZE_ROD;
  }
  
  public boolean isBurning()
  {
    return n();
  }
  
  protected void dropDeathLoot(boolean ☃, int ☃)
  {
    if (☃)
    {
      int ☃ = this.random.nextInt(2 + ☃);
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        a(Items.BLAZE_ROD, 1);
      }
    }
  }
  
  public boolean n()
  {
    return (this.datawatcher.getByte(16) & 0x1) != 0;
  }
  
  public void a(boolean ☃)
  {
    byte ☃ = this.datawatcher.getByte(16);
    if (☃) {
      ☃ = (byte)(☃ | 0x1);
    } else {
      ☃ = (byte)(☃ & 0xFFFFFFFE);
    }
    this.datawatcher.watch(16, Byte.valueOf(☃));
  }
  
  protected boolean n_()
  {
    return true;
  }
  
  static class PathfinderGoalBlazeFireball
    extends PathfinderGoal
  {
    private EntityBlaze a;
    private int b;
    private int c;
    
    public PathfinderGoalBlazeFireball(EntityBlaze ☃)
    {
      this.a = ☃;
      
      a(3);
    }
    
    public boolean a()
    {
      EntityLiving ☃ = this.a.getGoalTarget();
      if ((☃ == null) || (!☃.isAlive())) {
        return false;
      }
      return true;
    }
    
    public void c()
    {
      this.b = 0;
    }
    
    public void d()
    {
      this.a.a(false);
    }
    
    public void e()
    {
      this.c -= 1;
      
      EntityLiving ☃ = this.a.getGoalTarget();
      
      double ☃ = this.a.h(☃);
      if (☃ < 4.0D)
      {
        if (this.c <= 0)
        {
          this.c = 20;
          this.a.r(☃);
        }
        this.a.getControllerMove().a(☃.locX, ☃.locY, ☃.locZ, 1.0D);
      }
      else if (☃ < 256.0D)
      {
        double ☃ = ☃.locX - this.a.locX;
        double ☃ = ☃.getBoundingBox().b + ☃.length / 2.0F - (this.a.locY + this.a.length / 2.0F);
        double ☃ = ☃.locZ - this.a.locZ;
        if (this.c <= 0)
        {
          this.b += 1;
          if (this.b == 1)
          {
            this.c = 60;
            this.a.a(true);
          }
          else if (this.b <= 4)
          {
            this.c = 6;
          }
          else
          {
            this.c = 100;
            this.b = 0;
            this.a.a(false);
          }
          if (this.b > 1)
          {
            float ☃ = MathHelper.c(MathHelper.sqrt(☃)) * 0.5F;
            
            this.a.world.a(null, 1009, new BlockPosition((int)this.a.locX, (int)this.a.locY, (int)this.a.locZ), 0);
            for (int ☃ = 0; ☃ < 1; ☃++)
            {
              EntitySmallFireball ☃ = new EntitySmallFireball(this.a.world, this.a, ☃ + this.a.bc().nextGaussian() * ☃, ☃, ☃ + this.a.bc().nextGaussian() * ☃);
              ☃.locY = (this.a.locY + this.a.length / 2.0F + 0.5D);
              this.a.world.addEntity(☃);
            }
          }
        }
        this.a.getControllerLook().a(☃, 10.0F, 10.0F);
      }
      else
      {
        this.a.getNavigation().n();
        this.a.getControllerMove().a(☃.locX, ☃.locY, ☃.locZ, 1.0D);
      }
      super.e();
    }
  }
}
