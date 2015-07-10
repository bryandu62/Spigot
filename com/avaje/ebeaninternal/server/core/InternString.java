package com.avaje.ebeaninternal.server.core;

import java.util.HashMap;

public final class InternString
{
  private static HashMap<String, String> map = new HashMap();
  
  public static String intern(String s)
  {
    if (s == null) {
      return null;
    }
    synchronized (map)
    {
      String v = (String)map.get(s);
      if (v != null) {
        return v;
      }
      map.put(s, s);
      return s;
    }
  }
}
