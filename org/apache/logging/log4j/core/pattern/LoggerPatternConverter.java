package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name="LoggerPatternConverter", category="Converter")
@ConverterKeys({"c", "logger"})
public final class LoggerPatternConverter
  extends NamePatternConverter
{
  private static final LoggerPatternConverter INSTANCE = new LoggerPatternConverter(null);
  
  private LoggerPatternConverter(String[] options)
  {
    super("Logger", "logger", options);
  }
  
  public static LoggerPatternConverter newInstance(String[] options)
  {
    if ((options == null) || (options.length == 0)) {
      return INSTANCE;
    }
    return new LoggerPatternConverter(options);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo)
  {
    toAppendTo.append(abbreviate(event.getLoggerName()));
  }
}
