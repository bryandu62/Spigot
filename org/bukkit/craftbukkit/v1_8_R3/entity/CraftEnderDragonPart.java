package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityComplexPart;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;

public class CraftEnderDragonPart
  extends CraftComplexPart
  implements EnderDragonPart
{
  public CraftEnderDragonPart(CraftServer server, EntityComplexPart entity)
  {
    super(server, entity);
  }
  
  public EnderDragon getParent()
  {
    return (EnderDragon)super.getParent();
  }
  
  public EntityComplexPart getHandle()
  {
    return (EntityComplexPart)this.entity;
  }
  
  public String toString()
  {
    return "CraftEnderDragonPart";
  }
  
  public void damage(double amount)
  {
    getParent().damage(amount);
  }
  
  public void damage(double amount, Entity source)
  {
    getParent().damage(amount, source);
  }
  
  public double getHealth()
  {
    return getParent().getHealth();
  }
  
  public void setHealth(double health)
  {
    getParent().setHealth(health);
  }
  
  public double getMaxHealth()
  {
    return getParent().getMaxHealth();
  }
  
  public void setMaxHealth(double health)
  {
    getParent().setMaxHealth(health);
  }
  
  public void resetMaxHealth()
  {
    getParent().resetMaxHealth();
  }
}
