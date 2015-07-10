package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.api.TransactionEvent;
import com.avaje.ebeaninternal.api.TransactionEventBeans;
import com.avaje.ebeaninternal.api.TransactionEventTable;
import com.avaje.ebeaninternal.api.TransactionEventTable.TableIUD;
import com.avaje.ebeaninternal.server.cluster.ClusterManager;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PostCommitProcessing
{
  private static final Logger logger = Logger.getLogger(PostCommitProcessing.class.getName());
  private final ClusterManager clusterManager;
  private final TransactionEvent event;
  private final String serverName;
  private final TransactionManager manager;
  private final List<PersistRequestBean<?>> persistBeanRequests;
  private final BeanPersistIdMap beanPersistIdMap;
  private final RemoteTransactionEvent remoteTransactionEvent;
  private final DeleteByIdMap deleteByIdMap;
  
  public PostCommitProcessing(ClusterManager clusterManager, TransactionManager manager, SpiTransaction transaction, TransactionEvent event)
  {
    this.clusterManager = clusterManager;
    this.manager = manager;
    this.serverName = manager.getServerName();
    this.event = event;
    this.deleteByIdMap = event.getDeleteByIdMap();
    this.persistBeanRequests = createPersistBeanRequests();
    
    this.beanPersistIdMap = createBeanPersistIdMap();
    
    this.remoteTransactionEvent = createRemoteTransactionEvent();
  }
  
  public void notifyLocalCacheIndex()
  {
    processTableEvents(this.event.getEventTables());
    
    this.event.notifyCache();
  }
  
  private void processTableEvents(TransactionEventTable tableEvents)
  {
    BeanDescriptorManager dm;
    if ((tableEvents != null) && (!tableEvents.isEmpty()))
    {
      dm = this.manager.getBeanDescriptorManager();
      for (TransactionEventTable.TableIUD tableIUD : tableEvents.values()) {
        dm.cacheNotify(tableIUD);
      }
    }
  }
  
  public void notifyCluster()
  {
    if ((this.remoteTransactionEvent != null) && (!this.remoteTransactionEvent.isEmpty()))
    {
      if ((this.manager.getClusterDebugLevel() > 0) || (logger.isLoggable(Level.FINE))) {
        logger.info("Cluster Send: " + this.remoteTransactionEvent.toString());
      }
      this.clusterManager.broadcast(this.remoteTransactionEvent);
    }
  }
  
  public Runnable notifyPersistListeners()
  {
    new Runnable()
    {
      public void run()
      {
        PostCommitProcessing.this.localPersistListenersNotify();
      }
    };
  }
  
  private void localPersistListenersNotify()
  {
    if (this.persistBeanRequests != null) {
      for (int i = 0; i < this.persistBeanRequests.size(); i++) {
        ((PersistRequestBean)this.persistBeanRequests.get(i)).notifyLocalPersistListener();
      }
    }
    TransactionEventTable eventTables = this.event.getEventTables();
    BulkEventListenerMap map;
    if ((eventTables != null) && (!eventTables.isEmpty()))
    {
      map = this.manager.getBulkEventListenerMap();
      for (TransactionEventTable.TableIUD tableIUD : eventTables.values()) {
        map.process(tableIUD);
      }
    }
  }
  
  private List<PersistRequestBean<?>> createPersistBeanRequests()
  {
    TransactionEventBeans eventBeans = this.event.getEventBeans();
    if (eventBeans != null) {
      return eventBeans.getRequests();
    }
    return null;
  }
  
  private BeanPersistIdMap createBeanPersistIdMap()
  {
    if (this.persistBeanRequests == null) {
      return null;
    }
    BeanPersistIdMap m = new BeanPersistIdMap();
    for (int i = 0; i < this.persistBeanRequests.size(); i++) {
      ((PersistRequestBean)this.persistBeanRequests.get(i)).addToPersistMap(m);
    }
    return m;
  }
  
  private RemoteTransactionEvent createRemoteTransactionEvent()
  {
    if (!this.clusterManager.isClustering()) {
      return null;
    }
    RemoteTransactionEvent remoteTransactionEvent = new RemoteTransactionEvent(this.serverName);
    if (this.beanPersistIdMap != null) {
      for (BeanPersistIds beanPersist : this.beanPersistIdMap.values()) {
        remoteTransactionEvent.addBeanPersistIds(beanPersist);
      }
    }
    if (this.deleteByIdMap != null) {
      remoteTransactionEvent.setDeleteByIdMap(this.deleteByIdMap);
    }
    TransactionEventTable eventTables = this.event.getEventTables();
    if ((eventTables != null) && (!eventTables.isEmpty())) {
      for (TransactionEventTable.TableIUD tableIUD : eventTables.values()) {
        remoteTransactionEvent.addTableIUD(tableIUD);
      }
    }
    Set<IndexInvalidate> indexInvalidations = this.event.getIndexInvalidations();
    if (indexInvalidations != null) {
      for (IndexInvalidate indexInvalidate : indexInvalidations) {
        remoteTransactionEvent.addIndexInvalidate(indexInvalidate);
      }
    }
    return remoteTransactionEvent;
  }
}
