package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityCreeper;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.CreeperPowerEvent.PowerCause;
import org.bukkit.plugin.PluginManager;

public class CraftCreeper
  extends CraftMonster
  implements Creeper
{
  public CraftCreeper(CraftServer server, EntityCreeper entity)
  {
    super(server, entity);
  }
  
  public boolean isPowered()
  {
    return getHandle().isPowered();
  }
  
  public void setPowered(boolean powered)
  {
    CraftServer server = this.server;
    Creeper entity = (Creeper)getHandle().getBukkitEntity();
    if (powered)
    {
      CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
      server.getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
        getHandle().setPowered(true);
      }
    }
    else
    {
      CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
      server.getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
        getHandle().setPowered(false);
      }
    }
  }
  
  public EntityCreeper getHandle()
  {
    return (EntityCreeper)this.entity;
  }
  
  public String toString()
  {
    return "CraftCreeper";
  }
  
  public EntityType getType()
  {
    return EntityType.CREEPER;
  }
}
