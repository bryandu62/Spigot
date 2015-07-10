package org.bukkit.material;

import org.bukkit.Material;

public class PoweredRail
  extends ExtendedRails
  implements Redstone
{
  public PoweredRail()
  {
    super(Material.POWERED_RAIL);
  }
  
  @Deprecated
  public PoweredRail(int type)
  {
    super(type);
  }
  
  public PoweredRail(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public PoweredRail(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public PoweredRail(Material type, byte data)
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
  
  public PoweredRail clone()
  {
    return (PoweredRail)super.clone();
  }
}
