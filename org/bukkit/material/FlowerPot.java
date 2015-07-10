package org.bukkit.material;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;

public class FlowerPot
  extends MaterialData
{
  public FlowerPot()
  {
    super(Material.FLOWER_POT);
  }
  
  @Deprecated
  public FlowerPot(int type)
  {
    super(type);
  }
  
  public FlowerPot(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public FlowerPot(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public FlowerPot(Material type, byte data)
  {
    super(type, data);
  }
  
  public MaterialData getContents()
  {
    switch (getData())
    {
    case 1: 
      return new MaterialData(Material.RED_ROSE);
    case 2: 
      return new MaterialData(Material.YELLOW_FLOWER);
    case 3: 
      return new Tree(TreeSpecies.GENERIC);
    case 4: 
      return new Tree(TreeSpecies.REDWOOD);
    case 5: 
      return new Tree(TreeSpecies.BIRCH);
    case 6: 
      return new Tree(TreeSpecies.JUNGLE);
    case 7: 
      return new MaterialData(Material.RED_MUSHROOM);
    case 8: 
      return new MaterialData(Material.BROWN_MUSHROOM);
    case 9: 
      return new MaterialData(Material.CACTUS);
    case 10: 
      return new MaterialData(Material.DEAD_BUSH);
    case 11: 
      return new LongGrass(GrassSpecies.FERN_LIKE);
    }
    return null;
  }
  
  public void setContents(MaterialData materialData)
  {
    Material mat = materialData.getItemType();
    if (mat == Material.RED_ROSE)
    {
      setData((byte)1);
    }
    else if (mat == Material.YELLOW_FLOWER)
    {
      setData((byte)2);
    }
    else if (mat == Material.RED_MUSHROOM)
    {
      setData((byte)7);
    }
    else if (mat == Material.BROWN_MUSHROOM)
    {
      setData((byte)8);
    }
    else if (mat == Material.CACTUS)
    {
      setData((byte)9);
    }
    else if (mat == Material.DEAD_BUSH)
    {
      setData((byte)10);
    }
    else if (mat == Material.SAPLING)
    {
      TreeSpecies species = ((Tree)materialData).getSpecies();
      if (species == TreeSpecies.GENERIC) {
        setData((byte)3);
      } else if (species == TreeSpecies.REDWOOD) {
        setData((byte)4);
      } else if (species == TreeSpecies.BIRCH) {
        setData((byte)5);
      } else {
        setData((byte)6);
      }
    }
    else if (mat == Material.LONG_GRASS)
    {
      GrassSpecies species = ((LongGrass)materialData).getSpecies();
      if (species == GrassSpecies.FERN_LIKE) {
        setData((byte)11);
      }
    }
  }
  
  public String toString()
  {
    return super.toString() + " containing " + getContents();
  }
  
  public FlowerPot clone()
  {
    return (FlowerPot)super.clone();
  }
}
