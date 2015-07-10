package com.avaje.ebeaninternal.server.text.json;

import com.avaje.ebean.text.TextException;
import java.io.IOException;
import java.io.Writer;

public class WriteJsonBufferWriter
  implements WriteJsonBuffer
{
  private final Writer buffer;
  
  public WriteJsonBufferWriter(Writer buffer)
  {
    this.buffer = buffer;
  }
  
  public WriteJsonBufferWriter append(String content)
  {
    try
    {
      this.buffer.write(content);
      return this;
    }
    catch (IOException e)
    {
      throw new TextException(e);
    }
  }
  
  public WriteJsonBufferWriter append(CharSequence csq)
    throws IOException
  {
    return append(csq, 0, csq.length());
  }
  
  public WriteJsonBufferWriter append(CharSequence csq, int start, int end)
    throws IOException
  {
    for (int i = start; i < end; i++) {
      this.buffer.append(csq.charAt(i));
    }
    return this;
  }
  
  public WriteJsonBufferWriter append(char c)
    throws IOException
  {
    try
    {
      this.buffer.write(c);
      return this;
    }
    catch (IOException e)
    {
      throw new TextException(e);
    }
  }
}
