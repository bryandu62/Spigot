package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.server.lib.ShutdownManager;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class ServletContextListener
  implements javax.servlet.ServletContextListener
{
  private static final Logger logger = Logger.getLogger(ServletContextListener.class.getName());
  
  public void contextDestroyed(ServletContextEvent event) {}
  
  public void contextInitialized(ServletContextEvent event)
  {
    try
    {
      ServletContext servletContext = event.getServletContext();
      GlobalProperties.setServletContext(servletContext);
      if (servletContext != null)
      {
        String servletRealPath = servletContext.getRealPath("");
        GlobalProperties.put("servlet.realpath", servletRealPath);
        logger.info("servlet.realpath=[" + servletRealPath + "]");
      }
      Ebean.getServer(null);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
