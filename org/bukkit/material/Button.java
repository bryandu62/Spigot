package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Button
  extends SimpleAttachableMaterialData
  implements Redstone
{
  public Button()
  {
    super(Material.STONE_BUTTON);
  }
  
  @Deprecated
  public Button(int type)
  {
    super(type);
  }
  
  public Button(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Button(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Button(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isPowered()
  {
    return (getData() & 0x8) == 8;
  }
  
  public void setPowered(boolean bool)
  {
    setData((byte)(bool ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  public BlockFace getAttachedFace()
  {
    byte data = (byte)(getData() & 0x7);
    switch (data)
    {
    case 0: 
      return BlockFace.UP;
    case 1: 
      return BlockFace.WEST;
    case 2: 
      return BlockFace.EAST;
    case 3: 
      return BlockFace.NORTH;
    case 4: 
      return BlockFace.SOUTH;
    case 5: 
      return BlockFace.DOWN;
    }
    return null;
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data = (byte)(getData() & 0x8);
    switch (face)
    {
    case NORTH_EAST: 
      data = (byte)(data | 0x0);
      break;
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
      break;
    case NORTH: 
      data = (byte)(data | 0x5);
    }
    setData(data);
  }
  
  public String toString()
  {
    return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
  }
  
  public Button clone()
  {
    return (Button)super.clone();
  }
}
