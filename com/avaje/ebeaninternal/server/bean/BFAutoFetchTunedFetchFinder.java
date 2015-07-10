package com.avaje.ebeaninternal.server.bean;

import com.avaje.ebean.OrderBy;
import com.avaje.ebean.Query;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.common.BeanList;
import com.avaje.ebean.event.BeanFinder;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebean.meta.MetaAutoFetchTunedQueryInfo;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.server.autofetch.AutoFetchManager;
import com.avaje.ebeaninternal.server.autofetch.TunedQueryInfo;
import java.util.Iterator;
import javax.persistence.PersistenceException;

public class BFAutoFetchTunedFetchFinder
  implements BeanFinder<MetaAutoFetchTunedQueryInfo>
{
  public MetaAutoFetchTunedQueryInfo find(BeanQueryRequest<MetaAutoFetchTunedQueryInfo> request)
  {
    SpiQuery<?> query = (SpiQuery)request.getQuery();
    try
    {
      String queryPointKey = (String)query.getId();
      
      SpiEbeanServer server = (SpiEbeanServer)request.getEbeanServer();
      AutoFetchManager manager = server.getAutoFetchManager();
      
      TunedQueryInfo tunedFetch = manager.getTunedQueryInfo(queryPointKey);
      if (tunedFetch != null) {
        return tunedFetch.createPublicMeta();
      }
      return null;
    }
    catch (Exception e)
    {
      throw new PersistenceException(e);
    }
  }
  
  public BeanCollection<MetaAutoFetchTunedQueryInfo> findMany(BeanQueryRequest<MetaAutoFetchTunedQueryInfo> request)
  {
    SpiQuery.Type queryType = ((SpiQuery)request.getQuery()).getType();
    if (!queryType.equals(SpiQuery.Type.LIST)) {
      throw new PersistenceException("Only findList() supported at this stage.");
    }
    SpiEbeanServer server = (SpiEbeanServer)request.getEbeanServer();
    AutoFetchManager manager = server.getAutoFetchManager();
    
    BeanList<MetaAutoFetchTunedQueryInfo> list = new BeanList();
    
    Iterator<TunedQueryInfo> it = manager.iterateTunedQueryInfo();
    while (it.hasNext())
    {
      TunedQueryInfo tunedFetch = (TunedQueryInfo)it.next();
      
      list.add(tunedFetch.createPublicMeta());
    }
    String orderBy = request.getQuery().order().toStringFormat();
    if (orderBy == null) {
      orderBy = "beanType, origQueryPlanHash";
    }
    server.sort(list, orderBy);
    
    return list;
  }
}
