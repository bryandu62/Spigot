package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForwardLogHandler
  extends ConsoleHandler
{
  private Map<String, Logger> cachedLoggers = new ConcurrentHashMap();
  
  private Logger getLogger(String name)
  {
    Logger logger = (Logger)this.cachedLoggers.get(name);
    if (logger == null)
    {
      logger = LogManager.getLogger(name);
      this.cachedLoggers.put(name, logger);
    }
    return logger;
  }
  
  public void publish(LogRecord record)
  {
    Logger logger = getLogger(record.getLoggerName());
    Throwable exception = record.getThrown();
    Level level = record.getLevel();
    String message = getFormatter().formatMessage(record);
    if (level == Level.SEVERE) {
      logger.error(message, exception);
    } else if (level == Level.WARNING) {
      logger.warn(message, exception);
    } else if (level == Level.INFO) {
      logger.info(message, exception);
    } else if (level == Level.CONFIG) {
      logger.debug(message, exception);
    } else {
      logger.trace(message, exception);
    }
  }
  
  public void flush() {}
  
  public void close()
    throws SecurityException
  {}
}
