package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class FailoverConnectionProxy
  extends LoadBalancingConnectionProxy
{
  boolean failedOver;
  boolean hasTriedMaster;
  private long masterFailTimeMillis;
  boolean preferSlaveDuringFailover;
  private String primaryHostPortSpec;
  private long queriesBeforeRetryMaster;
  long queriesIssuedFailedOver;
  private int secondsBeforeRetryMaster;
  
  class FailoverInvocationHandler
    extends LoadBalancingConnectionProxy.ConnectionErrorFiringInvocationHandler
  {
    public FailoverInvocationHandler(Object toInvokeOn)
    {
      super(toInvokeOn);
    }
    
    public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable
    {
      String methodName = method.getName();
      if ((FailoverConnectionProxy.this.failedOver) && (methodName.indexOf("execute") != -1)) {
        FailoverConnectionProxy.this.queriesIssuedFailedOver += 1L;
      }
      return super.invoke(proxy, method, args);
    }
  }
  
  FailoverConnectionProxy(List<String> hosts, Properties props)
    throws SQLException
  {
    super(hosts, props);
    ConnectionPropertiesImpl connectionProps = new ConnectionPropertiesImpl();
    connectionProps.initializeProperties(props);
    
    this.queriesBeforeRetryMaster = connectionProps.getQueriesBeforeRetryMaster();
    this.secondsBeforeRetryMaster = connectionProps.getSecondsBeforeRetryMaster();
    this.preferSlaveDuringFailover = false;
  }
  
  protected LoadBalancingConnectionProxy.ConnectionErrorFiringInvocationHandler createConnectionProxy(Object toProxy)
  {
    return new FailoverInvocationHandler(toProxy);
  }
  
  void dealWithInvocationException(InvocationTargetException e)
    throws SQLException, Throwable, InvocationTargetException
  {
    Throwable t = e.getTargetException();
    if (t != null)
    {
      if (this.failedOver)
      {
        createPrimaryConnection();
        if (this.currentConn != null) {
          throw t;
        }
      }
      failOver();
      
      throw t;
    }
    throw e;
  }
  
  public Object invoke(Object proxy, Method method, Object[] args)
    throws Throwable
  {
    String methodName = method.getName();
    if ("setPreferSlaveDuringFailover".equals(methodName))
    {
      this.preferSlaveDuringFailover = ((Boolean)args[0]).booleanValue();
    }
    else if ("clearHasTriedMaster".equals(methodName))
    {
      this.hasTriedMaster = false;
    }
    else
    {
      if ("hasTriedMaster".equals(methodName)) {
        return Boolean.valueOf(this.hasTriedMaster);
      }
      if ("isMasterConnection".equals(methodName)) {
        return Boolean.valueOf(!this.failedOver);
      }
      if ("isSlaveConnection".equals(methodName)) {
        return Boolean.valueOf(this.failedOver);
      }
      if ("setReadOnly".equals(methodName))
      {
        if (this.failedOver) {
          return null;
        }
      }
      else if (("setAutoCommit".equals(methodName)) && (this.failedOver) && (shouldFallBack()) && (Boolean.TRUE.equals(args[0])) && (this.failedOver))
      {
        createPrimaryConnection();
        
        return super.invoke(proxy, method, args, this.failedOver);
      }
    }
    return super.invoke(proxy, method, args, this.failedOver);
  }
  
  private void createPrimaryConnection()
    throws SQLException
  {
    try
    {
      this.currentConn = createConnectionForHost(this.primaryHostPortSpec);
      this.failedOver = false;
      this.hasTriedMaster = true;
      
      this.queriesIssuedFailedOver = 0L;
    }
    catch (SQLException sqlEx)
    {
      this.failedOver = true;
      if (this.currentConn != null) {
        this.currentConn.getLog().logWarn("Connection to primary host failed", sqlEx);
      }
    }
  }
  
  synchronized void invalidateCurrentConnection()
    throws SQLException
  {
    if (!this.failedOver)
    {
      this.failedOver = true;
      this.queriesIssuedFailedOver = 0L;
      this.masterFailTimeMillis = System.currentTimeMillis();
    }
    super.invalidateCurrentConnection();
  }
  
  protected synchronized void pickNewConnection()
    throws SQLException
  {
    if (this.primaryHostPortSpec == null) {
      this.primaryHostPortSpec = ((String)this.hostList.remove(0));
    }
    if ((this.currentConn == null) || ((this.failedOver) && (shouldFallBack())))
    {
      createPrimaryConnection();
      if (this.currentConn != null) {
        return;
      }
    }
    failOver();
  }
  
  private void failOver()
    throws SQLException
  {
    if (this.failedOver)
    {
      Iterator<Map.Entry<String, ConnectionImpl>> iter = this.liveConnections.entrySet().iterator();
      while (iter.hasNext())
      {
        Map.Entry<String, ConnectionImpl> entry = (Map.Entry)iter.next();
        ((ConnectionImpl)entry.getValue()).close();
      }
      this.liveConnections.clear();
    }
    super.pickNewConnection();
    if (this.currentConn.getFailOverReadOnly()) {
      this.currentConn.setReadOnly(true);
    } else {
      this.currentConn.setReadOnly(false);
    }
    this.failedOver = true;
  }
  
  private boolean shouldFallBack()
  {
    long secondsSinceFailedOver = (System.currentTimeMillis() - this.masterFailTimeMillis) / 1000L;
    if (secondsSinceFailedOver >= this.secondsBeforeRetryMaster)
    {
      this.masterFailTimeMillis = System.currentTimeMillis();
      
      return true;
    }
    if ((this.queriesBeforeRetryMaster != 0L) && (this.queriesIssuedFailedOver >= this.queriesBeforeRetryMaster)) {
      return true;
    }
    return false;
  }
}
