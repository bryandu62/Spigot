package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;

public class Tree
  extends MaterialData
{
  public Tree()
  {
    super(Material.LOG);
  }
  
  public Tree(TreeSpecies species)
  {
    this();
    setSpecies(species);
  }
  
  public Tree(TreeSpecies species, BlockFace dir)
  {
    this();
    setSpecies(species);
    setDirection(dir);
  }
  
  @Deprecated
  public Tree(int type)
  {
    super(type);
  }
  
  public Tree(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Tree(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Tree(Material type, byte data)
  {
    super(type, data);
  }
  
  public TreeSpecies getSpecies()
  {
    return TreeSpecies.getByData((byte)(getData() & 0x3));
  }
  
  public void setSpecies(TreeSpecies species)
  {
    setData((byte)(getData() & 0xC | species.getData()));
  }
  
  public BlockFace getDirection()
  {
    switch (getData() >> 2 & 0x3)
    {
    case 0: 
    default: 
      return BlockFace.UP;
    case 1: 
      return BlockFace.WEST;
    case 2: 
      return BlockFace.NORTH;
    }
    return BlockFace.SELF;
  }
  
  public void setDirection(BlockFace dir)
  {
    int dat;
    int dat;
    int dat;
    int dat;
    switch (dir)
    {
    case NORTH: 
    case NORTH_EAST: 
    default: 
      dat = 0;
      break;
    case EAST: 
    case EAST_SOUTH_EAST: 
      dat = 1;
      break;
    case DOWN: 
    case EAST_NORTH_EAST: 
      dat = 2;
      break;
    case WEST_SOUTH_WEST: 
      dat = 3;
    }
    setData((byte)(getData() & 0x3 | dat << 2));
  }
  
  public String toString()
  {
    return getSpecies() + " " + getDirection() + " " + super.toString();
  }
  
  public Tree clone()
  {
    return (Tree)super.clone();
  }
}
