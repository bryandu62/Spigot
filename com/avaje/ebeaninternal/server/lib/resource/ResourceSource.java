package com.avaje.ebeaninternal.server.lib.resource;

import java.io.IOException;

public abstract interface ResourceSource
{
  public abstract String getRealPath();
  
  public abstract ResourceContent getContent(String paramString);
  
  public abstract String readString(ResourceContent paramResourceContent, int paramInt)
    throws IOException;
  
  public abstract byte[] readBytes(ResourceContent paramResourceContent, int paramInt)
    throws IOException;
}
