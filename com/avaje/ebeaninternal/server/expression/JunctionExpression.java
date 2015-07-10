package com.avaje.ebeaninternal.server.expression;

import com.avaje.ebean.Expression;
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
import com.avaje.ebeaninternal.api.SpiExpressionRequest;
import com.avaje.ebeaninternal.server.deploy.BeanDescriptor;
import com.avaje.ebeaninternal.util.DefaultExpressionList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class JunctionExpression<T>
  implements Junction<T>, SpiExpression, ExpressionList<T>
{
  private static final long serialVersionUID = -7422204102750462676L;
  private static final String OR = " or ";
  private static final String AND = " and ";
  private final DefaultExpressionList<T> exprList;
  private final String joinType;
  
  static class Conjunction<T>
    extends JunctionExpression<T>
  {
    private static final long serialVersionUID = -645619859900030678L;
    
    Conjunction(Query<T> query, ExpressionList<T> parent)
    {
      super(query, parent);
    }
  }
  
  static class Disjunction<T>
    extends JunctionExpression<T>
  {
    private static final long serialVersionUID = -8464470066692221413L;
    
    Disjunction(Query<T> query, ExpressionList<T> parent)
    {
      super(query, parent);
    }
  }
  
  JunctionExpression(String joinType, Query<T> query, ExpressionList<T> parent)
  {
    this.joinType = joinType;
    this.exprList = new DefaultExpressionList(query, parent);
  }
  
  public void containsMany(BeanDescriptor<?> desc, ManyWhereJoins manyWhereJoin)
  {
    List<SpiExpression> list = this.exprList.internalList();
    for (int i = 0; i < list.size(); i++) {
      ((SpiExpression)list.get(i)).containsMany(desc, manyWhereJoin);
    }
  }
  
  public Junction<T> add(Expression item)
  {
    SpiExpression i = (SpiExpression)item;
    this.exprList.add(i);
    return this;
  }
  
  public Junction<T> addAll(ExpressionList<T> addList)
  {
    this.exprList.addAll(addList);
    return this;
  }
  
  public void addBindValues(SpiExpressionRequest request)
  {
    List<SpiExpression> list = this.exprList.internalList();
    for (int i = 0; i < list.size(); i++)
    {
      SpiExpression item = (SpiExpression)list.get(i);
      item.addBindValues(request);
    }
  }
  
  public void addSql(SpiExpressionRequest request)
  {
    List<SpiExpression> list = this.exprList.internalList();
    if (!list.isEmpty())
    {
      request.append("(");
      for (int i = 0; i < list.size(); i++)
      {
        SpiExpression item = (SpiExpression)list.get(i);
        if (i > 0) {
          request.append(this.joinType);
        }
        item.addSql(request);
      }
      request.append(") ");
    }
  }
  
  public int queryAutoFetchHash()
  {
    int hc = JunctionExpression.class.getName().hashCode();
    hc = hc * 31 + this.joinType.hashCode();
    
    List<SpiExpression> list = this.exprList.internalList();
    for (int i = 0; i < list.size(); i++) {
      hc = hc * 31 + ((SpiExpression)list.get(i)).queryAutoFetchHash();
    }
    return hc;
  }
  
  public int queryPlanHash(BeanQueryRequest<?> request)
  {
    int hc = JunctionExpression.class.getName().hashCode();
    hc = hc * 31 + this.joinType.hashCode();
    
    List<SpiExpression> list = this.exprList.internalList();
    for (int i = 0; i < list.size(); i++) {
      hc = hc * 31 + ((SpiExpression)list.get(i)).queryPlanHash(request);
    }
    return hc;
  }
  
  public int queryBindHash()
  {
    int hc = JunctionExpression.class.getName().hashCode();
    
    List<SpiExpression> list = this.exprList.internalList();
    for (int i = 0; i < list.size(); i++) {
      hc = hc * 31 + ((SpiExpression)list.get(i)).queryBindHash();
    }
    return hc;
  }
  
  public ExpressionList<T> endJunction()
  {
    return this.exprList.endJunction();
  }
  
  public ExpressionList<T> allEq(Map<String, Object> propertyMap)
  {
    return this.exprList.allEq(propertyMap);
  }
  
  public ExpressionList<T> and(Expression expOne, Expression expTwo)
  {
    return this.exprList.and(expOne, expTwo);
  }
  
  public ExpressionList<T> between(String propertyName, Object value1, Object value2)
  {
    return this.exprList.between(propertyName, value1, value2);
  }
  
  public ExpressionList<T> betweenProperties(String lowProperty, String highProperty, Object value)
  {
    return this.exprList.betweenProperties(lowProperty, highProperty, value);
  }
  
  public Junction<T> conjunction()
  {
    return this.exprList.conjunction();
  }
  
  public ExpressionList<T> contains(String propertyName, String value)
  {
    return this.exprList.contains(propertyName, value);
  }
  
  public Junction<T> disjunction()
  {
    return this.exprList.disjunction();
  }
  
  public ExpressionList<T> endsWith(String propertyName, String value)
  {
    return this.exprList.endsWith(propertyName, value);
  }
  
  public ExpressionList<T> eq(String propertyName, Object value)
  {
    return this.exprList.eq(propertyName, value);
  }
  
  public ExpressionList<T> exampleLike(Object example)
  {
    return this.exprList.exampleLike(example);
  }
  
  public ExpressionList<T> filterMany(String prop)
  {
    throw new RuntimeException("filterMany not allowed on Junction expression list");
  }
  
  public FutureIds<T> findFutureIds()
  {
    return this.exprList.findFutureIds();
  }
  
  public FutureList<T> findFutureList()
  {
    return this.exprList.findFutureList();
  }
  
  public FutureRowCount<T> findFutureRowCount()
  {
    return this.exprList.findFutureRowCount();
  }
  
  public List<Object> findIds()
  {
    return this.exprList.findIds();
  }
  
  public void findVisit(QueryResultVisitor<T> visitor)
  {
    this.exprList.findVisit(visitor);
  }
  
  public QueryIterator<T> findIterate()
  {
    return this.exprList.findIterate();
  }
  
  public List<T> findList()
  {
    return this.exprList.findList();
  }
  
  public Map<?, T> findMap()
  {
    return this.exprList.findMap();
  }
  
  public <K> Map<K, T> findMap(String keyProperty, Class<K> keyType)
  {
    return this.exprList.findMap(keyProperty, keyType);
  }
  
  public PagingList<T> findPagingList(int pageSize)
  {
    return this.exprList.findPagingList(pageSize);
  }
  
  public int findRowCount()
  {
    return this.exprList.findRowCount();
  }
  
  public Set<T> findSet()
  {
    return this.exprList.findSet();
  }
  
  public T findUnique()
  {
    return (T)this.exprList.findUnique();
  }
  
  public ExpressionList<T> ge(String propertyName, Object value)
  {
    return this.exprList.ge(propertyName, value);
  }
  
  public ExpressionList<T> gt(String propertyName, Object value)
  {
    return this.exprList.gt(propertyName, value);
  }
  
  public ExpressionList<T> having()
  {
    throw new RuntimeException("having() not allowed on Junction expression list");
  }
  
  public ExpressionList<T> icontains(String propertyName, String value)
  {
    return this.exprList.icontains(propertyName, value);
  }
  
  public ExpressionList<T> idEq(Object value)
  {
    return this.exprList.idEq(value);
  }
  
  public ExpressionList<T> idIn(List<?> idValues)
  {
    return this.exprList.idIn(idValues);
  }
  
  public ExpressionList<T> iendsWith(String propertyName, String value)
  {
    return this.exprList.iendsWith(propertyName, value);
  }
  
  public ExpressionList<T> ieq(String propertyName, String value)
  {
    return this.exprList.ieq(propertyName, value);
  }
  
  public ExpressionList<T> iexampleLike(Object example)
  {
    return this.exprList.iexampleLike(example);
  }
  
  public ExpressionList<T> ilike(String propertyName, String value)
  {
    return this.exprList.ilike(propertyName, value);
  }
  
  public ExpressionList<T> in(String propertyName, Collection<?> values)
  {
    return this.exprList.in(propertyName, values);
  }
  
  public ExpressionList<T> in(String propertyName, Object... values)
  {
    return this.exprList.in(propertyName, values);
  }
  
  public ExpressionList<T> in(String propertyName, Query<?> subQuery)
  {
    return this.exprList.in(propertyName, subQuery);
  }
  
  public ExpressionList<T> isNotNull(String propertyName)
  {
    return this.exprList.isNotNull(propertyName);
  }
  
  public ExpressionList<T> isNull(String propertyName)
  {
    return this.exprList.isNull(propertyName);
  }
  
  public ExpressionList<T> istartsWith(String propertyName, String value)
  {
    return this.exprList.istartsWith(propertyName, value);
  }
  
  public Query<T> join(String assocProperty, String assocProperties)
  {
    return this.exprList.join(assocProperty, assocProperties);
  }
  
  public Query<T> join(String assocProperties)
  {
    return this.exprList.join(assocProperties);
  }
  
  public ExpressionList<T> le(String propertyName, Object value)
  {
    return this.exprList.le(propertyName, value);
  }
  
  public ExpressionList<T> like(String propertyName, String value)
  {
    return this.exprList.like(propertyName, value);
  }
  
  public ExpressionList<T> lt(String propertyName, Object value)
  {
    return this.exprList.lt(propertyName, value);
  }
  
  public ExpressionList<T> ne(String propertyName, Object value)
  {
    return this.exprList.ne(propertyName, value);
  }
  
  public ExpressionList<T> not(Expression exp)
  {
    return this.exprList.not(exp);
  }
  
  public ExpressionList<T> or(Expression expOne, Expression expTwo)
  {
    return this.exprList.or(expOne, expTwo);
  }
  
  public OrderBy<T> order()
  {
    return this.exprList.order();
  }
  
  public Query<T> order(String orderByClause)
  {
    return this.exprList.order(orderByClause);
  }
  
  public OrderBy<T> orderBy()
  {
    return this.exprList.orderBy();
  }
  
  public Query<T> orderBy(String orderBy)
  {
    return this.exprList.orderBy(orderBy);
  }
  
  public Query<T> query()
  {
    return this.exprList.query();
  }
  
  public ExpressionList<T> raw(String raw, Object value)
  {
    return this.exprList.raw(raw, value);
  }
  
  public ExpressionList<T> raw(String raw, Object[] values)
  {
    return this.exprList.raw(raw, values);
  }
  
  public ExpressionList<T> raw(String raw)
  {
    return this.exprList.raw(raw);
  }
  
  public Query<T> select(String properties)
  {
    return this.exprList.select(properties);
  }
  
  public Query<T> setBackgroundFetchAfter(int backgroundFetchAfter)
  {
    return this.exprList.setBackgroundFetchAfter(backgroundFetchAfter);
  }
  
  public Query<T> setFirstRow(int firstRow)
  {
    return this.exprList.setFirstRow(firstRow);
  }
  
  public Query<T> setListener(QueryListener<T> queryListener)
  {
    return this.exprList.setListener(queryListener);
  }
  
  public Query<T> setMapKey(String mapKey)
  {
    return this.exprList.setMapKey(mapKey);
  }
  
  public Query<T> setMaxRows(int maxRows)
  {
    return this.exprList.setMaxRows(maxRows);
  }
  
  public Query<T> setOrderBy(String orderBy)
  {
    return this.exprList.setOrderBy(orderBy);
  }
  
  public Query<T> setUseCache(boolean useCache)
  {
    return this.exprList.setUseCache(useCache);
  }
  
  public ExpressionList<T> startsWith(String propertyName, String value)
  {
    return this.exprList.startsWith(propertyName, value);
  }
  
  public ExpressionList<T> where()
  {
    return this.exprList.where();
  }
}
