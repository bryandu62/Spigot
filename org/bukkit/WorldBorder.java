package org.bukkit;

public abstract interface WorldBorder
{
  public abstract void reset();
  
  public abstract double getSize();
  
  public abstract void setSize(double paramDouble);
  
  public abstract void setSize(double paramDouble, long paramLong);
  
  public abstract Location getCenter();
  
  public abstract void setCenter(double paramDouble1, double paramDouble2);
  
  public abstract void setCenter(Location paramLocation);
  
  public abstract double getDamageBuffer();
  
  public abstract void setDamageBuffer(double paramDouble);
  
  public abstract double getDamageAmount();
  
  public abstract void setDamageAmount(double paramDouble);
  
  public abstract int getWarningTime();
  
  public abstract void setWarningTime(int paramInt);
  
  public abstract int getWarningDistance();
  
  public abstract void setWarningDistance(int paramInt);
}
