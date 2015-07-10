package com.avaje.ebeaninternal.server.cluster.socket;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.cluster.ClusterBroadcast;
import com.avaje.ebeaninternal.server.cluster.ClusterManager;
import com.avaje.ebeaninternal.server.cluster.DataHolder;
import com.avaje.ebeaninternal.server.cluster.SerialiseTransactionHelper;
import com.avaje.ebeaninternal.server.lib.util.StringHelper;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class SocketClusterBroadcast
  implements ClusterBroadcast
{
  private static final Logger logger = Logger.getLogger(SocketClusterBroadcast.class.getName());
  private final SocketClient local;
  private final HashMap<String, SocketClient> clientMap;
  private final SocketClusterListener listener;
  private SocketClient[] members;
  private ClusterManager clusterManager;
  private final TxnSerialiseHelper txnSerialiseHelper = new TxnSerialiseHelper();
  private final AtomicInteger txnOutgoing = new AtomicInteger();
  private final AtomicInteger txnIncoming = new AtomicInteger();
  
  public SocketClusterBroadcast()
  {
    String localHostPort = GlobalProperties.get("ebean.cluster.local", null);
    String members = GlobalProperties.get("ebean.cluster.members", null);
    
    logger.info("Clustering using Sockets local[" + localHostPort + "] members[" + members + "]");
    
    this.local = new SocketClient(parseFullName(localHostPort));
    this.clientMap = new HashMap();
    
    String[] memArray = StringHelper.delimitedToArray(members, ",", false);
    for (int i = 0; i < memArray.length; i++)
    {
      InetSocketAddress member = parseFullName(memArray[i]);
      SocketClient client = new SocketClient(member);
      if (!this.local.getHostPort().equalsIgnoreCase(client.getHostPort())) {
        this.clientMap.put(client.getHostPort(), client);
      }
    }
    this.members = ((SocketClient[])this.clientMap.values().toArray(new SocketClient[this.clientMap.size()]));
    this.listener = new SocketClusterListener(this, this.local.getPort());
  }
  
  public SocketClusterStatus getStatus()
  {
    int currentGroupSize = 0;
    for (int i = 0; i < this.members.length; i++) {
      if (this.members[i].isOnline()) {
        currentGroupSize++;
      }
    }
    int txnIn = this.txnIncoming.get();
    int txnOut = this.txnOutgoing.get();
    
    return new SocketClusterStatus(currentGroupSize, txnIn, txnOut);
  }
  
  public void startup(ClusterManager clusterManager)
  {
    this.clusterManager = clusterManager;
    try
    {
      this.listener.startListening();
      register();
    }
    catch (IOException e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public void shutdown()
  {
    deregister();
    this.listener.shutdown();
  }
  
  private void register()
  {
    SocketClusterMessage h = SocketClusterMessage.register(this.local.getHostPort(), true);
    for (int i = 0; i < this.members.length; i++)
    {
      boolean online = this.members[i].register(h);
      
      String msg = "Cluster Member [" + this.members[i].getHostPort() + "] online[" + online + "]";
      logger.info(msg);
    }
  }
  
  protected void setMemberOnline(String fullName, boolean online)
    throws IOException
  {
    synchronized (this.clientMap)
    {
      String msg = "Cluster Member [" + fullName + "] online[" + online + "]";
      logger.info(msg);
      SocketClient member = (SocketClient)this.clientMap.get(fullName);
      member.setOnline(online);
    }
  }
  
  private void send(SocketClient client, SocketClusterMessage msg)
  {
    try
    {
      client.send(msg);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, "Error sending message", ex);
      try
      {
        client.reconnect();
      }
      catch (IOException e)
      {
        logger.log(Level.SEVERE, "Error trying to reconnect", ex);
      }
    }
  }
  
  public void broadcast(RemoteTransactionEvent remoteTransEvent)
  {
    try
    {
      this.txnOutgoing.incrementAndGet();
      DataHolder dataHolder = this.txnSerialiseHelper.createDataHolder(remoteTransEvent);
      SocketClusterMessage msg = SocketClusterMessage.transEvent(dataHolder);
      broadcast(msg);
    }
    catch (Exception e)
    {
      String msg = "Error sending RemoteTransactionEvent " + remoteTransEvent + " to cluster members.";
      logger.log(Level.SEVERE, msg, e);
    }
  }
  
  protected void broadcast(SocketClusterMessage msg)
  {
    for (int i = 0; i < this.members.length; i++) {
      send(this.members[i], msg);
    }
  }
  
  private void deregister()
  {
    SocketClusterMessage h = SocketClusterMessage.register(this.local.getHostPort(), false);
    broadcast(h);
    for (int i = 0; i < this.members.length; i++) {
      this.members[i].disconnect();
    }
  }
  
  protected boolean process(SocketConnection request)
    throws IOException, ClassNotFoundException
  {
    try
    {
      SocketClusterMessage h = (SocketClusterMessage)request.readObject();
      if (h.isRegisterEvent())
      {
        setMemberOnline(h.getRegisterHost(), h.isRegister());
      }
      else
      {
        this.txnIncoming.incrementAndGet();
        DataHolder dataHolder = h.getDataHolder();
        RemoteTransactionEvent transEvent = this.txnSerialiseHelper.read(dataHolder);
        transEvent.run();
      }
      if ((h.isRegisterEvent()) && (!h.isRegister())) {
        return true;
      }
      return false;
    }
    catch (InterruptedIOException e)
    {
      String msg = "Timeout waiting for message";
      logger.log(Level.INFO, msg, e);
      try
      {
        request.disconnect();
      }
      catch (IOException ex)
      {
        logger.log(Level.INFO, "Error disconnecting after timeout", ex);
      }
    }
    return true;
  }
  
  private InetSocketAddress parseFullName(String hostAndPort)
  {
    try
    {
      hostAndPort = hostAndPort.trim();
      int colonPos = hostAndPort.indexOf(":");
      if (colonPos == -1)
      {
        String msg = "No colon \":\" in " + hostAndPort;
        throw new IllegalArgumentException(msg);
      }
      String host = hostAndPort.substring(0, colonPos);
      String sPort = hostAndPort.substring(colonPos + 1, hostAndPort.length());
      int port = Integer.parseInt(sPort);
      
      return new InetSocketAddress(host, port);
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Error parsing [" + hostAndPort + "] for the form [host:port]", ex);
    }
  }
  
  class TxnSerialiseHelper
    extends SerialiseTransactionHelper
  {
    TxnSerialiseHelper() {}
    
    public SpiEbeanServer getEbeanServer(String serverName)
    {
      return (SpiEbeanServer)SocketClusterBroadcast.this.clusterManager.getServer(serverName);
    }
  }
}
