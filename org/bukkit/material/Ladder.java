package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Ladder
  extends SimpleAttachableMaterialData
{
  public Ladder()
  {
    super(Material.LADDER);
  }
  
  @Deprecated
  public Ladder(int type)
  {
    super(type);
  }
  
  public Ladder(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Ladder(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Ladder(Material type, byte data)
  {
    super(type, data);
  }
  
  public BlockFace getAttachedFace()
  {
    byte data = getData();
    switch (data)
    {
    case 2: 
      return BlockFace.SOUTH;
    case 3: 
      return BlockFace.NORTH;
    case 4: 
      return BlockFace.EAST;
    case 5: 
      return BlockFace.WEST;
    }
    return null;
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data = 0;
    switch (face)
    {
    case EAST_NORTH_EAST: 
      data = 2;
      break;
    case DOWN: 
      data = 3;
      break;
    case EAST: 
      data = 4;
      break;
    case EAST_SOUTH_EAST: 
      data = 5;
    }
    setData(data);
  }
  
  public Ladder clone()
  {
    return (Ladder)super.clone();
  }
}
