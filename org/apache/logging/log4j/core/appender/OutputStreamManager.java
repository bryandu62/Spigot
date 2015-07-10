package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;

public class OutputStreamManager
  extends AbstractManager
{
  private volatile OutputStream os;
  private final byte[] footer;
  private final byte[] header;
  
  protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout)
  {
    super(streamName);
    this.os = os;
    if (layout != null)
    {
      this.footer = layout.getFooter();
      this.header = layout.getHeader();
      if (this.header != null) {
        try
        {
          this.os.write(this.header, 0, this.header.length);
        }
        catch (IOException ioe)
        {
          LOGGER.error("Unable to write header", ioe);
        }
      }
    }
    else
    {
      this.footer = null;
      this.header = null;
    }
  }
  
  public static <T> OutputStreamManager getManager(String name, T data, ManagerFactory<? extends OutputStreamManager, T> factory)
  {
    return (OutputStreamManager)AbstractManager.getManager(name, factory, data);
  }
  
  public void releaseSub()
  {
    if (this.footer != null) {
      write(this.footer);
    }
    close();
  }
  
  public boolean isOpen()
  {
    return getCount() > 0;
  }
  
  protected OutputStream getOutputStream()
  {
    return this.os;
  }
  
  protected void setOutputStream(OutputStream os)
  {
    if (this.header != null) {
      try
      {
        os.write(this.header, 0, this.header.length);
        this.os = os;
      }
      catch (IOException ioe)
      {
        LOGGER.error("Unable to write header", ioe);
      }
    } else {
      this.os = os;
    }
  }
  
  protected synchronized void write(byte[] bytes, int offset, int length)
  {
    try
    {
      this.os.write(bytes, offset, length);
    }
    catch (IOException ex)
    {
      String msg = "Error writing to stream " + getName();
      throw new AppenderLoggingException(msg, ex);
    }
  }
  
  protected void write(byte[] bytes)
  {
    write(bytes, 0, bytes.length);
  }
  
  protected synchronized void close()
  {
    OutputStream stream = this.os;
    if ((stream == System.out) || (stream == System.err)) {
      return;
    }
    try
    {
      stream.close();
    }
    catch (IOException ex)
    {
      LOGGER.error("Unable to close stream " + getName() + ". " + ex);
    }
  }
  
  public synchronized void flush()
  {
    try
    {
      this.os.flush();
    }
    catch (IOException ex)
    {
      String msg = "Error flushing stream " + getName();
      throw new AppenderLoggingException(msg, ex);
    }
  }
}
