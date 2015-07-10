package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class EntityMinecartFurnace
  extends EntityMinecartAbstract
{
  private int c;
  public double a;
  public double b;
  
  public EntityMinecartFurnace(World ☃)
  {
    super(☃);
  }
  
  public EntityMinecartFurnace(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public EntityMinecartAbstract.EnumMinecartType s()
  {
    return EntityMinecartAbstract.EnumMinecartType.FURNACE;
  }
  
  protected void h()
  {
    super.h();
    this.datawatcher.a(16, new Byte((byte)0));
  }
  
  public void t_()
  {
    super.t_();
    if (this.c > 0) {
      this.c -= 1;
    }
    if (this.c <= 0) {
      this.a = (this.b = 0.0D);
    }
    i(this.c > 0);
    if ((j()) && (this.random.nextInt(4) == 0)) {
      this.world.addParticle(EnumParticle.SMOKE_LARGE, this.locX, this.locY + 0.8D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
    }
  }
  
  protected double m()
  {
    return 0.2D;
  }
  
  public void a(DamageSource ☃)
  {
    super.a(☃);
    if ((!☃.isExplosion()) && (this.world.getGameRules().getBoolean("doEntityDrops"))) {
      a(new ItemStack(Blocks.FURNACE, 1), 0.0F);
    }
  }
  
  protected void a(BlockPosition ☃, IBlockData ☃)
  {
    super.a(☃, ☃);
    
    double ☃ = this.a * this.a + this.b * this.b;
    if ((☃ > 1.0E-4D) && (this.motX * this.motX + this.motZ * this.motZ > 0.001D))
    {
      ☃ = MathHelper.sqrt(☃);
      this.a /= ☃;
      this.b /= ☃;
      if (this.a * this.motX + this.b * this.motZ < 0.0D)
      {
        this.a = 0.0D;
        this.b = 0.0D;
      }
      else
      {
        double ☃ = ☃ / m();
        this.a *= ☃;
        this.b *= ☃;
      }
    }
  }
  
  protected void o()
  {
    double ☃ = this.a * this.a + this.b * this.b;
    if (☃ > 1.0E-4D)
    {
      ☃ = MathHelper.sqrt(☃);
      this.a /= ☃;
      this.b /= ☃;
      double ☃ = 1.0D;
      this.motX *= 0.800000011920929D;
      this.motY *= 0.0D;
      this.motZ *= 0.800000011920929D;
      this.motX += this.a * ☃;
      this.motZ += this.b * ☃;
    }
    else
    {
      this.motX *= 0.9800000190734863D;
      this.motY *= 0.0D;
      this.motZ *= 0.9800000190734863D;
    }
    super.o();
  }
  
  public boolean e(EntityHuman ☃)
  {
    ItemStack ☃ = ☃.inventory.getItemInHand();
    if ((☃ != null) && (☃.getItem() == Items.COAL))
    {
      if (!☃.abilities.canInstantlyBuild) {
        if (--☃.count == 0) {
          ☃.inventory.setItem(☃.inventory.itemInHandIndex, null);
        }
      }
      this.c += 3600;
    }
    this.a = (this.locX - ☃.locX);
    this.b = (this.locZ - ☃.locZ);
    
    return true;
  }
  
  protected void b(NBTTagCompound ☃)
  {
    super.b(☃);
    ☃.setDouble("PushX", this.a);
    ☃.setDouble("PushZ", this.b);
    ☃.setShort("Fuel", (short)this.c);
  }
  
  protected void a(NBTTagCompound ☃)
  {
    super.a(☃);
    this.a = ☃.getDouble("PushX");
    this.b = ☃.getDouble("PushZ");
    this.c = ☃.getShort("Fuel");
  }
  
  protected boolean j()
  {
    return (this.datawatcher.getByte(16) & 0x1) != 0;
  }
  
  protected void i(boolean ☃)
  {
    if (☃) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(this.datawatcher.getByte(16) | 0x1)));
    } else {
      this.datawatcher.watch(16, Byte.valueOf((byte)(this.datawatcher.getByte(16) & 0xFFFFFFFE)));
    }
  }
  
  public IBlockData u()
  {
    return (j() ? Blocks.LIT_FURNACE : Blocks.FURNACE).getBlockData().set(BlockFurnace.FACING, EnumDirection.NORTH);
  }
}
