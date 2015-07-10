package com.avaje.ebeaninternal.util;

import java.math.BigDecimal;
import java.net.URL;

public class ValueUtil
{
  public static boolean areEqual(Object obj1, Object obj2)
  {
    if (obj1 == null) {
      return obj2 == null;
    }
    if (obj2 == null) {
      return false;
    }
    if (obj1 == obj2) {
      return true;
    }
    if ((obj1 instanceof BigDecimal))
    {
      if ((obj2 instanceof BigDecimal))
      {
        Comparable<Object> com1 = (Comparable)obj1;
        return com1.compareTo(obj2) == 0;
      }
      return false;
    }
    if ((obj1 instanceof URL)) {
      return obj1.toString().equals(obj2.toString());
    }
    if (((obj1 instanceof byte[])) && ((obj2 instanceof byte[]))) {
      return areEqualBytes((byte[])obj1, (byte[])obj2);
    }
    if (((obj1 instanceof char[])) && ((obj2 instanceof char[]))) {
      return areEqualChars((char[])obj1, (char[])obj2);
    }
    return obj1.equals(obj2);
  }
  
  private static boolean areEqualBytes(byte[] b1, byte[] b2)
  {
    if (b1.length != b2.length) {
      return false;
    }
    for (int i = 0; i < b1.length; i++) {
      if (b1[i] != b2[i]) {
        return false;
      }
    }
    return true;
  }
  
  private static boolean areEqualChars(char[] b1, char[] b2)
  {
    if (b1.length != b2.length) {
      return false;
    }
    for (int i = 0; i < b1.length; i++) {
      if (b1[i] != b2[i]) {
        return false;
      }
    }
    return true;
  }
}
