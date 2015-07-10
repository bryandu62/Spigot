package org.bukkit.craftbukkit.libs.joptsimple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.craftbukkit.libs.joptsimple.internal.AbbreviationMap;
import org.bukkit.craftbukkit.libs.joptsimple.util.KeyValuePair;

public class OptionParser
{
  private final AbbreviationMap<AbstractOptionSpec<?>> recognizedOptions;
  private OptionParserState state;
  private boolean posixlyCorrect;
  
  public OptionParser()
  {
    this.recognizedOptions = new AbbreviationMap();
    this.state = OptionParserState.moreOptions(false);
  }
  
  public OptionParser(String optionSpecification)
  {
    this();
    
    new OptionSpecTokenizer(optionSpecification).configure(this);
  }
  
  public OptionSpecBuilder accepts(String option)
  {
    return acceptsAll(Collections.singletonList(option));
  }
  
  public OptionSpecBuilder accepts(String option, String description)
  {
    return acceptsAll(Collections.singletonList(option), description);
  }
  
  public OptionSpecBuilder acceptsAll(Collection<String> options)
  {
    return acceptsAll(options, "");
  }
  
  public OptionSpecBuilder acceptsAll(Collection<String> options, String description)
  {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("need at least one option");
    }
    ParserRules.ensureLegalOptions(options);
    
    return new OptionSpecBuilder(this, options, description);
  }
  
  public void posixlyCorrect(boolean setting)
  {
    this.posixlyCorrect = setting;
    this.state = OptionParserState.moreOptions(setting);
  }
  
  boolean posixlyCorrect()
  {
    return this.posixlyCorrect;
  }
  
  public void recognizeAlternativeLongOptions(boolean recognize)
  {
    if (recognize) {
      recognize(new AlternativeLongOptionSpec());
    } else {
      this.recognizedOptions.remove(String.valueOf("W"));
    }
  }
  
  void recognize(AbstractOptionSpec<?> spec)
  {
    this.recognizedOptions.putAll(spec.options(), spec);
  }
  
  public void printHelpOn(OutputStream sink)
    throws IOException
  {
    printHelpOn(new OutputStreamWriter(sink));
  }
  
  public void printHelpOn(Writer sink)
    throws IOException
  {
    sink.write(new HelpFormatter().format(this.recognizedOptions.toJavaUtilMap()));
    sink.flush();
  }
  
  public OptionSet parse(String... arguments)
  {
    ArgumentList argumentList = new ArgumentList(arguments);
    OptionSet detected = new OptionSet(defaultValues());
    while (argumentList.hasMore()) {
      this.state.handleArgument(this, argumentList, detected);
    }
    reset();
    return detected;
  }
  
  void handleLongOptionToken(String candidate, ArgumentList arguments, OptionSet detected)
  {
    KeyValuePair optionAndArgument = parseLongOptionWithArgument(candidate);
    if (!isRecognized(optionAndArgument.key)) {
      throw OptionException.unrecognizedOption(optionAndArgument.key);
    }
    AbstractOptionSpec<?> optionSpec = specFor(optionAndArgument.key);
    optionSpec.handleOption(this, arguments, detected, optionAndArgument.value);
  }
  
  void handleShortOptionToken(String candidate, ArgumentList arguments, OptionSet detected)
  {
    KeyValuePair optionAndArgument = parseShortOptionWithArgument(candidate);
    if (isRecognized(optionAndArgument.key)) {
      specFor(optionAndArgument.key).handleOption(this, arguments, detected, optionAndArgument.value);
    } else {
      handleShortOptionCluster(candidate, arguments, detected);
    }
  }
  
  private void handleShortOptionCluster(String candidate, ArgumentList arguments, OptionSet detected)
  {
    char[] options = extractShortOptionsFrom(candidate);
    validateOptionCharacters(options);
    
    AbstractOptionSpec<?> optionSpec = specFor(options[0]);
    if ((optionSpec.acceptsArguments()) && (options.length > 1))
    {
      String detectedArgument = String.valueOf(options, 1, options.length - 1);
      optionSpec.handleOption(this, arguments, detected, detectedArgument);
    }
    else
    {
      for (char each : options) {
        specFor(each).handleOption(this, arguments, detected, null);
      }
    }
  }
  
  void noMoreOptions()
  {
    this.state = OptionParserState.noMoreOptions();
  }
  
  boolean looksLikeAnOption(String argument)
  {
    return (ParserRules.isShortOptionToken(argument)) || (ParserRules.isLongOptionToken(argument));
  }
  
  private boolean isRecognized(String option)
  {
    return this.recognizedOptions.contains(option);
  }
  
  private AbstractOptionSpec<?> specFor(char option)
  {
    return specFor(String.valueOf(option));
  }
  
  private AbstractOptionSpec<?> specFor(String option)
  {
    return (AbstractOptionSpec)this.recognizedOptions.get(option);
  }
  
  private void reset()
  {
    this.state = OptionParserState.moreOptions(this.posixlyCorrect);
  }
  
  private static char[] extractShortOptionsFrom(String argument)
  {
    char[] options = new char[argument.length() - 1];
    argument.getChars(1, argument.length(), options, 0);
    
    return options;
  }
  
  private void validateOptionCharacters(char[] options)
  {
    for (int i = 0; i < options.length; i++)
    {
      String option = String.valueOf(options[i]);
      if (!isRecognized(option)) {
        throw OptionException.unrecognizedOption(option);
      }
      if (specFor(option).acceptsArguments())
      {
        if (i > 0) {
          throw OptionException.illegalOptionCluster(option);
        }
        return;
      }
    }
  }
  
  private static KeyValuePair parseLongOptionWithArgument(String argument)
  {
    return KeyValuePair.valueOf(argument.substring(2));
  }
  
  private static KeyValuePair parseShortOptionWithArgument(String argument)
  {
    return KeyValuePair.valueOf(argument.substring(1));
  }
  
  private Map<String, List<?>> defaultValues()
  {
    Map<String, List<?>> defaults = new HashMap();
    for (Map.Entry<String, AbstractOptionSpec<?>> each : this.recognizedOptions.toJavaUtilMap().entrySet()) {
      defaults.put(each.getKey(), ((AbstractOptionSpec)each.getValue()).defaultValues());
    }
    return defaults;
  }
}
