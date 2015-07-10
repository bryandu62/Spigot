package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractOutputStreamAppender
  extends AbstractAppender
{
  protected final boolean immediateFlush;
  private volatile OutputStreamManager manager;
  private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final Lock readLock = this.rwLock.readLock();
  private final Lock writeLock = this.rwLock.writeLock();
  
  protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, OutputStreamManager manager)
  {
    super(name, filter, layout, ignoreExceptions);
    this.manager = manager;
    this.immediateFlush = immediateFlush;
  }
  
  protected OutputStreamManager getManager()
  {
    return this.manager;
  }
  
  protected void replaceManager(OutputStreamManager newManager)
  {
    this.writeLock.lock();
    try
    {
      OutputStreamManager old = this.manager;
      this.manager = newManager;
      old.release();
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void start()
  {
    if (getLayout() == null) {
      LOGGER.error("No layout set for the appender named [" + getName() + "].");
    }
    if (this.manager == null) {
      LOGGER.error("No OutputStreamManager set for the appender named [" + getName() + "].");
    }
    super.start();
  }
  
  public void stop()
  {
    super.stop();
    this.manager.release();
  }
  
  public void append(LogEvent event)
  {
    this.readLock.lock();
    try
    {
      byte[] bytes = getLayout().toByteArray(event);
      if (bytes.length > 0)
      {
        this.manager.write(bytes);
        if ((this.immediateFlush) || (event.isEndOfBatch())) {
          this.manager.flush();
        }
      }
    }
    catch (AppenderLoggingException ex)
    {
      error("Unable to write to stream " + this.manager.getName() + " for appender " + getName());
      throw ex;
    }
    finally
    {
      this.readLock.unlock();
    }
  }
}
