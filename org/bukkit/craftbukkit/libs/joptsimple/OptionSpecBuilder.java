package org.bukkit.craftbukkit.libs.joptsimple;

import java.util.Collection;

public class OptionSpecBuilder
  extends NoArgumentOptionSpec
{
  private final OptionParser parser;
  
  OptionSpecBuilder(OptionParser parser, Collection<String> options, String description)
  {
    super(options, description);
    
    this.parser = parser;
    attachToParser();
  }
  
  private void attachToParser()
  {
    this.parser.recognize(this);
  }
  
  public ArgumentAcceptingOptionSpec<String> withRequiredArg()
  {
    ArgumentAcceptingOptionSpec<String> newSpec = new RequiredArgumentOptionSpec(options(), description());
    
    this.parser.recognize(newSpec);
    
    return newSpec;
  }
  
  public ArgumentAcceptingOptionSpec<String> withOptionalArg()
  {
    ArgumentAcceptingOptionSpec<String> newSpec = new OptionalArgumentOptionSpec(options(), description());
    
    this.parser.recognize(newSpec);
    
    return newSpec;
  }
}
