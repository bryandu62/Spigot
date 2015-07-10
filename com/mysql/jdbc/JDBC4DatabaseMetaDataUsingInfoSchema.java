package com.mysql.jdbc;

import java.sql.RowIdLifetime;
import java.sql.SQLException;

public class JDBC4DatabaseMetaDataUsingInfoSchema
  extends DatabaseMetaDataUsingInfoSchema
{
  public JDBC4DatabaseMetaDataUsingInfoSchema(MySQLConnection connToSet, String databaseToSet)
    throws SQLException
  {
    super(connToSet, databaseToSet);
  }
  
  public RowIdLifetime getRowIdLifetime()
    throws SQLException
  {
    return RowIdLifetime.ROWID_UNSUPPORTED;
  }
  
  public boolean isWrapperFor(Class<?> iface)
    throws SQLException
  {
    return iface.isInstance(this);
  }
  
  public <T> T unwrap(Class<T> iface)
    throws SQLException
  {
    try
    {
      return (T)iface.cast(this);
    }
    catch (ClassCastException cce)
    {
      throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.conn.getExceptionInterceptor());
    }
  }
  
  protected int getJDBC4FunctionNoTableConstant()
  {
    return 1;
  }
}
