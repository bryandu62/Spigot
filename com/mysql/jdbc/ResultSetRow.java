package com.mysql.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

public abstract class ResultSetRow
{
  protected ExceptionInterceptor exceptionInterceptor;
  protected Field[] metadata;
  
  protected ResultSetRow(ExceptionInterceptor exceptionInterceptor)
  {
    this.exceptionInterceptor = exceptionInterceptor;
  }
  
  public abstract void closeOpenStreams();
  
  public abstract InputStream getBinaryInputStream(int paramInt)
    throws SQLException;
  
  public abstract byte[] getColumnValue(int paramInt)
    throws SQLException;
  
  protected final Date getDateFast(int columnIndex, byte[] dateAsBytes, int offset, int length, MySQLConnection conn, ResultSetImpl rs, Calendar targetCalendar)
    throws SQLException
  {
    int year = 0;
    int month = 0;
    int day = 0;
    try
    {
      if (dateAsBytes == null) {
        return null;
      }
      boolean allZeroDate = true;
      
      boolean onlyTimePresent = false;
      for (int i = 0; i < length; i++) {
        if (dateAsBytes[(offset + i)] == 58)
        {
          onlyTimePresent = true;
          break;
        }
      }
      for (int i = 0; i < length; i++)
      {
        byte b = dateAsBytes[(offset + i)];
        if ((b == 32) || (b == 45) || (b == 47)) {
          onlyTimePresent = false;
        }
        if ((b != 48) && (b != 32) && (b != 58) && (b != 45) && (b != 47) && (b != 46))
        {
          allZeroDate = false;
          
          break;
        }
      }
      if ((!onlyTimePresent) && (allZeroDate))
      {
        if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
          return null;
        }
        if ("exception".equals(conn.getZeroDateTimeBehavior())) {
          throw SQLError.createSQLException("Value '" + new String(dateAsBytes) + "' can not be represented as java.sql.Date", "S1009", this.exceptionInterceptor);
        }
        return rs.fastDateCreate(targetCalendar, 1, 1, 1);
      }
      if (this.metadata[columnIndex].getMysqlType() == 7)
      {
        switch (length)
        {
        case 19: 
        case 21: 
        case 29: 
          year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
          
          month = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
          
          day = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
          
          return rs.fastDateCreate(targetCalendar, year, month, day);
        case 8: 
        case 14: 
          year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
          
          month = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
          
          day = StringUtils.getInt(dateAsBytes, offset + 6, offset + 8);
          
          return rs.fastDateCreate(targetCalendar, year, month, day);
        case 6: 
        case 10: 
        case 12: 
          year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
          if (year <= 69) {
            year += 100;
          }
          month = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
          
          day = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
          
          return rs.fastDateCreate(targetCalendar, year + 1900, month, day);
        case 4: 
          year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
          if (year <= 69) {
            year += 100;
          }
          month = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
          
          return rs.fastDateCreate(targetCalendar, year + 1900, month, 1);
        case 2: 
          year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
          if (year <= 69) {
            year += 100;
          }
          return rs.fastDateCreate(targetCalendar, year + 1900, 1, 1);
        }
        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), Constants.integerValueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
      }
      if (this.metadata[columnIndex].getMysqlType() == 13)
      {
        if ((length == 2) || (length == 1))
        {
          year = StringUtils.getInt(dateAsBytes, offset, offset + length);
          if (year <= 69) {
            year += 100;
          }
          year += 1900;
        }
        else
        {
          year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
        }
        return rs.fastDateCreate(targetCalendar, year, 1, 1);
      }
      if (this.metadata[columnIndex].getMysqlType() == 11) {
        return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
      }
      if (length < 10)
      {
        if (length == 8) {
          return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
        }
        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), Constants.integerValueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
      }
      if (length != 18)
      {
        year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
        
        month = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
        
        day = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
      }
      else
      {
        StringTokenizer st = new StringTokenizer(new String(dateAsBytes, offset, length, "ISO8859_1"), "- ");
        
        year = Integer.parseInt(st.nextToken());
        month = Integer.parseInt(st.nextToken());
        day = Integer.parseInt(st.nextToken());
      }
      return rs.fastDateCreate(targetCalendar, year, month, day);
    }
    catch (SQLException sqlEx)
    {
      throw sqlEx;
    }
    catch (Exception e)
    {
      SQLException sqlEx = SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), Constants.integerValueOf(columnIndex + 1) }), "S1009", this.exceptionInterceptor);
      
      sqlEx.initCause(e);
      
      throw sqlEx;
    }
  }
  
  public abstract Date getDateFast(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar)
    throws SQLException;
  
  public abstract int getInt(int paramInt)
    throws SQLException;
  
  public abstract long getLong(int paramInt)
    throws SQLException;
  
  protected Date getNativeDate(int columnIndex, byte[] bits, int offset, int length, MySQLConnection conn, ResultSetImpl rs, Calendar cal)
    throws SQLException
  {
    int year = 0;
    int month = 0;
    int day = 0;
    if (length != 0)
    {
      year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
      
      month = bits[(offset + 2)];
      day = bits[(offset + 3)];
    }
    if ((length == 0) || ((year == 0) && (month == 0) && (day == 0)))
    {
      if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
        return null;
      }
      if ("exception".equals(conn.getZeroDateTimeBehavior())) {
        throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009", this.exceptionInterceptor);
      }
      year = 1;
      month = 1;
      day = 1;
    }
    if (!rs.useLegacyDatetimeCode) {
      return TimeUtil.fastDateCreate(year, month, day, cal);
    }
    return rs.fastDateCreate(cal == null ? rs.getCalendarInstanceForSessionOrNew() : cal, year, month, day);
  }
  
  public abstract Date getNativeDate(int paramInt, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl, Calendar paramCalendar)
    throws SQLException;
  
  protected Object getNativeDateTimeValue(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
    throws SQLException
  {
    int year = 0;
    int month = 0;
    int day = 0;
    
    int hour = 0;
    int minute = 0;
    int seconds = 0;
    
    int nanos = 0;
    if (bits == null) {
      return null;
    }
    Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
    
    boolean populatedFromDateTimeValue = false;
    switch (mysqlType)
    {
    case 7: 
    case 12: 
      populatedFromDateTimeValue = true;
      if (length != 0)
      {
        year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
        
        month = bits[(offset + 2)];
        day = bits[(offset + 3)];
        if (length > 4)
        {
          hour = bits[(offset + 4)];
          minute = bits[(offset + 5)];
          seconds = bits[(offset + 6)];
        }
        if (length > 7) {
          nanos = (bits[(offset + 7)] & 0xFF | (bits[(offset + 8)] & 0xFF) << 8 | (bits[(offset + 9)] & 0xFF) << 16 | (bits[(offset + 10)] & 0xFF) << 24) * 1000;
        }
      }
      break;
    case 10: 
      populatedFromDateTimeValue = true;
      if (bits.length != 0)
      {
        year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
        
        month = bits[(offset + 2)];
        day = bits[(offset + 3)];
      }
      break;
    case 11: 
      populatedFromDateTimeValue = true;
      if (bits.length != 0)
      {
        hour = bits[(offset + 5)];
        minute = bits[(offset + 6)];
        seconds = bits[(offset + 7)];
      }
      year = 1970;
      month = 1;
      day = 1;
      
      break;
    case 8: 
    case 9: 
    default: 
      populatedFromDateTimeValue = false;
    }
    switch (jdbcType)
    {
    case 92: 
      if (populatedFromDateTimeValue)
      {
        if (!rs.useLegacyDatetimeCode) {
          return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
        }
        Time time = TimeUtil.fastTimeCreate(rs.getCalendarInstanceForSessionOrNew(), hour, minute, seconds, this.exceptionInterceptor);
        
        Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
        
        return adjustedTime;
      }
      return rs.getNativeTimeViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
    case 91: 
      if (populatedFromDateTimeValue)
      {
        if ((year == 0) && (month == 0) && (day == 0))
        {
          if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
            return null;
          }
          if ("exception".equals(conn.getZeroDateTimeBehavior())) {
            throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009");
          }
          year = 1;
          month = 1;
          day = 1;
        }
        if (!rs.useLegacyDatetimeCode) {
          return TimeUtil.fastDateCreate(year, month, day, targetCalendar);
        }
        return rs.fastDateCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day);
      }
      return rs.getNativeDateViaParseConversion(columnIndex + 1);
    case 93: 
      if (populatedFromDateTimeValue)
      {
        if ((year == 0) && (month == 0) && (day == 0))
        {
          if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
            return null;
          }
          if ("exception".equals(conn.getZeroDateTimeBehavior())) {
            throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009");
          }
          year = 1;
          month = 1;
          day = 1;
        }
        if (!rs.useLegacyDatetimeCode) {
          return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
        }
        Timestamp ts = rs.fastTimestampCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day, hour, minute, seconds, nanos);
        
        Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
        
        return adjustedTs;
      }
      return rs.getNativeTimestampViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
    }
    throw new SQLException("Internal error - conversion method doesn't support this type", "S1000");
  }
  
  public abstract Object getNativeDateTimeValue(int paramInt1, Calendar paramCalendar, int paramInt2, int paramInt3, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
    throws SQLException;
  
  protected double getNativeDouble(byte[] bits, int offset)
  {
    long valueAsLong = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24 | (bits[(offset + 4)] & 0xFF) << 32 | (bits[(offset + 5)] & 0xFF) << 40 | (bits[(offset + 6)] & 0xFF) << 48 | (bits[(offset + 7)] & 0xFF) << 56;
    
    return Double.longBitsToDouble(valueAsLong);
  }
  
  public abstract double getNativeDouble(int paramInt)
    throws SQLException;
  
  protected float getNativeFloat(byte[] bits, int offset)
  {
    int asInt = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24;
    
    return Float.intBitsToFloat(asInt);
  }
  
  public abstract float getNativeFloat(int paramInt)
    throws SQLException;
  
  protected int getNativeInt(byte[] bits, int offset)
  {
    int valueAsInt = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24;
    
    return valueAsInt;
  }
  
  public abstract int getNativeInt(int paramInt)
    throws SQLException;
  
  protected long getNativeLong(byte[] bits, int offset)
  {
    long valueAsLong = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8 | (bits[(offset + 2)] & 0xFF) << 16 | (bits[(offset + 3)] & 0xFF) << 24 | (bits[(offset + 4)] & 0xFF) << 32 | (bits[(offset + 5)] & 0xFF) << 40 | (bits[(offset + 6)] & 0xFF) << 48 | (bits[(offset + 7)] & 0xFF) << 56;
    
    return valueAsLong;
  }
  
  public abstract long getNativeLong(int paramInt)
    throws SQLException;
  
  protected short getNativeShort(byte[] bits, int offset)
  {
    short asShort = (short)(bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8);
    
    return asShort;
  }
  
  public abstract short getNativeShort(int paramInt)
    throws SQLException;
  
  protected Time getNativeTime(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
    throws SQLException
  {
    int hour = 0;
    int minute = 0;
    int seconds = 0;
    if (length != 0)
    {
      hour = bits[(offset + 5)];
      minute = bits[(offset + 6)];
      seconds = bits[(offset + 7)];
    }
    if (!rs.useLegacyDatetimeCode) {
      return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
    }
    Calendar sessionCalendar = rs.getCalendarInstanceForSessionOrNew();
    synchronized (sessionCalendar)
    {
      Time time = TimeUtil.fastTimeCreate(sessionCalendar, hour, minute, seconds, this.exceptionInterceptor);
      
      Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
      
      return adjustedTime;
    }
  }
  
  public abstract Time getNativeTime(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
    throws SQLException;
  
  protected Timestamp getNativeTimestamp(byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
    throws SQLException
  {
    int year = 0;
    int month = 0;
    int day = 0;
    
    int hour = 0;
    int minute = 0;
    int seconds = 0;
    
    int nanos = 0;
    if (length != 0)
    {
      year = bits[(offset + 0)] & 0xFF | (bits[(offset + 1)] & 0xFF) << 8;
      month = bits[(offset + 2)];
      day = bits[(offset + 3)];
      if (length > 4)
      {
        hour = bits[(offset + 4)];
        minute = bits[(offset + 5)];
        seconds = bits[(offset + 6)];
      }
      if (length > 7) {
        nanos = (bits[(offset + 7)] & 0xFF | (bits[(offset + 8)] & 0xFF) << 8 | (bits[(offset + 9)] & 0xFF) << 16 | (bits[(offset + 10)] & 0xFF) << 24) * 1000;
      }
    }
    if ((length == 0) || ((year == 0) && (month == 0) && (day == 0)))
    {
      if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
        return null;
      }
      if ("exception".equals(conn.getZeroDateTimeBehavior())) {
        throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009", this.exceptionInterceptor);
      }
      year = 1;
      month = 1;
      day = 1;
    }
    if (!rs.useLegacyDatetimeCode) {
      return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
    }
    Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
    synchronized (sessionCalendar)
    {
      Timestamp ts = rs.fastTimestampCreate(sessionCalendar, year, month, day, hour, minute, seconds, nanos);
      
      Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
      
      return adjustedTs;
    }
  }
  
  public abstract Timestamp getNativeTimestamp(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
    throws SQLException;
  
  public abstract Reader getReader(int paramInt)
    throws SQLException;
  
  public abstract String getString(int paramInt, String paramString, MySQLConnection paramMySQLConnection)
    throws SQLException;
  
  protected String getString(String encoding, MySQLConnection conn, byte[] value, int offset, int length)
    throws SQLException
  {
    String stringVal = null;
    if ((conn != null) && (conn.getUseUnicode())) {
      try
      {
        if (encoding == null)
        {
          stringVal = new String(value);
        }
        else
        {
          SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
          if (converter != null) {
            stringVal = converter.toString(value, offset, length);
          } else {
            stringVal = new String(value, offset, length, encoding);
          }
        }
      }
      catch (UnsupportedEncodingException E)
      {
        throw SQLError.createSQLException(Messages.getString("ResultSet.Unsupported_character_encoding____101") + encoding + "'.", "0S100", this.exceptionInterceptor);
      }
    } else {
      stringVal = StringUtils.toAsciiString(value, offset, length);
    }
    return stringVal;
  }
  
  /* Error */
  protected Time getTimeFast(int columnIndex, byte[] timeAsBytes, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
    throws SQLException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 10
    //   3: iconst_0
    //   4: istore 11
    //   6: iconst_0
    //   7: istore 12
    //   9: aload_2
    //   10: ifnonnull +5 -> 15
    //   13: aconst_null
    //   14: areturn
    //   15: iconst_1
    //   16: istore 13
    //   18: iconst_0
    //   19: istore 14
    //   21: iconst_0
    //   22: istore 15
    //   24: iload 15
    //   26: iload 4
    //   28: if_icmpge +26 -> 54
    //   31: aload_2
    //   32: iload_3
    //   33: iload 15
    //   35: iadd
    //   36: baload
    //   37: bipush 58
    //   39: if_icmpne +9 -> 48
    //   42: iconst_1
    //   43: istore 14
    //   45: goto +9 -> 54
    //   48: iinc 15 1
    //   51: goto -27 -> 24
    //   54: iconst_0
    //   55: istore 15
    //   57: iload 15
    //   59: iload 4
    //   61: if_icmpge +89 -> 150
    //   64: aload_2
    //   65: iload_3
    //   66: iload 15
    //   68: iadd
    //   69: baload
    //   70: istore 16
    //   72: iload 16
    //   74: bipush 32
    //   76: if_icmpeq +17 -> 93
    //   79: iload 16
    //   81: bipush 45
    //   83: if_icmpeq +10 -> 93
    //   86: iload 16
    //   88: bipush 47
    //   90: if_icmpne +6 -> 96
    //   93: iconst_0
    //   94: istore 14
    //   96: iload 16
    //   98: bipush 48
    //   100: if_icmpeq +44 -> 144
    //   103: iload 16
    //   105: bipush 32
    //   107: if_icmpeq +37 -> 144
    //   110: iload 16
    //   112: bipush 58
    //   114: if_icmpeq +30 -> 144
    //   117: iload 16
    //   119: bipush 45
    //   121: if_icmpeq +23 -> 144
    //   124: iload 16
    //   126: bipush 47
    //   128: if_icmpeq +16 -> 144
    //   131: iload 16
    //   133: bipush 46
    //   135: if_icmpeq +9 -> 144
    //   138: iconst_0
    //   139: istore 13
    //   141: goto +9 -> 150
    //   144: iinc 15 1
    //   147: goto -90 -> 57
    //   150: iload 14
    //   152: ifne +93 -> 245
    //   155: iload 13
    //   157: ifeq +88 -> 245
    //   160: ldc 31
    //   162: aload 8
    //   164: invokeinterface 37 1 0
    //   169: invokevirtual 43	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   172: ifeq +5 -> 177
    //   175: aconst_null
    //   176: areturn
    //   177: ldc 45
    //   179: aload 8
    //   181: invokeinterface 37 1 0
    //   186: invokevirtual 43	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   189: ifeq +45 -> 234
    //   192: new 47	java/lang/StringBuilder
    //   195: dup
    //   196: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   199: ldc 50
    //   201: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: new 39	java/lang/String
    //   207: dup
    //   208: aload_2
    //   209: invokespecial 57	java/lang/String:<init>	([B)V
    //   212: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: ldc_w 325
    //   218: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   224: ldc 64
    //   226: aload_0
    //   227: getfield 16	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
    //   230: invokestatic 70	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   233: athrow
    //   234: aload 9
    //   236: aload 5
    //   238: iconst_0
    //   239: iconst_0
    //   240: iconst_0
    //   241: invokevirtual 328	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
    //   244: areturn
    //   245: aload_0
    //   246: getfield 78	com/mysql/jdbc/ResultSetRow:metadata	[Lcom/mysql/jdbc/Field;
    //   249: iload_1
    //   250: aaload
    //   251: astore 15
    //   253: aload 15
    //   255: invokevirtual 84	com/mysql/jdbc/Field:getMysqlType	()I
    //   258: bipush 7
    //   260: if_icmpne +311 -> 571
    //   263: iload 4
    //   265: tableswitch	default:+202->467, 10:+168->433, 11:+202->467, 12:+112->377, 13:+202->467, 14:+112->377, 15:+202->467, 16:+202->467, 17:+202->467, 18:+202->467, 19:+55->320
    //   320: aload_2
    //   321: iload_3
    //   322: iload 4
    //   324: iadd
    //   325: bipush 8
    //   327: isub
    //   328: iload_3
    //   329: iload 4
    //   331: iadd
    //   332: bipush 6
    //   334: isub
    //   335: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   338: istore 10
    //   340: aload_2
    //   341: iload_3
    //   342: iload 4
    //   344: iadd
    //   345: iconst_5
    //   346: isub
    //   347: iload_3
    //   348: iload 4
    //   350: iadd
    //   351: iconst_3
    //   352: isub
    //   353: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   356: istore 11
    //   358: aload_2
    //   359: iload_3
    //   360: iload 4
    //   362: iadd
    //   363: iconst_2
    //   364: isub
    //   365: iload_3
    //   366: iload 4
    //   368: iadd
    //   369: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   372: istore 12
    //   374: goto +145 -> 519
    //   377: aload_2
    //   378: iload_3
    //   379: iload 4
    //   381: iadd
    //   382: bipush 6
    //   384: isub
    //   385: iload_3
    //   386: iload 4
    //   388: iadd
    //   389: iconst_4
    //   390: isub
    //   391: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   394: istore 10
    //   396: aload_2
    //   397: iload_3
    //   398: iload 4
    //   400: iadd
    //   401: iconst_4
    //   402: isub
    //   403: iload_3
    //   404: iload 4
    //   406: iadd
    //   407: iconst_2
    //   408: isub
    //   409: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   412: istore 11
    //   414: aload_2
    //   415: iload_3
    //   416: iload 4
    //   418: iadd
    //   419: iconst_2
    //   420: isub
    //   421: iload_3
    //   422: iload 4
    //   424: iadd
    //   425: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   428: istore 12
    //   430: goto +89 -> 519
    //   433: aload_2
    //   434: iload_3
    //   435: bipush 6
    //   437: iadd
    //   438: iload_3
    //   439: bipush 8
    //   441: iadd
    //   442: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   445: istore 10
    //   447: aload_2
    //   448: iload_3
    //   449: bipush 8
    //   451: iadd
    //   452: iload_3
    //   453: bipush 10
    //   455: iadd
    //   456: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   459: istore 11
    //   461: iconst_0
    //   462: istore 12
    //   464: goto +55 -> 519
    //   467: new 47	java/lang/StringBuilder
    //   470: dup
    //   471: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   474: ldc_w 330
    //   477: invokestatic 307	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   480: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   483: iload_1
    //   484: iconst_1
    //   485: iadd
    //   486: invokevirtual 333	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   489: ldc_w 335
    //   492: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   495: aload 15
    //   497: invokevirtual 338	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   500: ldc_w 340
    //   503: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   506: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   509: ldc 64
    //   511: aload_0
    //   512: getfield 16	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
    //   515: invokestatic 70	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   518: athrow
    //   519: new 342	java/sql/SQLWarning
    //   522: dup
    //   523: new 47	java/lang/StringBuilder
    //   526: dup
    //   527: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   530: ldc_w 344
    //   533: invokestatic 307	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   536: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   539: iload_1
    //   540: invokevirtual 333	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   543: ldc_w 335
    //   546: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   549: aload 15
    //   551: invokevirtual 338	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   554: ldc_w 340
    //   557: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   560: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   563: invokespecial 347	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
    //   566: astore 16
    //   568: goto +245 -> 813
    //   571: aload 15
    //   573: invokevirtual 84	com/mysql/jdbc/Field:getMysqlType	()I
    //   576: bipush 12
    //   578: if_icmpne +99 -> 677
    //   581: aload_2
    //   582: iload_3
    //   583: bipush 11
    //   585: iadd
    //   586: iload_3
    //   587: bipush 13
    //   589: iadd
    //   590: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   593: istore 10
    //   595: aload_2
    //   596: iload_3
    //   597: bipush 14
    //   599: iadd
    //   600: iload_3
    //   601: bipush 16
    //   603: iadd
    //   604: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   607: istore 11
    //   609: aload_2
    //   610: iload_3
    //   611: bipush 17
    //   613: iadd
    //   614: iload_3
    //   615: bipush 19
    //   617: iadd
    //   618: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   621: istore 12
    //   623: new 342	java/sql/SQLWarning
    //   626: dup
    //   627: new 47	java/lang/StringBuilder
    //   630: dup
    //   631: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   634: ldc_w 349
    //   637: invokestatic 307	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   640: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: iload_1
    //   644: iconst_1
    //   645: iadd
    //   646: invokevirtual 333	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   649: ldc_w 335
    //   652: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   655: aload 15
    //   657: invokevirtual 338	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   660: ldc_w 340
    //   663: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   666: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   669: invokespecial 347	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
    //   672: astore 16
    //   674: goto +139 -> 813
    //   677: aload 15
    //   679: invokevirtual 84	com/mysql/jdbc/Field:getMysqlType	()I
    //   682: bipush 10
    //   684: if_icmpne +13 -> 697
    //   687: aload 9
    //   689: aconst_null
    //   690: iconst_0
    //   691: iconst_0
    //   692: iconst_0
    //   693: invokevirtual 328	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
    //   696: areturn
    //   697: iload 4
    //   699: iconst_5
    //   700: if_icmpeq +65 -> 765
    //   703: iload 4
    //   705: bipush 8
    //   707: if_icmpeq +58 -> 765
    //   710: new 47	java/lang/StringBuilder
    //   713: dup
    //   714: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   717: ldc_w 351
    //   720: invokestatic 307	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   723: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   726: new 39	java/lang/String
    //   729: dup
    //   730: aload_2
    //   731: invokespecial 57	java/lang/String:<init>	([B)V
    //   734: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   737: ldc_w 353
    //   740: invokestatic 307	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   743: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   746: iload_1
    //   747: iconst_1
    //   748: iadd
    //   749: invokevirtual 333	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   752: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   755: ldc 64
    //   757: aload_0
    //   758: getfield 16	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
    //   761: invokestatic 70	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   764: athrow
    //   765: aload_2
    //   766: iload_3
    //   767: iconst_0
    //   768: iadd
    //   769: iload_3
    //   770: iconst_2
    //   771: iadd
    //   772: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   775: istore 10
    //   777: aload_2
    //   778: iload_3
    //   779: iconst_3
    //   780: iadd
    //   781: iload_3
    //   782: iconst_5
    //   783: iadd
    //   784: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   787: istore 11
    //   789: iload 4
    //   791: iconst_5
    //   792: if_icmpne +7 -> 799
    //   795: iconst_0
    //   796: goto +15 -> 811
    //   799: aload_2
    //   800: iload_3
    //   801: bipush 6
    //   803: iadd
    //   804: iload_3
    //   805: bipush 8
    //   807: iadd
    //   808: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   811: istore 12
    //   813: aload 9
    //   815: invokevirtual 175	com/mysql/jdbc/ResultSetImpl:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
    //   818: astore 16
    //   820: aload 9
    //   822: getfield 166	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
    //   825: ifne +17 -> 842
    //   828: aload 9
    //   830: aload 5
    //   832: iload 10
    //   834: iload 11
    //   836: iload 12
    //   838: invokevirtual 328	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
    //   841: areturn
    //   842: aload 16
    //   844: dup
    //   845: astore 17
    //   847: monitorenter
    //   848: aload 8
    //   850: aload 16
    //   852: aload 5
    //   854: aload 9
    //   856: aload 16
    //   858: iload 10
    //   860: iload 11
    //   862: iload 12
    //   864: invokevirtual 328	com/mysql/jdbc/ResultSetImpl:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
    //   867: aload 8
    //   869: invokeinterface 197 1 0
    //   874: aload 6
    //   876: iload 7
    //   878: invokestatic 201	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Time;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Time;
    //   881: aload 17
    //   883: monitorexit
    //   884: areturn
    //   885: astore 18
    //   887: aload 17
    //   889: monitorexit
    //   890: aload 18
    //   892: athrow
    //   893: astore 13
    //   895: aload 13
    //   897: invokevirtual 354	java/lang/Exception:toString	()Ljava/lang/String;
    //   900: ldc 64
    //   902: aload_0
    //   903: getfield 16	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
    //   906: invokestatic 70	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   909: astore 14
    //   911: aload 14
    //   913: aload 13
    //   915: invokevirtual 129	java/sql/SQLException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   918: pop
    //   919: aload 14
    //   921: athrow
    // Line number table:
    //   Java source line #819	-> byte code offset #0
    //   Java source line #820	-> byte code offset #3
    //   Java source line #821	-> byte code offset #6
    //   Java source line #825	-> byte code offset #9
    //   Java source line #826	-> byte code offset #13
    //   Java source line #829	-> byte code offset #15
    //   Java source line #830	-> byte code offset #18
    //   Java source line #832	-> byte code offset #21
    //   Java source line #833	-> byte code offset #31
    //   Java source line #834	-> byte code offset #42
    //   Java source line #835	-> byte code offset #45
    //   Java source line #832	-> byte code offset #48
    //   Java source line #839	-> byte code offset #54
    //   Java source line #840	-> byte code offset #64
    //   Java source line #842	-> byte code offset #72
    //   Java source line #843	-> byte code offset #93
    //   Java source line #846	-> byte code offset #96
    //   Java source line #848	-> byte code offset #138
    //   Java source line #850	-> byte code offset #141
    //   Java source line #839	-> byte code offset #144
    //   Java source line #854	-> byte code offset #150
    //   Java source line #855	-> byte code offset #160
    //   Java source line #857	-> byte code offset #175
    //   Java source line #858	-> byte code offset #177
    //   Java source line #860	-> byte code offset #192
    //   Java source line #868	-> byte code offset #234
    //   Java source line #871	-> byte code offset #245
    //   Java source line #873	-> byte code offset #253
    //   Java source line #875	-> byte code offset #263
    //   Java source line #878	-> byte code offset #320
    //   Java source line #880	-> byte code offset #340
    //   Java source line #882	-> byte code offset #358
    //   Java source line #886	-> byte code offset #374
    //   Java source line #889	-> byte code offset #377
    //   Java source line #891	-> byte code offset #396
    //   Java source line #893	-> byte code offset #414
    //   Java source line #897	-> byte code offset #430
    //   Java source line #900	-> byte code offset #433
    //   Java source line #902	-> byte code offset #447
    //   Java source line #904	-> byte code offset #461
    //   Java source line #907	-> byte code offset #464
    //   Java source line #910	-> byte code offset #467
    //   Java source line #920	-> byte code offset #519
    //   Java source line #929	-> byte code offset #568
    //   Java source line #930	-> byte code offset #581
    //   Java source line #931	-> byte code offset #595
    //   Java source line #932	-> byte code offset #609
    //   Java source line #934	-> byte code offset #623
    //   Java source line #944	-> byte code offset #674
    //   Java source line #945	-> byte code offset #687
    //   Java source line #950	-> byte code offset #697
    //   Java source line #951	-> byte code offset #710
    //   Java source line #959	-> byte code offset #765
    //   Java source line #960	-> byte code offset #777
    //   Java source line #961	-> byte code offset #789
    //   Java source line #965	-> byte code offset #813
    //   Java source line #967	-> byte code offset #820
    //   Java source line #968	-> byte code offset #828
    //   Java source line #971	-> byte code offset #842
    //   Java source line #972	-> byte code offset #848
    //   Java source line #976	-> byte code offset #885
    //   Java source line #977	-> byte code offset #893
    //   Java source line #978	-> byte code offset #895
    //   Java source line #980	-> byte code offset #911
    //   Java source line #982	-> byte code offset #919
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	922	0	this	ResultSetRow
    //   0	922	1	columnIndex	int
    //   0	922	2	timeAsBytes	byte[]
    //   0	922	3	offset	int
    //   0	922	4	length	int
    //   0	922	5	targetCalendar	Calendar
    //   0	922	6	tz	TimeZone
    //   0	922	7	rollForward	boolean
    //   0	922	8	conn	MySQLConnection
    //   0	922	9	rs	ResultSetImpl
    //   1	858	10	hr	int
    //   4	857	11	min	int
    //   7	856	12	sec	int
    //   16	140	13	allZeroTime	boolean
    //   893	21	13	ex	Exception
    //   19	132	14	onlyTimePresent	boolean
    //   909	11	14	sqlEx	SQLException
    //   22	27	15	i	int
    //   55	90	15	i	int
    //   251	427	15	timeColField	Field
    //   70	62	16	b	byte
    //   566	3	16	precisionLost	java.sql.SQLWarning
    //   672	3	16	precisionLost	java.sql.SQLWarning
    //   818	39	16	sessionCalendar	Calendar
    //   845	43	17	Ljava/lang/Object;	Object
    //   885	6	18	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   848	884	885	finally
    //   885	890	885	finally
    //   9	14	893	java/lang/Exception
    //   15	176	893	java/lang/Exception
    //   177	244	893	java/lang/Exception
    //   245	696	893	java/lang/Exception
    //   697	841	893	java/lang/Exception
    //   842	884	893	java/lang/Exception
    //   885	893	893	java/lang/Exception
  }
  
  public abstract Time getTimeFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
    throws SQLException;
  
  /* Error */
  protected Timestamp getTimestampFast(int columnIndex, byte[] timestampAsBytes, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
    throws SQLException
  {
    // Byte code:
    //   0: aload 8
    //   2: invokeinterface 183 1 0
    //   7: ifeq +13 -> 20
    //   10: aload 8
    //   12: invokeinterface 186 1 0
    //   17: goto +8 -> 25
    //   20: aload 9
    //   22: invokevirtual 175	com/mysql/jdbc/ResultSetImpl:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
    //   25: astore 10
    //   27: aload 10
    //   29: dup
    //   30: astore 11
    //   32: monitorenter
    //   33: iconst_1
    //   34: istore 12
    //   36: iconst_0
    //   37: istore 13
    //   39: iconst_0
    //   40: istore 14
    //   42: iload 14
    //   44: iload 4
    //   46: if_icmpge +26 -> 72
    //   49: aload_2
    //   50: iload_3
    //   51: iload 14
    //   53: iadd
    //   54: baload
    //   55: bipush 58
    //   57: if_icmpne +9 -> 66
    //   60: iconst_1
    //   61: istore 13
    //   63: goto +9 -> 72
    //   66: iinc 14 1
    //   69: goto -27 -> 42
    //   72: iconst_0
    //   73: istore 14
    //   75: iload 14
    //   77: iload 4
    //   79: if_icmpge +89 -> 168
    //   82: aload_2
    //   83: iload_3
    //   84: iload 14
    //   86: iadd
    //   87: baload
    //   88: istore 15
    //   90: iload 15
    //   92: bipush 32
    //   94: if_icmpeq +17 -> 111
    //   97: iload 15
    //   99: bipush 45
    //   101: if_icmpeq +10 -> 111
    //   104: iload 15
    //   106: bipush 47
    //   108: if_icmpne +6 -> 114
    //   111: iconst_0
    //   112: istore 13
    //   114: iload 15
    //   116: bipush 48
    //   118: if_icmpeq +44 -> 162
    //   121: iload 15
    //   123: bipush 32
    //   125: if_icmpeq +37 -> 162
    //   128: iload 15
    //   130: bipush 58
    //   132: if_icmpeq +30 -> 162
    //   135: iload 15
    //   137: bipush 45
    //   139: if_icmpeq +23 -> 162
    //   142: iload 15
    //   144: bipush 47
    //   146: if_icmpeq +16 -> 162
    //   149: iload 15
    //   151: bipush 46
    //   153: if_icmpeq +9 -> 162
    //   156: iconst_0
    //   157: istore 12
    //   159: goto +9 -> 168
    //   162: iinc 14 1
    //   165: goto -90 -> 75
    //   168: iload 13
    //   170: ifne +119 -> 289
    //   173: iload 12
    //   175: ifeq +114 -> 289
    //   178: ldc 31
    //   180: aload 8
    //   182: invokeinterface 37 1 0
    //   187: invokevirtual 43	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   190: ifeq +8 -> 198
    //   193: aconst_null
    //   194: aload 11
    //   196: monitorexit
    //   197: areturn
    //   198: ldc 45
    //   200: aload 8
    //   202: invokeinterface 37 1 0
    //   207: invokevirtual 43	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   210: ifeq +38 -> 248
    //   213: new 47	java/lang/StringBuilder
    //   216: dup
    //   217: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   220: ldc 50
    //   222: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: aload_2
    //   226: invokevirtual 338	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   229: ldc_w 368
    //   232: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   238: ldc 64
    //   240: aload_0
    //   241: getfield 16	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
    //   244: invokestatic 70	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   247: athrow
    //   248: aload 9
    //   250: getfield 166	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
    //   253: ifne +19 -> 272
    //   256: aload 6
    //   258: iconst_1
    //   259: iconst_1
    //   260: iconst_1
    //   261: iconst_0
    //   262: iconst_0
    //   263: iconst_0
    //   264: iconst_0
    //   265: invokestatic 216	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   268: aload 11
    //   270: monitorexit
    //   271: areturn
    //   272: aload 9
    //   274: aconst_null
    //   275: iconst_1
    //   276: iconst_1
    //   277: iconst_1
    //   278: iconst_0
    //   279: iconst_0
    //   280: iconst_0
    //   281: iconst_0
    //   282: invokevirtual 219	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
    //   285: aload 11
    //   287: monitorexit
    //   288: areturn
    //   289: aload_0
    //   290: getfield 78	com/mysql/jdbc/ResultSetRow:metadata	[Lcom/mysql/jdbc/Field;
    //   293: iload_1
    //   294: aaload
    //   295: invokevirtual 84	com/mysql/jdbc/Field:getMysqlType	()I
    //   298: bipush 13
    //   300: if_icmpne +75 -> 375
    //   303: aload 9
    //   305: getfield 166	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
    //   308: ifne +24 -> 332
    //   311: aload 6
    //   313: aload_2
    //   314: iload_3
    //   315: iconst_4
    //   316: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   319: iconst_1
    //   320: iconst_1
    //   321: iconst_0
    //   322: iconst_0
    //   323: iconst_0
    //   324: iconst_0
    //   325: invokestatic 216	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   328: aload 11
    //   330: monitorexit
    //   331: areturn
    //   332: aload 8
    //   334: aload 10
    //   336: aload 5
    //   338: aload 9
    //   340: aload 10
    //   342: aload_2
    //   343: iload_3
    //   344: iconst_4
    //   345: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   348: iconst_1
    //   349: iconst_1
    //   350: iconst_0
    //   351: iconst_0
    //   352: iconst_0
    //   353: iconst_0
    //   354: invokevirtual 219	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
    //   357: aload 8
    //   359: invokeinterface 197 1 0
    //   364: aload 6
    //   366: iload 7
    //   368: invokestatic 222	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   371: aload 11
    //   373: monitorexit
    //   374: areturn
    //   375: aload_2
    //   376: iload_3
    //   377: iload 4
    //   379: iadd
    //   380: iconst_1
    //   381: isub
    //   382: baload
    //   383: bipush 46
    //   385: if_icmpne +6 -> 391
    //   388: iinc 4 -1
    //   391: iconst_0
    //   392: istore 14
    //   394: iconst_0
    //   395: istore 15
    //   397: iconst_0
    //   398: istore 16
    //   400: iconst_0
    //   401: istore 17
    //   403: iconst_0
    //   404: istore 18
    //   406: iconst_0
    //   407: istore 19
    //   409: iconst_0
    //   410: istore 20
    //   412: iload 4
    //   414: tableswitch	default:+1000->1414, 2:+959->1373, 3:+1000->1414, 4:+915->1329, 5:+1000->1414, 6:+855->1269, 7:+1000->1414, 8:+711->1125, 9:+1000->1414, 10:+520->934, 11:+1000->1414, 12:+418->832, 13:+1000->1414, 14:+334->748, 15:+1000->1414, 16:+1000->1414, 17:+1000->1414, 18:+1000->1414, 19:+126->540, 20:+126->540, 21:+126->540, 22:+126->540, 23:+126->540, 24:+126->540, 25:+126->540, 26:+126->540, 27:+1000->1414, 28:+1000->1414, 29:+126->540
    //   540: aload_2
    //   541: iload_3
    //   542: iconst_0
    //   543: iadd
    //   544: iload_3
    //   545: iconst_4
    //   546: iadd
    //   547: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   550: istore 14
    //   552: aload_2
    //   553: iload_3
    //   554: iconst_5
    //   555: iadd
    //   556: iload_3
    //   557: bipush 7
    //   559: iadd
    //   560: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   563: istore 15
    //   565: aload_2
    //   566: iload_3
    //   567: bipush 8
    //   569: iadd
    //   570: iload_3
    //   571: bipush 10
    //   573: iadd
    //   574: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   577: istore 16
    //   579: aload_2
    //   580: iload_3
    //   581: bipush 11
    //   583: iadd
    //   584: iload_3
    //   585: bipush 13
    //   587: iadd
    //   588: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   591: istore 17
    //   593: aload_2
    //   594: iload_3
    //   595: bipush 14
    //   597: iadd
    //   598: iload_3
    //   599: bipush 16
    //   601: iadd
    //   602: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   605: istore 18
    //   607: aload_2
    //   608: iload_3
    //   609: bipush 17
    //   611: iadd
    //   612: iload_3
    //   613: bipush 19
    //   615: iadd
    //   616: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   619: istore 19
    //   621: iconst_0
    //   622: istore 20
    //   624: iload 4
    //   626: bipush 19
    //   628: if_icmple +841 -> 1469
    //   631: iconst_m1
    //   632: istore 21
    //   634: iconst_0
    //   635: istore 22
    //   637: iload 22
    //   639: iload 4
    //   641: if_icmpge +24 -> 665
    //   644: aload_2
    //   645: iload_3
    //   646: iload 22
    //   648: iadd
    //   649: baload
    //   650: bipush 46
    //   652: if_icmpne +7 -> 659
    //   655: iload 22
    //   657: istore 21
    //   659: iinc 22 1
    //   662: goto -25 -> 637
    //   665: iload 21
    //   667: iconst_m1
    //   668: if_icmpeq +77 -> 745
    //   671: iload 21
    //   673: iconst_2
    //   674: iadd
    //   675: iload 4
    //   677: if_icmpgt +60 -> 737
    //   680: aload_2
    //   681: iload 21
    //   683: iconst_1
    //   684: iadd
    //   685: iload_3
    //   686: iload 4
    //   688: iadd
    //   689: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   692: istore 20
    //   694: iload_3
    //   695: iload 4
    //   697: iadd
    //   698: iload 21
    //   700: iconst_1
    //   701: iadd
    //   702: isub
    //   703: istore 22
    //   705: iload 22
    //   707: bipush 9
    //   709: if_icmpge +25 -> 734
    //   712: ldc2_w 369
    //   715: bipush 9
    //   717: iload 22
    //   719: isub
    //   720: i2d
    //   721: invokestatic 376	java/lang/Math:pow	(DD)D
    //   724: d2i
    //   725: istore 23
    //   727: iload 20
    //   729: iload 23
    //   731: imul
    //   732: istore 20
    //   734: goto +11 -> 745
    //   737: new 378	java/lang/IllegalArgumentException
    //   740: dup
    //   741: invokespecial 379	java/lang/IllegalArgumentException:<init>	()V
    //   744: athrow
    //   745: goto +724 -> 1469
    //   748: aload_2
    //   749: iload_3
    //   750: iconst_0
    //   751: iadd
    //   752: iload_3
    //   753: iconst_4
    //   754: iadd
    //   755: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   758: istore 14
    //   760: aload_2
    //   761: iload_3
    //   762: iconst_4
    //   763: iadd
    //   764: iload_3
    //   765: bipush 6
    //   767: iadd
    //   768: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   771: istore 15
    //   773: aload_2
    //   774: iload_3
    //   775: bipush 6
    //   777: iadd
    //   778: iload_3
    //   779: bipush 8
    //   781: iadd
    //   782: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   785: istore 16
    //   787: aload_2
    //   788: iload_3
    //   789: bipush 8
    //   791: iadd
    //   792: iload_3
    //   793: bipush 10
    //   795: iadd
    //   796: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   799: istore 17
    //   801: aload_2
    //   802: iload_3
    //   803: bipush 10
    //   805: iadd
    //   806: iload_3
    //   807: bipush 12
    //   809: iadd
    //   810: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   813: istore 18
    //   815: aload_2
    //   816: iload_3
    //   817: bipush 12
    //   819: iadd
    //   820: iload_3
    //   821: bipush 14
    //   823: iadd
    //   824: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   827: istore 19
    //   829: goto +640 -> 1469
    //   832: aload_2
    //   833: iload_3
    //   834: iconst_0
    //   835: iadd
    //   836: iload_3
    //   837: iconst_2
    //   838: iadd
    //   839: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   842: istore 14
    //   844: iload 14
    //   846: bipush 69
    //   848: if_icmpgt +10 -> 858
    //   851: iload 14
    //   853: bipush 100
    //   855: iadd
    //   856: istore 14
    //   858: wide
    //   864: aload_2
    //   865: iload_3
    //   866: iconst_2
    //   867: iadd
    //   868: iload_3
    //   869: iconst_4
    //   870: iadd
    //   871: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   874: istore 15
    //   876: aload_2
    //   877: iload_3
    //   878: iconst_4
    //   879: iadd
    //   880: iload_3
    //   881: bipush 6
    //   883: iadd
    //   884: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   887: istore 16
    //   889: aload_2
    //   890: iload_3
    //   891: bipush 6
    //   893: iadd
    //   894: iload_3
    //   895: bipush 8
    //   897: iadd
    //   898: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   901: istore 17
    //   903: aload_2
    //   904: iload_3
    //   905: bipush 8
    //   907: iadd
    //   908: iload_3
    //   909: bipush 10
    //   911: iadd
    //   912: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   915: istore 18
    //   917: aload_2
    //   918: iload_3
    //   919: bipush 10
    //   921: iadd
    //   922: iload_3
    //   923: bipush 12
    //   925: iadd
    //   926: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   929: istore 19
    //   931: goto +538 -> 1469
    //   934: iconst_0
    //   935: istore 21
    //   937: iconst_0
    //   938: istore 22
    //   940: iload 22
    //   942: iload 4
    //   944: if_icmpge +26 -> 970
    //   947: aload_2
    //   948: iload_3
    //   949: iload 22
    //   951: iadd
    //   952: baload
    //   953: bipush 45
    //   955: if_icmpne +9 -> 964
    //   958: iconst_1
    //   959: istore 21
    //   961: goto +9 -> 970
    //   964: iinc 22 1
    //   967: goto -27 -> 940
    //   970: aload_0
    //   971: getfield 78	com/mysql/jdbc/ResultSetRow:metadata	[Lcom/mysql/jdbc/Field;
    //   974: iload_1
    //   975: aaload
    //   976: invokevirtual 84	com/mysql/jdbc/Field:getMysqlType	()I
    //   979: bipush 10
    //   981: if_icmpeq +8 -> 989
    //   984: iload 21
    //   986: ifeq +51 -> 1037
    //   989: aload_2
    //   990: iload_3
    //   991: iconst_0
    //   992: iadd
    //   993: iload_3
    //   994: iconst_4
    //   995: iadd
    //   996: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   999: istore 14
    //   1001: aload_2
    //   1002: iload_3
    //   1003: iconst_5
    //   1004: iadd
    //   1005: iload_3
    //   1006: bipush 7
    //   1008: iadd
    //   1009: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1012: istore 15
    //   1014: aload_2
    //   1015: iload_3
    //   1016: bipush 8
    //   1018: iadd
    //   1019: iload_3
    //   1020: bipush 10
    //   1022: iadd
    //   1023: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1026: istore 16
    //   1028: iconst_0
    //   1029: istore 17
    //   1031: iconst_0
    //   1032: istore 18
    //   1034: goto +435 -> 1469
    //   1037: aload_2
    //   1038: iload_3
    //   1039: iconst_0
    //   1040: iadd
    //   1041: iload_3
    //   1042: iconst_2
    //   1043: iadd
    //   1044: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1047: istore 14
    //   1049: iload 14
    //   1051: bipush 69
    //   1053: if_icmpgt +10 -> 1063
    //   1056: iload 14
    //   1058: bipush 100
    //   1060: iadd
    //   1061: istore 14
    //   1063: aload_2
    //   1064: iload_3
    //   1065: iconst_2
    //   1066: iadd
    //   1067: iload_3
    //   1068: iconst_4
    //   1069: iadd
    //   1070: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1073: istore 15
    //   1075: aload_2
    //   1076: iload_3
    //   1077: iconst_4
    //   1078: iadd
    //   1079: iload_3
    //   1080: bipush 6
    //   1082: iadd
    //   1083: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1086: istore 16
    //   1088: aload_2
    //   1089: iload_3
    //   1090: bipush 6
    //   1092: iadd
    //   1093: iload_3
    //   1094: bipush 8
    //   1096: iadd
    //   1097: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1100: istore 17
    //   1102: aload_2
    //   1103: iload_3
    //   1104: bipush 8
    //   1106: iadd
    //   1107: iload_3
    //   1108: bipush 10
    //   1110: iadd
    //   1111: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1114: istore 18
    //   1116: wide
    //   1122: goto +347 -> 1469
    //   1125: iconst_0
    //   1126: istore 21
    //   1128: iconst_0
    //   1129: istore 22
    //   1131: iload 22
    //   1133: iload 4
    //   1135: if_icmpge +26 -> 1161
    //   1138: aload_2
    //   1139: iload_3
    //   1140: iload 22
    //   1142: iadd
    //   1143: baload
    //   1144: bipush 58
    //   1146: if_icmpne +9 -> 1155
    //   1149: iconst_1
    //   1150: istore 21
    //   1152: goto +9 -> 1161
    //   1155: iinc 22 1
    //   1158: goto -27 -> 1131
    //   1161: iload 21
    //   1163: ifeq +55 -> 1218
    //   1166: aload_2
    //   1167: iload_3
    //   1168: iconst_0
    //   1169: iadd
    //   1170: iload_3
    //   1171: iconst_2
    //   1172: iadd
    //   1173: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1176: istore 17
    //   1178: aload_2
    //   1179: iload_3
    //   1180: iconst_3
    //   1181: iadd
    //   1182: iload_3
    //   1183: iconst_5
    //   1184: iadd
    //   1185: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1188: istore 18
    //   1190: aload_2
    //   1191: iload_3
    //   1192: bipush 6
    //   1194: iadd
    //   1195: iload_3
    //   1196: bipush 8
    //   1198: iadd
    //   1199: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1202: istore 19
    //   1204: sipush 1970
    //   1207: istore 14
    //   1209: iconst_1
    //   1210: istore 15
    //   1212: iconst_1
    //   1213: istore 16
    //   1215: goto +254 -> 1469
    //   1218: aload_2
    //   1219: iload_3
    //   1220: iconst_0
    //   1221: iadd
    //   1222: iload_3
    //   1223: iconst_4
    //   1224: iadd
    //   1225: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1228: istore 14
    //   1230: aload_2
    //   1231: iload_3
    //   1232: iconst_4
    //   1233: iadd
    //   1234: iload_3
    //   1235: bipush 6
    //   1237: iadd
    //   1238: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1241: istore 15
    //   1243: aload_2
    //   1244: iload_3
    //   1245: bipush 6
    //   1247: iadd
    //   1248: iload_3
    //   1249: bipush 8
    //   1251: iadd
    //   1252: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1255: istore 16
    //   1257: wide
    //   1263: iinc 15 -1
    //   1266: goto +203 -> 1469
    //   1269: aload_2
    //   1270: iload_3
    //   1271: iconst_0
    //   1272: iadd
    //   1273: iload_3
    //   1274: iconst_2
    //   1275: iadd
    //   1276: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1279: istore 14
    //   1281: iload 14
    //   1283: bipush 69
    //   1285: if_icmpgt +10 -> 1295
    //   1288: iload 14
    //   1290: bipush 100
    //   1292: iadd
    //   1293: istore 14
    //   1295: wide
    //   1301: aload_2
    //   1302: iload_3
    //   1303: iconst_2
    //   1304: iadd
    //   1305: iload_3
    //   1306: iconst_4
    //   1307: iadd
    //   1308: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1311: istore 15
    //   1313: aload_2
    //   1314: iload_3
    //   1315: iconst_4
    //   1316: iadd
    //   1317: iload_3
    //   1318: bipush 6
    //   1320: iadd
    //   1321: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1324: istore 16
    //   1326: goto +143 -> 1469
    //   1329: aload_2
    //   1330: iload_3
    //   1331: iconst_0
    //   1332: iadd
    //   1333: iload_3
    //   1334: iconst_2
    //   1335: iadd
    //   1336: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1339: istore 14
    //   1341: iload 14
    //   1343: bipush 69
    //   1345: if_icmpgt +10 -> 1355
    //   1348: iload 14
    //   1350: bipush 100
    //   1352: iadd
    //   1353: istore 14
    //   1355: aload_2
    //   1356: iload_3
    //   1357: iconst_2
    //   1358: iadd
    //   1359: iload_3
    //   1360: iconst_4
    //   1361: iadd
    //   1362: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1365: istore 15
    //   1367: iconst_1
    //   1368: istore 16
    //   1370: goto +99 -> 1469
    //   1373: aload_2
    //   1374: iload_3
    //   1375: iconst_0
    //   1376: iadd
    //   1377: iload_3
    //   1378: iconst_2
    //   1379: iadd
    //   1380: invokestatic 90	com/mysql/jdbc/StringUtils:getInt	([BII)I
    //   1383: istore 14
    //   1385: iload 14
    //   1387: bipush 69
    //   1389: if_icmpgt +10 -> 1399
    //   1392: iload 14
    //   1394: bipush 100
    //   1396: iadd
    //   1397: istore 14
    //   1399: wide
    //   1405: iconst_1
    //   1406: istore 15
    //   1408: iconst_1
    //   1409: istore 16
    //   1411: goto +58 -> 1469
    //   1414: new 23	java/sql/SQLException
    //   1417: dup
    //   1418: new 47	java/lang/StringBuilder
    //   1421: dup
    //   1422: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   1425: ldc_w 381
    //   1428: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1431: new 39	java/lang/String
    //   1434: dup
    //   1435: aload_2
    //   1436: invokespecial 57	java/lang/String:<init>	([B)V
    //   1439: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1442: ldc_w 383
    //   1445: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1448: iload_1
    //   1449: iconst_1
    //   1450: iadd
    //   1451: invokevirtual 333	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1454: ldc_w 385
    //   1457: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1460: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1463: ldc 64
    //   1465: invokespecial 206	java/sql/SQLException:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   1468: athrow
    //   1469: aload 9
    //   1471: getfield 166	com/mysql/jdbc/ResultSetImpl:useLegacyDatetimeCode	Z
    //   1474: ifne +26 -> 1500
    //   1477: aload 6
    //   1479: iload 14
    //   1481: iload 15
    //   1483: iload 16
    //   1485: iload 17
    //   1487: iload 18
    //   1489: iload 19
    //   1491: iload 20
    //   1493: invokestatic 216	com/mysql/jdbc/TimeUtil:fastTimestampCreate	(Ljava/util/TimeZone;IIIIIII)Ljava/sql/Timestamp;
    //   1496: aload 11
    //   1498: monitorexit
    //   1499: areturn
    //   1500: aload 8
    //   1502: aload 10
    //   1504: aload 5
    //   1506: aload 9
    //   1508: aload 10
    //   1510: iload 14
    //   1512: iload 15
    //   1514: iload 16
    //   1516: iload 17
    //   1518: iload 18
    //   1520: iload 19
    //   1522: iload 20
    //   1524: invokevirtual 219	com/mysql/jdbc/ResultSetImpl:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
    //   1527: aload 8
    //   1529: invokeinterface 197 1 0
    //   1534: aload 6
    //   1536: iload 7
    //   1538: invokestatic 222	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/MySQLConnection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
    //   1541: aload 11
    //   1543: monitorexit
    //   1544: areturn
    //   1545: astore 24
    //   1547: aload 11
    //   1549: monitorexit
    //   1550: aload 24
    //   1552: athrow
    //   1553: astore 10
    //   1555: new 47	java/lang/StringBuilder
    //   1558: dup
    //   1559: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   1562: ldc_w 387
    //   1565: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1568: aload_0
    //   1569: iload_1
    //   1570: ldc 108
    //   1572: aload 8
    //   1574: invokevirtual 389	com/mysql/jdbc/ResultSetRow:getString	(ILjava/lang/String;Lcom/mysql/jdbc/MySQLConnection;)Ljava/lang/String;
    //   1577: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1580: ldc_w 391
    //   1583: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1586: iload_1
    //   1587: iconst_1
    //   1588: iadd
    //   1589: invokevirtual 333	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1592: ldc_w 393
    //   1595: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1598: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1601: ldc 64
    //   1603: aload_0
    //   1604: getfield 16	com/mysql/jdbc/ResultSetRow:exceptionInterceptor	Lcom/mysql/jdbc/ExceptionInterceptor;
    //   1607: invokestatic 70	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
    //   1610: astore 11
    //   1612: aload 11
    //   1614: aload 10
    //   1616: invokevirtual 129	java/sql/SQLException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1619: pop
    //   1620: aload 11
    //   1622: athrow
    // Line number table:
    //   Java source line #996	-> byte code offset #0
    //   Java source line #1000	-> byte code offset #27
    //   Java source line #1001	-> byte code offset #33
    //   Java source line #1003	-> byte code offset #36
    //   Java source line #1005	-> byte code offset #39
    //   Java source line #1006	-> byte code offset #49
    //   Java source line #1007	-> byte code offset #60
    //   Java source line #1008	-> byte code offset #63
    //   Java source line #1005	-> byte code offset #66
    //   Java source line #1012	-> byte code offset #72
    //   Java source line #1013	-> byte code offset #82
    //   Java source line #1015	-> byte code offset #90
    //   Java source line #1016	-> byte code offset #111
    //   Java source line #1019	-> byte code offset #114
    //   Java source line #1021	-> byte code offset #156
    //   Java source line #1023	-> byte code offset #159
    //   Java source line #1012	-> byte code offset #162
    //   Java source line #1027	-> byte code offset #168
    //   Java source line #1029	-> byte code offset #178
    //   Java source line #1032	-> byte code offset #193
    //   Java source line #1033	-> byte code offset #198
    //   Java source line #1035	-> byte code offset #213
    //   Java source line #1043	-> byte code offset #248
    //   Java source line #1044	-> byte code offset #256
    //   Java source line #1048	-> byte code offset #272
    //   Java source line #1050	-> byte code offset #289
    //   Java source line #1052	-> byte code offset #303
    //   Java source line #1053	-> byte code offset #311
    //   Java source line #1058	-> byte code offset #332
    //   Java source line #1065	-> byte code offset #375
    //   Java source line #1066	-> byte code offset #388
    //   Java source line #1071	-> byte code offset #391
    //   Java source line #1072	-> byte code offset #394
    //   Java source line #1073	-> byte code offset #397
    //   Java source line #1074	-> byte code offset #400
    //   Java source line #1075	-> byte code offset #403
    //   Java source line #1076	-> byte code offset #406
    //   Java source line #1077	-> byte code offset #409
    //   Java source line #1079	-> byte code offset #412
    //   Java source line #1089	-> byte code offset #540
    //   Java source line #1091	-> byte code offset #552
    //   Java source line #1093	-> byte code offset #565
    //   Java source line #1095	-> byte code offset #579
    //   Java source line #1097	-> byte code offset #593
    //   Java source line #1099	-> byte code offset #607
    //   Java source line #1102	-> byte code offset #621
    //   Java source line #1104	-> byte code offset #624
    //   Java source line #1105	-> byte code offset #631
    //   Java source line #1107	-> byte code offset #634
    //   Java source line #1108	-> byte code offset #644
    //   Java source line #1109	-> byte code offset #655
    //   Java source line #1107	-> byte code offset #659
    //   Java source line #1113	-> byte code offset #665
    //   Java source line #1114	-> byte code offset #671
    //   Java source line #1115	-> byte code offset #680
    //   Java source line #1119	-> byte code offset #694
    //   Java source line #1121	-> byte code offset #705
    //   Java source line #1122	-> byte code offset #712
    //   Java source line #1123	-> byte code offset #727
    //   Java source line #1125	-> byte code offset #734
    //   Java source line #1126	-> byte code offset #737
    //   Java source line #1134	-> byte code offset #745
    //   Java source line #1140	-> byte code offset #748
    //   Java source line #1142	-> byte code offset #760
    //   Java source line #1144	-> byte code offset #773
    //   Java source line #1146	-> byte code offset #787
    //   Java source line #1148	-> byte code offset #801
    //   Java source line #1150	-> byte code offset #815
    //   Java source line #1153	-> byte code offset #829
    //   Java source line #1157	-> byte code offset #832
    //   Java source line #1160	-> byte code offset #844
    //   Java source line #1161	-> byte code offset #851
    //   Java source line #1164	-> byte code offset #858
    //   Java source line #1166	-> byte code offset #864
    //   Java source line #1168	-> byte code offset #876
    //   Java source line #1170	-> byte code offset #889
    //   Java source line #1172	-> byte code offset #903
    //   Java source line #1174	-> byte code offset #917
    //   Java source line #1177	-> byte code offset #931
    //   Java source line #1181	-> byte code offset #934
    //   Java source line #1183	-> byte code offset #937
    //   Java source line #1184	-> byte code offset #947
    //   Java source line #1185	-> byte code offset #958
    //   Java source line #1186	-> byte code offset #961
    //   Java source line #1183	-> byte code offset #964
    //   Java source line #1190	-> byte code offset #970
    //   Java source line #1192	-> byte code offset #989
    //   Java source line #1194	-> byte code offset #1001
    //   Java source line #1196	-> byte code offset #1014
    //   Java source line #1198	-> byte code offset #1028
    //   Java source line #1199	-> byte code offset #1031
    //   Java source line #1201	-> byte code offset #1037
    //   Java source line #1204	-> byte code offset #1049
    //   Java source line #1205	-> byte code offset #1056
    //   Java source line #1208	-> byte code offset #1063
    //   Java source line #1210	-> byte code offset #1075
    //   Java source line #1212	-> byte code offset #1088
    //   Java source line #1214	-> byte code offset #1102
    //   Java source line #1217	-> byte code offset #1116
    //   Java source line #1220	-> byte code offset #1122
    //   Java source line #1224	-> byte code offset #1125
    //   Java source line #1226	-> byte code offset #1128
    //   Java source line #1227	-> byte code offset #1138
    //   Java source line #1228	-> byte code offset #1149
    //   Java source line #1229	-> byte code offset #1152
    //   Java source line #1226	-> byte code offset #1155
    //   Java source line #1233	-> byte code offset #1161
    //   Java source line #1234	-> byte code offset #1166
    //   Java source line #1236	-> byte code offset #1178
    //   Java source line #1238	-> byte code offset #1190
    //   Java source line #1241	-> byte code offset #1204
    //   Java source line #1242	-> byte code offset #1209
    //   Java source line #1243	-> byte code offset #1212
    //   Java source line #1245	-> byte code offset #1215
    //   Java source line #1248	-> byte code offset #1218
    //   Java source line #1250	-> byte code offset #1230
    //   Java source line #1252	-> byte code offset #1243
    //   Java source line #1255	-> byte code offset #1257
    //   Java source line #1256	-> byte code offset #1263
    //   Java source line #1258	-> byte code offset #1266
    //   Java source line #1262	-> byte code offset #1269
    //   Java source line #1265	-> byte code offset #1281
    //   Java source line #1266	-> byte code offset #1288
    //   Java source line #1269	-> byte code offset #1295
    //   Java source line #1271	-> byte code offset #1301
    //   Java source line #1273	-> byte code offset #1313
    //   Java source line #1276	-> byte code offset #1326
    //   Java source line #1280	-> byte code offset #1329
    //   Java source line #1283	-> byte code offset #1341
    //   Java source line #1284	-> byte code offset #1348
    //   Java source line #1287	-> byte code offset #1355
    //   Java source line #1290	-> byte code offset #1367
    //   Java source line #1292	-> byte code offset #1370
    //   Java source line #1296	-> byte code offset #1373
    //   Java source line #1299	-> byte code offset #1385
    //   Java source line #1300	-> byte code offset #1392
    //   Java source line #1303	-> byte code offset #1399
    //   Java source line #1304	-> byte code offset #1405
    //   Java source line #1305	-> byte code offset #1408
    //   Java source line #1307	-> byte code offset #1411
    //   Java source line #1311	-> byte code offset #1414
    //   Java source line #1319	-> byte code offset #1469
    //   Java source line #1320	-> byte code offset #1477
    //   Java source line #1326	-> byte code offset #1500
    //   Java source line #1335	-> byte code offset #1545
    //   Java source line #1336	-> byte code offset #1553
    //   Java source line #1337	-> byte code offset #1555
    //   Java source line #1341	-> byte code offset #1612
    //   Java source line #1343	-> byte code offset #1620
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1623	0	this	ResultSetRow
    //   0	1623	1	columnIndex	int
    //   0	1623	2	timestampAsBytes	byte[]
    //   0	1623	3	offset	int
    //   0	1623	4	length	int
    //   0	1623	5	targetCalendar	Calendar
    //   0	1623	6	tz	TimeZone
    //   0	1623	7	rollForward	boolean
    //   0	1623	8	conn	MySQLConnection
    //   0	1623	9	rs	ResultSetImpl
    //   25	1484	10	sessionCalendar	Calendar
    //   1553	62	10	e	Exception
    //   1610	11	11	sqlEx	SQLException
    //   34	140	12	allZeroTimestamp	boolean
    //   37	132	13	onlyTimePresent	boolean
    //   40	27	14	i	int
    //   73	90	14	i	int
    //   392	1119	14	year	int
    //   88	62	15	b	byte
    //   395	1118	15	month	int
    //   398	1117	16	day	int
    //   401	1116	17	hour	int
    //   404	1115	18	minutes	int
    //   407	1114	19	seconds	int
    //   410	1113	20	nanos	int
    //   632	67	21	decimalIndex	int
    //   935	50	21	hasDash	boolean
    //   1126	36	21	hasColon	boolean
    //   635	25	22	i	int
    //   703	15	22	numDigits	int
    //   938	27	22	i	int
    //   1129	27	22	i	int
    //   725	5	23	factor	int
    //   1545	6	24	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   33	197	1545	finally
    //   198	271	1545	finally
    //   272	288	1545	finally
    //   289	331	1545	finally
    //   332	374	1545	finally
    //   375	1499	1545	finally
    //   1500	1544	1545	finally
    //   1545	1550	1545	finally
    //   0	197	1553	java/lang/Exception
    //   198	271	1553	java/lang/Exception
    //   272	288	1553	java/lang/Exception
    //   289	331	1553	java/lang/Exception
    //   332	374	1553	java/lang/Exception
    //   375	1499	1553	java/lang/Exception
    //   1500	1544	1553	java/lang/Exception
    //   1545	1553	1553	java/lang/Exception
  }
  
  public abstract Timestamp getTimestampFast(int paramInt, Calendar paramCalendar, TimeZone paramTimeZone, boolean paramBoolean, MySQLConnection paramMySQLConnection, ResultSetImpl paramResultSetImpl)
    throws SQLException;
  
  public abstract boolean isFloatingPointNumber(int paramInt)
    throws SQLException;
  
  public abstract boolean isNull(int paramInt)
    throws SQLException;
  
  public abstract long length(int paramInt)
    throws SQLException;
  
  public abstract void setColumnValue(int paramInt, byte[] paramArrayOfByte)
    throws SQLException;
  
  public ResultSetRow setMetadata(Field[] f)
    throws SQLException
  {
    this.metadata = f;
    
    return this;
  }
  
  public abstract int getBytesSize();
}
