package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class DirectionalContainer
  extends MaterialData
  implements Directional
{
  @Deprecated
  public DirectionalContainer(int type)
  {
    super(type);
  }
  
  public DirectionalContainer(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public DirectionalContainer(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public DirectionalContainer(Material type, byte data)
  {
    super(type, data);
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data;
    byte data;
    byte data;
    byte data;
    switch (face)
    {
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
    byte data = getData();
    switch (data)
    {
    case 2: 
      return BlockFace.NORTH;
    case 3: 
      return BlockFace.SOUTH;
    case 4: 
      return BlockFace.WEST;
    }
    return BlockFace.EAST;
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing();
  }
  
  public DirectionalContainer clone()
  {
    return (DirectionalContainer)super.clone();
  }
}
