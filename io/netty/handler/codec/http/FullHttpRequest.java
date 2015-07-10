package io.netty.handler.codec.http;

public abstract interface FullHttpRequest
  extends HttpRequest, FullHttpMessage
{
  public abstract FullHttpRequest copy();
  
  public abstract FullHttpRequest retain(int paramInt);
  
  public abstract FullHttpRequest retain();
  
  public abstract FullHttpRequest setProtocolVersion(HttpVersion paramHttpVersion);
  
  public abstract FullHttpRequest setMethod(HttpMethod paramHttpMethod);
  
  public abstract FullHttpRequest setUri(String paramString);
}
