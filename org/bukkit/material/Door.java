package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

@Deprecated
public class Door
  extends MaterialData
  implements Directional, Openable
{
  public Door()
  {
    super(Material.WOODEN_DOOR);
  }
  
  @Deprecated
  public Door(int type)
  {
    super(type);
  }
  
  public Door(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Door(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Door(Material type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public boolean isOpen()
  {
    return (getData() & 0x4) == 4;
  }
  
  @Deprecated
  public void setOpen(boolean isOpen)
  {
    setData((byte)(isOpen ? getData() | 0x4 : getData() & 0xFFFFFFFB));
  }
  
  public boolean isTopHalf()
  {
    return (getData() & 0x8) == 8;
  }
  
  @Deprecated
  public void setTopHalf(boolean isTopHalf)
  {
    setData((byte)(isTopHalf ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  @Deprecated
  public BlockFace getHingeCorner()
  {
    byte d = getData();
    if ((d & 0x3) == 3) {
      return BlockFace.NORTH_WEST;
    }
    if ((d & 0x1) == 1) {
      return BlockFace.SOUTH_EAST;
    }
    if ((d & 0x2) == 2) {
      return BlockFace.SOUTH_WEST;
    }
    return BlockFace.NORTH_EAST;
  }
  
  public String toString()
  {
    return (isTopHalf() ? "TOP" : "BOTTOM") + " half of " + super.toString();
  }
  
  @Deprecated
  public void setFacingDirection(BlockFace face)
  {
    byte data = (byte)(getData() & 0x12);
    switch (face)
    {
    case DOWN: 
      data = (byte)(data | 0x1);
      break;
    case EAST: 
      data = (byte)(data | 0x2);
      break;
    case EAST_NORTH_EAST: 
      data = (byte)(data | 0x3);
    }
    setData(data);
  }
  
  @Deprecated
  public BlockFace getFacing()
  {
    byte data = (byte)(getData() & 0x3);
    switch (data)
    {
    case 0: 
      return BlockFace.WEST;
    case 1: 
      return BlockFace.NORTH;
    case 2: 
      return BlockFace.EAST;
    case 3: 
      return BlockFace.SOUTH;
    }
    return null;
  }
  
  public Door clone()
  {
    return (Door)super.clone();
  }
}
