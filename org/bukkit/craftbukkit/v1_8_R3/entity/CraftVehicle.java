package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Vehicle;

public abstract class CraftVehicle
  extends CraftEntity
  implements Vehicle
{
  public CraftVehicle(CraftServer server, Entity entity)
  {
    super(server, entity);
  }
  
  public String toString()
  {
    return "CraftVehicle{passenger=" + getPassenger() + '}';
  }
}
