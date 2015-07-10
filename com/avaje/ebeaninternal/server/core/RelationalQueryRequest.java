package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.Transaction;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.api.SpiSqlQuery;
import com.avaje.ebeaninternal.api.SpiTransaction;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RelationalQueryRequest
{
  private final SpiSqlQuery query;
  private final RelationalQueryEngine queryEngine;
  private final SpiEbeanServer ebeanServer;
  private SpiTransaction trans;
  private boolean createdTransaction;
  private SpiQuery.Type queryType;
  
  public RelationalQueryRequest(SpiEbeanServer server, RelationalQueryEngine engine, SqlQuery q, Transaction t)
  {
    this.ebeanServer = server;
    this.queryEngine = engine;
    this.query = ((SpiSqlQuery)q);
    this.trans = ((SpiTransaction)t);
  }
  
  public void rollbackTransIfRequired()
  {
    if (this.createdTransaction) {
      this.trans.rollback();
    }
  }
  
  public void initTransIfRequired()
  {
    if (this.trans == null)
    {
      this.trans = this.ebeanServer.getCurrentServerTransaction();
      if ((this.trans == null) || (!this.trans.isActive()))
      {
        this.trans = this.ebeanServer.createServerTransaction(false, -1);
        
        this.createdTransaction = true;
      }
    }
  }
  
  public void endTransIfRequired()
  {
    if (this.createdTransaction) {
      this.trans.rollback();
    }
  }
  
  public List<SqlRow> findList()
  {
    this.queryType = SpiQuery.Type.LIST;
    return (List)this.queryEngine.findMany(this);
  }
  
  public Set<SqlRow> findSet()
  {
    this.queryType = SpiQuery.Type.SET;
    return (Set)this.queryEngine.findMany(this);
  }
  
  public Map<?, SqlRow> findMap()
  {
    this.queryType = SpiQuery.Type.MAP;
    return (Map)this.queryEngine.findMany(this);
  }
  
  public SpiSqlQuery getQuery()
  {
    return this.query;
  }
  
  public SpiQuery.Type getQueryType()
  {
    return this.queryType;
  }
  
  public EbeanServer getEbeanServer()
  {
    return this.ebeanServer;
  }
  
  public SpiTransaction getTransaction()
  {
    return this.trans;
  }
  
  public boolean isLogSql()
  {
    return this.trans.isLogSql();
  }
  
  public boolean isLogSummary()
  {
    return this.trans.isLogSummary();
  }
}
