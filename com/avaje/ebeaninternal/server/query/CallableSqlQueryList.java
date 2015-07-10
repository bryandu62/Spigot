package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.Transaction;
import java.util.List;
import java.util.concurrent.Callable;

public class CallableSqlQueryList
  implements Callable<List<SqlRow>>
{
  private final SqlQuery query;
  private final EbeanServer server;
  private final Transaction t;
  
  public CallableSqlQueryList(EbeanServer server, SqlQuery query, Transaction t)
  {
    this.server = server;
    this.query = query;
    this.t = t;
  }
  
  public List<SqlRow> call()
    throws Exception
  {
    return this.server.findList(this.query, this.t);
  }
}
