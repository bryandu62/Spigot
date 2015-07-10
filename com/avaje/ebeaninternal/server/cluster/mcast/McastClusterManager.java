package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.cluster.ClusterBroadcast;
import com.avaje.ebeaninternal.server.cluster.ClusterManager;
import com.avaje.ebeaninternal.server.cluster.Packet;
import com.avaje.ebeaninternal.server.cluster.PacketWriter;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class McastClusterManager
  implements ClusterBroadcast, Runnable
{
  private static final Logger logger = Logger.getLogger(McastClusterManager.class.getName());
  private ClusterManager clusterManager;
  private final Thread managerThread;
  private final McastPacketControl packageControl;
  private final McastListener listener;
  private final McastSender localSender;
  private final String localSenderHostPort;
  private final PacketWriter packetWriter;
  private final ArrayList<MessageResend> resendMessages = new ArrayList();
  private final ArrayList<MessageControl> controlMessages = new ArrayList();
  private final OutgoingPacketsCache outgoingPacketsCache = new OutgoingPacketsCache();
  private final IncomingPacketsLastAck incomingPacketsLastAck = new IncomingPacketsLastAck();
  private final int maxResendOutgoing;
  private long managerSleepMillis;
  private boolean sendWithNoMembers;
  private long minAcked;
  private long minAckedFromListener;
  private int currentGroupSize = -1;
  private long lastSendTime;
  private int lastSendTimeFreqMillis;
  private long lastStatusTime = System.currentTimeMillis();
  private int lastStatusTimeFreqMillis;
  private long totalTxnEventsSent;
  private long totalTxnEventsReceived;
  private long totalPacketsSent;
  private long totalBytesSent;
  private long totalPacketsResent;
  private long totalBytesResent;
  private long totalPacketsReceived;
  private long totalBytesReceived;
  
  public McastClusterManager()
  {
    this.managerSleepMillis = GlobalProperties.getInt("ebean.cluster.mcast.managerSleepMillis", 80);
    this.lastSendTimeFreqMillis = (1000 * GlobalProperties.getInt("ebean.cluster.mcast.pingFrequencySecs", 300));
    this.lastStatusTimeFreqMillis = (1000 * GlobalProperties.getInt("ebean.cluster.mcast.statusFrequencySecs", 600));
    
    this.maxResendOutgoing = GlobalProperties.getInt("ebean.cluster.mcast.maxResendOutgoing", 200);
    
    int maxResendIncoming = GlobalProperties.getInt("ebean.cluster.mcast.maxResendIncoming", 50);
    
    int port = GlobalProperties.getInt("ebean.cluster.mcast.listen.port", 0);
    String addr = GlobalProperties.get("ebean.cluster.mcast.listen.address", null);
    
    int sendPort = GlobalProperties.getInt("ebean.cluster.mcast.send.port", 0);
    String sendAddr = GlobalProperties.get("ebean.cluster.mcast.send.address", null);
    
    int maxSendPacketSize = GlobalProperties.getInt("ebean.cluster.mcast.send.maxPacketSize", 1500);
    
    this.sendWithNoMembers = GlobalProperties.getBoolean("ebean.cluster.mcast.send.sendWithNoMembers", true);
    
    boolean disableLoopback = GlobalProperties.getBoolean("ebean.cluster.mcast.listen.disableLoopback", false);
    int ttl = GlobalProperties.getInt("ebean.cluster.mcast.listen.ttl", -1);
    int timeout = GlobalProperties.getInt("ebean.cluster.mcast.listen.timeout", 1000);
    int bufferSize = GlobalProperties.getInt("ebean.cluster.mcast.listen.bufferSize", 65500);
    
    String mcastAddr = GlobalProperties.get("ebean.cluster.mcast.listen.mcastAddress", null);
    
    InetAddress mcastAddress = null;
    if (mcastAddr != null) {
      try
      {
        mcastAddress = InetAddress.getByName(mcastAddr);
      }
      catch (UnknownHostException e)
      {
        String msg = "Error getting Multicast InetAddress for " + mcastAddr;
        throw new RuntimeException(msg, e);
      }
    }
    if ((port == 0) || (addr == null))
    {
      String msg = "One of these Multicast settings has not been set. ebean.cluster.mcast.listen.port=" + port + ", ebean.cluster.mcast.listen.address=" + addr;
      
      throw new IllegalArgumentException(msg);
    }
    this.managerThread = new Thread(this, "EbeanClusterMcastManager");
    
    this.packetWriter = new PacketWriter(maxSendPacketSize);
    this.localSender = new McastSender(port, addr, sendPort, sendAddr);
    this.localSenderHostPort = this.localSender.getSenderHostPort();
    
    this.packageControl = new McastPacketControl(this, this.localSenderHostPort, maxResendIncoming);
    
    this.listener = new McastListener(this, this.packageControl, port, addr, bufferSize, timeout, this.localSenderHostPort, disableLoopback, ttl, mcastAddress);
  }
  
  protected void fromListenerTimeoutNoMembers()
  {
    synchronized (this.managerThread)
    {
      this.currentGroupSize = 0;
    }
  }
  
  protected void fromListener(long newMinAcked, MessageControl msgControl, MessageResend msgResend, int groupSize, long totalPacketsReceived, long totalBytesReceived, long totalTxnEventsReceived)
  {
    synchronized (this.managerThread)
    {
      if (newMinAcked > this.minAckedFromListener) {
        this.minAckedFromListener = newMinAcked;
      }
      if (msgControl != null) {
        this.controlMessages.add(msgControl);
      }
      if (msgResend != null) {
        this.resendMessages.add(msgResend);
      }
      this.currentGroupSize = groupSize;
      
      this.totalPacketsReceived = totalPacketsReceived;
      this.totalBytesReceived = totalBytesReceived;
      this.totalTxnEventsReceived = totalTxnEventsReceived;
    }
  }
  
  public McastStatus getStatus(boolean reset)
  {
    synchronized (this.managerThread)
    {
      long currentPacketId = this.packetWriter.currentPacketId();
      String lastAcks = this.incomingPacketsLastAck.toString();
      
      return new McastStatus(this.currentGroupSize, this.outgoingPacketsCache.size(), currentPacketId, this.minAcked, lastAcks, this.totalTxnEventsSent, this.totalTxnEventsReceived, this.totalPacketsSent, this.totalPacketsResent, this.totalPacketsReceived, this.totalBytesSent, this.totalBytesResent, this.totalBytesReceived);
    }
  }
  
  public void run()
  {
    try
    {
      String msg;
      for (;;)
      {
        Thread.sleep(this.managerSleepMillis);
        synchronized (this.managerThread)
        {
          handleControlMessages();
          
          handleResendMessages();
          if (this.currentGroupSize == 0)
          {
            int trimmedCount = this.outgoingPacketsCache.trimAll();
            if (trimmedCount > 0) {
              logger.fine("Cluster has no other members. Trimmed " + trimmedCount);
            }
          }
          else if (this.minAckedFromListener > this.minAcked)
          {
            this.outgoingPacketsCache.trimAcknowledgedMessages(this.minAckedFromListener);
            this.minAcked = this.minAckedFromListener;
          }
          AckResendMessages ackResendMessages = this.packageControl.getAckResendMessages(this.incomingPacketsLastAck);
          if (ackResendMessages.size() > 0) {
            if (sendMessages(false, ackResendMessages.getMessages())) {
              this.incomingPacketsLastAck.updateLastAck(ackResendMessages);
            }
          }
          if (this.lastSendTime < System.currentTimeMillis() - this.lastSendTimeFreqMillis) {
            sendPing();
          }
          if ((this.lastStatusTimeFreqMillis > 0) && 
            (this.lastStatusTime < System.currentTimeMillis() - this.lastStatusTimeFreqMillis))
          {
            McastStatus status = getStatus(false);
            logger.info("Cluster Status: " + status.getSummary());
            this.lastStatusTime = System.currentTimeMillis();
          }
        }
      }
    }
    catch (Exception e)
    {
      msg = "Error with Cluster Mcast Manager thread";
      logger.log(Level.SEVERE, msg, e);
    }
  }
  
  private void handleResendMessages()
  {
    if (this.resendMessages.size() > 0)
    {
      TreeSet<Long> s = new TreeSet();
      for (int i = 0; i < this.resendMessages.size(); i++)
      {
        MessageResend resendMsg = (MessageResend)this.resendMessages.get(i);
        s.addAll(resendMsg.getResendPacketIds());
      }
      this.totalPacketsResent += s.size();
      
      Iterator<Long> it = s.iterator();
      while (it.hasNext())
      {
        Long resendPacketId = (Long)it.next();
        Packet packet = this.outgoingPacketsCache.getPacket(resendPacketId);
        if (packet == null)
        {
          String msg = "Cluster unable to resend packet[" + resendPacketId + "] as it is no longer in the outgoingPacketsCache";
          logger.log(Level.SEVERE, msg);
        }
        else
        {
          int resendCount = packet.incrementResendCount();
          if (resendCount <= this.maxResendOutgoing)
          {
            resendPacket(packet);
          }
          else
          {
            String msg = "Cluster maxResendOutgoing [" + this.maxResendOutgoing + "] hit for packet " + resendPacketId + ". We will not try to send it anymore, removing it from the outgoingPacketsCache.";
            
            logger.log(Level.SEVERE, msg);
            this.outgoingPacketsCache.remove(packet);
          }
        }
      }
    }
  }
  
  private void resendPacket(Packet packet)
  {
    try
    {
      this.totalPacketsResent += 1L;
      this.totalBytesResent += this.localSender.sendPacket(packet);
    }
    catch (IOException e)
    {
      String msg = "Error trying to resend packet " + packet.getPacketId();
      logger.log(Level.SEVERE, msg, e);
    }
  }
  
  private void handleControlMessages()
  {
    boolean pingReponse = false;
    boolean joinReponse = false;
    for (int i = 0; i < this.controlMessages.size(); i++)
    {
      MessageControl message = (MessageControl)this.controlMessages.get(i);
      
      short type = message.getControlType();
      switch (type)
      {
      case 1: 
        logger.info("Cluster member Joined [" + message.getFromHostPort() + "]");
        joinReponse = true;
        break;
      case 7: 
        logger.info("Cluster member Online [" + message.getFromHostPort() + "]");
        
        break;
      case 3: 
        pingReponse = true;
        break;
      case 8: 
        break;
      case 2: 
        this.incomingPacketsLastAck.remove(message.getFromHostPort());
      }
    }
    this.controlMessages.clear();
    if (joinReponse) {
      sendJoinResponse();
    }
    if (pingReponse) {
      sendPingResponse();
    }
  }
  
  public void shutdown()
  {
    sendLeave();
    this.listener.shutdown();
  }
  
  public void startup(ClusterManager clusterManager)
  {
    this.clusterManager = clusterManager;
    this.listener.startListening();
    
    this.managerThread.setDaemon(true);
    this.managerThread.start();
    
    sendJoin();
  }
  
  protected SpiEbeanServer getEbeanServer(String serverName)
  {
    return (SpiEbeanServer)this.clusterManager.getServer(serverName);
  }
  
  private void sendJoin()
  {
    sendControlMessage(true, (short)1);
  }
  
  private void sendLeave()
  {
    sendControlMessage(false, (short)2);
  }
  
  private void sendJoinResponse()
  {
    sendControlMessage(true, (short)7);
  }
  
  private void sendPingResponse()
  {
    sendControlMessage(true, (short)8);
  }
  
  private void sendPing()
  {
    sendControlMessage(true, (short)3);
  }
  
  private void sendControlMessage(boolean requiresAck, short controlType)
  {
    sendMessage(requiresAck, new MessageControl(controlType, this.localSenderHostPort));
  }
  
  private void sendMessage(boolean requiresAck, Message msg)
  {
    ArrayList<Message> messages = new ArrayList(1);
    messages.add(msg);
    sendMessages(requiresAck, messages);
  }
  
  private boolean sendMessages(boolean requiresAck, List<? extends Message> messages)
  {
    synchronized (this.managerThread)
    {
      try
      {
        List<Packet> packets = this.packetWriter.write(requiresAck, messages);
        sendPackets(requiresAck, packets);
        return true;
      }
      catch (IOException e)
      {
        String msg = "Error sending Messages " + messages;
        logger.log(Level.SEVERE, msg, e);
        return false;
      }
    }
  }
  
  private boolean sendPackets(boolean requiresAck, List<Packet> packets)
    throws IOException
  {
    if ((this.currentGroupSize == 0) && (!this.sendWithNoMembers)) {
      return false;
    }
    if (requiresAck) {
      this.outgoingPacketsCache.registerPackets(packets);
    }
    this.totalPacketsSent += packets.size();
    this.totalBytesSent += this.localSender.sendPackets(packets);
    
    this.lastSendTime = System.currentTimeMillis();
    
    return true;
  }
  
  public void broadcast(RemoteTransactionEvent remoteTransEvent)
  {
    synchronized (this.managerThread)
    {
      try
      {
        List<Packet> packets = this.packetWriter.write(remoteTransEvent);
        if (sendPackets(true, packets)) {
          this.totalTxnEventsSent += 1L;
        }
      }
      catch (IOException e)
      {
        String msg = "Error sending RemoteTransactionEvent " + remoteTransEvent;
        logger.log(Level.SEVERE, msg, e);
      }
    }
  }
  
  public void setManagerSleepMillis(long managerSleepMillis)
  {
    synchronized (this.managerThread)
    {
      this.managerSleepMillis = managerSleepMillis;
    }
  }
  
  /* Error */
  public long getManagerSleepMillis()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 190	com/avaje/ebeaninternal/server/cluster/mcast/McastClusterManager:managerThread	Ljava/lang/Thread;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 98	com/avaje/ebeaninternal/server/cluster/mcast/McastClusterManager:managerSleepMillis	J
    //   11: aload_1
    //   12: monitorexit
    //   13: lreturn
    //   14: astore_2
    //   15: aload_1
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    // Line number table:
    //   Java source line #609	-> byte code offset #0
    //   Java source line #610	-> byte code offset #7
    //   Java source line #611	-> byte code offset #14
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	19	0	this	McastClusterManager
    //   5	11	1	Ljava/lang/Object;	Object
    //   14	4	2	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	13	14	finally
    //   14	17	14	finally
  }
}
