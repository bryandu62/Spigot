package io.netty.handler.codec.http;

public abstract interface FullHttpResponse
  extends HttpResponse, FullHttpMessage
{
  public abstract FullHttpResponse copy();
  
  public abstract FullHttpResponse retain(int paramInt);
  
  public abstract FullHttpResponse retain();
  
  public abstract FullHttpResponse setProtocolVersion(HttpVersion paramHttpVersion);
  
  public abstract FullHttpResponse setStatus(HttpResponseStatus paramHttpResponseStatus);
}
