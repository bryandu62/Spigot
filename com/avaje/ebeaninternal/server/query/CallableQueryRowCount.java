package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import java.util.concurrent.Callable;

public class CallableQueryRowCount<T>
  extends CallableQuery<T>
  implements Callable<Integer>
{
  public CallableQueryRowCount(SpiEbeanServer server, Query<T> query, Transaction t)
  {
    super(server, query, t);
  }
  
  public Integer call()
    throws Exception
  {
    return Integer.valueOf(this.server.findRowCountWithCopy(this.query, this.t));
  }
}
