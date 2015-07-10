package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Bed
  extends MaterialData
  implements Directional
{
  public Bed()
  {
    super(Material.BED_BLOCK);
  }
  
  public Bed(BlockFace direction)
  {
    this();
    setFacingDirection(direction);
  }
  
  @Deprecated
  public Bed(int type)
  {
    super(type);
  }
  
  public Bed(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Bed(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Bed(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isHeadOfBed()
  {
    return (getData() & 0x8) == 8;
  }
  
  public void setHeadOfBed(boolean isHeadOfBed)
  {
    setData((byte)(isHeadOfBed ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data;
    byte data;
    byte data;
    byte data;
    switch (face)
    {
    case EAST_NORTH_EAST: 
      data = 0;
      break;
    case EAST_SOUTH_EAST: 
      data = 1;
      break;
    case DOWN: 
      data = 2;
      break;
    case EAST: 
    default: 
      data = 3;
    }
    if (isHeadOfBed()) {
      data = (byte)(data | 0x8);
    }
    setData(data);
  }
  
  public BlockFace getFacing()
  {
    byte data = (byte)(getData() & 0x7);
    switch (data)
    {
    case 0: 
      return BlockFace.SOUTH;
    case 1: 
      return BlockFace.WEST;
    case 2: 
      return BlockFace.NORTH;
    }
    return BlockFace.EAST;
  }
  
  public String toString()
  {
    return (isHeadOfBed() ? "HEAD" : "FOOT") + " of " + super.toString() + " facing " + getFacing();
  }
  
  public Bed clone()
  {
    return (Bed)super.clone();
  }
}
