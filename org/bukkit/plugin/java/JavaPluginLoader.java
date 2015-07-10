package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.Warning.WarningState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.spigotmc.CustomTimingsHandler;
import org.yaml.snakeyaml.error.YAMLException;

public final class JavaPluginLoader
  implements PluginLoader
{
  final Server server;
  private final Pattern[] fileFilters = { Pattern.compile("\\.jar$") };
  private final Map<String, Class<?>> classes = new ConcurrentHashMap();
  private final Map<String, PluginClassLoader> loaders = new LinkedHashMap();
  public static final CustomTimingsHandler pluginParentTimer = new CustomTimingsHandler("** Plugins");
  
  @Deprecated
  public JavaPluginLoader(Server instance)
  {
    Validate.notNull(instance, "Server cannot be null");
    this.server = instance;
  }
  
  public Plugin loadPlugin(File file)
    throws InvalidPluginException
  {
    Validate.notNull(file, "File cannot be null");
    if (!file.exists()) {
      throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
    }
    try
    {
      description = getPluginDescription(file);
    }
    catch (InvalidDescriptionException ex)
    {
      PluginDescriptionFile description;
      throw new InvalidPluginException(ex);
    }
    PluginDescriptionFile description;
    File parentFile = file.getParentFile();
    File dataFolder = new File(parentFile, description.getName());
    
    File oldDataFolder = new File(parentFile, description.getRawName());
    if (!dataFolder.equals(oldDataFolder)) {
      if ((dataFolder.isDirectory()) && (oldDataFolder.isDirectory()))
      {
        this.server.getLogger().warning(String.format(
          "While loading %s (%s) found old-data folder: `%s' next to the new one `%s'", new Object[] {
          description.getFullName(), 
          file, 
          oldDataFolder, 
          dataFolder }));
      }
      else if ((oldDataFolder.isDirectory()) && (!dataFolder.exists()))
      {
        if (!oldDataFolder.renameTo(dataFolder)) {
          throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "' to: `" + dataFolder + "'");
        }
        this.server.getLogger().log(Level.INFO, String.format(
          "While loading %s (%s) renamed data folder: `%s' to `%s'", new Object[] {
          description.getFullName(), 
          file, 
          oldDataFolder, 
          dataFolder }));
      }
    }
    if ((dataFolder.exists()) && (!dataFolder.isDirectory())) {
      throw new InvalidPluginException(String.format(
        "Projected datafolder: `%s' for %s (%s) exists and is not a directory", new Object[] {
        dataFolder, 
        description.getFullName(), 
        file }));
    }
    for (String pluginName : description.getDepend())
    {
      if (this.loaders == null) {
        throw new UnknownDependencyException(pluginName);
      }
      PluginClassLoader current = (PluginClassLoader)this.loaders.get(pluginName);
      if (current == null) {
        throw new UnknownDependencyException(pluginName);
      }
    }
    try
    {
      loader = new PluginClassLoader(this, getClass().getClassLoader(), description, dataFolder, file);
    }
    catch (InvalidPluginException ex)
    {
      PluginClassLoader loader;
      throw ex;
    }
    catch (Throwable ex)
    {
      throw new InvalidPluginException(ex);
    }
    PluginClassLoader loader;
    this.loaders.put(description.getName(), loader);
    
    return loader.plugin;
  }
  
  public PluginDescriptionFile getPluginDescription(File file)
    throws InvalidDescriptionException
  {
    Validate.notNull(file, "File cannot be null");
    
    JarFile jar = null;
    InputStream stream = null;
    try
    {
      jar = new JarFile(file);
      JarEntry entry = jar.getJarEntry("plugin.yml");
      if (entry == null) {
        throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
      }
      stream = jar.getInputStream(entry);
      
      return new PluginDescriptionFile(stream);
    }
    catch (IOException ex)
    {
      throw new InvalidDescriptionException(ex);
    }
    catch (YAMLException ex)
    {
      throw new InvalidDescriptionException(ex);
    }
    finally
    {
      if (jar != null) {
        try
        {
          jar.close();
        }
        catch (IOException localIOException3) {}
      }
      if (stream != null) {
        try
        {
          stream.close();
        }
        catch (IOException localIOException4) {}
      }
    }
  }
  
  public Pattern[] getPluginFileFilters()
  {
    return (Pattern[])this.fileFilters.clone();
  }
  
  Class<?> getClassByName(String name)
  {
    Class<?> cachedClass = (Class)this.classes.get(name);
    if (cachedClass != null) {
      return cachedClass;
    }
    for (String current : this.loaders.keySet())
    {
      PluginClassLoader loader = (PluginClassLoader)this.loaders.get(current);
      try
      {
        cachedClass = loader.findClass(name, false);
      }
      catch (ClassNotFoundException localClassNotFoundException) {}
      if (cachedClass != null) {
        return cachedClass;
      }
    }
    return null;
  }
  
  void setClass(String name, Class<?> clazz)
  {
    if (!this.classes.containsKey(name))
    {
      this.classes.put(name, clazz);
      if (ConfigurationSerializable.class.isAssignableFrom(clazz))
      {
        Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
        ConfigurationSerialization.registerClass(serializable);
      }
    }
  }
  
  private void removeClass(String name)
  {
    Class<?> clazz = (Class)this.classes.remove(name);
    try
    {
      if ((clazz != null) && (ConfigurationSerializable.class.isAssignableFrom(clazz)))
      {
        Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
        ConfigurationSerialization.unregisterClass(serializable);
      }
    }
    catch (NullPointerException localNullPointerException) {}
  }
  
  public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin)
  {
    Validate.notNull(plugin, "Plugin can not be null");
    Validate.notNull(listener, "Listener can not be null");
    
    this.server.getPluginManager().useTimings();
    Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap();
    Method[] privateMethods;
    try
    {
      Method[] publicMethods = listener.getClass().getMethods();
      privateMethods = listener.getClass().getDeclaredMethods();
      Set<Method> methods = new HashSet(publicMethods.length + privateMethods.length, 1.0F);
      Method[] arrayOfMethod1;
      int i = (arrayOfMethod1 = publicMethods).length;
      for (int j = 0; j < i; j++)
      {
        Method method = arrayOfMethod1[j];
        methods.add(method);
      }
      i = (arrayOfMethod1 = privateMethods).length;
      for (j = 0; j < i; j++)
      {
        Method method = arrayOfMethod1[j];
        methods.add(method);
      }
    }
    catch (NoClassDefFoundError e)
    {
      plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
      return ret;
    }
    Set<Method> methods;
    for (final Method method : methods)
    {
      EventHandler eh = (EventHandler)method.getAnnotation(EventHandler.class);
      if (eh != null) {
        if ((!method.isBridge()) && (!method.isSynthetic()))
        {
          Object checkClass;
          if ((method.getParameterTypes().length != 1) || (!Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])))
          {
            plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
          }
          else
          {
            Class<?> checkClass;
            final Object eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Object eventSet = (Set)ret.get(eventClass);
            if (eventSet == null)
            {
              eventSet = new HashSet();
              ret.put(eventClass, eventSet);
            }
            for (Class<?> clazz = (Class<?>)eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
              if (clazz.getAnnotation(Deprecated.class) != null)
              {
                Warning warning = (Warning)clazz.getAnnotation(Warning.class);
                Warning.WarningState warningState = this.server.getWarningState();
                if (!warningState.printFor(warning)) {
                  break;
                }
                plugin.getLogger().log(
                  Level.WARNING, 
                  String.format(
                  "\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated. \"%s\"; please notify the authors %s.", new Object[] {
                  
                  plugin.getDescription().getFullName(), 
                  clazz.getName(), 
                  method.toGenericString(), 
                  (warning != null) && (warning.reason().length() != 0) ? warning.reason() : "Server performance will be affected", 
                  Arrays.toString(plugin.getDescription().getAuthors().toArray()) }), 
                  warningState == Warning.WarningState.ON ? new AuthorNagException(null) : null);
                break;
              }
            }
            final CustomTimingsHandler timings = new CustomTimingsHandler("Plugin: " + plugin.getDescription().getFullName() + " Event: " + listener.getClass().getName() + "::" + method.getName() + "(" + ((Class)eventClass).getSimpleName() + ")", pluginParentTimer);
            EventExecutor executor = new EventExecutor()
            {
              public void execute(Listener listener, Event event)
                throws EventException
              {
                try
                {
                  if (!eventClass.isAssignableFrom(event.getClass())) {
                    return;
                  }
                  boolean isAsync = event.isAsynchronous();
                  if (!isAsync) {
                    timings.startTiming();
                  }
                  method.invoke(listener, new Object[] { event });
                  if (!isAsync) {
                    timings.stopTiming();
                  }
                }
                catch (InvocationTargetException ex)
                {
                  throw new EventException(ex.getCause());
                }
                catch (Throwable t)
                {
                  throw new EventException(t);
                }
              }
            };
            ((Set)eventSet).add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
          }
        }
      }
    }
    return ret;
  }
  
  public void enablePlugin(Plugin plugin)
  {
    Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");
    if (!plugin.isEnabled())
    {
      plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());
      
      JavaPlugin jPlugin = (JavaPlugin)plugin;
      
      String pluginName = jPlugin.getDescription().getName();
      if (!this.loaders.containsKey(pluginName)) {
        this.loaders.put(pluginName, (PluginClassLoader)jPlugin.getClassLoader());
      }
      try
      {
        jPlugin.setEnabled(true);
      }
      catch (Throwable ex)
      {
        this.server.getLogger().log(Level.SEVERE, "Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
      }
      this.server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
    }
  }
  
  public void disablePlugin(Plugin plugin)
  {
    Validate.isTrue(plugin instanceof JavaPlugin, "Plugin is not associated with this PluginLoader");
    if (plugin.isEnabled())
    {
      String message = String.format("Disabling %s", new Object[] { plugin.getDescription().getFullName() });
      plugin.getLogger().info(message);
      
      this.server.getPluginManager().callEvent(new PluginDisableEvent(plugin));
      
      JavaPlugin jPlugin = (JavaPlugin)plugin;
      ClassLoader cloader = jPlugin.getClassLoader();
      try
      {
        jPlugin.setEnabled(false);
      }
      catch (Throwable ex)
      {
        this.server.getLogger().log(Level.SEVERE, "Error occurred while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
      }
      this.loaders.remove(jPlugin.getDescription().getName());
      if ((cloader instanceof PluginClassLoader))
      {
        PluginClassLoader loader = (PluginClassLoader)cloader;
        Set<String> names = loader.getClasses();
        for (String name : names) {
          removeClass(name);
        }
      }
    }
  }
}
