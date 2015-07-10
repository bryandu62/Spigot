package org.apache.logging.log4j.core;

import org.apache.logging.log4j.LogManager;

public class AbstractServer
{
  private final LoggerContext context;
  
  protected AbstractServer()
  {
    this.context = ((LoggerContext)LogManager.getContext(false));
  }
  
  protected void log(LogEvent event)
  {
    Logger logger = this.context.getLogger(event.getLoggerName());
    if (logger.config.filter(event.getLevel(), event.getMarker(), event.getMessage(), event.getThrown())) {
      logger.config.logEvent(event);
    }
  }
}
