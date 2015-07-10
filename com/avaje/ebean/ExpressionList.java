package com.avaje.ebean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract interface ExpressionList<T>
  extends Serializable
{
  public abstract Query<T> query();
  
  public abstract Query<T> order(String paramString);
  
  public abstract OrderBy<T> order();
  
  public abstract OrderBy<T> orderBy();
  
  public abstract Query<T> orderBy(String paramString);
  
  public abstract Query<T> setOrderBy(String paramString);
  
  public abstract QueryIterator<T> findIterate();
  
  public abstract void findVisit(QueryResultVisitor<T> paramQueryResultVisitor);
  
  public abstract List<T> findList();
  
  public abstract List<Object> findIds();
  
  public abstract int findRowCount();
  
  public abstract Set<T> findSet();
  
  public abstract Map<?, T> findMap();
  
  public abstract <K> Map<K, T> findMap(String paramString, Class<K> paramClass);
  
  public abstract T findUnique();
  
  public abstract FutureRowCount<T> findFutureRowCount();
  
  public abstract FutureIds<T> findFutureIds();
  
  public abstract FutureList<T> findFutureList();
  
  public abstract PagingList<T> findPagingList(int paramInt);
  
  public abstract ExpressionList<T> filterMany(String paramString);
  
  public abstract Query<T> select(String paramString);
  
  public abstract Query<T> join(String paramString);
  
  public abstract Query<T> join(String paramString1, String paramString2);
  
  public abstract Query<T> setFirstRow(int paramInt);
  
  public abstract Query<T> setMaxRows(int paramInt);
  
  public abstract Query<T> setBackgroundFetchAfter(int paramInt);
  
  public abstract Query<T> setMapKey(String paramString);
  
  public abstract Query<T> setListener(QueryListener<T> paramQueryListener);
  
  public abstract Query<T> setUseCache(boolean paramBoolean);
  
  public abstract ExpressionList<T> having();
  
  public abstract ExpressionList<T> where();
  
  public abstract ExpressionList<T> add(Expression paramExpression);
  
  public abstract ExpressionList<T> addAll(ExpressionList<T> paramExpressionList);
  
  public abstract ExpressionList<T> eq(String paramString, Object paramObject);
  
  public abstract ExpressionList<T> ne(String paramString, Object paramObject);
  
  public abstract ExpressionList<T> ieq(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> between(String paramString, Object paramObject1, Object paramObject2);
  
  public abstract ExpressionList<T> betweenProperties(String paramString1, String paramString2, Object paramObject);
  
  public abstract ExpressionList<T> gt(String paramString, Object paramObject);
  
  public abstract ExpressionList<T> ge(String paramString, Object paramObject);
  
  public abstract ExpressionList<T> lt(String paramString, Object paramObject);
  
  public abstract ExpressionList<T> le(String paramString, Object paramObject);
  
  public abstract ExpressionList<T> isNull(String paramString);
  
  public abstract ExpressionList<T> isNotNull(String paramString);
  
  public abstract ExpressionList<T> exampleLike(Object paramObject);
  
  public abstract ExpressionList<T> iexampleLike(Object paramObject);
  
  public abstract ExpressionList<T> like(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> ilike(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> startsWith(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> istartsWith(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> endsWith(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> iendsWith(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> contains(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> icontains(String paramString1, String paramString2);
  
  public abstract ExpressionList<T> in(String paramString, Query<?> paramQuery);
  
  public abstract ExpressionList<T> in(String paramString, Object... paramVarArgs);
  
  public abstract ExpressionList<T> in(String paramString, Collection<?> paramCollection);
  
  public abstract ExpressionList<T> idIn(List<?> paramList);
  
  public abstract ExpressionList<T> idEq(Object paramObject);
  
  public abstract ExpressionList<T> allEq(Map<String, Object> paramMap);
  
  public abstract ExpressionList<T> raw(String paramString, Object paramObject);
  
  public abstract ExpressionList<T> raw(String paramString, Object[] paramArrayOfObject);
  
  public abstract ExpressionList<T> raw(String paramString);
  
  public abstract ExpressionList<T> and(Expression paramExpression1, Expression paramExpression2);
  
  public abstract ExpressionList<T> or(Expression paramExpression1, Expression paramExpression2);
  
  public abstract ExpressionList<T> not(Expression paramExpression);
  
  public abstract Junction<T> conjunction();
  
  public abstract Junction<T> disjunction();
  
  public abstract ExpressionList<T> endJunction();
}
