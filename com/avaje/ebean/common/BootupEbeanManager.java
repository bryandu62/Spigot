package com.avaje.ebean.common;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;

public abstract interface BootupEbeanManager
{
  public abstract EbeanServer createServer(ServerConfig paramServerConfig);
  
  public abstract EbeanServer createServer(String paramString);
  
  public abstract void shutdown();
}
