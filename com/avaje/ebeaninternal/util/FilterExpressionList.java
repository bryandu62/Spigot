package com.avaje.ebeaninternal.util;

import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.FutureIds;
import com.avaje.ebean.FutureList;
import com.avaje.ebean.FutureRowCount;
import com.avaje.ebean.OrderBy;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;
import com.avaje.ebean.QueryListener;
import com.avaje.ebeaninternal.server.expression.FilterExprPath;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.PersistenceException;

public class FilterExpressionList<T>
  extends DefaultExpressionList<T>
{
  private static final long serialVersionUID = 2226895827150099020L;
  private final Query<T> rootQuery;
  private final FilterExprPath pathPrefix;
  
  public FilterExpressionList(FilterExprPath pathPrefix, ExpressionFactory expr, Query<T> rootQuery)
  {
    super(null, expr, null);
    this.pathPrefix = pathPrefix;
    this.rootQuery = rootQuery;
  }
  
  public void trimPath(int prefixTrim)
  {
    this.pathPrefix.trimPath(prefixTrim);
  }
  
  public FilterExprPath getPathPrefix()
  {
    return this.pathPrefix;
  }
  
  private String notAllowedMessage = "This method is not allowed on a filter";
  
  public ExpressionList<T> filterMany(String prop)
  {
    return this.rootQuery.filterMany(prop);
  }
  
  public FutureIds<T> findFutureIds()
  {
    return this.rootQuery.findFutureIds();
  }
  
  public FutureList<T> findFutureList()
  {
    return this.rootQuery.findFutureList();
  }
  
  public FutureRowCount<T> findFutureRowCount()
  {
    return this.rootQuery.findFutureRowCount();
  }
  
  public List<T> findList()
  {
    return this.rootQuery.findList();
  }
  
  public Map<?, T> findMap()
  {
    return this.rootQuery.findMap();
  }
  
  public PagingList<T> findPagingList(int pageSize)
  {
    return this.rootQuery.findPagingList(pageSize);
  }
  
  public int findRowCount()
  {
    return this.rootQuery.findRowCount();
  }
  
  public Set<T> findSet()
  {
    return this.rootQuery.findSet();
  }
  
  public T findUnique()
  {
    return (T)this.rootQuery.findUnique();
  }
  
  public ExpressionList<T> having()
  {
    throw new PersistenceException(this.notAllowedMessage);
  }
  
  public ExpressionList<T> idEq(Object value)
  {
    throw new PersistenceException(this.notAllowedMessage);
  }
  
  public ExpressionList<T> idIn(List<?> idValues)
  {
    throw new PersistenceException(this.notAllowedMessage);
  }
  
  public Query<T> join(String assocProperty, String assocProperties)
  {
    throw new PersistenceException(this.notAllowedMessage);
  }
  
  public Query<T> join(String assocProperties)
  {
    throw new PersistenceException(this.notAllowedMessage);
  }
  
  public OrderBy<T> order()
  {
    return this.rootQuery.order();
  }
  
  public Query<T> order(String orderByClause)
  {
    return this.rootQuery.order(orderByClause);
  }
  
  public Query<T> orderBy(String orderBy)
  {
    return this.rootQuery.orderBy(orderBy);
  }
  
  public Query<T> query()
  {
    return this.rootQuery;
  }
  
  public Query<T> select(String properties)
  {
    throw new PersistenceException(this.notAllowedMessage);
  }
  
  public Query<T> setBackgroundFetchAfter(int backgroundFetchAfter)
  {
    return this.rootQuery.setBackgroundFetchAfter(backgroundFetchAfter);
  }
  
  public Query<T> setFirstRow(int firstRow)
  {
    return this.rootQuery.setFirstRow(firstRow);
  }
  
  public Query<T> setListener(QueryListener<T> queryListener)
  {
    return this.rootQuery.setListener(queryListener);
  }
  
  public Query<T> setMapKey(String mapKey)
  {
    return this.rootQuery.setMapKey(mapKey);
  }
  
  public Query<T> setMaxRows(int maxRows)
  {
    return this.rootQuery.setMaxRows(maxRows);
  }
  
  public Query<T> setUseCache(boolean useCache)
  {
    return this.rootQuery.setUseCache(useCache);
  }
  
  public ExpressionList<T> where()
  {
    return this.rootQuery.where();
  }
}
