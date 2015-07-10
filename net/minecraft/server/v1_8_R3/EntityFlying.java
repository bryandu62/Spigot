package net.minecraft.server.v1_8_R3;

public abstract class EntityFlying
  extends EntityInsentient
{
  public EntityFlying(World ☃)
  {
    super(☃);
  }
  
  public void e(float ☃, float ☃) {}
  
  protected void a(double ☃, boolean ☃, Block ☃, BlockPosition ☃) {}
  
  public void g(float ☃, float ☃)
  {
    if (V())
    {
      a(☃, ☃, 0.02F);
      move(this.motX, this.motY, this.motZ);
      
      this.motX *= 0.800000011920929D;
      this.motY *= 0.800000011920929D;
      this.motZ *= 0.800000011920929D;
    }
    else if (ab())
    {
      a(☃, ☃, 0.02F);
      move(this.motX, this.motY, this.motZ);
      this.motX *= 0.5D;
      this.motY *= 0.5D;
      this.motZ *= 0.5D;
    }
    else
    {
      float ☃ = 0.91F;
      if (this.onGround) {
        ☃ = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
      }
      float ☃ = 0.16277136F / (☃ * ☃ * ☃);
      a(☃, ☃, this.onGround ? 0.1F * ☃ : 0.02F);
      
      ☃ = 0.91F;
      if (this.onGround) {
        ☃ = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
      }
      move(this.motX, this.motY, this.motZ);
      
      this.motX *= ☃;
      this.motY *= ☃;
      this.motZ *= ☃;
    }
    this.aA = this.aB;
    double ☃ = this.locX - this.lastX;
    double ☃ = this.locZ - this.lastZ;
    float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃) * 4.0F;
    if (☃ > 1.0F) {
      ☃ = 1.0F;
    }
    this.aB += (☃ - this.aB) * 0.4F;
    this.aC += this.aB;
  }
  
  public boolean k_()
  {
    return false;
  }
}
