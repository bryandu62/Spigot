package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;

public abstract interface HttpObject
{
  public abstract DecoderResult getDecoderResult();
  
  public abstract void setDecoderResult(DecoderResult paramDecoderResult);
}
