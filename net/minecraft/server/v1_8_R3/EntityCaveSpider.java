package net.minecraft.server.v1_8_R3;

public class EntityCaveSpider
  extends EntitySpider
{
  public EntityCaveSpider(World ☃)
  {
    super(☃);
    setSize(0.7F, 0.5F);
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    
    getAttributeInstance(GenericAttributes.maxHealth).setValue(12.0D);
  }
  
  public boolean r(Entity ☃)
  {
    if (super.r(☃))
    {
      if ((☃ instanceof EntityLiving))
      {
        int ☃ = 0;
        if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
          ☃ = 7;
        } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
          ☃ = 15;
        }
        if (☃ > 0) {
          ((EntityLiving)☃).addEffect(new MobEffect(MobEffectList.POISON.id, ☃ * 20, 0));
        }
      }
      return true;
    }
    return false;
  }
  
  public GroupDataEntity prepare(DifficultyDamageScaler ☃, GroupDataEntity ☃)
  {
    return ☃;
  }
  
  public float getHeadHeight()
  {
    return 0.45F;
  }
}
