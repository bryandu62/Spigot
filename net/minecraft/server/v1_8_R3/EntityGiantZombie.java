package net.minecraft.server.v1_8_R3;

public class EntityGiantZombie
  extends EntityMonster
{
  public EntityGiantZombie(World ☃)
  {
    super(☃);
    setSize(this.width * 6.0F, this.length * 6.0F);
  }
  
  public float getHeadHeight()
  {
    return 10.440001F;
  }
  
  protected void initAttributes()
  {
    super.initAttributes();
    
    getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
    getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(50.0D);
  }
  
  public float a(BlockPosition ☃)
  {
    return this.world.o(☃) - 0.5F;
  }
}
