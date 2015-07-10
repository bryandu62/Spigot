package org.apache.logging.log4j;

import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.AbstractLoggerWrapper;

public final class EventLogger
{
  private static final String NAME = "EventLogger";
  public static final Marker EVENT_MARKER = MarkerManager.getMarker("EVENT");
  private static final String FQCN = EventLogger.class.getName();
  private static AbstractLoggerWrapper loggerWrapper;
  
  static
  {
    Logger eventLogger = LogManager.getLogger("EventLogger");
    if (!(eventLogger instanceof AbstractLogger)) {
      throw new LoggingException("Logger returned must be based on AbstractLogger");
    }
    loggerWrapper = new AbstractLoggerWrapper((AbstractLogger)eventLogger, "EventLogger", null);
  }
  
  public static void logEvent(StructuredDataMessage msg)
  {
    loggerWrapper.log(EVENT_MARKER, FQCN, Level.OFF, msg, null);
  }
  
  public static void logEvent(StructuredDataMessage msg, Level level)
  {
    loggerWrapper.log(EVENT_MARKER, FQCN, level, msg, null);
  }
}
