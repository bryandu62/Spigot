package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Constants;

@Plugin(name="LineSeparatorPatternConverter", category="Converter")
@ConverterKeys({"n"})
public final class LineSeparatorPatternConverter
  extends LogEventPatternConverter
{
  private static final LineSeparatorPatternConverter INSTANCE = new LineSeparatorPatternConverter();
  private final String lineSep;
  
  private LineSeparatorPatternConverter()
  {
    super("Line Sep", "lineSep");
    this.lineSep = Constants.LINE_SEP;
  }
  
  public static LineSeparatorPatternConverter newInstance(String[] options)
  {
    return INSTANCE;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo)
  {
    toAppendTo.append(this.lineSep);
  }
}
