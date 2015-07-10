package io.netty.channel.local;

import io.netty.channel.SingleThreadEventLoop;
import java.util.concurrent.ThreadFactory;

final class LocalEventLoop
  extends SingleThreadEventLoop
{
  LocalEventLoop(LocalEventLoopGroup parent, ThreadFactory threadFactory)
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
