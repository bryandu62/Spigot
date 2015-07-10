package io.netty.handler.codec.http;

public abstract interface HttpRequest
  extends HttpMessage
{
  public abstract HttpMethod getMethod();
  
  public abstract HttpRequest setMethod(HttpMethod paramHttpMethod);
  
  public abstract String getUri();
  
  public abstract HttpRequest setUri(String paramString);
  
  public abstract HttpRequest setProtocolVersion(HttpVersion paramHttpVersion);
}
