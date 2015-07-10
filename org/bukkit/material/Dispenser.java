package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Dispenser
  extends FurnaceAndDispenser
{
  public Dispenser()
  {
    super(Material.DISPENSER);
  }
  
  public Dispenser(BlockFace direction)
  {
    this();
    setFacingDirection(direction);
  }
  
  @Deprecated
  public Dispenser(int type)
  {
    super(type);
  }
  
  public Dispenser(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Dispenser(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Dispenser(Material type, byte data)
  {
    super(type, data);
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data;
    byte data;
    byte data;
    byte data;
    byte data;
    byte data;
    switch (face)
    {
    case NORTH_EAST: 
      data = 0;
      break;
    case NORTH: 
      data = 1;
      break;
    case DOWN: 
      data = 2;
      break;
    case EAST_NORTH_EAST: 
      data = 3;
      break;
    case EAST_SOUTH_EAST: 
      data = 4;
      break;
    case EAST: 
    default: 
      data = 5;
    }
    setData(data);
  }
  
  public BlockFace getFacing()
  {
    int data = getData() & 0x7;
    switch (data)
    {
    case 0: 
      return BlockFace.DOWN;
    case 1: 
      return BlockFace.UP;
    case 2: 
      return BlockFace.NORTH;
    case 3: 
      return BlockFace.SOUTH;
    case 4: 
      return BlockFace.WEST;
    }
    return BlockFace.EAST;
  }
  
  public Dispenser clone()
  {
    return (Dispenser)super.clone();
  }
}
