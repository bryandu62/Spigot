package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name="XMLConfigurationFactory", category="ConfigurationFactory")
@Order(5)
public class XMLConfigurationFactory
  extends ConfigurationFactory
{
  public static final String[] SUFFIXES = { ".xml", "*" };
  
  public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource source)
  {
    return new XMLConfiguration(source);
  }
  
  public String[] getSupportedTypes()
  {
    return SUFFIXES;
  }
}
