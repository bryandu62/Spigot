package com.avaje.ebeaninternal.server.core;

import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.QueryResultVisitor;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSql.ColumnMapping;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.PersistenceContext;
import com.avaje.ebean.event.BeanFinder;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.BeanIdList;
import com.avaje.ebeaninternal.api.LoadContext;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.api.SpiQuery;
import com.avaje.ebeaninternal.api.SpiQuery.Type;
import com.avaje.ebeaninternal.api.SpiTransaction;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.server.deploy.BeanProperty;
import com.avaje.ebeaninternal.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebeaninternal.server.deploy.DeployParser;
import com.avaje.ebeaninternal.server.deploy.DeployPropertyParserMap;
import com.avaje.ebeaninternal.server.loadcontext.DLoadContext;
import com.avaje.ebeaninternal.server.query.CQueryPlan;
import com.avaje.ebeaninternal.server.query.CancelableQuery;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.PersistenceException;

public final class OrmQueryRequest<T>
  extends BeanRequest
  implements BeanQueryRequest<T>, SpiOrmQueryRequest<T>
{
  private final BeanDescriptor<T> beanDescriptor;
  private final OrmQueryEngine queryEngine;
  private final SpiQuery<T> query;
  private final boolean vanillaMode;
  private final BeanFinder<T> finder;
  private final LoadContext graphContext;
  private final Boolean readOnly;
  private final RawSql rawSql;
  private PersistenceContext persistenceContext;
  private Integer cacheKey;
  private int queryPlanHash;
  private boolean backgroundFetching;
  
  public OrmQueryRequest(SpiEbeanServer server, OrmQueryEngine queryEngine, SpiQuery<T> query, BeanDescriptor<T> desc, SpiTransaction t)
  {
    super(server, t);
    
    this.beanDescriptor = desc;
    this.rawSql = query.getRawSql();
    this.finder = this.beanDescriptor.getBeanFinder();
    this.queryEngine = queryEngine;
    this.query = query;
    this.vanillaMode = query.isVanillaMode(server.isVanillaMode());
    this.readOnly = query.isReadOnly();
    
    this.graphContext = new DLoadContext(this.ebeanServer, this.beanDescriptor, this.readOnly, query);
    this.graphContext.registerSecondaryQueries(query);
  }
  
  public void setTotalHits(int totalHits)
  {
    this.query.setTotalHits(totalHits);
  }
  
  public void executeSecondaryQueries(int defaultQueryBatch)
  {
    this.graphContext.executeSecondaryQueries(this, defaultQueryBatch);
  }
  
  public int getSecondaryQueriesMinBatchSize(int defaultQueryBatch)
  {
    return this.graphContext.getSecondaryQueriesMinBatchSize(this, defaultQueryBatch);
  }
  
  public Boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public BeanDescriptor<T> getBeanDescriptor()
  {
    return this.beanDescriptor;
  }
  
  public LoadContext getGraphContext()
  {
    return this.graphContext;
  }
  
  public void calculateQueryPlanHash()
  {
    this.queryPlanHash = this.query.queryPlanHash(this);
  }
  
  public boolean isRawSql()
  {
    return this.rawSql != null;
  }
  
  public DeployParser createDeployParser()
  {
    if (this.rawSql != null) {
      return new DeployPropertyParserMap(this.rawSql.getColumnMapping().getMapping());
    }
    return this.beanDescriptor.createDeployPropertyParser();
  }
  
  public boolean isSqlSelect()
  {
    return (this.query.isSqlSelect()) && (this.query.getRawSql() == null);
  }
  
  public PersistenceContext getPersistenceContext()
  {
    return this.persistenceContext;
  }
  
  public void initTransIfRequired()
  {
    if (this.query.createOwnTransaction())
    {
      this.transaction = this.ebeanServer.createQueryTransaction();
      this.createdTransaction = true;
    }
    else if (this.transaction == null)
    {
      this.transaction = this.ebeanServer.getCurrentServerTransaction();
      if (this.transaction == null)
      {
        this.transaction = this.ebeanServer.createQueryTransaction();
        this.createdTransaction = true;
      }
    }
    this.persistenceContext = getPersistenceContext(this.query, this.transaction);
    this.graphContext.setPersistenceContext(this.persistenceContext);
  }
  
  private PersistenceContext getPersistenceContext(SpiQuery<?> query, SpiTransaction t)
  {
    PersistenceContext ctx = query.getPersistenceContext();
    if (ctx == null) {
      ctx = t.getPersistenceContext();
    }
    return ctx;
  }
  
  public void endTransIfRequired()
  {
    if ((this.createdTransaction) && (!this.backgroundFetching)) {
      this.transaction.rollback();
    }
  }
  
  public void setBackgroundFetching()
  {
    this.backgroundFetching = true;
  }
  
  public boolean isFindById()
  {
    return this.query.getType() == SpiQuery.Type.BEAN;
  }
  
  public boolean isVanillaMode()
  {
    return this.vanillaMode;
  }
  
  public Object findId()
  {
    return this.queryEngine.findId(this);
  }
  
  public int findRowCount()
  {
    return this.queryEngine.findRowCount(this);
  }
  
  public List<Object> findIds()
  {
    BeanIdList idList = this.queryEngine.findIds(this);
    return idList.getIdList();
  }
  
  public void findVisit(QueryResultVisitor<T> visitor)
  {
    QueryIterator<T> it = this.queryEngine.findIterate(this);
    try
    {
      while (it.hasNext()) {
        if (!visitor.accept(it.next())) {
          break;
        }
      }
    }
    finally
    {
      it.close();
    }
  }
  
  public QueryIterator<T> findIterate()
  {
    return this.queryEngine.findIterate(this);
  }
  
  public List<T> findList()
  {
    BeanCollection<T> bc = this.queryEngine.findMany(this);
    return (List)(this.vanillaMode ? bc.getActualCollection() : bc);
  }
  
  public Set<?> findSet()
  {
    BeanCollection<T> bc = this.queryEngine.findMany(this);
    return (Set)(this.vanillaMode ? bc.getActualCollection() : bc);
  }
  
  public Map<?, ?> findMap()
  {
    String mapKey = this.query.getMapKey();
    if (mapKey == null)
    {
      BeanProperty[] ids = this.beanDescriptor.propertiesId();
      if (ids.length == 1)
      {
        this.query.setMapKey(ids[0].getName());
      }
      else
      {
        String msg = "No mapKey specified for query";
        throw new PersistenceException(msg);
      }
    }
    BeanCollection<T> bc = this.queryEngine.findMany(this);
    return (Map)(this.vanillaMode ? bc.getActualCollection() : bc);
  }
  
  public SpiQuery.Type getQueryType()
  {
    return this.query.getType();
  }
  
  public BeanFinder<T> getBeanFinder()
  {
    return this.finder;
  }
  
  public SpiQuery<T> getQuery()
  {
    return this.query;
  }
  
  public BeanPropertyAssocMany<?> getManyProperty()
  {
    return this.beanDescriptor.getManyProperty(this.query);
  }
  
  public CQueryPlan getQueryPlan()
  {
    return this.beanDescriptor.getQueryPlan(Integer.valueOf(this.queryPlanHash));
  }
  
  public int getQueryPlanHash()
  {
    return this.queryPlanHash;
  }
  
  public void putQueryPlan(CQueryPlan queryPlan)
  {
    this.beanDescriptor.putQueryPlan(Integer.valueOf(this.queryPlanHash), queryPlan);
  }
  
  public boolean isUseBeanCache()
  {
    return this.beanDescriptor.calculateUseCache(this.query.isUseBeanCache());
  }
  
  public BeanCollection<T> getFromQueryCache()
  {
    if (!this.query.isUseQueryCache()) {
      return null;
    }
    if (this.query.getType() == null) {
      this.cacheKey = Integer.valueOf(this.query.queryHash());
    } else {
      this.cacheKey = Integer.valueOf(31 * this.query.queryHash() + this.query.getType().hashCode());
    }
    return null;
  }
  
  public void putToQueryCache(BeanCollection<T> queryResult)
  {
    this.beanDescriptor.queryCachePut(this.cacheKey, queryResult);
  }
  
  public void setCancelableQuery(CancelableQuery cancelableQuery)
  {
    this.query.setCancelableQuery(cancelableQuery);
  }
  
  public void logSql(String sql)
  {
    if (this.transaction.isLogSql()) {
      this.transaction.logInternal(sql);
    }
  }
}
