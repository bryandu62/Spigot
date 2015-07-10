package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;
import java.util.Map.Entry;

public abstract class DefaultHttpMessage
  extends DefaultHttpObject
  implements HttpMessage
{
  private HttpVersion version;
  private final HttpHeaders headers;
  
  protected DefaultHttpMessage(HttpVersion version)
  {
    this(version, true);
  }
  
  protected DefaultHttpMessage(HttpVersion version, boolean validate)
  {
    if (version == null) {
      throw new NullPointerException("version");
    }
    this.version = version;
    this.headers = new DefaultHttpHeaders(validate);
  }
  
  public HttpHeaders headers()
  {
    return this.headers;
  }
  
  public HttpVersion getProtocolVersion()
  {
    return this.version;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append("(version: ");
    buf.append(getProtocolVersion().text());
    buf.append(", keepAlive: ");
    buf.append(HttpHeaders.isKeepAlive(this));
    buf.append(')');
    buf.append(StringUtil.NEWLINE);
    appendHeaders(buf);
    
    buf.setLength(buf.length() - StringUtil.NEWLINE.length());
    return buf.toString();
  }
  
  public HttpMessage setProtocolVersion(HttpVersion version)
  {
    if (version == null) {
      throw new NullPointerException("version");
    }
    this.version = version;
    return this;
  }
  
  void appendHeaders(StringBuilder buf)
  {
    for (Map.Entry<String, String> e : headers())
    {
      buf.append((String)e.getKey());
      buf.append(": ");
      buf.append((String)e.getValue());
      buf.append(StringUtil.NEWLINE);
    }
  }
}
