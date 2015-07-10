package org.bukkit.material;

import org.bukkit.CoalType;
import org.bukkit.Material;

public class Coal
  extends MaterialData
{
  public Coal()
  {
    super(Material.COAL);
  }
  
  public Coal(CoalType type)
  {
    this();
    setType(type);
  }
  
  @Deprecated
  public Coal(int type)
  {
    super(type);
  }
  
  public Coal(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Coal(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Coal(Material type, byte data)
  {
    super(type, data);
  }
  
  public CoalType getType()
  {
    return CoalType.getByData(getData());
  }
  
  public void setType(CoalType type)
  {
    setData(type.getData());
  }
  
  public String toString()
  {
    return getType() + " " + super.toString();
  }
  
  public Coal clone()
  {
    return (Coal)super.clone();
  }
}
