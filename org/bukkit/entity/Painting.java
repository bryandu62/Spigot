package org.bukkit.entity;

import org.bukkit.Art;

public abstract interface Painting
  extends Hanging
{
  public abstract Art getArt();
  
  public abstract boolean setArt(Art paramArt);
  
  public abstract boolean setArt(Art paramArt, boolean paramBoolean);
}
