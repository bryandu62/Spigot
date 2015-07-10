package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Sign
  extends MaterialData
  implements Attachable
{
  public Sign()
  {
    super(Material.SIGN_POST);
  }
  
  @Deprecated
  public Sign(int type)
  {
    super(type);
  }
  
  public Sign(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Sign(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Sign(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isWallSign()
  {
    return getItemType() == Material.WALL_SIGN;
  }
  
  public BlockFace getAttachedFace()
  {
    if (isWallSign())
    {
      byte data = getData();
      switch (data)
      {
      case 2: 
        return BlockFace.SOUTH;
      case 3: 
        return BlockFace.NORTH;
      case 4: 
        return BlockFace.EAST;
      case 5: 
        return BlockFace.WEST;
      }
      return null;
    }
    return BlockFace.DOWN;
  }
  
  public BlockFace getFacing()
  {
    byte data = getData();
    if (!isWallSign())
    {
      switch (data)
      {
      case 0: 
        return BlockFace.SOUTH;
      case 1: 
        return BlockFace.SOUTH_SOUTH_WEST;
      case 2: 
        return BlockFace.SOUTH_WEST;
      case 3: 
        return BlockFace.WEST_SOUTH_WEST;
      case 4: 
        return BlockFace.WEST;
      case 5: 
        return BlockFace.WEST_NORTH_WEST;
      case 6: 
        return BlockFace.NORTH_WEST;
      case 7: 
        return BlockFace.NORTH_NORTH_WEST;
      case 8: 
        return BlockFace.NORTH;
      case 9: 
        return BlockFace.NORTH_NORTH_EAST;
      case 10: 
        return BlockFace.NORTH_EAST;
      case 11: 
        return BlockFace.EAST_NORTH_EAST;
      case 12: 
        return BlockFace.EAST;
      case 13: 
        return BlockFace.EAST_SOUTH_EAST;
      case 14: 
        return BlockFace.SOUTH_EAST;
      case 15: 
        return BlockFace.SOUTH_SOUTH_EAST;
      }
      return null;
    }
    return getAttachedFace().getOppositeFace();
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data;
    if (isWallSign())
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
        
        break;
      }
    }
    else
    {
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      byte data;
      switch (face)
      {
      case EAST_NORTH_EAST: 
        data = 0;
        break;
      case WEST: 
        data = 1;
        break;
      case SELF: 
        data = 2;
        break;
      case WEST_NORTH_WEST: 
        data = 3;
        break;
      case EAST_SOUTH_EAST: 
        data = 4;
        break;
      case SOUTH: 
        data = 5;
        break;
      case NORTH_NORTH_WEST: 
        data = 6;
        break;
      case SOUTH_EAST: 
        data = 7;
        break;
      case DOWN: 
        data = 8;
        break;
      case SOUTH_SOUTH_EAST: 
        data = 9;
        break;
      case NORTH_NORTH_EAST: 
        data = 10;
        break;
      case SOUTH_SOUTH_WEST: 
        data = 11;
        break;
      case EAST: 
        data = 12;
        break;
      case SOUTH_WEST: 
        data = 13;
        break;
      case UP: 
        data = 15;
        break;
      case NORTH: 
      case NORTH_EAST: 
      case NORTH_WEST: 
      default: 
        data = 14;
      }
    }
    setData(data);
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing();
  }
  
  public Sign clone()
  {
    return (Sign)super.clone();
  }
}
