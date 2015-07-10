package org.bukkit.craftbukkit.v1_8_R3.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.spigotmc.CustomTimingsHandler;

public class CraftScheduler
  implements BukkitScheduler
{
  private final AtomicInteger ids = new AtomicInteger(1);
  private volatile CraftTask head = new CraftTask();
  private final AtomicReference<CraftTask> tail = new AtomicReference(this.head);
  private final PriorityQueue<CraftTask> pending = new PriorityQueue(10, 
    new Comparator()
  {
    public int compare(CraftTask o1, CraftTask o2)
    {
      return (int)(o1.getNextRun() - o2.getNextRun());
    }
  }
    );
  private final List<CraftTask> temp = new ArrayList();
  private final ConcurrentHashMap<Integer, CraftTask> runners = new ConcurrentHashMap();
  private volatile int currentTick = -1;
  private final Executor executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Craft Scheduler Thread - %1$d").build());
  private CraftAsyncDebugger debugHead = new CraftAsyncDebugger(-1, null, null)
  {
    StringBuilder debugTo(StringBuilder string)
    {
      return string;
    }
  };
  private CraftAsyncDebugger debugTail = this.debugHead;
  private static final int RECENT_TICKS = 30;
  
  public int scheduleSyncDelayedTask(Plugin plugin, Runnable task)
  {
    return scheduleSyncDelayedTask(plugin, task, 0L);
  }
  
  public BukkitTask runTask(Plugin plugin, Runnable runnable)
  {
    return runTaskLater(plugin, runnable, 0L);
  }
  
  @Deprecated
  public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task)
  {
    return scheduleAsyncDelayedTask(plugin, task, 0L);
  }
  
  public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable)
  {
    return runTaskLaterAsynchronously(plugin, runnable, 0L);
  }
  
  public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay)
  {
    return scheduleSyncRepeatingTask(plugin, task, delay, -1L);
  }
  
  public BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long delay)
  {
    return runTaskTimer(plugin, runnable, delay, -1L);
  }
  
  @Deprecated
  public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay)
  {
    return scheduleAsyncRepeatingTask(plugin, task, delay, -1L);
  }
  
  public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay)
  {
    return runTaskTimerAsynchronously(plugin, runnable, delay, -1L);
  }
  
  public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period)
  {
    return runTaskTimer(plugin, runnable, delay, period).getTaskId();
  }
  
  public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period)
  {
    validate(plugin, runnable);
    if (delay < 0L) {
      delay = 0L;
    }
    if (period == 0L) {
      period = 1L;
    } else if (period < -1L) {
      period = -1L;
    }
    return handle(new CraftTask(plugin, runnable, nextId(), period), delay);
  }
  
  @Deprecated
  public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period)
  {
    return runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
  }
  
  public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period)
  {
    validate(plugin, runnable);
    if (delay < 0L) {
      delay = 0L;
    }
    if (period == 0L) {
      period = 1L;
    } else if (period < -1L) {
      period = -1L;
    }
    return handle(new CraftAsyncTask(this.runners, plugin, runnable, nextId(), period), delay);
  }
  
  public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task)
  {
    validate(plugin, task);
    CraftFuture<T> future = new CraftFuture(task, plugin, nextId());
    handle(future, 0L);
    return future;
  }
  
  public void cancelTask(final int taskId)
  {
    if (taskId <= 0) {
      return;
    }
    CraftTask task = (CraftTask)this.runners.get(Integer.valueOf(taskId));
    if (task != null) {
      task.cancel0();
    }
    task = new CraftTask(
      new Runnable()
      {
        public void run()
        {
          if (!check(CraftScheduler.this.temp)) {
            check(CraftScheduler.this.pending);
          }
        }
        
        private boolean check(Iterable<CraftTask> collection)
        {
          Iterator<CraftTask> tasks = collection.iterator();
          while (tasks.hasNext())
          {
            CraftTask task = (CraftTask)tasks.next();
            if (task.getTaskId() == taskId)
            {
              task.cancel0();
              tasks.remove();
              if (task.isSync()) {
                CraftScheduler.this.runners.remove(Integer.valueOf(taskId));
              }
              return true;
            }
          }
          return false;
        }
      });
    handle(task, 0L);
    for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext())
    {
      if (taskPending == task) {
        return;
      }
      if (taskPending.getTaskId() == taskId) {
        taskPending.cancel0();
      }
    }
  }
  
  public void cancelTasks(final Plugin plugin)
  {
    Validate.notNull(plugin, "Cannot cancel tasks of null plugin");
    CraftTask task = new CraftTask(
      new Runnable()
      {
        public void run()
        {
          check(CraftScheduler.this.pending);
          check(CraftScheduler.this.temp);
        }
        
        void check(Iterable<CraftTask> collection)
        {
          Iterator<CraftTask> tasks = collection.iterator();
          while (tasks.hasNext())
          {
            CraftTask task = (CraftTask)tasks.next();
            if (task.getOwner().equals(plugin))
            {
              task.cancel0();
              tasks.remove();
              if (task.isSync()) {
                CraftScheduler.this.runners.remove(Integer.valueOf(task.getTaskId()));
              }
            }
          }
        }
      });
    handle(task, 0L);
    for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext())
    {
      if (taskPending == task) {
        return;
      }
      if ((taskPending.getTaskId() != -1) && (taskPending.getOwner().equals(plugin))) {
        taskPending.cancel0();
      }
    }
    for (CraftTask runner : this.runners.values()) {
      if (runner.getOwner().equals(plugin)) {
        runner.cancel0();
      }
    }
  }
  
  public void cancelAllTasks()
  {
    CraftTask task = new CraftTask(
      new Runnable()
      {
        public void run()
        {
          Iterator<CraftTask> it = CraftScheduler.this.runners.values().iterator();
          while (it.hasNext())
          {
            CraftTask task = (CraftTask)it.next();
            task.cancel0();
            if (task.isSync()) {
              it.remove();
            }
          }
          CraftScheduler.this.pending.clear();
          CraftScheduler.this.temp.clear();
        }
      });
    handle(task, 0L);
    for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext())
    {
      if (taskPending == task) {
        break;
      }
      taskPending.cancel0();
    }
    for (CraftTask runner : this.runners.values()) {
      runner.cancel0();
    }
  }
  
  /* Error */
  public boolean isCurrentlyRunning(int taskId)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 87	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftScheduler:runners	Ljava/util/concurrent/ConcurrentHashMap;
    //   4: iload_1
    //   5: invokestatic 206	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   8: invokevirtual 210	java/util/concurrent/ConcurrentHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   11: checkcast 57	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftTask
    //   14: astore_2
    //   15: aload_2
    //   16: ifnull +10 -> 26
    //   19: aload_2
    //   20: invokevirtual 277	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftTask:isSync	()Z
    //   23: ifeq +5 -> 28
    //   26: iconst_0
    //   27: ireturn
    //   28: aload_2
    //   29: checkcast 184	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask
    //   32: astore_3
    //   33: aload_3
    //   34: invokevirtual 281	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getWorkers	()Ljava/util/LinkedList;
    //   37: dup
    //   38: astore 4
    //   40: monitorenter
    //   41: aload_3
    //   42: invokevirtual 281	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftAsyncTask:getWorkers	()Ljava/util/LinkedList;
    //   45: invokevirtual 286	java/util/LinkedList:isEmpty	()Z
    //   48: aload 4
    //   50: monitorexit
    //   51: ireturn
    //   52: aload 4
    //   54: monitorexit
    //   55: athrow
    // Line number table:
    //   Java source line #268	-> byte code offset #0
    //   Java source line #269	-> byte code offset #15
    //   Java source line #270	-> byte code offset #26
    //   Java source line #272	-> byte code offset #28
    //   Java source line #273	-> byte code offset #33
    //   Java source line #274	-> byte code offset #41
    //   Java source line #273	-> byte code offset #52
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	56	0	this	CraftScheduler
    //   0	56	1	taskId	int
    //   14	15	2	task	CraftTask
    //   32	10	3	asyncTask	CraftAsyncTask
    //   38	15	4	Ljava/lang/Object;	Object
    // Exception table:
    //   from	to	target	type
    //   41	51	52	finally
    //   52	55	52	finally
  }
  
  public boolean isQueued(int taskId)
  {
    if (taskId <= 0) {
      return false;
    }
    for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
      if (task.getTaskId() == taskId) {
        return task.getPeriod() >= -1L;
      }
    }
    CraftTask task = (CraftTask)this.runners.get(Integer.valueOf(taskId));
    return (task != null) && (task.getPeriod() >= -1L);
  }
  
  public List<BukkitWorker> getActiveWorkers()
  {
    ArrayList<BukkitWorker> workers = new ArrayList();
    for (CraftTask taskObj : this.runners.values()) {
      if (!taskObj.isSync())
      {
        CraftAsyncTask task = (CraftAsyncTask)taskObj;
        synchronized (task.getWorkers())
        {
          workers.addAll(task.getWorkers());
        }
      }
    }
    return workers;
  }
  
  public List<BukkitTask> getPendingTasks()
  {
    ArrayList<CraftTask> truePending = new ArrayList();
    for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
      if (task.getTaskId() != -1) {
        truePending.add(task);
      }
    }
    ArrayList<BukkitTask> pending = new ArrayList();
    for (CraftTask task : this.runners.values()) {
      if (task.getPeriod() >= -1L) {
        pending.add(task);
      }
    }
    for (CraftTask task : truePending) {
      if ((task.getPeriod() >= -1L) && (!pending.contains(task))) {
        pending.add(task);
      }
    }
    return pending;
  }
  
  public void mainThreadHeartbeat(int currentTick)
  {
    this.currentTick = currentTick;
    List<CraftTask> temp = this.temp;
    parsePending();
    while (isReady(currentTick))
    {
      CraftTask task = (CraftTask)this.pending.remove();
      if (task.getPeriod() < -1L)
      {
        if (task.isSync()) {
          this.runners.remove(Integer.valueOf(task.getTaskId()), task);
        }
        parsePending();
      }
      else
      {
        if (task.isSync())
        {
          try
          {
            task.timings.startTiming();
            task.run();
            task.timings.stopTiming();
          }
          catch (Throwable throwable)
          {
            task.getOwner().getLogger().log(
              Level.WARNING, 
              String.format(
              "Task #%s for %s generated an exception", new Object[] {
              Integer.valueOf(task.getTaskId()), 
              task.getOwner().getDescription().getFullName() }), 
              throwable);
          }
          parsePending();
        }
        else
        {
          this.debugTail = this.debugTail.setNext(new CraftAsyncDebugger(currentTick + RECENT_TICKS, task.getOwner(), task.getTaskClass()));
          this.executor.execute(task);
        }
        long period = task.getPeriod();
        if (period > 0L)
        {
          task.setNextRun(currentTick + period);
          temp.add(task);
        }
        else if (task.isSync())
        {
          this.runners.remove(Integer.valueOf(task.getTaskId()));
        }
      }
    }
    this.pending.addAll(temp);
    temp.clear();
    this.debugHead = this.debugHead.getNextHead(currentTick);
  }
  
  private void addTask(CraftTask task)
  {
    AtomicReference<CraftTask> tail = this.tail;
    CraftTask tailTask = (CraftTask)tail.get();
    while (!tail.compareAndSet(tailTask, task)) {
      tailTask = (CraftTask)tail.get();
    }
    tailTask.setNext(task);
  }
  
  private CraftTask handle(CraftTask task, long delay)
  {
    task.setNextRun(this.currentTick + delay);
    addTask(task);
    return task;
  }
  
  private static void validate(Plugin plugin, Object task)
  {
    Validate.notNull(plugin, "Plugin cannot be null");
    Validate.notNull(task, "Task cannot be null");
    if (!plugin.isEnabled()) {
      throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
    }
  }
  
  private int nextId()
  {
    return this.ids.incrementAndGet();
  }
  
  private void parsePending()
  {
    CraftTask head = this.head;
    CraftTask task = head.getNext();
    CraftTask lastTask = head;
    for (; task != null; task = (lastTask = task).getNext()) {
      if (task.getTaskId() == -1)
      {
        task.run();
      }
      else if (task.getPeriod() >= -1L)
      {
        this.pending.add(task);
        this.runners.put(Integer.valueOf(task.getTaskId()), task);
      }
    }
    for (task = head; task != lastTask; task = head)
    {
      head = task.getNext();
      task.setNext(null);
    }
    this.head = lastTask;
  }
  
  private boolean isReady(int currentTick)
  {
    return (!this.pending.isEmpty()) && (((CraftTask)this.pending.peek()).getNextRun() <= currentTick);
  }
  
  public String toString()
  {
    int debugTick = this.currentTick;
    StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - RECENT_TICKS).append('-').append(debugTick).append('{');
    this.debugHead.debugTo(string);
    return '}';
  }
  
  @Deprecated
  public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay)
  {
    return scheduleSyncDelayedTask(plugin, task, delay);
  }
  
  @Deprecated
  public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task)
  {
    return scheduleSyncDelayedTask(plugin, task);
  }
  
  @Deprecated
  public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period)
  {
    return scheduleSyncRepeatingTask(plugin, task, delay, period);
  }
  
  @Deprecated
  public BukkitTask runTask(Plugin plugin, BukkitRunnable task)
    throws IllegalArgumentException
  {
    return runTask(plugin, task);
  }
  
  @Deprecated
  public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task)
    throws IllegalArgumentException
  {
    return runTaskAsynchronously(plugin, task);
  }
  
  @Deprecated
  public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay)
    throws IllegalArgumentException
  {
    return runTaskLater(plugin, task, delay);
  }
  
  @Deprecated
  public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay)
    throws IllegalArgumentException
  {
    return runTaskLaterAsynchronously(plugin, task, delay);
  }
  
  @Deprecated
  public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period)
    throws IllegalArgumentException
  {
    return runTaskTimer(plugin, task, delay, period);
  }
  
  @Deprecated
  public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period)
    throws IllegalArgumentException
  {
    return runTaskTimerAsynchronously(plugin, task, delay, period);
  }
}
