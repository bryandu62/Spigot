package org.bukkit.craftbukkit.v1_8_R3.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import org.apache.commons.lang.Validate;

public final class AsynchronousExecutor<P, T, C, E extends Throwable>
{
  static final AtomicIntegerFieldUpdater STATE_FIELD = AtomicIntegerFieldUpdater.newUpdater(Task.class, "state");
  final CallBackProvider<P, T, C, E> provider;
  
  private static boolean set(Task $this, int expected, int value)
  {
    return STATE_FIELD.compareAndSet($this, expected, value);
  }
  
  public static abstract interface CallBackProvider<P, T, C, E extends Throwable>
    extends ThreadFactory
  {
    public abstract T callStage1(P paramP)
      throws Throwable;
    
    public abstract void callStage2(P paramP, T paramT)
      throws Throwable;
    
    public abstract void callStage3(P paramP, T paramT, C paramC)
      throws Throwable;
  }
  
  class Task
    implements Runnable
  {
    static final int PENDING = 0;
    static final int STAGE_1_ASYNC = 1;
    static final int STAGE_1_SYNC = 2;
    static final int STAGE_1_COMPLETE = 3;
    static final int FINISHED = 4;
    volatile int state = 0;
    final P parameter;
    T object;
    final List<C> callbacks = new LinkedList();
    E t = null;
    
    Task()
    {
      this.parameter = parameter;
    }
    
    public void run()
    {
      if (initAsync()) {
        AsynchronousExecutor.this.finished.add(this);
      }
    }
    
    /* Error */
    boolean initAsync()
    {
      // Byte code:
      //   0: aload_0
      //   1: iconst_0
      //   2: iconst_1
      //   3: invokestatic 77	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor:access$0	(Lorg/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task;II)Z
      //   6: ifeq +97 -> 103
      //   9: iconst_1
      //   10: istore_1
      //   11: aload_0
      //   12: invokevirtual 80	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task:init	()V
      //   15: goto +46 -> 61
      //   18: astore_2
      //   19: aload_0
      //   20: iconst_1
      //   21: iconst_3
      //   22: invokestatic 77	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor:access$0	(Lorg/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task;II)Z
      //   25: ifne +34 -> 59
      //   28: aload_0
      //   29: dup
      //   30: astore_3
      //   31: monitorenter
      //   32: aload_0
      //   33: getfield 47	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task:state	I
      //   36: iconst_2
      //   37: if_icmpeq +7 -> 44
      //   40: aload_0
      //   41: invokevirtual 85	java/lang/Object:notifyAll	()V
      //   44: aload_0
      //   45: iconst_3
      //   46: putfield 47	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task:state	I
      //   49: aload_3
      //   50: monitorexit
      //   51: goto +6 -> 57
      //   54: aload_3
      //   55: monitorexit
      //   56: athrow
      //   57: iconst_0
      //   58: istore_1
      //   59: aload_2
      //   60: athrow
      //   61: aload_0
      //   62: iconst_1
      //   63: iconst_3
      //   64: invokestatic 77	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor:access$0	(Lorg/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task;II)Z
      //   67: ifne +34 -> 101
      //   70: aload_0
      //   71: dup
      //   72: astore_3
      //   73: monitorenter
      //   74: aload_0
      //   75: getfield 47	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task:state	I
      //   78: iconst_2
      //   79: if_icmpeq +7 -> 86
      //   82: aload_0
      //   83: invokevirtual 85	java/lang/Object:notifyAll	()V
      //   86: aload_0
      //   87: iconst_3
      //   88: putfield 47	org/bukkit/craftbukkit/v1_8_R3/util/AsynchronousExecutor$Task:state	I
      //   91: aload_3
      //   92: monitorexit
      //   93: goto +6 -> 99
      //   96: aload_3
      //   97: monitorexit
      //   98: athrow
      //   99: iconst_0
      //   100: istore_1
      //   101: iload_1
      //   102: ireturn
      //   103: iconst_0
      //   104: ireturn
      // Line number table:
      //   Java source line #91	-> byte code offset #0
      //   Java source line #92	-> byte code offset #9
      //   Java source line #95	-> byte code offset #11
      //   Java source line #96	-> byte code offset #15
      //   Java source line #97	-> byte code offset #19
      //   Java source line #101	-> byte code offset #28
      //   Java source line #102	-> byte code offset #32
      //   Java source line #104	-> byte code offset #40
      //   Java source line #108	-> byte code offset #44
      //   Java source line #101	-> byte code offset #49
      //   Java source line #111	-> byte code offset #57
      //   Java source line #113	-> byte code offset #59
      //   Java source line #97	-> byte code offset #61
      //   Java source line #101	-> byte code offset #70
      //   Java source line #102	-> byte code offset #74
      //   Java source line #104	-> byte code offset #82
      //   Java source line #108	-> byte code offset #86
      //   Java source line #101	-> byte code offset #91
      //   Java source line #111	-> byte code offset #99
      //   Java source line #115	-> byte code offset #101
      //   Java source line #117	-> byte code offset #103
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	105	0	this	Task
      //   10	92	1	ret	boolean
      //   18	42	2	localObject1	Object
      // Exception table:
      //   from	to	target	type
      //   11	18	18	finally
      //   32	51	54	finally
      //   54	56	54	finally
      //   74	93	96	finally
      //   96	98	96	finally
    }
    
    void initSync()
    {
      if (AsynchronousExecutor.set(this, 0, 3)) {
        init();
      } else if (AsynchronousExecutor.set(this, 1, 2)) {
        synchronized (this)
        {
          if (AsynchronousExecutor.set(this, 2, 0)) {
            while (this.state != 3) {
              try
              {
                wait();
              }
              catch (InterruptedException e)
              {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Unable to handle interruption on " + this.parameter, e);
              }
            }
          }
        }
      }
    }
    
    void init()
    {
      try
      {
        this.object = AsynchronousExecutor.this.provider.callStage1(this.parameter);
      }
      catch (Throwable t)
      {
        this.t = t;
      }
    }
    
    T get()
      throws Throwable
    {
      initSync();
      if (this.callbacks.isEmpty()) {
        this.callbacks.add(this);
      }
      finish();
      return (T)this.object;
    }
    
    void finish()
      throws Throwable
    {
      switch (this.state)
      {
      case 0: 
      case 1: 
      case 2: 
      default: 
        throw new IllegalStateException("Attempting to finish unprepared(" + this.state + ") task(" + this.parameter + ")");
      case 3: 
        try
        {
          if (this.t != null) {
            throw this.t;
          }
          if (this.callbacks.isEmpty()) {
            return;
          }
          AsynchronousExecutor.CallBackProvider<P, T, C, E> provider = AsynchronousExecutor.this.provider;
          P parameter = this.parameter;
          T object = this.object;
          
          provider.callStage2(parameter, object);
          for (C callback : this.callbacks) {
            if (callback != this) {
              provider.callStage3(parameter, object, callback);
            }
          }
        }
        finally
        {
          AsynchronousExecutor.this.tasks.remove(this.parameter);
          this.state = 4;
        }
        AsynchronousExecutor.this.tasks.remove(this.parameter);
        this.state = 4;
      }
    }
    
    boolean drop()
    {
      if (AsynchronousExecutor.set(this, 0, 4))
      {
        AsynchronousExecutor.this.tasks.remove(this.parameter);
        return true;
      }
      return false;
    }
  }
  
  final Queue<AsynchronousExecutor<P, T, C, E>.Task> finished = new ConcurrentLinkedQueue();
  final Map<P, AsynchronousExecutor<P, T, C, E>.Task> tasks = new HashMap();
  final ThreadPoolExecutor pool;
  
  public AsynchronousExecutor(CallBackProvider<P, T, C, E> provider, int coreSize)
  {
    Validate.notNull(provider, "Provider cannot be null");
    this.provider = provider;
    
    this.pool = new ThreadPoolExecutor(coreSize, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), provider);
  }
  
  public void add(P parameter, C callback)
  {
    AsynchronousExecutor<P, T, C, E>.Task task = (Task)this.tasks.get(parameter);
    if (task == null)
    {
      this.tasks.put(parameter, task = new Task(parameter));
      this.pool.execute(task);
    }
    task.callbacks.add(callback);
  }
  
  public boolean drop(P parameter, C callback)
    throws IllegalStateException
  {
    AsynchronousExecutor<P, T, C, E>.Task task = (Task)this.tasks.get(parameter);
    if (task == null) {
      return true;
    }
    if (!task.callbacks.remove(callback)) {
      throw new IllegalStateException("Unknown " + callback + " for " + parameter);
    }
    if (task.callbacks.isEmpty()) {
      return task.drop();
    }
    return false;
  }
  
  public T get(P parameter)
    throws Throwable, IllegalStateException
  {
    AsynchronousExecutor<P, T, C, E>.Task task = (Task)this.tasks.get(parameter);
    if (task == null) {
      throw new IllegalStateException("Unknown " + parameter);
    }
    return (T)task.get();
  }
  
  public T getSkipQueue(P parameter)
    throws Throwable
  {
    return (T)skipQueue(parameter);
  }
  
  public T getSkipQueue(P parameter, C callback)
    throws Throwable
  {
    T object = skipQueue(parameter);
    this.provider.callStage3(parameter, object, callback);
    return object;
  }
  
  public T getSkipQueue(P parameter, C... callbacks)
    throws Throwable
  {
    CallBackProvider<P, T, C, E> provider = this.provider;
    T object = skipQueue(parameter);
    Object[] arrayOfObject;
    int i = (arrayOfObject = callbacks).length;
    for (int j = 0; j < i; j++)
    {
      C callback = arrayOfObject[j];
      provider.callStage3(parameter, object, callback);
    }
    return object;
  }
  
  public T getSkipQueue(P parameter, Iterable<C> callbacks)
    throws Throwable
  {
    CallBackProvider<P, T, C, E> provider = this.provider;
    T object = skipQueue(parameter);
    for (C callback : callbacks) {
      provider.callStage3(parameter, object, callback);
    }
    return object;
  }
  
  private T skipQueue(P parameter)
    throws Throwable
  {
    AsynchronousExecutor<P, T, C, E>.Task task = (Task)this.tasks.get(parameter);
    if (task != null) {
      return (T)task.get();
    }
    T object = this.provider.callStage1(parameter);
    this.provider.callStage2(parameter, object);
    return object;
  }
  
  public void finishActive()
    throws Throwable
  {
    Queue<AsynchronousExecutor<P, T, C, E>.Task> finished = this.finished;
    while (!finished.isEmpty()) {
      ((Task)finished.poll()).finish();
    }
  }
  
  public void setActiveThreads(int coreSize)
  {
    this.pool.setCorePoolSize(coreSize);
  }
}
