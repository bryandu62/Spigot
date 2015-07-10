package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EntityEnderSignal
  extends Entity
{
  private double a;
  private double b;
  private double c;
  private int d;
  private boolean e;
  
  public EntityEnderSignal(World ☃)
  {
    super(☃);
    setSize(0.25F, 0.25F);
  }
  
  protected void h() {}
  
  public EntityEnderSignal(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃);
    this.d = 0;
    
    setSize(0.25F, 0.25F);
    
    setPosition(☃, ☃, ☃);
  }
  
  public void a(BlockPosition ☃)
  {
    double ☃ = ☃.getX();
    int ☃ = ☃.getY();
    double ☃ = ☃.getZ();
    
    double ☃ = ☃ - this.locX;double ☃ = ☃ - this.locZ;
    float ☃ = MathHelper.sqrt(☃ * ☃ + ☃ * ☃);
    if (☃ > 12.0F)
    {
      this.a = (this.locX + ☃ / ☃ * 12.0D);
      this.c = (this.locZ + ☃ / ☃ * 12.0D);
      this.b = (this.locY + 8.0D);
    }
    else
    {
      this.a = ☃;
      this.b = ☃;
      this.c = ☃;
    }
    this.d = 0;
    this.e = (this.random.nextInt(5) > 0);
  }
  
  public void t_()
  {
    this.P = this.locX;
    this.Q = this.locY;
    this.R = this.locZ;
    super.t_();
    
    this.locX += this.motX;
    this.locY += this.motY;
    this.locZ += this.motZ;
    
    float ☃ = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
    this.yaw = ((float)(MathHelper.b(this.motX, this.motZ) * 180.0D / 3.1415927410125732D));
    this.pitch = ((float)(MathHelper.b(this.motY, ☃) * 180.0D / 3.1415927410125732D));
    while (this.pitch - this.lastPitch < -180.0F) {
      this.lastPitch -= 360.0F;
    }
    while (this.pitch - this.lastPitch >= 180.0F) {
      this.lastPitch += 360.0F;
    }
    while (this.yaw - this.lastYaw < -180.0F) {
      this.lastYaw -= 360.0F;
    }
    while (this.yaw - this.lastYaw >= 180.0F) {
      this.lastYaw += 360.0F;
    }
    this.pitch = (this.lastPitch + (this.pitch - this.lastPitch) * 0.2F);
    this.yaw = (this.lastYaw + (this.yaw - this.lastYaw) * 0.2F);
    if (!this.world.isClientSide)
    {
      double ☃ = this.a - this.locX;double ☃ = this.c - this.locZ;
      float ☃ = (float)Math.sqrt(☃ * ☃ + ☃ * ☃);
      float ☃ = (float)MathHelper.b(☃, ☃);
      double ☃ = ☃ + (☃ - ☃) * 0.0025D;
      if (☃ < 1.0F)
      {
        ☃ *= 0.8D;
        this.motY *= 0.8D;
      }
      this.motX = (Math.cos(☃) * ☃);
      this.motZ = (Math.sin(☃) * ☃);
      if (this.locY < this.b) {
        this.motY += (1.0D - this.motY) * 0.014999999664723873D;
      } else {
        this.motY += (-1.0D - this.motY) * 0.014999999664723873D;
      }
    }
    float ☃ = 0.25F;
    if (V()) {
      for (int ☃ = 0; ☃ < 4; ☃++) {
        this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - this.motX * ☃, this.locY - this.motY * ☃, this.locZ - this.motZ * ☃, this.motX, this.motY, this.motZ, new int[0]);
      }
    } else {
      this.world.addParticle(EnumParticle.PORTAL, this.locX - this.motX * ☃ + this.random.nextDouble() * 0.6D - 0.3D, this.locY - this.motY * ☃ - 0.5D, this.locZ - this.motZ * ☃ + this.random.nextDouble() * 0.6D - 0.3D, this.motX, this.motY, this.motZ, new int[0]);
    }
    if (!this.world.isClientSide)
    {
      setPosition(this.locX, this.locY, this.locZ);
      
      this.d += 1;
      if ((this.d > 80) && (!this.world.isClientSide))
      {
        die();
        if (this.e) {
          this.world.addEntity(new EntityItem(this.world, this.locX, this.locY, this.locZ, new ItemStack(Items.ENDER_EYE)));
        } else {
          this.world.triggerEffect(2003, new BlockPosition(this), 0);
        }
      }
    }
  }
  
  public void b(NBTTagCompound ☃) {}
  
  public void a(NBTTagCompound ☃) {}
  
  public float c(float ☃)
  {
    return 1.0F;
  }
  
  public boolean aD()
  {
    return false;
  }
}
