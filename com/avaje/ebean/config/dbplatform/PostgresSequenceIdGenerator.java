package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.BackgroundExecutor;
import javax.sql.DataSource;

public class PostgresSequenceIdGenerator
  extends SequenceIdGenerator
{
  private final String baseSql;
  
  public PostgresSequenceIdGenerator(BackgroundExecutor be, DataSource ds, String seqName, int batchSize)
  {
    super(be, ds, seqName, batchSize);
    this.baseSql = ("select nextval('" + seqName + "'), s.generate_series from (" + "select generate_series from generate_series(1,");
  }
  
  public String getSql(int batchSize)
  {
    return this.baseSql + batchSize + ") ) as s";
  }
}
