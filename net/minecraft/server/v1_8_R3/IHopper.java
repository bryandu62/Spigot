package net.minecraft.server.v1_8_R3;

public abstract interface IHopper
  extends IInventory
{
  public abstract World getWorld();
  
  public abstract double A();
  
  public abstract double B();
  
  public abstract double C();
}
