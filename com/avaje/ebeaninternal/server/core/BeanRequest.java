package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.LogLevel;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiTransaction;
import java.sql.Connection;

public abstract class BeanRequest
{
  final SpiEbeanServer ebeanServer;
  final String serverName;
  SpiTransaction transaction;
  boolean createdTransaction;
  boolean readOnly;
  
  public BeanRequest(SpiEbeanServer ebeanServer, SpiTransaction t)
  {
    this.ebeanServer = ebeanServer;
    this.serverName = ebeanServer.getName();
    this.transaction = t;
  }
  
  public abstract void initTransIfRequired();
  
  public void createImplicitTransIfRequired(boolean readOnlyTransaction)
  {
    if (this.transaction == null)
    {
      this.transaction = this.ebeanServer.getCurrentServerTransaction();
      if ((this.transaction == null) || (!this.transaction.isActive()))
      {
        this.transaction = this.ebeanServer.createServerTransaction(false, -1);
        
        this.createdTransaction = true;
      }
    }
  }
  
  public void commitTransIfRequired()
  {
    if (this.createdTransaction) {
      if (this.readOnly) {
        this.transaction.rollback();
      } else {
        this.transaction.commit();
      }
    }
  }
  
  public void rollbackTransIfRequired()
  {
    if (this.createdTransaction) {
      this.transaction.rollback();
    }
  }
  
  public EbeanServer getEbeanServer()
  {
    return this.ebeanServer;
  }
  
  public SpiTransaction getTransaction()
  {
    return this.transaction;
  }
  
  public Connection getConnection()
  {
    return this.transaction.getInternalConnection();
  }
  
  public boolean isLogSql()
  {
    return this.transaction.getLogLevel().ordinal() >= LogLevel.SQL.ordinal();
  }
  
  public boolean isLogSummary()
  {
    return this.transaction.getLogLevel().ordinal() >= LogLevel.SUMMARY.ordinal();
  }
}
