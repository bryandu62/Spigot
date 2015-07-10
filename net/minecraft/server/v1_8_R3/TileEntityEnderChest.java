package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class TileEntityEnderChest
  extends TileEntity
  implements IUpdatePlayerListBox
{
  public float a;
  public float f;
  public int g;
  private int h;
  
  public void c()
  {
    if (++this.h % 20 * 4 == 0) {
      this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
    }
    this.f = this.a;
    
    int ☃ = this.position.getX();
    int ☃ = this.position.getY();
    int ☃ = this.position.getZ();
    
    float ☃ = 0.1F;
    if ((this.g > 0) && (this.a == 0.0F))
    {
      double ☃ = ☃ + 0.5D;
      double ☃ = ☃ + 0.5D;
      
      this.world.makeSound(☃, ☃ + 0.5D, ☃, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
    if (((this.g == 0) && (this.a > 0.0F)) || ((this.g > 0) && (this.a < 1.0F)))
    {
      float ☃ = this.a;
      if (this.g > 0) {
        this.a += ☃;
      } else {
        this.a -= ☃;
      }
      if (this.a > 1.0F) {
        this.a = 1.0F;
      }
      float ☃ = 0.5F;
      if ((this.a < ☃) && (☃ >= ☃))
      {
        double ☃ = ☃ + 0.5D;
        double ☃ = ☃ + 0.5D;
        
        this.world.makeSound(☃, ☃ + 0.5D, ☃, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
      }
      if (this.a < 0.0F) {
        this.a = 0.0F;
      }
    }
  }
  
  public boolean c(int ☃, int ☃)
  {
    if (☃ == 1)
    {
      this.g = ☃;
      return true;
    }
    return super.c(☃, ☃);
  }
  
  public void y()
  {
    E();
    super.y();
  }
  
  public void b()
  {
    this.g += 1;
    this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
  }
  
  public void d()
  {
    this.g -= 1;
    this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
  }
  
  public boolean a(EntityHuman ☃)
  {
    if (this.world.getTileEntity(this.position) != this) {
      return false;
    }
    if (☃.e(this.position.getX() + 0.5D, this.position.getY() + 0.5D, this.position.getZ() + 0.5D) > 64.0D) {
      return false;
    }
    return true;
  }
}
