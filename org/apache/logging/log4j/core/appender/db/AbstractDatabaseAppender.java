package org.apache.logging.log4j.core.appender.db;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;

public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager>
  extends AbstractAppender
{
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = this.lock.readLock();
  private final Lock writeLock = this.lock.writeLock();
  private T manager;
  
  protected AbstractDatabaseAppender(String name, Filter filter, boolean ignoreExceptions, T manager)
  {
    super(name, filter, null, ignoreExceptions);
    this.manager = manager;
  }
  
  public final Layout<LogEvent> getLayout()
  {
    return null;
  }
  
  public final T getManager()
  {
    return this.manager;
  }
  
  public final void start()
  {
    if (getManager() == null) {
      LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", new Object[] { getName() });
    }
    super.start();
    if (getManager() != null) {
      getManager().connect();
    }
  }
  
  public final void stop()
  {
    super.stop();
    if (getManager() != null) {
      getManager().release();
    }
  }
  
  public final void append(LogEvent event)
  {
    this.readLock.lock();
    try
    {
      getManager().write(event);
    }
    catch (LoggingException e)
    {
      LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[] { getManager().getName(), getName(), e });
      
      throw e;
    }
    catch (Exception e)
    {
      LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[] { getManager().getName(), getName(), e });
      
      throw new AppenderLoggingException("Unable to write to database in appender: " + e.getMessage(), e);
    }
    finally
    {
      this.readLock.unlock();
    }
  }
  
  protected final void replaceManager(T manager)
  {
    this.writeLock.lock();
    try
    {
      T old = getManager();
      if (!manager.isConnected()) {
        manager.connect();
      }
      this.manager = manager;
      old.release();
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
}
