package com.avaje.ebean.event;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
import java.util.Set;

public abstract interface BeanPersistRequest<T>
{
  public abstract EbeanServer getEbeanServer();
  
  public abstract Transaction getTransaction();
  
  public abstract Set<String> getLoadedProperties();
  
  public abstract Set<String> getUpdatedProperties();
  
  public abstract T getBean();
  
  public abstract T getOldValues();
}
