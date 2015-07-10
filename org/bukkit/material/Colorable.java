package org.bukkit.material;

import org.bukkit.DyeColor;

public abstract interface Colorable
{
  public abstract DyeColor getColor();
  
  public abstract void setColor(DyeColor paramDyeColor);
}
