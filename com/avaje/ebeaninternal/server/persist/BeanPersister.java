package com.avaje.ebeaninternal.server.persist;

import com.avaje.ebeaninternal.server.core.PersistRequestBean;
import javax.persistence.PersistenceException;

public abstract interface BeanPersister
{
  public abstract void insert(PersistRequestBean<?> paramPersistRequestBean)
    throws PersistenceException;
  
  public abstract void update(PersistRequestBean<?> paramPersistRequestBean)
    throws PersistenceException;
  
  public abstract void delete(PersistRequestBean<?> paramPersistRequestBean)
    throws PersistenceException;
}
