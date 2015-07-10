package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityPig;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

public class CraftPig
  extends CraftAnimals
  implements Pig
{
  public CraftPig(CraftServer server, EntityPig entity)
  {
    super(server, entity);
  }
  
  public boolean hasSaddle()
  {
    return getHandle().hasSaddle();
  }
  
  public void setSaddle(boolean saddled)
  {
    getHandle().setSaddle(saddled);
  }
  
  public EntityPig getHandle()
  {
    return (EntityPig)this.entity;
  }
  
  public String toString()
  {
    return "CraftPig";
  }
  
  public EntityType getType()
  {
    return EntityType.PIG;
  }
}
