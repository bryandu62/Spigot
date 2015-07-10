package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Gate
  extends MaterialData
  implements Directional, Openable
{
  private static final byte OPEN_BIT = 4;
  private static final byte DIR_BIT = 3;
  private static final byte GATE_SOUTH = 0;
  private static final byte GATE_WEST = 1;
  private static final byte GATE_NORTH = 2;
  private static final byte GATE_EAST = 3;
  
  public Gate()
  {
    super(Material.FENCE_GATE);
  }
  
  public Gate(int type, byte data)
  {
    super(type, data);
  }
  
  public Gate(byte data)
  {
    super(Material.FENCE_GATE, data);
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data = (byte)(getData() & 0xFFFFFFFC);
    switch (face)
    {
    case EAST: 
    default: 
      data = (byte)(data | 0x0);
      break;
    case EAST_NORTH_EAST: 
      data = (byte)(data | 0x1);
      break;
    case EAST_SOUTH_EAST: 
      data = (byte)(data | 0x2);
      break;
    case DOWN: 
      data = (byte)(data | 0x3);
    }
    setData(data);
  }
  
  public BlockFace getFacing()
  {
    switch (getData() & 0x3)
    {
    case 0: 
      return BlockFace.EAST;
    case 1: 
      return BlockFace.SOUTH;
    case 2: 
      return BlockFace.WEST;
    case 3: 
      return BlockFace.NORTH;
    }
    return BlockFace.EAST;
  }
  
  public boolean isOpen()
  {
    return (getData() & 0x4) > 0;
  }
  
  public void setOpen(boolean isOpen)
  {
    byte data = getData();
    if (isOpen) {
      data = (byte)(data | 0x4);
    } else {
      data = (byte)(data & 0xFFFFFFFB);
    }
    setData(data);
  }
  
  public String toString()
  {
    return (isOpen() ? "OPEN " : "CLOSED ") + " facing and opening " + getFacing();
  }
  
  public Gate clone()
  {
    return (Gate)super.clone();
  }
}
