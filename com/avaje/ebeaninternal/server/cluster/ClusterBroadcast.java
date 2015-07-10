package com.avaje.ebeaninternal.server.cluster;

import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;

public abstract interface ClusterBroadcast
{
  public abstract void startup(ClusterManager paramClusterManager);
  
  public abstract void shutdown();
  
  public abstract void broadcast(RemoteTransactionEvent paramRemoteTransactionEvent);
}
