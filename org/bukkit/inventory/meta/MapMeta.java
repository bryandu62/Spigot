package org.bukkit.inventory.meta;

public abstract interface MapMeta
  extends ItemMeta
{
  public abstract boolean isScaling();
  
  public abstract void setScaling(boolean paramBoolean);
  
  public abstract MapMeta clone();
}
