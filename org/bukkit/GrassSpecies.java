package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum GrassSpecies
{
  DEAD(
  
    0),  NORMAL(
  
    1),  FERN_LIKE(
  
    2);
  
  private final byte data;
  private static final Map<Byte, GrassSpecies> BY_DATA;
  
  private GrassSpecies(int data)
  {
    this.data = ((byte)data);
  }
  
  @Deprecated
  public byte getData()
  {
    return this.data;
  }
  
  @Deprecated
  public static GrassSpecies getByData(byte data)
  {
    return (GrassSpecies)BY_DATA.get(Byte.valueOf(data));
  }
  
  static
  {
    BY_DATA = Maps.newHashMap();
    GrassSpecies[] arrayOfGrassSpecies;
    int i = (arrayOfGrassSpecies = values()).length;
    for (int j = 0; j < i; j++)
    {
      GrassSpecies grassSpecies = arrayOfGrassSpecies[j];
      BY_DATA.put(Byte.valueOf(grassSpecies.getData()), grassSpecies);
    }
  }
}
