package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebeaninternal.api.BeanIdList;

public abstract interface OrmQueryEngine
{
  public abstract <T> T findId(OrmQueryRequest<T> paramOrmQueryRequest);
  
  public abstract <T> BeanCollection<T> findMany(OrmQueryRequest<T> paramOrmQueryRequest);
  
  public abstract <T> QueryIterator<T> findIterate(OrmQueryRequest<T> paramOrmQueryRequest);
  
  public abstract <T> int findRowCount(OrmQueryRequest<T> paramOrmQueryRequest);
  
  public abstract <T> BeanIdList findIds(OrmQueryRequest<T> paramOrmQueryRequest);
}
