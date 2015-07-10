package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityZombie;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class CraftZombie
  extends CraftMonster
  implements Zombie
{
  public CraftZombie(CraftServer server, EntityZombie entity)
  {
    super(server, entity);
  }
  
  public EntityZombie getHandle()
  {
    return (EntityZombie)this.entity;
  }
  
  public String toString()
  {
    return "CraftZombie";
  }
  
  public EntityType getType()
  {
    return EntityType.ZOMBIE;
  }
  
  public boolean isBaby()
  {
    return getHandle().isBaby();
  }
  
  public void setBaby(boolean flag)
  {
    getHandle().setBaby(flag);
  }
  
  public boolean isVillager()
  {
    return getHandle().isVillager();
  }
  
  public void setVillager(boolean flag)
  {
    getHandle().setVillager(flag);
  }
}
