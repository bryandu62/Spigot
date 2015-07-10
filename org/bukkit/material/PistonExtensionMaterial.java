package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class PistonExtensionMaterial
  extends MaterialData
  implements Attachable
{
  @Deprecated
  public PistonExtensionMaterial(int type)
  {
    super(type);
  }
  
  public PistonExtensionMaterial(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public PistonExtensionMaterial(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public PistonExtensionMaterial(Material type, byte data)
  {
    super(type, data);
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data = (byte)(getData() & 0x8);
    switch (face)
    {
    case NORTH: 
      data = (byte)(data | 0x1);
      break;
    case DOWN: 
      data = (byte)(data | 0x2);
      break;
    case EAST_NORTH_EAST: 
      data = (byte)(data | 0x3);
      break;
    case EAST_SOUTH_EAST: 
      data = (byte)(data | 0x4);
      break;
    case EAST: 
      data = (byte)(data | 0x5);
    }
    setData(data);
  }
  
  public BlockFace getFacing()
  {
    byte dir = (byte)(getData() & 0x7);
    switch (dir)
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
    case 5: 
      return BlockFace.EAST;
    }
    return BlockFace.SELF;
  }
  
  public boolean isSticky()
  {
    return (getData() & 0x8) == 8;
  }
  
  public void setSticky(boolean sticky)
  {
    setData((byte)(sticky ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  public BlockFace getAttachedFace()
  {
    return getFacing().getOppositeFace();
  }
  
  public PistonExtensionMaterial clone()
  {
    return (PistonExtensionMaterial)super.clone();
  }
}
