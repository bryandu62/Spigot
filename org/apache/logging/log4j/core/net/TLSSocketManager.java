package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;

public class TLSSocketManager
  extends TCPSocketManager
{
  public static final int DEFAULT_PORT = 6514;
  private static final TLSSocketManagerFactory FACTORY = new TLSSocketManagerFactory(null);
  private SSLConfiguration sslConfig;
  
  public TLSSocketManager(String name, OutputStream os, Socket sock, SSLConfiguration sslConfig, InetAddress addr, String host, int port, int delay, boolean immediateFail, Layout layout)
  {
    super(name, os, sock, addr, host, port, delay, immediateFail, layout);
    this.sslConfig = sslConfig;
  }
  
  private static class TLSFactoryData
  {
    protected SSLConfiguration sslConfig;
    private final String host;
    private final int port;
    private final int delay;
    private final boolean immediateFail;
    private final Layout layout;
    
    public TLSFactoryData(SSLConfiguration sslConfig, String host, int port, int delay, boolean immediateFail, Layout layout)
    {
      this.host = host;
      this.port = port;
      this.delay = delay;
      this.immediateFail = immediateFail;
      this.layout = layout;
      this.sslConfig = sslConfig;
    }
  }
  
  public static TLSSocketManager getSocketManager(SSLConfiguration sslConfig, String host, int port, int delay, boolean immediateFail, Layout layout)
  {
    if (Strings.isEmpty(host)) {
      throw new IllegalArgumentException("A host name is required");
    }
    if (port <= 0) {
      port = 6514;
    }
    if (delay == 0) {
      delay = 30000;
    }
    return (TLSSocketManager)getManager("TLS:" + host + ":" + port, new TLSFactoryData(sslConfig, host, port, delay, immediateFail, layout), FACTORY);
  }
  
  protected Socket createSocket(String host, int port)
    throws IOException
  {
    SSLSocketFactory socketFactory = createSSLSocketFactory(this.sslConfig);
    return socketFactory.createSocket(host, port);
  }
  
  private static SSLSocketFactory createSSLSocketFactory(SSLConfiguration sslConf)
  {
    SSLSocketFactory socketFactory;
    SSLSocketFactory socketFactory;
    if (sslConf != null) {
      socketFactory = sslConf.getSSLSocketFactory();
    } else {
      socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    }
    return socketFactory;
  }
  
  private static class TLSSocketManagerFactory
    implements ManagerFactory<TLSSocketManager, TLSSocketManager.TLSFactoryData>
  {
    public TLSSocketManager createManager(String name, TLSSocketManager.TLSFactoryData data)
    {
      InetAddress address = null;
      OutputStream os = null;
      Socket socket = null;
      try
      {
        address = resolveAddress(TLSSocketManager.TLSFactoryData.access$100(data));
        socket = createSocket(data);
        os = socket.getOutputStream();
        checkDelay(TLSSocketManager.TLSFactoryData.access$200(data), os);
      }
      catch (IOException e)
      {
        TLSSocketManager.LOGGER.error("TLSSocketManager (" + name + ") " + e);
        os = new ByteArrayOutputStream();
      }
      catch (TLSSocketManagerFactoryException e)
      {
        return null;
      }
      return createManager(name, os, socket, data.sslConfig, address, TLSSocketManager.TLSFactoryData.access$100(data), TLSSocketManager.TLSFactoryData.access$400(data), TLSSocketManager.TLSFactoryData.access$200(data), TLSSocketManager.TLSFactoryData.access$500(data), TLSSocketManager.TLSFactoryData.access$600(data));
    }
    
    private InetAddress resolveAddress(String hostName)
      throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactoryException
    {
      InetAddress address;
      try
      {
        address = InetAddress.getByName(hostName);
      }
      catch (UnknownHostException ex)
      {
        TLSSocketManager.LOGGER.error("Could not find address of " + hostName, ex);
        throw new TLSSocketManagerFactoryException(null);
      }
      return address;
    }
    
    private void checkDelay(int delay, OutputStream os)
      throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactoryException
    {
      if ((delay == 0) && (os == null)) {
        throw new TLSSocketManagerFactoryException(null);
      }
    }
    
    private Socket createSocket(TLSSocketManager.TLSFactoryData data)
      throws IOException
    {
      SSLSocketFactory socketFactory = TLSSocketManager.createSSLSocketFactory(data.sslConfig);
      SSLSocket socket = (SSLSocket)socketFactory.createSocket(TLSSocketManager.TLSFactoryData.access$100(data), TLSSocketManager.TLSFactoryData.access$400(data));
      return socket;
    }
    
    private TLSSocketManager createManager(String name, OutputStream os, Socket socket, SSLConfiguration sslConfig, InetAddress address, String host, int port, int delay, boolean immediateFail, Layout layout)
    {
      return new TLSSocketManager(name, os, socket, sslConfig, address, host, port, delay, immediateFail, layout);
    }
    
    private class TLSSocketManagerFactoryException
      extends Exception
    {
      private TLSSocketManagerFactoryException() {}
    }
  }
}
