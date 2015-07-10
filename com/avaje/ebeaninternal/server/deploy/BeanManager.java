package com.avaje.ebeaninternal.server.deploy;

import com.avaje.ebeaninternal.server.persist.BeanPersister;

public class BeanManager<T>
{
  private final BeanPersister persister;
  private final BeanDescriptor<T> descriptor;
  
  public BeanManager(BeanDescriptor<T> descriptor, BeanPersister persister)
  {
    this.descriptor = descriptor;
    this.persister = persister;
  }
  
  public BeanPersister getBeanPersister()
  {
    return this.persister;
  }
  
  public BeanDescriptor<T> getBeanDescriptor()
  {
    return this.descriptor;
  }
  
  public boolean isLdapEntityType()
  {
    return this.descriptor.isLdapEntityType();
  }
}
