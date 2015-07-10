package io.netty.handler.codec.http;

import io.netty.buffer.ByteBufHolder;

public abstract interface HttpContent
  extends HttpObject, ByteBufHolder
{
  public abstract HttpContent copy();
  
  public abstract HttpContent duplicate();
  
  public abstract HttpContent retain();
  
  public abstract HttpContent retain(int paramInt);
}
