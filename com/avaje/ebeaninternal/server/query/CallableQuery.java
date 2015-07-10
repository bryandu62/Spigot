package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.api.SpiEbeanServer;

public abstract class CallableQuery<T>
{
  protected final Query<T> query;
  protected final SpiEbeanServer server;
  protected final Transaction t;
  
  public CallableQuery(SpiEbeanServer server, Query<T> query, Transaction t)
  {
    this.server = server;
    this.query = query;
    this.t = t;
  }
}
