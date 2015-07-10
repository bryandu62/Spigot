package com.avaje.ebean.config.dbplatform;

public class SqlAnywherePlatform
  extends DatabasePlatform
{
  public SqlAnywherePlatform()
  {
    this.name = "sqlanywhere";
    this.dbIdentity.setIdType(IdType.IDENTITY);
    
    this.sqlLimiter = new SqlAnywhereLimiter();
    this.dbIdentity.setSupportsGetGeneratedKeys(false);
    this.dbIdentity.setSelectLastInsertedIdTemplate("select @@IDENTITY as X");
    this.dbIdentity.setSupportsIdentity(true);
    
    this.dbTypeMap.put(16, new DbType("bit default 0"));
    this.dbTypeMap.put(-5, new DbType("numeric", 19));
    this.dbTypeMap.put(7, new DbType("float(16)"));
    this.dbTypeMap.put(8, new DbType("float(32)"));
    this.dbTypeMap.put(-6, new DbType("smallint"));
    this.dbTypeMap.put(3, new DbType("numeric", 28));
    
    this.dbTypeMap.put(2004, new DbType("binary(4500)"));
    this.dbTypeMap.put(2005, new DbType("long varchar"));
    this.dbTypeMap.put(-4, new DbType("long binary"));
    this.dbTypeMap.put(-1, new DbType("long varchar"));
  }
}
