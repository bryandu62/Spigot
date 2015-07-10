package gnu.trove.impl;

import java.io.PrintStream;

public class Constants
{
  private static final boolean VERBOSE = System.getProperty("gnu.trove.verbose", null) != null;
  public static final int DEFAULT_CAPACITY = 10;
  public static final float DEFAULT_LOAD_FACTOR = 0.5F;
  public static final byte DEFAULT_BYTE_NO_ENTRY_VALUE;
  public static final short DEFAULT_SHORT_NO_ENTRY_VALUE;
  public static final char DEFAULT_CHAR_NO_ENTRY_VALUE;
  public static final int DEFAULT_INT_NO_ENTRY_VALUE;
  public static final long DEFAULT_LONG_NO_ENTRY_VALUE;
  public static final float DEFAULT_FLOAT_NO_ENTRY_VALUE;
  public static final double DEFAULT_DOUBLE_NO_ENTRY_VALUE;
  
  static
  {
    String property = System.getProperty("gnu.trove.no_entry.byte", "0");
    byte value;
    byte value;
    if ("MAX_VALUE".equalsIgnoreCase(property))
    {
      value = Byte.MAX_VALUE;
    }
    else
    {
      byte value;
      if ("MIN_VALUE".equalsIgnoreCase(property)) {
        value = Byte.MIN_VALUE;
      } else {
        value = Byte.valueOf(property).byteValue();
      }
    }
    if (value > Byte.MAX_VALUE) {
      value = Byte.MAX_VALUE;
    } else if (value < Byte.MIN_VALUE) {
      value = Byte.MIN_VALUE;
    }
    DEFAULT_BYTE_NO_ENTRY_VALUE = value;
    if (VERBOSE) {
      System.out.println("DEFAULT_BYTE_NO_ENTRY_VALUE: " + DEFAULT_BYTE_NO_ENTRY_VALUE);
    }
    String property = System.getProperty("gnu.trove.no_entry.short", "0");
    short value;
    short value;
    if ("MAX_VALUE".equalsIgnoreCase(property))
    {
      value = Short.MAX_VALUE;
    }
    else
    {
      short value;
      if ("MIN_VALUE".equalsIgnoreCase(property)) {
        value = Short.MIN_VALUE;
      } else {
        value = Short.valueOf(property).shortValue();
      }
    }
    if (value > Short.MAX_VALUE) {
      value = Short.MAX_VALUE;
    } else if (value < Short.MIN_VALUE) {
      value = Short.MIN_VALUE;
    }
    DEFAULT_SHORT_NO_ENTRY_VALUE = value;
    if (VERBOSE) {
      System.out.println("DEFAULT_SHORT_NO_ENTRY_VALUE: " + DEFAULT_SHORT_NO_ENTRY_VALUE);
    }
    String property = System.getProperty("gnu.trove.no_entry.char", "\000");
    char value;
    char value;
    if ("MAX_VALUE".equalsIgnoreCase(property))
    {
      value = 65535;
    }
    else
    {
      char value;
      if ("MIN_VALUE".equalsIgnoreCase(property)) {
        value = '\000';
      } else {
        value = property.toCharArray()[0];
      }
    }
    if (value > 65535) {
      value = 65535;
    } else if (value < 0) {
      value = '\000';
    }
    DEFAULT_CHAR_NO_ENTRY_VALUE = value;
    if (VERBOSE) {
      System.out.println("DEFAULT_CHAR_NO_ENTRY_VALUE: " + Integer.valueOf(value));
    }
    String property = System.getProperty("gnu.trove.no_entry.int", "0");
    int value;
    int value;
    if ("MAX_VALUE".equalsIgnoreCase(property))
    {
      value = Integer.MAX_VALUE;
    }
    else
    {
      int value;
      if ("MIN_VALUE".equalsIgnoreCase(property)) {
        value = Integer.MIN_VALUE;
      } else {
        value = Integer.valueOf(property).intValue();
      }
    }
    DEFAULT_INT_NO_ENTRY_VALUE = value;
    if (VERBOSE) {
      System.out.println("DEFAULT_INT_NO_ENTRY_VALUE: " + DEFAULT_INT_NO_ENTRY_VALUE);
    }
    String property = System.getProperty("gnu.trove.no_entry.long", "0");
    long value;
    long value;
    if ("MAX_VALUE".equalsIgnoreCase(property))
    {
      value = Long.MAX_VALUE;
    }
    else
    {
      long value;
      if ("MIN_VALUE".equalsIgnoreCase(property)) {
        value = Long.MIN_VALUE;
      } else {
        value = Long.valueOf(property).longValue();
      }
    }
    DEFAULT_LONG_NO_ENTRY_VALUE = value;
    if (VERBOSE) {
      System.out.println("DEFAULT_LONG_NO_ENTRY_VALUE: " + DEFAULT_LONG_NO_ENTRY_VALUE);
    }
    String property = System.getProperty("gnu.trove.no_entry.float", "0");
    float value;
    float value;
    if ("MAX_VALUE".equalsIgnoreCase(property))
    {
      value = Float.MAX_VALUE;
    }
    else
    {
      float value;
      if ("MIN_VALUE".equalsIgnoreCase(property))
      {
        value = Float.MIN_VALUE;
      }
      else
      {
        float value;
        if ("MIN_NORMAL".equalsIgnoreCase(property))
        {
          value = 1.17549435E-38F;
        }
        else
        {
          float value;
          if ("NEGATIVE_INFINITY".equalsIgnoreCase(property))
          {
            value = Float.NEGATIVE_INFINITY;
          }
          else
          {
            float value;
            if ("POSITIVE_INFINITY".equalsIgnoreCase(property)) {
              value = Float.POSITIVE_INFINITY;
            } else {
              value = Float.valueOf(property).floatValue();
            }
          }
        }
      }
    }
    DEFAULT_FLOAT_NO_ENTRY_VALUE = value;
    if (VERBOSE) {
      System.out.println("DEFAULT_FLOAT_NO_ENTRY_VALUE: " + DEFAULT_FLOAT_NO_ENTRY_VALUE);
    }
    String property = System.getProperty("gnu.trove.no_entry.double", "0");
    double value;
    double value;
    if ("MAX_VALUE".equalsIgnoreCase(property))
    {
      value = Double.MAX_VALUE;
    }
    else
    {
      double value;
      if ("MIN_VALUE".equalsIgnoreCase(property))
      {
        value = Double.MIN_VALUE;
      }
      else
      {
        double value;
        if ("MIN_NORMAL".equalsIgnoreCase(property))
        {
          value = 2.2250738585072014E-308D;
        }
        else
        {
          double value;
          if ("NEGATIVE_INFINITY".equalsIgnoreCase(property))
          {
            value = Double.NEGATIVE_INFINITY;
          }
          else
          {
            double value;
            if ("POSITIVE_INFINITY".equalsIgnoreCase(property)) {
              value = Double.POSITIVE_INFINITY;
            } else {
              value = Double.valueOf(property).doubleValue();
            }
          }
        }
      }
    }
    DEFAULT_DOUBLE_NO_ENTRY_VALUE = value;
    if (VERBOSE) {
      System.out.println("DEFAULT_DOUBLE_NO_ENTRY_VALUE: " + DEFAULT_DOUBLE_NO_ENTRY_VALUE);
    }
  }
}
