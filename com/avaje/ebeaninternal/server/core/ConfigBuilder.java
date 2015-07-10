package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.config.ServerConfig;

public class ConfigBuilder
{
  public ServerConfig build(String serverName)
  {
    ServerConfig config = new ServerConfig();
    config.setName(serverName);
    
    config.loadFromProperties();
    
    return config;
  }
}
