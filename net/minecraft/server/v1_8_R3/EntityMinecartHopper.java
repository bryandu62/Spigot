package net.minecraft.server.v1_8_R3;

import java.util.List;

public class EntityMinecartHopper
  extends EntityMinecartContainer
  implements IHopper
{
  private boolean a = true;
  private int b = -1;
  private BlockPosition c = BlockPosition.ZERO;
  
  public EntityMinecartHopper(World ☃)
  {
    super(☃);
  }
  
  public EntityMinecartHopper(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public EntityMinecartAbstract.EnumMinecartType s()
  {
    return EntityMinecartAbstract.EnumMinecartType.HOPPER;
  }
  
  public IBlockData u()
  {
    return Blocks.HOPPER.getBlockData();
  }
  
  public int w()
  {
    return 1;
  }
  
  public int getSize()
  {
    return 5;
  }
  
  public boolean e(EntityHuman ☃)
  {
    if (!this.world.isClientSide) {
      ☃.openContainer(this);
    }
    return true;
  }
  
  public void a(int ☃, int ☃, int ☃, boolean ☃)
  {
    boolean ☃ = !☃;
    if (☃ != y()) {
      i(☃);
    }
  }
  
  public boolean y()
  {
    return this.a;
  }
  
  public void i(boolean ☃)
  {
    this.a = ☃;
  }
  
  public World getWorld()
  {
    return this.world;
  }
  
  public double A()
  {
    return this.locX;
  }
  
  public double B()
  {
    return this.locY + 0.5D;
  }
  
  public double C()
  {
    return this.locZ;
  }
  
  public void t_()
  {
    super.t_();
    if ((!this.world.isClientSide) && (isAlive()) && (y()))
    {
      BlockPosition ☃ = new BlockPosition(this);
      if (☃.equals(this.c)) {
        this.b -= 1;
      } else {
        m(0);
      }
      if (!E())
      {
        m(0);
        if (D())
        {
          m(4);
          update();
        }
      }
    }
  }
  
  public boolean D()
  {
    if (TileEntityHopper.a(this)) {
      return true;
    }
    List<EntityItem> ☃ = this.world.a(EntityItem.class, getBoundingBox().grow(0.25D, 0.0D, 0.25D), IEntitySelector.a);
    if (☃.size() > 0) {
      TileEntityHopper.a(this, (EntityItem)☃.get(0));
    }
    return false;
  }
  
  public void a(DamageSource ☃)
  {
    super.a(☃);
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      a(Item.getItemOf(Blocks.HOPPER), 1, 0.0F);
    }
  }
  
  protected void b(NBTTagCompound ☃)
  {
    super.b(☃);
    ☃.setInt("TransferCooldown", this.b);
  }
  
  protected void a(NBTTagCompound ☃)
  {
    super.a(☃);
    this.b = ☃.getInt("TransferCooldown");
  }
  
  public void m(int ☃)
  {
    this.b = ☃;
  }
  
  public boolean E()
  {
    return this.b > 0;
  }
  
  public String getContainerName()
  {
    return "minecraft:hopper";
  }
  
  public Container createContainer(PlayerInventory ☃, EntityHuman ☃)
  {
    return new ContainerHopper(☃, this, ☃);
  }
}
