package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebeaninternal.server.transaction.DefaultPersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class DeleteUnloadedForeignKeys
{
  private final List<BeanPropertyAssocOne<?>> propList = new ArrayList(4);
  private final SpiEbeanServer server;
  private final PersistRequestBean<?> request;
  private Object beanWithForeignKeys;
  
  public DeleteUnloadedForeignKeys(SpiEbeanServer server, PersistRequestBean<?> request)
  {
    this.server = server;
    this.request = request;
  }
  
  public boolean isEmpty()
  {
    return this.propList.isEmpty();
  }
  
  public void add(BeanPropertyAssocOne<?> prop)
  {
    this.propList.add(prop);
  }
  
  public void queryForeignKeys()
  {
    BeanDescriptor<?> descriptor = this.request.getBeanDescriptor();
    SpiQuery<?> q = (SpiQuery)this.server.createQuery(descriptor.getBeanType());
    
    Object id = this.request.getBeanId();
    
    StringBuilder sb = new StringBuilder(30);
    for (int i = 0; i < this.propList.size(); i++) {
      sb.append(((BeanPropertyAssocOne)this.propList.get(i)).getName()).append(",");
    }
    q.setPersistenceContext(new DefaultPersistenceContext());
    q.setAutofetch(false);
    q.select(sb.toString());
    q.where().idEq(id);
    
    SpiTransaction t = this.request.getTransaction();
    if (t.isLogSummary()) {
      t.logInternal("-- Ebean fetching foreign key values for delete of " + descriptor.getName() + " id:" + id);
    }
    this.beanWithForeignKeys = this.server.findUnique(q, t);
  }
  
  public void deleteCascade()
  {
    for (int i = 0; i < this.propList.size(); i++)
    {
      BeanPropertyAssocOne<?> prop = (BeanPropertyAssocOne)this.propList.get(i);
      Object detailBean = prop.getValue(this.beanWithForeignKeys);
      if ((detailBean != null) && (prop.hasId(detailBean))) {
        this.server.delete(detailBean, this.request.getTransaction());
      }
    }
  }
}
