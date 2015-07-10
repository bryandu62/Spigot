package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.BackgroundExecutor;
import javax.sql.DataSource;

public class OracleSequenceIdGenerator
  extends SequenceIdGenerator
{
  private final String baseSql;
  
  public OracleSequenceIdGenerator(BackgroundExecutor be, DataSource ds, String seqName, int batchSize)
  {
    super(be, ds, seqName, batchSize);
    this.baseSql = ("select " + seqName + ".nextval, a from (select level as a FROM dual CONNECT BY level <= ");
  }
  
  public String getSql(int batchSize)
  {
    return this.baseSql + batchSize + ")";
  }
}