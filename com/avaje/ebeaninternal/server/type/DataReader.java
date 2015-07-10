package com.avaje.ebeaninternal.server.type;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public abstract interface DataReader
{
  public abstract void close()
    throws SQLException;
  
  public abstract boolean next()
    throws SQLException;
  
  public abstract void resetColumnPosition();
  
  public abstract void incrementPos(int paramInt);
  
  public abstract byte[] getBinaryBytes()
    throws SQLException;
  
  public abstract byte[] getBlobBytes()
    throws SQLException;
  
  public abstract String getStringFromStream()
    throws SQLException;
  
  public abstract String getStringClob()
    throws SQLException;
  
  public abstract String getString()
    throws SQLException;
  
  public abstract Boolean getBoolean()
    throws SQLException;
  
  public abstract Byte getByte()
    throws SQLException;
  
  public abstract Short getShort()
    throws SQLException;
  
  public abstract Integer getInt()
    throws SQLException;
  
  public abstract Long getLong()
    throws SQLException;
  
  public abstract Float getFloat()
    throws SQLException;
  
  public abstract Double getDouble()
    throws SQLException;
  
  public abstract byte[] getBytes()
    throws SQLException;
  
  public abstract Date getDate()
    throws SQLException;
  
  public abstract Time getTime()
    throws SQLException;
  
  public abstract Timestamp getTimestamp()
    throws SQLException;
  
  public abstract BigDecimal getBigDecimal()
    throws SQLException;
  
  public abstract Array getArray()
    throws SQLException;
  
  public abstract Object getObject()
    throws SQLException;
}
