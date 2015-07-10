package com.avaje.ebeaninternal.server.core;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.SpiUpdate;
import com.avaje.ebeaninternal.api.SpiUpdate.OrmUpdateType;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanManager;
import com.avaje.ebeaninternal.server.persist.PersistExecute;
import java.sql.SQLException;

public final class PersistRequestOrmUpdate
  extends PersistRequest
{
  private final BeanDescriptor<?> beanDescriptor;
  private SpiUpdate<?> ormUpdate;
  private int rowCount;
  private String bindLog;
  
  public PersistRequestOrmUpdate(SpiEbeanServer server, BeanManager<?> mgr, SpiUpdate<?> ormUpdate, SpiTransaction t, PersistExecute persistExecute)
  {
    super(server, t, persistExecute);
    this.beanDescriptor = mgr.getBeanDescriptor();
    this.ormUpdate = ormUpdate;
  }
  
  public BeanDescriptor<?> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public int executeNow()
  {
    return this.persistExecute.executeOrmUpdate(this);
  }
  
  public int executeOrQueue()
  {
    return executeStatement();
  }
  
  public SpiUpdate<?> getOrmUpdate()
  {
    return this.ormUpdate;
  }
  
  public void checkRowCount(int count)
    throws SQLException
  {
    this.rowCount = count;
  }
  
  public boolean useGeneratedKeys()
  {
    return false;
  }
  
  public void setGeneratedKey(Object idValue) {}
  
  public void setBindLog(String bindLog)
  {
    this.bindLog = bindLog;
  }
  
  public void postExecute()
    throws SQLException
  {
    SpiUpdate.OrmUpdateType ormUpdateType = this.ormUpdate.getOrmUpdateType();
    String tableName = this.ormUpdate.getBaseTable();
    if (this.transaction.isLogSummary())
    {
      String m = ormUpdateType + " table[" + tableName + "] rows[" + this.rowCount + "] bind[" + this.bindLog + "]";
      this.transaction.logInternal(m);
    }
    if (this.ormUpdate.isNotifyCache()) {
      switch (ormUpdateType)
      {
      case INSERT: 
        this.transaction.getEvent().add(tableName, true, false, false);
        break;
      case UPDATE: 
        this.transaction.getEvent().add(tableName, false, true, false);
        break;
      case DELETE: 
        this.transaction.getEvent().add(tableName, false, false, true);
        break;
      }
    }
  }
}
