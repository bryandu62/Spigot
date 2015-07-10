package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityIronGolem;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

public class CraftIronGolem
  extends CraftGolem
  implements IronGolem
{
  public CraftIronGolem(CraftServer server, EntityIronGolem entity)
  {
    super(server, entity);
  }
  
  public EntityIronGolem getHandle()
  {
    return (EntityIronGolem)this.entity;
  }
  
  public String toString()
  {
    return "CraftIronGolem";
  }
  
  public boolean isPlayerCreated()
  {
    return getHandle().isPlayerCreated();
  }
  
  public void setPlayerCreated(boolean playerCreated)
  {
    getHandle().setPlayerCreated(playerCreated);
  }
  
  public EntityType getType()
  {
    return EntityType.IRON_GOLEM;
  }
}
