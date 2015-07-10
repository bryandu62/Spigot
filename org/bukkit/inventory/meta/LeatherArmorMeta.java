package org.bukkit.inventory.meta;

import org.bukkit.Color;

public abstract interface LeatherArmorMeta
  extends ItemMeta
{
  public abstract Color getColor();
  
  public abstract void setColor(Color paramColor);
  
  public abstract LeatherArmorMeta clone();
}
