package com.avaje.ebeaninternal.server.cluster.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class SocketClient
{
  private static final Logger logger = Logger.getLogger(SocketClient.class.getName());
  private final InetSocketAddress address;
  private final String hostPort;
  private boolean online;
  private Socket socket;
  private OutputStream os;
  private ObjectOutputStream oos;
  
  public SocketClient(InetSocketAddress address)
  {
    this.address = address;
    this.hostPort = (address.getHostName() + ":" + address.getPort());
  }
  
  public String getHostPort()
  {
    return this.hostPort;
  }
  
  public int getPort()
  {
    return this.address.getPort();
  }
  
  public boolean isOnline()
  {
    return this.online;
  }
  
  public void setOnline(boolean online)
    throws IOException
  {
    if (online) {
      setOnline();
    } else {
      disconnect();
    }
  }
  
  private void setOnline()
    throws IOException
  {
    connect();
    this.online = true;
  }
  
  public void reconnect()
    throws IOException
  {
    disconnect();
    connect();
  }
  
  private void connect()
    throws IOException
  {
    if (this.socket != null) {
      throw new IllegalStateException("Already got a socket connection?");
    }
    Socket s = new Socket();
    s.setKeepAlive(true);
    s.connect(this.address);
    
    this.socket = s;
    this.os = this.socket.getOutputStream();
  }
  
  public void disconnect()
  {
    this.online = false;
    if (this.socket != null)
    {
      try
      {
        this.socket.close();
      }
      catch (IOException e)
      {
        String msg = "Error disconnecting from Cluster member " + this.hostPort;
        logger.log(Level.INFO, msg, e);
      }
      this.os = null;
      this.oos = null;
      this.socket = null;
    }
  }
  
  public boolean register(SocketClusterMessage registerMsg)
  {
    try
    {
      setOnline();
      send(registerMsg);
      return true;
    }
    catch (IOException e)
    {
      disconnect();
    }
    return false;
  }
  
  public boolean send(SocketClusterMessage msg)
    throws IOException
  {
    if (this.online)
    {
      writeObject(msg);
      return true;
    }
    return false;
  }
  
  private void writeObject(Object object)
    throws IOException
  {
    if (this.oos == null) {
      this.oos = new ObjectOutputStream(this.os);
    }
    this.oos.writeObject(object);
    this.oos.flush();
  }
}
