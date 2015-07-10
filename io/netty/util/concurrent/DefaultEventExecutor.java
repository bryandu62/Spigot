package io.netty.util.concurrent;

import java.util.concurrent.ThreadFactory;

final class DefaultEventExecutor
  extends SingleThreadEventExecutor
{
  DefaultEventExecutor(DefaultEventExecutorGroup parent, ThreadFactory threadFactory)
  {
    super(parent, threadFactory, true);
  }
  
  protected void run()
  {
    for (;;)
    {
      Runnable task = takeTask();
      if (task != null)
      {
        task.run();
        updateLastExecutionTime();
      }
      if (confirmShutdown()) {
        break;
      }
    }
  }
}
