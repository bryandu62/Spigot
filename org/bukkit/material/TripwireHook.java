package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class TripwireHook
  extends SimpleAttachableMaterialData
  implements Redstone
{
  public TripwireHook()
  {
    super(Material.TRIPWIRE_HOOK);
  }
  
  @Deprecated
  public TripwireHook(int type)
  {
    super(type);
  }
  
  @Deprecated
  public TripwireHook(int type, byte data)
  {
    super(type, data);
  }
  
  public TripwireHook(BlockFace dir)
  {
    this();
    setFacingDirection(dir);
  }
  
  public boolean isConnected()
  {
    return (getData() & 0x4) != 0;
  }
  
  public void setConnected(boolean connected)
  {
    int dat = getData() & 0xB;
    if (connected) {
      dat |= 0x4;
    }
    setData((byte)dat);
  }
  
  public boolean isActivated()
  {
    return (getData() & 0x8) != 0;
  }
  
  public void setActivated(boolean act)
  {
    int dat = getData() & 0x7;
    if (act) {
      dat |= 0x8;
    }
    setData((byte)dat);
  }
  
  public void setFacingDirection(BlockFace face)
  {
    int dat = getData() & 0xC;
    switch (face)
    {
    case EAST_SOUTH_EAST: 
      dat |= 0x1;
      break;
    case DOWN: 
      dat |= 0x2;
      break;
    case EAST: 
      dat |= 0x3;
      break;
    }
    setData((byte)dat);
  }
  
  public BlockFace getAttachedFace()
  {
    switch (getData() & 0x3)
    {
    case 0: 
      return BlockFace.NORTH;
    case 1: 
      return BlockFace.EAST;
    case 2: 
      return BlockFace.SOUTH;
    case 3: 
      return BlockFace.WEST;
    }
    return null;
  }
  
  public boolean isPowered()
  {
    return isActivated();
  }
  
  public TripwireHook clone()
  {
    return (TripwireHook)super.clone();
  }
  
  public String toString()
  {
    return super.toString() + " facing " + getFacing() + (isActivated() ? " Activated" : "") + (isConnected() ? " Connected" : "");
  }
}
