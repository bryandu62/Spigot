package com.avaje.ebeaninternal.server.lib.sql;

import com.avaje.ebeaninternal.jdbc.ConnectionDelegator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PooledConnection
  extends ConnectionDelegator
{
  private static final Logger logger = Logger.getLogger(PooledConnection.class.getName());
  private static String IDLE_CONNECTION_ACCESSED_ERROR = "Pooled Connection has been accessed whilst idle in the pool, via method: ";
  static final int STATUS_IDLE = 88;
  static final int STATUS_ACTIVE = 89;
  static final int STATUS_ENDED = 87;
  final String name;
  final DataSourcePool pool;
  final Connection connection;
  final long creationTime;
  final PstmtCache pstmtCache;
  final Object pstmtMonitor = new Object();
  int status = 88;
  boolean longRunning;
  boolean hadErrors;
  long startUseTime;
  long lastUseTime;
  String lastStatement;
  int pstmtHitCounter;
  int pstmtMissCounter;
  String createdByMethod;
  StackTraceElement[] stackTrace;
  int maxStackTrace;
  int slotId;
  
  public PooledConnection(DataSourcePool pool, int uniqueId, Connection connection)
    throws SQLException
  {
    super(connection);
    
    this.pool = pool;
    this.connection = connection;
    this.name = (pool.getName() + "." + uniqueId);
    this.pstmtCache = new PstmtCache(this.name, pool.getPstmtCacheSize());
    this.maxStackTrace = pool.getMaxStackTraceSize();
    this.creationTime = System.currentTimeMillis();
    this.lastUseTime = this.creationTime;
  }
  
  protected PooledConnection(String name)
  {
    super(null);
    this.name = name;
    this.pool = null;
    this.connection = null;
    this.pstmtCache = null;
    this.maxStackTrace = 0;
    this.creationTime = System.currentTimeMillis();
    this.lastUseTime = this.creationTime;
  }
  
  public int getSlotId()
  {
    return this.slotId;
  }
  
  public void setSlotId(int slotId)
  {
    this.slotId = slotId;
  }
  
  public DataSourcePool getDataSourcePool()
  {
    return this.pool;
  }
  
  public long getCreationTime()
  {
    return this.creationTime;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String toString()
  {
    return this.name;
  }
  
  public String getDescription()
  {
    return "name[" + this.name + "] startTime[" + getStartUseTime() + "] stmt[" + getLastStatement() + "] createdBy[" + getCreatedByMethod() + "]";
  }
  
  public String getStatistics()
  {
    return "name[" + this.name + "] startTime[" + getStartUseTime() + "] pstmtHits[" + this.pstmtHitCounter + "] pstmtMiss[" + this.pstmtMissCounter + "] " + this.pstmtCache.getDescription();
  }
  
  public boolean isLongRunning()
  {
    return this.longRunning;
  }
  
  public void setLongRunning(boolean longRunning)
  {
    this.longRunning = longRunning;
  }
  
  public void closeConnectionFully(boolean logErrors)
  {
    String msg = "Closing Connection[" + getName() + "]" + " psReuse[" + this.pstmtHitCounter + "] psCreate[" + this.pstmtMissCounter + "] psSize[" + this.pstmtCache.size() + "]";
    
    logger.info(msg);
    try
    {
      if (this.connection.isClosed())
      {
        msg = "Closing Connection[" + getName() + "] that is already closed?";
        logger.log(Level.SEVERE, msg);
        return;
      }
    }
    catch (SQLException ex)
    {
      if (logErrors)
      {
        msg = "Error when fully closing connection [" + getName() + "]";
        logger.log(Level.SEVERE, msg, ex);
      }
    }
    try
    {
      Iterator<ExtendedPreparedStatement> psi = this.pstmtCache.values().iterator();
      while (psi.hasNext())
      {
        ExtendedPreparedStatement ps = (ExtendedPreparedStatement)psi.next();
        ps.closeDestroy();
      }
    }
    catch (SQLException ex)
    {
      if (logErrors) {
        logger.log(Level.WARNING, "Error when closing connection Statements", ex);
      }
    }
    try
    {
      this.connection.close();
    }
    catch (SQLException ex)
    {
      if (logErrors)
      {
        msg = "Error when fully closing connection [" + getName() + "]";
        logger.log(Level.SEVERE, msg, ex);
      }
    }
  }
  
  public PstmtCache getPstmtCache()
  {
    return this.pstmtCache;
  }
  
  public Statement createStatement()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "createStatement()");
    }
    try
    {
      return this.connection.createStatement();
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public Statement createStatement(int resultSetType, int resultSetConcurreny)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "createStatement()");
    }
    try
    {
      return this.connection.createStatement(resultSetType, resultSetConcurreny);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  protected void returnPreparedStatement(ExtendedPreparedStatement pstmt)
  {
    synchronized (this.pstmtMonitor)
    {
      ExtendedPreparedStatement alreadyInCache = this.pstmtCache.get(pstmt.getCacheKey());
      if (alreadyInCache == null) {
        this.pstmtCache.put(pstmt.getCacheKey(), pstmt);
      } else {
        try
        {
          pstmt.closeDestroy();
        }
        catch (SQLException e)
        {
          logger.log(Level.SEVERE, "Error closing Pstmt", e);
        }
      }
    }
  }
  
  public PreparedStatement prepareStatement(String sql, int returnKeysFlag)
    throws SQLException
  {
    String cacheKey = sql + returnKeysFlag;
    return prepareStatement(sql, true, returnKeysFlag, cacheKey);
  }
  
  public PreparedStatement prepareStatement(String sql)
    throws SQLException
  {
    return prepareStatement(sql, false, 0, sql);
  }
  
  /* Error */
  private PreparedStatement prepareStatement(String sql, boolean useFlag, int flag, String cacheKey)
    throws SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 58	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:status	I
    //   4: bipush 88
    //   6: if_icmpne +37 -> 43
    //   9: new 66	java/lang/StringBuilder
    //   12: dup
    //   13: invokespecial 67	java/lang/StringBuilder:<init>	()V
    //   16: getstatic 256	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:IDLE_CONNECTION_ACCESSED_ERROR	Ljava/lang/String;
    //   19: invokevirtual 77	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: ldc_w 300
    //   25: invokevirtual 77	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   31: astore 5
    //   33: new 46	java/sql/SQLException
    //   36: dup
    //   37: aload 5
    //   39: invokespecial 260	java/sql/SQLException:<init>	(Ljava/lang/String;)V
    //   42: athrow
    //   43: aload_0
    //   44: getfield 56	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:pstmtMonitor	Ljava/lang/Object;
    //   47: dup
    //   48: astore 5
    //   50: monitorenter
    //   51: aload_0
    //   52: aload_1
    //   53: putfield 302	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:lastStatement	Ljava/lang/String;
    //   56: aload_0
    //   57: getfield 98	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:pstmtCache	Lcom/avaje/ebeaninternal/server/lib/sql/PstmtCache;
    //   60: aload 4
    //   62: invokevirtual 305	com/avaje/ebeaninternal/server/lib/sql/PstmtCache:remove	(Ljava/lang/Object;)Lcom/avaje/ebeaninternal/server/lib/sql/ExtendedPreparedStatement;
    //   65: astore 6
    //   67: aload 6
    //   69: ifnull +19 -> 88
    //   72: aload_0
    //   73: dup
    //   74: getfield 153	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:pstmtHitCounter	I
    //   77: iconst_1
    //   78: iadd
    //   79: putfield 153	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:pstmtHitCounter	I
    //   82: aload 6
    //   84: aload 5
    //   86: monitorexit
    //   87: areturn
    //   88: aload_0
    //   89: dup
    //   90: getfield 157	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:pstmtMissCounter	I
    //   93: iconst_1
    //   94: iadd
    //   95: putfield 157	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:pstmtMissCounter	I
    //   98: iload_2
    //   99: ifeq +19 -> 118
    //   102: aload_0
    //   103: getfield 64	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:connection	Ljava/sql/Connection;
    //   106: aload_1
    //   107: iload_3
    //   108: invokeinterface 307 3 0
    //   113: astore 7
    //   115: goto +15 -> 130
    //   118: aload_0
    //   119: getfield 64	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:connection	Ljava/sql/Connection;
    //   122: aload_1
    //   123: invokeinterface 309 2 0
    //   128: astore 7
    //   130: new 230	com/avaje/ebeaninternal/server/lib/sql/ExtendedPreparedStatement
    //   133: dup
    //   134: aload_0
    //   135: aload 7
    //   137: aload_1
    //   138: aload 4
    //   140: invokespecial 312	com/avaje/ebeaninternal/server/lib/sql/ExtendedPreparedStatement:<init>	(Lcom/avaje/ebeaninternal/server/lib/sql/PooledConnection;Ljava/sql/PreparedStatement;Ljava/lang/String;Ljava/lang/String;)V
    //   143: aload 5
    //   145: monitorexit
    //   146: areturn
    //   147: astore 8
    //   149: aload 5
    //   151: monitorexit
    //   152: aload 8
    //   154: athrow
    //   155: astore 5
    //   157: aload_0
    //   158: aload 5
    //   160: invokevirtual 266	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:addError	(Ljava/lang/Throwable;)V
    //   163: aload 5
    //   165: athrow
    // Line number table:
    //   Java source line #404	-> byte code offset #0
    //   Java source line #405	-> byte code offset #9
    //   Java source line #406	-> byte code offset #33
    //   Java source line #409	-> byte code offset #43
    //   Java source line #410	-> byte code offset #51
    //   Java source line #413	-> byte code offset #56
    //   Java source line #415	-> byte code offset #67
    //   Java source line #416	-> byte code offset #72
    //   Java source line #417	-> byte code offset #82
    //   Java source line #421	-> byte code offset #88
    //   Java source line #423	-> byte code offset #98
    //   Java source line #424	-> byte code offset #102
    //   Java source line #426	-> byte code offset #118
    //   Java source line #428	-> byte code offset #130
    //   Java source line #429	-> byte code offset #147
    //   Java source line #431	-> byte code offset #155
    //   Java source line #432	-> byte code offset #157
    //   Java source line #433	-> byte code offset #163
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	166	0	this	PooledConnection
    //   0	166	1	sql	String
    //   0	166	2	useFlag	boolean
    //   0	166	3	flag	int
    //   0	166	4	cacheKey	String
    //   31	7	5	m	String
    //   155	9	5	ex	SQLException
    //   65	18	6	pstmt	ExtendedPreparedStatement
    //   113	3	7	actualPstmt	PreparedStatement
    //   128	8	7	actualPstmt	PreparedStatement
    //   147	6	8	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   51	87	147	finally
    //   88	146	147	finally
    //   147	152	147	finally
    //   43	87	155	java/sql/SQLException
    //   88	146	155	java/sql/SQLException
    //   147	155	155	java/sql/SQLException
  }
  
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurreny)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "prepareStatement()");
    }
    try
    {
      this.pstmtMissCounter += 1;
      this.lastStatement = sql;
      return this.connection.prepareStatement(sql, resultSetType, resultSetConcurreny);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  protected void resetForUse()
  {
    this.status = 89;
    this.startUseTime = System.currentTimeMillis();
    this.createdByMethod = null;
    this.lastStatement = null;
    this.hadErrors = false;
    this.longRunning = false;
  }
  
  public void addError(Throwable e)
  {
    this.hadErrors = true;
  }
  
  public boolean hadErrors()
  {
    return this.hadErrors;
  }
  
  public void close()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "close()");
    }
    if ((this.hadErrors) && 
      (!this.pool.validateConnection(this)))
    {
      closeConnectionFully(false);
      this.pool.checkDataSource();
      return;
    }
    try
    {
      if (this.connection.getAutoCommit() != this.pool.getAutoCommit()) {
        this.connection.setAutoCommit(this.pool.getAutoCommit());
      }
      if (this.resetIsolationReadOnlyRequired)
      {
        resetIsolationReadOnly();
        this.resetIsolationReadOnlyRequired = false;
      }
      this.lastUseTime = System.currentTimeMillis();
      
      this.status = 88;
      this.pool.returnConnection(this);
    }
    catch (Exception ex)
    {
      closeConnectionFully(false);
      this.pool.checkDataSource();
    }
  }
  
  private void resetIsolationReadOnly()
    throws SQLException
  {
    if (this.connection.getTransactionIsolation() != this.pool.getTransactionIsolation()) {
      this.connection.setTransactionIsolation(this.pool.getTransactionIsolation());
    }
    if (this.connection.isReadOnly()) {
      this.connection.setReadOnly(false);
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if ((this.connection != null) && (!this.connection.isClosed()))
      {
        String msg = "Closing Connection[" + getName() + "] on finalize().";
        logger.warning(msg);
        closeConnectionFully(false);
      }
    }
    catch (Exception e)
    {
      logger.log(Level.SEVERE, null, e);
    }
    super.finalize();
  }
  
  public long getStartUseTime()
  {
    return this.startUseTime;
  }
  
  public long getLastUsedTime()
  {
    return this.lastUseTime;
  }
  
  public String getLastStatement()
  {
    return this.lastStatement;
  }
  
  protected void setLastStatement(String lastStatement)
  {
    this.lastStatement = lastStatement;
    if (logger.isLoggable(Level.FINER)) {
      logger.finer(".setLastStatement[" + lastStatement + "]");
    }
  }
  
  boolean resetIsolationReadOnlyRequired = false;
  
  public void setReadOnly(boolean readOnly)
    throws SQLException
  {
    this.resetIsolationReadOnlyRequired = true;
    this.connection.setReadOnly(readOnly);
  }
  
  public void setTransactionIsolation(int level)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "setTransactionIsolation()");
    }
    try
    {
      this.resetIsolationReadOnlyRequired = true;
      this.connection.setTransactionIsolation(level);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public void clearWarnings()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "clearWarnings()");
    }
    this.connection.clearWarnings();
  }
  
  public void commit()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "commit()");
    }
    try
    {
      this.status = 87;
      this.connection.commit();
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public boolean getAutoCommit()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "getAutoCommit()");
    }
    return this.connection.getAutoCommit();
  }
  
  public String getCatalog()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "getCatalog()");
    }
    return this.connection.getCatalog();
  }
  
  public DatabaseMetaData getMetaData()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "getMetaData()");
    }
    return this.connection.getMetaData();
  }
  
  public int getTransactionIsolation()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "getTransactionIsolation()");
    }
    return this.connection.getTransactionIsolation();
  }
  
  public Map<String, Class<?>> getTypeMap()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "getTypeMap()");
    }
    return this.connection.getTypeMap();
  }
  
  public SQLWarning getWarnings()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "getWarnings()");
    }
    return this.connection.getWarnings();
  }
  
  public boolean isClosed()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "isClosed()");
    }
    return this.connection.isClosed();
  }
  
  public boolean isReadOnly()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "isReadOnly()");
    }
    return this.connection.isReadOnly();
  }
  
  public String nativeSQL(String sql)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "nativeSQL()");
    }
    this.lastStatement = sql;
    return this.connection.nativeSQL(sql);
  }
  
  public CallableStatement prepareCall(String sql)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "prepareCall()");
    }
    this.lastStatement = sql;
    return this.connection.prepareCall(sql);
  }
  
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurreny)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "prepareCall()");
    }
    this.lastStatement = sql;
    return this.connection.prepareCall(sql, resultSetType, resultSetConcurreny);
  }
  
  public void rollback()
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "rollback()");
    }
    try
    {
      this.status = 87;
      this.connection.rollback();
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public void setAutoCommit(boolean autoCommit)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "setAutoCommit()");
    }
    try
    {
      this.connection.setAutoCommit(autoCommit);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public void setCatalog(String catalog)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "setCatalog()");
    }
    this.connection.setCatalog(catalog);
  }
  
  public void setTypeMap(Map<String, Class<?>> map)
    throws SQLException
  {
    if (this.status == 88) {
      throw new SQLException(IDLE_CONNECTION_ACCESSED_ERROR + "setTypeMap()");
    }
    this.connection.setTypeMap(map);
  }
  
  public Savepoint setSavepoint()
    throws SQLException
  {
    try
    {
      return this.connection.setSavepoint();
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public Savepoint setSavepoint(String savepointName)
    throws SQLException
  {
    try
    {
      return this.connection.setSavepoint(savepointName);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public void rollback(Savepoint sp)
    throws SQLException
  {
    try
    {
      this.connection.rollback(sp);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public void releaseSavepoint(Savepoint sp)
    throws SQLException
  {
    try
    {
      this.connection.releaseSavepoint(sp);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public void setHoldability(int i)
    throws SQLException
  {
    try
    {
      this.connection.setHoldability(i);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public int getHoldability()
    throws SQLException
  {
    try
    {
      return this.connection.getHoldability();
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public Statement createStatement(int i, int x, int y)
    throws SQLException
  {
    try
    {
      return this.connection.createStatement(i, x, y);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public PreparedStatement prepareStatement(String s, int i, int x, int y)
    throws SQLException
  {
    try
    {
      return this.connection.prepareStatement(s, i, x, y);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public PreparedStatement prepareStatement(String s, int[] i)
    throws SQLException
  {
    try
    {
      return this.connection.prepareStatement(s, i);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public PreparedStatement prepareStatement(String s, String[] s2)
    throws SQLException
  {
    try
    {
      return this.connection.prepareStatement(s, s2);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public CallableStatement prepareCall(String s, int i, int x, int y)
    throws SQLException
  {
    try
    {
      return this.connection.prepareCall(s, i, x, y);
    }
    catch (SQLException ex)
    {
      addError(ex);
      throw ex;
    }
  }
  
  public String getCreatedByMethod()
  {
    if (this.createdByMethod != null) {
      return this.createdByMethod;
    }
    if (this.stackTrace == null) {
      return null;
    }
    for (int j = 0; j < this.stackTrace.length; j++)
    {
      String methodLine = this.stackTrace[j].toString();
      if (!skipElement(methodLine))
      {
        this.createdByMethod = methodLine;
        return this.createdByMethod;
      }
    }
    return null;
  }
  
  private boolean skipElement(String methodLine)
  {
    if (methodLine.startsWith("java.lang.")) {
      return true;
    }
    if (methodLine.startsWith("java.util.")) {
      return true;
    }
    if (methodLine.startsWith("com.avaje.ebeaninternal.server.query.CallableQuery.<init>")) {
      return true;
    }
    if (methodLine.startsWith("com.avaje.ebeaninternal.server.query.Callable")) {
      return false;
    }
    if (methodLine.startsWith("com.avaje.ebeaninternal")) {
      return true;
    }
    return false;
  }
  
  protected void setStackTrace(StackTraceElement[] stackTrace)
  {
    this.stackTrace = stackTrace;
  }
  
  public StackTraceElement[] getStackTrace()
  {
    if (this.stackTrace == null) {
      return null;
    }
    ArrayList<StackTraceElement> filteredList = new ArrayList();
    boolean include = false;
    for (int i = 0; i < this.stackTrace.length; i++)
    {
      if ((!include) && (!skipElement(this.stackTrace[i].toString()))) {
        include = true;
      }
      if ((include) && (filteredList.size() < this.maxStackTrace)) {
        filteredList.add(this.stackTrace[i]);
      }
    }
    return (StackTraceElement[])filteredList.toArray(new StackTraceElement[filteredList.size()]);
  }
}
