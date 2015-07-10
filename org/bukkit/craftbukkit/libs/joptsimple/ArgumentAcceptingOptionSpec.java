package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Objects;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Reflection;
import org.bukkit.craftbukkit.libs.joptsimple.internal.ReflectionException;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

public abstract class ArgumentAcceptingOptionSpec<V>
  extends AbstractOptionSpec<V>
{
  private static final char NIL_VALUE_SEPARATOR = '\000';
  private final boolean argumentRequired;
  private ValueConverter<V> converter;
  private String argumentDescription = "";
  private String valueSeparator = String.valueOf('\000');
  private final List<V> defaultValues = new ArrayList();
  
  ArgumentAcceptingOptionSpec(String option, boolean argumentRequired)
  {
    super(option);
    
    this.argumentRequired = argumentRequired;
  }
  
  ArgumentAcceptingOptionSpec(Collection<String> options, boolean argumentRequired, String description)
  {
    super(options, description);
    
    this.argumentRequired = argumentRequired;
  }
  
  public final <T> ArgumentAcceptingOptionSpec<T> ofType(Class<T> argumentType)
  {
    return withValuesConvertedBy(Reflection.findConverter(argumentType));
  }
  
  public final <T> ArgumentAcceptingOptionSpec<T> withValuesConvertedBy(ValueConverter<T> aConverter)
  {
    if (aConverter == null) {
      throw new NullPointerException("illegal null converter");
    }
    this.converter = aConverter;
    return this;
  }
  
  public final ArgumentAcceptingOptionSpec<V> describedAs(String description)
  {
    this.argumentDescription = description;
    return this;
  }
  
  public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(char separator)
  {
    if (separator == 0) {
      throw new IllegalArgumentException("cannot use U+0000 as separator");
    }
    this.valueSeparator = String.valueOf(separator);
    return this;
  }
  
  public ArgumentAcceptingOptionSpec<V> defaultsTo(V value, V... values)
  {
    addDefaultValue(value);
    for (V each : values) {
      addDefaultValue(each);
    }
    return this;
  }
  
  private void addDefaultValue(V value)
  {
    Objects.ensureNotNull(value);
    this.defaultValues.add(value);
  }
  
  final void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument)
  {
    if (Strings.isNullOrEmpty(detectedArgument)) {
      detectOptionArgument(parser, arguments, detectedOptions);
    } else {
      addArguments(detectedOptions, detectedArgument);
    }
  }
  
  protected void addArguments(OptionSet detectedOptions, String detectedArgument)
  {
    StringTokenizer lexer = new StringTokenizer(detectedArgument, this.valueSeparator);
    if (!lexer.hasMoreTokens()) {
      detectedOptions.addWithArgument(this, detectedArgument);
    } else {
      while (lexer.hasMoreTokens()) {
        detectedOptions.addWithArgument(this, lexer.nextToken());
      }
    }
  }
  
  protected abstract void detectOptionArgument(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet);
  
  protected final V convert(String argument)
  {
    if (this.converter == null) {
      return argument;
    }
    try
    {
      return (V)this.converter.convert(argument);
    }
    catch (ReflectionException ex)
    {
      throw new OptionArgumentConversionException(options(), argument, this.converter.valueType(), ex);
    }
    catch (ValueConversionException ex)
    {
      throw new OptionArgumentConversionException(options(), argument, this.converter.valueType(), ex);
    }
  }
  
  protected boolean canConvertArgument(String argument)
  {
    StringTokenizer lexer = new StringTokenizer(argument, this.valueSeparator);
    try
    {
      while (lexer.hasMoreTokens()) {
        convert(lexer.nextToken());
      }
      return true;
    }
    catch (OptionException ignored) {}
    return false;
  }
  
  protected boolean isArgumentOfNumberType()
  {
    return (this.converter != null) && (Number.class.isAssignableFrom(this.converter.valueType()));
  }
  
  boolean acceptsArguments()
  {
    return true;
  }
  
  boolean requiresArgument()
  {
    return this.argumentRequired;
  }
  
  String argumentDescription()
  {
    return this.argumentDescription;
  }
  
  String typeIndicator()
  {
    if (this.converter == null) {
      return null;
    }
    String pattern = this.converter.valuePattern();
    return pattern == null ? this.converter.valueType().getName() : pattern;
  }
  
  List<V> defaultValues()
  {
    return Collections.unmodifiableList(this.defaultValues);
  }
  
  public boolean equals(Object that)
  {
    if (!super.equals(that)) {
      return false;
    }
    ArgumentAcceptingOptionSpec<?> other = (ArgumentAcceptingOptionSpec)that;
    return requiresArgument() == other.requiresArgument();
  }
  
  public int hashCode()
  {
    return super.hashCode() ^ (this.argumentRequired ? 0 : 1);
  }
}
