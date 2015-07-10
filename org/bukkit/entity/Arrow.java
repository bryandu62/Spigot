package org.bukkit.entity;

public abstract interface Arrow
  extends Projectile
{
  public abstract int getKnockbackStrength();
  
  public abstract void setKnockbackStrength(int paramInt);
  
  public abstract boolean isCritical();
  
  public abstract void setCritical(boolean paramBoolean);
  
  public abstract Spigot spigot();
  
  public static class Spigot
    extends Entity.Spigot
  {
    public double getDamage()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setDamage(double damage)
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
