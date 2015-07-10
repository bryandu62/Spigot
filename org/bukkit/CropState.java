package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum CropState
{
  SEEDED(
  
    0),  GERMINATED(
  
    1),  VERY_SMALL(
  
    2),  SMALL(
  
    3),  MEDIUM(
  
    4),  TALL(
  
    5),  VERY_TALL(
  
    6),  RIPE(
  
    7);
  
  private final byte data;
  private static final Map<Byte, CropState> BY_DATA;
  
  private CropState(int data)
  {
    this.data = ((byte)data);
  }
  
  @Deprecated
  public byte getData()
  {
    return this.data;
  }
  
  @Deprecated
  public static CropState getByData(byte data)
  {
    return (CropState)BY_DATA.get(Byte.valueOf(data));
  }
  
  static
  {
    BY_DATA = Maps.newHashMap();
    CropState[] arrayOfCropState;
    int i = (arrayOfCropState = values()).length;
    for (int j = 0; j < i; j++)
    {
      CropState cropState = arrayOfCropState[j];
      BY_DATA.put(Byte.valueOf(cropState.getData()), cropState);
    }
  }
}
