package com.avaje.ebean;

import com.avaje.ebean.common.BootupEbeanManager;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.ClassUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

public class EbeanServerFactory
{
  private static final Logger logger = Logger.getLogger(EbeanServerFactory.class.getName());
  private static BootupEbeanManager serverFactory = createServerFactory();
  
  public static EbeanServer create(String name)
  {
    EbeanServer server = serverFactory.createServer(name);
    
    return server;
  }
  
  public static EbeanServer create(ServerConfig config)
  {
    if (config.getName() == null) {
      throw new PersistenceException("The name is null (it is required)");
    }
    EbeanServer server = serverFactory.createServer(config);
    if (config.isDefaultServer()) {
      GlobalProperties.setSkipPrimaryServer(true);
    }
    if (config.isRegister()) {
      Ebean.register(server, config.isDefaultServer());
    }
    return server;
  }
  
  private static BootupEbeanManager createServerFactory()
  {
    String dflt = "com.avaje.ebeaninternal.server.core.DefaultServerFactory";
    String implClassName = GlobalProperties.get("ebean.serverfactory", dflt);
    
    int delaySecs = GlobalProperties.getInt("ebean.start.delay", 0);
    if (delaySecs > 0) {
      try
      {
        String m = "Ebean sleeping " + delaySecs + " seconds due to ebean.start.delay";
        logger.log(Level.INFO, m);
        Thread.sleep(delaySecs * 1000);
      }
      catch (InterruptedException e)
      {
        String m = "Interrupting debug.start.delay of " + delaySecs;
        logger.log(Level.SEVERE, m, e);
      }
    }
    try
    {
      return (BootupEbeanManager)ClassUtil.newInstance(implClassName);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
}
