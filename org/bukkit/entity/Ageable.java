package org.bukkit.entity;

public abstract interface Ageable
  extends Creature
{
  public abstract int getAge();
  
  public abstract void setAge(int paramInt);
  
  public abstract void setAgeLock(boolean paramBoolean);
  
  public abstract boolean getAgeLock();
  
  public abstract void setBaby();
  
  public abstract void setAdult();
  
  public abstract boolean isAdult();
  
  public abstract boolean canBreed();
  
  public abstract void setBreed(boolean paramBoolean);
}
