package com.avaje.ebeaninternal.server.deploy;

import java.util.Map;
import java.util.Set;

public final class DeployPropertyParserMap
  extends DeployParser
{
  private final Map<String, String> map;
  
  public DeployPropertyParserMap(Map<String, String> map)
  {
    this.map = map;
  }
  
  public Set<String> getIncludes()
  {
    return null;
  }
  
  public String convertWord()
  {
    String r = getDeployWord(this.word);
    return r == null ? this.word : r;
  }
  
  public String getDeployWord(String expression)
  {
    String deployExpr = (String)this.map.get(expression);
    if (deployExpr == null) {
      return null;
    }
    return deployExpr;
  }
}
