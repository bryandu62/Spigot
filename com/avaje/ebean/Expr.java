package com.avaje.ebean;

import java.util.Collection;
import java.util.Map;

public class Expr
{
  public static Expression eq(String propertyName, Object value)
  {
    return Ebean.getExpressionFactory().eq(propertyName, value);
  }
  
  public static Expression ne(String propertyName, Object value)
  {
    return Ebean.getExpressionFactory().ne(propertyName, value);
  }
  
  public static Expression ieq(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().ieq(propertyName, value);
  }
  
  public static Expression between(String propertyName, Object value1, Object value2)
  {
    return Ebean.getExpressionFactory().between(propertyName, value1, value2);
  }
  
  public static Expression gt(String propertyName, Object value)
  {
    return Ebean.getExpressionFactory().gt(propertyName, value);
  }
  
  public static Expression ge(String propertyName, Object value)
  {
    return Ebean.getExpressionFactory().ge(propertyName, value);
  }
  
  public static Expression lt(String propertyName, Object value)
  {
    return Ebean.getExpressionFactory().lt(propertyName, value);
  }
  
  public static Expression le(String propertyName, Object value)
  {
    return Ebean.getExpressionFactory().le(propertyName, value);
  }
  
  public static Expression isNull(String propertyName)
  {
    return Ebean.getExpressionFactory().isNull(propertyName);
  }
  
  public static Expression isNotNull(String propertyName)
  {
    return Ebean.getExpressionFactory().isNotNull(propertyName);
  }
  
  public static ExampleExpression iexampleLike(Object example)
  {
    return Ebean.getExpressionFactory().iexampleLike(example);
  }
  
  public static ExampleExpression exampleLike(Object example)
  {
    return Ebean.getExpressionFactory().exampleLike(example);
  }
  
  public static ExampleExpression exampleLike(Object example, boolean caseInsensitive, LikeType likeType)
  {
    return Ebean.getExpressionFactory().exampleLike(example, caseInsensitive, likeType);
  }
  
  public static Expression like(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().like(propertyName, value);
  }
  
  public static Expression ilike(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().ilike(propertyName, value);
  }
  
  public static Expression startsWith(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().startsWith(propertyName, value);
  }
  
  public static Expression istartsWith(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().istartsWith(propertyName, value);
  }
  
  public static Expression endsWith(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().endsWith(propertyName, value);
  }
  
  public static Expression iendsWith(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().iendsWith(propertyName, value);
  }
  
  public static Expression contains(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().contains(propertyName, value);
  }
  
  public static Expression icontains(String propertyName, String value)
  {
    return Ebean.getExpressionFactory().icontains(propertyName, value);
  }
  
  public static Expression in(String propertyName, Object[] values)
  {
    return Ebean.getExpressionFactory().in(propertyName, values);
  }
  
  public static Expression in(String propertyName, Query<?> subQuery)
  {
    return Ebean.getExpressionFactory().in(propertyName, subQuery);
  }
  
  public static Expression in(String propertyName, Collection<?> values)
  {
    return Ebean.getExpressionFactory().in(propertyName, values);
  }
  
  public static Expression idEq(Object value)
  {
    return Ebean.getExpressionFactory().idEq(value);
  }
  
  public static Expression allEq(Map<String, Object> propertyMap)
  {
    return Ebean.getExpressionFactory().allEq(propertyMap);
  }
  
  public static Expression raw(String raw, Object value)
  {
    return Ebean.getExpressionFactory().raw(raw, value);
  }
  
  public static Expression raw(String raw, Object[] values)
  {
    return Ebean.getExpressionFactory().raw(raw, values);
  }
  
  public static Expression raw(String raw)
  {
    return Ebean.getExpressionFactory().raw(raw);
  }
  
  public static Expression and(Expression expOne, Expression expTwo)
  {
    return Ebean.getExpressionFactory().and(expOne, expTwo);
  }
  
  public static Expression or(Expression expOne, Expression expTwo)
  {
    return Ebean.getExpressionFactory().or(expOne, expTwo);
  }
  
  public static Expression not(Expression exp)
  {
    return Ebean.getExpressionFactory().not(exp);
  }
  
  public static <T> Junction<T> conjunction(Query<T> query)
  {
    return Ebean.getExpressionFactory().conjunction(query);
  }
  
  public static <T> Junction<T> disjunction(Query<T> query)
  {
    return Ebean.getExpressionFactory().disjunction(query);
  }
}
