package net.minecraft.server.v1_8_R3;

import com.google.common.util.concurrent.ListenableFuture;

public abstract interface IAsyncTaskHandler
{
  public abstract ListenableFuture<Object> postToMainThread(Runnable paramRunnable);
  
  public abstract boolean isMainThread();
}
