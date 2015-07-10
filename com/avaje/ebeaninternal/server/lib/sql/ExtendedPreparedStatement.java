package com.avaje.ebeaninternal.server.lib.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class ExtendedPreparedStatement
  extends ExtendedStatement
  implements PreparedStatement
{
  final String sql;
  final String cacheKey;
  
  public ExtendedPreparedStatement(PooledConnection pooledConnection, PreparedStatement pstmt, String sql, String cacheKey)
  {
    super(pooledConnection, pstmt);
    this.sql = sql;
    this.cacheKey = cacheKey;
  }
  
  public PreparedStatement getDelegate()
  {
    return this.pstmt;
  }
  
  public String getCacheKey()
  {
    return this.cacheKey;
  }
  
  public String getSql()
  {
    return this.sql;
  }
  
  public void closeDestroy()
    throws SQLException
  {
    this.pstmt.close();
  }
  
  public void close()
    throws SQLException
  {
    this.pooledConnection.returnPreparedStatement(this);
  }
  
  public void addBatch()
    throws SQLException
  {
    try
    {
      this.pstmt.addBatch();
    }
    catch (SQLException e)
    {
      this.pooledConnection.addError(e);
      throw e;
    }
  }
  
  public void clearParameters()
    throws SQLException
  {
    try
    {
      this.pstmt.clearParameters();
    }
    catch (SQLException e)
    {
      this.pooledConnection.addError(e);
      throw e;
    }
  }
  
  public boolean execute()
    throws SQLException
  {
    try
    {
      return this.pstmt.execute();
    }
    catch (SQLException e)
    {
      this.pooledConnection.addError(e);
      throw e;
    }
  }
  
  public ResultSet executeQuery()
    throws SQLException
  {
    try
    {
      return this.pstmt.executeQuery();
    }
    catch (SQLException e)
    {
      this.pooledConnection.addError(e);
      throw e;
    }
  }
  
  public int executeUpdate()
    throws SQLException
  {
    try
    {
      return this.pstmt.executeUpdate();
    }
    catch (SQLException e)
    {
      this.pooledConnection.addError(e);
      throw e;
    }
  }
  
  public ResultSetMetaData getMetaData()
    throws SQLException
  {
    try
    {
      return this.pstmt.getMetaData();
    }
    catch (SQLException e)
    {
      this.pooledConnection.addError(e);
      throw e;
    }
  }
  
  public ParameterMetaData getParameterMetaData()
    throws SQLException
  {
    return this.pstmt.getParameterMetaData();
  }
  
  public void setArray(int i, Array x)
    throws SQLException
  {
    this.pstmt.setArray(i, x);
  }
  
  public void setAsciiStream(int parameterIndex, InputStream x, int length)
    throws SQLException
  {
    this.pstmt.setAsciiStream(parameterIndex, x, length);
  }
  
  public void setBigDecimal(int parameterIndex, BigDecimal x)
    throws SQLException
  {
    this.pstmt.setBigDecimal(parameterIndex, x);
  }
  
  public void setBinaryStream(int parameterIndex, InputStream x, int length)
    throws SQLException
  {
    this.pstmt.setBinaryStream(parameterIndex, x, length);
  }
  
  public void setBlob(int i, Blob x)
    throws SQLException
  {
    this.pstmt.setBlob(i, x);
  }
  
  public void setBoolean(int parameterIndex, boolean x)
    throws SQLException
  {
    this.pstmt.setBoolean(parameterIndex, x);
  }
  
  public void setByte(int parameterIndex, byte x)
    throws SQLException
  {
    this.pstmt.setByte(parameterIndex, x);
  }
  
  public void setBytes(int parameterIndex, byte[] x)
    throws SQLException
  {
    this.pstmt.setBytes(parameterIndex, x);
  }
  
  public void setCharacterStream(int parameterIndex, Reader reader, int length)
    throws SQLException
  {
    this.pstmt.setCharacterStream(parameterIndex, reader, length);
  }
  
  public void setClob(int i, Clob x)
    throws SQLException
  {
    this.pstmt.setClob(i, x);
  }
  
  public void setDate(int parameterIndex, Date x)
    throws SQLException
  {
    this.pstmt.setDate(parameterIndex, x);
  }
  
  public void setDate(int parameterIndex, Date x, Calendar cal)
    throws SQLException
  {
    this.pstmt.setDate(parameterIndex, x, cal);
  }
  
  public void setDouble(int parameterIndex, double x)
    throws SQLException
  {
    this.pstmt.setDouble(parameterIndex, x);
  }
  
  public void setFloat(int parameterIndex, float x)
    throws SQLException
  {
    this.pstmt.setFloat(parameterIndex, x);
  }
  
  public void setInt(int parameterIndex, int x)
    throws SQLException
  {
    this.pstmt.setInt(parameterIndex, x);
  }
  
  public void setLong(int parameterIndex, long x)
    throws SQLException
  {
    this.pstmt.setLong(parameterIndex, x);
  }
  
  public void setNull(int parameterIndex, int sqlType)
    throws SQLException
  {
    this.pstmt.setNull(parameterIndex, sqlType);
  }
  
  public void setNull(int paramIndex, int sqlType, String typeName)
    throws SQLException
  {
    this.pstmt.setNull(paramIndex, sqlType, typeName);
  }
  
  public void setObject(int parameterIndex, Object x)
    throws SQLException
  {
    this.pstmt.setObject(parameterIndex, x);
  }
  
  public void setObject(int parameterIndex, Object x, int targetSqlType)
    throws SQLException
  {
    this.pstmt.setObject(parameterIndex, x, targetSqlType);
  }
  
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
    throws SQLException
  {
    this.pstmt.setObject(parameterIndex, x, targetSqlType, scale);
  }
  
  public void setRef(int i, Ref x)
    throws SQLException
  {
    this.pstmt.setRef(i, x);
  }
  
  public void setShort(int parameterIndex, short x)
    throws SQLException
  {
    this.pstmt.setShort(parameterIndex, x);
  }
  
  public void setString(int parameterIndex, String x)
    throws SQLException
  {
    this.pstmt.setString(parameterIndex, x);
  }
  
  public void setTime(int parameterIndex, Time x)
    throws SQLException
  {
    this.pstmt.setTime(parameterIndex, x);
  }
  
  public void setTime(int parameterIndex, Time x, Calendar cal)
    throws SQLException
  {
    this.pstmt.setTime(parameterIndex, x, cal);
  }
  
  public void setTimestamp(int parameterIndex, Timestamp x)
    throws SQLException
  {
    this.pstmt.setTimestamp(parameterIndex, x);
  }
  
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
    throws SQLException
  {
    this.pstmt.setTimestamp(parameterIndex, x, cal);
  }
  
  /**
   * @deprecated
   */
  public void setUnicodeStream(int parameterIndex, InputStream x, int length)
    throws SQLException
  {
    this.pstmt.setUnicodeStream(parameterIndex, x, length);
  }
  
  public void setURL(int parameterIndex, URL x)
    throws SQLException
  {
    this.pstmt.setURL(parameterIndex, x);
  }
}
