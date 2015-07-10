package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.LogLevel;
import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

public class JtaTransaction
  extends JdbcTransaction
{
  private UserTransaction userTransaction;
  private DataSource dataSource;
  private boolean commmitted = false;
  private boolean newTransaction = false;
  
  public JtaTransaction(String id, boolean explicit, LogLevel logLevel, UserTransaction utx, DataSource ds, TransactionManager manager)
  {
    super(id, explicit, logLevel, null, manager);
    this.userTransaction = utx;
    this.dataSource = ds;
    try
    {
      this.newTransaction = (this.userTransaction.getStatus() == 6);
      if (this.newTransaction) {
        this.userTransaction.begin();
      }
    }
    catch (Exception e)
    {
      throw new PersistenceException(e);
    }
    try
    {
      this.connection = this.dataSource.getConnection();
      if (this.connection == null) {
        throw new PersistenceException("The DataSource returned a null connection.");
      }
      if (this.connection.getAutoCommit()) {
        this.connection.setAutoCommit(false);
      }
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public void commit()
  {
    if (this.commmitted) {
      throw new PersistenceException("This transaction has already been committed.");
    }
    try
    {
      try
      {
        if (this.newTransaction) {
          this.userTransaction.commit();
        }
        notifyCommit();
      }
      finally
      {
        close();
      }
    }
    catch (Exception e)
    {
      throw new PersistenceException(e);
    }
    this.commmitted = true;
  }
  
  public void rollback()
  {
    rollback(null);
  }
  
  public void rollback(Throwable e)
  {
    if (!this.commmitted) {
      try
      {
        try
        {
          if (this.userTransaction != null) {
            if (this.newTransaction) {
              this.userTransaction.rollback();
            } else {
              this.userTransaction.setRollbackOnly();
            }
          }
          notifyRollback(e);
        }
        finally
        {
          close();
        }
      }
      catch (Exception ex)
      {
        throw new PersistenceException(ex);
      }
    }
  }
  
  private void close()
    throws SQLException
  {
    if (this.connection != null)
    {
      this.connection.close();
      this.connection = null;
    }
  }
}
