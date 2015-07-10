package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import java.util.Random;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class EntityIronGolem
  extends EntityGolem
{
  private int b;
  Village a;
  private int c;
  private int bm;
  
  public EntityIronGolem(World world)
  {
    super(world);
    setSize(1.4F, 2.9F);
    ((Navigation)getNavigation()).a(true);
    this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
    this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
    this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, true));
    this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
    this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
    this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.6D));
    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
    this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
    this.targetSelector.a(3, new PathfinderGoalNearestGolemTarget(this, EntityInsentient.class, 10, false, true, IMonster.e));
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, Byte.valueOf((byte)0));
  }
  
  protected void E()
  {
    if (--this.b <= 0)
    {
      this.b = (70 + this.random.nextInt(50));
      this.a = this.world.ae().getClosestVillage(new BlockPosition(this), 32);
      if (this.a == null)
      {
        cj();
      }
      else
      {
        BlockPosition blockposition = this.a.a();
        
        a(blockposition, (int)(this.a.b() * 0.6F));
      }
    }
    super.E();
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
  }
  
  protected int j(int i)
  {
    return i;
  }
  
  protected void s(Entity entity)
  {
    if (((entity instanceof IMonster)) && (!(entity instanceof EntityCreeper)) && (bc().nextInt(20) == 0)) {
      setGoalTarget((EntityLiving)entity, EntityTargetEvent.TargetReason.COLLISION, true);
    }
    super.s(entity);
  }
  
  public void m()
  {
    super.m();
    if (this.c > 0) {
      this.c -= 1;
    }
    if (this.bm > 0) {
      this.bm -= 1;
    }
    if ((this.motX * this.motX + this.motZ * this.motZ > 2.500000277905201E-7D) && (this.random.nextInt(5) == 0))
    {
      int i = MathHelper.floor(this.locX);
      int j = MathHelper.floor(this.locY - 0.20000000298023224D);
      int k = MathHelper.floor(this.locZ);
      IBlockData iblockdata = this.world.getType(new BlockPosition(i, j, k));
      Block block = iblockdata.getBlock();
      if (block.getMaterial() != Material.AIR) {
        this.world.addParticle(EnumParticle.BLOCK_CRACK, this.locX + (this.random.nextFloat() - 0.5D) * this.width, getBoundingBox().b + 0.1D, this.locZ + (this.random.nextFloat() - 0.5D) * this.width, 4.0D * (this.random.nextFloat() - 0.5D), 0.5D, (this.random.nextFloat() - 0.5D) * 4.0D, new int[] { Block.getCombinedId(iblockdata) });
      }
    }
  }
  
  public boolean a(Class<? extends EntityLiving> oclass)
  {
    return oclass == EntityCreeper.class ? false : (isPlayerCreated()) && (EntityHuman.class.isAssignableFrom(oclass)) ? false : super.a(oclass);
  }
  
  public void b(NBTTagCompound nbttagcompound)
  {
    super.b(nbttagcompound);
    nbttagcompound.setBoolean("PlayerCreated", isPlayerCreated());
  }
  
  public void a(NBTTagCompound nbttagcompound)
  {
    super.a(nbttagcompound);
    setPlayerCreated(nbttagcompound.getBoolean("PlayerCreated"));
  }
  
  public boolean r(Entity entity)
  {
    this.c = 10;
    this.world.broadcastEntityEffect(this, (byte)4);
    boolean flag = entity.damageEntity(DamageSource.mobAttack(this), 7 + this.random.nextInt(15));
    if (flag)
    {
      entity.motY += 0.4000000059604645D;
      a(this, entity);
    }
    makeSound("mob.irongolem.throw", 1.0F, 1.0F);
    return flag;
  }
  
  public Village n()
  {
    return this.a;
  }
  
  public void a(boolean flag)
  {
    this.bm = (flag ? 400 : 0);
    this.world.broadcastEntityEffect(this, (byte)11);
  }
  
  protected String bo()
  {
    return "mob.irongolem.hit";
  }
  
  protected String bp()
  {
    return "mob.irongolem.death";
  }
  
  protected void a(BlockPosition blockposition, Block block)
  {
    makeSound("mob.irongolem.walk", 1.0F, 1.0F);
  }
  
  protected void dropDeathLoot(boolean flag, int i)
  {
    int j = this.random.nextInt(3);
    for (int k = 0; k < j; k++) {
      a(Item.getItemOf(Blocks.RED_FLOWER), 1, BlockFlowers.EnumFlowerVarient.POPPY.b());
    }
    k = 3 + this.random.nextInt(3);
    for (int l = 0; l < k; l++) {
      a(Items.IRON_INGOT, 1);
    }
  }
  
  public int cm()
  {
    return this.bm;
  }
  
  public boolean isPlayerCreated()
  {
    return (this.datawatcher.getByte(16) & 0x1) != 0;
  }
  
  public void setPlayerCreated(boolean flag)
  {
    byte b0 = this.datawatcher.getByte(16);
    if (flag) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(b0 | 0x1)));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
    }
  }
  
  public void die(DamageSource damagesource)
  {
    if ((!isPlayerCreated()) && (this.killer != null) && (this.a != null)) {
      this.a.a(this.killer.getName(), -5);
    }
    super.die(damagesource);
  }
  
  static class PathfinderGoalNearestGolemTarget<T extends EntityLiving>
    extends PathfinderGoalNearestAttackableTarget<T>
  {
    public PathfinderGoalNearestGolemTarget(final EntityCreature entitycreature, Class<T> oclass, int i, boolean flag, boolean flag1, final Predicate<? super T> predicate)
    {
      super(oclass, i, flag, flag1, predicate);
      this.c = new Predicate()
      {
        public boolean a(T t0)
        {
          if ((predicate != null) && (!predicate.apply(t0))) {
            return false;
          }
          if ((t0 instanceof EntityCreeper)) {
            return false;
          }
          if ((t0 instanceof EntityHuman))
          {
            double d0 = EntityIronGolem.PathfinderGoalNearestGolemTarget.this.f();
            if (t0.isSneaking()) {
              d0 *= 0.800000011920929D;
            }
            if (t0.isInvisible())
            {
              float f = ((EntityHuman)t0).bY();
              if (f < 0.1F) {
                f = 0.1F;
              }
              d0 *= 0.7F * f;
            }
            if (t0.g(entitycreature) > d0) {
              return false;
            }
          }
          return EntityIronGolem.PathfinderGoalNearestGolemTarget.this.a(t0, false);
        }
        
        public boolean apply(Object object)
        {
          return a((EntityLiving)object);
        }
      };
    }
  }
}