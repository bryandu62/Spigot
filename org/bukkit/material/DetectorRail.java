package org.bukkit.material;

import org.bukkit.Material;

public class DetectorRail
  extends ExtendedRails
  implements PressureSensor
{
  public DetectorRail()
  {
    super(Material.DETECTOR_RAIL);
  }
  
  @Deprecated
  public DetectorRail(int type)
  {
    super(type);
  }
  
  public DetectorRail(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public DetectorRail(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public DetectorRail(Material type, byte data)
  {
    super(type, data);
  }
  
  public boolean isPressed()
  {
    return (getData() & 0x8) == 8;
  }
  
  public void setPressed(boolean isPressed)
  {
    setData((byte)(isPressed ? getData() | 0x8 : getData() & 0xFFFFFFF7));
  }
  
  public DetectorRail clone()
  {
    return (DetectorRail)super.clone();
  }
}
