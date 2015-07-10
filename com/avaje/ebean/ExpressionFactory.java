package com.avaje.ebean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract interface ExpressionFactory
{
  public abstract String getLang();
  
  public abstract Expression eq(String paramString, Object paramObject);
  
  public abstract Expression ne(String paramString, Object paramObject);
  
  public abstract Expression ieq(String paramString1, String paramString2);
  
  public abstract Expression between(String paramString, Object paramObject1, Object paramObject2);
  
  public abstract Expression betweenProperties(String paramString1, String paramString2, Object paramObject);
  
  public abstract Expression gt(String paramString, Object paramObject);
  
  public abstract Expression ge(String paramString, Object paramObject);
  
  public abstract Expression lt(String paramString, Object paramObject);
  
  public abstract Expression le(String paramString, Object paramObject);
  
  public abstract Expression isNull(String paramString);
  
  public abstract Expression isNotNull(String paramString);
  
  public abstract ExampleExpression iexampleLike(Object paramObject);
  
  public abstract ExampleExpression exampleLike(Object paramObject);
  
  public abstract ExampleExpression exampleLike(Object paramObject, boolean paramBoolean, LikeType paramLikeType);
  
  public abstract Expression like(String paramString1, String paramString2);
  
  public abstract Expression ilike(String paramString1, String paramString2);
  
  public abstract Expression startsWith(String paramString1, String paramString2);
  
  public abstract Expression istartsWith(String paramString1, String paramString2);
  
  public abstract Expression endsWith(String paramString1, String paramString2);
  
  public abstract Expression iendsWith(String paramString1, String paramString2);
  
  public abstract Expression contains(String paramString1, String paramString2);
  
  public abstract Expression icontains(String paramString1, String paramString2);
  
  public abstract Expression in(String paramString, Object[] paramArrayOfObject);
  
  public abstract Expression in(String paramString, Query<?> paramQuery);
  
  public abstract Expression in(String paramString, Collection<?> paramCollection);
  
  public abstract Expression idEq(Object paramObject);
  
  public abstract Expression idIn(List<?> paramList);
  
  public abstract Expression allEq(Map<String, Object> paramMap);
  
  public abstract Expression raw(String paramString, Object paramObject);
  
  public abstract Expression raw(String paramString, Object[] paramArrayOfObject);
  
  public abstract Expression raw(String paramString);
  
  public abstract Expression and(Expression paramExpression1, Expression paramExpression2);
  
  public abstract Expression or(Expression paramExpression1, Expression paramExpression2);
  
  public abstract Expression not(Expression paramExpression);
  
  public abstract <T> Junction<T> conjunction(Query<T> paramQuery);
  
  public abstract <T> Junction<T> disjunction(Query<T> paramQuery);
  
  public abstract <T> Junction<T> conjunction(Query<T> paramQuery, ExpressionList<T> paramExpressionList);
  
  public abstract <T> Junction<T> disjunction(Query<T> paramQuery, ExpressionList<T> paramExpressionList);
}
