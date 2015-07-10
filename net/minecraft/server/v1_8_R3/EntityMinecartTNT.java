package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EntityMinecartTNT
  extends EntityMinecartAbstract
{
  private int a = -1;
  
  public EntityMinecartTNT(World ☃)
  {
    super(☃);
  }
  
  public EntityMinecartTNT(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public EntityMinecartAbstract.EnumMinecartType s()
  {
    return EntityMinecartAbstract.EnumMinecartType.TNT;
  }
  
  public IBlockData u()
  {
    return Blocks.TNT.getBlockData();
  }
  
  public void t_()
  {
    super.t_();
    if (this.a > 0)
    {
      this.a -= 1;
      this.world.addParticle(EnumParticle.SMOKE_NORMAL, this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
    }
    else if (this.a == 0)
    {
      b(this.motX * this.motX + this.motZ * this.motZ);
    }
    if (this.positionChanged)
    {
      double ☃ = this.motX * this.motX + this.motZ * this.motZ;
      if (☃ >= 0.009999999776482582D) {
        b(☃);
      }
    }
  }
  
  public boolean damageEntity(DamageSource ☃, float ☃)
  {
    Entity ☃ = ☃.i();
    if ((☃ instanceof EntityArrow))
    {
      EntityArrow ☃ = (EntityArrow)☃;
      if (☃.isBurning()) {
        b(☃.motX * ☃.motX + ☃.motY * ☃.motY + ☃.motZ * ☃.motZ);
      }
    }
    return super.damageEntity(☃, ☃);
  }
  
  public void a(DamageSource ☃)
  {
    super.a(☃);
    
    double ☃ = this.motX * this.motX + this.motZ * this.motZ;
    if ((!☃.isExplosion()) && (this.world.getGameRules().getBoolean("doEntityDrops"))) {
      a(new ItemStack(Blocks.TNT, 1), 0.0F);
    }
    if ((☃.o()) || (☃.isExplosion()) || (☃ >= 0.009999999776482582D)) {
      b(☃);
    }
  }
  
  protected void b(double ☃)
  {
    if (!this.world.isClientSide)
    {
      double ☃ = Math.sqrt(☃);
      if (☃ > 5.0D) {
        ☃ = 5.0D;
      }
      this.world.explode(this, this.locX, this.locY, this.locZ, (float)(4.0D + this.random.nextDouble() * 1.5D * ☃), true);
      die();
    }
  }
  
  public void e(float ☃, float ☃)
  {
    if (☃ >= 3.0F)
    {
      float ☃ = ☃ / 10.0F;
      b(☃ * ☃);
    }
    super.e(☃, ☃);
  }
  
  public void a(int ☃, int ☃, int ☃, boolean ☃)
  {
    if ((☃) && (this.a < 0)) {
      j();
    }
  }
  
  public void j()
  {
    this.a = 80;
    if (!this.world.isClientSide)
    {
      this.world.broadcastEntityEffect(this, (byte)10);
      if (!R()) {
        this.world.makeSound(this, "game.tnt.primed", 1.0F, 1.0F);
      }
    }
  }
  
  public boolean y()
  {
    return this.a > -1;
  }
  
  public float a(Explosion ☃, World ☃, BlockPosition ☃, IBlockData ☃)
  {
    if ((y()) && ((BlockMinecartTrackAbstract.d(☃)) || (BlockMinecartTrackAbstract.e(☃, ☃.up())))) {
      return 0.0F;
    }
    return super.a(☃, ☃, ☃, ☃);
  }
  
  public boolean a(Explosion ☃, World ☃, BlockPosition ☃, IBlockData ☃, float ☃)
  {
    if ((y()) && ((BlockMinecartTrackAbstract.d(☃)) || (BlockMinecartTrackAbstract.e(☃, ☃.up())))) {
      return false;
    }
    return super.a(☃, ☃, ☃, ☃, ☃);
  }
  
  protected void a(NBTTagCompound ☃)
  {
    super.a(☃);
    if (☃.hasKeyOfType("TNTFuse", 99)) {
      this.a = ☃.getInt("TNTFuse");
    }
  }
  
  protected void b(NBTTagCompound ☃)
  {
    super.b(☃);
    ☃.setInt("TNTFuse", this.a);
  }
}
