package org.apache.logging.log4j.core.appender;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;

public class RandomAccessFileManager
  extends OutputStreamManager
{
  static final int DEFAULT_BUFFER_SIZE = 262144;
  private static final RandomAccessFileManagerFactory FACTORY = new RandomAccessFileManagerFactory(null);
  private final boolean isImmediateFlush;
  private final String advertiseURI;
  private final RandomAccessFile randomAccessFile;
  private final ByteBuffer buffer;
  private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal();
  
  protected RandomAccessFileManager(RandomAccessFile file, String fileName, OutputStream os, boolean immediateFlush, String advertiseURI, Layout<? extends Serializable> layout)
  {
    super(os, fileName, layout);
    this.isImmediateFlush = immediateFlush;
    this.randomAccessFile = file;
    this.advertiseURI = advertiseURI;
    this.isEndOfBatch.set(Boolean.FALSE);
    
    this.buffer = ByteBuffer.allocate(262144);
  }
  
  public static RandomAccessFileManager getFileManager(String fileName, boolean append, boolean isFlush, String advertiseURI, Layout<? extends Serializable> layout)
  {
    return (RandomAccessFileManager)getManager(fileName, new FactoryData(append, isFlush, advertiseURI, layout), FACTORY);
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
  
  public String getFileName()
  {
    return getName();
  }
  
  public Map<String, String> getContentFormat()
  {
    Map<String, String> result = new HashMap(super.getContentFormat());
    
    result.put("fileURI", this.advertiseURI);
    return result;
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
    private final boolean append;
    private final boolean immediateFlush;
    private final String advertiseURI;
    private final Layout<? extends Serializable> layout;
    
    public FactoryData(boolean append, boolean immediateFlush, String advertiseURI, Layout<? extends Serializable> layout)
    {
      this.append = append;
      this.immediateFlush = immediateFlush;
      this.advertiseURI = advertiseURI;
      this.layout = layout;
    }
  }
  
  private static class RandomAccessFileManagerFactory
    implements ManagerFactory<RandomAccessFileManager, RandomAccessFileManager.FactoryData>
  {
    public RandomAccessFileManager createManager(String name, RandomAccessFileManager.FactoryData data)
    {
      File file = new File(name);
      File parent = file.getParentFile();
      if ((null != parent) && (!parent.exists())) {
        parent.mkdirs();
      }
      if (!RandomAccessFileManager.FactoryData.access$100(data)) {
        file.delete();
      }
      OutputStream os = new RandomAccessFileManager.DummyOutputStream();
      try
      {
        RandomAccessFile raf = new RandomAccessFile(name, "rw");
        if (RandomAccessFileManager.FactoryData.access$100(data)) {
          raf.seek(raf.length());
        } else {
          raf.setLength(0L);
        }
        return new RandomAccessFileManager(raf, name, os, RandomAccessFileManager.FactoryData.access$200(data), RandomAccessFileManager.FactoryData.access$300(data), RandomAccessFileManager.FactoryData.access$400(data));
      }
      catch (Exception ex)
      {
        AbstractManager.LOGGER.error("RandomAccessFileManager (" + name + ") " + ex);
      }
      return null;
    }
  }
}
