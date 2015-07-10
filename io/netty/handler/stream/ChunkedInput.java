package io.netty.handler.stream;

import io.netty.channel.ChannelHandlerContext;

public abstract interface ChunkedInput<B>
{
  public abstract boolean isEndOfInput()
    throws Exception;
  
  public abstract void close()
    throws Exception;
  
  public abstract B readChunk(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
}
