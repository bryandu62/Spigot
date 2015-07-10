package com.avaje.ebean.config.dbplatform;

public class DbType
{
  private final String name;
  private final int defaultLength;
  private final int defaultScale;
  private final boolean canHaveLength;
  
  public DbType(String name)
  {
    this(name, 0, 0);
  }
  
  public DbType(String name, int defaultLength)
  {
    this(name, defaultLength, 0);
  }
  
  public DbType(String name, int defaultPrecision, int defaultScale)
  {
    this.name = name;
    this.defaultLength = defaultPrecision;
    this.defaultScale = defaultScale;
    this.canHaveLength = true;
  }
  
  public DbType(String name, boolean canHaveLength)
  {
    this.name = name;
    this.defaultLength = 0;
    this.defaultScale = 0;
    this.canHaveLength = canHaveLength;
  }
  
  public String renderType(int deployLength, int deployScale)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(this.name);
    if (this.canHaveLength)
    {
      int len = deployLength != 0 ? deployLength : this.defaultLength;
      if (len > 0)
      {
        sb.append("(");
        sb.append(len);
        int scale = deployScale != 0 ? deployScale : this.defaultScale;
        if (scale > 0)
        {
          sb.append(",");
          sb.append(scale);
        }
        sb.append(")");
      }
    }
    return sb.toString();
  }
}
