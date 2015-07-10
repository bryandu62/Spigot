package com.avaje.ebeaninternal.server.cluster.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class RequestProcessor
  implements Runnable
{
  private static final Logger logger = Logger.getLogger(RequestProcessor.class.getName());
  private final Socket clientSocket;
  private final SocketClusterBroadcast owner;
  
  public RequestProcessor(SocketClusterBroadcast owner, Socket clientSocket)
  {
    this.clientSocket = clientSocket;
    this.owner = owner;
  }
  
  public void run()
  {
    try
    {
      SocketConnection sc = new SocketConnection(this.clientSocket);
      for (;;)
      {
        if (this.owner.process(sc)) {
          break;
        }
      }
      sc.disconnect();
    }
    catch (IOException e)
    {
      logger.log(Level.SEVERE, null, e);
    }
    catch (ClassNotFoundException e)
    {
      logger.log(Level.SEVERE, null, e);
    }
  }
}
