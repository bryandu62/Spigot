package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Torch
  extends SimpleAttachableMaterialData
{
  public Torch()
  {
    super(Material.TORCH);
  }
  
  @Deprecated
  public Torch(int type)
  {
    super(type);
  }
  
  public Torch(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Torch(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Torch(Material type, byte data)
  {
    super(type, data);
  }
  
  public BlockFace getAttachedFace()
  {
    byte data = getData();
    switch (data)
    {
    case 1: 
      return BlockFace.WEST;
    case 2: 
      return BlockFace.EAST;
    case 3: 
      return BlockFace.NORTH;
    case 4: 
      return BlockFace.SOUTH;
    }
    return BlockFace.DOWN;
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data;
    byte data;
    byte data;
    byte data;
    byte data;
    switch (face)
    {
    case EAST: 
      data = 1;
      break;
    case EAST_SOUTH_EAST: 
      data = 2;
      break;
    case EAST_NORTH_EAST: 
      data = 3;
      break;
    case DOWN: 
      data = 4;
      break;
    case NORTH: 
    default: 
      data = 5;
    }
    setData(data);
  }
  
  public Torch clone()
  {
    return (Torch)super.clone();
  }
}
