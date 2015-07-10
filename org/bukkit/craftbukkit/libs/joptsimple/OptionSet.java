package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Objects;

public class OptionSet
{
  private final Map<String, AbstractOptionSpec<?>> detectedOptions;
  private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
  private final List<String> nonOptionArguments;
  private final Map<String, List<?>> defaultValues;
  
  OptionSet(Map<String, List<?>> defaults)
  {
    this.detectedOptions = new HashMap();
    this.optionsToArguments = new IdentityHashMap();
    this.nonOptionArguments = new ArrayList();
    this.defaultValues = new HashMap(defaults);
  }
  
  public boolean has(String option)
  {
    return this.detectedOptions.containsKey(option);
  }
  
  public boolean has(OptionSpec<?> option)
  {
    return this.optionsToArguments.containsKey(option);
  }
  
  public boolean hasArgument(String option)
  {
    AbstractOptionSpec<?> spec = (AbstractOptionSpec)this.detectedOptions.get(option);
    return (spec != null) && (hasArgument(spec));
  }
  
  public boolean hasArgument(OptionSpec<?> option)
  {
    Objects.ensureNotNull(option);
    
    List<String> values = (List)this.optionsToArguments.get(option);
    return (values != null) && (!values.isEmpty());
  }
  
  public Object valueOf(String option)
  {
    Objects.ensureNotNull(option);
    
    AbstractOptionSpec<?> spec = (AbstractOptionSpec)this.detectedOptions.get(option);
    if (spec == null)
    {
      List<?> defaults = defaultValuesFor(option);
      return defaults.isEmpty() ? null : defaults.get(0);
    }
    return valueOf(spec);
  }
  
  public <V> V valueOf(OptionSpec<V> option)
  {
    Objects.ensureNotNull(option);
    
    List<V> values = valuesOf(option);
    switch (values.size())
    {
    case 0: 
      return null;
    case 1: 
      return (V)values.get(0);
    }
    throw new MultipleArgumentsForOptionException(option.options());
  }
  
  public List<?> valuesOf(String option)
  {
    Objects.ensureNotNull(option);
    
    AbstractOptionSpec<?> spec = (AbstractOptionSpec)this.detectedOptions.get(option);
    return spec == null ? defaultValuesFor(option) : valuesOf(spec);
  }
  
  public <V> List<V> valuesOf(OptionSpec<V> option)
  {
    Objects.ensureNotNull(option);
    
    List<String> values = (List)this.optionsToArguments.get(option);
    if ((values == null) || (values.isEmpty())) {
      return defaultValueFor(option);
    }
    AbstractOptionSpec<V> spec = (AbstractOptionSpec)option;
    List<V> convertedValues = new ArrayList();
    for (String each : values) {
      convertedValues.add(spec.convert(each));
    }
    return Collections.unmodifiableList(convertedValues);
  }
  
  public List<String> nonOptionArguments()
  {
    return Collections.unmodifiableList(this.nonOptionArguments);
  }
  
  void add(AbstractOptionSpec<?> option)
  {
    addWithArgument(option, null);
  }
  
  void addWithArgument(AbstractOptionSpec<?> option, String argument)
  {
    for (String each : option.options()) {
      this.detectedOptions.put(each, option);
    }
    List<String> optionArguments = (List)this.optionsToArguments.get(option);
    if (optionArguments == null)
    {
      optionArguments = new ArrayList();
      this.optionsToArguments.put(option, optionArguments);
    }
    if (argument != null) {
      optionArguments.add(argument);
    }
  }
  
  void addNonOptionArgument(String argument)
  {
    this.nonOptionArguments.add(argument);
  }
  
  public boolean equals(Object that)
  {
    if (this == that) {
      return true;
    }
    if ((that == null) || (!getClass().equals(that.getClass()))) {
      return false;
    }
    OptionSet other = (OptionSet)that;
    Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap(this.optionsToArguments);
    
    Map<AbstractOptionSpec<?>, List<String>> otherOptionsToArguments = new HashMap(other.optionsToArguments);
    
    return (this.detectedOptions.equals(other.detectedOptions)) && (thisOptionsToArguments.equals(otherOptionsToArguments)) && (this.nonOptionArguments.equals(other.nonOptionArguments()));
  }
  
  public int hashCode()
  {
    Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap(this.optionsToArguments);
    
    return this.detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode() ^ this.nonOptionArguments.hashCode();
  }
  
  private <V> List<V> defaultValuesFor(String option)
  {
    if (this.defaultValues.containsKey(option))
    {
      List<V> defaults = (List)this.defaultValues.get(option);
      return defaults;
    }
    return Collections.emptyList();
  }
  
  private <V> List<V> defaultValueFor(OptionSpec<V> option)
  {
    return defaultValuesFor((String)option.options().iterator().next());
  }
}
