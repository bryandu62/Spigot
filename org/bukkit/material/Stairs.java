package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Stairs
  extends MaterialData
  implements Directional
{
  @Deprecated
  public Stairs(int type)
  {
    super(type);
  }
  
  public Stairs(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Stairs(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Stairs(Material type, byte data)
  {
    super(type, data);
  }
  
  public BlockFace getAscendingDirection()
  {
    byte data = getData();
    switch (data & 0x3)
    {
    case 0: 
    default: 
      return BlockFace.EAST;
    case 1: 
      return BlockFace.WEST;
    case 2: 
      return BlockFace.SOUTH;
    }
    return BlockFace.NORTH;
  }
  
  public BlockFace getDescendingDirection()
  {
    return getAscendingDirection().getOppositeFace();
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
      data = 3;
      break;
    case EAST_NORTH_EAST: 
      data = 2;
      break;
    case EAST: 
    default: 
      data = 0;
      break;
    case EAST_SOUTH_EAST: 
      data = 1;
    }
    setData((byte)(getData() & 0xC | data));
  }
  
  public BlockFace getFacing()
  {
    return getDescendingDirection();
  }
  
  public boolean isInverted()
  {
    return (getData() & 0x4) != 0;
  }
  
  public void setInverted(boolean inv)
  {
    int dat = getData() & 0x3;
    if (inv) {
      dat |= 0x4;
    }
    setData((byte)dat);
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing() + (isInverted() ? " inverted" : "");
  }
  
  public Stairs clone()
  {
    return (Stairs)super.clone();
  }
}
