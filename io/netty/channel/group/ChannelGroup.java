package io.netty.channel.group;

import io.netty.channel.Channel;
import java.util.Set;

public abstract interface ChannelGroup
  extends Set<Channel>, Comparable<ChannelGroup>
{
  public abstract String name();
  
  public abstract ChannelGroupFuture write(Object paramObject);
  
  public abstract ChannelGroupFuture write(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroup flush();
  
  public abstract ChannelGroup flush(ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroupFuture writeAndFlush(Object paramObject);
  
  @Deprecated
  public abstract ChannelGroupFuture flushAndWrite(Object paramObject);
  
  public abstract ChannelGroupFuture writeAndFlush(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  @Deprecated
  public abstract ChannelGroupFuture flushAndWrite(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroupFuture disconnect();
  
  public abstract ChannelGroupFuture disconnect(ChannelMatcher paramChannelMatcher);
  
  public abstract ChannelGroupFuture close();
  
  public abstract ChannelGroupFuture close(ChannelMatcher paramChannelMatcher);
  
  @Deprecated
  public abstract ChannelGroupFuture deregister();
  
  @Deprecated
  public abstract ChannelGroupFuture deregister(ChannelMatcher paramChannelMatcher);
}
