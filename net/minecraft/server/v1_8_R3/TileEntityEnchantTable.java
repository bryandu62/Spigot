package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class TileEntityEnchantTable
  extends TileEntity
  implements IUpdatePlayerListBox, ITileEntityContainer
{
  public int a;
  public float f;
  public float g;
  public float h;
  public float i;
  public float j;
  public float k;
  public float l;
  public float m;
  public float n;
  private static Random o = new Random();
  private String p;
  
  public void b(NBTTagCompound ☃)
  {
    super.b(☃);
    if (hasCustomName()) {
      ☃.setString("CustomName", this.p);
    }
  }
  
  public void a(NBTTagCompound ☃)
  {
    super.a(☃);
    if (☃.hasKeyOfType("CustomName", 8)) {
      this.p = ☃.getString("CustomName");
    }
  }
  
  public void c()
  {
    this.k = this.j;
    this.m = this.l;
    
    EntityHuman ☃ = this.world.findNearbyPlayer(this.position.getX() + 0.5F, this.position.getY() + 0.5F, this.position.getZ() + 0.5F, 3.0D);
    if (☃ != null)
    {
      double ☃ = ☃.locX - (this.position.getX() + 0.5F);
      double ☃ = ☃.locZ - (this.position.getZ() + 0.5F);
      
      this.n = ((float)MathHelper.b(☃, ☃));
      
      this.j += 0.1F;
      if ((this.j < 0.5F) || (o.nextInt(40) == 0))
      {
        float ☃ = this.h;
        do
        {
          this.h += o.nextInt(4) - o.nextInt(4);
        } while (☃ == this.h);
      }
    }
    else
    {
      this.n += 0.02F;
      this.j -= 0.1F;
    }
    while (this.l >= 3.1415927F) {
      this.l -= 6.2831855F;
    }
    while (this.l < -3.1415927F) {
      this.l += 6.2831855F;
    }
    while (this.n >= 3.1415927F) {
      this.n -= 6.2831855F;
    }
    while (this.n < -3.1415927F) {
      this.n += 6.2831855F;
    }
    float ☃ = this.n - this.l;
    while (☃ >= 3.1415927F) {
      ☃ -= 6.2831855F;
    }
    while (☃ < -3.1415927F) {
      ☃ += 6.2831855F;
    }
    this.l += ☃ * 0.4F;
    
    this.j = MathHelper.a(this.j, 0.0F, 1.0F);
    
    this.a += 1;
    this.g = this.f;
    
    float ☃ = (this.h - this.f) * 0.4F;
    float ☃ = 0.2F;
    ☃ = MathHelper.a(☃, -☃, ☃);
    this.i += (☃ - this.i) * 0.9F;
    
    this.f += this.i;
  }
  
  public String getName()
  {
    return hasCustomName() ? this.p : "container.enchant";
  }
  
  public boolean hasCustomName()
  {
    return (this.p != null) && (this.p.length() > 0);
  }
  
  public void a(String ☃)
  {
    this.p = ☃;
  }
  
  public IChatBaseComponent getScoreboardDisplayName()
  {
    if (hasCustomName()) {
      return new ChatComponentText(getName());
    }
    return new ChatMessage(getName(), new Object[0]);
  }
  
  public Container createContainer(PlayerInventory ☃, EntityHuman ☃)
  {
    return new ContainerEnchantTable(☃, this.world, this.position);
  }
  
  public String getContainerName()
  {
    return "minecraft:enchanting_table";
  }
}
