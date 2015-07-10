package org.bukkit.entity;

import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public abstract interface Minecart
  extends Vehicle
{
  public abstract void setDamage(double paramDouble);
  
  public abstract double getDamage();
  
  public abstract double getMaxSpeed();
  
  public abstract void setMaxSpeed(double paramDouble);
  
  public abstract boolean isSlowWhenEmpty();
  
  public abstract void setSlowWhenEmpty(boolean paramBoolean);
  
  public abstract Vector getFlyingVelocityMod();
  
  public abstract void setFlyingVelocityMod(Vector paramVector);
  
  public abstract Vector getDerailedVelocityMod();
  
  public abstract void setDerailedVelocityMod(Vector paramVector);
  
  public abstract void setDisplayBlock(MaterialData paramMaterialData);
  
  public abstract MaterialData getDisplayBlock();
  
  public abstract void setDisplayBlockOffset(int paramInt);
  
  public abstract int getDisplayBlockOffset();
}
