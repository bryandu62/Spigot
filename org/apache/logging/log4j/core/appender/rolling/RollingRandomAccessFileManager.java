package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class RollingRandomAccessFileManager
  extends RollingFileManager
{
  static final int DEFAULT_BUFFER_SIZE = 262144;
  private static final RollingRandomAccessFileManagerFactory FACTORY = new RollingRandomAccessFileManagerFactory(null);
  private final boolean isImmediateFlush;
  private RandomAccessFile randomAccessFile;
  private final ByteBuffer buffer;
  private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal();
  
  public RollingRandomAccessFileManager(RandomAccessFile raf, String fileName, String pattern, OutputStream os, boolean append, boolean immediateFlush, long size, long time, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout)
  {
    super(fileName, pattern, os, append, size, time, policy, strategy, advertiseURI, layout);
    this.isImmediateFlush = immediateFlush;
    this.randomAccessFile = raf;
    this.isEndOfBatch.set(Boolean.FALSE);
    
    this.buffer = ByteBuffer.allocate(262144);
  }
  
  public static RollingRandomAccessFileManager getRollingRandomAccessFileManager(String fileName, String filePattern, boolean isAppend, boolean immediateFlush, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout)
  {
    return (RollingRandomAccessFileManager)getManager(fileName, new FactoryData(filePattern, isAppend, immediateFlush, policy, strategy, advertiseURI, layout), FACTORY);
  }
  
  public Boolean isEndOfBatch()
  {
    return (Boolean)this.isEndOfBatch.get();
  }
  
  public void setEndOfBatch(boolean isEndOfBatch)
  {
    this.isEndOfBatch.set(Boolean.valueOf(isEndOfBatch));
  }
  
  protected synchronized void write(byte[] bytes, int offset, int length)
  {
    super.write(bytes, offset, length);
    
    int chunk = 0;
    do
    {
      if (length > this.buffer.remaining()) {
        flush();
      }
      chunk = Math.min(length, this.buffer.remaining());
      this.buffer.put(bytes, offset, chunk);
      offset += chunk;
      length -= chunk;
    } while (length > 0);
    if ((this.isImmediateFlush) || (this.isEndOfBatch.get() == Boolean.TRUE)) {
      flush();
    }
  }
  
  protected void createFileAfterRollover()
    throws IOException
  {
    this.randomAccessFile = new RandomAccessFile(getFileName(), "rw");
    if (isAppend()) {
      this.randomAccessFile.seek(this.randomAccessFile.length());
    }
  }
  
  public synchronized void flush()
  {
    this.buffer.flip();
    try
    {
      this.randomAccessFile.write(this.buffer.array(), 0, this.buffer.limit());
    }
    catch (IOException ex)
    {
      String msg = "Error writing to RandomAccessFile " + getName();
      throw new AppenderLoggingException(msg, ex);
    }
    this.buffer.clear();
  }
  
  public synchronized void close()
  {
    flush();
    try
    {
      this.randomAccessFile.close();
    }
    catch (IOException ex)
    {
      LOGGER.error("Unable to close RandomAccessFile " + getName() + ". " + ex);
    }
  }
  
  private static class RollingRandomAccessFileManagerFactory
    implements ManagerFactory<RollingRandomAccessFileManager, RollingRandomAccessFileManager.FactoryData>
  {
    public RollingRandomAccessFileManager createManager(String name, RollingRandomAccessFileManager.FactoryData data)
    {
      File file = new File(name);
      File parent = file.getParentFile();
      if ((null != parent) && (!parent.exists())) {
        parent.mkdirs();
      }
      if (!data.append) {
        file.delete();
      }
      long size = data.append ? file.length() : 0L;
      long time = file.exists() ? file.lastModified() : System.currentTimeMillis();
      
      RandomAccessFile raf = null;
      try
      {
        raf = new RandomAccessFile(name, "rw");
        if (data.append)
        {
          long length = raf.length();
          RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} seek to {}", new Object[] { name, Long.valueOf(length) });
          raf.seek(length);
        }
        else
        {
          RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} set length to 0", new Object[] { name });
          raf.setLength(0L);
        }
        return new RollingRandomAccessFileManager(raf, name, data.pattern, new RollingRandomAccessFileManager.DummyOutputStream(), data.append, data.immediateFlush, size, time, data.policy, data.strategy, data.advertiseURI, data.layout);
      }
      catch (IOException ex)
      {
        RollingRandomAccessFileManager.LOGGER.error("Cannot access RandomAccessFile {}) " + ex);
        if (raf != null) {
          try
          {
            raf.close();
          }
          catch (IOException e)
          {
            RollingRandomAccessFileManager.LOGGER.error("Cannot close RandomAccessFile {}", new Object[] { name, e });
          }
        }
      }
      return null;
    }
  }
  
  static class DummyOutputStream
    extends OutputStream
  {
    public void write(int b)
      throws IOException
    {}
    
    public void write(byte[] b, int off, int len)
      throws IOException
    {}
  }
  
  private static class FactoryData
  {
    private final String pattern;
    private final boolean append;
    private final boolean immediateFlush;
    private final TriggeringPolicy policy;
    private final RolloverStrategy strategy;
    private final String advertiseURI;
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(String pattern, boolean append, boolean immediateFlush, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout)
    {
      this.pattern = pattern;
      this.append = append;
      this.immediateFlush = immediateFlush;
      this.policy = policy;
      this.strategy = strategy;
      this.advertiseURI = advertiseURI;
      this.layout = layout;
    }
  }
}
