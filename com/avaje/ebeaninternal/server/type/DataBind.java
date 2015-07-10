package com.avaje.ebeaninternal.server.type;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class DataBind
{
  private final PreparedStatement pstmt;
  private int pos;
  
  public DataBind(PreparedStatement pstmt)
  {
    this.pstmt = pstmt;
  }
  
  public void close()
    throws SQLException
  {
    this.pstmt.close();
  }
  
  public int currentPos()
  {
    return this.pos;
  }
  
  public void resetPos()
  {
    this.pos = 0;
  }
  
  public void setObject(Object value)
    throws SQLException
  {
    this.pstmt.setObject(++this.pos, value);
  }
  
  public void setObject(Object value, int sqlType)
    throws SQLException
  {
    this.pstmt.setObject(++this.pos, value, sqlType);
  }
  
  public void setNull(int jdbcType)
    throws SQLException
  {
    this.pstmt.setNull(++this.pos, jdbcType);
  }
  
  public int nextPos()
  {
    return ++this.pos;
  }
  
  public int decrementPos()
  {
    return ++this.pos;
  }
  
  public int executeUpdate()
    throws SQLException
  {
    return this.pstmt.executeUpdate();
  }
  
  public PreparedStatement getPstmt()
  {
    return this.pstmt;
  }
  
  public void setString(String s)
    throws SQLException
  {
    this.pstmt.setString(++this.pos, s);
  }
  
  public void setInt(int i)
    throws SQLException
  {
    this.pstmt.setInt(++this.pos, i);
  }
  
  public void setLong(long i)
    throws SQLException
  {
    this.pstmt.setLong(++this.pos, i);
  }
  
  public void setShort(short i)
    throws SQLException
  {
    this.pstmt.setShort(++this.pos, i);
  }
  
  public void setFloat(float i)
    throws SQLException
  {
    this.pstmt.setFloat(++this.pos, i);
  }
  
  public void setDouble(double i)
    throws SQLException
  {
    this.pstmt.setDouble(++this.pos, i);
  }
  
  public void setBigDecimal(BigDecimal v)
    throws SQLException
  {
    this.pstmt.setBigDecimal(++this.pos, v);
  }
  
  public void setDate(Date v)
    throws SQLException
  {
    this.pstmt.setDate(++this.pos, v);
  }
  
  public void setTimestamp(Timestamp v)
    throws SQLException
  {
    this.pstmt.setTimestamp(++this.pos, v);
  }
  
  public void setTime(Time v)
    throws SQLException
  {
    this.pstmt.setTime(++this.pos, v);
  }
  
  public void setBoolean(boolean v)
    throws SQLException
  {
    this.pstmt.setBoolean(++this.pos, v);
  }
  
  public void setBytes(byte[] v)
    throws SQLException
  {
    this.pstmt.setBytes(++this.pos, v);
  }
  
  public void setByte(byte v)
    throws SQLException
  {
    this.pstmt.setByte(++this.pos, v);
  }
  
  public void setChar(char v)
    throws SQLException
  {
    this.pstmt.setString(++this.pos, String.valueOf(v));
  }
  
  public void setBlob(byte[] bytes)
    throws SQLException
  {
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    this.pstmt.setBinaryStream(++this.pos, is, bytes.length);
  }
  
  public void setClob(String content)
    throws SQLException
  {
    Reader reader = new StringReader(content);
    this.pstmt.setCharacterStream(++this.pos, reader, content.length());
  }
}
