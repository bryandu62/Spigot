package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum Instrument
{
  PIANO(
  
    0),  BASS_DRUM(
  
    1),  SNARE_DRUM(
  
    2),  STICKS(
  
    3),  BASS_GUITAR(
  
    4);
  
  private final byte type;
  private static final Map<Byte, Instrument> BY_DATA;
  
  private Instrument(int type)
  {
    this.type = ((byte)type);
  }
  
  @Deprecated
  public byte getType()
  {
    return this.type;
  }
  
  @Deprecated
  public static Instrument getByType(byte type)
  {
    return (Instrument)BY_DATA.get(Byte.valueOf(type));
  }
  
  static
  {
    BY_DATA = Maps.newHashMap();
    Instrument[] arrayOfInstrument;
    int i = (arrayOfInstrument = values()).length;
    for (int j = 0; j < i; j++)
    {
      Instrument instrument = arrayOfInstrument[j];
      BY_DATA.put(Byte.valueOf(instrument.getType()), instrument);
    }
  }
}
