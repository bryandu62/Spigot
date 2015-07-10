package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum TreeSpecies
{
  GENERIC(
  
    0),  REDWOOD(
  
    1),  BIRCH(
  
    2),  JUNGLE(
  
    3),  ACACIA(
  
    4),  DARK_OAK(
  
    5);
  
  private final byte data;
  private static final Map<Byte, TreeSpecies> BY_DATA;
  
  private TreeSpecies(int data)
  {
    this.data = ((byte)data);
  }
  
  @Deprecated
  public byte getData()
  {
    return this.data;
  }
  
  @Deprecated
  public static TreeSpecies getByData(byte data)
  {
    return (TreeSpecies)BY_DATA.get(Byte.valueOf(data));
  }
  
  static
  {
    BY_DATA = Maps.newHashMap();
    TreeSpecies[] arrayOfTreeSpecies;
    int i = (arrayOfTreeSpecies = values()).length;
    for (int j = 0; j < i; j++)
    {
      TreeSpecies species = arrayOfTreeSpecies[j];
      BY_DATA.put(Byte.valueOf(species.data), species);
    }
  }
}
