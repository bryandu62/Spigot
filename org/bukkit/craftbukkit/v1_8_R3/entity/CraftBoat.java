package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityBoat;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;

public class CraftBoat
  extends CraftVehicle
  implements Boat
{
  public CraftBoat(CraftServer server, EntityBoat entity)
  {
    super(server, entity);
  }
  
  public double getMaxSpeed()
  {
    return getHandle().maxSpeed;
  }
  
  public void setMaxSpeed(double speed)
  {
    if (speed >= 0.0D) {
      getHandle().maxSpeed = speed;
    }
  }
  
  public double getOccupiedDeceleration()
  {
    return getHandle().occupiedDeceleration;
  }
  
  public void setOccupiedDeceleration(double speed)
  {
    if (speed >= 0.0D) {
      getHandle().occupiedDeceleration = speed;
    }
  }
  
  public double getUnoccupiedDeceleration()
  {
    return getHandle().unoccupiedDeceleration;
  }
  
  public void setUnoccupiedDeceleration(double speed)
  {
    getHandle().unoccupiedDeceleration = speed;
  }
  
  public boolean getWorkOnLand()
  {
    return getHandle().landBoats;
  }
  
  public void setWorkOnLand(boolean workOnLand)
  {
    getHandle().landBoats = workOnLand;
  }
  
  public EntityBoat getHandle()
  {
    return (EntityBoat)this.entity;
  }
  
  public String toString()
  {
    return "CraftBoat";
  }
  
  public EntityType getType()
  {
    return EntityType.BOAT;
  }
}
