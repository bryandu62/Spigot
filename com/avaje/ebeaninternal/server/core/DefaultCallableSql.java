package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.CallableSql;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.BindParams.Param;
import com.avaje.ebeaninternal.api.SpiCallableSql;
import com.avaje.ebeaninternal.api.TransactionEventTable;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class DefaultCallableSql
  implements Serializable, SpiCallableSql
{
  private static final long serialVersionUID = 8984272253185424701L;
  private final transient EbeanServer server;
  private String sql;
  private String label;
  private int timeout;
  private TransactionEventTable transactionEvent = new TransactionEventTable();
  private BindParams bindParameters = new BindParams();
  
  public DefaultCallableSql(EbeanServer server, String sql)
  {
    this.server = server;
    this.sql = sql;
  }
  
  public void execute()
  {
    this.server.execute(this, null);
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public CallableSql setLabel(String label)
  {
    this.label = label;
    return this;
  }
  
  public int getTimeout()
  {
    return this.timeout;
  }
  
  public String getSql()
  {
    return this.sql;
  }
  
  public CallableSql setTimeout(int secs)
  {
    this.timeout = secs;
    return this;
  }
  
  public CallableSql setSql(String sql)
  {
    this.sql = sql;
    return this;
  }
  
  public CallableSql bind(int position, Object value)
  {
    this.bindParameters.setParameter(position, value);
    return this;
  }
  
  public CallableSql setParameter(int position, Object value)
  {
    this.bindParameters.setParameter(position, value);
    return this;
  }
  
  public CallableSql registerOut(int position, int type)
  {
    this.bindParameters.registerOut(position, type);
    return this;
  }
  
  public Object getObject(int position)
  {
    BindParams.Param p = this.bindParameters.getParameter(position);
    return p.getOutValue();
  }
  
  public boolean executeOverride(CallableStatement cstmt)
    throws SQLException
  {
    return false;
  }
  
  public CallableSql addModification(String tableName, boolean inserts, boolean updates, boolean deletes)
  {
    this.transactionEvent.add(tableName, inserts, updates, deletes);
    return this;
  }
  
  public TransactionEventTable getTransactionEventTable()
  {
    return this.transactionEvent;
  }
  
  public BindParams getBindParams()
  {
    return this.bindParameters;
  }
}
