package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;

@Beta
public final class RemovalListeners
{
  public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> listener, Executor executor)
  {
    Preconditions.checkNotNull(listener);
    Preconditions.checkNotNull(executor);
    new RemovalListener()
    {
      public void onRemoval(final RemovalNotification<K, V> notification)
      {
        this.val$executor.execute(new Runnable()
        {
          public void run()
          {
            RemovalListeners.1.this.val$listener.onRemoval(notification);
          }
        });
      }
    };
  }
}
