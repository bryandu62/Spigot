package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

public class SimpleServicesManager
  implements ServicesManager
{
  private final Map<Class<?>, List<RegisteredServiceProvider<?>>> providers = new HashMap();
  
  public <T> void register(Class<T> service, T provider, Plugin plugin, ServicePriority priority)
  {
    RegisteredServiceProvider<T> registeredProvider = null;
    synchronized (this.providers)
    {
      List<RegisteredServiceProvider<?>> registered = (List)this.providers.get(service);
      if (registered == null)
      {
        registered = new ArrayList();
        this.providers.put(service, registered);
      }
      registeredProvider = new RegisteredServiceProvider(service, provider, priority, plugin);
      
      int position = Collections.binarySearch(registered, registeredProvider);
      if (position < 0) {
        registered.add(-(position + 1), registeredProvider);
      } else {
        registered.add(position, registeredProvider);
      }
    }
    Bukkit.getServer().getPluginManager().callEvent(new ServiceRegisterEvent(registeredProvider));
  }
  
  public void unregisterAll(Plugin plugin)
  {
    ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList();
    Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it;
    synchronized (this.providers)
    {
      it = this.providers.entrySet().iterator();
      try
      {
        while (it.hasNext())
        {
          Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = (Map.Entry)it.next();
          Iterator<RegisteredServiceProvider<?>> it2 = ((List)entry.getValue()).iterator();
          try
          {
            while (it2.hasNext())
            {
              RegisteredServiceProvider<?> registered = (RegisteredServiceProvider)it2.next();
              if (registered.getPlugin().equals(plugin))
              {
                it2.remove();
                unregisteredEvents.add(new ServiceUnregisterEvent(registered));
              }
            }
          }
          catch (NoSuchElementException localNoSuchElementException1) {}
          if (((List)entry.getValue()).size() == 0) {
            it.remove();
          }
        }
      }
      catch (NoSuchElementException localNoSuchElementException2) {}
    }
    for (ServiceUnregisterEvent event : unregisteredEvents) {
      Bukkit.getServer().getPluginManager().callEvent(event);
    }
  }
  
  public void unregister(Class<?> service, Object provider)
  {
    ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList();
    Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it;
    synchronized (this.providers)
    {
      it = this.providers.entrySet().iterator();
      try
      {
        while (it.hasNext())
        {
          Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = (Map.Entry)it.next();
          if (entry.getKey() == service)
          {
            Iterator<RegisteredServiceProvider<?>> it2 = ((List)entry.getValue()).iterator();
            try
            {
              while (it2.hasNext())
              {
                RegisteredServiceProvider<?> registered = (RegisteredServiceProvider)it2.next();
                if (registered.getProvider() == provider)
                {
                  it2.remove();
                  unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                }
              }
            }
            catch (NoSuchElementException localNoSuchElementException1) {}
            if (((List)entry.getValue()).size() == 0) {
              it.remove();
            }
          }
        }
      }
      catch (NoSuchElementException localNoSuchElementException2) {}
    }
    for (ServiceUnregisterEvent event : unregisteredEvents) {
      Bukkit.getServer().getPluginManager().callEvent(event);
    }
  }
  
  public void unregister(Object provider)
  {
    ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList();
    Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it;
    synchronized (this.providers)
    {
      it = this.providers.entrySet().iterator();
      try
      {
        while (it.hasNext())
        {
          Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = (Map.Entry)it.next();
          Iterator<RegisteredServiceProvider<?>> it2 = ((List)entry.getValue()).iterator();
          try
          {
            while (it2.hasNext())
            {
              RegisteredServiceProvider<?> registered = (RegisteredServiceProvider)it2.next();
              if (registered.getProvider().equals(provider))
              {
                it2.remove();
                unregisteredEvents.add(new ServiceUnregisterEvent(registered));
              }
            }
          }
          catch (NoSuchElementException localNoSuchElementException1) {}
          if (((List)entry.getValue()).size() == 0) {
            it.remove();
          }
        }
      }
      catch (NoSuchElementException localNoSuchElementException2) {}
    }
    for (ServiceUnregisterEvent event : unregisteredEvents) {
      Bukkit.getServer().getPluginManager().callEvent(event);
    }
  }
  
  public <T> T load(Class<T> service)
  {
    synchronized (this.providers)
    {
      List<RegisteredServiceProvider<?>> registered = (List)this.providers.get(service);
      if (registered == null) {
        return null;
      }
      return (T)service.cast(((RegisteredServiceProvider)registered.get(0)).getProvider());
    }
  }
  
  public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service)
  {
    synchronized (this.providers)
    {
      List<RegisteredServiceProvider<?>> registered = (List)this.providers.get(service);
      if (registered == null) {
        return null;
      }
      return (RegisteredServiceProvider)registered.get(0);
    }
  }
  
  public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin)
  {
    ImmutableList.Builder<RegisteredServiceProvider<?>> ret = ImmutableList.builder();
    synchronized (this.providers)
    {
      Iterator localIterator2;
      for (Iterator localIterator1 = this.providers.values().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
      {
        List<RegisteredServiceProvider<?>> registered = (List)localIterator1.next();
        localIterator2 = registered.iterator(); continue;RegisteredServiceProvider<?> provider = (RegisteredServiceProvider)localIterator2.next();
        if (provider.getPlugin().equals(plugin)) {
          ret.add(provider);
        }
      }
    }
    return ret.build();
  }
  
  public <T> List<RegisteredServiceProvider<T>> getRegistrations(Class<T> service)
  {
    synchronized (this.providers)
    {
      List<RegisteredServiceProvider<?>> registered = (List)this.providers.get(service);
      if (registered == null) {
        return ImmutableList.of();
      }
      ImmutableList.Builder<RegisteredServiceProvider<T>> ret = ImmutableList.builder();
      for (RegisteredServiceProvider<?> provider : registered) {
        ret.add(provider);
      }
    }
    ImmutableList.Builder<RegisteredServiceProvider<T>> ret;
    return ret.build();
  }
  
  /* Error */
  public Set<Class<?>> getKnownServices()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	org/bukkit/plugin/SimpleServicesManager:providers	Ljava/util/Map;
    //   4: dup
    //   5: astore_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 29	org/bukkit/plugin/SimpleServicesManager:providers	Ljava/util/Map;
    //   11: invokeinterface 223 1 0
    //   16: invokestatic 229	com/google/common/collect/ImmutableSet:copyOf	(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableSet;
    //   19: aload_1
    //   20: monitorexit
    //   21: areturn
    //   22: aload_1
    //   23: monitorexit
    //   24: athrow
    // Line number table:
    //   Java source line #289	-> byte code offset #0
    //   Java source line #290	-> byte code offset #7
    //   Java source line #289	-> byte code offset #22
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	25	0	this	SimpleServicesManager
    //   5	18	1	Ljava/lang/Object;	Object
    // Exception table:
    //   from	to	target	type
    //   7	21	22	finally
    //   22	24	22	finally
  }
  
  /* Error */
  public <T> boolean isProvidedFor(Class<T> service)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	org/bukkit/plugin/SimpleServicesManager:providers	Ljava/util/Map;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 29	org/bukkit/plugin/SimpleServicesManager:providers	Ljava/util/Map;
    //   11: aload_1
    //   12: invokeinterface 234 2 0
    //   17: aload_2
    //   18: monitorexit
    //   19: ireturn
    //   20: aload_2
    //   21: monitorexit
    //   22: athrow
    // Line number table:
    //   Java source line #302	-> byte code offset #0
    //   Java source line #303	-> byte code offset #7
    //   Java source line #302	-> byte code offset #20
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	23	0	this	SimpleServicesManager
    //   0	23	1	service	Class<T>
    //   5	16	2	Ljava/lang/Object;	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	20	finally
    //   20	22	20	finally
  }
}
