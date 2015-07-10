package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityBat;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

public class CraftBat
  extends CraftAmbient
  implements Bat
{
  public CraftBat(CraftServer server, EntityBat entity)
  {
    super(server, entity);
  }
  
  public EntityBat getHandle()
  {
    return (EntityBat)this.entity;
  }
  
  public String toString()
  {
    return "CraftBat";
  }
  
  public EntityType getType()
  {
    return EntityType.BAT;
  }
  
  public boolean isAwake()
  {
    return !getHandle().isAsleep();
  }
  
  public void setAwake(boolean state)
  {
    getHandle().setAsleep(!state);
  }
}
