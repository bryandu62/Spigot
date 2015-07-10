package org.bukkit.inventory.meta;

public abstract interface SkullMeta
  extends ItemMeta
{
  public abstract String getOwner();
  
  public abstract boolean hasOwner();
  
  public abstract boolean setOwner(String paramString);
  
  public abstract SkullMeta clone();
}
