package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class ExtendedRails
  extends Rails
{
  @Deprecated
  public ExtendedRails(int type)
  {
    super(type);
  }
  
  public ExtendedRails(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public ExtendedRails(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public ExtendedRails(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isCurve()
  {
    return false;
  }
  
  @Deprecated
  protected byte getConvertedData()
  {
    return (byte)(getData() & 0x7);
  }
  
  public void setDirection(BlockFace face, boolean isOnSlope)
  {
    boolean extraBitSet = (getData() & 0x8) == 8;
    if ((face != BlockFace.WEST) && (face != BlockFace.EAST) && (face != BlockFace.NORTH) && (face != BlockFace.SOUTH)) {
      throw new IllegalArgumentException("Detector rails and powered rails cannot be set on a curve!");
    }
    super.setDirection(face, isOnSlope);
    setData((byte)(extraBitSet ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  public ExtendedRails clone()
  {
    return (ExtendedRails)super.clone();
  }
}
