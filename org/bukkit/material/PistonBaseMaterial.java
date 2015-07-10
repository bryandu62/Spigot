package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class PistonBaseMaterial
  extends MaterialData
  implements Directional, Redstone
{
  @Deprecated
  public PistonBaseMaterial(int type)
  {
    super(type);
  }
  
  public PistonBaseMaterial(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public PistonBaseMaterial(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public PistonBaseMaterial(Material type, byte data)
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
  
  public boolean isPowered()
  {
    return (getData() & 0x8) == 8;
  }
  
  public void setPowered(boolean powered)
  {
    setData((byte)(powered ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  public boolean isSticky()
  {
    return getItemType() == Material.PISTON_STICKY_BASE;
  }
  
  public PistonBaseMaterial clone()
  {
    return (PistonBaseMaterial)super.clone();
  }
}
