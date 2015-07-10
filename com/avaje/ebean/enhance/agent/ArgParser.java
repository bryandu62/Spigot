package com.avaje.ebean.enhance.agent;

import java.util.HashMap;

public class ArgParser
{
  public static HashMap<String, String> parse(String args)
  {
    HashMap<String, String> map = new HashMap();
    if (args != null)
    {
      String[] split = args.split(";");
      for (String nameValuePair : split)
      {
        String[] nameValue = nameValuePair.split("=");
        if (nameValue.length == 2) {
          map.put(nameValue[0].toLowerCase(), nameValue[1]);
        }
      }
    }
    return map;
  }
}
