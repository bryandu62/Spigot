package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name="FileDatePatternConverter", category="FileConverter")
@ConverterKeys({"d", "date"})
public final class FileDatePatternConverter
{
  public static PatternConverter newInstance(String[] options)
  {
    if ((options == null) || (options.length == 0)) {
      return DatePatternConverter.newInstance(new String[] { "yyyy-MM-dd" });
    }
    return DatePatternConverter.newInstance(options);
  }
}
