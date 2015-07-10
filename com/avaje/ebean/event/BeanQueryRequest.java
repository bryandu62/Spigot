package com.avaje.ebean.event;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;

public abstract interface BeanQueryRequest<T>
{
  public abstract EbeanServer getEbeanServer();
  
  public abstract Transaction getTransaction();
  
  public abstract Query<T> getQuery();
}
