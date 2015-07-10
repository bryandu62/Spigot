package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityPigZombie;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie
  extends CraftZombie
  implements PigZombie
{
  public CraftPigZombie(CraftServer server, EntityPigZombie entity)
  {
    super(server, entity);
  }
  
  public int getAnger()
  {
    return getHandle().angerLevel;
  }
  
  public void setAnger(int level)
  {
    getHandle().angerLevel = level;
  }
  
  public void setAngry(boolean angry)
  {
    setAnger(angry ? 400 : 0);
  }
  
  public boolean isAngry()
  {
    return getAnger() > 0;
  }
  
  public EntityPigZombie getHandle()
  {
    return (EntityPigZombie)this.entity;
  }
  
  public String toString()
  {
    return "CraftPigZombie";
  }
  
  public EntityType getType()
  {
    return EntityType.PIG_ZOMBIE;
  }
}
