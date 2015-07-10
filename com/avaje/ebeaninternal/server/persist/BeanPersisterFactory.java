package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;

public abstract interface BeanPersisterFactory
{
  public abstract BeanPersister create(BeanDescriptor<?> paramBeanDescriptor);
}
