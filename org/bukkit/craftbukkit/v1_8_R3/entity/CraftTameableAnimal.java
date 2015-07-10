package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.UUID;
import net.minecraft.server.v1_8_R3.EntityTameableAnimal;
import net.minecraft.server.v1_8_R3.PathfinderGoalSit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

public class CraftTameableAnimal
  extends CraftAnimals
  implements Tameable, Creature
{
  public CraftTameableAnimal(CraftServer server, EntityTameableAnimal entity)
  {
    super(server, entity);
  }
  
  public EntityTameableAnimal getHandle()
  {
    return (EntityTameableAnimal)super.getHandle();
  }
  
  public UUID getOwnerUUID()
  {
    try
    {
      return UUID.fromString(getHandle().getOwnerUUID());
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return null;
  }
  
  public void setOwnerUUID(UUID uuid)
  {
    if (uuid == null) {
      getHandle().setOwnerUUID("");
    } else {
      getHandle().setOwnerUUID(uuid.toString());
    }
  }
  
  public AnimalTamer getOwner()
  {
    if (getOwnerUUID() == null) {
      return null;
    }
    AnimalTamer owner = getServer().getPlayer(getOwnerUUID());
    if (owner == null) {
      owner = getServer().getOfflinePlayer(getOwnerUUID());
    }
    return owner;
  }
  
  public boolean isTamed()
  {
    return getHandle().isTamed();
  }
  
  public void setOwner(AnimalTamer tamer)
  {
    if (tamer != null)
    {
      setTamed(true);
      getHandle().setGoalTarget(null, null, false);
      setOwnerUUID(tamer.getUniqueId());
    }
    else
    {
      setTamed(false);
      setOwnerUUID(null);
    }
  }
  
  public void setTamed(boolean tame)
  {
    getHandle().setTamed(tame);
    if (!tame) {
      setOwnerUUID(null);
    }
  }
  
  public boolean isSitting()
  {
    return getHandle().isSitting();
  }
  
  public void setSitting(boolean sitting)
  {
    getHandle().getGoalSit().setSitting(sitting);
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + "{owner=" + getOwner() + ",tamed=" + isTamed() + "}";
  }
}
