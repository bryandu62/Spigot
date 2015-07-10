package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EntityRabbit
  extends EntityAnimal
{
  private PathfinderGoalRabbitAvoidTarget<EntityWolf> bm;
  private int bo = 0;
  private int bp = 0;
  private boolean bq = false;
  private boolean br = false;
  private int bs = 0;
  private EnumRabbitState bt;
  private int bu;
  private EntityHuman bv;
  
  public EntityRabbit(World world)
  {
    super(world);
    this.bt = EnumRabbitState.HOP;
    this.bu = 0;
    this.bv = null;
    setSize(0.6F, 0.7F);
    this.g = new ControllerJumpRabbit(this);
    this.moveController = new ControllerMoveRabbit(this);
    ((Navigation)getNavigation()).a(true);
    initializePathFinderGoals();
    b(0.0D);
  }
  
  public void initializePathFinderGoals()
  {
    this.navigation.a(2.5F);
    this.goalSelector.a(1, new PathfinderGoalFloat(this));
    this.goalSelector.a(1, new PathfinderGoalRabbitPanic(this, 1.33D));
    this.goalSelector.a(2, new PathfinderGoalTempt(this, 1.0D, Items.CARROT, false));
    this.goalSelector.a(2, new PathfinderGoalTempt(this, 1.0D, Items.GOLDEN_CARROT, false));
    this.goalSelector.a(2, new PathfinderGoalTempt(this, 1.0D, Item.getItemOf(Blocks.YELLOW_FLOWER), false));
    this.goalSelector.a(3, new PathfinderGoalBreed(this, 0.8D));
    this.goalSelector.a(5, new PathfinderGoalEatCarrots(this));
    this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.6D));
    this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
    this.bm = new PathfinderGoalRabbitAvoidTarget(this, EntityWolf.class, 16.0F, 1.33D, 1.33D);
    this.goalSelector.a(4, this.bm);
  }
  
  protected float bE()
  {
    return (this.moveController.a()) && (this.moveController.e() > this.locY + 0.5D) ? 0.5F : this.bt.b();
  }
  
  public void a(EnumRabbitState entityrabbit_enumrabbitstate)
  {
    this.bt = entityrabbit_enumrabbitstate;
  }
  
  public void b(double d0)
  {
    getNavigation().a(d0);
    this.moveController.a(this.moveController.d(), this.moveController.e(), this.moveController.f(), d0);
  }
  
  public void a(boolean flag, EnumRabbitState entityrabbit_enumrabbitstate)
  {
    super.i(flag);
    if (!flag)
    {
      if (this.bt == EnumRabbitState.ATTACK) {
        this.bt = EnumRabbitState.HOP;
      }
    }
    else
    {
      b(1.5D * entityrabbit_enumrabbitstate.a());
      makeSound(cm(), bB(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
    }
    this.bq = flag;
  }
  
  public void b(EnumRabbitState entityrabbit_enumrabbitstate)
  {
    a(true, entityrabbit_enumrabbitstate);
    this.bp = entityrabbit_enumrabbitstate.d();
    this.bo = 0;
  }
  
  public boolean cl()
  {
    return this.bq;
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(18, Byte.valueOf((byte)0));
  }
  
  public void E()
  {
    if (this.moveController.b() > 0.8D) {
      a(EnumRabbitState.SPRINT);
    } else if (this.bt != EnumRabbitState.ATTACK) {
      a(EnumRabbitState.HOP);
    }
    if (this.bs > 0) {
      this.bs -= 1;
    }
    if (this.bu > 0)
    {
      this.bu -= this.random.nextInt(3);
      if (this.bu < 0) {
        this.bu = 0;
      }
    }
    if (this.onGround)
    {
      if (!this.br)
      {
        a(false, EnumRabbitState.NONE);
        cw();
      }
      if ((getRabbitType() == 99) && (this.bs == 0))
      {
        EntityLiving entityliving = getGoalTarget();
        if ((entityliving != null) && (h(entityliving) < 16.0D))
        {
          a(entityliving.locX, entityliving.locZ);
          this.moveController.a(entityliving.locX, entityliving.locY, entityliving.locZ, this.moveController.b());
          b(EnumRabbitState.ATTACK);
          this.br = true;
        }
      }
      ControllerJumpRabbit entityrabbit_controllerjumprabbit = (ControllerJumpRabbit)this.g;
      if (!entityrabbit_controllerjumprabbit.c())
      {
        if ((this.moveController.a()) && (this.bs == 0))
        {
          PathEntity pathentity = this.navigation.j();
          Vec3D vec3d = new Vec3D(this.moveController.d(), this.moveController.e(), this.moveController.f());
          if ((pathentity != null) && (pathentity.e() < pathentity.d())) {
            vec3d = pathentity.a(this);
          }
          a(vec3d.a, vec3d.c);
          b(this.bt);
        }
      }
      else if (!entityrabbit_controllerjumprabbit.d()) {
        ct();
      }
    }
    this.br = this.onGround;
  }
  
  public void Y() {}
  
  private void a(double d0, double d1)
  {
    this.yaw = ((float)(MathHelper.b(d1 - this.locZ, d0 - this.locX) * 180.0D / 3.1415927410125732D) - 90.0F);
  }
  
  private void ct()
  {
    ((ControllerJumpRabbit)this.g).a(true);
  }
  
  private void cu()
  {
    ((ControllerJumpRabbit)this.g).a(false);
  }
  
  private void cv()
  {
    this.bs = co();
  }
  
  private void cw()
  {
    cv();
    cu();
  }
  
  public void m()
  {
    super.m();
    if (this.bo != this.bp)
    {
      if ((this.bo == 0) && (!this.world.isClientSide)) {
        this.world.broadcastEntityEffect(this, (byte)1);
      }
      this.bo += 1;
    }
    else if (this.bp != 0)
    {
      this.bo = 0;
      this.bp = 0;
    }
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setInt("RabbitType", getRabbitType());
    nbttagcompound.setInt("MoreCarrotTicks", this.bu);
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    setRabbitType(nbttagcompound.getInt("RabbitType"));
    this.bu = nbttagcompound.getInt("MoreCarrotTicks");
  }
  
  protected String cm()
  {
    return "mob.rabbit.hop";
  }
  
  protected String z()
  {
    return "mob.rabbit.idle";
  }
  
  protected String bo()
  {
    return "mob.rabbit.hurt";
  }
  
  protected String bp()
  {
    return "mob.rabbit.death";
  }
  
  public boolean r(Entity entity)
  {
    if (getRabbitType() == 99)
    {
      makeSound("mob.attack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
      return entity.damageEntity(DamageSource.mobAttack(this), 8.0F);
    }
    return entity.damageEntity(DamageSource.mobAttack(this), 3.0F);
  }
  
  public int br()
  {
    return getRabbitType() == 99 ? 8 : super.br();
  }
  
  public boolean damageEntity(DamageSource damagesource, float f)
  {
    return isInvulnerable(damagesource) ? false : super.damageEntity(damagesource, f);
  }
  
  protected void getRareDrop()
  {
    a(new ItemStack(Items.RABBIT_FOOT, 1), 0.0F);
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    int j = this.random.nextInt(2) + this.random.nextInt(1 + i);
    for (int k = 0; k < j; k++) {
      a(Items.RABBIT_HIDE, 1);
    }
    j = this.random.nextInt(2);
    for (k = 0; k < j; k++) {
      if (isBurning()) {
        a(Items.COOKED_RABBIT, 1);
      } else {
        a(Items.RABBIT, 1);
      }
    }
  }
  
  private boolean a(Item item)
  {
    return (item == Items.CARROT) || (item == Items.GOLDEN_CARROT) || (item == Item.getItemOf(Blocks.YELLOW_FLOWER));
  }
  
  public EntityRabbit b(EntityAgeable entityageable)
  {
    EntityRabbit entityrabbit = new EntityRabbit(this.world);
    if ((entityageable instanceof EntityRabbit)) {
      entityrabbit.setRabbitType(this.random.nextBoolean() ? getRabbitType() : ((EntityRabbit)entityageable).getRabbitType());
    }
    return entityrabbit;
  }
  
  public boolean d(ItemStack itemstack)
  {
    return (itemstack != null) && (a(itemstack.getItem()));
  }
  
  public int getRabbitType()
  {
    return this.datawatcher.getByte(18);
  }
  
  public void setRabbitType(int i)
  {
    if (i == 99)
    {
      this.goalSelector.a(this.bm);
      this.goalSelector.a(4, new PathfinderGoalKillerRabbitMeleeAttack(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityWolf.class, true));
      if (!hasCustomName()) {
        setCustomName(LocaleI18n.get("entity.KillerBunny.name"));
      }
    }
    this.datawatcher.watch(18, Byte.valueOf((byte)i));
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, GroupDataEntity groupdataentity)
  {
    Object object = super.prepare(difficultydamagescaler, groupdataentity);
    int i = this.random.nextInt(6);
    boolean flag = false;
    if ((object instanceof GroupDataRabbit))
    {
      i = ((GroupDataRabbit)object).a;
      flag = true;
    }
    else
    {
      object = new GroupDataRabbit(i);
    }
    setRabbitType(i);
    if (flag) {
      setAgeRaw(41536);
    }
    return (GroupDataEntity)object;
  }
  
  private boolean cx()
  {
    return this.bu == 0;
  }
  
  protected int co()
  {
    return this.bt.c();
  }
  
  protected void cp()
  {
    this.world.addParticle(EnumParticle.BLOCK_DUST, this.locX + this.random.nextFloat() * this.width * 2.0F - this.width, this.locY + 0.5D + this.random.nextFloat() * this.length, this.locZ + this.random.nextFloat() * this.width * 2.0F - this.width, 0.0D, 0.0D, 0.0D, new int[] { Block.getCombinedId(Blocks.CARROTS.fromLegacyData(7)) });
    this.bu = 100;
  }
  
  public EntityAgeable createChild(EntityAgeable entityageable)
  {
    return b(entityageable);
  }
  
  static enum EnumRabbitState
  {
    NONE(0.0F, 0.0F, 30, 1),  HOP(0.8F, 0.2F, 20, 10),  STEP(1.0F, 0.45F, 14, 14),  SPRINT(1.75F, 0.4F, 1, 8),  ATTACK(2.0F, 0.7F, 7, 8);
    
    private final float f;
    private final float g;
    private final int h;
    private final int i;
    
    private EnumRabbitState(float f, float f1, int i, int j)
    {
      this.f = f;
      this.g = f1;
      this.h = i;
      this.i = j;
    }
    
    public float a()
    {
      return this.f;
    }
    
    public float b()
    {
      return this.g;
    }
    
    public int c()
    {
      return this.h;
    }
    
    public int d()
    {
      return this.i;
    }
  }
  
  static class PathfinderGoalKillerRabbitMeleeAttack
    extends PathfinderGoalMeleeAttack
  {
    public PathfinderGoalKillerRabbitMeleeAttack(EntityRabbit entityrabbit)
    {
      super(EntityLiving.class, 1.4D, true);
    }
    
    protected double a(EntityLiving entityliving)
    {
      return 4.0F + entityliving.width;
    }
  }
  
  static class PathfinderGoalRabbitPanic
    extends PathfinderGoalPanic
  {
    private EntityRabbit b;
    
    public PathfinderGoalRabbitPanic(EntityRabbit entityrabbit, double d0)
    {
      super(d0);
      this.b = entityrabbit;
    }
    
    public void e()
    {
      super.e();
      this.b.b(this.a);
    }
  }
  
  static class PathfinderGoalEatCarrots
    extends PathfinderGoalGotoTarget
  {
    private final EntityRabbit c;
    private boolean d;
    private boolean e = false;
    
    public PathfinderGoalEatCarrots(EntityRabbit entityrabbit)
    {
      super(0.699999988079071D, 16);
      this.c = entityrabbit;
    }
    
    public boolean a()
    {
      if (this.a <= 0)
      {
        if (!this.c.world.getGameRules().getBoolean("mobGriefing")) {
          return false;
        }
        this.e = false;
        this.d = this.c.cx();
      }
      return super.a();
    }
    
    public boolean b()
    {
      return (this.e) && (super.b());
    }
    
    public void c()
    {
      super.c();
    }
    
    public void d()
    {
      super.d();
    }
    
    public void e()
    {
      super.e();
      this.c.getControllerLook().a(this.b.getX() + 0.5D, this.b.getY() + 1, this.b.getZ() + 0.5D, 10.0F, this.c.bQ());
      if (f())
      {
        World world = this.c.world;
        BlockPosition blockposition = this.b.up();
        IBlockData iblockdata = world.getType(blockposition);
        Block block = iblockdata.getBlock();
        if ((this.e) && ((block instanceof BlockCarrots)) && (((Integer)iblockdata.get(BlockCarrots.AGE)).intValue() == 7))
        {
          world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 2);
          world.setAir(blockposition, true);
          this.c.cp();
        }
        this.e = false;
        this.a = 10;
      }
    }
    
    protected boolean a(World world, BlockPosition blockposition)
    {
      Block block = world.getType(blockposition).getBlock();
      if (block == Blocks.FARMLAND)
      {
        blockposition = blockposition.up();
        IBlockData iblockdata = world.getType(blockposition);
        
        block = iblockdata.getBlock();
        if (((block instanceof BlockCarrots)) && (((Integer)iblockdata.get(BlockCarrots.AGE)).intValue() == 7) && (this.d) && (!this.e))
        {
          this.e = true;
          return true;
        }
      }
      return false;
    }
  }
  
  static class PathfinderGoalRabbitAvoidTarget<T extends Entity>
    extends PathfinderGoalAvoidTarget<T>
  {
    private EntityRabbit c;
    
    public PathfinderGoalRabbitAvoidTarget(EntityRabbit entityrabbit, Class<T> oclass, float f, double d0, double d1)
    {
      super(oclass, f, d0, d1);
      this.c = entityrabbit;
    }
    
    public void e()
    {
      super.e();
    }
  }
  
  static class ControllerMoveRabbit
    extends ControllerMove
  {
    private EntityRabbit g;
    
    public ControllerMoveRabbit(EntityRabbit entityrabbit)
    {
      super();
      this.g = entityrabbit;
    }
    
    public void c()
    {
      if ((this.g.onGround) && (!this.g.cl())) {
        this.g.b(0.0D);
      }
      super.c();
    }
  }
  
  public class ControllerJumpRabbit
    extends ControllerJump
  {
    private EntityRabbit c;
    private boolean d = false;
    
    public ControllerJumpRabbit(EntityRabbit entityrabbit)
    {
      super();
      this.c = entityrabbit;
    }
    
    public boolean c()
    {
      return this.a;
    }
    
    public boolean d()
    {
      return this.d;
    }
    
    public void a(boolean flag)
    {
      this.d = flag;
    }
    
    public void b()
    {
      if (this.a)
      {
        this.c.b(EntityRabbit.EnumRabbitState.STEP);
        this.a = false;
      }
    }
  }
  
  public static class GroupDataRabbit
    implements GroupDataEntity
  {
    public int a;
    
    public GroupDataRabbit(int i)
    {
      this.a = i;
    }
  }
}
