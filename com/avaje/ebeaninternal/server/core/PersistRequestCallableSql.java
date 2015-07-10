package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.CallableSql;
import com.avaje.ebeaninternal.api.BindParams;
import com.avaje.ebeaninternal.api.BindParams.Param;
import com.avaje.ebeaninternal.api.SpiCallableSql;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.api.TransactionEventTable;
import com.avaje.ebeaninternal.server.persist.PersistExecute;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

public final class PersistRequestCallableSql
  extends PersistRequest
{
  private final SpiCallableSql callableSql;
  private int rowCount;
  private String bindLog;
  private CallableStatement cstmt;
  private BindParams bindParam;
  
  public PersistRequestCallableSql(SpiEbeanServer server, CallableSql cs, SpiTransaction t, PersistExecute persistExecute)
  {
    super(server, t, persistExecute);
    this.type = PersistRequest.Type.CALLABLESQL;
    this.callableSql = ((SpiCallableSql)cs);
  }
  
  public int executeOrQueue()
  {
    return executeStatement();
  }
  
  public int executeNow()
  {
    return this.persistExecute.executeSqlCallable(this);
  }
  
  public SpiCallableSql getCallableSql()
  {
    return this.callableSql;
  }
  
  public void setBindLog(String bindLog)
  {
    this.bindLog = bindLog;
  }
  
  public void checkRowCount(int count)
    throws SQLException
  {
    this.rowCount = count;
  }
  
  public void setGeneratedKey(Object idValue) {}
  
  public boolean useGeneratedKeys()
  {
    return false;
  }
  
  public void postExecute()
    throws SQLException
  {
    if (this.transaction.isLogSummary())
    {
      String m = "CallableSql label[" + this.callableSql.getLabel() + "]" + " rows[" + this.rowCount + "]" + " bind[" + this.bindLog + "]";
      this.transaction.logInternal(m);
    }
    TransactionEventTable tableEvents = this.callableSql.getTransactionEventTable();
    if ((tableEvents != null) && (!tableEvents.isEmpty())) {
      this.transaction.getEvent().add(tableEvents);
    }
  }
  
  public void setBound(BindParams bindParam, CallableStatement cstmt)
  {
    this.bindParam = bindParam;
    this.cstmt = cstmt;
  }
  
  public int executeUpdate()
    throws SQLException
  {
    if (this.callableSql.executeOverride(this.cstmt)) {
      return -1;
    }
    this.rowCount = this.cstmt.executeUpdate();
    
    readOutParams();
    
    return this.rowCount;
  }
  
  private void readOutParams()
    throws SQLException
  {
    List<BindParams.Param> list = this.bindParam.positionedParameters();
    int pos = 0;
    for (int i = 0; i < list.size(); i++)
    {
      pos++;
      BindParams.Param param = (BindParams.Param)list.get(i);
      if (param.isOutParam())
      {
        Object outValue = this.cstmt.getObject(pos);
        param.setOutValue(outValue);
      }
    }
  }
}
