package com.avaje.ebeaninternal.server.lib.util;

import java.util.LinkedHashMap;

public class MapFromString
{
  LinkedHashMap<String, String> map = new LinkedHashMap();
  String mapToString;
  int stringLength;
  int keyStart = 0;
  int eqPos = 0;
  int valEnd = 0;
  
  public static LinkedHashMap<String, String> parse(String mapToString)
  {
    MapFromString c = new MapFromString(mapToString);
    return c.parse();
  }
  
  private MapFromString(String mapToString)
  {
    if (mapToString.charAt(0) == '{') {
      mapToString = mapToString.substring(1);
    }
    if (mapToString.charAt(mapToString.length() - 1) == '}') {
      mapToString = mapToString.substring(0, mapToString.length() - 1);
    }
    this.mapToString = mapToString;
    this.stringLength = mapToString.length();
  }
  
  private LinkedHashMap<String, String> parse()
  {
    while (findNext()) {}
    return this.map;
  }
  
  private boolean findNext()
  {
    if (this.keyStart > this.stringLength) {
      return false;
    }
    this.eqPos = this.mapToString.indexOf("=", this.keyStart);
    if (this.eqPos == -1) {
      throw new RuntimeException("No = after " + this.keyStart);
    }
    this.valEnd = this.mapToString.indexOf(", ", this.eqPos);
    if (this.valEnd == -1) {
      this.valEnd = this.mapToString.length();
    }
    String keyValue = this.mapToString.substring(this.keyStart, this.eqPos);
    String valValue = this.mapToString.substring(this.eqPos + 1, this.valEnd);
    this.map.put(keyValue, valValue);
    this.keyStart = (this.valEnd + 2);
    return true;
  }
}
