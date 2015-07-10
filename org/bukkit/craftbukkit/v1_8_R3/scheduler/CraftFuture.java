package org.bukkit.craftbukkit.v1_8_R3.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.bukkit.plugin.Plugin;

class CraftFuture<T>
  extends CraftTask
  implements Future<T>
{
  private final Callable<T> callable;
  private T value;
  private Exception exception = null;
  
  CraftFuture(Callable<T> callable, Plugin plugin, int id)
  {
    super(plugin, null, id, -1L);
    this.callable = callable;
  }
  
  public synchronized boolean cancel(boolean mayInterruptIfRunning)
  {
    if (getPeriod() != -1L) {
      return false;
    }
    setPeriod(-2L);
    return true;
  }
  
  public boolean isCancelled()
  {
    return getPeriod() == -2L;
  }
  
  public boolean isDone()
  {
    long period = getPeriod();
    return (period != -1L) && (period != -3L);
  }
  
  public T get()
    throws CancellationException, InterruptedException, ExecutionException
  {
    try
    {
      return (T)get(0L, TimeUnit.MILLISECONDS);
    }
    catch (TimeoutException e)
    {
      throw new Error(e);
    }
  }
  
  public synchronized T get(long timeout, TimeUnit unit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    timeout = unit.toMillis(timeout);
    long period = getPeriod();
    long timestamp = timeout > 0L ? System.currentTimeMillis() : 0L;
    while ((period == -1L) || (period == -3L))
    {
      wait(timeout);
      period = getPeriod();
      if ((period == -1L) || (period == -3L)) {
        if (timeout != 0L)
        {
          timeout += timestamp - (timestamp = System.currentTimeMillis());
          if (timeout <= 0L) {
            throw new TimeoutException();
          }
        }
      }
    }
    if (period == -2L) {
      throw new CancellationException();
    }
    if (period == -4L)
    {
      if (this.exception == null) {
        return (T)this.value;
      }
      throw new ExecutionException(this.exception);
    }
    throw new IllegalStateException("Expected -1 to -4, got " + period);
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: aload_0
    //   1: dup
    //   2: astore_1
    //   3: monitorenter
    //   4: aload_0
    //   5: invokevirtual 40	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:getPeriod	()J
    //   8: ldc2_w 41
    //   11: lcmp
    //   12: ifne +6 -> 18
    //   15: aload_1
    //   16: monitorexit
    //   17: return
    //   18: aload_0
    //   19: ldc2_w 52
    //   22: invokevirtual 46	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:setPeriod	(J)V
    //   25: aload_1
    //   26: monitorexit
    //   27: goto +6 -> 33
    //   30: aload_1
    //   31: monitorexit
    //   32: athrow
    //   33: aload_0
    //   34: aload_0
    //   35: getfield 27	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:callable	Ljava/util/concurrent/Callable;
    //   38: invokeinterface 135 1 0
    //   43: putfield 103	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:value	Ljava/lang/Object;
    //   46: goto +58 -> 104
    //   49: astore_1
    //   50: aload_0
    //   51: aload_1
    //   52: putfield 25	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:exception	Ljava/lang/Exception;
    //   55: aload_0
    //   56: dup
    //   57: astore_2
    //   58: monitorenter
    //   59: aload_0
    //   60: ldc2_w 100
    //   63: invokevirtual 46	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:setPeriod	(J)V
    //   66: aload_0
    //   67: invokevirtual 138	java/lang/Object:notifyAll	()V
    //   70: aload_2
    //   71: monitorexit
    //   72: goto +55 -> 127
    //   75: aload_2
    //   76: monitorexit
    //   77: athrow
    //   78: astore_3
    //   79: aload_0
    //   80: dup
    //   81: astore_2
    //   82: monitorenter
    //   83: aload_0
    //   84: ldc2_w 100
    //   87: invokevirtual 46	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:setPeriod	(J)V
    //   90: aload_0
    //   91: invokevirtual 138	java/lang/Object:notifyAll	()V
    //   94: aload_2
    //   95: monitorexit
    //   96: goto +6 -> 102
    //   99: aload_2
    //   100: monitorexit
    //   101: athrow
    //   102: aload_3
    //   103: athrow
    //   104: aload_0
    //   105: dup
    //   106: astore_2
    //   107: monitorenter
    //   108: aload_0
    //   109: ldc2_w 100
    //   112: invokevirtual 46	org/bukkit/craftbukkit/v1_8_R3/scheduler/CraftFuture:setPeriod	(J)V
    //   115: aload_0
    //   116: invokevirtual 138	java/lang/Object:notifyAll	()V
    //   119: aload_2
    //   120: monitorexit
    //   121: goto +6 -> 127
    //   124: aload_2
    //   125: monitorexit
    //   126: athrow
    //   127: return
    // Line number table:
    //   Java source line #82	-> byte code offset #0
    //   Java source line #83	-> byte code offset #4
    //   Java source line #84	-> byte code offset #15
    //   Java source line #86	-> byte code offset #18
    //   Java source line #82	-> byte code offset #25
    //   Java source line #89	-> byte code offset #33
    //   Java source line #90	-> byte code offset #46
    //   Java source line #91	-> byte code offset #50
    //   Java source line #93	-> byte code offset #55
    //   Java source line #94	-> byte code offset #59
    //   Java source line #95	-> byte code offset #66
    //   Java source line #93	-> byte code offset #70
    //   Java source line #92	-> byte code offset #78
    //   Java source line #93	-> byte code offset #79
    //   Java source line #94	-> byte code offset #83
    //   Java source line #95	-> byte code offset #90
    //   Java source line #93	-> byte code offset #94
    //   Java source line #97	-> byte code offset #102
    //   Java source line #93	-> byte code offset #104
    //   Java source line #94	-> byte code offset #108
    //   Java source line #95	-> byte code offset #115
    //   Java source line #93	-> byte code offset #119
    //   Java source line #98	-> byte code offset #127
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	128	0	this	CraftFuture<T>
    //   2	29	1	Ljava/lang/Object;	Object
    //   49	3	1	e	Exception
    //   57	19	2	Ljava/lang/Object;	Object
    //   81	19	2	Ljava/lang/Object;	Object
    //   106	19	2	Ljava/lang/Object;	Object
    //   78	25	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   4	17	30	finally
    //   18	27	30	finally
    //   30	32	30	finally
    //   33	46	49	java/lang/Exception
    //   59	72	75	finally
    //   75	77	75	finally
    //   33	55	78	finally
    //   83	96	99	finally
    //   99	101	99	finally
    //   108	121	124	finally
    //   124	126	124	finally
  }
  
  synchronized boolean cancel0()
  {
    if (getPeriod() != -1L) {
      return false;
    }
    setPeriod(-2L);
    notifyAll();
    return true;
  }
}
