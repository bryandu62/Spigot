package org.bukkit.entity;

public abstract interface Boat
  extends Vehicle
{
  public abstract double getMaxSpeed();
  
  public abstract void setMaxSpeed(double paramDouble);
  
  public abstract double getOccupiedDeceleration();
  
  public abstract void setOccupiedDeceleration(double paramDouble);
  
  public abstract double getUnoccupiedDeceleration();
  
  public abstract void setUnoccupiedDeceleration(double paramDouble);
  
  public abstract boolean getWorkOnLand();
  
  public abstract void setWorkOnLand(boolean paramBoolean);
}
