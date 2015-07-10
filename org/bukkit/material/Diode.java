package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Diode
  extends MaterialData
  implements Directional
{
  public Diode()
  {
    super(Material.DIODE_BLOCK_ON);
  }
  
  @Deprecated
  public Diode(int type)
  {
    super(type);
  }
  
  public Diode(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Diode(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Diode(Material type, byte data)
  {
    super(type, data);
  }
  
  public void setDelay(int delay)
  {
    if (delay > 4) {
      delay = 4;
    }
    if (delay < 1) {
      delay = 1;
    }
    byte newData = (byte)(getData() & 0x3);
    
    setData((byte)(newData | delay - 1 << 2));
  }
  
  public int getDelay()
  {
    return (getData() >> 2) + 1;
  }
  
  public void setFacingDirection(BlockFace face)
  {
    int delay = getDelay();
    byte data;
    byte data;
    byte data;
    byte data;
    switch (face)
    {
    case EAST: 
      data = 1;
      break;
    case EAST_NORTH_EAST: 
      data = 2;
      break;
    case EAST_SOUTH_EAST: 
      data = 3;
      break;
    case DOWN: 
    default: 
      data = 0;
    }
    setData(data);
    setDelay(delay);
  }
  
  public BlockFace getFacing()
  {
    byte data = (byte)(getData() & 0x3);
    switch (data)
    {
    case 0: 
    default: 
      return BlockFace.NORTH;
    case 1: 
      return BlockFace.EAST;
    case 2: 
      return BlockFace.SOUTH;
    }
    return BlockFace.WEST;
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing() + " with " + getDelay() + " ticks delay";
  }
  
  public Diode clone()
  {
    return (Diode)super.clone();
  }
}
