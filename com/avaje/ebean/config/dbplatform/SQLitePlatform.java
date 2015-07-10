package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.BackgroundExecutor;
import javax.sql.DataSource;

public class SQLitePlatform
  extends DatabasePlatform
{
  public SQLitePlatform()
  {
    this.name = "sqlite";
    
    this.dbIdentity.setIdType(IdType.IDENTITY);
    this.dbIdentity.setSupportsGetGeneratedKeys(false);
    this.dbIdentity.setSelectLastInsertedIdTemplate("select last_insert_rowid()");
    this.openQuote = "\"";
    this.closeQuote = "\"";
    
    this.booleanDbType = 4;
    
    this.dbTypeMap.put(-7, new DbType("int default 0"));
    this.dbTypeMap.put(16, new DbType("int default 0"));
    
    this.dbDdlSyntax.setInlinePrimaryKeyConstraint(true);
    this.dbDdlSyntax.setIdentity("AUTOINCREMENT");
    this.dbDdlSyntax.setDisableReferentialIntegrity("PRAGMA foreign_keys = OFF");
    this.dbDdlSyntax.setEnableReferentialIntegrity("PRAGMA foreign_keys = ON");
  }
  
  public IdGenerator createSequenceIdGenerator(BackgroundExecutor be, DataSource ds, String seqName, int batchSize)
  {
    return null;
  }
}
