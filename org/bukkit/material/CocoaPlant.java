package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class CocoaPlant
  extends MaterialData
  implements Directional, Attachable
{
  public static enum CocoaPlantSize
  {
    SMALL,  MEDIUM,  LARGE;
  }
  
  public CocoaPlant()
  {
    super(Material.COCOA);
  }
  
  @Deprecated
  public CocoaPlant(int type)
  {
    super(type);
  }
  
  @Deprecated
  public CocoaPlant(int type, byte data)
  {
    super(type, data);
  }
  
  public CocoaPlant(CocoaPlantSize sz)
  {
    this();
    setSize(sz);
  }
  
  public CocoaPlant(CocoaPlantSize sz, BlockFace dir)
  {
    this();
    setSize(sz);
    setFacingDirection(dir);
  }
  
  public CocoaPlantSize getSize()
  {
    switch (getData() & 0xC)
    {
    case 0: 
      return CocoaPlantSize.SMALL;
    case 4: 
      return CocoaPlantSize.MEDIUM;
    }
    return CocoaPlantSize.LARGE;
  }
  
  public void setSize(CocoaPlantSize sz)
  {
    int dat = getData() & 0x3;
    switch (sz)
    {
    case LARGE: 
      break;
    case MEDIUM: 
      dat |= 0x4;
      break;
    case SMALL: 
      dat |= 0x8;
    }
    setData((byte)dat);
  }
  
  public BlockFace getAttachedFace()
  {
    return getFacing().getOppositeFace();
  }
  
  public void setFacingDirection(BlockFace face)
  {
    int dat = getData() & 0xC;
    switch (face)
    {
    case EAST_NORTH_EAST: 
    default: 
      break;
    case EAST_SOUTH_EAST: 
      dat |= 0x1;
      break;
    case DOWN: 
      dat |= 0x2;
      break;
    case EAST: 
      dat |= 0x3;
    }
    setData((byte)dat);
  }
  
  public BlockFace getFacing()
  {
    switch (getData() & 0x3)
    {
    case 0: 
      return BlockFace.SOUTH;
    case 1: 
      return BlockFace.WEST;
    case 2: 
      return BlockFace.NORTH;
    case 3: 
      return BlockFace.EAST;
    }
    return null;
  }
  
  public CocoaPlant clone()
  {
    return (CocoaPlant)super.clone();
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing() + " " + getSize();
  }
}
