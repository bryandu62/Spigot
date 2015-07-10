package org.apache.logging.log4j.core.appender.rolling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.FileManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public class RollingFileManager
  extends FileManager
{
  private static RollingFileManagerFactory factory = new RollingFileManagerFactory(null);
  private long size;
  private long initialTime;
  private final PatternProcessor patternProcessor;
  private final Semaphore semaphore = new Semaphore(1);
  private final TriggeringPolicy policy;
  private final RolloverStrategy strategy;
  
  protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long time, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout)
  {
    super(fileName, os, append, false, advertiseURI, layout);
    this.size = size;
    this.initialTime = time;
    this.policy = policy;
    this.strategy = strategy;
    this.patternProcessor = new PatternProcessor(pattern);
    policy.initialize(this);
  }
  
  public static RollingFileManager getFileManager(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout)
  {
    return (RollingFileManager)getManager(fileName, new FactoryData(pattern, append, bufferedIO, policy, strategy, advertiseURI, layout), factory);
  }
  
  protected synchronized void write(byte[] bytes, int offset, int length)
  {
    this.size += length;
    super.write(bytes, offset, length);
  }
  
  public long getFileSize()
  {
    return this.size;
  }
  
  public long getFileTime()
  {
    return this.initialTime;
  }
  
  public synchronized void checkRollover(LogEvent event)
  {
    if ((this.policy.isTriggeringEvent(event)) && (rollover(this.strategy))) {
      try
      {
        this.size = 0L;
        this.initialTime = System.currentTimeMillis();
        createFileAfterRollover();
      }
      catch (IOException ex)
      {
        LOGGER.error("FileManager (" + getFileName() + ") " + ex);
      }
    }
  }
  
  protected void createFileAfterRollover()
    throws IOException
  {
    OutputStream os = new FileOutputStream(getFileName(), isAppend());
    setOutputStream(os);
  }
  
  public PatternProcessor getPatternProcessor()
  {
    return this.patternProcessor;
  }
  
  private boolean rollover(RolloverStrategy strategy)
  {
    try
    {
      this.semaphore.acquire();
    }
    catch (InterruptedException ie)
    {
      LOGGER.error("Thread interrupted while attempting to check rollover", ie);
      return false;
    }
    boolean success = false;
    Thread thread = null;
    try
    {
      RolloverDescription descriptor = strategy.rollover(this);
      if (descriptor != null)
      {
        close();
        if (descriptor.getSynchronous() != null) {
          try
          {
            success = descriptor.getSynchronous().execute();
          }
          catch (Exception ex)
          {
            LOGGER.error("Error in synchronous task", ex);
          }
        }
        if ((success) && (descriptor.getAsynchronous() != null))
        {
          thread = new Thread(new AsyncAction(descriptor.getAsynchronous(), this));
          thread.start();
        }
        return 1;
      }
      return 0;
    }
    finally
    {
      if (thread == null) {
        this.semaphore.release();
      }
    }
  }
  
  private static class AsyncAction
    extends AbstractAction
  {
    private final Action action;
    private final RollingFileManager manager;
    
    public AsyncAction(Action act, RollingFileManager manager)
    {
      this.action = act;
      this.manager = manager;
    }
    
    public boolean execute()
      throws IOException
    {
      try
      {
        return this.action.execute();
      }
      finally
      {
        this.manager.semaphore.release();
      }
    }
    
    public void close()
    {
      this.action.close();
    }
    
    public boolean isComplete()
    {
      return this.action.isComplete();
    }
  }
  
  private static class FactoryData
  {
    private final String pattern;
    private final boolean append;
    private final boolean bufferedIO;
    private final TriggeringPolicy policy;
    private final RolloverStrategy strategy;
    private final String advertiseURI;
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout)
    {
      this.pattern = pattern;
      this.append = append;
      this.bufferedIO = bufferedIO;
      this.policy = policy;
      this.strategy = strategy;
      this.advertiseURI = advertiseURI;
      this.layout = layout;
    }
  }
  
  private static class RollingFileManagerFactory
    implements ManagerFactory<RollingFileManager, RollingFileManager.FactoryData>
  {
    public RollingFileManager createManager(String name, RollingFileManager.FactoryData data)
    {
      File file = new File(name);
      File parent = file.getParentFile();
      if ((null != parent) && (!parent.exists())) {
        parent.mkdirs();
      }
      try
      {
        file.createNewFile();
      }
      catch (IOException ioe)
      {
        RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
        return null;
      }
      long size = RollingFileManager.FactoryData.access$300(data) ? file.length() : 0L;
      long time = file.lastModified();
      try
      {
        OutputStream os = new FileOutputStream(name, RollingFileManager.FactoryData.access$300(data));
        if (RollingFileManager.FactoryData.access$400(data)) {
          os = new BufferedOutputStream(os);
        }
        return new RollingFileManager(name, RollingFileManager.FactoryData.access$500(data), os, RollingFileManager.FactoryData.access$300(data), size, time, RollingFileManager.FactoryData.access$600(data), RollingFileManager.FactoryData.access$700(data), RollingFileManager.FactoryData.access$800(data), RollingFileManager.FactoryData.access$900(data));
      }
      catch (FileNotFoundException ex)
      {
        RollingFileManager.LOGGER.error("FileManager (" + name + ") " + ex);
      }
      return null;
    }
  }
}
