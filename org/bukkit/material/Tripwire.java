package org.bukkit.material;

import org.bukkit.Material;

public class Tripwire
  extends MaterialData
{
  public Tripwire()
  {
    super(Material.TRIPWIRE);
  }
  
  @Deprecated
  public Tripwire(int type)
  {
    super(type);
  }
  
  @Deprecated
  public Tripwire(int type, byte data)
  {
    super(type, data);
  }
  
  public boolean isActivated()
  {
    return (getData() & 0x4) != 0;
  }
  
  public void setActivated(boolean act)
  {
    int dat = getData() & 0xB;
    if (act) {
      dat |= 0x4;
    }
    setData((byte)dat);
  }
  
  public boolean isObjectTriggering()
  {
    return (getData() & 0x1) != 0;
  }
  
  public void setObjectTriggering(boolean trig)
  {
    int dat = getData() & 0xE;
    if (trig) {
      dat |= 0x1;
    }
    setData((byte)dat);
  }
  
  public Tripwire clone()
  {
    return (Tripwire)super.clone();
  }
  
  public String toString()
  {
    return super.toString() + (isActivated() ? " Activated" : "") + (isObjectTriggering() ? " Triggered" : "");
  }
}
