package io.netty.handler.codec.http;

public abstract interface FullHttpMessage
  extends HttpMessage, LastHttpContent
{
  public abstract FullHttpMessage copy();
  
  public abstract FullHttpMessage retain(int paramInt);
  
  public abstract FullHttpMessage retain();
}
