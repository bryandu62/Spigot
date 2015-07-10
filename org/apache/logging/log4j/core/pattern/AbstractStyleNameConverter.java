package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

public abstract class AbstractStyleNameConverter
  extends LogEventPatternConverter
{
  private final List<PatternFormatter> formatters;
  private final String style;
  
  protected AbstractStyleNameConverter(String name, List<PatternFormatter> formatters, String styling)
  {
    super(name, "style");
    this.formatters = formatters;
    this.style = styling;
  }
  
  @Plugin(name="black", category="Converter")
  @ConverterKeys({"black"})
  public static final class Black
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "black";
    
    public Black(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static Black newInstance(Configuration config, String[] options)
    {
      return (Black)newInstance(Black.class, "black", config, options);
    }
  }
  
  @Plugin(name="blue", category="Converter")
  @ConverterKeys({"blue"})
  public static final class Blue
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "blue";
    
    public Blue(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static Blue newInstance(Configuration config, String[] options)
    {
      return (Blue)newInstance(Blue.class, "blue", config, options);
    }
  }
  
  @Plugin(name="cyan", category="Converter")
  @ConverterKeys({"cyan"})
  public static final class Cyan
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "cyan";
    
    public Cyan(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static Cyan newInstance(Configuration config, String[] options)
    {
      return (Cyan)newInstance(Cyan.class, "cyan", config, options);
    }
  }
  
  @Plugin(name="green", category="Converter")
  @ConverterKeys({"green"})
  public static final class Green
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "green";
    
    public Green(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static Green newInstance(Configuration config, String[] options)
    {
      return (Green)newInstance(Green.class, "green", config, options);
    }
  }
  
  @Plugin(name="magenta", category="Converter")
  @ConverterKeys({"magenta"})
  public static final class Magenta
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "magenta";
    
    public Magenta(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static Magenta newInstance(Configuration config, String[] options)
    {
      return (Magenta)newInstance(Magenta.class, "magenta", config, options);
    }
  }
  
  @Plugin(name="red", category="Converter")
  @ConverterKeys({"red"})
  public static final class Red
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "red";
    
    public Red(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static Red newInstance(Configuration config, String[] options)
    {
      return (Red)newInstance(Red.class, "red", config, options);
    }
  }
  
  @Plugin(name="white", category="Converter")
  @ConverterKeys({"white"})
  public static final class White
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "white";
    
    public White(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static White newInstance(Configuration config, String[] options)
    {
      return (White)newInstance(White.class, "white", config, options);
    }
  }
  
  @Plugin(name="yellow", category="Converter")
  @ConverterKeys({"yellow"})
  public static final class Yellow
    extends AbstractStyleNameConverter
  {
    protected static final String NAME = "yellow";
    
    public Yellow(List<PatternFormatter> formatters, String styling)
    {
      super(formatters, styling);
    }
    
    public static Yellow newInstance(Configuration config, String[] options)
    {
      return (Yellow)newInstance(Yellow.class, "yellow", config, options);
    }
  }
  
  protected static <T extends AbstractStyleNameConverter> T newInstance(Class<T> asnConverterClass, String name, Configuration config, String[] options)
  {
    List<PatternFormatter> formatters = toPatternFormatterList(config, options);
    if (formatters == null) {
      return null;
    }
    try
    {
      Constructor<T> constructor = asnConverterClass.getConstructor(new Class[] { List.class, String.class });
      return (AbstractStyleNameConverter)constructor.newInstance(new Object[] { formatters, AnsiEscape.createSequence(new String[] { name }) });
    }
    catch (SecurityException e)
    {
      LOGGER.error(e.toString(), e);
    }
    catch (NoSuchMethodException e)
    {
      LOGGER.error(e.toString(), e);
    }
    catch (IllegalArgumentException e)
    {
      LOGGER.error(e.toString(), e);
    }
    catch (InstantiationException e)
    {
      LOGGER.error(e.toString(), e);
    }
    catch (IllegalAccessException e)
    {
      LOGGER.error(e.toString(), e);
    }
    catch (InvocationTargetException e)
    {
      LOGGER.error(e.toString(), e);
    }
    return null;
  }
  
  private static List<PatternFormatter> toPatternFormatterList(Configuration config, String[] options)
  {
    if ((options.length == 0) || (options[0] == null))
    {
      LOGGER.error("No pattern supplied on style for config=" + config);
      return null;
    }
    PatternParser parser = PatternLayout.createPatternParser(config);
    if (parser == null)
    {
      LOGGER.error("No PatternParser created for config=" + config + ", options=" + Arrays.toString(options));
      return null;
    }
    return parser.parse(options[0]);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo)
  {
    StringBuilder buf = new StringBuilder();
    for (PatternFormatter formatter : this.formatters) {
      formatter.format(event, buf);
    }
    if (buf.length() > 0) {
      toAppendTo.append(this.style).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
    }
  }
}
