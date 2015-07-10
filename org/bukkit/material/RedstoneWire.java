package org.bukkit.material;

import org.bukkit.Material;

public class RedstoneWire
  extends MaterialData
  implements Redstone
{
  public RedstoneWire()
  {
    super(Material.REDSTONE_WIRE);
  }
  
  @Deprecated
  public RedstoneWire(int type)
  {
    super(type);
  }
  
  public RedstoneWire(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public RedstoneWire(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public RedstoneWire(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isPowered()
  {
    return getData() > 0;
  }
  
  public String toString()
  {
    return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
  }
  
  public RedstoneWire clone()
  {
    return (RedstoneWire)super.clone();
  }
}
