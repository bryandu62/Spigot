package org.bukkit.entity;

import org.bukkit.material.MaterialData;

public abstract interface Enderman
  extends Monster
{
  public abstract MaterialData getCarriedMaterial();
  
  public abstract void setCarriedMaterial(MaterialData paramMaterialData);
}
