package io.netty.handler.codec.http.multipart;

import java.io.IOException;

public abstract interface Attribute
  extends HttpData
{
  public abstract String getValue()
    throws IOException;
  
  public abstract void setValue(String paramString)
    throws IOException;
  
  public abstract Attribute copy();
  
  public abstract Attribute duplicate();
  
  public abstract Attribute retain();
  
  public abstract Attribute retain(int paramInt);
}
