package io.netty.handler.codec.http.multipart;

import io.netty.util.ReferenceCounted;

public abstract interface InterfaceHttpData
  extends Comparable<InterfaceHttpData>, ReferenceCounted
{
  public abstract String getName();
  
  public abstract HttpDataType getHttpDataType();
  
  public static enum HttpDataType
  {
    Attribute,  FileUpload,  InternalAttribute;
    
    private HttpDataType() {}
  }
}
