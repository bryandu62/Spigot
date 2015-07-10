package net.minecraft.server.v1_8_R3;

public class EntitySnowball
  extends EntityProjectile
{
  public EntitySnowball(World ☃)
  {
    super(☃);
  }
  
  public EntitySnowball(World ☃, EntityLiving ☃)
  {
    super(☃, ☃);
  }
  
  public EntitySnowball(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  protected void a(MovingObjectPosition ☃)
  {
    if (☃.entity != null)
    {
      int ☃ = 0;
      if ((☃.entity instanceof EntityBlaze)) {
        ☃ = 3;
      }
      ☃.entity.damageEntity(DamageSource.projectile(this, getShooter()), ☃);
    }
    for (int ☃ = 0; ☃ < 8; ☃++) {
      this.world.addParticle(EnumParticle.SNOWBALL, this.locX, this.locY, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
    }
    if (!this.world.isClientSide) {
      die();
    }
  }
}
