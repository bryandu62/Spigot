package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import java.util.List;
import java.util.concurrent.Callable;

public class CallableQueryIds<T>
  extends CallableQuery<T>
  implements Callable<List<Object>>
{
  public CallableQueryIds(SpiEbeanServer server, Query<T> query, Transaction t)
  {
    super(server, query, t);
  }
  
  public List<Object> call()
    throws Exception
  {
    return this.server.findIdsWithCopy(this.query, this.t);
  }
}
