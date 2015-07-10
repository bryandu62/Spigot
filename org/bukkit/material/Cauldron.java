package org.bukkit.material;

import org.bukkit.Material;

public class Cauldron
  extends MaterialData
{
  private static final int CAULDRON_FULL = 3;
  private static final int CAULDRON_EMPTY = 0;
  
  public Cauldron()
  {
    super(Material.CAULDRON);
  }
  
  @Deprecated
  public Cauldron(int type, byte data)
  {
    super(type, data);
  }
  
  @Deprecated
  public Cauldron(byte data)
  {
    super(Material.CAULDRON, data);
  }
  
  public boolean isFull()
  {
    return getData() >= 3;
  }
  
  public boolean isEmpty()
  {
    return getData() <= 0;
  }
  
  public String toString()
  {
    return (isFull() ? "FULL" : isEmpty() ? "EMPTY" : new StringBuilder(String.valueOf(getData())).append("/3 FULL").toString()) + " CAULDRON";
  }
  
  public Cauldron clone()
  {
    return (Cauldron)super.clone();
  }
}
