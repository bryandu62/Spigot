package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class SingleThreadEventExecutor
  extends AbstractEventExecutor
{
  private static final InternalLogger logger;
  private static final int ST_NOT_STARTED = 1;
  private static final int ST_STARTED = 2;
  private static final int ST_SHUTTING_DOWN = 3;
  private static final int ST_SHUTDOWN = 4;
  private static final int ST_TERMINATED = 5;
  private static final Runnable WAKEUP_TASK;
  private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER;
  private final EventExecutorGroup parent;
  private final Queue<Runnable> taskQueue;
  
  static
  {
    logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
    
    WAKEUP_TASK = new Runnable()
    {
      public void run() {}
    };
    AtomicIntegerFieldUpdater<SingleThreadEventExecutor> updater = PlatformDependent.newAtomicIntegerFieldUpdater(SingleThreadEventExecutor.class, "state");
    if (updater == null) {
      updater = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
    }
    STATE_UPDATER = updater;
  }
  
  final Queue<ScheduledFutureTask<?>> delayedTaskQueue = new PriorityQueue();
  private final Thread thread;
  private final Semaphore threadLock = new Semaphore(0);
  private final Set<Runnable> shutdownHooks = new LinkedHashSet();
  private final boolean addTaskWakesUp;
  private long lastExecutionTime;
  private volatile int state = 1;
  private volatile long gracefulShutdownQuietPeriod;
  private volatile long gracefulShutdownTimeout;
  private long gracefulShutdownStartTime;
  private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
  
  protected SingleThreadEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp)
  {
    if (threadFactory == null) {
      throw new NullPointerException("threadFactory");
    }
    this.parent = parent;
    this.addTaskWakesUp = addTaskWakesUp;
    
    this.thread = threadFactory.newThread(new Runnable()
    {
      public void run()
      {
        boolean success = false;
        SingleThreadEventExecutor.this.updateLastExecutionTime();
        try
        {
          SingleThreadEventExecutor.this.run();
          success = true;
        }
        catch (Throwable t)
        {
          int oldState;
          SingleThreadEventExecutor.logger.warn("Unexpected exception from an event executor: ", t);
        }
        finally
        {
          for (;;)
          {
            int oldState;
            int oldState = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
            if ((oldState >= 3) || (SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState, 3))) {
              break;
            }
          }
          if ((success) && (SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L)) {
            SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
          }
          try
          {
            for (;;)
            {
              if (SingleThreadEventExecutor.this.confirmShutdown()) {
                break;
              }
            }
          }
          finally
          {
            try
            {
              SingleThreadEventExecutor.this.cleanup();
            }
            finally
            {
              SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
              SingleThreadEventExecutor.this.threadLock.release();
              if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
              }
              SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
            }
          }
        }
      }
    });
    this.taskQueue = newTaskQueue();
  }
  
  protected Queue<Runnable> newTaskQueue()
  {
    return new LinkedBlockingQueue();
  }
  
  public EventExecutorGroup parent()
  {
    return this.parent;
  }
  
  protected void interruptThread()
  {
    this.thread.interrupt();
  }
  
  protected Runnable pollTask()
  {
    assert (inEventLoop());
    Runnable task;
    do
    {
      task = (Runnable)this.taskQueue.poll();
    } while (task == WAKEUP_TASK);
    return task;
  }
  
  protected Runnable takeTask()
  {
    assert (inEventLoop());
    if (!(this.taskQueue instanceof BlockingQueue)) {
      throw new UnsupportedOperationException();
    }
    BlockingQueue<Runnable> taskQueue = (BlockingQueue)this.taskQueue;
    for (;;)
    {
      ScheduledFutureTask<?> delayedTask = (ScheduledFutureTask)this.delayedTaskQueue.peek();
      if (delayedTask == null)
      {
        Runnable task = null;
        try
        {
          task = (Runnable)taskQueue.take();
          if (task == WAKEUP_TASK) {
            task = null;
          }
        }
        catch (InterruptedException e) {}
        return task;
      }
      long delayNanos = delayedTask.delayNanos();
      Runnable task = null;
      if (delayNanos > 0L) {
        try
        {
          task = (Runnable)taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException e)
        {
          return null;
        }
      }
      if (task == null)
      {
        fetchFromDelayedQueue();
        task = (Runnable)taskQueue.poll();
      }
      if (task != null) {
        return task;
      }
    }
  }
  
  private void fetchFromDelayedQueue()
  {
    long nanoTime = 0L;
    for (;;)
    {
      ScheduledFutureTask<?> delayedTask = (ScheduledFutureTask)this.delayedTaskQueue.peek();
      if (delayedTask == null) {
        break;
      }
      if (nanoTime == 0L) {
        nanoTime = ScheduledFutureTask.nanoTime();
      }
      if (delayedTask.deadlineNanos() > nanoTime) {
        break;
      }
      this.delayedTaskQueue.remove();
      this.taskQueue.add(delayedTask);
    }
  }
  
  protected Runnable peekTask()
  {
    assert (inEventLoop());
    return (Runnable)this.taskQueue.peek();
  }
  
  protected boolean hasTasks()
  {
    assert (inEventLoop());
    return !this.taskQueue.isEmpty();
  }
  
  protected boolean hasScheduledTasks()
  {
    assert (inEventLoop());
    ScheduledFutureTask<?> delayedTask = (ScheduledFutureTask)this.delayedTaskQueue.peek();
    return (delayedTask != null) && (delayedTask.deadlineNanos() <= ScheduledFutureTask.nanoTime());
  }
  
  public final int pendingTasks()
  {
    return this.taskQueue.size();
  }
  
  protected void addTask(Runnable task)
  {
    if (task == null) {
      throw new NullPointerException("task");
    }
    if (isShutdown()) {
      reject();
    }
    this.taskQueue.add(task);
  }
  
  protected boolean removeTask(Runnable task)
  {
    if (task == null) {
      throw new NullPointerException("task");
    }
    return this.taskQueue.remove(task);
  }
  
  protected boolean runAllTasks()
  {
    fetchFromDelayedQueue();
    Runnable task = pollTask();
    if (task == null) {
      return false;
    }
    do
    {
      try
      {
        task.run();
      }
      catch (Throwable t)
      {
        logger.warn("A task raised an exception.", t);
      }
      task = pollTask();
    } while (task != null);
    this.lastExecutionTime = ScheduledFutureTask.nanoTime();
    return true;
  }
  
  protected boolean runAllTasks(long timeoutNanos)
  {
    fetchFromDelayedQueue();
    Runnable task = pollTask();
    if (task == null) {
      return false;
    }
    long deadline = ScheduledFutureTask.nanoTime() + timeoutNanos;
    long runTasks = 0L;
    do
    {
      try
      {
        task.run();
      }
      catch (Throwable t)
      {
        logger.warn("A task raised an exception.", t);
      }
      runTasks += 1L;
      if ((runTasks & 0x3F) == 0L)
      {
        long lastExecutionTime = ScheduledFutureTask.nanoTime();
        if (lastExecutionTime >= deadline) {
          break;
        }
      }
      task = pollTask();
    } while (task != null);
    long lastExecutionTime = ScheduledFutureTask.nanoTime();
    
    this.lastExecutionTime = lastExecutionTime;
    return true;
  }
  
  protected long delayNanos(long currentTimeNanos)
  {
    ScheduledFutureTask<?> delayedTask = (ScheduledFutureTask)this.delayedTaskQueue.peek();
    if (delayedTask == null) {
      return SCHEDULE_PURGE_INTERVAL;
    }
    return delayedTask.delayNanos(currentTimeNanos);
  }
  
  protected void updateLastExecutionTime()
  {
    this.lastExecutionTime = ScheduledFutureTask.nanoTime();
  }
  
  protected void wakeup(boolean inEventLoop)
  {
    if ((!inEventLoop) || (STATE_UPDATER.get(this) == 3)) {
      this.taskQueue.add(WAKEUP_TASK);
    }
  }
  
  public boolean inEventLoop(Thread thread)
  {
    return thread == this.thread;
  }
  
  public void addShutdownHook(final Runnable task)
  {
    if (inEventLoop()) {
      this.shutdownHooks.add(task);
    } else {
      execute(new Runnable()
      {
        public void run()
        {
          SingleThreadEventExecutor.this.shutdownHooks.add(task);
        }
      });
    }
  }
  
  public void removeShutdownHook(final Runnable task)
  {
    if (inEventLoop()) {
      this.shutdownHooks.remove(task);
    } else {
      execute(new Runnable()
      {
        public void run()
        {
          SingleThreadEventExecutor.this.shutdownHooks.remove(task);
        }
      });
    }
  }
  
  private boolean runShutdownHooks()
  {
    boolean ran = false;
    while (!this.shutdownHooks.isEmpty())
    {
      List<Runnable> copy = new ArrayList(this.shutdownHooks);
      this.shutdownHooks.clear();
      for (Runnable task : copy) {
        try
        {
          task.run();
        }
        catch (Throwable t)
        {
          logger.warn("Shutdown hook raised an exception.", t);
        }
        finally
        {
          ran = true;
        }
      }
    }
    if (ran) {
      this.lastExecutionTime = ScheduledFutureTask.nanoTime();
    }
    return ran;
  }
  
  public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
  {
    if (quietPeriod < 0L) {
      throw new IllegalArgumentException("quietPeriod: " + quietPeriod + " (expected >= 0)");
    }
    if (timeout < quietPeriod) {
      throw new IllegalArgumentException("timeout: " + timeout + " (expected >= quietPeriod (" + quietPeriod + "))");
    }
    if (unit == null) {
      throw new NullPointerException("unit");
    }
    if (isShuttingDown()) {
      return terminationFuture();
    }
    boolean inEventLoop = inEventLoop();
    boolean wakeup;
    int oldState;
    for (;;)
    {
      if (isShuttingDown()) {
        return terminationFuture();
      }
      wakeup = true;
      oldState = STATE_UPDATER.get(this);
      int newState;
      int newState;
      if (inEventLoop) {
        newState = 3;
      } else {
        switch (oldState)
        {
        case 1: 
        case 2: 
          newState = 3;
          break;
        default: 
          newState = oldState;
          wakeup = false;
        }
      }
      if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
        break;
      }
    }
    this.gracefulShutdownQuietPeriod = unit.toNanos(quietPeriod);
    this.gracefulShutdownTimeout = unit.toNanos(timeout);
    if (oldState == 1) {
      this.thread.start();
    }
    if (wakeup) {
      wakeup(inEventLoop);
    }
    return terminationFuture();
  }
  
  public Future<?> terminationFuture()
  {
    return this.terminationFuture;
  }
  
  @Deprecated
  public void shutdown()
  {
    if (isShutdown()) {
      return;
    }
    boolean inEventLoop = inEventLoop();
    boolean wakeup;
    int oldState;
    for (;;)
    {
      if (isShuttingDown()) {
        return;
      }
      wakeup = true;
      oldState = STATE_UPDATER.get(this);
      int newState;
      int newState;
      if (inEventLoop) {
        newState = 4;
      } else {
        switch (oldState)
        {
        case 1: 
        case 2: 
        case 3: 
          newState = 4;
          break;
        default: 
          newState = oldState;
          wakeup = false;
        }
      }
      if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
        break;
      }
    }
    if (oldState == 1) {
      this.thread.start();
    }
    if (wakeup) {
      wakeup(inEventLoop);
    }
  }
  
  public boolean isShuttingDown()
  {
    return STATE_UPDATER.get(this) >= 3;
  }
  
  public boolean isShutdown()
  {
    return STATE_UPDATER.get(this) >= 4;
  }
  
  public boolean isTerminated()
  {
    return STATE_UPDATER.get(this) == 5;
  }
  
  protected boolean confirmShutdown()
  {
    if (!isShuttingDown()) {
      return false;
    }
    if (!inEventLoop()) {
      throw new IllegalStateException("must be invoked from an event loop");
    }
    cancelDelayedTasks();
    if (this.gracefulShutdownStartTime == 0L) {
      this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
    }
    if ((runAllTasks()) || (runShutdownHooks()))
    {
      if (isShutdown()) {
        return true;
      }
      wakeup(true);
      return false;
    }
    long nanoTime = ScheduledFutureTask.nanoTime();
    if ((isShutdown()) || (nanoTime - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout)) {
      return true;
    }
    if (nanoTime - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod)
    {
      wakeup(true);
      try
      {
        Thread.sleep(100L);
      }
      catch (InterruptedException e) {}
      return false;
    }
    return true;
  }
  
  private void cancelDelayedTasks()
  {
    if (this.delayedTaskQueue.isEmpty()) {
      return;
    }
    ScheduledFutureTask<?>[] delayedTasks = (ScheduledFutureTask[])this.delayedTaskQueue.toArray(new ScheduledFutureTask[this.delayedTaskQueue.size()]);
    for (ScheduledFutureTask<?> task : delayedTasks) {
      task.cancel(false);
    }
    this.delayedTaskQueue.clear();
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit)
    throws InterruptedException
  {
    if (unit == null) {
      throw new NullPointerException("unit");
    }
    if (inEventLoop()) {
      throw new IllegalStateException("cannot await termination of the current thread");
    }
    if (this.threadLock.tryAcquire(timeout, unit)) {
      this.threadLock.release();
    }
    return isTerminated();
  }
  
  public void execute(Runnable task)
  {
    if (task == null) {
      throw new NullPointerException("task");
    }
    boolean inEventLoop = inEventLoop();
    if (inEventLoop)
    {
      addTask(task);
    }
    else
    {
      startThread();
      addTask(task);
      if ((isShutdown()) && (removeTask(task))) {
        reject();
      }
    }
    if ((!this.addTaskWakesUp) && (wakesUpForTask(task))) {
      wakeup(inEventLoop);
    }
  }
  
  protected boolean wakesUpForTask(Runnable task)
  {
    return true;
  }
  
  protected static void reject()
  {
    throw new RejectedExecutionException("event executor terminated");
  }
  
  private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
  
  public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
  {
    if (command == null) {
      throw new NullPointerException("command");
    }
    if (unit == null) {
      throw new NullPointerException("unit");
    }
    if (delay < 0L) {
      throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[] { Long.valueOf(delay) }));
    }
    return schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, command, null, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
  }
  
  public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
  {
    if (callable == null) {
      throw new NullPointerException("callable");
    }
    if (unit == null) {
      throw new NullPointerException("unit");
    }
    if (delay < 0L) {
      throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[] { Long.valueOf(delay) }));
    }
    return schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, callable, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
  }
  
  public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
  {
    if (command == null) {
      throw new NullPointerException("command");
    }
    if (unit == null) {
      throw new NullPointerException("unit");
    }
    if (initialDelay < 0L) {
      throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) }));
    }
    if (period <= 0L) {
      throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", new Object[] { Long.valueOf(period) }));
    }
    return schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
  }
  
  public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
  {
    if (command == null) {
      throw new NullPointerException("command");
    }
    if (unit == null) {
      throw new NullPointerException("unit");
    }
    if (initialDelay < 0L) {
      throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) }));
    }
    if (delay <= 0L) {
      throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", new Object[] { Long.valueOf(delay) }));
    }
    return schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
  }
  
  private <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task)
  {
    if (task == null) {
      throw new NullPointerException("task");
    }
    if (inEventLoop()) {
      this.delayedTaskQueue.add(task);
    } else {
      execute(new Runnable()
      {
        public void run()
        {
          SingleThreadEventExecutor.this.delayedTaskQueue.add(task);
        }
      });
    }
    return task;
  }
  
  private void startThread()
  {
    if ((STATE_UPDATER.get(this) == 1) && 
      (STATE_UPDATER.compareAndSet(this, 1, 2)))
    {
      this.delayedTaskQueue.add(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(new PurgeTask(null), null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL));
      
      this.thread.start();
    }
  }
  
  protected abstract void run();
  
  protected void cleanup() {}
  
  private final class PurgeTask
    implements Runnable
  {
    private PurgeTask() {}
    
    public void run()
    {
      Iterator<ScheduledFutureTask<?>> i = SingleThreadEventExecutor.this.delayedTaskQueue.iterator();
      while (i.hasNext())
      {
        ScheduledFutureTask<?> task = (ScheduledFutureTask)i.next();
        if (task.isCancelled()) {
          i.remove();
        }
      }
    }
  }
}
