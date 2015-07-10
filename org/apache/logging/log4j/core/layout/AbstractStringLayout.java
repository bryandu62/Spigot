package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractStringLayout
  extends AbstractLayout<String>
{
  private final Charset charset;
  
  protected AbstractStringLayout(Charset charset)
  {
    this.charset = charset;
  }
  
  public byte[] toByteArray(LogEvent event)
  {
    return ((String)toSerializable(event)).getBytes(this.charset);
  }
  
  public String getContentType()
  {
    return "text/plain";
  }
  
  protected Charset getCharset()
  {
    return this.charset;
  }
}
