package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityHanging;
import net.minecraft.server.v1_8_R3.EnumDirection;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging
  extends CraftEntity
  implements Hanging
{
  public CraftHanging(CraftServer server, EntityHanging entity)
  {
    super(server, entity);
  }
  
  public BlockFace getAttachedFace()
  {
    return getFacing().getOppositeFace();
  }
  
  public void setFacingDirection(BlockFace face)
  {
    setFacingDirection(face, false);
  }
  
  public boolean setFacingDirection(BlockFace face, boolean force)
  {
    EntityHanging hanging = getHandle();
    EnumDirection dir = hanging.direction;
    switch (face)
    {
    case EAST_NORTH_EAST: 
    default: 
      getHandle().setDirection(EnumDirection.SOUTH);
      break;
    case EAST_SOUTH_EAST: 
      getHandle().setDirection(EnumDirection.WEST);
      break;
    case DOWN: 
      getHandle().setDirection(EnumDirection.NORTH);
      break;
    case EAST: 
      getHandle().setDirection(EnumDirection.EAST);
    }
    if ((!force) && (!hanging.survives()))
    {
      hanging.setDirection(dir);
      return false;
    }
    return true;
  }
  
  public BlockFace getFacing()
  {
    EnumDirection direction = getHandle().direction;
    if (direction == null) {
      return BlockFace.SELF;
    }
    switch (direction)
    {
    case SOUTH: 
    default: 
      return BlockFace.SOUTH;
    case UP: 
      return BlockFace.WEST;
    case NORTH: 
      return BlockFace.NORTH;
    }
    return BlockFace.EAST;
  }
  
  public EntityHanging getHandle()
  {
    return (EntityHanging)this.entity;
  }
  
  public String toString()
  {
    return "CraftHanging";
  }
  
  public EntityType getType()
  {
    return EntityType.UNKNOWN;
  }
}
