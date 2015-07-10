package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class GlobalTrafficShapingHandler
  extends AbstractTrafficShapingHandler
{
  private Map<Integer, List<ToSend>> messagesQueues = new HashMap();
  
  void createGlobalTrafficCounter(ScheduledExecutorService executor)
  {
    if (executor == null) {
      throw new NullPointerException("executor");
    }
    TrafficCounter tc = new TrafficCounter(this, executor, "GlobalTC", this.checkInterval);
    
    setTrafficCounter(tc);
    tc.start();
  }
  
  public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval, long maxTime)
  {
    super(writeLimit, readLimit, checkInterval, maxTime);
    createGlobalTrafficCounter(executor);
  }
  
  public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval)
  {
    super(writeLimit, readLimit, checkInterval);
    createGlobalTrafficCounter(executor);
  }
  
  public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit)
  {
    super(writeLimit, readLimit);
    createGlobalTrafficCounter(executor);
  }
  
  public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long checkInterval)
  {
    super(checkInterval);
    createGlobalTrafficCounter(executor);
  }
  
  public GlobalTrafficShapingHandler(EventExecutor executor)
  {
    createGlobalTrafficCounter(executor);
  }
  
  public final void release()
  {
    if (this.trafficCounter != null) {
      this.trafficCounter.stop();
    }
  }
  
  public void handlerAdded(ChannelHandlerContext ctx)
    throws Exception
  {
    Integer key = Integer.valueOf(ctx.channel().hashCode());
    List<ToSend> mq = new LinkedList();
    this.messagesQueues.put(key, mq);
  }
  
  public synchronized void handlerRemoved(ChannelHandlerContext ctx)
    throws Exception
  {
    Integer key = Integer.valueOf(ctx.channel().hashCode());
    List<ToSend> mq = (List)this.messagesQueues.remove(key);
    if (mq != null)
    {
      for (ToSend toSend : mq) {
        if ((toSend.toSend instanceof ByteBuf)) {
          ((ByteBuf)toSend.toSend).release();
        }
      }
      mq.clear();
    }
  }
  
  private static final class ToSend
  {
    final long date;
    final Object toSend;
    final ChannelPromise promise;
    
    private ToSend(long delay, Object toSend, ChannelPromise promise)
    {
      this.date = (System.currentTimeMillis() + delay);
      this.toSend = toSend;
      this.promise = promise;
    }
  }
  
  protected synchronized void submitWrite(final ChannelHandlerContext ctx, Object msg, long delay, ChannelPromise promise)
  {
    Integer key = Integer.valueOf(ctx.channel().hashCode());
    List<ToSend> messagesQueue = (List)this.messagesQueues.get(key);
    if ((delay == 0L) && ((messagesQueue == null) || (messagesQueue.isEmpty())))
    {
      ctx.write(msg, promise);
      return;
    }
    ToSend newToSend = new ToSend(delay, msg, promise, null);
    if (messagesQueue == null)
    {
      messagesQueue = new LinkedList();
      this.messagesQueues.put(key, messagesQueue);
    }
    messagesQueue.add(newToSend);
    final List<ToSend> mqfinal = messagesQueue;
    ctx.executor().schedule(new Runnable()
    {
      public void run()
      {
        GlobalTrafficShapingHandler.this.sendAllValid(ctx, mqfinal);
      }
    }, delay, TimeUnit.MILLISECONDS);
  }
  
  private synchronized void sendAllValid(ChannelHandlerContext ctx, List<ToSend> messagesQueue)
  {
    while (!messagesQueue.isEmpty())
    {
      ToSend newToSend = (ToSend)messagesQueue.remove(0);
      if (newToSend.date <= System.currentTimeMillis())
      {
        ctx.write(newToSend.toSend, newToSend.promise);
      }
      else
      {
        messagesQueue.add(0, newToSend);
        break;
      }
    }
    ctx.flush();
  }
}
