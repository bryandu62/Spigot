package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public class Dye
  extends MaterialData
  implements Colorable
{
  public Dye()
  {
    super(Material.INK_SACK);
  }
  
  @Deprecated
  public Dye(int type)
  {
    super(type);
  }
  
  public Dye(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Dye(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Dye(Material type, byte data)
  {
    super(type, data);
  }
  
  public DyeColor getColor()
  {
    return DyeColor.getByDyeData(getData());
  }
  
  public void setColor(DyeColor color)
  {
    setData(color.getDyeData());
  }
  
  public String toString()
  {
    return getColor() + " DYE(" + getData() + ")";
  }
  
  public Dye clone()
  {
    return (Dye)super.clone();
  }
}
