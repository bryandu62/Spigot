package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Skull
  extends MaterialData
  implements Directional
{
  public Skull()
  {
    super(Material.SKULL);
  }
  
  public Skull(BlockFace direction)
  {
    this();
    setFacingDirection(direction);
  }
  
  @Deprecated
  public Skull(int type)
  {
    super(type);
  }
  
  public Skull(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Skull(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Skull(Material type, byte data)
  {
    super(type, data);
  }
  
  public void setFacingDirection(BlockFace face)
  {
    int data;
    int data;
    int data;
    int data;
    int data;
    switch (face)
    {
    case WEST_SOUTH_WEST: 
    default: 
      data = 1;
      break;
    case DOWN: 
      data = 2;
      break;
    case EAST: 
      data = 4;
      break;
    case EAST_NORTH_EAST: 
      data = 3;
      break;
    case EAST_SOUTH_EAST: 
      data = 5;
    }
    setData((byte)data);
  }
  
  public BlockFace getFacing()
  {
    int data = getData();
    switch (data)
    {
    case 1: 
    default: 
      return BlockFace.SELF;
    case 2: 
      return BlockFace.NORTH;
    case 3: 
      return BlockFace.SOUTH;
    case 4: 
      return BlockFace.EAST;
    }
    return BlockFace.WEST;
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing();
  }
  
  public Skull clone()
  {
    return (Skull)super.clone();
  }
}
