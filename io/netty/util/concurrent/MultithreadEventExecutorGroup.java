package io.netty.util.concurrent;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultithreadEventExecutorGroup
  extends AbstractEventExecutorGroup
{
  private final EventExecutor[] children;
  private final AtomicInteger childIndex = new AtomicInteger();
  private final AtomicInteger terminatedChildren = new AtomicInteger();
  private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
  private final EventExecutorChooser chooser;
  
  protected MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args)
  {
    if (nThreads <= 0) {
      throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", new Object[] { Integer.valueOf(nThreads) }));
    }
    if (threadFactory == null) {
      threadFactory = newDefaultThreadFactory();
    }
    this.children = new SingleThreadEventExecutor[nThreads];
    if (isPowerOfTwo(this.children.length)) {
      this.chooser = new PowerOfTwoEventExecutorChooser(null);
    } else {
      this.chooser = new GenericEventExecutorChooser(null);
    }
    for (int i = 0; i < nThreads; i++)
    {
      boolean success = false;
      try
      {
        this.children[i] = newChild(threadFactory, args);
        success = true;
      }
      catch (Exception e)
      {
        int j;
        int j;
        EventExecutor e;
        throw new IllegalStateException("failed to create a child event loop", e);
      }
      finally
      {
        if (!success)
        {
          for (int j = 0; j < i; j++) {
            this.children[j].shutdownGracefully();
          }
          for (int j = 0; j < i; j++)
          {
            EventExecutor e = this.children[j];
            try
            {
              while (!e.isTerminated()) {
                e.awaitTermination(2147483647L, TimeUnit.SECONDS);
              }
            }
            catch (InterruptedException interrupted)
            {
              Thread.currentThread().interrupt();
              break;
            }
          }
        }
      }
    }
    FutureListener<Object> terminationListener = new FutureListener()
    {
      public void operationComplete(Future<Object> future)
        throws Exception
      {
        if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
          MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
        }
      }
    };
    for (EventExecutor e : this.children) {
      e.terminationFuture().addListener(terminationListener);
    }
  }
  
  protected ThreadFactory newDefaultThreadFactory()
  {
    return new DefaultThreadFactory(getClass());
  }
  
  public EventExecutor next()
  {
    return this.chooser.next();
  }
  
  public Iterator<EventExecutor> iterator()
  {
    return children().iterator();
  }
  
  public final int executorCount()
  {
    return this.children.length;
  }
  
  protected Set<EventExecutor> children()
  {
    Set<EventExecutor> children = Collections.newSetFromMap(new LinkedHashMap());
    Collections.addAll(children, this.children);
    return children;
  }
  
  protected abstract EventExecutor newChild(ThreadFactory paramThreadFactory, Object... paramVarArgs)
    throws Exception;
  
  public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit)
  {
    for (EventExecutor l : this.children) {
      l.shutdownGracefully(quietPeriod, timeout, unit);
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
    for (EventExecutor l : this.children) {
      l.shutdown();
    }
  }
  
  public boolean isShuttingDown()
  {
    for (EventExecutor l : this.children) {
      if (!l.isShuttingDown()) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isShutdown()
  {
    for (EventExecutor l : this.children) {
      if (!l.isShutdown()) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isTerminated()
  {
    for (EventExecutor l : this.children) {
      if (!l.isTerminated()) {
        return false;
      }
    }
    return true;
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit)
    throws InterruptedException
  {
    long deadline = System.nanoTime() + unit.toNanos(timeout);
    for (EventExecutor l : this.children) {
      for (;;)
      {
        long timeLeft = deadline - System.nanoTime();
        if (timeLeft <= 0L) {
          break label84;
        }
        if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) {
          break;
        }
      }
    }
    label84:
    return isTerminated();
  }
  
  private static boolean isPowerOfTwo(int val)
  {
    return (val & -val) == val;
  }
  
  private static abstract interface EventExecutorChooser
  {
    public abstract EventExecutor next();
  }
  
  private final class PowerOfTwoEventExecutorChooser
    implements MultithreadEventExecutorGroup.EventExecutorChooser
  {
    private PowerOfTwoEventExecutorChooser() {}
    
    public EventExecutor next()
    {
      return MultithreadEventExecutorGroup.this.children[(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() & MultithreadEventExecutorGroup.this.children.length - 1)];
    }
  }
  
  private final class GenericEventExecutorChooser
    implements MultithreadEventExecutorGroup.EventExecutorChooser
  {
    private GenericEventExecutorChooser() {}
    
    public EventExecutor next()
    {
      return MultithreadEventExecutorGroup.this.children[Math.abs(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() % MultithreadEventExecutorGroup.this.children.length)];
    }
  }
}
