package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebeaninternal.api.SpiQuery;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DatabasePlatform
{
  private static final Logger logger = Logger.getLogger(DatabasePlatform.class.getName());
  protected String openQuote = "\"";
  protected String closeQuote = "\"";
  protected SqlLimiter sqlLimiter = new LimitOffsetSqlLimiter();
  protected DbTypeMap dbTypeMap = new DbTypeMap();
  protected DbDdlSyntax dbDdlSyntax = new DbDdlSyntax();
  protected DbIdentity dbIdentity = new DbIdentity();
  protected int booleanDbType = 16;
  protected int blobDbType = 2004;
  protected int clobDbType = 2005;
  protected boolean treatEmptyStringsAsNull;
  protected String name = "generic";
  private static final char BACK_TICK = '`';
  protected DbEncrypt dbEncrypt;
  protected boolean idInExpandedForm;
  protected boolean selectCountWithAlias;
  
  public String getName()
  {
    return this.name;
  }
  
  public IdGenerator createSequenceIdGenerator(BackgroundExecutor be, DataSource ds, String seqName, int batchSize)
  {
    return null;
  }
  
  public DbEncrypt getDbEncrypt()
  {
    return this.dbEncrypt;
  }
  
  public void setDbEncrypt(DbEncrypt dbEncrypt)
  {
    this.dbEncrypt = dbEncrypt;
  }
  
  public DbTypeMap getDbTypeMap()
  {
    return this.dbTypeMap;
  }
  
  public DbDdlSyntax getDbDdlSyntax()
  {
    return this.dbDdlSyntax;
  }
  
  public String getCloseQuote()
  {
    return this.closeQuote;
  }
  
  public String getOpenQuote()
  {
    return this.openQuote;
  }
  
  public int getBooleanDbType()
  {
    return this.booleanDbType;
  }
  
  public int getBlobDbType()
  {
    return this.blobDbType;
  }
  
  public int getClobDbType()
  {
    return this.clobDbType;
  }
  
  public boolean isTreatEmptyStringsAsNull()
  {
    return this.treatEmptyStringsAsNull;
  }
  
  public boolean isIdInExpandedForm()
  {
    return this.idInExpandedForm;
  }
  
  public DbIdentity getDbIdentity()
  {
    return this.dbIdentity;
  }
  
  public SqlLimiter getSqlLimiter()
  {
    return this.sqlLimiter;
  }
  
  public String convertQuotedIdentifiers(String dbName)
  {
    if ((dbName != null) && (dbName.length() > 0) && 
      (dbName.charAt(0) == '`'))
    {
      if (dbName.charAt(dbName.length() - 1) == '`')
      {
        String quotedName = getOpenQuote();
        quotedName = quotedName + dbName.substring(1, dbName.length() - 1);
        quotedName = quotedName + getCloseQuote();
        
        return quotedName;
      }
      logger.log(Level.SEVERE, "Missing backquote on [" + dbName + "]");
    }
    return dbName;
  }
  
  public boolean isSelectCountWithAlias()
  {
    return this.selectCountWithAlias;
  }
  
  public String completeSql(String sql, SpiQuery<?> query)
  {
    if (Boolean.TRUE.equals(query.isForUpdate())) {
      sql = withForUpdate(sql);
    }
    return sql;
  }
  
  protected String withForUpdate(String sql)
  {
    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO, "it seems your database does not support the 'for update' clause");
    }
    return sql;
  }
}
