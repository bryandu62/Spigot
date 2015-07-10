package com.avaje.ebeaninternal.server.core;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.persist.BatchControl;
import com.avaje.ebeaninternal.server.persist.BatchPostExecute;
import com.avaje.ebeaninternal.server.persist.PersistExecute;

public abstract class PersistRequest
  extends BeanRequest
  implements BatchPostExecute
{
  boolean persistCascade;
  Type type;
  final PersistExecute persistExecute;
  
  public static enum Type
  {
    INSERT,  UPDATE,  DELETE,  ORMUPDATE,  UPDATESQL,  CALLABLESQL;
    
    private Type() {}
  }
  
  public PersistRequest(SpiEbeanServer server, SpiTransaction t, PersistExecute persistExecute)
  {
    super(server, t);
    this.persistExecute = persistExecute;
  }
  
  public abstract int executeOrQueue();
  
  public abstract int executeNow();
  
  public PstmtBatch getPstmtBatch()
  {
    return this.ebeanServer.getPstmtBatch();
  }
  
  public boolean isLogSql()
  {
    return this.transaction.isLogSql();
  }
  
  public boolean isLogSummary()
  {
    return this.transaction.isLogSummary();
  }
  
  public int executeStatement()
  {
    boolean batch = this.transaction.isBatchThisRequest();
    
    BatchControl control = this.transaction.getBatchControl();
    int rows;
    int rows;
    if (control != null)
    {
      rows = control.executeStatementOrBatch(this, batch);
    }
    else
    {
      int rows;
      if (batch)
      {
        control = this.persistExecute.createBatchControl(this.transaction);
        rows = control.executeStatementOrBatch(this, batch);
      }
      else
      {
        rows = executeNow();
      }
    }
    return rows;
  }
  
  public void initTransIfRequired()
  {
    createImplicitTransIfRequired(false);
    this.persistCascade = this.transaction.isPersistCascade();
  }
  
  public Type getType()
  {
    return this.type;
  }
  
  public void setType(Type type)
  {
    this.type = type;
  }
  
  public boolean isPersistCascade()
  {
    return this.persistCascade;
  }
}
