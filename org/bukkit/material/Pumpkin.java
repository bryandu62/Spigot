package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Pumpkin
  extends MaterialData
  implements Directional
{
  public Pumpkin()
  {
    super(Material.PUMPKIN);
  }
  
  public Pumpkin(BlockFace direction)
  {
    this();
    setFacingDirection(direction);
  }
  
  @Deprecated
  public Pumpkin(int type)
  {
    super(type);
  }
  
  public Pumpkin(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Pumpkin(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Pumpkin(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isLit()
  {
    return getItemType() == Material.JACK_O_LANTERN;
  }
  
  public void setFacingDirection(BlockFace face)
  {
    byte data;
    byte data;
    byte data;
    byte data;
    switch (face)
    {
    case DOWN: 
      data = 0;
      break;
    case EAST: 
      data = 1;
      break;
    case EAST_NORTH_EAST: 
      data = 2;
      break;
    case EAST_SOUTH_EAST: 
    default: 
      data = 3;
    }
    setData(data);
  }
  
  public BlockFace getFacing()
  {
    byte data = getData();
    switch (data)
    {
    case 0: 
      return BlockFace.NORTH;
    case 1: 
      return BlockFace.EAST;
    case 2: 
      return BlockFace.SOUTH;
    }
    return BlockFace.EAST;
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing() + " " + (isLit() ? "" : "NOT ") + "LIT";
  }
  
  public Pumpkin clone()
  {
    return (Pumpkin)super.clone();
  }
}
