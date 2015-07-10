package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractLayout<T extends Serializable>
  implements Layout<T>
{
  protected static final Logger LOGGER = ;
  protected byte[] header;
  protected byte[] footer;
  
  public byte[] getHeader()
  {
    return this.header;
  }
  
  public void setHeader(byte[] header)
  {
    this.header = header;
  }
  
  public byte[] getFooter()
  {
    return this.footer;
  }
  
  public void setFooter(byte[] footer)
  {
    this.footer = footer;
  }
}
