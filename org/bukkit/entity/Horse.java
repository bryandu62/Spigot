package org.bukkit.entity;

import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.InventoryHolder;

public abstract interface Horse
  extends Animals, Vehicle, InventoryHolder, Tameable
{
  public abstract Variant getVariant();
  
  public abstract void setVariant(Variant paramVariant);
  
  public abstract Color getColor();
  
  public abstract void setColor(Color paramColor);
  
  public abstract Style getStyle();
  
  public abstract void setStyle(Style paramStyle);
  
  public abstract boolean isCarryingChest();
  
  public abstract void setCarryingChest(boolean paramBoolean);
  
  public abstract int getDomestication();
  
  public abstract void setDomestication(int paramInt);
  
  public abstract int getMaxDomestication();
  
  public abstract void setMaxDomestication(int paramInt);
  
  public abstract double getJumpStrength();
  
  public abstract void setJumpStrength(double paramDouble);
  
  public abstract HorseInventory getInventory();
  
  public static enum Variant
  {
    HORSE,  DONKEY,  MULE,  UNDEAD_HORSE,  SKELETON_HORSE;
  }
  
  public static enum Color
  {
    WHITE,  CREAMY,  CHESTNUT,  BROWN,  BLACK,  GRAY,  DARK_BROWN;
  }
  
  public static enum Style
  {
    NONE,  WHITE,  WHITEFIELD,  WHITE_DOTS,  BLACK_DOTS;
  }
}
