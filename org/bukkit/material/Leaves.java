package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

public class Leaves
  extends MaterialData
{
  public Leaves()
  {
    super(Material.LEAVES);
  }
  
  public Leaves(TreeSpecies species)
  {
    this();
    setSpecies(species);
  }
  
  @Deprecated
  public Leaves(int type)
  {
    super(type);
  }
  
  public Leaves(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Leaves(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Leaves(Material type, byte data)
  {
    super(type, data);
  }
  
  public TreeSpecies getSpecies()
  {
    return TreeSpecies.getByData((byte)(getData() & 0x3));
  }
  
  public void setSpecies(TreeSpecies species)
  {
    setData(species.getData());
  }
  
  public String toString()
  {
    return getSpecies() + " " + super.toString();
  }
  
  public Leaves clone()
  {
    return (Leaves)super.clone();
  }
}
