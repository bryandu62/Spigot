package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

abstract class AbstractOptionSpec<V>
  implements OptionSpec<V>
{
  private final List<String> options = new ArrayList();
  private final String description;
  
  protected AbstractOptionSpec(String option)
  {
    this(Collections.singletonList(option), "");
  }
  
  protected AbstractOptionSpec(Collection<String> options, String description)
  {
    arrangeOptions(options);
    
    this.description = description;
  }
  
  public final Collection<String> options()
  {
    return Collections.unmodifiableCollection(this.options);
  }
  
  public final List<V> values(OptionSet detectedOptions)
  {
    return detectedOptions.valuesOf(this);
  }
  
  public final V value(OptionSet detectedOptions)
  {
    return (V)detectedOptions.valueOf(this);
  }
  
  abstract List<V> defaultValues();
  
  String description()
  {
    return this.description;
  }
  
  protected abstract V convert(String paramString);
  
  abstract void handleOption(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet, String paramString);
  
  abstract boolean acceptsArguments();
  
  abstract boolean requiresArgument();
  
  abstract void accept(OptionSpecVisitor paramOptionSpecVisitor);
  
  private void arrangeOptions(Collection<String> unarranged)
  {
    if (unarranged.size() == 1)
    {
      this.options.addAll(unarranged);
      return;
    }
    List<String> shortOptions = new ArrayList();
    List<String> longOptions = new ArrayList();
    for (String each : unarranged) {
      if (each.length() == 1) {
        shortOptions.add(each);
      } else {
        longOptions.add(each);
      }
    }
    Collections.sort(shortOptions);
    Collections.sort(longOptions);
    
    this.options.addAll(shortOptions);
    this.options.addAll(longOptions);
  }
  
  public boolean equals(Object that)
  {
    if (!(that instanceof AbstractOptionSpec)) {
      return false;
    }
    AbstractOptionSpec<?> other = (AbstractOptionSpec)that;
    return this.options.equals(other.options);
  }
  
  public int hashCode()
  {
    return this.options.hashCode();
  }
  
  public String toString()
  {
    return this.options.toString();
  }
}
