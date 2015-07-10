package com.avaje.ebeaninternal.server.cluster.mcast;

import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.cluster.Packet;
import com.avaje.ebeaninternal.server.cluster.PacketTransactionEvent;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class McastListener
  implements Runnable
{
  private static final Logger logger = Logger.getLogger(McastListener.class.getName());
  private final McastClusterManager owner;
  private final McastPacketControl packetControl;
  private final MulticastSocket sock;
  private final Thread listenerThread;
  private final String localSenderHostPort;
  private final InetAddress group;
  private final boolean debugIgnore;
  private DatagramPacket pack;
  private byte[] receiveBuffer;
  private volatile boolean shutdown;
  private volatile boolean shutdownComplete;
  private long totalPacketsReceived;
  private long totalBytesReceived;
  private long totalTxnEventsReceived;
  
  public McastListener(McastClusterManager owner, McastPacketControl packetControl, int port, String address, int bufferSize, int timeout, String localSenderHostPort, boolean disableLoopback, int ttl, InetAddress mcastBindAddress)
  {
    this.debugIgnore = GlobalProperties.getBoolean("ebean.debug.mcast.ignore", false);
    
    this.owner = owner;
    this.packetControl = packetControl;
    this.localSenderHostPort = localSenderHostPort;
    this.receiveBuffer = new byte[bufferSize];
    this.listenerThread = new Thread(this, "EbeanClusterMcastListener");
    
    String msg = "Cluster Multicast Listening address[" + address + "] port[" + port + "] disableLoopback[" + disableLoopback + "]";
    if (ttl >= 0) {
      msg = msg + " ttl[" + ttl + "]";
    }
    if (mcastBindAddress != null) {
      msg = msg + " mcastBindAddress[" + mcastBindAddress + "]";
    }
    logger.info(msg);
    try
    {
      this.group = InetAddress.getByName(address);
      this.sock = new MulticastSocket(port);
      this.sock.setSoTimeout(timeout);
      if (disableLoopback) {
        this.sock.setLoopbackMode(disableLoopback);
      }
      if (mcastBindAddress != null) {
        this.sock.setInterface(mcastBindAddress);
      }
      if (ttl >= 0) {
        this.sock.setTimeToLive(ttl);
      }
      this.sock.setReuseAddress(true);
      this.pack = new DatagramPacket(this.receiveBuffer, this.receiveBuffer.length);
      this.sock.joinGroup(this.group);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void startListening()
  {
    this.listenerThread.setDaemon(true);
    this.listenerThread.start();
    
    logger.info("Cluster Multicast Listener up and joined Group");
  }
  
  public void shutdown()
  {
    this.shutdown = true;
    synchronized (this.listenerThread)
    {
      try
      {
        this.listenerThread.wait(20000L);
      }
      catch (InterruptedException e)
      {
        logger.info("InterruptedException:" + e);
      }
    }
    if (!this.shutdownComplete)
    {
      String msg = "WARNING: Shutdown of McastListener did not complete?";
      System.err.println(msg);
      logger.warning(msg);
    }
    try
    {
      this.sock.leaveGroup(this.group);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      String msg = "Error leaving Multicast group";
      logger.log(Level.INFO, msg, e);
    }
    try
    {
      this.sock.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      String msg = "Error closing Multicast socket";
      logger.log(Level.INFO, msg, e);
    }
  }
  
  public void run()
  {
    while (!this.shutdown) {
      try
      {
        this.pack.setLength(this.receiveBuffer.length);
        this.sock.receive(this.pack);
        
        InetSocketAddress senderAddr = (InetSocketAddress)this.pack.getSocketAddress();
        
        String senderHostPort = senderAddr.getAddress().getHostAddress() + ":" + senderAddr.getPort();
        if (senderHostPort.equals(this.localSenderHostPort))
        {
          if ((this.debugIgnore) || (logger.isLoggable(Level.FINE))) {
            logger.info("Ignoring message as sent by localSender: " + this.localSenderHostPort);
          }
        }
        else
        {
          byte[] data = this.pack.getData();
          
          ByteArrayInputStream bi = new ByteArrayInputStream(data);
          DataInputStream dataInput = new DataInputStream(bi);
          
          this.totalPacketsReceived += 1L;
          this.totalBytesReceived += this.pack.getLength();
          
          Packet header = Packet.readHeader(dataInput);
          
          long packetId = header.getPacketId();
          boolean ackMsg = packetId == 0L;
          
          boolean processThisPacket = (ackMsg) || (this.packetControl.isProcessPacket(senderHostPort, header.getPacketId()));
          if (!processThisPacket)
          {
            if ((this.debugIgnore) || (logger.isLoggable(Level.FINE))) {
              logger.info("Already processed packet: " + header.getPacketId() + " type:" + header.getPacketType() + " len:" + data.length);
            }
          }
          else
          {
            if (logger.isLoggable(Level.FINER)) {
              logger.info("Incoming packet:" + header.getPacketId() + " type:" + header.getPacketType() + " len:" + data.length);
            }
            processPacket(senderHostPort, header, dataInput);
          }
        }
      }
      catch (SocketTimeoutException e)
      {
        if (logger.isLoggable(Level.FINE)) {
          logger.log(Level.FINE, "timeout", e);
        }
        this.packetControl.onListenerTimeout();
      }
      catch (IOException e)
      {
        logger.log(Level.INFO, "error ?", e);
      }
    }
    this.shutdownComplete = true;
    synchronized (this.listenerThread)
    {
      this.listenerThread.notifyAll();
    }
  }
  
  protected void processPacket(String senderHostPort, Packet header, DataInput dataInput)
  {
    try
    {
      switch (header.getPacketType())
      {
      case 1: 
        this.packetControl.processMessagesPacket(senderHostPort, header, dataInput, this.totalPacketsReceived, this.totalBytesReceived, this.totalTxnEventsReceived);
        
        break;
      case 2: 
        this.totalTxnEventsReceived += 1L;
        processTransactionEventPacket(header, dataInput);
        break;
      default: 
        String msg = "Unknown Packet type:" + header.getPacketType();
        logger.log(Level.SEVERE, msg);
      }
    }
    catch (IOException e)
    {
      String msg = "Error reading Packet " + header.getPacketId() + " type:" + header.getPacketType();
      logger.log(Level.SEVERE, msg, e);
    }
  }
  
  private void processTransactionEventPacket(Packet header, DataInput dataInput)
    throws IOException
  {
    SpiEbeanServer server = this.owner.getEbeanServer(header.getServerName());
    
    PacketTransactionEvent tranEventPacket = PacketTransactionEvent.forRead(header, server);
    tranEventPacket.read(dataInput);
    
    server.remoteTransactionEvent(tranEventPacket.getEvent());
  }
}
