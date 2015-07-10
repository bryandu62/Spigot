package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;

public class EntityGuardian
  extends EntityMonster
{
  private float a;
  private float b;
  private float c;
  private float bm;
  private float bn;
  private EntityLiving bo;
  private int bp;
  private boolean bq;
  public PathfinderGoalRandomStroll goalRandomStroll;
  
  public EntityGuardian(World ☃)
  {
    super(☃);
    
    this.b_ = 10;
    setSize(0.85F, 0.85F);
    
    this.goalSelector.a(4, new PathfinderGoalGuardianAttack(this));
    PathfinderGoalMoveTowardsRestriction ☃;
    this.goalSelector.a(5, ☃ = new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
    this.goalSelector.a(7, this.goalRandomStroll = new PathfinderGoalRandomStroll(this, 1.0D, 80));
    this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityGuardian.class, 12.0F, 0.01F));
    this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
    
    this.goalRandomStroll.a(3);
    ☃.a(3);
    
    this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityLiving.class, 10, true, false, new EntitySelectorGuardianTargetHumanSquid(this)));
    
    this.moveController = new ControllerMoveGuardian(this);
    
    this.b = (this.a = this.random.nextFloat());
  }
  
  public void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(6.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
    getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
    getAttributeInstance(GenericAttributes.maxHealth).setValue(30.0D);
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    
    setElder(☃.getBoolean("Elder"));
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    
    ☃.setBoolean("Elder", isElder());
  }
  
  protected NavigationAbstract b(World ☃)
  {
    return new NavigationGuardian(this, ☃);
  }
  
  protected void h()
  {
    super.h();
    
    this.datawatcher.a(16, Integer.valueOf(0));
    this.datawatcher.a(17, Integer.valueOf(0));
  }
  
  private boolean a(int ☃)
  {
    return (this.datawatcher.getInt(16) & ☃) != 0;
  }
  
  private void a(int ☃, boolean ☃)
  {
    int ☃ = this.datawatcher.getInt(16);
    if (☃) {
      this.datawatcher.watch(16, Integer.valueOf(☃ | ☃));
    } else {
      this.datawatcher.watch(16, Integer.valueOf(☃ & (☃ ^ 0xFFFFFFFF)));
    }
  }
  
  public boolean n()
  {
    return a(2);
  }
  
  private void l(boolean ☃)
  {
    a(2, ☃);
  }
  
  public int cm()
  {
    if (isElder()) {
      return 60;
    }
    return 80;
  }
  
  public boolean isElder()
  {
    return a(4);
  }
  
  public void setElder(boolean ☃)
  {
    a(4, ☃);
    if (☃)
    {
      setSize(1.9975F, 1.9975F);
      getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
      getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(8.0D);
      getAttributeInstance(GenericAttributes.maxHealth).setValue(80.0D);
      bX();
      
      this.goalRandomStroll.setTimeBetweenMovement(400);
    }
  }
  
  private void b(int ☃)
  {
    this.datawatcher.watch(17, Integer.valueOf(☃));
  }
  
  public boolean cp()
  {
    return this.datawatcher.getInt(17) != 0;
  }
  
  public EntityLiving cq()
  {
    if (!cp()) {
      return null;
    }
    if (this.world.isClientSide)
    {
      if (this.bo != null) {
        return this.bo;
      }
      Entity ☃ = this.world.a(this.datawatcher.getInt(17));
      if ((☃ instanceof EntityLiving))
      {
        this.bo = ((EntityLiving)☃);
        return this.bo;
      }
      return null;
    }
    return getGoalTarget();
  }
  
  public void i(int ☃)
  {
    super.i(☃);
    if (☃ == 16)
    {
      if ((isElder()) && (this.width < 1.0F)) {
        setSize(1.9975F, 1.9975F);
      }
    }
    else if (☃ == 17)
    {
      this.bp = 0;
      this.bo = null;
    }
  }
  
  public int w()
  {
    return 160;
  }
  
  protected String z()
  {
    if (!V()) {
      return "mob.guardian.land.idle";
    }
    if (isElder()) {
      return "mob.guardian.elder.idle";
    }
    return "mob.guardian.idle";
  }
  
  protected String bo()
  {
    if (!V()) {
      return "mob.guardian.land.hit";
    }
    if (isElder()) {
      return "mob.guardian.elder.hit";
    }
    return "mob.guardian.hit";
  }
  
  protected String bp()
  {
    if (!V()) {
      return "mob.guardian.land.death";
    }
    if (isElder()) {
      return "mob.guardian.elder.death";
    }
    return "mob.guardian.death";
  }
  
  protected boolean s_()
  {
    return false;
  }
  
  public float getHeadHeight()
  {
    return this.length * 0.5F;
  }
  
  public float a(BlockPosition ☃)
  {
    if (this.world.getType(☃).getBlock().getMaterial() == Material.WATER) {
      return 10.0F + this.world.o(☃) - 0.5F;
    }
    return super.a(☃);
  }
  
  public void m()
  {
    if (this.world.isClientSide)
    {
      this.b = this.a;
      if (!V())
      {
        this.c = 2.0F;
        if ((this.motY > 0.0D) && (this.bq) && (!R())) {
          this.world.a(this.locX, this.locY, this.locZ, "mob.guardian.flop", 1.0F, 1.0F, false);
        }
        this.bq = ((this.motY < 0.0D) && (this.world.d(new BlockPosition(this).down(), false)));
      }
      else if (n())
      {
        if (this.c < 0.5F) {
          this.c = 4.0F;
        } else {
          this.c += (0.5F - this.c) * 0.1F;
        }
      }
      else
      {
        this.c += (0.125F - this.c) * 0.2F;
      }
      this.a += this.c;
      
      this.bn = this.bm;
      if (!V()) {
        this.bm = this.random.nextFloat();
      } else if (n()) {
        this.bm += (0.0F - this.bm) * 0.25F;
      } else {
        this.bm += (1.0F - this.bm) * 0.06F;
      }
      if ((n()) && (V()))
      {
        Vec3D ☃ = d(0.0F);
        for (int ☃ = 0; ☃ < 2; ☃++) {
          this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX + (this.random.nextDouble() - 0.5D) * this.width - ☃.a * 1.5D, this.locY + this.random.nextDouble() * this.length - ☃.b * 1.5D, this.locZ + (this.random.nextDouble() - 0.5D) * this.width - ☃.c * 1.5D, 0.0D, 0.0D, 0.0D, new int[0]);
        }
      }
      if (cp())
      {
        if (this.bp < cm()) {
          this.bp += 1;
        }
        EntityLiving ☃ = cq();
        if (☃ != null)
        {
          getControllerLook().a(☃, 90.0F, 90.0F);
          getControllerLook().a();
          
          double ☃ = q(0.0F);
          double ☃ = ☃.locX - this.locX;
          double ☃ = ☃.locY + ☃.length * 0.5F - (this.locY + getHeadHeight());
          double ☃ = ☃.locZ - this.locZ;
          double ☃ = Math.sqrt(☃ * ☃ + ☃ * ☃ + ☃ * ☃);
          ☃ /= ☃;
          ☃ /= ☃;
          ☃ /= ☃;
          double ☃ = this.random.nextDouble();
          while (☃ < ☃)
          {
            ☃ += 1.8D - ☃ + this.random.nextDouble() * (1.7D - ☃);
            this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX + ☃ * ☃, this.locY + ☃ * ☃ + getHeadHeight(), this.locZ + ☃ * ☃, 0.0D, 0.0D, 0.0D, new int[0]);
          }
        }
      }
    }
    if (this.inWater)
    {
      setAirTicks(300);
    }
    else if (this.onGround)
    {
      this.motY += 0.5D;
      this.motX += (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F;
      this.motZ += (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F;
      this.yaw = (this.random.nextFloat() * 360.0F);
      this.onGround = false;
      this.ai = true;
    }
    if (cp()) {
      this.yaw = this.aK;
    }
    super.m();
  }
  
  public float q(float ☃)
  {
    return (this.bp + ☃) / cm();
  }
  
  protected void E()
  {
    super.E();
    if (isElder())
    {
      int ☃ = 1200;
      int ☃ = 1200;
      int ☃ = 6000;
      int ☃ = 2;
      MobEffectList ☃;
      if ((this.ticksLived + getId()) % 1200 == 0)
      {
        ☃ = MobEffectList.SLOWER_DIG;
        
        List<EntityPlayer> ☃ = this.world.b(EntityPlayer.class, new Predicate()
        {
          public boolean a(EntityPlayer ☃)
          {
            return (EntityGuardian.this.h(☃) < 2500.0D) && (☃.playerInteractManager.c());
          }
        });
        for (EntityPlayer ☃ : ☃) {
          if ((!☃.hasEffect(☃)) || (☃.getEffect(☃).getAmplifier() < 2) || (☃.getEffect(☃).getDuration() < 1200))
          {
            ☃.playerConnection.sendPacket(new PacketPlayOutGameStateChange(10, 0.0F));
            ☃.addEffect(new MobEffect(☃.id, 6000, 2));
          }
        }
      }
      if (!ck()) {
        a(new BlockPosition(this), 16);
      }
    }
  }
  
  protected void dropDeathLoot(boolean ☃, int ☃)
  {
    int ☃ = this.random.nextInt(3) + this.random.nextInt(☃ + 1);
    if (☃ > 0) {
      a(new ItemStack(Items.PRISMARINE_SHARD, ☃, 0), 1.0F);
    }
    if (this.random.nextInt(3 + ☃) > 1) {
      a(new ItemStack(Items.FISH, 1, ItemFish.EnumFish.COD.a()), 1.0F);
    } else if (this.random.nextInt(3 + ☃) > 1) {
      a(new ItemStack(Items.PRISMARINE_CRYSTALS, 1, 0), 1.0F);
    }
    if ((☃) && 
      (isElder())) {
      a(new ItemStack(Blocks.SPONGE, 1, 1), 1.0F);
    }
  }
  
  protected void getRareDrop()
  {
    ItemStack ☃ = ((PossibleFishingResult)WeightedRandom.a(this.random, EntityFishingHook.j())).a(this.random);
    a(☃, 1.0F);
  }
  
  protected boolean n_()
  {
    return true;
  }
  
  public boolean canSpawn()
  {
    return (this.world.a(getBoundingBox(), this)) && (this.world.getCubes(this, getBoundingBox()).isEmpty());
  }
  
  public boolean bR()
  {
    return ((this.random.nextInt(20) == 0) || (!this.world.j(new BlockPosition(this)))) && (super.bR());
  }
  
  public boolean damageEntity(DamageSource ☃, float ☃)
  {
    if ((!n()) && (!☃.isMagic()) && ((☃.i() instanceof EntityLiving)))
    {
      EntityLiving ☃ = (EntityLiving)☃.i();
      if (!☃.isExplosion())
      {
        ☃.damageEntity(DamageSource.a(this), 2.0F);
        ☃.makeSound("damage.thorns", 0.5F, 1.0F);
      }
    }
    this.goalRandomStroll.f();
    return super.damageEntity(☃, ☃);
  }
  
  public int bQ()
  {
    return 180;
  }
  
  public void g(float ☃, float ☃)
  {
    if (bM())
    {
      if (V())
      {
        a(☃, ☃, 0.1F);
        move(this.motX, this.motY, this.motZ);
        
        this.motX *= 0.8999999761581421D;
        this.motY *= 0.8999999761581421D;
        this.motZ *= 0.8999999761581421D;
        if ((!n()) && (getGoalTarget() == null)) {
          this.motY -= 0.005D;
        }
      }
      else
      {
        super.g(☃, ☃);
      }
    }
    else {
      super.g(☃, ☃);
    }
  }
  
  static class EntitySelectorGuardianTargetHumanSquid
    implements Predicate<EntityLiving>
  {
    private EntityGuardian a;
    
    public EntitySelectorGuardianTargetHumanSquid(EntityGuardian ☃)
    {
      this.a = ☃;
    }
    
    public boolean a(EntityLiving ☃)
    {
      return (((☃ instanceof EntityHuman)) || ((☃ instanceof EntitySquid))) && (☃.h(this.a) > 9.0D);
    }
  }
  
  static class PathfinderGoalGuardianAttack
    extends PathfinderGoal
  {
    private EntityGuardian a;
    private int b;
    
    public PathfinderGoalGuardianAttack(EntityGuardian ☃)
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
    
    public boolean b()
    {
      return (super.b()) && ((this.a.isElder()) || (this.a.h(this.a.getGoalTarget()) > 9.0D));
    }
    
    public void c()
    {
      this.b = -10;
      this.a.getNavigation().n();
      this.a.getControllerLook().a(this.a.getGoalTarget(), 90.0F, 90.0F);
      
      this.a.ai = true;
    }
    
    public void d()
    {
      EntityGuardian.a(this.a, 0);
      this.a.setGoalTarget(null);
      
      EntityGuardian.a(this.a).f();
    }
    
    public void e()
    {
      EntityLiving ☃ = this.a.getGoalTarget();
      
      this.a.getNavigation().n();
      this.a.getControllerLook().a(☃, 90.0F, 90.0F);
      if (!this.a.hasLineOfSight(☃))
      {
        this.a.setGoalTarget(null);
        return;
      }
      this.b += 1;
      if (this.b == 0)
      {
        EntityGuardian.a(this.a, this.a.getGoalTarget().getId());
        this.a.world.broadcastEntityEffect(this.a, (byte)21);
      }
      else if (this.b >= this.a.cm())
      {
        float ☃ = 1.0F;
        if (this.a.world.getDifficulty() == EnumDifficulty.HARD) {
          ☃ += 2.0F;
        }
        if (this.a.isElder()) {
          ☃ += 2.0F;
        }
        ☃.damageEntity(DamageSource.b(this.a, this.a), ☃);
        ☃.damageEntity(DamageSource.mobAttack(this.a), (float)this.a.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue());
        this.a.setGoalTarget(null);
      }
      else if ((this.b < 60) || (this.b % 20 != 0)) {}
      super.e();
    }
  }
  
  static class ControllerMoveGuardian
    extends ControllerMove
  {
    private EntityGuardian g;
    
    public ControllerMoveGuardian(EntityGuardian ☃)
    {
      super();
      this.g = ☃;
    }
    
    public void c()
    {
      if ((!this.f) || (this.g.getNavigation().m()))
      {
        this.g.k(0.0F);
        EntityGuardian.a(this.g, false);
        return;
      }
      double ☃ = this.b - this.g.locX;
      double ☃ = this.c - this.g.locY;
      double ☃ = this.d - this.g.locZ;
      double ☃ = ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
      ☃ = MathHelper.sqrt(☃);
      ☃ /= ☃;
      
      float ☃ = (float)(MathHelper.b(☃, ☃) * 180.0D / 3.1415927410125732D) - 90.0F;
      
      this.g.yaw = a(this.g.yaw, ☃, 30.0F);
      this.g.aI = this.g.yaw;
      
      float ☃ = (float)(this.e * this.g.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
      this.g.k(this.g.bI() + (☃ - this.g.bI()) * 0.125F);
      double ☃ = Math.sin((this.g.ticksLived + this.g.getId()) * 0.5D) * 0.05D;
      double ☃ = Math.cos(this.g.yaw * 3.1415927F / 180.0F);
      double ☃ = Math.sin(this.g.yaw * 3.1415927F / 180.0F);
      this.g.motX += ☃ * ☃;
      this.g.motZ += ☃ * ☃;
      
      ☃ = Math.sin((this.g.ticksLived + this.g.getId()) * 0.75D) * 0.05D;
      this.g.motY += ☃ * (☃ + ☃) * 0.25D;
      this.g.motY += this.g.bI() * ☃ * 0.1D;
      
      ControllerLook ☃ = this.g.getControllerLook();
      double ☃ = this.g.locX + ☃ / ☃ * 2.0D;
      double ☃ = this.g.getHeadHeight() + this.g.locY + ☃ / ☃ * 1.0D;
      double ☃ = this.g.locZ + ☃ / ☃ * 2.0D;
      double ☃ = ☃.e();
      double ☃ = ☃.f();
      double ☃ = ☃.g();
      if (!☃.b())
      {
        ☃ = ☃;
        ☃ = ☃;
        ☃ = ☃;
      }
      this.g.getControllerLook().a(☃ + (☃ - ☃) * 0.125D, ☃ + (☃ - ☃) * 0.125D, ☃ + (☃ - ☃) * 0.125D, 10.0F, 40.0F);
      EntityGuardian.a(this.g, true);
    }
  }
}
