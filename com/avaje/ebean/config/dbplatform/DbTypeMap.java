package com.avaje.ebean.config.dbplatform;

import java.util.HashMap;
import java.util.Map;

public class DbTypeMap
{
  private final Map<Integer, DbType> typeMap = new HashMap();
  
  public DbTypeMap()
  {
    loadDefaults();
  }
  
  private void loadDefaults()
  {
    put(16, new DbType("boolean"));
    put(-7, new DbType("bit"));
    
    put(4, new DbType("integer"));
    put(-5, new DbType("bigint"));
    put(7, new DbType("float"));
    put(8, new DbType("double"));
    put(5, new DbType("smallint"));
    put(-6, new DbType("tinyint"));
    put(3, new DbType("decimal", 38));
    
    put(12, new DbType("varchar", 255));
    put(1, new DbType("char", 1));
    
    put(2004, new DbType("blob"));
    put(2005, new DbType("clob"));
    put(-4, new DbType("longvarbinary"));
    put(-1, new DbType("lonvarchar"));
    put(-3, new DbType("varbinary", 255));
    put(-2, new DbType("binary", 255));
    
    put(91, new DbType("date"));
    put(92, new DbType("time"));
    put(93, new DbType("timestamp"));
  }
  
  public void put(int jdbcType, DbType dbType)
  {
    this.typeMap.put(Integer.valueOf(jdbcType), dbType);
  }
  
  public DbType get(int jdbcType)
  {
    DbType dbType = (DbType)this.typeMap.get(Integer.valueOf(jdbcType));
    if (dbType == null)
    {
      String m = "No DB type for JDBC type " + jdbcType;
      throw new RuntimeException(m);
    }
    return dbType;
  }
}
