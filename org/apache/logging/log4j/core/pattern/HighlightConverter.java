package org.apache.logging.log4j.core.pattern;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="highlight", category="Converter")
@ConverterKeys({"highlight"})
public final class HighlightConverter
  extends LogEventPatternConverter
{
  private static final EnumMap<Level, String> DEFAULT_STYLES = new EnumMap(Level.class);
  private static final EnumMap<Level, String> LOGBACK_STYLES = new EnumMap(Level.class);
  private static final String STYLE_KEY = "STYLE";
  private static final String STYLE_KEY_DEFAULT = "DEFAULT";
  private static final String STYLE_KEY_LOGBACK = "LOGBACK";
  private static final Map<String, EnumMap<Level, String>> STYLES = new HashMap();
  private final EnumMap<Level, String> levelStyles;
  private final List<PatternFormatter> patternFormatters;
  
  static
  {
    DEFAULT_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
    DEFAULT_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
    DEFAULT_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "YELLOW" }));
    DEFAULT_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "GREEN" }));
    DEFAULT_STYLES.put(Level.DEBUG, AnsiEscape.createSequence(new String[] { "CYAN" }));
    DEFAULT_STYLES.put(Level.TRACE, AnsiEscape.createSequence(new String[] { "BLACK" }));
    
    LOGBACK_STYLES.put(Level.FATAL, AnsiEscape.createSequence(new String[] { "BLINK", "BRIGHT", "RED" }));
    LOGBACK_STYLES.put(Level.ERROR, AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
    LOGBACK_STYLES.put(Level.WARN, AnsiEscape.createSequence(new String[] { "RED" }));
    LOGBACK_STYLES.put(Level.INFO, AnsiEscape.createSequence(new String[] { "BLUE" }));
    LOGBACK_STYLES.put(Level.DEBUG, AnsiEscape.createSequence((String[])null));
    LOGBACK_STYLES.put(Level.TRACE, AnsiEscape.createSequence((String[])null));
    
    STYLES.put("DEFAULT", DEFAULT_STYLES);
    STYLES.put("LOGBACK", LOGBACK_STYLES);
  }
  
  private static EnumMap<Level, String> createLevelStyleMap(String[] options)
  {
    if (options.length < 2) {
      return DEFAULT_STYLES;
    }
    Map<String, String> styles = AnsiEscape.createMap(options[1], new String[] { "STYLE" });
    EnumMap<Level, String> levelStyles = new EnumMap(DEFAULT_STYLES);
    for (Map.Entry<String, String> entry : styles.entrySet())
    {
      String key = ((String)entry.getKey()).toUpperCase(Locale.ENGLISH);
      String value = (String)entry.getValue();
      if ("STYLE".equalsIgnoreCase(key))
      {
        EnumMap<Level, String> enumMap = (EnumMap)STYLES.get(value.toUpperCase(Locale.ENGLISH));
        if (enumMap == null) {
          LOGGER.error("Unknown level style: " + value + ". Use one of " + Arrays.toString(STYLES.keySet().toArray()));
        } else {
          levelStyles.putAll(enumMap);
        }
      }
      else
      {
        Level level = Level.valueOf(key);
        if (level == null) {
          LOGGER.error("Unknown level name: " + key + ". Use one of " + Arrays.toString(DEFAULT_STYLES.keySet().toArray()));
        } else {
          levelStyles.put(level, value);
        }
      }
    }
    return levelStyles;
  }
  
  public static HighlightConverter newInstance(Configuration config, String[] options)
  {
    if (options.length < 1)
    {
      LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
      return null;
    }
    if (options[0] == null)
    {
      LOGGER.error("No pattern supplied on style");
      return null;
    }
    PatternParser parser = PatternLayout.createPatternParser(config);
    List<PatternFormatter> formatters = parser.parse(options[0]);
    return new HighlightConverter(formatters, createLevelStyleMap(options));
  }
  
  private HighlightConverter(List<PatternFormatter> patternFormatters, EnumMap<Level, String> levelStyles)
  {
    super("style", "style");
    this.patternFormatters = patternFormatters;
    this.levelStyles = levelStyles;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo)
  {
    StringBuilder buf = new StringBuilder();
    for (PatternFormatter formatter : this.patternFormatters) {
      formatter.format(event, buf);
    }
    if (buf.length() > 0) {
      toAppendTo.append((String)this.levelStyles.get(event.getLevel())).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
    }
  }
  
  public boolean handlesThrowable()
  {
    for (PatternFormatter formatter : this.patternFormatters) {
      if (formatter.handlesThrowable()) {
        return true;
      }
    }
    return false;
  }
}
