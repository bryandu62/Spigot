package io.netty.channel;

public final class ChannelPromiseNotifier
  implements ChannelFutureListener
{
  private final ChannelPromise[] promises;
  
  public ChannelPromiseNotifier(ChannelPromise... promises)
  {
    if (promises == null) {
      throw new NullPointerException("promises");
    }
    for (ChannelPromise promise : promises) {
      if (promise == null) {
        throw new IllegalArgumentException("promises contains null ChannelPromise");
      }
    }
    this.promises = ((ChannelPromise[])promises.clone());
  }
  
  public void operationComplete(ChannelFuture cf)
    throws Exception
  {
    if (cf.isSuccess())
    {
      for (ChannelPromise p : this.promises) {
        p.setSuccess();
      }
      return;
    }
    Throwable cause = cf.cause();
    for (ChannelPromise p : this.promises) {
      p.setFailure(cause);
    }
  }
}
