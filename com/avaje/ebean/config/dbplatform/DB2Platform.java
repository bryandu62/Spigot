package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.BackgroundExecutor;
import javax.sql.DataSource;

public class DB2Platform
  extends DatabasePlatform
{
  public DB2Platform()
  {
    this.name = "db2";
    
    this.dbIdentity.setSupportsGetGeneratedKeys(true);
    this.dbIdentity.setIdType(IdType.IDENTITY);
    this.dbIdentity.setSupportsSequence(true);
    
    this.openQuote = "\"";
    this.closeQuote = "\"";
    
    this.booleanDbType = 4;
    this.dbTypeMap.put(16, new DbType("smallint default 0"));
    this.dbTypeMap.put(4, new DbType("integer"));
    this.dbTypeMap.put(-5, new DbType("bigint"));
    this.dbTypeMap.put(7, new DbType("float"));
    this.dbTypeMap.put(8, new DbType("float"));
    this.dbTypeMap.put(5, new DbType("smallint"));
    this.dbTypeMap.put(-6, new DbType("smallint"));
    this.dbTypeMap.put(3, new DbType("decimal", 15));
    
    this.dbDdlSyntax.setIdentity("generated by default as identity");
  }
  
  public IdGenerator createSequenceIdGenerator(BackgroundExecutor be, DataSource ds, String seqName, int batchSize)
  {
    return new DB2SequenceIdGenerator(be, ds, seqName, batchSize);
  }
}
