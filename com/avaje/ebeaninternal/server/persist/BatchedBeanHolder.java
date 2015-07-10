package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.server.core.PersistRequest;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.ArrayList;
import java.util.HashSet;

public class BatchedBeanHolder
{
  private final BatchControl control;
  private final String shortDesc;
  private final int order;
  private ArrayList<PersistRequest> inserts;
  private ArrayList<PersistRequest> updates;
  private ArrayList<PersistRequest> deletes;
  private HashSet<Integer> beanHashCodes = new HashSet();
  
  public BatchedBeanHolder(BatchControl control, BeanDescriptor<?> beanDescriptor, int order)
  {
    this.control = control;
    this.shortDesc = (beanDescriptor.getName() + ":" + order);
    this.order = order;
  }
  
  public int getOrder()
  {
    return this.order;
  }
  
  public void executeNow()
  {
    if ((this.inserts != null) && (!this.inserts.isEmpty()))
    {
      this.control.executeNow(this.inserts);
      this.inserts.clear();
    }
    if ((this.updates != null) && (!this.updates.isEmpty()))
    {
      this.control.executeNow(this.updates);
      this.updates.clear();
    }
    if ((this.deletes != null) && (!this.deletes.isEmpty()))
    {
      this.control.executeNow(this.deletes);
      this.deletes.clear();
    }
    this.beanHashCodes.clear();
  }
  
  public String toString()
  {
    return this.shortDesc;
  }
  
  public ArrayList<PersistRequest> getList(PersistRequestBean<?> request)
  {
    Integer objHashCode = Integer.valueOf(System.identityHashCode(request.getBean()));
    if (!this.beanHashCodes.add(objHashCode)) {
      return null;
    }
    switch (request.getType())
    {
    case INSERT: 
      if (this.inserts == null) {
        this.inserts = new ArrayList();
      }
      return this.inserts;
    case UPDATE: 
      if (this.updates == null) {
        this.updates = new ArrayList();
      }
      return this.updates;
    case DELETE: 
      if (this.deletes == null) {
        this.deletes = new ArrayList();
      }
      return this.deletes;
    }
    throw new RuntimeException("Invalid type code " + request.getType());
  }
}
