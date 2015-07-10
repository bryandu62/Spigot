package com.avaje.ebean.event;

import com.avaje.ebean.bean.BeanCollection;

public abstract interface BeanFinder<T>
{
  public abstract T find(BeanQueryRequest<T> paramBeanQueryRequest);
  
  public abstract BeanCollection<T> findMany(BeanQueryRequest<T> paramBeanQueryRequest);
}
