package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EntityWitch
  extends EntityMonster
  implements IRangedEntity
{
  private static final UUID a = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
  private static final AttributeModifier b = new AttributeModifier(a, "Drinking speed penalty", -0.25D, 0).a(false);
  private static final Item[] c = { Items.GLOWSTONE_DUST, Items.SUGAR, Items.REDSTONE, Items.SPIDER_EYE, Items.GLASS_BOTTLE, Items.GUNPOWDER, Items.STICK, Items.STICK };
  private int bm;
  
  public EntityWitch(World ☃)
  {
    super(☃);
    setSize(0.6F, 1.95F);
    
    this.goalSelector.a(1, new PathfinderGoalFloat(this));
    this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 60, 10.0F));
    this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
    this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
    
    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
  }
  
  protected void h()
  {
    super.h();
    
    getDataWatcher().a(21, Byte.valueOf((byte)0));
  }
  
  protected String z()
  {
    return null;
  }
  
  protected String bo()
  {
    return null;
  }
  
  protected String bp()
  {
    return null;
  }
  
  public void a(boolean ☃)
  {
    getDataWatcher().watch(21, Byte.valueOf((byte)(☃ ? 1 : 0)));
  }
  
  public boolean n()
  {
    return getDataWatcher().getByte(21) == 1;
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    
    getAttributeInstance(GenericAttributes.maxHealth).setValue(26.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
  }
  
  public void m()
  {
    if (!this.world.isClientSide)
    {
      if (n())
      {
        if (this.bm-- <= 0)
        {
          a(false);
          ItemStack ☃ = bA();
          setEquipment(0, null);
          if ((☃ != null) && (☃.getItem() == Items.POTION))
          {
            List<MobEffect> ☃ = Items.POTION.h(☃);
            if (☃ != null) {
              for (MobEffect ☃ : ☃) {
                addEffect(new MobEffect(☃));
              }
            }
          }
          getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).c(b);
        }
      }
      else
      {
        int ☃ = -1;
        if ((this.random.nextFloat() < 0.15F) && (a(Material.WATER)) && (!hasEffect(MobEffectList.WATER_BREATHING))) {
          ☃ = 8237;
        } else if ((this.random.nextFloat() < 0.15F) && (isBurning()) && (!hasEffect(MobEffectList.FIRE_RESISTANCE))) {
          ☃ = 16307;
        } else if ((this.random.nextFloat() < 0.05F) && (getHealth() < getMaxHealth())) {
          ☃ = 16341;
        } else if ((this.random.nextFloat() < 0.25F) && (getGoalTarget() != null) && (!hasEffect(MobEffectList.FASTER_MOVEMENT)) && (getGoalTarget().h(this) > 121.0D)) {
          ☃ = 16274;
        } else if ((this.random.nextFloat() < 0.25F) && (getGoalTarget() != null) && (!hasEffect(MobEffectList.FASTER_MOVEMENT)) && (getGoalTarget().h(this) > 121.0D)) {
          ☃ = 16274;
        }
        if (☃ > -1)
        {
          setEquipment(0, new ItemStack(Items.POTION, 1, ☃));
          this.bm = bA().l();
          a(true);
          AttributeInstance ☃ = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
          ☃.c(b);
          ☃.b(b);
        }
      }
      if (this.random.nextFloat() < 7.5E-4F) {
        this.world.broadcastEntityEffect(this, (byte)15);
      }
    }
    super.m();
  }
  
  protected float applyMagicModifier(DamageSource ☃, float ☃)
  {
    ☃ = super.applyMagicModifier(☃, ☃);
    if (☃.getEntity() == this) {
      ☃ = 0.0F;
    }
    if (☃.isMagic()) {
      ☃ = (float)(☃ * 0.15D);
    }
    return ☃;
  }
  
  protected void dropDeathLoot(boolean ☃, int ☃)
  {
    int ☃ = this.random.nextInt(3) + 1;
    for (int ☃ = 0; ☃ < ☃; ☃++)
    {
      int ☃ = this.random.nextInt(3);
      Item ☃ = c[this.random.nextInt(c.length)];
      if (☃ > 0) {
        ☃ += this.random.nextInt(☃ + 1);
      }
      for (int ☃ = 0; ☃ < ☃; ☃++) {
        a(☃, 1);
      }
    }
  }
  
  public void a(EntityLiving ☃, float ☃)
  {
    if (n()) {
      return;
    }
    EntityPotion ☃ = new EntityPotion(this.world, this, 32732);
    double ☃ = ☃.locY + ☃.getHeadHeight() - 1.100000023841858D;
    ☃.pitch -= -20.0F;
    double ☃ = ☃.locX + ☃.motX - this.locX;
    double ☃ = ☃ - this.locY;
    double ☃ = ☃.locZ + ☃.motZ - this.locZ;
    float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
    if ((☃ >= 8.0F) && (!☃.hasEffect(MobEffectList.SLOWER_MOVEMENT))) {
      ☃.setPotionValue(32698);
    } else if ((☃.getHealth() >= 8.0F) && (!☃.hasEffect(MobEffectList.POISON))) {
      ☃.setPotionValue(32660);
    } else if ((☃ <= 3.0F) && (!☃.hasEffect(MobEffectList.WEAKNESS)) && (this.random.nextFloat() < 0.25F)) {
      ☃.setPotionValue(32696);
    }
    ☃.shoot(☃, ☃ + ☃ * 0.2F, ☃, 0.75F, 8.0F);
    
    this.world.addEntity(☃);
  }
  
  public float getHeadHeight()
  {
    return 1.62F;
  }
}
