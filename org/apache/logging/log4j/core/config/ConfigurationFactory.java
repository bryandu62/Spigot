package org.apache.logging.log4j.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public abstract class ConfigurationFactory
{
  public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
  public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
  protected static final Logger LOGGER = ;
  protected static final String TEST_PREFIX = "log4j2-test";
  protected static final String DEFAULT_PREFIX = "log4j2";
  private static final String CLASS_LOADER_SCHEME = "classloader";
  private static final int CLASS_LOADER_SCHEME_LENGTH = "classloader".length() + 1;
  private static final String CLASS_PATH_SCHEME = "classpath";
  private static final int CLASS_PATH_SCHEME_LENGTH = "classpath".length() + 1;
  private static volatile List<ConfigurationFactory> factories = null;
  private static ConfigurationFactory configFactory = new Factory(null);
  protected final StrSubstitutor substitutor;
  
  public ConfigurationFactory()
  {
    this.substitutor = new StrSubstitutor(new Interpolator());
  }
  
  public static ConfigurationFactory getInstance()
  {
    if (factories == null) {
      synchronized ("log4j2-test")
      {
        if (factories == null)
        {
          List<ConfigurationFactory> list = new ArrayList();
          String factoryClass = PropertiesUtil.getProperties().getStringProperty("log4j.configurationFactory");
          if (factoryClass != null) {
            addFactory(list, factoryClass);
          }
          PluginManager manager = new PluginManager("ConfigurationFactory");
          manager.collectPlugins();
          Map<String, PluginType<?>> plugins = manager.getPlugins();
          Set<WeightedFactory> ordered = new TreeSet();
          for (PluginType<?> type : plugins.values()) {
            try
            {
              Class<ConfigurationFactory> clazz = type.getPluginClass();
              Order order = (Order)clazz.getAnnotation(Order.class);
              if (order != null)
              {
                int weight = order.value();
                ordered.add(new WeightedFactory(weight, clazz));
              }
            }
            catch (Exception ex)
            {
              LOGGER.warn("Unable to add class " + type.getPluginClass());
            }
          }
          for (WeightedFactory wf : ordered) {
            addFactory(list, wf.factoryClass);
          }
          factories = Collections.unmodifiableList(list);
        }
      }
    }
    return configFactory;
  }
  
  private static void addFactory(List<ConfigurationFactory> list, String factoryClass)
  {
    try
    {
      addFactory(list, Class.forName(factoryClass));
    }
    catch (ClassNotFoundException ex)
    {
      LOGGER.error("Unable to load class " + factoryClass, ex);
    }
    catch (Exception ex)
    {
      LOGGER.error("Unable to load class " + factoryClass, ex);
    }
  }
  
  private static void addFactory(List<ConfigurationFactory> list, Class<ConfigurationFactory> factoryClass)
  {
    try
    {
      list.add(factoryClass.newInstance());
    }
    catch (Exception ex)
    {
      LOGGER.error("Unable to create instance of " + factoryClass.getName(), ex);
    }
  }
  
  public static void setConfigurationFactory(ConfigurationFactory factory)
  {
    configFactory = factory;
  }
  
  public static void resetConfigurationFactory()
  {
    configFactory = new Factory(null);
  }
  
  public static void removeConfigurationFactory(ConfigurationFactory factory)
  {
    if (configFactory == factory) {
      configFactory = new Factory(null);
    }
  }
  
  protected abstract String[] getSupportedTypes();
  
  protected boolean isActive()
  {
    return true;
  }
  
  public abstract Configuration getConfiguration(ConfigurationSource paramConfigurationSource);
  
  public Configuration getConfiguration(String name, URI configLocation)
  {
    if (!isActive()) {
      return null;
    }
    if (configLocation != null)
    {
      ConfigurationSource source = getInputFromURI(configLocation);
      if (source != null) {
        return getConfiguration(source);
      }
    }
    return null;
  }
  
  protected ConfigurationSource getInputFromURI(URI configLocation)
  {
    File configFile = FileUtils.fileFromURI(configLocation);
    if ((configFile != null) && (configFile.exists()) && (configFile.canRead())) {
      try
      {
        return new ConfigurationSource(new FileInputStream(configFile), configFile);
      }
      catch (FileNotFoundException ex)
      {
        LOGGER.error("Cannot locate file " + configLocation.getPath(), ex);
      }
    }
    String scheme = configLocation.getScheme();
    boolean isClassLoaderScheme = (scheme != null) && (scheme.equals("classloader"));
    boolean isClassPathScheme = (scheme != null) && (!isClassLoaderScheme) && (scheme.equals("classpath"));
    if ((scheme == null) || (isClassLoaderScheme) || (isClassPathScheme))
    {
      ClassLoader loader = getClass().getClassLoader();
      String path;
      String path;
      if (isClassLoaderScheme)
      {
        path = configLocation.toString().substring(CLASS_LOADER_SCHEME_LENGTH);
      }
      else
      {
        String path;
        if (isClassPathScheme) {
          path = configLocation.toString().substring(CLASS_PATH_SCHEME_LENGTH);
        } else {
          path = configLocation.getPath();
        }
      }
      ConfigurationSource source = getInputFromResource(path, loader);
      if (source != null) {
        return source;
      }
    }
    try
    {
      return new ConfigurationSource(configLocation.toURL().openStream(), configLocation.getPath());
    }
    catch (MalformedURLException ex)
    {
      LOGGER.error("Invalid URL " + configLocation.toString(), ex);
    }
    catch (IOException ex)
    {
      LOGGER.error("Unable to access " + configLocation.toString(), ex);
    }
    catch (Exception ex)
    {
      LOGGER.error("Unable to access " + configLocation.toString(), ex);
    }
    return null;
  }
  
  protected ConfigurationSource getInputFromString(String config, ClassLoader loader)
  {
    try
    {
      URL url = new URL(config);
      return new ConfigurationSource(url.openStream(), FileUtils.fileFromURI(url.toURI()));
    }
    catch (Exception ex)
    {
      ConfigurationSource source = getInputFromResource(config, loader);
      if (source == null) {
        try
        {
          File file = new File(config);
          return new ConfigurationSource(new FileInputStream(file), file);
        }
        catch (FileNotFoundException fnfe) {}
      }
      return source;
    }
  }
  
  protected ConfigurationSource getInputFromResource(String resource, ClassLoader loader)
  {
    URL url = Loader.getResource(resource, loader);
    if (url == null) {
      return null;
    }
    InputStream is = null;
    try
    {
      is = url.openStream();
    }
    catch (IOException ioe)
    {
      return null;
    }
    if (is == null) {
      return null;
    }
    if (FileUtils.isFile(url)) {
      try
      {
        return new ConfigurationSource(is, FileUtils.fileFromURI(url.toURI()));
      }
      catch (URISyntaxException ex) {}
    }
    return new ConfigurationSource(is, resource);
  }
  
  private static class WeightedFactory
    implements Comparable<WeightedFactory>
  {
    private final int weight;
    private final Class<ConfigurationFactory> factoryClass;
    
    public WeightedFactory(int weight, Class<ConfigurationFactory> clazz)
    {
      this.weight = weight;
      this.factoryClass = clazz;
    }
    
    public int compareTo(WeightedFactory wf)
    {
      int w = wf.weight;
      if (this.weight == w) {
        return 0;
      }
      if (this.weight > w) {
        return -1;
      }
      return 1;
    }
  }
  
  private static class Factory
    extends ConfigurationFactory
  {
    public Configuration getConfiguration(String name, URI configLocation)
    {
      String config;
      ConfigurationFactory.ConfigurationSource source;
      if (configLocation == null)
      {
        config = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty("log4j.configurationFile"));
        if (config != null)
        {
          source = null;
          try
          {
            source = getInputFromURI(new URI(config));
          }
          catch (Exception ex) {}
          if (source == null)
          {
            ClassLoader loader = getClass().getClassLoader();
            source = getInputFromString(config, loader);
          }
          if (source != null) {
            for (ConfigurationFactory factory : ConfigurationFactory.factories)
            {
              String[] types = factory.getSupportedTypes();
              if (types != null) {
                for (String type : types) {
                  if ((type.equals("*")) || (config.endsWith(type)))
                  {
                    Configuration c = factory.getConfiguration(source);
                    if (c != null) {
                      return c;
                    }
                  }
                }
              }
            }
          }
        }
      }
      else
      {
        for (ConfigurationFactory factory : ConfigurationFactory.factories)
        {
          String[] types = factory.getSupportedTypes();
          if (types != null) {
            for (String type : types) {
              if ((type.equals("*")) || (configLocation.toString().endsWith(type)))
              {
                Configuration config = factory.getConfiguration(name, configLocation);
                if (config != null) {
                  return config;
                }
              }
            }
          }
        }
      }
      Configuration config = getConfiguration(true, name);
      if (config == null)
      {
        config = getConfiguration(true, null);
        if (config == null)
        {
          config = getConfiguration(false, name);
          if (config == null) {
            config = getConfiguration(false, null);
          }
        }
      }
      return config != null ? config : new DefaultConfiguration();
    }
    
    private Configuration getConfiguration(boolean isTest, String name)
    {
      boolean named = (name != null) && (name.length() > 0);
      ClassLoader loader = getClass().getClassLoader();
      for (ConfigurationFactory factory : ConfigurationFactory.factories)
      {
        String prefix = isTest ? "log4j2-test" : "log4j2";
        String[] types = factory.getSupportedTypes();
        if (types != null) {
          for (String suffix : types) {
            if (!suffix.equals("*"))
            {
              String configName = prefix + suffix;
              
              ConfigurationFactory.ConfigurationSource source = getInputFromResource(configName, loader);
              if (source != null) {
                return factory.getConfiguration(source);
              }
            }
          }
        }
      }
      return null;
    }
    
    public String[] getSupportedTypes()
    {
      return null;
    }
    
    public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource source)
    {
      String config;
      if (source != null)
      {
        config = source.getLocation();
        for (ConfigurationFactory factory : ConfigurationFactory.factories)
        {
          String[] types = factory.getSupportedTypes();
          if (types != null) {
            for (String type : types) {
              if ((type.equals("*")) || ((config != null) && (config.endsWith(type))))
              {
                Configuration c = factory.getConfiguration(source);
                if (c != null) {
                  return c;
                }
                LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", new Object[] { config });
                return null;
              }
            }
          }
        }
      }
      LOGGER.error("Cannot process configuration, input source is null");
      return null;
    }
  }
  
  public static class ConfigurationSource
  {
    private File file;
    private String location;
    private InputStream stream;
    
    public ConfigurationSource() {}
    
    public ConfigurationSource(InputStream stream)
    {
      this.stream = stream;
      this.file = null;
      this.location = null;
    }
    
    public ConfigurationSource(InputStream stream, File file)
    {
      this.stream = stream;
      this.file = file;
      this.location = file.getAbsolutePath();
    }
    
    public ConfigurationSource(InputStream stream, String location)
    {
      this.stream = stream;
      this.location = location;
      this.file = null;
    }
    
    public File getFile()
    {
      return this.file;
    }
    
    public void setFile(File file)
    {
      this.file = file;
    }
    
    public String getLocation()
    {
      return this.location;
    }
    
    public void setLocation(String location)
    {
      this.location = location;
    }
    
    public InputStream getInputStream()
    {
      return this.stream;
    }
    
    public void setInputStream(InputStream stream)
    {
      this.stream = stream;
    }
  }
}
