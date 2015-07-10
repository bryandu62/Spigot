package org.apache.logging.log4j.core.config;

import java.io.File;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name="JSONConfigurationFactory", category="ConfigurationFactory")
@Order(6)
public class JSONConfigurationFactory
  extends ConfigurationFactory
{
  public static final String[] SUFFIXES = { ".json", ".jsn" };
  private static String[] dependencies = { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser" };
  private final File configFile = null;
  private boolean isActive;
  
  public JSONConfigurationFactory()
  {
    try
    {
      for (String item : dependencies) {
        Class.forName(item);
      }
    }
    catch (ClassNotFoundException ex)
    {
      LOGGER.debug("Missing dependencies for Json support");
      this.isActive = false;
      return;
    }
    this.isActive = true;
  }
  
  protected boolean isActive()
  {
    return this.isActive;
  }
  
  public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource source)
  {
    if (!this.isActive) {
      return null;
    }
    return new JSONConfiguration(source);
  }
  
  public String[] getSupportedTypes()
  {
    return SUFFIXES;
  }
}
