package org.bukkit.entity;

public abstract interface LightningStrike
  extends Weather
{
  public abstract boolean isEffect();
  
  public abstract Spigot spigot();
  
  public static class Spigot
    extends Entity.Spigot
  {
    public boolean isSilent()
    {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
