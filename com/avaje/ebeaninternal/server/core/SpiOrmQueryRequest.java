package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.QueryResultVisitor;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract interface SpiOrmQueryRequest<T>
{
  public abstract SpiQuery<T> getQuery();
  
  public abstract BeanDescriptor<?> getBeanDescriptor();
  
  public abstract void initTransIfRequired();
  
  public abstract void endTransIfRequired();
  
  public abstract void rollbackTransIfRequired();
  
  public abstract Object findId();
  
  public abstract int findRowCount();
  
  public abstract List<Object> findIds();
  
  public abstract void findVisit(QueryResultVisitor<T> paramQueryResultVisitor);
  
  public abstract QueryIterator<T> findIterate();
  
  public abstract List<T> findList();
  
  public abstract Set<?> findSet();
  
  public abstract Map<?, ?> findMap();
  
  public abstract BeanCollection<T> getFromQueryCache();
}
