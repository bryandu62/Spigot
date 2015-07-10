package net.minecraft.server.v1_8_R3;

import java.util.Random;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.plugin.PluginManager;

public class EntitySlime
  extends EntityInsentient
  implements IMonster
{
  public float a;
  public float b;
  public float c;
  private boolean bk;
  
  public EntitySlime(World world)
  {
    super(world);
    this.moveController = new ControllerMoveSlime(this);
    this.goalSelector.a(1, new PathfinderGoalSlimeRandomJump(this));
    this.goalSelector.a(2, new PathfinderGoalSlimeNearestPlayer(this));
    this.goalSelector.a(3, new PathfinderGoalSlimeRandomDirection(this));
    this.goalSelector.a(5, new PathfinderGoalSlimeIdle(this));
    this.targetSelector.a(1, new PathfinderGoalTargetNearestPlayer(this));
    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTargetInsentient(this, EntityIronGolem.class));
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, Byte.valueOf((byte)1));
  }
  
  public void setSize(int i)
  {
    this.datawatcher.watch(16, Byte.valueOf((byte)i));
    setSize(0.51000005F * i, 0.51000005F * i);
    setPosition(this.locX, this.locY, this.locZ);
    getAttributeInstance(GenericAttributes.maxHealth).setValue(i * i);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.2F + 0.1F * i);
    setHealth(getMaxHealth());
    this.b_ = i;
  }
  
  public int getSize()
  {
    return this.datawatcher.getByte(16);
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("Size", getSize() - 1);
    nbttagcompound.setBoolean("wasOnGround", this.bk);
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    int i = nbttagcompound.getInt("Size");
    if (i < 0) {
      i = 0;
    }
    setSize(i + 1);
    this.bk = nbttagcompound.getBoolean("wasOnGround");
  }
  
  protected EnumParticle n()
  {
    return EnumParticle.SLIME;
  }
  
  protected String ck()
  {
    return "mob.slime." + (getSize() > 1 ? "big" : "small");
  }
  
  public void t_()
  {
    if ((!this.world.isClientSide) && (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) && (getSize() > 0)) {
      this.dead = true;
    }
    this.b += (this.a - this.b) * 0.5F;
    this.c = this.b;
    super.t_();
    if ((this.onGround) && (!this.bk))
    {
      int i = getSize();
      for (int j = 0; j < i * 8; j++)
      {
        float f = this.random.nextFloat() * 3.1415927F * 2.0F;
        float f1 = this.random.nextFloat() * 0.5F + 0.5F;
        float f2 = MathHelper.sin(f) * i * 0.5F * f1;
        float f3 = MathHelper.cos(f) * i * 0.5F * f1;
        World world = this.world;
        EnumParticle enumparticle = n();
        double d0 = this.locX + f2;
        double d1 = this.locZ + f3;
        
        world.addParticle(enumparticle, d0, getBoundingBox().b, d1, 0.0D, 0.0D, 0.0D, new int[0]);
      }
      if (cl()) {
        makeSound(ck(), bB(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
      }
      this.a = -0.5F;
    }
    else if ((!this.onGround) && (this.bk))
    {
      this.a = 1.0F;
    }
    this.bk = this.onGround;
    ch();
  }
  
  protected void ch()
  {
    this.a *= 0.6F;
  }
  
  protected int cg()
  {
    return this.random.nextInt(20) + 10;
  }
  
  protected EntitySlime cf()
  {
    return new EntitySlime(this.world);
  }
  
  public void i(int i)
  {
    if (i == 16)
    {
      int j = getSize();
      
      setSize(0.51000005F * j, 0.51000005F * j);
      this.yaw = this.aK;
      this.aI = this.aK;
      if ((V()) && (this.random.nextInt(20) == 0)) {
        X();
      }
    }
    super.i(i);
  }
  
  public void die()
  {
    int i = getSize();
    if ((!this.world.isClientSide) && (i > 1) && (getHealth() <= 0.0F))
    {
      int j = 2 + this.random.nextInt(3);
      
      SlimeSplitEvent event = new SlimeSplitEvent((Slime)getBukkitEntity(), j);
      this.world.getServer().getPluginManager().callEvent(event);
      if ((!event.isCancelled()) && (event.getCount() > 0))
      {
        j = event.getCount();
      }
      else
      {
        super.die();
        return;
      }
      for (int k = 0; k < j; k++)
      {
        float f = (k % 2 - 0.5F) * i / 4.0F;
        float f1 = (k / 2 - 0.5F) * i / 4.0F;
        EntitySlime entityslime = cf();
        if (hasCustomName()) {
          entityslime.setCustomName(getCustomName());
        }
        if (isPersistent()) {
          entityslime.bX();
        }
        entityslime.setSize(i / 2);
        entityslime.setPositionRotation(this.locX + f, this.locY + 0.5D, this.locZ + f1, this.random.nextFloat() * 360.0F, 0.0F);
        this.world.addEntity(entityslime, CreatureSpawnEvent.SpawnReason.SLIME_SPLIT);
      }
    }
    super.die();
  }
  
  public void collide(Entity entity)
  {
    super.collide(entity);
    if (((entity instanceof EntityIronGolem)) && (ci())) {
      e((EntityLiving)entity);
    }
  }
  
  public void d(EntityHuman entityhuman)
  {
    if (ci()) {
      e(entityhuman);
    }
  }
  
  protected void e(EntityLiving entityliving)
  {
    int i = getSize();
    if ((hasLineOfSight(entityliving)) && (h(entityliving) < 0.6D * i * 0.6D * i) && (entityliving.damageEntity(DamageSource.mobAttack(this), cj())))
    {
      makeSound("mob.attack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
      a(this, entityliving);
    }
  }
  
  public float getHeadHeight()
  {
    return 0.625F * this.length;
  }
  
  protected boolean ci()
  {
    return getSize() > 1;
  }
  
  protected int cj()
  {
    return getSize();
  }
  
  protected String bo()
  {
    return "mob.slime." + (getSize() > 1 ? "big" : "small");
  }
  
  protected String bp()
  {
    return "mob.slime." + (getSize() > 1 ? "big" : "small");
  }
  
  protected Item getLoot()
  {
    return getSize() == 1 ? Items.SLIME : null;
  }
  
  public boolean bR()
  {
    BlockPosition blockposition = new BlockPosition(MathHelper.floor(this.locX), 0, MathHelper.floor(this.locZ));
    Chunk chunk = this.world.getChunkAtWorldCoords(blockposition);
    if ((this.world.getWorldData().getType() == WorldType.FLAT) && (this.random.nextInt(4) != 1)) {
      return false;
    }
    if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)
    {
      BiomeBase biomebase = this.world.getBiome(blockposition);
      if ((biomebase == BiomeBase.SWAMPLAND) && (this.locY > 50.0D) && (this.locY < 70.0D) && (this.random.nextFloat() < 0.5F) && (this.random.nextFloat() < this.world.y()) && (this.world.getLightLevel(new BlockPosition(this)) <= this.random.nextInt(8))) {
        return super.bR();
      }
      if ((this.random.nextInt(10) == 0) && (chunk.a(987234911L).nextInt(10) == 0) && (this.locY < 40.0D)) {
        return super.bR();
      }
    }
    return false;
  }
  
  protected float bB()
  {
    return 0.4F * getSize();
  }
  
  public int bQ()
  {
    return 0;
  }
  
  protected boolean cn()
  {
    return getSize() > 0;
  }
  
  protected boolean cl()
  {
    return getSize() > 2;
  }
  
  protected void bF()
  {
    this.motY = 0.41999998688697815D;
    this.ai = true;
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, GroupDataEntity groupdataentity)
  {
    int i = this.random.nextInt(3);
    if ((i < 2) && (this.random.nextFloat() < 0.5F * difficultydamagescaler.c())) {
      i++;
    }
    int j = 1 << i;
    
    setSize(j);
    return super.prepare(difficultydamagescaler, groupdataentity);
  }
  
  static class PathfinderGoalSlimeIdle
    extends PathfinderGoal
  {
    private EntitySlime a;
    
    public PathfinderGoalSlimeIdle(EntitySlime entityslime)
    {
      this.a = entityslime;
      a(5);
    }
    
    public boolean a()
    {
      return true;
    }
    
    public void e()
    {
      ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(1.0D);
    }
  }
  
  static class PathfinderGoalSlimeRandomJump
    extends PathfinderGoal
  {
    private EntitySlime a;
    
    public PathfinderGoalSlimeRandomJump(EntitySlime entityslime)
    {
      this.a = entityslime;
      a(5);
      ((Navigation)entityslime.getNavigation()).d(true);
    }
    
    public boolean a()
    {
      return (this.a.V()) || (this.a.ab());
    }
    
    public void e()
    {
      if (this.a.bc().nextFloat() < 0.8F) {
        this.a.getControllerJump().a();
      }
      ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(1.2D);
    }
  }
  
  static class PathfinderGoalSlimeRandomDirection
    extends PathfinderGoal
  {
    private EntitySlime a;
    private float b;
    private int c;
    
    public PathfinderGoalSlimeRandomDirection(EntitySlime entityslime)
    {
      this.a = entityslime;
      a(2);
    }
    
    public boolean a()
    {
      return (this.a.getGoalTarget() == null) && ((this.a.onGround) || (this.a.V()) || (this.a.ab()));
    }
    
    public void e()
    {
      if (--this.c <= 0)
      {
        this.c = (40 + this.a.bc().nextInt(60));
        this.b = this.a.bc().nextInt(360);
      }
      ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(this.b, false);
    }
  }
  
  static class PathfinderGoalSlimeNearestPlayer
    extends PathfinderGoal
  {
    private EntitySlime a;
    private int b;
    
    public PathfinderGoalSlimeNearestPlayer(EntitySlime entityslime)
    {
      this.a = entityslime;
      a(2);
    }
    
    public boolean a()
    {
      EntityLiving entityliving = this.a.getGoalTarget();
      
      return entityliving != null;
    }
    
    public void c()
    {
      this.b = 300;
      super.c();
    }
    
    public boolean b()
    {
      EntityLiving entityliving = this.a.getGoalTarget();
      
      return entityliving != null;
    }
    
    public void e()
    {
      this.a.a(this.a.getGoalTarget(), 10.0F, 10.0F);
      ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(this.a.yaw, this.a.ci());
    }
  }
  
  static class ControllerMoveSlime
    extends ControllerMove
  {
    private float g;
    private int h;
    private EntitySlime i;
    private boolean j;
    
    public ControllerMoveSlime(EntitySlime entityslime)
    {
      super();
      this.i = entityslime;
    }
    
    public void a(float f, boolean flag)
    {
      this.g = f;
      this.j = flag;
    }
    
    public void a(double d0)
    {
      this.e = d0;
      this.f = true;
    }
    
    public void c()
    {
      this.a.yaw = a(this.a.yaw, this.g, 30.0F);
      this.a.aK = this.a.yaw;
      this.a.aI = this.a.yaw;
      if (!this.f)
      {
        this.a.n(0.0F);
      }
      else
      {
        this.f = false;
        if (this.a.onGround)
        {
          this.a.k((float)(this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
          if (this.h-- <= 0)
          {
            this.h = this.i.cg();
            if (this.j) {
              this.h /= 3;
            }
            this.i.getControllerJump().a();
            if (this.i.cn()) {
              this.i.makeSound(this.i.ck(), this.i.bB(), ((this.i.bc().nextFloat() - this.i.bc().nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }
          }
          else
          {
            this.i.aZ = (this.i.ba = 0.0F);
            this.a.k(0.0F);
          }
        }
        else
        {
          this.a.k((float)(this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
        }
      }
    }
  }
}
