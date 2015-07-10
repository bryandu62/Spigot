package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class EntityWaterAnimal
  extends EntityInsentient
  implements IAnimal
{
  public EntityWaterAnimal(World ☃)
  {
    super(☃);
  }
  
  public boolean aY()
  {
    return true;
  }
  
  public boolean bR()
  {
    return true;
  }
  
  public boolean canSpawn()
  {
    return this.world.a(getBoundingBox(), this);
  }
  
  public int w()
  {
    return 120;
  }
  
  protected boolean isTypeNotPersistent()
  {
    return true;
  }
  
  protected int getExpValue(EntityHuman ☃)
  {
    return 1 + this.world.random.nextInt(3);
  }
  
  public void K()
  {
    int ☃ = getAirTicks();
    
    super.K();
    if ((isAlive()) && (!V()))
    {
      setAirTicks(--☃);
      if (getAirTicks() == -20)
      {
        setAirTicks(0);
        damageEntity(DamageSource.DROWN, 2.0F);
      }
    }
    else
    {
      setAirTicks(300);
    }
  }
  
  public boolean aL()
  {
    return false;
  }
}
