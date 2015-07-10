package com.mysql.jdbc;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class DatabaseMetaData
  implements java.sql.DatabaseMetaData
{
  private static String mysqlKeywordsThatArentSQL92;
  protected static final int MAX_IDENTIFIER_LENGTH = 64;
  private static final int DEFERRABILITY = 13;
  private static final int DELETE_RULE = 10;
  private static final int FK_NAME = 11;
  private static final int FKCOLUMN_NAME = 7;
  private static final int FKTABLE_CAT = 4;
  private static final int FKTABLE_NAME = 6;
  private static final int FKTABLE_SCHEM = 5;
  private static final int KEY_SEQ = 8;
  private static final int PK_NAME = 12;
  private static final int PKCOLUMN_NAME = 3;
  private static final int PKTABLE_CAT = 0;
  private static final int PKTABLE_NAME = 2;
  private static final int PKTABLE_SCHEM = 1;
  private static final String SUPPORTS_FK = "SUPPORTS_FK";
  
  protected abstract class IteratorWithCleanup
  {
    protected IteratorWithCleanup() {}
    
    abstract void close()
      throws SQLException;
    
    abstract boolean hasNext()
      throws SQLException;
    
    abstract Object next()
      throws SQLException;
  }
  
  class LocalAndReferencedColumns
  {
    String constraintName;
    List localColumnsList;
    String referencedCatalog;
    List referencedColumnsList;
    String referencedTable;
    
    LocalAndReferencedColumns(List localColumns, List refColumns, String constName, String refCatalog, String refTable)
    {
      this.localColumnsList = localColumns;
      this.referencedColumnsList = refColumns;
      this.constraintName = constName;
      this.referencedTable = refTable;
      this.referencedCatalog = refCatalog;
    }
  }
  
  protected class ResultSetIterator
    extends DatabaseMetaData.IteratorWithCleanup
  {
    int colIndex;
    ResultSet resultSet;
    
    ResultSetIterator(ResultSet rs, int index)
    {
      super();
      this.resultSet = rs;
      this.colIndex = index;
    }
    
    void close()
      throws SQLException
    {
      this.resultSet.close();
    }
    
    boolean hasNext()
      throws SQLException
    {
      return this.resultSet.next();
    }
    
    Object next()
      throws SQLException
    {
      return this.resultSet.getObject(this.colIndex);
    }
  }
  
  protected class SingleStringIterator
    extends DatabaseMetaData.IteratorWithCleanup
  {
    boolean onFirst = true;
    String value;
    
    SingleStringIterator(String s)
    {
      super();
      this.value = s;
    }
    
    void close()
      throws SQLException
    {}
    
    boolean hasNext()
      throws SQLException
    {
      return this.onFirst;
    }
    
    Object next()
      throws SQLException
    {
      this.onFirst = false;
      return this.value;
    }
  }
  
  class TypeDescriptor
  {
    int bufferLength;
    int charOctetLength;
    Integer columnSize;
    short dataType;
    Integer decimalDigits;
    String isNullable;
    int nullability;
    int numPrecRadix = 10;
    String typeName;
    
    TypeDescriptor(String typeInfo, String nullabilityInfo)
      throws SQLException
    {
      if (typeInfo == null) {
        throw SQLError.createSQLException("NULL typeinfo not supported.", "S1009", DatabaseMetaData.this.getExceptionInterceptor());
      }
      String mysqlType = "";
      String fullMysqlType = null;
      if (typeInfo.indexOf("(") != -1) {
        mysqlType = typeInfo.substring(0, typeInfo.indexOf("("));
      } else {
        mysqlType = typeInfo;
      }
      int indexOfUnsignedInMysqlType = StringUtils.indexOfIgnoreCase(mysqlType, "unsigned");
      if (indexOfUnsignedInMysqlType != -1) {
        mysqlType = mysqlType.substring(0, indexOfUnsignedInMysqlType - 1);
      }
      boolean isUnsigned = false;
      if (StringUtils.indexOfIgnoreCase(typeInfo, "unsigned") != -1)
      {
        fullMysqlType = mysqlType + " unsigned";
        isUnsigned = true;
      }
      else
      {
        fullMysqlType = mysqlType;
      }
      if (DatabaseMetaData.this.conn.getCapitalizeTypeNames()) {
        fullMysqlType = fullMysqlType.toUpperCase(Locale.ENGLISH);
      }
      this.dataType = ((short)MysqlDefs.mysqlToJavaType(mysqlType));
      
      this.typeName = fullMysqlType;
      if (StringUtils.startsWithIgnoreCase(typeInfo, "enum"))
      {
        String temp = typeInfo.substring(typeInfo.indexOf("("), typeInfo.lastIndexOf(")"));
        
        StringTokenizer tokenizer = new StringTokenizer(temp, ",");
        
        int maxLength = 0;
        while (tokenizer.hasMoreTokens()) {
          maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
        }
        this.columnSize = Constants.integerValueOf(maxLength);
        this.decimalDigits = null;
      }
      else if (StringUtils.startsWithIgnoreCase(typeInfo, "set"))
      {
        String temp = typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.lastIndexOf(")"));
        
        StringTokenizer tokenizer = new StringTokenizer(temp, ",");
        
        int maxLength = 0;
        
        int numElements = tokenizer.countTokens();
        if (numElements > 0) {
          maxLength += numElements - 1;
        }
        while (tokenizer.hasMoreTokens())
        {
          String setMember = tokenizer.nextToken().trim();
          if ((setMember.startsWith("'")) && (setMember.endsWith("'"))) {
            maxLength += setMember.length() - 2;
          } else {
            maxLength += setMember.length();
          }
        }
        this.columnSize = Constants.integerValueOf(maxLength);
        this.decimalDigits = null;
      }
      else if (typeInfo.indexOf(",") != -1)
      {
        this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.indexOf(",")).trim());
        
        this.decimalDigits = Integer.valueOf(typeInfo.substring(typeInfo.indexOf(",") + 1, typeInfo.indexOf(")")).trim());
      }
      else
      {
        this.columnSize = null;
        this.decimalDigits = null;
        if (((StringUtils.indexOfIgnoreCase(typeInfo, "char") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "text") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "blob") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "binary") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "bit") != -1)) && (typeInfo.indexOf("(") != -1))
        {
          int endParenIndex = typeInfo.indexOf(")");
          if (endParenIndex == -1) {
            endParenIndex = typeInfo.length();
          }
          this.columnSize = Integer.valueOf(typeInfo.substring(typeInfo.indexOf("(") + 1, endParenIndex).trim());
          if ((DatabaseMetaData.this.conn.getTinyInt1isBit()) && (this.columnSize.intValue() == 1) && (StringUtils.startsWithIgnoreCase(typeInfo, 0, "tinyint"))) {
            if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean())
            {
              this.dataType = 16;
              this.typeName = "BOOLEAN";
            }
            else
            {
              this.dataType = -7;
              this.typeName = "BIT";
            }
          }
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyint"))
        {
          if ((DatabaseMetaData.this.conn.getTinyInt1isBit()) && (typeInfo.indexOf("(1)") != -1))
          {
            if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean())
            {
              this.dataType = 16;
              this.typeName = "BOOLEAN";
            }
            else
            {
              this.dataType = -7;
              this.typeName = "BIT";
            }
          }
          else
          {
            this.columnSize = Constants.integerValueOf(3);
            this.decimalDigits = Constants.integerValueOf(0);
          }
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "smallint"))
        {
          this.columnSize = Constants.integerValueOf(5);
          this.decimalDigits = Constants.integerValueOf(0);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumint"))
        {
          this.columnSize = Constants.integerValueOf(isUnsigned ? 8 : 7);
          this.decimalDigits = Constants.integerValueOf(0);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int"))
        {
          this.columnSize = Constants.integerValueOf(10);
          this.decimalDigits = Constants.integerValueOf(0);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "integer"))
        {
          this.columnSize = Constants.integerValueOf(10);
          this.decimalDigits = Constants.integerValueOf(0);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "bigint"))
        {
          this.columnSize = Constants.integerValueOf(isUnsigned ? 20 : 19);
          this.decimalDigits = Constants.integerValueOf(0);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int24"))
        {
          this.columnSize = Constants.integerValueOf(19);
          this.decimalDigits = Constants.integerValueOf(0);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "real"))
        {
          this.columnSize = Constants.integerValueOf(12);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "float"))
        {
          this.columnSize = Constants.integerValueOf(12);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "decimal"))
        {
          this.columnSize = Constants.integerValueOf(12);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "numeric"))
        {
          this.columnSize = Constants.integerValueOf(12);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "double"))
        {
          this.columnSize = Constants.integerValueOf(22);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "char"))
        {
          this.columnSize = Constants.integerValueOf(1);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "varchar"))
        {
          this.columnSize = Constants.integerValueOf(255);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "timestamp"))
        {
          this.columnSize = Constants.integerValueOf(19);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "datetime"))
        {
          this.columnSize = Constants.integerValueOf(19);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "date"))
        {
          this.columnSize = Constants.integerValueOf(10);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "time"))
        {
          this.columnSize = Constants.integerValueOf(8);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyblob"))
        {
          this.columnSize = Constants.integerValueOf(255);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "blob"))
        {
          this.columnSize = Constants.integerValueOf(65535);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumblob"))
        {
          this.columnSize = Constants.integerValueOf(16777215);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longblob"))
        {
          this.columnSize = Constants.integerValueOf(Integer.MAX_VALUE);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinytext"))
        {
          this.columnSize = Constants.integerValueOf(255);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "text"))
        {
          this.columnSize = Constants.integerValueOf(65535);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumtext"))
        {
          this.columnSize = Constants.integerValueOf(16777215);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longtext"))
        {
          this.columnSize = Constants.integerValueOf(Integer.MAX_VALUE);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "enum"))
        {
          this.columnSize = Constants.integerValueOf(255);
        }
        else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "set"))
        {
          this.columnSize = Constants.integerValueOf(255);
        }
      }
      this.bufferLength = MysqlIO.getMaxBuf();
      
      this.numPrecRadix = 10;
      if (nullabilityInfo != null)
      {
        if (nullabilityInfo.equals("YES"))
        {
          this.nullability = 1;
          this.isNullable = "YES";
        }
        else
        {
          this.nullability = 0;
          this.isNullable = "NO";
        }
      }
      else
      {
        this.nullability = 0;
        this.isNullable = "NO";
      }
    }
  }
  
  private static final byte[] TABLE_AS_BYTES = "TABLE".getBytes();
  private static final byte[] SYSTEM_TABLE_AS_BYTES = "SYSTEM TABLE".getBytes();
  private static final int UPDATE_RULE = 9;
  private static final byte[] VIEW_AS_BYTES = "VIEW".getBytes();
  private static final Constructor JDBC_4_DBMD_SHOW_CTOR;
  private static final Constructor JDBC_4_DBMD_IS_CTOR;
  protected MySQLConnection conn;
  
  static
  {
    if (Util.isJdbc4())
    {
      try
      {
        JDBC_4_DBMD_SHOW_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaData").getConstructor(new Class[] { MySQLConnection.class, String.class });
        
        JDBC_4_DBMD_IS_CTOR = Class.forName("com.mysql.jdbc.JDBC4DatabaseMetaDataUsingInfoSchema").getConstructor(new Class[] { MySQLConnection.class, String.class });
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
      JDBC_4_DBMD_IS_CTOR = null;
      JDBC_4_DBMD_SHOW_CTOR = null;
    }
    String[] allMySQLKeywords = { "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONNECTION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MATCH", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS", "READ_ONLY", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH", "WRITE", "X509", "XOR", "YEAR_MONTH", "ZEROFILL" };
    
    String[] sql92Keywords = { "ABSOLUTE", "EXEC", "OVERLAPS", "ACTION", "EXECUTE", "PAD", "ADA", "EXISTS", "PARTIAL", "ADD", "EXTERNAL", "PASCAL", "ALL", "EXTRACT", "POSITION", "ALLOCATE", "FALSE", "PRECISION", "ALTER", "FETCH", "PREPARE", "AND", "FIRST", "PRESERVE", "ANY", "FLOAT", "PRIMARY", "ARE", "FOR", "PRIOR", "AS", "FOREIGN", "PRIVILEGES", "ASC", "FORTRAN", "PROCEDURE", "ASSERTION", "FOUND", "PUBLIC", "AT", "FROM", "READ", "AUTHORIZATION", "FULL", "REAL", "AVG", "GET", "REFERENCES", "BEGIN", "GLOBAL", "RELATIVE", "BETWEEN", "GO", "RESTRICT", "BIT", "GOTO", "REVOKE", "BIT_LENGTH", "GRANT", "RIGHT", "BOTH", "GROUP", "ROLLBACK", "BY", "HAVING", "ROWS", "CASCADE", "HOUR", "SCHEMA", "CASCADED", "IDENTITY", "SCROLL", "CASE", "IMMEDIATE", "SECOND", "CAST", "IN", "SECTION", "CATALOG", "INCLUDE", "SELECT", "CHAR", "INDEX", "SESSION", "CHAR_LENGTH", "INDICATOR", "SESSION_USER", "CHARACTER", "INITIALLY", "SET", "CHARACTER_LENGTH", "INNER", "SIZE", "CHECK", "INPUT", "SMALLINT", "CLOSE", "INSENSITIVE", "SOME", "COALESCE", "INSERT", "SPACE", "COLLATE", "INT", "SQL", "COLLATION", "INTEGER", "SQLCA", "COLUMN", "INTERSECT", "SQLCODE", "COMMIT", "INTERVAL", "SQLERROR", "CONNECT", "INTO", "SQLSTATE", "CONNECTION", "IS", "SQLWARNING", "CONSTRAINT", "ISOLATION", "SUBSTRING", "CONSTRAINTS", "JOIN", "SUM", "CONTINUE", "KEY", "SYSTEM_USER", "CONVERT", "LANGUAGE", "TABLE", "CORRESPONDING", "LAST", "TEMPORARY", "COUNT", "LEADING", "THEN", "CREATE", "LEFT", "TIME", "CROSS", "LEVEL", "TIMESTAMP", "CURRENT", "LIKE", "TIMEZONE_HOUR", "CURRENT_DATE", "LOCAL", "TIMEZONE_MINUTE", "CURRENT_TIME", "LOWER", "TO", "CURRENT_TIMESTAMP", "MATCH", "TRAILING", "CURRENT_USER", "MAX", "TRANSACTION", "CURSOR", "MIN", "TRANSLATE", "DATE", "MINUTE", "TRANSLATION", "DAY", "MODULE", "TRIM", "DEALLOCATE", "MONTH", "TRUE", "DEC", "NAMES", "UNION", "DECIMAL", "NATIONAL", "UNIQUE", "DECLARE", "NATURAL", "UNKNOWN", "DEFAULT", "NCHAR", "UPDATE", "DEFERRABLE", "NEXT", "UPPER", "DEFERRED", "NO", "USAGE", "DELETE", "NONE", "USER", "DESC", "NOT", "USING", "DESCRIBE", "NULL", "VALUE", "DESCRIPTOR", "NULLIF", "VALUES", "DIAGNOSTICS", "NUMERIC", "VARCHAR", "DISCONNECT", "OCTET_LENGTH", "VARYING", "DISTINCT", "OF", "VIEW", "DOMAIN", "ON", "WHEN", "DOUBLE", "ONLY", "WHENEVER", "DROP", "OPEN", "WHERE", "ELSE", "OPTION", "WITH", "END", "OR", "WORK", "END-EXEC", "ORDER", "WRITE", "ESCAPE", "OUTER", "YEAR", "EXCEPT", "OUTPUT", "ZONE", "EXCEPTION" };
    
    TreeMap mySQLKeywordMap = new TreeMap();
    for (int i = 0; i < allMySQLKeywords.length; i++) {
      mySQLKeywordMap.put(allMySQLKeywords[i], null);
    }
    HashMap sql92KeywordMap = new HashMap(sql92Keywords.length);
    for (int i = 0; i < sql92Keywords.length; i++) {
      sql92KeywordMap.put(sql92Keywords[i], null);
    }
    Iterator it = sql92KeywordMap.keySet().iterator();
    while (it.hasNext()) {
      mySQLKeywordMap.remove(it.next());
    }
    StringBuffer keywordBuf = new StringBuffer();
    
    it = mySQLKeywordMap.keySet().iterator();
    if (it.hasNext()) {
      keywordBuf.append(it.next().toString());
    }
    while (it.hasNext())
    {
      keywordBuf.append(",");
      keywordBuf.append(it.next().toString());
    }
    mysqlKeywordsThatArentSQL92 = keywordBuf.toString();
  }
  
  protected String database = null;
  protected String quotedId = null;
  private ExceptionInterceptor exceptionInterceptor;
  
  protected static DatabaseMetaData getInstance(MySQLConnection connToSet, String databaseToSet, boolean checkForInfoSchema)
    throws SQLException
  {
    if (!Util.isJdbc4())
    {
      if ((checkForInfoSchema) && (connToSet != null) && (connToSet.getUseInformationSchema()) && (connToSet.versionMeetsMinimum(5, 0, 7))) {
        return new DatabaseMetaDataUsingInfoSchema(connToSet, databaseToSet);
      }
      return new DatabaseMetaData(connToSet, databaseToSet);
    }
    if ((checkForInfoSchema) && (connToSet != null) && (connToSet.getUseInformationSchema()) && (connToSet.versionMeetsMinimum(5, 0, 7))) {
      return (DatabaseMetaData)Util.handleNewInstance(JDBC_4_DBMD_IS_CTOR, new Object[] { connToSet, databaseToSet }, connToSet.getExceptionInterceptor());
    }
    return (DatabaseMetaData)Util.handleNewInstance(JDBC_4_DBMD_SHOW_CTOR, new Object[] { connToSet, databaseToSet }, connToSet.getExceptionInterceptor());
  }
  
  protected DatabaseMetaData(MySQLConnection connToSet, String databaseToSet)
  {
    this.conn = connToSet;
    this.database = databaseToSet;
    this.exceptionInterceptor = this.conn.getExceptionInterceptor();
    try
    {
      this.quotedId = (this.conn.supportsQuotedIdentifiers() ? getIdentifierQuoteString() : "");
    }
    catch (SQLException sqlEx)
    {
      AssertionFailedException.shouldNotHappen(sqlEx);
    }
  }
  
  public boolean allProceduresAreCallable()
    throws SQLException
  {
    return false;
  }
  
  public boolean allTablesAreSelectable()
    throws SQLException
  {
    return false;
  }
  
  private ResultSet buildResultSet(Field[] fields, ArrayList rows)
    throws SQLException
  {
    return buildResultSet(fields, rows, this.conn);
  }
  
  static ResultSet buildResultSet(Field[] fields, ArrayList rows, MySQLConnection c)
    throws SQLException
  {
    int fieldsLength = fields.length;
    for (int i = 0; i < fieldsLength; i++)
    {
      int jdbcType = fields[i].getSQLType();
      switch (jdbcType)
      {
      case -1: 
      case 1: 
      case 12: 
        fields[i].setCharacterSet(c.getCharacterSetMetadata());
        break;
      }
      fields[i].setConnection(c);
      fields[i].setUseOldNameMetadata(true);
    }
    return ResultSetImpl.getInstance(c.getCatalog(), fields, new RowDataStatic(rows), c, null, false);
  }
  
  private void convertToJdbcFunctionList(String catalog, ResultSet proceduresRs, boolean needsClientFiltering, String db, Map procedureRowsOrderedByName, int nameIndex, Field[] fields)
    throws SQLException
  {
    while (proceduresRs.next())
    {
      boolean shouldAdd = true;
      if (needsClientFiltering)
      {
        shouldAdd = false;
        
        String procDb = proceduresRs.getString(1);
        if ((db == null) && (procDb == null)) {
          shouldAdd = true;
        } else if ((db != null) && (db.equals(procDb))) {
          shouldAdd = true;
        }
      }
      if (shouldAdd)
      {
        String functionName = proceduresRs.getString(nameIndex);
        
        byte[][] rowData = (byte[][])null;
        if ((fields != null) && (fields.length == 9))
        {
          rowData = new byte[9][];
          rowData[0] = (catalog == null ? null : s2b(catalog));
          rowData[1] = null;
          rowData[2] = s2b(functionName);
          rowData[3] = null;
          rowData[4] = null;
          rowData[5] = null;
          rowData[6] = s2b(proceduresRs.getString("comment"));
          rowData[7] = s2b(Integer.toString(2));
          rowData[8] = s2b(functionName);
        }
        else
        {
          rowData = new byte[6][];
          
          rowData[0] = (catalog == null ? null : s2b(catalog));
          rowData[1] = null;
          rowData[2] = s2b(functionName);
          rowData[3] = s2b(proceduresRs.getString("comment"));
          rowData[4] = s2b(Integer.toString(getJDBC4FunctionNoTableConstant()));
          rowData[5] = s2b(functionName);
        }
        procedureRowsOrderedByName.put(functionName, new ByteArrayRow(rowData, getExceptionInterceptor()));
      }
    }
  }
  
  protected int getJDBC4FunctionNoTableConstant()
  {
    return 0;
  }
  
  private void convertToJdbcProcedureList(boolean fromSelect, String catalog, ResultSet proceduresRs, boolean needsClientFiltering, String db, Map procedureRowsOrderedByName, int nameIndex)
    throws SQLException
  {
    while (proceduresRs.next())
    {
      boolean shouldAdd = true;
      if (needsClientFiltering)
      {
        shouldAdd = false;
        
        String procDb = proceduresRs.getString(1);
        if ((db == null) && (procDb == null)) {
          shouldAdd = true;
        } else if ((db != null) && (db.equals(procDb))) {
          shouldAdd = true;
        }
      }
      if (shouldAdd)
      {
        String procedureName = proceduresRs.getString(nameIndex);
        byte[][] rowData = new byte[9][];
        rowData[0] = (catalog == null ? null : s2b(catalog));
        rowData[1] = null;
        rowData[2] = s2b(procedureName);
        rowData[3] = null;
        rowData[4] = null;
        rowData[5] = null;
        rowData[6] = null;
        
        boolean isFunction = fromSelect ? "FUNCTION".equalsIgnoreCase(proceduresRs.getString("type")) : false;
        
        rowData[7] = s2b(isFunction ? Integer.toString(2) : Integer.toString(0));
        
        rowData[8] = s2b(procedureName);
        
        procedureRowsOrderedByName.put(procedureName, new ByteArrayRow(rowData, getExceptionInterceptor()));
      }
    }
  }
  
  private ResultSetRow convertTypeDescriptorToProcedureRow(byte[] procNameAsBytes, byte[] procCatAsBytes, String paramName, boolean isOutParam, boolean isInParam, boolean isReturnParam, TypeDescriptor typeDesc, boolean forGetFunctionColumns, int ordinal)
    throws SQLException
  {
    byte[][] row = forGetFunctionColumns ? new byte[17][] : new byte[14][];
    row[0] = procCatAsBytes;
    row[1] = null;
    row[2] = procNameAsBytes;
    row[3] = s2b(paramName);
    if ((isInParam) && (isOutParam)) {
      row[4] = s2b(String.valueOf(2));
    } else if (isInParam) {
      row[4] = s2b(String.valueOf(1));
    } else if (isOutParam) {
      row[4] = s2b(String.valueOf(4));
    } else if (isReturnParam) {
      row[4] = s2b(String.valueOf(5));
    } else {
      row[4] = s2b(String.valueOf(0));
    }
    row[5] = s2b(Short.toString(typeDesc.dataType));
    row[6] = s2b(typeDesc.typeName);
    row[7] = (typeDesc.columnSize == null ? null : s2b(typeDesc.columnSize.toString()));
    row[8] = row[7];
    row[9] = (typeDesc.decimalDigits == null ? null : s2b(typeDesc.decimalDigits.toString()));
    row[10] = s2b(Integer.toString(typeDesc.numPrecRadix));
    switch (typeDesc.nullability)
    {
    case 0: 
      row[11] = s2b(String.valueOf(0));
      
      break;
    case 1: 
      row[11] = s2b(String.valueOf(1));
      
      break;
    case 2: 
      row[11] = s2b(String.valueOf(2));
      
      break;
    default: 
      throw SQLError.createSQLException("Internal error while parsing callable statement metadata (unknown nullability value fount)", "S1000", getExceptionInterceptor());
    }
    row[12] = null;
    if (forGetFunctionColumns)
    {
      row[13] = null;
      
      row[14] = s2b(String.valueOf(ordinal));
      
      row[15] = Constants.EMPTY_BYTE_ARRAY;
      
      row[16] = s2b(paramName);
    }
    return new ByteArrayRow(row, getExceptionInterceptor());
  }
  
  protected ExceptionInterceptor getExceptionInterceptor()
  {
    return this.exceptionInterceptor;
  }
  
  public boolean dataDefinitionCausesTransactionCommit()
    throws SQLException
  {
    return true;
  }
  
  public boolean dataDefinitionIgnoredInTransactions()
    throws SQLException
  {
    return false;
  }
  
  public boolean deletesAreDetected(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean doesMaxRowSizeIncludeBlobs()
    throws SQLException
  {
    return true;
  }
  
  public List extractForeignKeyForTable(ArrayList rows, ResultSet rs, String catalog)
    throws SQLException
  {
    byte[][] row = new byte[3][];
    row[0] = rs.getBytes(1);
    row[1] = s2b("SUPPORTS_FK");
    
    String createTableString = rs.getString(2);
    StringTokenizer lineTokenizer = new StringTokenizer(createTableString, "\n");
    
    StringBuffer commentBuf = new StringBuffer("comment; ");
    boolean firstTime = true;
    
    String quoteChar = getIdentifierQuoteString();
    if (quoteChar == null) {
      quoteChar = "`";
    }
    while (lineTokenizer.hasMoreTokens())
    {
      String line = lineTokenizer.nextToken().trim();
      
      String constraintName = null;
      if (StringUtils.startsWithIgnoreCase(line, "CONSTRAINT"))
      {
        boolean usingBackTicks = true;
        int beginPos = line.indexOf(quoteChar);
        if (beginPos == -1)
        {
          beginPos = line.indexOf("\"");
          usingBackTicks = false;
        }
        if (beginPos != -1)
        {
          int endPos = -1;
          if (usingBackTicks) {
            endPos = line.indexOf(quoteChar, beginPos + 1);
          } else {
            endPos = line.indexOf("\"", beginPos + 1);
          }
          if (endPos != -1)
          {
            constraintName = line.substring(beginPos + 1, endPos);
            line = line.substring(endPos + 1, line.length()).trim();
          }
        }
      }
      if (line.startsWith("FOREIGN KEY"))
      {
        if (line.endsWith(",")) {
          line = line.substring(0, line.length() - 1);
        }
        char quote = this.quotedId.charAt(0);
        
        int indexOfFK = line.indexOf("FOREIGN KEY");
        
        String localColumnName = null;
        String referencedCatalogName = this.quotedId + catalog + this.quotedId;
        String referencedTableName = null;
        String referencedColumnName = null;
        if (indexOfFK != -1)
        {
          int afterFk = indexOfFK + "FOREIGN KEY".length();
          
          int indexOfRef = StringUtils.indexOfIgnoreCaseRespectQuotes(afterFk, line, "REFERENCES", quote, true);
          if (indexOfRef != -1)
          {
            int indexOfParenOpen = line.indexOf('(', afterFk);
            int indexOfParenClose = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfParenOpen, line, ")", quote, true);
            if ((indexOfParenOpen != -1) && (indexOfParenClose == -1)) {}
            localColumnName = line.substring(indexOfParenOpen + 1, indexOfParenClose);
            
            int afterRef = indexOfRef + "REFERENCES".length();
            
            int referencedColumnBegin = StringUtils.indexOfIgnoreCaseRespectQuotes(afterRef, line, "(", quote, true);
            if (referencedColumnBegin != -1)
            {
              referencedTableName = line.substring(afterRef, referencedColumnBegin);
              
              int referencedColumnEnd = StringUtils.indexOfIgnoreCaseRespectQuotes(referencedColumnBegin + 1, line, ")", quote, true);
              if (referencedColumnEnd != -1) {
                referencedColumnName = line.substring(referencedColumnBegin + 1, referencedColumnEnd);
              }
              int indexOfCatalogSep = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referencedTableName, ".", quote, true);
              if (indexOfCatalogSep != -1)
              {
                referencedCatalogName = referencedTableName.substring(0, indexOfCatalogSep);
                referencedTableName = referencedTableName.substring(indexOfCatalogSep + 1);
              }
            }
          }
        }
        if (!firstTime) {
          commentBuf.append("; ");
        } else {
          firstTime = false;
        }
        if (constraintName != null) {
          commentBuf.append(constraintName);
        } else {
          commentBuf.append("not_available");
        }
        commentBuf.append("(");
        commentBuf.append(localColumnName);
        commentBuf.append(") REFER ");
        commentBuf.append(referencedCatalogName);
        commentBuf.append("/");
        commentBuf.append(referencedTableName);
        commentBuf.append("(");
        commentBuf.append(referencedColumnName);
        commentBuf.append(")");
        
        int lastParenIndex = line.lastIndexOf(")");
        if (lastParenIndex != line.length() - 1)
        {
          String cascadeOptions = line.substring(lastParenIndex + 1);
          
          commentBuf.append(" ");
          commentBuf.append(cascadeOptions);
        }
      }
    }
    row[2] = s2b(commentBuf.toString());
    rows.add(new ByteArrayRow(row, getExceptionInterceptor()));
    
    return rows;
  }
  
  public ResultSet extractForeignKeyFromCreateTable(String catalog, String tableName)
    throws SQLException
  {
    ArrayList tableList = new ArrayList();
    ResultSet rs = null;
    java.sql.Statement stmt = null;
    if (tableName != null) {
      tableList.add(tableName);
    } else {
      try
      {
        rs = getTables(catalog, "", "%", new String[] { "TABLE" });
        while (rs.next()) {
          tableList.add(rs.getString("TABLE_NAME"));
        }
      }
      finally
      {
        if (rs != null) {
          rs.close();
        }
        rs = null;
      }
    }
    ArrayList rows = new ArrayList();
    Field[] fields = new Field[3];
    fields[0] = new Field("", "Name", 1, Integer.MAX_VALUE);
    fields[1] = new Field("", "Type", 1, 255);
    fields[2] = new Field("", "Comment", 1, Integer.MAX_VALUE);
    
    int numTables = tableList.size();
    stmt = this.conn.getMetadataSafeStatement();
    
    String quoteChar = getIdentifierQuoteString();
    if (quoteChar == null) {
      quoteChar = "`";
    }
    try
    {
      for (int i = 0; i < numTables; i++)
      {
        String tableToExtract = (String)tableList.get(i);
        if (tableToExtract.indexOf(quoteChar) > 0) {
          tableToExtract = StringUtils.escapeQuote(tableToExtract, quoteChar);
        }
        String query = "SHOW CREATE TABLE " + quoteChar + catalog + quoteChar + "." + quoteChar + tableToExtract + quoteChar;
        try
        {
          rs = stmt.executeQuery(query);
        }
        catch (SQLException sqlEx)
        {
          String sqlState = sqlEx.getSQLState();
          if ((!"42S02".equals(sqlState)) && (sqlEx.getErrorCode() != 1146)) {
            throw sqlEx;
          }
          continue;
        }
        while (rs.next()) {
          extractForeignKeyForTable(rows, rs, catalog);
        }
      }
    }
    finally
    {
      if (rs != null) {
        rs.close();
      }
      rs = null;
      if (stmt != null) {
        stmt.close();
      }
      stmt = null;
    }
    return buildResultSet(fields, rows);
  }
  
  public ResultSet getAttributes(String arg0, String arg1, String arg2, String arg3)
    throws SQLException
  {
    Field[] fields = new Field[21];
    fields[0] = new Field("", "TYPE_CAT", 1, 32);
    fields[1] = new Field("", "TYPE_SCHEM", 1, 32);
    fields[2] = new Field("", "TYPE_NAME", 1, 32);
    fields[3] = new Field("", "ATTR_NAME", 1, 32);
    fields[4] = new Field("", "DATA_TYPE", 5, 32);
    fields[5] = new Field("", "ATTR_TYPE_NAME", 1, 32);
    fields[6] = new Field("", "ATTR_SIZE", 4, 32);
    fields[7] = new Field("", "DECIMAL_DIGITS", 4, 32);
    fields[8] = new Field("", "NUM_PREC_RADIX", 4, 32);
    fields[9] = new Field("", "NULLABLE ", 4, 32);
    fields[10] = new Field("", "REMARKS", 1, 32);
    fields[11] = new Field("", "ATTR_DEF", 1, 32);
    fields[12] = new Field("", "SQL_DATA_TYPE", 4, 32);
    fields[13] = new Field("", "SQL_DATETIME_SUB", 4, 32);
    fields[14] = new Field("", "CHAR_OCTET_LENGTH", 4, 32);
    fields[15] = new Field("", "ORDINAL_POSITION", 4, 32);
    fields[16] = new Field("", "IS_NULLABLE", 1, 32);
    fields[17] = new Field("", "SCOPE_CATALOG", 1, 32);
    fields[18] = new Field("", "SCOPE_SCHEMA", 1, 32);
    fields[19] = new Field("", "SCOPE_TABLE", 1, 32);
    fields[20] = new Field("", "SOURCE_DATA_TYPE", 5, 32);
    
    return buildResultSet(fields, new ArrayList());
  }
  
  public ResultSet getBestRowIdentifier(String catalog, String schema, final String table, int scope, boolean nullable)
    throws SQLException
  {
    if (table == null) {
      throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
    }
    Field[] fields = new Field[8];
    fields[0] = new Field("", "SCOPE", 5, 5);
    fields[1] = new Field("", "COLUMN_NAME", 1, 32);
    fields[2] = new Field("", "DATA_TYPE", 4, 32);
    fields[3] = new Field("", "TYPE_NAME", 1, 32);
    fields[4] = new Field("", "COLUMN_SIZE", 4, 10);
    fields[5] = new Field("", "BUFFER_LENGTH", 4, 10);
    fields[6] = new Field("", "DECIMAL_DIGITS", 5, 10);
    fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
    
    final ArrayList rows = new ArrayList();
    final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
    try
    {
      new IterateBlock(getCatalogIterator(catalog))
      {
        void forEach(Object catalogStr)
          throws SQLException
        {
          ResultSet results = null;
          try
          {
            StringBuffer queryBuf = new StringBuffer("SHOW COLUMNS FROM ");
            
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(table);
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(" FROM ");
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(catalogStr.toString());
            queryBuf.append(DatabaseMetaData.this.quotedId);
            
            results = stmt.executeQuery(queryBuf.toString());
            while (results.next())
            {
              String keyType = results.getString("Key");
              if ((keyType != null) && 
                (StringUtils.startsWithIgnoreCase(keyType, "PRI")))
              {
                byte[][] rowVal = new byte[8][];
                rowVal[0] = Integer.toString(2).getBytes();
                
                rowVal[1] = results.getBytes("Field");
                
                String type = results.getString("Type");
                int size = MysqlIO.getMaxBuf();
                int decimals = 0;
                if (type.indexOf("enum") != -1)
                {
                  String temp = type.substring(type.indexOf("("), type.indexOf(")"));
                  
                  StringTokenizer tokenizer = new StringTokenizer(temp, ",");
                  
                  int maxLength = 0;
                  while (tokenizer.hasMoreTokens()) {
                    maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
                  }
                  size = maxLength;
                  decimals = 0;
                  type = "enum";
                }
                else if (type.indexOf("(") != -1)
                {
                  if (type.indexOf(",") != -1)
                  {
                    size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(",")));
                    
                    decimals = Integer.parseInt(type.substring(type.indexOf(",") + 1, type.indexOf(")")));
                  }
                  else
                  {
                    size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(")")));
                  }
                  type = type.substring(0, type.indexOf("("));
                }
                rowVal[2] = DatabaseMetaData.this.s2b(String.valueOf(MysqlDefs.mysqlToJavaType(type)));
                
                rowVal[3] = DatabaseMetaData.this.s2b(type);
                rowVal[4] = Integer.toString(size + decimals).getBytes();
                
                rowVal[5] = Integer.toString(size + decimals).getBytes();
                
                rowVal[6] = Integer.toString(decimals).getBytes();
                
                rowVal[7] = Integer.toString(1).getBytes();
                
                rows.add(new ByteArrayRow(rowVal, DatabaseMetaData.this.getExceptionInterceptor()));
              }
            }
          }
          catch (SQLException sqlEx)
          {
            if (!"42S02".equals(sqlEx.getSQLState())) {
              throw sqlEx;
            }
          }
          finally
          {
            if (results != null)
            {
              try
              {
                results.close();
              }
              catch (Exception ex) {}
              results = null;
            }
          }
        }
      }.doForAll();
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
    ResultSet results = buildResultSet(fields, rows);
    
    return results;
  }
  
  private void getCallStmtParameterTypes(String catalog, String procName, String parameterNamePattern, List resultRows)
    throws SQLException
  {
    getCallStmtParameterTypes(catalog, procName, parameterNamePattern, resultRows, false);
  }
  
  private void getCallStmtParameterTypes(String catalog, String procName, String parameterNamePattern, List resultRows, boolean forGetFunctionColumns)
    throws SQLException
  {
    java.sql.Statement paramRetrievalStmt = null;
    ResultSet paramRetrievalRs = null;
    if (parameterNamePattern == null) {
      if (this.conn.getNullNamePatternMatchesAll()) {
        parameterNamePattern = "%";
      } else {
        throw SQLError.createSQLException("Parameter/Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }
    }
    String quoteChar = getIdentifierQuoteString();
    
    String parameterDef = null;
    
    byte[] procNameAsBytes = null;
    byte[] procCatAsBytes = null;
    
    boolean isProcedureInAnsiMode = false;
    String storageDefnDelims = null;
    String storageDefnClosures = null;
    try
    {
      paramRetrievalStmt = this.conn.getMetadataSafeStatement();
      if ((this.conn.lowerCaseTableNames()) && (catalog != null) && (catalog.length() != 0))
      {
        String oldCatalog = this.conn.getCatalog();
        ResultSet rs = null;
        try
        {
          this.conn.setCatalog(catalog.replaceAll(quoteChar, ""));
          rs = paramRetrievalStmt.executeQuery("SELECT DATABASE()");
          rs.next();
          
          catalog = rs.getString(1);
        }
        finally
        {
          this.conn.setCatalog(oldCatalog);
          if (rs != null) {
            rs.close();
          }
        }
      }
      if (paramRetrievalStmt.getMaxRows() != 0) {
        paramRetrievalStmt.setMaxRows(0);
      }
      int dotIndex = -1;
      if (!" ".equals(quoteChar)) {
        dotIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procName, ".", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
      } else {
        dotIndex = procName.indexOf(".");
      }
      String dbName = null;
      if ((dotIndex != -1) && (dotIndex + 1 < procName.length()))
      {
        dbName = procName.substring(0, dotIndex);
        procName = procName.substring(dotIndex + 1);
      }
      else
      {
        dbName = catalog;
      }
      String tmpProcName = procName;
      tmpProcName = tmpProcName.replaceAll(quoteChar, "");
      try
      {
        procNameAsBytes = tmpProcName.getBytes("UTF-8");
      }
      catch (UnsupportedEncodingException ueEx)
      {
        procNameAsBytes = s2b(tmpProcName);
      }
      tmpProcName = dbName;
      tmpProcName = tmpProcName.replaceAll(quoteChar, "");
      try
      {
        procCatAsBytes = tmpProcName.getBytes("UTF-8");
      }
      catch (UnsupportedEncodingException ueEx)
      {
        procCatAsBytes = s2b(tmpProcName);
      }
      StringBuffer procNameBuf = new StringBuffer();
      if (dbName != null)
      {
        if ((!" ".equals(quoteChar)) && (!dbName.startsWith(quoteChar))) {
          procNameBuf.append(quoteChar);
        }
        procNameBuf.append(dbName);
        if ((!" ".equals(quoteChar)) && (!dbName.startsWith(quoteChar))) {
          procNameBuf.append(quoteChar);
        }
        procNameBuf.append(".");
      }
      boolean procNameIsNotQuoted = !procName.startsWith(quoteChar);
      if ((!" ".equals(quoteChar)) && (procNameIsNotQuoted)) {
        procNameBuf.append(quoteChar);
      }
      procNameBuf.append(procName);
      if ((!" ".equals(quoteChar)) && (procNameIsNotQuoted)) {
        procNameBuf.append(quoteChar);
      }
      boolean parsingFunction = false;
      try
      {
        paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE PROCEDURE " + procNameBuf.toString());
        
        parsingFunction = false;
      }
      catch (SQLException sqlEx)
      {
        paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE FUNCTION " + procNameBuf.toString());
        
        parsingFunction = true;
      }
      if (paramRetrievalRs.next())
      {
        String procedureDef = parsingFunction ? paramRetrievalRs.getString("Create Function") : paramRetrievalRs.getString("Create Procedure");
        if ((procedureDef == null) || (procedureDef.length() == 0)) {
          throw SQLError.createSQLException("User does not have access to metadata required to determine stored procedure parameter types. If rights can not be granted, configure connection with \"noAccessToProcedureBodies=true\" to have driver generate parameters that represent INOUT strings irregardless of actual parameter types.", "S1000", getExceptionInterceptor());
        }
        try
        {
          String sqlMode = paramRetrievalRs.getString("sql_mode");
          if (StringUtils.indexOfIgnoreCase(sqlMode, "ANSI") != -1) {
            isProcedureInAnsiMode = true;
          }
        }
        catch (SQLException sqlEx) {}
        String identifierMarkers = isProcedureInAnsiMode ? "`\"" : "`";
        String identifierAndStringMarkers = "'" + identifierMarkers;
        storageDefnDelims = "(" + identifierMarkers;
        storageDefnClosures = ")" + identifierMarkers;
        
        procedureDef = StringUtils.stripComments(procedureDef, identifierAndStringMarkers, identifierAndStringMarkers, true, false, true, true);
        
        int openParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
        
        int endOfParamDeclarationIndex = 0;
        
        endOfParamDeclarationIndex = endPositionOfParameterDeclaration(openParenIndex, procedureDef, quoteChar);
        if (parsingFunction)
        {
          int returnsIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, " RETURNS ", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
          
          int endReturnsDef = findEndOfReturnsClause(procedureDef, quoteChar, returnsIndex);
          
          int declarationStart = returnsIndex + "RETURNS ".length();
          while ((declarationStart < procedureDef.length()) && 
            (Character.isWhitespace(procedureDef.charAt(declarationStart)))) {
            declarationStart++;
          }
          String returnsDefn = procedureDef.substring(declarationStart, endReturnsDef).trim();
          TypeDescriptor returnDescriptor = new TypeDescriptor(returnsDefn, null);
          
          resultRows.add(convertTypeDescriptorToProcedureRow(procNameAsBytes, procCatAsBytes, "", false, false, true, returnDescriptor, forGetFunctionColumns, 0));
        }
        if ((openParenIndex == -1) || (endOfParamDeclarationIndex == -1)) {
          throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
        }
        parameterDef = procedureDef.substring(openParenIndex + 1, endOfParamDeclarationIndex);
      }
    }
    finally
    {
      SQLException sqlExRethrow = null;
      if (paramRetrievalRs != null)
      {
        try
        {
          paramRetrievalRs.close();
        }
        catch (SQLException sqlEx)
        {
          sqlExRethrow = sqlEx;
        }
        paramRetrievalRs = null;
      }
      if (paramRetrievalStmt != null)
      {
        try
        {
          paramRetrievalStmt.close();
        }
        catch (SQLException sqlEx)
        {
          sqlExRethrow = sqlEx;
        }
        paramRetrievalStmt = null;
      }
      if (sqlExRethrow != null) {
        throw sqlExRethrow;
      }
    }
    if (parameterDef != null)
    {
      int ordinal = 1;
      
      List parseList = StringUtils.split(parameterDef, ",", storageDefnDelims, storageDefnClosures, true);
      
      int parseListLen = parseList.size();
      for (int i = 0; i < parseListLen; i++)
      {
        String declaration = (String)parseList.get(i);
        if (declaration.trim().length() == 0) {
          break;
        }
        declaration = declaration.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
        StringTokenizer declarationTok = new StringTokenizer(declaration, " \t");
        
        String paramName = null;
        boolean isOutParam = false;
        boolean isInParam = false;
        if (declarationTok.hasMoreTokens())
        {
          String possibleParamName = declarationTok.nextToken();
          if (possibleParamName.equalsIgnoreCase("OUT"))
          {
            isOutParam = true;
            if (declarationTok.hasMoreTokens()) {
              paramName = declarationTok.nextToken();
            } else {
              throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
            }
          }
          else if (possibleParamName.equalsIgnoreCase("INOUT"))
          {
            isOutParam = true;
            isInParam = true;
            if (declarationTok.hasMoreTokens()) {
              paramName = declarationTok.nextToken();
            } else {
              throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
            }
          }
          else if (possibleParamName.equalsIgnoreCase("IN"))
          {
            isOutParam = false;
            isInParam = true;
            if (declarationTok.hasMoreTokens()) {
              paramName = declarationTok.nextToken();
            } else {
              throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000", getExceptionInterceptor());
            }
          }
          else
          {
            isOutParam = false;
            isInParam = true;
            
            paramName = possibleParamName;
          }
          TypeDescriptor typeDesc = null;
          if (declarationTok.hasMoreTokens())
          {
            StringBuffer typeInfoBuf = new StringBuffer(declarationTok.nextToken());
            while (declarationTok.hasMoreTokens())
            {
              typeInfoBuf.append(" ");
              typeInfoBuf.append(declarationTok.nextToken());
            }
            String typeInfo = typeInfoBuf.toString();
            
            typeDesc = new TypeDescriptor(typeInfo, null);
          }
          else
          {
            throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter type)", "S1000", getExceptionInterceptor());
          }
          if (((paramName.startsWith("`")) && (paramName.endsWith("`"))) || ((isProcedureInAnsiMode) && (paramName.startsWith("\"")) && (paramName.endsWith("\"")))) {
            paramName = paramName.substring(1, paramName.length() - 1);
          }
          int wildCompareRes = StringUtils.wildCompare(paramName, parameterNamePattern);
          if (wildCompareRes != -1)
          {
            ResultSetRow row = convertTypeDescriptorToProcedureRow(procNameAsBytes, procCatAsBytes, paramName, isOutParam, isInParam, false, typeDesc, forGetFunctionColumns, ordinal++);
            
            resultRows.add(row);
          }
        }
        else
        {
          throw SQLError.createSQLException("Internal error when parsing callable statement metadata (unknown output from 'SHOW CREATE PROCEDURE')", "S1000", getExceptionInterceptor());
        }
      }
    }
  }
  
  private int endPositionOfParameterDeclaration(int beginIndex, String procedureDef, String quoteChar)
    throws SQLException
  {
    int currentPos = beginIndex + 1;
    int parenDepth = 1;
    while ((parenDepth > 0) && (currentPos < procedureDef.length()))
    {
      int closedParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, ")", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
      if (closedParenIndex != -1)
      {
        int nextOpenParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
        if ((nextOpenParenIndex != -1) && (nextOpenParenIndex < closedParenIndex))
        {
          parenDepth++;
          currentPos = closedParenIndex + 1;
        }
        else
        {
          parenDepth--;
          currentPos = closedParenIndex;
        }
      }
      else
      {
        throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
      }
    }
    return currentPos;
  }
  
  private int findEndOfReturnsClause(String procedureDefn, String quoteChar, int positionOfReturnKeyword)
    throws SQLException
  {
    String[] tokens = { "LANGUAGE", "NOT", "DETERMINISTIC", "CONTAINS", "NO", "READ", "MODIFIES", "SQL", "COMMENT", "BEGIN", "RETURN" };
    
    int startLookingAt = positionOfReturnKeyword + "RETURNS".length() + 1;
    
    int endOfReturn = -1;
    for (int i = 0; i < tokens.length; i++)
    {
      int nextEndOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, tokens[i], quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
      if ((nextEndOfReturn != -1) && (
        (endOfReturn == -1) || (nextEndOfReturn < endOfReturn))) {
        endOfReturn = nextEndOfReturn;
      }
    }
    if (endOfReturn != -1) {
      return endOfReturn;
    }
    endOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, ":", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
    if (endOfReturn != -1) {
      for (int i = endOfReturn; i > 0; i--) {
        if (Character.isWhitespace(procedureDefn.charAt(i))) {
          return i;
        }
      }
    }
    throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000", getExceptionInterceptor());
  }
  
  private int getCascadeDeleteOption(String cascadeOptions)
  {
    int onDeletePos = cascadeOptions.indexOf("ON DELETE");
    if (onDeletePos != -1)
    {
      String deleteOptions = cascadeOptions.substring(onDeletePos, cascadeOptions.length());
      if (deleteOptions.startsWith("ON DELETE CASCADE")) {
        return 0;
      }
      if (deleteOptions.startsWith("ON DELETE SET NULL")) {
        return 2;
      }
      if (deleteOptions.startsWith("ON DELETE RESTRICT")) {
        return 1;
      }
      if (deleteOptions.startsWith("ON DELETE NO ACTION")) {
        return 3;
      }
    }
    return 3;
  }
  
  private int getCascadeUpdateOption(String cascadeOptions)
  {
    int onUpdatePos = cascadeOptions.indexOf("ON UPDATE");
    if (onUpdatePos != -1)
    {
      String updateOptions = cascadeOptions.substring(onUpdatePos, cascadeOptions.length());
      if (updateOptions.startsWith("ON UPDATE CASCADE")) {
        return 0;
      }
      if (updateOptions.startsWith("ON UPDATE SET NULL")) {
        return 2;
      }
      if (updateOptions.startsWith("ON UPDATE RESTRICT")) {
        return 1;
      }
      if (updateOptions.startsWith("ON UPDATE NO ACTION")) {
        return 3;
      }
    }
    return 3;
  }
  
  protected IteratorWithCleanup getCatalogIterator(String catalogSpec)
    throws SQLException
  {
    IteratorWithCleanup allCatalogsIter;
    IteratorWithCleanup allCatalogsIter;
    if (catalogSpec != null)
    {
      IteratorWithCleanup allCatalogsIter;
      if (!catalogSpec.equals("")) {
        allCatalogsIter = new SingleStringIterator(catalogSpec);
      } else {
        allCatalogsIter = new SingleStringIterator(this.database);
      }
    }
    else
    {
      IteratorWithCleanup allCatalogsIter;
      if (this.conn.getNullCatalogMeansCurrent()) {
        allCatalogsIter = new SingleStringIterator(this.database);
      } else {
        allCatalogsIter = new ResultSetIterator(getCatalogs(), 1);
      }
    }
    return allCatalogsIter;
  }
  
  public ResultSet getCatalogs()
    throws SQLException
  {
    ResultSet results = null;
    java.sql.Statement stmt = null;
    try
    {
      stmt = this.conn.createStatement();
      stmt.setEscapeProcessing(false);
      results = stmt.executeQuery("SHOW DATABASES");
      
      ResultSetMetaData resultsMD = results.getMetaData();
      Field[] fields = new Field[1];
      fields[0] = new Field("", "TABLE_CAT", 12, resultsMD.getColumnDisplaySize(1));
      
      ArrayList tuples = new ArrayList();
      byte[][] rowVal;
      while (results.next())
      {
        rowVal = new byte[1][];
        rowVal[0] = results.getBytes(1);
        tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
      }
      return buildResultSet(fields, tuples);
    }
    finally
    {
      if (results != null)
      {
        try
        {
          results.close();
        }
        catch (SQLException sqlEx)
        {
          AssertionFailedException.shouldNotHappen(sqlEx);
        }
        results = null;
      }
      if (stmt != null)
      {
        try
        {
          stmt.close();
        }
        catch (SQLException sqlEx)
        {
          AssertionFailedException.shouldNotHappen(sqlEx);
        }
        stmt = null;
      }
    }
  }
  
  public String getCatalogSeparator()
    throws SQLException
  {
    return ".";
  }
  
  public String getCatalogTerm()
    throws SQLException
  {
    return "database";
  }
  
  public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
    throws SQLException
  {
    Field[] fields = new Field[8];
    fields[0] = new Field("", "TABLE_CAT", 1, 64);
    fields[1] = new Field("", "TABLE_SCHEM", 1, 1);
    fields[2] = new Field("", "TABLE_NAME", 1, 64);
    fields[3] = new Field("", "COLUMN_NAME", 1, 64);
    fields[4] = new Field("", "GRANTOR", 1, 77);
    fields[5] = new Field("", "GRANTEE", 1, 77);
    fields[6] = new Field("", "PRIVILEGE", 1, 64);
    fields[7] = new Field("", "IS_GRANTABLE", 1, 3);
    
    StringBuffer grantQuery = new StringBuffer("SELECT c.host, c.db, t.grantor, c.user, c.table_name, c.column_name, c.column_priv from mysql.columns_priv c, mysql.tables_priv t where c.host = t.host and c.db = t.db and c.table_name = t.table_name ");
    if ((catalog != null) && (catalog.length() != 0))
    {
      grantQuery.append(" AND c.db='");
      grantQuery.append(catalog);
      grantQuery.append("' ");
    }
    grantQuery.append(" AND c.table_name ='");
    grantQuery.append(table);
    grantQuery.append("' AND c.column_name like '");
    grantQuery.append(columnNamePattern);
    grantQuery.append("'");
    
    java.sql.Statement stmt = null;
    ResultSet results = null;
    ArrayList grantRows = new ArrayList();
    try
    {
      stmt = this.conn.createStatement();
      stmt.setEscapeProcessing(false);
      results = stmt.executeQuery(grantQuery.toString());
      while (results.next())
      {
        String host = results.getString(1);
        String db = results.getString(2);
        String grantor = results.getString(3);
        String user = results.getString(4);
        if ((user == null) || (user.length() == 0)) {
          user = "%";
        }
        StringBuffer fullUser = new StringBuffer(user);
        if ((host != null) && (this.conn.getUseHostsInPrivileges()))
        {
          fullUser.append("@");
          fullUser.append(host);
        }
        String columnName = results.getString(6);
        String allPrivileges = results.getString(7);
        if (allPrivileges != null)
        {
          allPrivileges = allPrivileges.toUpperCase(Locale.ENGLISH);
          
          StringTokenizer st = new StringTokenizer(allPrivileges, ",");
          while (st.hasMoreTokens())
          {
            String privilege = st.nextToken().trim();
            byte[][] tuple = new byte[8][];
            tuple[0] = s2b(db);
            tuple[1] = null;
            tuple[2] = s2b(table);
            tuple[3] = s2b(columnName);
            if (grantor != null) {
              tuple[4] = s2b(grantor);
            } else {
              tuple[4] = null;
            }
            tuple[5] = s2b(fullUser.toString());
            tuple[6] = s2b(privilege);
            tuple[7] = null;
            grantRows.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
          }
        }
      }
    }
    finally
    {
      if (results != null)
      {
        try
        {
          results.close();
        }
        catch (Exception ex) {}
        results = null;
      }
      if (stmt != null)
      {
        try
        {
          stmt.close();
        }
        catch (Exception ex) {}
        stmt = null;
      }
    }
    return buildResultSet(fields, grantRows);
  }
  
  public ResultSet getColumns(String catalog, final String schemaPattern, final String tableNamePattern, String columnNamePattern)
    throws SQLException
  {
    if (columnNamePattern == null) {
      if (this.conn.getNullNamePatternMatchesAll()) {
        columnNamePattern = "%";
      } else {
        throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }
    }
    final String colPattern = columnNamePattern;
    
    Field[] fields = createColumnsFields();
    
    final ArrayList rows = new ArrayList();
    final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
    try
    {
      new IterateBlock(getCatalogIterator(catalog))
      {
        void forEach(Object catalogStr)
          throws SQLException
        {
          ArrayList tableNameList = new ArrayList();
          if (tableNamePattern == null)
          {
            ResultSet tables = null;
            try
            {
              tables = DatabaseMetaData.this.getTables((String)catalogStr, schemaPattern, "%", new String[0]);
              while (tables.next())
              {
                String tableNameFromList = tables.getString("TABLE_NAME");
                
                tableNameList.add(tableNameFromList);
              }
            }
            finally
            {
              if (tables != null)
              {
                try
                {
                  tables.close();
                }
                catch (Exception sqlEx)
                {
                  AssertionFailedException.shouldNotHappen(sqlEx);
                }
                tables = null;
              }
            }
          }
          else
          {
            ResultSet tables = null;
            try
            {
              tables = DatabaseMetaData.this.getTables((String)catalogStr, schemaPattern, tableNamePattern, new String[0]);
              while (tables.next())
              {
                String tableNameFromList = tables.getString("TABLE_NAME");
                
                tableNameList.add(tableNameFromList);
              }
            }
            finally
            {
              if (tables != null)
              {
                try
                {
                  tables.close();
                }
                catch (SQLException sqlEx)
                {
                  AssertionFailedException.shouldNotHappen(sqlEx);
                }
                tables = null;
              }
            }
          }
          Iterator tableNames = tableNameList.iterator();
          while (tableNames.hasNext())
          {
            String tableName = (String)tableNames.next();
            
            ResultSet results = null;
            try
            {
              StringBuffer queryBuf = new StringBuffer("SHOW ");
              if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
                queryBuf.append("FULL ");
              }
              queryBuf.append("COLUMNS FROM ");
              queryBuf.append(DatabaseMetaData.this.quotedId);
              queryBuf.append(tableName);
              queryBuf.append(DatabaseMetaData.this.quotedId);
              queryBuf.append(" FROM ");
              queryBuf.append(DatabaseMetaData.this.quotedId);
              queryBuf.append((String)catalogStr);
              queryBuf.append(DatabaseMetaData.this.quotedId);
              queryBuf.append(" LIKE '");
              queryBuf.append(colPattern);
              queryBuf.append("'");
              
              boolean fixUpOrdinalsRequired = false;
              Object ordinalFixUpMap = null;
              if (!colPattern.equals("%"))
              {
                fixUpOrdinalsRequired = true;
                
                StringBuffer fullColumnQueryBuf = new StringBuffer("SHOW ");
                if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
                  fullColumnQueryBuf.append("FULL ");
                }
                fullColumnQueryBuf.append("COLUMNS FROM ");
                fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
                fullColumnQueryBuf.append(tableName);
                fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
                fullColumnQueryBuf.append(" FROM ");
                fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
                fullColumnQueryBuf.append((String)catalogStr);
                
                fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
                
                results = stmt.executeQuery(fullColumnQueryBuf.toString());
                
                ordinalFixUpMap = new HashMap();
                
                int fullOrdinalPos = 1;
                while (results.next())
                {
                  String fullOrdColName = results.getString("Field");
                  
                  ((Map)ordinalFixUpMap).put(fullOrdColName, Constants.integerValueOf(fullOrdinalPos++));
                }
              }
              results = stmt.executeQuery(queryBuf.toString());
              
              int ordPos = 1;
              while (results.next())
              {
                byte[][] rowVal = new byte[23][];
                rowVal[0] = DatabaseMetaData.this.s2b((String)catalogStr);
                rowVal[1] = null;
                
                rowVal[2] = DatabaseMetaData.this.s2b(tableName);
                rowVal[3] = results.getBytes("Field");
                
                DatabaseMetaData.TypeDescriptor typeDesc = new DatabaseMetaData.TypeDescriptor(DatabaseMetaData.this, results.getString("Type"), results.getString("Null"));
                
                rowVal[4] = Short.toString(typeDesc.dataType).getBytes();
                
                rowVal[5] = DatabaseMetaData.this.s2b(typeDesc.typeName);
                
                rowVal[6] = (typeDesc.columnSize == null ? null : DatabaseMetaData.this.s2b(typeDesc.columnSize.toString()));
                rowVal[7] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.bufferLength));
                rowVal[8] = (typeDesc.decimalDigits == null ? null : DatabaseMetaData.this.s2b(typeDesc.decimalDigits.toString()));
                rowVal[9] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.numPrecRadix));
                
                rowVal[10] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.nullability));
                try
                {
                  if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
                    rowVal[11] = results.getBytes("Comment");
                  } else {
                    rowVal[11] = results.getBytes("Extra");
                  }
                }
                catch (Exception E)
                {
                  rowVal[11] = new byte[0];
                }
                rowVal[12] = results.getBytes("Default");
                
                rowVal[13] = { 48 };
                rowVal[14] = { 48 };
                if ((StringUtils.indexOfIgnoreCase(typeDesc.typeName, "CHAR") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "BLOB") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "TEXT") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "BINARY") != -1)) {
                  rowVal[15] = rowVal[6];
                } else {
                  rowVal[15] = null;
                }
                if (!fixUpOrdinalsRequired)
                {
                  rowVal[16] = Integer.toString(ordPos++).getBytes();
                }
                else
                {
                  String origColName = results.getString("Field");
                  
                  Integer realOrdinal = (Integer)((Map)ordinalFixUpMap).get(origColName);
                  if (realOrdinal != null) {
                    rowVal[16] = realOrdinal.toString().getBytes();
                  } else {
                    throw SQLError.createSQLException("Can not find column in full column list to determine true ordinal position.", "S1000", DatabaseMetaData.this.getExceptionInterceptor());
                  }
                }
                rowVal[17] = DatabaseMetaData.this.s2b(typeDesc.isNullable);
                
                rowVal[18] = null;
                rowVal[19] = null;
                rowVal[20] = null;
                rowVal[21] = null;
                
                rowVal[22] = DatabaseMetaData.this.s2b("");
                
                String extra = results.getString("Extra");
                if (extra != null) {
                  rowVal[22] = DatabaseMetaData.this.s2b(StringUtils.indexOfIgnoreCase(extra, "auto_increment") != -1 ? "YES" : "NO");
                }
                rows.add(new ByteArrayRow(rowVal, DatabaseMetaData.this.getExceptionInterceptor()));
              }
            }
            finally
            {
              if (results != null)
              {
                try
                {
                  results.close();
                }
                catch (Exception ex) {}
                results = null;
              }
            }
          }
        }
      }.doForAll();
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
    ResultSet results = buildResultSet(fields, rows);
    
    return results;
  }
  
  protected Field[] createColumnsFields()
  {
    Field[] fields = new Field[23];
    fields[0] = new Field("", "TABLE_CAT", 1, 255);
    fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
    fields[2] = new Field("", "TABLE_NAME", 1, 255);
    fields[3] = new Field("", "COLUMN_NAME", 1, 32);
    fields[4] = new Field("", "DATA_TYPE", 4, 5);
    fields[5] = new Field("", "TYPE_NAME", 1, 16);
    fields[6] = new Field("", "COLUMN_SIZE", 4, Integer.toString(Integer.MAX_VALUE).length());
    
    fields[7] = new Field("", "BUFFER_LENGTH", 4, 10);
    fields[8] = new Field("", "DECIMAL_DIGITS", 4, 10);
    fields[9] = new Field("", "NUM_PREC_RADIX", 4, 10);
    fields[10] = new Field("", "NULLABLE", 4, 10);
    fields[11] = new Field("", "REMARKS", 1, 0);
    fields[12] = new Field("", "COLUMN_DEF", 1, 0);
    fields[13] = new Field("", "SQL_DATA_TYPE", 4, 10);
    fields[14] = new Field("", "SQL_DATETIME_SUB", 4, 10);
    fields[15] = new Field("", "CHAR_OCTET_LENGTH", 4, Integer.toString(Integer.MAX_VALUE).length());
    
    fields[16] = new Field("", "ORDINAL_POSITION", 4, 10);
    fields[17] = new Field("", "IS_NULLABLE", 1, 3);
    fields[18] = new Field("", "SCOPE_CATALOG", 1, 255);
    fields[19] = new Field("", "SCOPE_SCHEMA", 1, 255);
    fields[20] = new Field("", "SCOPE_TABLE", 1, 255);
    fields[21] = new Field("", "SOURCE_DATA_TYPE", 5, 10);
    fields[22] = new Field("", "IS_AUTOINCREMENT", 1, 3);
    return fields;
  }
  
  public Connection getConnection()
    throws SQLException
  {
    return this.conn;
  }
  
  public ResultSet getCrossReference(final String primaryCatalog, final String primarySchema, final String primaryTable, final String foreignCatalog, final String foreignSchema, final String foreignTable)
    throws SQLException
  {
    if (primaryTable == null) {
      throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
    }
    Field[] fields = createFkMetadataFields();
    
    final ArrayList tuples = new ArrayList();
    if (this.conn.versionMeetsMinimum(3, 23, 0))
    {
      final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
      try
      {
        new IterateBlock(getCatalogIterator(foreignCatalog))
        {
          void forEach(Object catalogStr)
            throws SQLException
          {
            ResultSet fkresults = null;
            try
            {
              if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50))
              {
                fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr.toString(), null);
              }
              else
              {
                StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
                
                queryBuf.append(DatabaseMetaData.this.quotedId);
                queryBuf.append(catalogStr.toString());
                queryBuf.append(DatabaseMetaData.this.quotedId);
                
                fkresults = stmt.executeQuery(queryBuf.toString());
              }
              String foreignTableWithCase = DatabaseMetaData.this.getTableNameWithCase(foreignTable);
              String primaryTableWithCase = DatabaseMetaData.this.getTableNameWithCase(primaryTable);
              while (fkresults.next())
              {
                String tableType = fkresults.getString("Type");
                if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
                {
                  String comment = fkresults.getString("Comment").trim();
                  if (comment != null)
                  {
                    StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
                    if (commentTokens.hasMoreTokens()) {
                      String str1 = commentTokens.nextToken();
                    }
                    while (commentTokens.hasMoreTokens())
                    {
                      String keys = commentTokens.nextToken();
                      
                      DatabaseMetaData.LocalAndReferencedColumns parsedInfo = DatabaseMetaData.this.parseTableStatusIntoLocalAndReferencedColumns(keys);
                      
                      int keySeq = 0;
                      
                      Iterator referencingColumns = parsedInfo.localColumnsList.iterator();
                      
                      Iterator referencedColumns = parsedInfo.referencedColumnsList.iterator();
                      while (referencingColumns.hasNext())
                      {
                        String referencingColumn = DatabaseMetaData.this.removeQuotedId(referencingColumns.next().toString());
                        
                        byte[][] tuple = new byte[14][];
                        tuple[4] = (foreignCatalog == null ? null : DatabaseMetaData.this.s2b(foreignCatalog));
                        
                        tuple[5] = (foreignSchema == null ? null : DatabaseMetaData.this.s2b(foreignSchema));
                        
                        String dummy = fkresults.getString("Name");
                        if (dummy.compareTo(foreignTableWithCase) == 0)
                        {
                          tuple[6] = DatabaseMetaData.this.s2b(dummy);
                          
                          tuple[7] = DatabaseMetaData.this.s2b(referencingColumn);
                          tuple[0] = (primaryCatalog == null ? null : DatabaseMetaData.this.s2b(primaryCatalog));
                          
                          tuple[1] = (primarySchema == null ? null : DatabaseMetaData.this.s2b(primarySchema));
                          if (parsedInfo.referencedTable.compareTo(primaryTableWithCase) == 0)
                          {
                            tuple[2] = DatabaseMetaData.this.s2b(parsedInfo.referencedTable);
                            tuple[3] = DatabaseMetaData.this.s2b(DatabaseMetaData.this.removeQuotedId(referencedColumns.next().toString()));
                            
                            tuple[8] = Integer.toString(keySeq).getBytes();
                            
                            int[] actions = DatabaseMetaData.this.getForeignKeyActions(keys);
                            
                            tuple[9] = Integer.toString(actions[1]).getBytes();
                            
                            tuple[10] = Integer.toString(actions[0]).getBytes();
                            
                            tuple[11] = null;
                            tuple[12] = null;
                            tuple[13] = Integer.toString(7).getBytes();
                            
                            tuples.add(new ByteArrayRow(tuple, DatabaseMetaData.this.getExceptionInterceptor()));
                            keySeq++;
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            finally
            {
              if (fkresults != null)
              {
                try
                {
                  fkresults.close();
                }
                catch (Exception sqlEx)
                {
                  AssertionFailedException.shouldNotHappen(sqlEx);
                }
                fkresults = null;
              }
            }
          }
        }.doForAll();
      }
      finally
      {
        if (stmt != null) {
          stmt.close();
        }
      }
    }
    ResultSet results = buildResultSet(fields, tuples);
    
    return results;
  }
  
  protected Field[] createFkMetadataFields()
  {
    Field[] fields = new Field[14];
    fields[0] = new Field("", "PKTABLE_CAT", 1, 255);
    fields[1] = new Field("", "PKTABLE_SCHEM", 1, 0);
    fields[2] = new Field("", "PKTABLE_NAME", 1, 255);
    fields[3] = new Field("", "PKCOLUMN_NAME", 1, 32);
    fields[4] = new Field("", "FKTABLE_CAT", 1, 255);
    fields[5] = new Field("", "FKTABLE_SCHEM", 1, 0);
    fields[6] = new Field("", "FKTABLE_NAME", 1, 255);
    fields[7] = new Field("", "FKCOLUMN_NAME", 1, 32);
    fields[8] = new Field("", "KEY_SEQ", 5, 2);
    fields[9] = new Field("", "UPDATE_RULE", 5, 2);
    fields[10] = new Field("", "DELETE_RULE", 5, 2);
    fields[11] = new Field("", "FK_NAME", 1, 0);
    fields[12] = new Field("", "PK_NAME", 1, 0);
    fields[13] = new Field("", "DEFERRABILITY", 5, 2);
    return fields;
  }
  
  public int getDatabaseMajorVersion()
    throws SQLException
  {
    return this.conn.getServerMajorVersion();
  }
  
  public int getDatabaseMinorVersion()
    throws SQLException
  {
    return this.conn.getServerMinorVersion();
  }
  
  public String getDatabaseProductName()
    throws SQLException
  {
    return "MySQL";
  }
  
  public String getDatabaseProductVersion()
    throws SQLException
  {
    return this.conn.getServerVersion();
  }
  
  public int getDefaultTransactionIsolation()
    throws SQLException
  {
    if (this.conn.supportsIsolationLevel()) {
      return 2;
    }
    return 0;
  }
  
  public int getDriverMajorVersion()
  {
    return NonRegisteringDriver.getMajorVersionInternal();
  }
  
  public int getDriverMinorVersion()
  {
    return NonRegisteringDriver.getMinorVersionInternal();
  }
  
  public String getDriverName()
    throws SQLException
  {
    return "MySQL-AB JDBC Driver";
  }
  
  public String getDriverVersion()
    throws SQLException
  {
    return "mysql-connector-java-5.1.14 ( Revision: ${bzr.revision-id} )";
  }
  
  public ResultSet getExportedKeys(String catalog, String schema, final String table)
    throws SQLException
  {
    if (table == null) {
      throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
    }
    Field[] fields = createFkMetadataFields();
    
    final ArrayList rows = new ArrayList();
    if (this.conn.versionMeetsMinimum(3, 23, 0))
    {
      final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
      try
      {
        new IterateBlock(getCatalogIterator(catalog))
        {
          void forEach(Object catalogStr)
            throws SQLException
          {
            ResultSet fkresults = null;
            try
            {
              if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50))
              {
                fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr.toString(), null);
              }
              else
              {
                StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
                
                queryBuf.append(DatabaseMetaData.this.quotedId);
                queryBuf.append(catalogStr.toString());
                queryBuf.append(DatabaseMetaData.this.quotedId);
                
                fkresults = stmt.executeQuery(queryBuf.toString());
              }
              String tableNameWithCase = DatabaseMetaData.this.getTableNameWithCase(table);
              while (fkresults.next())
              {
                String tableType = fkresults.getString("Type");
                if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
                {
                  String comment = fkresults.getString("Comment").trim();
                  if (comment != null)
                  {
                    StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
                    if (commentTokens.hasMoreTokens())
                    {
                      commentTokens.nextToken();
                      while (commentTokens.hasMoreTokens())
                      {
                        String keys = commentTokens.nextToken();
                        
                        DatabaseMetaData.this.getExportKeyResults(catalogStr.toString(), tableNameWithCase, keys, rows, fkresults.getString("Name"));
                      }
                    }
                  }
                }
              }
            }
            finally
            {
              if (fkresults != null)
              {
                try
                {
                  fkresults.close();
                }
                catch (SQLException sqlEx)
                {
                  AssertionFailedException.shouldNotHappen(sqlEx);
                }
                fkresults = null;
              }
            }
          }
        }.doForAll();
      }
      finally
      {
        if (stmt != null) {
          stmt.close();
        }
      }
    }
    ResultSet results = buildResultSet(fields, rows);
    
    return results;
  }
  
  private void getExportKeyResults(String catalog, String exportingTable, String keysComment, List tuples, String fkTableName)
    throws SQLException
  {
    getResultsImpl(catalog, exportingTable, keysComment, tuples, fkTableName, true);
  }
  
  public String getExtraNameCharacters()
    throws SQLException
  {
    return "#@";
  }
  
  private int[] getForeignKeyActions(String commentString)
  {
    int[] actions = { 3, 3 };
    
    int lastParenIndex = commentString.lastIndexOf(")");
    if (lastParenIndex != commentString.length() - 1)
    {
      String cascadeOptions = commentString.substring(lastParenIndex + 1).trim().toUpperCase(Locale.ENGLISH);
      
      actions[0] = getCascadeDeleteOption(cascadeOptions);
      actions[1] = getCascadeUpdateOption(cascadeOptions);
    }
    return actions;
  }
  
  public String getIdentifierQuoteString()
    throws SQLException
  {
    if (this.conn.supportsQuotedIdentifiers())
    {
      if (!this.conn.useAnsiQuotedIdentifiers()) {
        return "`";
      }
      return "\"";
    }
    return " ";
  }
  
  public ResultSet getImportedKeys(String catalog, String schema, final String table)
    throws SQLException
  {
    if (table == null) {
      throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
    }
    Field[] fields = createFkMetadataFields();
    
    final ArrayList rows = new ArrayList();
    if (this.conn.versionMeetsMinimum(3, 23, 0))
    {
      final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
      try
      {
        new IterateBlock(getCatalogIterator(catalog))
        {
          void forEach(Object catalogStr)
            throws SQLException
          {
            ResultSet fkresults = null;
            try
            {
              if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50))
              {
                fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr.toString(), table);
              }
              else
              {
                StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS ");
                
                queryBuf.append(" FROM ");
                queryBuf.append(DatabaseMetaData.this.quotedId);
                queryBuf.append(catalogStr.toString());
                queryBuf.append(DatabaseMetaData.this.quotedId);
                queryBuf.append(" LIKE '");
                queryBuf.append(table);
                queryBuf.append("'");
                
                fkresults = stmt.executeQuery(queryBuf.toString());
              }
              while (fkresults.next())
              {
                String tableType = fkresults.getString("Type");
                if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
                {
                  String comment = fkresults.getString("Comment").trim();
                  if (comment != null)
                  {
                    StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
                    if (commentTokens.hasMoreTokens())
                    {
                      commentTokens.nextToken();
                      while (commentTokens.hasMoreTokens())
                      {
                        String keys = commentTokens.nextToken();
                        
                        DatabaseMetaData.this.getImportKeyResults(catalogStr.toString(), table, keys, rows);
                      }
                    }
                  }
                }
              }
            }
            finally
            {
              if (fkresults != null)
              {
                try
                {
                  fkresults.close();
                }
                catch (SQLException sqlEx)
                {
                  AssertionFailedException.shouldNotHappen(sqlEx);
                }
                fkresults = null;
              }
            }
          }
        }.doForAll();
      }
      finally
      {
        if (stmt != null) {
          stmt.close();
        }
      }
    }
    ResultSet results = buildResultSet(fields, rows);
    
    return results;
  }
  
  private void getImportKeyResults(String catalog, String importingTable, String keysComment, List tuples)
    throws SQLException
  {
    getResultsImpl(catalog, importingTable, keysComment, tuples, null, false);
  }
  
  public ResultSet getIndexInfo(String catalog, String schema, final String table, final boolean unique, boolean approximate)
    throws SQLException
  {
    Field[] fields = createIndexInfoFields();
    
    final ArrayList rows = new ArrayList();
    final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
    try
    {
      new IterateBlock(getCatalogIterator(catalog))
      {
        void forEach(Object catalogStr)
          throws SQLException
        {
          ResultSet results = null;
          try
          {
            StringBuffer queryBuf = new StringBuffer("SHOW INDEX FROM ");
            
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(table);
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(" FROM ");
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(catalogStr.toString());
            queryBuf.append(DatabaseMetaData.this.quotedId);
            try
            {
              results = stmt.executeQuery(queryBuf.toString());
            }
            catch (SQLException sqlEx)
            {
              int errorCode = sqlEx.getErrorCode();
              if (!"42S02".equals(sqlEx.getSQLState())) {
                if (errorCode != 1146) {
                  throw sqlEx;
                }
              }
            }
            while ((results != null) && (results.next()))
            {
              byte[][] row = new byte[14][];
              row[0] = (catalogStr.toString() == null ? new byte[0] : DatabaseMetaData.this.s2b(catalogStr.toString()));
              
              row[1] = null;
              row[2] = results.getBytes("Table");
              
              boolean indexIsUnique = results.getInt("Non_unique") == 0;
              
              row[3] = (!indexIsUnique ? DatabaseMetaData.this.s2b("true") : DatabaseMetaData.this.s2b("false"));
              
              row[4] = new byte[0];
              row[5] = results.getBytes("Key_name");
              row[6] = Integer.toString(3).getBytes();
              
              row[7] = results.getBytes("Seq_in_index");
              row[8] = results.getBytes("Column_name");
              row[9] = results.getBytes("Collation");
              
              long cardinality = results.getLong("Cardinality");
              if (cardinality > 2147483647L) {
                cardinality = 2147483647L;
              }
              row[10] = DatabaseMetaData.this.s2b(String.valueOf(cardinality));
              row[11] = DatabaseMetaData.this.s2b("0");
              row[12] = null;
              if (unique)
              {
                if (indexIsUnique) {
                  rows.add(new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
                }
              }
              else {
                rows.add(new ByteArrayRow(row, DatabaseMetaData.this.getExceptionInterceptor()));
              }
            }
          }
          finally
          {
            if (results != null)
            {
              try
              {
                results.close();
              }
              catch (Exception ex) {}
              results = null;
            }
          }
        }
      }.doForAll();
      ResultSet indexInfo = buildResultSet(fields, rows);
      
      return indexInfo;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
  }
  
  protected Field[] createIndexInfoFields()
  {
    Field[] fields = new Field[13];
    fields[0] = new Field("", "TABLE_CAT", 1, 255);
    fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
    fields[2] = new Field("", "TABLE_NAME", 1, 255);
    fields[3] = new Field("", "NON_UNIQUE", 16, 4);
    fields[4] = new Field("", "INDEX_QUALIFIER", 1, 1);
    fields[5] = new Field("", "INDEX_NAME", 1, 32);
    fields[6] = new Field("", "TYPE", 5, 32);
    fields[7] = new Field("", "ORDINAL_POSITION", 5, 5);
    fields[8] = new Field("", "COLUMN_NAME", 1, 32);
    fields[9] = new Field("", "ASC_OR_DESC", 1, 1);
    fields[10] = new Field("", "CARDINALITY", 4, 20);
    fields[11] = new Field("", "PAGES", 4, 10);
    fields[12] = new Field("", "FILTER_CONDITION", 1, 32);
    return fields;
  }
  
  public int getJDBCMajorVersion()
    throws SQLException
  {
    return 4;
  }
  
  public int getJDBCMinorVersion()
    throws SQLException
  {
    return 0;
  }
  
  public int getMaxBinaryLiteralLength()
    throws SQLException
  {
    return 16777208;
  }
  
  public int getMaxCatalogNameLength()
    throws SQLException
  {
    return 32;
  }
  
  public int getMaxCharLiteralLength()
    throws SQLException
  {
    return 16777208;
  }
  
  public int getMaxColumnNameLength()
    throws SQLException
  {
    return 64;
  }
  
  public int getMaxColumnsInGroupBy()
    throws SQLException
  {
    return 64;
  }
  
  public int getMaxColumnsInIndex()
    throws SQLException
  {
    return 16;
  }
  
  public int getMaxColumnsInOrderBy()
    throws SQLException
  {
    return 64;
  }
  
  public int getMaxColumnsInSelect()
    throws SQLException
  {
    return 256;
  }
  
  public int getMaxColumnsInTable()
    throws SQLException
  {
    return 512;
  }
  
  public int getMaxConnections()
    throws SQLException
  {
    return 0;
  }
  
  public int getMaxCursorNameLength()
    throws SQLException
  {
    return 64;
  }
  
  public int getMaxIndexLength()
    throws SQLException
  {
    return 256;
  }
  
  public int getMaxProcedureNameLength()
    throws SQLException
  {
    return 0;
  }
  
  public int getMaxRowSize()
    throws SQLException
  {
    return 2147483639;
  }
  
  public int getMaxSchemaNameLength()
    throws SQLException
  {
    return 0;
  }
  
  public int getMaxStatementLength()
    throws SQLException
  {
    return MysqlIO.getMaxBuf() - 4;
  }
  
  public int getMaxStatements()
    throws SQLException
  {
    return 0;
  }
  
  public int getMaxTableNameLength()
    throws SQLException
  {
    return 64;
  }
  
  public int getMaxTablesInSelect()
    throws SQLException
  {
    return 256;
  }
  
  public int getMaxUserNameLength()
    throws SQLException
  {
    return 16;
  }
  
  public String getNumericFunctions()
    throws SQLException
  {
    return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
  }
  
  public ResultSet getPrimaryKeys(String catalog, String schema, final String table)
    throws SQLException
  {
    Field[] fields = new Field[6];
    fields[0] = new Field("", "TABLE_CAT", 1, 255);
    fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
    fields[2] = new Field("", "TABLE_NAME", 1, 255);
    fields[3] = new Field("", "COLUMN_NAME", 1, 32);
    fields[4] = new Field("", "KEY_SEQ", 5, 5);
    fields[5] = new Field("", "PK_NAME", 1, 32);
    if (table == null) {
      throw SQLError.createSQLException("Table not specified.", "S1009", getExceptionInterceptor());
    }
    final ArrayList rows = new ArrayList();
    final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
    try
    {
      new IterateBlock(getCatalogIterator(catalog))
      {
        void forEach(Object catalogStr)
          throws SQLException
        {
          ResultSet rs = null;
          try
          {
            StringBuffer queryBuf = new StringBuffer("SHOW KEYS FROM ");
            
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(table);
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(" FROM ");
            queryBuf.append(DatabaseMetaData.this.quotedId);
            queryBuf.append(catalogStr.toString());
            queryBuf.append(DatabaseMetaData.this.quotedId);
            
            rs = stmt.executeQuery(queryBuf.toString());
            
            TreeMap sortMap = new TreeMap();
            while (rs.next())
            {
              String keyType = rs.getString("Key_name");
              if ((keyType != null) && (
                (keyType.equalsIgnoreCase("PRIMARY")) || (keyType.equalsIgnoreCase("PRI"))))
              {
                byte[][] tuple = new byte[6][];
                tuple[0] = (catalogStr.toString() == null ? new byte[0] : DatabaseMetaData.this.s2b(catalogStr.toString()));
                
                tuple[1] = null;
                tuple[2] = DatabaseMetaData.this.s2b(table);
                
                String columnName = rs.getString("Column_name");
                
                tuple[3] = DatabaseMetaData.this.s2b(columnName);
                tuple[4] = DatabaseMetaData.this.s2b(rs.getString("Seq_in_index"));
                tuple[5] = DatabaseMetaData.this.s2b(keyType);
                sortMap.put(columnName, tuple);
              }
            }
            Iterator sortedIterator = sortMap.values().iterator();
            while (sortedIterator.hasNext()) {
              rows.add(new ByteArrayRow((byte[][])sortedIterator.next(), DatabaseMetaData.this.getExceptionInterceptor()));
            }
          }
          finally
          {
            if (rs != null)
            {
              try
              {
                rs.close();
              }
              catch (Exception ex) {}
              rs = null;
            }
          }
        }
      }.doForAll();
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
    ResultSet results = buildResultSet(fields, rows);
    
    return results;
  }
  
  public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
    throws SQLException
  {
    Field[] fields = createProcedureColumnsFields();
    
    return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, procedureNamePattern, columnNamePattern, true, true);
  }
  
  protected Field[] createProcedureColumnsFields()
  {
    Field[] fields = new Field[13];
    
    fields[0] = new Field("", "PROCEDURE_CAT", 1, 512);
    fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 512);
    fields[2] = new Field("", "PROCEDURE_NAME", 1, 512);
    fields[3] = new Field("", "COLUMN_NAME", 1, 512);
    fields[4] = new Field("", "COLUMN_TYPE", 1, 64);
    fields[5] = new Field("", "DATA_TYPE", 5, 6);
    fields[6] = new Field("", "TYPE_NAME", 1, 64);
    fields[7] = new Field("", "PRECISION", 4, 12);
    fields[8] = new Field("", "LENGTH", 4, 12);
    fields[9] = new Field("", "SCALE", 5, 12);
    fields[10] = new Field("", "RADIX", 5, 6);
    fields[11] = new Field("", "NULLABLE", 5, 6);
    fields[12] = new Field("", "REMARKS", 1, 512);
    return fields;
  }
  
  protected ResultSet getProcedureOrFunctionColumns(Field[] fields, String catalog, String schemaPattern, String procedureOrFunctionNamePattern, String columnNamePattern, boolean returnProcedures, boolean returnFunctions)
    throws SQLException
  {
    List proceduresToExtractList = new ArrayList();
    
    ResultSet procedureNameRs = null;
    if (supportsStoredProcedures()) {
      try
      {
        String tmpProcedureOrFunctionNamePattern = null;
        if ((procedureOrFunctionNamePattern != null) && (procedureOrFunctionNamePattern != "%")) {
          tmpProcedureOrFunctionNamePattern = StringUtils.sanitizeProcOrFuncName(procedureOrFunctionNamePattern);
        }
        if (tmpProcedureOrFunctionNamePattern == null)
        {
          tmpProcedureOrFunctionNamePattern = procedureOrFunctionNamePattern;
        }
        else
        {
          String tmpCatalog = catalog;
          List parseList = StringUtils.splitDBdotName(tmpProcedureOrFunctionNamePattern, tmpCatalog, this.quotedId, this.conn.isNoBackslashEscapesSet());
          if (parseList.size() == 2)
          {
            tmpCatalog = (String)parseList.get(0);
            tmpProcedureOrFunctionNamePattern = (String)parseList.get(1);
          }
        }
        procedureNameRs = getProceduresAndOrFunctions(createFieldMetadataForGetProcedures(), catalog, schemaPattern, tmpProcedureOrFunctionNamePattern, returnProcedures, returnFunctions);
        
        String tmpstrPNameRs = null;
        String tmpstrCatNameRs = null;
        
        boolean hasResults = false;
        while (procedureNameRs.next())
        {
          tmpstrCatNameRs = procedureNameRs.getString(1);
          tmpstrPNameRs = procedureNameRs.getString(3);
          if (((!tmpstrCatNameRs.startsWith(this.quotedId)) || (!tmpstrCatNameRs.endsWith(this.quotedId))) && ((!tmpstrCatNameRs.startsWith("\"")) || (!tmpstrCatNameRs.endsWith("\"")))) {
            tmpstrCatNameRs = this.quotedId + tmpstrCatNameRs + this.quotedId;
          }
          if (((!tmpstrPNameRs.startsWith(this.quotedId)) || (!tmpstrPNameRs.endsWith(this.quotedId))) && ((!tmpstrPNameRs.startsWith("\"")) || (!tmpstrPNameRs.endsWith("\"")))) {
            tmpstrPNameRs = this.quotedId + tmpstrPNameRs + this.quotedId;
          }
          if (proceduresToExtractList.indexOf(tmpstrCatNameRs + "." + tmpstrPNameRs) < 0) {
            proceduresToExtractList.add(tmpstrCatNameRs + "." + tmpstrPNameRs);
          }
          hasResults = true;
        }
        if (hasResults) {
          Collections.sort(proceduresToExtractList);
        }
      }
      finally
      {
        SQLException rethrowSqlEx = null;
        if (procedureNameRs != null) {
          try
          {
            procedureNameRs.close();
          }
          catch (SQLException sqlEx)
          {
            rethrowSqlEx = sqlEx;
          }
        }
        if (rethrowSqlEx != null) {
          throw rethrowSqlEx;
        }
      }
    }
    ArrayList resultRows = new ArrayList();
    int idx = 0;
    String procNameToCall = "";
    for (Iterator iter = proceduresToExtractList.iterator(); iter.hasNext();)
    {
      String procName = (String)iter.next();
      if (!" ".equals(this.quotedId)) {
        idx = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procName, ".", this.quotedId.charAt(0), !this.conn.isNoBackslashEscapesSet());
      } else {
        idx = procName.indexOf(".");
      }
      if (idx > 0)
      {
        catalog = procName.substring(0, idx);
        if ((this.quotedId != " ") && (catalog.startsWith(this.quotedId)) && (catalog.endsWith(this.quotedId))) {
          catalog = procName.substring(1, catalog.length() - 1);
        }
        procNameToCall = procName;
      }
      else
      {
        procNameToCall = procName;
      }
      getCallStmtParameterTypes(catalog, procNameToCall, columnNamePattern, resultRows, fields.length == 17);
    }
    return buildResultSet(fields, resultRows);
  }
  
  public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
    throws SQLException
  {
    Field[] fields = createFieldMetadataForGetProcedures();
    
    return getProceduresAndOrFunctions(fields, catalog, schemaPattern, procedureNamePattern, true, true);
  }
  
  private Field[] createFieldMetadataForGetProcedures()
  {
    Field[] fields = new Field[9];
    fields[0] = new Field("", "PROCEDURE_CAT", 1, 255);
    fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 255);
    fields[2] = new Field("", "PROCEDURE_NAME", 1, 255);
    fields[3] = new Field("", "reserved1", 1, 0);
    fields[4] = new Field("", "reserved2", 1, 0);
    fields[5] = new Field("", "reserved3", 1, 0);
    fields[6] = new Field("", "REMARKS", 1, 255);
    fields[7] = new Field("", "PROCEDURE_TYPE", 5, 6);
    fields[8] = new Field("", "SPECIFIC_NAME", 1, 255);
    
    return fields;
  }
  
  protected ResultSet getProceduresAndOrFunctions(final Field[] fields, String catalog, String schemaPattern, String procedureNamePattern, final boolean returnProcedures, final boolean returnFunctions)
    throws SQLException
  {
    if ((procedureNamePattern == null) || (procedureNamePattern.length() == 0)) {
      if (this.conn.getNullNamePatternMatchesAll()) {
        procedureNamePattern = "%";
      } else {
        throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }
    }
    final ArrayList procedureRows = new ArrayList();
    if (supportsStoredProcedures())
    {
      final String procNamePattern = procedureNamePattern;
      
      final Map procedureRowsOrderedByName = new TreeMap();
      
      new IterateBlock(getCatalogIterator(catalog))
      {
        void forEach(Object catalogStr)
          throws SQLException
        {
          String db = catalogStr.toString();
          
          boolean fromSelect = false;
          ResultSet proceduresRs = null;
          boolean needsClientFiltering = true;
          PreparedStatement proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SELECT name, type, comment FROM mysql.proc WHERE name like ? and db <=> ? ORDER BY name");
          try
          {
            boolean hasTypeColumn = false;
            if (db != null)
            {
              if (DatabaseMetaData.this.conn.lowerCaseTableNames()) {
                db = db.toLowerCase();
              }
              proceduresStmt.setString(2, db);
            }
            else
            {
              proceduresStmt.setNull(2, 12);
            }
            int nameIndex = 1;
            if (proceduresStmt.getMaxRows() != 0) {
              proceduresStmt.setMaxRows(0);
            }
            proceduresStmt.setString(1, procNamePattern);
            try
            {
              proceduresRs = proceduresStmt.executeQuery();
              fromSelect = true;
              needsClientFiltering = false;
              hasTypeColumn = true;
            }
            catch (SQLException sqlEx)
            {
              proceduresStmt.close();
              
              fromSelect = false;
              if (DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 1)) {
                nameIndex = 2;
              } else {
                nameIndex = 1;
              }
              proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SHOW PROCEDURE STATUS LIKE ?");
              if (proceduresStmt.getMaxRows() != 0) {
                proceduresStmt.setMaxRows(0);
              }
              proceduresStmt.setString(1, procNamePattern);
              
              proceduresRs = proceduresStmt.executeQuery();
            }
            if (returnProcedures) {
              DatabaseMetaData.this.convertToJdbcProcedureList(fromSelect, db, proceduresRs, needsClientFiltering, db, procedureRowsOrderedByName, nameIndex);
            }
            if (!hasTypeColumn)
            {
              if (proceduresStmt != null) {
                proceduresStmt.close();
              }
              proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SHOW FUNCTION STATUS LIKE ?");
              if (proceduresStmt.getMaxRows() != 0) {
                proceduresStmt.setMaxRows(0);
              }
              proceduresStmt.setString(1, procNamePattern);
              
              proceduresRs = proceduresStmt.executeQuery();
            }
            if (returnFunctions) {
              DatabaseMetaData.this.convertToJdbcFunctionList(db, proceduresRs, needsClientFiltering, db, procedureRowsOrderedByName, nameIndex, fields);
            }
            Iterator proceduresIter = procedureRowsOrderedByName.values().iterator();
            while (proceduresIter.hasNext()) {
              procedureRows.add(proceduresIter.next());
            }
          }
          finally
          {
            SQLException rethrowSqlEx = null;
            if (proceduresRs != null) {
              try
              {
                proceduresRs.close();
              }
              catch (SQLException sqlEx)
              {
                rethrowSqlEx = sqlEx;
              }
            }
            if (proceduresStmt != null) {
              try
              {
                proceduresStmt.close();
              }
              catch (SQLException sqlEx)
              {
                rethrowSqlEx = sqlEx;
              }
            }
            if (rethrowSqlEx != null) {
              throw rethrowSqlEx;
            }
          }
        }
      }.doForAll();
    }
    return buildResultSet(fields, procedureRows);
  }
  
  public String getProcedureTerm()
    throws SQLException
  {
    return "PROCEDURE";
  }
  
  public int getResultSetHoldability()
    throws SQLException
  {
    return 1;
  }
  
  private void getResultsImpl(String catalog, String table, String keysComment, List tuples, String fkTableName, boolean isExport)
    throws SQLException
  {
    LocalAndReferencedColumns parsedInfo = parseTableStatusIntoLocalAndReferencedColumns(keysComment);
    if ((isExport) && (!parsedInfo.referencedTable.equals(table))) {
      return;
    }
    if (parsedInfo.localColumnsList.size() != parsedInfo.referencedColumnsList.size()) {
      throw SQLError.createSQLException("Error parsing foreign keys definition,number of local and referenced columns is not the same.", "S1000", getExceptionInterceptor());
    }
    Iterator localColumnNames = parsedInfo.localColumnsList.iterator();
    Iterator referColumnNames = parsedInfo.referencedColumnsList.iterator();
    
    int keySeqIndex = 1;
    while (localColumnNames.hasNext())
    {
      byte[][] tuple = new byte[14][];
      String lColumnName = removeQuotedId(localColumnNames.next().toString());
      
      String rColumnName = removeQuotedId(referColumnNames.next().toString());
      
      tuple[4] = (catalog == null ? new byte[0] : s2b(catalog));
      
      tuple[5] = null;
      tuple[6] = s2b(isExport ? fkTableName : table);
      tuple[7] = s2b(lColumnName);
      tuple[0] = s2b(parsedInfo.referencedCatalog);
      tuple[1] = null;
      tuple[2] = s2b(isExport ? table : parsedInfo.referencedTable);
      
      tuple[3] = s2b(rColumnName);
      tuple[8] = s2b(Integer.toString(keySeqIndex++));
      
      int[] actions = getForeignKeyActions(keysComment);
      
      tuple[9] = s2b(Integer.toString(actions[1]));
      tuple[10] = s2b(Integer.toString(actions[0]));
      tuple[11] = s2b(parsedInfo.constraintName);
      tuple[12] = null;
      tuple[13] = s2b(Integer.toString(7));
      
      tuples.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
    }
  }
  
  public ResultSet getSchemas()
    throws SQLException
  {
    Field[] fields = new Field[2];
    fields[0] = new Field("", "TABLE_SCHEM", 1, 0);
    fields[1] = new Field("", "TABLE_CATALOG", 1, 0);
    
    ArrayList tuples = new ArrayList();
    ResultSet results = buildResultSet(fields, tuples);
    
    return results;
  }
  
  public String getSchemaTerm()
    throws SQLException
  {
    return "";
  }
  
  public String getSearchStringEscape()
    throws SQLException
  {
    return "\\";
  }
  
  public String getSQLKeywords()
    throws SQLException
  {
    return mysqlKeywordsThatArentSQL92;
  }
  
  public int getSQLStateType()
    throws SQLException
  {
    if (this.conn.versionMeetsMinimum(4, 1, 0)) {
      return 2;
    }
    if (this.conn.getUseSqlStateCodes()) {
      return 2;
    }
    return 1;
  }
  
  public String getStringFunctions()
    throws SQLException
  {
    return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
  }
  
  public ResultSet getSuperTables(String arg0, String arg1, String arg2)
    throws SQLException
  {
    Field[] fields = new Field[4];
    fields[0] = new Field("", "TABLE_CAT", 1, 32);
    fields[1] = new Field("", "TABLE_SCHEM", 1, 32);
    fields[2] = new Field("", "TABLE_NAME", 1, 32);
    fields[3] = new Field("", "SUPERTABLE_NAME", 1, 32);
    
    return buildResultSet(fields, new ArrayList());
  }
  
  public ResultSet getSuperTypes(String arg0, String arg1, String arg2)
    throws SQLException
  {
    Field[] fields = new Field[6];
    fields[0] = new Field("", "TYPE_CAT", 1, 32);
    fields[1] = new Field("", "TYPE_SCHEM", 1, 32);
    fields[2] = new Field("", "TYPE_NAME", 1, 32);
    fields[3] = new Field("", "SUPERTYPE_CAT", 1, 32);
    fields[4] = new Field("", "SUPERTYPE_SCHEM", 1, 32);
    fields[5] = new Field("", "SUPERTYPE_NAME", 1, 32);
    
    return buildResultSet(fields, new ArrayList());
  }
  
  public String getSystemFunctions()
    throws SQLException
  {
    return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
  }
  
  private String getTableNameWithCase(String table)
  {
    String tableNameWithCase = this.conn.lowerCaseTableNames() ? table.toLowerCase() : table;
    
    return tableNameWithCase;
  }
  
  public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
    throws SQLException
  {
    if (tableNamePattern == null) {
      if (this.conn.getNullNamePatternMatchesAll()) {
        tableNamePattern = "%";
      } else {
        throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }
    }
    Field[] fields = new Field[7];
    fields[0] = new Field("", "TABLE_CAT", 1, 64);
    fields[1] = new Field("", "TABLE_SCHEM", 1, 1);
    fields[2] = new Field("", "TABLE_NAME", 1, 64);
    fields[3] = new Field("", "GRANTOR", 1, 77);
    fields[4] = new Field("", "GRANTEE", 1, 77);
    fields[5] = new Field("", "PRIVILEGE", 1, 64);
    fields[6] = new Field("", "IS_GRANTABLE", 1, 3);
    
    StringBuffer grantQuery = new StringBuffer("SELECT host,db,table_name,grantor,user,table_priv from mysql.tables_priv ");
    
    grantQuery.append(" WHERE ");
    if ((catalog != null) && (catalog.length() != 0))
    {
      grantQuery.append(" db='");
      grantQuery.append(catalog);
      grantQuery.append("' AND ");
    }
    grantQuery.append("table_name like '");
    grantQuery.append(tableNamePattern);
    grantQuery.append("'");
    
    ResultSet results = null;
    ArrayList grantRows = new ArrayList();
    java.sql.Statement stmt = null;
    try
    {
      stmt = this.conn.createStatement();
      stmt.setEscapeProcessing(false);
      
      results = stmt.executeQuery(grantQuery.toString());
      while (results.next())
      {
        String host = results.getString(1);
        String db = results.getString(2);
        String table = results.getString(3);
        String grantor = results.getString(4);
        String user = results.getString(5);
        if ((user == null) || (user.length() == 0)) {
          user = "%";
        }
        StringBuffer fullUser = new StringBuffer(user);
        if ((host != null) && (this.conn.getUseHostsInPrivileges()))
        {
          fullUser.append("@");
          fullUser.append(host);
        }
        String allPrivileges = results.getString(6);
        if (allPrivileges != null)
        {
          allPrivileges = allPrivileges.toUpperCase(Locale.ENGLISH);
          
          StringTokenizer st = new StringTokenizer(allPrivileges, ",");
          while (st.hasMoreTokens())
          {
            String privilege = st.nextToken().trim();
            
            ResultSet columnResults = null;
            try
            {
              columnResults = getColumns(catalog, schemaPattern, table, "%");
              while (columnResults.next())
              {
                byte[][] tuple = new byte[8][];
                tuple[0] = s2b(db);
                tuple[1] = null;
                tuple[2] = s2b(table);
                if (grantor != null) {
                  tuple[3] = s2b(grantor);
                } else {
                  tuple[3] = null;
                }
                tuple[4] = s2b(fullUser.toString());
                tuple[5] = s2b(privilege);
                tuple[6] = null;
                grantRows.add(new ByteArrayRow(tuple, getExceptionInterceptor()));
              }
            }
            finally
            {
              if (columnResults != null) {
                try
                {
                  columnResults.close();
                }
                catch (Exception ex) {}
              }
            }
          }
        }
      }
    }
    finally
    {
      if (results != null)
      {
        try
        {
          results.close();
        }
        catch (Exception ex) {}
        results = null;
      }
      if (stmt != null)
      {
        try
        {
          stmt.close();
        }
        catch (Exception ex) {}
        stmt = null;
      }
    }
    return buildResultSet(fields, grantRows);
  }
  
  public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, final String[] types)
    throws SQLException
  {
    if (tableNamePattern == null) {
      if (this.conn.getNullNamePatternMatchesAll()) {
        tableNamePattern = "%";
      } else {
        throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009", getExceptionInterceptor());
      }
    }
    Field[] fields = new Field[5];
    fields[0] = new Field("", "TABLE_CAT", 12, 255);
    fields[1] = new Field("", "TABLE_SCHEM", 12, 0);
    fields[2] = new Field("", "TABLE_NAME", 12, 255);
    fields[3] = new Field("", "TABLE_TYPE", 12, 5);
    fields[4] = new Field("", "REMARKS", 12, 0);
    
    final ArrayList tuples = new ArrayList();
    
    final java.sql.Statement stmt = this.conn.getMetadataSafeStatement();
    
    List parseList = StringUtils.splitDBdotName(tableNamePattern, "", this.quotedId, this.conn.isNoBackslashEscapesSet());
    String tableNamePat;
    final String tableNamePat;
    if (parseList.size() == 2) {
      tableNamePat = (String)parseList.get(1);
    } else {
      tableNamePat = tableNamePattern;
    }
    final boolean operatingOnInformationSchema = "information_schema".equalsIgnoreCase(catalog);
    try
    {
      new IterateBlock(getCatalogIterator(catalog))
      {
        void forEach(Object catalogStr)
          throws SQLException
        {
          ResultSet results = null;
          try
          {
            if (!DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 2)) {
              try
              {
                results = stmt.executeQuery("SHOW TABLES FROM " + DatabaseMetaData.this.quotedId + catalogStr.toString() + DatabaseMetaData.this.quotedId + " LIKE '" + tableNamePat + "'");
              }
              catch (SQLException sqlEx)
              {
                if ("08S01".equals(sqlEx.getSQLState())) {
                  throw sqlEx;
                }
                return;
              }
            } else {
              try
              {
                results = stmt.executeQuery("SHOW FULL TABLES FROM " + DatabaseMetaData.this.quotedId + catalogStr.toString() + DatabaseMetaData.this.quotedId + " LIKE '" + tableNamePat + "'");
              }
              catch (SQLException sqlEx)
              {
                if ("08S01".equals(sqlEx.getSQLState())) {
                  throw sqlEx;
                }
                return;
              }
            }
            boolean shouldReportTables = false;
            boolean shouldReportViews = false;
            boolean shouldReportSystemTables = false;
            if ((types == null) || (types.length == 0))
            {
              shouldReportTables = true;
              shouldReportViews = true;
              shouldReportSystemTables = true;
            }
            else
            {
              for (int i = 0; i < types.length; i++)
              {
                if ("TABLE".equalsIgnoreCase(types[i])) {
                  shouldReportTables = true;
                }
                if ("VIEW".equalsIgnoreCase(types[i])) {
                  shouldReportViews = true;
                }
                if ("SYSTEM TABLE".equalsIgnoreCase(types[i])) {
                  shouldReportSystemTables = true;
                }
              }
            }
            int typeColumnIndex = 0;
            boolean hasTableTypes = false;
            if (DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 2)) {
              try
              {
                typeColumnIndex = results.findColumn("table_type");
                
                hasTableTypes = true;
              }
              catch (SQLException sqlEx)
              {
                try
                {
                  typeColumnIndex = results.findColumn("Type");
                  
                  hasTableTypes = true;
                }
                catch (SQLException sqlEx2)
                {
                  hasTableTypes = false;
                }
              }
            }
            TreeMap tablesOrderedByName = null;
            TreeMap viewsOrderedByName = null;
            while (results.next())
            {
              byte[][] row = new byte[5][];
              row[0] = (catalogStr.toString() == null ? null : DatabaseMetaData.this.s2b(catalogStr.toString()));
              
              row[1] = null;
              row[2] = results.getBytes(1);
              row[4] = new byte[0];
              if (hasTableTypes)
              {
                String tableType = results.getString(typeColumnIndex);
                if ((("table".equalsIgnoreCase(tableType)) || ("base table".equalsIgnoreCase(tableType))) && (shouldReportTables))
                {
                  boolean reportTable = false;
                  if ((!operatingOnInformationSchema) && (shouldReportTables))
                  {
                    row[3] = DatabaseMetaData.TABLE_AS_BYTES;
                    reportTable = true;
                  }
                  else if ((operatingOnInformationSchema) && (shouldReportSystemTables))
                  {
                    row[3] = DatabaseMetaData.SYSTEM_TABLE_AS_BYTES;
                    reportTable = true;
                  }
                  if (reportTable)
                  {
                    if (tablesOrderedByName == null) {
                      tablesOrderedByName = new TreeMap();
                    }
                    tablesOrderedByName.put(results.getString(1), row);
                  }
                }
                else if (("system view".equalsIgnoreCase(tableType)) && (shouldReportSystemTables))
                {
                  row[3] = DatabaseMetaData.SYSTEM_TABLE_AS_BYTES;
                  if (tablesOrderedByName == null) {
                    tablesOrderedByName = new TreeMap();
                  }
                  tablesOrderedByName.put(results.getString(1), row);
                }
                else if (("view".equalsIgnoreCase(tableType)) && (shouldReportViews))
                {
                  row[3] = DatabaseMetaData.VIEW_AS_BYTES;
                  if (viewsOrderedByName == null) {
                    viewsOrderedByName = new TreeMap();
                  }
                  viewsOrderedByName.put(results.getString(1), row);
                }
                else if (!hasTableTypes)
                {
                  row[3] = DatabaseMetaData.TABLE_AS_BYTES;
                  if (tablesOrderedByName == null) {
                    tablesOrderedByName = new TreeMap();
                  }
                  tablesOrderedByName.put(results.getString(1), row);
                }
              }
              else if (shouldReportTables)
              {
                row[3] = DatabaseMetaData.TABLE_AS_BYTES;
                if (tablesOrderedByName == null) {
                  tablesOrderedByName = new TreeMap();
                }
                tablesOrderedByName.put(results.getString(1), row);
              }
            }
            if (tablesOrderedByName != null)
            {
              Iterator tablesIter = tablesOrderedByName.values().iterator();
              while (tablesIter.hasNext()) {
                tuples.add(new ByteArrayRow((byte[][])tablesIter.next(), DatabaseMetaData.this.getExceptionInterceptor()));
              }
            }
            if (viewsOrderedByName != null)
            {
              Iterator viewsIter = viewsOrderedByName.values().iterator();
              while (viewsIter.hasNext()) {
                tuples.add(new ByteArrayRow((byte[][])viewsIter.next(), DatabaseMetaData.this.getExceptionInterceptor()));
              }
            }
          }
          finally
          {
            if (results != null)
            {
              try
              {
                results.close();
              }
              catch (Exception ex) {}
              results = null;
            }
          }
        }
      }.doForAll();
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
    ResultSet tables = buildResultSet(fields, tuples);
    
    return tables;
  }
  
  public ResultSet getTableTypes()
    throws SQLException
  {
    ArrayList tuples = new ArrayList();
    Field[] fields = new Field[1];
    fields[0] = new Field("", "TABLE_TYPE", 12, 5);
    
    byte[][] tableTypeRow = new byte[1][];
    tableTypeRow[0] = TABLE_AS_BYTES;
    tuples.add(new ByteArrayRow(tableTypeRow, getExceptionInterceptor()));
    if (this.conn.versionMeetsMinimum(5, 0, 1))
    {
      byte[][] viewTypeRow = new byte[1][];
      viewTypeRow[0] = VIEW_AS_BYTES;
      tuples.add(new ByteArrayRow(viewTypeRow, getExceptionInterceptor()));
    }
    byte[][] tempTypeRow = new byte[1][];
    tempTypeRow[0] = s2b("LOCAL TEMPORARY");
    tuples.add(new ByteArrayRow(tempTypeRow, getExceptionInterceptor()));
    
    return buildResultSet(fields, tuples);
  }
  
  public String getTimeDateFunctions()
    throws SQLException
  {
    return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
  }
  
  public ResultSet getTypeInfo()
    throws SQLException
  {
    Field[] fields = new Field[18];
    fields[0] = new Field("", "TYPE_NAME", 1, 32);
    fields[1] = new Field("", "DATA_TYPE", 4, 5);
    fields[2] = new Field("", "PRECISION", 4, 10);
    fields[3] = new Field("", "LITERAL_PREFIX", 1, 4);
    fields[4] = new Field("", "LITERAL_SUFFIX", 1, 4);
    fields[5] = new Field("", "CREATE_PARAMS", 1, 32);
    fields[6] = new Field("", "NULLABLE", 5, 5);
    fields[7] = new Field("", "CASE_SENSITIVE", 16, 3);
    fields[8] = new Field("", "SEARCHABLE", 5, 3);
    fields[9] = new Field("", "UNSIGNED_ATTRIBUTE", 16, 3);
    fields[10] = new Field("", "FIXED_PREC_SCALE", 16, 3);
    fields[11] = new Field("", "AUTO_INCREMENT", 16, 3);
    fields[12] = new Field("", "LOCAL_TYPE_NAME", 1, 32);
    fields[13] = new Field("", "MINIMUM_SCALE", 5, 5);
    fields[14] = new Field("", "MAXIMUM_SCALE", 5, 5);
    fields[15] = new Field("", "SQL_DATA_TYPE", 4, 10);
    fields[16] = new Field("", "SQL_DATETIME_SUB", 4, 10);
    fields[17] = new Field("", "NUM_PREC_RADIX", 4, 10);
    
    byte[][] rowVal = (byte[][])null;
    ArrayList tuples = new ArrayList();
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("BIT");
    rowVal[1] = Integer.toString(-7).getBytes();
    
    rowVal[2] = s2b("1");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("BIT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("BOOL");
    rowVal[1] = Integer.toString(-7).getBytes();
    
    rowVal[2] = s2b("1");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("BOOL");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("TINYINT");
    rowVal[1] = Integer.toString(-6).getBytes();
    
    rowVal[2] = s2b("3");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("TINYINT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("TINYINT UNSIGNED");
    rowVal[1] = Integer.toString(-6).getBytes();
    
    rowVal[2] = s2b("3");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("TINYINT UNSIGNED");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("BIGINT");
    rowVal[1] = Integer.toString(-5).getBytes();
    
    rowVal[2] = s2b("19");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("BIGINT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("BIGINT UNSIGNED");
    rowVal[1] = Integer.toString(-5).getBytes();
    
    rowVal[2] = s2b("20");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("BIGINT UNSIGNED");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("LONG VARBINARY");
    rowVal[1] = Integer.toString(-4).getBytes();
    
    rowVal[2] = s2b("16777215");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("LONG VARBINARY");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("MEDIUMBLOB");
    rowVal[1] = Integer.toString(-4).getBytes();
    
    rowVal[2] = s2b("16777215");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("MEDIUMBLOB");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("LONGBLOB");
    rowVal[1] = Integer.toString(-4).getBytes();
    
    rowVal[2] = Integer.toString(Integer.MAX_VALUE).getBytes();
    
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("LONGBLOB");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("BLOB");
    rowVal[1] = Integer.toString(-4).getBytes();
    
    rowVal[2] = s2b("65535");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("BLOB");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("TINYBLOB");
    rowVal[1] = Integer.toString(-4).getBytes();
    
    rowVal[2] = s2b("255");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("TINYBLOB");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("VARBINARY");
    rowVal[1] = Integer.toString(-3).getBytes();
    
    rowVal[2] = s2b("255");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("(M)");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("VARBINARY");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("BINARY");
    rowVal[1] = Integer.toString(-2).getBytes();
    
    rowVal[2] = s2b("255");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("(M)");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("true");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("BINARY");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("LONG VARCHAR");
    rowVal[1] = Integer.toString(-1).getBytes();
    
    rowVal[2] = s2b("16777215");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("LONG VARCHAR");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("MEDIUMTEXT");
    rowVal[1] = Integer.toString(-1).getBytes();
    
    rowVal[2] = s2b("16777215");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("MEDIUMTEXT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("LONGTEXT");
    rowVal[1] = Integer.toString(-1).getBytes();
    
    rowVal[2] = Integer.toString(Integer.MAX_VALUE).getBytes();
    
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("LONGTEXT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("TEXT");
    rowVal[1] = Integer.toString(-1).getBytes();
    
    rowVal[2] = s2b("65535");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("TEXT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("TINYTEXT");
    rowVal[1] = Integer.toString(-1).getBytes();
    
    rowVal[2] = s2b("255");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("TINYTEXT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("CHAR");
    rowVal[1] = Integer.toString(1).getBytes();
    
    rowVal[2] = s2b("255");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("(M)");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("CHAR");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    int decimalPrecision = 254;
    if (this.conn.versionMeetsMinimum(5, 0, 3)) {
      if (this.conn.versionMeetsMinimum(5, 0, 6)) {
        decimalPrecision = 65;
      } else {
        decimalPrecision = 64;
      }
    }
    rowVal = new byte[18][];
    rowVal[0] = s2b("NUMERIC");
    rowVal[1] = Integer.toString(2).getBytes();
    
    rowVal[2] = s2b(String.valueOf(decimalPrecision));
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("NUMERIC");
    rowVal[13] = s2b("-308");
    rowVal[14] = s2b("308");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("DECIMAL");
    rowVal[1] = Integer.toString(3).getBytes();
    
    rowVal[2] = s2b(String.valueOf(decimalPrecision));
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("DECIMAL");
    rowVal[13] = s2b("-308");
    rowVal[14] = s2b("308");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("INTEGER");
    rowVal[1] = Integer.toString(4).getBytes();
    
    rowVal[2] = s2b("10");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("INTEGER");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("INTEGER UNSIGNED");
    rowVal[1] = Integer.toString(4).getBytes();
    
    rowVal[2] = s2b("10");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("INTEGER UNSIGNED");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("INT");
    rowVal[1] = Integer.toString(4).getBytes();
    
    rowVal[2] = s2b("10");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("INT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("INT UNSIGNED");
    rowVal[1] = Integer.toString(4).getBytes();
    
    rowVal[2] = s2b("10");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("INT UNSIGNED");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("MEDIUMINT");
    rowVal[1] = Integer.toString(4).getBytes();
    
    rowVal[2] = s2b("7");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("MEDIUMINT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("MEDIUMINT UNSIGNED");
    rowVal[1] = Integer.toString(4).getBytes();
    
    rowVal[2] = s2b("8");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("MEDIUMINT UNSIGNED");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("SMALLINT");
    rowVal[1] = Integer.toString(5).getBytes();
    
    rowVal[2] = s2b("5");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("SMALLINT");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("SMALLINT UNSIGNED");
    rowVal[1] = Integer.toString(5).getBytes();
    
    rowVal[2] = s2b("5");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("true");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("SMALLINT UNSIGNED");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("FLOAT");
    rowVal[1] = Integer.toString(7).getBytes();
    
    rowVal[2] = s2b("10");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("FLOAT");
    rowVal[13] = s2b("-38");
    rowVal[14] = s2b("38");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("DOUBLE");
    rowVal[1] = Integer.toString(8).getBytes();
    
    rowVal[2] = s2b("17");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("DOUBLE");
    rowVal[13] = s2b("-308");
    rowVal[14] = s2b("308");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("DOUBLE PRECISION");
    rowVal[1] = Integer.toString(8).getBytes();
    
    rowVal[2] = s2b("17");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("DOUBLE PRECISION");
    rowVal[13] = s2b("-308");
    rowVal[14] = s2b("308");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("REAL");
    rowVal[1] = Integer.toString(8).getBytes();
    
    rowVal[2] = s2b("17");
    rowVal[3] = s2b("");
    rowVal[4] = s2b("");
    rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("true");
    rowVal[12] = s2b("REAL");
    rowVal[13] = s2b("-308");
    rowVal[14] = s2b("308");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("VARCHAR");
    rowVal[1] = Integer.toString(12).getBytes();
    
    rowVal[2] = s2b("255");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("(M)");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("VARCHAR");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("ENUM");
    rowVal[1] = Integer.toString(12).getBytes();
    
    rowVal[2] = s2b("65535");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("ENUM");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("SET");
    rowVal[1] = Integer.toString(12).getBytes();
    
    rowVal[2] = s2b("64");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("SET");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("DATE");
    rowVal[1] = Integer.toString(91).getBytes();
    
    rowVal[2] = s2b("0");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("DATE");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("TIME");
    rowVal[1] = Integer.toString(92).getBytes();
    
    rowVal[2] = s2b("0");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("TIME");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("DATETIME");
    rowVal[1] = Integer.toString(93).getBytes();
    
    rowVal[2] = s2b("0");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("DATETIME");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    rowVal = new byte[18][];
    rowVal[0] = s2b("TIMESTAMP");
    rowVal[1] = Integer.toString(93).getBytes();
    
    rowVal[2] = s2b("0");
    rowVal[3] = s2b("'");
    rowVal[4] = s2b("'");
    rowVal[5] = s2b("[(M)]");
    rowVal[6] = Integer.toString(1).getBytes();
    
    rowVal[7] = s2b("false");
    rowVal[8] = Integer.toString(3).getBytes();
    
    rowVal[9] = s2b("false");
    rowVal[10] = s2b("false");
    rowVal[11] = s2b("false");
    rowVal[12] = s2b("TIMESTAMP");
    rowVal[13] = s2b("0");
    rowVal[14] = s2b("0");
    rowVal[15] = s2b("0");
    rowVal[16] = s2b("0");
    rowVal[17] = s2b("10");
    tuples.add(new ByteArrayRow(rowVal, getExceptionInterceptor()));
    
    return buildResultSet(fields, tuples);
  }
  
  public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
    throws SQLException
  {
    Field[] fields = new Field[6];
    fields[0] = new Field("", "TYPE_CAT", 12, 32);
    fields[1] = new Field("", "TYPE_SCHEM", 12, 32);
    fields[2] = new Field("", "TYPE_NAME", 12, 32);
    fields[3] = new Field("", "CLASS_NAME", 12, 32);
    fields[4] = new Field("", "DATA_TYPE", 12, 32);
    fields[5] = new Field("", "REMARKS", 12, 32);
    
    ArrayList tuples = new ArrayList();
    
    return buildResultSet(fields, tuples);
  }
  
  public String getURL()
    throws SQLException
  {
    return this.conn.getURL();
  }
  
  public String getUserName()
    throws SQLException
  {
    if (this.conn.getUseHostsInPrivileges())
    {
      java.sql.Statement stmt = null;
      ResultSet rs = null;
      try
      {
        stmt = this.conn.createStatement();
        stmt.setEscapeProcessing(false);
        
        rs = stmt.executeQuery("SELECT USER()");
        rs.next();
        
        return rs.getString(1);
      }
      finally
      {
        if (rs != null)
        {
          try
          {
            rs.close();
          }
          catch (Exception ex)
          {
            AssertionFailedException.shouldNotHappen(ex);
          }
          rs = null;
        }
        if (stmt != null)
        {
          try
          {
            stmt.close();
          }
          catch (Exception ex)
          {
            AssertionFailedException.shouldNotHappen(ex);
          }
          stmt = null;
        }
      }
    }
    return this.conn.getUser();
  }
  
  public ResultSet getVersionColumns(String catalog, String schema, String table)
    throws SQLException
  {
    Field[] fields = new Field[8];
    fields[0] = new Field("", "SCOPE", 5, 5);
    fields[1] = new Field("", "COLUMN_NAME", 1, 32);
    fields[2] = new Field("", "DATA_TYPE", 4, 5);
    fields[3] = new Field("", "TYPE_NAME", 1, 16);
    fields[4] = new Field("", "COLUMN_SIZE", 4, 16);
    fields[5] = new Field("", "BUFFER_LENGTH", 4, 16);
    fields[6] = new Field("", "DECIMAL_DIGITS", 5, 16);
    fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
    
    return buildResultSet(fields, new ArrayList());
  }
  
  public boolean insertsAreDetected(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean isCatalogAtStart()
    throws SQLException
  {
    return true;
  }
  
  public boolean isReadOnly()
    throws SQLException
  {
    return false;
  }
  
  public boolean locatorsUpdateCopy()
    throws SQLException
  {
    return !this.conn.getEmulateLocators();
  }
  
  public boolean nullPlusNonNullIsNull()
    throws SQLException
  {
    return true;
  }
  
  public boolean nullsAreSortedAtEnd()
    throws SQLException
  {
    return false;
  }
  
  public boolean nullsAreSortedAtStart()
    throws SQLException
  {
    return (this.conn.versionMeetsMinimum(4, 0, 2)) && (!this.conn.versionMeetsMinimum(4, 0, 11));
  }
  
  public boolean nullsAreSortedHigh()
    throws SQLException
  {
    return false;
  }
  
  public boolean nullsAreSortedLow()
    throws SQLException
  {
    return !nullsAreSortedHigh();
  }
  
  public boolean othersDeletesAreVisible(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean othersInsertsAreVisible(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean othersUpdatesAreVisible(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean ownDeletesAreVisible(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean ownInsertsAreVisible(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean ownUpdatesAreVisible(int type)
    throws SQLException
  {
    return false;
  }
  
  private LocalAndReferencedColumns parseTableStatusIntoLocalAndReferencedColumns(String keysComment)
    throws SQLException
  {
    String columnsDelimitter = ",";
    
    char quoteChar = this.quotedId.length() == 0 ? '\000' : this.quotedId.charAt(0);
    
    int indexOfOpenParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysComment, "(", quoteChar, true);
    if (indexOfOpenParenLocalColumns == -1) {
      throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of local columns list.", "S1000", getExceptionInterceptor());
    }
    String constraintName = removeQuotedId(keysComment.substring(0, indexOfOpenParenLocalColumns).trim());
    
    keysComment = keysComment.substring(indexOfOpenParenLocalColumns, keysComment.length());
    
    String keysCommentTrimmed = keysComment.trim();
    
    int indexOfCloseParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, ")", quoteChar, true);
    if (indexOfCloseParenLocalColumns == -1) {
      throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of local columns list.", "S1000", getExceptionInterceptor());
    }
    String localColumnNamesString = keysCommentTrimmed.substring(1, indexOfCloseParenLocalColumns);
    
    int indexOfRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, "REFER ", this.quotedId.charAt(0), true);
    if (indexOfRefer == -1) {
      throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced tables list.", "S1000", getExceptionInterceptor());
    }
    int indexOfOpenParenReferCol = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfRefer, keysCommentTrimmed, "(", quoteChar, false);
    if (indexOfOpenParenReferCol == -1) {
      throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced columns list.", "S1000", getExceptionInterceptor());
    }
    String referCatalogTableString = keysCommentTrimmed.substring(indexOfRefer + "REFER ".length(), indexOfOpenParenReferCol);
    
    int indexOfSlash = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referCatalogTableString, "/", this.quotedId.charAt(0), false);
    if (indexOfSlash == -1) {
      throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find name of referenced catalog.", "S1000", getExceptionInterceptor());
    }
    String referCatalog = removeQuotedId(referCatalogTableString.substring(0, indexOfSlash));
    
    String referTable = removeQuotedId(referCatalogTableString.substring(indexOfSlash + 1).trim());
    
    int indexOfCloseParenRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfOpenParenReferCol, keysCommentTrimmed, ")", quoteChar, true);
    if (indexOfCloseParenRefer == -1) {
      throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of referenced columns list.", "S1000", getExceptionInterceptor());
    }
    String referColumnNamesString = keysCommentTrimmed.substring(indexOfOpenParenReferCol + 1, indexOfCloseParenRefer);
    
    List referColumnsList = StringUtils.split(referColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
    
    List localColumnsList = StringUtils.split(localColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
    
    return new LocalAndReferencedColumns(localColumnsList, referColumnsList, constraintName, referCatalog, referTable);
  }
  
  private String removeQuotedId(String s)
  {
    if (s == null) {
      return null;
    }
    if (this.quotedId.equals("")) {
      return s;
    }
    s = s.trim();
    
    int frontOffset = 0;
    int backOffset = s.length();
    int quoteLength = this.quotedId.length();
    if (s.startsWith(this.quotedId)) {
      frontOffset = quoteLength;
    }
    if (s.endsWith(this.quotedId)) {
      backOffset -= quoteLength;
    }
    return s.substring(frontOffset, backOffset);
  }
  
  protected byte[] s2b(String s)
    throws SQLException
  {
    if (s == null) {
      return null;
    }
    return StringUtils.getBytes(s, this.conn.getCharacterSetMetadata(), this.conn.getServerCharacterEncoding(), this.conn.parserKnowsUnicode(), this.conn, getExceptionInterceptor());
  }
  
  public boolean storesLowerCaseIdentifiers()
    throws SQLException
  {
    return this.conn.storesLowerCaseTableName();
  }
  
  public boolean storesLowerCaseQuotedIdentifiers()
    throws SQLException
  {
    return this.conn.storesLowerCaseTableName();
  }
  
  public boolean storesMixedCaseIdentifiers()
    throws SQLException
  {
    return !this.conn.storesLowerCaseTableName();
  }
  
  public boolean storesMixedCaseQuotedIdentifiers()
    throws SQLException
  {
    return !this.conn.storesLowerCaseTableName();
  }
  
  public boolean storesUpperCaseIdentifiers()
    throws SQLException
  {
    return false;
  }
  
  public boolean storesUpperCaseQuotedIdentifiers()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsAlterTableWithAddColumn()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsAlterTableWithDropColumn()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsANSI92EntryLevelSQL()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsANSI92FullSQL()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsANSI92IntermediateSQL()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsBatchUpdates()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsCatalogsInDataManipulation()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInIndexDefinitions()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInPrivilegeDefinitions()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInProcedureCalls()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsCatalogsInTableDefinitions()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(3, 22, 0);
  }
  
  public boolean supportsColumnAliasing()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsConvert()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsConvert(int fromType, int toType)
    throws SQLException
  {
    switch (fromType)
    {
    case -4: 
    case -3: 
    case -2: 
    case -1: 
    case 1: 
    case 12: 
      switch (toType)
      {
      case -6: 
      case -5: 
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 12: 
      case 91: 
      case 92: 
      case 93: 
      case 1111: 
        return true;
      }
      return false;
    case -7: 
      return false;
    case -6: 
    case -5: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
      switch (toType)
      {
      case -6: 
      case -5: 
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 12: 
        return true;
      }
      return false;
    case 0: 
      return false;
    case 1111: 
      switch (toType)
      {
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 1: 
      case 12: 
        return true;
      }
      return false;
    case 91: 
      switch (toType)
      {
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 1: 
      case 12: 
        return true;
      }
      return false;
    case 92: 
      switch (toType)
      {
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 1: 
      case 12: 
        return true;
      }
      return false;
    case 93: 
      switch (toType)
      {
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 1: 
      case 12: 
      case 91: 
      case 92: 
        return true;
      }
      return false;
    }
    return false;
  }
  
  public boolean supportsCoreSQLGrammar()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsCorrelatedSubqueries()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsDataDefinitionAndDataManipulationTransactions()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsDataManipulationTransactionsOnly()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsDifferentTableCorrelationNames()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsExpressionsInOrderBy()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsExtendedSQLGrammar()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsFullOuterJoins()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsGetGeneratedKeys()
  {
    return true;
  }
  
  public boolean supportsGroupBy()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsGroupByBeyondSelect()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsGroupByUnrelated()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsIntegrityEnhancementFacility()
    throws SQLException
  {
    if (!this.conn.getOverrideSupportsIntegrityEnhancementFacility()) {
      return false;
    }
    return true;
  }
  
  public boolean supportsLikeEscapeClause()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsLimitedOuterJoins()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsMinimumSQLGrammar()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsMixedCaseIdentifiers()
    throws SQLException
  {
    return !this.conn.lowerCaseTableNames();
  }
  
  public boolean supportsMixedCaseQuotedIdentifiers()
    throws SQLException
  {
    return !this.conn.lowerCaseTableNames();
  }
  
  public boolean supportsMultipleOpenResults()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsMultipleResultSets()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsMultipleTransactions()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsNamedParameters()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsNonNullableColumns()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsOpenCursorsAcrossCommit()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsOpenCursorsAcrossRollback()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsOpenStatementsAcrossCommit()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsOpenStatementsAcrossRollback()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsOrderByUnrelated()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsOuterJoins()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsPositionedDelete()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsPositionedUpdate()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsResultSetConcurrency(int type, int concurrency)
    throws SQLException
  {
    switch (type)
    {
    case 1004: 
      if ((concurrency == 1007) || (concurrency == 1008)) {
        return true;
      }
      throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
    case 1003: 
      if ((concurrency == 1007) || (concurrency == 1008)) {
        return true;
      }
      throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
    case 1005: 
      return false;
    }
    throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009", getExceptionInterceptor());
  }
  
  public boolean supportsResultSetHoldability(int holdability)
    throws SQLException
  {
    return holdability == 1;
  }
  
  public boolean supportsResultSetType(int type)
    throws SQLException
  {
    return type == 1004;
  }
  
  public boolean supportsSavepoints()
    throws SQLException
  {
    return (this.conn.versionMeetsMinimum(4, 0, 14)) || (this.conn.versionMeetsMinimum(4, 1, 1));
  }
  
  public boolean supportsSchemasInDataManipulation()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsSchemasInIndexDefinitions()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsSchemasInPrivilegeDefinitions()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsSchemasInProcedureCalls()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsSchemasInTableDefinitions()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsSelectForUpdate()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 0, 0);
  }
  
  public boolean supportsStatementPooling()
    throws SQLException
  {
    return false;
  }
  
  public boolean supportsStoredProcedures()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(5, 0, 0);
  }
  
  public boolean supportsSubqueriesInComparisons()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsSubqueriesInExists()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsSubqueriesInIns()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsSubqueriesInQuantifieds()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 1, 0);
  }
  
  public boolean supportsTableCorrelationNames()
    throws SQLException
  {
    return true;
  }
  
  public boolean supportsTransactionIsolationLevel(int level)
    throws SQLException
  {
    if (this.conn.supportsIsolationLevel())
    {
      switch (level)
      {
      case 1: 
      case 2: 
      case 4: 
      case 8: 
        return true;
      }
      return false;
    }
    return false;
  }
  
  public boolean supportsTransactions()
    throws SQLException
  {
    return this.conn.supportsTransactions();
  }
  
  public boolean supportsUnion()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 0, 0);
  }
  
  public boolean supportsUnionAll()
    throws SQLException
  {
    return this.conn.versionMeetsMinimum(4, 0, 0);
  }
  
  public boolean updatesAreDetected(int type)
    throws SQLException
  {
    return false;
  }
  
  public boolean usesLocalFilePerTable()
    throws SQLException
  {
    return false;
  }
  
  public boolean usesLocalFiles()
    throws SQLException
  {
    return false;
  }
  
  public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern)
    throws SQLException
  {
    Field[] fields = createFunctionColumnsFields();
    
    return getProcedureOrFunctionColumns(fields, catalog, schemaPattern, functionNamePattern, columnNamePattern, false, true);
  }
  
  protected Field[] createFunctionColumnsFields()
  {
    Field[] fields = { new Field("", "FUNCTION_CAT", 12, 512), new Field("", "FUNCTION_SCHEM", 12, 512), new Field("", "FUNCTION_NAME", 12, 512), new Field("", "COLUMN_NAME", 12, 512), new Field("", "COLUMN_TYPE", 12, 64), new Field("", "DATA_TYPE", 5, 6), new Field("", "TYPE_NAME", 12, 64), new Field("", "PRECISION", 4, 12), new Field("", "LENGTH", 4, 12), new Field("", "SCALE", 5, 12), new Field("", "RADIX", 5, 6), new Field("", "NULLABLE", 5, 6), new Field("", "REMARKS", 12, 512), new Field("", "CHAR_OCTET_LENGTH", 4, 32), new Field("", "ORDINAL_POSITION", 4, 32), new Field("", "IS_NULLABLE", 12, 12), new Field("", "SPECIFIC_NAME", 12, 64) };
    
    return fields;
  }
  
  public boolean providesQueryObjectGenerator()
    throws SQLException
  {
    return false;
  }
  
  public ResultSet getSchemas(String catalog, String schemaPattern)
    throws SQLException
  {
    Field[] fields = { new Field("", "TABLE_SCHEM", 12, 255), new Field("", "TABLE_CATALOG", 12, 255) };
    
    return buildResultSet(fields, new ArrayList());
  }
  
  public boolean supportsStoredFunctionsUsingCallSyntax()
    throws SQLException
  {
    return true;
  }
  
  protected PreparedStatement prepareMetaDataSafeStatement(String sql)
    throws SQLException
  {
    PreparedStatement pStmt = this.conn.clientPrepareStatement(sql);
    if (pStmt.getMaxRows() != 0) {
      pStmt.setMaxRows(0);
    }
    ((Statement)pStmt).setHoldResultsOpenOverClose(true);
    
    return pStmt;
  }
}
