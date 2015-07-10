package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

class NoArgumentOptionSpec
  extends AbstractOptionSpec<Void>
{
  NoArgumentOptionSpec(String option)
  {
    this(Collections.singletonList(option), "");
  }
  
  NoArgumentOptionSpec(Collection<String> options, String description)
  {
    super(options, description);
  }
  
  void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument)
  {
    detectedOptions.add(this);
  }
  
  boolean acceptsArguments()
  {
    return false;
  }
  
  boolean requiresArgument()
  {
    return false;
  }
  
  void accept(OptionSpecVisitor visitor)
  {
    visitor.visit(this);
  }
  
  protected Void convert(String argument)
  {
    return null;
  }
  
  List<Void> defaultValues()
  {
    return Collections.emptyList();
  }
}
