package com.mysql.jdbc;

public class Constants
{
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  public static final String MILLIS_I18N = Messages.getString("Milliseconds");
  public static final byte[] SLASH_STAR_SPACE_AS_BYTES = { 47, 42, 32 };
  public static final byte[] SPACE_STAR_SLASH_SPACE_AS_BYTES = { 32, 42, 47, 32 };
  private static final Character[] CHARACTER_CACHE = new Character[''];
  private static final int BYTE_CACHE_OFFSET = 128;
  private static final Byte[] BYTE_CACHE = new Byte['Ā'];
  private static final int INTEGER_CACHE_OFFSET = 128;
  private static final Integer[] INTEGER_CACHE = new Integer['Ā'];
  private static final int SHORT_CACHE_OFFSET = 128;
  private static final Short[] SHORT_CACHE = new Short['Ā'];
  private static final Long[] LONG_CACHE = new Long['Ā'];
  private static final int LONG_CACHE_OFFSET = 128;
  
  static
  {
    for (int i = 0; i < CHARACTER_CACHE.length; i++) {
      CHARACTER_CACHE[i] = new Character((char)i);
    }
    for (int i = 0; i < INTEGER_CACHE.length; i++) {
      INTEGER_CACHE[i] = new Integer(i - 128);
    }
    for (int i = 0; i < SHORT_CACHE.length; i++) {
      SHORT_CACHE[i] = new Short((short)(i - 128));
    }
    for (int i = 0; i < LONG_CACHE.length; i++) {
      LONG_CACHE[i] = new Long(i - 128);
    }
    for (int i = 0; i < BYTE_CACHE.length; i++) {
      BYTE_CACHE[i] = new Byte((byte)(i - 128));
    }
  }
  
  public static Character characterValueOf(char c)
  {
    if (c <= '') {
      return CHARACTER_CACHE[c];
    }
    return new Character(c);
  }
  
  public static final Byte byteValueOf(byte b)
  {
    return BYTE_CACHE[(b + 128)];
  }
  
  public static final Integer integerValueOf(int i)
  {
    if ((i >= -128) && (i <= 127)) {
      return INTEGER_CACHE[(i + 128)];
    }
    return new Integer(i);
  }
  
  public static Short shortValueOf(short s)
  {
    if ((s >= -128) && (s <= 127)) {
      return SHORT_CACHE[(s + 128)];
    }
    return new Short(s);
  }
  
  public static final Long longValueOf(long l)
  {
    if ((l >= -128L) && (l <= 127L)) {
      return LONG_CACHE[((int)l + 128)];
    }
    return new Long(l);
  }
}
