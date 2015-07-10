package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.event.BeanFinder;
import com.avaje.ebeaninternal.api.BeanIdList;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.core.OrmQueryEngine;
import com.avaje.ebeaninternal.server.core.OrmQueryRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptorManager;
import java.util.Collection;

public class DefaultOrmQueryEngine
  implements OrmQueryEngine
{
  private final CQueryEngine queryEngine;
  
  public DefaultOrmQueryEngine(BeanDescriptorManager descMgr, CQueryEngine queryEngine)
  {
    this.queryEngine = queryEngine;
  }
  
  public <T> int findRowCount(OrmQueryRequest<T> request)
  {
    return this.queryEngine.findRowCount(request);
  }
  
  public <T> BeanIdList findIds(OrmQueryRequest<T> request)
  {
    return this.queryEngine.findIds(request);
  }
  
  public <T> QueryIterator<T> findIterate(OrmQueryRequest<T> request)
  {
    SpiTransaction t = request.getTransaction();
    
    t.flushBatch();
    
    return this.queryEngine.findIterate(request);
  }
  
  public <T> BeanCollection<T> findMany(OrmQueryRequest<T> request)
  {
    SpiQuery<T> query = request.getQuery();
    
    BeanCollection<T> result = null;
    
    SpiTransaction t = request.getTransaction();
    
    t.flushBatch();
    
    BeanFinder<T> finder = request.getBeanFinder();
    if (finder != null) {
      result = finder.findMany(request);
    } else {
      result = this.queryEngine.findMany(request);
    }
    BeanDescriptor<T> descriptor;
    if (query.isLoadBeanCache())
    {
      descriptor = request.getBeanDescriptor();
      Collection<T> c = result.getActualDetails();
      for (T bean : c) {
        descriptor.cachePutBeanData(bean);
      }
    }
    if ((!result.isEmpty()) && (query.isUseQueryCache())) {
      request.putToQueryCache(result);
    }
    return result;
  }
  
  public <T> T findId(OrmQueryRequest<T> request)
  {
    T result = null;
    
    SpiTransaction t = request.getTransaction();
    if (t.isBatchFlushOnQuery()) {
      t.flushBatch();
    }
    BeanFinder<T> finder = request.getBeanFinder();
    if (finder != null) {
      result = finder.find(request);
    } else {
      result = this.queryEngine.find(request);
    }
    if ((result != null) && (request.isUseBeanCache())) {
      request.getBeanDescriptor().cachePutBeanData(result);
    }
    return result;
  }
}
