package com.avaje.ebeaninternal.server.transaction;

import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.TransactionEventTable.TableIUD;
import com.avaje.ebeaninternal.server.cluster.BinaryMessageList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoteTransactionEvent
  implements Runnable
{
  private List<BeanPersistIds> beanPersistList = new ArrayList();
  private List<TransactionEventTable.TableIUD> tableList;
  private List<BeanDeltaList> beanDeltaLists;
  private BeanDeltaMap beanDeltaMap;
  private List<IndexEvent> indexEventList;
  private Set<IndexInvalidate> indexInvalidations;
  private DeleteByIdMap deleteByIdMap;
  private String serverName;
  private transient SpiEbeanServer server;
  
  public RemoteTransactionEvent(String serverName)
  {
    this.serverName = serverName;
  }
  
  public RemoteTransactionEvent(SpiEbeanServer server)
  {
    this.server = server;
  }
  
  public void run()
  {
    this.server.remoteTransactionEvent(this);
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if (this.beanDeltaMap != null) {
      sb.append(this.beanDeltaMap);
    }
    sb.append(this.beanPersistList);
    if (this.tableList != null) {
      sb.append(this.tableList);
    }
    return sb.toString();
  }
  
  public void writeBinaryMessage(BinaryMessageList msgList)
    throws IOException
  {
    if (this.indexInvalidations != null) {
      for (IndexInvalidate indexInvalidate : this.indexInvalidations) {
        indexInvalidate.writeBinaryMessage(msgList);
      }
    }
    if (this.tableList != null) {
      for (int i = 0; i < this.tableList.size(); i++) {
        ((TransactionEventTable.TableIUD)this.tableList.get(i)).writeBinaryMessage(msgList);
      }
    }
    if (this.deleteByIdMap != null) {
      for (BeanPersistIds deleteIds : this.deleteByIdMap.values()) {
        deleteIds.writeBinaryMessage(msgList);
      }
    }
    if (this.beanPersistList != null) {
      for (int i = 0; i < this.beanPersistList.size(); i++) {
        ((BeanPersistIds)this.beanPersistList.get(i)).writeBinaryMessage(msgList);
      }
    }
    if (this.beanDeltaLists != null) {
      for (int i = 0; i < this.beanDeltaLists.size(); i++) {
        ((BeanDeltaList)this.beanDeltaLists.get(i)).writeBinaryMessage(msgList);
      }
    }
    if (this.indexEventList != null) {
      for (int i = 0; i < this.indexEventList.size(); i++) {
        ((IndexEvent)this.indexEventList.get(i)).writeBinaryMessage(msgList);
      }
    }
  }
  
  public boolean isEmpty()
  {
    return (this.beanPersistList.isEmpty()) && ((this.tableList == null) || (this.tableList.isEmpty()));
  }
  
  public void addBeanPersistIds(BeanPersistIds beanPersist)
  {
    this.beanPersistList.add(beanPersist);
  }
  
  public void addIndexInvalidate(IndexInvalidate indexInvalidate)
  {
    if (this.indexInvalidations == null) {
      this.indexInvalidations = new HashSet();
    }
    this.indexInvalidations.add(indexInvalidate);
  }
  
  public void addTableIUD(TransactionEventTable.TableIUD tableIud)
  {
    if (this.tableList == null) {
      this.tableList = new ArrayList(4);
    }
    this.tableList.add(tableIud);
  }
  
  public void addBeanDeltaList(BeanDeltaList deltaList)
  {
    if (this.beanDeltaLists == null) {
      this.beanDeltaLists = new ArrayList();
    }
    this.beanDeltaLists.add(deltaList);
  }
  
  public void addBeanDelta(BeanDelta beanDelta)
  {
    if (this.beanDeltaMap == null) {
      this.beanDeltaMap = new BeanDeltaMap();
    }
    this.beanDeltaMap.addBeanDelta(beanDelta);
  }
  
  public void addIndexEvent(IndexEvent indexEvent)
  {
    if (this.indexEventList == null) {
      this.indexEventList = new ArrayList(2);
    }
    this.indexEventList.add(indexEvent);
  }
  
  public String getServerName()
  {
    return this.serverName;
  }
  
  public SpiEbeanServer getServer()
  {
    return this.server;
  }
  
  public void setServer(SpiEbeanServer server)
  {
    this.server = server;
  }
  
  public DeleteByIdMap getDeleteByIdMap()
  {
    return this.deleteByIdMap;
  }
  
  public void setDeleteByIdMap(DeleteByIdMap deleteByIdMap)
  {
    this.deleteByIdMap = deleteByIdMap;
  }
  
  public Set<IndexInvalidate> getIndexInvalidations()
  {
    return this.indexInvalidations;
  }
  
  public List<IndexEvent> getIndexEventList()
  {
    return this.indexEventList;
  }
  
  public List<TransactionEventTable.TableIUD> getTableIUDList()
  {
    return this.tableList;
  }
  
  public List<BeanPersistIds> getBeanPersistList()
  {
    return this.beanPersistList;
  }
  
  public List<BeanDeltaList> getBeanDeltaLists()
  {
    if (this.beanDeltaMap != null) {
      this.beanDeltaLists.addAll(this.beanDeltaMap.deltaLists());
    }
    return this.beanDeltaLists;
  }
}
