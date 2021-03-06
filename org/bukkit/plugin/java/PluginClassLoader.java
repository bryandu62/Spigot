package org.bukkit.plugin.java;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

final class PluginClassLoader
  extends URLClassLoader
{
  private final JavaPluginLoader loader;
  private final Map<String, Class<?>> classes = new ConcurrentHashMap();
  private final PluginDescriptionFile description;
  private final File dataFolder;
  private final File file;
  final JavaPlugin plugin;
  private JavaPlugin pluginInit;
  private IllegalStateException pluginState;
  
  static
  {
    try
    {
      Method method = ClassLoader.class.getDeclaredMethod("registerAsParallelCapable", new Class[0]);
      if (method != null)
      {
        boolean oldAccessible = method.isAccessible();
        method.setAccessible(true);
        method.invoke(null, new Object[0]);
        method.setAccessible(oldAccessible);
        Bukkit.getLogger().log(Level.INFO, "Set PluginClassLoader as parallel capable");
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException) {}catch (Exception ex)
    {
      Bukkit.getLogger().log(Level.WARNING, "Error setting PluginClassLoader as parallel capable", ex);
    }
  }
  
  PluginClassLoader(JavaPluginLoader loader, ClassLoader parent, PluginDescriptionFile description, File dataFolder, File file)
    throws InvalidPluginException, MalformedURLException
  {
    super(new URL[] { file.toURI().toURL() }, parent);
    Validate.notNull(loader, "Loader cannot be null");
    
    this.loader = loader;
    this.description = description;
    this.dataFolder = dataFolder;
    this.file = file;
    try
    {
      try
      {
        jarClass = Class.forName(description.getMain(), true, this);
      }
      catch (ClassNotFoundException ex)
      {
        Class<?> jarClass;
        throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
      }
      try
      {
        Class<?> jarClass;
        pluginClass = jarClass.asSubclass(JavaPlugin.class);
      }
      catch (ClassCastException ex)
      {
        Class<? extends JavaPlugin> pluginClass;
        throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex);
      }
      Class<? extends JavaPlugin> pluginClass;
      this.plugin = ((JavaPlugin)pluginClass.newInstance());
    }
    catch (IllegalAccessException ex)
    {
      throw new InvalidPluginException("No public constructor", ex);
    }
    catch (InstantiationException ex)
    {
      throw new InvalidPluginException("Abnormal plugin type", ex);
    }
  }
  
  protected Class<?> findClass(String name)
    throws ClassNotFoundException
  {
    return findClass(name, true);
  }
  
  Class<?> findClass(String name, boolean checkGlobal)
    throws ClassNotFoundException
  {
    if ((name.startsWith("org.bukkit.")) || (name.startsWith("net.minecraft."))) {
      throw new ClassNotFoundException(name);
    }
    Class<?> result = (Class)this.classes.get(name);
    if (result == null)
    {
      if (checkGlobal) {
        result = this.loader.getClassByName(name);
      }
      if (result == null)
      {
        result = super.findClass(name);
        if (result != null) {
          this.loader.setClass(name, result);
        }
      }
      this.classes.put(name, result);
    }
    return result;
  }
  
  Set<String> getClasses()
  {
    return this.classes.keySet();
  }
  
  synchronized void initialize(JavaPlugin javaPlugin)
  {
    Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
    Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
    if ((this.plugin != null) || (this.pluginInit != null)) {
      throw new IllegalArgumentException("Plugin already initialized!", this.pluginState);
    }
    this.pluginState = new IllegalStateException("Initial initialization");
    this.pluginInit = javaPlugin;
    
    javaPlugin.init(this.loader, this.loader.server, this.description, this.dataFolder, this.file, this);
  }
}
