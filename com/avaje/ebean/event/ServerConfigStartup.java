package com.avaje.ebean.event;

import com.avaje.ebean.config.ServerConfig;

public abstract interface ServerConfigStartup
{
  public abstract void onStart(ServerConfig paramServerConfig);
}
