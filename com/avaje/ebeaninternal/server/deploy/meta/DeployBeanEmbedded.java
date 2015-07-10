package com.avaje.ebeaninternal.server.deploy.meta;

import java.util.HashMap;
import java.util.Map;

public class DeployBeanEmbedded
{
  Map<String, String> propMap = new HashMap();
  
  public void put(String propertyName, String dbCoumn)
  {
    this.propMap.put(propertyName, dbCoumn);
  }
  
  public void putAll(Map<String, String> propertyColumnMap)
  {
    this.propMap.putAll(propertyColumnMap);
  }
  
  public Map<String, String> getPropertyColumnMap()
  {
    return this.propMap;
  }
}
