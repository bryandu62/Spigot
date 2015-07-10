package com.avaje.ebeaninternal.server.cluster;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.GlobalProperties;
import com.avaje.ebeaninternal.api.ClassUtil;
import com.avaje.ebeaninternal.server.cluster.mcast.McastClusterManager;
import com.avaje.ebeaninternal.server.cluster.socket.SocketClusterBroadcast;
import com.avaje.ebeaninternal.server.transaction.RemoteTransactionEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterManager
{
  private static final Logger logger = Logger.getLogger(ClusterManager.class.getName());
  private final ConcurrentHashMap<String, EbeanServer> serverMap = new ConcurrentHashMap();
  private final Object monitor = new Object();
  private final ClusterBroadcast broadcast;
  private boolean started;
  
  public ClusterManager()
  {
    String clusterType = GlobalProperties.get("ebean.cluster.type", null);
    if ((clusterType == null) || (clusterType.trim().length() == 0)) {
      this.broadcast = null;
    } else {
      try
      {
        if ("mcast".equalsIgnoreCase(clusterType))
        {
          this.broadcast = new McastClusterManager();
        }
        else if ("socket".equalsIgnoreCase(clusterType))
        {
          this.broadcast = new SocketClusterBroadcast();
        }
        else
        {
          logger.info("Clustering using [" + clusterType + "]");
          this.broadcast = ((ClusterBroadcast)ClassUtil.newInstance(clusterType));
        }
      }
      catch (Exception e)
      {
        String msg = "Error initialising ClusterManager type [" + clusterType + "]";
        logger.log(Level.SEVERE, msg, e);
        throw new RuntimeException(e);
      }
    }
  }
  
  public void registerServer(EbeanServer server)
  {
    synchronized (this.monitor)
    {
      if (!this.started) {
        startup();
      }
      this.serverMap.put(server.getName(), server);
    }
  }
  
  /* Error */
  public EbeanServer getServer(String name)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	com/avaje/ebeaninternal/server/cluster/ClusterManager:monitor	Ljava/lang/Object;
    //   4: dup
    //   5: astore_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 27	com/avaje/ebeaninternal/server/cluster/ClusterManager:serverMap	Ljava/util/concurrent/ConcurrentHashMap;
    //   11: aload_1
    //   12: invokevirtual 140	java/util/concurrent/ConcurrentHashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   15: checkcast 126	com/avaje/ebean/EbeanServer
    //   18: aload_2
    //   19: monitorexit
    //   20: areturn
    //   21: astore_3
    //   22: aload_2
    //   23: monitorexit
    //   24: aload_3
    //   25: athrow
    // Line number table:
    //   Java source line #85	-> byte code offset #0
    //   Java source line #86	-> byte code offset #7
    //   Java source line #87	-> byte code offset #21
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	26	0	this	ClusterManager
    //   0	26	1	name	String
    //   5	18	2	Ljava/lang/Object;	Object
    //   21	4	3	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   7	20	21	finally
    //   21	24	21	finally
  }
  
  private void startup()
  {
    this.started = true;
    if (this.broadcast != null) {
      this.broadcast.startup(this);
    }
  }
  
  public boolean isClustering()
  {
    return this.broadcast != null;
  }
  
  public void broadcast(RemoteTransactionEvent remoteTransEvent)
  {
    if (this.broadcast != null) {
      this.broadcast.broadcast(remoteTransEvent);
    }
  }
  
  public void shutdown()
  {
    if (this.broadcast != null)
    {
      logger.info("ClusterManager shutdown ");
      this.broadcast.shutdown();
    }
  }
}
