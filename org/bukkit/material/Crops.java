package org.bukkit.material;

import org.bukkit.CropState;
import org.bukkit.Material;

public class Crops
  extends MaterialData
{
  public Crops()
  {
    super(Material.CROPS);
  }
  
  public Crops(CropState state)
  {
    this();
    setState(state);
  }
  
  @Deprecated
  public Crops(int type)
  {
    super(type);
  }
  
  public Crops(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Crops(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Crops(Material type, byte data)
  {
    super(type, data);
  }
  
  public CropState getState()
  {
    return CropState.getByData(getData());
  }
  
  public void setState(CropState state)
  {
    setData(state.getData());
  }
  
  public String toString()
  {
    return getState() + " " + super.toString();
  }
  
  public Crops clone()
  {
    return (Crops)super.clone();
  }
}
