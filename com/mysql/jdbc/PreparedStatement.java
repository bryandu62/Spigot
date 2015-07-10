package com.mysql.jdbc;

import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
import com.mysql.jdbc.exceptions.MySQLTimeoutException;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;

public class PreparedStatement
  extends StatementImpl
  implements java.sql.PreparedStatement
{
  private static final Constructor JDBC_4_PSTMT_2_ARG_CTOR;
  private static final Constructor JDBC_4_PSTMT_3_ARG_CTOR;
  private static final Constructor JDBC_4_PSTMT_4_ARG_CTOR;
  
  static
  {
    if (Util.isJdbc4())
    {
      try
      {
        JDBC_4_PSTMT_2_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class });
        
        JDBC_4_PSTMT_3_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class, String.class });
        
        JDBC_4_PSTMT_4_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class, String.class, ParseInfo.class });
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
    }
    else
    {
      JDBC_4_PSTMT_2_ARG_CTOR = null;
      JDBC_4_PSTMT_3_ARG_CTOR = null;
      JDBC_4_PSTMT_4_ARG_CTOR = null;
    }
  }
  
  class BatchParams
  {
    boolean[] isNull = null;
    boolean[] isStream = null;
    InputStream[] parameterStreams = null;
    byte[][] parameterStrings = (byte[][])null;
    int[] streamLengths = null;
    
    BatchParams(byte[][] strings, InputStream[] streams, boolean[] isStreamFlags, int[] lengths, boolean[] isNullFlags)
    {
      this.parameterStrings = new byte[strings.length][];
      this.parameterStreams = new InputStream[streams.length];
      this.isStream = new boolean[isStreamFlags.length];
      this.streamLengths = new int[lengths.length];
      this.isNull = new boolean[isNullFlags.length];
      System.arraycopy(strings, 0, this.parameterStrings, 0, strings.length);
      
      System.arraycopy(streams, 0, this.parameterStreams, 0, streams.length);
      
      System.arraycopy(isStreamFlags, 0, this.isStream, 0, isStreamFlags.length);
      
      System.arraycopy(lengths, 0, this.streamLengths, 0, lengths.length);
      System.arraycopy(isNullFlags, 0, this.isNull, 0, isNullFlags.length);
    }
  }
  
  class EndPoint
  {
    int begin;
    int end;
    
    EndPoint(int b, int e)
    {
      this.begin = b;
      this.end = e;
    }
  }
  
  class ParseInfo
  {
    char firstStmtChar = '\000';
    boolean foundLimitClause = false;
    boolean foundLoadData = false;
    long lastUsed = 0L;
    int statementLength = 0;
    int statementStartPos = 0;
    boolean canRewriteAsMultiValueInsert = false;
    byte[][] staticSql = (byte[][])null;
    boolean isOnDuplicateKeyUpdate = false;
    int locationOfOnDuplicateKeyUpdate = -1;
    String valuesClause;
    boolean parametersInDuplicateKeyClause = false;
    private ParseInfo batchHead;
    private ParseInfo batchValues;
    private ParseInfo batchODKUClause;
    
    ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter)
      throws SQLException
    {
      this(sql, conn, dbmd, encoding, converter, true);
    }
    
    public ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter, boolean buildRewriteInfo)
      throws SQLException
    {
      try
      {
        if (sql == null) {
          throw SQLError.createSQLException(Messages.getString("PreparedStatement.61"), "S1009", PreparedStatement.this.getExceptionInterceptor());
        }
        this.locationOfOnDuplicateKeyUpdate = PreparedStatement.this.getOnDuplicateKeyLocation(sql);
        this.isOnDuplicateKeyUpdate = (this.locationOfOnDuplicateKeyUpdate != -1);
        
        this.lastUsed = System.currentTimeMillis();
        
        quotedIdentifierString = dbmd.getIdentifierQuoteString();
        
        char quotedIdentifierChar = '\000';
        if ((quotedIdentifierString != null) && (!quotedIdentifierString.equals(" ")) && (quotedIdentifierString.length() > 0)) {
          quotedIdentifierChar = quotedIdentifierString.charAt(0);
        }
        this.statementLength = sql.length();
        
        ArrayList endpointList = new ArrayList();
        boolean inQuotes = false;
        char quoteChar = '\000';
        boolean inQuotedId = false;
        int lastParmEnd = 0;
        
        int stopLookingForLimitClause = this.statementLength - 5;
        
        this.foundLimitClause = false;
        
        boolean noBackslashEscapes = PreparedStatement.this.connection.isNoBackslashEscapesSet();
        
        this.statementStartPos = PreparedStatement.this.findStartOfStatement(sql);
        for (int i = this.statementStartPos; i < this.statementLength; i++)
        {
          char c = sql.charAt(i);
          if ((this.firstStmtChar == 0) && (Character.isLetter(c))) {
            this.firstStmtChar = Character.toUpperCase(c);
          }
          if ((!noBackslashEscapes) && (c == '\\') && (i < this.statementLength - 1))
          {
            i++;
          }
          else
          {
            if ((!inQuotes) && (quotedIdentifierChar != 0) && (c == quotedIdentifierChar)) {
              inQuotedId = !inQuotedId;
            } else if (!inQuotedId) {
              if (inQuotes)
              {
                if (((c == '\'') || (c == '"')) && (c == quoteChar))
                {
                  if ((i < this.statementLength - 1) && (sql.charAt(i + 1) == quoteChar))
                  {
                    i++;
                    continue;
                  }
                  inQuotes = !inQuotes;
                  quoteChar = '\000';
                }
                else if (((c == '\'') || (c == '"')) && (c == quoteChar))
                {
                  inQuotes = !inQuotes;
                  quoteChar = '\000';
                }
              }
              else
              {
                if ((c == '#') || ((c == '-') && (i + 1 < this.statementLength) && (sql.charAt(i + 1) == '-')))
                {
                  int endOfStmt = this.statementLength - 1;
                  for (; i < endOfStmt; i++)
                  {
                    c = sql.charAt(i);
                    if ((c == '\r') || (c == '\n')) {
                      break;
                    }
                  }
                }
                if ((c == '/') && (i + 1 < this.statementLength))
                {
                  char cNext = sql.charAt(i + 1);
                  if (cNext == '*')
                  {
                    i += 2;
                    for (int j = i; j < this.statementLength; j++)
                    {
                      i++;
                      cNext = sql.charAt(j);
                      if ((cNext == '*') && (j + 1 < this.statementLength) && 
                        (sql.charAt(j + 1) == '/'))
                      {
                        i++;
                        if (i >= this.statementLength) {
                          break;
                        }
                        c = sql.charAt(i); break;
                      }
                    }
                  }
                }
                else if ((c == '\'') || (c == '"'))
                {
                  inQuotes = true;
                  quoteChar = c;
                }
              }
            }
            if ((c == '?') && (!inQuotes) && (!inQuotedId))
            {
              endpointList.add(new int[] { lastParmEnd, i });
              lastParmEnd = i + 1;
              if ((this.isOnDuplicateKeyUpdate) && (i > this.locationOfOnDuplicateKeyUpdate)) {
                this.parametersInDuplicateKeyClause = true;
              }
            }
            if ((!inQuotes) && (i < stopLookingForLimitClause) && (
              (c == 'L') || (c == 'l')))
            {
              char posI1 = sql.charAt(i + 1);
              if ((posI1 == 'I') || (posI1 == 'i'))
              {
                char posM = sql.charAt(i + 2);
                if ((posM == 'M') || (posM == 'm'))
                {
                  char posI2 = sql.charAt(i + 3);
                  if ((posI2 == 'I') || (posI2 == 'i'))
                  {
                    char posT = sql.charAt(i + 4);
                    if ((posT == 'T') || (posT == 't')) {
                      this.foundLimitClause = true;
                    }
                  }
                }
              }
            }
          }
        }
        if (this.firstStmtChar == 'L')
        {
          if (StringUtils.startsWithIgnoreCaseAndWs(sql, "LOAD DATA")) {
            this.foundLoadData = true;
          } else {
            this.foundLoadData = false;
          }
        }
        else {
          this.foundLoadData = false;
        }
        endpointList.add(new int[] { lastParmEnd, this.statementLength });
        this.staticSql = new byte[endpointList.size()][];
        char[] asCharArray = sql.toCharArray();
        for (i = 0; i < this.staticSql.length; i++)
        {
          int[] ep = (int[])endpointList.get(i);
          int end = ep[1];
          int begin = ep[0];
          int len = end - begin;
          if (this.foundLoadData)
          {
            String temp = new String(asCharArray, begin, len);
            this.staticSql[i] = temp.getBytes();
          }
          else if (encoding == null)
          {
            byte[] buf = new byte[len];
            for (int j = 0; j < len; j++) {
              buf[j] = ((byte)sql.charAt(begin + j));
            }
            this.staticSql[i] = buf;
          }
          else if (converter != null)
          {
            this.staticSql[i] = StringUtils.getBytes(sql, converter, encoding, PreparedStatement.this.connection.getServerCharacterEncoding(), begin, len, PreparedStatement.this.connection.parserKnowsUnicode(), PreparedStatement.this.getExceptionInterceptor());
          }
          else
          {
            String temp = new String(asCharArray, begin, len);
            
            this.staticSql[i] = StringUtils.getBytes(temp, encoding, PreparedStatement.this.connection.getServerCharacterEncoding(), PreparedStatement.this.connection.parserKnowsUnicode(), conn, PreparedStatement.this.getExceptionInterceptor());
          }
        }
      }
      catch (StringIndexOutOfBoundsException oobEx)
      {
        String quotedIdentifierString;
        SQLException sqlEx = new SQLException("Parse error for " + sql);
        sqlEx.initCause(oobEx);
        
        throw sqlEx;
      }
      if (buildRewriteInfo)
      {
        this.canRewriteAsMultiValueInsert = ((PreparedStatement.canRewrite(sql, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementStartPos)) && (!this.parametersInDuplicateKeyClause));
        if ((this.canRewriteAsMultiValueInsert) && (conn.getRewriteBatchedStatements())) {
          buildRewriteBatchedParams(sql, conn, dbmd, encoding, converter);
        }
      }
    }
    
    private void buildRewriteBatchedParams(String sql, MySQLConnection conn, DatabaseMetaData metadata, String encoding, SingleByteCharsetConverter converter)
      throws SQLException
    {
      this.valuesClause = extractValuesClause(sql);
      String odkuClause = this.isOnDuplicateKeyUpdate ? sql.substring(this.locationOfOnDuplicateKeyUpdate) : null;
      
      String headSql = null;
      if (this.isOnDuplicateKeyUpdate) {
        headSql = sql.substring(0, this.locationOfOnDuplicateKeyUpdate);
      } else {
        headSql = sql;
      }
      this.batchHead = new ParseInfo(PreparedStatement.this, headSql, conn, metadata, encoding, converter, false);
      
      this.batchValues = new ParseInfo(PreparedStatement.this, "," + this.valuesClause, conn, metadata, encoding, converter, false);
      
      this.batchODKUClause = null;
      if ((odkuClause != null) && (odkuClause.length() > 0)) {
        this.batchODKUClause = new ParseInfo(PreparedStatement.this, "," + this.valuesClause + " " + odkuClause, conn, metadata, encoding, converter, false);
      }
    }
    
    private String extractValuesClause(String sql)
      throws SQLException
    {
      String quoteCharStr = PreparedStatement.this.connection.getMetaData().getIdentifierQuoteString();
      
      int indexOfValues = -1;
      int valuesSearchStart = this.statementStartPos;
      while (indexOfValues == -1)
      {
        if (quoteCharStr.length() > 0) {
          indexOfValues = StringUtils.indexOfIgnoreCaseRespectQuotes(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES", quoteCharStr.charAt(0), false);
        } else {
          indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES");
        }
        if (indexOfValues <= 0) {
          break;
        }
        char c = PreparedStatement.this.originalSql.charAt(indexOfValues - 1);
        if ((!Character.isWhitespace(c)) && (c != ')') && (c != '`'))
        {
          valuesSearchStart = indexOfValues + 6;
          indexOfValues = -1;
        }
        else
        {
          c = PreparedStatement.this.originalSql.charAt(indexOfValues + 6);
          if ((!Character.isWhitespace(c)) && (c != '('))
          {
            valuesSearchStart = indexOfValues + 6;
            indexOfValues = -1;
          }
        }
      }
      if (indexOfValues == -1) {
        return null;
      }
      int indexOfFirstParen = sql.indexOf('(', indexOfValues + 6);
      if (indexOfFirstParen == -1) {
        return null;
      }
      int endOfValuesClause = sql.lastIndexOf(')');
      if (endOfValuesClause == -1) {
        return null;
      }
      if (this.isOnDuplicateKeyUpdate) {
        endOfValuesClause = this.locationOfOnDuplicateKeyUpdate - 1;
      }
      return sql.substring(indexOfFirstParen, endOfValuesClause + 1);
    }
    
    synchronized ParseInfo getParseInfoForBatch(int numBatch)
    {
      PreparedStatement.AppendingBatchVisitor apv = new PreparedStatement.AppendingBatchVisitor(PreparedStatement.this);
      buildInfoForBatch(numBatch, apv);
      
      ParseInfo batchParseInfo = new ParseInfo(PreparedStatement.this, apv.getStaticSqlStrings(), this.firstStmtChar, this.foundLimitClause, this.foundLoadData, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementLength, this.statementStartPos);
      
      return batchParseInfo;
    }
    
    String getSqlForBatch(int numBatch)
      throws UnsupportedEncodingException
    {
      ParseInfo batchInfo = getParseInfoForBatch(numBatch);
      
      return getSqlForBatch(batchInfo);
    }
    
    String getSqlForBatch(ParseInfo batchInfo)
      throws UnsupportedEncodingException
    {
      int size = 0;
      byte[][] sqlStrings = batchInfo.staticSql;
      int sqlStringsLength = sqlStrings.length;
      for (int i = 0; i < sqlStringsLength; i++)
      {
        size += sqlStrings[i].length;
        size++;
      }
      StringBuffer buf = new StringBuffer(size);
      for (int i = 0; i < sqlStringsLength - 1; i++)
      {
        buf.append(new String(sqlStrings[i], PreparedStatement.this.charEncoding));
        buf.append("?");
      }
      buf.append(new String(sqlStrings[(sqlStringsLength - 1)]));
      
      return buf.toString();
    }
    
    private void buildInfoForBatch(int numBatch, PreparedStatement.BatchVisitor visitor)
    {
      byte[][] headStaticSql = this.batchHead.staticSql;
      int headStaticSqlLength = headStaticSql.length;
      if (headStaticSqlLength > 1) {
        for (int i = 0; i < headStaticSqlLength - 1; i++) {
          visitor.append(headStaticSql[i]).increment();
        }
      }
      byte[] endOfHead = headStaticSql[(headStaticSqlLength - 1)];
      byte[][] valuesStaticSql = this.batchValues.staticSql;
      byte[] beginOfValues = valuesStaticSql[0];
      
      visitor.merge(endOfHead, beginOfValues).increment();
      
      int numValueRepeats = numBatch - 1;
      if (this.batchODKUClause != null) {
        numValueRepeats--;
      }
      int valuesStaticSqlLength = valuesStaticSql.length;
      byte[] endOfValues = valuesStaticSql[(valuesStaticSqlLength - 1)];
      for (int i = 0; i < numValueRepeats; i++)
      {
        for (int j = 1; j < valuesStaticSqlLength - 1; j++) {
          visitor.append(valuesStaticSql[j]).increment();
        }
        visitor.merge(endOfValues, beginOfValues).increment();
      }
      if (this.batchODKUClause != null)
      {
        byte[][] batchOdkuStaticSql = this.batchODKUClause.staticSql;
        byte[] beginOfOdku = batchOdkuStaticSql[0];
        visitor.decrement().merge(endOfValues, beginOfOdku).increment();
        
        int batchOdkuStaticSqlLength = batchOdkuStaticSql.length;
        if (numBatch > 1) {
          for (int i = 1; i < batchOdkuStaticSqlLength; i++) {
            visitor.append(batchOdkuStaticSql[i]).increment();
          }
        } else {
          visitor.decrement().append(batchOdkuStaticSql[(batchOdkuStaticSqlLength - 1)]);
        }
      }
      else
      {
        visitor.decrement().append(this.staticSql[(this.staticSql.length - 1)]);
      }
    }
    
    private ParseInfo(byte[][] staticSql, char firstStmtChar, boolean foundLimitClause, boolean foundLoadData, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementLength, int statementStartPos)
    {
      this.firstStmtChar = firstStmtChar;
      this.foundLimitClause = foundLimitClause;
      this.foundLoadData = foundLoadData;
      this.isOnDuplicateKeyUpdate = isOnDuplicateKeyUpdate;
      this.locationOfOnDuplicateKeyUpdate = locationOfOnDuplicateKeyUpdate;
      this.statementLength = statementLength;
      this.statementStartPos = statementStartPos;
      this.staticSql = staticSql;
    }
  }
  
  static abstract interface BatchVisitor
  {
    public abstract BatchVisitor increment();
    
    public abstract BatchVisitor decrement();
    
    public abstract BatchVisitor append(byte[] paramArrayOfByte);
    
    public abstract BatchVisitor merge(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
  }
  
  class AppendingBatchVisitor
    implements PreparedStatement.BatchVisitor
  {
    LinkedList statementComponents = new LinkedList();
    
    AppendingBatchVisitor() {}
    
    public PreparedStatement.BatchVisitor append(byte[] values)
    {
      this.statementComponents.addLast(values);
      
      return this;
    }
    
    public PreparedStatement.BatchVisitor increment()
    {
      return this;
    }
    
    public PreparedStatement.BatchVisitor decrement()
    {
      this.statementComponents.removeLast();
      
      return this;
    }
    
    public PreparedStatement.BatchVisitor merge(byte[] front, byte[] back)
    {
      int mergedLength = front.length + back.length;
      byte[] merged = new byte[mergedLength];
      System.arraycopy(front, 0, merged, 0, front.length);
      System.arraycopy(back, 0, merged, front.length, back.length);
      this.statementComponents.addLast(merged);
      return this;
    }
    
    public byte[][] getStaticSqlStrings()
    {
      byte[][] asBytes = new byte[this.statementComponents.size()][];
      this.statementComponents.toArray(asBytes);
      
      return asBytes;
    }
    
    public String toString()
    {
      StringBuffer buf = new StringBuffer();
      Iterator iter = this.statementComponents.iterator();
      while (iter.hasNext()) {
        buf.append(new String((byte[])iter.next()));
      }
      return buf.toString();
    }
  }
  
  private static final byte[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  
  protected static int readFully(Reader reader, char[] buf, int length)
    throws IOException
  {
    int numCharsRead = 0;
    while (numCharsRead < length)
    {
      int count = reader.read(buf, numCharsRead, length - numCharsRead);
      if (count < 0) {
        break;
      }
      numCharsRead += count;
    }
    return numCharsRead;
  }
  
  protected boolean batchHasPlainStatements = false;
  private DatabaseMetaData dbmd = null;
  protected char firstCharOfStmt = '\000';
  protected boolean hasLimitClause = false;
  protected boolean isLoadDataQuery = false;
  private boolean[] isNull = null;
  private boolean[] isStream = null;
  protected int numberOfExecutions = 0;
  protected String originalSql = null;
  protected int parameterCount;
  protected MysqlParameterMetadata parameterMetaData;
  private InputStream[] parameterStreams = null;
  private byte[][] parameterValues = (byte[][])null;
  protected int[] parameterTypes = null;
  protected ParseInfo parseInfo;
  private java.sql.ResultSetMetaData pstmtResultMetaData;
  private byte[][] staticSqlStrings = (byte[][])null;
  private byte[] streamConvertBuf = new byte['က'];
  private int[] streamLengths = null;
  private SimpleDateFormat tsdf = null;
  protected boolean useTrueBoolean = false;
  protected boolean usingAnsiMode;
  protected String batchedValuesClause;
  private boolean doPingInstead;
  private SimpleDateFormat ddf;
  private SimpleDateFormat tdf;
  private boolean compensateForOnDuplicateKeyUpdate = false;
  private CharsetEncoder charsetEncoder;
  private int batchCommandIndex = -1;
  
  protected static PreparedStatement getInstance(MySQLConnection conn, String catalog)
    throws SQLException
  {
    if (!Util.isJdbc4()) {
      return new PreparedStatement(conn, catalog);
    }
    return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_2_ARG_CTOR, new Object[] { conn, catalog }, conn.getExceptionInterceptor());
  }
  
  protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog)
    throws SQLException
  {
    if (!Util.isJdbc4()) {
      return new PreparedStatement(conn, sql, catalog);
    }
    return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_3_ARG_CTOR, new Object[] { conn, sql, catalog }, conn.getExceptionInterceptor());
  }
  
  protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo)
    throws SQLException
  {
    if (!Util.isJdbc4()) {
      return new PreparedStatement(conn, sql, catalog, cachedParseInfo);
    }
    return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_4_ARG_CTOR, new Object[] { conn, sql, catalog, cachedParseInfo }, conn.getExceptionInterceptor());
  }
  
  public PreparedStatement(MySQLConnection conn, String catalog)
    throws SQLException
  {
    super(conn, catalog);
    
    this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
  }
  
  public PreparedStatement(MySQLConnection conn, String sql, String catalog)
    throws SQLException
  {
    super(conn, catalog);
    if (sql == null) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), "S1009", getExceptionInterceptor());
    }
    this.originalSql = sql;
    if (this.originalSql.startsWith("/* ping */")) {
      this.doPingInstead = true;
    } else {
      this.doPingInstead = false;
    }
    this.dbmd = this.connection.getMetaData();
    
    this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
    
    this.parseInfo = new ParseInfo(sql, this.connection, this.dbmd, this.charEncoding, this.charConverter);
    
    initializeFromParseInfo();
    
    this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
    if (conn.getRequiresEscapingEncoder()) {
      this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
    }
  }
  
  public PreparedStatement(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo)
    throws SQLException
  {
    super(conn, catalog);
    if (sql == null) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), "S1009", getExceptionInterceptor());
    }
    this.originalSql = sql;
    
    this.dbmd = this.connection.getMetaData();
    
    this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
    
    this.parseInfo = cachedParseInfo;
    
    this.usingAnsiMode = (!this.connection.useAnsiQuotedIdentifiers());
    
    initializeFromParseInfo();
    
    this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
    if (conn.getRequiresEscapingEncoder()) {
      this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
    }
  }
  
  public void addBatch()
    throws SQLException
  {
    if (this.batchedArgs == null) {
      this.batchedArgs = new ArrayList();
    }
    for (int i = 0; i < this.parameterValues.length; i++) {
      checkAllParametersSet(this.parameterValues[i], this.parameterStreams[i], i);
    }
    this.batchedArgs.add(new BatchParams(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull));
  }
  
  public synchronized void addBatch(String sql)
    throws SQLException
  {
    this.batchHasPlainStatements = true;
    
    super.addBatch(sql);
  }
  
  protected String asSql()
    throws SQLException
  {
    return asSql(false);
  }
  
  protected String asSql(boolean quoteStreamsAndUnknowns)
    throws SQLException
  {
    if (this.isClosed) {
      return "statement has been closed, no further internal information available";
    }
    StringBuffer buf = new StringBuffer();
    try
    {
      int realParameterCount = this.parameterCount + getParameterIndexOffset();
      Object batchArg = null;
      if (this.batchCommandIndex != -1) {
        batchArg = this.batchedArgs.get(this.batchCommandIndex);
      }
      for (int i = 0; i < realParameterCount; i++)
      {
        if (this.charEncoding != null) {
          buf.append(new String(this.staticSqlStrings[i], this.charEncoding));
        } else {
          buf.append(new String(this.staticSqlStrings[i]));
        }
        byte[] val = null;
        if ((batchArg != null) && ((batchArg instanceof String)))
        {
          buf.append((String)batchArg);
        }
        else
        {
          if (this.batchCommandIndex == -1) {
            val = this.parameterValues[i];
          } else {
            val = ((BatchParams)batchArg).parameterStrings[i];
          }
          boolean isStreamParam = false;
          if (this.batchCommandIndex == -1) {
            isStreamParam = this.isStream[i];
          } else {
            isStreamParam = ((BatchParams)batchArg).isStream[i];
          }
          if ((val == null) && (!isStreamParam))
          {
            if (quoteStreamsAndUnknowns) {
              buf.append("'");
            }
            buf.append("** NOT SPECIFIED **");
            if (quoteStreamsAndUnknowns) {
              buf.append("'");
            }
          }
          else if (isStreamParam)
          {
            if (quoteStreamsAndUnknowns) {
              buf.append("'");
            }
            buf.append("** STREAM DATA **");
            if (quoteStreamsAndUnknowns) {
              buf.append("'");
            }
          }
          else if (this.charConverter != null)
          {
            buf.append(this.charConverter.toString(val));
          }
          else if (this.charEncoding != null)
          {
            buf.append(new String(val, this.charEncoding));
          }
          else
          {
            buf.append(StringUtils.toAsciiString(val));
          }
        }
      }
      if (this.charEncoding != null) {
        buf.append(new String(this.staticSqlStrings[(this.parameterCount + getParameterIndexOffset())], this.charEncoding));
      } else {
        buf.append(StringUtils.toAsciiString(this.staticSqlStrings[(this.parameterCount + getParameterIndexOffset())]));
      }
    }
    catch (UnsupportedEncodingException uue)
    {
      throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
    }
    return buf.toString();
  }
  
  public synchronized void clearBatch()
    throws SQLException
  {
    this.batchHasPlainStatements = false;
    
    super.clearBatch();
  }
  
  public synchronized void clearParameters()
    throws SQLException
  {
    checkClosed();
    for (int i = 0; i < this.parameterValues.length; i++)
    {
      this.parameterValues[i] = null;
      this.parameterStreams[i] = null;
      this.isStream[i] = false;
      this.isNull[i] = false;
      this.parameterTypes[i] = 0;
    }
  }
  
  public synchronized void close()
    throws SQLException
  {
    realClose(true, true);
  }
  
  private final void escapeblockFast(byte[] buf, Buffer packet, int size)
    throws SQLException
  {
    int lastwritten = 0;
    for (int i = 0; i < size; i++)
    {
      byte b = buf[i];
      if (b == 0)
      {
        if (i > lastwritten) {
          packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
        }
        packet.writeByte((byte)92);
        packet.writeByte((byte)48);
        lastwritten = i + 1;
      }
      else if ((b == 92) || (b == 39) || ((!this.usingAnsiMode) && (b == 34)))
      {
        if (i > lastwritten) {
          packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
        }
        packet.writeByte((byte)92);
        lastwritten = i;
      }
    }
    if (lastwritten < size) {
      packet.writeBytesNoNull(buf, lastwritten, size - lastwritten);
    }
  }
  
  private final void escapeblockFast(byte[] buf, ByteArrayOutputStream bytesOut, int size)
  {
    int lastwritten = 0;
    for (int i = 0; i < size; i++)
    {
      byte b = buf[i];
      if (b == 0)
      {
        if (i > lastwritten) {
          bytesOut.write(buf, lastwritten, i - lastwritten);
        }
        bytesOut.write(92);
        bytesOut.write(48);
        lastwritten = i + 1;
      }
      else if ((b == 92) || (b == 39) || ((!this.usingAnsiMode) && (b == 34)))
      {
        if (i > lastwritten) {
          bytesOut.write(buf, lastwritten, i - lastwritten);
        }
        bytesOut.write(92);
        lastwritten = i;
      }
    }
    if (lastwritten < size) {
      bytesOut.write(buf, lastwritten, size - lastwritten);
    }
  }
  
  protected boolean checkReadOnlySafeStatement()
    throws SQLException
  {
    return (!this.connection.isReadOnly()) || (this.firstCharOfStmt == 'S');
  }
  
  public boolean execute()
    throws SQLException
  {
    checkClosed();
    
    MySQLConnection locallyScopedConn = this.connection;
    if (!checkReadOnlySafeStatement()) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), "S1009", getExceptionInterceptor());
    }
    ResultSetInternalMethods rs = null;
    
    CachedResultSetMetaData cachedMetadata = null;
    synchronized (locallyScopedConn.getMutex())
    {
      this.lastQueryIsOnDupKeyUpdate = false;
      if (this.retrieveGeneratedKeys) {
        this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyUpdateInSQL();
      }
      boolean doStreaming = createStreamingResultSet();
      
      clearWarnings();
      if ((doStreaming) && (this.connection.getNetTimeoutForStreamingResults() > 0)) {
        executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
      }
      this.batchedGeneratedKeys = null;
      
      Buffer sendPacket = fillSendPacket();
      
      String oldCatalog = null;
      if (!locallyScopedConn.getCatalog().equals(this.currentCatalog))
      {
        oldCatalog = locallyScopedConn.getCatalog();
        locallyScopedConn.setCatalog(this.currentCatalog);
      }
      if (locallyScopedConn.getCacheResultSetMetadata()) {
        cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
      }
      Field[] metadataFromCache = null;
      if (cachedMetadata != null) {
        metadataFromCache = cachedMetadata.fields;
      }
      boolean oldInfoMsgState = false;
      if (this.retrieveGeneratedKeys)
      {
        oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
        locallyScopedConn.setReadInfoMsgEnabled(true);
      }
      if (locallyScopedConn.useMaxRows())
      {
        int rowLimit = -1;
        if (this.firstCharOfStmt == 'S')
        {
          if (this.hasLimitClause) {
            rowLimit = this.maxRows;
          } else if (this.maxRows <= 0) {
            executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
          } else {
            executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows);
          }
        }
        else {
          executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
        }
        rs = executeInternal(rowLimit, sendPacket, doStreaming, this.firstCharOfStmt == 'S', metadataFromCache, false);
      }
      else
      {
        rs = executeInternal(-1, sendPacket, doStreaming, this.firstCharOfStmt == 'S', metadataFromCache, false);
      }
      if (cachedMetadata != null) {
        locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
      } else if ((rs.reallyResult()) && (locallyScopedConn.getCacheResultSetMetadata())) {
        locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, rs);
      }
      if (this.retrieveGeneratedKeys)
      {
        locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
        rs.setFirstCharOfQuery(this.firstCharOfStmt);
      }
      if (oldCatalog != null) {
        locallyScopedConn.setCatalog(oldCatalog);
      }
      if (rs != null)
      {
        this.lastInsertId = rs.getUpdateID();
        
        this.results = rs;
      }
    }
    return (rs != null) && (rs.reallyResult());
  }
  
  public int[] executeBatch()
    throws SQLException
  {
    checkClosed();
    if (this.connection.isReadOnly()) {
      throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), "S1009");
    }
    synchronized (this.connection.getMutex())
    {
      if ((this.batchedArgs == null) || (this.batchedArgs.size() == 0)) {
        return new int[0];
      }
      int batchTimeout = this.timeoutInMillis;
      this.timeoutInMillis = 0;
      
      resetCancelledState();
      try
      {
        clearWarnings();
        if ((!this.batchHasPlainStatements) && (this.connection.getRewriteBatchedStatements()))
        {
          if (canRewriteAsMultiValueInsertAtSqlLevel())
          {
            arrayOfInt = executeBatchedInserts(batchTimeout);jsr 83;return arrayOfInt;
          }
          if ((this.connection.versionMeetsMinimum(4, 1, 0)) && (!this.batchHasPlainStatements) && (this.batchedArgs != null) && (this.batchedArgs.size() > 3))
          {
            arrayOfInt = executePreparedBatchAsMultiStatement(batchTimeout);jsr 28;return arrayOfInt;
          }
        }
        int[] arrayOfInt = executeBatchSerially(batchTimeout);jsr 15;return arrayOfInt;
      }
      finally
      {
        jsr 6;
      }
      localObject2 = returnAddress;clearBatch();ret;
    }
  }
  
  public boolean canRewriteAsMultiValueInsertAtSqlLevel()
    throws SQLException
  {
    return this.parseInfo.canRewriteAsMultiValueInsert;
  }
  
  protected int getLocationOfOnDuplicateKeyUpdate()
  {
    return this.parseInfo.locationOfOnDuplicateKeyUpdate;
  }
  
  private String generateMultiStatementForBatch(int numBatches)
  {
    StringBuffer newStatementSql = new StringBuffer((this.originalSql.length() + 1) * numBatches);
    
    newStatementSql.append(this.originalSql);
    for (int i = 0; i < numBatches - 1; i++)
    {
      newStatementSql.append(';');
      newStatementSql.append(this.originalSql);
    }
    return newStatementSql.toString();
  }
  
  protected String getValuesClause()
    throws SQLException
  {
    return this.parseInfo.valuesClause;
  }
  
  protected int computeBatchSize(int numBatchedArgs)
    throws SQLException
  {
    long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
    
    long maxSizeOfParameterSet = combinedValues[0];
    long sizeOfEntireBatch = combinedValues[1];
    
    int maxAllowedPacket = this.connection.getMaxAllowedPacket();
    if (sizeOfEntireBatch < maxAllowedPacket - this.originalSql.length()) {
      return numBatchedArgs;
    }
    return (int)Math.max(1L, (maxAllowedPacket - this.originalSql.length()) / maxSizeOfParameterSet);
  }
  
  protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs)
    throws SQLException
  {
    long sizeOfEntireBatch = 0L;
    long maxSizeOfParameterSet = 0L;
    for (int i = 0; i < numBatchedArgs; i++)
    {
      BatchParams paramArg = (BatchParams)this.batchedArgs.get(i);
      
      boolean[] isNullBatch = paramArg.isNull;
      boolean[] isStreamBatch = paramArg.isStream;
      
      long sizeOfParameterSet = 0L;
      for (int j = 0; j < isNullBatch.length; j++) {
        if (isNullBatch[j] == 0)
        {
          if (isStreamBatch[j] != 0)
          {
            int streamLength = paramArg.streamLengths[j];
            if (streamLength != -1)
            {
              sizeOfParameterSet += streamLength * 2;
            }
            else
            {
              int paramLength = paramArg.parameterStrings[j].length;
              sizeOfParameterSet += paramLength;
            }
          }
          else
          {
            sizeOfParameterSet += paramArg.parameterStrings[j].length;
          }
        }
        else {
          sizeOfParameterSet += 4L;
        }
      }
      if (getValuesClause() != null) {
        sizeOfParameterSet += getValuesClause().length() + 1;
      } else {
        sizeOfParameterSet += this.originalSql.length() + 1;
      }
      sizeOfEntireBatch += sizeOfParameterSet;
      if (sizeOfParameterSet > maxSizeOfParameterSet) {
        maxSizeOfParameterSet = sizeOfParameterSet;
      }
    }
    return new long[] { maxSizeOfParameterSet, sizeOfEntireBatch };
  }
  
  protected int[] executeBatchSerially(int batchTimeout)
    throws SQLException
  {
    MySQLConnection locallyScopedConn = this.connection;
    if (locallyScopedConn == null) {
      checkClosed();
    }
    int[] updateCounts = null;
    if (this.batchedArgs != null)
    {
      int nbrCommands = this.batchedArgs.size();
      updateCounts = new int[nbrCommands];
      for (int i = 0; i < nbrCommands; i++) {
        updateCounts[i] = -3;
      }
      SQLException sqlEx = null;
      
      StatementImpl.CancelTask timeoutTask = null;
      try
      {
        if ((locallyScopedConn.getEnableQueryTimeouts()) && (batchTimeout != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
        {
          timeoutTask = new StatementImpl.CancelTask(this, this);
          locallyScopedConn.getCancelTimer().schedule(timeoutTask, batchTimeout);
        }
        if (this.retrieveGeneratedKeys) {
          this.batchedGeneratedKeys = new ArrayList(nbrCommands);
        }
        for (this.batchCommandIndex = 0; this.batchCommandIndex < nbrCommands; this.batchCommandIndex += 1)
        {
          Object arg = this.batchedArgs.get(this.batchCommandIndex);
          if ((arg instanceof String))
          {
            updateCounts[this.batchCommandIndex] = executeUpdate((String)arg);
          }
          else
          {
            BatchParams paramArg = (BatchParams)arg;
            try
            {
              updateCounts[this.batchCommandIndex] = executeUpdate(paramArg.parameterStrings, paramArg.parameterStreams, paramArg.isStream, paramArg.streamLengths, paramArg.isNull, true);
              if (this.retrieveGeneratedKeys)
              {
                ResultSet rs = null;
                try
                {
                  if (containsOnDuplicateKeyUpdateInSQL()) {
                    rs = getGeneratedKeysInternal(1);
                  } else {
                    rs = getGeneratedKeysInternal();
                  }
                  while (rs.next()) {
                    this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[][] { rs.getBytes(1) }, getExceptionInterceptor()));
                  }
                }
                finally
                {
                  if (rs != null) {
                    rs.close();
                  }
                }
              }
            }
            catch (SQLException ex)
            {
              updateCounts[this.batchCommandIndex] = -3;
              if ((this.continueBatchOnError) && (!(ex instanceof MySQLTimeoutException)) && (!(ex instanceof MySQLStatementCancelledException)) && (!hasDeadlockOrTimeoutRolledBackTx(ex)))
              {
                sqlEx = ex;
              }
              else
              {
                int[] newUpdateCounts = new int[this.batchCommandIndex];
                System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex);
                
                throw new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
              }
            }
          }
        }
        if (sqlEx != null) {
          throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
        }
      }
      catch (NullPointerException npe)
      {
        try
        {
          checkClosed();
        }
        catch (SQLException connectionClosedEx)
        {
          updateCounts[this.batchCommandIndex] = -3;
          
          int[] newUpdateCounts = new int[this.batchCommandIndex];
          
          System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex);
          
          throw new BatchUpdateException(connectionClosedEx.getMessage(), connectionClosedEx.getSQLState(), connectionClosedEx.getErrorCode(), newUpdateCounts);
        }
        throw npe;
      }
      finally
      {
        this.batchCommandIndex = -1;
        if (timeoutTask != null)
        {
          timeoutTask.cancel();
          locallyScopedConn.getCancelTimer().purge();
        }
        resetCancelledState();
      }
    }
    return updateCounts != null ? updateCounts : new int[0];
  }
  
  public String getDateTime(String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(new java.util.Date());
  }
  
  protected ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, Field[] metadataFromCache, boolean isBatch)
    throws SQLException
  {
    try
    {
      resetCancelledState();
      
      MySQLConnection locallyScopedConnection = this.connection;
      
      this.numberOfExecutions += 1;
      if (this.doPingInstead)
      {
        doPingInstead();
        
        return this.results;
      }
      StatementImpl.CancelTask timeoutTask = null;
      ResultSetInternalMethods rs;
      try
      {
        if ((locallyScopedConnection.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConnection.versionMeetsMinimum(5, 0, 0)))
        {
          timeoutTask = new StatementImpl.CancelTask(this, this);
          locallyScopedConnection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
        }
        rs = locallyScopedConnection.execSQL(this, null, maxRowsToRetrieve, sendPacket, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet, this.currentCatalog, metadataFromCache, isBatch);
        if (timeoutTask != null)
        {
          timeoutTask.cancel();
          
          locallyScopedConnection.getCancelTimer().purge();
          if (timeoutTask.caughtWhileCancelling != null) {
            throw timeoutTask.caughtWhileCancelling;
          }
          timeoutTask = null;
        }
        synchronized (this.cancelTimeoutMutex)
        {
          if (this.wasCancelled)
          {
            SQLException cause = null;
            if (this.wasCancelledByTimeout) {
              cause = new MySQLTimeoutException();
            } else {
              cause = new MySQLStatementCancelledException();
            }
            resetCancelledState();
            
            throw cause;
          }
        }
      }
      finally
      {
        if (timeoutTask != null)
        {
          timeoutTask.cancel();
          locallyScopedConnection.getCancelTimer().purge();
        }
      }
      return rs;
    }
    catch (NullPointerException npe)
    {
      checkClosed();
      
      throw npe;
    }
  }
  
  public ResultSet executeQuery()
    throws SQLException
  {
    checkClosed();
    
    MySQLConnection locallyScopedConn = this.connection;
    
    checkForDml(this.originalSql, this.firstCharOfStmt);
    
    CachedResultSetMetaData cachedMetadata = null;
    synchronized (locallyScopedConn.getMutex())
    {
      clearWarnings();
      
      boolean doStreaming = createStreamingResultSet();
      
      this.batchedGeneratedKeys = null;
      if ((doStreaming) && (this.connection.getNetTimeoutForStreamingResults() > 0))
      {
        Statement stmt = null;
        try
        {
          stmt = this.connection.createStatement();
          
          ((StatementImpl)stmt).executeSimpleNonQuery(this.connection, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
        }
        finally
        {
          if (stmt != null) {
            stmt.close();
          }
        }
      }
      Buffer sendPacket = fillSendPacket();
      if ((this.results != null) && 
        (!this.connection.getHoldResultsOpenOverStatementClose()) && 
        (!this.holdResultsOpenOverClose)) {
        this.results.realClose(false);
      }
      String oldCatalog = null;
      if (!locallyScopedConn.getCatalog().equals(this.currentCatalog))
      {
        oldCatalog = locallyScopedConn.getCatalog();
        locallyScopedConn.setCatalog(this.currentCatalog);
      }
      if (locallyScopedConn.getCacheResultSetMetadata()) {
        cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
      }
      Field[] metadataFromCache = null;
      if (cachedMetadata != null) {
        metadataFromCache = cachedMetadata.fields;
      }
      if (locallyScopedConn.useMaxRows())
      {
        if (this.hasLimitClause)
        {
          this.results = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), true, metadataFromCache, false);
        }
        else
        {
          if (this.maxRows <= 0) {
            executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
          } else {
            executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows);
          }
          this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
          if (oldCatalog != null) {
            this.connection.setCatalog(oldCatalog);
          }
        }
      }
      else {
        this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
      }
      if (oldCatalog != null) {
        locallyScopedConn.setCatalog(oldCatalog);
      }
      if (cachedMetadata != null) {
        locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
      } else if (locallyScopedConn.getCacheResultSetMetadata()) {
        locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, this.results);
      }
    }
    this.lastInsertId = this.results.getUpdateID();
    
    return this.results;
  }
  
  public int executeUpdate()
    throws SQLException
  {
    return executeUpdate(true, false);
  }
  
  protected int executeUpdate(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch)
    throws SQLException
  {
    if (clearBatchedGeneratedKeysAndWarnings)
    {
      clearWarnings();
      this.batchedGeneratedKeys = null;
    }
    return executeUpdate(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch);
  }
  
  protected int executeUpdate(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths, boolean[] batchedIsNull, boolean isReallyBatch)
    throws SQLException
  {
    checkClosed();
    
    MySQLConnection locallyScopedConn = this.connection;
    if (locallyScopedConn.isReadOnly()) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), "S1009", getExceptionInterceptor());
    }
    if ((this.firstCharOfStmt == 'S') && (isSelectQuery())) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03", getExceptionInterceptor());
    }
    if ((this.results != null) && 
      (!locallyScopedConn.getHoldResultsOpenOverStatementClose())) {
      this.results.realClose(false);
    }
    ResultSetInternalMethods rs = null;
    synchronized (locallyScopedConn.getMutex())
    {
      Buffer sendPacket = fillSendPacket(batchedParameterStrings, batchedParameterStreams, batchedIsStream, batchedStreamLengths);
      
      String oldCatalog = null;
      if (!locallyScopedConn.getCatalog().equals(this.currentCatalog))
      {
        oldCatalog = locallyScopedConn.getCatalog();
        locallyScopedConn.setCatalog(this.currentCatalog);
      }
      if (locallyScopedConn.useMaxRows()) {
        executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
      }
      boolean oldInfoMsgState = false;
      if (this.retrieveGeneratedKeys)
      {
        oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
        locallyScopedConn.setReadInfoMsgEnabled(true);
      }
      rs = executeInternal(-1, sendPacket, false, false, null, isReallyBatch);
      if (this.retrieveGeneratedKeys)
      {
        locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
        rs.setFirstCharOfQuery(this.firstCharOfStmt);
      }
      if (oldCatalog != null) {
        locallyScopedConn.setCatalog(oldCatalog);
      }
    }
    this.results = rs;
    
    this.updateCount = rs.getUpdateCount();
    if ((containsOnDuplicateKeyUpdateInSQL()) && (this.compensateForOnDuplicateKeyUpdate)) {
      if ((this.updateCount == 2L) || (this.updateCount == 0L)) {
        this.updateCount = 1L;
      }
    }
    int truncatedUpdateCount = 0;
    if (this.updateCount > 2147483647L) {
      truncatedUpdateCount = Integer.MAX_VALUE;
    } else {
      truncatedUpdateCount = (int)this.updateCount;
    }
    this.lastInsertId = rs.getUpdateID();
    
    return truncatedUpdateCount;
  }
  
  protected boolean containsOnDuplicateKeyUpdateInSQL()
  {
    return this.parseInfo.isOnDuplicateKeyUpdate;
  }
  
  protected Buffer fillSendPacket()
    throws SQLException
  {
    return fillSendPacket(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths);
  }
  
  protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths)
    throws SQLException
  {
    Buffer sendPacket = this.connection.getIO().getSharedSendPacket();
    
    sendPacket.clear();
    
    sendPacket.writeByte((byte)3);
    
    boolean useStreamLengths = this.connection.getUseStreamLengthsInPrepStmts();
    
    int ensurePacketSize = 0;
    
    String statementComment = this.connection.getStatementComment();
    
    byte[] commentAsBytes = null;
    if (statementComment != null)
    {
      if (this.charConverter != null) {
        commentAsBytes = this.charConverter.toBytes(statementComment);
      } else {
        commentAsBytes = StringUtils.getBytes(statementComment, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
      }
      ensurePacketSize += commentAsBytes.length;
      ensurePacketSize += 6;
    }
    for (int i = 0; i < batchedParameterStrings.length; i++) {
      if ((batchedIsStream[i] != 0) && (useStreamLengths)) {
        ensurePacketSize += batchedStreamLengths[i];
      }
    }
    if (ensurePacketSize != 0) {
      sendPacket.ensureCapacity(ensurePacketSize);
    }
    if (commentAsBytes != null)
    {
      sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
      sendPacket.writeBytesNoNull(commentAsBytes);
      sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
    }
    for (int i = 0; i < batchedParameterStrings.length; i++)
    {
      checkAllParametersSet(batchedParameterStrings[i], batchedParameterStreams[i], i);
      
      sendPacket.writeBytesNoNull(this.staticSqlStrings[i]);
      if (batchedIsStream[i] != 0) {
        streamToBytes(sendPacket, batchedParameterStreams[i], true, batchedStreamLengths[i], useStreamLengths);
      } else {
        sendPacket.writeBytesNoNull(batchedParameterStrings[i]);
      }
    }
    sendPacket.writeBytesNoNull(this.staticSqlStrings[batchedParameterStrings.length]);
    
    return sendPacket;
  }
  
  private void checkAllParametersSet(byte[] parameterString, InputStream parameterStream, int columnIndex)
    throws SQLException
  {
    if ((parameterString == null) && (parameterStream == null)) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.40") + (columnIndex + 1), "07001", getExceptionInterceptor());
    }
  }
  
  protected PreparedStatement prepareBatchedInsertSQL(MySQLConnection localConn, int numBatches)
    throws SQLException
  {
    PreparedStatement pstmt = new PreparedStatement(localConn, "Rewritten batch of: " + this.originalSql, this.currentCatalog, this.parseInfo.getParseInfoForBatch(numBatches));
    pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
    pstmt.rewrittenBatchSize = numBatches;
    
    return pstmt;
  }
  
  protected int rewrittenBatchSize = 0;
  
  public int getRewrittenBatchSize()
  {
    return this.rewrittenBatchSize;
  }
  
  public String getNonRewrittenSql()
  {
    int indexOfBatch = this.originalSql.indexOf(" of: ");
    if (indexOfBatch != -1) {
      return this.originalSql.substring(indexOfBatch + 5);
    }
    return this.originalSql;
  }
  
  public byte[] getBytesRepresentation(int parameterIndex)
    throws SQLException
  {
    if (this.isStream[parameterIndex] != 0) {
      return streamToBytes(this.parameterStreams[parameterIndex], false, this.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
    }
    byte[] parameterVal = this.parameterValues[parameterIndex];
    if (parameterVal == null) {
      return null;
    }
    if ((parameterVal[0] == 39) && (parameterVal[(parameterVal.length - 1)] == 39))
    {
      byte[] valNoQuotes = new byte[parameterVal.length - 2];
      System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
      
      return valNoQuotes;
    }
    return parameterVal;
  }
  
  protected byte[] getBytesRepresentationForBatch(int parameterIndex, int commandIndex)
    throws SQLException
  {
    Object batchedArg = this.batchedArgs.get(commandIndex);
    if ((batchedArg instanceof String)) {
      try
      {
        return ((String)batchedArg).getBytes(this.charEncoding);
      }
      catch (UnsupportedEncodingException uue)
      {
        throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
      }
    }
    BatchParams params = (BatchParams)batchedArg;
    if (params.isStream[parameterIndex] != 0) {
      return streamToBytes(params.parameterStreams[parameterIndex], false, params.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
    }
    byte[] parameterVal = params.parameterStrings[parameterIndex];
    if (parameterVal == null) {
      return null;
    }
    if ((parameterVal[0] == 39) && (parameterVal[(parameterVal.length - 1)] == 39))
    {
      byte[] valNoQuotes = new byte[parameterVal.length - 2];
      System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
      
      return valNoQuotes;
    }
    return parameterVal;
  }
  
  private final String getDateTimePattern(String dt, boolean toTime)
    throws Exception
  {
    int dtLength = dt != null ? dt.length() : 0;
    if ((dtLength >= 8) && (dtLength <= 10))
    {
      int dashCount = 0;
      boolean isDateOnly = true;
      for (int i = 0; i < dtLength; i++)
      {
        char c = dt.charAt(i);
        if ((!Character.isDigit(c)) && (c != '-'))
        {
          isDateOnly = false;
          
          break;
        }
        if (c == '-') {
          dashCount++;
        }
      }
      if ((isDateOnly) && (dashCount == 2)) {
        return "yyyy-MM-dd";
      }
    }
    boolean colonsOnly = true;
    for (int i = 0; i < dtLength; i++)
    {
      char c = dt.charAt(i);
      if ((!Character.isDigit(c)) && (c != ':'))
      {
        colonsOnly = false;
        
        break;
      }
    }
    if (colonsOnly) {
      return "HH:mm:ss";
    }
    StringReader reader = new StringReader(dt + " ");
    ArrayList vec = new ArrayList();
    ArrayList vecRemovelist = new ArrayList();
    Object[] nv = new Object[3];
    
    nv[0] = Constants.characterValueOf('y');
    nv[1] = new StringBuffer();
    nv[2] = Constants.integerValueOf(0);
    vec.add(nv);
    if (toTime)
    {
      nv = new Object[3];
      nv[0] = Constants.characterValueOf('h');
      nv[1] = new StringBuffer();
      nv[2] = Constants.integerValueOf(0);
      vec.add(nv);
    }
    int z;
    while ((z = reader.read()) != -1)
    {
      char separator = (char)z;
      int maxvecs = vec.size();
      for (int count = 0; count < maxvecs; count++)
      {
        Object[] v = (Object[])vec.get(count);
        int n = ((Integer)v[2]).intValue();
        char c = getSuccessor(((Character)v[0]).charValue(), n);
        if (!Character.isLetterOrDigit(separator))
        {
          if ((c == ((Character)v[0]).charValue()) && (c != 'S'))
          {
            vecRemovelist.add(v);
          }
          else
          {
            ((StringBuffer)v[1]).append(separator);
            if ((c == 'X') || (c == 'Y')) {
              v[2] = Constants.integerValueOf(4);
            }
          }
        }
        else
        {
          if (c == 'X')
          {
            c = 'y';
            nv = new Object[3];
            nv[1] = new StringBuffer(((StringBuffer)v[1]).toString()).append('M');
            
            nv[0] = Constants.characterValueOf('M');
            nv[2] = Constants.integerValueOf(1);
            vec.add(nv);
          }
          else if (c == 'Y')
          {
            c = 'M';
            nv = new Object[3];
            nv[1] = new StringBuffer(((StringBuffer)v[1]).toString()).append('d');
            
            nv[0] = Constants.characterValueOf('d');
            nv[2] = Constants.integerValueOf(1);
            vec.add(nv);
          }
          ((StringBuffer)v[1]).append(c);
          if (c == ((Character)v[0]).charValue())
          {
            v[2] = Constants.integerValueOf(n + 1);
          }
          else
          {
            v[0] = Constants.characterValueOf(c);
            v[2] = Constants.integerValueOf(1);
          }
        }
      }
      int size = vecRemovelist.size();
      for (int i = 0; i < size; i++)
      {
        Object[] v = (Object[])vecRemovelist.get(i);
        vec.remove(v);
      }
      vecRemovelist.clear();
    }
    int size = vec.size();
    for (int i = 0; i < size; i++)
    {
      Object[] v = (Object[])vec.get(i);
      char c = ((Character)v[0]).charValue();
      int n = ((Integer)v[2]).intValue();
      
      boolean bk = getSuccessor(c, n) != c;
      boolean atEnd = ((c == 's') || (c == 'm') || ((c == 'h') && (toTime))) && (bk);
      boolean finishesAtDate = (bk) && (c == 'd') && (!toTime);
      boolean containsEnd = ((StringBuffer)v[1]).toString().indexOf('W') != -1;
      if (((!atEnd) && (!finishesAtDate)) || (containsEnd)) {
        vecRemovelist.add(v);
      }
    }
    size = vecRemovelist.size();
    for (int i = 0; i < size; i++) {
      vec.remove(vecRemovelist.get(i));
    }
    vecRemovelist.clear();
    Object[] v = (Object[])vec.get(0);
    
    StringBuffer format = (StringBuffer)v[1];
    format.setLength(format.length() - 1);
    
    return format.toString();
  }
  
  public java.sql.ResultSetMetaData getMetaData()
    throws SQLException
  {
    if (!isSelectQuery()) {
      return null;
    }
    PreparedStatement mdStmt = null;
    ResultSet mdRs = null;
    if (this.pstmtResultMetaData == null) {
      try
      {
        mdStmt = new PreparedStatement(this.connection, this.originalSql, this.currentCatalog, this.parseInfo);
        
        mdStmt.setMaxRows(0);
        
        int paramCount = this.parameterValues.length;
        for (int i = 1; i <= paramCount; i++) {
          mdStmt.setString(i, "");
        }
        boolean hadResults = mdStmt.execute();
        if (hadResults)
        {
          mdRs = mdStmt.getResultSet();
          
          this.pstmtResultMetaData = mdRs.getMetaData();
        }
        else
        {
          this.pstmtResultMetaData = new ResultSetMetaData(new Field[0], this.connection.getUseOldAliasMetadataBehavior(), getExceptionInterceptor());
        }
      }
      finally
      {
        SQLException sqlExRethrow = null;
        if (mdRs != null)
        {
          try
          {
            mdRs.close();
          }
          catch (SQLException sqlEx)
          {
            sqlExRethrow = sqlEx;
          }
          mdRs = null;
        }
        if (mdStmt != null)
        {
          try
          {
            mdStmt.close();
          }
          catch (SQLException sqlEx)
          {
            sqlExRethrow = sqlEx;
          }
          mdStmt = null;
        }
        if (sqlExRethrow != null) {
          throw sqlExRethrow;
        }
      }
    }
    return this.pstmtResultMetaData;
  }
  
  protected boolean isSelectQuery()
  {
    return StringUtils.startsWithIgnoreCaseAndWs(StringUtils.stripComments(this.originalSql, "'\"", "'\"", true, false, true, true), "SELECT");
  }
  
  public ParameterMetaData getParameterMetaData()
    throws SQLException
  {
    if (this.parameterMetaData == null) {
      if (this.connection.getGenerateSimpleParameterMetadata()) {
        this.parameterMetaData = new MysqlParameterMetadata(this.parameterCount);
      } else {
        this.parameterMetaData = new MysqlParameterMetadata(null, this.parameterCount, getExceptionInterceptor());
      }
    }
    return this.parameterMetaData;
  }
  
  ParseInfo getParseInfo()
  {
    return this.parseInfo;
  }
  
  private final char getSuccessor(char c, int n)
  {
    return (c == 's') && (n < 2) ? 's' : c == 'm' ? 's' : (c == 'm') && (n < 2) ? 'm' : c == 'H' ? 'm' : (c == 'H') && (n < 2) ? 'H' : c == 'd' ? 'H' : (c == 'd') && (n < 2) ? 'd' : c == 'M' ? 'd' : (c == 'M') && (n < 3) ? 'M' : (c == 'M') && (n == 2) ? 'Y' : c == 'y' ? 'M' : (c == 'y') && (n < 4) ? 'y' : (c == 'y') && (n == 2) ? 'X' : 'W';
  }
  
  private final void hexEscapeBlock(byte[] buf, Buffer packet, int size)
    throws SQLException
  {
    for (int i = 0; i < size; i++)
    {
      byte b = buf[i];
      int lowBits = (b & 0xFF) / 16;
      int highBits = (b & 0xFF) % 16;
      
      packet.writeByte(HEX_DIGITS[lowBits]);
      packet.writeByte(HEX_DIGITS[highBits]);
    }
  }
  
  private void initializeFromParseInfo()
    throws SQLException
  {
    this.staticSqlStrings = this.parseInfo.staticSql;
    this.hasLimitClause = this.parseInfo.foundLimitClause;
    this.isLoadDataQuery = this.parseInfo.foundLoadData;
    this.firstCharOfStmt = this.parseInfo.firstStmtChar;
    
    this.parameterCount = (this.staticSqlStrings.length - 1);
    
    this.parameterValues = new byte[this.parameterCount][];
    this.parameterStreams = new InputStream[this.parameterCount];
    this.isStream = new boolean[this.parameterCount];
    this.streamLengths = new int[this.parameterCount];
    this.isNull = new boolean[this.parameterCount];
    this.parameterTypes = new int[this.parameterCount];
    
    clearParameters();
    for (int j = 0; j < this.parameterCount; j++) {
      this.isStream[j] = false;
    }
  }
  
  boolean isNull(int paramIndex)
  {
    return this.isNull[paramIndex];
  }
  
  private final int readblock(InputStream i, byte[] b)
    throws SQLException
  {
    try
    {
      return i.read(b);
    }
    catch (Throwable ex)
    {
      SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
      
      sqlEx.initCause(ex);
      
      throw sqlEx;
    }
  }
  
  private final int readblock(InputStream i, byte[] b, int length)
    throws SQLException
  {
    try
    {
      int lengthToRead = length;
      if (lengthToRead > b.length) {
        lengthToRead = b.length;
      }
      return i.read(b, 0, lengthToRead);
    }
    catch (Throwable ex)
    {
      SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
      
      sqlEx.initCause(ex);
      
      throw sqlEx;
    }
  }
  
  protected void realClose(boolean calledExplicitly, boolean closeOpenResults)
    throws SQLException
  {
    if ((this.useUsageAdvisor) && 
      (this.numberOfExecutions <= 1))
    {
      String message = Messages.getString("PreparedStatement.43");
      
      this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
    }
    super.realClose(calledExplicitly, closeOpenResults);
    
    this.dbmd = null;
    this.originalSql = null;
    this.staticSqlStrings = ((byte[][])null);
    this.parameterValues = ((byte[][])null);
    this.parameterStreams = null;
    this.isStream = null;
    this.streamLengths = null;
    this.isNull = null;
    this.streamConvertBuf = null;
    this.parameterTypes = null;
  }
  
  public void setArray(int i, Array x)
    throws SQLException
  {
    throw SQLError.notImplemented();
  }
  
  public void setAsciiStream(int parameterIndex, InputStream x, int length)
    throws SQLException
  {
    if (x == null) {
      setNull(parameterIndex, 12);
    } else {
      setBinaryStream(parameterIndex, x, length);
    }
  }
  
  public void setBigDecimal(int parameterIndex, BigDecimal x)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, 3);
    }
    else
    {
      setInternal(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString(x)));
      
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 3;
    }
  }
  
  public void setBinaryStream(int parameterIndex, InputStream x, int length)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, -2);
    }
    else
    {
      int parameterIndexOffset = getParameterIndexOffset();
      if ((parameterIndex < 1) || (parameterIndex > this.staticSqlStrings.length)) {
        throw SQLError.createSQLException(Messages.getString("PreparedStatement.2") + parameterIndex + Messages.getString("PreparedStatement.3") + this.staticSqlStrings.length + Messages.getString("PreparedStatement.4"), "S1009", getExceptionInterceptor());
      }
      if ((parameterIndexOffset == -1) && (parameterIndex == 1)) {
        throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
      }
      this.parameterStreams[(parameterIndex - 1 + parameterIndexOffset)] = x;
      this.isStream[(parameterIndex - 1 + parameterIndexOffset)] = true;
      this.streamLengths[(parameterIndex - 1 + parameterIndexOffset)] = length;
      this.isNull[(parameterIndex - 1 + parameterIndexOffset)] = false;
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2004;
    }
  }
  
  public void setBlob(int parameterIndex, InputStream inputStream, long length)
    throws SQLException
  {
    setBinaryStream(parameterIndex, inputStream, (int)length);
  }
  
  public void setBlob(int i, Blob x)
    throws SQLException
  {
    if (x == null)
    {
      setNull(i, 2004);
    }
    else
    {
      ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
      
      bytesOut.write(39);
      escapeblockFast(x.getBytes(1L, (int)x.length()), bytesOut, (int)x.length());
      
      bytesOut.write(39);
      
      setInternal(i, bytesOut.toByteArray());
      
      this.parameterTypes[(i - 1 + getParameterIndexOffset())] = 2004;
    }
  }
  
  public void setBoolean(int parameterIndex, boolean x)
    throws SQLException
  {
    if (this.useTrueBoolean)
    {
      setInternal(parameterIndex, x ? "1" : "0");
    }
    else
    {
      setInternal(parameterIndex, x ? "'t'" : "'f'");
      
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 16;
    }
  }
  
  public void setByte(int parameterIndex, byte x)
    throws SQLException
  {
    setInternal(parameterIndex, String.valueOf(x));
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -6;
  }
  
  public void setBytes(int parameterIndex, byte[] x)
    throws SQLException
  {
    setBytes(parameterIndex, x, true, true);
    if (x != null) {
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -2;
    }
  }
  
  protected void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, -2);
    }
    else
    {
      String connectionEncoding = this.connection.getEncoding();
      if ((this.connection.isNoBackslashEscapesSet()) || ((escapeForMBChars) && (this.connection.getUseUnicode()) && (connectionEncoding != null) && (CharsetMapping.isMultibyteCharset(connectionEncoding))))
      {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(x.length * 2 + 3);
        
        bOut.write(120);
        bOut.write(39);
        for (int i = 0; i < x.length; i++)
        {
          int lowBits = (x[i] & 0xFF) / 16;
          int highBits = (x[i] & 0xFF) % 16;
          
          bOut.write(HEX_DIGITS[lowBits]);
          bOut.write(HEX_DIGITS[highBits]);
        }
        bOut.write(39);
        
        setInternal(parameterIndex, bOut.toByteArray());
        
        return;
      }
      int numBytes = x.length;
      
      int pad = 2;
      
      boolean needsIntroducer = (checkForIntroducer) && (this.connection.versionMeetsMinimum(4, 1, 0));
      if (needsIntroducer) {
        pad += 7;
      }
      ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + pad);
      if (needsIntroducer)
      {
        bOut.write(95);
        bOut.write(98);
        bOut.write(105);
        bOut.write(110);
        bOut.write(97);
        bOut.write(114);
        bOut.write(121);
      }
      bOut.write(39);
      for (int i = 0; i < numBytes; i++)
      {
        byte b = x[i];
        switch (b)
        {
        case 0: 
          bOut.write(92);
          bOut.write(48);
          
          break;
        case 10: 
          bOut.write(92);
          bOut.write(110);
          
          break;
        case 13: 
          bOut.write(92);
          bOut.write(114);
          
          break;
        case 92: 
          bOut.write(92);
          bOut.write(92);
          
          break;
        case 39: 
          bOut.write(92);
          bOut.write(39);
          
          break;
        case 34: 
          bOut.write(92);
          bOut.write(34);
          
          break;
        case 26: 
          bOut.write(92);
          bOut.write(90);
          
          break;
        default: 
          bOut.write(b);
        }
      }
      bOut.write(39);
      
      setInternal(parameterIndex, bOut.toByteArray());
    }
  }
  
  protected void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes)
    throws SQLException
  {
    byte[] parameterWithQuotes = new byte[parameterAsBytes.length + 2];
    parameterWithQuotes[0] = 39;
    System.arraycopy(parameterAsBytes, 0, parameterWithQuotes, 1, parameterAsBytes.length);
    
    parameterWithQuotes[(parameterAsBytes.length + 1)] = 39;
    
    setInternal(parameterIndex, parameterWithQuotes);
  }
  
  protected void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes)
    throws SQLException
  {
    setInternal(parameterIndex, parameterAsBytes);
  }
  
  public void setCharacterStream(int parameterIndex, Reader reader, int length)
    throws SQLException
  {
    try
    {
      if (reader == null)
      {
        setNull(parameterIndex, -1);
      }
      else
      {
        char[] c = null;
        int len = 0;
        
        boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
        
        String forcedEncoding = this.connection.getClobCharacterEncoding();
        if ((useLength) && (length != -1))
        {
          c = new char[length];
          
          int numCharsRead = readFully(reader, c, length);
          if (forcedEncoding == null) {
            setString(parameterIndex, new String(c, 0, numCharsRead));
          } else {
            try
            {
              setBytes(parameterIndex, new String(c, 0, numCharsRead).getBytes(forcedEncoding));
            }
            catch (UnsupportedEncodingException uee)
            {
              throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
            }
          }
        }
        else
        {
          c = new char['က'];
          
          StringBuffer buf = new StringBuffer();
          while ((len = reader.read(c)) != -1) {
            buf.append(c, 0, len);
          }
          if (forcedEncoding == null) {
            setString(parameterIndex, buf.toString());
          } else {
            try
            {
              setBytes(parameterIndex, buf.toString().getBytes(forcedEncoding));
            }
            catch (UnsupportedEncodingException uee)
            {
              throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
            }
          }
        }
        this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2005;
      }
    }
    catch (IOException ioEx)
    {
      throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
    }
  }
  
  public void setClob(int i, Clob x)
    throws SQLException
  {
    if (x == null)
    {
      setNull(i, 2005);
    }
    else
    {
      String forcedEncoding = this.connection.getClobCharacterEncoding();
      if (forcedEncoding == null) {
        setString(i, x.getSubString(1L, (int)x.length()));
      } else {
        try
        {
          setBytes(i, x.getSubString(1L, (int)x.length()).getBytes(forcedEncoding));
        }
        catch (UnsupportedEncodingException uee)
        {
          throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
        }
      }
      this.parameterTypes[(i - 1 + getParameterIndexOffset())] = 2005;
    }
  }
  
  public void setDate(int parameterIndex, java.sql.Date x)
    throws SQLException
  {
    setDate(parameterIndex, x, null);
  }
  
  public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, 91);
    }
    else
    {
      checkClosed();
      if (!this.useLegacyDatetimeCode)
      {
        newSetDateInternal(parameterIndex, x, cal);
      }
      else
      {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
        
        setInternal(parameterIndex, dateFormatter.format(x));
        
        this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 91;
      }
    }
  }
  
  public void setDouble(int parameterIndex, double x)
    throws SQLException
  {
    if ((!this.connection.getAllowNanAndInf()) && ((x == Double.POSITIVE_INFINITY) || (x == Double.NEGATIVE_INFINITY) || (Double.isNaN(x)))) {
      throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", "S1009", getExceptionInterceptor());
    }
    setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 8;
  }
  
  public void setFloat(int parameterIndex, float x)
    throws SQLException
  {
    setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 6;
  }
  
  public void setInt(int parameterIndex, int x)
    throws SQLException
  {
    setInternal(parameterIndex, String.valueOf(x));
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 4;
  }
  
  protected final void setInternal(int paramIndex, byte[] val)
    throws SQLException
  {
    if (this.isClosed) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.48"), "S1009", getExceptionInterceptor());
    }
    int parameterIndexOffset = getParameterIndexOffset();
    
    checkBounds(paramIndex, parameterIndexOffset);
    
    this.isStream[(paramIndex - 1 + parameterIndexOffset)] = false;
    this.isNull[(paramIndex - 1 + parameterIndexOffset)] = false;
    this.parameterStreams[(paramIndex - 1 + parameterIndexOffset)] = null;
    this.parameterValues[(paramIndex - 1 + parameterIndexOffset)] = val;
  }
  
  private void checkBounds(int paramIndex, int parameterIndexOffset)
    throws SQLException
  {
    if (paramIndex < 1) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), "S1009", getExceptionInterceptor());
    }
    if (paramIndex > this.parameterCount) {
      throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + this.parameterValues.length + Messages.getString("PreparedStatement.53"), "S1009", getExceptionInterceptor());
    }
    if ((parameterIndexOffset == -1) && (paramIndex == 1)) {
      throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
    }
  }
  
  protected final void setInternal(int paramIndex, String val)
    throws SQLException
  {
    checkClosed();
    
    byte[] parameterAsBytes = null;
    if (this.charConverter != null) {
      parameterAsBytes = this.charConverter.toBytes(val);
    } else {
      parameterAsBytes = StringUtils.getBytes(val, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
    }
    setInternal(paramIndex, parameterAsBytes);
  }
  
  public void setLong(int parameterIndex, long x)
    throws SQLException
  {
    setInternal(parameterIndex, String.valueOf(x));
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -5;
  }
  
  public void setNull(int parameterIndex, int sqlType)
    throws SQLException
  {
    setInternal(parameterIndex, "null");
    this.isNull[(parameterIndex - 1 + getParameterIndexOffset())] = true;
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 0;
  }
  
  public void setNull(int parameterIndex, int sqlType, String arg)
    throws SQLException
  {
    setNull(parameterIndex, sqlType);
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 0;
  }
  
  private void setNumericObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale)
    throws SQLException
  {
    Number parameterAsNum;
    Number parameterAsNum;
    if ((parameterObj instanceof Boolean))
    {
      parameterAsNum = ((Boolean)parameterObj).booleanValue() ? Constants.integerValueOf(1) : Constants.integerValueOf(0);
    }
    else if ((parameterObj instanceof String))
    {
      Number parameterAsNum;
      switch (targetSqlType)
      {
      case -7: 
        Number parameterAsNum;
        if (("1".equals((String)parameterObj)) || ("0".equals((String)parameterObj)))
        {
          parameterAsNum = Integer.valueOf((String)parameterObj);
        }
        else
        {
          boolean parameterAsBoolean = "true".equalsIgnoreCase((String)parameterObj);
          
          parameterAsNum = parameterAsBoolean ? Constants.integerValueOf(1) : Constants.integerValueOf(0);
        }
        break;
      case -6: 
      case 4: 
      case 5: 
        parameterAsNum = Integer.valueOf((String)parameterObj);
        
        break;
      case -5: 
        parameterAsNum = Long.valueOf((String)parameterObj);
        
        break;
      case 7: 
        parameterAsNum = Float.valueOf((String)parameterObj);
        
        break;
      case 6: 
      case 8: 
        parameterAsNum = Double.valueOf((String)parameterObj);
        
        break;
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 0: 
      case 1: 
      case 2: 
      case 3: 
      default: 
        parameterAsNum = new BigDecimal((String)parameterObj);break;
      }
    }
    else
    {
      parameterAsNum = (Number)parameterObj;
    }
    switch (targetSqlType)
    {
    case -7: 
    case -6: 
    case 4: 
    case 5: 
      setInt(parameterIndex, parameterAsNum.intValue());
      
      break;
    case -5: 
      setLong(parameterIndex, parameterAsNum.longValue());
      
      break;
    case 7: 
      setFloat(parameterIndex, parameterAsNum.floatValue());
      
      break;
    case 6: 
    case 8: 
      setDouble(parameterIndex, parameterAsNum.doubleValue());
      
      break;
    case 2: 
    case 3: 
      if ((parameterAsNum instanceof BigDecimal))
      {
        BigDecimal scaledBigDecimal = null;
        try
        {
          scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale);
        }
        catch (ArithmeticException ex)
        {
          try
          {
            scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale, 4);
          }
          catch (ArithmeticException arEx)
          {
            throw SQLError.createSQLException("Can't set scale of '" + scale + "' for DECIMAL argument '" + parameterAsNum + "'", "S1009", getExceptionInterceptor());
          }
        }
        setBigDecimal(parameterIndex, scaledBigDecimal);
      }
      else if ((parameterAsNum instanceof BigInteger))
      {
        setBigDecimal(parameterIndex, new BigDecimal((BigInteger)parameterAsNum, scale));
      }
      else
      {
        setBigDecimal(parameterIndex, new BigDecimal(parameterAsNum.doubleValue()));
      }
      break;
    }
  }
  
  public void setObject(int parameterIndex, Object parameterObj)
    throws SQLException
  {
    if (parameterObj == null) {
      setNull(parameterIndex, 1111);
    } else if ((parameterObj instanceof Byte)) {
      setInt(parameterIndex, ((Byte)parameterObj).intValue());
    } else if ((parameterObj instanceof String)) {
      setString(parameterIndex, (String)parameterObj);
    } else if ((parameterObj instanceof BigDecimal)) {
      setBigDecimal(parameterIndex, (BigDecimal)parameterObj);
    } else if ((parameterObj instanceof Short)) {
      setShort(parameterIndex, ((Short)parameterObj).shortValue());
    } else if ((parameterObj instanceof Integer)) {
      setInt(parameterIndex, ((Integer)parameterObj).intValue());
    } else if ((parameterObj instanceof Long)) {
      setLong(parameterIndex, ((Long)parameterObj).longValue());
    } else if ((parameterObj instanceof Float)) {
      setFloat(parameterIndex, ((Float)parameterObj).floatValue());
    } else if ((parameterObj instanceof Double)) {
      setDouble(parameterIndex, ((Double)parameterObj).doubleValue());
    } else if ((parameterObj instanceof byte[])) {
      setBytes(parameterIndex, (byte[])parameterObj);
    } else if ((parameterObj instanceof java.sql.Date)) {
      setDate(parameterIndex, (java.sql.Date)parameterObj);
    } else if ((parameterObj instanceof Time)) {
      setTime(parameterIndex, (Time)parameterObj);
    } else if ((parameterObj instanceof Timestamp)) {
      setTimestamp(parameterIndex, (Timestamp)parameterObj);
    } else if ((parameterObj instanceof Boolean)) {
      setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
    } else if ((parameterObj instanceof InputStream)) {
      setBinaryStream(parameterIndex, (InputStream)parameterObj, -1);
    } else if ((parameterObj instanceof Blob)) {
      setBlob(parameterIndex, (Blob)parameterObj);
    } else if ((parameterObj instanceof Clob)) {
      setClob(parameterIndex, (Clob)parameterObj);
    } else if ((this.connection.getTreatUtilDateAsTimestamp()) && ((parameterObj instanceof java.util.Date))) {
      setTimestamp(parameterIndex, new Timestamp(((java.util.Date)parameterObj).getTime()));
    } else if ((parameterObj instanceof BigInteger)) {
      setString(parameterIndex, parameterObj.toString());
    } else {
      setSerializableObject(parameterIndex, parameterObj);
    }
  }
  
  public void setObject(int parameterIndex, Object parameterObj, int targetSqlType)
    throws SQLException
  {
    if (!(parameterObj instanceof BigDecimal)) {
      setObject(parameterIndex, parameterObj, targetSqlType, 0);
    } else {
      setObject(parameterIndex, parameterObj, targetSqlType, ((BigDecimal)parameterObj).scale());
    }
  }
  
  public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale)
    throws SQLException
  {
    if (parameterObj == null) {
      setNull(parameterIndex, 1111);
    } else {
      try
      {
        switch (targetSqlType)
        {
        case 16: 
          if ((parameterObj instanceof Boolean))
          {
            setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
          }
          else if ((parameterObj instanceof String))
          {
            setBoolean(parameterIndex, ("true".equalsIgnoreCase((String)parameterObj)) || (!"0".equalsIgnoreCase((String)parameterObj)));
          }
          else if ((parameterObj instanceof Number))
          {
            int intValue = ((Number)parameterObj).intValue();
            
            setBoolean(parameterIndex, intValue != 0);
          }
          else
          {
            throw SQLError.createSQLException("No conversion from " + parameterObj.getClass().getName() + " to Types.BOOLEAN possible.", "S1009", getExceptionInterceptor());
          }
          break;
        case -7: 
        case -6: 
        case -5: 
        case 2: 
        case 3: 
        case 4: 
        case 5: 
        case 6: 
        case 7: 
        case 8: 
          setNumericObject(parameterIndex, parameterObj, targetSqlType, scale);
          
          break;
        case -1: 
        case 1: 
        case 12: 
          if ((parameterObj instanceof BigDecimal)) {
            setString(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString((BigDecimal)parameterObj)));
          } else {
            setString(parameterIndex, parameterObj.toString());
          }
          break;
        case 2005: 
          if ((parameterObj instanceof Clob)) {
            setClob(parameterIndex, (Clob)parameterObj);
          } else {
            setString(parameterIndex, parameterObj.toString());
          }
          break;
        case -4: 
        case -3: 
        case -2: 
        case 2004: 
          if ((parameterObj instanceof byte[])) {
            setBytes(parameterIndex, (byte[])parameterObj);
          } else if ((parameterObj instanceof Blob)) {
            setBlob(parameterIndex, (Blob)parameterObj);
          } else {
            setBytes(parameterIndex, StringUtils.getBytes(parameterObj.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
          }
          break;
        case 91: 
        case 93: 
          java.util.Date parameterAsDate;
          java.util.Date parameterAsDate;
          if ((parameterObj instanceof String))
          {
            ParsePosition pp = new ParsePosition(0);
            DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, false), Locale.US);
            
            parameterAsDate = sdf.parse((String)parameterObj, pp);
          }
          else
          {
            parameterAsDate = (java.util.Date)parameterObj;
          }
          switch (targetSqlType)
          {
          case 91: 
            if ((parameterAsDate instanceof java.sql.Date)) {
              setDate(parameterIndex, (java.sql.Date)parameterAsDate);
            } else {
              setDate(parameterIndex, new java.sql.Date(parameterAsDate.getTime()));
            }
            break;
          case 93: 
            if ((parameterAsDate instanceof Timestamp)) {
              setTimestamp(parameterIndex, (Timestamp)parameterAsDate);
            } else {
              setTimestamp(parameterIndex, new Timestamp(parameterAsDate.getTime()));
            }
            break;
          }
          break;
        case 92: 
          if ((parameterObj instanceof String))
          {
            DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, true), Locale.US);
            
            setTime(parameterIndex, new Time(sdf.parse((String)parameterObj).getTime()));
          }
          else if ((parameterObj instanceof Timestamp))
          {
            Timestamp xT = (Timestamp)parameterObj;
            setTime(parameterIndex, new Time(xT.getTime()));
          }
          else
          {
            setTime(parameterIndex, (Time)parameterObj);
          }
          break;
        case 1111: 
          setSerializableObject(parameterIndex, parameterObj);
          
          break;
        default: 
          throw SQLError.createSQLException(Messages.getString("PreparedStatement.16"), "S1000", getExceptionInterceptor());
        }
      }
      catch (Exception ex)
      {
        if ((ex instanceof SQLException)) {
          throw ((SQLException)ex);
        }
        SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.17") + parameterObj.getClass().toString() + Messages.getString("PreparedStatement.18") + ex.getClass().getName() + Messages.getString("PreparedStatement.19") + ex.getMessage(), "S1000", getExceptionInterceptor());
        
        sqlEx.initCause(ex);
        
        throw sqlEx;
      }
    }
  }
  
  protected int setOneBatchedParameterSet(java.sql.PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet)
    throws SQLException
  {
    BatchParams paramArg = (BatchParams)paramSet;
    
    boolean[] isNullBatch = paramArg.isNull;
    boolean[] isStreamBatch = paramArg.isStream;
    for (int j = 0; j < isNullBatch.length; j++) {
      if (isNullBatch[j] != 0) {
        batchedStatement.setNull(batchedParamIndex++, 0);
      } else if (isStreamBatch[j] != 0) {
        batchedStatement.setBinaryStream(batchedParamIndex++, paramArg.parameterStreams[j], paramArg.streamLengths[j]);
      } else {
        ((PreparedStatement)batchedStatement).setBytesNoEscapeNoQuotes(batchedParamIndex++, paramArg.parameterStrings[j]);
      }
    }
    return batchedParamIndex;
  }
  
  public void setRef(int i, Ref x)
    throws SQLException
  {
    throw SQLError.notImplemented();
  }
  
  void setResultSetConcurrency(int concurrencyFlag)
  {
    this.resultSetConcurrency = concurrencyFlag;
  }
  
  void setResultSetType(int typeFlag)
  {
    this.resultSetType = typeFlag;
  }
  
  protected void setRetrieveGeneratedKeys(boolean retrieveGeneratedKeys)
  {
    this.retrieveGeneratedKeys = retrieveGeneratedKeys;
  }
  
  private final void setSerializableObject(int parameterIndex, Object parameterObj)
    throws SQLException
  {
    try
    {
      ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
      ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
      objectOut.writeObject(parameterObj);
      objectOut.flush();
      objectOut.close();
      bytesOut.flush();
      bytesOut.close();
      
      byte[] buf = bytesOut.toByteArray();
      ByteArrayInputStream bytesIn = new ByteArrayInputStream(buf);
      setBinaryStream(parameterIndex, bytesIn, buf.length);
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -2;
    }
    catch (Exception ex)
    {
      SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.54") + ex.getClass().getName(), "S1009", getExceptionInterceptor());
      
      sqlEx.initCause(ex);
      
      throw sqlEx;
    }
  }
  
  public void setShort(int parameterIndex, short x)
    throws SQLException
  {
    setInternal(parameterIndex, String.valueOf(x));
    
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 5;
  }
  
  public void setString(int parameterIndex, String x)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, 1);
    }
    else
    {
      checkClosed();
      
      int stringLength = x.length();
      if (this.connection.isNoBackslashEscapesSet())
      {
        boolean needsHexEscape = isEscapeNeededForString(x, stringLength);
        if (!needsHexEscape)
        {
          byte[] parameterAsBytes = null;
          
          StringBuffer quotedString = new StringBuffer(x.length() + 2);
          quotedString.append('\'');
          quotedString.append(x);
          quotedString.append('\'');
          if (!this.isLoadDataQuery) {
            parameterAsBytes = StringUtils.getBytes(quotedString.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
          } else {
            parameterAsBytes = quotedString.toString().getBytes();
          }
          setInternal(parameterIndex, parameterAsBytes);
        }
        else
        {
          byte[] parameterAsBytes = null;
          if (!this.isLoadDataQuery) {
            parameterAsBytes = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
          } else {
            parameterAsBytes = x.getBytes();
          }
          setBytes(parameterIndex, parameterAsBytes);
        }
        return;
      }
      String parameterAsString = x;
      boolean needsQuoted = true;
      if ((this.isLoadDataQuery) || (isEscapeNeededForString(x, stringLength)))
      {
        needsQuoted = false;
        
        StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D));
        
        buf.append('\'');
        for (int i = 0; i < stringLength; i++)
        {
          char c = x.charAt(i);
          switch (c)
          {
          case '\000': 
            buf.append('\\');
            buf.append('0');
            
            break;
          case '\n': 
            buf.append('\\');
            buf.append('n');
            
            break;
          case '\r': 
            buf.append('\\');
            buf.append('r');
            
            break;
          case '\\': 
            buf.append('\\');
            buf.append('\\');
            
            break;
          case '\'': 
            buf.append('\\');
            buf.append('\'');
            
            break;
          case '"': 
            if (this.usingAnsiMode) {
              buf.append('\\');
            }
            buf.append('"');
            
            break;
          case '\032': 
            buf.append('\\');
            buf.append('Z');
            
            break;
          case '¥': 
          case '₩': 
            if (this.charsetEncoder != null)
            {
              CharBuffer cbuf = CharBuffer.allocate(1);
              ByteBuffer bbuf = ByteBuffer.allocate(1);
              cbuf.put(c);
              cbuf.position(0);
              this.charsetEncoder.encode(cbuf, bbuf, true);
              if (bbuf.get(0) == 92) {
                buf.append('\\');
              }
            }
            break;
          }
          buf.append(c);
        }
        buf.append('\'');
        
        parameterAsString = buf.toString();
      }
      byte[] parameterAsBytes = null;
      if (!this.isLoadDataQuery)
      {
        if (needsQuoted) {
          parameterAsBytes = StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
        } else {
          parameterAsBytes = StringUtils.getBytes(parameterAsString, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
        }
      }
      else {
        parameterAsBytes = parameterAsString.getBytes();
      }
      setInternal(parameterIndex, parameterAsBytes);
      
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 12;
    }
  }
  
  private boolean isEscapeNeededForString(String x, int stringLength)
  {
    boolean needsHexEscape = false;
    for (int i = 0; i < stringLength; i++)
    {
      char c = x.charAt(i);
      switch (c)
      {
      case '\000': 
        needsHexEscape = true;
        break;
      case '\n': 
        needsHexEscape = true;
        
        break;
      case '\r': 
        needsHexEscape = true;
        break;
      case '\\': 
        needsHexEscape = true;
        
        break;
      case '\'': 
        needsHexEscape = true;
        
        break;
      case '"': 
        needsHexEscape = true;
        
        break;
      case '\032': 
        needsHexEscape = true;
      }
      if (needsHexEscape) {
        break;
      }
    }
    return needsHexEscape;
  }
  
  public void setTime(int parameterIndex, Time x, Calendar cal)
    throws SQLException
  {
    setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
  }
  
  public void setTime(int parameterIndex, Time x)
    throws SQLException
  {
    setTimeInternal(parameterIndex, x, null, Util.getDefaultTimeZone(), false);
  }
  
  private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, 92);
    }
    else
    {
      checkClosed();
      if (!this.useLegacyDatetimeCode)
      {
        newSetTimeInternal(parameterIndex, x, targetCalendar);
      }
      else
      {
        Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
        synchronized (sessionCalendar)
        {
          x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
        }
        setInternal(parameterIndex, "'" + x.toString() + "'");
      }
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 92;
    }
  }
  
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
    throws SQLException
  {
    setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
  }
  
  public void setTimestamp(int parameterIndex, Timestamp x)
    throws SQLException
  {
    setTimestampInternal(parameterIndex, x, null, Util.getDefaultTimeZone(), false);
  }
  
  private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, 93);
    }
    else
    {
      checkClosed();
      if (!this.useLegacyDatetimeCode)
      {
        newSetTimestampInternal(parameterIndex, x, targetCalendar);
      }
      else
      {
        String timestampString = null;
        
        Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
        synchronized (sessionCalendar)
        {
          x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
        }
        if (this.connection.getUseSSPSCompatibleTimezoneShift()) {
          doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
        } else {
          synchronized (this)
          {
            if (this.tsdf == null) {
              this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''", Locale.US);
            }
            timestampString = this.tsdf.format(x);
            
            setInternal(parameterIndex, timestampString);
          }
        }
      }
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 93;
    }
  }
  
  private synchronized void newSetTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar)
    throws SQLException
  {
    if (this.tsdf == null) {
      this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
    }
    String timestampString = null;
    if (targetCalendar != null)
    {
      targetCalendar.setTime(x);
      this.tsdf.setTimeZone(targetCalendar.getTimeZone());
      
      timestampString = this.tsdf.format(x);
    }
    else
    {
      this.tsdf.setTimeZone(this.connection.getServerTimezoneTZ());
      timestampString = this.tsdf.format(x);
    }
    StringBuffer buf = new StringBuffer();
    buf.append(timestampString);
    buf.append('.');
    buf.append(formatNanos(x.getNanos()));
    buf.append('\'');
    
    setInternal(parameterIndex, buf.toString());
  }
  
  private String formatNanos(int nanos)
  {
    return "0";
  }
  
  private synchronized void newSetTimeInternal(int parameterIndex, Time x, Calendar targetCalendar)
    throws SQLException
  {
    if (this.tdf == null) {
      this.tdf = new SimpleDateFormat("''HH:mm:ss''", Locale.US);
    }
    String timeString = null;
    if (targetCalendar != null)
    {
      targetCalendar.setTime(x);
      this.tdf.setTimeZone(targetCalendar.getTimeZone());
      
      timeString = this.tdf.format(x);
    }
    else
    {
      this.tdf.setTimeZone(this.connection.getServerTimezoneTZ());
      timeString = this.tdf.format(x);
    }
    setInternal(parameterIndex, timeString);
  }
  
  private synchronized void newSetDateInternal(int parameterIndex, java.sql.Date x, Calendar targetCalendar)
    throws SQLException
  {
    if (this.ddf == null) {
      this.ddf = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
    }
    String timeString = null;
    if (targetCalendar != null)
    {
      targetCalendar.setTime(x);
      this.ddf.setTimeZone(targetCalendar.getTimeZone());
      
      timeString = this.ddf.format(x);
    }
    else
    {
      this.ddf.setTimeZone(this.connection.getServerTimezoneTZ());
      timeString = this.ddf.format(x);
    }
    setInternal(parameterIndex, timeString);
  }
  
  private void doSSPSCompatibleTimezoneShift(int parameterIndex, Timestamp x, Calendar sessionCalendar)
    throws SQLException
  {
    Calendar sessionCalendar2 = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
    synchronized (sessionCalendar2)
    {
      java.util.Date oldTime = sessionCalendar2.getTime();
      try
      {
        sessionCalendar2.setTime(x);
        
        int year = sessionCalendar2.get(1);
        int month = sessionCalendar2.get(2) + 1;
        int date = sessionCalendar2.get(5);
        
        int hour = sessionCalendar2.get(11);
        int minute = sessionCalendar2.get(12);
        int seconds = sessionCalendar2.get(13);
        
        StringBuffer tsBuf = new StringBuffer();
        
        tsBuf.append('\'');
        tsBuf.append(year);
        
        tsBuf.append("-");
        if (month < 10) {
          tsBuf.append('0');
        }
        tsBuf.append(month);
        
        tsBuf.append('-');
        if (date < 10) {
          tsBuf.append('0');
        }
        tsBuf.append(date);
        
        tsBuf.append(' ');
        if (hour < 10) {
          tsBuf.append('0');
        }
        tsBuf.append(hour);
        
        tsBuf.append(':');
        if (minute < 10) {
          tsBuf.append('0');
        }
        tsBuf.append(minute);
        
        tsBuf.append(':');
        if (seconds < 10) {
          tsBuf.append('0');
        }
        tsBuf.append(seconds);
        
        tsBuf.append('.');
        tsBuf.append(formatNanos(x.getNanos()));
        tsBuf.append('\'');
        
        setInternal(parameterIndex, tsBuf.toString());
      }
      finally
      {
        sessionCalendar.setTime(oldTime);
      }
    }
  }
  
  /**
   * @deprecated
   */
  public void setUnicodeStream(int parameterIndex, InputStream x, int length)
    throws SQLException
  {
    if (x == null)
    {
      setNull(parameterIndex, 12);
    }
    else
    {
      setBinaryStream(parameterIndex, x, length);
      
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2005;
    }
  }
  
  public void setURL(int parameterIndex, URL arg)
    throws SQLException
  {
    if (arg != null)
    {
      setString(parameterIndex, arg.toString());
      
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 70;
    }
    else
    {
      setNull(parameterIndex, 1);
    }
  }
  
  private final void streamToBytes(Buffer packet, InputStream in, boolean escape, int streamLength, boolean useLength)
    throws SQLException
  {
    try
    {
      String connectionEncoding = this.connection.getEncoding();
      
      boolean hexEscape = false;
      if ((this.connection.isNoBackslashEscapesSet()) || ((this.connection.getUseUnicode()) && (connectionEncoding != null) && (CharsetMapping.isMultibyteCharset(connectionEncoding)) && (!this.connection.parserKnowsUnicode()))) {
        hexEscape = true;
      }
      if (streamLength == -1) {
        useLength = false;
      }
      int bc = -1;
      if (useLength) {
        bc = readblock(in, this.streamConvertBuf, streamLength);
      } else {
        bc = readblock(in, this.streamConvertBuf);
      }
      int lengthLeftToRead = streamLength - bc;
      if (hexEscape) {
        packet.writeStringNoNull("x");
      } else if (this.connection.getIO().versionMeetsMinimum(4, 1, 0)) {
        packet.writeStringNoNull("_binary");
      }
      if (escape) {
        packet.writeByte((byte)39);
      }
      while (bc > 0)
      {
        if (hexEscape) {
          hexEscapeBlock(this.streamConvertBuf, packet, bc);
        } else if (escape) {
          escapeblockFast(this.streamConvertBuf, packet, bc);
        } else {
          packet.writeBytesNoNull(this.streamConvertBuf, 0, bc);
        }
        if (useLength)
        {
          bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
          if (bc > 0) {
            lengthLeftToRead -= bc;
          }
        }
        else
        {
          bc = readblock(in, this.streamConvertBuf);
        }
      }
      if (escape) {
        packet.writeByte((byte)39);
      }
    }
    finally
    {
      if (this.connection.getAutoClosePStmtStreams())
      {
        try
        {
          in.close();
        }
        catch (IOException ioEx) {}
        in = null;
      }
    }
  }
  
  private final byte[] streamToBytes(InputStream in, boolean escape, int streamLength, boolean useLength)
    throws SQLException
  {
    try
    {
      if (streamLength == -1) {
        useLength = false;
      }
      ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
      
      int bc = -1;
      if (useLength) {
        bc = readblock(in, this.streamConvertBuf, streamLength);
      } else {
        bc = readblock(in, this.streamConvertBuf);
      }
      int lengthLeftToRead = streamLength - bc;
      if (escape)
      {
        if (this.connection.versionMeetsMinimum(4, 1, 0))
        {
          bytesOut.write(95);
          bytesOut.write(98);
          bytesOut.write(105);
          bytesOut.write(110);
          bytesOut.write(97);
          bytesOut.write(114);
          bytesOut.write(121);
        }
        bytesOut.write(39);
      }
      while (bc > 0)
      {
        if (escape) {
          escapeblockFast(this.streamConvertBuf, bytesOut, bc);
        } else {
          bytesOut.write(this.streamConvertBuf, 0, bc);
        }
        if (useLength)
        {
          bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
          if (bc > 0) {
            lengthLeftToRead -= bc;
          }
        }
        else
        {
          bc = readblock(in, this.streamConvertBuf);
        }
      }
      if (escape) {
        bytesOut.write(39);
      }
      return bytesOut.toByteArray();
    }
    finally
    {
      if (this.connection.getAutoClosePStmtStreams())
      {
        try
        {
          in.close();
        }
        catch (IOException ioEx) {}
        in = null;
      }
    }
  }
  
  public String toString()
  {
    StringBuffer buf = new StringBuffer();
    buf.append(super.toString());
    buf.append(": ");
    try
    {
      buf.append(asSql());
    }
    catch (SQLException sqlEx)
    {
      buf.append("EXCEPTION: " + sqlEx.toString());
    }
    return buf.toString();
  }
  
  public synchronized boolean isClosed()
    throws SQLException
  {
    return this.isClosed;
  }
  
  protected int getParameterIndexOffset()
  {
    return 0;
  }
  
  public void setAsciiStream(int parameterIndex, InputStream x)
    throws SQLException
  {
    setAsciiStream(parameterIndex, x, -1);
  }
  
  public void setAsciiStream(int parameterIndex, InputStream x, long length)
    throws SQLException
  {
    setAsciiStream(parameterIndex, x, (int)length);
    this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2005;
  }
  
  public void setBinaryStream(int parameterIndex, InputStream x)
    throws SQLException
  {
    setBinaryStream(parameterIndex, x, -1);
  }
  
  public void setBinaryStream(int parameterIndex, InputStream x, long length)
    throws SQLException
  {
    setBinaryStream(parameterIndex, x, (int)length);
  }
  
  public void setBlob(int parameterIndex, InputStream inputStream)
    throws SQLException
  {
    setBinaryStream(parameterIndex, inputStream);
  }
  
  public void setCharacterStream(int parameterIndex, Reader reader)
    throws SQLException
  {
    setCharacterStream(parameterIndex, reader, -1);
  }
  
  public void setCharacterStream(int parameterIndex, Reader reader, long length)
    throws SQLException
  {
    setCharacterStream(parameterIndex, reader, (int)length);
  }
  
  public void setClob(int parameterIndex, Reader reader)
    throws SQLException
  {
    setCharacterStream(parameterIndex, reader);
  }
  
  public void setClob(int parameterIndex, Reader reader, long length)
    throws SQLException
  {
    setCharacterStream(parameterIndex, reader, length);
  }
  
  public void setNCharacterStream(int parameterIndex, Reader value)
    throws SQLException
  {
    setNCharacterStream(parameterIndex, value, -1L);
  }
  
  public void setNString(int parameterIndex, String x)
    throws SQLException
  {
    if ((this.charEncoding.equalsIgnoreCase("UTF-8")) || (this.charEncoding.equalsIgnoreCase("utf8")))
    {
      setString(parameterIndex, x);
      return;
    }
    if (x == null)
    {
      setNull(parameterIndex, 1);
    }
    else
    {
      int stringLength = x.length();
      
      StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D + 4.0D));
      buf.append("_utf8");
      buf.append('\'');
      for (int i = 0; i < stringLength; i++)
      {
        char c = x.charAt(i);
        switch (c)
        {
        case '\000': 
          buf.append('\\');
          buf.append('0');
          
          break;
        case '\n': 
          buf.append('\\');
          buf.append('n');
          
          break;
        case '\r': 
          buf.append('\\');
          buf.append('r');
          
          break;
        case '\\': 
          buf.append('\\');
          buf.append('\\');
          
          break;
        case '\'': 
          buf.append('\\');
          buf.append('\'');
          
          break;
        case '"': 
          if (this.usingAnsiMode) {
            buf.append('\\');
          }
          buf.append('"');
          
          break;
        case '\032': 
          buf.append('\\');
          buf.append('Z');
          
          break;
        default: 
          buf.append(c);
        }
      }
      buf.append('\'');
      
      String parameterAsString = buf.toString();
      
      byte[] parameterAsBytes = null;
      if (!this.isLoadDataQuery) {
        parameterAsBytes = StringUtils.getBytes(parameterAsString, this.connection.getCharsetConverter("UTF-8"), "UTF-8", this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
      } else {
        parameterAsBytes = parameterAsString.getBytes();
      }
      setInternal(parameterIndex, parameterAsBytes);
      
      this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = -9;
    }
  }
  
  public void setNCharacterStream(int parameterIndex, Reader reader, long length)
    throws SQLException
  {
    try
    {
      if (reader == null)
      {
        setNull(parameterIndex, -1);
      }
      else
      {
        char[] c = null;
        int len = 0;
        
        boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
        if ((useLength) && (length != -1L))
        {
          c = new char[(int)length];
          
          int numCharsRead = readFully(reader, c, (int)length);
          
          setNString(parameterIndex, new String(c, 0, numCharsRead));
        }
        else
        {
          c = new char['က'];
          
          StringBuffer buf = new StringBuffer();
          while ((len = reader.read(c)) != -1) {
            buf.append(c, 0, len);
          }
          setNString(parameterIndex, buf.toString());
        }
        this.parameterTypes[(parameterIndex - 1 + getParameterIndexOffset())] = 2011;
      }
    }
    catch (IOException ioEx)
    {
      throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
    }
  }
  
  public void setNClob(int parameterIndex, Reader reader)
    throws SQLException
  {
    setNCharacterStream(parameterIndex, reader);
  }
  
  public void setNClob(int parameterIndex, Reader reader, long length)
    throws SQLException
  {
    if (reader == null) {
      setNull(parameterIndex, -1);
    } else {
      setNCharacterStream(parameterIndex, reader, length);
    }
  }
  
  public ParameterBindings getParameterBindings()
    throws SQLException
  {
    return new EmulatedPreparedStatementBindings();
  }
  
  class EmulatedPreparedStatementBindings
    implements ParameterBindings
  {
    private ResultSetImpl bindingsAsRs;
    private boolean[] parameterIsNull;
    
    public EmulatedPreparedStatementBindings()
      throws SQLException
    {
      List rows = new ArrayList();
      this.parameterIsNull = new boolean[PreparedStatement.this.parameterCount];
      System.arraycopy(PreparedStatement.this.isNull, 0, this.parameterIsNull, 0, PreparedStatement.this.parameterCount);
      
      byte[][] rowData = new byte[PreparedStatement.this.parameterCount][];
      Field[] typeMetadata = new Field[PreparedStatement.this.parameterCount];
      for (int i = 0; i < PreparedStatement.this.parameterCount; i++)
      {
        if (PreparedStatement.this.batchCommandIndex == -1) {
          rowData[i] = PreparedStatement.this.getBytesRepresentation(i);
        } else {
          rowData[i] = PreparedStatement.this.getBytesRepresentationForBatch(i, PreparedStatement.this.batchCommandIndex);
        }
        int charsetIndex = 0;
        if ((PreparedStatement.this.parameterTypes[i] == -2) || (PreparedStatement.this.parameterTypes[i] == 2004))
        {
          charsetIndex = 63;
        }
        else
        {
          String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(PreparedStatement.this.connection.getEncoding(), PreparedStatement.this.connection);
          
          charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(mysqlEncodingName);
        }
        Field parameterMetadata = new Field(null, "parameter_" + (i + 1), charsetIndex, PreparedStatement.this.parameterTypes[i], rowData[i].length);
        
        parameterMetadata.setConnection(PreparedStatement.this.connection);
        typeMetadata[i] = parameterMetadata;
      }
      rows.add(new ByteArrayRow(rowData, PreparedStatement.this.getExceptionInterceptor()));
      
      this.bindingsAsRs = new ResultSetImpl(PreparedStatement.this.connection.getCatalog(), typeMetadata, new RowDataStatic(rows), PreparedStatement.this.connection, null);
      
      this.bindingsAsRs.next();
    }
    
    public Array getArray(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getArray(parameterIndex);
    }
    
    public InputStream getAsciiStream(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getAsciiStream(parameterIndex);
    }
    
    public BigDecimal getBigDecimal(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getBigDecimal(parameterIndex);
    }
    
    public InputStream getBinaryStream(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getBinaryStream(parameterIndex);
    }
    
    public Blob getBlob(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getBlob(parameterIndex);
    }
    
    public boolean getBoolean(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getBoolean(parameterIndex);
    }
    
    public byte getByte(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getByte(parameterIndex);
    }
    
    public byte[] getBytes(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getBytes(parameterIndex);
    }
    
    public Reader getCharacterStream(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getCharacterStream(parameterIndex);
    }
    
    public Clob getClob(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getClob(parameterIndex);
    }
    
    public java.sql.Date getDate(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getDate(parameterIndex);
    }
    
    public double getDouble(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getDouble(parameterIndex);
    }
    
    public float getFloat(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getFloat(parameterIndex);
    }
    
    public int getInt(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getInt(parameterIndex);
    }
    
    public long getLong(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getLong(parameterIndex);
    }
    
    public Reader getNCharacterStream(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getCharacterStream(parameterIndex);
    }
    
    public Reader getNClob(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getCharacterStream(parameterIndex);
    }
    
    public Object getObject(int parameterIndex)
      throws SQLException
    {
      PreparedStatement.this.checkBounds(parameterIndex, 0);
      if (this.parameterIsNull[(parameterIndex - 1)] != 0) {
        return null;
      }
      switch (PreparedStatement.this.parameterTypes[(parameterIndex - 1)])
      {
      case -6: 
        return new Byte(getByte(parameterIndex));
      case 5: 
        return new Short(getShort(parameterIndex));
      case 4: 
        return new Integer(getInt(parameterIndex));
      case -5: 
        return new Long(getLong(parameterIndex));
      case 6: 
        return new Float(getFloat(parameterIndex));
      case 8: 
        return new Double(getDouble(parameterIndex));
      }
      return this.bindingsAsRs.getObject(parameterIndex);
    }
    
    public Ref getRef(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getRef(parameterIndex);
    }
    
    public short getShort(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getShort(parameterIndex);
    }
    
    public String getString(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getString(parameterIndex);
    }
    
    public Time getTime(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getTime(parameterIndex);
    }
    
    public Timestamp getTimestamp(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getTimestamp(parameterIndex);
    }
    
    public URL getURL(int parameterIndex)
      throws SQLException
    {
      return this.bindingsAsRs.getURL(parameterIndex);
    }
    
    public boolean isNull(int parameterIndex)
      throws SQLException
    {
      PreparedStatement.this.checkBounds(parameterIndex, 0);
      
      return this.parameterIsNull[(parameterIndex - 1)];
    }
  }
  
  public String getPreparedSql()
  {
    if (this.rewrittenBatchSize == 0) {
      return this.originalSql;
    }
    try
    {
      return this.parseInfo.getSqlForBatch(this.parseInfo);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public int getUpdateCount()
    throws SQLException
  {
    int count = super.getUpdateCount();
    if ((containsOnDuplicateKeyUpdateInSQL()) && (this.compensateForOnDuplicateKeyUpdate)) {
      if ((count == 2) || (count == 0)) {
        count = 1;
      }
    }
    return count;
  }
  
  protected static boolean canRewrite(String sql, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementStartPos)
  {
    boolean rewritableOdku = true;
    if (isOnDuplicateKeyUpdate)
    {
      int updateClausePos = StringUtils.indexOfIgnoreCase(locationOfOnDuplicateKeyUpdate, sql, " UPDATE ");
      if (updateClausePos != -1) {
        rewritableOdku = StringUtils.indexOfIgnoreCaseRespectMarker(updateClausePos, sql, "LAST_INSERT_ID", "\"'`", "\"'`", false) == -1;
      }
    }
    return (StringUtils.startsWithIgnoreCaseAndWs(sql, "INSERT", statementStartPos)) && (StringUtils.indexOfIgnoreCaseRespectMarker(statementStartPos, sql, "SELECT", "\"'`", "\"'`", false) == -1) && (rewritableOdku);
  }
  
  /* Error */
  protected int[] executePreparedBatchAsMultiStatement(int batchTimeout)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 185	com/mysql/jdbc/PreparedStatement:connection	Lcom/mysql/jdbc/MySQLConnection;
    //   4: invokeinterface 430 1 0
    //   9: dup
    //   10: astore_2
    //   11: monitorenter
    //   12: aload_0
    //   13: getfield 589	com/mysql/jdbc/PreparedStatement:batchedValuesClause	Ljava/lang/String;
    //   16: ifnonnull +30 -> 46
    //   19: aload_0
    //   20: new 352	java/lang/StringBuilder
    //   23: dup
    //   24: invokespecial 353	java/lang/StringBuilder:<init>	()V
    //   27: aload_0
    //   28: getfield 159	com/mysql/jdbc/PreparedStatement:originalSql	Ljava/lang/String;
    //   31: invokevirtual 358	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: ldc_w 591
    //   37: invokevirtual 358	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: invokevirtual 362	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   43: putfield 589	com/mysql/jdbc/PreparedStatement:batchedValuesClause	Ljava/lang/String;
    //   46: aload_0
    //   47: getfield 185	com/mysql/jdbc/PreparedStatement:connection	Lcom/mysql/jdbc/MySQLConnection;
    //   50: astore_3
    //   51: aload_3
    //   52: invokeinterface 594 1 0
    //   57: istore 4
    //   59: aconst_null
    //   60: astore 5
    //   62: aload_0
    //   63: invokevirtual 445	com/mysql/jdbc/PreparedStatement:clearWarnings	()V
    //   66: aload_0
    //   67: getfield 272	com/mysql/jdbc/PreparedStatement:batchedArgs	Ljava/util/List;
    //   70: invokeinterface 556 1 0
    //   75: istore 6
    //   77: aload_0
    //   78: getfield 436	com/mysql/jdbc/PreparedStatement:retrieveGeneratedKeys	Z
    //   81: ifeq +16 -> 97
    //   84: aload_0
    //   85: new 274	java/util/ArrayList
    //   88: dup
    //   89: iload 6
    //   91: invokespecial 596	java/util/ArrayList:<init>	(I)V
    //   94: putfield 460	com/mysql/jdbc/PreparedStatement:batchedGeneratedKeys	Ljava/util/ArrayList;
    //   97: aload_0
    //   98: iload 6
    //   100: invokevirtual 600	com/mysql/jdbc/PreparedStatement:computeBatchSize	(I)I
    //   103: istore 7
    //   105: iload 6
    //   107: iload 7
    //   109: if_icmpge +7 -> 116
    //   112: iload 6
    //   114: istore 7
    //   116: aconst_null
    //   117: astore 8
    //   119: iconst_1
    //   120: istore 9
    //   122: iconst_0
    //   123: istore 10
    //   125: iconst_0
    //   126: istore 11
    //   128: iconst_0
    //   129: istore 12
    //   131: iload 6
    //   133: newarray <illegal type>
    //   135: astore 13
    //   137: aconst_null
    //   138: astore 14
    //   140: iload 4
    //   142: ifne +12 -> 154
    //   145: aload_3
    //   146: invokeinterface 604 1 0
    //   151: invokevirtual 609	com/mysql/jdbc/MysqlIO:enableMultiQueries	()V
    //   154: aload_0
    //   155: getfield 436	com/mysql/jdbc/PreparedStatement:retrieveGeneratedKeys	Z
    //   158: ifeq +21 -> 179
    //   161: aload_3
    //   162: aload_0
    //   163: iload 7
    //   165: invokespecial 613	com/mysql/jdbc/PreparedStatement:generateMultiStatementForBatch	(I)Ljava/lang/String;
    //   168: iconst_1
    //   169: invokeinterface 617 3 0
    //   174: astore 8
    //   176: goto +17 -> 193
    //   179: aload_3
    //   180: aload_0
    //   181: iload 7
    //   183: invokespecial 613	com/mysql/jdbc/PreparedStatement:generateMultiStatementForBatch	(I)Ljava/lang/String;
    //   186: invokeinterface 620 2 0
    //   191: astore 8
    //   193: aload_3
    //   194: invokeinterface 623 1 0
    //   199: ifeq +47 -> 246
    //   202: iload_1
    //   203: ifeq +43 -> 246
    //   206: aload_3
    //   207: iconst_5
    //   208: iconst_0
    //   209: iconst_0
    //   210: invokeinterface 225 4 0
    //   215: ifeq +31 -> 246
    //   218: new 27	com/mysql/jdbc/StatementImpl$CancelTask
    //   221: dup
    //   222: aload_0
    //   223: aload 8
    //   225: checkcast 4	com/mysql/jdbc/StatementImpl
    //   228: invokespecial 626	com/mysql/jdbc/StatementImpl$CancelTask:<init>	(Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   231: astore 5
    //   233: aload_3
    //   234: invokeinterface 630 1 0
    //   239: aload 5
    //   241: iload_1
    //   242: i2l
    //   243: invokevirtual 636	java/util/Timer:schedule	(Ljava/util/TimerTask;J)V
    //   246: iload 6
    //   248: iload 7
    //   250: if_icmpge +10 -> 260
    //   253: iload 6
    //   255: istore 10
    //   257: goto +10 -> 267
    //   260: iload 6
    //   262: iload 7
    //   264: idiv
    //   265: istore 10
    //   267: iload 10
    //   269: iload 7
    //   271: imul
    //   272: istore 15
    //   274: iconst_0
    //   275: istore 16
    //   277: iload 16
    //   279: iload 15
    //   281: if_icmpge +98 -> 379
    //   284: iload 16
    //   286: ifeq +63 -> 349
    //   289: iload 16
    //   291: iload 7
    //   293: irem
    //   294: ifne +55 -> 349
    //   297: aload 8
    //   299: invokeinterface 638 1 0
    //   304: pop
    //   305: goto +19 -> 324
    //   308: astore 17
    //   310: aload_0
    //   311: iload 11
    //   313: iload 7
    //   315: aload 13
    //   317: aload 17
    //   319: invokevirtual 642	com/mysql/jdbc/PreparedStatement:handleExceptionForBatch	(II[ILjava/sql/SQLException;)Ljava/sql/SQLException;
    //   322: astore 14
    //   324: aload_0
    //   325: aload 8
    //   327: checkcast 4	com/mysql/jdbc/StatementImpl
    //   330: iload 12
    //   332: aload 13
    //   334: invokevirtual 646	com/mysql/jdbc/PreparedStatement:processMultiCountsAndKeys	(Lcom/mysql/jdbc/StatementImpl;I[I)I
    //   337: istore 12
    //   339: aload 8
    //   341: invokeinterface 648 1 0
    //   346: iconst_1
    //   347: istore 9
    //   349: aload_0
    //   350: aload 8
    //   352: iload 9
    //   354: aload_0
    //   355: getfield 272	com/mysql/jdbc/PreparedStatement:batchedArgs	Ljava/util/List;
    //   358: iload 11
    //   360: iinc 11 1
    //   363: invokeinterface 317 2 0
    //   368: invokevirtual 652	com/mysql/jdbc/PreparedStatement:setOneBatchedParameterSet	(Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   371: istore 9
    //   373: iinc 16 1
    //   376: goto -99 -> 277
    //   379: aload 8
    //   381: invokeinterface 638 1 0
    //   386: pop
    //   387: goto +21 -> 408
    //   390: astore 16
    //   392: aload_0
    //   393: iload 11
    //   395: iconst_1
    //   396: isub
    //   397: iload 7
    //   399: aload 13
    //   401: aload 16
    //   403: invokevirtual 642	com/mysql/jdbc/PreparedStatement:handleExceptionForBatch	(II[ILjava/sql/SQLException;)Ljava/sql/SQLException;
    //   406: astore 14
    //   408: aload_0
    //   409: aload 8
    //   411: checkcast 4	com/mysql/jdbc/StatementImpl
    //   414: iload 12
    //   416: aload 13
    //   418: invokevirtual 646	com/mysql/jdbc/PreparedStatement:processMultiCountsAndKeys	(Lcom/mysql/jdbc/StatementImpl;I[I)I
    //   421: istore 12
    //   423: aload 8
    //   425: invokeinterface 648 1 0
    //   430: iload 6
    //   432: iload 11
    //   434: isub
    //   435: istore 7
    //   437: jsr +14 -> 451
    //   440: goto +27 -> 467
    //   443: astore 18
    //   445: jsr +6 -> 451
    //   448: aload 18
    //   450: athrow
    //   451: astore 19
    //   453: aload 8
    //   455: ifnull +10 -> 465
    //   458: aload 8
    //   460: invokeinterface 654 1 0
    //   465: ret 19
    //   467: iload 7
    //   469: ifle +145 -> 614
    //   472: aload_0
    //   473: getfield 436	com/mysql/jdbc/PreparedStatement:retrieveGeneratedKeys	Z
    //   476: ifeq +21 -> 497
    //   479: aload_3
    //   480: aload_0
    //   481: iload 7
    //   483: invokespecial 613	com/mysql/jdbc/PreparedStatement:generateMultiStatementForBatch	(I)Ljava/lang/String;
    //   486: iconst_1
    //   487: invokeinterface 617 3 0
    //   492: astore 8
    //   494: goto +17 -> 511
    //   497: aload_3
    //   498: aload_0
    //   499: iload 7
    //   501: invokespecial 613	com/mysql/jdbc/PreparedStatement:generateMultiStatementForBatch	(I)Ljava/lang/String;
    //   504: invokeinterface 620 2 0
    //   509: astore 8
    //   511: aload 5
    //   513: ifnull +13 -> 526
    //   516: aload 5
    //   518: aload 8
    //   520: checkcast 4	com/mysql/jdbc/StatementImpl
    //   523: putfield 658	com/mysql/jdbc/StatementImpl$CancelTask:toCancel	Lcom/mysql/jdbc/StatementImpl;
    //   526: iconst_1
    //   527: istore 9
    //   529: iload 11
    //   531: iload 6
    //   533: if_icmpge +30 -> 563
    //   536: aload_0
    //   537: aload 8
    //   539: iload 9
    //   541: aload_0
    //   542: getfield 272	com/mysql/jdbc/PreparedStatement:batchedArgs	Ljava/util/List;
    //   545: iload 11
    //   547: iinc 11 1
    //   550: invokeinterface 317 2 0
    //   555: invokevirtual 652	com/mysql/jdbc/PreparedStatement:setOneBatchedParameterSet	(Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   558: istore 9
    //   560: goto -31 -> 529
    //   563: aload 8
    //   565: invokeinterface 638 1 0
    //   570: pop
    //   571: goto +21 -> 592
    //   574: astore 15
    //   576: aload_0
    //   577: iload 11
    //   579: iconst_1
    //   580: isub
    //   581: iload 7
    //   583: aload 13
    //   585: aload 15
    //   587: invokevirtual 642	com/mysql/jdbc/PreparedStatement:handleExceptionForBatch	(II[ILjava/sql/SQLException;)Ljava/sql/SQLException;
    //   590: astore 14
    //   592: aload_0
    //   593: aload 8
    //   595: checkcast 4	com/mysql/jdbc/StatementImpl
    //   598: iload 12
    //   600: aload 13
    //   602: invokevirtual 646	com/mysql/jdbc/PreparedStatement:processMultiCountsAndKeys	(Lcom/mysql/jdbc/StatementImpl;I[I)I
    //   605: istore 12
    //   607: aload 8
    //   609: invokeinterface 648 1 0
    //   614: aload 5
    //   616: ifnull +36 -> 652
    //   619: aload 5
    //   621: getfield 662	com/mysql/jdbc/StatementImpl$CancelTask:caughtWhileCancelling	Ljava/sql/SQLException;
    //   624: ifnull +9 -> 633
    //   627: aload 5
    //   629: getfield 662	com/mysql/jdbc/StatementImpl$CancelTask:caughtWhileCancelling	Ljava/sql/SQLException;
    //   632: athrow
    //   633: aload 5
    //   635: invokevirtual 665	com/mysql/jdbc/StatementImpl$CancelTask:cancel	()Z
    //   638: pop
    //   639: aload_3
    //   640: invokeinterface 630 1 0
    //   645: invokevirtual 668	java/util/Timer:purge	()I
    //   648: pop
    //   649: aconst_null
    //   650: astore 5
    //   652: aload 14
    //   654: ifnull +28 -> 682
    //   657: new 670	java/sql/BatchUpdateException
    //   660: dup
    //   661: aload 14
    //   663: invokevirtual 673	java/sql/SQLException:getMessage	()Ljava/lang/String;
    //   666: aload 14
    //   668: invokevirtual 676	java/sql/SQLException:getSQLState	()Ljava/lang/String;
    //   671: aload 14
    //   673: invokevirtual 679	java/sql/SQLException:getErrorCode	()I
    //   676: aload 13
    //   678: invokespecial 682	java/sql/BatchUpdateException:<init>	(Ljava/lang/String;Ljava/lang/String;I[I)V
    //   681: athrow
    //   682: aload 13
    //   684: astore 15
    //   686: jsr +19 -> 705
    //   689: jsr +40 -> 729
    //   692: aload_2
    //   693: monitorexit
    //   694: aload 15
    //   696: areturn
    //   697: astore 20
    //   699: jsr +6 -> 705
    //   702: aload 20
    //   704: athrow
    //   705: astore 21
    //   707: aload 8
    //   709: ifnull +10 -> 719
    //   712: aload 8
    //   714: invokeinterface 654 1 0
    //   719: ret 21
    //   721: astore 22
    //   723: jsr +6 -> 729
    //   726: aload 22
    //   728: athrow
    //   729: astore 23
    //   731: aload 5
    //   733: ifnull +19 -> 752
    //   736: aload 5
    //   738: invokevirtual 665	com/mysql/jdbc/StatementImpl$CancelTask:cancel	()Z
    //   741: pop
    //   742: aload_3
    //   743: invokeinterface 630 1 0
    //   748: invokevirtual 668	java/util/Timer:purge	()I
    //   751: pop
    //   752: aload_0
    //   753: invokevirtual 562	com/mysql/jdbc/PreparedStatement:resetCancelledState	()V
    //   756: iload 4
    //   758: ifne +12 -> 770
    //   761: aload_3
    //   762: invokeinterface 604 1 0
    //   767: invokevirtual 685	com/mysql/jdbc/MysqlIO:disableMultiQueries	()V
    //   770: aload_0
    //   771: invokevirtual 579	com/mysql/jdbc/PreparedStatement:clearBatch	()V
    //   774: ret 23
    //   776: astore 24
    //   778: aload_2
    //   779: monitorexit
    //   780: aload 24
    //   782: athrow
    // Line number table:
    //   Java source line #1475	-> byte code offset #0
    //   Java source line #1477	-> byte code offset #12
    //   Java source line #1478	-> byte code offset #19
    //   Java source line #1481	-> byte code offset #46
    //   Java source line #1483	-> byte code offset #51
    //   Java source line #1484	-> byte code offset #59
    //   Java source line #1487	-> byte code offset #62
    //   Java source line #1489	-> byte code offset #66
    //   Java source line #1491	-> byte code offset #77
    //   Java source line #1492	-> byte code offset #84
    //   Java source line #1495	-> byte code offset #97
    //   Java source line #1497	-> byte code offset #105
    //   Java source line #1498	-> byte code offset #112
    //   Java source line #1501	-> byte code offset #116
    //   Java source line #1503	-> byte code offset #119
    //   Java source line #1504	-> byte code offset #122
    //   Java source line #1505	-> byte code offset #125
    //   Java source line #1506	-> byte code offset #128
    //   Java source line #1507	-> byte code offset #131
    //   Java source line #1508	-> byte code offset #137
    //   Java source line #1511	-> byte code offset #140
    //   Java source line #1512	-> byte code offset #145
    //   Java source line #1515	-> byte code offset #154
    //   Java source line #1516	-> byte code offset #161
    //   Java source line #1520	-> byte code offset #179
    //   Java source line #1524	-> byte code offset #193
    //   Java source line #1527	-> byte code offset #218
    //   Java source line #1528	-> byte code offset #233
    //   Java source line #1532	-> byte code offset #246
    //   Java source line #1533	-> byte code offset #253
    //   Java source line #1535	-> byte code offset #260
    //   Java source line #1538	-> byte code offset #267
    //   Java source line #1540	-> byte code offset #274
    //   Java source line #1541	-> byte code offset #284
    //   Java source line #1543	-> byte code offset #297
    //   Java source line #1547	-> byte code offset #305
    //   Java source line #1544	-> byte code offset #308
    //   Java source line #1545	-> byte code offset #310
    //   Java source line #1549	-> byte code offset #324
    //   Java source line #1553	-> byte code offset #339
    //   Java source line #1554	-> byte code offset #346
    //   Java source line #1557	-> byte code offset #349
    //   Java source line #1540	-> byte code offset #373
    //   Java source line #1563	-> byte code offset #379
    //   Java source line #1567	-> byte code offset #387
    //   Java source line #1564	-> byte code offset #390
    //   Java source line #1565	-> byte code offset #392
    //   Java source line #1569	-> byte code offset #408
    //   Java source line #1573	-> byte code offset #423
    //   Java source line #1575	-> byte code offset #430
    //   Java source line #1576	-> byte code offset #437
    //   Java source line #1580	-> byte code offset #440
    //   Java source line #1577	-> byte code offset #443
    //   Java source line #1578	-> byte code offset #458
    //   Java source line #1583	-> byte code offset #467
    //   Java source line #1585	-> byte code offset #472
    //   Java source line #1586	-> byte code offset #479
    //   Java source line #1590	-> byte code offset #497
    //   Java source line #1594	-> byte code offset #511
    //   Java source line #1595	-> byte code offset #516
    //   Java source line #1598	-> byte code offset #526
    //   Java source line #1600	-> byte code offset #529
    //   Java source line #1601	-> byte code offset #536
    //   Java source line #1607	-> byte code offset #563
    //   Java source line #1611	-> byte code offset #571
    //   Java source line #1608	-> byte code offset #574
    //   Java source line #1609	-> byte code offset #576
    //   Java source line #1613	-> byte code offset #592
    //   Java source line #1617	-> byte code offset #607
    //   Java source line #1620	-> byte code offset #614
    //   Java source line #1621	-> byte code offset #619
    //   Java source line #1622	-> byte code offset #627
    //   Java source line #1625	-> byte code offset #633
    //   Java source line #1627	-> byte code offset #639
    //   Java source line #1629	-> byte code offset #649
    //   Java source line #1632	-> byte code offset #652
    //   Java source line #1633	-> byte code offset #657
    //   Java source line #1638	-> byte code offset #682
    //   Java source line #1640	-> byte code offset #697
    //   Java source line #1641	-> byte code offset #712
    //   Java source line #1645	-> byte code offset #721
    //   Java source line #1646	-> byte code offset #736
    //   Java source line #1647	-> byte code offset #742
    //   Java source line #1650	-> byte code offset #752
    //   Java source line #1652	-> byte code offset #756
    //   Java source line #1653	-> byte code offset #761
    //   Java source line #1656	-> byte code offset #770
    //   Java source line #1658	-> byte code offset #776
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	783	0	this	PreparedStatement
    //   0	783	1	batchTimeout	int
    //   10	769	2	Ljava/lang/Object;	Object
    //   50	712	3	locallyScopedConn	MySQLConnection
    //   57	700	4	multiQueriesEnabled	boolean
    //   60	677	5	timeoutTask	StatementImpl.CancelTask
    //   75	457	6	numBatchedArgs	int
    //   103	479	7	numValuesPerBatch	int
    //   117	596	8	batchedStatement	java.sql.PreparedStatement
    //   120	439	9	batchedParamIndex	int
    //   123	145	10	numberToExecuteAsMultiValue	int
    //   126	452	11	batchCounter	int
    //   129	477	12	updateCountCounter	int
    //   135	548	13	updateCounts	int[]
    //   138	534	14	sqlEx	SQLException
    //   272	8	15	numberArgsToExecute	int
    //   574	121	15	ex	SQLException
    //   275	99	16	i	int
    //   390	12	16	ex	SQLException
    //   308	10	17	ex	SQLException
    //   443	6	18	localObject1	Object
    //   451	1	19	localObject2	Object
    //   697	6	20	localObject3	Object
    //   705	1	21	localObject4	Object
    //   721	6	22	localObject5	Object
    //   729	1	23	localObject6	Object
    //   776	5	24	localObject7	Object
    // Exception table:
    //   from	to	target	type
    //   297	305	308	java/sql/SQLException
    //   379	387	390	java/sql/SQLException
    //   140	440	443	finally
    //   443	448	443	finally
    //   563	571	574	java/sql/SQLException
    //   467	689	697	finally
    //   697	702	697	finally
    //   62	692	721	finally
    //   697	726	721	finally
    //   12	694	776	finally
    //   697	780	776	finally
  }
  
  /* Error */
  protected int[] executeBatchedInserts(int batchTimeout)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 711	com/mysql/jdbc/PreparedStatement:getValuesClause	()Ljava/lang/String;
    //   4: astore_2
    //   5: aload_0
    //   6: getfield 185	com/mysql/jdbc/PreparedStatement:connection	Lcom/mysql/jdbc/MySQLConnection;
    //   9: astore_3
    //   10: aload_2
    //   11: ifnonnull +9 -> 20
    //   14: aload_0
    //   15: iload_1
    //   16: invokevirtual 578	com/mysql/jdbc/PreparedStatement:executeBatchSerially	(I)[I
    //   19: areturn
    //   20: aload_0
    //   21: getfield 272	com/mysql/jdbc/PreparedStatement:batchedArgs	Ljava/util/List;
    //   24: invokeinterface 556 1 0
    //   29: istore 4
    //   31: aload_0
    //   32: getfield 436	com/mysql/jdbc/PreparedStatement:retrieveGeneratedKeys	Z
    //   35: ifeq +16 -> 51
    //   38: aload_0
    //   39: new 274	java/util/ArrayList
    //   42: dup
    //   43: iload 4
    //   45: invokespecial 596	java/util/ArrayList:<init>	(I)V
    //   48: putfield 460	com/mysql/jdbc/PreparedStatement:batchedGeneratedKeys	Ljava/util/ArrayList;
    //   51: aload_0
    //   52: iload 4
    //   54: invokevirtual 600	com/mysql/jdbc/PreparedStatement:computeBatchSize	(I)I
    //   57: istore 5
    //   59: iload 4
    //   61: iload 5
    //   63: if_icmpge +7 -> 70
    //   66: iload 4
    //   68: istore 5
    //   70: aconst_null
    //   71: astore 6
    //   73: iconst_1
    //   74: istore 7
    //   76: iconst_0
    //   77: istore 8
    //   79: iconst_0
    //   80: istore 9
    //   82: iconst_0
    //   83: istore 10
    //   85: aconst_null
    //   86: astore 11
    //   88: aconst_null
    //   89: astore 12
    //   91: iload 4
    //   93: newarray <illegal type>
    //   95: astore 13
    //   97: iconst_0
    //   98: istore 14
    //   100: iload 14
    //   102: aload_0
    //   103: getfield 272	com/mysql/jdbc/PreparedStatement:batchedArgs	Ljava/util/List;
    //   106: invokeinterface 556 1 0
    //   111: if_icmpge +15 -> 126
    //   114: aload 13
    //   116: iload 14
    //   118: iconst_1
    //   119: iastore
    //   120: iinc 14 1
    //   123: goto -23 -> 100
    //   126: aload_0
    //   127: aload_3
    //   128: iload 5
    //   130: invokevirtual 715	com/mysql/jdbc/PreparedStatement:prepareBatchedInsertSQL	(Lcom/mysql/jdbc/MySQLConnection;I)Lcom/mysql/jdbc/PreparedStatement;
    //   133: astore 6
    //   135: aload_3
    //   136: invokeinterface 623 1 0
    //   141: ifeq +47 -> 188
    //   144: iload_1
    //   145: ifeq +43 -> 188
    //   148: aload_3
    //   149: iconst_5
    //   150: iconst_0
    //   151: iconst_0
    //   152: invokeinterface 225 4 0
    //   157: ifeq +31 -> 188
    //   160: new 27	com/mysql/jdbc/StatementImpl$CancelTask
    //   163: dup
    //   164: aload_0
    //   165: aload 6
    //   167: checkcast 4	com/mysql/jdbc/StatementImpl
    //   170: invokespecial 626	com/mysql/jdbc/StatementImpl$CancelTask:<init>	(Lcom/mysql/jdbc/StatementImpl;Lcom/mysql/jdbc/StatementImpl;)V
    //   173: astore 11
    //   175: aload_3
    //   176: invokeinterface 630 1 0
    //   181: aload 11
    //   183: iload_1
    //   184: i2l
    //   185: invokevirtual 636	java/util/Timer:schedule	(Ljava/util/TimerTask;J)V
    //   188: iload 4
    //   190: iload 5
    //   192: if_icmpge +10 -> 202
    //   195: iload 4
    //   197: istore 9
    //   199: goto +10 -> 209
    //   202: iload 4
    //   204: iload 5
    //   206: idiv
    //   207: istore 9
    //   209: iload 9
    //   211: iload 5
    //   213: imul
    //   214: istore 14
    //   216: iconst_0
    //   217: istore 15
    //   219: iload 15
    //   221: iload 14
    //   223: if_icmpge +95 -> 318
    //   226: iload 15
    //   228: ifeq +60 -> 288
    //   231: iload 15
    //   233: iload 5
    //   235: irem
    //   236: ifne +52 -> 288
    //   239: iload 8
    //   241: aload 6
    //   243: invokeinterface 718 1 0
    //   248: iadd
    //   249: istore 8
    //   251: goto +21 -> 272
    //   254: astore 16
    //   256: aload_0
    //   257: iload 10
    //   259: iconst_1
    //   260: isub
    //   261: iload 5
    //   263: aload 13
    //   265: aload 16
    //   267: invokevirtual 642	com/mysql/jdbc/PreparedStatement:handleExceptionForBatch	(II[ILjava/sql/SQLException;)Ljava/sql/SQLException;
    //   270: astore 12
    //   272: aload_0
    //   273: aload 6
    //   275: invokevirtual 722	com/mysql/jdbc/PreparedStatement:getBatchedGeneratedKeys	(Ljava/sql/Statement;)V
    //   278: aload 6
    //   280: invokeinterface 648 1 0
    //   285: iconst_1
    //   286: istore 7
    //   288: aload_0
    //   289: aload 6
    //   291: iload 7
    //   293: aload_0
    //   294: getfield 272	com/mysql/jdbc/PreparedStatement:batchedArgs	Ljava/util/List;
    //   297: iload 10
    //   299: iinc 10 1
    //   302: invokeinterface 317 2 0
    //   307: invokevirtual 652	com/mysql/jdbc/PreparedStatement:setOneBatchedParameterSet	(Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   310: istore 7
    //   312: iinc 15 1
    //   315: goto -96 -> 219
    //   318: iload 8
    //   320: aload 6
    //   322: invokeinterface 718 1 0
    //   327: iadd
    //   328: istore 8
    //   330: goto +21 -> 351
    //   333: astore 15
    //   335: aload_0
    //   336: iload 10
    //   338: iconst_1
    //   339: isub
    //   340: iload 5
    //   342: aload 13
    //   344: aload 15
    //   346: invokevirtual 642	com/mysql/jdbc/PreparedStatement:handleExceptionForBatch	(II[ILjava/sql/SQLException;)Ljava/sql/SQLException;
    //   349: astore 12
    //   351: aload_0
    //   352: aload 6
    //   354: invokevirtual 722	com/mysql/jdbc/PreparedStatement:getBatchedGeneratedKeys	(Ljava/sql/Statement;)V
    //   357: iload 4
    //   359: iload 10
    //   361: isub
    //   362: istore 5
    //   364: jsr +14 -> 378
    //   367: goto +27 -> 394
    //   370: astore 17
    //   372: jsr +6 -> 378
    //   375: aload 17
    //   377: athrow
    //   378: astore 18
    //   380: aload 6
    //   382: ifnull +10 -> 392
    //   385: aload 6
    //   387: invokeinterface 654 1 0
    //   392: ret 18
    //   394: iload 5
    //   396: ifle +103 -> 499
    //   399: aload_0
    //   400: aload_3
    //   401: iload 5
    //   403: invokevirtual 715	com/mysql/jdbc/PreparedStatement:prepareBatchedInsertSQL	(Lcom/mysql/jdbc/MySQLConnection;I)Lcom/mysql/jdbc/PreparedStatement;
    //   406: astore 6
    //   408: aload 11
    //   410: ifnull +13 -> 423
    //   413: aload 11
    //   415: aload 6
    //   417: checkcast 4	com/mysql/jdbc/StatementImpl
    //   420: putfield 658	com/mysql/jdbc/StatementImpl$CancelTask:toCancel	Lcom/mysql/jdbc/StatementImpl;
    //   423: iconst_1
    //   424: istore 7
    //   426: iload 10
    //   428: iload 4
    //   430: if_icmpge +30 -> 460
    //   433: aload_0
    //   434: aload 6
    //   436: iload 7
    //   438: aload_0
    //   439: getfield 272	com/mysql/jdbc/PreparedStatement:batchedArgs	Ljava/util/List;
    //   442: iload 10
    //   444: iinc 10 1
    //   447: invokeinterface 317 2 0
    //   452: invokevirtual 652	com/mysql/jdbc/PreparedStatement:setOneBatchedParameterSet	(Ljava/sql/PreparedStatement;ILjava/lang/Object;)I
    //   455: istore 7
    //   457: goto -31 -> 426
    //   460: iload 8
    //   462: aload 6
    //   464: invokeinterface 718 1 0
    //   469: iadd
    //   470: istore 8
    //   472: goto +21 -> 493
    //   475: astore 14
    //   477: aload_0
    //   478: iload 10
    //   480: iconst_1
    //   481: isub
    //   482: iload 5
    //   484: aload 13
    //   486: aload 14
    //   488: invokevirtual 642	com/mysql/jdbc/PreparedStatement:handleExceptionForBatch	(II[ILjava/sql/SQLException;)Ljava/sql/SQLException;
    //   491: astore 12
    //   493: aload_0
    //   494: aload 6
    //   496: invokevirtual 722	com/mysql/jdbc/PreparedStatement:getBatchedGeneratedKeys	(Ljava/sql/Statement;)V
    //   499: aload 12
    //   501: ifnull +28 -> 529
    //   504: new 670	java/sql/BatchUpdateException
    //   507: dup
    //   508: aload 12
    //   510: invokevirtual 673	java/sql/SQLException:getMessage	()Ljava/lang/String;
    //   513: aload 12
    //   515: invokevirtual 676	java/sql/SQLException:getSQLState	()Ljava/lang/String;
    //   518: aload 12
    //   520: invokevirtual 679	java/sql/SQLException:getErrorCode	()I
    //   523: aload 13
    //   525: invokespecial 682	java/sql/BatchUpdateException:<init>	(Ljava/lang/String;Ljava/lang/String;I[I)V
    //   528: athrow
    //   529: aload 13
    //   531: astore 14
    //   533: jsr +17 -> 550
    //   536: jsr +38 -> 574
    //   539: aload 14
    //   541: areturn
    //   542: astore 19
    //   544: jsr +6 -> 550
    //   547: aload 19
    //   549: athrow
    //   550: astore 20
    //   552: aload 6
    //   554: ifnull +10 -> 564
    //   557: aload 6
    //   559: invokeinterface 654 1 0
    //   564: ret 20
    //   566: astore 21
    //   568: jsr +6 -> 574
    //   571: aload 21
    //   573: athrow
    //   574: astore 22
    //   576: aload 11
    //   578: ifnull +19 -> 597
    //   581: aload 11
    //   583: invokevirtual 665	com/mysql/jdbc/StatementImpl$CancelTask:cancel	()Z
    //   586: pop
    //   587: aload_3
    //   588: invokeinterface 630 1 0
    //   593: invokevirtual 668	java/util/Timer:purge	()I
    //   596: pop
    //   597: aload_0
    //   598: invokevirtual 562	com/mysql/jdbc/PreparedStatement:resetCancelledState	()V
    //   601: ret 22
    // Line number table:
    //   Java source line #1685	-> byte code offset #0
    //   Java source line #1687	-> byte code offset #5
    //   Java source line #1689	-> byte code offset #10
    //   Java source line #1690	-> byte code offset #14
    //   Java source line #1693	-> byte code offset #20
    //   Java source line #1695	-> byte code offset #31
    //   Java source line #1696	-> byte code offset #38
    //   Java source line #1699	-> byte code offset #51
    //   Java source line #1701	-> byte code offset #59
    //   Java source line #1702	-> byte code offset #66
    //   Java source line #1705	-> byte code offset #70
    //   Java source line #1707	-> byte code offset #73
    //   Java source line #1708	-> byte code offset #76
    //   Java source line #1709	-> byte code offset #79
    //   Java source line #1710	-> byte code offset #82
    //   Java source line #1711	-> byte code offset #85
    //   Java source line #1712	-> byte code offset #88
    //   Java source line #1714	-> byte code offset #91
    //   Java source line #1716	-> byte code offset #97
    //   Java source line #1717	-> byte code offset #114
    //   Java source line #1716	-> byte code offset #120
    //   Java source line #1722	-> byte code offset #126
    //   Java source line #1725	-> byte code offset #135
    //   Java source line #1728	-> byte code offset #160
    //   Java source line #1730	-> byte code offset #175
    //   Java source line #1734	-> byte code offset #188
    //   Java source line #1735	-> byte code offset #195
    //   Java source line #1737	-> byte code offset #202
    //   Java source line #1741	-> byte code offset #209
    //   Java source line #1744	-> byte code offset #216
    //   Java source line #1745	-> byte code offset #226
    //   Java source line #1747	-> byte code offset #239
    //   Java source line #1752	-> byte code offset #251
    //   Java source line #1749	-> byte code offset #254
    //   Java source line #1750	-> byte code offset #256
    //   Java source line #1754	-> byte code offset #272
    //   Java source line #1755	-> byte code offset #278
    //   Java source line #1756	-> byte code offset #285
    //   Java source line #1760	-> byte code offset #288
    //   Java source line #1744	-> byte code offset #312
    //   Java source line #1766	-> byte code offset #318
    //   Java source line #1770	-> byte code offset #330
    //   Java source line #1767	-> byte code offset #333
    //   Java source line #1768	-> byte code offset #335
    //   Java source line #1772	-> byte code offset #351
    //   Java source line #1774	-> byte code offset #357
    //   Java source line #1775	-> byte code offset #364
    //   Java source line #1779	-> byte code offset #367
    //   Java source line #1776	-> byte code offset #370
    //   Java source line #1777	-> byte code offset #385
    //   Java source line #1782	-> byte code offset #394
    //   Java source line #1783	-> byte code offset #399
    //   Java source line #1787	-> byte code offset #408
    //   Java source line #1788	-> byte code offset #413
    //   Java source line #1791	-> byte code offset #423
    //   Java source line #1793	-> byte code offset #426
    //   Java source line #1794	-> byte code offset #433
    //   Java source line #1800	-> byte code offset #460
    //   Java source line #1804	-> byte code offset #472
    //   Java source line #1801	-> byte code offset #475
    //   Java source line #1802	-> byte code offset #477
    //   Java source line #1806	-> byte code offset #493
    //   Java source line #1809	-> byte code offset #499
    //   Java source line #1810	-> byte code offset #504
    //   Java source line #1815	-> byte code offset #529
    //   Java source line #1817	-> byte code offset #542
    //   Java source line #1818	-> byte code offset #557
    //   Java source line #1822	-> byte code offset #566
    //   Java source line #1823	-> byte code offset #581
    //   Java source line #1824	-> byte code offset #587
    //   Java source line #1827	-> byte code offset #597
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	603	0	this	PreparedStatement
    //   0	603	1	batchTimeout	int
    //   4	7	2	valuesClause	String
    //   9	579	3	locallyScopedConn	MySQLConnection
    //   29	400	4	numBatchedArgs	int
    //   57	426	5	numValuesPerBatch	int
    //   71	487	6	batchedStatement	java.sql.PreparedStatement
    //   74	382	7	batchedParamIndex	int
    //   77	394	8	updateCountRunningTotal	int
    //   80	130	9	numberToExecuteAsMultiValue	int
    //   83	396	10	batchCounter	int
    //   86	496	11	timeoutTask	StatementImpl.CancelTask
    //   89	430	12	sqlEx	SQLException
    //   95	435	13	updateCounts	int[]
    //   98	23	14	i	int
    //   214	8	14	numberArgsToExecute	int
    //   475	65	14	ex	SQLException
    //   217	96	15	i	int
    //   333	12	15	ex	SQLException
    //   254	12	16	ex	SQLException
    //   370	6	17	localObject1	Object
    //   378	1	18	localObject2	Object
    //   542	6	19	localObject3	Object
    //   550	1	20	localObject4	Object
    //   566	6	21	localObject5	Object
    //   574	1	22	localObject6	Object
    // Exception table:
    //   from	to	target	type
    //   239	251	254	java/sql/SQLException
    //   318	330	333	java/sql/SQLException
    //   126	367	370	finally
    //   370	375	370	finally
    //   460	472	475	java/sql/SQLException
    //   394	536	542	finally
    //   542	547	542	finally
    //   126	539	566	finally
    //   542	571	566	finally
  }
}
