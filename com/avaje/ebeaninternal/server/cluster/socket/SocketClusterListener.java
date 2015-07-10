package com.avaje.ebeaninternal.server.cluster.socket;

import com.avaje.ebeaninternal.server.lib.thread.ThreadPool;
import com.avaje.ebeaninternal.server.lib.thread.ThreadPoolManager;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

class SocketClusterListener
  implements Runnable
{
  private static final Logger logger = Logger.getLogger(SocketClusterListener.class.getName());
  private final int port;
  private final int listenTimeout = 60000;
  private final ServerSocket serverListenSocket;
  private final Thread listenerThread;
  private final ThreadPool threadPool;
  private final SocketClusterBroadcast owner;
  boolean doingShutdown;
  boolean isActive;
  
  public SocketClusterListener(SocketClusterBroadcast owner, int port)
  {
    this.owner = owner;
    this.threadPool = ThreadPoolManager.getThreadPool("EbeanClusterMember");
    this.port = port;
    try
    {
      this.serverListenSocket = new ServerSocket(port);
      this.serverListenSocket.setSoTimeout(60000);
      this.listenerThread = new Thread(this, "EbeanClusterListener");
    }
    catch (IOException e)
    {
      String msg = "Error starting cluster socket listener on port " + port;
      throw new RuntimeException(msg, e);
    }
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  public void startListening()
    throws IOException
  {
    this.listenerThread.setDaemon(true);
    this.listenerThread.start();
  }
  
  public void shutdown()
  {
    this.doingShutdown = true;
    try
    {
      if (this.isActive) {
        synchronized (this.listenerThread)
        {
          try
          {
            this.listenerThread.wait(1000L);
          }
          catch (InterruptedException e) {}
        }
      }
      this.listenerThread.interrupt();
      this.serverListenSocket.close();
    }
    catch (IOException e)
    {
      logger.log(Level.SEVERE, null, e);
    }
  }
  
  public void run()
  {
    while (!this.doingShutdown) {
      try
      {
        synchronized (this.listenerThread)
        {
          Socket clientSocket = this.serverListenSocket.accept();
          
          this.isActive = true;
          
          Runnable request = new RequestProcessor(this.owner, clientSocket);
          this.threadPool.assign(request, true);
          
          this.isActive = false;
        }
      }
      catch (SocketException e)
      {
        if (this.doingShutdown)
        {
          String msg = "doingShutdown and accept threw:" + e.getMessage();
          logger.info(msg);
        }
        else
        {
          logger.log(Level.SEVERE, null, e);
        }
      }
      catch (InterruptedIOException e)
      {
        logger.fine("Possibly expected due to accept timeout?" + e.getMessage());
      }
      catch (IOException e)
      {
        logger.log(Level.SEVERE, null, e);
      }
    }
  }
}
