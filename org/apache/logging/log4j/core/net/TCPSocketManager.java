package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.helpers.Strings;

public class TCPSocketManager
  extends AbstractSocketManager
{
  public static final int DEFAULT_RECONNECTION_DELAY = 30000;
  private static final int DEFAULT_PORT = 4560;
  private static final TCPSocketManagerFactory FACTORY = new TCPSocketManagerFactory();
  private final int reconnectionDelay;
  private Reconnector connector = null;
  private Socket socket;
  private final boolean retry;
  private final boolean immediateFail;
  
  public TCPSocketManager(String name, OutputStream os, Socket sock, InetAddress addr, String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout)
  {
    super(name, os, addr, host, port, layout);
    this.reconnectionDelay = delay;
    this.socket = sock;
    this.immediateFail = immediateFail;
    this.retry = (delay > 0);
    if (sock == null)
    {
      this.connector = new Reconnector(this);
      this.connector.setDaemon(true);
      this.connector.setPriority(1);
      this.connector.start();
    }
  }
  
  public static TCPSocketManager getSocketManager(String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout)
  {
    if (Strings.isEmpty(host)) {
      throw new IllegalArgumentException("A host name is required");
    }
    if (port <= 0) {
      port = 4560;
    }
    if (delay == 0) {
      delay = 30000;
    }
    return (TCPSocketManager)getManager("TCP:" + host + ":" + port, new FactoryData(host, port, delay, immediateFail, layout), FACTORY);
  }
  
  protected void write(byte[] bytes, int offset, int length)
  {
    if (this.socket == null)
    {
      if ((this.connector != null) && (!this.immediateFail)) {
        this.connector.latch();
      }
      if (this.socket == null)
      {
        String msg = "Error writing to " + getName() + " socket not available";
        throw new AppenderLoggingException(msg);
      }
    }
    synchronized (this)
    {
      try
      {
        getOutputStream().write(bytes, offset, length);
      }
      catch (IOException ex)
      {
        if ((this.retry) && (this.connector == null))
        {
          this.connector = new Reconnector(this);
          this.connector.setDaemon(true);
          this.connector.setPriority(1);
          this.connector.start();
        }
        String msg = "Error writing to " + getName();
        throw new AppenderLoggingException(msg, ex);
      }
    }
  }
  
  protected synchronized void close()
  {
    super.close();
    if (this.connector != null)
    {
      this.connector.shutdown();
      this.connector.interrupt();
      this.connector = null;
    }
  }
  
  public Map<String, String> getContentFormat()
  {
    Map<String, String> result = new HashMap(super.getContentFormat());
    result.put("protocol", "tcp");
    result.put("direction", "out");
    return result;
  }
  
  private class Reconnector
    extends Thread
  {
    private final CountDownLatch latch = new CountDownLatch(1);
    private boolean shutdown = false;
    private final Object owner;
    
    public Reconnector(OutputStreamManager owner)
    {
      this.owner = owner;
    }
    
    public void latch()
    {
      try
      {
        this.latch.await();
      }
      catch (InterruptedException ex) {}
    }
    
    public void shutdown()
    {
      this.shutdown = true;
    }
    
    public void run()
    {
      while (!this.shutdown) {
        try
        {
          sleep(TCPSocketManager.this.reconnectionDelay);
          Socket sock = TCPSocketManager.this.createSocket(TCPSocketManager.this.address, TCPSocketManager.this.port);
          OutputStream newOS = sock.getOutputStream();
          synchronized (this.owner)
          {
            try
            {
              TCPSocketManager.this.getOutputStream().close();
            }
            catch (IOException ioe) {}
            TCPSocketManager.this.setOutputStream(newOS);
            TCPSocketManager.this.socket = sock;
            TCPSocketManager.this.connector = null;
            this.shutdown = true;
          }
          TCPSocketManager.LOGGER.debug("Connection to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " reestablished.");
        }
        catch (InterruptedException ie)
        {
          TCPSocketManager.LOGGER.debug("Reconnection interrupted.");
        }
        catch (ConnectException ex)
        {
          TCPSocketManager.LOGGER.debug(TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " refused connection");
        }
        catch (IOException ioe)
        {
          TCPSocketManager.LOGGER.debug("Unable to reconnect to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port);
        }
        finally
        {
          this.latch.countDown();
        }
      }
    }
  }
  
  protected Socket createSocket(InetAddress host, int port)
    throws IOException
  {
    return createSocket(host.getHostName(), port);
  }
  
  protected Socket createSocket(String host, int port)
    throws IOException
  {
    return new Socket(host, port);
  }
  
  private static class FactoryData
  {
    private final String host;
    private final int port;
    private final int delay;
    private final boolean immediateFail;
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(String host, int port, int delay, boolean immediateFail, Layout<? extends Serializable> layout)
    {
      this.host = host;
      this.port = port;
      this.delay = delay;
      this.immediateFail = immediateFail;
      this.layout = layout;
    }
  }
  
  protected static class TCPSocketManagerFactory
    implements ManagerFactory<TCPSocketManager, TCPSocketManager.FactoryData>
  {
    public TCPSocketManager createManager(String name, TCPSocketManager.FactoryData data)
    {
      InetAddress address;
      try
      {
        address = InetAddress.getByName(TCPSocketManager.FactoryData.access$900(data));
      }
      catch (UnknownHostException ex)
      {
        TCPSocketManager.LOGGER.error("Could not find address of " + TCPSocketManager.FactoryData.access$900(data), ex);
        return null;
      }
      OutputStream os;
      try
      {
        Socket socket = new Socket(TCPSocketManager.FactoryData.access$900(data), TCPSocketManager.FactoryData.access$1100(data));
        os = socket.getOutputStream();
        return new TCPSocketManager(name, os, socket, address, TCPSocketManager.FactoryData.access$900(data), TCPSocketManager.FactoryData.access$1100(data), TCPSocketManager.FactoryData.access$1200(data), TCPSocketManager.FactoryData.access$1300(data), TCPSocketManager.FactoryData.access$1400(data));
      }
      catch (IOException ex)
      {
        TCPSocketManager.LOGGER.error("TCPSocketManager (" + name + ") " + ex);
        os = new ByteArrayOutputStream();
        if (TCPSocketManager.FactoryData.access$1200(data) == 0) {
          return null;
        }
      }
      return new TCPSocketManager(name, os, null, address, TCPSocketManager.FactoryData.access$900(data), TCPSocketManager.FactoryData.access$1100(data), TCPSocketManager.FactoryData.access$1200(data), TCPSocketManager.FactoryData.access$1300(data), TCPSocketManager.FactoryData.access$1400(data));
    }
  }
}
