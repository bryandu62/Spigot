package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class LoadBalancingConnectionProxy
  implements InvocationHandler, PingTarget
{
  private static Method getLocalTimeMethod;
  private long totalPhysicalConnections = 0L;
  private long activePhysicalConnections = 0L;
  private String hostToRemove = null;
  private long lastUsed = 0L;
  private long transactionCount = 0L;
  private ConnectionGroup connectionGroup = null;
  private String closedReason = null;
  public static final String BLACKLIST_TIMEOUT_PROPERTY_KEY = "loadBalanceBlacklistTimeout";
  protected MySQLConnection currentConn;
  protected List<String> hostList;
  protected Map<String, ConnectionImpl> liveConnections;
  private Map<ConnectionImpl, String> connectionsToHostsMap;
  private long[] responseTimes;
  private Map<String, Integer> hostsToListIndexMap;
  
  protected class ConnectionErrorFiringInvocationHandler
    implements InvocationHandler
  {
    Object invokeOn = null;
    
    public ConnectionErrorFiringInvocationHandler(Object toInvokeOn)
    {
      this.invokeOn = toInvokeOn;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable
    {
      Object result = null;
      try
      {
        result = method.invoke(this.invokeOn, args);
        if (result != null) {
          result = LoadBalancingConnectionProxy.this.proxyIfInterfaceIsJdbc(result, result.getClass());
        }
      }
      catch (InvocationTargetException e)
      {
        LoadBalancingConnectionProxy.this.dealWithInvocationException(e);
      }
      return result;
    }
  }
  
  private boolean inTransaction = false;
  private long transactionStartTime = 0L;
  private Properties localProps;
  private boolean isClosed = false;
  private BalanceStrategy balancer;
  private int retriesAllDown;
  private static Map<String, Long> globalBlacklist;
  private int globalBlacklistTimeout = 0;
  private long connectionGroupProxyID = 0L;
  private LoadBalanceExceptionChecker exceptionChecker;
  private Map<Class, Boolean> jdbcInterfacesForProxyCache = new HashMap();
  private MySQLConnection thisAsConnection = null;
  private int autoCommitSwapThreshold = 0;
  private static Constructor JDBC_4_LB_CONNECTION_CTOR;
  
  static
  {
    try
    {
      getLocalTimeMethod = System.class.getMethod("nanoTime", new Class[0]);
    }
    catch (SecurityException e) {}catch (NoSuchMethodException e) {}
    globalBlacklist = new HashMap();
    if (Util.isJdbc4()) {
      try
      {
        JDBC_4_LB_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4LoadBalancedMySQLConnection").getConstructor(new Class[] { LoadBalancingConnectionProxy.class });
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
  }
  
  LoadBalancingConnectionProxy(List<String> hosts, Properties props)
    throws SQLException
  {
    String group = props.getProperty("loadBalanceConnectionGroup", null);
    
    boolean enableJMX = false;
    String enableJMXAsString = props.getProperty("loadBalanceEnableJMX", "false");
    try
    {
      enableJMX = Boolean.parseBoolean(enableJMXAsString);
    }
    catch (Exception e)
    {
      throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceEnableJMX", new Object[] { enableJMXAsString }), "S1009", null);
    }
    if (group != null)
    {
      this.connectionGroup = ConnectionGroupManager.getConnectionGroupInstance(group);
      if (enableJMX) {
        ConnectionGroupManager.registerJmx();
      }
      this.connectionGroupProxyID = this.connectionGroup.registerConnectionProxy(this, hosts);
      hosts = new ArrayList(this.connectionGroup.getInitialHosts());
    }
    this.hostList = hosts;
    
    int numHosts = this.hostList.size();
    
    this.liveConnections = new HashMap(numHosts);
    this.connectionsToHostsMap = new HashMap(numHosts);
    this.responseTimes = new long[numHosts];
    this.hostsToListIndexMap = new HashMap(numHosts);
    
    this.localProps = ((Properties)props.clone());
    this.localProps.remove("HOST");
    this.localProps.remove("PORT");
    for (int i = 0; i < numHosts; i++)
    {
      this.hostsToListIndexMap.put(this.hostList.get(i), new Integer(i));
      this.localProps.remove("HOST." + (i + 1));
      
      this.localProps.remove("PORT." + (i + 1));
    }
    this.localProps.remove("NUM_HOSTS");
    this.localProps.setProperty("useLocalSessionState", "true");
    
    String strategy = this.localProps.getProperty("loadBalanceStrategy", "random");
    
    String lbExceptionChecker = this.localProps.getProperty("loadBalanceExceptionChecker", "com.mysql.jdbc.StandardLoadBalanceExceptionChecker");
    
    String retriesAllDownAsString = this.localProps.getProperty("retriesAllDown", "120");
    try
    {
      this.retriesAllDown = Integer.parseInt(retriesAllDownAsString);
    }
    catch (NumberFormatException nfe)
    {
      throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForRetriesAllDown", new Object[] { retriesAllDownAsString }), "S1009", null);
    }
    String blacklistTimeoutAsString = this.localProps.getProperty("loadBalanceBlacklistTimeout", "0");
    try
    {
      this.globalBlacklistTimeout = Integer.parseInt(blacklistTimeoutAsString);
    }
    catch (NumberFormatException nfe)
    {
      throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceBlacklistTimeout", new Object[] { retriesAllDownAsString }), "S1009", null);
    }
    if ("random".equals(strategy)) {
      this.balancer = ((BalanceStrategy)Util.loadExtensions(null, props, "com.mysql.jdbc.RandomBalanceStrategy", "InvalidLoadBalanceStrategy", null).get(0));
    } else if ("bestResponseTime".equals(strategy)) {
      this.balancer = ((BalanceStrategy)Util.loadExtensions(null, props, "com.mysql.jdbc.BestResponseTimeBalanceStrategy", "InvalidLoadBalanceStrategy", null).get(0));
    } else {
      this.balancer = ((BalanceStrategy)Util.loadExtensions(null, props, strategy, "InvalidLoadBalanceStrategy", null).get(0));
    }
    String autoCommitSwapThresholdAsString = props.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
    try
    {
      this.autoCommitSwapThreshold = Integer.parseInt(autoCommitSwapThresholdAsString);
    }
    catch (NumberFormatException nfe)
    {
      throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceAutoCommitStatementThreshold", new Object[] { autoCommitSwapThresholdAsString }), "S1009", null);
    }
    String autoCommitSwapRegex = props.getProperty("loadBalanceAutoCommitStatementRegex", "");
    if (!"".equals(autoCommitSwapRegex)) {
      try
      {
        "".matches(autoCommitSwapRegex);
      }
      catch (Exception e)
      {
        throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceAutoCommitStatementRegex", new Object[] { autoCommitSwapRegex }), "S1009", null);
      }
    }
    if (this.autoCommitSwapThreshold > 0)
    {
      String statementInterceptors = this.localProps.getProperty("statementInterceptors");
      if (statementInterceptors == null) {
        this.localProps.setProperty("statementInterceptors", "com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
      } else if (statementInterceptors.length() > 0) {
        this.localProps.setProperty("statementInterceptors", statementInterceptors + ",com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
      }
      props.setProperty("statementInterceptors", this.localProps.getProperty("statementInterceptors"));
    }
    this.balancer.init(null, props);
    
    this.exceptionChecker = ((LoadBalanceExceptionChecker)Util.loadExtensions(null, props, lbExceptionChecker, "InvalidLoadBalanceExceptionChecker", null).get(0));
    
    this.exceptionChecker.init(null, props);
    if ((Util.isJdbc4()) || (JDBC_4_LB_CONNECTION_CTOR != null)) {
      this.thisAsConnection = ((MySQLConnection)Util.handleNewInstance(JDBC_4_LB_CONNECTION_CTOR, new Object[] { this }, null));
    } else {
      this.thisAsConnection = new LoadBalancedMySQLConnection(this);
    }
    pickNewConnection();
  }
  
  public synchronized ConnectionImpl createConnectionForHost(String hostPortSpec)
    throws SQLException
  {
    Properties connProps = (Properties)this.localProps.clone();
    
    String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(hostPortSpec);
    
    String hostName = hostPortPair[0];
    String portNumber = hostPortPair[1];
    String dbName = connProps.getProperty("DBNAME");
    if (hostName == null) {
      throw new SQLException("Could not find a hostname to start a connection to");
    }
    if (portNumber == null) {
      portNumber = "3306";
    }
    connProps.setProperty("HOST", hostName);
    connProps.setProperty("PORT", portNumber);
    
    connProps.setProperty("HOST.1", hostName);
    
    connProps.setProperty("PORT.1", portNumber);
    
    connProps.setProperty("NUM_HOSTS", "1");
    connProps.setProperty("roundRobinLoadBalance", "false");
    
    ConnectionImpl conn = (ConnectionImpl)ConnectionImpl.getInstance(hostName, Integer.parseInt(portNumber), connProps, dbName, "jdbc:mysql://" + hostName + ":" + portNumber + "/");
    
    this.liveConnections.put(hostPortSpec, conn);
    this.connectionsToHostsMap.put(conn, hostPortSpec);
    
    this.activePhysicalConnections += 1L;
    this.totalPhysicalConnections += 1L;
    
    conn.setProxy(this.thisAsConnection);
    
    return conn;
  }
  
  void dealWithInvocationException(InvocationTargetException e)
    throws SQLException, Throwable, InvocationTargetException
  {
    Throwable t = e.getTargetException();
    if (t != null)
    {
      if (((t instanceof SQLException)) && (shouldExceptionTriggerFailover((SQLException)t)))
      {
        invalidateCurrentConnection();
        pickNewConnection();
      }
      throw t;
    }
    throw e;
  }
  
  synchronized void invalidateCurrentConnection()
    throws SQLException
  {
    try
    {
      if (!this.currentConn.isClosed()) {
        this.currentConn.close();
      }
    }
    finally
    {
      if (isGlobalBlacklistEnabled()) {
        addToGlobalBlacklist((String)this.connectionsToHostsMap.get(this.currentConn));
      }
      this.liveConnections.remove(this.connectionsToHostsMap.get(this.currentConn));
      
      Object mappedHost = this.connectionsToHostsMap.remove(this.currentConn);
      if ((mappedHost != null) && (this.hostsToListIndexMap.containsKey(mappedHost)))
      {
        int hostIndex = ((Integer)this.hostsToListIndexMap.get(mappedHost)).intValue();
        synchronized (this.responseTimes)
        {
          this.responseTimes[hostIndex] = 0L;
        }
      }
    }
  }
  
  private void closeAllConnections()
  {
    synchronized (this)
    {
      Iterator<ConnectionImpl> allConnections = this.liveConnections.values().iterator();
      while (allConnections.hasNext()) {
        try
        {
          this.activePhysicalConnections -= 1L;
          ((ConnectionImpl)allConnections.next()).close();
        }
        catch (SQLException e) {}
      }
      if (!this.isClosed)
      {
        this.balancer.destroy();
        if (this.connectionGroup != null) {
          this.connectionGroup.closeConnectionProxy(this);
        }
      }
      this.liveConnections.clear();
      this.connectionsToHostsMap.clear();
    }
  }
  
  public Object invoke(Object proxy, Method method, Object[] args)
    throws Throwable
  {
    return invoke(proxy, method, args, true);
  }
  
  public Object invoke(Object proxy, Method method, Object[] args, boolean swapAtTransactionBoundary)
    throws Throwable
  {
    String methodName = method.getName();
    if ("getLoadBalanceSafeProxy".equals(methodName)) {
      return this.currentConn;
    }
    if (("equals".equals(methodName)) && (args.length == 1))
    {
      if ((args[0] instanceof Proxy)) {
        return Boolean.valueOf(((Proxy)args[0]).equals(this));
      }
      return Boolean.valueOf(equals(args[0]));
    }
    if ("hashCode".equals(methodName)) {
      return new Integer(hashCode());
    }
    if ("close".equals(methodName))
    {
      closeAllConnections();
      this.isClosed = true;
      this.closedReason = "Connection explicitly closed.";
      return null;
    }
    if ("isClosed".equals(methodName)) {
      return Boolean.valueOf(this.isClosed);
    }
    if (this.isClosed)
    {
      String reason = "No operations allowed after connection closed.";
      if (this.closedReason != null) {
        reason = reason + "  " + this.closedReason;
      }
      throw SQLError.createSQLException(reason, "08003", null);
    }
    if (!this.inTransaction)
    {
      this.inTransaction = true;
      this.transactionStartTime = getLocalTimeBestResolution();
      this.transactionCount += 1L;
    }
    Object result = null;
    try
    {
      this.lastUsed = System.currentTimeMillis();
      result = method.invoke(this.thisAsConnection, args);
      if (result != null)
      {
        if ((result instanceof Statement)) {
          ((Statement)result).setPingTarget(this);
        }
        result = proxyIfInterfaceIsJdbc(result, result.getClass());
      }
    }
    catch (InvocationTargetException e)
    {
      dealWithInvocationException(e);
    }
    finally
    {
      if ((swapAtTransactionBoundary) && (("commit".equals(methodName)) || ("rollback".equals(methodName))))
      {
        this.inTransaction = false;
        
        String host = (String)this.connectionsToHostsMap.get(this.currentConn);
        if (host != null) {
          synchronized (this.responseTimes)
          {
            int hostIndex = ((Integer)this.hostsToListIndexMap.get(host)).intValue();
            if (hostIndex < this.responseTimes.length) {
              this.responseTimes[hostIndex] = (getLocalTimeBestResolution() - this.transactionStartTime);
            }
          }
        }
        pickNewConnection();
      }
    }
    return result;
  }
  
  protected synchronized void pickNewConnection()
    throws SQLException
  {
    if (this.currentConn == null)
    {
      this.currentConn = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[])this.responseTimes.clone(), this.retriesAllDown);
      
      return;
    }
    if (this.currentConn.isClosed()) {
      invalidateCurrentConnection();
    }
    int pingTimeout = this.currentConn.getLoadBalancePingTimeout();
    boolean pingBeforeReturn = this.currentConn.getLoadBalanceValidateConnectionOnSwapServer();
    
    int hostsTried = 0;
    for (int hostsToTry = this.hostList.size(); hostsTried <= hostsToTry; hostsTried++) {
      try
      {
        ConnectionImpl newConn = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[])this.responseTimes.clone(), this.retriesAllDown);
        if (this.currentConn != null)
        {
          if (pingBeforeReturn) {
            if (pingTimeout == 0) {
              newConn.ping();
            } else {
              newConn.pingInternal(true, pingTimeout);
            }
          }
          syncSessionState(this.currentConn, newConn);
        }
        this.currentConn = newConn;
        return;
      }
      catch (SQLException e)
      {
        if (shouldExceptionTriggerFailover(e)) {
          invalidateCurrentConnection();
        }
      }
    }
    this.isClosed = true;
    this.closedReason = "Connection closed after inability to pick valid new connection during fail-over.";
  }
  
  Object proxyIfInterfaceIsJdbc(Object toProxy, Class clazz)
  {
    if (isInterfaceJdbc(clazz))
    {
      Class[] interfacesToProxy = getAllInterfacesToProxy(clazz);
      
      return Proxy.newProxyInstance(toProxy.getClass().getClassLoader(), interfacesToProxy, createConnectionProxy(toProxy));
    }
    return toProxy;
  }
  
  private Map<Class, Class[]> allInterfacesToProxy = new HashMap();
  
  private Class[] getAllInterfacesToProxy(Class clazz)
  {
    Class[] interfacesToProxy = (Class[])this.allInterfacesToProxy.get(clazz);
    if (interfacesToProxy != null) {
      return interfacesToProxy;
    }
    List<Class> interfaces = new LinkedList();
    
    Class superClass = clazz;
    while (!superClass.equals(Object.class))
    {
      Class[] declared = superClass.getInterfaces();
      for (int i = 0; i < declared.length; i++) {
        interfaces.add(declared[i]);
      }
      superClass = superClass.getSuperclass();
    }
    interfacesToProxy = new Class[interfaces.size()];
    interfaces.toArray(interfacesToProxy);
    
    this.allInterfacesToProxy.put(clazz, interfacesToProxy);
    
    return interfacesToProxy;
  }
  
  private boolean isInterfaceJdbc(Class clazz)
  {
    if (this.jdbcInterfacesForProxyCache.containsKey(clazz)) {
      return ((Boolean)this.jdbcInterfacesForProxyCache.get(clazz)).booleanValue();
    }
    Class[] interfaces = clazz.getInterfaces();
    for (int i = 0; i < interfaces.length; i++)
    {
      String packageName = interfaces[i].getPackage().getName();
      if (("java.sql".equals(packageName)) || ("javax.sql".equals(packageName)) || ("com.mysql.jdbc".equals(packageName)))
      {
        this.jdbcInterfacesForProxyCache.put(clazz, new Boolean(true));
        return true;
      }
      if (isInterfaceJdbc(interfaces[i]))
      {
        this.jdbcInterfacesForProxyCache.put(clazz, new Boolean(true));
        return true;
      }
    }
    this.jdbcInterfacesForProxyCache.put(clazz, new Boolean(false));
    return false;
  }
  
  protected ConnectionErrorFiringInvocationHandler createConnectionProxy(Object toProxy)
  {
    return new ConnectionErrorFiringInvocationHandler(toProxy);
  }
  
  private static long getLocalTimeBestResolution()
  {
    if (getLocalTimeMethod != null) {
      try
      {
        return ((Long)getLocalTimeMethod.invoke(null, (Object[])null)).longValue();
      }
      catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
    }
    return System.currentTimeMillis();
  }
  
  public synchronized void doPing()
    throws SQLException
  {
    SQLException se = null;
    boolean foundHost = false;
    int pingTimeout = this.currentConn.getLoadBalancePingTimeout();
    Iterator<String> i;
    synchronized (this)
    {
      for (i = this.hostList.iterator(); i.hasNext();)
      {
        String host = (String)i.next();
        ConnectionImpl conn = (ConnectionImpl)this.liveConnections.get(host);
        if (conn != null) {
          try
          {
            if (pingTimeout == 0) {
              conn.ping();
            } else {
              conn.pingInternal(true, pingTimeout);
            }
            foundHost = true;
          }
          catch (SQLException e)
          {
            this.activePhysicalConnections -= 1L;
            if (host.equals(this.connectionsToHostsMap.get(this.currentConn)))
            {
              closeAllConnections();
              this.isClosed = true;
              this.closedReason = "Connection closed because ping of current connection failed.";
              throw e;
            }
            if (e.getMessage().equals(Messages.getString("Connection.exceededConnectionLifetime")))
            {
              if (se == null) {
                se = e;
              }
            }
            else
            {
              se = e;
              if (isGlobalBlacklistEnabled()) {
                addToGlobalBlacklist(host);
              }
            }
            this.liveConnections.remove(this.connectionsToHostsMap.get(conn));
          }
        }
      }
    }
    if (!foundHost)
    {
      closeAllConnections();
      this.isClosed = true;
      this.closedReason = "Connection closed due to inability to ping any active connections.";
      if (se != null) {
        throw se;
      }
      ((ConnectionImpl)this.currentConn).throwConnectionClosedException();
    }
  }
  
  public void addToGlobalBlacklist(String host, long timeout)
  {
    if (isGlobalBlacklistEnabled()) {
      synchronized (globalBlacklist)
      {
        globalBlacklist.put(host, new Long(timeout));
      }
    }
  }
  
  public void addToGlobalBlacklist(String host)
  {
    addToGlobalBlacklist(host, System.currentTimeMillis() + this.globalBlacklistTimeout);
  }
  
  public boolean isGlobalBlacklistEnabled()
  {
    return this.globalBlacklistTimeout > 0;
  }
  
  public Map<String, Long> getGlobalBlacklist()
  {
    if (!isGlobalBlacklistEnabled())
    {
      String localHostToRemove = this.hostToRemove;
      if (this.hostToRemove != null)
      {
        HashMap<String, Long> fakedBlacklist = new HashMap();
        fakedBlacklist.put(localHostToRemove, new Long(System.currentTimeMillis() + 5000L));
        return fakedBlacklist;
      }
      return new HashMap(1);
    }
    Map<String, Long> blacklistClone = new HashMap(globalBlacklist.size());
    synchronized (globalBlacklist)
    {
      blacklistClone.putAll(globalBlacklist);
    }
    Set<String> keys = blacklistClone.keySet();
    
    keys.retainAll(this.hostList);
    if (keys.size() == this.hostList.size()) {
      return new HashMap(1);
    }
    for (Object i = keys.iterator(); ((Iterator)i).hasNext();)
    {
      String host = (String)((Iterator)i).next();
      
      Long timeout = (Long)globalBlacklist.get(host);
      if ((timeout != null) && (timeout.longValue() < System.currentTimeMillis()))
      {
        synchronized (globalBlacklist)
        {
          globalBlacklist.remove(host);
        }
        ((Iterator)i).remove();
      }
    }
    return blacklistClone;
  }
  
  public boolean shouldExceptionTriggerFailover(SQLException ex)
  {
    return this.exceptionChecker.shouldExceptionTriggerFailover(ex);
  }
  
  public void removeHostWhenNotInUse(String host)
    throws SQLException
  {
    int timeBetweenChecks = 1000;
    long timeBeforeHardFail = 15000L;
    addToGlobalBlacklist(host, timeBeforeHardFail + 1000L);
    long cur = System.currentTimeMillis();
    while (System.currentTimeMillis() - timeBeforeHardFail < cur)
    {
      synchronized (this)
      {
        this.hostToRemove = host;
        if (!host.equals(this.currentConn.getHost()))
        {
          removeHost(host);
          return;
        }
      }
      try
      {
        Thread.sleep(timeBetweenChecks);
      }
      catch (InterruptedException e) {}
    }
    removeHost(host);
  }
  
  public void removeHost(String host)
    throws SQLException
  {
    synchronized (this)
    {
      if ((this.connectionGroup != null) && 
        (this.connectionGroup.getInitialHosts().size() == 1) && (this.connectionGroup.getInitialHosts().contains(host))) {
        throw SQLError.createSQLException("Cannot remove only configured host.", null);
      }
      this.hostToRemove = host;
      if (host.equals(this.currentConn.getHost()))
      {
        closeAllConnections();
      }
      else
      {
        this.connectionsToHostsMap.remove(this.liveConnections.remove(host));
        Integer idx = (Integer)this.hostsToListIndexMap.remove(host);
        long[] newResponseTimes = new long[this.responseTimes.length - 1];
        int newIdx = 0;
        for (Iterator<String> i = this.hostList.iterator(); i.hasNext(); newIdx++)
        {
          String copyHost = (String)i.next();
          if ((idx != null) && (idx.intValue() < this.responseTimes.length))
          {
            newResponseTimes[newIdx] = this.responseTimes[idx.intValue()];
            this.hostsToListIndexMap.put(copyHost, new Integer(newIdx));
          }
        }
        this.responseTimes = newResponseTimes;
      }
    }
  }
  
  public synchronized boolean addHost(String host)
  {
    synchronized (this)
    {
      if (this.hostsToListIndexMap.containsKey(host)) {
        return false;
      }
      long[] newResponseTimes = new long[this.responseTimes.length + 1];
      for (int i = 0; i < this.responseTimes.length; i++) {
        newResponseTimes[i] = this.responseTimes[i];
      }
      this.responseTimes = newResponseTimes;
      this.hostList.add(host);
      this.hostsToListIndexMap.put(host, new Integer(this.responseTimes.length - 1));
    }
    return true;
  }
  
  public long getLastUsed()
  {
    return this.lastUsed;
  }
  
  public boolean inTransaction()
  {
    return this.inTransaction;
  }
  
  public long getTransactionCount()
  {
    return this.transactionCount;
  }
  
  public long getActivePhysicalConnectionCount()
  {
    return this.activePhysicalConnections;
  }
  
  public long getTotalPhysicalConnectionCount()
  {
    return this.totalPhysicalConnections;
  }
  
  public long getConnectionGroupProxyID()
  {
    return this.connectionGroupProxyID;
  }
  
  public String getCurrentActiveHost()
  {
    MySQLConnection c = this.currentConn;
    if (c != null)
    {
      Object o = this.connectionsToHostsMap.get(c);
      if (o != null) {
        return o.toString();
      }
    }
    return null;
  }
  
  public long getCurrentTransactionDuration()
  {
    long st = 0L;
    if ((this.inTransaction) && ((st = this.transactionStartTime) > 0L)) {
      return getLocalTimeBestResolution() - this.transactionStartTime;
    }
    return 0L;
  }
  
  protected void syncSessionState(Connection initial, Connection target)
    throws SQLException
  {
    if ((initial == null) || (target == null)) {
      return;
    }
    target.setAutoCommit(initial.getAutoCommit());
    target.setCatalog(initial.getCatalog());
    target.setTransactionIsolation(initial.getTransactionIsolation());
    target.setReadOnly(initial.isReadOnly());
  }
}
