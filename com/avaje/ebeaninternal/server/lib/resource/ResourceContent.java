package com.avaje.ebeaninternal.server.lib.resource;

import java.io.IOException;
import java.io.InputStream;

public abstract interface ResourceContent
{
  public abstract String getName();
  
  public abstract long size();
  
  public abstract long lastModified();
  
  public abstract InputStream getInputStream()
    throws IOException;
}
