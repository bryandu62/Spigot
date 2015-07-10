package com.avaje.ebeaninternal.util;

public class CamelCaseHelper
{
  public static String toCamelFromUnderscore(String underscore)
  {
    StringBuffer result = new StringBuffer();
    String[] vals = underscore.split("_");
    for (int i = 0; i < vals.length; i++)
    {
      String lower = vals[i].toLowerCase();
      if (i > 0)
      {
        char c = Character.toUpperCase(lower.charAt(0));
        result.append(c);
        result.append(lower.substring(1));
      }
      else
      {
        result.append(lower);
      }
    }
    return result.toString();
  }
}
