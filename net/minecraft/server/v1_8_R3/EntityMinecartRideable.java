package net.minecraft.server.v1_8_R3;

public class EntityMinecartRideable
  extends EntityMinecartAbstract
{
  public EntityMinecartRideable(World ☃)
  {
    super(☃);
  }
  
  public EntityMinecartRideable(World ☃, double ☃, double ☃, double ☃)
  {
    super(☃, ☃, ☃, ☃);
  }
  
  public boolean e(EntityHuman ☃)
  {
    if ((this.passenger != null) && ((this.passenger instanceof EntityHuman)) && (this.passenger != ☃)) {
      return true;
    }
    if ((this.passenger != null) && (this.passenger != ☃)) {
      return false;
    }
    if (!this.world.isClientSide) {
      ☃.mount(this);
    }
    return true;
  }
  
  public void a(int ☃, int ☃, int ☃, boolean ☃)
  {
    if (☃)
    {
      if (this.passenger != null) {
        this.passenger.mount(null);
      }
      if (getType() == 0)
      {
        k(-r());
        j(10);
        setDamage(50.0F);
        ac();
      }
    }
  }
  
  public EntityMinecartAbstract.EnumMinecartType s()
  {
    return EntityMinecartAbstract.EnumMinecartType.RIDEABLE;
  }
}
