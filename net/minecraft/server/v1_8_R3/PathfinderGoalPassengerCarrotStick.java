package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class PathfinderGoalPassengerCarrotStick
  extends PathfinderGoal
{
  private final EntityInsentient a;
  private final float b;
  private float c;
  private boolean d;
  private int e;
  private int f;
  
  public PathfinderGoalPassengerCarrotStick(EntityInsentient ☃, float ☃)
  {
    this.a = ☃;
    this.b = ☃;
    a(7);
  }
  
  public void c()
  {
    this.c = 0.0F;
  }
  
  public void d()
  {
    this.d = false;
    this.c = 0.0F;
  }
  
  public boolean a()
  {
    return (this.a.isAlive()) && (this.a.passenger != null) && ((this.a.passenger instanceof EntityHuman)) && ((this.d) || (this.a.bW()));
  }
  
  public void e()
  {
    EntityHuman ☃ = (EntityHuman)this.a.passenger;
    EntityCreature ☃ = (EntityCreature)this.a;
    
    float ☃ = MathHelper.g(☃.yaw - this.a.yaw) * 0.5F;
    if (☃ > 5.0F) {
      ☃ = 5.0F;
    }
    if (☃ < -5.0F) {
      ☃ = -5.0F;
    }
    this.a.yaw = MathHelper.g(this.a.yaw + ☃);
    if (this.c < this.b) {
      this.c += (this.b - this.c) * 0.01F;
    }
    if (this.c > this.b) {
      this.c = this.b;
    }
    int ☃ = MathHelper.floor(this.a.locX);
    int ☃ = MathHelper.floor(this.a.locY);
    int ☃ = MathHelper.floor(this.a.locZ);
    float ☃ = this.c;
    if (this.d)
    {
      if (this.e++ > this.f) {
        this.d = false;
      }
      ☃ += ☃ * 1.15F * MathHelper.sin(this.e / this.f * 3.1415927F);
    }
    float ☃ = 0.91F;
    if (this.a.onGround) {
      ☃ = this.a.world.getType(new BlockPosition(MathHelper.d(☃), MathHelper.d(☃) - 1, MathHelper.d(☃))).getBlock().frictionFactor * 0.91F;
    }
    float ☃ = 0.16277136F / (☃ * ☃ * ☃);
    float ☃ = MathHelper.sin(☃.yaw * 3.1415927F / 180.0F);
    float ☃ = MathHelper.cos(☃.yaw * 3.1415927F / 180.0F);
    float ☃ = ☃.bI() * ☃;
    float ☃ = Math.max(☃, 1.0F);
    ☃ = ☃ / ☃;
    float ☃ = ☃ * ☃;
    float ☃ = -(☃ * ☃);
    float ☃ = ☃ * ☃;
    if (MathHelper.e(☃) > MathHelper.e(☃))
    {
      if (☃ < 0.0F) {
        ☃ -= this.a.width / 2.0F;
      }
      if (☃ > 0.0F) {
        ☃ += this.a.width / 2.0F;
      }
      ☃ = 0.0F;
    }
    else
    {
      ☃ = 0.0F;
      if (☃ < 0.0F) {
        ☃ -= this.a.width / 2.0F;
      }
      if (☃ > 0.0F) {
        ☃ += this.a.width / 2.0F;
      }
    }
    int ☃ = MathHelper.floor(this.a.locX + ☃);
    int ☃ = MathHelper.floor(this.a.locZ + ☃);
    
    int ☃ = MathHelper.d(this.a.width + 1.0F);
    int ☃ = MathHelper.d(this.a.length + ☃.length + 1.0F);
    int ☃ = MathHelper.d(this.a.width + 1.0F);
    if ((☃ != ☃) || (☃ != ☃))
    {
      Block ☃ = this.a.world.getType(new BlockPosition(☃, ☃, ☃)).getBlock();
      boolean ☃ = (!a(☃)) && ((☃.getMaterial() != Material.AIR) || (!a(this.a.world.getType(new BlockPosition(☃, ☃ - 1, ☃)).getBlock())));
      if ((☃) && (0 == PathfinderNormal.a(this.a.world, this.a, ☃, ☃, ☃, ☃, ☃, ☃, false, false, true)) && (1 == PathfinderNormal.a(this.a.world, this.a, ☃, ☃ + 1, ☃, ☃, ☃, ☃, false, false, true)) && (1 == PathfinderNormal.a(this.a.world, this.a, ☃, ☃ + 1, ☃, ☃, ☃, ☃, false, false, true))) {
        ☃.getControllerJump().a();
      }
    }
    if ((!☃.abilities.canInstantlyBuild) && (this.c >= this.b * 0.5F) && (this.a.bc().nextFloat() < 0.006F) && (!this.d))
    {
      ItemStack ☃ = ☃.bA();
      if ((☃ != null) && (☃.getItem() == Items.CARROT_ON_A_STICK))
      {
        ☃.damage(1, ☃);
        if (☃.count == 0)
        {
          ItemStack ☃ = new ItemStack(Items.FISHING_ROD);
          ☃.setTag(☃.getTag());
          ☃.inventory.items[☃.inventory.itemInHandIndex] = ☃;
        }
      }
    }
    this.a.g(0.0F, ☃);
  }
  
  private boolean a(Block ☃)
  {
    return ((☃ instanceof BlockStairs)) || ((☃ instanceof BlockStepAbstract));
  }
  
  public boolean f()
  {
    return this.d;
  }
  
  public void g()
  {
    this.d = true;
    this.e = 0;
    this.f = (this.a.bc().nextInt(841) + 140);
  }
  
  public boolean h()
  {
    return (!f()) && (this.c > this.b * 0.3F);
  }
}
