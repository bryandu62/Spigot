package io.netty.handler.codec.http;

public abstract interface HttpResponse
  extends HttpMessage
{
  public abstract HttpResponseStatus getStatus();
  
  public abstract HttpResponse setStatus(HttpResponseStatus paramHttpResponseStatus);
  
  public abstract HttpResponse setProtocolVersion(HttpVersion paramHttpVersion);
}
