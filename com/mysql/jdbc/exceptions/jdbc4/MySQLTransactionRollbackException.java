package com.mysql.jdbc.exceptions.jdbc4;

import com.mysql.jdbc.exceptions.DeadlockTimeoutRollbackMarker;
import java.sql.SQLTransactionRollbackException;

public class MySQLTransactionRollbackException
  extends SQLTransactionRollbackException
  implements DeadlockTimeoutRollbackMarker
{
  public MySQLTransactionRollbackException(String reason, String SQLState, int vendorCode)
  {
    super(reason, SQLState, vendorCode);
  }
  
  public MySQLTransactionRollbackException(String reason, String SQLState)
  {
    super(reason, SQLState);
  }
  
  public MySQLTransactionRollbackException(String reason)
  {
    super(reason);
  }
  
  public MySQLTransactionRollbackException() {}
}
