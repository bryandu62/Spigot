package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebean.event.BeanPersistListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChainedBeanPersistListener<T>
  implements BeanPersistListener<T>
{
  private final List<BeanPersistListener<T>> list;
  private final BeanPersistListener<T>[] chain;
  
  public ChainedBeanPersistListener(BeanPersistListener<T> c1, BeanPersistListener<T> c2)
  {
    this(addList(c1, c2));
  }
  
  private static <T> List<BeanPersistListener<T>> addList(BeanPersistListener<T> c1, BeanPersistListener<T> c2)
  {
    ArrayList<BeanPersistListener<T>> addList = new ArrayList(2);
    addList.add(c1);
    addList.add(c2);
    return addList;
  }
  
  public ChainedBeanPersistListener(List<BeanPersistListener<T>> list)
  {
    this.list = list;
    this.chain = ((BeanPersistListener[])list.toArray(new BeanPersistListener[list.size()]));
  }
  
  public ChainedBeanPersistListener<T> register(BeanPersistListener<T> c)
  {
    if (this.list.contains(c)) {
      return this;
    }
    List<BeanPersistListener<T>> newList = new ArrayList();
    newList.addAll(this.list);
    newList.add(c);
    
    return new ChainedBeanPersistListener(newList);
  }
  
  public ChainedBeanPersistListener<T> deregister(BeanPersistListener<T> c)
  {
    if (!this.list.contains(c)) {
      return this;
    }
    ArrayList<BeanPersistListener<T>> newList = new ArrayList();
    newList.addAll(this.list);
    newList.remove(c);
    
    return new ChainedBeanPersistListener(newList);
  }
  
  public boolean deleted(T bean)
  {
    boolean notifyCluster = false;
    for (int i = 0; i < this.chain.length; i++) {
      if (this.chain[i].deleted(bean)) {
        notifyCluster = true;
      }
    }
    return notifyCluster;
  }
  
  public boolean inserted(T bean)
  {
    boolean notifyCluster = false;
    for (int i = 0; i < this.chain.length; i++) {
      if (this.chain[i].inserted(bean)) {
        notifyCluster = true;
      }
    }
    return notifyCluster;
  }
  
  public void remoteDelete(Object id)
  {
    for (int i = 0; i < this.chain.length; i++) {
      this.chain[i].remoteDelete(id);
    }
  }
  
  public void remoteInsert(Object id)
  {
    for (int i = 0; i < this.chain.length; i++) {
      this.chain[i].remoteInsert(id);
    }
  }
  
  public void remoteUpdate(Object id)
  {
    for (int i = 0; i < this.chain.length; i++) {
      this.chain[i].remoteUpdate(id);
    }
  }
  
  public boolean updated(T bean, Set<String> updatedProperties)
  {
    boolean notifyCluster = false;
    for (int i = 0; i < this.chain.length; i++) {
      if (this.chain[i].updated(bean, updatedProperties)) {
        notifyCluster = true;
      }
    }
    return notifyCluster;
  }
}
