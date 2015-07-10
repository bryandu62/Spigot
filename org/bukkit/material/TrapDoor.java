package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class TrapDoor
  extends SimpleAttachableMaterialData
  implements Openable
{
  public TrapDoor()
  {
    super(Material.TRAP_DOOR);
  }
  
  @Deprecated
  public TrapDoor(int type)
  {
    super(type);
  }
  
  public TrapDoor(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public TrapDoor(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public TrapDoor(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isOpen()
  {
    return (getData() & 0x4) == 4;
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
  
  public boolean isInverted()
  {
    return (getData() & 0x8) != 0;
  }
  
  public void setInverted(boolean inv)
  {
    int dat = getData() & 0x7;
    if (inv) {
      dat |= 0x8;
    }
    setData((byte)dat);
  }
  
  public BlockFace getAttachedFace()
  {
    byte data = (byte)(getData() & 0x3);
    switch (data)
    {
    case 0: 
      return BlockFace.SOUTH;
    case 1: 
      return BlockFace.NORTH;
    case 2: 
      return BlockFace.EAST;
    case 3: 
      return BlockFace.WEST;
    }
    return null;
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data = (byte)(getData() & 0xC);
    switch (face)
    {
    case EAST_NORTH_EAST: 
      data = (byte)(data | 0x1);
      break;
    case EAST_SOUTH_EAST: 
      data = (byte)(data | 0x2);
      break;
    case EAST: 
      data = (byte)(data | 0x3);
    }
    setData(data);
  }
  
  public String toString()
  {
    return (isOpen() ? "OPEN " : "CLOSED ") + super.toString() + " with hinges set " + getAttachedFace() + (isInverted() ? " inverted" : "");
  }
  
  public TrapDoor clone()
  {
    return (TrapDoor)super.clone();
  }
}
