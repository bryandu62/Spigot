package com.avaje.ebeaninternal.server.lib.sql;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PooledConnectionQueue
{
  private static final Logger logger = Logger.getLogger(PooledConnectionQueue.class.getName());
  private static final TimeUnit MILLIS_TIME_UNIT = TimeUnit.MILLISECONDS;
  private final String name;
  private final DataSourcePool pool;
  private final FreeConnectionBuffer freeList;
  private final BusyConnectionBuffer busyList;
  private final ReentrantLock lock;
  private final Condition notEmpty;
  private int connectionId;
  private long waitTimeoutMillis;
  private long leakTimeMinutes;
  private int warningSize;
  private int maxSize;
  private int minSize;
  private int waitingThreads;
  private int waitCount;
  private int hitCount;
  private int highWaterMark;
  private long lastResetTime;
  private boolean doingShutdown;
  
  public PooledConnectionQueue(DataSourcePool pool)
  {
    this.pool = pool;
    this.name = pool.getName();
    this.minSize = pool.getMinSize();
    this.maxSize = pool.getMaxSize();
    
    this.warningSize = pool.getWarningSize();
    this.waitTimeoutMillis = pool.getWaitTimeoutMillis();
    this.leakTimeMinutes = pool.getLeakTimeMinutes();
    
    this.busyList = new BusyConnectionBuffer(50, 20);
    this.freeList = new FreeConnectionBuffer(this.maxSize);
    
    this.lock = new ReentrantLock(true);
    this.notEmpty = this.lock.newCondition();
  }
  
  private DataSourcePool.Status createStatus()
  {
    return new DataSourcePool.Status(this.name, this.minSize, this.maxSize, this.freeList.size(), this.busyList.size(), this.waitingThreads, this.highWaterMark, this.waitCount, this.hitCount);
  }
  
  public String toString()
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      return createStatus().toString();
    }
    finally
    {
      lock.unlock();
    }
  }
  
  public DataSourcePool.Status getStatus(boolean reset)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      DataSourcePool.Status s = createStatus();
      if (reset)
      {
        this.highWaterMark = this.busyList.size();
        this.hitCount = 0;
        this.waitCount = 0;
      }
      return s;
    }
    finally
    {
      lock.unlock();
    }
  }
  
  public void setMinSize(int minSize)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      if (minSize > this.maxSize) {
        throw new IllegalArgumentException("minSize " + minSize + " > maxSize " + this.maxSize);
      }
      this.minSize = minSize;
    }
    finally
    {
      lock.unlock();
    }
  }
  
  public void setMaxSize(int maxSize)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      if (maxSize < this.minSize) {
        throw new IllegalArgumentException("maxSize " + maxSize + " < minSize " + this.minSize);
      }
      this.freeList.setCapacity(maxSize);
      this.maxSize = maxSize;
    }
    finally
    {
      lock.unlock();
    }
  }
  
  public void setWarningSize(int warningSize)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      if (warningSize > this.maxSize) {
        throw new IllegalArgumentException("warningSize " + warningSize + " > maxSize " + this.maxSize);
      }
      this.warningSize = warningSize;
    }
    finally
    {
      lock.unlock();
    }
  }
  
  private int totalConnections()
  {
    return this.freeList.size() + this.busyList.size();
  }
  
  public void ensureMinimumConnections()
    throws SQLException
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      int add = this.minSize - totalConnections();
      if (add > 0)
      {
        for (int i = 0; i < add; i++)
        {
          PooledConnection c = this.pool.createConnectionForQueue(this.connectionId++);
          this.freeList.add(c);
        }
        this.notEmpty.signal();
      }
    }
    finally
    {
      lock.unlock();
    }
  }
  
  protected void returnPooledConnection(PooledConnection c)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      if (!this.busyList.remove(c)) {
        logger.log(Level.SEVERE, "Connection [" + c + "] not found in BusyList? ");
      }
      if (c.getCreationTime() <= this.lastResetTime)
      {
        c.closeConnectionFully(false);
      }
      else
      {
        this.freeList.add(c);
        this.notEmpty.signal();
      }
    }
    finally
    {
      lock.unlock();
    }
  }
  
  private PooledConnection extractFromFreeList()
  {
    PooledConnection c = this.freeList.remove();
    registerBusyConnection(c);
    return c;
  }
  
  public PooledConnection getPooledConnection()
    throws SQLException
  {
    try
    {
      PooledConnection pc = _getPooledConnection();
      pc.resetForUse();
      return pc;
    }
    catch (InterruptedException e)
    {
      String msg = "Interrupted getting connection from pool " + e;
      throw new SQLException(msg);
    }
  }
  
  private int registerBusyConnection(PooledConnection c)
  {
    int busySize = this.busyList.add(c);
    if (busySize > this.highWaterMark) {
      this.highWaterMark = busySize;
    }
    return busySize;
  }
  
  /* Error */
  private PooledConnection _getPooledConnection()
    throws InterruptedException, SQLException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 102	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:lock	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: astore_1
    //   5: aload_1
    //   6: invokevirtual 264	java/util/concurrent/locks/ReentrantLock:lockInterruptibly	()V
    //   9: aload_0
    //   10: getfield 266	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:doingShutdown	Z
    //   13: ifeq +14 -> 27
    //   16: new 178	java/sql/SQLException
    //   19: dup
    //   20: ldc_w 268
    //   23: invokespecial 254	java/sql/SQLException:<init>	(Ljava/lang/String;)V
    //   26: athrow
    //   27: aload_0
    //   28: dup
    //   29: getfield 124	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:hitCount	I
    //   32: iconst_1
    //   33: iadd
    //   34: putfield 124	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:hitCount	I
    //   37: aload_0
    //   38: getfield 118	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitingThreads	I
    //   41: ifne +156 -> 197
    //   44: aload_0
    //   45: getfield 95	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:freeList	Lcom/avaje/ebeaninternal/server/lib/sql/FreeConnectionBuffer;
    //   48: invokevirtual 115	com/avaje/ebeaninternal/server/lib/sql/FreeConnectionBuffer:size	()I
    //   51: istore_2
    //   52: iload_2
    //   53: ifle +14 -> 67
    //   56: aload_0
    //   57: invokespecial 270	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:extractFromFreeList	()Lcom/avaje/ebeaninternal/server/lib/sql/PooledConnection;
    //   60: astore_3
    //   61: aload_1
    //   62: invokevirtual 137	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   65: aload_3
    //   66: areturn
    //   67: aload_0
    //   68: getfield 88	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:busyList	Lcom/avaje/ebeaninternal/server/lib/sql/BusyConnectionBuffer;
    //   71: invokevirtual 116	com/avaje/ebeaninternal/server/lib/sql/BusyConnectionBuffer:size	()I
    //   74: aload_0
    //   75: getfield 65	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:maxSize	I
    //   78: if_icmpge +119 -> 197
    //   81: aload_0
    //   82: getfield 48	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:pool	Lcom/avaje/ebeaninternal/server/lib/sql/DataSourcePool;
    //   85: aload_0
    //   86: dup
    //   87: getfield 182	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:connectionId	I
    //   90: dup_x1
    //   91: iconst_1
    //   92: iadd
    //   93: putfield 182	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:connectionId	I
    //   96: invokevirtual 186	com/avaje/ebeaninternal/server/lib/sql/DataSourcePool:createConnectionForQueue	(I)Lcom/avaje/ebeaninternal/server/lib/sql/PooledConnection;
    //   99: astore_3
    //   100: aload_0
    //   101: aload_3
    //   102: invokespecial 242	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:registerBusyConnection	(Lcom/avaje/ebeaninternal/server/lib/sql/PooledConnection;)I
    //   105: istore 4
    //   107: new 147	java/lang/StringBuilder
    //   110: dup
    //   111: invokespecial 148	java/lang/StringBuilder:<init>	()V
    //   114: ldc_w 272
    //   117: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: aload_0
    //   121: getfield 54	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:name	Ljava/lang/String;
    //   124: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   127: ldc_w 274
    //   130: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: aload_3
    //   134: invokevirtual 275	com/avaje/ebeaninternal/server/lib/sql/PooledConnection:getName	()Ljava/lang/String;
    //   137: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: ldc_w 277
    //   143: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: iload 4
    //   148: invokevirtual 157	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   151: ldc_w 279
    //   154: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: aload_0
    //   158: getfield 65	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:maxSize	I
    //   161: invokevirtual 157	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   164: ldc_w 281
    //   167: invokevirtual 154	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: invokevirtual 160	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   173: astore 5
    //   175: getstatic 205	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:logger	Ljava/util/logging/Logger;
    //   178: aload 5
    //   180: invokevirtual 284	java/util/logging/Logger:info	(Ljava/lang/String;)V
    //   183: aload_0
    //   184: invokespecial 287	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:checkForWarningSize	()V
    //   187: aload_3
    //   188: astore 6
    //   190: aload_1
    //   191: invokevirtual 137	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   194: aload 6
    //   196: areturn
    //   197: aload_0
    //   198: dup
    //   199: getfield 122	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitCount	I
    //   202: iconst_1
    //   203: iadd
    //   204: putfield 122	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitCount	I
    //   207: aload_0
    //   208: dup
    //   209: getfield 118	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitingThreads	I
    //   212: iconst_1
    //   213: iadd
    //   214: putfield 118	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitingThreads	I
    //   217: aload_0
    //   218: invokespecial 290	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:_getPooledConnectionWaitLoop	()Lcom/avaje/ebeaninternal/server/lib/sql/PooledConnection;
    //   221: astore_2
    //   222: aload_0
    //   223: dup
    //   224: getfield 118	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitingThreads	I
    //   227: iconst_1
    //   228: isub
    //   229: putfield 118	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitingThreads	I
    //   232: aload_1
    //   233: invokevirtual 137	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   236: aload_2
    //   237: areturn
    //   238: astore 7
    //   240: aload_0
    //   241: dup
    //   242: getfield 118	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitingThreads	I
    //   245: iconst_1
    //   246: isub
    //   247: putfield 118	com/avaje/ebeaninternal/server/lib/sql/PooledConnectionQueue:waitingThreads	I
    //   250: aload 7
    //   252: athrow
    //   253: astore 8
    //   255: aload_1
    //   256: invokevirtual 137	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   259: aload 8
    //   261: athrow
    // Line number table:
    //   Java source line #269	-> byte code offset #0
    //   Java source line #270	-> byte code offset #5
    //   Java source line #272	-> byte code offset #9
    //   Java source line #273	-> byte code offset #16
    //   Java source line #278	-> byte code offset #27
    //   Java source line #281	-> byte code offset #37
    //   Java source line #283	-> byte code offset #44
    //   Java source line #284	-> byte code offset #52
    //   Java source line #286	-> byte code offset #56
    //   Java source line #313	-> byte code offset #61
    //   Java source line #289	-> byte code offset #67
    //   Java source line #291	-> byte code offset #81
    //   Java source line #292	-> byte code offset #100
    //   Java source line #294	-> byte code offset #107
    //   Java source line #295	-> byte code offset #175
    //   Java source line #297	-> byte code offset #183
    //   Java source line #298	-> byte code offset #187
    //   Java source line #313	-> byte code offset #190
    //   Java source line #305	-> byte code offset #197
    //   Java source line #306	-> byte code offset #207
    //   Java source line #307	-> byte code offset #217
    //   Java source line #309	-> byte code offset #222
    //   Java source line #313	-> byte code offset #232
    //   Java source line #309	-> byte code offset #238
    //   Java source line #313	-> byte code offset #253
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	262	0	this	PooledConnectionQueue
    //   4	252	1	lock	ReentrantLock
    //   51	186	2	freeSize	int
    //   60	6	3	localPooledConnection1	PooledConnection
    //   99	89	3	c	PooledConnection
    //   105	42	4	busySize	int
    //   173	6	5	msg	String
    //   188	7	6	localPooledConnection2	PooledConnection
    //   238	13	7	localObject1	Object
    //   253	7	8	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   197	222	238	finally
    //   238	240	238	finally
    //   9	61	253	finally
    //   67	190	253	finally
    //   197	232	253	finally
    //   238	255	253	finally
  }
  
  private PooledConnection _getPooledConnectionWaitLoop()
    throws SQLException, InterruptedException
  {
    long nanos = MILLIS_TIME_UNIT.toNanos(this.waitTimeoutMillis);
    for (;;)
    {
      if (nanos <= 0L)
      {
        String msg = "Unsuccessfully waited [" + this.waitTimeoutMillis + "] millis for a connection to be returned." + " No connections are free. You need to Increase the max connections of [" + this.maxSize + "]" + " or look for a connection pool leak using datasource.xxx.capturestacktrace=true";
        if (this.pool.isCaptureStackTrace()) {
          dumpBusyConnectionInformation();
        }
        throw new SQLException(msg);
      }
      try
      {
        nanos = this.notEmpty.awaitNanos(nanos);
        if (!this.freeList.isEmpty()) {
          return extractFromFreeList();
        }
      }
      catch (InterruptedException ie)
      {
        this.notEmpty.signal();
        throw ie;
      }
    }
  }
  
  public void shutdown()
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      this.doingShutdown = true;
      DataSourcePool.Status status = createStatus();
      logger.info("DataSourcePool [" + this.name + "] shutdown: " + status);
      
      closeFreeConnections(true);
      if (!this.busyList.isEmpty())
      {
        String msg = "A potential connection leak was detected.  Busy connections: " + this.busyList.size();
        logger.warning(msg);
        
        dumpBusyConnectionInformation();
        closeBusyConnections(0L);
      }
    }
    finally
    {
      lock.unlock();
    }
  }
  
  public void reset(long leakTimeMinutes)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      DataSourcePool.Status status = createStatus();
      logger.info("Reseting DataSourcePool [" + this.name + "] " + status);
      this.lastResetTime = System.currentTimeMillis();
      
      closeFreeConnections(false);
      closeBusyConnections(leakTimeMinutes);
      
      String busyMsg = "Busy Connections:\r\n" + getBusyConnectionInformation();
      logger.info(busyMsg);
    }
    finally
    {
      lock.unlock();
    }
  }
  
  public void trim(int maxInactiveTimeSecs)
    throws SQLException
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      trimInactiveConnections(maxInactiveTimeSecs);
      ensureMinimumConnections();
    }
    finally
    {
      lock.unlock();
    }
  }
  
  private int trimInactiveConnections(int maxInactiveTimeSecs)
  {
    int maxTrim = this.freeList.size() - this.minSize;
    if (maxTrim <= 0) {
      return 0;
    }
    int trimedCount = 0;
    long usedSince = System.currentTimeMillis() - maxInactiveTimeSecs * 1000;
    
    List<PooledConnection> freeListCopy = this.freeList.getShallowCopy();
    
    Iterator<PooledConnection> it = freeListCopy.iterator();
    while (it.hasNext())
    {
      PooledConnection pc = (PooledConnection)it.next();
      if (pc.getLastUsedTime() < usedSince)
      {
        trimedCount++;
        it.remove();
        pc.closeConnectionFully(true);
        if (trimedCount >= maxTrim) {
          break;
        }
      }
    }
    if (trimedCount > 0)
    {
      this.freeList.setShallowCopy(freeListCopy);
      
      String msg = "DataSourcePool [" + this.name + "] trimmed [" + trimedCount + "] inactive connections. New size[" + totalConnections() + "]";
      logger.info(msg);
    }
    return trimedCount;
  }
  
  public void closeFreeConnections(boolean logErrors)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      while (!this.freeList.isEmpty())
      {
        PooledConnection c = this.freeList.remove();
        logger.info("PSTMT Statistics: " + c.getStatistics());
        c.closeConnectionFully(logErrors);
      }
    }
    finally
    {
      lock.unlock();
    }
  }
  
  public void closeBusyConnections(long leakTimeMinutes)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      long olderThanTime = System.currentTimeMillis() - leakTimeMinutes * 60000L;
      
      List<PooledConnection> copy = this.busyList.getShallowCopy();
      for (int i = 0; i < copy.size(); i++)
      {
        PooledConnection pc = (PooledConnection)copy.get(i);
        if ((!pc.isLongRunning()) && (pc.getLastUsedTime() <= olderThanTime))
        {
          this.busyList.remove(pc);
          closeBusyConnection(pc);
        }
      }
    }
    finally
    {
      lock.unlock();
    }
  }
  
  private void closeBusyConnection(PooledConnection pc)
  {
    try
    {
      String methodLine = pc.getCreatedByMethod();
      
      Date luDate = new Date();
      luDate.setTime(pc.getLastUsedTime());
      
      String msg = "DataSourcePool closing leaked connection?  name[" + pc.getName() + "] lastUsed[" + luDate + "] createdBy[" + methodLine + "] lastStmt[" + pc.getLastStatement() + "]";
      
      logger.warning(msg);
      logStackElement(pc, "Possible Leaked Connection: ");
      
      System.out.println("CLOSING BUSY CONNECTION ??? " + pc);
      pc.close();
    }
    catch (SQLException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }
  
  private void logStackElement(PooledConnection pc, String prefix)
  {
    StackTraceElement[] stackTrace = pc.getStackTrace();
    if (stackTrace != null)
    {
      String s = Arrays.toString(stackTrace);
      String msg = prefix + " name[" + pc.getName() + "] stackTrace: " + s;
      logger.warning(msg);
      
      System.err.println(msg);
    }
  }
  
  private void checkForWarningSize()
  {
    int availableGrowth = this.maxSize - totalConnections();
    if (availableGrowth < this.warningSize)
    {
      closeBusyConnections(this.leakTimeMinutes);
      
      String msg = "DataSourcePool [" + this.name + "] is [" + availableGrowth + "] connections from its maximum size.";
      this.pool.notifyWarning(msg);
    }
  }
  
  public String getBusyConnectionInformation()
  {
    return getBusyConnectionInformation(false);
  }
  
  public void dumpBusyConnectionInformation()
  {
    getBusyConnectionInformation(true);
  }
  
  private String getBusyConnectionInformation(boolean toLogger)
  {
    ReentrantLock lock = this.lock;
    lock.lock();
    try
    {
      if (toLogger) {
        logger.info("Dumping busy connections: (Use datasource.xxx.capturestacktrace=true  ... to get stackTraces)");
      }
      StringBuilder sb = new StringBuilder();
      
      List<PooledConnection> copy = this.busyList.getShallowCopy();
      for (int i = 0; i < copy.size(); i++)
      {
        PooledConnection pc = (PooledConnection)copy.get(i);
        if (toLogger)
        {
          logger.info(pc.getDescription());
          logStackElement(pc, "Busy Connection: ");
        }
        else
        {
          sb.append(pc.getDescription()).append("\r\n");
        }
      }
      return sb.toString();
    }
    finally
    {
      lock.unlock();
    }
  }
}
