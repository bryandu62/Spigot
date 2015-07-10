package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Beta
public final class Uninterruptibles
{
  public static void awaitUninterruptibly(CountDownLatch latch)
  {
    boolean interrupted = false;
    try
    {
      latch.await();
    }
    catch (InterruptedException e)
    {
      for (;;)
      {
        interrupted = true;
      }
    }
    finally
    {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }
  
  /* Error */
  public static boolean awaitUninterruptibly(CountDownLatch latch, long timeout, java.util.concurrent.TimeUnit unit)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 4
    //   3: aload_3
    //   4: lload_1
    //   5: invokevirtual 40	java/util/concurrent/TimeUnit:toNanos	(J)J
    //   8: lstore 5
    //   10: invokestatic 46	java/lang/System:nanoTime	()J
    //   13: lload 5
    //   15: ladd
    //   16: lstore 7
    //   18: aload_0
    //   19: lload 5
    //   21: getstatic 50	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
    //   24: invokevirtual 53	java/util/concurrent/CountDownLatch:await	(JLjava/util/concurrent/TimeUnit;)Z
    //   27: istore 9
    //   29: iload 4
    //   31: ifeq +9 -> 40
    //   34: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   37: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   40: iload 9
    //   42: ireturn
    //   43: astore 9
    //   45: iconst_1
    //   46: istore 4
    //   48: lload 7
    //   50: invokestatic 46	java/lang/System:nanoTime	()J
    //   53: lsub
    //   54: lstore 5
    //   56: goto -38 -> 18
    //   59: astore 10
    //   61: iload 4
    //   63: ifeq +9 -> 72
    //   66: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   69: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   72: aload 10
    //   74: athrow
    // Line number table:
    //   Java source line #75	-> byte code offset #0
    //   Java source line #77	-> byte code offset #3
    //   Java source line #78	-> byte code offset #10
    //   Java source line #83	-> byte code offset #18
    //   Java source line #90	-> byte code offset #29
    //   Java source line #91	-> byte code offset #34
    //   Java source line #84	-> byte code offset #43
    //   Java source line #85	-> byte code offset #45
    //   Java source line #86	-> byte code offset #48
    //   Java source line #87	-> byte code offset #56
    //   Java source line #90	-> byte code offset #59
    //   Java source line #91	-> byte code offset #66
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	75	0	latch	CountDownLatch
    //   0	75	1	timeout	long
    //   0	75	3	unit	java.util.concurrent.TimeUnit
    //   1	61	4	interrupted	boolean
    //   8	47	5	remainingNanos	long
    //   16	33	7	end	long
    //   27	14	9	bool1	boolean
    //   43	3	9	e	InterruptedException
    //   59	14	10	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   18	29	43	java/lang/InterruptedException
    //   3	29	59	finally
    //   43	61	59	finally
  }
  
  public static void joinUninterruptibly(Thread toJoin)
  {
    boolean interrupted = false;
    try
    {
      toJoin.join();
    }
    catch (InterruptedException e)
    {
      for (;;)
      {
        interrupted = true;
      }
    }
    finally
    {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }
  
  public static <V> V getUninterruptibly(Future<V> future)
    throws ExecutionException
  {
    boolean interrupted = false;
    try
    {
      return (V)future.get();
    }
    catch (InterruptedException e)
    {
      for (;;)
      {
        interrupted = true;
      }
    }
    finally
    {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }
  
  /* Error */
  public static <V> V getUninterruptibly(Future<V> future, long timeout, java.util.concurrent.TimeUnit unit)
    throws ExecutionException, java.util.concurrent.TimeoutException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 4
    //   3: aload_3
    //   4: lload_1
    //   5: invokevirtual 40	java/util/concurrent/TimeUnit:toNanos	(J)J
    //   8: lstore 5
    //   10: invokestatic 46	java/lang/System:nanoTime	()J
    //   13: lload 5
    //   15: ladd
    //   16: lstore 7
    //   18: aload_0
    //   19: lload 5
    //   21: getstatic 50	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
    //   24: invokeinterface 84 4 0
    //   29: astore 9
    //   31: iload 4
    //   33: ifeq +9 -> 42
    //   36: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   39: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   42: aload 9
    //   44: areturn
    //   45: astore 9
    //   47: iconst_1
    //   48: istore 4
    //   50: lload 7
    //   52: invokestatic 46	java/lang/System:nanoTime	()J
    //   55: lsub
    //   56: lstore 5
    //   58: goto -40 -> 18
    //   61: astore 10
    //   63: iload 4
    //   65: ifeq +9 -> 74
    //   68: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   71: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   74: aload 10
    //   76: athrow
    // Line number table:
    //   Java source line #163	-> byte code offset #0
    //   Java source line #165	-> byte code offset #3
    //   Java source line #166	-> byte code offset #10
    //   Java source line #171	-> byte code offset #18
    //   Java source line #178	-> byte code offset #31
    //   Java source line #179	-> byte code offset #36
    //   Java source line #172	-> byte code offset #45
    //   Java source line #173	-> byte code offset #47
    //   Java source line #174	-> byte code offset #50
    //   Java source line #175	-> byte code offset #58
    //   Java source line #178	-> byte code offset #61
    //   Java source line #179	-> byte code offset #68
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	77	0	future	Future<V>
    //   0	77	1	timeout	long
    //   0	77	3	unit	java.util.concurrent.TimeUnit
    //   1	63	4	interrupted	boolean
    //   8	49	5	remainingNanos	long
    //   16	35	7	end	long
    //   29	14	9	localObject1	Object
    //   45	3	9	e	InterruptedException
    //   61	14	10	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   18	31	45	java/lang/InterruptedException
    //   3	31	61	finally
    //   45	63	61	finally
  }
  
  /* Error */
  public static void joinUninterruptibly(Thread toJoin, long timeout, java.util.concurrent.TimeUnit unit)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 91	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: iconst_0
    //   6: istore 4
    //   8: aload_3
    //   9: lload_1
    //   10: invokevirtual 40	java/util/concurrent/TimeUnit:toNanos	(J)J
    //   13: lstore 5
    //   15: invokestatic 46	java/lang/System:nanoTime	()J
    //   18: lload 5
    //   20: ladd
    //   21: lstore 7
    //   23: getstatic 50	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
    //   26: aload_0
    //   27: lload 5
    //   29: invokevirtual 95	java/util/concurrent/TimeUnit:timedJoin	(Ljava/lang/Thread;J)V
    //   32: iload 4
    //   34: ifeq +9 -> 43
    //   37: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   40: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   43: return
    //   44: astore 9
    //   46: iconst_1
    //   47: istore 4
    //   49: lload 7
    //   51: invokestatic 46	java/lang/System:nanoTime	()J
    //   54: lsub
    //   55: lstore 5
    //   57: goto -34 -> 23
    //   60: astore 10
    //   62: iload 4
    //   64: ifeq +9 -> 73
    //   67: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   70: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   73: aload 10
    //   75: athrow
    // Line number table:
    //   Java source line #191	-> byte code offset #0
    //   Java source line #192	-> byte code offset #5
    //   Java source line #194	-> byte code offset #8
    //   Java source line #195	-> byte code offset #15
    //   Java source line #199	-> byte code offset #23
    //   Java source line #207	-> byte code offset #32
    //   Java source line #208	-> byte code offset #37
    //   Java source line #201	-> byte code offset #44
    //   Java source line #202	-> byte code offset #46
    //   Java source line #203	-> byte code offset #49
    //   Java source line #204	-> byte code offset #57
    //   Java source line #207	-> byte code offset #60
    //   Java source line #208	-> byte code offset #67
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	76	0	toJoin	Thread
    //   0	76	1	timeout	long
    //   0	76	3	unit	java.util.concurrent.TimeUnit
    //   6	57	4	interrupted	boolean
    //   13	43	5	remainingNanos	long
    //   21	29	7	end	long
    //   44	3	9	e	InterruptedException
    //   60	14	10	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   23	32	44	java/lang/InterruptedException
    //   8	32	60	finally
    //   44	62	60	finally
  }
  
  public static <E> E takeUninterruptibly(BlockingQueue<E> queue)
  {
    boolean interrupted = false;
    try
    {
      return (E)queue.take();
    }
    catch (InterruptedException e)
    {
      for (;;)
      {
        interrupted = true;
      }
    }
    finally
    {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }
  
  public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element)
  {
    boolean interrupted = false;
    try
    {
      queue.put(element);
    }
    catch (InterruptedException e)
    {
      for (;;)
      {
        interrupted = true;
      }
    }
    finally
    {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }
  
  /* Error */
  public static void sleepUninterruptibly(long sleepFor, java.util.concurrent.TimeUnit unit)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_2
    //   3: lload_0
    //   4: invokevirtual 40	java/util/concurrent/TimeUnit:toNanos	(J)J
    //   7: lstore 4
    //   9: invokestatic 46	java/lang/System:nanoTime	()J
    //   12: lload 4
    //   14: ladd
    //   15: lstore 6
    //   17: getstatic 50	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
    //   20: lload 4
    //   22: invokevirtual 120	java/util/concurrent/TimeUnit:sleep	(J)V
    //   25: iload_3
    //   26: ifeq +9 -> 35
    //   29: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   32: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   35: return
    //   36: astore 8
    //   38: iconst_1
    //   39: istore_3
    //   40: lload 6
    //   42: invokestatic 46	java/lang/System:nanoTime	()J
    //   45: lsub
    //   46: lstore 4
    //   48: goto -31 -> 17
    //   51: astore 9
    //   53: iload_3
    //   54: ifeq +9 -> 63
    //   57: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   60: invokevirtual 25	java/lang/Thread:interrupt	()V
    //   63: aload 9
    //   65: athrow
    // Line number table:
    //   Java source line #266	-> byte code offset #0
    //   Java source line #268	-> byte code offset #2
    //   Java source line #269	-> byte code offset #9
    //   Java source line #273	-> byte code offset #17
    //   Java source line #281	-> byte code offset #25
    //   Java source line #282	-> byte code offset #29
    //   Java source line #275	-> byte code offset #36
    //   Java source line #276	-> byte code offset #38
    //   Java source line #277	-> byte code offset #40
    //   Java source line #278	-> byte code offset #48
    //   Java source line #281	-> byte code offset #51
    //   Java source line #282	-> byte code offset #57
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	66	0	sleepFor	long
    //   0	66	2	unit	java.util.concurrent.TimeUnit
    //   1	53	3	interrupted	boolean
    //   7	40	4	remainingNanos	long
    //   15	26	6	end	long
    //   36	3	8	e	InterruptedException
    //   51	13	9	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   17	25	36	java/lang/InterruptedException
    //   2	25	51	finally
    //   36	53	51	finally
  }
}
