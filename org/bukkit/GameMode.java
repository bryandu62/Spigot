package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum GameMode
{
  CREATIVE(
  
    1),  SURVIVAL(
  
    0),  ADVENTURE(
  
    2),  SPECTATOR(
  
    3);
  
  private final int value;
  private static final Map<Integer, GameMode> BY_ID;
  
  private GameMode(int value)
  {
    this.value = value;
  }
  
  @Deprecated
  public int getValue()
  {
    return this.value;
  }
  
  @Deprecated
  public static GameMode getByValue(int value)
  {
    return (GameMode)BY_ID.get(Integer.valueOf(value));
  }
  
  static
  {
    BY_ID = Maps.newHashMap();
    GameMode[] arrayOfGameMode;
    int i = (arrayOfGameMode = values()).length;
    for (int j = 0; j < i; j++)
    {
      GameMode mode = arrayOfGameMode[j];
      BY_ID.put(Integer.valueOf(mode.getValue()), mode);
    }
  }
}
