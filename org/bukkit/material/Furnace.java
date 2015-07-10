package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Furnace
  extends FurnaceAndDispenser
{
  public Furnace()
  {
    super(Material.FURNACE);
  }
  
  public Furnace(BlockFace direction)
  {
    this();
    setFacingDirection(direction);
  }
  
  @Deprecated
  public Furnace(int type)
  {
    super(type);
  }
  
  public Furnace(Material type)
  {
    super(type);
  }
  
  @Deprecated
  public Furnace(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Furnace(Material type, byte data)
  {
    super(type, data);
  }
  
  public Furnace clone()
  {
    return (Furnace)super.clone();
  }
}
