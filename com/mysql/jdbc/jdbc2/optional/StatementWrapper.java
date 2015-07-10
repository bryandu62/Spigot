package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class StatementWrapper
  extends WrapperBase
  implements java.sql.Statement
{
  private static final Constructor JDBC_4_STATEMENT_WRAPPER_CTOR;
  protected java.sql.Statement wrappedStmt;
  protected ConnectionWrapper wrappedConn;
  
  static
  {
    if (Util.isJdbc4()) {
      try
      {
        JDBC_4_STATEMENT_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4StatementWrapper").getConstructor(new Class[] { ConnectionWrapper.class, MysqlPooledConnection.class, java.sql.Statement.class });
      }
      catch (SecurityException e)
      {
        throw new RuntimeException(e);
      }
      catch (NoSuchMethodException e)
      {
        throw new RuntimeException(e);
      }
      catch (ClassNotFoundException e)
      {
        throw new RuntimeException(e);
      }
    } else {
      JDBC_4_STATEMENT_WRAPPER_CTOR = null;
    }
  }
  
  protected static StatementWrapper getInstance(ConnectionWrapper c, MysqlPooledConnection conn, java.sql.Statement toWrap)
    throws SQLException
  {
    if (!Util.isJdbc4()) {
      return new StatementWrapper(c, conn, toWrap);
    }
    return (StatementWrapper)Util.handleNewInstance(JDBC_4_STATEMENT_WRAPPER_CTOR, new Object[] { c, conn, toWrap }, conn.getExceptionInterceptor());
  }
  
  public StatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, java.sql.Statement toWrap)
  {
    super(conn);
    this.wrappedStmt = toWrap;
    this.wrappedConn = c;
  }
  
  public Connection getConnection()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedConn;
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return null;
  }
  
  public void setCursorName(String name)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.setCursorName(name);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public void setEscapeProcessing(boolean enable)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.setEscapeProcessing(enable);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public void setFetchDirection(int direction)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.setFetchDirection(direction);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public int getFetchDirection()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getFetchDirection();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 1000;
  }
  
  public void setFetchSize(int rows)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.setFetchSize(rows);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public int getFetchSize()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getFetchSize();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 0;
  }
  
  public ResultSet getGeneratedKeys()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getGeneratedKeys();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return null;
  }
  
  public void setMaxFieldSize(int max)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.setMaxFieldSize(max);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public int getMaxFieldSize()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getMaxFieldSize();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 0;
  }
  
  public void setMaxRows(int max)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.setMaxRows(max);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public int getMaxRows()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getMaxRows();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 0;
  }
  
  public boolean getMoreResults()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getMoreResults();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return false;
  }
  
  public boolean getMoreResults(int current)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getMoreResults(current);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return false;
  }
  
  public void setQueryTimeout(int seconds)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.setQueryTimeout(seconds);
      } else {
        throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public int getQueryTimeout()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getQueryTimeout();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 0;
  }
  
  public ResultSet getResultSet()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null)
      {
        ResultSet rs = this.wrappedStmt.getResultSet();
        if (rs != null) {
          ((ResultSetInternalMethods)rs).setWrapperStatement(this);
        }
        return rs;
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return null;
  }
  
  public int getResultSetConcurrency()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getResultSetConcurrency();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 0;
  }
  
  public int getResultSetHoldability()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getResultSetHoldability();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 1;
  }
  
  public int getResultSetType()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getResultSetType();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return 1003;
  }
  
  public int getUpdateCount()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getUpdateCount();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return -1;
  }
  
  public SQLWarning getWarnings()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.getWarnings();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return null;
  }
  
  public void addBatch(String sql)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.addBatch(sql);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public void cancel()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.cancel();
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public void clearBatch()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.clearBatch();
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public void clearWarnings()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.clearWarnings();
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
  
  public void close()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        this.wrappedStmt.close();
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    finally
    {
      this.wrappedStmt = null;
      this.pooledConnection = null;
    }
  }
  
  public boolean execute(String sql, int autoGeneratedKeys)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.execute(sql, autoGeneratedKeys);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return false;
  }
  
  public boolean execute(String sql, int[] columnIndexes)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.execute(sql, columnIndexes);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return false;
  }
  
  public boolean execute(String sql, String[] columnNames)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.execute(sql, columnNames);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return false;
  }
  
  public boolean execute(String sql)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.execute(sql);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return false;
  }
  
  public int[] executeBatch()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.executeBatch();
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return null;
  }
  
  public ResultSet executeQuery(String sql)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null)
      {
        ResultSet rs = this.wrappedStmt.executeQuery(sql);
        ((ResultSetInternalMethods)rs).setWrapperStatement(this);
        
        return rs;
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return null;
  }
  
  public int executeUpdate(String sql, int autoGeneratedKeys)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.executeUpdate(sql, autoGeneratedKeys);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return -1;
  }
  
  public int executeUpdate(String sql, int[] columnIndexes)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.executeUpdate(sql, columnIndexes);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return -1;
  }
  
  public int executeUpdate(String sql, String[] columnNames)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.executeUpdate(sql, columnNames);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return -1;
  }
  
  public int executeUpdate(String sql)
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        return this.wrappedStmt.executeUpdate(sql);
      }
      throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
    return -1;
  }
  
  public void enableStreamingResults()
    throws SQLException
  {
    try
    {
      if (this.wrappedStmt != null) {
        ((com.mysql.jdbc.Statement)this.wrappedStmt).enableStreamingResults();
      } else {
        throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
      }
    }
    catch (SQLException sqlEx)
    {
      checkAndFireConnectionError(sqlEx);
    }
  }
}
