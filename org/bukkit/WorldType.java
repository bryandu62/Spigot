package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum WorldType
{
  NORMAL("DEFAULT"),  FLAT("FLAT"),  VERSION_1_1("DEFAULT_1_1"),  LARGE_BIOMES("LARGEBIOMES"),  AMPLIFIED("AMPLIFIED"),  CUSTOMIZED("CUSTOMIZED");
  
  private static final Map<String, WorldType> BY_NAME;
  private final String name;
  
  private WorldType(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public static WorldType getByName(String name)
  {
    return (WorldType)BY_NAME.get(name.toUpperCase());
  }
  
  static
  {
    BY_NAME = Maps.newHashMap();
    WorldType[] arrayOfWorldType;
    int i = (arrayOfWorldType = values()).length;
    for (int j = 0; j < i; j++)
    {
      WorldType type = arrayOfWorldType[j];
      BY_NAME.put(type.name, type);
    }
  }
}
