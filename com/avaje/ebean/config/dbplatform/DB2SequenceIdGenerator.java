package com.avaje.ebean.config.dbplatform;

import com.avaje.ebean.BackgroundExecutor;
import javax.sql.DataSource;

public class DB2SequenceIdGenerator
  extends SequenceIdGenerator
{
  private final String baseSql;
  private final String unionBaseSql;
  
  public DB2SequenceIdGenerator(BackgroundExecutor be, DataSource ds, String seqName, int batchSize)
  {
    super(be, ds, seqName, batchSize);
    this.baseSql = ("select nextval for " + seqName);
    this.unionBaseSql = (" union " + this.baseSql);
  }
  
  public String getSql(int batchSize)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(this.baseSql);
    for (int i = 1; i < batchSize; i++) {
      sb.append(this.unionBaseSql);
    }
    return sb.toString();
  }
}
