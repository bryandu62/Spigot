package org.apache.logging.log4j.core.appender.db;

import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public abstract class AbstractDatabaseManager
  extends AbstractManager
{
  private final ArrayList<LogEvent> buffer;
  private final int bufferSize;
  private boolean connected = false;
  
  protected AbstractDatabaseManager(String name, int bufferSize)
  {
    super(name);
    this.bufferSize = bufferSize;
    this.buffer = new ArrayList(bufferSize + 1);
  }
  
  protected abstract void connectInternal()
    throws Exception;
  
  public final synchronized void connect()
  {
    if (!isConnected()) {
      try
      {
        connectInternal();
        this.connected = true;
      }
      catch (Exception e)
      {
        LOGGER.error("Could not connect to database using logging manager [{}].", new Object[] { getName(), e });
      }
    }
  }
  
  protected abstract void disconnectInternal()
    throws Exception;
  
  public final synchronized void disconnect()
  {
    flush();
    if (isConnected()) {
      try
      {
        disconnectInternal();
      }
      catch (Exception e)
      {
        LOGGER.warn("Error while disconnecting from database using logging manager [{}].", new Object[] { getName(), e });
      }
      finally
      {
        this.connected = false;
      }
    }
  }
  
  public final boolean isConnected()
  {
    return this.connected;
  }
  
  protected abstract void writeInternal(LogEvent paramLogEvent);
  
  public final synchronized void flush()
  {
    if ((isConnected()) && (this.buffer.size() > 0))
    {
      for (LogEvent event : this.buffer) {
        writeInternal(event);
      }
      this.buffer.clear();
    }
  }
  
  public final synchronized void write(LogEvent event)
  {
    if (this.bufferSize > 0)
    {
      this.buffer.add(event);
      if ((this.buffer.size() >= this.bufferSize) || (event.isEndOfBatch())) {
        flush();
      }
    }
    else
    {
      writeInternal(event);
    }
  }
  
  public final void releaseSub()
  {
    disconnect();
  }
  
  public final String toString()
  {
    return getName();
  }
  
  protected static <M extends AbstractDatabaseManager, T extends AbstractFactoryData> M getManager(String name, T data, ManagerFactory<M, T> factory)
  {
    return (AbstractDatabaseManager)AbstractManager.getManager(name, factory, data);
  }
  
  protected static abstract class AbstractFactoryData
  {
    private final int bufferSize;
    
    protected AbstractFactoryData(int bufferSize)
    {
      this.bufferSize = bufferSize;
    }
    
    public int getBufferSize()
    {
      return this.bufferSize;
    }
  }
}
