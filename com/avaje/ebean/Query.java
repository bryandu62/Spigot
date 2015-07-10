package com.avaje.ebean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract interface Query<T>
  extends Serializable
{
  public abstract Query<T> setUseIndex(UseIndex paramUseIndex);
  
  public abstract UseIndex getUseIndex();
  
  public abstract RawSql getRawSql();
  
  public abstract Query<T> setRawSql(RawSql paramRawSql);
  
  public abstract void cancel();
  
  public abstract Query<T> copy();
  
  public abstract ExpressionFactory getExpressionFactory();
  
  public abstract boolean isAutofetchTuned();
  
  public abstract Query<T> setAutofetch(boolean paramBoolean);
  
  public abstract Query<T> select(String paramString);
  
  public abstract Query<T> fetch(String paramString1, String paramString2);
  
  public abstract Query<T> fetch(String paramString1, String paramString2, FetchConfig paramFetchConfig);
  
  public abstract Query<T> fetch(String paramString);
  
  public abstract Query<T> fetch(String paramString, FetchConfig paramFetchConfig);
  
  public abstract List<Object> findIds();
  
  public abstract QueryIterator<T> findIterate();
  
  public abstract void findVisit(QueryResultVisitor<T> paramQueryResultVisitor);
  
  public abstract List<T> findList();
  
  public abstract Set<T> findSet();
  
  public abstract Map<?, T> findMap();
  
  public abstract <K> Map<K, T> findMap(String paramString, Class<K> paramClass);
  
  public abstract T findUnique();
  
  public abstract int findRowCount();
  
  public abstract FutureRowCount<T> findFutureRowCount();
  
  public abstract FutureIds<T> findFutureIds();
  
  public abstract FutureList<T> findFutureList();
  
  public abstract PagingList<T> findPagingList(int paramInt);
  
  public abstract Query<T> setParameter(String paramString, Object paramObject);
  
  public abstract Query<T> setParameter(int paramInt, Object paramObject);
  
  public abstract Query<T> setListener(QueryListener<T> paramQueryListener);
  
  public abstract Query<T> setId(Object paramObject);
  
  public abstract Query<T> where(String paramString);
  
  public abstract Query<T> where(Expression paramExpression);
  
  public abstract ExpressionList<T> where();
  
  public abstract ExpressionList<T> filterMany(String paramString);
  
  public abstract ExpressionList<T> having();
  
  public abstract Query<T> having(String paramString);
  
  public abstract Query<T> having(Expression paramExpression);
  
  public abstract Query<T> orderBy(String paramString);
  
  public abstract Query<T> order(String paramString);
  
  public abstract OrderBy<T> order();
  
  public abstract OrderBy<T> orderBy();
  
  public abstract Query<T> setOrder(OrderBy<T> paramOrderBy);
  
  public abstract Query<T> setOrderBy(OrderBy<T> paramOrderBy);
  
  public abstract Query<T> setDistinct(boolean paramBoolean);
  
  public abstract Query<T> setVanillaMode(boolean paramBoolean);
  
  public abstract int getFirstRow();
  
  public abstract Query<T> setFirstRow(int paramInt);
  
  public abstract int getMaxRows();
  
  public abstract Query<T> setMaxRows(int paramInt);
  
  public abstract Query<T> setBackgroundFetchAfter(int paramInt);
  
  public abstract Query<T> setMapKey(String paramString);
  
  public abstract Query<T> setUseCache(boolean paramBoolean);
  
  public abstract Query<T> setUseQueryCache(boolean paramBoolean);
  
  public abstract Query<T> setReadOnly(boolean paramBoolean);
  
  public abstract Query<T> setLoadBeanCache(boolean paramBoolean);
  
  public abstract Query<T> setTimeout(int paramInt);
  
  public abstract Query<T> setBufferFetchSizeHint(int paramInt);
  
  public abstract String getGeneratedSql();
  
  public abstract int getTotalHits();
  
  public abstract Query<T> setForUpdate(boolean paramBoolean);
  
  public static enum UseIndex
  {
    NO,  DEFAULT,  YES_IDS,  YES_OBJECTS;
    
    private UseIndex() {}
  }
}
