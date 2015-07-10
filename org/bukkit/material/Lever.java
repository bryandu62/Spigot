package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Lever
  extends SimpleAttachableMaterialData
  implements Redstone
{
  public Lever()
  {
    super(Material.LEVER);
  }
  
  @Deprecated
  public Lever(int type)
  {
    super(type);
  }
  
  public Lever(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Lever(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Lever(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isPowered()
  {
    return (getData() & 0x8) == 8;
  }
  
  public void setPowered(boolean isPowered)
  {
    setData((byte)(isPowered ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  public BlockFace getAttachedFace()
  {
    byte data = (byte)(getData() & 0x7);
    switch (data)
    {
    case 1: 
      return BlockFace.WEST;
    case 2: 
      return BlockFace.EAST;
    case 3: 
      return BlockFace.NORTH;
    case 4: 
      return BlockFace.SOUTH;
    case 5: 
    case 6: 
      return BlockFace.DOWN;
    case 0: 
    case 7: 
      return BlockFace.UP;
    }
    return null;
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data = (byte)(getData() & 0x8);
    BlockFace attach = getAttachedFace();
    if (attach == BlockFace.DOWN) {
      switch (face)
      {
      case DOWN: 
      case EAST_NORTH_EAST: 
        data = (byte)(data | 0x5);
        break;
      case EAST: 
      case EAST_SOUTH_EAST: 
        data = (byte)(data | 0x6);
      }
    } else if (attach == BlockFace.UP) {
      switch (face)
      {
      case DOWN: 
      case EAST_NORTH_EAST: 
        data = (byte)(data | 0x7);
        break;
      case EAST: 
      case EAST_SOUTH_EAST: 
        data = (byte)(data | 0x0);
      }
    } else {
      switch (face)
      {
      case EAST: 
        data = (byte)(data | 0x1);
        break;
      case EAST_SOUTH_EAST: 
        data = (byte)(data | 0x2);
        break;
      case EAST_NORTH_EAST: 
        data = (byte)(data | 0x3);
        break;
      case DOWN: 
        data = (byte)(data | 0x4);
      }
    }
    setData(data);
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
  }
  
  public Lever clone()
  {
    return (Lever)super.clone();
  }
}
