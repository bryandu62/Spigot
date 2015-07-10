package org.bukkit.material;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;

public class LongGrass
  extends MaterialData
{
  public LongGrass()
  {
    super(Material.LONG_GRASS);
  }
  
  public LongGrass(GrassSpecies species)
  {
    this();
    setSpecies(species);
  }
  
  @Deprecated
  public LongGrass(int type)
  {
    super(type);
  }
  
  public LongGrass(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public LongGrass(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public LongGrass(Material type, byte data)
  {
    super(type, data);
  }
  
  public GrassSpecies getSpecies()
  {
    return GrassSpecies.getByData(getData());
  }
  
  public void setSpecies(GrassSpecies species)
  {
    setData(species.getData());
  }
  
  public String toString()
  {
    return getSpecies() + " " + super.toString();
  }
  
  public LongGrass clone()
  {
    return (LongGrass)super.clone();
  }
}
