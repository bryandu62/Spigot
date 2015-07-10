package org.bukkit.craftbukkit.libs.jline.internal;

import java.util.ArrayList;
import java.util.List;

public class ShutdownHooks
{
  public static final String JLINE_SHUTDOWNHOOK = "org.bukkit.craftbukkit.libs.jline.shutdownhook";
  private static final boolean enabled = Configuration.getBoolean("org.bukkit.craftbukkit.libs.jline.shutdownhook", true);
  private static final List<Task> tasks = new ArrayList();
  private static Thread hook;
  
  public static synchronized <T extends Task> T add(T task)
  {
    Preconditions.checkNotNull(task);
    if (!enabled)
    {
      Log.debug(new Object[] { "Shutdown-hook is disabled; not installing: ", task });
      return task;
    }
    if (hook == null) {
      hook = addHook(new Thread("JLine Shutdown Hook")
      {
        public void run() {}
      });
    }
    Log.debug(new Object[] { "Adding shutdown-hook task: ", task });
    tasks.add(task);
    
    return task;
  }
  
  private static synchronized void runTasks()
  {
    Log.debug(new Object[] { "Running all shutdown-hook tasks" });
    for (Task task : (Task[])tasks.toArray(new Task[tasks.size()]))
    {
      Log.debug(new Object[] { "Running task: ", task });
      try
      {
        task.run();
      }
      catch (Throwable e)
      {
        Log.warn(new Object[] { "Task failed", e });
      }
    }
    tasks.clear();
  }
  
  private static Thread addHook(Thread thread)
  {
    Log.debug(new Object[] { "Registering shutdown-hook: ", thread });
    try
    {
      Runtime.getRuntime().addShutdownHook(thread);
    }
    catch (AbstractMethodError e)
    {
      Log.debug(new Object[] { "Failed to register shutdown-hook", e });
    }
    return thread;
  }
  
  public static synchronized void remove(Task task)
  {
    Preconditions.checkNotNull(task);
    if ((!enabled) || (hook == null)) {
      return;
    }
    tasks.remove(task);
    if (tasks.isEmpty())
    {
      removeHook(hook);
      hook = null;
    }
  }
  
  private static void removeHook(Thread thread)
  {
    Log.debug(new Object[] { "Removing shutdown-hook: ", thread });
    try
    {
      Runtime.getRuntime().removeShutdownHook(thread);
    }
    catch (AbstractMethodError e)
    {
      Log.debug(new Object[] { "Failed to remove shutdown-hook", e });
    }
    catch (IllegalStateException e) {}
  }
  
  public static abstract interface Task
  {
    public abstract void run()
      throws Exception;
  }
}
