package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntitySkeleton;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

public class CraftSkeleton
  extends CraftMonster
  implements Skeleton
{
  public CraftSkeleton(CraftServer server, EntitySkeleton entity)
  {
    super(server, entity);
  }
  
  public EntitySkeleton getHandle()
  {
    return (EntitySkeleton)this.entity;
  }
  
  public String toString()
  {
    return "CraftSkeleton";
  }
  
  public EntityType getType()
  {
    return EntityType.SKELETON;
  }
  
  public Skeleton.SkeletonType getSkeletonType()
  {
    return Skeleton.SkeletonType.getType(getHandle().getSkeletonType());
  }
  
  public void setSkeletonType(Skeleton.SkeletonType type)
  {
    Validate.notNull(type);
    getHandle().setSkeletonType(type.getId());
  }
}
