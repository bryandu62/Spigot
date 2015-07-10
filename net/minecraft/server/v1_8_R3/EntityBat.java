package net.minecraft.server.v1_8_R3;

import java.util.Calendar;
import java.util.Random;

public class EntityBat
  extends EntityAmbient
{
  private BlockPosition a;
  
  public EntityBat(World ☃)
  {
    super(☃);
    
    setSize(0.5F, 0.9F);
    setAsleep(true);
  }
  
  protected void h()
  {
    super.h();
    
    this.datawatcher.a(16, new Byte((byte)0));
  }
  
  protected float bB()
  {
    return 0.1F;
  }
  
  protected float bC()
  {
    return super.bC() * 0.95F;
  }
  
  protected String z()
  {
    if ((isAsleep()) && (this.random.nextInt(4) != 0)) {
      return null;
    }
    return "mob.bat.idle";
  }
  
  protected String bo()
  {
    return "mob.bat.hurt";
  }
  
  protected String bp()
  {
    return "mob.bat.death";
  }
  
  public boolean ae()
  {
    return false;
  }
  
  protected void s(Entity ☃) {}
  
  protected void bL() {}
  
  protected void initAttributes()
  {
    super.initAttributes();
    
    getAttributeInstance(GenericAttributes.maxHealth).setValue(6.0D);
  }
  
  public boolean isAsleep()
  {
    return (this.datawatcher.getByte(16) & 0x1) != 0;
  }
  
  public void setAsleep(boolean ☃)
  {
    byte ☃ = this.datawatcher.getByte(16);
    if (☃) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(☃ | 0x1)));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)(☃ & 0xFFFFFFFE)));
    }
  }
  
  public void t_()
  {
    super.t_();
    if (isAsleep())
    {
      this.motX = (this.motY = this.motZ = 0.0D);
      this.locY = (MathHelper.floor(this.locY) + 1.0D - this.length);
    }
    else
    {
      this.motY *= 0.6000000238418579D;
    }
  }
  
  protected void E()
  {
    super.E();
    
    BlockPosition ☃ = new BlockPosition(this);
    BlockPosition ☃ = ☃.up();
    if (isAsleep())
    {
      if (!this.world.getType(☃).getBlock().isOccluding())
      {
        setAsleep(false);
        this.world.a(null, 1015, ☃, 0);
      }
      else
      {
        if (this.random.nextInt(200) == 0) {
          this.aK = this.random.nextInt(360);
        }
        if (this.world.findNearbyPlayer(this, 4.0D) != null)
        {
          setAsleep(false);
          this.world.a(null, 1015, ☃, 0);
        }
      }
    }
    else
    {
      if ((this.a != null) && ((!this.world.isEmpty(this.a)) || (this.a.getY() < 1))) {
        this.a = null;
      }
      if ((this.a == null) || (this.random.nextInt(30) == 0) || (this.a.c((int)this.locX, (int)this.locY, (int)this.locZ) < 4.0D)) {
        this.a = new BlockPosition((int)this.locX + this.random.nextInt(7) - this.random.nextInt(7), (int)this.locY + this.random.nextInt(6) - 2, (int)this.locZ + this.random.nextInt(7) - this.random.nextInt(7));
      }
      double ☃ = this.a.getX() + 0.5D - this.locX;
      double ☃ = this.a.getY() + 0.1D - this.locY;
      double ☃ = this.a.getZ() + 0.5D - this.locZ;
      
      this.motX += (Math.signum(☃) * 0.5D - this.motX) * 0.10000000149011612D;
      this.motY += (Math.signum(☃) * 0.699999988079071D - this.motY) * 0.10000000149011612D;
      this.motZ += (Math.signum(☃) * 0.5D - this.motZ) * 0.10000000149011612D;
      
      float ☃ = (float)(MathHelper.b(this.motZ, this.motX) * 180.0D / 3.1415927410125732D) - 90.0F;
      float ☃ = MathHelper.g(☃ - this.yaw);
      this.ba = 0.5F;
      this.yaw += ☃;
      if ((this.random.nextInt(100) == 0) && (this.world.getType(☃).getBlock().isOccluding())) {
        setAsleep(true);
      }
    }
  }
  
  protected boolean s_()
  {
    return false;
  }
  
  public void e(float ☃, float ☃) {}
  
  protected void a(double ☃, boolean ☃, Block ☃, BlockPosition ☃) {}
  
  public boolean aI()
  {
    return true;
  }
  
  public boolean damageEntity(DamageSource ☃, float ☃)
  {
    if (isInvulnerable(☃)) {
      return false;
    }
    if ((!this.world.isClientSide) && 
      (isAsleep())) {
      setAsleep(false);
    }
    return super.damageEntity(☃, ☃);
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    
    this.datawatcher.watch(16, Byte.valueOf(☃.getByte("BatFlags")));
  }
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    
    ☃.setByte("BatFlags", this.datawatcher.getByte(16));
  }
  
  public boolean bR()
  {
    BlockPosition ☃ = new BlockPosition(this.locX, getBoundingBox().b, this.locZ);
    if (☃.getY() >= this.world.F()) {
      return false;
    }
    int ☃ = this.world.getLightLevel(☃);
    int ☃ = 4;
    if (a(this.world.Y())) {
      ☃ = 7;
    } else if (this.random.nextBoolean()) {
      return false;
    }
    if (☃ > this.random.nextInt(☃)) {
      return false;
    }
    return super.bR();
  }
  
  private boolean a(Calendar ☃)
  {
    return ((☃.get(2) + 1 == 10) && (☃.get(5) >= 20)) || ((☃.get(2) + 1 == 11) && (☃.get(5) <= 3));
  }
  
  public float getHeadHeight()
  {
    return this.length / 2.0F;
  }
}
