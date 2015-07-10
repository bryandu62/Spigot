package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityMinecartFurnace;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PoweredMinecart;

public class CraftMinecartFurnace
  extends CraftMinecart
  implements PoweredMinecart
{
  public CraftMinecartFurnace(CraftServer server, EntityMinecartFurnace entity)
  {
    super(server, entity);
  }
  
  public String toString()
  {
    return "CraftMinecartFurnace";
  }
  
  public EntityType getType()
  {
    return EntityType.MINECART_FURNACE;
  }
}
