package org.apache.logging.log4j.core.layout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="SerializedLayout", category="Core", elementType="layout", printObject=true)
public final class SerializedLayout
  extends AbstractLayout<LogEvent>
{
  private static byte[] header;
  
  static
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.close();
      header = baos.toByteArray();
    }
    catch (Exception ex)
    {
      LOGGER.error("Unable to generate Object stream header", ex);
    }
  }
  
  public byte[] toByteArray(LogEvent event)
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      ObjectOutputStream oos = new PrivateObjectOutputStream(baos);
      try
      {
        oos.writeObject(event);
        oos.reset();
      }
      finally
      {
        oos.close();
      }
    }
    catch (IOException ioe)
    {
      LOGGER.error("Serialization of LogEvent failed.", ioe);
    }
    return baos.toByteArray();
  }
  
  public LogEvent toSerializable(LogEvent event)
  {
    return event;
  }
  
  @PluginFactory
  public static SerializedLayout createLayout()
  {
    return new SerializedLayout();
  }
  
  public byte[] getHeader()
  {
    return header;
  }
  
  public Map<String, String> getContentFormat()
  {
    return new HashMap();
  }
  
  public String getContentType()
  {
    return "application/octet-stream";
  }
  
  private class PrivateObjectOutputStream
    extends ObjectOutputStream
  {
    public PrivateObjectOutputStream(OutputStream os)
      throws IOException
    {
      super();
    }
    
    protected void writeStreamHeader() {}
  }
}
