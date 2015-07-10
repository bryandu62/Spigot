package com.avaje.ebeaninternal.util;

import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionFactory;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.FutureIds;
import com.avaje.ebean.FutureList;
import com.avaje.ebean.FutureRowCount;
import com.avaje.ebean.Junction;
import com.avaje.ebean.OrderBy;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;
import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.QueryListener;
import com.avaje.ebean.QueryResultVisitor;
import com.avaje.ebean.event.BeanQueryRequest;
import com.avaje.ebeaninternal.api.ManyWhereJoins;
import com.avaje.ebeaninternal.api.SpiExpression;
import com.avaje.ebeaninternal.api.SpiExpressionList;
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultExpressionList<T>
  implements SpiExpressionList<T>
{
  private static final long serialVersionUID = -6992345500247035947L;
  private final ArrayList<SpiExpression> list = new ArrayList();
  private final Query<T> query;
  private final ExpressionList<T> parentExprList;
  private transient ExpressionFactory expr;
  private final String exprLang;
  private final String listAndStart;
  private final String listAndEnd;
  private final String listAndJoin;
  
  public DefaultExpressionList(Query<T> query, ExpressionList<T> parentExprList)
  {
    this(query, query.getExpressionFactory(), parentExprList);
  }
  
  public DefaultExpressionList(Query<T> query, ExpressionFactory expr, ExpressionList<T> parentExprList)
  {
    this.query = query;
    this.expr = expr;
    this.exprLang = expr.getLang();
    this.parentExprList = parentExprList;
    if ("ldap".equals(this.exprLang))
    {
      this.listAndStart = "(&";
      this.listAndEnd = ")";
      this.listAndJoin = "";
    }
    else
    {
      this.listAndStart = "";
      this.listAndEnd = "";
      this.listAndJoin = " and ";
    }
  }
  
  public void trimPath(int prefixTrim)
  {
    throw new RuntimeException("Only allowed on FilterExpressionList");
  }
  
  public List<SpiExpression> internalList()
  {
    return this.list;
  }
  
  public void setExpressionFactory(ExpressionFactory expr)
  {
    this.expr = expr;
  }
  
  public DefaultExpressionList<T> copy(Query<T> query)
  {
    DefaultExpressionList<T> copy = new DefaultExpressionList(query, this.expr, null);
    copy.list.addAll(this.list);
    return copy;
  }
  
  public void containsMany(BeanDescriptor<?> desc, ManyWhereJoins whereManyJoins)
  {
    for (int i = 0; i < this.list.size(); i++) {
      ((SpiExpression)this.list.get(i)).containsMany(desc, whereManyJoins);
    }
  }
  
  public ExpressionList<T> endJunction()
  {
    return this.parentExprList == null ? this : this.parentExprList;
  }
  
  public Query<T> query()
  {
    return this.query;
  }
  
  public ExpressionList<T> where()
  {
    return this.query.where();
  }
  
  public OrderBy<T> order()
  {
    return this.query.order();
  }
  
  public OrderBy<T> orderBy()
  {
    return this.query.order();
  }
  
  public Query<T> order(String orderByClause)
  {
    return this.query.order(orderByClause);
  }
  
  public Query<T> orderBy(String orderBy)
  {
    return this.query.order(orderBy);
  }
  
  public Query<T> setOrderBy(String orderBy)
  {
    return this.query.order(orderBy);
  }
  
  public FutureIds<T> findFutureIds()
  {
    return this.query.findFutureIds();
  }
  
  public FutureRowCount<T> findFutureRowCount()
  {
    return this.query.findFutureRowCount();
  }
  
  public FutureList<T> findFutureList()
  {
    return this.query.findFutureList();
  }
  
  public PagingList<T> findPagingList(int pageSize)
  {
    return this.query.findPagingList(pageSize);
  }
  
  public int findRowCount()
  {
    return this.query.findRowCount();
  }
  
  public List<Object> findIds()
  {
    return this.query.findIds();
  }
  
  public void findVisit(QueryResultVisitor<T> visitor)
  {
    this.query.findVisit(visitor);
  }
  
  public QueryIterator<T> findIterate()
  {
    return this.query.findIterate();
  }
  
  public List<T> findList()
  {
    return this.query.findList();
  }
  
  public Set<T> findSet()
  {
    return this.query.findSet();
  }
  
  public Map<?, T> findMap()
  {
    return this.query.findMap();
  }
  
  public <K> Map<K, T> findMap(String keyProperty, Class<K> keyType)
  {
    return this.query.findMap(keyProperty, keyType);
  }
  
  public T findUnique()
  {
    return (T)this.query.findUnique();
  }
  
  public ExpressionList<T> filterMany(String prop)
  {
    return this.query.filterMany(prop);
  }
  
  public Query<T> select(String fetchProperties)
  {
    return this.query.select(fetchProperties);
  }
  
  public Query<T> join(String assocProperties)
  {
    return this.query.fetch(assocProperties);
  }
  
  public Query<T> join(String assocProperty, String assocProperties)
  {
    return this.query.fetch(assocProperty, assocProperties);
  }
  
  public Query<T> setFirstRow(int firstRow)
  {
    return this.query.setFirstRow(firstRow);
  }
  
  public Query<T> setMaxRows(int maxRows)
  {
    return this.query.setMaxRows(maxRows);
  }
  
  public Query<T> setBackgroundFetchAfter(int backgroundFetchAfter)
  {
    return this.query.setBackgroundFetchAfter(backgroundFetchAfter);
  }
  
  public Query<T> setMapKey(String mapKey)
  {
    return this.query.setMapKey(mapKey);
  }
  
  public Query<T> setListener(QueryListener<T> queryListener)
  {
    return this.query.setListener(queryListener);
  }
  
  public Query<T> setUseCache(boolean useCache)
  {
    return this.query.setUseCache(useCache);
  }
  
  public ExpressionList<T> having()
  {
    return this.query.having();
  }
  
  public ExpressionList<T> add(Expression expr)
  {
    this.list.add((SpiExpression)expr);
    return this;
  }
  
  public ExpressionList<T> addAll(ExpressionList<T> exprList)
  {
    SpiExpressionList<T> spiList = (SpiExpressionList)exprList;
    this.list.addAll(spiList.getUnderlyingList());
    return this;
  }
  
  public List<SpiExpression> getUnderlyingList()
  {
    return this.list;
  }
  
  public boolean isEmpty()
  {
    return this.list.isEmpty();
  }
  
  public String buildSql(SpiExpressionRequest request)
  {
    request.append(this.listAndStart);
    int i = 0;
    for (int size = this.list.size(); i < size; i++)
    {
      SpiExpression expression = (SpiExpression)this.list.get(i);
      if (i > 0) {
        request.append(this.listAndJoin);
      }
      expression.addSql(request);
    }
    request.append(this.listAndEnd);
    return request.getSql();
  }
  
  public ArrayList<Object> buildBindValues(SpiExpressionRequest request)
  {
    int i = 0;
    for (int size = this.list.size(); i < size; i++)
    {
      SpiExpression expression = (SpiExpression)this.list.get(i);
      expression.addBindValues(request);
    }
    return request.getBindValues();
  }
  
  public int queryAutoFetchHash()
  {
    int hash = DefaultExpressionList.class.getName().hashCode();
    int i = 0;
    for (int size = this.list.size(); i < size; i++)
    {
      SpiExpression expression = (SpiExpression)this.list.get(i);
      hash = hash * 31 + expression.queryAutoFetchHash();
    }
    return hash;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    int hash = DefaultExpressionList.class.getName().hashCode();
    int i = 0;
    for (int size = this.list.size(); i < size; i++)
    {
      SpiExpression expression = (SpiExpression)this.list.get(i);
      hash = hash * 31 + expression.queryPlanHash(request);
    }
    return hash;
  }
  
  public int queryBindHash()
  {
    int hash = DefaultExpressionList.class.getName().hashCode();
    int i = 0;
    for (int size = this.list.size(); i < size; i++)
    {
      SpiExpression expression = (SpiExpression)this.list.get(i);
      hash = hash * 31 + expression.queryBindHash();
    }
    return hash;
  }
  
  public ExpressionList<T> eq(String propertyName, Object value)
  {
    add(this.expr.eq(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> ieq(String propertyName, String value)
  {
    add(this.expr.ieq(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> ne(String propertyName, Object value)
  {
    add(this.expr.ne(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> allEq(Map<String, Object> propertyMap)
  {
    add(this.expr.allEq(propertyMap));
    return this;
  }
  
  public ExpressionList<T> and(Expression expOne, Expression expTwo)
  {
    add(this.expr.and(expOne, expTwo));
    return this;
  }
  
  public ExpressionList<T> between(String propertyName, Object value1, Object value2)
  {
    add(this.expr.between(propertyName, value1, value2));
    return this;
  }
  
  public ExpressionList<T> betweenProperties(String lowProperty, String highProperty, Object value)
  {
    add(this.expr.betweenProperties(lowProperty, highProperty, value));
    return this;
  }
  
  public Junction<T> conjunction()
  {
    Junction<T> conjunction = this.expr.conjunction(this.query, this);
    add(conjunction);
    return conjunction;
  }
  
  public ExpressionList<T> contains(String propertyName, String value)
  {
    add(this.expr.contains(propertyName, value));
    return this;
  }
  
  public Junction<T> disjunction()
  {
    Junction<T> disjunction = this.expr.disjunction(this.query, this);
    add(disjunction);
    return disjunction;
  }
  
  public ExpressionList<T> endsWith(String propertyName, String value)
  {
    add(this.expr.endsWith(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> ge(String propertyName, Object value)
  {
    add(this.expr.ge(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> gt(String propertyName, Object value)
  {
    add(this.expr.gt(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> icontains(String propertyName, String value)
  {
    add(this.expr.icontains(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> idIn(List<?> idList)
  {
    add(this.expr.idIn(idList));
    return this;
  }
  
  public ExpressionList<T> idEq(Object value)
  {
    if ((this.query != null) && (this.parentExprList == null)) {
      this.query.setId(value);
    } else {
      add(this.expr.idEq(value));
    }
    return this;
  }
  
  public ExpressionList<T> iendsWith(String propertyName, String value)
  {
    add(this.expr.iendsWith(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> ilike(String propertyName, String value)
  {
    add(this.expr.ilike(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> in(String propertyName, Query<?> subQuery)
  {
    add(this.expr.in(propertyName, subQuery));
    return this;
  }
  
  public ExpressionList<T> in(String propertyName, Collection<?> values)
  {
    add(this.expr.in(propertyName, values));
    return this;
  }
  
  public ExpressionList<T> in(String propertyName, Object... values)
  {
    add(this.expr.in(propertyName, values));
    return this;
  }
  
  public ExpressionList<T> isNotNull(String propertyName)
  {
    add(this.expr.isNotNull(propertyName));
    return this;
  }
  
  public ExpressionList<T> isNull(String propertyName)
  {
    add(this.expr.isNull(propertyName));
    return this;
  }
  
  public ExpressionList<T> istartsWith(String propertyName, String value)
  {
    add(this.expr.istartsWith(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> le(String propertyName, Object value)
  {
    add(this.expr.le(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> exampleLike(Object example)
  {
    add(this.expr.exampleLike(example));
    return this;
  }
  
  public ExpressionList<T> iexampleLike(Object example)
  {
    add(this.expr.iexampleLike(example));
    return this;
  }
  
  public ExpressionList<T> like(String propertyName, String value)
  {
    add(this.expr.like(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> lt(String propertyName, Object value)
  {
    add(this.expr.lt(propertyName, value));
    return this;
  }
  
  public ExpressionList<T> not(Expression exp)
  {
    add(this.expr.not(exp));
    return this;
  }
  
  public ExpressionList<T> or(Expression expOne, Expression expTwo)
  {
    add(this.expr.or(expOne, expTwo));
    return this;
  }
  
  public ExpressionList<T> raw(String raw, Object value)
  {
    add(this.expr.raw(raw, value));
    return this;
  }
  
  public ExpressionList<T> raw(String raw, Object[] values)
  {
    add(this.expr.raw(raw, values));
    return this;
  }
  
  public ExpressionList<T> raw(String raw)
  {
    add(this.expr.raw(raw));
    return this;
  }
  
  public ExpressionList<T> startsWith(String propertyName, String value)
  {
    add(this.expr.startsWith(propertyName, value));
    return this;
  }
}
