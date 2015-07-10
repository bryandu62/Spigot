package org.bukkit.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class HandlerList
{
  private volatile RegisteredListener[] handlers = null;
  private static ArrayList<HandlerList> allLists = new ArrayList();
  
  public static void bakeAll()
  {
    synchronized (allLists)
    {
      for (HandlerList h : allLists) {
        h.bake();
      }
    }
  }
  
  public static void unregisterAll()
  {
    synchronized (allLists)
    {
      for (HandlerList h : allLists) {
        synchronized (h)
        {
          for (List<RegisteredListener> list : h.handlerslots.values()) {
            list.clear();
          }
          h.handlers = null;
        }
      }
    }
  }
  
  public static void unregisterAll(Plugin plugin)
  {
    synchronized (allLists)
    {
      for (HandlerList h : allLists) {
        h.unregister(plugin);
      }
    }
  }
  
  public static void unregisterAll(Listener listener)
  {
    synchronized (allLists)
    {
      for (HandlerList h : allLists) {
        h.unregister(listener);
      }
    }
  }
  
  private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerslots = new EnumMap(EventPriority.class);
  
  public HandlerList()
  {
    EventPriority[] arrayOfEventPriority;
    int i = (arrayOfEventPriority = EventPriority.values()).length;
    for (int j = 0; j < i; j++)
    {
      EventPriority o = arrayOfEventPriority[j];
      this.handlerslots.put(o, new ArrayList());
    }
    synchronized (allLists)
    {
      allLists.add(this);
    }
  }
  
  public synchronized void register(RegisteredListener listener)
  {
    if (((ArrayList)this.handlerslots.get(listener.getPriority())).contains(listener)) {
      throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
    }
    this.handlers = null;
    ((ArrayList)this.handlerslots.get(listener.getPriority())).add(listener);
  }
  
  public void registerAll(Collection<RegisteredListener> listeners)
  {
    for (RegisteredListener listener : listeners) {
      register(listener);
    }
  }
  
  public synchronized void unregister(RegisteredListener listener)
  {
    if (((ArrayList)this.handlerslots.get(listener.getPriority())).remove(listener)) {
      this.handlers = null;
    }
  }
  
  public synchronized void unregister(Plugin plugin)
  {
    boolean changed = false;
    ListIterator<RegisteredListener> i;
    for (Iterator localIterator = this.handlerslots.values().iterator(); localIterator.hasNext(); i.hasNext())
    {
      List<RegisteredListener> list = (List)localIterator.next();
      i = list.listIterator(); continue;
      if (((RegisteredListener)i.next()).getPlugin().equals(plugin))
      {
        i.remove();
        changed = true;
      }
    }
    if (changed) {
      this.handlers = null;
    }
  }
  
  public synchronized void unregister(Listener listener)
  {
    boolean changed = false;
    ListIterator<RegisteredListener> i;
    for (Iterator localIterator = this.handlerslots.values().iterator(); localIterator.hasNext(); i.hasNext())
    {
      List<RegisteredListener> list = (List)localIterator.next();
      i = list.listIterator(); continue;
      if (((RegisteredListener)i.next()).getListener().equals(listener))
      {
        i.remove();
        changed = true;
      }
    }
    if (changed) {
      this.handlers = null;
    }
  }
  
  public synchronized void bake()
  {
    if (this.handlers != null) {
      return;
    }
    List<RegisteredListener> entries = new ArrayList();
    for (Map.Entry<EventPriority, ArrayList<RegisteredListener>> entry : this.handlerslots.entrySet()) {
      entries.addAll((Collection)entry.getValue());
    }
    this.handlers = ((RegisteredListener[])entries.toArray(new RegisteredListener[entries.size()]));
  }
  
  public RegisteredListener[] getRegisteredListeners()
  {
    RegisteredListener[] handlers;
    while ((handlers = this.handlers) == null)
    {
      RegisteredListener[] handlers;
      bake();
    }
    return handlers;
  }
  
  public static ArrayList<RegisteredListener> getRegisteredListeners(Plugin plugin)
  {
    ArrayList<RegisteredListener> listeners = new ArrayList();
    synchronized (allLists)
    {
      for (HandlerList h : allLists) {
        synchronized (h)
        {
          Iterator localIterator3;
          for (Iterator localIterator2 = h.handlerslots.values().iterator(); localIterator2.hasNext(); localIterator3.hasNext())
          {
            List<RegisteredListener> list = (List)localIterator2.next();
            localIterator3 = list.iterator(); continue;RegisteredListener listener = (RegisteredListener)localIterator3.next();
            if (listener.getPlugin().equals(plugin)) {
              listeners.add(listener);
            }
          }
        }
      }
    }
    return listeners;
  }
  
  /* Error */
  public static ArrayList<HandlerList> getHandlerLists()
  {
    // Byte code:
    //   0: getstatic 27	org/bukkit/event/HandlerList:allLists	Ljava/util/ArrayList;
    //   3: dup
    //   4: astore_0
    //   5: monitorenter
    //   6: getstatic 27	org/bukkit/event/HandlerList:allLists	Ljava/util/ArrayList;
    //   9: invokevirtual 216	java/util/ArrayList:clone	()Ljava/lang/Object;
    //   12: checkcast 22	java/util/ArrayList
    //   15: aload_0
    //   16: monitorexit
    //   17: areturn
    //   18: aload_0
    //   19: monitorexit
    //   20: athrow
    // Line number table:
    //   Java source line #227	-> byte code offset #0
    //   Java source line #228	-> byte code offset #6
    //   Java source line #227	-> byte code offset #18
    // Local variable table:
    //   start	length	slot	name	signature
    //   4	15	0	Ljava/lang/Object;	Object
    // Exception table:
    //   from	to	target	type
    //   6	17	18	finally
    //   18	20	18	finally
  }
}
