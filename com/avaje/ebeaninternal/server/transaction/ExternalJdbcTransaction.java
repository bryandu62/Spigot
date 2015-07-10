package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebean.LogLevel;
import java.sql.Connection;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

public class ExternalJdbcTransaction
  extends JdbcTransaction
{
  public ExternalJdbcTransaction(Connection connection)
  {
    super(null, true, LogLevel.NONE, connection, null);
  }
  
  public ExternalJdbcTransaction(String id, boolean explicit, Connection connection, TransactionManager manager)
  {
    super(id, explicit, manager.getTransactionLogLevel(), connection, manager);
  }
  
  public ExternalJdbcTransaction(String id, boolean explicit, LogLevel logLevel, Connection connection, TransactionManager manager)
  {
    super(id, explicit, logLevel, connection, manager);
  }
  
  public void commit()
    throws RollbackException
  {
    throw new PersistenceException("This is an external transaction so must be committed externally");
  }
  
  public void end()
    throws PersistenceException
  {
    throw new PersistenceException("This is an external transaction so must be committed externally");
  }
  
  public void rollback()
    throws PersistenceException
  {
    throw new PersistenceException("This is an external transaction so must be rolled back externally");
  }
  
  public void rollback(Throwable e)
    throws PersistenceException
  {
    throw new PersistenceException("This is an external transaction so must be rolled back externally");
  }
}
