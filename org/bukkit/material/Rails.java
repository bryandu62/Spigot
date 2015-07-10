package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Rails
  extends MaterialData
{
  public Rails()
  {
    super(Material.RAILS);
  }
  
  @Deprecated
  public Rails(int type)
  {
    super(type);
  }
  
  public Rails(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Rails(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Rails(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isOnSlope()
  {
    byte d = getConvertedData();
    
    return (d == 2) || (d == 3) || (d == 4) || (d == 5);
  }
  
  public boolean isCurve()
  {
    byte d = getConvertedData();
    
    return (d == 6) || (d == 7) || (d == 8) || (d == 9);
  }
  
  public BlockFace getDirection()
  {
    byte d = getConvertedData();
    switch (d)
    {
    case 0: 
    default: 
      return BlockFace.SOUTH;
    case 1: 
      return BlockFace.EAST;
    case 2: 
      return BlockFace.EAST;
    case 3: 
      return BlockFace.WEST;
    case 4: 
      return BlockFace.NORTH;
    case 5: 
      return BlockFace.SOUTH;
    case 6: 
      return BlockFace.NORTH_WEST;
    case 7: 
      return BlockFace.NORTH_EAST;
    case 8: 
      return BlockFace.SOUTH_EAST;
    }
    return BlockFace.SOUTH_WEST;
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getDirection() + (isOnSlope() ? " on a slope" : isCurve() ? " on a curve" : "");
  }
  
  @Deprecated
  protected byte getConvertedData()
  {
    return getData();
  }
  
  public void setDirection(BlockFace face, boolean isOnSlope)
  {
    switch (face)
    {
    case EAST: 
      setData((byte)(isOnSlope ? 2 : 1));
      break;
    case EAST_SOUTH_EAST: 
      setData((byte)(isOnSlope ? 3 : 1));
      break;
    case DOWN: 
      setData((byte)(isOnSlope ? 4 : 0));
      break;
    case EAST_NORTH_EAST: 
      setData((byte)(isOnSlope ? 5 : 0));
      break;
    case NORTH_NORTH_WEST: 
      setData((byte)6);
      break;
    case NORTH_NORTH_EAST: 
      setData((byte)7);
      break;
    case NORTH_WEST: 
      setData((byte)8);
      break;
    case SELF: 
      setData((byte)9);
    }
  }
  
  public Rails clone()
  {
    return (Rails)super.clone();
  }
}
